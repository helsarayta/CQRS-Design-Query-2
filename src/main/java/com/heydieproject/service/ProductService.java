package com.heydieproject.service;

import com.heydieproject.entity.Product;
import com.heydieproject.entity.dto.ProductDto;
import com.heydieproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found !!"));
    }

    @KafkaListener(topics = "product-cqrs", groupId = "product-cqrs-group")
    public void kafkaProcess(ProductDto productDto) {
        try {
            if(productDto.getEventKafka().equals("create")) {
                Product product = new Product();
                Product dtoProduct = productDto.getProduct();
                product.setProductName(dtoProduct.getProductName());
                product.setDescription(dtoProduct.getDescription());
                product.setProductImg(dtoProduct.getProductImg());
                product.setQuantity(dtoProduct.getQuantity());
                product.setPrice(dtoProduct.getPrice());
                product.setId(dtoProduct.getId());
                product.setCreatedAt(dtoProduct.getCreatedAt());
                product.setUpdatedAt(dtoProduct.getUpdatedAt());

                productRepository.save(product);
            } else {
                Product productExisting = productRepository.findById(productDto.getProduct().getId()).get();

                Product dtoProduct = productDto.getProduct();
                productExisting.setProductName(dtoProduct.getProductName());
                productExisting.setProductImg(dtoProduct.getProductImg());
                productExisting.setPrice(dtoProduct.getPrice());
                productExisting.setId(dtoProduct.getId());
                productExisting.setQuantity(dtoProduct.getQuantity());
                productExisting.setDescription(dtoProduct.getDescription());
                productExisting.setUpdatedAt(dtoProduct.getUpdatedAt());
                productExisting.setCreatedAt(dtoProduct.getCreatedAt());

                productRepository.save(productExisting);

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
