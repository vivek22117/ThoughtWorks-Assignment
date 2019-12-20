package com.ddsolutions.iot.equipment.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ddsolutions.iot.equipment.data.db.DynamoDBOperation;
import com.ddsolutions.iot.equipment.data.domain.EquipmentRecord;
import com.ddsolutions.iot.equipment.data.exception.ApplicationException;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private DynamoDBOperation dynamoDBOperation;

    @Autowired
    public EquipmentServiceImpl(DynamoDBOperation dynamoDBOperation) {
        this.dynamoDBOperation = dynamoDBOperation;
    }

    @Override
    public List<EquipmentRecord> getEquipmentByCount(Integer count) {
        return dynamoDBOperation.getRecordsBasedOnCount(count);
    }

    @Override
    public boolean save(EquipmentRecord record) {
        EquipmentRecord duplicateRecord =
                dynamoDBOperation.getRecordBasedOnEquipmentId(record.getEquipmentId());
        if (!Objects.isNull(duplicateRecord)) {
            throw new ApplicationException("Equipment with Id " + record.getEquipmentId() + "" +
                    " is already available");
        }
        return dynamoDBOperation.save(record);
    }

    @Override
    public void saveDummyData(EquipmentRecord record) {
        dynamoDBOperation.save(record);
    }

    @Override
    public Optional<EquipmentRecord> getByEquipmentId(Integer id) {
        EquipmentRecord record = dynamoDBOperation.getRecordBasedOnEquipmentId(id);
        if (Objects.isNull(record)) {
            return Optional.empty();
        }
        return Optional.of(record);
    }
}
