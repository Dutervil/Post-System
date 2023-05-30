package com.wadson.pos.org.JWT;

import com.wadson.pos.org.POJO.User;
import com.wadson.pos.org.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
public class CustomUserDetailService  implements UserDetailsService {


    UserDao userDao;

    private com.wadson.pos.org.POJO.User user;

    public CustomUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    public com.wadson.pos.org.POJO.User getUser() {
        return this.user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         log.info("Inside loadUserByUsername {}",username);
        this.user=userDao.findByEmailId(username);
        if(!Objects.isNull(this.user))
            return new org.springframework.security.core.userdetails.User(
                    this.user.getEmail(),this.user.getPassword(),new ArrayList<>()
            );
        else
            throw  new UsernameNotFoundException("User not found");
    }
}
