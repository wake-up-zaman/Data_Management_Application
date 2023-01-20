package com.example.Personal.Data.Management.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class GenerateOTPUtil {

    public static String generateOTP() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();

            String OTP = String.valueOf(secureRandom.ints(10_000, 100_000)
                    .findFirst().getAsInt());

            return OTP;

        } catch (NoSuchAlgorithmException e) {
            SecureRandom secureRandom = new SecureRandom();
            int randomOTP = secureRandom.ints(10_000, 100_000)
                    .findFirst().getAsInt();
            return String.valueOf(randomOTP);
        }
    }

}
