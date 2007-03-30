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

package org.eclipse.birt.chart.model.data;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Query</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This type represents design-time definition for the dataset in a series.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.data.Query#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.Query#getRules <em>Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery()
 * @model extendedMetaData="name='Query' kind='elementOnly'"
 * @generated
 */
public interface Query extends EObject
{

	/**
	 * Returns the value of the '<em><b>Definition</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the value of the '<em>Definition</em>' attribute.
	 * @see #setDefinition(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery_Definition()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 * @generated
	 */
	String getDefinition( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.Query#getDefinition <em>Definition</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' attribute.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition( String value );

	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.birt.chart.model.data.Rule}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery_Rules()
	 * @model type="org.eclipse.birt.chart.model.data.Rule" containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Rules'"
	 * @deprecated only reserved for compatibility
	 */
	EList getRules( );

	/**
	 * 
	 * @return
	 */
	boolean isDefined( );

} // Query
