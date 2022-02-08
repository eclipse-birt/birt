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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Axis</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type defines the basic elements that are
 * expected in any axis. An axis refers to a line along the X, Y, or Z
 * co-ordinate system used in the plot. It provides a point of reference for the
 * various values plotted in each direction. This class can further be extended
 * for special axis types.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getTitle
 * <em>Title</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getSubTitle <em>Sub
 * Title</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getTitlePosition
 * <em>Title Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getAssociatedAxes
 * <em>Associated Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getAncillaryAxes
 * <em>Ancillary Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getSeriesDefinitions
 * <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getGapWidth <em>Gap
 * Width</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getOrientation
 * <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getLabelPosition
 * <em>Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isStaggered
 * <em>Staggered</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getInterval
 * <em>Interval</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getMarkerLines
 * <em>Marker Lines</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getMarkerRanges
 * <em>Marker Ranges</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getMajorGrid <em>Major
 * Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getMinorGrid <em>Minor
 * Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getScale
 * <em>Scale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getOrigin
 * <em>Origin</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis
 * <em>Primary Axis</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isCategoryAxis
 * <em>Category Axis</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isPercent
 * <em>Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isLabelWithinAxes
 * <em>Label Within Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isAligned
 * <em>Aligned</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#isSideBySide <em>Side
 * By Side</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getCursor
 * <em>Cursor</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getLabelSpan <em>Label
 * Span</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Axis#getAxisPercent
 * <em>Axis Percent</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis()
 * @model extendedMetaData="name='Axis' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Axis extends IChartObject {

	/**
	 *  
	 */
	public static final int BASE = IConstants.BASE;

	/**
	 *  
	 */
	public static final int ORTHOGONAL = IConstants.ORTHOGONAL;

	/**
	 * 
	 */
	public static final int ANCILLARY_BASE = IConstants.ANCILLARY_BASE;

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The default value
	 * is <code>"Linear"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.AxisType}. <!-- begin-user-doc
	 * --> Returns the type of the axis. The type specifies the data that can be
	 * used by associated series. The expected types with the data type expected is
	 * as follows:
	 * <DL>
	 * <DT>Linear</DT>
	 * <DD>Double</DD>
	 * <DT>Logarithmic</DT>
	 * <DD>Double</DD>
	 * <DT>Date/Time</DT>
	 * <DD>Calendar</DD>
	 * <DT>Text</DT>
	 * <DD>String</DD>
	 * </DL>
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the type of Axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(AxisType)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Type()
	 * @model default="Linear" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	AxisType getType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(AxisType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(AxisType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getType <em>Type</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(AxisType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Returns the value of the '<em><b>Title</b></em>' containment reference. <!--
	 * begin-user-doc --> Gets the axis title. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * This is the element title.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Title</em>' containment reference.
	 * @see #setTitle(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Title()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Label getTitle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getTitle <em>Title</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Title</em>' containment reference.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(Label value);

	/**
	 * Returns the value of the '<em><b>Sub Title</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This is the element subtitle.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Sub Title</em>' containment reference.
	 * @see #setSubTitle(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_SubTitle()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Label getSubTitle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getSubTitle <em>Sub
	 * Title</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Sub Title</em>' containment reference.
	 * @see #getSubTitle()
	 * @generated
	 */
	void setSubTitle(Label value);

	/**
	 * Returns the value of the '<em><b>Title Position</b></em>' attribute. The
	 * default value is <code>"Above"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Position}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies where the title for the axis should be displayed.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Title Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetTitlePosition()
	 * @see #unsetTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_TitlePosition()
	 * @model default="Above" unique="false" unsettable="true"
	 * @generated
	 */
	Position getTitlePosition();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#getTitlePosition <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Title Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetTitlePosition()
	 * @see #unsetTitlePosition()
	 * @see #getTitlePosition()
	 * @generated
	 */
	void setTitlePosition(Position value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#getTitlePosition <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetTitlePosition()
	 * @see #getTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @generated
	 */
	void unsetTitlePosition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getTitlePosition
	 * <em>Title Position</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Title Position</em>' attribute is set.
	 * @see #unsetTitlePosition()
	 * @see #getTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @generated
	 */
	boolean isSetTitlePosition();

	/**
	 * Returns the value of the '<em><b>Associated Axes</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.Axis}. <!-- begin-user-doc -->
	 * Gets the list of orthogonal axes associated with this axis. (This call only
	 * makes sence when made on Base axes.) <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Holds the value orthogonal axes associated with this axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Associated Axes</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_AssociatedAxes()
	 * @model type="org.eclipse.birt.chart.model.component.Axis" containment="true"
	 *        resolveProxies="false"
	 * @generated
	 */
	EList<Axis> getAssociatedAxes();

	/**
	 * Returns the value of the '<em><b>Ancillary Axes</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.Axis}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the value ancillary base axes associated with this axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Ancillary Axes</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_AncillaryAxes()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='AncillaryAxes'"
	 * @generated
	 */
	EList<Axis> getAncillaryAxes();

	/**
	 * Returns the value of the '<em><b>Series Definitions</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.SeriesDefinition}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the source of the series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Series Definitions</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_SeriesDefinitions()
	 * @model type="org.eclipse.birt.chart.model.data.SeriesDefinition"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList<SeriesDefinition> getSeriesDefinitions();

	/**
	 * Returns the value of the '<em><b>Gap Width</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the spacing between the first (or last) series marker and the edge of
	 * the unit on the axis. This is specified as a percentage.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Gap Width</em>' attribute.
	 * @see #isSetGapWidth()
	 * @see #unsetGapWidth()
	 * @see #setGapWidth(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_GapWidth()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double"
	 * @generated
	 */
	double getGapWidth();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getGapWidth <em>Gap
	 * Width</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Gap Width</em>' attribute.
	 * @see #isSetGapWidth()
	 * @see #unsetGapWidth()
	 * @see #getGapWidth()
	 * @generated
	 */
	void setGapWidth(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getGapWidth <em>Gap
	 * Width</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetGapWidth()
	 * @see #getGapWidth()
	 * @see #setGapWidth(double)
	 * @generated
	 */
	void unsetGapWidth();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#getGapWidth <em>Gap
	 * Width</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Gap Width</em>' attribute is set.
	 * @see #unsetGapWidth()
	 * @see #getGapWidth()
	 * @see #setGapWidth(double)
	 * @generated
	 */
	boolean isSetGapWidth();

	/**
	 * Returns the value of the '<em><b>Orientation</b></em>' attribute. The default
	 * value is <code>"Horizontal"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Orientation}. <!--
	 * begin-user-doc --> Gets the orientation of the axis. This will be affected by
	 * the Orientation for the chart as a whole. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Specifies the orientation of the Axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetOrientation()
	 * @see #unsetOrientation()
	 * @see #setOrientation(Orientation)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Orientation()
	 * @model default="Horizontal" unique="false" unsettable="true"
	 * @generated
	 */
	Orientation getOrientation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getOrientation
	 * <em>Orientation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetOrientation()
	 * @see #unsetOrientation()
	 * @see #getOrientation()
	 * @generated
	 */
	void setOrientation(Orientation value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getOrientation
	 * <em>Orientation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetOrientation()
	 * @see #getOrientation()
	 * @see #setOrientation(Orientation)
	 * @generated
	 */
	void unsetOrientation();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#getOrientation
	 * <em>Orientation</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Orientation</em>' attribute is set.
	 * @see #unsetOrientation()
	 * @see #getOrientation()
	 * @see #setOrientation(Orientation)
	 * @generated
	 */
	boolean isSetOrientation();

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the formatting information for the axis line.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_LineAttributes()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLineAttributes
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
	 * begin-user-doc --> Returns a Label instance that defines the properties to be
	 * used for all axis labels. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * A label instance to hold attributes for axis labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Label()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Label getLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabel <em>Label</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label</em>' containment reference.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the formatting for axis labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Format Specifier</em>' containment reference.
	 * @see #setFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_FormatSpecifier()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	FormatSpecifier getFormatSpecifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getFormatSpecifier
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
	 * Returns the value of the '<em><b>Label Position</b></em>' attribute. The
	 * default value is <code>"Above"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Position}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies where the labels for the axis should be displayed.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetLabelPosition()
	 * @see #unsetLabelPosition()
	 * @see #setLabelPosition(Position)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_LabelPosition()
	 * @model default="Above" unique="false" unsettable="true"
	 * @generated
	 */
	Position getLabelPosition();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#getLabelPosition <em>Label
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetLabelPosition()
	 * @see #unsetLabelPosition()
	 * @see #getLabelPosition()
	 * @generated
	 */
	void setLabelPosition(Position value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#getLabelPosition <em>Label
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetLabelPosition()
	 * @see #getLabelPosition()
	 * @see #setLabelPosition(Position)
	 * @generated
	 */
	void unsetLabelPosition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabelPosition
	 * <em>Label Position</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Label Position</em>' attribute is set.
	 * @see #unsetLabelPosition()
	 * @see #getLabelPosition()
	 * @see #setLabelPosition(Position)
	 * @generated
	 */
	boolean isSetLabelPosition();

	/**
	 * Returns the value of the '<em><b>Staggered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the position to be used for the labels. (Staggered/Straight)
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Staggered</em>' attribute.
	 * @see #isSetStaggered()
	 * @see #unsetStaggered()
	 * @see #setStaggered(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Staggered()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isStaggered();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isStaggered
	 * <em>Staggered</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Staggered</em>' attribute.
	 * @see #isSetStaggered()
	 * @see #unsetStaggered()
	 * @see #isStaggered()
	 * @generated
	 */
	void setStaggered(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isStaggered
	 * <em>Staggered</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetStaggered()
	 * @see #isStaggered()
	 * @see #setStaggered(boolean)
	 * @generated
	 */
	void unsetStaggered();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#isStaggered
	 * <em>Staggered</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Staggered</em>' attribute is set.
	 * @see #unsetStaggered()
	 * @see #isStaggered()
	 * @see #setStaggered(boolean)
	 * @generated
	 */
	boolean isSetStaggered();

	/**
	 * Returns the value of the '<em><b>Interval</b></em>' attribute. The default
	 * value is <code>"1"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Specifies the showing interval for the labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Interval</em>' attribute.
	 * @see #isSetInterval()
	 * @see #unsetInterval()
	 * @see #setInterval(int)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Interval()
	 * @model default="1" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='element' name='Interval'"
	 * @generated
	 */
	int getInterval();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getInterval
	 * <em>Interval</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Interval</em>' attribute.
	 * @see #isSetInterval()
	 * @see #unsetInterval()
	 * @see #getInterval()
	 * @generated
	 */
	void setInterval(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getInterval
	 * <em>Interval</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetInterval()
	 * @see #getInterval()
	 * @see #setInterval(int)
	 * @generated
	 */
	void unsetInterval();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getInterval
	 * <em>Interval</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Interval</em>' attribute is set.
	 * @see #unsetInterval()
	 * @see #getInterval()
	 * @see #setInterval(int)
	 * @generated
	 */
	boolean isSetInterval();

	/**
	 * Returns the value of the '<em><b>Marker Lines</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.MarkerLine}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines a set of markers for positions on the axis scale displayed as lines
	 * across the plot at those locations.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Marker Lines</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_MarkerLines()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='MarkerLines'"
	 * @generated
	 */
	EList<MarkerLine> getMarkerLines();

	/**
	 * Returns the value of the '<em><b>Marker Ranges</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.MarkerRange}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines a set of marker areas for a range of values on the axis displayed as
	 * filled rectangles extending across the plot between the start and end
	 * positions.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Marker Ranges</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_MarkerRanges()
	 * @model type="org.eclipse.birt.chart.model.component.MarkerRange"
	 *        containment="true" resolveProxies="false"
	 * @generated
	 */
	EList<MarkerRange> getMarkerRanges();

	/**
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.Trigger}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the triggers for the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Triggers()
	 * @model containment="true" extendedMetaData="kind='element' name='Triggers'"
	 * @generated
	 */
	EList<Trigger> getTriggers();

	/**
	 * Returns the value of the '<em><b>Major Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the major grid associated with the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Major Grid</em>' containment reference.
	 * @see #setMajorGrid(Grid)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_MajorGrid()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Grid getMajorGrid();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getMajorGrid <em>Major
	 * Grid</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Major Grid</em>' containment
	 *              reference.
	 * @see #getMajorGrid()
	 * @generated
	 */
	void setMajorGrid(Grid value);

	/**
	 * Returns the value of the '<em><b>Minor Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the minor grid associated with the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Minor Grid</em>' containment reference.
	 * @see #setMinorGrid(Grid)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_MinorGrid()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Grid getMinorGrid();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getMinorGrid <em>Minor
	 * Grid</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Minor Grid</em>' containment
	 *              reference.
	 * @see #getMinorGrid()
	 * @generated
	 */
	void setMinorGrid(Grid value);

	/**
	 * Returns the value of the '<em><b>Scale</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the scale for the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Scale</em>' containment reference.
	 * @see #setScale(Scale)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Scale()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Scale getScale();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getScale <em>Scale</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Scale</em>' containment reference.
	 * @see #getScale()
	 * @generated
	 */
	void setScale(Scale value);

	/**
	 * Returns the value of the '<em><b>Origin</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the location on the axis that should co-incide with the origin of
	 * the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Origin</em>' containment reference.
	 * @see #setOrigin(AxisOrigin)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Origin()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	AxisOrigin getOrigin();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getOrigin
	 * <em>Origin</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Origin</em>' containment reference.
	 * @see #getOrigin()
	 * @generated
	 */
	void setOrigin(AxisOrigin value);

	/**
	 * Returns the value of the '<em><b>Primary Axis</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether or not this is a primary axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Primary Axis</em>' attribute.
	 * @see #isSetPrimaryAxis()
	 * @see #unsetPrimaryAxis()
	 * @see #setPrimaryAxis(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_PrimaryAxis()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isPrimaryAxis();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis <em>Primary
	 * Axis</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Primary Axis</em>' attribute.
	 * @see #isSetPrimaryAxis()
	 * @see #unsetPrimaryAxis()
	 * @see #isPrimaryAxis()
	 * @generated
	 */
	void setPrimaryAxis(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis <em>Primary
	 * Axis</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetPrimaryAxis()
	 * @see #isPrimaryAxis()
	 * @see #setPrimaryAxis(boolean)
	 * @generated
	 */
	void unsetPrimaryAxis();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis <em>Primary
	 * Axis</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Primary Axis</em>' attribute is set.
	 * @see #unsetPrimaryAxis()
	 * @see #isPrimaryAxis()
	 * @see #setPrimaryAxis(boolean)
	 * @generated
	 */
	boolean isSetPrimaryAxis();

	/**
	 * Returns the value of the '<em><b>Category Axis</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether or not this is a category axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Category Axis</em>' attribute.
	 * @see #isSetCategoryAxis()
	 * @see #unsetCategoryAxis()
	 * @see #setCategoryAxis(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_CategoryAxis()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isCategoryAxis();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isCategoryAxis
	 * <em>Category Axis</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Category Axis</em>' attribute.
	 * @see #isSetCategoryAxis()
	 * @see #unsetCategoryAxis()
	 * @see #isCategoryAxis()
	 * @generated
	 */
	void setCategoryAxis(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.component.Axis#isCategoryAxis
	 * <em>Category Axis</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetCategoryAxis()
	 * @see #isCategoryAxis()
	 * @see #setCategoryAxis(boolean)
	 * @generated
	 */
	void unsetCategoryAxis();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isCategoryAxis
	 * <em>Category Axis</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Category Axis</em>' attribute is set.
	 * @see #unsetCategoryAxis()
	 * @see #isCategoryAxis()
	 * @see #setCategoryAxis(boolean)
	 * @generated
	 */
	boolean isSetCategoryAxis();

	/**
	 * Returns the value of the '<em><b>Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether or not this is a percentage axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Percent</em>' attribute.
	 * @see #isSetPercent()
	 * @see #unsetPercent()
	 * @see #setPercent(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Percent()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element' name='Percent'"
	 * @generated
	 */
	boolean isPercent();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPercent
	 * <em>Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Percent</em>' attribute.
	 * @see #isSetPercent()
	 * @see #unsetPercent()
	 * @see #isPercent()
	 * @generated
	 */
	void setPercent(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPercent
	 * <em>Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetPercent()
	 * @see #isPercent()
	 * @see #setPercent(boolean)
	 * @generated
	 */
	void unsetPercent();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPercent
	 * <em>Percent</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Percent</em>' attribute is set.
	 * @see #unsetPercent()
	 * @see #isPercent()
	 * @see #setPercent(boolean)
	 * @generated
	 */
	boolean isSetPercent();

	/**
	 * Returns the value of the '<em><b>Label Within Axes</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether axis labels are within axes, i.e. inside the axis delimited
	 * area.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label Within Axes</em>' attribute.
	 * @see #isSetLabelWithinAxes()
	 * @see #unsetLabelWithinAxes()
	 * @see #setLabelWithinAxes(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_LabelWithinAxes()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='LabelWithinAxes'"
	 * @generated
	 */
	boolean isLabelWithinAxes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isLabelWithinAxes
	 * <em>Label Within Axes</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Within Axes</em>' attribute.
	 * @see #isSetLabelWithinAxes()
	 * @see #unsetLabelWithinAxes()
	 * @see #isLabelWithinAxes()
	 * @generated
	 */
	void setLabelWithinAxes(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isLabelWithinAxes
	 * <em>Label Within Axes</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetLabelWithinAxes()
	 * @see #isLabelWithinAxes()
	 * @see #setLabelWithinAxes(boolean)
	 * @generated
	 */
	void unsetLabelWithinAxes();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isLabelWithinAxes
	 * <em>Label Within Axes</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Label Within Axes</em>' attribute is
	 *         set.
	 * @see #unsetLabelWithinAxes()
	 * @see #isLabelWithinAxes()
	 * @see #setLabelWithinAxes(boolean)
	 * @generated
	 */
	boolean isSetLabelWithinAxes();

	/**
	 * Returns the value of the '<em><b>Aligned</b></em>' attribute. The default
	 * value is <code>"false"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * Specifies whether the zero point of this axis is to be aligned with other
	 * axes whose "Aligned" is true.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Aligned</em>' attribute.
	 * @see #isSetAligned()
	 * @see #unsetAligned()
	 * @see #setAligned(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Aligned()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='Aligned'"
	 * @generated
	 */
	boolean isAligned();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isAligned
	 * <em>Aligned</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Aligned</em>' attribute.
	 * @see #isSetAligned()
	 * @see #unsetAligned()
	 * @see #isAligned()
	 * @generated
	 */
	void setAligned(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isAligned
	 * <em>Aligned</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetAligned()
	 * @see #isAligned()
	 * @see #setAligned(boolean)
	 * @generated
	 */
	void unsetAligned();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isAligned
	 * <em>Aligned</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Aligned</em>' attribute is set.
	 * @see #unsetAligned()
	 * @see #isAligned()
	 * @see #setAligned(boolean)
	 * @generated
	 */
	boolean isSetAligned();

	/**
	 * Returns the value of the '<em><b>Side By Side</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether the data points of this axis is to be placed side by side
	 * instead of overlayed with those of other axes whose "SideBySide" is true.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Side By Side</em>' attribute.
	 * @see #isSetSideBySide()
	 * @see #unsetSideBySide()
	 * @see #setSideBySide(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_SideBySide()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='SideBySide'"
	 * @generated
	 */
	boolean isSideBySide();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isSideBySide <em>Side By
	 * Side</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Side By Side</em>' attribute.
	 * @see #isSetSideBySide()
	 * @see #unsetSideBySide()
	 * @see #isSideBySide()
	 * @generated
	 */
	void setSideBySide(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isSideBySide <em>Side By
	 * Side</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetSideBySide()
	 * @see #isSideBySide()
	 * @see #setSideBySide(boolean)
	 * @generated
	 */
	void unsetSideBySide();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isSideBySide <em>Side By
	 * Side</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Side By Side</em>' attribute is set.
	 * @see #unsetSideBySide()
	 * @see #isSideBySide()
	 * @see #setSideBySide(boolean)
	 * @generated
	 */
	boolean isSetSideBySide();

	/**
	 * Returns the value of the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The element represents a cursor for axis area.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Cursor</em>' containment reference.
	 * @see #setCursor(Cursor)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_Cursor()
	 * @model containment="true" extendedMetaData="kind='element' name='Cursor'"
	 * @generated
	 */
	Cursor getCursor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getCursor
	 * <em>Cursor</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Cursor</em>' containment reference.
	 * @see #getCursor()
	 * @generated
	 */
	void setCursor(Cursor value);

	/**
	 * Returns the value of the '<em><b>Label Span</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Give the user a way to customize a fixed thickness of axis label. Span means
	 * width for vertical axis, and height for horizontal axis. By default, this
	 * value is unset.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label Span</em>' attribute.
	 * @see #isSetLabelSpan()
	 * @see #unsetLabelSpan()
	 * @see #setLabelSpan(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_LabelSpan()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='LabelSpan'"
	 * @generated
	 */
	double getLabelSpan();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabelSpan <em>Label
	 * Span</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Span</em>' attribute.
	 * @see #isSetLabelSpan()
	 * @see #unsetLabelSpan()
	 * @see #getLabelSpan()
	 * @generated
	 */
	void setLabelSpan(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabelSpan <em>Label
	 * Span</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetLabelSpan()
	 * @see #getLabelSpan()
	 * @see #setLabelSpan(double)
	 * @generated
	 */
	void unsetLabelSpan();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabelSpan <em>Label
	 * Span</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Label Span</em>' attribute is set.
	 * @see #unsetLabelSpan()
	 * @see #getLabelSpan()
	 * @see #setLabelSpan(double)
	 * @generated
	 */
	boolean isSetLabelSpan();

	/**
	 * Returns the value of the '<em><b>Axis Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> This field
	 * indicates this axis will use what percent size of chart plot height if the
	 * AxesStudyLayout of ChartWithAxes is true. The value of this field is just a
	 * number, not a percent value. The actual percent value will be computed by
	 * dividing total numbers of all axes. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Axis Percent</em>' attribute.
	 * @see #isSetAxisPercent()
	 * @see #unsetAxisPercent()
	 * @see #setAxisPercent(int)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getAxis_AxisPercent()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        required="true" extendedMetaData="kind='element' name='AxisPercent'"
	 * @generated
	 */
	int getAxisPercent();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getAxisPercent <em>Axis
	 * Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Axis Percent</em>' attribute.
	 * @see #isSetAxisPercent()
	 * @see #unsetAxisPercent()
	 * @see #getAxisPercent()
	 * @generated
	 */
	void setAxisPercent(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getAxisPercent <em>Axis
	 * Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetAxisPercent()
	 * @see #getAxisPercent()
	 * @see #setAxisPercent(int)
	 * @generated
	 */
	void unsetAxisPercent();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getAxisPercent <em>Axis
	 * Percent</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Axis Percent</em>' attribute is set.
	 * @see #unsetAxisPercent()
	 * @see #getAxisPercent()
	 * @see #setAxisPercent(int)
	 * @generated
	 */
	boolean isSetAxisPercent();

	/**
	 * 
	 * @return All runtime series associated with a particular axis
	 */
	Series[] getRuntimeSeries();

	/**
	 * @generated
	 */
	Axis copyInstance();

} // Axis
