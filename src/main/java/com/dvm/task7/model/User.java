package com.dvm.task7.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String name;
    private String email;
    private int age;
    private String address;
    private long money;
}
