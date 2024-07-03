package com.nageoffer.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//不能把DO返回给前端
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRespDTO {

    private String token;

}
