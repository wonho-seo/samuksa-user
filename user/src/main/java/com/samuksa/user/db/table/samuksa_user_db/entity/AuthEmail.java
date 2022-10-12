package com.samuksa.user.db.table.samuksa_user_db.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_email",schema = "samuksa_user_db")
public class AuthEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_email_idx")
    private int authEmailIdx;
    @Column(name = "auth_email_mail")
    private String authEmail;
    @Column(name = "auth_email_key")
    private String authKey;
    @Column(name = "auth_email_auth")
    private int auth;
}
