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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Tooltip
 * Value</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type defines the value for a 'Show_Tooltip' action.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getText
 * <em>Text</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay
 * <em>Delay</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue()
 * @model
 * @generated
 */
public interface TooltipValue extends ActionValue {

	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Attribute "Text" specifies the tooltip text.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue_Text()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Text'"
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getText
	 * <em>Text</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

	/**
	 * Returns the value of the '<em><b>Delay</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Attribute "Delay" specifies the delay in milliseconds afer which the tooltip
	 * is to be shown.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Delay</em>' attribute.
	 * @see #isSetDelay()
	 * @see #unsetDelay()
	 * @see #setDelay(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue_Delay()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        required="true" extendedMetaData="kind='element' name='Delay'"
	 * @generated
	 */
	int getDelay();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay
	 * <em>Delay</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Delay</em>' attribute.
	 * @see #isSetDelay()
	 * @see #unsetDelay()
	 * @see #getDelay()
	 * @generated
	 */
	void setDelay(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay
	 * <em>Delay</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetDelay()
	 * @see #getDelay()
	 * @see #setDelay(int)
	 * @generated
	 */
	void unsetDelay();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay
	 * <em>Delay</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return whether the value of the '<em>Delay</em>' attribute is set.
	 * @see #unsetDelay()
	 * @see #getDelay()
	 * @see #setDelay(int)
	 * @generated
	 */
	boolean isSetDelay();

	/**
	 * Returns the value of the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 *
	 * Attribute "FormatSpecifier" specifies the tooltip format. <!-- end-model-doc
	 * -->
	 *
	 * @return the value of the '<em>Format Specifier</em>' containment reference.
	 * @see #setFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue_FormatSpecifier()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='FormatSpecifier'"
	 * @generated
	 */
	FormatSpecifier getFormatSpecifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getFormatSpecifier
	 * <em>Format Specifier</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Format Specifier</em>' containment
	 *              reference.
	 * @see #getFormatSpecifier()
	 * @generated
	 */
	void setFormatSpecifier(FormatSpecifier value);

	/**
	 * @generated
	 */
	@Override
	TooltipValue copyInstance();

} // TooltipValue
