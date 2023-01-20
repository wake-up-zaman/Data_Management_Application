package com.example.Personal.Data.Management.repositories;

import com.example.Personal.Data.Management.models.User;
import com.example.Personal.Data.Management.models.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WhiteTokenRepo extends JpaRepository<WhiteList, Long> {
    Optional<WhiteList> findByWhiteToken(String token);
}
