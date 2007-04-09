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

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Palette</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This type represents a palette of Fills.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.Palette#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.Palette#getEntries <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPalette()
 * @model extendedMetaData="name='Palette' kind='elementOnly'"
 * @generated
 */
public interface Palette extends EObject
{

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Specifies the name that uniquely identifies this palette.
	 * 					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPalette_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Name'"
	 * @generated
	 */
	String getName( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Palette#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> Sets the name for the palette. <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName( String value );

	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.attribute.Fill}. <!-- begin-user-doc --> Gets the entries defined in the
	 * palette. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the entries contained in the palette.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getPalette_Entries()
	 * @model type="org.eclipse.birt.chart.model.attribute.Fill" containment="true" resolveProxies="false"
	 *        required="true"
	 * @generated
	 */
	EList getEntries( );

	/**
	 * Updates the content of a palette with a library
	 * of colors and rotation with the steps.
	 * 
	 * NOTE: Manually written
	 * 
	 * @param iStep
	 *            moving steps to rotate the color. If the step is zero or the
	 *            absolute value is greater than the size of list, do nothing.
	 *            Negative value means moving to the left side, and positive
	 *            value is to the right side.
	 */
	void update( int iStep );

	/**
	 * Updates the content of a palette by cleaning all and adding a single
	 * color
	 * 
	 * NOTE: Manually written
	 * 
	 * @param f
	 *            color to replace
	 */
	void update( Fill f );
} // Palette
