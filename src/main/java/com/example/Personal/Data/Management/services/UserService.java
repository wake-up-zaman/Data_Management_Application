package com.example.Personal.Data.Management.services;

import com.example.Personal.Data.Management.payloads.*;
import java.util.List;


public interface UserService {

    //user
    UserDto registerNewUser(UserDto user);

    void deRegisterUser(String userId) throws InterruptedException;

    List<UserDto> getAllUsers(Integer pageNumber,Integer pageSize);


    //userDetails
    UserDetailsDto createUserDetails(UserDetailsDto user);

    List< UserDetailsDto> getAllUserDetails();

    UserDetailsDto getUserDetailsById();

    //Date Wise User Info
    List<UserRegCountDto> getUserList(Integer pageNumber, Integer pageSize);

    List<DeRegisterCountDto> getDeRegistrationList(Integer pageNumber, Integer pageSize);

    List<DeleteAccountCountDto> getDeletionList(Integer pageNumber, Integer pageSize);


    //Token Deletion
    void deleteWhiteToken(String token);





    //User

//    UserDto updateUser(UserDto user, Long userId);
//
//    UserDto getUserById(Long userId);


    //UserDetails

//    UserDetailsDto updateUserDetails( UserDetailsDto user, Long userDetailsId);

//    void deleteUserDetails(Long userId);


}
