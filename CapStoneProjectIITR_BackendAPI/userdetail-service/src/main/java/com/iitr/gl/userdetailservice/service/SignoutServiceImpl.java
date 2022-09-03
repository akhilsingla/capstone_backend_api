package com.iitr.gl.userdetailservice.service;

import com.iitr.gl.userdetailservice.data.ExpiredToken;
import com.iitr.gl.userdetailservice.data.ExpiredTokenMySqlRepository;
import com.iitr.gl.userdetailservice.shared.ExpiredTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SignoutServiceImpl {

    @Autowired
    ExpiredTokenMySqlRepository expiredTokenMySqlRepository;

    public HttpStatus signOut(ExpiredTokenDto expiredTokenDto)
    {
        ExpiredToken expiredToken = new ExpiredToken();
        expiredToken.setToken(expiredTokenDto.getToken().replace("Bearer", ""));
        expiredToken.setUserId(expiredTokenDto.getUserId());

        expiredTokenMySqlRepository.save(expiredToken);
        return HttpStatus.OK;
    }

}
