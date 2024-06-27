package com.project.web_banhang.Controller;

import com.project.web_banhang.Components.LocalizationUtils;
import com.project.web_banhang.DTOS.UserDTO;
import com.project.web_banhang.DTOS.UserLoginDTO;
import com.project.web_banhang.Model.User;
import com.project.web_banhang.Responses.LoginResponses;
import com.project.web_banhang.Responses.RegisterResponse;
import com.project.web_banhang.Service.IUserService;
import com.project.web_banhang.Utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

            RegisterResponse registerResponse = new RegisterResponse();
            if(result.hasErrors()){
                List<String> errMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                registerResponse.setMessage(errMessages.toString());
                return ResponseEntity.badRequest().body(registerResponse);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                registerResponse.setMessage(localizationUtils.getLocalizeMessage(MessageKeys.PASSWORD_NOT_MATCH));
                return ResponseEntity.badRequest().body(registerResponse);
            }
        try {
            User user = userService.createUser(userDTO);
            registerResponse.setMessage(localizationUtils.getLocalizeMessage(MessageKeys.REGISTER_SUCCESSFULLY));
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponses> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ResponseEntity.ok(LoginResponses.builder().message(localizationUtils.getLocalizeMessage(MessageKeys.LOGIN_SUCCESSFULLY)).token(token).build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponses.builder().message(localizationUtils.getLocalizeMessage(MessageKeys.LOGIN_FAILED, e.getMessage())).build()
            );
        }
    }

}
