/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>URL
 * Value</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> URLValue extends type ActionValue to devote itself
 * to 'URL_Redirect' actions.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseUrl
 * <em>Base Url</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.URLValue#getTarget
 * <em>Target</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseParameterName
 * <em>Base Parameter Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.URLValue#getValueParameterName
 * <em>Value Parameter Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.URLValue#getSeriesParameterName
 * <em>Series Parameter Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.URLValue#getTooltip
 * <em>Tooltip</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue()
 * @model extendedMetaData="name='URLValue' kind='elementOnly'"
 * @generated
 */
public interface URLValue extends ActionValue {

	/**
	 * Returns the value of the '<em><b>Base Url</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the base URL. This should include any static parameters like login
	 * information etc. and should NOT be encoded.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Base Url</em>' attribute.
	 * @see #setBaseUrl(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue_BaseUrl()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='BaseUrl'"
	 * @generated
	 */
	String getBaseUrl();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseUrl <em>Base
	 * Url</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Base Url</em>' attribute.
	 * @see #getBaseUrl()
	 * @generated
	 */
	void setBaseUrl(String value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This element defines the target value to be used for the browser.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Target</em>' attribute.
	 * @see #setTarget(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue_Target()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Target'"
	 * @generated
	 */
	String getTarget();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getTarget
	 * <em>Target</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Target</em>' attribute.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(String value);

	/**
	 * Returns the value of the '<em><b>Base Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This element specifies the name to be used for the parameter whose value will
	 * be picked up from the base axis (if any).
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Base Parameter Name</em>' attribute.
	 * @see #setBaseParameterName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue_BaseParameterName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getBaseParameterName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseParameterName
	 * <em>Base Parameter Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Base Parameter Name</em>' attribute.
	 * @see #getBaseParameterName()
	 * @generated
	 */
	void setBaseParameterName(String value);

	/**
	 * Returns the value of the '<em><b>Value Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This element specifies the name to be used for the parameter whose value will
	 * be picked up from the current data value.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Value Parameter Name</em>' attribute.
	 * @see #setValueParameterName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue_ValueParameterName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getValueParameterName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getValueParameterName
	 * <em>Value Parameter Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value Parameter Name</em>' attribute.
	 * @see #getValueParameterName()
	 * @generated
	 */
	void setValueParameterName(String value);

	/**
	 * Returns the value of the '<em><b>Series Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This element specifies the name to be used for the parameter whose value will
	 * be picked up from the current series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Series Parameter Name</em>' attribute.
	 * @see #setSeriesParameterName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue_SeriesParameterName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getSeriesParameterName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getSeriesParameterName
	 * <em>Series Parameter Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Series Parameter Name</em>' attribute.
	 * @see #getSeriesParameterName()
	 * @generated
	 */
	void setSeriesParameterName(String value);

	/**
	 * Returns the value of the '<em><b>Tooltip</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The element defines a tooltip string to be displayed when mouse is over.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Tooltip</em>' attribute.
	 * @see #setTooltip(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getURLValue_Tooltip()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Tooltip'"
	 * @generated
	 */
	String getTooltip();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getTooltip
	 * <em>Tooltip</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Tooltip</em>' attribute.
	 * @see #getTooltip()
	 * @generated
	 */
	void setTooltip(String value);

	/**
	 * @generated
	 */
	URLValue copyInstance();

} // URLValue
