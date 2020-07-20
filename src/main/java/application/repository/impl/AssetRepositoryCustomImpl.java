package application.repository.impl;

import application.model.entity.Asset;
import application.model.responsedto.AssetGetAllResponseDto;
import application.model.responsedto.AssetGetOneResponseDto;
import application.model.responsedto.AssetGetResponseDto;
import application.model.responsedto.AssociatedAssetGetResponseDto;
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
    public List<Asset> getSimpleListOfId(String id) {
        List<AggregationOperation> aggregationList = new ArrayList<>();
        if (id != null) {
            Criteria criteria = Criteria.where("assetGroupId").is(id);
            MatchOperation match1 = Aggregation.match(criteria);
            aggregationList.add(match1);
        }

        ProjectionOperation projection1 = Aggregation.project("_id");
        aggregationList.add(projection1);
        Aggregation aggregation = Aggregation.newAggregation(aggregationList);
        AggregationResults<Asset> results = mongoTemplate.aggregate(aggregation, "assets", Asset.class);
        return results.getMappedResults();
    }

    @Override
    public int countByAssetGroup(String groupId) {

        Criteria criteria = Criteria.where("assetGroupId").is(groupId);

        return (int) mongoTemplate.count(Query.query(criteria), Asset.class);
    }

    @Override
    public List<? extends AssetGetResponseDto> findAllAssetGetResponse() {
        List<AggregationOperation> aggregationOperationList = createBaseAssetAggregationList();
        ProjectionOperation projectionOperation = Aggregation.project(
                "assetCode",
                "name",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "owner",
                "overdue")
                .and("$assetTypeA.name").as("assetType")
                .and("$assetGroupA.name").as("assetGroup")
                .and("$assetStatusA.name").as("assetStatus")
                .and("$officeSiteA.name").as("officeSite");

        aggregationOperationList.add(projectionOperation);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperationList);

        AggregationResults<AssetGetAllResponseDto> results = mongoTemplate.aggregate(aggregation, "assets", AssetGetAllResponseDto.class);

        List<AssetGetAllResponseDto> responses = new ArrayList<>();

        for (AssetGetAllResponseDto result : results) {
            responses.add(result);
        }
        return responses;

    }

    private List<AggregationOperation> createBaseAssetAggregationList() {
        List<AggregationOperation> aggregationList = new ArrayList<>();

        ProjectionOperation projection1 = Aggregation.project(
                "assetCode",
                "assetTypeId",
                "assetGroupId",
                "name",
                "unit",
                "note",
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
        LookupOperation lookupAssoc = Aggregation.lookup("assets", "associatedAsset", "assetCode", "associatedAsset");
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


        aggregationList.addAll(
                Arrays.asList(
                        projection1
                        , lookupPic, lookupAssoc, lookupType, lookupGroup, lookupStatus, lookupOffice, lookupManufacturer, lookupSupplier
                        , projection2
                )
        );

        return aggregationList;
    }

    @Override
    public AssetGetOneResponseDto findAssetGetResponseById(String id) {

        MatchOperation match1 = Aggregation.match(Criteria.where("_id").is(id));
        List<AggregationOperation> aggregationList = createBaseAssetAggregationList();

        ProjectionOperation projectionOperation = Aggregation.project(
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
                "overdue"
        )
                .and("$picUser.firstName").as("pic")
                .and("$assetTypeA.value").as("assetType")
                .and("$assetGroupA.value").as("assetGroup")
                .and("$assetStatusA.value").as("assetStatus")
                .and("$manufacturerA.value").as("manufacturer")
                .and("$supplierA.value").as("supplier")
                .and("$officeSiteA.value").as("officeSite");

        aggregationList.add(0, match1);
        aggregationList.add(projectionOperation);
        Aggregation aggregation = Aggregation.newAggregation(aggregationList);
        AggregationResults<AssetGetOneResponseDto> results = mongoTemplate.aggregate(aggregation, "assets", AssetGetOneResponseDto.class);

        return results.getUniqueMappedResult();
    }

    @Override
    public List<AssociatedAssetGetResponseDto> findAssocByName(String req) {

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("assetGroupId").is("28"),
                Criteria.where("name").regex("^" + req, "i")
//                ,Criteria.where("assetStatus").is("0")
        );

        MatchOperation matchOperation = Aggregation.match(criteria);
        ProjectionOperation projectionOperation = Aggregation.project(
                "name", "assetCode"
        );

        AggregationResults<AssociatedAssetGetResponseDto> results = mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, projectionOperation), "assets", AssociatedAssetGetResponseDto.class);

        return results.getMappedResults();

    }
}
