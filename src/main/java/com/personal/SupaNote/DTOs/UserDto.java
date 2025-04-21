package com.personal.SupaNote.DTOs;

import com.personal.SupaNote.Models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    public UserDto(UserModel user) {
        this.username = user.getName();
        this.email = user.getEmail();
    }
}
