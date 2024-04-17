package com.estsoft.blogjpa.service;

import com.estsoft.blogjpa.dto.AddUserRequest;
import com.estsoft.blogjpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.estsoft.blogjpa.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest2 {
    @Mock
    UserRepository userRepository;

    @Spy
    BCryptPasswordEncoder encoder; //가짜객체, encoder.encode();

    @InjectMocks
    UserService userService;

    @Test
    void testSave() {
        // given
        AddUserRequest request = new AddUserRequest("mock_email@1", "pw");
        String encodedPassword = encoder.encode(request.getPassword());
        doReturn(User.builder().password(encodedPassword)
                .email(request.getEmail()).build()).when(userRepository).save(any(User.class));

        // when
        User returnUser= userService.save(request);

        // then
        assertThat(returnUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(returnUser.getPassword()).isEqualTo(encodedPassword);
//        assertThat(returnUser.getPassword()).isEqualTo(request.getPassword()); -> 오류남
        assertThat(encoder.matches(returnUser.getPassword(),request.getPassword(

        )));

        verify(userRepository, times(1)).save(any(User.class));
        verify(encoder, times(2)).encode(any(String.class));
    }

}