package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.exceptions.ProductException;
import thesawan.in.ecommerce.exceptions.SellerException;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.response.createProductRequest;
import thesawan.in.ecommerce.service.ProductService;
import thesawan.in.ecommerce.service.SellerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/product")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Product>> getProductsBySellerId(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductsBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String jwt, @RequestBody createProductRequest req) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(req, seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws Exception {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product)
            throws ProductException {
        Product updatedProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }


}
