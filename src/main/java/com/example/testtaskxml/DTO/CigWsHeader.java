package com.example.testtaskxml.DTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "userName",
        "password"
})
public class CigWsHeader {

    @XmlElement(name = "UserName", namespace = "https://ws.creditinfo.com", required = true)
    protected String userName;

    @XmlElement(name = "Password", namespace = "https://ws.creditinfo.com", required = true)
    protected String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }
}
