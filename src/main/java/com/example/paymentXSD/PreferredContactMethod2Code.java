//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.08.05 at 02:11:44 PM MSK 
//


package com.example.paymentXSD;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PreferredContactMethod2Code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PreferredContactMethod2Code"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MAIL"/&gt;
 *     &lt;enumeration value="FAXX"/&gt;
 *     &lt;enumeration value="LETT"/&gt;
 *     &lt;enumeration value="CELL"/&gt;
 *     &lt;enumeration value="ONLI"/&gt;
 *     &lt;enumeration value="PHON"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PreferredContactMethod2Code")
@XmlEnum
public enum PreferredContactMethod2Code {

    MAIL,
    FAXX,
    LETT,
    CELL,
    ONLI,
    PHON;

    public String value() {
        return name();
    }

    public static PreferredContactMethod2Code fromValue(String v) {
        return valueOf(v);
    }

}