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

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Chart
 * Preferences</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type defines the global chart preferences.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.ChartPreferences#getLabels
 * <em>Labels</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.ChartPreferences#getBlocks
 * <em>Blocks</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getChartPreferences()
 * @model extendedMetaData="name='ChartPreferences' kind='elementOnly'"
 * @deprecated only reserved for compatibility
 */
public interface ChartPreferences extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Labels</b></em>' containment reference list.
	 * The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.Label}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * A collection of labels holding preferences for the various labels used in a
	 * chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Labels</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getChartPreferences_Labels()
	 * @model type="org.eclipse.birt.chart.model.component.Label" containment="true"
	 *        resolveProxies="false" required="true"
	 * @generated
	 */
	EList<Label> getLabels();

	/**
	 * Returns the value of the '<em><b>Blocks</b></em>' containment reference list.
	 * The list contents are of type
	 * {@link org.eclipse.birt.chart.model.layout.Block}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * A collection of blocks holding preferences for the different blocks used in a
	 * chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Blocks</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getChartPreferences_Blocks()
	 * @model type="org.eclipse.birt.chart.model.layout.Block" containment="true"
	 *        resolveProxies="false" required="true"
	 * @generated
	 */
	EList<Block> getBlocks();

	/**
	 * @generated
	 */
	ChartPreferences copyInstance();

} // ChartPreferences
