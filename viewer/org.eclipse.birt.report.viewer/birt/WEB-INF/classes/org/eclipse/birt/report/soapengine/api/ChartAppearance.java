/**
 * ChartAppearance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ChartAppearance  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private java.lang.Boolean showLegend;
    private java.lang.Boolean showValues;
    private org.eclipse.birt.report.soapengine.api.ChartLocation location;
    private java.lang.String width;
    private java.lang.String height;

    public ChartAppearance() {
    }

    public ChartAppearance(
           java.lang.Boolean showLegend,
           java.lang.Boolean showValues,
           org.eclipse.birt.report.soapengine.api.ChartLocation location,
           java.lang.String width,
           java.lang.String height) {
           this.showLegend = showLegend;
           this.showValues = showValues;
           this.location = location;
           this.width = width;
           this.height = height;
    }


    /**
     * Gets the showLegend value for this ChartAppearance.
     * 
     * @return showLegend
     */
    public java.lang.Boolean getShowLegend() {
        return showLegend;
    }


    /**
     * Sets the showLegend value for this ChartAppearance.
     * 
     * @param showLegend
     */
    public void setShowLegend(java.lang.Boolean showLegend) {
        this.showLegend = showLegend;
    }


    /**
     * Gets the showValues value for this ChartAppearance.
     * 
     * @return showValues
     */
    public java.lang.Boolean getShowValues() {
        return showValues;
    }


    /**
     * Sets the showValues value for this ChartAppearance.
     * 
     * @param showValues
     */
    public void setShowValues(java.lang.Boolean showValues) {
        this.showValues = showValues;
    }


    /**
     * Gets the location value for this ChartAppearance.
     * 
     * @return location
     */
    public org.eclipse.birt.report.soapengine.api.ChartLocation getLocation() {
        return location;
    }


    /**
     * Sets the location value for this ChartAppearance.
     * 
     * @param location
     */
    public void setLocation(org.eclipse.birt.report.soapengine.api.ChartLocation location) {
        this.location = location;
    }


    /**
     * Gets the width value for this ChartAppearance.
     * 
     * @return width
     */
    public java.lang.String getWidth() {
        return width;
    }


    /**
     * Sets the width value for this ChartAppearance.
     * 
     * @param width
     */
    public void setWidth(java.lang.String width) {
        this.width = width;
    }


    /**
     * Gets the height value for this ChartAppearance.
     * 
     * @return height
     */
    public java.lang.String getHeight() {
        return height;
    }


    /**
     * Sets the height value for this ChartAppearance.
     * 
     * @param height
     */
    public void setHeight(java.lang.String height) {
        this.height = height;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChartAppearance)) return false;
        ChartAppearance other = (ChartAppearance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.showLegend==null && other.getShowLegend()==null) || 
             (this.showLegend!=null &&
              this.showLegend.equals(other.getShowLegend()))) &&
            ((this.showValues==null && other.getShowValues()==null) || 
             (this.showValues!=null &&
              this.showValues.equals(other.getShowValues()))) &&
            ((this.location==null && other.getLocation()==null) || 
             (this.location!=null &&
              this.location.equals(other.getLocation()))) &&
            ((this.width==null && other.getWidth()==null) || 
             (this.width!=null &&
              this.width.equals(other.getWidth()))) &&
            ((this.height==null && other.getHeight()==null) || 
             (this.height!=null &&
              this.height.equals(other.getHeight())));
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
        if (getShowLegend() != null) {
            _hashCode += getShowLegend().hashCode();
        }
        if (getShowValues() != null) {
            _hashCode += getShowValues().hashCode();
        }
        if (getLocation() != null) {
            _hashCode += getLocation().hashCode();
        }
        if (getWidth() != null) {
            _hashCode += getWidth().hashCode();
        }
        if (getHeight() != null) {
            _hashCode += getHeight().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ChartAppearance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartAppearance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showLegend");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ShowLegend"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ShowValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("location");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Location"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartLocation"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("width");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Width"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("height");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Height"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
