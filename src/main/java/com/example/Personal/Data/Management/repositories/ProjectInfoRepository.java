package com.example.Personal.Data.Management.repositories;


import com.example.Personal.Data.Management.models.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {

}
