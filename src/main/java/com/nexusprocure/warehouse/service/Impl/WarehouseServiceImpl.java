package com.nexusprocure.warehouse.service.Impl;

import com.nexusprocure.exception.DuplicateResourceException;
import com.nexusprocure.exception.InvalidWarehouseStateException;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import com.nexusprocure.warehouse.dto.Filter.WarehouseFilterRequest;
import com.nexusprocure.warehouse.dto.Request.WarehouseRequest;
import com.nexusprocure.warehouse.dto.Response.WarehouseResponse;
import com.nexusprocure.warehouse.dto.Request.WarehouseUpdateRequest;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.entity.WarehouseStatus;
import com.nexusprocure.warehouse.mapper.WarehouseMapper;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import com.nexusprocure.warehouse.service.WarehouseService;
import com.nexusprocure.warehouse.specification.WarehouseSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    @PreAuthorize("@warehouseSecurity.canManageWarehouse()")
    public WarehouseResponse createWarehouse(WarehouseRequest request) {

        // Check Duplicate WareHouseEmail
        if (warehouseRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Warehouse email already exists: " + request.getEmail());
        }
        //Find warehouseManager
        User manager = userRepository.findById(request.getManagerId()).orElseThrow(() ->
                new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
        //Convert DTO into entity
        Warehouse warehouse = warehouseMapper.toEntity(request);
        //Generate Warehouse Code
        warehouse.assignWarehouseCode(generateWarehouseCode());
        //Assign Manager
        warehouse.assign(manager);
        //Activate Warehouse
        warehouse.activate();
        //Save Entity
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        //Convert entity to DTO response
        return warehouseMapper.toResponse(savedWarehouse);
    }


    @Override
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Warehouse not found with id: " + id));
        return warehouseMapper.toResponse(warehouse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponse> getAllWarehouses(Pageable pageable, @ModelAttribute WarehouseFilterRequest request){
        Specification<Warehouse> specification = WarehouseSpecificationBuilder.build(request);
        Page<Warehouse> warehouses = warehouseRepository.findAll(pageable);
        return warehouses.map(warehouseMapper::toResponse);
    }
    @Override
    @PreAuthorize("@warehouseSecurity.canManageWarehouse()")
    public  WarehouseResponse updateWarehouse(Long id, WarehouseUpdateRequest request) {
        // find existing warehouse
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Warehouse not found with id: " + id
                )
        );
        //check duplicate email only if email is checked
        if (!warehouse.getEmail().equals(request.getEmail()) && warehouseRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Warehouse email already exists: "
                            + request.getEmail()
            );
        }
        // Find manager
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Manager not found with id: "
                                        + request.getManagerId()
                        )
                );
        //updating warehouse Details
        warehouse.updateDetails(request.getName(),
                request.getAddress(),
                request.getCapacity(),
                request.getContactNumber(),
                request.getEmail(),
                request.getDescription());
        //update manager
        warehouse.assign(manager);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(updatedWarehouse);
    }
    @Override
    @PreAuthorize("@warehouseSecurity.canManageWarehouse()")
    public WarehouseResponse deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Warehouse not found with id: " + id
                )
        );
        if (warehouse.getStatus() == WarehouseStatus.INACTIVE) {
            throw new InvalidWarehouseStateException(
                    "Warehouse is already inactive."
            );
        }
        warehouse.deactivate();
        Warehouse deletedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponse(deletedWarehouse);
    }
    private String generateWarehouseCode(){
        Optional<Warehouse> latestWarehouse = warehouseRepository.findTopByOrderByIdDesc();
        // First Warehouse
        if(latestWarehouse.isEmpty()){
            return "WH-00001";
        }
        String lastCode = latestWarehouse.get().getWarehouseCode();
        // Remove WH
        String numericPart = lastCode.substring(3);
        //Increment number
        int nextNumber = Integer.parseInt(numericPart)+1;
        // Format WH-0001
        return String.format("WH-%05d", nextNumber);
    }

}





