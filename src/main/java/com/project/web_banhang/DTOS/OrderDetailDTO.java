package com.project.web_banhang.DTOS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.web_banhang.Model.Product;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;

    @Min(value = 0, message = "Price must be > 0")
    private Float price;

    @Min(value = 1, message = "Number of product ID must be > 0")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(value = 0, message = "Total must be > 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private  String color;

}
