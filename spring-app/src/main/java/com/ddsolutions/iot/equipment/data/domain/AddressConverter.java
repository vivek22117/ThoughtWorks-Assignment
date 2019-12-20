package com.ddsolutions.iot.equipment.data.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.ddsolutions.iot.equipment.data.utility.JsonUtility;

public class AddressConverter implements DynamoDBTypeConverter<String, Address> {
    private static final JsonUtility jsonUtil = new JsonUtility();

    @Override
    public String convert(Address object) {
        return jsonUtil.convertToString(object);
    }

    @Override
    public Address unconvert(String object) {
        return jsonUtil.convertFromJson(object, Address.class);
    }
}
