package com.samuksa.user.db.table.samuksa_user_db.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_image", schema = "samuksa_user_db")
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_user_idx")
    private long idx;

    @Column(name = "profile_image")
    private String profilePath;

    @OneToOne
    @MapsId
    @JoinColumn(name = "cust_user_idx")
    private CustUser custUser;
}
