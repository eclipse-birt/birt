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

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Text</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Text is intended to encapsulate a string to be
 * displayed on the chart.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Text#getValue
 * <em>Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Text#getFont
 * <em>Font</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Text#getColor
 * <em>Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getText()
 * @model extendedMetaData="name='Text' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Text extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the actual Text String. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 *
	 * Holds the actual Text String.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getText_Value()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Text#getValue <em>Value</em>}'
	 * attribute. <!-- begin-user-doc --> Sets the actual Text String. <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Font</b></em>' containment reference. <!--
	 * begin-user-doc --> Gets the Font Information to be used for this text
	 * element. <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds the Font Information to be used for this text element.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Font</em>' containment reference.
	 * @see #setFont(FontDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getText_Font()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	FontDefinition getFont();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Text#getFont <em>Font</em>}'
	 * containment reference. <!-- begin-user-doc --> Sets the Font Information for
	 * this text element. <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Font</em>' containment reference.
	 * @see #getFont()
	 * @generated
	 */
	void setFont(FontDefinition value);

	/**
	 * Returns the value of the '<em><b>Color</b></em>' containment reference. <!--
	 * begin-user-doc --> Gets the foreground color to be used to render the text.
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the color to be used to render the text.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Color</em>' containment reference.
	 * @see #setColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getText_Color()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Text#getColor <em>Color</em>}'
	 * containment reference. <!-- begin-user-doc --> Sets the foreground color to
	 * be used to render the text. <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Color</em>' containment reference.
	 * @see #getColor()
	 * @generated
	 */
	void setColor(ColorDefinition value);

	/**
	 * @generated
	 */
	@Override
	Text copyInstance();

} // Text
