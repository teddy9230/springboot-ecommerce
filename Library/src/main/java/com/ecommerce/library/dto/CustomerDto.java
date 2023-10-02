package com.ecommerce.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @Size(min = 3, max = 15, message = "請輸入 3 ~ 5 個字母")
    private String firstName;

    @Size(min = 3, max = 15, message = "請輸入 3 ~ 5 個字母")
    private String lastName;

    private String username;

    @Size(min = 5, max = 20, message = "請輸入 5 ~ 20 個字母")
    private String password;

    private String repeatPassword;
}
