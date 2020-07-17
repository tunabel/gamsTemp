package application.controller;

import application.model.entity.*;
import application.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/datasets")
public class DatasetController extends BaseController {
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


    @GetMapping(value = "/assets/types/")
    public List<AssetType> getAllAssetType() {
        return assetTypeRepository.findAll();
    }

    @GetMapping(value = "/assets/types/physical/")
    public List<AssetGroup> getAllPhysicalType() {
        return assetGroupRepository.findAllByAssetType(1);
    }

    @GetMapping(value = "/manufacturers/")
    public List<Manufacturer> getAllManufacturer() {
        return manufacturerRepository.findAll();
    }

    @GetMapping(value = "/suppliers/")
    public List<Supplier> getAllSupplier() {
        return supplierRepository.findAll();
    }

    @GetMapping(value = "/offices/")
    public List<OfficeSite> getAllOfficeSite() {
        return officeSiteRepository.findAll();
    }
}
