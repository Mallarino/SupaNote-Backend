package com.personal.SupaNote.Services;

import com.personal.SupaNote.Models.UserModel;
import com.personal.SupaNote.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServices {

    @Autowired
    IUserRepository userRepository;

    public Optional<UserModel> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserModel saveUser(UserModel user){
        return userRepository.save(user);
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

}
