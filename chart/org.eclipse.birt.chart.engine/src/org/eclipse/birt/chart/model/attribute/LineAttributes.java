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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Line
 * Attributes</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> LineAttributes represents a holder of all settings
 * for rendering a line in the chart graphic.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle
 * <em>Style</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness
 * <em>Thickness</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getColor
 * <em>Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible
 * <em>Visible</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineAttributes()
 * @model extendedMetaData="name='LineAttributes' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface LineAttributes extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Style</b></em>' attribute. The default value
	 * is <code>"Solid"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.LineStyle}. <!-- begin-user-doc
	 * --> Gets the line style. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the line style.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Style</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @see #isSetStyle()
	 * @see #unsetStyle()
	 * @see #setStyle(LineStyle)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineAttributes_Style()
	 * @model default="Solid" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	LineStyle getStyle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle
	 * <em>Style</em>}' attribute. <!-- begin-user-doc --> Sets the line style. <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Style</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @see #isSetStyle()
	 * @see #unsetStyle()
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(LineStyle value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle
	 * <em>Style</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetStyle()
	 * @see #getStyle()
	 * @see #setStyle(LineStyle)
	 * @generated
	 */
	void unsetStyle();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle
	 * <em>Style</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Style</em>' attribute is set.
	 * @see #unsetStyle()
	 * @see #getStyle()
	 * @see #setStyle(LineStyle)
	 * @generated
	 */
	boolean isSetStyle();

	/**
	 * Returns the value of the '<em><b>Thickness</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the thickness of the line. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Holds the thickness of the line.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Thickness</em>' attribute.
	 * @see #isSetThickness()
	 * @see #unsetThickness()
	 * @see #setThickness(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineAttributes_Thickness()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getThickness();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness
	 * <em>Thickness</em>}' attribute. <!-- begin-user-doc --> Sets the thickness of
	 * the line. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Thickness</em>' attribute.
	 * @see #isSetThickness()
	 * @see #unsetThickness()
	 * @see #getThickness()
	 * @generated
	 */
	void setThickness(int value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness
	 * <em>Thickness</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetThickness()
	 * @see #getThickness()
	 * @see #setThickness(int)
	 * @generated
	 */
	void unsetThickness();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness
	 * <em>Thickness</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Thickness</em>' attribute is set.
	 * @see #unsetThickness()
	 * @see #getThickness()
	 * @see #setThickness(int)
	 * @generated
	 */
	boolean isSetThickness();

	/**
	 * Returns the value of the '<em><b>Color</b></em>' containment reference. <!--
	 * begin-user-doc --> Gets the line color. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Holds the line color.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Color</em>' containment reference.
	 * @see #setColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineAttributes_Color()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getColor
	 * <em>Color</em>}' containment reference. <!-- begin-user-doc --> Sets the line
	 * color. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Color</em>' containment reference.
	 * @see #getColor()
	 * @generated
	 */
	void setColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether or not the line is to be rendered. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether or not the line is to be rendered.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #setVisible(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineAttributes_Visible()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> Specifies whether or
	 * not the line is to be rendered. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetVisible()
	 * @see #isVisible()
	 * @see #setVisible(boolean)
	 * @generated
	 */
	void unsetVisible();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible
	 * <em>Visible</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Visible</em>' attribute is set.
	 * @see #unsetVisible()
	 * @see #isVisible()
	 * @see #setVisible(boolean)
	 * @generated
	 */
	boolean isSetVisible();

	/**
	 * @generated
	 */
	LineAttributes copyInstance();

} // LineAttributes
