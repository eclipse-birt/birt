/**
 * Filter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Filter  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.FilterType type;
    private org.eclipse.birt.report.soapengine.api.FilterExpression expression;
    private org.eclipse.birt.report.soapengine.api.ReportParameterList reportParameterList;
    private org.eclipse.birt.report.soapengine.api.ConditionLineList conditionLineList;

    public Filter() {
    }

    public Filter(
           org.eclipse.birt.report.soapengine.api.FilterType type,
           org.eclipse.birt.report.soapengine.api.FilterExpression expression,
           org.eclipse.birt.report.soapengine.api.ReportParameterList reportParameterList,
           org.eclipse.birt.report.soapengine.api.ConditionLineList conditionLineList) {
           this.type = type;
           this.expression = expression;
           this.reportParameterList = reportParameterList;
           this.conditionLineList = conditionLineList;
    }


    /**
     * Gets the type value for this Filter.
     * 
     * @return type
     */
    public org.eclipse.birt.report.soapengine.api.FilterType getType() {
        return type;
    }


    /**
     * Sets the type value for this Filter.
     * 
     * @param type
     */
    public void setType(org.eclipse.birt.report.soapengine.api.FilterType type) {
        this.type = type;
    }


    /**
     * Gets the expression value for this Filter.
     * 
     * @return expression
     */
    public org.eclipse.birt.report.soapengine.api.FilterExpression getExpression() {
        return expression;
    }


    /**
     * Sets the expression value for this Filter.
     * 
     * @param expression
     */
    public void setExpression(org.eclipse.birt.report.soapengine.api.FilterExpression expression) {
        this.expression = expression;
    }


    /**
     * Gets the reportParameterList value for this Filter.
     * 
     * @return reportParameterList
     */
    public org.eclipse.birt.report.soapengine.api.ReportParameterList getReportParameterList() {
        return reportParameterList;
    }


    /**
     * Sets the reportParameterList value for this Filter.
     * 
     * @param reportParameterList
     */
    public void setReportParameterList(org.eclipse.birt.report.soapengine.api.ReportParameterList reportParameterList) {
        this.reportParameterList = reportParameterList;
    }


    /**
     * Gets the conditionLineList value for this Filter.
     * 
     * @return conditionLineList
     */
    public org.eclipse.birt.report.soapengine.api.ConditionLineList getConditionLineList() {
        return conditionLineList;
    }


    /**
     * Sets the conditionLineList value for this Filter.
     * 
     * @param conditionLineList
     */
    public void setConditionLineList(org.eclipse.birt.report.soapengine.api.ConditionLineList conditionLineList) {
        this.conditionLineList = conditionLineList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Filter)) return false;
        Filter other = (Filter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.expression==null && other.getExpression()==null) || 
             (this.expression!=null &&
              this.expression.equals(other.getExpression()))) &&
            ((this.reportParameterList==null && other.getReportParameterList()==null) || 
             (this.reportParameterList!=null &&
              this.reportParameterList.equals(other.getReportParameterList()))) &&
            ((this.conditionLineList==null && other.getConditionLineList()==null) || 
             (this.conditionLineList!=null &&
              this.conditionLineList.equals(other.getConditionLineList())));
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
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getExpression() != null) {
            _hashCode += getExpression().hashCode();
        }
        if (getReportParameterList() != null) {
            _hashCode += getReportParameterList().hashCode();
        }
        if (getConditionLineList() != null) {
            _hashCode += getConditionLineList().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Filter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Filter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FilterType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expression");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Expression"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FilterExpression"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportParameterList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportParameterList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportParameterList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conditionLineList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ConditionLineList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ConditionLineList"));
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
