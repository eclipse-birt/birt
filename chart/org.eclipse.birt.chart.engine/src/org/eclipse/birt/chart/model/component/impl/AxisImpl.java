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

package org.eclipse.birt.chart.model.component.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Axis</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getTitle
 * <em>Title</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getSubTitle
 * <em>Sub Title</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getTitlePosition
 * <em>Title Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getAssociatedAxes
 * <em>Associated Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getAncillaryAxes
 * <em>Ancillary Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getSeriesDefinitions
 * <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getGapWidth
 * <em>Gap Width</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getOrientation
 * <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getLabelPosition
 * <em>Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isStaggered
 * <em>Staggered</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getInterval
 * <em>Interval</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getMarkerLines
 * <em>Marker Lines</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getMarkerRanges
 * <em>Marker Ranges</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getMajorGrid
 * <em>Major Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getMinorGrid
 * <em>Minor Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getScale
 * <em>Scale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getOrigin
 * <em>Origin</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isPrimaryAxis
 * <em>Primary Axis</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isCategoryAxis
 * <em>Category Axis</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isPercent
 * <em>Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isLabelWithinAxes
 * <em>Label Within Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isAligned
 * <em>Aligned</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#isSideBySide
 * <em>Side By Side</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getCursor
 * <em>Cursor</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getLabelSpan
 * <em>Label Span</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.AxisImpl#getAxisPercent
 * <em>Axis Percent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AxisImpl extends EObjectImpl implements Axis {

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final AxisType TYPE_EDEFAULT = AxisType.LINEAR_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected AxisType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected Label title;

	/**
	 * The cached value of the '{@link #getSubTitle() <em>Sub Title</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSubTitle()
	 * @generated
	 * @ordered
	 */
	protected Label subTitle;

	/**
	 * The default value of the ' {@link #getTitlePosition() <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTitlePosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position TITLE_POSITION_EDEFAULT = Position.LEFT_LITERAL;

	/**
	 * The cached value of the ' {@link #getTitlePosition() <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTitlePosition()
	 * @generated
	 * @ordered
	 */
	protected Position titlePosition = TITLE_POSITION_EDEFAULT;

	/**
	 * This is true if the Title Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean titlePositionESet;

	/**
	 * The cached value of the '{@link #getAssociatedAxes() <em>Associated
	 * Axes</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAssociatedAxes()
	 * @generated
	 * @ordered
	 */
	protected EList<Axis> associatedAxes;

	/**
	 * The cached value of the '{@link #getAncillaryAxes() <em>Ancillary Axes</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAncillaryAxes()
	 * @generated
	 * @ordered
	 */
	protected EList<Axis> ancillaryAxes;

	/**
	 * The cached value of the '{@link #getSeriesDefinitions() <em>Series
	 * Definitions</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getSeriesDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<SeriesDefinition> seriesDefinitions;

	/**
	 * The default value of the '{@link #getGapWidth() <em>Gap Width</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGapWidth()
	 * @generated
	 * @ordered
	 */
	protected static final double GAP_WIDTH_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getGapWidth() <em>Gap Width</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGapWidth()
	 * @generated
	 * @ordered
	 */
	protected double gapWidth = GAP_WIDTH_EDEFAULT;

	/**
	 * This is true if the Gap Width attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean gapWidthESet;

	/**
	 * The default value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected static final Orientation ORIENTATION_EDEFAULT = Orientation.VERTICAL_LITERAL;

	/**
	 * The cached value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected Orientation orientation = ORIENTATION_EDEFAULT;

	/**
	 * This is true if the Orientation attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean orientationESet;

	/**
	 * The cached value of the '{@link #getLineAttributes() <em>Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes lineAttributes;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * The cached value of the '{@link #getFormatSpecifier() <em>Format
	 * Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier formatSpecifier;

	/**
	 * The default value of the ' {@link #getLabelPosition() <em>Label
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position LABEL_POSITION_EDEFAULT = Position.LEFT_LITERAL;

	/**
	 * The cached value of the ' {@link #getLabelPosition() <em>Label
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelPosition()
	 * @generated
	 * @ordered
	 */
	protected Position labelPosition = LABEL_POSITION_EDEFAULT;

	/**
	 * This is true if the Label Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean labelPositionESet;

	/**
	 * The default value of the '{@link #isStaggered() <em>Staggered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStaggered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STAGGERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isStaggered() <em>Staggered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStaggered()
	 * @generated
	 * @ordered
	 */
	protected boolean staggered = STAGGERED_EDEFAULT;

	/**
	 * This is true if the Staggered attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean staggeredESet;

	/**
	 * The default value of the '{@link #getInterval() <em>Interval</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInterval()
	 * @generated
	 * @ordered
	 */
	protected static final int INTERVAL_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getInterval() <em>Interval</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInterval()
	 * @generated
	 * @ordered
	 */
	protected int interval = INTERVAL_EDEFAULT;

	/**
	 * This is true if the Interval attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean intervalESet;

	/**
	 * The cached value of the '{@link #getMarkerLines() <em>Marker Lines</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMarkerLines()
	 * @generated
	 * @ordered
	 */
	protected EList<MarkerLine> markerLines;

	/**
	 * The cached value of the '{@link #getMarkerRanges() <em>Marker Ranges</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMarkerRanges()
	 * @generated
	 * @ordered
	 */
	protected EList<MarkerRange> markerRanges;

	/**
	 * The cached value of the '{@link #getTriggers() <em>Triggers</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTriggers()
	 * @generated
	 * @ordered
	 */
	protected EList<Trigger> triggers;

	/**
	 * The cached value of the '{@link #getMajorGrid() <em>Major Grid</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMajorGrid()
	 * @generated
	 * @ordered
	 */
	protected Grid majorGrid;

	/**
	 * The cached value of the '{@link #getMinorGrid() <em>Minor Grid</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMinorGrid()
	 * @generated
	 * @ordered
	 */
	protected Grid minorGrid;

	/**
	 * The cached value of the '{@link #getScale() <em>Scale</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getScale()
	 * @generated
	 * @ordered
	 */
	protected Scale scale;

	/**
	 * The cached value of the '{@link #getOrigin() <em>Origin</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected AxisOrigin origin;

	/**
	 * The default value of the '{@link #isPrimaryAxis() <em>Primary Axis</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isPrimaryAxis()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PRIMARY_AXIS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPrimaryAxis() <em>Primary Axis</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isPrimaryAxis()
	 * @generated
	 * @ordered
	 */
	protected boolean primaryAxis = PRIMARY_AXIS_EDEFAULT;

	/**
	 * This is true if the Primary Axis attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean primaryAxisESet;

	/**
	 * The default value of the ' {@link #isCategoryAxis() <em>Category Axis</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isCategoryAxis()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CATEGORY_AXIS_EDEFAULT = false;

	/**
	 * The cached value of the ' {@link #isCategoryAxis() <em>Category Axis</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isCategoryAxis()
	 * @generated
	 * @ordered
	 */
	protected boolean categoryAxis = CATEGORY_AXIS_EDEFAULT;

	/**
	 * This is true if the Category Axis attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean categoryAxisESet;

	/**
	 * The default value of the '{@link #isPercent() <em>Percent</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isPercent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PERCENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPercent() <em>Percent</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isPercent()
	 * @generated
	 * @ordered
	 */
	protected boolean percent = PERCENT_EDEFAULT;

	/**
	 * This is true if the Percent attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean percentESet;

	/**
	 * The default value of the '{@link #isLabelWithinAxes() <em>Label Within
	 * Axes</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isLabelWithinAxes()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LABEL_WITHIN_AXES_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLabelWithinAxes() <em>Label Within
	 * Axes</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isLabelWithinAxes()
	 * @generated
	 * @ordered
	 */
	protected boolean labelWithinAxes = LABEL_WITHIN_AXES_EDEFAULT;

	/**
	 * This is true if the Label Within Axes attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean labelWithinAxesESet;

	/**
	 * The default value of the '{@link #isAligned() <em>Aligned</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAligned()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ALIGNED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAligned() <em>Aligned</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAligned()
	 * @generated
	 * @ordered
	 */
	protected boolean aligned = ALIGNED_EDEFAULT;

	/**
	 * This is true if the Aligned attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean alignedESet;

	/**
	 * The default value of the '{@link #isSideBySide() <em>Side By Side</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSideBySide()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SIDE_BY_SIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSideBySide() <em>Side By Side</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSideBySide()
	 * @generated
	 * @ordered
	 */
	protected boolean sideBySide = SIDE_BY_SIDE_EDEFAULT;

	/**
	 * This is true if the Side By Side attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean sideBySideESet;

	/**
	 * The cached value of the '{@link #getCursor() <em>Cursor</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCursor()
	 * @generated
	 * @ordered
	 */
	protected Cursor cursor;

	/**
	 * The default value of the '{@link #getLabelSpan() <em>Label Span</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelSpan()
	 * @generated
	 * @ordered
	 */
	protected static final double LABEL_SPAN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLabelSpan() <em>Label Span</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelSpan()
	 * @generated
	 * @ordered
	 */
	protected double labelSpan = LABEL_SPAN_EDEFAULT;

	/**
	 * This is true if the Label Span attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean labelSpanESet;

	/**
	 * The default value of the '{@link #getAxisPercent() <em>Axis Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAxisPercent()
	 * @generated
	 * @ordered
	 */
	protected static final int AXIS_PERCENT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAxisPercent() <em>Axis Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAxisPercent()
	 * @generated
	 * @ordered
	 */
	protected int axisPercent = AXIS_PERCENT_EDEFAULT;

	/**
	 * This is true if the Axis Percent attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean axisPercentESet;

	/*
	 * private static int iLastID = Integer.MIN_VALUE; private final int iID;
	 */

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AxisImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.AXIS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AxisType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(AxisType newType) {
		AxisType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__TYPE, oldType, type,
					!oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetType() {
		AxisType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__TYPE, oldType, TYPE_EDEFAULT,
					oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetType() {
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Label getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetTitle(Label newTitle, NotificationChain msgs) {
		Label oldTitle = title;
		title = newTitle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__TITLE,
					oldTitle, newTitle);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTitle(Label newTitle) {
		if (newTitle != title) {
			NotificationChain msgs = null;
			if (title != null)
				msgs = ((InternalEObject) title).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__TITLE, null, msgs);
			if (newTitle != null)
				msgs = ((InternalEObject) newTitle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__TITLE, null, msgs);
			msgs = basicSetTitle(newTitle, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__TITLE, newTitle, newTitle));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Label getSubTitle() {
		return subTitle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSubTitle(Label newSubTitle, NotificationChain msgs) {
		Label oldSubTitle = subTitle;
		subTitle = newSubTitle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__SUB_TITLE, oldSubTitle, newSubTitle);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSubTitle(Label newSubTitle) {
		if (newSubTitle != subTitle) {
			NotificationChain msgs = null;
			if (subTitle != null)
				msgs = ((InternalEObject) subTitle).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__SUB_TITLE, null, msgs);
			if (newSubTitle != null)
				msgs = ((InternalEObject) newSubTitle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__SUB_TITLE, null, msgs);
			msgs = basicSetSubTitle(newSubTitle, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__SUB_TITLE, newSubTitle,
					newSubTitle));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Position getTitlePosition() {
		return titlePosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTitlePosition(Position newTitlePosition) {
		Position oldTitlePosition = titlePosition;
		titlePosition = newTitlePosition == null ? TITLE_POSITION_EDEFAULT : newTitlePosition;
		boolean oldTitlePositionESet = titlePositionESet;
		titlePositionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__TITLE_POSITION,
					oldTitlePosition, titlePosition, !oldTitlePositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTitlePosition() {
		Position oldTitlePosition = titlePosition;
		boolean oldTitlePositionESet = titlePositionESet;
		titlePosition = TITLE_POSITION_EDEFAULT;
		titlePositionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__TITLE_POSITION,
					oldTitlePosition, TITLE_POSITION_EDEFAULT, oldTitlePositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTitlePosition() {
		return titlePositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Axis> getAssociatedAxes() {
		if (associatedAxes == null) {
			associatedAxes = new EObjectContainmentEList<Axis>(Axis.class, this,
					ComponentPackage.AXIS__ASSOCIATED_AXES);
		}
		return associatedAxes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Axis> getAncillaryAxes() {
		if (ancillaryAxes == null) {
			ancillaryAxes = new EObjectContainmentEList<Axis>(Axis.class, this, ComponentPackage.AXIS__ANCILLARY_AXES);
		}
		return ancillaryAxes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<SeriesDefinition> getSeriesDefinitions() {
		if (seriesDefinitions == null) {
			seriesDefinitions = new EObjectContainmentEList<SeriesDefinition>(SeriesDefinition.class, this,
					ComponentPackage.AXIS__SERIES_DEFINITIONS);
		}
		return seriesDefinitions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getGapWidth() {
		return gapWidth;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGapWidth(double newGapWidth) {
		double oldGapWidth = gapWidth;
		gapWidth = newGapWidth;
		boolean oldGapWidthESet = gapWidthESet;
		gapWidthESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__GAP_WIDTH, oldGapWidth,
					gapWidth, !oldGapWidthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetGapWidth() {
		double oldGapWidth = gapWidth;
		boolean oldGapWidthESet = gapWidthESet;
		gapWidth = GAP_WIDTH_EDEFAULT;
		gapWidthESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__GAP_WIDTH, oldGapWidth,
					GAP_WIDTH_EDEFAULT, oldGapWidthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetGapWidth() {
		return gapWidthESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOrientation(Orientation newOrientation) {
		Orientation oldOrientation = orientation;
		orientation = newOrientation == null ? ORIENTATION_EDEFAULT : newOrientation;
		boolean oldOrientationESet = orientationESet;
		orientationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__ORIENTATION, oldOrientation,
					orientation, !oldOrientationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetOrientation() {
		Orientation oldOrientation = orientation;
		boolean oldOrientationESet = orientationESet;
		orientation = ORIENTATION_EDEFAULT;
		orientationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__ORIENTATION, oldOrientation,
					ORIENTATION_EDEFAULT, oldOrientationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetOrientation() {
		return orientationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getLineAttributes() {
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLineAttributes(LineAttributes newLineAttributes, NotificationChain msgs) {
		LineAttributes oldLineAttributes = lineAttributes;
		lineAttributes = newLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLineAttributes(LineAttributes newLineAttributes) {
		if (newLineAttributes != lineAttributes) {
			NotificationChain msgs = null;
			if (lineAttributes != null)
				msgs = ((InternalEObject) lineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__LINE_ATTRIBUTES, null, msgs);
			if (newLineAttributes != null)
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__LINE_ATTRIBUTES, null, msgs);
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs) {
		Label oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__LABEL,
					oldLabel, newLabel);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabel(Label newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null)
				msgs = ((InternalEObject) label).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__LABEL, null, msgs);
			if (newLabel != null)
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__LABEL, null, msgs);
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__LABEL, newLabel, newLabel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FormatSpecifier getFormatSpecifier() {
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs) {
		FormatSpecifier oldFormatSpecifier = formatSpecifier;
		formatSpecifier = newFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null)
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__FORMAT_SPECIFIER, null, msgs);
			if (newFormatSpecifier != null)
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__FORMAT_SPECIFIER, null, msgs);
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__FORMAT_SPECIFIER,
					newFormatSpecifier, newFormatSpecifier));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Position getLabelPosition() {
		return labelPosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelPosition(Position newLabelPosition) {
		Position oldLabelPosition = labelPosition;
		labelPosition = newLabelPosition == null ? LABEL_POSITION_EDEFAULT : newLabelPosition;
		boolean oldLabelPositionESet = labelPositionESet;
		labelPositionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__LABEL_POSITION,
					oldLabelPosition, labelPosition, !oldLabelPositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLabelPosition() {
		Position oldLabelPosition = labelPosition;
		boolean oldLabelPositionESet = labelPositionESet;
		labelPosition = LABEL_POSITION_EDEFAULT;
		labelPositionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__LABEL_POSITION,
					oldLabelPosition, LABEL_POSITION_EDEFAULT, oldLabelPositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLabelPosition() {
		return labelPositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isStaggered() {
		return staggered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStaggered(boolean newStaggered) {
		boolean oldStaggered = staggered;
		staggered = newStaggered;
		boolean oldStaggeredESet = staggeredESet;
		staggeredESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__STAGGERED, oldStaggered,
					staggered, !oldStaggeredESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetStaggered() {
		boolean oldStaggered = staggered;
		boolean oldStaggeredESet = staggeredESet;
		staggered = STAGGERED_EDEFAULT;
		staggeredESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__STAGGERED, oldStaggered,
					STAGGERED_EDEFAULT, oldStaggeredESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetStaggered() {
		return staggeredESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInterval(int newInterval) {
		int oldInterval = interval;
		interval = newInterval;
		boolean oldIntervalESet = intervalESet;
		intervalESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__INTERVAL, oldInterval,
					interval, !oldIntervalESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetInterval() {
		int oldInterval = interval;
		boolean oldIntervalESet = intervalESet;
		interval = INTERVAL_EDEFAULT;
		intervalESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__INTERVAL, oldInterval,
					INTERVAL_EDEFAULT, oldIntervalESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetInterval() {
		return intervalESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<MarkerLine> getMarkerLines() {
		if (markerLines == null) {
			markerLines = new EObjectContainmentEList<MarkerLine>(MarkerLine.class, this,
					ComponentPackage.AXIS__MARKER_LINES);
		}
		return markerLines;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<MarkerRange> getMarkerRanges() {
		if (markerRanges == null) {
			markerRanges = new EObjectContainmentEList<MarkerRange>(MarkerRange.class, this,
					ComponentPackage.AXIS__MARKER_RANGES);
		}
		return markerRanges;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, ComponentPackage.AXIS__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Grid getMajorGrid() {
		return majorGrid;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetMajorGrid(Grid newMajorGrid, NotificationChain msgs) {
		Grid oldMajorGrid = majorGrid;
		majorGrid = newMajorGrid;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__MAJOR_GRID, oldMajorGrid, newMajorGrid);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMajorGrid(Grid newMajorGrid) {
		if (newMajorGrid != majorGrid) {
			NotificationChain msgs = null;
			if (majorGrid != null)
				msgs = ((InternalEObject) majorGrid).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__MAJOR_GRID, null, msgs);
			if (newMajorGrid != null)
				msgs = ((InternalEObject) newMajorGrid).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__MAJOR_GRID, null, msgs);
			msgs = basicSetMajorGrid(newMajorGrid, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__MAJOR_GRID, newMajorGrid,
					newMajorGrid));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Grid getMinorGrid() {
		return minorGrid;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetMinorGrid(Grid newMinorGrid, NotificationChain msgs) {
		Grid oldMinorGrid = minorGrid;
		minorGrid = newMinorGrid;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__MINOR_GRID, oldMinorGrid, newMinorGrid);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMinorGrid(Grid newMinorGrid) {
		if (newMinorGrid != minorGrid) {
			NotificationChain msgs = null;
			if (minorGrid != null)
				msgs = ((InternalEObject) minorGrid).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__MINOR_GRID, null, msgs);
			if (newMinorGrid != null)
				msgs = ((InternalEObject) newMinorGrid).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__MINOR_GRID, null, msgs);
			msgs = basicSetMinorGrid(newMinorGrid, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__MINOR_GRID, newMinorGrid,
					newMinorGrid));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Scale getScale() {
		return scale;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetScale(Scale newScale, NotificationChain msgs) {
		Scale oldScale = scale;
		scale = newScale;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__SCALE,
					oldScale, newScale);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setScale(Scale newScale) {
		if (newScale != scale) {
			NotificationChain msgs = null;
			if (scale != null)
				msgs = ((InternalEObject) scale).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__SCALE, null, msgs);
			if (newScale != null)
				msgs = ((InternalEObject) newScale).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__SCALE, null, msgs);
			msgs = basicSetScale(newScale, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__SCALE, newScale, newScale));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AxisOrigin getOrigin() {
		return origin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetOrigin(AxisOrigin newOrigin, NotificationChain msgs) {
		AxisOrigin oldOrigin = origin;
		origin = newOrigin;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__ORIGIN, oldOrigin, newOrigin);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOrigin(AxisOrigin newOrigin) {
		if (newOrigin != origin) {
			NotificationChain msgs = null;
			if (origin != null)
				msgs = ((InternalEObject) origin).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__ORIGIN, null, msgs);
			if (newOrigin != null)
				msgs = ((InternalEObject) newOrigin).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__ORIGIN, null, msgs);
			msgs = basicSetOrigin(newOrigin, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__ORIGIN, newOrigin, newOrigin));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isPrimaryAxis() {
		return primaryAxis;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPrimaryAxis(boolean newPrimaryAxis) {
		boolean oldPrimaryAxis = primaryAxis;
		primaryAxis = newPrimaryAxis;
		boolean oldPrimaryAxisESet = primaryAxisESet;
		primaryAxisESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__PRIMARY_AXIS, oldPrimaryAxis,
					primaryAxis, !oldPrimaryAxisESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetPrimaryAxis() {
		boolean oldPrimaryAxis = primaryAxis;
		boolean oldPrimaryAxisESet = primaryAxisESet;
		primaryAxis = PRIMARY_AXIS_EDEFAULT;
		primaryAxisESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__PRIMARY_AXIS, oldPrimaryAxis,
					PRIMARY_AXIS_EDEFAULT, oldPrimaryAxisESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetPrimaryAxis() {
		return primaryAxisESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isCategoryAxis() {
		return categoryAxis;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCategoryAxis(boolean newCategoryAxis) {
		boolean oldCategoryAxis = categoryAxis;
		categoryAxis = newCategoryAxis;
		boolean oldCategoryAxisESet = categoryAxisESet;
		categoryAxisESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__CATEGORY_AXIS, oldCategoryAxis,
					categoryAxis, !oldCategoryAxisESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetCategoryAxis() {
		boolean oldCategoryAxis = categoryAxis;
		boolean oldCategoryAxisESet = categoryAxisESet;
		categoryAxis = CATEGORY_AXIS_EDEFAULT;
		categoryAxisESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__CATEGORY_AXIS,
					oldCategoryAxis, CATEGORY_AXIS_EDEFAULT, oldCategoryAxisESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetCategoryAxis() {
		return categoryAxisESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isPercent() {
		return percent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPercent(boolean newPercent) {
		boolean oldPercent = percent;
		percent = newPercent;
		boolean oldPercentESet = percentESet;
		percentESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__PERCENT, oldPercent, percent,
					!oldPercentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetPercent() {
		boolean oldPercent = percent;
		boolean oldPercentESet = percentESet;
		percent = PERCENT_EDEFAULT;
		percentESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__PERCENT, oldPercent,
					PERCENT_EDEFAULT, oldPercentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetPercent() {
		return percentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isLabelWithinAxes() {
		return labelWithinAxes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelWithinAxes(boolean newLabelWithinAxes) {
		boolean oldLabelWithinAxes = labelWithinAxes;
		labelWithinAxes = newLabelWithinAxes;
		boolean oldLabelWithinAxesESet = labelWithinAxesESet;
		labelWithinAxesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__LABEL_WITHIN_AXES,
					oldLabelWithinAxes, labelWithinAxes, !oldLabelWithinAxesESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLabelWithinAxes() {
		boolean oldLabelWithinAxes = labelWithinAxes;
		boolean oldLabelWithinAxesESet = labelWithinAxesESet;
		labelWithinAxes = LABEL_WITHIN_AXES_EDEFAULT;
		labelWithinAxesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__LABEL_WITHIN_AXES,
					oldLabelWithinAxes, LABEL_WITHIN_AXES_EDEFAULT, oldLabelWithinAxesESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLabelWithinAxes() {
		return labelWithinAxesESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAligned() {
		return aligned;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAligned(boolean newAligned) {
		boolean oldAligned = aligned;
		aligned = newAligned;
		boolean oldAlignedESet = alignedESet;
		alignedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__ALIGNED, oldAligned, aligned,
					!oldAlignedESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetAligned() {
		boolean oldAligned = aligned;
		boolean oldAlignedESet = alignedESet;
		aligned = ALIGNED_EDEFAULT;
		alignedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__ALIGNED, oldAligned,
					ALIGNED_EDEFAULT, oldAlignedESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetAligned() {
		return alignedESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSideBySide() {
		return sideBySide;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSideBySide(boolean newSideBySide) {
		boolean oldSideBySide = sideBySide;
		sideBySide = newSideBySide;
		boolean oldSideBySideESet = sideBySideESet;
		sideBySideESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__SIDE_BY_SIDE, oldSideBySide,
					sideBySide, !oldSideBySideESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetSideBySide() {
		boolean oldSideBySide = sideBySide;
		boolean oldSideBySideESet = sideBySideESet;
		sideBySide = SIDE_BY_SIDE_EDEFAULT;
		sideBySideESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__SIDE_BY_SIDE, oldSideBySide,
					SIDE_BY_SIDE_EDEFAULT, oldSideBySideESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetSideBySide() {
		return sideBySideESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetCursor(Cursor newCursor, NotificationChain msgs) {
		Cursor oldCursor = cursor;
		cursor = newCursor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.AXIS__CURSOR, oldCursor, newCursor);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCursor(Cursor newCursor) {
		if (newCursor != cursor) {
			NotificationChain msgs = null;
			if (cursor != null)
				msgs = ((InternalEObject) cursor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__CURSOR, null, msgs);
			if (newCursor != null)
				msgs = ((InternalEObject) newCursor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.AXIS__CURSOR, null, msgs);
			msgs = basicSetCursor(newCursor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__CURSOR, newCursor, newCursor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getLabelSpan() {
		return labelSpan;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelSpan(double newLabelSpan) {
		double oldLabelSpan = labelSpan;
		labelSpan = newLabelSpan;
		boolean oldLabelSpanESet = labelSpanESet;
		labelSpanESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__LABEL_SPAN, oldLabelSpan,
					labelSpan, !oldLabelSpanESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLabelSpan() {
		double oldLabelSpan = labelSpan;
		boolean oldLabelSpanESet = labelSpanESet;
		labelSpan = LABEL_SPAN_EDEFAULT;
		labelSpanESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__LABEL_SPAN, oldLabelSpan,
					LABEL_SPAN_EDEFAULT, oldLabelSpanESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLabelSpan() {
		return labelSpanESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getAxisPercent() {
		return axisPercent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAxisPercent(int newAxisPercent) {
		int oldAxisPercent = axisPercent;
		axisPercent = newAxisPercent;
		boolean oldAxisPercentESet = axisPercentESet;
		axisPercentESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.AXIS__AXIS_PERCENT, oldAxisPercent,
					axisPercent, !oldAxisPercentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetAxisPercent() {
		int oldAxisPercent = axisPercent;
		boolean oldAxisPercentESet = axisPercentESet;
		axisPercent = AXIS_PERCENT_EDEFAULT;
		axisPercentESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.AXIS__AXIS_PERCENT, oldAxisPercent,
					AXIS_PERCENT_EDEFAULT, oldAxisPercentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetAxisPercent() {
		return axisPercentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.AXIS__TITLE:
			return basicSetTitle(null, msgs);
		case ComponentPackage.AXIS__SUB_TITLE:
			return basicSetSubTitle(null, msgs);
		case ComponentPackage.AXIS__ASSOCIATED_AXES:
			return ((InternalEList<?>) getAssociatedAxes()).basicRemove(otherEnd, msgs);
		case ComponentPackage.AXIS__ANCILLARY_AXES:
			return ((InternalEList<?>) getAncillaryAxes()).basicRemove(otherEnd, msgs);
		case ComponentPackage.AXIS__SERIES_DEFINITIONS:
			return ((InternalEList<?>) getSeriesDefinitions()).basicRemove(otherEnd, msgs);
		case ComponentPackage.AXIS__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case ComponentPackage.AXIS__LABEL:
			return basicSetLabel(null, msgs);
		case ComponentPackage.AXIS__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
		case ComponentPackage.AXIS__MARKER_LINES:
			return ((InternalEList<?>) getMarkerLines()).basicRemove(otherEnd, msgs);
		case ComponentPackage.AXIS__MARKER_RANGES:
			return ((InternalEList<?>) getMarkerRanges()).basicRemove(otherEnd, msgs);
		case ComponentPackage.AXIS__TRIGGERS:
			return ((InternalEList<?>) getTriggers()).basicRemove(otherEnd, msgs);
		case ComponentPackage.AXIS__MAJOR_GRID:
			return basicSetMajorGrid(null, msgs);
		case ComponentPackage.AXIS__MINOR_GRID:
			return basicSetMinorGrid(null, msgs);
		case ComponentPackage.AXIS__SCALE:
			return basicSetScale(null, msgs);
		case ComponentPackage.AXIS__ORIGIN:
			return basicSetOrigin(null, msgs);
		case ComponentPackage.AXIS__CURSOR:
			return basicSetCursor(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ComponentPackage.AXIS__TYPE:
			return getType();
		case ComponentPackage.AXIS__TITLE:
			return getTitle();
		case ComponentPackage.AXIS__SUB_TITLE:
			return getSubTitle();
		case ComponentPackage.AXIS__TITLE_POSITION:
			return getTitlePosition();
		case ComponentPackage.AXIS__ASSOCIATED_AXES:
			return getAssociatedAxes();
		case ComponentPackage.AXIS__ANCILLARY_AXES:
			return getAncillaryAxes();
		case ComponentPackage.AXIS__SERIES_DEFINITIONS:
			return getSeriesDefinitions();
		case ComponentPackage.AXIS__GAP_WIDTH:
			return getGapWidth();
		case ComponentPackage.AXIS__ORIENTATION:
			return getOrientation();
		case ComponentPackage.AXIS__LINE_ATTRIBUTES:
			return getLineAttributes();
		case ComponentPackage.AXIS__LABEL:
			return getLabel();
		case ComponentPackage.AXIS__FORMAT_SPECIFIER:
			return getFormatSpecifier();
		case ComponentPackage.AXIS__LABEL_POSITION:
			return getLabelPosition();
		case ComponentPackage.AXIS__STAGGERED:
			return isStaggered();
		case ComponentPackage.AXIS__INTERVAL:
			return getInterval();
		case ComponentPackage.AXIS__MARKER_LINES:
			return getMarkerLines();
		case ComponentPackage.AXIS__MARKER_RANGES:
			return getMarkerRanges();
		case ComponentPackage.AXIS__TRIGGERS:
			return getTriggers();
		case ComponentPackage.AXIS__MAJOR_GRID:
			return getMajorGrid();
		case ComponentPackage.AXIS__MINOR_GRID:
			return getMinorGrid();
		case ComponentPackage.AXIS__SCALE:
			return getScale();
		case ComponentPackage.AXIS__ORIGIN:
			return getOrigin();
		case ComponentPackage.AXIS__PRIMARY_AXIS:
			return isPrimaryAxis();
		case ComponentPackage.AXIS__CATEGORY_AXIS:
			return isCategoryAxis();
		case ComponentPackage.AXIS__PERCENT:
			return isPercent();
		case ComponentPackage.AXIS__LABEL_WITHIN_AXES:
			return isLabelWithinAxes();
		case ComponentPackage.AXIS__ALIGNED:
			return isAligned();
		case ComponentPackage.AXIS__SIDE_BY_SIDE:
			return isSideBySide();
		case ComponentPackage.AXIS__CURSOR:
			return getCursor();
		case ComponentPackage.AXIS__LABEL_SPAN:
			return getLabelSpan();
		case ComponentPackage.AXIS__AXIS_PERCENT:
			return getAxisPercent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ComponentPackage.AXIS__TYPE:
			setType((AxisType) newValue);
			return;
		case ComponentPackage.AXIS__TITLE:
			setTitle((Label) newValue);
			return;
		case ComponentPackage.AXIS__SUB_TITLE:
			setSubTitle((Label) newValue);
			return;
		case ComponentPackage.AXIS__TITLE_POSITION:
			setTitlePosition((Position) newValue);
			return;
		case ComponentPackage.AXIS__ASSOCIATED_AXES:
			getAssociatedAxes().clear();
			getAssociatedAxes().addAll((Collection<? extends Axis>) newValue);
			return;
		case ComponentPackage.AXIS__ANCILLARY_AXES:
			getAncillaryAxes().clear();
			getAncillaryAxes().addAll((Collection<? extends Axis>) newValue);
			return;
		case ComponentPackage.AXIS__SERIES_DEFINITIONS:
			getSeriesDefinitions().clear();
			getSeriesDefinitions().addAll((Collection<? extends SeriesDefinition>) newValue);
			return;
		case ComponentPackage.AXIS__GAP_WIDTH:
			setGapWidth((Double) newValue);
			return;
		case ComponentPackage.AXIS__ORIENTATION:
			setOrientation((Orientation) newValue);
			return;
		case ComponentPackage.AXIS__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.AXIS__LABEL:
			setLabel((Label) newValue);
			return;
		case ComponentPackage.AXIS__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
			return;
		case ComponentPackage.AXIS__LABEL_POSITION:
			setLabelPosition((Position) newValue);
			return;
		case ComponentPackage.AXIS__STAGGERED:
			setStaggered((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__INTERVAL:
			setInterval((Integer) newValue);
			return;
		case ComponentPackage.AXIS__MARKER_LINES:
			getMarkerLines().clear();
			getMarkerLines().addAll((Collection<? extends MarkerLine>) newValue);
			return;
		case ComponentPackage.AXIS__MARKER_RANGES:
			getMarkerRanges().clear();
			getMarkerRanges().addAll((Collection<? extends MarkerRange>) newValue);
			return;
		case ComponentPackage.AXIS__TRIGGERS:
			getTriggers().clear();
			getTriggers().addAll((Collection<? extends Trigger>) newValue);
			return;
		case ComponentPackage.AXIS__MAJOR_GRID:
			setMajorGrid((Grid) newValue);
			return;
		case ComponentPackage.AXIS__MINOR_GRID:
			setMinorGrid((Grid) newValue);
			return;
		case ComponentPackage.AXIS__SCALE:
			setScale((Scale) newValue);
			return;
		case ComponentPackage.AXIS__ORIGIN:
			setOrigin((AxisOrigin) newValue);
			return;
		case ComponentPackage.AXIS__PRIMARY_AXIS:
			setPrimaryAxis((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__CATEGORY_AXIS:
			setCategoryAxis((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__PERCENT:
			setPercent((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__LABEL_WITHIN_AXES:
			setLabelWithinAxes((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__ALIGNED:
			setAligned((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__SIDE_BY_SIDE:
			setSideBySide((Boolean) newValue);
			return;
		case ComponentPackage.AXIS__CURSOR:
			setCursor((Cursor) newValue);
			return;
		case ComponentPackage.AXIS__LABEL_SPAN:
			setLabelSpan((Double) newValue);
			return;
		case ComponentPackage.AXIS__AXIS_PERCENT:
			setAxisPercent((Integer) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ComponentPackage.AXIS__TYPE:
			unsetType();
			return;
		case ComponentPackage.AXIS__TITLE:
			setTitle((Label) null);
			return;
		case ComponentPackage.AXIS__SUB_TITLE:
			setSubTitle((Label) null);
			return;
		case ComponentPackage.AXIS__TITLE_POSITION:
			unsetTitlePosition();
			return;
		case ComponentPackage.AXIS__ASSOCIATED_AXES:
			getAssociatedAxes().clear();
			return;
		case ComponentPackage.AXIS__ANCILLARY_AXES:
			getAncillaryAxes().clear();
			return;
		case ComponentPackage.AXIS__SERIES_DEFINITIONS:
			getSeriesDefinitions().clear();
			return;
		case ComponentPackage.AXIS__GAP_WIDTH:
			unsetGapWidth();
			return;
		case ComponentPackage.AXIS__ORIENTATION:
			unsetOrientation();
			return;
		case ComponentPackage.AXIS__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case ComponentPackage.AXIS__LABEL:
			setLabel((Label) null);
			return;
		case ComponentPackage.AXIS__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
			return;
		case ComponentPackage.AXIS__LABEL_POSITION:
			unsetLabelPosition();
			return;
		case ComponentPackage.AXIS__STAGGERED:
			unsetStaggered();
			return;
		case ComponentPackage.AXIS__INTERVAL:
			unsetInterval();
			return;
		case ComponentPackage.AXIS__MARKER_LINES:
			getMarkerLines().clear();
			return;
		case ComponentPackage.AXIS__MARKER_RANGES:
			getMarkerRanges().clear();
			return;
		case ComponentPackage.AXIS__TRIGGERS:
			getTriggers().clear();
			return;
		case ComponentPackage.AXIS__MAJOR_GRID:
			setMajorGrid((Grid) null);
			return;
		case ComponentPackage.AXIS__MINOR_GRID:
			setMinorGrid((Grid) null);
			return;
		case ComponentPackage.AXIS__SCALE:
			setScale((Scale) null);
			return;
		case ComponentPackage.AXIS__ORIGIN:
			setOrigin((AxisOrigin) null);
			return;
		case ComponentPackage.AXIS__PRIMARY_AXIS:
			unsetPrimaryAxis();
			return;
		case ComponentPackage.AXIS__CATEGORY_AXIS:
			unsetCategoryAxis();
			return;
		case ComponentPackage.AXIS__PERCENT:
			unsetPercent();
			return;
		case ComponentPackage.AXIS__LABEL_WITHIN_AXES:
			unsetLabelWithinAxes();
			return;
		case ComponentPackage.AXIS__ALIGNED:
			unsetAligned();
			return;
		case ComponentPackage.AXIS__SIDE_BY_SIDE:
			unsetSideBySide();
			return;
		case ComponentPackage.AXIS__CURSOR:
			setCursor((Cursor) null);
			return;
		case ComponentPackage.AXIS__LABEL_SPAN:
			unsetLabelSpan();
			return;
		case ComponentPackage.AXIS__AXIS_PERCENT:
			unsetAxisPercent();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ComponentPackage.AXIS__TYPE:
			return isSetType();
		case ComponentPackage.AXIS__TITLE:
			return title != null;
		case ComponentPackage.AXIS__SUB_TITLE:
			return subTitle != null;
		case ComponentPackage.AXIS__TITLE_POSITION:
			return isSetTitlePosition();
		case ComponentPackage.AXIS__ASSOCIATED_AXES:
			return associatedAxes != null && !associatedAxes.isEmpty();
		case ComponentPackage.AXIS__ANCILLARY_AXES:
			return ancillaryAxes != null && !ancillaryAxes.isEmpty();
		case ComponentPackage.AXIS__SERIES_DEFINITIONS:
			return seriesDefinitions != null && !seriesDefinitions.isEmpty();
		case ComponentPackage.AXIS__GAP_WIDTH:
			return isSetGapWidth();
		case ComponentPackage.AXIS__ORIENTATION:
			return isSetOrientation();
		case ComponentPackage.AXIS__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case ComponentPackage.AXIS__LABEL:
			return label != null;
		case ComponentPackage.AXIS__FORMAT_SPECIFIER:
			return formatSpecifier != null;
		case ComponentPackage.AXIS__LABEL_POSITION:
			return isSetLabelPosition();
		case ComponentPackage.AXIS__STAGGERED:
			return isSetStaggered();
		case ComponentPackage.AXIS__INTERVAL:
			return isSetInterval();
		case ComponentPackage.AXIS__MARKER_LINES:
			return markerLines != null && !markerLines.isEmpty();
		case ComponentPackage.AXIS__MARKER_RANGES:
			return markerRanges != null && !markerRanges.isEmpty();
		case ComponentPackage.AXIS__TRIGGERS:
			return triggers != null && !triggers.isEmpty();
		case ComponentPackage.AXIS__MAJOR_GRID:
			return majorGrid != null;
		case ComponentPackage.AXIS__MINOR_GRID:
			return minorGrid != null;
		case ComponentPackage.AXIS__SCALE:
			return scale != null;
		case ComponentPackage.AXIS__ORIGIN:
			return origin != null;
		case ComponentPackage.AXIS__PRIMARY_AXIS:
			return isSetPrimaryAxis();
		case ComponentPackage.AXIS__CATEGORY_AXIS:
			return isSetCategoryAxis();
		case ComponentPackage.AXIS__PERCENT:
			return isSetPercent();
		case ComponentPackage.AXIS__LABEL_WITHIN_AXES:
			return isSetLabelWithinAxes();
		case ComponentPackage.AXIS__ALIGNED:
			return isSetAligned();
		case ComponentPackage.AXIS__SIDE_BY_SIDE:
			return isSetSideBySide();
		case ComponentPackage.AXIS__CURSOR:
			return cursor != null;
		case ComponentPackage.AXIS__LABEL_SPAN:
			return isSetLabelSpan();
		case ComponentPackage.AXIS__AXIS_PERCENT:
			return isSetAxisPercent();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (type: "); //$NON-NLS-1$
		if (typeESet)
			result.append(type);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", titlePosition: "); //$NON-NLS-1$
		if (titlePositionESet)
			result.append(titlePosition);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", gapWidth: "); //$NON-NLS-1$
		if (gapWidthESet)
			result.append(gapWidth);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", orientation: "); //$NON-NLS-1$
		if (orientationESet)
			result.append(orientation);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", labelPosition: "); //$NON-NLS-1$
		if (labelPositionESet)
			result.append(labelPosition);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", staggered: "); //$NON-NLS-1$
		if (staggeredESet)
			result.append(staggered);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", interval: "); //$NON-NLS-1$
		if (intervalESet)
			result.append(interval);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", primaryAxis: "); //$NON-NLS-1$
		if (primaryAxisESet)
			result.append(primaryAxis);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", categoryAxis: "); //$NON-NLS-1$
		if (categoryAxisESet)
			result.append(categoryAxis);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", percent: "); //$NON-NLS-1$
		if (percentESet)
			result.append(percent);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", labelWithinAxes: "); //$NON-NLS-1$
		if (labelWithinAxesESet)
			result.append(labelWithinAxes);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", aligned: "); //$NON-NLS-1$
		if (alignedESet)
			result.append(aligned);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", sideBySide: "); //$NON-NLS-1$
		if (sideBySideESet)
			result.append(sideBySide);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", labelSpan: "); //$NON-NLS-1$
		if (labelSpanESet)
			result.append(labelSpan);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", axisPercent: "); //$NON-NLS-1$
		if (axisPercentESet)
			result.append(axisPercent);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method to create an initialized 'Axis' instance
	 * 
	 * @param iAxisType The type of axis defined by Axis.BASE or Axis.ORTHOGONAL
	 * @return axis instance with 'isSet' flag.
	 */
	public static final Axis create(int iAxisType) {
		final Axis ax = ComponentFactory.eINSTANCE.createAxis();
		((AxisImpl) ax).initialize(iAxisType);
		return ax;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize(int iAxisType) {
		// AXIS LABEL COLOR, FONT, OUTLINE, FILLCOLOR, TEXTALIGNMENT, FORMAT
		// SPECIFIER
		setLabel(LabelImpl.create());

		// AXIS LINE
		LineAttributes lia = LineAttributesImpl.create(null, LineStyle.SOLID_LITERAL, 1);
		setLineAttributes(lia);

		// INTERSECTION VALUE
		AxisOrigin ao = AttributeFactory.eINSTANCE.createAxisOrigin();
		ao.setType(IntersectionType.MIN_LITERAL);
		ao.setValue(null);
		setOrigin(ao);

		// PRIMARY AXIS
		setPrimaryAxis(false);

		// AXIS TITLE
		Label la = LabelImpl.create();
		la.getCaption().setValue("Axis Title"); //$NON-NLS-1$
		la.getCaption().getFont().setSize(14);
		la.getCaption().getFont().setBold(true);
		TextAlignment ta = TextAlignmentImpl.create();
		ta.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		ta.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		la.getCaption().getFont().setAlignment(ta);
		la.setVisible(false);
		setTitle(la);

		// MAJOR GRID
		Grid gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(196, 196, 196), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(196, 196, 196), LineStyle.SOLID_LITERAL, 1);
		gr.setTickAttributes(lia);
		gr.setTickStyle(TickStyle.ACROSS_LITERAL);
		setMajorGrid(gr);

		// MINOR GRID
		gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(225, 225, 225), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(225, 225, 225), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		gr.setTickAttributes(lia);
		gr.setTickStyle(TickStyle.ACROSS_LITERAL);
		setMinorGrid(gr);

		// SCALE
		Scale sc = ComponentFactory.eINSTANCE.createScale();
		sc.setMinorGridsPerUnit(5);
		setScale(sc);
		setPercent(false);

		if (iAxisType == Axis.BASE) {
			setOrientation(Orientation.HORIZONTAL_LITERAL);
			setLabelPosition(Position.ABOVE_LITERAL);
		} else if (iAxisType == Axis.ORTHOGONAL) {
			setOrientation(Orientation.VERTICAL_LITERAL);
			setLabelPosition(Position.RIGHT_LITERAL);
		}
		setOrigin(AxisOriginImpl.create(IntersectionType.MAX_LITERAL, null));
	}

	/**
	 * A convenience method to create an initialized 'Axis' instance
	 * 
	 * @param iAxisType The type of axis defined by Axis.BASE or Axis.ORTHOGONAL
	 * @return axis instance without 'isSet' flag.
	 */
	public static final Axis createDefault(int iAxisType) {
		final Axis ax = ComponentFactory.eINSTANCE.createAxis();
		((AxisImpl) ax).initDefault(iAxisType);
		return ax;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initDefault(int iAxisType) {
		// AXIS LABEL COLOR, FONT, OUTLINE, FILLCOLOR, TEXTALIGNMENT, FORMAT
		// SPECIFIER
		setLabel(LabelImpl.createDefault());

		// AXIS LINE
		LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setLineAttributes(lia);

		// INTERSECTION VALUE
		AxisOrigin ao = AxisOriginImpl.createDefault(IntersectionType.MIN_LITERAL, null);
		setOrigin(ao);

		// PRIMARY AXIS
		primaryAxis = false;

		// AXIS TITLE
		Label la = LabelImpl.createDefault(false);
		la.getCaption().setValue(null);
		try {
			ChartElementUtil.setDefaultValue(la.getCaption().getFont(), "size", //$NON-NLS-1$
					14);
			ChartElementUtil.setDefaultValue(la.getCaption().getFont(), "bold", //$NON-NLS-1$
					true);
		} catch (ChartException e) {
		}

		TextAlignment ta = TextAlignmentImpl.createDefault(HorizontalAlignment.CENTER_LITERAL,
				VerticalAlignment.CENTER_LITERAL);
		try {
			ChartElementUtil.setDefaultValue(la.getCaption().getFont(), "alignment", //$NON-NLS-1$
					ta);
		} catch (ChartException e) {
		}

		setTitle(la);

		// MAJOR GRID
		Grid gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1);
		gr.setTickAttributes(lia);
		((GridImpl) gr).tickStyle = TickStyle.ACROSS_LITERAL;
		setMajorGrid(gr);

		// MINOR GRID
		gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		gr.setTickAttributes(lia);
		((GridImpl) gr).tickStyle = TickStyle.ACROSS_LITERAL;
		setMinorGrid(gr);

		// SCALE
		Scale sc = ComponentFactory.eINSTANCE.createScale();
		((ScaleImpl) sc).minorGridsPerUnit = 5;
		setScale(sc);
		percent = false;

		if (iAxisType == Axis.BASE) {
			orientation = Orientation.HORIZONTAL_LITERAL;
			labelPosition = Position.ABOVE_LITERAL;
		} else if (iAxisType == Axis.ORTHOGONAL) {
			orientation = Orientation.VERTICAL_LITERAL;
			labelPosition = Position.RIGHT_LITERAL;
		}
		setOrigin(AxisOriginImpl.createDefault(IntersectionType.MAX_LITERAL, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Axis#getRuntimeSeries()
	 */
	public final Series[] getRuntimeSeries() {
		List<Series> al = new ArrayList<Series>(8);

		for (SeriesDefinition sd : getSeriesDefinitions()) {
			al.addAll(sd.getRunTimeSeries());
		}

		return al.toArray(new Series[al.size()]);
	}

	/**
	 * @generated
	 */
	public Axis copyInstance() {
		AxisImpl dest = new AxisImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Axis src) {

		// children

		if (src.getTitle() != null) {
			setTitle(src.getTitle().copyInstance());
		}

		if (src.getSubTitle() != null) {
			setSubTitle(src.getSubTitle().copyInstance());
		}

		if (src.getAssociatedAxes() != null) {
			EList<Axis> list = getAssociatedAxes();
			for (Axis element : src.getAssociatedAxes()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getAncillaryAxes() != null) {
			EList<Axis> list = getAncillaryAxes();
			for (Axis element : src.getAncillaryAxes()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getSeriesDefinitions() != null) {
			EList<SeriesDefinition> list = getSeriesDefinitions();
			for (SeriesDefinition element : src.getSeriesDefinitions()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getLabel() != null) {
			setLabel(src.getLabel().copyInstance());
		}

		if (src.getFormatSpecifier() != null) {
			setFormatSpecifier(src.getFormatSpecifier().copyInstance());
		}

		if (src.getMarkerLines() != null) {
			EList<MarkerLine> list = getMarkerLines();
			for (MarkerLine element : src.getMarkerLines()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getMarkerRanges() != null) {
			EList<MarkerRange> list = getMarkerRanges();
			for (MarkerRange element : src.getMarkerRanges()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getTriggers() != null) {
			EList<Trigger> list = getTriggers();
			for (Trigger element : src.getTriggers()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getMajorGrid() != null) {
			setMajorGrid(src.getMajorGrid().copyInstance());
		}

		if (src.getMinorGrid() != null) {
			setMinorGrid(src.getMinorGrid().copyInstance());
		}

		if (src.getScale() != null) {
			setScale(src.getScale().copyInstance());
		}

		if (src.getOrigin() != null) {
			setOrigin(src.getOrigin().copyInstance());
		}

		if (src.getCursor() != null) {
			setCursor(src.getCursor().copyInstance());
		}

		// attributes

		type = src.getType();

		typeESet = src.isSetType();

		titlePosition = src.getTitlePosition();

		titlePositionESet = src.isSetTitlePosition();

		gapWidth = src.getGapWidth();

		gapWidthESet = src.isSetGapWidth();

		orientation = src.getOrientation();

		orientationESet = src.isSetOrientation();

		labelPosition = src.getLabelPosition();

		labelPositionESet = src.isSetLabelPosition();

		staggered = src.isStaggered();

		staggeredESet = src.isSetStaggered();

		interval = src.getInterval();

		intervalESet = src.isSetInterval();

		primaryAxis = src.isPrimaryAxis();

		primaryAxisESet = src.isSetPrimaryAxis();

		categoryAxis = src.isCategoryAxis();

		categoryAxisESet = src.isSetCategoryAxis();

		percent = src.isPercent();

		percentESet = src.isSetPercent();

		labelWithinAxes = src.isLabelWithinAxes();

		labelWithinAxesESet = src.isSetLabelWithinAxes();

		aligned = src.isAligned();

		alignedESet = src.isSetAligned();

		sideBySide = src.isSideBySide();

		sideBySideESet = src.isSetSideBySide();

		labelSpan = src.getLabelSpan();

		labelSpanESet = src.isSetLabelSpan();

		axisPercent = src.getAxisPercent();

		axisPercentESet = src.isSetAxisPercent();

	}

} // AxisImpl
