package com.ecommerce.library.dto;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    @Size(min = 3, max = 10, message = "請輸入 3 ~ 5 個字母")
    private String firstName;

    @Size(min = 3, max = 10, message = "請輸入 3 ~ 5 個字母")
    private String lastName;

    private String username;

    @Size(min = 3, max = 15, message = "請輸入 3 ~ 5 個字母")
    private String password;

    private String repeatPassword;
}
