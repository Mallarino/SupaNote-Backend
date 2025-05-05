package com.personal.SupaNote.DTOs;

import com.personal.SupaNote.Models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserDto user;
    private String message;
}