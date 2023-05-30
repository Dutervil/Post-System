package com.wadson.pos.org.restImpl;

import com.wadson.pos.org.constents.GlobalConstent;
import com.wadson.pos.org.rest.UserRest;
import com.wadson.pos.org.service.UserService;
import com.wadson.pos.org.utils.Utils;
import com.wadson.pos.org.wrapper.UserWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    UserService userService;
    public UserRestImpl(UserService userService) {
        this.userService = userService;
    }



    @Override
    public ResponseEntity<?> signUp(Map<String, String> request) {
        try {
            return userService.signUp(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(GlobalConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<?> login(Map<String, String> request) {
        try {
            return userService.login(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(GlobalConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<?> update(Map<String, String> request) {
        try {
            return userService.update(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(GlobalConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
