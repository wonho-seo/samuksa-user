package com.samuksa.user.dto.email.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Setter
@Getter
public class EmailAuth {
    private String email;
    private String authKey;
    private int auth;
}
