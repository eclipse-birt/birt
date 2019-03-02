/**
 * CellDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class CellDefinition  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private int level;
    private boolean isHeader;
    private int rowIndex;
    private int cellIndex;
    private org.eclipse.birt.report.soapengine.api.Font font;
    private org.eclipse.birt.report.soapengine.api.Format format;
    private org.eclipse.birt.report.soapengine.api.ColumnProperties properties;
    private org.eclipse.birt.report.soapengine.api.Alignment alignment;

    public CellDefinition() {
    }

    public CellDefinition(
           int level,
           boolean isHeader,
           int rowIndex,
           int cellIndex,
           org.eclipse.birt.report.soapengine.api.Font font,
           org.eclipse.birt.report.soapengine.api.Format format,
           org.eclipse.birt.report.soapengine.api.ColumnProperties properties,
           org.eclipse.birt.report.soapengine.api.Alignment alignment) {
           this.level = level;
           this.isHeader = isHeader;
           this.rowIndex = rowIndex;
           this.cellIndex = cellIndex;
           this.font = font;
           this.format = format;
           this.properties = properties;
           this.alignment = alignment;
    }


    /**
     * Gets the level value for this CellDefinition.
     * 
     * @return level
     */
    public int getLevel() {
        return level;
    }


    /**
     * Sets the level value for this CellDefinition.
     * 
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }


    /**
     * Gets the isHeader value for this CellDefinition.
     * 
     * @return isHeader
     */
    public boolean isIsHeader() {
        return isHeader;
    }


    /**
     * Sets the isHeader value for this CellDefinition.
     * 
     * @param isHeader
     */
    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }


    /**
     * Gets the rowIndex value for this CellDefinition.
     * 
     * @return rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }


    /**
     * Sets the rowIndex value for this CellDefinition.
     * 
     * @param rowIndex
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }


    /**
     * Gets the cellIndex value for this CellDefinition.
     * 
     * @return cellIndex
     */
    public int getCellIndex() {
        return cellIndex;
    }


    /**
     * Sets the cellIndex value for this CellDefinition.
     * 
     * @param cellIndex
     */
    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }


    /**
     * Gets the font value for this CellDefinition.
     * 
     * @return font
     */
    public org.eclipse.birt.report.soapengine.api.Font getFont() {
        return font;
    }


    /**
     * Sets the font value for this CellDefinition.
     * 
     * @param font
     */
    public void setFont(org.eclipse.birt.report.soapengine.api.Font font) {
        this.font = font;
    }


    /**
     * Gets the format value for this CellDefinition.
     * 
     * @return format
     */
    public org.eclipse.birt.report.soapengine.api.Format getFormat() {
        return format;
    }


    /**
     * Sets the format value for this CellDefinition.
     * 
     * @param format
     */
    public void setFormat(org.eclipse.birt.report.soapengine.api.Format format) {
        this.format = format;
    }


    /**
     * Gets the properties value for this CellDefinition.
     * 
     * @return properties
     */
    public org.eclipse.birt.report.soapengine.api.ColumnProperties getProperties() {
        return properties;
    }


    /**
     * Sets the properties value for this CellDefinition.
     * 
     * @param properties
     */
    public void setProperties(org.eclipse.birt.report.soapengine.api.ColumnProperties properties) {
        this.properties = properties;
    }


    /**
     * Gets the alignment value for this CellDefinition.
     * 
     * @return alignment
     */
    public org.eclipse.birt.report.soapengine.api.Alignment getAlignment() {
        return alignment;
    }


    /**
     * Sets the alignment value for this CellDefinition.
     * 
     * @param alignment
     */
    public void setAlignment(org.eclipse.birt.report.soapengine.api.Alignment alignment) {
        this.alignment = alignment;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CellDefinition)) return false;
        CellDefinition other = (CellDefinition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.level == other.getLevel() &&
            this.isHeader == other.isIsHeader() &&
            this.rowIndex == other.getRowIndex() &&
            this.cellIndex == other.getCellIndex() &&
            ((this.font==null && other.getFont()==null) || 
             (this.font!=null &&
              this.font.equals(other.getFont()))) &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.properties==null && other.getProperties()==null) || 
             (this.properties!=null &&
              this.properties.equals(other.getProperties()))) &&
            ((this.alignment==null && other.getAlignment()==null) || 
             (this.alignment!=null &&
              this.alignment.equals(other.getAlignment())));
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
        _hashCode += getLevel();
        _hashCode += (isIsHeader() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getRowIndex();
        _hashCode += getCellIndex();
        if (getFont() != null) {
            _hashCode += getFont().hashCode();
        }
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        if (getProperties() != null) {
            _hashCode += getProperties().hashCode();
        }
        if (getAlignment() != null) {
            _hashCode += getAlignment().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CellDefinition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CellDefinition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("level");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Level"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isHeader");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsHeader"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rowIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "RowIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cellIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CellIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("font");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("properties");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Properties"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnProperties"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alignment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Alignment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Alignment"));
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
