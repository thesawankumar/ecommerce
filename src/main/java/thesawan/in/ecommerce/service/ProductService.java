package thesawan.in.ecommerce.service;


import org.springframework.data.domain.Page;
import thesawan.in.ecommerce.exceptions.ProductException;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.response.createProductRequest;

import java.util.List;

public interface ProductService {
    public Product createProduct(createProductRequest req, Seller seller) throws Exception;

    public void deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product product) throws ProductException;

    Product getProductById(Long productId) throws ProductException;

    List<Product> searchProducts(String query);

    public Page<Product> getAllProducts(String category,
                                        String brand, String colors, String sizes, Integer minPrice, Integer maxPrice,
                                        Integer minDiscount, String sort, String stock, Integer pageNumber);

    List<Product> getProductsBySellerId(Long sellerId);

}

