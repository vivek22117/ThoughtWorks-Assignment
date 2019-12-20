package com.ddsolutions.iot.equipment.data.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.ddsolutions.iot.equipment.data.domain.EquipmentRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Component
public class DynamoDBOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBOperation.class);
    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public DynamoDBOperation(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public boolean save(EquipmentRecord equipmentRecord) {
        try {
            dynamoDBMapper.save(equipmentRecord);
            return true;
        } catch (Exception ex) {
            LOGGER.error("DynamoDB persistence failed...");
            return false;
        }
    }

    public List<EquipmentRecord> getRecordsBasedOnCount(Integer count) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withLimit(count)
                .withConsistentRead(false);


        ScanResultPage<EquipmentRecord> equipmentRecords =
                dynamoDBMapper.scanPage(EquipmentRecord.class, scanExpression);
        return equipmentRecords.getResults().stream()
                .sorted(comparing(EquipmentRecord::getContractStartDate))
                .collect(toList());
    }

    public EquipmentRecord getRecordBasedOnEquipmentId(Integer equipmentId) {

        EquipmentRecord equipmentRecord =
                dynamoDBMapper.load(EquipmentRecord.class, equipmentId);

        return equipmentRecord;
    }
}
