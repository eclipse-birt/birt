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

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Stock
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This is a Series type that holds data for Stock Charts.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.StockSeries#getFill
 * <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries()
 * @model
 * @generated
 */
public interface StockSeries extends Series {

	/**
	 * Returns the value of the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the fill to be used for the Candle.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Fill</em>' containment reference.
	 * @see #setFill(Fill)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries_Fill()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Fill getFill();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getFill <em>Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Fill</em>' containment reference.
	 * @see #getFill()
	 * @generated
	 */
	void setFill(Fill value);

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 *
	 * Specifies the style to be used to display the lines for this series.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries_LineAttributes()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes
	 * <em>Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Line Attributes</em>' containment
	 *              reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
	void setLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Show As Bar Stick</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> Specifies if show graph as
	 * bar-stick look. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Show As Bar Stick</em>' attribute.
	 * @see #isSetShowAsBarStick()
	 * @see #unsetShowAsBarStick()
	 * @see #setShowAsBarStick(boolean)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries_ShowAsBarStick()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ShowAsBarStick'"
	 * @generated
	 */
	boolean isShowAsBarStick();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#isShowAsBarStick
	 * <em>Show As Bar Stick</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Show As Bar Stick</em>' attribute.
	 * @see #isSetShowAsBarStick()
	 * @see #unsetShowAsBarStick()
	 * @see #isShowAsBarStick()
	 * @generated
	 */
	void setShowAsBarStick(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#isShowAsBarStick
	 * <em>Show As Bar Stick</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #isSetShowAsBarStick()
	 * @see #isShowAsBarStick()
	 * @see #setShowAsBarStick(boolean)
	 * @generated
	 */
	void unsetShowAsBarStick();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#isShowAsBarStick
	 * <em>Show As Bar Stick</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Show As Bar Stick</em>' attribute is
	 *         set.
	 * @see #unsetShowAsBarStick()
	 * @see #isShowAsBarStick()
	 * @see #setShowAsBarStick(boolean)
	 * @generated
	 */
	boolean isSetShowAsBarStick();

	/**
	 * Returns the value of the '<em><b>Stick Length</b></em>' attribute. The
	 * default value is <code>"5"</code>. <!-- begin-user-doc --> <!-- end-user-doc
	 * --> <!-- begin-model-doc --> Specifies the length of the stick when show as
	 * bar-stick look. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Stick Length</em>' attribute.
	 * @see #isSetStickLength()
	 * @see #unsetStickLength()
	 * @see #setStickLength(int)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries_StickLength()
	 * @model default="5" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='element' name='StickLength'"
	 * @generated
	 */
	int getStickLength();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getStickLength
	 * <em>Stick Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Stick Length</em>' attribute.
	 * @see #isSetStickLength()
	 * @see #unsetStickLength()
	 * @see #getStickLength()
	 * @generated
	 */
	void setStickLength(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getStickLength
	 * <em>Stick Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #isSetStickLength()
	 * @see #getStickLength()
	 * @see #setStickLength(int)
	 * @generated
	 */
	void unsetStickLength();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getStickLength
	 * <em>Stick Length</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Stick Length</em>' attribute is set.
	 * @see #unsetStickLength()
	 * @see #getStickLength()
	 * @see #setStickLength(int)
	 * @generated
	 */
	boolean isSetStickLength();

	/**
	 * @generated
	 */
	@Override
	StockSeries copyInstance();

} // StockSeries
