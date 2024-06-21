package com.project.web_banhang.Service;

import com.project.web_banhang.DTOS.ProductDTO;
import com.project.web_banhang.DTOS.ProductImageDTO;
import com.project.web_banhang.Exceptions.DataNotFoundException;
import com.project.web_banhang.Model.Product;
import com.project.web_banhang.Model.ProductImage;
import com.project.web_banhang.Responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(long id);

    boolean existsByName(String name);

    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
