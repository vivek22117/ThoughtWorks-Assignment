package com.ddsolutions.iot.equipment.data.service;

import java.util.List;
import java.util.Optional;

import com.ddsolutions.iot.equipment.data.domain.EquipmentRecord;

public interface EquipmentService {

    List<EquipmentRecord> getEquipmentByCount(Integer count);

    boolean save(EquipmentRecord record);

    void saveDummyData(EquipmentRecord record);

    Optional<EquipmentRecord> getByEquipmentId(Integer id);
}
