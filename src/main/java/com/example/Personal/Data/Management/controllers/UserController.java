package com.example.Personal.Data.Management.controllers;


import com.example.Personal.Data.Management.exceptions.ApiException;
import com.example.Personal.Data.Management.models.UserDetailsInfo;
import com.example.Personal.Data.Management.payloads.*;
import com.example.Personal.Data.Management.repositories.UserDetailsRepository;
import com.example.Personal.Data.Management.services.UserService;
import com.example.Personal.Data.Management.utils.UserIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    //User Controller

    //DELETE
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @RequestMapping(value={"/users"},method=RequestMethod.DELETE)
    public ResponseEntity<ApiResponse> deRegisterUser() throws InterruptedException {
        String userId = UserIdUtil.getUserId();
        userService.deRegisterUser(userId);
        return new ResponseEntity(new ApiResponse("User Deleted Successfully!"),HttpStatus.OK);
    }

    //GET All
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userAll"},method=RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(value="pageNumber", defaultValue = "1",required = false) Integer pageNumber,
            @RequestParam(value="pageSize",defaultValue = "3",required = false) Integer pageSize
    ){
        return ResponseEntity.ok(userService.getAllUsers(pageNumber,pageSize));
    }


    //UserDetails Controller

    //POST
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @RequestMapping(value={"/userDetails"},method= RequestMethod.POST)
    public ResponseEntity<UserDetailsDto> createUserDetails(@RequestBody UserDetailsDto userDetailsDto) throws ApiException {

        String UserId = UserIdUtil.getUserId();
        System.out.println("From Spring Security: "+UserId);

        Optional<UserDetailsInfo> userDetailsInfo2=this.userDetailsRepository.findById(UserId);
        if (userDetailsInfo2.isPresent()) {
            return new ResponseEntity(new ApiResponse("You have already created user details information"),HttpStatus.BAD_REQUEST);
        }
        else{
            UserDetailsDto createUserDetailsDto = userService.createUserDetails(userDetailsDto);
            return new ResponseEntity<>(createUserDetailsDto, HttpStatus.CREATED);
        }

    }

    //GET
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @RequestMapping(value={"/userDetails"},method=RequestMethod.GET)
    public ResponseEntity<UserDetailsDto> getUserDetailsById(){
        return ResponseEntity.ok(userService.getUserDetailsById());
    }


    //GET All
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/userDetailsAll"},method=RequestMethod.GET)
    public ResponseEntity<List<UserDetailsDto>> getAllUserDetails(){
        return ResponseEntity.ok(userService.getAllUserDetails());
    }


    //GET Day Wise User Registration Number

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/registrationCount"},method=RequestMethod.GET)
    public ResponseEntity<List<UserRegCountDto>> getRegistrationCountNumber(
            @RequestParam(value="pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam(value="pageSize",defaultValue = "1",required = false) Integer pageSize
    ){
        return ResponseEntity.ok(userService.getUserList(pageNumber,pageSize));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/accountDeletionCount"},method=RequestMethod.GET)
    public ResponseEntity<List<DeleteAccountCountDto>> getDeleteCountNumber(
            @RequestParam(value="pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam(value="pageSize",defaultValue = "1",required = false) Integer pageSize
    ){
        return ResponseEntity.ok(userService.getDeletionList(pageNumber,pageSize));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/deRegistrationCount"},method=RequestMethod.GET)
    public ResponseEntity<List<DeRegisterCountDto>> getDeRegCountNumber(
            @RequestParam(value="pageNumber", defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam(value="pageSize",defaultValue = "1",required = false) Integer pageSize
    ){
        return ResponseEntity.ok(userService.getDeRegistrationList(pageNumber,pageSize));
    }






    //User Controller

//    //PUT
//    @PreAuthorize("hasRole('ROLE_NORMAL')")
//    @RequestMapping(value={"/users/{userId}"},method=RequestMethod.PUT)
//    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,@Valid @RequestBody UserDto userDto){
//        UserDto updateUserDto = userService.updateUser(userDto,userId);
//        return ResponseEntity.ok(updateUserDto);
//    }
//

//    //GET
//    @PreAuthorize("hasRole('ROLE_NORMAL')")
//    @RequestMapping(value={"/users/{userId}"},method=RequestMethod.GET)
//    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
//        return ResponseEntity.ok(userService.getUserById(userId));
//    }



//     UserDetails Controller

//    //PUT
//    @PreAuthorize("hasRole('ROLE_NORMAL')")
//    @RequestMapping(value={"/usersDetails/{userId}"},method=RequestMethod.PUT)
//    public ResponseEntity<UserDetailsDto> updateUserDetails(@PathVariable Long userDetailsId, @Valid @RequestBody UserDetailsDto userDto){
//        UserDetailsDto updateUserDetailsDto = userService.updateUserDetails(userDto,userDetailsId);
//        return ResponseEntity.ok(updateUserDetailsDto);
//    }
//
//    //DELETE
//    @PreAuthorize("hasRole('ROLE_NORMAL')")
//    @RequestMapping(value={"/userDetails/{userId}"},method=RequestMethod.DELETE)
//    public ResponseEntity<ApiResponse> deleteUserDetails(@PathVariable Long userId){
//        userService.deleteUserDetails(userId);
//        return new ResponseEntity(new ApiResponse("User Information Deleted Successfully", true),HttpStatus.OK);
//    }
//



}






