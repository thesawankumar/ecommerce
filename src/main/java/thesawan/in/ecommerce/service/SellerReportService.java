package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);

    SellerReport updateSellerReport(SellerReport sellerReport);
}
