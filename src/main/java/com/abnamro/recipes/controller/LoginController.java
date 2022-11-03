package com.abnamro.recipes.controller;

import com.abnamro.recipes.Model.Dto.WebUserDto;
import com.abnamro.recipes.config.jwt.JwtTokenProvider;
import com.abnamro.recipes.config.jwt.JwtTokenResponse;
import com.abnamro.recipes.util.ApiMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RestController
@RequestMapping(ApiMapping.LOGIN)
@CrossOrigin
public class LoginController {

    private AuthenticationManager authMngr;
    private JwtTokenProvider jwtTokenProvider;

    public LoginController(AuthenticationManager authMngr, JwtTokenProvider jwtTokenProvider) {
        this.authMngr = authMngr;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody @Valid WebUserDto webUser) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    webUser.getUsername(), webUser.getPassword());
            authMngr.authenticate(authToken);
            return ResponseEntity.ok(new JwtTokenResponse(jwtTokenProvider.createToken(webUser.getUsername())));
        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED " + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS " + e.getMessage());
        }
    }

}