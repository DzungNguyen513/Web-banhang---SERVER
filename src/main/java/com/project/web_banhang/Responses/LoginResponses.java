package com.project.web_banhang.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.web_banhang.Model.Order;
import com.project.web_banhang.Model.OrderDetail;
import com.project.web_banhang.Model.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponses {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
}
