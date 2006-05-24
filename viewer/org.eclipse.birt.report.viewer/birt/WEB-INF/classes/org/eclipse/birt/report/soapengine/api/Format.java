/**
 * Format.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Format  implements java.io.Serializable {
    private org.eclipse.birt.report.soapengine.api.CategoryChoice[] stringFormat;

    private org.eclipse.birt.report.soapengine.api.CategoryChoice[] dateTimeFormat;

    private org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] numberFormat;

    public Format() {
    }

    public Format(
           org.eclipse.birt.report.soapengine.api.CategoryChoice[] stringFormat,
           org.eclipse.birt.report.soapengine.api.CategoryChoice[] dateTimeFormat,
           org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] numberFormat) {
           this.stringFormat = stringFormat;
           this.dateTimeFormat = dateTimeFormat;
           this.numberFormat = numberFormat;
    }


    /**
     * Gets the stringFormat value for this Format.
     * 
     * @return stringFormat
     */
    public org.eclipse.birt.report.soapengine.api.CategoryChoice[] getStringFormat() {
        return stringFormat;
    }


    /**
     * Sets the stringFormat value for this Format.
     * 
     * @param stringFormat
     */
    public void setStringFormat(org.eclipse.birt.report.soapengine.api.CategoryChoice[] stringFormat) {
        this.stringFormat = stringFormat;
    }


    /**
     * Gets the dateTimeFormat value for this Format.
     * 
     * @return dateTimeFormat
     */
    public org.eclipse.birt.report.soapengine.api.CategoryChoice[] getDateTimeFormat() {
        return dateTimeFormat;
    }


    /**
     * Sets the dateTimeFormat value for this Format.
     * 
     * @param dateTimeFormat
     */
    public void setDateTimeFormat(org.eclipse.birt.report.soapengine.api.CategoryChoice[] dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }


    /**
     * Gets the numberFormat value for this Format.
     * 
     * @return numberFormat
     */
    public org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] getNumberFormat() {
        return numberFormat;
    }


    /**
     * Sets the numberFormat value for this Format.
     * 
     * @param numberFormat
     */
    public void setNumberFormat(org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] numberFormat) {
        this.numberFormat = numberFormat;
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
              java.util.Arrays.equals(this.stringFormat, other.getStringFormat()))) &&
            ((this.dateTimeFormat==null && other.getDateTimeFormat()==null) || 
             (this.dateTimeFormat!=null &&
              java.util.Arrays.equals(this.dateTimeFormat, other.getDateTimeFormat()))) &&
            ((this.numberFormat==null && other.getNumberFormat()==null) || 
             (this.numberFormat!=null &&
              java.util.Arrays.equals(this.numberFormat, other.getNumberFormat())));
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStringFormat());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStringFormat(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDateTimeFormat() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDateTimeFormat());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDateTimeFormat(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumberFormat() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNumberFormat());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNumberFormat(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTimeFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DateTimeFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumberFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumberCategoryChoice"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumberCategoryChoice"));
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
