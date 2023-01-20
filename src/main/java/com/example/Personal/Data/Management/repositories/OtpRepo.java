package com.example.Personal.Data.Management.repositories;



import com.example.Personal.Data.Management.models.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OtpRepo extends JpaRepository<Otp, String> {
//    Optional<Otp> findByOtp(String phoneNumber);
    Optional<Otp> findByOtp(String otp);

}
