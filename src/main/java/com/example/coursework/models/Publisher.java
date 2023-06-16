package com.example.coursework.models;

import com.example.coursework.models.enams.Publishers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher implements Entity {
    private static final String HEADERS = "publisherId, publisher";
    private Integer publisherId;
    private Publishers publisher;

    public static String getHeaders() {
        return HEADERS;
    }

    public String toCsv() {
        return getPublisherId() + ", " + getPublisher();
    }

}
