package application.service.impl;

import application.controller.exception.BadConnectionException;
import application.model.entity.*;
import application.model.request.AssetAddRequest;
import application.model.response.AssetGetResponse;
import application.repository.*;
import application.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public AssetGetResponse mapAssetToResponse(Asset asset, AssetType assetType, AssetGroup assetGroup,
                                               OfficeSite officeSite, Manufacturer manufacturer, Supplier supplier, AssetStatus assetStatus) {


        return new AssetGetResponse(
                asset.getId(),
                asset.getAssetCode(),
                assetType.getName(),
                assetGroup.getName(),
                asset.getName(),
                asset.getUnit(),
                asset.getNote(),
                asset.getAssociatedAsset(),
                officeSite.getName(),
                asset.getPic(),
                manufacturer.getName(),
                supplier.getName(),
                asset.getPrice(),
                asset.getPurchaseDate(),
                asset.getWarrantyInMonth(),
                assetStatus.getName(),
                asset.getCiaC(),
                asset.getCiaI(),
                asset.getCiaA(),
                asset.getCiaSum(),
                asset.getCiaImportance(),
                asset.getCiaNote(),
                asset.getOwner(),
                asset.getAssignDateStart(),
                asset.getAssignDateEnd(),
                asset.isOverdue()
        );
    }

    public AbstractObject getNameFromDatasetById(List<? extends AbstractObject> dataset, String inputId) {

        return dataset.stream().filter(obj -> inputId.equalsIgnoreCase(obj.getId()))
                .collect(Collectors.toList())
                .get(0);
    }

    public List<AssetGetResponse> mapAssetListToResponseList(List<Asset> assetList) {

        List<AssetType> assetTypeList = assetTypeRepository.findAll();
        List<AssetGroup> assetGroupList = assetGroupRepository.findAll();
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        List<Supplier> supplierList = supplierRepository.findAll();
        List<OfficeSite> officeSiteList = officeSiteRepository.findAll();
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();

        List<AssetGetResponse> responseList = new ArrayList<>();

        for (Asset asset : assetList) {
            AssetType assetType = (AssetType) getNameFromDatasetById(assetTypeList, asset.getAssetTypeId());
            AssetGroup assetGroup = (AssetGroup) getNameFromDatasetById(assetGroupList, asset.getAssetGroupId());
            Manufacturer manufacturer = (Manufacturer) getNameFromDatasetById(manufacturerList, asset.getManufacturerId());
            Supplier supplier = (Supplier) getNameFromDatasetById(supplierList, asset.getSupplierId());
            OfficeSite officeSite = (OfficeSite) getNameFromDatasetById(officeSiteList, asset.getOfficeSiteId());
            AssetStatus assetStatus = (AssetStatus) getNameFromDatasetById(assetStatusList, asset.getAssetStatusId());

            AssetGetResponse response = new AssetGetResponse(
                    asset.getId(),
                    asset.getAssetCode(),
                    assetType.getName(),
                    assetGroup.getName(),
                    asset.getName(),
                    asset.getUnit(),
                    asset.getNote(),
                    asset.getAssociatedAsset(),
                    officeSite.getName(),
                    asset.getPic(),
                    manufacturer.getName(),
                    supplier.getName(),
                    asset.getPrice(),
                    asset.getPurchaseDate(),
                    asset.getWarrantyInMonth(),
                    assetStatus.getName(),
                    asset.getCiaC(),
                    asset.getCiaI(),
                    asset.getCiaA(),
                    asset.getCiaSum(),
                    asset.getCiaImportance(),
                    asset.getCiaNote(),
                    asset.getOwner(),
                    asset.getAssignDateStart(),
                    asset.getAssignDateEnd(),
                    asset.isOverdue()
            );
            responseList.add(response);
        }

        return responseList;
    }

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
        Optional<Asset> assetOptional = assetRepository.findById(id);
        if (assetOptional.isPresent()) {
            Asset asset = assetOptional.get();
            AssetType assetType = assetTypeRepository.findById(asset.getAssetStatusId()).orElseThrow();
            AssetGroup assetGroup = assetGroupRepository.findById(asset.getAssetGroupId()).orElseThrow();
            OfficeSite officeSite = officeSiteRepository.findById(asset.getOfficeSiteId()).orElseThrow();
            Manufacturer manufacturer  = manufacturerRepository.findById(asset.getManufacturerId()).orElseThrow();
            Supplier supplier = supplierRepository.findById(asset.getSupplierId()).orElseThrow();
            AssetStatus assetStatus = assetStatusRepository.findById(asset.getAssetStatusId()).orElseThrow();

            return mapAssetToResponse(asset,assetType, assetGroup, officeSite, manufacturer, supplier, assetStatus );
        }
        return null;
    }
}
