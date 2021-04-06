/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentRoot.java,v 1.1.28.1 2010/11/29 06:23:53 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Document
 * Root</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXMLNSPrefixMap
 * <em>XMLNS Prefix Map</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXSISchemaLocation
 * <em>XSI Schema Location</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameter
 * <em>Data Set Parameter</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameters
 * <em>Data Set Parameters</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDesignValues
 * <em>Design Values</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDynamicList
 * <em>Dynamic List</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list
	 * contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry"
	 *        many="true" extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map. The key is
	 * of type {@link java.lang.String}, and the value is of type
	 * {@link java.lang.String}, <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString,
	 *        org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap<String, String> getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map. The key
	 * is of type {@link java.lang.String}, and the value is of type
	 * {@link java.lang.String}, <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString,
	 *        org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap<String, String> getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Data Set Parameter</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Set Parameter</em>' containment reference
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Data Set Parameter</em>' containment reference.
	 * @see #setDataSetParameter(DataSetParameter)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_DataSetParameter()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData="kind='element'
	 *        name='DataSetParameter' namespace='##targetNamespace'"
	 * @generated
	 */
	DataSetParameter getDataSetParameter();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameter
	 * <em>Data Set Parameter</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Data Set Parameter</em>' containment
	 *              reference.
	 * @see #getDataSetParameter()
	 * @generated
	 */
	void setDataSetParameter(DataSetParameter value);

	/**
	 * Returns the value of the '<em><b>Data Set Parameters</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Set Parameters</em>' containment reference
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Data Set Parameters</em>' containment
	 *         reference.
	 * @see #setDataSetParameters(DataSetParameters)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_DataSetParameters()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData="kind='element'
	 *        name='DataSetParameters' namespace='##targetNamespace'"
	 * @generated
	 */
	DataSetParameters getDataSetParameters();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameters
	 * <em>Data Set Parameters</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Data Set Parameters</em>' containment
	 *              reference.
	 * @see #getDataSetParameters()
	 * @generated
	 */
	void setDataSetParameters(DataSetParameters value);

	/**
	 * Returns the value of the '<em><b>Design Values</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> A collection of ODA related values. Includes data set parameters and
	 * result set columns. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Design Values</em>' containment reference.
	 * @see #setDesignValues(DesignValues)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_DesignValues()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData="kind='element' name='DesignValues'
	 *        namespace='##targetNamespace'"
	 * @generated
	 */
	DesignValues getDesignValues();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDesignValues
	 * <em>Design Values</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Design Values</em>' containment
	 *              reference.
	 * @see #getDesignValues()
	 * @generated
	 */
	void setDesignValues(DesignValues value);

	/**
	 * Returns the value of the '<em><b>Dynamic List</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dynamic List</em>' containment reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Dynamic List</em>' containment reference.
	 * @see #setDynamicList(DynamicList)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDocumentRoot_DynamicList()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData="kind='element' name='DynamicList'
	 *        namespace='##targetNamespace'"
	 * @generated
	 */
	DynamicList getDynamicList();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDynamicList
	 * <em>Dynamic List</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Dynamic List</em>' containment
	 *              reference.
	 * @see #getDynamicList()
	 * @generated
	 */
	void setDynamicList(DynamicList value);

} // DocumentRoot
