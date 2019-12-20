package com.ddsolutions.iot.equipment.data.controller;

import com.ddsolutions.iot.equipment.data.domain.EquipmentRecord;
import com.ddsolutions.iot.equipment.data.exception.ApplicationException;
import com.ddsolutions.iot.equipment.data.service.EquipmentService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.lang.Integer.valueOf;

@RestController
@RequestMapping(path = "/equipment")
public class EquipmentDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDataController.class);

    private final EquipmentService equipmentService;
    private final Gson gson;

    @Autowired
    public EquipmentDataController(EquipmentService equipmentService, Gson gson) {
        this.equipmentService = equipmentService;
        this.gson = gson;
    }

    @ApiOperation(value = "Get list of equipments based on count ", response = EquipmentRecord.class)
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<?> getEquipmentData(@RequestParam(value = "limit") Integer limit) {
        String message = null;
        if (limit <= 0) {
            message = "Limit should be greater than 0";
            return new ResponseEntity<>(gson.toJson(message), HttpStatus.BAD_REQUEST);
        }

        List<EquipmentRecord> records = equipmentService.getEquipmentByCount(limit);

        if (records.isEmpty()) {
            message = "No records available";
            return new ResponseEntity<>(gson.toJson(message), HttpStatus.OK);
        }
        return ResponseEntity.ok(records);
    }

    @ApiOperation(value = "Get equipment details based on equipmentNumber ", response = EquipmentRecord.class)
    @GetMapping(value = "/{equipmentNumber}", produces = "application/json")
    public ResponseEntity<?> getEquipmentById(@PathVariable final String equipmentNumber) {
        String message = null;
        Optional<EquipmentRecord> record = equipmentService.getByEquipmentId(valueOf(equipmentNumber));

        if (!record.isPresent()) {
            message = "No equipment available with id: " + equipmentNumber;
            return new ResponseEntity<>(gson.toJson(message), HttpStatus.OK);
        }

        return ResponseEntity.ok(record.get());
    }

    @ApiOperation(value = "Creates a new equipment")
    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createEquipment(@RequestBody EquipmentRecord record) {
        String message = null;
        try {
            boolean saveStatus = equipmentService.save(record);
            if (saveStatus) {
                message = "Successfully saved equipment with Id " + record.getEquipmentId();
                return new ResponseEntity<>(gson.toJson(message), HttpStatus.CREATED);
            }
        } catch (ApplicationException ex) {
            LOGGER.error(ex.getMessage());
        }
        message = "Equipment with Id " + record.getEquipmentId() + " already exist!";
        return new ResponseEntity<>(gson.toJson(message), HttpStatus.CONFLICT);
    }
}
