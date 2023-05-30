package com.wadson.pos.org.serviceImpl;


import com.wadson.pos.org.JWT.CustomUserDetailService;
import com.wadson.pos.org.JWT.JwtFilter;
import com.wadson.pos.org.JWT.JwtUtility;
import com.wadson.pos.org.POJO.User;
import com.wadson.pos.org.constents.GlobalConstent;
import com.wadson.pos.org.dao.UserDao;
import com.wadson.pos.org.service.UserService;
import com.wadson.pos.org.utils.Utils;
import com.wadson.pos.org.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private  UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private JwtFilter jwtFilter;
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public ResponseEntity<?> signUp(Map<String, String> request) {

        log.info("Inside signe {} ",request);
       try {
           if (validateSignUpMap(request)) {
               User user = this.userDao.findByEmailId(request.get("email"));
               if (Objects.isNull(user)) {
                   User u = extractUser(request);
                   this.userDao.save(u);
                   return Utils.getResponseEntity("Successfully registered", HttpStatus.OK);
               } else {
                   return Utils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
               }
           } else {
               return Utils.getResponseEntity(GlobalConstent.INVALID_DATA, HttpStatus.BAD_REQUEST);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
         return Utils.getResponseEntity(GlobalConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<?> login(Map<String, String> request) {
        try {
            Authentication auth=this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.get("email"),request.get("password"))
            );
            if (auth.isAuthenticated()){
                if (customUserDetailService.getUser().getStatus().equalsIgnoreCase("true")){
                   String token=this.jwtUtility.generateToken(customUserDetailService.getUser().getEmail(),customUserDetailService.getUser().getRole());
                    return ResponseEntity.ok(Map.of("token",token));
                }else{
                    return ResponseEntity.ok(Map.of("message","Your Account is not active, Wait for admin approval"));

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(Map.of("message","Bad Credentials"));

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()){
             this.userDao.getAllUser();
                return new ResponseEntity<>(this.userDao.getAllUser(),HttpStatus.OK);

            }else{
                return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> update(Map<String, String> request) {
       try {
            if (jwtFilter.isAdmin()){
            Optional<User> user=this.userDao.findById(Long.parseLong(request.get("id")));
                if (!user.isEmpty()){
                  userDao.updateStatus(request.get("status"),Long.parseLong(request.get("id")));
                    return Utils.getResponseEntity("User Status updated successfully", HttpStatus.OK);
                }else{
                    return Utils.getResponseEntity("User id does not exist", HttpStatus.OK);
                }
            }else{
                return Utils.getResponseEntity(GlobalConstent.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
            }
       } catch (Exception e){
        e.printStackTrace();
    }
    return Utils.getResponseEntity(GlobalConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

}

    private User extractUser(Map<String, String> request) {
      User user =new User();
      user.setName(request.get("name"));
      user.setContactNumber(request.get("contactNumber"));
      user.setEmail(request.get("email"));
      user.setPassword(this.passwordEncoder.encode(request.get("password")));
      user.setStatus("true");
      user.setRole("user");
      return user;
    }


    private boolean validateSignUpMap(Map<String,String> request){

        if (request.containsKey("name") && request.containsKey("contactNumber") &&
                request.containsKey("email") && request.containsKey("password")){
            return true;
        }
        return false;
    }


}
