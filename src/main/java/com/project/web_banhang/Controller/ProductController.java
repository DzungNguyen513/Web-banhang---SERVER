package com.project.web_banhang.Controller;

import com.github.javafaker.Faker;
import com.project.web_banhang.Components.LocalizationUtils;
import com.project.web_banhang.DTOS.CategoryDTO;
import com.project.web_banhang.DTOS.ProductDTO;
import com.project.web_banhang.DTOS.ProductImageDTO;
import com.project.web_banhang.Exceptions.DataNotFoundException;
import com.project.web_banhang.Model.Product;
import com.project.web_banhang.Model.ProductImage;
import com.project.web_banhang.Responses.ProductListResponse;
import com.project.web_banhang.Responses.ProductResponse;
import com.project.web_banhang.Service.IProductService;
import com.project.web_banhang.Utils.MessageKeys;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StringUtil;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final LocalizationUtils localizationUtils;


    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .productResponses(products)
                .totalPages(totalPages).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
           Product product = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(product));

        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createProduct (@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
        try {
            if(result.hasErrors()){
                List<String> errMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessages);
            }
            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok("Create product successfully " + newProduct);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files, @PathVariable Long id) {
        try {
            Product existtingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body(localizationUtils
                        .getLocalizeMessage(MessageKeys.UPLOAD_IMAGES_MAX_5));
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                if(file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body(localizationUtils
                                    .getLocalizeMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body(localizationUtils.getLocalizeMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
                }
                //lưu file và cập nhật thumbnail trong DTO
                String fileName = storageFile(file);
                ProductImage productImage = productService.createProductImage(existtingProduct.getId(), ProductImageDTO.builder().imageUrl(fileName).build());

                productImages.add(productImage);

            }
            return ResponseEntity.ok(productImages);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    private String storageFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() != null) {
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + " - " + fileName;

        java.nio.file.Path uploadDir = Paths.get("Uploads");
        //kieem tra và tạo thư mục nếu nó không tồn tại
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        //đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        //sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        try {
            Product updateProduct = productService.updateProduct(productId, productDTO);
            return ResponseEntity.ok(updateProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
                productService.deleteProduct(productId);
                return ResponseEntity.ok(String.format("Product with id  = %d deleted successfully", productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakerProducts() {
        Faker faker = new Faker();
        for (int i = 0; i< 100; i++) {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName))
            {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10,90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,3))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }
}
