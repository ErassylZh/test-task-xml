package com.example.testtaskxml.DTO;

import lombok.ToString;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "header",
        "body"
})
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@ToString
public class SoapEnvelope {

    @XmlElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/", required = true)
    protected SoapHeader header;

    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/", required = true)
    protected SoapBody body;

    public SoapHeader getHeader() {
        return header;
    }

    public void setHeader(SoapHeader value) {
        this.header = value;
    }

    public SoapBody getBody() {
        return body;
    }

    public void setBody(SoapBody value) {
        this.body = value;
    }
}