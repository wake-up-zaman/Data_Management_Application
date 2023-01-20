package com.example.Personal.Data.Management.repositories;


import com.example.Personal.Data.Management.models.BankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankInfoRepository extends JpaRepository<BankInfo, Long> {
}
