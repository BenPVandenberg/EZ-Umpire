// Ben Vandenberg © 2019
package application;

import java.util.Properties;  
import javax.mail.*;  
import javax.mail.internet.*;  
  
public class SendEmail {  
	
    /**
     * Sends email
     *
     * @param toEmail Where the message is to be sent
     * @param msg The message to send
     */
	public static void send(String toEmail,String subject,String msg) {  
		
		if (msg.trim().equals("\n") || msg.trim().equals("")) {
			FxDialogs.showWarning("Can't send an email with no message", "");
			return;
		}
	  	
		String host="smtp.gmail.com";
		final String email="Scheduling.EzUmpire@gmail.com";
		final String authKey="_dW8g6Cn!!G_-BBdx2yF";
	    
//		String to="7053083088@msg.telus.com";
		String to = toEmail;
	  
		//Get the session object  
		Properties props = new Properties();  
		props.put("mail.smtp.host",host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); //TLS
	     
		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(email,authKey);
					}  
	    		});  
	  
		//Compose the message  
	    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(email));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
		     message.setSubject(subject);  
		     message.setText(msg);
		       
		    //send the message  
		     Transport.send(message);  
		  
		     FxDialogs.showInformation("Email Sent!", "");
	   
	     } catch (MessagingException e) {
//	    	 e.printStackTrace();
	    	 FxDialogs.showException("Unable to Send Email", "", e);
	     }  
	}
}  