/**
 * JoinMetadata.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class JoinMetadata  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.IOList IOList;
    private org.eclipse.birt.report.soapengine.api.JoinList joinList;

    public JoinMetadata() {
    }

    public JoinMetadata(
           org.eclipse.birt.report.soapengine.api.IOList IOList,
           org.eclipse.birt.report.soapengine.api.JoinList joinList) {
           this.IOList = IOList;
           this.joinList = joinList;
    }


    /**
     * Gets the IOList value for this JoinMetadata.
     * 
     * @return IOList
     */
    public org.eclipse.birt.report.soapengine.api.IOList getIOList() {
        return IOList;
    }


    /**
     * Sets the IOList value for this JoinMetadata.
     * 
     * @param IOList
     */
    public void setIOList(org.eclipse.birt.report.soapengine.api.IOList IOList) {
        this.IOList = IOList;
    }


    /**
     * Gets the joinList value for this JoinMetadata.
     * 
     * @return joinList
     */
    public org.eclipse.birt.report.soapengine.api.JoinList getJoinList() {
        return joinList;
    }


    /**
     * Sets the joinList value for this JoinMetadata.
     * 
     * @param joinList
     */
    public void setJoinList(org.eclipse.birt.report.soapengine.api.JoinList joinList) {
        this.joinList = joinList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JoinMetadata)) return false;
        JoinMetadata other = (JoinMetadata) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.IOList==null && other.getIOList()==null) || 
             (this.IOList!=null &&
              this.IOList.equals(other.getIOList()))) &&
            ((this.joinList==null && other.getJoinList()==null) || 
             (this.joinList!=null &&
              this.joinList.equals(other.getJoinList())));
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
        if (getIOList() != null) {
            _hashCode += getIOList().hashCode();
        }
        if (getJoinList() != null) {
            _hashCode += getJoinList().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JoinMetadata.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinMetadata"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IOList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOList"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("joinList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinList"));
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
