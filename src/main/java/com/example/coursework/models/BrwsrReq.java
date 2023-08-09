package com.example.coursework.models;

import com.example.coursework.models.enams.SupportedBrwsr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrwsrReq implements Entity {
    private static final String HEADERS = "id, supportedBrowser";
    private Integer brwsrReqId;
    private SupportedBrwsr supportedBrwsr;

    public  static String getHeaders() {
        return HEADERS;
    }

    public String toCsv() {
        return getBrwsrReqId() + ", " + getSupportedBrwsr();
    }

}
