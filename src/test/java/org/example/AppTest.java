package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


class AppTest {

    @Test
    @DisplayName("Simple app test")
    void testApp() {
        assertTrue(true, "This test should always pass");
    }

    @Test
    @DisplayName("Test main method runs without exceptions")
    void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            App.main(new String[]{});
        }, "Main method should run without throwing exceptions");
    }
}
