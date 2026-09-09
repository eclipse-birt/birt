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
 *   LabelBlock is a sub-type of Block, which is specialized for laying out a text  
 * in the chart for general purpose. It contains a Label, which holds the content 
 * and graphical properties of the text to be rendered.  Following example shows
 * how to add a arbitrary text "Sample Text" to a chart: 
 * 
 *   <p xmlns="http://www.birt.eclipse.org/ChartModelLayout">
 *     		Chart cm = ....
 *     <br/>
 *     		LabelBlock lbBlock = (LabelBlock) LabelBlockImpl.create( );
 *     <br/>
 *     		lbBlock.getBounds( ).set( 50, 50, 100, 100 );
 *     <br/>
 *     		lbBlock.getLabel( ).getCaption( ).setValue( "Sample Text" );
 *     <br/>
 *     		cm.getBlock( ).getChildren( ).add( lbBlock );
 * 
 *   </p>
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.layout.LabelBlock#getLabel <em>Label</em>}</li>
 * </ul>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLabelBlock()
 * @model extendedMetaData="name='LabelBlock' kind='elementOnly'"
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
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.LabelBlock#getLabel <em>Label</em>}' containment reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
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
