package com.example.Personal.Data.Management.repositories;


import com.example.Personal.Data.Management.models.UserDetailsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsInfo, String> {

}
