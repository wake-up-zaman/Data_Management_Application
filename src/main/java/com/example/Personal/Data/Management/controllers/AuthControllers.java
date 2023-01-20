package com.example.Personal.Data.Management.controllers;


import com.example.Personal.Data.Management.config.AppConstants;
import com.example.Personal.Data.Management.exceptions.ApiException;
import com.example.Personal.Data.Management.models.*;
import com.example.Personal.Data.Management.payloads.*;
import com.example.Personal.Data.Management.repositories.AccountStatusRepository;
import com.example.Personal.Data.Management.repositories.OtpRepo;
import com.example.Personal.Data.Management.repositories.UserRepository;
import com.example.Personal.Data.Management.repositories.WhiteTokenRepo;
import com.example.Personal.Data.Management.security.JwtTokenHelper;
import com.example.Personal.Data.Management.services.UserService;
import com.example.Personal.Data.Management.utils.GenerateOTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Service
@Component
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class AuthControllers {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManagers;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private WhiteTokenRepo whiteTokenRepo;

    @Autowired
    private AccountStatusRepository accountStatusRepository;

    Logger logger = LoggerFactory.getLogger(AuthControllers.class);

    //User Login1
    @RequestMapping(value={"/auth/login1"},method= RequestMethod.POST)
    public ResponseEntity<OtpResponse> createOTP(@RequestBody OtpRequest request) throws ApiException, InterruptedException {

        //authenticationOfPhoneNumber
        this.authenticateId(request.getId());

        //Checking Account Status
        Optional<AccountStatus> accountStatus=this.accountStatusRepository.findById(request.getId());
        LocalDateTime lastActiveDate=accountStatus.get().getDate();
        if(LocalDateTime.now().isBefore(lastActiveDate.plusMinutes(1)) && accountStatus.get().getStatus().equals(1)){

            //Generate OTP
            String otp=this.generateOTPMethod(request.getId());
            OtpResponse response = new OtpResponse();
            response.setOtp(otp);

            //Update Account Status
            LocalDateTime localDateTime=LocalDateTime.now();
            AccountStatus accountStatus2=new AccountStatus();
            accountStatus2.setId(accountStatus.get().getId());
            accountStatus2.setDate(localDateTime);
            accountStatus2.setStatus(1);
            this.accountStatusRepository.save(accountStatus2);

//            return new ResponseEntity<>(response, HttpStatus.OK);

            //Disable Account-Process-1
            Thread.sleep(2*60000);

            LocalDateTime localDateTime3=LocalDateTime.now();
            AccountStatus accountStatus3=new AccountStatus();
            accountStatus3.setId(accountStatus.get().getId());
            accountStatus3.setDate(localDateTime3);
            accountStatus3.setStatus(0);
            this.accountStatusRepository.save(accountStatus3);
            logger.info(otp);
            return new ResponseEntity(new ApiResponse("Your Account is disabled"),HttpStatus.OK);
        }
        else{

            //Disable Account-Process-2
//            LocalDateTime localDateTime2=LocalDateTime.now();
//            AccountStatus accountStatus3=new AccountStatus();
//            accountStatus3.setId(accountStatus.get().getId());
//            accountStatus3.setDate(localDateTime2);
//            accountStatus3.setStatus(0);
//            this.accountStatusRepository.save(accountStatus3);

            return new ResponseEntity(new ApiResponse("Your Account is disabled"),HttpStatus.OK);
        }
    }

    private void authenticateId(String id) {
        this.userRepository.findById(id).orElseThrow(()->new ApiException("Invalid Phone Number"));
    }

    public String generateOTPMethod(String id) {
        Optional<Otp> otp=this.otpRepo.findById(id);
        String OTP = GenerateOTPUtil.generateOTP();
        System.out.println(OTP);

        if (otp.isPresent()) {
            var existingOTPFor = otp.get();
            LocalDateTime localDateTime=existingOTPFor.getExpiresAt();
            if(localDateTime.plusMinutes(5l).isAfter(LocalDateTime.now())){
                existingOTPFor.setOtp(existingOTPFor.getOtp());
                System.out.println("I am running: 0");
                existingOTPFor.setExpiresAt(existingOTPFor.getExpiresAt());
                this.otpRepo.save(existingOTPFor);
                return existingOTPFor.getOtp();
            }
            else{
                existingOTPFor.setOtp(OTP);
                existingOTPFor.setExpiresAt(LocalDateTime.now().plusMinutes(5L));
                System.out.println("I am running: 1");
                this.otpRepo.save(existingOTPFor);
                return OTP;
            }
        }

        else {
            Optional<User> user = this.userRepository.findById(id);
            Otp newOTP = new Otp();
            newOTP.setId(user.get().getId());
            newOTP.setOtp(OTP);
            System.out.println("I am running: 2");
            newOTP.setExpiresAt(LocalDateTime.now().plusMinutes(5L));
            this.otpRepo.save(newOTP);
            return OTP;
        }
    }

    //User Login2
    @RequestMapping(value={"/auth/login2"},method= RequestMethod.POST)
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) throws Exception {

        Optional<Otp> otp=this.otpRepo.findByOtp(request.getOtp());
        String id=otp.get().getId();
        System.out.println("create token method: "+id);

        //OTP authentication
        String token=this.checkOTP(request.getOtp(),id, AppConstants.Constant_Pass);
        WhiteList whiteList=new WhiteList();
        whiteList.setWhiteToken(token);
        this.whiteTokenRepo.save(whiteList);

        TokenResponse response = new TokenResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String checkOTP(String otp,String username,String password) throws Exception {
        Optional<Otp> optionalOtp = this.otpRepo.findByOtp(otp);
        var existingOTP = optionalOtp.get();
        String OTP1=existingOTP.getOtp();
        String ID= existingOTP.getId();

        LocalDateTime localDateTime=existingOTP.getExpiresAt();
        System.out.println("OTP:"+OTP1+"  ID:"+ ID+" time:"+localDateTime);

        if (existingOTP.getOtp().equals(otp) && existingOTP.getExpiresAt().isAfter(LocalDateTime.now())) {
            this.authenticate(username,password);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            String token = this.jwtTokenHelper.generateToken(userDetails);
            return token;
        }
    return null;
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        try{
            this.authenticationManagers.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException ex){
            System.out.println("Invalid Details");
            throw new ApiException("Invalid username or Password !!");
        }
    }

    //User Registration
    @RequestMapping(value={"/auth/register"},method=RequestMethod.POST)
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        UserDto registeredUser = this.userService.registerNewUser(userDto);

        //Create Status
        LocalDateTime localDateTime=LocalDateTime.now();
        AccountStatus accountStatus=new AccountStatus();
        accountStatus.setId(userDto.getId());
        accountStatus.setDate(localDateTime);
        accountStatus.setStatus(1);
        this.accountStatusRepository.save(accountStatus);

        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    //Logout
    @RequestMapping(value={"/auth/logout"},method=RequestMethod.POST)
    public ResponseEntity<ApiResponse> deletionToken(@RequestBody WhiteListDto whiteListDto){
        String Token= whiteListDto.getWhiteToken();
        userService.deleteWhiteToken(Token);
        return new ResponseEntity(new ApiResponse("Token Remove Successfully"),HttpStatus.OK);
    }
}
