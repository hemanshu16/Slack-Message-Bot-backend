package com.hemanshu.slackmessage.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Preferences {
    private boolean newsLetter;
    private boolean updates;


    @Override
    public String toString() {
        return "\t( newsLetter=" + newsLetter + ", updates=" + updates + " )";

    }
}
