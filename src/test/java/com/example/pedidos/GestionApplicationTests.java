package com.example.pedidos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GestionApplicationTests {

    @Test
    void contextLoads() {}

    @Test
    void testMain() {
        assertDoesNotThrow(() -> {
            GestionApplication.main(new String[] {});
        });
    }
}
