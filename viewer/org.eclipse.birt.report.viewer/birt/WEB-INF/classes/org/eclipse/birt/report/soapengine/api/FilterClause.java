/**
 * FilterClause.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class FilterClause  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.ColumnDefinition column;
    private org.eclipse.birt.report.soapengine.api.BoundDataColumn columnName;
    private java.lang.String filterOp;
    private java.lang.String[] operand;

    public FilterClause() {
    }

    public FilterClause(
           org.eclipse.birt.report.soapengine.api.ColumnDefinition column,
           org.eclipse.birt.report.soapengine.api.BoundDataColumn columnName,
           java.lang.String filterOp,
           java.lang.String[] operand) {
           this.column = column;
           this.columnName = columnName;
           this.filterOp = filterOp;
           this.operand = operand;
    }


    /**
     * Gets the column value for this FilterClause.
     * 
     * @return column
     */
    public org.eclipse.birt.report.soapengine.api.ColumnDefinition getColumn() {
        return column;
    }


    /**
     * Sets the column value for this FilterClause.
     * 
     * @param column
     */
    public void setColumn(org.eclipse.birt.report.soapengine.api.ColumnDefinition column) {
        this.column = column;
    }


    /**
     * Gets the columnName value for this FilterClause.
     * 
     * @return columnName
     */
    public org.eclipse.birt.report.soapengine.api.BoundDataColumn getColumnName() {
        return columnName;
    }


    /**
     * Sets the columnName value for this FilterClause.
     * 
     * @param columnName
     */
    public void setColumnName(org.eclipse.birt.report.soapengine.api.BoundDataColumn columnName) {
        this.columnName = columnName;
    }


    /**
     * Gets the filterOp value for this FilterClause.
     * 
     * @return filterOp
     */
    public java.lang.String getFilterOp() {
        return filterOp;
    }


    /**
     * Sets the filterOp value for this FilterClause.
     * 
     * @param filterOp
     */
    public void setFilterOp(java.lang.String filterOp) {
        this.filterOp = filterOp;
    }


    /**
     * Gets the operand value for this FilterClause.
     * 
     * @return operand
     */
    public java.lang.String[] getOperand() {
        return operand;
    }


    /**
     * Sets the operand value for this FilterClause.
     * 
     * @param operand
     */
    public void setOperand(java.lang.String[] operand) {
        this.operand = operand;
    }

    public java.lang.String getOperand(int i) {
        return this.operand[i];
    }

    public void setOperand(int i, java.lang.String _value) {
        this.operand[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FilterClause)) return false;
        FilterClause other = (FilterClause) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.column==null && other.getColumn()==null) || 
             (this.column!=null &&
              this.column.equals(other.getColumn()))) &&
            ((this.columnName==null && other.getColumnName()==null) || 
             (this.columnName!=null &&
              this.columnName.equals(other.getColumnName()))) &&
            ((this.filterOp==null && other.getFilterOp()==null) || 
             (this.filterOp!=null &&
              this.filterOp.equals(other.getFilterOp()))) &&
            ((this.operand==null && other.getOperand()==null) || 
             (this.operand!=null &&
              java.util.Arrays.equals(this.operand, other.getOperand())));
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
        if (getColumn() != null) {
            _hashCode += getColumn().hashCode();
        }
        if (getColumnName() != null) {
            _hashCode += getColumnName().hashCode();
        }
        if (getFilterOp() != null) {
            _hashCode += getFilterOp().hashCode();
        }
        if (getOperand() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOperand());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOperand(), i);
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
        new org.apache.axis.description.TypeDesc(FilterClause.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FilterClause"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("column");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Column"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefinition"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumn"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filterOp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FilterOp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operand");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Operand"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
