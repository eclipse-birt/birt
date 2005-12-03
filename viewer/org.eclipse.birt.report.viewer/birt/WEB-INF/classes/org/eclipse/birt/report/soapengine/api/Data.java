/**
 * Data.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Data  implements java.io.Serializable {
    private org.eclipse.birt.report.soapengine.api.Font font;
    private org.eclipse.birt.report.soapengine.api.Columns columns;
    private org.eclipse.birt.report.soapengine.api.Format format;
    private org.eclipse.birt.report.soapengine.api.TOC TOC;
    private org.eclipse.birt.report.soapengine.api.Export export;
    private org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter;
    private org.eclipse.birt.report.soapengine.api.SimpleFilter simpleFilter;
    private org.eclipse.birt.report.soapengine.api.Vector columnValues;
    private org.eclipse.birt.report.soapengine.api.Page page;

    public Data() {
    }

    public org.eclipse.birt.report.soapengine.api.Font getFont() {
        return font;
    }

    public void setFont(org.eclipse.birt.report.soapengine.api.Font font) {
        this.font = font;
    }

    public org.eclipse.birt.report.soapengine.api.Columns getColumns() {
        return columns;
    }

    public void setColumns(org.eclipse.birt.report.soapengine.api.Columns columns) {
        this.columns = columns;
    }

    public org.eclipse.birt.report.soapengine.api.Format getFormat() {
        return format;
    }

    public void setFormat(org.eclipse.birt.report.soapengine.api.Format format) {
        this.format = format;
    }

    public org.eclipse.birt.report.soapengine.api.TOC getTOC() {
        return TOC;
    }

    public void setTOC(org.eclipse.birt.report.soapengine.api.TOC TOC) {
        this.TOC = TOC;
    }

    public org.eclipse.birt.report.soapengine.api.Export getExport() {
        return export;
    }

    public void setExport(org.eclipse.birt.report.soapengine.api.Export export) {
        this.export = export;
    }

    public org.eclipse.birt.report.soapengine.api.CascadeParameter getCascadeParameter() {
        return cascadeParameter;
    }

    public void setCascadeParameter(org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter) {
        this.cascadeParameter = cascadeParameter;
    }

    public org.eclipse.birt.report.soapengine.api.SimpleFilter getSimpleFilter() {
        return simpleFilter;
    }

    public void setSimpleFilter(org.eclipse.birt.report.soapengine.api.SimpleFilter simpleFilter) {
        this.simpleFilter = simpleFilter;
    }

    public org.eclipse.birt.report.soapengine.api.Vector getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(org.eclipse.birt.report.soapengine.api.Vector columnValues) {
        this.columnValues = columnValues;
    }

    public org.eclipse.birt.report.soapengine.api.Page getPage() {
        return page;
    }

    public void setPage(org.eclipse.birt.report.soapengine.api.Page page) {
        this.page = page;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Data)) return false;
        Data other = (Data) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.font==null && other.getFont()==null) || 
             (this.font!=null &&
              this.font.equals(other.getFont()))) &&
            ((this.columns==null && other.getColumns()==null) || 
             (this.columns!=null &&
              this.columns.equals(other.getColumns()))) &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.TOC==null && other.getTOC()==null) || 
             (this.TOC!=null &&
              this.TOC.equals(other.getTOC()))) &&
            ((this.export==null && other.getExport()==null) || 
             (this.export!=null &&
              this.export.equals(other.getExport()))) &&
            ((this.cascadeParameter==null && other.getCascadeParameter()==null) || 
             (this.cascadeParameter!=null &&
              this.cascadeParameter.equals(other.getCascadeParameter()))) &&
            ((this.simpleFilter==null && other.getSimpleFilter()==null) || 
             (this.simpleFilter!=null &&
              this.simpleFilter.equals(other.getSimpleFilter()))) &&
            ((this.columnValues==null && other.getColumnValues()==null) || 
             (this.columnValues!=null &&
              this.columnValues.equals(other.getColumnValues()))) &&
            ((this.page==null && other.getPage()==null) || 
             (this.page!=null &&
              this.page.equals(other.getPage())));
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
        if (getFont() != null) {
            _hashCode += getFont().hashCode();
        }
        if (getColumns() != null) {
            _hashCode += getColumns().hashCode();
        }
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        if (getTOC() != null) {
            _hashCode += getTOC().hashCode();
        }
        if (getExport() != null) {
            _hashCode += getExport().hashCode();
        }
        if (getCascadeParameter() != null) {
            _hashCode += getCascadeParameter().hashCode();
        }
        if (getSimpleFilter() != null) {
            _hashCode += getSimpleFilter().hashCode();
        }
        if (getColumnValues() != null) {
            _hashCode += getColumnValues().hashCode();
        }
        if (getPage() != null) {
            _hashCode += getPage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Data.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Data"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("font");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Columns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Columns"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TOC");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("export");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cascadeParameter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("simpleFilter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SimpleFilter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SimpleFilter"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("page");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page"));
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
