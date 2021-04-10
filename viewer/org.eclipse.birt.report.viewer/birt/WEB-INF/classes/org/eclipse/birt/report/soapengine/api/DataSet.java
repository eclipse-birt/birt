/**
 * DataSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class DataSet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private java.lang.String name;
	private java.lang.String displayName;
	private java.lang.String description;
	private boolean isDeletable;
	private boolean isModifiable;
	private org.eclipse.birt.report.soapengine.api.DataSetDefinition definition;
	private org.eclipse.birt.report.soapengine.api.DataSource dataSource;

	public DataSet() {
	}

	public DataSet(long id, java.lang.String name, java.lang.String displayName, java.lang.String description,
			boolean isDeletable, boolean isModifiable,
			org.eclipse.birt.report.soapengine.api.DataSetDefinition definition,
			org.eclipse.birt.report.soapengine.api.DataSource dataSource) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
		this.description = description;
		this.isDeletable = isDeletable;
		this.isModifiable = isModifiable;
		this.definition = definition;
		this.dataSource = dataSource;
	}

	/**
	 * Gets the id value for this DataSet.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value for this DataSet.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name value for this DataSet.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this DataSet.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the displayName value for this DataSet.
	 * 
	 * @return displayName
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the displayName value for this DataSet.
	 * 
	 * @param displayName
	 */
	public void setDisplayName(java.lang.String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the description value for this DataSet.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this DataSet.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the isDeletable value for this DataSet.
	 * 
	 * @return isDeletable
	 */
	public boolean isIsDeletable() {
		return isDeletable;
	}

	/**
	 * Sets the isDeletable value for this DataSet.
	 * 
	 * @param isDeletable
	 */
	public void setIsDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}

	/**
	 * Gets the isModifiable value for this DataSet.
	 * 
	 * @return isModifiable
	 */
	public boolean isIsModifiable() {
		return isModifiable;
	}

	/**
	 * Sets the isModifiable value for this DataSet.
	 * 
	 * @param isModifiable
	 */
	public void setIsModifiable(boolean isModifiable) {
		this.isModifiable = isModifiable;
	}

	/**
	 * Gets the definition value for this DataSet.
	 * 
	 * @return definition
	 */
	public org.eclipse.birt.report.soapengine.api.DataSetDefinition getDefinition() {
		return definition;
	}

	/**
	 * Sets the definition value for this DataSet.
	 * 
	 * @param definition
	 */
	public void setDefinition(org.eclipse.birt.report.soapengine.api.DataSetDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Gets the dataSource value for this DataSet.
	 * 
	 * @return dataSource
	 */
	public org.eclipse.birt.report.soapengine.api.DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the dataSource value for this DataSet.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(org.eclipse.birt.report.soapengine.api.DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DataSet))
			return false;
		DataSet other = (DataSet) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && this.id == other.getId()
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.displayName == null && other.getDisplayName() == null)
						|| (this.displayName != null && this.displayName.equals(other.getDisplayName())))
				&& ((this.description == null && other.getDescription() == null)
						|| (this.description != null && this.description.equals(other.getDescription())))
				&& this.isDeletable == other.isIsDeletable() && this.isModifiable == other.isIsModifiable()
				&& ((this.definition == null && other.getDefinition() == null)
						|| (this.definition != null && this.definition.equals(other.getDefinition())))
				&& ((this.dataSource == null && other.getDataSource() == null)
						|| (this.dataSource != null && this.dataSource.equals(other.getDataSource())));
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
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getDisplayName() != null) {
			_hashCode += getDisplayName().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		_hashCode += (isIsDeletable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isIsModifiable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		if (getDefinition() != null) {
			_hashCode += getDefinition().hashCode();
		}
		if (getDataSource() != null) {
			_hashCode += getDataSource().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			DataSet.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSet"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("id");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("name");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Name"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("displayName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DisplayName"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Description"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("isDeletable");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsDeletable"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("isModifiable");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsModifiable"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("definition");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Definition"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSetDefinition"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataSource");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSource"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSource"));
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
