package com.example.coursework.models;

import com.example.coursework.models.enams.Countries;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Entity {
    private static final String HEADERS = "userId, nickName, dateOfRegister, country, firstName, lastName";
    private Integer userId;
    private String nickname;
    private String dateOfRegister;
    private Countries country;
    private String firstName;
    private String lastName;

    public static String getHeaders() {
        return HEADERS;
    }

    public String toCsv() {
        return getUserId() + ", " + getNickname()
                + ", " + getDateOfRegister() + ", " + getCountry()
                + ", " + getFirstName() + ", " + getLastName();
    }
}
