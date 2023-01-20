package com.example.Personal.Data.Management.repositories;


import com.example.Personal.Data.Management.models.DeRegisterCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DeRegisterCountRepository extends JpaRepository<DeRegisterCount, Long> {
    Optional<DeRegisterCount> findByDate(LocalDate date);

}
