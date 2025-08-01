package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.SellerReport;
import thesawan.in.ecommerce.repository.SellerReportRepository;
import thesawan.in.ecommerce.service.SellerReportService;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {
    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sr = sellerReportRepository.findBySellerId(seller.getId());
        if (sr == null) {
            SellerReport sellerReport = new SellerReport();
            sellerReport.setSeller(seller);
            return sellerReportRepository.save(sellerReport);
        }
        return sr;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
