package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.Home;
import thesawan.in.ecommerce.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
