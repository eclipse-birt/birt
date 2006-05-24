/**
 * AvailableOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class AvailableOperation  implements java.io.Serializable {
    private boolean sortAsc;
    private boolean sortDsc;
    private boolean advancedSort;
    private boolean addGroup;
    private boolean deleteGroup;
    private boolean hideColumn;
    private boolean showColumns;
    private boolean reorderColumns;
    private boolean filter;
    private boolean calculation;
    private boolean aggregation;
    private boolean changeFont;
    private boolean format;

    public AvailableOperation() {
    }

    public AvailableOperation(
           boolean sortAsc,
           boolean sortDsc,
           boolean advancedSort,
           boolean addGroup,
           boolean deleteGroup,
           boolean hideColumn,
           boolean showColumns,
           boolean reorderColumns,
           boolean filter,
           boolean calculation,
           boolean aggregation,
           boolean changeFont,
           boolean format) {
           this.sortAsc = sortAsc;
           this.sortDsc = sortDsc;
           this.advancedSort = advancedSort;
           this.addGroup = addGroup;
           this.deleteGroup = deleteGroup;
           this.hideColumn = hideColumn;
           this.showColumns = showColumns;
           this.reorderColumns = reorderColumns;
           this.filter = filter;
           this.calculation = calculation;
           this.aggregation = aggregation;
           this.changeFont = changeFont;
           this.format = format;
    }


    /**
     * Gets the sortAsc value for this AvailableOperation.
     * 
     * @return sortAsc
     */
    public boolean isSortAsc() {
        return sortAsc;
    }


    /**
     * Sets the sortAsc value for this AvailableOperation.
     * 
     * @param sortAsc
     */
    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }


    /**
     * Gets the sortDsc value for this AvailableOperation.
     * 
     * @return sortDsc
     */
    public boolean isSortDsc() {
        return sortDsc;
    }


    /**
     * Sets the sortDsc value for this AvailableOperation.
     * 
     * @param sortDsc
     */
    public void setSortDsc(boolean sortDsc) {
        this.sortDsc = sortDsc;
    }


    /**
     * Gets the advancedSort value for this AvailableOperation.
     * 
     * @return advancedSort
     */
    public boolean isAdvancedSort() {
        return advancedSort;
    }


    /**
     * Sets the advancedSort value for this AvailableOperation.
     * 
     * @param advancedSort
     */
    public void setAdvancedSort(boolean advancedSort) {
        this.advancedSort = advancedSort;
    }


    /**
     * Gets the addGroup value for this AvailableOperation.
     * 
     * @return addGroup
     */
    public boolean isAddGroup() {
        return addGroup;
    }


    /**
     * Sets the addGroup value for this AvailableOperation.
     * 
     * @param addGroup
     */
    public void setAddGroup(boolean addGroup) {
        this.addGroup = addGroup;
    }


    /**
     * Gets the deleteGroup value for this AvailableOperation.
     * 
     * @return deleteGroup
     */
    public boolean isDeleteGroup() {
        return deleteGroup;
    }


    /**
     * Sets the deleteGroup value for this AvailableOperation.
     * 
     * @param deleteGroup
     */
    public void setDeleteGroup(boolean deleteGroup) {
        this.deleteGroup = deleteGroup;
    }


    /**
     * Gets the hideColumn value for this AvailableOperation.
     * 
     * @return hideColumn
     */
    public boolean isHideColumn() {
        return hideColumn;
    }


    /**
     * Sets the hideColumn value for this AvailableOperation.
     * 
     * @param hideColumn
     */
    public void setHideColumn(boolean hideColumn) {
        this.hideColumn = hideColumn;
    }


    /**
     * Gets the showColumns value for this AvailableOperation.
     * 
     * @return showColumns
     */
    public boolean isShowColumns() {
        return showColumns;
    }


    /**
     * Sets the showColumns value for this AvailableOperation.
     * 
     * @param showColumns
     */
    public void setShowColumns(boolean showColumns) {
        this.showColumns = showColumns;
    }


    /**
     * Gets the reorderColumns value for this AvailableOperation.
     * 
     * @return reorderColumns
     */
    public boolean isReorderColumns() {
        return reorderColumns;
    }


    /**
     * Sets the reorderColumns value for this AvailableOperation.
     * 
     * @param reorderColumns
     */
    public void setReorderColumns(boolean reorderColumns) {
        this.reorderColumns = reorderColumns;
    }


    /**
     * Gets the filter value for this AvailableOperation.
     * 
     * @return filter
     */
    public boolean isFilter() {
        return filter;
    }


    /**
     * Sets the filter value for this AvailableOperation.
     * 
     * @param filter
     */
    public void setFilter(boolean filter) {
        this.filter = filter;
    }


    /**
     * Gets the calculation value for this AvailableOperation.
     * 
     * @return calculation
     */
    public boolean isCalculation() {
        return calculation;
    }


    /**
     * Sets the calculation value for this AvailableOperation.
     * 
     * @param calculation
     */
    public void setCalculation(boolean calculation) {
        this.calculation = calculation;
    }


    /**
     * Gets the aggregation value for this AvailableOperation.
     * 
     * @return aggregation
     */
    public boolean isAggregation() {
        return aggregation;
    }


    /**
     * Sets the aggregation value for this AvailableOperation.
     * 
     * @param aggregation
     */
    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }


    /**
     * Gets the changeFont value for this AvailableOperation.
     * 
     * @return changeFont
     */
    public boolean isChangeFont() {
        return changeFont;
    }


    /**
     * Sets the changeFont value for this AvailableOperation.
     * 
     * @param changeFont
     */
    public void setChangeFont(boolean changeFont) {
        this.changeFont = changeFont;
    }


    /**
     * Gets the format value for this AvailableOperation.
     * 
     * @return format
     */
    public boolean isFormat() {
        return format;
    }


    /**
     * Sets the format value for this AvailableOperation.
     * 
     * @param format
     */
    public void setFormat(boolean format) {
        this.format = format;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AvailableOperation)) return false;
        AvailableOperation other = (AvailableOperation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.sortAsc == other.isSortAsc() &&
            this.sortDsc == other.isSortDsc() &&
            this.advancedSort == other.isAdvancedSort() &&
            this.addGroup == other.isAddGroup() &&
            this.deleteGroup == other.isDeleteGroup() &&
            this.hideColumn == other.isHideColumn() &&
            this.showColumns == other.isShowColumns() &&
            this.reorderColumns == other.isReorderColumns() &&
            this.filter == other.isFilter() &&
            this.calculation == other.isCalculation() &&
            this.aggregation == other.isAggregation() &&
            this.changeFont == other.isChangeFont() &&
            this.format == other.isFormat();
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
        _hashCode += (isSortAsc() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isSortDsc() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAdvancedSort() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAddGroup() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDeleteGroup() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHideColumn() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowColumns() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isReorderColumns() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFilter() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCalculation() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAggregation() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isChangeFont() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFormat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AvailableOperation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AvailableOperation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sortAsc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortAsc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sortDsc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDsc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("advancedSort");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AdvancedSort"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AddGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deleteGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DeleteGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hideColumn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "HideColumn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showColumns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ShowColumns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reorderColumns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReorderColumns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("calculation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Calculation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aggregation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Aggregation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changeFont");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChangeFont"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
