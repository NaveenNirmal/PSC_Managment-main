package com.psc.psc_management.Services.Interfaces;

import com.psc.psc_management.Models.Branches;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BranchService extends CrudRepository<Branches, Integer> {

    // @Query("SELECT SUM(generate_income) FROM paddy_management_system.branches;")
    // float findTotalIncome();

}