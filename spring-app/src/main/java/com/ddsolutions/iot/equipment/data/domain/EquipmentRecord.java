package com.ddsolutions.iot.equipment.data.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "EquipmentRecord")
public class EquipmentRecord {

    @DynamoDBHashKey(attributeName = "equipmentId")
    private Integer equipmentId;

    @DynamoDBTypeConverted(converter = AddressConverter.class)
    @DynamoDBAttribute(attributeName = "address")
    private Address address;

    @DynamoDBAttribute(attributeName = "contractStartDate")
    private String contractStartDate;

    @DynamoDBAttribute(attributeName = "contractEndDate")
    private String contractEndDate;

    @DynamoDBAttribute(attributeName = "status")
    private String status;

    public EquipmentRecord() {
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EquipmentRecord{" +
                "equipmentId=" + equipmentId +
                ", address='" + address + '\'' +
                ", contractStartDate='" + contractStartDate + '\'' +
                ", contractEndDate='" + contractEndDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
