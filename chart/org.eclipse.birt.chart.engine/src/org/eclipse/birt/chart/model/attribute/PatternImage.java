/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Pattern
 * Image</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type represents the pattern image.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBitmap
 * <em>Bitmap</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.PatternImage#getForeColor
 * <em>Fore Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBackColor
 * <em>Back Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPatternImage()
 * @model extendedMetaData="name='PatternImage' kind='elementOnly'"
 * @generated
 */
public interface PatternImage extends Image {

	/**
	 * Returns the value of the '<em><b>Bitmap</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The 8x8
	 * bitmap. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Bitmap</em>' attribute.
	 * @see #isSetBitmap()
	 * @see #unsetBitmap()
	 * @see #setBitmap(long)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPatternImage_Bitmap()
	 * @model unsettable="true"
	 *        dataType="org.eclipse.birt.chart.model.attribute.PatternBitmap"
	 *        required="true" extendedMetaData="kind='element' name='Bitmap'"
	 * @generated
	 */
	long getBitmap();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBitmap
	 * <em>Bitmap</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Bitmap</em>' attribute.
	 * @see #isSetBitmap()
	 * @see #unsetBitmap()
	 * @see #getBitmap()
	 * @generated
	 */
	void setBitmap(long value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBitmap
	 * <em>Bitmap</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetBitmap()
	 * @see #getBitmap()
	 * @see #setBitmap(long)
	 * @generated
	 */
	void unsetBitmap();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBitmap
	 * <em>Bitmap</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return whether the value of the '<em>Bitmap</em>' attribute is set.
	 * @see #unsetBitmap()
	 * @see #getBitmap()
	 * @see #setBitmap(long)
	 * @generated
	 */
	boolean isSetBitmap();

	/**
	 * Returns the value of the '<em><b>Fore Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The
	 * foreground color. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Fore Color</em>' containment reference.
	 * @see #setForeColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPatternImage_ForeColor()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='ForeColor'"
	 * @generated
	 */
	ColorDefinition getForeColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getForeColor
	 * <em>Fore Color</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Fore Color</em>' containment
	 *              reference.
	 * @see #getForeColor()
	 * @generated
	 */
	void setForeColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Back Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The
	 * background color. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Back Color</em>' containment reference.
	 * @see #setBackColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPatternImage_BackColor()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='BackColor'"
	 * @generated
	 */
	ColorDefinition getBackColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBackColor
	 * <em>Back Color</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Back Color</em>' containment
	 *              reference.
	 * @see #getBackColor()
	 * @generated
	 */
	void setBackColor(ColorDefinition value);

	/**
	 * @generated
	 */
	@Override
	PatternImage copyInstance();

} // PatternImage
