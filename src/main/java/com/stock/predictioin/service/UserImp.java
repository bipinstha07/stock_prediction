package com.stock.predictioin.service;

import com.stock.predictioin.dto.UserDto;
import com.stock.predictioin.entity.User;
import com.stock.predictioin.exceptionHandler.ResourceNotFoundException;
import com.stock.predictioin.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserImp {

    private UserRepo userRepo;
    private ModelMapper modelMapper;

    public UserImp(ModelMapper modelMapper, UserRepo userRepo) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    public UserDto createUser(UserDto userDto){
        User user = modelMapper.map(userDto,User.class);
        user.setId(UUID.randomUUID().toString());
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }

    public UserDto getUser(String email){
        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new ResourceNotFoundException("No User Found");
        }
        return modelMapper.map(user,UserDto.class);
    }

}
