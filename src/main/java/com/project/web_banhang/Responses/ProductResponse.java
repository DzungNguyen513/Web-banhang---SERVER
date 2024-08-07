package com.project.web_banhang.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.web_banhang.Model.BaseEntity;
import com.project.web_banhang.Model.Product;
import com.project.web_banhang.Model.ProductImage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse  {
    private Long id;

    private String name;

    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    public static ProductResponse fromProduct(Product product) {
        //List<String> imageUrls = product.getProductImageUrls();
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .productImages(product.getProductImages())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
