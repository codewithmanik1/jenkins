package com.wave.matrix.masters.service.impl;

import com.wave.matrix.common.ApiResponse;
import com.wave.matrix.common.FilterDto;
import com.wave.matrix.masters.dto.request.CategoryRequestDto;
import com.wave.matrix.masters.dto.request.FAQsRequestDto;
import com.wave.matrix.masters.dto.request.ProductsRequestDto;
import com.wave.matrix.masters.dto.response.ProductsCategoryResponseDto;
import com.wave.matrix.masters.entity.Category;
import com.wave.matrix.masters.entity.FAQs;
import com.wave.matrix.masters.entity.ProductColours;
import com.wave.matrix.masters.entity.Products;
import com.wave.matrix.masters.repository.CategoryRepository;
import com.wave.matrix.masters.repository.FAQsRepository;
import com.wave.matrix.masters.repository.ProductRepository;
import com.wave.matrix.masters.service.MasterService;
import com.wave.matrix.storage.FileSystemStorageServiceMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MasterServiceImpl implements MasterService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FAQsRepository faQsRepository;

    @Autowired
    private FileSystemStorageServiceMaster fileSystemStorageServiceMaster;


    @Override
    public ResponseEntity<?> getImageAsFile(String imagePath) {
        var response = new ApiResponse<>();

        // Use the absolute directory path
        String directoryPath = "/home/maanik/ccodes/wavematrix/products/";
        System.out.println("Requested File Name: " + imagePath);
        System.out.println("Directory Path: " + directoryPath);

        try {
            // Construct the full path to the image file
            File imageFile = new File(directoryPath + File.separator + imagePath);

            // Log the full file path
            System.out.println("Full File Path: " + imageFile.toPath());

            // Check if the file exists
            if (!imageFile.exists() || !imageFile.isFile()) {
                response.responseMethod(HttpStatus.NOT_FOUND.value(), "Image not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Prepare the file as a Resource
            Path filePath = imageFile.toPath();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                response.responseMethod(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not read the file", null, null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            // Return the file as a response entity
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            // Handle exceptions during file read operation
            response.responseMethod(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while retrieving the image: " + e.getMessage(),
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<?> saveProducts(ProductsRequestDto productsRequestDto) {
        var response = new ApiResponse<>();
        Products products = new Products();

        if (productsRequestDto.getId() != null) {
            // Fetch existing product, else throw exception
            products = productRepository.findById(productsRequestDto.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            products.setLastModifiedBy(productsRequestDto.getUpdateBy());
            products.setLastModifiedDate(LocalDateTime.now());
            products.setIsActive(productsRequestDto.getIsActive());
            response.responseMethod(HttpStatus.OK.value(), "Data updated successfully", null, null);
        } else {
            products.setCreatedBy(productsRequestDto.getCreatedBy());
            products.setCreatedDate(LocalDateTime.now());
            products.setLastModifiedBy(productsRequestDto.getCreatedBy());
            products.setLastModifiedDate(LocalDateTime.now());
            products.setIsActive(true);
            response.responseMethod(HttpStatus.OK.value(), "Product created successfully", null, null);
        }

        products.setCategoryId(productsRequestDto.getCategoryId());
        products.setProductName(productsRequestDto.getProductName());
        products.setProductDescription(productsRequestDto.getProductDescription());
        products.setProductMaterial(productsRequestDto.getProductMaterial());
        System.out.println("In out");
        products.setDimensions(productsRequestDto.getDimensions());
        products.setKeyFeatures(productsRequestDto.getKeyFeatures());

        // ✅ Convert Base64 to Image and Store Paths
        List<ProductColours> productColoursList = productsRequestDto.getProductColours().stream().map(data -> {
            ProductColours productColours = new ProductColours();
            List<String> imagePaths = new ArrayList<>();

            if (data.getProductBase64() != null) {
                data.getProductBase64().forEach(base64Image -> {
                    try {
                        // Fetch category
                        Category category = categoryRepository.findById(productsRequestDto.getCategoryId().getId())
                                .orElseThrow(() -> new RuntimeException("Category not found"));

                        // Generate unique filename
                        String uniqueFilename = System.currentTimeMillis() + "_" + category.getCategoryName() + ".png";

                        // Store Base64 as Image
                        fileSystemStorageServiceMaster.storeBaseImage(base64Image, uniqueFilename, FileSystemStorageServiceMaster.PRODUCTS);

                        // Add image path to list
                        imagePaths.add(uniqueFilename);

                        System.out.println("Product image saved at: " + FileSystemStorageServiceMaster.PRODUCTS + "/" + uniqueFilename);
                    } catch (IOException e) {
                        System.err.println("Failed to save image: " + e.getMessage());
                    }
                });
            }

            // Store color and associated image paths
            productColours.setProductColour(data.getProductColour());
            productColours.setProductBase64(imagePaths);

            return productColours;
        }).collect(Collectors.toList());

        products.setProductColours(productColoursList);  // ✅ Store List of Images for Each Color


        // Save to DB
        productRepository.save(products);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> saveCategory(CategoryRequestDto categoryRequestDto) {
        var response = new ApiResponse<>();

        Category category = new Category();

        if(category.getId() != null){
            category = categoryRepository.findById(categoryRequestDto.getId()).get();
            category.setLastModifiedBy(categoryRequestDto.getUpdatedBy());
            category.setLastModifiedDate(LocalDateTime.now());
            category.setIsActive(categoryRequestDto.getIsActive());
            response.responseMethod(HttpStatus.OK.value(), "Data updated successfully", null, null);
        }else{
            category.setCreatedBy(categoryRequestDto.getCreatedBy());
            category.setCreatedDate(LocalDateTime.now());
            category.setLastModifiedBy(categoryRequestDto.getCreatedBy());
            category.setLastModifiedDate(LocalDateTime.now());
            response.responseMethod(HttpStatus.OK.value(), "Category created successfully", null, null);
        }

        category.setCategoryName(categoryRequestDto.getCategoryName());
        try {
            if (categoryRequestDto.getCategoryThumbNail() != null) {
                String uniqueFilename = System.currentTimeMillis() + categoryRequestDto.getCategoryName() + ".png";
                fileSystemStorageServiceMaster.storeBaseImage(categoryRequestDto.getCategoryThumbNail(), uniqueFilename, FileSystemStorageServiceMaster.PRODUCTS);
                category.setCategoryThumbNailPath(uniqueFilename);
                System.out.println("Category save here "+ "/"+FileSystemStorageServiceMaster.PRODUCTS + "/"+ uniqueFilename);
            }else{
                category.setCategoryThumbNailPath(null);
            }
        } catch (IOException e) {
            response.responseMethod(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save image", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        categoryRepository.save(category);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getProductsCategory() {
        var response = new ApiResponse<>();

        List<ProductsCategoryResponseDto> data = categoryRepository.getProductsCategory();
        if(!data.isEmpty()){
            Long getCount = categoryRepository.getProductsCategoryCount();
            response.responseMethod(HttpStatus.OK.value(), "Data fetch successfully", data, getCount);
        }else{
            response.responseMethod(HttpStatus.NOT_FOUND.value(), "Data not found", data, null);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getProductsByCategoryId(FilterDto filterDto)  {
        var response = new ApiResponse<>();

        List<Map<String, Objects>> data = productRepository.getProductsByCategoryId(filterDto.getId(), filterDto.getPage(), filterDto.getSize());
        if(!data.isEmpty()){
            Long getCount = productRepository.getProductsByCategoryIdCount(filterDto.getId());
            response.responseMethod(HttpStatus.OK.value(), "Data fetch successfully", data, getCount);
        }else{
            response.responseMethod(HttpStatus.NOT_FOUND.value(), "Data not found", data, null);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> saveFAQs(FAQsRequestDto faQsRequestDto) {
        var response = new ApiResponse<>();

        FAQs faQs = new FAQs();

        if (faQs.getId() != null) {
            // Fetch existing product, else throw exception
            faQs = faQsRepository.findById(faQsRequestDto.getId())
                    .orElseThrow(() -> new RuntimeException("FAQs not found"));

            faQs.setLastModifiedBy(faQsRequestDto.getUpdatedBy());
            faQs.setLastModifiedDate(LocalDateTime.now());
            faQs.setIsActive(faQsRequestDto.getIsActive());
            response.responseMethod(HttpStatus.OK.value(), "Data updated successfully", null, null);
        } else {
            faQs.setCreatedBy(faQsRequestDto.getCreatedBy());
            faQs.setCreatedDate(LocalDateTime.now());
            faQs.setLastModifiedBy(faQsRequestDto.getCreatedBy());
            faQs.setLastModifiedDate(LocalDateTime.now());
            faQs.setIsActive(true);
            response.responseMethod(HttpStatus.OK.value(), "FAQ created successfully", null, null);
        }
        faQs.setQuestions(faQsRequestDto.getQuestions());
        faQs.setAnswer(faQsRequestDto.getAnswer());

        faQsRepository.save(faQs);
        return ResponseEntity.ok(response);
    }
}
