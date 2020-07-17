package application.service.impl;

import application.model.entity.Asset;
import application.model.entity.AssetGroup;
import application.model.request.AssetAddRequest;
import application.repository.AssetGroupRepository;
import application.repository.AssetRepository;
import application.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Override
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    @Override
    public Asset insert(AssetAddRequest request) {
        Asset asset = new Asset();

        //SET ID AS AUTO INCREMENTING
        asset.setId(String.valueOf(assetRepository.count()+1));

        asset.setName(request.getName());
        asset.setAssetTypeId(request.getAssetTypeId());
        asset.setAssetGroupId(request.getAssetGroupId());
        asset.setNote(request.getNote());
        asset.setUnit(request.getUnit());
        asset.setPic(request.getPic());
        asset.setManufacturerId(request.getManufacturerId());
        asset.setSupplierId(request.getSupplierId());
        asset.setPrice(request.getPrice());
        asset.setPurchaseDate(LocalDate.parse(request.getPurchaseDate()));
        asset.setWarrantyInMonth(request.getWarrantyInMonth());

        //to create asset code, we need YEAR && ASSETGROUPNAME && COUNT NO OF THAT GROUP
        /* to get asset group name and count number of that group
        we can create a query that returns something like
        db.assetgroups.find({assetType: assetTypeId, _id: assetGroupId)
         */

        asset.setStatus("0");
        String year = request.getPurchaseDate().substring(0,4);
        AssetGroup assetGroupOptional = assetGroupRepository.findById(request.getAssetGroupId()).orElseThrow();

        String group = assetGroupOptional.getAbbreviation();
        String countGroup = String.format("%03d", assetRepository.countByAssetGroup(request.getAssetGroupId())+1);

        asset.setAssetCode(year.concat(group).concat(countGroup));

        assetRepository.insert(asset);

        return asset;
    }
}
