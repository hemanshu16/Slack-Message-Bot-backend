package com.hemanshu.slackmessage.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDTO {

    private String address;

    private String gender;

    private String name;

    private String phoneNumber;

    private Preferences preferences;

    @Override
    public String toString() {
        return
                "name :" + name +
                "\naddress :" + address +
                "\ngender :" + gender +
                "\nphoneNumber :" + phoneNumber +
                "\npreferences : " + (preferences != null ? preferences.toString() : "null");
    }
    
}
