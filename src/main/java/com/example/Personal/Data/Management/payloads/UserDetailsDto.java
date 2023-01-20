package com.example.Personal.Data.Management.payloads;

import com.example.Personal.Data.Management.models.BankInfo;
import com.example.Personal.Data.Management.models.ProjectInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    private String id;
    @NotEmpty
    @Size(min=1, message = "Username must be min of 4 characters")
    private String name;
    private String fatherName;
    @Email(message = "Your email address is not valid")
    private String email;
    private String DOB;
    private String currentAddress;
    private String nationality;
    private List<BankInfo> bankInfo;
    private List<ProjectInfo> projectInfo;

}
