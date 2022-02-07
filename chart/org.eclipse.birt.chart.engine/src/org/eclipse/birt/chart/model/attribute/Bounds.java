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
 * '<em><b>Bounds</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Bounds represents the physical size and position of
 * an element. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Bounds#getLeft
 * <em>Left</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Bounds#getTop
 * <em>Top</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Bounds#getWidth
 * <em>Width</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Bounds#getHeight
 * <em>Height</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getBounds()
 * @model extendedMetaData="name='Bounds' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Bounds extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Left</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds Left co-ord of the Top Left Corner of chart element
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Left</em>' attribute.
	 * @see #isSetLeft()
	 * @see #unsetLeft()
	 * @see #setLeft(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getBounds_Left()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='Left'"
	 * @generated
	 */
	double getLeft();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getLeft <em>Left</em>}'
	 * attribute. <!-- begin-user-doc --> Sets the Left co-ord of the Top Left
	 * Corner of chart element <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Left</em>' attribute.
	 * @see #isSetLeft()
	 * @see #unsetLeft()
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getLeft <em>Left</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetLeft()
	 * @see #getLeft()
	 * @see #setLeft(double)
	 * @generated
	 */
	void unsetLeft();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getLeft <em>Left</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Left</em>' attribute is set.
	 * @see #unsetLeft()
	 * @see #getLeft()
	 * @see #setLeft(double)
	 * @generated
	 */
	boolean isSetLeft();

	/**
	 * Returns the value of the '<em><b>Top</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds Top co-ord of the Top Left Corner of chart element
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Top</em>' attribute.
	 * @see #isSetTop()
	 * @see #unsetTop()
	 * @see #setTop(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getBounds_Top()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 * @generated
	 */
	double getTop();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getTop <em>Top</em>}'
	 * attribute. <!-- begin-user-doc --> Sets the Top co-ord of the Top Left Corner
	 * of chart element <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Top</em>' attribute.
	 * @see #isSetTop()
	 * @see #unsetTop()
	 * @see #getTop()
	 * @generated
	 */
	void setTop(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getTop <em>Top</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetTop()
	 * @see #getTop()
	 * @see #setTop(double)
	 * @generated
	 */
	void unsetTop();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getTop <em>Top</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Top</em>' attribute is set.
	 * @see #unsetTop()
	 * @see #getTop()
	 * @see #setTop(double)
	 * @generated
	 */
	boolean isSetTop();

	/**
	 * Returns the value of the '<em><b>Width</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds width of the chart element
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Width</em>' attribute.
	 * @see #isSetWidth()
	 * @see #unsetWidth()
	 * @see #setWidth(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getBounds_Width()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='Width'"
	 * @generated
	 */
	double getWidth();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getWidth
	 * <em>Width</em>}' attribute. <!-- begin-user-doc --> Sets the width component
	 * of the bounds. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Width</em>' attribute.
	 * @see #isSetWidth()
	 * @see #unsetWidth()
	 * @see #getWidth()
	 * @generated
	 */
	void setWidth(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getWidth
	 * <em>Width</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetWidth()
	 * @see #getWidth()
	 * @see #setWidth(double)
	 * @generated
	 */
	void unsetWidth();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getWidth
	 * <em>Width</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Width</em>' attribute is set.
	 * @see #unsetWidth()
	 * @see #getWidth()
	 * @see #setWidth(double)
	 * @generated
	 */
	boolean isSetWidth();

	/**
	 * Returns the value of the '<em><b>Height</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds height of the chart element
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Height</em>' attribute.
	 * @see #isSetHeight()
	 * @see #unsetHeight()
	 * @see #setHeight(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getBounds_Height()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='Height'"
	 * @generated
	 */
	double getHeight();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getHeight
	 * <em>Height</em>}' attribute. <!-- begin-user-doc --> Sets the height
	 * component of the bounds. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Height</em>' attribute.
	 * @see #isSetHeight()
	 * @see #unsetHeight()
	 * @see #getHeight()
	 * @generated
	 */
	void setHeight(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getHeight
	 * <em>Height</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetHeight()
	 * @see #getHeight()
	 * @see #setHeight(double)
	 * @generated
	 */
	void unsetHeight();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getHeight
	 * <em>Height</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Height</em>' attribute is set.
	 * @see #unsetHeight()
	 * @see #getHeight()
	 * @see #setHeight(double)
	 * @generated
	 */
	boolean isSetHeight();

	/**
	 * Causes the internal (left,right,width,height) values to be scaled by a the
	 * specified Insets
	 * 
	 * @param ins
	 */
	void adjust(Insets ins);

	/**
	 * Creates a new 'Bounds' instance by adjusting the existing 'Bounds' instance
	 * using the given 'Insets'
	 * 
	 * NOTE: Manually written
	 * 
	 * @param ins
	 * @return
	 */
	Bounds adjustedInstance(Insets ins);

	/**
	 * Adds a 'delta' value to the existing member variables and applies it to the
	 * current instance
	 * 
	 * @param dLeft
	 * @param dTop
	 * @param dWidth
	 * @param dHeight
	 */
	void delta(double dLeft, double dTop, double dWidth, double dHeight);

	/**
	 * Causes the internal (left,right) location to be translated by a relative
	 * value of (dTranslateX, dTranslateY)
	 * 
	 * @param dX
	 * @param dY
	 */
	void translate(double dTranslateX, double dTranslateY);

	/**
	 * Creates a new 'Bounds' instance by translate the existing 'Bounds' instance
	 * using given offsets.
	 * 
	 * @param dTranslateX
	 * @param dTranslateY
	 * @return
	 */
	Bounds translateInstance(double dTranslateX, double dTranslateY);

	/**
	 * Causes the internal (left,right,width,height) values to be scaled by a
	 * relative (dScale) value
	 * 
	 * @param dScale
	 */
	void scale(double dScale);

	/**
	 * Returns a new Bounds instance with scaled members
	 * 
	 * @param dScale The scaling factor
	 * 
	 * @return A new scaled instance
	 */
	Bounds scaledInstance(double dScale);

	/**
	 * A convenience method provided to update all members of an existing instance
	 * 
	 * @param dLeft
	 * @param dTop
	 * @param dWidth
	 * @param dHeight
	 */
	void set(double dLeft, double dTop, double dWidth, double dHeight);

	/**
	 * Updates an existing Bounds instance to encompass the polygon specified by a
	 * given set of points
	 * 
	 * @param loa
	 */
	void updateFrom(Location[] loa);

	/**
	 * Returns if the given point is within this bounds.
	 * 
	 * @param lo
	 * @return
	 */
	boolean contains(Location lo);

	/**
	 * 
	 * @param bo
	 */
	void max(Bounds bo);

	/**
	 * @generated
	 */
	Bounds copyInstance();

} // Bounds
