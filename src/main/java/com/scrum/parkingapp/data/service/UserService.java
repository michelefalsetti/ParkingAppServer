package com.scrum.parkingapp.data.service;



import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.dto.EmailUserDto;
import com.scrum.parkingapp.dto.PasswordUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto getById(UUID id);

    UserDto save(User user);

    List<UserDetailsDto> getAllDto();

    UserDetailsDto getUserDetailsById(UUID id);

    User getUserById(UUID id);

    UserDto updatePassword(UUID userId, PasswordUserDto userDto);

    UserDto updateEmail(UUID userId, EmailUserDto userDto);


    boolean delete(UUID userId);

    List<User> getAll();
}
