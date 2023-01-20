package com.example.Personal.Data.Management.repositories;

import com.example.Personal.Data.Management.models.DeRegisterCount;
import com.example.Personal.Data.Management.models.DeleteAccountCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DeleteAccountCountRepository extends JpaRepository<DeleteAccountCount, Long> {
    Optional<DeleteAccountCount> findByDate(LocalDate date);
}
