/**
 * FormatRule.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class FormatRule implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.FormatRuleCondition condition;
	private org.eclipse.birt.report.soapengine.api.FormatRuleEffect effect;

	public FormatRule() {
	}

	public FormatRule(org.eclipse.birt.report.soapengine.api.FormatRuleCondition condition,
			org.eclipse.birt.report.soapengine.api.FormatRuleEffect effect) {
		this.condition = condition;
		this.effect = effect;
	}

	/**
	 * Gets the condition value for this FormatRule.
	 * 
	 * @return condition
	 */
	public org.eclipse.birt.report.soapengine.api.FormatRuleCondition getCondition() {
		return condition;
	}

	/**
	 * Sets the condition value for this FormatRule.
	 * 
	 * @param condition
	 */
	public void setCondition(org.eclipse.birt.report.soapengine.api.FormatRuleCondition condition) {
		this.condition = condition;
	}

	/**
	 * Gets the effect value for this FormatRule.
	 * 
	 * @return effect
	 */
	public org.eclipse.birt.report.soapengine.api.FormatRuleEffect getEffect() {
		return effect;
	}

	/**
	 * Sets the effect value for this FormatRule.
	 * 
	 * @param effect
	 */
	public void setEffect(org.eclipse.birt.report.soapengine.api.FormatRuleEffect effect) {
		this.effect = effect;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FormatRule))
			return false;
		FormatRule other = (FormatRule) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.condition == null && other.getCondition() == null)
						|| (this.condition != null && this.condition.equals(other.getCondition())))
				&& ((this.effect == null && other.getEffect() == null)
						|| (this.effect != null && this.effect.equals(other.getEffect())));
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
		if (getCondition() != null) {
			_hashCode += getCondition().hashCode();
		}
		if (getEffect() != null) {
			_hashCode += getEffect().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			FormatRule.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRule"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("condition");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Condition"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRuleCondition"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("effect");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Effect"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRuleEffect"));
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
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
