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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Text;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Label</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type defines a text label. It is not intended
 * as a standalone element but should be associated with a chart element. <!--
 * end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#getCaption
 * <em>Caption</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#getBackground
 * <em>Background</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#getShadowColor
 * <em>Shadow Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#getInsets
 * <em>Insets</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Label#getEllipsis
 * <em>Ellipsis</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel()
 * @model extendedMetaData="name='Label' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Label extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Caption</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The string content displayed in the label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Caption</em>' containment reference.
	 * @see #setCaption(Text)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_Caption()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Text getCaption();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getCaption
	 * <em>Caption</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Caption</em>' containment reference.
	 * @see #getCaption()
	 * @generated
	 */
	void setCaption(Text value);

	/**
	 * Returns the value of the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the background for the label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Background</em>' containment reference.
	 * @see #setBackground(Fill)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_Background()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Fill getBackground();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getBackground
	 * <em>Background</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Background</em>' containment
	 *              reference.
	 * @see #getBackground()
	 * @generated
	 */
	void setBackground(Fill value);

	/**
	 * Returns the value of the '<em><b>Outline</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the outline (border) for the label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Outline</em>' containment reference.
	 * @see #setOutline(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_Outline()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getOutline();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getOutline
	 * <em>Outline</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Outline</em>' containment reference.
	 * @see #getOutline()
	 * @generated
	 */
	void setOutline(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Shadow Color</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the shadow color for the label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Shadow Color</em>' containment reference.
	 * @see #setShadowColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_ShadowColor()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getShadowColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getShadowColor
	 * <em>Shadow Color</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Shadow Color</em>' containment
	 *              reference.
	 * @see #getShadowColor()
	 * @generated
	 */
	void setShadowColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the insets for the label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Insets</em>' containment reference.
	 * @see #setInsets(Insets)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_Insets()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Insets getInsets();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getInsets
	 * <em>Insets</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Insets</em>' containment reference.
	 * @see #getInsets()
	 * @generated
	 */
	void setInsets(Insets value);

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute. The default
	 * value is <code>"true"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * Specifies whether or not the label is visible on the chart. By default it's
	 * true;
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #setVisible(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_Visible()
	 * @model default="true" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='Visible'"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#isVisible
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
	 * {@link org.eclipse.birt.chart.model.component.Label#isVisible
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
	 * Returns the value of the '<em><b>Ellipsis</b></em>' attribute. The default
	 * value is <code>"0"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Specifies the min count of characters before ellipsis. 0
	 * means ellipsis won't be used. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Ellipsis</em>' attribute.
	 * @see #isSetEllipsis()
	 * @see #unsetEllipsis()
	 * @see #setEllipsis(int)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getLabel_Ellipsis()
	 * @model default="0" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 *        extendedMetaData="kind='element' name='Ellipsis'"
	 * @generated
	 */
	int getEllipsis();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getEllipsis
	 * <em>Ellipsis</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Ellipsis</em>' attribute.
	 * @see #isSetEllipsis()
	 * @see #unsetEllipsis()
	 * @see #getEllipsis()
	 * @generated
	 */
	void setEllipsis(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getEllipsis
	 * <em>Ellipsis</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetEllipsis()
	 * @see #getEllipsis()
	 * @see #setEllipsis(int)
	 * @generated
	 */
	void unsetEllipsis();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Label#getEllipsis
	 * <em>Ellipsis</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Ellipsis</em>' attribute is set.
	 * @see #unsetEllipsis()
	 * @see #getEllipsis()
	 * @see #setEllipsis(int)
	 * @generated
	 */
	boolean isSetEllipsis();

	/**
	 * @generated
	 */
	Label copyInstance();

} // Label
