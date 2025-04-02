package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.Address;
import com.fpoly.duan.shopdientuv2.entitys.Account;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByAccount(Account account);
}
