package com.community.domain.user.repository;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.community.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    private static User newUser(String email, String password, String nickname, String imageUrl) {
        return new User(email, password, nickname, imageUrl);
    }

    private static User getDefaultUser() {
        return newUser("test1@email.com", "password","test1","imageURL1");
    }

    @Test
    void save_NewUser_AssignsIdAndPersists() {
        //given
        User u = newUser("test1@email.com", "password","test1","imageURL1");
        assertNull(u.getId());

        //when
        Long id = repository.save(u);
        Optional<User> found = repository.findById(id);

        //then
        assertNotNull(id);
        assertTrue(found.isPresent());
        assertEquals(u, found.get());
    }

    @Test
    void save_ExistingUser_UpdatesStoredValue() {
        //given
        User u = getDefaultUser();
        Long id = repository.save(u);

        //when
        u.updateNickname("newNickName");
        Long id2 = repository.save(u);

        //then
        assertEquals(id, id2);
        User updatedUser = repository.findById(id).orElseThrow();
        assertEquals("newNickName", updatedUser.getNickname());
    }

    @Test
    void findById_NotFound_ReturnsEmpty() {
        assertTrue(repository.findById(999L).isEmpty());
    }

    @Test
    void findByEmail_FindsFirstMatch() {
        //given
        repository.save(newUser("test1@email.com", "password","test1","imageURL1"));
        repository.save(newUser("test2@email.com", "password","test2","imageURL2"));

        //then
        Optional<User> found = repository.findByEmail("test2@email.com");

        //then
        assertTrue(found.isPresent());
        assertEquals("test2@email.com", found.get().getEmail());
    }

    @Test
    void findByNickName_FindsFirstMatch() {
        //given
        repository.save(newUser("test1@email.com", "password","test1","imageURL1"));
        repository.save(newUser("test2@email.com", "password","test2","imageURL2"));

        //then
        Optional<User> found = repository.findByNickName("test2");

        //then
        assertTrue(found.isPresent());
        assertEquals("test2", found.get().getNickname());
    }

    @Test
    void delete_RemovesUser() {
        //given
        User u = getDefaultUser();
        Long id = repository.save(u);

        //when
        repository.delete(u);

        //then
        assertTrue(repository.findById(id).isEmpty());
        assertTrue(repository.findByEmail("a@test.com").isEmpty());
        assertTrue(repository.findByNickName("alice").isEmpty());
    }

    @Test
    void threadSafety_SmokeTest() throws InterruptedException {
        //given
        int n = 1000;
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            int finalI = i;
            threads[i] = new Thread(() ->
                    repository.save(newUser("u"+finalI+"@t.com", "n"+finalI , "nickname" + finalI, "imageUrl" + finalI))
            );
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        //when
        int hits = 0;
        for (int i = 0; i < n; i++) {
            if (repository.findByEmail("u"+i+"@t.com").isPresent()) hits++;
        }

        //then
        assertEquals(n, hits);
    }
}
