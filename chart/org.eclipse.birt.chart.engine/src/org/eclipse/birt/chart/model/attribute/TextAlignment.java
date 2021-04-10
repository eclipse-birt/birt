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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Text
 * Alignment</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines the alignment to be used for the text.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment
 * <em>Horizontal Alignment</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment
 * <em>Vertical Alignment</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTextAlignment()
 * @model
 * @generated
 */
public interface TextAlignment extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Horizontal Alignment</b></em>' attribute.
	 * The default value is <code>"Left"</code>. The literals are from the
	 * enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment}. <!--
	 * begin-user-doc --> Gets the horizontal component of the text alignment. <!--
	 * end-user-doc -->
	 * 
	 * @return the value of the '<em>Horizontal Alignment</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @see #isSetHorizontalAlignment()
	 * @see #unsetHorizontalAlignment()
	 * @see #setHorizontalAlignment(HorizontalAlignment)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTextAlignment_HorizontalAlignment()
	 * @model default="Left" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='horizontalAlignment'"
	 * @generated
	 */
	HorizontalAlignment getHorizontalAlignment();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment
	 * <em>Horizontal Alignment</em>}' attribute. <!-- begin-user-doc --> Sets the
	 * horizontal component of the text alignment. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Horizontal Alignment</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @see #isSetHorizontalAlignment()
	 * @see #unsetHorizontalAlignment()
	 * @see #getHorizontalAlignment()
	 * @generated
	 */
	void setHorizontalAlignment(HorizontalAlignment value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment
	 * <em>Horizontal Alignment</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetHorizontalAlignment()
	 * @see #getHorizontalAlignment()
	 * @see #setHorizontalAlignment(HorizontalAlignment)
	 * @generated
	 */
	void unsetHorizontalAlignment();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment
	 * <em>Horizontal Alignment</em>}' attribute is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Horizontal Alignment</em>' attribute is
	 *         set.
	 * @see #unsetHorizontalAlignment()
	 * @see #getHorizontalAlignment()
	 * @see #setHorizontalAlignment(HorizontalAlignment)
	 * @generated
	 */
	boolean isSetHorizontalAlignment();

	/**
	 * Returns the value of the '<em><b>Vertical Alignment</b></em>' attribute. The
	 * default value is <code>"Top"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.VerticalAlignment}. <!--
	 * begin-user-doc --> Gets the vertical component of the text alignment. <!--
	 * end-user-doc -->
	 * 
	 * @return the value of the '<em>Vertical Alignment</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @see #isSetVerticalAlignment()
	 * @see #unsetVerticalAlignment()
	 * @see #setVerticalAlignment(VerticalAlignment)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTextAlignment_VerticalAlignment()
	 * @model default="Top" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='verticalAlignment'"
	 * @generated
	 */
	VerticalAlignment getVerticalAlignment();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment
	 * <em>Vertical Alignment</em>}' attribute. <!-- begin-user-doc --> Sets the
	 * horizontal component of the text alignment. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Vertical Alignment</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @see #isSetVerticalAlignment()
	 * @see #unsetVerticalAlignment()
	 * @see #getVerticalAlignment()
	 * @generated
	 */
	void setVerticalAlignment(VerticalAlignment value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment
	 * <em>Vertical Alignment</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetVerticalAlignment()
	 * @see #getVerticalAlignment()
	 * @see #setVerticalAlignment(VerticalAlignment)
	 * @generated
	 */
	void unsetVerticalAlignment();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment
	 * <em>Vertical Alignment</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Vertical Alignment</em>' attribute is
	 *         set.
	 * @see #unsetVerticalAlignment()
	 * @see #getVerticalAlignment()
	 * @see #setVerticalAlignment(VerticalAlignment)
	 * @generated
	 */
	boolean isSetVerticalAlignment();

	/**
	 * @generated
	 */
	TextAlignment copyInstance();

} // TextAlignment
