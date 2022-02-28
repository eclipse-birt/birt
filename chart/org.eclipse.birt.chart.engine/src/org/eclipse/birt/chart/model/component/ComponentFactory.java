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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory </b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage
 * @generated
 */
public interface ComponentFactory extends EFactory {

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	ComponentFactory eINSTANCE = org.eclipse.birt.chart.model.component.impl.ComponentFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Axis</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Axis</em>'.
	 * @generated
	 */
	Axis createAxis();

	/**
	 * Returns a new object of class '<em>Chart Preferences</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Chart Preferences</em>'.
	 * @deprecated only reserved for compatibility
	 */
	@Deprecated
	ChartPreferences createChartPreferences();

	/**
	 * Returns a new object of class '<em>Curve Fitting</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Curve Fitting</em>'.
	 * @generated
	 */
	CurveFitting createCurveFitting();

	/**
	 * Returns a new object of class '<em>Dial</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Dial</em>'.
	 * @generated
	 */
	Dial createDial();

	/**
	 * Returns a new object of class '<em>Dial Region</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Dial Region</em>'.
	 * @generated
	 */
	DialRegion createDialRegion();

	/**
	 * Returns a new object of class '<em>Grid</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Grid</em>'.
	 * @generated
	 */
	Grid createGrid();

	/**
	 * Returns a new object of class '<em>Label</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Label</em>'.
	 * @generated
	 */
	Label createLabel();

	/**
	 * Returns a new object of class '<em>Marker Line</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Marker Line</em>'.
	 * @generated
	 */
	MarkerLine createMarkerLine();

	/**
	 * Returns a new object of class '<em>Marker Range</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Marker Range</em>'.
	 * @generated
	 */
	MarkerRange createMarkerRange();

	/**
	 * Returns a new object of class '<em>Needle</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Needle</em>'.
	 * @generated
	 */
	Needle createNeedle();

	/**
	 * Returns a new object of class '<em>Scale</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Scale</em>'.
	 * @generated
	 */
	Scale createScale();

	/**
	 * Returns a new object of class '<em>Series</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return a new object of class '<em>Series</em>'.
	 * @generated
	 */
	Series createSeries();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	ComponentPackage getComponentPackage();

} // ComponentFactory
