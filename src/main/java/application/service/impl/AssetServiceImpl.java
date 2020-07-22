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
    public Asset insert(AssetCreateRequestDto request) {

        List<User> picList = userRepository.findActiveByFieldWithFixedValue("_id", request.getPic());

        if (picList.isEmpty()) {
            throw new ItemNotFoundException("Personnel In Charge's Id not found");
        }

        Asset asset = new Asset();

        //SET ID AS AUTO INCREMENTING -- NEED TO FIND HIGHEST NUMBER, NOT TOTAL COUNT
        asset.setId(String.valueOf(createHighestIdByField(null)));

        asset.setAssetTypeId(request.getAssetTypeId());
        asset.setAssetGroupId(request.getAssetGroupId());
        asset.setAssociatedAsset(new HashSet<>());
        asset.setName(request.getName());
        asset.setUnit(request.getUnit());
        asset.setNote(request.getNote());
        asset.setOfficeSiteId(request.getAssetTypeId());
        asset.setPic(picList.get(0));
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
        asset.setOwner(null);
        asset.setAssetStatusId("0");
        String year = request.getPurchaseDate().substring(0, 4);
        Optional<AssetGroup> assetGroupOptional = assetGroupRepository.findById(request.getAssetGroupId());

        if (assetGroupOptional.isPresent()) {
            String group = assetGroupOptional.get().getAbbreviation();

            String countGroup = String.format("%03d", createHighestIdByField(request.getAssetGroupId()));

            asset.setAssetCode(year.concat(group).concat(countGroup));
        } else {
            throw new ItemNotFoundException("Asset Group Id not found");
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
    public AssetGetOneResponseDto findById(String id) {

        if (assetRepository.existsById(id)) {
            Asset asset = assetRepository.findByIdCustom(id);
            Set<Asset> assocSet = asset.getAssociatedAsset();
            Set<AssetShortResponseDto> dtoSet = new HashSet<>();

            for (Asset assoc : assocSet) {
                AssetShortResponseDto assocDto = new AssetShortResponseDto(
                        assoc.getId(),
                        assoc.getName(),
                        assoc.getAssetCode()
                );
                dtoSet.add(assocDto);
            }

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
            throw new ItemNotFoundException("Submitted Asset Id not found");
        }
    }

    @Override
    public List<AssetGetAllResponseDto> findListByFields(String code, String name, String group, String owner) {

        List<AssetWithName> assetList = assetRepository.findListByFields(code, name, group, owner);

        return mapAssetWithNameListToGetAllResponseList(assetList);
    }

    @Override
    public List<AssetShortResponseDto> findAssocByName(String name) {

        List<Asset> assetList = assetRepository.findAssocByName(name);
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


    private int createHighestIdByField(String fieldId) {
        List<Asset> assetList = assetRepository.getSimpleListOfIdByAssetGroupIdOrAll(fieldId);
        int max = 0;

        for (Asset asset : assetList) {
            int id = Integer.parseInt(asset.getId());
            max = Math.max(id,max);
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

}
