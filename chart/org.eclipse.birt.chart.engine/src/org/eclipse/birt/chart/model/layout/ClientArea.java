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

package org.eclipse.birt.chart.model.layout;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Client
 * Area</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type defines an modular area of a chart that can be positioned (and
 * resized ?) during rendering / output.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.ClientArea#getBackground
 * <em>Background</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.ClientArea#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.ClientArea#getShadowColor
 * <em>Shadow Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.ClientArea#getInsets
 * <em>Insets</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getClientArea()
 * @model
 * @generated
 */
public interface ClientArea extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the background fill for the client area. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds the background for the client area.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Background</em>' containment reference.
	 * @see #setBackground(Fill)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getClientArea_Background()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Fill getBackground();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getBackground
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
	 * <!-- begin-user-doc --> Gets the attributes of the border for the client
	 * area. <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds the outline information for the client area.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Outline</em>' containment reference.
	 * @see #setOutline(LineAttributes)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getClientArea_Outline()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getOutline();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getOutline
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
	 * reference. <!-- begin-user-doc --> Gets the shadow color of the client area.
	 * If this is not specified, the client area will not have a shadow. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies the shadow color for the client area.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Shadow Color</em>' containment reference.
	 * @see #setShadowColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getClientArea_ShadowColor()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getShadowColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getShadowColor
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
	 * Specifies the insets for the client area.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Insets</em>' containment reference.
	 * @see #setInsets(Insets)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getClientArea_Insets()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Insets getInsets();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getInsets
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
	 * <!-- begin-model-doc --> Attribute "Visible" specifies whether client area is
	 * visible. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #setVisible(boolean)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getClientArea_Visible()
	 * @model default="true" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='Visible'"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#isVisible
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
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetVisible()
	 * @see #isVisible()
	 * @see #setVisible(boolean)
	 * @generated
	 */
	void unsetVisible();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#isVisible
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
	@Override
	ClientArea copyInstance();

} // ClientArea
