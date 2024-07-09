package com.project.web_banhang.Service;

import com.project.web_banhang.DTOS.UserDTO;
import com.project.web_banhang.Exceptions.DataNotFoundException;
import com.project.web_banhang.Model.User;

public interface IUserService {

    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;

    public User getUserDetailsFromToken(String token) throws Exception;

}
