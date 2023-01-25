package com.blogapplication.blogapplication.common.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class CommonUtils {


	public static final String INVALID_EMAIL_ADDRESS = "INVALID_EMAIL_ADDRESS";
	public static final String INVALID_PHONE_NUMBER = "INVALID_PHONE_NUMBER";
	@Autowired
	private Environment environment;
	
	
	public  Boolean  isEmailValid(String emailAddress) {
		
//		ErrorMessages errorMessages = new ErrorMessages();
		 
	 	if(Objects.isNull(emailAddress)) {
			return false;
	 	}else if (emailAddress.trim().length()==0) {
			return false;
		}
		
		
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
		        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";		
		 boolean matched = Pattern.compile(regexPattern).matcher(emailAddress).matches();
	
		 return matched;

	}
	
	

	    
	    public  boolean isPhoneNumberValid(String str)
	    {
	    	
//	    	ErrorMessages errorMessages = new ErrorMessages();
			 
		 	if(Objects.isNull(str)) {
				return false;
		 	}else if (str.trim().length()==0) {
				return false;
			}
		 
	    	
	        // Regex to check string contains only digits
	        String regex = "[0-9]+";
	        Pattern p = Pattern.compile(regex);
	        
	        if (!(!Objects.isNull(str) && !str.isBlank())) {
	            return false;
	        }
	        Matcher m = p.matcher(str);
	        
	        return m.matches();
	    }
	    

	    
	    public  String generateOtp(int length) {
	    	String numbers = "1234567890";
	        Random random = new Random();
	        char[] otp = new char[length];

	        for(int i = 0; i< length ; i++) {
	           otp[i] = numbers.charAt(random.nextInt(numbers.length()));
	        }
	        return new String(otp);
	    }
	    
	    public  boolean isValidPassword(String password) {
	    	
	    	 String regex = "^(?=.*[0-9])"
                     + "(?=.*[a-z])(?=.*[A-Z])"
                     + "(?=.*[@#$%^&+=])"
                     + "(?=\\S+$).{8,20}$";

     
	    	 Pattern p = Pattern.compile(regex);

      
	    	 if (password == null) {
	    		 return false;
	    	 }

      
	    	 Matcher m = p.matcher(password);

	    	return m.matches();
	    	
	    }
	    
	    public  String generateProfileId() {
	    	
	    	String profileId = "";
	    	
	    	int size = Integer.parseInt(environment.getProperty("sizeOfProfileId"));
	    	
	    	String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	    	         + "0123456789"
	    	         + "abcdefghijklmnopqrstuvxyz";
	    	 
	    	  StringBuilder sb = new StringBuilder(size);	    	 
	    	  for (int i = 0; i < size; i++) {
	    	  
	    	   int index
	    	    = (int)(AlphaNumericString.length()
	    	      * Math.random());
	    		    	  
	    	   sb.append(AlphaNumericString
	    	      .charAt(index));
	    	  }
	    	 

	    		  profileId = "BA-"+sb.toString();

	    	  
	    	  
	    	  return profileId;
	
	    }

}
