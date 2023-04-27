package phuongnq.asm3.jwt;

import java.io.IOException;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import phuongnq.asm3.dao.RoleRepository;
import phuongnq.asm3.entity.Role;
import phuongnq.asm3.entity.User;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    private JwtTokenUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    public JwtTokenFilter(JwtTokenUtil theJwtTokenUtil) {
        jwtUtil = theJwtTokenUtil;
        mapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            if (response.getStatus() == 401) {
                resolver.resolveException(request, response, null,
                        new RuntimeException("Unauthorized. 'Authorization' header must not be blank, " +
                                "must begin with 'Bearer', must not be expired, and must be valid."));
            }
            return;
        }

        String token = getAccessToken(request);

        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            if (response.getStatus() == 401) {
                resolver.resolveException(request, response, null,
                        new RuntimeException("Can not validate access token, please try again!"));
            }
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        Claims claims = jwtUtil.parseClaims(token);
        String subject = (String) claims.get(Claims.SUBJECT);
        String roleName = (String) claims.get("role");

        Role theRole = roleRepo.findByRoleName(roleName);
        userDetails.setRole(theRole);

        String[] jwtSubject = subject.split(",");

        userDetails.setId(Integer.parseInt(jwtSubject[0]));
        userDetails.setUsername(jwtSubject[1]);

        return userDetails;
    }
}
