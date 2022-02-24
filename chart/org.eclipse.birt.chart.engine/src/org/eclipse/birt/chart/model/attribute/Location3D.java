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
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.computation.Vector;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Location3 D</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Location3D represents a 3D point with its
 * coordinates. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Location3D#getZ
 * <em>Z</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLocation3D()
 * @model extendedMetaData="name='Location3D' kind='elementOnly'"
 * @generated
 */
public interface Location3D extends Location {

	/**
	 * Returns the value of the '<em><b>Z</b></em>' attribute. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Z</em>' attribute isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Z</em>' attribute.
	 * @see #isSetZ()
	 * @see #unsetZ()
	 * @see #setZ(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLocation3D_Z()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='z'"
	 * @generated
	 */
	double getZ();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Location3D#getZ <em>Z</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Z</em>' attribute.
	 * @see #isSetZ()
	 * @see #unsetZ()
	 * @see #getZ()
	 * @generated
	 */
	void setZ(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Location3D#getZ <em>Z</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetZ()
	 * @see #getZ()
	 * @see #setZ(double)
	 * @generated
	 */
	void unsetZ();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Location3D#getZ <em>Z</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Z</em>' attribute is set.
	 * @see #unsetZ()
	 * @see #getZ()
	 * @see #setZ(double)
	 * @generated
	 */
	boolean isSetZ();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Location#scale(double)
	 */
	void scale(double dScale);

	/**
	 * Causes the internal (x,y,z) values to be translated by a relative value of
	 * (dTranslateX, dTranslateY, dTranslateZ)
	 * 
	 * @param dTranslateX
	 * @param dTranslateY
	 * @param dTranslateZ
	 */
	void translate(double dTranslateX, double dTranslateY, double dTranslateZ);

	/**
	 * A convenience method for defining member variables
	 * 
	 * NOTE: Manually created
	 * 
	 * @param dX
	 * @param dY
	 * @param dZ
	 */
	void set(double dX, double dY, double dZ);

	/**
	 * Used to link a Location3D to a Vector for live update of the coordinates.
	 */
	public void linkToVector(Vector vector);

	/**
	 * @generated
	 */
	Location3D copyInstance();

} // Location3D
