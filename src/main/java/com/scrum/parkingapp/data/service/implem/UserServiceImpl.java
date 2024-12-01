package com.scrum.parkingapp.data.service.implem;


import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.UserService;
import com.scrum.parkingapp.dto.EmailUserDto;
import com.scrum.parkingapp.dto.PasswordUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.UserDto;
import com.scrum.parkingapp.exception.InvalidCredentialException;
import com.scrum.parkingapp.exception.UserAlreadyExistsException;
import com.scrum.parkingapp.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersDao userDao;


    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getById(UUID id) {
        try {
            User user = userDao.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return modelMapper.map(user, UserDto.class);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by ID: "+ id +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public UserDetailsDto getUserDetailsById(UUID id) {
        try{
            User user = userDao.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return modelMapper.map(user, UserDetailsDto.class);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user by ID: "+ id +", "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public UserDto save(User user) {
        try {
            User savedUser = userDao.save(user);

            return modelMapper.map(savedUser, UserDto.class);

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while saving user: {}", user, e);
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while saving user: {}", user, e);
            throw new RuntimeException("Unexpected error occurred while saving user");
        }
    }

    public List<UserDetailsDto> getAllDto() {
        try{
            List<User> users = userDao.findAll();

            return users.stream().map(user1 -> modelMapper.map(user1, UserDetailsDto.class)).toList();
        }catch(Exception e){
            log.error("Unexpected error while fetching all users: "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    public List<User> getAll() {
        try{
            return userDao.findAll();
        }catch(Exception e){
            log.error("Unexpected error while fetching all users: "+ e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }


    @Override
    public User getUserById(UUID id) {
        try{
            /*return userDao.findByIdWithAddresses(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));*/

            return userDao.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }catch(Exception e){
            log.error("Unexpected error while fetching user with id: "+ id+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    public UserDto updatePassword(UUID userId, PasswordUserDto userDto) {
        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (!passwordEncoder.matches(userDto.getOldPassword(), user.getCredential().getPassword())) {
                throw new InvalidCredentialException("Old password is incorrect");
            }

            user.getCredential().setPassword(passwordEncoder.encode(userDto.getNewPassword()));
            userDao.save(user);
            return modelMapper.map(user, UserDto.class);
        }catch(Exception e){
            log.error("Unexpected error while updating user password with id: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public UserDto updateEmail(UUID userId, EmailUserDto userDto) {
        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            userDao.findByCredentialEmail(userDto.getNewEmail()).ifPresent(u -> {
                throw new UserAlreadyExistsException("User with this email already exists");
            });

            user.getCredential().setEmail(userDto.getNewEmail());
            userDao.save(user);
            return modelMapper.map(user, UserDto.class);
        }catch(Exception e){
            log.error("Unexpected error while updating user email with id: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }


    @Transactional
    @Override
    public boolean delete(UUID userId) {
        try {
            User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

            userDao.delete(user);

            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete user, related records exist", e);
        }
        catch(Exception e){
            log.error("Unexpected error while deleting user with id: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }
}