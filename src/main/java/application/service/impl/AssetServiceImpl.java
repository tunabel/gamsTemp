package application.service.impl;

import application.controller.exception.ItemNotFoundException;
import application.model.entity.*;
import application.model.requestdto.AssetCreateRequestDto;
import application.model.requestdto.AssetUpdateRequestDto;
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
    public AssetGetOneResponseDto insert(AssetCreateRequestDto request) {

        User pic = userRepository.findActiveAdminById(request.getPic());

        if (pic == null) {
            throw new ItemNotFoundException("User not authorized to create asset");
        }

        Optional<AssetType> assetTypeOptional = assetTypeRepository.findById(request.getAssetTypeId());
        if (!assetTypeOptional.isPresent()) {
            throw new ItemNotFoundException("Asset Type not found");
        }

        Optional<AssetGroup> assetGroupOptional = assetGroupRepository.findById(request.getAssetGroupId());
        if (!assetGroupOptional.isPresent()) {
            throw new ItemNotFoundException("Asset Group not found");
        }

        Optional<OfficeSite> officeSiteOptional = officeSiteRepository.findById(request.getOfficeSiteId());
        if (!officeSiteOptional.isPresent()) {
            throw new ItemNotFoundException("Office Site not found");
        }

        Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(request.getManufacturerId());
        if (!manufacturerOptional.isPresent()) {
            throw new ItemNotFoundException("Manufacturer not found");
        }

        Optional<Supplier> supplierOptional = supplierRepository.findById(request.getSupplierId());
        if (!supplierOptional.isPresent()) {
            throw new ItemNotFoundException("Supplier not found");
        }

        String year = request.getPurchaseDate().substring(0, 4);
        String group = assetGroupOptional.get().getAbbreviation();
        String countGroup = String.format("%03d", createHighestIdByFieldOrAll(request.getAssetGroupId()));
        String assetCode = year.concat(group).concat(countGroup);

        Asset asset = new Asset();

        asset.setId(String.valueOf(createHighestIdByFieldOrAll(null)));
        asset.setAssetTypeId(request.getAssetTypeId());
        asset.setAssetGroupId(request.getAssetGroupId());
        asset.setAssociatedAsset(new HashSet<>());
        asset.setAssetCode(assetCode);
        asset.setName(request.getName());
        asset.setUnit(request.getUnit());
        asset.setNote(request.getNote());
        asset.setOfficeSiteId(request.getAssetTypeId());
        asset.setPic(pic);
        asset.setManufacturerId(request.getManufacturerId());
        asset.setSupplierId(request.getSupplierId());
        asset.setPrice(request.getPrice());
        asset.setPurchaseDate(LocalDate.parse(request.getPurchaseDate()));
        asset.setWarrantyInMonth(request.getWarrantyInMonth());
        asset.setOwner(null);
        asset.setAssetStatusId("0");
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

        Asset result = assetRepository.insert(asset);

        return mapAssetToAssetGetOneResponseDto(result);
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

    @Override
    public Asset update(AssetUpdateRequestDto requestDto) {


        return null;
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
                asset.getOfficeSiteId(),
                mapUserEntityToShortResponseDto(asset.getPic()),
                asset.getManufacturerId(),
                asset.getSupplierId(),
                asset.getPrice(),
                asset.getPurchaseDate(),
                asset.getWarrantyInMonth(),
                asset.getAssetStatusId()
        );
    }

}
