package com.samuksa.user.domain.singup.service.email;

public interface EmailService {
    String sendSimpleMessage(String userEmail)throws Exception;

}
