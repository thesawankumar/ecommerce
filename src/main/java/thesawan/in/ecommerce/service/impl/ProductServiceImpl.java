package thesawan.in.ecommerce.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.exceptions.ProductException;
import thesawan.in.ecommerce.model.Category;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.repository.CategoryRepository;
import thesawan.in.ecommerce.repository.ProductRepository;
import thesawan.in.ecommerce.response.createProductRequest;
import thesawan.in.ecommerce.service.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Category createOrFetchCategory(String categoryId, int level, Category parentCategory) {
        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("Category ID must not be null or blank");
        }

        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            category.setParentCategory(parentCategory); // null for level 1
            category = categoryRepository.save(category);
        }
        return category;
    }

    @Override
    public Product createProduct(createProductRequest req, Seller seller) {
//        Category category1 = categoryRepository.findByCategoryId(req.getCategory());
//        if (category1 == null) {
//            Category category = new Category();
//            category.setCategoryId(req.getCategory());
//            category.setLevel(1);
//            category1 = categoryRepository.save(category);
//        }
//        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
//        if (category2 == null) {
//            Category category = new Category();
//            category.setCategoryId(req.getCategory2());
//            category.setLevel(2);
//            category.setParentCategory(category1);
//            category2 = categoryRepository.save(category);
//        }
//        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
//        if (category3 == null) {
//            Category category = new Category();
//            category.setCategoryId(req.getCategory3());
//            category.setLevel(3);
//            category.setParentCategory(category2);
//            category3 = categoryRepository.save(category);
//        }
        Category category1 = createOrFetchCategory(req.getCategory(), 1, null);
        Category category2 = createOrFetchCategory(req.getCategory2(), 2, category1);
        Category category3 = createOrFetchCategory(req.getCategory3(), 3, category2);

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setTitle(req.getTitle());
        product.setCreatedAt(LocalDateTime.now());
        product.setMrpPrice(req.getMrpPrice());
        product.setSellingPrice(req.getSellingPrice());
        product.setColor(req.getColor());
        product.setSizes(req.getSizes());
        product.setImages(req.getImages());
        product.setDiscountPercentage(discountPercentage);
        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice < 0) {
            throw new IllegalArgumentException("MRP Price and Selling Price must be greater than zero.");
        }
        return (mrpPrice - sellingPrice) * 100 / mrpPrice;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = getProductById(productId);
        productRepository.delete(product);

    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        getProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductException("Product not found with id: " + productId));
    }


    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand,
                                        String colors, String sizes,
                                        Integer minPrice, Integer maxPrice,
                                        Integer minDiscount, String sort,
                                        String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));

            }
            if (colors != null && !colors.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            if (sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            pageable = switch (sort) {
                case "price_low" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
            };
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
