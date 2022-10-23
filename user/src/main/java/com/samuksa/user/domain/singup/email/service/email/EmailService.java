package com.samuksa.user.domain.singup.email.service.email;

public interface EmailService {
    String sendSimpleMessage(String userEmail)throws Exception;

}
