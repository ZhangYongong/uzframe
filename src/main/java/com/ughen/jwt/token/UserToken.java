package com.ughen.jwt.token;

import com.ughen.model.db.Role;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-29
 * Time: 17:42
 */
@Data
public class UserToken {
    private List<Role> roles;
    private Long userId;
    private String accessToken;

}
