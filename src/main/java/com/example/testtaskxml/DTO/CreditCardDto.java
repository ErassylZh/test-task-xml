package com.example.testtaskxml.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class CreditCardDto {
    private String contractCode;
    private String agreementNumber;
    private String fundingType;
    private String creditPurpose2;
    private String creditObject;
    private String contractPhase;
    private String contractStatus;
    private String startDate;
    private String endDate;
    private String annualEffectiveRate;
    private String nominalRate;
    private String paymentPeriod;
    private String totalAmount;
    private String instalmentAmount;
    private String outstanding_amount;
    private String firstname;
    private String surname;
    private String fathersname;
    private String dateOfBirth;
    private String identification1;
    private String ident1_date_reg;
    private String identification2;
    private String katoid1;
    private String katoid2;
    private String streetName1;
    private String streetName2;
    private String accountingDate;
    private String ident2_date_reg;
    private String resident;
    private String fcb_status;
    private String scb_status;
    private String batch_fcb;
    private String batch_scb;

}
