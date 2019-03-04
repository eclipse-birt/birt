/**
 * IOInfoList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class IOInfoList  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.IOFieldList[] IOFieldListArray;

    public IOInfoList() {
    }

    public IOInfoList(
           org.eclipse.birt.report.soapengine.api.IOFieldList[] IOFieldListArray) {
           this.IOFieldListArray = IOFieldListArray;
    }


    /**
     * Gets the IOFieldListArray value for this IOInfoList.
     * 
     * @return IOFieldListArray
     */
    public org.eclipse.birt.report.soapengine.api.IOFieldList[] getIOFieldListArray() {
        return IOFieldListArray;
    }


    /**
     * Sets the IOFieldListArray value for this IOInfoList.
     * 
     * @param IOFieldListArray
     */
    public void setIOFieldListArray(org.eclipse.birt.report.soapengine.api.IOFieldList[] IOFieldListArray) {
        this.IOFieldListArray = IOFieldListArray;
    }

    public org.eclipse.birt.report.soapengine.api.IOFieldList getIOFieldListArray(int i) {
        return this.IOFieldListArray[i];
    }

    public void setIOFieldListArray(int i, org.eclipse.birt.report.soapengine.api.IOFieldList _value) {
        this.IOFieldListArray[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IOInfoList)) return false;
        IOInfoList other = (IOInfoList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.IOFieldListArray==null && other.getIOFieldListArray()==null) || 
             (this.IOFieldListArray!=null &&
              java.util.Arrays.equals(this.IOFieldListArray, other.getIOFieldListArray())));
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
        if (getIOFieldListArray() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIOFieldListArray());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIOFieldListArray(), i);
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
        new org.apache.axis.description.TypeDesc(IOInfoList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOInfoList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IOFieldListArray");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOFieldListArray"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOFieldList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
