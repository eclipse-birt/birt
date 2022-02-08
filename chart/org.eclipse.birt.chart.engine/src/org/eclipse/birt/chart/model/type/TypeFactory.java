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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory </b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.type.TypePackage
 * @generated
 */
public interface TypeFactory extends EFactory {

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	TypeFactory eINSTANCE = org.eclipse.birt.chart.model.type.impl.TypeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Area Series</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Area Series</em>'.
	 * @generated
	 */
	AreaSeries createAreaSeries();

	/**
	 * Returns a new object of class '<em>Bar Series</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bar Series</em>'.
	 * @generated
	 */
	BarSeries createBarSeries();

	/**
	 * Returns a new object of class '<em>Bubble Series</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bubble Series</em>'.
	 * @generated
	 */
	BubbleSeries createBubbleSeries();

	/**
	 * Returns a new object of class '<em>Dial Series</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Dial Series</em>'.
	 * @generated
	 */
	DialSeries createDialSeries();

	/**
	 * Returns a new object of class '<em>Difference Series</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Difference Series</em>'.
	 * @generated
	 */
	DifferenceSeries createDifferenceSeries();

	/**
	 * Returns a new object of class '<em>Gantt Series</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Gantt Series</em>'.
	 * @generated
	 */
	GanttSeries createGanttSeries();

	/**
	 * Returns a new object of class '<em>Line Series</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Line Series</em>'.
	 * @generated
	 */
	LineSeries createLineSeries();

	/**
	 * Returns a new object of class '<em>Pie Series</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Pie Series</em>'.
	 * @generated
	 */
	PieSeries createPieSeries();

	/**
	 * Returns a new object of class '<em>Scatter Series</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Scatter Series</em>'.
	 * @generated
	 */
	ScatterSeries createScatterSeries();

	/**
	 * Returns a new object of class '<em>Stock Series</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Stock Series</em>'.
	 * @generated
	 */
	StockSeries createStockSeries();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	TypePackage getTypePackage();

} // TypeFactory
