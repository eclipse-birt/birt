/**
 * Data.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
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

    public Data(
           org.eclipse.birt.report.soapengine.api.Font font,
           org.eclipse.birt.report.soapengine.api.Columns columns,
           org.eclipse.birt.report.soapengine.api.Format format,
           org.eclipse.birt.report.soapengine.api.TOC TOC,
           org.eclipse.birt.report.soapengine.api.Export export,
           org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter,
           org.eclipse.birt.report.soapengine.api.SimpleFilter simpleFilter,
           org.eclipse.birt.report.soapengine.api.Vector columnValues,
           org.eclipse.birt.report.soapengine.api.Page page) {
           this.font = font;
           this.columns = columns;
           this.format = format;
           this.TOC = TOC;
           this.export = export;
           this.cascadeParameter = cascadeParameter;
           this.simpleFilter = simpleFilter;
           this.columnValues = columnValues;
           this.page = page;
    }


    /**
     * Gets the font value for this Data.
     * 
     * @return font
     */
    public org.eclipse.birt.report.soapengine.api.Font getFont() {
        return font;
    }


    /**
     * Sets the font value for this Data.
     * 
     * @param font
     */
    public void setFont(org.eclipse.birt.report.soapengine.api.Font font) {
        this.font = font;
    }


    /**
     * Gets the columns value for this Data.
     * 
     * @return columns
     */
    public org.eclipse.birt.report.soapengine.api.Columns getColumns() {
        return columns;
    }


    /**
     * Sets the columns value for this Data.
     * 
     * @param columns
     */
    public void setColumns(org.eclipse.birt.report.soapengine.api.Columns columns) {
        this.columns = columns;
    }


    /**
     * Gets the format value for this Data.
     * 
     * @return format
     */
    public org.eclipse.birt.report.soapengine.api.Format getFormat() {
        return format;
    }


    /**
     * Sets the format value for this Data.
     * 
     * @param format
     */
    public void setFormat(org.eclipse.birt.report.soapengine.api.Format format) {
        this.format = format;
    }


    /**
     * Gets the TOC value for this Data.
     * 
     * @return TOC
     */
    public org.eclipse.birt.report.soapengine.api.TOC getTOC() {
        return TOC;
    }


    /**
     * Sets the TOC value for this Data.
     * 
     * @param TOC
     */
    public void setTOC(org.eclipse.birt.report.soapengine.api.TOC TOC) {
        this.TOC = TOC;
    }


    /**
     * Gets the export value for this Data.
     * 
     * @return export
     */
    public org.eclipse.birt.report.soapengine.api.Export getExport() {
        return export;
    }


    /**
     * Sets the export value for this Data.
     * 
     * @param export
     */
    public void setExport(org.eclipse.birt.report.soapengine.api.Export export) {
        this.export = export;
    }


    /**
     * Gets the cascadeParameter value for this Data.
     * 
     * @return cascadeParameter
     */
    public org.eclipse.birt.report.soapengine.api.CascadeParameter getCascadeParameter() {
        return cascadeParameter;
    }


    /**
     * Sets the cascadeParameter value for this Data.
     * 
     * @param cascadeParameter
     */
    public void setCascadeParameter(org.eclipse.birt.report.soapengine.api.CascadeParameter cascadeParameter) {
        this.cascadeParameter = cascadeParameter;
    }


    /**
     * Gets the simpleFilter value for this Data.
     * 
     * @return simpleFilter
     */
    public org.eclipse.birt.report.soapengine.api.SimpleFilter getSimpleFilter() {
        return simpleFilter;
    }


    /**
     * Sets the simpleFilter value for this Data.
     * 
     * @param simpleFilter
     */
    public void setSimpleFilter(org.eclipse.birt.report.soapengine.api.SimpleFilter simpleFilter) {
        this.simpleFilter = simpleFilter;
    }


    /**
     * Gets the columnValues value for this Data.
     * 
     * @return columnValues
     */
    public org.eclipse.birt.report.soapengine.api.Vector getColumnValues() {
        return columnValues;
    }


    /**
     * Sets the columnValues value for this Data.
     * 
     * @param columnValues
     */
    public void setColumnValues(org.eclipse.birt.report.soapengine.api.Vector columnValues) {
        this.columnValues = columnValues;
    }


    /**
     * Gets the page value for this Data.
     * 
     * @return page
     */
    public org.eclipse.birt.report.soapengine.api.Page getPage() {
        return page;
    }


    /**
     * Sets the page value for this Data.
     * 
     * @param page
     */
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
        new org.apache.axis.description.TypeDesc(Data.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Data"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("font");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Columns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Columns"));
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
        elemField.setFieldName("TOC");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("export");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cascadeParameter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("simpleFilter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SimpleFilter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SimpleFilter"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("page");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page"));
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
