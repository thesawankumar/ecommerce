package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.model.Home;
import thesawan.in.ecommerce.model.HomeCategory;
import thesawan.in.ecommerce.service.HomeCategoryService;
import thesawan.in.ecommerce.service.HomeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home-category")
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/create")
    public ResponseEntity<Home> createHomeCategory(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories = homeCategoryService.createHomeCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/get-all")
    public ResponseEntity<List<HomeCategory>> getHomeCategory() {
        List<HomeCategory> categories = homeCategoryService.findAllHomeCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PatchMapping("/admin/update/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@RequestBody HomeCategory homeCategory, @PathVariable Long id) throws Exception {
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
        return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }


}
