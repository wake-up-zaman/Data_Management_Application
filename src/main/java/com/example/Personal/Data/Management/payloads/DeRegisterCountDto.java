package com.example.Personal.Data.Management.payloads;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeRegisterCountDto {

    private LocalDate date;
    private Long count;

}
