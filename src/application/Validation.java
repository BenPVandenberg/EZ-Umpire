package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.cryptolens.methods.*;
import io.cryptolens.models.*;

public class Validation {
	public static boolean check(String key,	String user) {
		String RSAPubKey = "<RSAKeyValue><Modulus>zljcFHYEi0ocKj7YeI4VuVIkpsr/Wz4ZtJfXFeYbVm2jALs70DyzKDiEP1OUvLoKvQJ++GXFmYnmUQImbu7E6LreYSsmf1x2H7u2xyb1EhDSLQtKgzH001kU/7J3RKrYoVKNWxPPT+50KP0CLoGjutlvXHcvNxo0OLr1DvbGTz/DQtbCqLtB8mzeMmahd5WJHpYXrcFxQ8x/FqImV+NXXhJAwT8g64mGVj1XDqDzbfuCA1YXWUDqbwaR5QPS2X8GRURPLV2PD9/Z8QFmLe5Mf0oD2w/Z6dmlt2OuQC6vHY/RM6zB7Bc5z++XQX8VG1IkZw0Uit8BrOCq+jicOxPSgQ==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
		String auth = "WyIxMjEyMyIsInZ2NUlCT20rNGkwd29BejhWUWhMeGM0Nk9jQkJPYkFDT1BZWVU3WGIiXQ==";

		LicenseKey license = Key.Activate(auth, RSAPubKey, 
		                      new ActivateModel(5334, 
		                    		  key, 
		                      Helpers.GetMachineCode()));
		
		if (license == null || !Helpers.IsOnRightMachine(license)) {
			
		    String contents = "";
		    try {
		        contents = new String(Files.readAllBytes(Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\EZ_Umpire\\licensefile.skm")));
		    } catch (IOException e) {
		    	FxDialogs.showInformation("No Licence Key Entered","From the Welcome menu click the \"About\" button and enter your licence key. Contact Ben2212@hotmail.ca to get one.");
		    }

		    LicenseKey licenseFile = LicenseKey.LoadFromString(RSAPubKey, contents, 3);

		    if(licenseFile != null && Helpers.IsOnRightMachine(license, Helpers.GetMachineCode())) {
		        System.out.println("Offline mode");
		        System.out.println("The license is valid!");
		        System.out.println("It will expire: " + licenseFile.Expires);
		    } else {
		        System.out.println("The license does not work.");
		    }
		    return false;
		    
		    
		} else {
		    System.out.println("The license is valid!");
		    System.out.println("It will expire: " + license.Expires);

		    // -------------------new code starts -------------------
		    // saving a copy of the response/certificate
		    try {
		        PrintWriter pw = new PrintWriter("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\EZ_Umpire\\licensefile.skm");
		        pw.println(license.SaveAsString());
		        pw.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		    return true;
		}
	}
}
