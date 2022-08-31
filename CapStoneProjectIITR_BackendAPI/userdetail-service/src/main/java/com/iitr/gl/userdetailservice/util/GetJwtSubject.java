package com.iitr.gl.userdetailservice.util;

import com.iitr.gl.userdetailservice.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;


public class GetJwtSubject {

    public boolean isAuthorized(String jwt, Environment environment, String userId, boolean isAdmin) {
        String subject = Jwts.parser().
                setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(jwt).
                getBody().getSubject();
        String[] userDetail = subject.split(":::");
        if (isAdmin) {
            if (userDetail[1].equals("true") && userDetail[0].equals(userId))
                return true;
            else return false;
        } else {
            if (!userDetail[1].equals("true") && userDetail[0].equals(userId))
                return true;
            else return false;
        }
    }

    public void verifyIfAuthorized(String token, String userId, Environment environment, boolean isAdmin) {
        if (!isAuthorized(token.replace("Bearer", ""), environment, userId, isAdmin))
            throw new UnauthorizedException("You are not authorized");
    }
}
