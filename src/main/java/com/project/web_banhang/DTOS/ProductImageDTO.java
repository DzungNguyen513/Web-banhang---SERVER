package com.project.web_banhang.DTOS;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product ID must be > 0")
    private Long productId;

    @Size(min = 5, max = 200, message = "Image name must be between 3 and 200 characters")
    @JsonProperty("image_url")
    private String imageUrl;
}
