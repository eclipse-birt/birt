/**
 * Export.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Export  implements java.io.Serializable {
    private org.eclipse.birt.report.soapengine.api.Vector availableColumns;
    private org.eclipse.birt.report.soapengine.api.Vector selectedColumn;
    private org.eclipse.birt.report.soapengine.api.ExportCriteria[] criteria;

    public Export() {
    }

    public org.eclipse.birt.report.soapengine.api.Vector getAvailableColumns() {
        return availableColumns;
    }

    public void setAvailableColumns(org.eclipse.birt.report.soapengine.api.Vector availableColumns) {
        this.availableColumns = availableColumns;
    }

    public org.eclipse.birt.report.soapengine.api.Vector getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(org.eclipse.birt.report.soapengine.api.Vector selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public org.eclipse.birt.report.soapengine.api.ExportCriteria[] getCriteria() {
        return criteria;
    }

    public void setCriteria(org.eclipse.birt.report.soapengine.api.ExportCriteria[] criteria) {
        this.criteria = criteria;
    }

    public org.eclipse.birt.report.soapengine.api.ExportCriteria getCriteria(int i) {
        return criteria[i];
    }

    public void setCriteria(int i, org.eclipse.birt.report.soapengine.api.ExportCriteria value) {
        this.criteria[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Export)) return false;
        Export other = (Export) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.availableColumns==null && other.getAvailableColumns()==null) || 
             (this.availableColumns!=null &&
              this.availableColumns.equals(other.getAvailableColumns()))) &&
            ((this.selectedColumn==null && other.getSelectedColumn()==null) || 
             (this.selectedColumn!=null &&
              this.selectedColumn.equals(other.getSelectedColumn()))) &&
            ((this.criteria==null && other.getCriteria()==null) || 
             (this.criteria!=null &&
              java.util.Arrays.equals(this.criteria, other.getCriteria())));
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
        if (getAvailableColumns() != null) {
            _hashCode += getAvailableColumns().hashCode();
        }
        if (getSelectedColumn() != null) {
            _hashCode += getSelectedColumn().hashCode();
        }
        if (getCriteria() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCriteria());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCriteria(), i);
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
        new org.apache.axis.description.TypeDesc(Export.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("availableColumns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AvailableColumns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectedColumn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SelectedColumn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criteria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Criteria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ExportCriteria"));
        elemField.setMinOccurs(0);
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
