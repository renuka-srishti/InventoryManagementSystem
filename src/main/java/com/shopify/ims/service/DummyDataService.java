package com.shopify.ims.service;

import com.shopify.ims.model.Group;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.repository.GroupRepository;
import com.shopify.ims.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class DummyDataService {

    private final InventoryRepository inventoryRepository;
    private final GroupRepository groupRepository;

    /**
     * Creates a dummy data.
     */
    public void addDummyData() {
        List<Inventory> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Inventory in = new Inventory();
            in.setName("Dummy" + i);
            in.setDescription("Dummy Item blah blah");
            in.setQuantity(5);
            in.setPricePerItem(20.0);
            in = inventoryRepository.save(in);
            list.add(in);
        }

        for (int i = 0; i < 5; i++) {
            Group g = new Group();
            g.setName("Dummy" + i);

            if (i % 2 == 0) {
                g.getInventories().add(list.get(i));
            }

            groupRepository.save(g);
        }


    }
}
