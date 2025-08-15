package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.model.Deal;
import thesawan.in.ecommerce.response.ApiResponse;
import thesawan.in.ecommerce.service.DealService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deal")
public class DealController {

    private final DealService dealService;

    @PostMapping("/create")
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) throws Exception {

        Deal createdDeals = dealService.createDeal(deal);
        return new ResponseEntity<>(createdDeals, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Deal>> getAllDeals() {
        List<Deal> deals = dealService.getDeals();
        return new ResponseEntity<>(deals, HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Deal> updateDeal(@RequestBody Deal deal,
                                           @PathVariable Long id) throws Exception {
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return new ResponseEntity<>(updatedDeal, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse res = new ApiResponse();
        res.setMessage("Deal deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
