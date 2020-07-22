package application.service.impl;

import application.controller.exception.ItemNotFoundException;
import application.model.entity.*;
import application.model.requestdto.AssetCreateRequestDto;
import application.model.responsedto.AssetGetAllResponseDto;
import application.model.responsedto.AssetGetOneResponseDto;
import application.model.responsedto.AssetShortResponseDto;
import application.repository.*;
import application.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static application.service.impl.UserServiceImpl.mapUserEntityToShortResponseDto;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    UserRepository userRepository;

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
    public List<AssetGetAllResponseDto> findAll() {
        List<AssetWithName> assetList = assetRepository.findAllWithName();

        return mapAssetWithNameListToGetAllResponseList(assetList);
    }

    @Override
    public AssetGetOneResponseDto insert(AssetCreateRequestDto request, boolean isAnInsert) {

        Asset newAsset = new Asset();
        Asset oldAsset = new Asset();
        User pic = null;
        if (isAnInsert) {
            pic = userRepository.findActiveAdminById(request.getPic());
            if (pic == null) {
                throw new ItemNotFoundException("User not authorized to create asset!");
            }

            Optional<AssetType> assetTypeOptional = assetTypeRepository.findById(request.getAssetTypeId());
            if (!assetTypeOptional.isPresent()) {
                throw new ItemNotFoundException("Asset Type not found");
            }

            Optional<OfficeSite> officeSiteOptional = officeSiteRepository.findById(request.getOfficeSiteId());
            if (!officeSiteOptional.isPresent()) {
                throw new ItemNotFoundException("Office Site not found");
            }
        } else {
            Optional<Asset> assetOptional = assetRepository.findById(request.getId());
            if (!assetOptional.isPresent()) {
                throw new ItemNotFoundException("Asset not found!");
            }
            oldAsset = assetOptional.get();
        }

        String assetCode = oldAsset.getAssetCode();

        if (isAnInsert || !oldAsset.getAssetGroupId().equalsIgnoreCase(request.getAssetGroupId())) {
            Optional<AssetGroup> assetGroupOptional = assetGroupRepository.findById(request.getAssetGroupId());
            if (!assetGroupOptional.isPresent()) {
                throw new ItemNotFoundException("Asset Group not found");
            }

            String year = request.getPurchaseDate().substring(0, 4);
            String group = assetGroupOptional.get().getAbbreviation();
            String countGroup = String.format("%03d", createHighestIdByFieldOrAll(request.getAssetGroupId()));
            assetCode = year.concat(group).concat(countGroup);
        }

        if (isAnInsert || !oldAsset.getManufacturerId().equalsIgnoreCase(request.getManufacturerId())) {
            Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(request.getManufacturerId());
            if (!manufacturerOptional.isPresent()) {
                throw new ItemNotFoundException("Manufacturer not found");
            }
        }

        if (isAnInsert || !oldAsset.getSupplierId().equalsIgnoreCase(request.getSupplierId())) {
            Optional<Supplier> supplierOptional = supplierRepository.findById(request.getSupplierId());
            if (!supplierOptional.isPresent()) {
                throw new ItemNotFoundException("Supplier not found");
            }
        }

        if (isAnInsert) {
            newAsset.setId(String.valueOf(createHighestIdByFieldOrAll(null)));
            newAsset.setAssetTypeId(request.getAssetTypeId());
            newAsset.setOfficeSiteId(request.getAssetTypeId());
            newAsset.setPic(pic);
            newAsset.setOwner(null);
            newAsset.setAssetStatusId("0");
            newAsset.setAssignDateStart(null);
            newAsset.setAssignDateEnd(null);
            newAsset.setOverdue(false);
            newAsset.setAssociatedAsset(new HashSet<>());

            newAsset.setCiaC(0);
            newAsset.setCiaI(0);
            newAsset.setCiaA(0);
            newAsset.setCiaSum(0);
            newAsset.setCiaImportance(null);
            newAsset.setCiaNote(null);
        } else {
            newAsset.setId(request.getId());
            newAsset.setAssetTypeId(oldAsset.getAssetTypeId());
            newAsset.setPic(oldAsset.getPic());
            newAsset.setOfficeSiteId(oldAsset.getOfficeSiteId());
            newAsset.setOwner(oldAsset.getOwner());
            newAsset.setAssetStatusId(oldAsset.getAssetStatusId());
            newAsset.setAssignDateStart(oldAsset.getAssignDateStart());
            newAsset.setAssignDateEnd(oldAsset.getAssignDateEnd());
            newAsset.setOverdue(oldAsset.isOverdue());

//            check associated asset
            Set<Asset> assocList = new HashSet<>();
            for (String assocCode : request.getAssociatedAssetCode()) {
                Asset assoc = assetRepository.findAvailableAssocByAssetCode(assocCode);
                if (assoc == null) {
                    throw new ItemNotFoundException("Associated Asset not found!");
                }
                assocList.add(assoc);
            }
            newAsset.setAssociatedAsset(assocList);

            newAsset.setCiaC(request.getCiaC());
            newAsset.setCiaI(request.getCiaI());
            newAsset.setCiaA(request.getCiaA());
            newAsset.setCiaSum(request.getCiaSum());
            newAsset.setCiaImportance(request.getCiaImportance());
            newAsset.setCiaNote(request.getCiaNote());
        }

        newAsset.setAssetGroupId(request.getAssetGroupId());
        newAsset.setAssetCode(assetCode);
        newAsset.setName(request.getName());
        newAsset.setUnit(request.getUnit());
        newAsset.setNote(request.getNote());
        newAsset.setManufacturerId(request.getManufacturerId());
        newAsset.setSupplierId(request.getSupplierId());
        newAsset.setPrice(request.getPrice());
        newAsset.setPurchaseDate(LocalDate.parse(request.getPurchaseDate()));
        newAsset.setWarrantyInMonth(request.getWarrantyInMonth());

        Asset result = assetRepository.save(newAsset);

        return mapAssetToAssetGetOneResponseDto(result);
    }

    @Override
    public AssetGetOneResponseDto findById(String id) {

        if (assetRepository.existsById(id)) {
            Asset asset = assetRepository.findByIdCustom(id);

            Set<AssetShortResponseDto> dtoSet = mapAssetSetToAssetShortResponseDtoSet(asset.getAssociatedAsset());

            return new AssetGetOneResponseDto(
                    asset.getId(),
                    asset.getAssetCode(),
                    asset.getAssetTypeId(),
                    asset.getAssetGroupId(),
                    asset.getName(),
                    asset.getUnit(),
                    asset.getNote(),
                    dtoSet,
                    asset.getOfficeSiteId(),
                    mapUserEntityToShortResponseDto(asset.getPic()),
                    asset.getManufacturerId(),
                    asset.getSupplierId(),
                    asset.getPrice(),
                    asset.getPurchaseDate(),
                    asset.getWarrantyInMonth(),
                    asset.getAssetStatusId(),
                    asset.getCiaC(),
                    asset.getCiaI(),
                    asset.getCiaA(),
                    asset.getCiaSum(),
                    asset.getCiaImportance(),
                    asset.getCiaNote(),
                    mapUserEntityToShortResponseDto(asset.getOwner()),
                    asset.getAssignDateStart(),
                    asset.getAssignDateEnd(),
                    asset.isOverdue()
            );
        } else {
            throw new ItemNotFoundException("Submitted Asset not found");
        }
    }

    @Override
    public List<AssetGetAllResponseDto> findListByFields(String code, String name, String group, String owner) {

        List<AssetWithName> assetList = assetRepository.findListByFields(code, name, group, owner);

        return mapAssetWithNameListToGetAllResponseList(assetList);
    }

    @Override
    public List<AssetShortResponseDto> findAssocByName(String name) {

        List<Asset> assetList = assetRepository.listAvailableAssocNameByField("name", name);
        List<AssetShortResponseDto> dtoList = new ArrayList<>();
        for (Asset asset : assetList) {
            AssetShortResponseDto dto = new AssetShortResponseDto();
            dto.setId(asset.getId());
            dto.setName(asset.getName());
            dto.setValue(asset.getAssetCode());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private int createHighestIdByFieldOrAll(String fieldId) {
        List<Asset> assetList = assetRepository.getSimpleListOfIdByAssetGroupIdOrAll(fieldId);
        int max = 0;

        for (Asset asset : assetList) {
            int id = Integer.parseInt(asset.getId());
            max = Math.max(id, max);
        }
        return max + 1;
    }


    private AssetGetAllResponseDto mapAssetWithNameToGetAllResponseDto(AssetWithName asset) {
        return new AssetGetAllResponseDto(
                asset.getId(),
                asset.getAssetCode(),
                asset.getAssetType(),
                asset.getAssetGroup(),
                asset.getName(),
                asset.getOfficeSite(),
                asset.getAssetStatus(),
                asset.getCiaC(),
                asset.getCiaI(),
                asset.getCiaA(),
                asset.getCiaSum(),
                asset.getOwner(),
                asset.isOverdue()
        );
    }

    private List<AssetGetAllResponseDto> mapAssetWithNameListToGetAllResponseList(List<AssetWithName> assetList) {

        List<AssetGetAllResponseDto> dtoList = new ArrayList<>();

        for (AssetWithName asset : assetList) {
            dtoList.add(mapAssetWithNameToGetAllResponseDto(asset));
        }
        return dtoList;
    }

    private Set<AssetShortResponseDto> mapAssetSetToAssetShortResponseDtoSet(Set<Asset> assets) {

        Set<AssetShortResponseDto> dtoSet = new HashSet<>();

        for (Asset assoc : assets) {
            AssetShortResponseDto assocDto = new AssetShortResponseDto(
                    assoc.getId(),
                    assoc.getName(),
                    assoc.getAssetCode()
            );
            dtoSet.add(assocDto);
        }

        return dtoSet;
    }

    private AssetGetOneResponseDto mapAssetToAssetGetOneResponseDto(Asset asset) {
        if (asset == null) {
            return null;
        }

        return new AssetGetOneResponseDto(
                asset.getId(),
                asset.getAssetCode(),
                asset.getAssetTypeId(),
                asset.getAssetGroupId(),
                asset.getName(),
                asset.getUnit(),
                asset.getNote(),
                mapAssetSetToAssetShortResponseDtoSet(asset.getAssociatedAsset()),
                asset.getOfficeSiteId(),
                mapUserEntityToShortResponseDto(asset.getPic()),
                asset.getManufacturerId(),
                asset.getSupplierId(),
                asset.getPrice(),
                asset.getPurchaseDate(),
                asset.getWarrantyInMonth(),
                asset.getAssetStatusId(),
                asset.getCiaC(),
                asset.getCiaI(),
                asset.getCiaA(),
                asset.getCiaSum(),
                asset.getCiaNote(),
                asset.getCiaImportance(),
                mapUserEntityToShortResponseDto(asset.getOwner()),
                asset.getAssignDateStart(),
                asset.getAssignDateEnd(),
                asset.isOverdue()
        );
    }

}
