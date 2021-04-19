package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // This annotation automatically opens and closes Mocks
class StudentServiceTest {

    // Use @Mock because we know that all repository method work right
    @Mock private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void getAllStudents() {

        // when
        underTest.getAllStudents();

        //then
        verify(studentRepository).findAll();
    }

    @Test
    void addStudent() {

        // given
        Student student = new Student("Jamila", "jamila@gmail.com", Gender.FEMALE); // Student that underTest receives

        // when
        underTest.addStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture()); // Captures the student we receive from StudentService

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void throwExceptionWhenEmailIsTakenInAddStudent() {

        // given
        Student student = new Student("Jamila", "jamila@gmail.com", Gender.FEMALE); // Student that underTest receives

        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);

        // We can also use anyString instead of student.getEmail()
//        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    @Disabled
    void deleteStudent() {
    }
}