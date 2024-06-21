package com.project.web_banhang.Service;

import com.project.web_banhang.DTOS.UserDTO;
import com.project.web_banhang.Exceptions.DataNotFoundException;
import com.project.web_banhang.Model.User;

public interface IUserService {

    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String phoneNumber, String password);

}