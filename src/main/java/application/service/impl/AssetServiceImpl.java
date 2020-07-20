package application.service.impl;

import application.controller.exception.BadConnectionException;
import application.controller.exception.ItemNotFoundException;
import application.model.entity.*;
import application.model.request.AssetAddRequest;
import application.model.response.AssetGetResponse;
import application.repository.*;
import application.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetTypeRepository assetTypeRepository;

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    OfficeSiteRepository officeSiteRepository;

    @Autowired
    AssetStatusRepository assetStatusRepository;

    @Override
    public List<AssetGetResponse> findAll() {
        return assetRepository.findAllAssetGetResponse();

    }

    @Override
    public Asset insert(AssetAddRequest request) {
        Asset asset = new Asset();

        //SET ID AS AUTO INCREMENTING -- NEED TO FIND HIGHEST NUMBER, NOT TOTAL COUNT
        asset.setId(String.valueOf(assetRepository.count() + 1));

        asset.setAssetTypeId(request.getAssetTypeId());
        asset.setAssetGroupId(request.getAssetGroupId());
        asset.setAssociatedAsset(null);
        asset.setPic(null);
        asset.setName(request.getName());
        asset.setUnit(request.getUnit());
        asset.setNote(request.getNote());
        asset.setOfficeSiteId(request.getAssetTypeId());
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

        asset.setAssetStatusId("0");
        String year = request.getPurchaseDate().substring(0, 4);
        Optional<AssetGroup> assetGroupOptional = assetGroupRepository.findById(request.getAssetGroupId());

        if (assetGroupOptional.isPresent()) {
            String group = assetGroupOptional.get().getAbbreviation();

            String countGroup = String.format("%03d", assetRepository.countByAssetGroup(request.getAssetGroupId()) + 1);

            asset.setAssetCode(year.concat(group).concat(countGroup));
        } else {
            throw new BadConnectionException("Bad asset group id");
        }

        asset.setCiaC(0);
        asset.setCiaI(0);
        asset.setCiaA(0);
        asset.setCiaSum(0);
        asset.setCiaImportance(null);
        asset.setCiaNote(null);
        asset.setOwner(null);
        asset.setAssignDateStart(null);
        asset.setAssignDateEnd(null);
        asset.setOverdue(false);

        assetRepository.insert(asset);

        return asset;
    }

    @Override
    public AssetGetResponse findById(String id) {

        if (assetRepository.existsById(id)) {
            return assetRepository.findAssetGetResponseById(id);
        } else {
            throw new ItemNotFoundException("Submitted Asset Id not found");
        }
    }
}
