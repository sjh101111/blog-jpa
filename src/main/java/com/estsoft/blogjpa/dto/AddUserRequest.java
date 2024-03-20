package com.estsoft.blogjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;
}
