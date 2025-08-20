package thesawan.in.ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminProfileResponse {
    private Long totalUsers;
    private Long totalSellers;
    private Long totalOrders;
}
