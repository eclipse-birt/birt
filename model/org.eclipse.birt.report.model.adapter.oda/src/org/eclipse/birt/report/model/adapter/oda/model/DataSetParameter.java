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
 * $Id: DataSetParameter.java,v 1.1.2.1 2010/11/29 06:23:53 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Data Set
 * Parameter</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getParameterDefinition
 * <em>Parameter Definition</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getDynamicList
 * <em>Dynamic List</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDataSetParameter()
 * @model extendedMetaData="name='DataSetParameter' kind='elementOnly'"
 * @generated
 */
public interface DataSetParameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameter Definition</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Top-level parameter definition; may be input and/or output mode.
	 * Parameter may be of scalar or complex type. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Parameter Definition</em>' containment
	 *         reference.
	 * @see #setParameterDefinition(ParameterDefinition)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDataSetParameter_ParameterDefinition()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='ParameterDefinition'
	 *        namespace='http://www.eclipse.org/datatools/connectivity/oda/design'"
	 * @generated
	 */
	ParameterDefinition getParameterDefinition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getParameterDefinition
	 * <em>Parameter Definition</em>}' containment reference. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Parameter Definition</em>' containment
	 *              reference.
	 * @see #getParameterDefinition()
	 * @generated
	 */
	void setParameterDefinition(ParameterDefinition value);

	/**
	 * Returns the value of the '<em><b>Dynamic List</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dynamic List</em>' containment reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Dynamic List</em>' containment reference.
	 * @see #setDynamicList(DynamicList)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDataSetParameter_DynamicList()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='dynamicList'"
	 * @generated
	 */
	DynamicList getDynamicList();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getDynamicList
	 * <em>Dynamic List</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Dynamic List</em>' containment
	 *              reference.
	 * @see #getDynamicList()
	 * @generated
	 */
	void setDynamicList(DynamicList value);

} // DataSetParameter
