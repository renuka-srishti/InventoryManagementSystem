package com.shopify.ims.service;

import com.shopify.ims.exception.GroupNotExistException;
import com.shopify.ims.model.Group;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.repository.GroupRepository;
import com.shopify.ims.repository.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    private Group testGroup;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        testGroup = new Group();
        testGroup.setName("test group");

        testInventory = new Inventory();
        testInventory.setName("test inventory");

        testGroup.getInventories().add(testInventory);

        testInventory.getGroups().add(testGroup);
    }

    @Test
    void getAllGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(testGroup);

        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> returnedGroups = groupService.getAllGroups();

        assertEquals(returnedGroups.size(), 1);
        assertEquals(returnedGroups.get(0).getName(), testGroup.getName());
    }

    @Test
    void createGroup() {
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        Group returnedGroup = groupService.createGroup(testGroup.getName());

        assertEquals(testGroup.getName(), returnedGroup.getName());
    }

    @Test
    void assignGroupToInventories() {
        when(groupRepository.existsById(1L)).thenReturn(true);
        when(groupRepository.getById(1L)).thenReturn(testGroup);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        groupService.assignGroupsToInventories(Collections.singletonList(1L), Collections.singletonList(1L));

        verify(inventoryRepository, times(1)).save(testInventory);
    }

    @Test
    void delete() {
        when(groupRepository.existsById(1L)).thenReturn(true);
        when(groupRepository.getById(1L)).thenReturn(testGroup);

        groupService.delete(1L);

        verify(groupRepository, times(1)).delete(testGroup);
    }

    @Test
    void delete_throwsGroupNotExistException() {
        when(groupRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(GroupNotExistException.class, () -> groupService.delete(1L));
    }

    @Test
    void getInventoriesByGroup() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));

        List<Inventory> returnedInventories = groupService.getInventoriesByGroup(1L);

        assertEquals(returnedInventories.size(), 1);
        assertEquals(returnedInventories.get(0).getName(), testInventory.getName());
    }

    @Test
    void getInventoriesByGroup_returnsEmpty() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        List<Inventory> returnedInventories = groupService.getInventoriesByGroup(1L);

        assertEquals(returnedInventories.size(), 0);
    }

    @Test
    void removeInventoriesFromGroup_doesNothingBecauseEitherGroupOrInventoryDoesnotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        groupService.removeInventoriesFromGroup(Collections.singletonList(1L), Collections.singletonList(1L));

        verify(groupRepository, times(0)).save(testGroup);
    }

    @Test
    void removeInventoriesFromGroup() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        groupService.removeInventoriesFromGroup(Collections.singletonList(1L), Collections.singletonList(1L));

        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    void getGroupById() {
        when(groupRepository.existsById(1L)).thenReturn(true);

        groupService.getGroupById(1L);

        verify(groupRepository, times(1)).getById(1L);
    }

    @Test
    void getGroupById_throwsGroupNotExistException() {
        when(groupRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(GroupNotExistException.class, () -> groupService.getGroupById(1L));
    }
}