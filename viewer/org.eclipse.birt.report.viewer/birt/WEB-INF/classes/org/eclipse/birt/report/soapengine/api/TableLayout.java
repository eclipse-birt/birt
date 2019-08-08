/**
 * TableLayout.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class TableLayout  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	/** Design id of table */
    private long id;
    /** Number of sections */
    private int s_Count;
    /** Number of ERNI groups */
    private int g_Count;
    /** The information of column header row */
    private org.eclipse.birt.report.soapengine.api.TableRowInfo CH_Row;
    /** The information of the table level chart row */
    private org.eclipse.birt.report.soapengine.api.TableRowInfo TC_Row;
    private org.eclipse.birt.report.soapengine.api.TableSections sections;
    private org.eclipse.birt.report.soapengine.api.TableGroups groups;

    public TableLayout() {
    }

    public TableLayout(
           long id,
           int s_Count,
           int g_Count,
           org.eclipse.birt.report.soapengine.api.TableRowInfo CH_Row,
           org.eclipse.birt.report.soapengine.api.TableRowInfo TC_Row,
           org.eclipse.birt.report.soapengine.api.TableSections sections,
           org.eclipse.birt.report.soapengine.api.TableGroups groups) {
           this.id = id;
           this.s_Count = s_Count;
           this.g_Count = g_Count;
           this.CH_Row = CH_Row;
           this.TC_Row = TC_Row;
           this.sections = sections;
           this.groups = groups;
    }


    /**
     * Gets the id value for this TableLayout.
     * 
     * @return id Design id of table
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the id value for this TableLayout.
     * 
     * @param id Design id of table
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Gets the s_Count value for this TableLayout.
     * 
     * @return s_Count Number of sections
     */
    public int getS_Count() {
        return s_Count;
    }


    /**
     * Sets the s_Count value for this TableLayout.
     * 
     * @param s_Count Number of sections
     */
    public void setS_Count(int s_Count) {
        this.s_Count = s_Count;
    }


    /**
     * Gets the g_Count value for this TableLayout.
     * 
     * @return g_Count Number of ERNI groups
     */
    public int getG_Count() {
        return g_Count;
    }


    /**
     * Sets the g_Count value for this TableLayout.
     * 
     * @param g_Count Number of ERNI groups
     */
    public void setG_Count(int g_Count) {
        this.g_Count = g_Count;
    }


    /**
     * Gets the CH_Row value for this TableLayout.
     * 
     * @return CH_Row The information of column header row
     */
    public org.eclipse.birt.report.soapengine.api.TableRowInfo getCH_Row() {
        return CH_Row;
    }


    /**
     * Sets the CH_Row value for this TableLayout.
     * 
     * @param CH_Row The information of column header row
     */
    public void setCH_Row(org.eclipse.birt.report.soapengine.api.TableRowInfo CH_Row) {
        this.CH_Row = CH_Row;
    }


    /**
     * Gets the TC_Row value for this TableLayout.
     * 
     * @return TC_Row The information of the table level chart row
     */
    public org.eclipse.birt.report.soapengine.api.TableRowInfo getTC_Row() {
        return TC_Row;
    }


    /**
     * Sets the TC_Row value for this TableLayout.
     * 
     * @param TC_Row The information of the table level chart row
     */
    public void setTC_Row(org.eclipse.birt.report.soapengine.api.TableRowInfo TC_Row) {
        this.TC_Row = TC_Row;
    }


    /**
     * Gets the sections value for this TableLayout.
     * 
     * @return sections
     */
    public org.eclipse.birt.report.soapengine.api.TableSections getSections() {
        return sections;
    }


    /**
     * Sets the sections value for this TableLayout.
     * 
     * @param sections
     */
    public void setSections(org.eclipse.birt.report.soapengine.api.TableSections sections) {
        this.sections = sections;
    }


    /**
     * Gets the groups value for this TableLayout.
     * 
     * @return groups
     */
    public org.eclipse.birt.report.soapengine.api.TableGroups getGroups() {
        return groups;
    }


    /**
     * Sets the groups value for this TableLayout.
     * 
     * @param groups
     */
    public void setGroups(org.eclipse.birt.report.soapengine.api.TableGroups groups) {
        this.groups = groups;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TableLayout)) return false;
        TableLayout other = (TableLayout) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.s_Count == other.getS_Count() &&
            this.g_Count == other.getG_Count() &&
            ((this.CH_Row==null && other.getCH_Row()==null) || 
             (this.CH_Row!=null &&
              this.CH_Row.equals(other.getCH_Row()))) &&
            ((this.TC_Row==null && other.getTC_Row()==null) || 
             (this.TC_Row!=null &&
              this.TC_Row.equals(other.getTC_Row()))) &&
            ((this.sections==null && other.getSections()==null) || 
             (this.sections!=null &&
              this.sections.equals(other.getSections()))) &&
            ((this.groups==null && other.getGroups()==null) || 
             (this.groups!=null &&
              this.groups.equals(other.getGroups())));
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
        _hashCode += Long.valueOf(getId()).hashCode();
        _hashCode += getS_Count();
        _hashCode += getG_Count();
        if (getCH_Row() != null) {
            _hashCode += getCH_Row().hashCode();
        }
        if (getTC_Row() != null) {
            _hashCode += getTC_Row().hashCode();
        }
        if (getSections() != null) {
            _hashCode += getSections().hashCode();
        }
        if (getGroups() != null) {
            _hashCode += getGroups().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TableLayout.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableLayout"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("s_Count");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "S_Count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("g_Count");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "G_Count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CH_Row");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CH_Row"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableRowInfo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TC_Row");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TC_Row"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableRowInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sections");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Sections"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableSections"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groups");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Groups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableGroups"));
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
