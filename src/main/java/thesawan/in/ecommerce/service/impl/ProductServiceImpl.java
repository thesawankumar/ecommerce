package thesawan.in.ecommerce.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.response.createProductRequest;
import thesawan.in.ecommerce.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Product createProduct(createProductRequest req, Seller seller) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product getProductById(Long productId) {
        return null;
    }

    @Override
    public List<Product> findAllProducts() {
        return List.of();
    }

    @Override
    public List<Product> searchProducts() {
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        return null;
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return List.of();
    }
}
