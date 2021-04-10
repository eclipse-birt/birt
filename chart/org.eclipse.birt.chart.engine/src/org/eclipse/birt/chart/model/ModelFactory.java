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

package org.eclipse.birt.chart.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory </b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ModelFactory eINSTANCE = org.eclipse.birt.chart.model.impl.ModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Chart</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Chart</em>'.
	 * @generated
	 */
	Chart createChart();

	/**
	 * Returns a new object of class '<em>Chart With Axes</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Chart With Axes</em>'.
	 * @generated
	 */
	ChartWithAxes createChartWithAxes();

	/**
	 * Returns a new object of class '<em>Chart Without Axes</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Chart Without Axes</em>'.
	 * @generated
	 */
	ChartWithoutAxes createChartWithoutAxes();

	/**
	 * Returns a new object of class '<em>Dial Chart</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Dial Chart</em>'.
	 * @generated
	 */
	DialChart createDialChart();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelPackage getModelPackage();

} // ModelFactory
