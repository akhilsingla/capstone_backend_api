package com.iitr.gl.apigateway;

import com.iitr.gl.apigateway.data.ExpiredTokenMySqlRepository;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;


@Component
public class AuthorizationTokenFilter extends AbstractGatewayFilterFactory<AuthorizationTokenFilter.Config> {

    @Autowired
    Environment environment;

    @Autowired
    ExpiredTokenMySqlRepository expiredTokenMySqlRepository;

    public AuthorizationTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                boolean validToken = isValidJwtToken(authorizationHeader.replace("Bearer", ""));
                if (validToken)
                    return chain.filter(exchange);
            }

            ServerHttpResponse httpResponse = exchange.getResponse();
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return httpResponse.setComplete();

        });
    }

    private boolean isValidJwtToken(String jwt) {
        if(expiredTokenMySqlRepository.findByToken(jwt) != null)
            return false;


        try {
            String subject = Jwts.parser().
                    setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(jwt).
                    getBody().getSubject();
            if (subject == null || subject.isEmpty())
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static class Config {

    }
}
