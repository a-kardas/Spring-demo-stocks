package com.fp.stock.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
