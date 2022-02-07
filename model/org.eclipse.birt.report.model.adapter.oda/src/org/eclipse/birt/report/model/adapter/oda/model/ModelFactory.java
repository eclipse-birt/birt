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
 * $Id: ModelFactory.java,v 1.1.28.1 2010/11/29 06:23:53 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ModelFactory eINSTANCE = org.eclipse.birt.report.model.adapter.oda.model.impl.ModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Data Set Parameter</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Data Set Parameter</em>'.
	 * @generated
	 */
	DataSetParameter createDataSetParameter();

	/**
	 * Returns a new object of class '<em>Data Set Parameters</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Data Set Parameters</em>'.
	 * @generated
	 */
	DataSetParameters createDataSetParameters();

	/**
	 * Returns a new object of class '<em>Design Values</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Design Values</em>'.
	 * @generated
	 */
	DesignValues createDesignValues();

	/**
	 * Returns a new object of class '<em>Document Root</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
	DocumentRoot createDocumentRoot();

	/**
	 * Returns a new object of class '<em>Dynamic List</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Dynamic List</em>'.
	 * @generated
	 */
	DynamicList createDynamicList();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelPackage getModelPackage();

} // ModelFactory
