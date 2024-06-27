package com.project.web_banhang.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.web_banhang.Model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}

