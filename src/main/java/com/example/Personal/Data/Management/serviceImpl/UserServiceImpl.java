package com.example.Personal.Data.Management.serviceImpl;

import com.example.Personal.Data.Management.config.AppConstants;
import com.example.Personal.Data.Management.exceptions.ResourceNotFoundException;
import com.example.Personal.Data.Management.models.*;
import com.example.Personal.Data.Management.payloads.*;
import com.example.Personal.Data.Management.repositories.*;
import com.example.Personal.Data.Management.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRegCountRepo userRegCountRepo;

    @Autowired
    private DeRegisterCountRepository deRegisterCountRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private DeleteAccountCountRepository deleteAccountCountRepository;

    @Autowired
    private WhiteTokenRepo whiteTokenRepo;

    //User Services
    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(AppConstants.Constant_Pass));
        Role role = this.roleRepository.findById(AppConstants.ROLE_NORMAL).get();
        user.getRoles().add(role);
        User newUser = this.userRepository.save(user);

        //Day Wise Registration Count
        LocalDate localDate=LocalDate.now();
        Optional<UserRegCount> userRegCount=this.userRegCountRepo.findByDate(localDate);
        if (userRegCount.isPresent()) {
            var existingUserRegCount = userRegCount.get();
            Long c=userRegCount.get().getCount()+1;
            existingUserRegCount.setDate(localDate);
            existingUserRegCount.setCount(c);
            this.userRegCountRepo.save(existingUserRegCount);
        }
        else {
            UserRegCount userRegCount1=new UserRegCount();
            userRegCount1.setDate(localDate);
            userRegCount1.setCount(1l);
            this.userRegCountRepo.save(userRegCount1);
        }
        return this.modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public void deRegisterUser(String userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));

        //Day wise De Registration Count
        LocalDate localDateDeReg=LocalDate.now();
        Optional<DeRegisterCount> userDeRegCount=this.deRegisterCountRepository.findByDate(localDateDeReg);
        if (userDeRegCount.isPresent()) {
            var DeRegCount = userDeRegCount.get();
            Long c=userDeRegCount.get().getCount()+1;
            DeRegCount.setDate(localDateDeReg);
            DeRegCount.setCount(c);
            this.deRegisterCountRepository.save(DeRegCount);
        }
        else {
            DeRegisterCount userDeRegCount1=new DeRegisterCount();
            userDeRegCount1.setDate(localDateDeReg);
            userDeRegCount1.setCount(1l);
            this.deRegisterCountRepository.save(userDeRegCount1);
        }

//        Deletion of UserDetails
        UserDetailsInfo userDetailsInfo = userDetailsRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
        System.out.println(new Date());
        Thread.sleep(60000);
        System.out.println(new Date());
        userDetailsRepository.delete(userDetailsInfo);

        //Deletion of User Registration Info
        user.getRoles().removeAll(user.getRoles());
        userRepository.delete(user);

        //Day Wise Delete Count
        LocalDate localDateDelete=LocalDate.now();
        Optional<DeleteAccountCount> DeleteCount=this.deleteAccountCountRepository.findByDate(localDateDelete);
        if (DeleteCount.isPresent()) {
            var deleteCount = DeleteCount.get();
            Long c=DeleteCount.get().getCount()+1;
            System.out.println("De Count is: "+c);
            deleteCount.setDate(localDateDeReg);
            deleteCount.setCount(c);
            this.deleteAccountCountRepository.save(deleteCount);
        }
        else {
            DeleteAccountCount deleteAccountCount=new DeleteAccountCount();
            deleteAccountCount.setDate(localDateDeReg);
            deleteAccountCount.setCount(1l);
            this.deleteAccountCountRepository.save(deleteAccountCount);
        }
    }


    @Override
    public List<UserDto> getAllUsers(Integer pageNumber,Integer pageSize) {
        Pageable p=PageRequest.of(pageNumber,pageSize);
        Page<User> pagePost= userRepository.findAll(p);
        List<User> users = pagePost.getContent();
        List<UserDto> userDto = users.stream().map(user -> userToDto(user)).collect(Collectors.toList());
        return userDto;
    }


//User Details Service
    @Override
    public UserDetailsDto createUserDetails(UserDetailsDto userDetailsDto) {

        //Get UserName(Id) from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String UserId = authentication.getName();


        UserDetailsInfo userDetailsInfo=new UserDetailsInfo();
        userDetailsInfo.setId(UserId);
        userDetailsInfo.setName(userDetailsDto.getName());
        userDetailsInfo.setEmail(userDetailsDto.getEmail());
        userDetailsInfo.setFatherName(userDetailsDto.getFatherName());
        userDetailsInfo.setDOB(userDetailsDto.getDOB());
        userDetailsInfo.setCurrentAddress(userDetailsDto.getCurrentAddress());
        userDetailsInfo.setNationality(userDetailsDto.getNationality());
        userDetailsInfo.setBankInfo(userDetailsDto.getBankInfo());
        userDetailsInfo.setProjectInfo(userDetailsDto.getProjectInfo());
        UserDetailsInfo saveUserDetailsInfo = userDetailsRepository.save(userDetailsInfo);
        return userDetailsToDto(saveUserDetailsInfo);

    }


    @Override
    public UserDetailsDto getUserDetailsById() {

        //Get UserName(Id) from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDetailsInfo userDetailsInfo = userDetailsRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
        return userDetailsToDto(userDetailsInfo);
    }


    @Override
    public List<UserDetailsDto> getAllUserDetails() {
        List<UserDetailsInfo> userDetailInfos = userDetailsRepository.findAll();
        List<UserDetailsDto> userDetailsDto = userDetailInfos.stream().map(user -> userDetailsToDto(user)).collect(Collectors.toList());
        return userDetailsDto;
    }



    //Date Wise User Info
    @Override
    public List<UserRegCountDto> getUserList(Integer pageNumber, Integer pageSize) {
        Pageable p= PageRequest.of(pageNumber,pageSize);
        Page<UserRegCount> pagePost= userRegCountRepo.findAll(p);
        List<UserRegCount> users = pagePost.getContent();
        List<UserRegCountDto> userRegCountDto = users.stream().map(user -> registrationCountToDto(user)).collect(Collectors.toList());
        return userRegCountDto;
    }

    @Override
    public List<DeleteAccountCountDto> getDeletionList(Integer pageNumber, Integer pageSize) {
        Pageable p=PageRequest.of(pageNumber,pageSize);
        Page<DeleteAccountCount> pagePost= deleteAccountCountRepository.findAll(p);
        List<DeleteAccountCount> users = pagePost.getContent();
        List<DeleteAccountCountDto> userRegCountDto = users.stream().map(user -> deletionCountToDto(user)).collect(Collectors.toList());
        return userRegCountDto;
    }

    @Override
    public List<DeRegisterCountDto> getDeRegistrationList(Integer pageNumber, Integer pageSize) {
        Pageable p=PageRequest.of(pageNumber,pageSize);
        Page<DeRegisterCount> pagePost= deRegisterCountRepository.findAll(p);
        List<DeRegisterCount> users = pagePost.getContent();
        List<DeRegisterCountDto> userRegCountDto = users.stream().map(user -> deRegistrationCountToDto(user)).collect(Collectors.toList());
        return userRegCountDto;
    }


    //Token Remover
    @Override
    public void deleteWhiteToken(String token) {
        WhiteList whiteList = whiteTokenRepo.findByWhiteToken(token).orElseThrow(()->new ResourceNotFoundException("User", "id", "Ok"));
        whiteTokenRepo.delete(whiteList);
    }



    // Dto to Model & Model To Dto

    //User
    private User dtoToUser(UserDto userDto){
        User user = modelMapper.map(userDto,User.class);
        return user;
    }

    private UserDto userToDto(User user){
        UserDto userDto = modelMapper.map(user,UserDto.class);
        return userDto;
    }

    //UserDetails
    private UserDetailsDto userDetailsToDto(UserDetailsInfo userDetailsInfo){
        UserDetailsDto userDetailsDto = modelMapper.map(userDetailsInfo,UserDetailsDto.class);
        return userDetailsDto;
    }

    private UserDetailsInfo dtoToUserDetails(UserDetailsDto userDetailsDto){
        UserDetailsInfo userDetailsInfo = modelMapper.map(userDetailsDto, UserDetailsInfo.class);
        return userDetailsInfo;
    }

    // Date Wise Details
    private UserRegCountDto registrationCountToDto(UserRegCount userRegCount){
        UserRegCountDto userRegCountDto = modelMapper.map(userRegCount,UserRegCountDto.class);
        return userRegCountDto;
    }

    private DeleteAccountCountDto deletionCountToDto(DeleteAccountCount deleteAccountCount){
        DeleteAccountCountDto deleteAccountCountDto = modelMapper.map(deleteAccountCount,DeleteAccountCountDto.class);
        return deleteAccountCountDto;
    }

    private DeRegisterCountDto deRegistrationCountToDto(DeRegisterCount deRegisterCount){
        DeRegisterCountDto deRegisterCountDto = modelMapper.map(deRegisterCount,DeRegisterCountDto.class);
        return deRegisterCountDto;
    }














    //User Service

//    @Override
//    public UserDto updateUser(UserDto userDto, Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
////        user.setPhoneNumber(userDto.getPhoneNumber());
//        user.setPassword(AppConstants.Constant_Pass);
//        User userUpdate = userRepository.save(user);
//        return userToDto(userUpdate);
//    }
//
//    @Override
//    public UserDto getUserById(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
//        return userToDto(user);
//    }
//


//    //User Details Services

//    @Override
//    public UserDetailsDto updateUserDetails(UserDetailsDto userDetailsDto, Long userDetailsId) {
//        UserDetailsInfo userDetailsInfo = userDetailsRepository.findById(userDetailsId).orElseThrow(()->new ResourceNotFoundException("User", "id", userDetailsId));
////        userDetailsInfo.setId(userDetailsDto.getId());
////        userDetailsInfo.setName(userDetailsDto.getName());
////        userDetailsInfo.setFatherName(userDetailsDto.getFatherName());
////        userDetailsInfo.setDOB(userDetailsDto.getDOB());
////        userDetailsInfo.setEmail(userDetailsDto.getEmail());
////        userDetailsInfo.setPhoneNumber(userDetailsDto.getPhoneNumber());
////        userDetailsInfo.setCurrentAddress(userDetailsInfo.getCurrentAddress());
////        userDetailsInfo.setNationality(userDetailsDto.getNationality());
////        userDetailsInfo.setBankInfo(userDetailsDto.getBankInfo());
////        userDetailsInfo.setProjectInfo(userDetailsDto.getProjectInfo());
////        UserDetailsInfo userUpdate = userDetailsRepository.save(userDetailsInfo);
////        return userDetailsToDto(userUpdate);
//        return null;
//    }
//

//    @Override
//    public void deleteUserDetails(Long userDetailsId) {
//        UserDetailsInfo userDetailsInfo = userDetailsRepository.findById(userDetailsId).orElseThrow(()->new ResourceNotFoundException("User", "id", userDetailsId));
//        userDetailsRepository.delete(userDetailsInfo);
//    }


}