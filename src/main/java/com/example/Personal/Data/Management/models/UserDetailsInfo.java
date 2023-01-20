package com.example.Personal.Data.Management.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsInfo {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String fatherName;
    private String email;

    private String DOB;
    private String currentAddress;
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="userDetailsInfo_id")
    private List<BankInfo> bankInfo=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="userDetailsInfo_id")
    private List<ProjectInfo> projectInfo=new ArrayList<>();
}
