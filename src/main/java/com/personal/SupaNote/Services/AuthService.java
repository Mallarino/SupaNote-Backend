package com.personal.SupaNote.Services;

import com.personal.SupaNote.DTOs.AuthResponse;
import com.personal.SupaNote.DTOs.LoginRequest;
import com.personal.SupaNote.DTOs.RegisterRequest;
import com.personal.SupaNote.DTOs.UserDto;
import com.personal.SupaNote.Models.UserModel;
import com.personal.SupaNote.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public AuthResponse register(RegisterRequest request) {
        // Verifica si ya existe un usuario con ese email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(null,null ,"Email already registered");
        }

        UserModel user = UserModel.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .notes(new ArrayList<>())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        UserDto userDto = new UserDto(user);
        return new AuthResponse(token, userDto, "User registered");
    }

    public AuthResponse login(LoginRequest loginRequest) {
        UserModel user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

        if (user == null) {
            return new AuthResponse(null, null, "Email not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new AuthResponse(null, null, "Incorrect password");
        }

        String token = jwtService.generateToken(user);
        UserDto userDto = new UserDto(user);
        return new AuthResponse(token, userDto, "Successful login");
    }


}
