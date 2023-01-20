package com.example.Personal.Data.Management.repositories;


import com.example.Personal.Data.Management.models.UserRegCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRegCountRepo extends JpaRepository<UserRegCount, Long> {

    Optional<UserRegCount> findByDate(LocalDate date);
}
