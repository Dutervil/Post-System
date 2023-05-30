package com.wadson.pos.org.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data @NoArgsConstructor
public class UserWrapper {

    private  Long id;
    private String name;
    private String contactNumber;
    private String email;
    private String status;
    private String role;

    public UserWrapper(Long id, String name, String contactNumber, String email, String status, String role) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.status = status;
        this.role = role;
    }
}
