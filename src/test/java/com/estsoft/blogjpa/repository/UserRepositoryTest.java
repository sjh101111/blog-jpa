package com.estsoft.blogjpa.repository;

import com.estsoft.blogjpa.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void testFindByEmail() {
         User user =  User.builder().password("pw")
                .email("a@a").build();

        userRepository.save(user);

        //when
        User returnUser = userRepository.findByEmail(user.getEmail()).orElseThrow();

        //then
        assertEquals(user.getEmail(), returnUser.getEmail());
        assertEquals(user.getPassword(), returnUser.getPassword());
    }

    @DisplayName("사용자 전체 조회")
    @Test
    void testFindAll() {
        User user =  User.builder().password("pw").email("a@a").build();
        userRepository.save(user);

        List<User> all = userRepository.findAll();

        assertEquals(1, all.size());
    }
}