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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Curve
 * Fitting</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type defines the curve fitting element. <!--
 * end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.CurveFitting#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabelAnchor
 * <em>Label Anchor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getCurveFitting()
 * @model extendedMetaData="name='CurveFitting' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface CurveFitting extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the formatting information for the curve.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getCurveFitting_LineAttributes()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='LineAttributes'"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLineAttributes
	 * <em>Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Line Attributes</em>' containment
	 *              reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
	void setLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> A label
	 * instance to hold attributes for curve labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getCurveFitting_Label()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Label'"
	 * @generated
	 */
	Label getLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabel
	 * <em>Label</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label</em>' containment reference.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Label Anchor</b></em>' attribute. The
	 * literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Anchor}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies where the labels
	 * for the curve should be displayed.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label Anchor</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see #isSetLabelAnchor()
	 * @see #unsetLabelAnchor()
	 * @see #setLabelAnchor(Anchor)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getCurveFitting_LabelAnchor()
	 * @model unsettable="true" extendedMetaData="kind='element' name='LabelAnchor'"
	 * @generated
	 */
	Anchor getLabelAnchor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabelAnchor
	 * <em>Label Anchor</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Label Anchor</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see #isSetLabelAnchor()
	 * @see #unsetLabelAnchor()
	 * @see #getLabelAnchor()
	 * @generated
	 */
	void setLabelAnchor(Anchor value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabelAnchor
	 * <em>Label Anchor</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetLabelAnchor()
	 * @see #getLabelAnchor()
	 * @see #setLabelAnchor(Anchor)
	 * @generated
	 */
	void unsetLabelAnchor();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabelAnchor
	 * <em>Label Anchor</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Label Anchor</em>' attribute is set.
	 * @see #unsetLabelAnchor()
	 * @see #getLabelAnchor()
	 * @see #setLabelAnchor(Anchor)
	 * @generated
	 */
	boolean isSetLabelAnchor();

	/**
	 * @generated
	 */
	CurveFitting copyInstance();

} // CurveFitting
