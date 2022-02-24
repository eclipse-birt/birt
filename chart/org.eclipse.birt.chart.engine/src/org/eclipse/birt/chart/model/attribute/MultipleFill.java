/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Multiple
 * Fill</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> MultipleFill extends type Fill to represent a
 * pre-defined series of possible Fills, one of which will be selected to fill
 * an chart element due to a certain condition. For example, the color of the
 * increasing datapoints in a Stock Chart is different with the decreasing ones.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.MultipleFill#getFills
 * <em>Fills</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMultipleFill()
 * @model extendedMetaData="name='MultipleFill' kind='elementOnly'"
 * @generated
 */
public interface MultipleFill extends Fill {

	/**
	 * Returns the value of the '<em><b>Fills</b></em>' containment reference list.
	 * The list contents are of type
	 * {@link org.eclipse.birt.chart.model.attribute.Fill}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fills</em>' containment reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc --> <!-- begin-model-doc --> The list element "Fills"
	 * represent the pre-defined candidates of Fill. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Fills</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMultipleFill_Fills()
	 * @model containment="true" extendedMetaData="kind='element' name='Fills'"
	 * @generated
	 */
	EList<Fill> getFills();

	/**
	 * @generated
	 */
	MultipleFill copyInstance();

} // MultipleFill
