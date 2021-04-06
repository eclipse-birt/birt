/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.data;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.RuleType;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Rule</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type represents a rule to process a query expression.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.Rule#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.Rule#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getRule()
 * @model extendedMetaData="name='Rule' kind='elementOnly'"
 * @deprecated only reserved for compatibility
 */
public interface Rule extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The default value
	 * is <code>"Filter"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.RuleType}. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(RuleType)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getRule_Type()
	 * @model default="Filter" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	RuleType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.Rule#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(RuleType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Rule#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(RuleType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Rule#getType <em>Type</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(RuleType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getRule_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Value'"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.Rule#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * @generated
	 */
	Rule copyInstance();

} // Rule
