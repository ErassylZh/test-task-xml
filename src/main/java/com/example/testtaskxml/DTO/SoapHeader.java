package com.example.testtaskxml.DTO;

import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "cigWsHeader"
})
@ToString
public class SoapHeader {

    @XmlElement(name = "CigWsHeader", namespace = "https://ws.creditinfo.com", required = true)
    protected CigWsHeader cigWsHeader;

    public CigWsHeader getCigWsHeader() {
        return cigWsHeader;
    }

    public void setCigWsHeader(CigWsHeader value) {
        this.cigWsHeader = value;
    }
}