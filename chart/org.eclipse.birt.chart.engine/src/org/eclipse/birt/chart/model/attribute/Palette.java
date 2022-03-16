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
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Palette</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Palette represents a palette of Fills.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Palette#getName
 * <em>Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Palette#getEntries
 * <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPalette()
 * @model extendedMetaData="name='Palette' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Palette extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Name" specifies the name that uniquely identifies this palette.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPalette_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Palette#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> Sets the name for the
	 * palette. <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.attribute.Fill}. <!-- begin-user-doc -->
	 * Gets the entries defined in the palette. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 *
	 * Holds the entries contained in the palette.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPalette_Entries()
	 * @model type="org.eclipse.birt.chart.model.attribute.Fill" containment="true"
	 *        resolveProxies="false" required="true"
	 * @generated
	 */
	EList<Fill> getEntries();

	/**
	 * This convenience method updates the content of a palette with a library of
	 * colors
	 *
	 * NOTE: Manually written
	 *
	 * @param iIndex
	 * @deprecated to use {@link #shift(int)}
	 * @see #shift(int)
	 */
	@Deprecated
	void update(int iIndex);

	/**
	 * This convenience method updates the content of a palette with a single color
	 *
	 * NOTE: Manually written
	 *
	 * @param f
	 */
	void update(Fill f);

	/**
	 * Shifts the colors in palette with the steps.
	 *
	 * NOTE: Manually written
	 *
	 * @param iStep moving steps to rotate the color. If the step is zero or the
	 *              absolute value is greater than the size of list, do nothing.
	 *              Negative value means moving to the left side, and positive value
	 *              is to the right side.
	 * @since 2.2
	 */
	void shift(int iStep);

	/**
	 * Re-creates the palette colors with specified size and shifts the colors in
	 * palette with the steps.
	 *
	 * NOTE: Manually written
	 *
	 * @param iStep moving steps to rotate the color. If the step is zero or the
	 *              absolute value is greater than the size of list, do nothing.
	 *              Negative value means moving to the left side, and positive value
	 *              is to the right side.
	 * @param iSize the size of color library to create
	 * @since 2.2
	 */
	void shift(int iStep, int iSize);

	/**
	 * @generated
	 */
	@Override
	Palette copyInstance();

} // Palette
