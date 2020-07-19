package application.repository.impl;

import application.model.entity.Asset;
import application.model.response.AssetGetResponse;
import application.repository.AssetRepositoryCustom;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetRepositoryCustomImpl implements AssetRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int countByAssetGroup(String groupId) {

        Criteria criteria = Criteria.where("assetGroupId").is(groupId);

        return (int) mongoTemplate.count(Query.query(criteria), Asset.class);


    }

    @Override
    public int getHighestIdByAssetGroup(String groupId) {
        return 0;
    }

    @Override
    public int getHighestId() {
        return 0;
    }

    @Override
    public List<AssetGetResponse> findAllAssetGetResponse() {

        ProjectionOperation projection1 = Aggregation.project(
                "assetCode",
                "assetTypeId",
                "assetGroupId",
                "name",
                "unit",
                "note",
                "associatedAsset",
                "officeSiteId",
                "manufacturerId",
                "supplierId",
                "price",
                "purchaseDate",
                "warrantyInMonth",
                "assetStatusId",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "ciaImportance",
                "ciaNote",
                "owner",
                "assignDateStart",
                "assignDateEnd",
                "overdue")
                .andArrayOf(new Document("$toObjectId", "$pic")).as("picId");
        LookupOperation lookupPic = Aggregation.lookup("users", "picId", "_id", "picUser");
        LookupOperation lookupType = Aggregation.lookup("assettypes", "assetTypeId", "_id", "assetType");
        LookupOperation lookupGroup = Aggregation.lookup("assetgroups", "assetGroupId", "_id", "assetGroup");
        LookupOperation lookupStatus = Aggregation.lookup("assetstatus", "assetStatusId", "_id", "assetStatus");
        LookupOperation lookupOffice = Aggregation.lookup("officesites", "officeSiteId", "_id", "officeSite");
        LookupOperation lookupManufacturer = Aggregation.lookup("manufacturers", "manufacturerId", "_id", "manufacturer");
        LookupOperation lookupSupplier = Aggregation.lookup("suppliers", "supplierId", "_id", "supplier");
        ProjectionOperation projection2 = Aggregation.project(
                "assetCode",
                "name",
                "unit",
                "note",
                "associatedAsset",
                "price",
                "purchaseDate",
                "warrantyInMonth",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "ciaImportance",
                "ciaNote",
                "owner",
                "assignDateStart",
                "assignDateEnd",
                "overdue")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$picUser", 0))).as("picUser")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$assetType", 0))).as("assetTypeA")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$assetGroup", 0))).as("assetGroupA")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$assetStatus", 0))).as("assetStatusA")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$manufacturer", 0))).as("manufacturerA")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$supplier", 0))).as("supplierA")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$officeSite", 0))).as("officeSiteA");
        ProjectionOperation projection3 = Aggregation.project(
                "assetCode",
                "name",
                "unit",
                "note",
                "associatedAsset",
                "price",
                "purchaseDate",
                "warrantyInMonth",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "ciaImportance",
                "ciaNote",
                "owner",
                "assignDateStart",
                "assignDateEnd",
                "overdue")
                .and("$picUser.firstName").as("pic")
                .and("$assetTypeA.name").as("assetType")
                .and("$assetGroupA.name").as("assetGroup")
                .and("$assetStatusA.name").as("assetStatus")
                .and("$manufacturerA.name").as("manufacturer")
                .and("$supplierA.name").as("supplier")
                .and("$officeSiteA.name").as("officeSite");

        Aggregation aggregation = Aggregation.newAggregation(
                projection1
                ,lookupPic, lookupType, lookupGroup, lookupStatus, lookupOffice, lookupManufacturer, lookupSupplier
                ,projection2
                ,projection3
        );
        AggregationResults<AssetGetResponse> results = mongoTemplate.aggregate(aggregation, "assets", AssetGetResponse.class);

        List<AssetGetResponse> responses = new ArrayList<>();

        for (AssetGetResponse result : results) {
            responses.add(result);
        }
        return responses;

    }
}
