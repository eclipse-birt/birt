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

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Accessibility Value</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> AccessibilityValue extends the type ActionValue
 * specific for accessibilities. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue#getText
 * <em>Text</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue#getAccessibility
 * <em>Accessibility</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAccessibilityValue()
 * @model extendedMetaData="name='AccessibilityValue' kind='elementOnly'"
 * @generated
 */
public interface AccessibilityValue extends ActionValue {

	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Text" provides a short description of the accessibility. <!-- end-model-doc
	 * -->
	 * 
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAccessibilityValue_Text()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='Text'"
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue#getText
	 * <em>Text</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

	/**
	 * Returns the value of the '<em><b>Accessibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Accessibility" provides a full description of the accessibility.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Accessibility</em>' attribute.
	 * @see #setAccessibility(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAccessibilityValue_Accessibility()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='Accessibility'"
	 * @generated
	 */
	String getAccessibility();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue#getAccessibility
	 * <em>Accessibility</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Accessibility</em>' attribute.
	 * @see #getAccessibility()
	 * @generated
	 */
	void setAccessibility(String value);

	/**
	 * @generated
	 */
	AccessibilityValue copyInstance();

} // AccessibilityValue
