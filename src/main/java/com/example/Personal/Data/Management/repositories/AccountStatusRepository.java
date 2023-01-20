package com.example.Personal.Data.Management.repositories;

import com.example.Personal.Data.Management.models.AccountStatus;
import com.example.Personal.Data.Management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStatusRepository extends JpaRepository<AccountStatus, String> {
}
