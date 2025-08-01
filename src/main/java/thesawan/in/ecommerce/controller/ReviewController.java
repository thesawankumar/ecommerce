package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.Review;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.response.ApiResponse;
import thesawan.in.ecommerce.response.CreateReviewRequest;
import thesawan.in.ecommerce.service.ProductService;
import thesawan.in.ecommerce.service.ReviewService;
import thesawan.in.ecommerce.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/product/{productId}/write-review")
    public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest req,
                                              @RequestHeader("Authorization") String jwt,
                                              @PathVariable Long productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getProductById(productId);
        Review review = reviewService.createReview(req, user, product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/update-review/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId,
                                               @RequestBody CreateReviewRequest req,
                                               @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Review updatedReview = reviewService.updateReview(reviewId, req.getReviewText(), req.getReviewRating(), user.getId());
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/delete-review/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
                                                    @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res = new ApiResponse();
        res.setMessage("Review deleted successfully");
        return ResponseEntity.ok(res);
    }
}
