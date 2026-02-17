package com.ems.service;

import com.ems.dto.response.UserResponse;
import com.ems.exception.ResourceNotFoundException;
import com.ems.model.User;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse getUserById(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with user id:"+id));

        return convertToResponse(user);
    }

    public UserResponse getUserByUsername(String username){
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found with username:"+username));

        return convertToResponse(user);
    }

    public UserResponse getUserByEmail(String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found with email:"+email));

        return convertToResponse(user);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public UserResponse updateUserProfile(Long id,String firstName,String lastName,String phoneNumber){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with user id:"+id));

        if (firstName!=null){
            user.setFirstName(firstName);
        }
        if (lastName!=null){
            user.setLastName(lastName);
        }
        if (phoneNumber!=null){
            user.setPhoneNumber(phoneNumber);
        }

        User updateProfile=userRepository.save(user);
        return convertToResponse(updateProfile);
    }


    @Transactional
    public void changePassword(Long id,String oldPassword,String newPassword){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with user id:"+id));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Transactional
    public void deActiveUser(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with id:"+id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activeUsers(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with id:"+id));

        user.setIsActive(true);
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with id:"+id));
        userRepository.delete(user);
    }




    private UserResponse convertToResponse(User user){
        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRoles(user.getRoles().stream()
                .map(role->role.getName().name())
                .collect(Collectors.toSet())
        );

        userResponse.setIsActive(user.getIsActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        return userResponse;
    }

}
