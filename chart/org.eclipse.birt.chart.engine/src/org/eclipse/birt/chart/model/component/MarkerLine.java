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
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Marker
 * Line</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type defines a single line element. It is intended for use as a marker
 * associated with an Axis. e.g. To denote a Target value
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getAttributes
 * <em>Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getPosition
 * <em>Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getAssociatedLabel
 * <em>Associated Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelPosition
 * <em>Label Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine()
 * @model
 * @generated
 */
public interface MarkerLine extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 *
	 * Specify the line properties.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_LineAttributes()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getLineAttributes
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
	 * Returns the value of the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines where this line is to be positioned w.r.t the axis. (Can be a value
	 * on the axis or a category name).
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(DataElement)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_Value()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	DataElement getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getValue
	 * <em>Value</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(DataElement value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies the label associated with this line.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_Label()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Label getLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getLabel
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
	 * default value is <code>"North"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Anchor}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies where the label associated with this line is to be positioned with
	 * respect to the line itself.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Label Anchor</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see #isSetLabelAnchor()
	 * @see #unsetLabelAnchor()
	 * @see #setLabelAnchor(Anchor)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_LabelAnchor()
	 * @model default="North" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	Anchor getLabelAnchor();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor
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
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor
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
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor
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
	 * Returns the value of the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 *
	 * Specifies the formatting for marker line labels.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Format Specifier</em>' containment reference.
	 * @see #setFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_FormatSpecifier()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	FormatSpecifier getFormatSpecifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getFormatSpecifier
	 * <em>Format Specifier</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Format Specifier</em>' containment
	 *              reference.
	 * @see #getFormatSpecifier()
	 * @generated
	 */
	void setFormatSpecifier(FormatSpecifier value);

	/**
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.Trigger}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds the triggers for the marker line.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_Triggers()
	 * @model containment="true" extendedMetaData="kind='element' name='Triggers'"
	 * @generated
	 */
	EList<Trigger> getTriggers();

	/**
	 * Returns the value of the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The element
	 * represents a cursor for marker line. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Cursor</em>' containment reference.
	 * @see #setCursor(Cursor)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getMarkerLine_Cursor()
	 * @model containment="true" extendedMetaData="kind='element' name='Cursor'"
	 * @generated
	 */
	Cursor getCursor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getCursor
	 * <em>Cursor</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Cursor</em>' containment reference.
	 * @see #getCursor()
	 * @generated
	 */
	void setCursor(Cursor value);

	/**
	 * @generated
	 */
	@Override
	MarkerLine copyInstance();

} // MarkerLine
