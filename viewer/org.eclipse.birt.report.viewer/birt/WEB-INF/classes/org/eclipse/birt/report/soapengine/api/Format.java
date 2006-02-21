/**
 * Format.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Sep 06, 2005 (12:48:20 PDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Format  implements java.io.Serializable {
    private org.eclipse.birt.report.soapengine.api.Select stringFormat;
    private org.eclipse.birt.report.soapengine.api.Select dateTimeFormat;
    private org.eclipse.birt.report.soapengine.api.NumericFormat nunericFormat;

    public Format() {
    }

    public Format(
           org.eclipse.birt.report.soapengine.api.Select stringFormat,
           org.eclipse.birt.report.soapengine.api.Select dateTimeFormat,
           org.eclipse.birt.report.soapengine.api.NumericFormat nunericFormat) {
           this.stringFormat = stringFormat;
           this.dateTimeFormat = dateTimeFormat;
           this.nunericFormat = nunericFormat;
    }


    /**
     * Gets the stringFormat value for this Format.
     * 
     * @return stringFormat
     */
    public org.eclipse.birt.report.soapengine.api.Select getStringFormat() {
        return stringFormat;
    }


    /**
     * Sets the stringFormat value for this Format.
     * 
     * @param stringFormat
     */
    public void setStringFormat(org.eclipse.birt.report.soapengine.api.Select stringFormat) {
        this.stringFormat = stringFormat;
    }


    /**
     * Gets the dateTimeFormat value for this Format.
     * 
     * @return dateTimeFormat
     */
    public org.eclipse.birt.report.soapengine.api.Select getDateTimeFormat() {
        return dateTimeFormat;
    }


    /**
     * Sets the dateTimeFormat value for this Format.
     * 
     * @param dateTimeFormat
     */
    public void setDateTimeFormat(org.eclipse.birt.report.soapengine.api.Select dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }


    /**
     * Gets the nunericFormat value for this Format.
     * 
     * @return nunericFormat
     */
    public org.eclipse.birt.report.soapengine.api.NumericFormat getNunericFormat() {
        return nunericFormat;
    }


    /**
     * Sets the nunericFormat value for this Format.
     * 
     * @param nunericFormat
     */
    public void setNunericFormat(org.eclipse.birt.report.soapengine.api.NumericFormat nunericFormat) {
        this.nunericFormat = nunericFormat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Format)) return false;
        Format other = (Format) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.stringFormat==null && other.getStringFormat()==null) || 
             (this.stringFormat!=null &&
              this.stringFormat.equals(other.getStringFormat()))) &&
            ((this.dateTimeFormat==null && other.getDateTimeFormat()==null) || 
             (this.dateTimeFormat!=null &&
              this.dateTimeFormat.equals(other.getDateTimeFormat()))) &&
            ((this.nunericFormat==null && other.getNunericFormat()==null) || 
             (this.nunericFormat!=null &&
              this.nunericFormat.equals(other.getNunericFormat())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getStringFormat() != null) {
            _hashCode += getStringFormat().hashCode();
        }
        if (getDateTimeFormat() != null) {
            _hashCode += getDateTimeFormat().hashCode();
        }
        if (getNunericFormat() != null) {
            _hashCode += getNunericFormat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Format.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stringFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "StringFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Select"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTimeFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DateTimeFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Select"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nunericFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NunericFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumericFormat"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
