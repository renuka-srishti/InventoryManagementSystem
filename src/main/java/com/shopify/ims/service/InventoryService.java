package com.shopify.ims.service;

import com.shopify.ims.exception.InventoryNotExistException;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.model.Group;
import com.shopify.ims.repository.InventoryRepository;
import com.shopify.ims.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final GroupRepository groupRepository;

    public List<Inventory> getInventoryList() {
        return inventoryRepository.findAll();
    }

    public void delete(List<Long> inventoryIds) {
        for (long inventoryId : inventoryIds) {
            if (inventoryRepository.existsById(inventoryId)) {
                inventoryRepository.delete(inventoryRepository.getById(inventoryId));
                log.info(String.format("Deleted inventory with id: %d", inventoryId));
            }
        }
    }

    public void add(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public void update(Long inventoryId, Inventory inventory) {
        if (inventoryRepository.existsById(inventoryId)) {
            Inventory oldInventory = inventoryRepository.getById(inventoryId);
            oldInventory.setName(inventory.getName());
            oldInventory.setDescription(inventory.getDescription());
            oldInventory.setQuantity(inventory.getQuantity());
            oldInventory.setPricePerItem(inventory.getPricePerItem());

            inventoryRepository.save(oldInventory);
            log.info(String.format("Updated inventory with id: %d", inventoryId));
        }
    }

    public void assignInventoryToGroups(Long inventoryId, List<Long> groupIds) {
        if (inventoryRepository.existsById(inventoryId)) {
            Inventory inventory = inventoryRepository.getById(inventoryId);

            for (Long id : groupIds) {
                Optional<Group> group = groupRepository.findById(id);
                if (group.isPresent()) {
                    group.get().getInventories().add(inventory);
                    groupRepository.save(group.get());
                }
            }
        }
    }

    public Inventory getInventoryById(long inventoryId) {
        if(!inventoryRepository.existsById(inventoryId)) {
            throw new InventoryNotExistException(inventoryId);
        }

        return inventoryRepository.findById(inventoryId).get();
    }

    public List<Group> getGroupsByInventoryId(long inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new InventoryNotExistException(inventoryId);
        }

        return new ArrayList<>(inventoryRepository.findById(inventoryId).get().getGroups());
    }
}
