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

import org.eclipse.birt.chart.model.component.Label;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Label
 * Block</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type defines a text block in the chart.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.LabelBlock#getLabel
 * <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLabelBlock()
 * @model
 * @generated
 */
public interface LabelBlock extends Block {

	/**
	 * Returns the value of the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * The actual text content of the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(Label)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLabelBlock_Label()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Label getLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.LabelBlock#getLabel
	 * <em>Label</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Label</em>' containment reference.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(Label value);

	/**
	 * @generated
	 */
	@Override
	LabelBlock copyInstance();

} // LabelBlock
