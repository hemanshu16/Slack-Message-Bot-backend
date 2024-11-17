package com.hemanshu.slackmessage.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;
    private String address;

    private String gender;

    private String name;

    private String phoneNumber;
    private boolean newsLetter;
    private boolean updates;

    private boolean successfullySent;
}
