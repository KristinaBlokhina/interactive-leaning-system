package edu.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningServiceTest {

    @Mock
    LearningService learningService;

    @Test
    void testWelcomeMessage() {
        when(learningService.welcome("Kristina"))
                .thenReturn("Welcome to Interactive Learning System, Kristina");

        String result = learningService.welcome("Kristina");

        assertEquals("Welcome to Interactive Learning System, Kristina", result);
        verify(learningService).welcome("Kristina");
    }

    @Test
    void testCalculateScoreWithCaptor() {
        when(learningService.calculateScore(anyInt(), anyInt()))
                .thenReturn(80);

        int result = learningService.calculateScore(8, 10);
        assertEquals(80, result);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(learningService).calculateScore(captor.capture(), captor.capture());

        assertEquals(8, captor.getAllValues().get(0));
        assertEquals(10, captor.getAllValues().get(1));
    }
}
