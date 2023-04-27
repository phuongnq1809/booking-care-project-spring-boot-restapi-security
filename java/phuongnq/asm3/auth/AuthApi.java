/*
 * AuthApi Class: generate access token for user login
 *
 */

package phuongnq.asm3.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import phuongnq.asm3.entity.User;
import phuongnq.asm3.exception.EntityErrorResponse;
import phuongnq.asm3.jwt.JwtTokenUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@RestController
public class AuthApi {

    AuthenticationManager authManager;

    JwtTokenUtil jwtUtil;

    @Autowired
    public AuthApi(AuthenticationManager theAuthenticationManager, JwtTokenUtil theJwtTokenUtil) {
        authManager = theAuthenticationManager;
        jwtUtil = theJwtTokenUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getUsername(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {

            // create a EntityErrorResponse
            EntityErrorResponse error = new EntityErrorResponse();
            error.setStatus(HttpStatus.UNAUTHORIZED.value());
            error.setMessage(ex.getMessage());
            String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
            error.setTimeStamp(currentDateTime);

            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

        }
    }
}
