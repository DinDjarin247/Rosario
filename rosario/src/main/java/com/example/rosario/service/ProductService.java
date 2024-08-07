package com.example.rosario.service;

import com.example.rosario.dto.ProductDetailDto;
import com.example.rosario.entity.Product;
import com.example.rosario.entity.ProductImg;
import com.example.rosario.entity.ProductSeller;
import com.example.rosario.entity.Seller;
import com.example.rosario.repository.ProductImgRepository;
import com.example.rosario.repository.ProductRepository;
import com.example.rosario.repository.ProductSellerRepository;
import com.example.rosario.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSellerRepository productSellerRepository;

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductImgRepository productImgRepository;
    public Product saveProduct(Product product, Long sellerId) {
        Product savedProduct = productRepository.save(product);
        Optional<Seller> sellerOpt = sellerRepository.findById(sellerId);
        if (sellerOpt.isPresent()) {
            ProductSeller productSeller = new ProductSeller();
            productSeller.setProduct(savedProduct);
            productSeller.setSeller(sellerOpt.get());
            productSellerRepository.save(productSeller);
        } else {
            throw new RuntimeException("Seller not found with id: " + sellerId);
        }
        return savedProduct;
    }

    public List<Product> getProductsBySellerId(Long sellerId) {
        // SellerID로 상품 판매자 목록 조회하기
        List<ProductSeller> productSellers = productSellerRepository.findBySeller_SellerId(sellerId);
        // 판매자의 상품 ID 목록 추출하기
        List<Long> productIds = productSellers.stream()
                .map(productSeller -> productSeller.getProduct().getProductId())
                .collect(Collectors.toList());
        // 상품 ID 목록으로 상품 조회하기
        return productRepository.findByProductIdIn(productIds);
    }

    public List<ProductSeller> getSellersByProductId(Long productId) {
        // SellerID로 상품 판매자 목록 조회하기
        List<ProductSeller> productSellers = productSellerRepository.findByProduct_ProductId(productId);
        // 판매자의 상품 ID 목록 추출하기
//        List<Long> productIds = productSellers.stream()
//                .map(productSeller -> productSeller.getProduct().getProductId())
//                .collect(Collectors.toList());
        // 상품 ID 목록으로 상품 조회하기
//        return productRepository.findByProductIdIn(productIds);
        return productSellers;
    }
    public ProductDetailDto getProductDetail(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            // ProductImg를 List에서 Set으로 변환
            Set<ProductImg> productImages = productImgRepository.findByProductProductId(productId)
                    .stream()
                    .collect(Collectors.toSet());
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setProductId(product.getProductId());
            productDetailDto.setProductNm(product.getProductNm());
            productDetailDto.setProductPrice(product.getProductPrice());
            productDetailDto.setProductStock(product.getProductStock());
            productDetailDto.setProductSize(product.getProductSize());
            productDetailDto.setProductImages(productImages);

            return productDetailDto;
        } else {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }
}
