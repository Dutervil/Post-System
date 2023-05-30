package com.wadson.pos.org.service;

import com.wadson.pos.org.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<?> signUp(Map<String, String> request);

    ResponseEntity<?> login(Map<String, String> request);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<?> update(Map<String, String> request);
}
