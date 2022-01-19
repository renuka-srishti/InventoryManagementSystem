package com.shopify.ims.service;

import com.shopify.ims.exception.GroupNotExistException;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.model.Group;
import com.shopify.ims.repository.GroupRepository;
import com.shopify.ims.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class GroupService {
    private final InventoryRepository inventoryRepository;
    private final GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group createGroup(String name) {
        Group group = new Group();
        group.setName(name);
        return groupRepository.save(group);
    }

    public void assignGroupsToInventories(List<Long> groupIds, List<Long> inventoryIds) {
        for (Long groupId : groupIds) {
            if (groupRepository.existsById(groupId)) {
                Group group = groupRepository.getById(groupId);

                for (Long id : inventoryIds) {
                    Optional<Inventory> inventory = inventoryRepository.findById(id);

                    if (inventory.isPresent()) {
                        inventory.get().getGroups().add(group);
                        inventoryRepository.save(inventory.get());
                    }
                }
            }
        }
    }

    public void delete(long groupId) {
        if (groupRepository.existsById(groupId)) {
            groupRepository.delete(groupRepository.getById(groupId));
            log.info(String.format("Deleted GroupId: %d", groupId));
            return;
        }
        throw new GroupNotExistException(groupId);
    }

    public List<Inventory> getInventoriesByGroup(long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(group.get().getInventories());
    }

    public void removeInventoriesFromGroup(List<Long> groupIds, List<Long> inventoryIds) {
        for (Long groupId : groupIds) {
            for (Long inventoryId : inventoryIds) {
                Optional<Group> group = groupRepository.findById(groupId);
                Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);

                if (group.isEmpty() || inventory.isEmpty()) {
                    return;
                }

                group.get().getInventories().remove(inventory.get());
                groupRepository.save(group.get());
            }
        }
    }

    public Group getGroupById(long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotExistException(groupId);
        }
        return groupRepository.getById(groupId);
    }
}
