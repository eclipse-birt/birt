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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.TickStyle;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Grid</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type defines the grid associated with an axis.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.Grid#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Grid#getTickStyle <em>Tick
 * Style</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Grid#getTickAttributes
 * <em>Tick Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Grid#getTickSize <em>Tick
 * Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Grid#getTickCount <em>Tick
 * Count</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getGrid()
 * @model extendedMetaData="name='Grid' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Grid extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Holds formatting information for the grid lines.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getGrid_LineAttributes()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getLineAttributes
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
	 * Returns the value of the '<em><b>Tick Style</b></em>' attribute. The default
	 * value is <code>"Left"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.TickStyle}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies how the tick is to be marked for the grid line.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Tick Style</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @see #isSetTickStyle()
	 * @see #unsetTickStyle()
	 * @see #setTickStyle(TickStyle)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getGrid_TickStyle()
	 * @model default="Left" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	TickStyle getTickStyle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickStyle <em>Tick
	 * Style</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Tick Style</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @see #isSetTickStyle()
	 * @see #unsetTickStyle()
	 * @see #getTickStyle()
	 * @generated
	 */
	void setTickStyle(TickStyle value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickStyle <em>Tick
	 * Style</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetTickStyle()
	 * @see #getTickStyle()
	 * @see #setTickStyle(TickStyle)
	 * @generated
	 */
	void unsetTickStyle();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Grid#getTickStyle <em>Tick
	 * Style</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Tick Style</em>' attribute is set.
	 * @see #unsetTickStyle()
	 * @see #getTickStyle()
	 * @see #setTickStyle(TickStyle)
	 * @generated
	 */
	boolean isSetTickStyle();

	/**
	 * Returns the value of the '<em><b>Tick Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the formatting information for the tick.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Tick Attributes</em>' containment reference.
	 * @see #setTickAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getGrid_TickAttributes()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	LineAttributes getTickAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickAttributes
	 * <em>Tick Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Tick Attributes</em>' containment
	 *              reference.
	 * @see #getTickAttributes()
	 * @generated
	 */
	void setTickAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Tick Size</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the formatting information for the tick.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Tick Size</em>' attribute.
	 * @see #isSetTickSize()
	 * @see #unsetTickSize()
	 * @see #setTickSize(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getGrid_TickSize()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double"
	 * @generated
	 */
	double getTickSize();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickSize <em>Tick
	 * Size</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Tick Size</em>' attribute.
	 * @see #isSetTickSize()
	 * @see #unsetTickSize()
	 * @see #getTickSize()
	 * @generated
	 */
	void setTickSize(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickSize <em>Tick
	 * Size</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetTickSize()
	 * @see #getTickSize()
	 * @see #setTickSize(double)
	 * @generated
	 */
	void unsetTickSize();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Grid#getTickSize <em>Tick
	 * Size</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Tick Size</em>' attribute is set.
	 * @see #unsetTickSize()
	 * @see #getTickSize()
	 * @see #setTickSize(double)
	 * @generated
	 */
	boolean isSetTickSize();

	/**
	 * Returns the value of the '<em><b>Tick Count</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the frequency of the grid lines per unit of the scale.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Tick Count</em>' attribute.
	 * @see #isSetTickCount()
	 * @see #unsetTickCount()
	 * @see #setTickCount(int)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getGrid_TickCount()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getTickCount();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickCount <em>Tick
	 * Count</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Tick Count</em>' attribute.
	 * @see #isSetTickCount()
	 * @see #unsetTickCount()
	 * @see #getTickCount()
	 * @generated
	 */
	void setTickCount(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickCount <em>Tick
	 * Count</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetTickCount()
	 * @see #getTickCount()
	 * @see #setTickCount(int)
	 * @generated
	 */
	void unsetTickCount();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Grid#getTickCount <em>Tick
	 * Count</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Tick Count</em>' attribute is set.
	 * @see #unsetTickCount()
	 * @see #getTickCount()
	 * @see #setTickCount(int)
	 * @generated
	 */
	boolean isSetTickCount();

	/**
	 * @generated
	 */
	Grid copyInstance();

} // Grid
