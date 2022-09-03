package com.iitr.gl.signupservice.service;

import com.iitr.gl.signupservice.data.UserEntity;
import com.iitr.gl.signupservice.data.UserRepository;
import com.iitr.gl.signupservice.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SignupServiceImpl implements SignupService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDetails) {

        if(userRepository.findByEmail(userDetails.getEmail()) != null)
        {
            userDetails.setMessage("Email-id is already exist, please use different email-id");
            return userDetails;
        }

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        userRepository.save(userEntity);
        UserDto dto = modelMapper.map(userEntity, UserDto.class);
        dto.setMessage("User Successfully created");
        return dto;
    }
}
