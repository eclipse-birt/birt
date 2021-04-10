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

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Style
 * Map</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> StyleMap represents the style map for the chart. It
 * includes a list of supported chart components and the styles associated with
 * them.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName
 * <em>Component Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.StyleMap#getStyle
 * <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyleMap()
 * @model extendedMetaData="name='StyleMap' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface StyleMap extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Component Name</b></em>' attribute. The
	 * default value is <code>"Chart_All"</code>. The literals are from the
	 * enumeration {@link org.eclipse.birt.chart.model.attribute.StyledComponent}.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the name of a chart component that can have styles associated with
	 * it.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Component Name</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @see #isSetComponentName()
	 * @see #unsetComponentName()
	 * @see #setComponentName(StyledComponent)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyleMap_ComponentName()
	 * @model default="Chart_All" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='ComponentName'"
	 * @generated
	 */
	StyledComponent getComponentName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName
	 * <em>Component Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Component Name</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @see #isSetComponentName()
	 * @see #unsetComponentName()
	 * @see #getComponentName()
	 * @generated
	 */
	void setComponentName(StyledComponent value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName
	 * <em>Component Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetComponentName()
	 * @see #getComponentName()
	 * @see #setComponentName(StyledComponent)
	 * @generated
	 */
	void unsetComponentName();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName
	 * <em>Component Name</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Component Name</em>' attribute is set.
	 * @see #unsetComponentName()
	 * @see #getComponentName()
	 * @see #setComponentName(StyledComponent)
	 * @generated
	 */
	boolean isSetComponentName();

	/**
	 * Returns the value of the '<em><b>Style</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the style identifier(s) to be associated with this entry.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Style</em>' containment reference.
	 * @see #setStyle(Style)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyleMap_Style()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Style'"
	 * @generated
	 */
	Style getStyle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getStyle
	 * <em>Style</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Style</em>' containment reference.
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(Style value);

	/**
	 * @generated
	 */
	StyleMap copyInstance();

} // StyleMap
