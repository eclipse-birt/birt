/**
 * JoinMetadata.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class JoinMetadata  implements java.io.Serializable {
    private org.eclipse.birt.report.soapengine.api.IOReference[] IOList;

    private org.eclipse.birt.report.soapengine.api.JoinDefinition[] joinList;

    public JoinMetadata() {
    }

    public JoinMetadata(
           org.eclipse.birt.report.soapengine.api.IOReference[] IOList,
           org.eclipse.birt.report.soapengine.api.JoinDefinition[] joinList) {
           this.IOList = IOList;
           this.joinList = joinList;
    }


    /**
     * Gets the IOList value for this JoinMetadata.
     * 
     * @return IOList
     */
    public org.eclipse.birt.report.soapengine.api.IOReference[] getIOList() {
        return IOList;
    }


    /**
     * Sets the IOList value for this JoinMetadata.
     * 
     * @param IOList
     */
    public void setIOList(org.eclipse.birt.report.soapengine.api.IOReference[] IOList) {
        this.IOList = IOList;
    }


    /**
     * Gets the joinList value for this JoinMetadata.
     * 
     * @return joinList
     */
    public org.eclipse.birt.report.soapengine.api.JoinDefinition[] getJoinList() {
        return joinList;
    }


    /**
     * Sets the joinList value for this JoinMetadata.
     * 
     * @param joinList
     */
    public void setJoinList(org.eclipse.birt.report.soapengine.api.JoinDefinition[] joinList) {
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
              java.util.Arrays.equals(this.IOList, other.getIOList()))) &&
            ((this.joinList==null && other.getJoinList()==null) || 
             (this.joinList!=null &&
              java.util.Arrays.equals(this.joinList, other.getJoinList())));
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIOList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIOList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJoinList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJoinList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJoinList(), i);
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
        new org.apache.axis.description.TypeDesc(JoinMetadata.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinMetadata"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IOList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOReference"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IO"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("joinList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinDefinition"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Join"));
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
