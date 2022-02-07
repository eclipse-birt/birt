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

package org.eclipse.birt.chart.model.layout;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory </b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage
 * @generated
 */
public interface LayoutFactory extends EFactory {

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	LayoutFactory eINSTANCE = org.eclipse.birt.chart.model.layout.impl.LayoutFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Block</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Block</em>'.
	 * @generated
	 */
	Block createBlock();

	/**
	 * Returns a new object of class '<em>Client Area</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Client Area</em>'.
	 * @generated
	 */
	ClientArea createClientArea();

	/**
	 * Returns a new object of class '<em>Label Block</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Label Block</em>'.
	 * @generated
	 */
	LabelBlock createLabelBlock();

	/**
	 * Returns a new object of class '<em>Legend</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Legend</em>'.
	 * @generated
	 */
	Legend createLegend();

	/**
	 * Returns a new object of class '<em>Plot</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Plot</em>'.
	 * @generated
	 */
	Plot createPlot();

	/**
	 * Returns a new object of class '<em>Title Block</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Title Block</em>'.
	 * @generated
	 */
	TitleBlock createTitleBlock();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	LayoutPackage getLayoutPackage();

} // LayoutFactory
