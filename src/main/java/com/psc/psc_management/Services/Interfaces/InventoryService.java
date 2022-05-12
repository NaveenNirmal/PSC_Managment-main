package com.psc.psc_management.Services.Interfaces;

import java.util.Collection;

import com.psc.psc_management.Models.Inventorys;
import com.psc.psc_management.Models.IssuePaddy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InventoryService extends CrudRepository<Inventorys, Integer> {

    // @Query("SELECT p FROM buy_paddy p WHERE p.status = 'Request'")
    // Iterable<IssuePaddy> findPendingReq();

}
