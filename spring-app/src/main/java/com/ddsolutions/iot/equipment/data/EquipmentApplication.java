package com.ddsolutions.iot.equipment.data;

import com.ddsolutions.iot.equipment.data.domain.EquipmentRecord;
import com.ddsolutions.iot.equipment.data.service.EquipmentService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class EquipmentApplication implements CommandLineRunner {

    @Autowired
    private EquipmentService equipmentService;

    public static void main(String[] args) {
        SpringApplication.run(EquipmentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<EquipmentRecord> equipmentRecords = EquipmentFromFile.importEquipments();
        equipmentRecords.parallelStream().forEach(record -> {
            Random r = new Random();
            record.setEquipmentId(r.nextInt(1000));
        });
        equipmentRecords.parallelStream().forEach(record -> equipmentService.saveDummyData(record));
    }

    static class EquipmentFromFile {
        static List<EquipmentRecord> importEquipments() throws IOException {
            return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                    .readValue(EquipmentFromFile.class.getResourceAsStream("/static_equipment_data.json"), new TypeReference<List<EquipmentRecord>>() {
                    });
        }
    }
}
