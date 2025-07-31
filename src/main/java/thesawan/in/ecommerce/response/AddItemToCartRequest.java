package thesawan.in.ecommerce.response;

import lombok.Data;

@Data
public class AddItemToCartRequest {
    private String size;
    private int quantity;
    private Long productId;
}
