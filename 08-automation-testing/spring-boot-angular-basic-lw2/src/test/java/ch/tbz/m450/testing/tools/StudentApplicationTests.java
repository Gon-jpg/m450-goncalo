package ch.tbz.m450.testing.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void getReturns200AndContainsSeedStudents() {
        ResponseEntity<Map[]> response = rest.getForEntity("/students", Map[].class);

        assertEquals(200, response.getStatusCode().value());
        Map[] students = response.getBody();
        assertNotNull(students);
        assertTrue(students.length >= 5);
        for (String name : new String[]{"Jonas", "Patrick", "Yves", "Peter", "Ann"}) {
            assertTrue(containsName(students, name), "missing seed student " + name);
        }
    }

    @Test
    void postThenGetContainsNewStudent() {
        String name = "Test-" + UUID.randomUUID();
        Map<String, String> body = Map.of("name", name, "email", name + "@example.com");

        ResponseEntity<String> post = rest.postForEntity("/students", body, String.class);

        assertEquals(200, post.getStatusCode().value());
        Map[] students = rest.getForEntity("/students", Map[].class).getBody();
        assertNotNull(students);
        assertTrue(containsName(students, name));
    }

    @Test
    void postWithEmptyBodyIsNotRejected() {
        ResponseEntity<String> response = rest.postForEntity("/students", Map.of(), String.class);

        System.out.println("POST /students with {} -> " + response.getStatusCode());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    private boolean containsName(Map[] students, String name) {
        return Arrays.stream(students).anyMatch(s -> name.equals(s.get("name")));
    }
}
