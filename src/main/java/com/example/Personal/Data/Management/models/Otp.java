package com.example.Personal.Data.Management.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "new_users_otp")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Otp {
    @Id
    private String id;
    private LocalDateTime expiresAt;
    private String otp;

}
