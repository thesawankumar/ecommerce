package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.Review;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.response.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest req, User user, Product product);

    List<Review> getReviewsByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;

}
