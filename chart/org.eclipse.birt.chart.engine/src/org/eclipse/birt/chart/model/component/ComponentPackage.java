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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package </b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * Schema file for the chart.model package.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.component.ComponentFactory
 * @generated
 */
public interface ComponentPackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "component"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelComponent"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "component"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ComponentPackage eINSTANCE = org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.AxisImpl <em>Axis</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.AxisImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getAxis()
	 * @generated
	 */
	int AXIS = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__TITLE = 1;

	/**
	 * The feature id for the '<em><b>Sub Title</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__SUB_TITLE = 2;

	/**
	 * The feature id for the '<em><b>Title Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__TITLE_POSITION = 3;

	/**
	 * The feature id for the '<em><b>Associated Axes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__ASSOCIATED_AXES = 4;

	/**
	 * The feature id for the '<em><b>Ancillary Axes</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__ANCILLARY_AXES = 5;

	/**
	 * The feature id for the '<em><b>Series Definitions</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__SERIES_DEFINITIONS = 6;

	/**
	 * The feature id for the '<em><b>Gap Width</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__GAP_WIDTH = 7;

	/**
	 * The feature id for the '<em><b>Orientation</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__ORIENTATION = 8;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__LINE_ATTRIBUTES = 9;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__LABEL = 10;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__FORMAT_SPECIFIER = 11;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__LABEL_POSITION = 12;

	/**
	 * The feature id for the '<em><b>Staggered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__STAGGERED = 13;

	/**
	 * The feature id for the '<em><b>Interval</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__INTERVAL = 14;

	/**
	 * The feature id for the '<em><b>Marker Lines</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__MARKER_LINES = 15;

	/**
	 * The feature id for the '<em><b>Marker Ranges</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__MARKER_RANGES = 16;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__TRIGGERS = 17;

	/**
	 * The feature id for the '<em><b>Major Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__MAJOR_GRID = 18;

	/**
	 * The feature id for the '<em><b>Minor Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__MINOR_GRID = 19;

	/**
	 * The feature id for the '<em><b>Scale</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__SCALE = 20;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__ORIGIN = 21;

	/**
	 * The feature id for the '<em><b>Primary Axis</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__PRIMARY_AXIS = 22;

	/**
	 * The feature id for the '<em><b>Category Axis</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__CATEGORY_AXIS = 23;

	/**
	 * The feature id for the '<em><b>Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__PERCENT = 24;

	/**
	 * The feature id for the '<em><b>Label Within Axes</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__LABEL_WITHIN_AXES = 25;

	/**
	 * The feature id for the '<em><b>Aligned</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__ALIGNED = 26;

	/**
	 * The feature id for the '<em><b>Side By Side</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__SIDE_BY_SIDE = 27;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__CURSOR = 28;

	/**
	 * The feature id for the '<em><b>Label Span</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__LABEL_SPAN = 29;

	/**
	 * The feature id for the '<em><b>Axis Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS__AXIS_PERCENT = 30;

	/**
	 * The number of structural features of the '<em>Axis</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS_FEATURE_COUNT = 31;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl
	 * <em>Chart Preferences</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getChartPreferences()
	 * @generated
	 */
	int CHART_PREFERENCES = 1;

	/**
	 * The feature id for the '<em><b>Labels</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHART_PREFERENCES__LABELS = 0;

	/**
	 * The feature id for the '<em><b>Blocks</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHART_PREFERENCES__BLOCKS = 1;

	/**
	 * The number of structural features of the '<em>Chart Preferences</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHART_PREFERENCES_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.CurveFittingImpl
	 * <em>Curve Fitting</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.CurveFittingImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getCurveFitting()
	 * @generated
	 */
	int CURVE_FITTING = 2;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURVE_FITTING__LINE_ATTRIBUTES = 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURVE_FITTING__LABEL = 1;

	/**
	 * The feature id for the '<em><b>Label Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURVE_FITTING__LABEL_ANCHOR = 2;

	/**
	 * The number of structural features of the '<em>Curve Fitting</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURVE_FITTING_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.DialImpl <em>Dial</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.DialImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getDial()
	 * @generated
	 */
	int DIAL = 3;

	/**
	 * The feature id for the '<em><b>Start Angle</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__START_ANGLE = 0;

	/**
	 * The feature id for the '<em><b>Stop Angle</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__STOP_ANGLE = 1;

	/**
	 * The feature id for the '<em><b>Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__RADIUS = 2;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__LINE_ATTRIBUTES = 3;

	/**
	 * The feature id for the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__FILL = 4;

	/**
	 * The feature id for the '<em><b>Dial Regions</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__DIAL_REGIONS = 5;

	/**
	 * The feature id for the '<em><b>Major Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__MAJOR_GRID = 6;

	/**
	 * The feature id for the '<em><b>Minor Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__MINOR_GRID = 7;

	/**
	 * The feature id for the '<em><b>Scale</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__SCALE = 8;

	/**
	 * The feature id for the '<em><b>Inverse Scale</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__INVERSE_SCALE = 9;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__LABEL = 10;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL__FORMAT_SPECIFIER = 11;

	/**
	 * The number of structural features of the '<em>Dial</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_FEATURE_COUNT = 12;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.DialRegionImpl <em>Dial
	 * Region</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.DialRegionImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getDialRegion()
	 * @generated
	 */
	int DIAL_REGION = 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.LabelImpl
	 * <em>Label</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.LabelImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getLabel()
	 * @generated
	 */
	int LABEL = 7;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.GridImpl <em>Grid</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.GridImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getGrid()
	 * @generated
	 */
	int GRID = 6;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl <em>Marker
	 * Line</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.MarkerLineImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getMarkerLine()
	 * @generated
	 */
	int MARKER_LINE = 8;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl
	 * <em>Marker Range</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getMarkerRange()
	 * @generated
	 */
	int MARKER_RANGE = 9;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__OUTLINE = 0;

	/**
	 * The feature id for the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__FILL = 1;

	/**
	 * The feature id for the '<em><b>Start Value</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__START_VALUE = 2;

	/**
	 * The feature id for the '<em><b>End Value</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__END_VALUE = 3;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__LABEL = 4;

	/**
	 * The feature id for the '<em><b>Label Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__LABEL_ANCHOR = 5;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__FORMAT_SPECIFIER = 6;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__TRIGGERS = 7;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE__CURSOR = 8;

	/**
	 * The number of structural features of the '<em>Marker Range</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_RANGE_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__OUTLINE = MARKER_RANGE__OUTLINE;

	/**
	 * The feature id for the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__FILL = MARKER_RANGE__FILL;

	/**
	 * The feature id for the '<em><b>Start Value</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__START_VALUE = MARKER_RANGE__START_VALUE;

	/**
	 * The feature id for the '<em><b>End Value</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__END_VALUE = MARKER_RANGE__END_VALUE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__LABEL = MARKER_RANGE__LABEL;

	/**
	 * The feature id for the '<em><b>Label Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__LABEL_ANCHOR = MARKER_RANGE__LABEL_ANCHOR;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__FORMAT_SPECIFIER = MARKER_RANGE__FORMAT_SPECIFIER;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__TRIGGERS = MARKER_RANGE__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__CURSOR = MARKER_RANGE__CURSOR;

	/**
	 * The feature id for the '<em><b>Inner Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__INNER_RADIUS = MARKER_RANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Outer Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION__OUTER_RADIUS = MARKER_RANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Dial Region</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_REGION_FEATURE_COUNT = MARKER_RANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.NeedleImpl
	 * <em>Needle</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.NeedleImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getNeedle()
	 * @generated
	 */
	int NEEDLE = 10;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl
	 * <em>Scale</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.ScaleImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getScale()
	 * @generated
	 */
	int SCALE = 11;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl
	 * <em>Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getSeries()
	 * @generated
	 */
	int SERIES = 12;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.component.impl.EStringToDataSetMapEntryImpl
	 * <em>EString To Data Set Map Entry</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.EStringToDataSetMapEntryImpl
	 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getEStringToDataSetMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DATA_SET_MAP_ENTRY = 5;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DATA_SET_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Data Set Map
	 * Entry</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DATA_SET_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRID__LINE_ATTRIBUTES = 0;

	/**
	 * The feature id for the '<em><b>Tick Style</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRID__TICK_STYLE = 1;

	/**
	 * The feature id for the '<em><b>Tick Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRID__TICK_ATTRIBUTES = 2;

	/**
	 * The feature id for the '<em><b>Tick Size</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRID__TICK_SIZE = 3;

	/**
	 * The feature id for the '<em><b>Tick Count</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRID__TICK_COUNT = 4;

	/**
	 * The number of structural features of the '<em>Grid</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRID_FEATURE_COUNT = 5;

	/**
	 * The feature id for the '<em><b>Caption</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__CAPTION = 0;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__BACKGROUND = 1;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__OUTLINE = 2;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__SHADOW_COLOR = 3;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__INSETS = 4;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__VISIBLE = 5;

	/**
	 * The feature id for the '<em><b>Ellipsis</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL__ELLIPSIS = 6;

	/**
	 * The number of structural features of the '<em>Label</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_FEATURE_COUNT = 7;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__LINE_ATTRIBUTES = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__LABEL = 2;

	/**
	 * The feature id for the '<em><b>Label Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__LABEL_ANCHOR = 3;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__FORMAT_SPECIFIER = 4;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__TRIGGERS = 5;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE__CURSOR = 6;

	/**
	 * The number of structural features of the '<em>Marker Line</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_LINE_FEATURE_COUNT = 7;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NEEDLE__LINE_ATTRIBUTES = 0;

	/**
	 * The feature id for the '<em><b>Decorator</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NEEDLE__DECORATOR = 1;

	/**
	 * The number of structural features of the '<em>Needle</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NEEDLE_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Min</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__MIN = 0;

	/**
	 * The feature id for the '<em><b>Max</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__MAX = 1;

	/**
	 * The feature id for the '<em><b>Step</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__STEP = 2;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__UNIT = 3;

	/**
	 * The feature id for the '<em><b>Minor Grids Per Unit</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__MINOR_GRIDS_PER_UNIT = 4;

	/**
	 * The feature id for the '<em><b>Step Number</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__STEP_NUMBER = 5;

	/**
	 * The feature id for the '<em><b>Show Outside</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__SHOW_OUTSIDE = 6;

	/**
	 * The feature id for the '<em><b>Tick Between Categories</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__TICK_BETWEEN_CATEGORIES = 7;

	/**
	 * The feature id for the '<em><b>Auto Expand</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__AUTO_EXPAND = 8;

	/**
	 * The feature id for the '<em><b>Major Grids Step Number</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__MAJOR_GRIDS_STEP_NUMBER = 9;

	/**
	 * The feature id for the '<em><b>Factor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE__FACTOR = 10;

	/**
	 * The number of structural features of the '<em>Scale</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCALE_FEATURE_COUNT = 11;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__VISIBLE = 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__LABEL = 1;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__DATA_DEFINITION = 2;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__SERIES_IDENTIFIER = 3;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__DATA_POINT = 4;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__DATA_SETS = 5;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__LABEL_POSITION = 6;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__STACKED = 7;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__TRIGGERS = 8;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__TRANSLUCENT = 9;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__CURVE_FITTING = 10;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES__CURSOR = 11;

	/**
	 * The number of structural features of the '<em>Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_FEATURE_COUNT = 12;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.component.Axis <em>Axis</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Axis</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis
	 * @generated
	 */
	EClass getAxis();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getType()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_Type();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getTitle
	 * <em>Title</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Title</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getTitle()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_Title();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getSubTitle <em>Sub
	 * Title</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Sub Title</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getSubTitle()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_SubTitle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getTitlePosition
	 * <em>Title Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Title Position</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getTitlePosition()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_TitlePosition();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getAssociatedAxes
	 * <em>Associated Axes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Associated
	 *         Axes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getAssociatedAxes()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_AssociatedAxes();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getAncillaryAxes
	 * <em>Ancillary Axes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Ancillary
	 *         Axes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getAncillaryAxes()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_AncillaryAxes();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getSeriesDefinitions
	 * <em>Series Definitions</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Series
	 *         Definitions</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getSeriesDefinitions()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_SeriesDefinitions();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getGapWidth <em>Gap
	 * Width</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Gap Width</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getGapWidth()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_GapWidth();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getOrientation
	 * <em>Orientation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Orientation</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getOrientation()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_Orientation();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getLineAttributes()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_LineAttributes();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getLabel()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_Label();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getFormatSpecifier()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_FormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabelPosition
	 * <em>Label Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Position</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getLabelPosition()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_LabelPosition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isStaggered
	 * <em>Staggered</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Staggered</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isStaggered()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_Staggered();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getInterval
	 * <em>Interval</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Interval</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getInterval()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_Interval();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getMarkerLines <em>Marker
	 * Lines</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Marker
	 *         Lines</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getMarkerLines()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_MarkerLines();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getMarkerRanges
	 * <em>Marker Ranges</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Marker
	 *         Ranges</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getMarkerRanges()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_MarkerRanges();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getTriggers
	 * <em>Triggers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Triggers</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getTriggers()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_Triggers();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getMajorGrid <em>Major
	 * Grid</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Major Grid</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getMajorGrid()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_MajorGrid();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getMinorGrid <em>Minor
	 * Grid</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Minor Grid</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getMinorGrid()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_MinorGrid();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getScale
	 * <em>Scale</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Scale</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getScale()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_Scale();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getOrigin
	 * <em>Origin</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Origin</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getOrigin()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_Origin();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis <em>Primary
	 * Axis</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Primary Axis</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_PrimaryAxis();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isCategoryAxis
	 * <em>Category Axis</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Category Axis</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isCategoryAxis()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_CategoryAxis();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isPercent
	 * <em>Percent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Percent</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isPercent()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_Percent();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isLabelWithinAxes
	 * <em>Label Within Axes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Within Axes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isLabelWithinAxes()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_LabelWithinAxes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isAligned
	 * <em>Aligned</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Aligned</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isAligned()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_Aligned();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#isSideBySide <em>Side By
	 * Side</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Side By Side</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#isSideBySide()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_SideBySide();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getCursor
	 * <em>Cursor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cursor</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getCursor()
	 * @see #getAxis()
	 * @generated
	 */
	EReference getAxis_Cursor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getLabelSpan <em>Label
	 * Span</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Span</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getLabelSpan()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_LabelSpan();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Axis#getAxisPercent <em>Axis
	 * Percent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Axis Percent</em>'.
	 * @see org.eclipse.birt.chart.model.component.Axis#getAxisPercent()
	 * @see #getAxis()
	 * @generated
	 */
	EAttribute getAxis_AxisPercent();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.ChartPreferences <em>Chart
	 * Preferences</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Chart Preferences</em>'.
	 * @see org.eclipse.birt.chart.model.component.ChartPreferences
	 * @generated
	 */
	EClass getChartPreferences();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.ChartPreferences#getLabels
	 * <em>Labels</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Labels</em>'.
	 * @see org.eclipse.birt.chart.model.component.ChartPreferences#getLabels()
	 * @see #getChartPreferences()
	 * @generated
	 */
	EReference getChartPreferences_Labels();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.ChartPreferences#getBlocks
	 * <em>Blocks</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Blocks</em>'.
	 * @see org.eclipse.birt.chart.model.component.ChartPreferences#getBlocks()
	 * @see #getChartPreferences()
	 * @generated
	 */
	EReference getChartPreferences_Blocks();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting <em>Curve
	 * Fitting</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Curve Fitting</em>'.
	 * @see org.eclipse.birt.chart.model.component.CurveFitting
	 * @generated
	 */
	EClass getCurveFitting();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.CurveFitting#getLineAttributes()
	 * @see #getCurveFitting()
	 * @generated
	 */
	EReference getCurveFitting_LineAttributes();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.CurveFitting#getLabel()
	 * @see #getCurveFitting()
	 * @generated
	 */
	EReference getCurveFitting_Label();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.CurveFitting#getLabelAnchor
	 * <em>Label Anchor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Anchor</em>'.
	 * @see org.eclipse.birt.chart.model.component.CurveFitting#getLabelAnchor()
	 * @see #getCurveFitting()
	 * @generated
	 */
	EAttribute getCurveFitting_LabelAnchor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.Dial <em>Dial</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dial</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial
	 * @generated
	 */
	EClass getDial();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStartAngle <em>Start
	 * Angle</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Start Angle</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getStartAngle()
	 * @see #getDial()
	 * @generated
	 */
	EAttribute getDial_StartAngle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStopAngle <em>Stop
	 * Angle</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Stop Angle</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getStopAngle()
	 * @see #getDial()
	 * @generated
	 */
	EAttribute getDial_StopAngle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getRadius
	 * <em>Radius</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Radius</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getRadius()
	 * @see #getDial()
	 * @generated
	 */
	EAttribute getDial_Radius();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getLineAttributes()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_LineAttributes();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getFill <em>Fill</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Fill</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getFill()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_Fill();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getDialRegions <em>Dial
	 * Regions</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Dial
	 *         Regions</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getDialRegions()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_DialRegions();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getMajorGrid <em>Major
	 * Grid</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Major Grid</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getMajorGrid()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_MajorGrid();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getMinorGrid <em>Minor
	 * Grid</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Minor Grid</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getMinorGrid()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_MinorGrid();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getScale
	 * <em>Scale</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Scale</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getScale()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_Scale();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Dial#isInverseScale
	 * <em>Inverse Scale</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Inverse Scale</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#isInverseScale()
	 * @see #getDial()
	 * @generated
	 */
	EAttribute getDial_InverseScale();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getLabel()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_Label();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.component.Dial#getFormatSpecifier()
	 * @see #getDial()
	 * @generated
	 */
	EReference getDial_FormatSpecifier();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion <em>Dial
	 * Region</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dial Region</em>'.
	 * @see org.eclipse.birt.chart.model.component.DialRegion
	 * @generated
	 */
	EClass getDialRegion();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getInnerRadius
	 * <em>Inner Radius</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Inner Radius</em>'.
	 * @see org.eclipse.birt.chart.model.component.DialRegion#getInnerRadius()
	 * @see #getDialRegion()
	 * @generated
	 */
	EAttribute getDialRegion_InnerRadius();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.DialRegion#getOuterRadius
	 * <em>Outer Radius</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Outer Radius</em>'.
	 * @see org.eclipse.birt.chart.model.component.DialRegion#getOuterRadius()
	 * @see #getDialRegion()
	 * @generated
	 */
	EAttribute getDialRegion_OuterRadius();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.component.Grid <em>Grid</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Grid</em>'.
	 * @see org.eclipse.birt.chart.model.component.Grid
	 * @generated
	 */
	EClass getGrid();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Grid#getLineAttributes()
	 * @see #getGrid()
	 * @generated
	 */
	EReference getGrid_LineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickStyle <em>Tick
	 * Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Tick Style</em>'.
	 * @see org.eclipse.birt.chart.model.component.Grid#getTickStyle()
	 * @see #getGrid()
	 * @generated
	 */
	EAttribute getGrid_TickStyle();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickAttributes
	 * <em>Tick Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Tick
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Grid#getTickAttributes()
	 * @see #getGrid()
	 * @generated
	 */
	EReference getGrid_TickAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickSize <em>Tick
	 * Size</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Tick Size</em>'.
	 * @see org.eclipse.birt.chart.model.component.Grid#getTickSize()
	 * @see #getGrid()
	 * @generated
	 */
	EAttribute getGrid_TickSize();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Grid#getTickCount <em>Tick
	 * Count</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Tick Count</em>'.
	 * @see org.eclipse.birt.chart.model.component.Grid#getTickCount()
	 * @see #getGrid()
	 * @generated
	 */
	EAttribute getGrid_TickCount();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.Label <em>Label</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label
	 * @generated
	 */
	EClass getLabel();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Label#getCaption
	 * <em>Caption</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Caption</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#getCaption()
	 * @see #getLabel()
	 * @generated
	 */
	EReference getLabel_Caption();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Label#getBackground
	 * <em>Background</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Background</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#getBackground()
	 * @see #getLabel()
	 * @generated
	 */
	EReference getLabel_Background();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Label#getOutline
	 * <em>Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#getOutline()
	 * @see #getLabel()
	 * @generated
	 */
	EReference getLabel_Outline();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Label#getShadowColor
	 * <em>Shadow Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Shadow
	 *         Color</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#getShadowColor()
	 * @see #getLabel()
	 * @generated
	 */
	EReference getLabel_ShadowColor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Label#getInsets
	 * <em>Insets</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Insets</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#getInsets()
	 * @see #getLabel()
	 * @generated
	 */
	EReference getLabel_Insets();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Label#isVisible
	 * <em>Visible</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#isVisible()
	 * @see #getLabel()
	 * @generated
	 */
	EAttribute getLabel_Visible();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Label#getEllipsis
	 * <em>Ellipsis</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Ellipsis</em>'.
	 * @see org.eclipse.birt.chart.model.component.Label#getEllipsis()
	 * @see #getLabel()
	 * @generated
	 */
	EAttribute getLabel_Ellipsis();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine <em>Marker
	 * Line</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Marker Line</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine
	 * @generated
	 */
	EClass getMarkerLine();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getLineAttributes()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EReference getMarkerLine_LineAttributes();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getValue()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EReference getMarkerLine_Value();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getLabel()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EReference getMarkerLine_Label();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor
	 * <em>Label Anchor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Anchor</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EAttribute getMarkerLine_LabelAnchor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getFormatSpecifier()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EReference getMarkerLine_FormatSpecifier();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getTriggers
	 * <em>Triggers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Triggers</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getTriggers()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EReference getMarkerLine_Triggers();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerLine#getCursor
	 * <em>Cursor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cursor</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerLine#getCursor()
	 * @see #getMarkerLine()
	 * @generated
	 */
	EReference getMarkerLine_Cursor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange <em>Marker
	 * Range</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Marker Range</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange
	 * @generated
	 */
	EClass getMarkerRange();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getOutline
	 * <em>Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getOutline()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_Outline();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getFill
	 * <em>Fill</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Fill</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getFill()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_Fill();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getStartValue
	 * <em>Start Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Start Value</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getStartValue()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_StartValue();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getEndValue
	 * <em>End Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>End Value</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getEndValue()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_EndValue();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getLabel()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_Label();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getLabelAnchor
	 * <em>Label Anchor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Anchor</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getLabelAnchor()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EAttribute getMarkerRange_LabelAnchor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getFormatSpecifier()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_FormatSpecifier();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getTriggers
	 * <em>Triggers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Triggers</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getTriggers()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_Triggers();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.MarkerRange#getCursor
	 * <em>Cursor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cursor</em>'.
	 * @see org.eclipse.birt.chart.model.component.MarkerRange#getCursor()
	 * @see #getMarkerRange()
	 * @generated
	 */
	EReference getMarkerRange_Cursor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.Needle <em>Needle</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Needle</em>'.
	 * @see org.eclipse.birt.chart.model.component.Needle
	 * @generated
	 */
	EClass getNeedle();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Needle#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.component.Needle#getLineAttributes()
	 * @see #getNeedle()
	 * @generated
	 */
	EReference getNeedle_LineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Needle#getDecorator
	 * <em>Decorator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Decorator</em>'.
	 * @see org.eclipse.birt.chart.model.component.Needle#getDecorator()
	 * @see #getNeedle()
	 * @generated
	 */
	EAttribute getNeedle_Decorator();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.Scale <em>Scale</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Scale</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale
	 * @generated
	 */
	EClass getScale();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getMin <em>Min</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Min</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getMin()
	 * @see #getScale()
	 * @generated
	 */
	EReference getScale_Min();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getMax <em>Max</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Max</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getMax()
	 * @see #getScale()
	 * @generated
	 */
	EReference getScale_Max();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getStep <em>Step</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Step</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getStep()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_Step();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getUnit()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_Unit();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit
	 * <em>Minor Grids Per Unit</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Minor Grids Per Unit</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_MinorGridsPerUnit();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getStepNumber <em>Step
	 * Number</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Step Number</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getStepNumber()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_StepNumber();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#isShowOutside <em>Show
	 * Outside</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show Outside</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#isShowOutside()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_ShowOutside();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#isTickBetweenCategories
	 * <em>Tick Between Categories</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Tick Between Categories</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#isTickBetweenCategories()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_TickBetweenCategories();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#isAutoExpand <em>Auto
	 * Expand</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Auto Expand</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#isAutoExpand()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_AutoExpand();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getMajorGridsStepNumber
	 * <em>Major Grids Step Number</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Major Grids Step Number</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getMajorGridsStepNumber()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_MajorGridsStepNumber();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Scale#getFactor
	 * <em>Factor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Factor</em>'.
	 * @see org.eclipse.birt.chart.model.component.Scale#getFactor()
	 * @see #getScale()
	 * @generated
	 */
	EAttribute getScale_Factor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.component.Series <em>Series</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Series</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series
	 * @generated
	 */
	EClass getSeries();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Series#isVisible
	 * <em>Visible</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#isVisible()
	 * @see #getSeries()
	 * @generated
	 */
	EAttribute getSeries_Visible();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Series#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getLabel()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_Label();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Series#getDataDefinition
	 * <em>Data Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Data
	 *         Definition</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getDataDefinition()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_DataDefinition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Series#getSeriesIdentifier
	 * <em>Series Identifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Series Identifier</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getSeriesIdentifier()
	 * @see #getSeries()
	 * @generated
	 */
	EAttribute getSeries_SeriesIdentifier();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Series#getDataPoint <em>Data
	 * Point</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Data Point</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getDataPoint()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_DataPoint();

	/**
	 * Returns the meta object for the map
	 * '{@link org.eclipse.birt.chart.model.component.Series#getDataSets <em>Data
	 * Sets</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the map '<em>Data Sets</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getDataSets()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_DataSets();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Series#getLabelPosition
	 * <em>Label Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Position</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getLabelPosition()
	 * @see #getSeries()
	 * @generated
	 */
	EAttribute getSeries_LabelPosition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Series#isStacked
	 * <em>Stacked</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Stacked</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#isStacked()
	 * @see #getSeries()
	 * @generated
	 */
	EAttribute getSeries_Stacked();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.component.Series#getTriggers
	 * <em>Triggers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Triggers</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getTriggers()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_Triggers();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.component.Series#isTranslucent
	 * <em>Translucent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Translucent</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#isTranslucent()
	 * @see #getSeries()
	 * @generated
	 */
	EAttribute getSeries_Translucent();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Series#getCurveFitting
	 * <em>Curve Fitting</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Curve
	 *         Fitting</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getCurveFitting()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_CurveFitting();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.component.Series#getCursor
	 * <em>Cursor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cursor</em>'.
	 * @see org.eclipse.birt.chart.model.component.Series#getCursor()
	 * @see #getSeries()
	 * @generated
	 */
	EReference getSeries_Cursor();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To
	 * Data Set Map Entry</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>EString To Data Set Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.xml.type.String" keyRequired="true"
	 *        keyExtendedMetaData="kind='element' name='Key'"
	 *        valueType="org.eclipse.birt.chart.model.data.DataSet"
	 *        valueContainment="true" valueRequired="true"
	 *        valueExtendedMetaData="kind='element' name='Value'"
	 *        extendedMetaData="name='EStringToDataSetMapEntry' kind='elementOnly'"
	 * @generated
	 */
	EClass getEStringToDataSetMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry
	 * <em>Key</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDataSetMapEntry()
	 * @generated
	 */
	EAttribute getEStringToDataSetMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link java.util.Map.Entry <em>Value</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDataSetMapEntry()
	 * @generated
	 */
	EReference getEStringToDataSetMapEntry_Value();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ComponentFactory getComponentFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.AxisImpl <em>Axis</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.AxisImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getAxis()
		 * @generated
		 */
		EClass AXIS = eINSTANCE.getAxis();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__TYPE = eINSTANCE.getAxis_Type();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__TITLE = eINSTANCE.getAxis_Title();

		/**
		 * The meta object literal for the '<em><b>Sub Title</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__SUB_TITLE = eINSTANCE.getAxis_SubTitle();

		/**
		 * The meta object literal for the '<em><b>Title Position</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__TITLE_POSITION = eINSTANCE.getAxis_TitlePosition();

		/**
		 * The meta object literal for the '<em><b>Associated Axes</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__ASSOCIATED_AXES = eINSTANCE.getAxis_AssociatedAxes();

		/**
		 * The meta object literal for the '<em><b>Ancillary Axes</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__ANCILLARY_AXES = eINSTANCE.getAxis_AncillaryAxes();

		/**
		 * The meta object literal for the '<em><b>Series Definitions</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference AXIS__SERIES_DEFINITIONS = eINSTANCE.getAxis_SeriesDefinitions();

		/**
		 * The meta object literal for the '<em><b>Gap Width</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__GAP_WIDTH = eINSTANCE.getAxis_GapWidth();

		/**
		 * The meta object literal for the '<em><b>Orientation</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__ORIENTATION = eINSTANCE.getAxis_Orientation();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__LINE_ATTRIBUTES = eINSTANCE.getAxis_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__LABEL = eINSTANCE.getAxis_Label();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__FORMAT_SPECIFIER = eINSTANCE.getAxis_FormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Label Position</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__LABEL_POSITION = eINSTANCE.getAxis_LabelPosition();

		/**
		 * The meta object literal for the '<em><b>Staggered</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__STAGGERED = eINSTANCE.getAxis_Staggered();

		/**
		 * The meta object literal for the '<em><b>Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__INTERVAL = eINSTANCE.getAxis_Interval();

		/**
		 * The meta object literal for the '<em><b>Marker Lines</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__MARKER_LINES = eINSTANCE.getAxis_MarkerLines();

		/**
		 * The meta object literal for the '<em><b>Marker Ranges</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__MARKER_RANGES = eINSTANCE.getAxis_MarkerRanges();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__TRIGGERS = eINSTANCE.getAxis_Triggers();

		/**
		 * The meta object literal for the '<em><b>Major Grid</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__MAJOR_GRID = eINSTANCE.getAxis_MajorGrid();

		/**
		 * The meta object literal for the '<em><b>Minor Grid</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__MINOR_GRID = eINSTANCE.getAxis_MinorGrid();

		/**
		 * The meta object literal for the '<em><b>Scale</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__SCALE = eINSTANCE.getAxis_Scale();

		/**
		 * The meta object literal for the '<em><b>Origin</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__ORIGIN = eINSTANCE.getAxis_Origin();

		/**
		 * The meta object literal for the '<em><b>Primary Axis</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__PRIMARY_AXIS = eINSTANCE.getAxis_PrimaryAxis();

		/**
		 * The meta object literal for the '<em><b>Category Axis</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__CATEGORY_AXIS = eINSTANCE.getAxis_CategoryAxis();

		/**
		 * The meta object literal for the '<em><b>Percent</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__PERCENT = eINSTANCE.getAxis_Percent();

		/**
		 * The meta object literal for the '<em><b>Label Within Axes</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__LABEL_WITHIN_AXES = eINSTANCE.getAxis_LabelWithinAxes();

		/**
		 * The meta object literal for the '<em><b>Aligned</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__ALIGNED = eINSTANCE.getAxis_Aligned();

		/**
		 * The meta object literal for the '<em><b>Side By Side</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__SIDE_BY_SIDE = eINSTANCE.getAxis_SideBySide();

		/**
		 * The meta object literal for the '<em><b>Cursor</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS__CURSOR = eINSTANCE.getAxis_Cursor();

		/**
		 * The meta object literal for the '<em><b>Label Span</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__LABEL_SPAN = eINSTANCE.getAxis_LabelSpan();

		/**
		 * The meta object literal for the '<em><b>Axis Percent</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS__AXIS_PERCENT = eINSTANCE.getAxis_AxisPercent();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl
		 * <em>Chart Preferences</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getChartPreferences()
		 * @generated
		 */
		EClass CHART_PREFERENCES = eINSTANCE.getChartPreferences();

		/**
		 * The meta object literal for the '<em><b>Labels</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CHART_PREFERENCES__LABELS = eINSTANCE.getChartPreferences_Labels();

		/**
		 * The meta object literal for the '<em><b>Blocks</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CHART_PREFERENCES__BLOCKS = eINSTANCE.getChartPreferences_Blocks();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.CurveFittingImpl
		 * <em>Curve Fitting</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.CurveFittingImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getCurveFitting()
		 * @generated
		 */
		EClass CURVE_FITTING = eINSTANCE.getCurveFitting();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CURVE_FITTING__LINE_ATTRIBUTES = eINSTANCE.getCurveFitting_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CURVE_FITTING__LABEL = eINSTANCE.getCurveFitting_Label();

		/**
		 * The meta object literal for the '<em><b>Label Anchor</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CURVE_FITTING__LABEL_ANCHOR = eINSTANCE.getCurveFitting_LabelAnchor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.DialImpl <em>Dial</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.DialImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getDial()
		 * @generated
		 */
		EClass DIAL = eINSTANCE.getDial();

		/**
		 * The meta object literal for the '<em><b>Start Angle</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAL__START_ANGLE = eINSTANCE.getDial_StartAngle();

		/**
		 * The meta object literal for the '<em><b>Stop Angle</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAL__STOP_ANGLE = eINSTANCE.getDial_StopAngle();

		/**
		 * The meta object literal for the '<em><b>Radius</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAL__RADIUS = eINSTANCE.getDial_Radius();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__LINE_ATTRIBUTES = eINSTANCE.getDial_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Fill</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__FILL = eINSTANCE.getDial_Fill();

		/**
		 * The meta object literal for the '<em><b>Dial Regions</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__DIAL_REGIONS = eINSTANCE.getDial_DialRegions();

		/**
		 * The meta object literal for the '<em><b>Major Grid</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__MAJOR_GRID = eINSTANCE.getDial_MajorGrid();

		/**
		 * The meta object literal for the '<em><b>Minor Grid</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__MINOR_GRID = eINSTANCE.getDial_MinorGrid();

		/**
		 * The meta object literal for the '<em><b>Scale</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__SCALE = eINSTANCE.getDial_Scale();

		/**
		 * The meta object literal for the '<em><b>Inverse Scale</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAL__INVERSE_SCALE = eINSTANCE.getDial_InverseScale();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__LABEL = eINSTANCE.getDial_Label();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL__FORMAT_SPECIFIER = eINSTANCE.getDial_FormatSpecifier();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.DialRegionImpl <em>Dial
		 * Region</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.DialRegionImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getDialRegion()
		 * @generated
		 */
		EClass DIAL_REGION = eINSTANCE.getDialRegion();

		/**
		 * The meta object literal for the '<em><b>Inner Radius</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAL_REGION__INNER_RADIUS = eINSTANCE.getDialRegion_InnerRadius();

		/**
		 * The meta object literal for the '<em><b>Outer Radius</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAL_REGION__OUTER_RADIUS = eINSTANCE.getDialRegion_OuterRadius();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.GridImpl <em>Grid</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.GridImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getGrid()
		 * @generated
		 */
		EClass GRID = eINSTANCE.getGrid();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GRID__LINE_ATTRIBUTES = eINSTANCE.getGrid_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Tick Style</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GRID__TICK_STYLE = eINSTANCE.getGrid_TickStyle();

		/**
		 * The meta object literal for the '<em><b>Tick Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GRID__TICK_ATTRIBUTES = eINSTANCE.getGrid_TickAttributes();

		/**
		 * The meta object literal for the '<em><b>Tick Size</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GRID__TICK_SIZE = eINSTANCE.getGrid_TickSize();

		/**
		 * The meta object literal for the '<em><b>Tick Count</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GRID__TICK_COUNT = eINSTANCE.getGrid_TickCount();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.LabelImpl
		 * <em>Label</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.LabelImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getLabel()
		 * @generated
		 */
		EClass LABEL = eINSTANCE.getLabel();

		/**
		 * The meta object literal for the '<em><b>Caption</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LABEL__CAPTION = eINSTANCE.getLabel_Caption();

		/**
		 * The meta object literal for the '<em><b>Background</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LABEL__BACKGROUND = eINSTANCE.getLabel_Background();

		/**
		 * The meta object literal for the '<em><b>Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LABEL__OUTLINE = eINSTANCE.getLabel_Outline();

		/**
		 * The meta object literal for the '<em><b>Shadow Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LABEL__SHADOW_COLOR = eINSTANCE.getLabel_ShadowColor();

		/**
		 * The meta object literal for the '<em><b>Insets</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LABEL__INSETS = eINSTANCE.getLabel_Insets();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LABEL__VISIBLE = eINSTANCE.getLabel_Visible();

		/**
		 * The meta object literal for the '<em><b>Ellipsis</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LABEL__ELLIPSIS = eINSTANCE.getLabel_Ellipsis();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl <em>Marker
		 * Line</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.MarkerLineImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getMarkerLine()
		 * @generated
		 */
		EClass MARKER_LINE = eINSTANCE.getMarkerLine();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_LINE__LINE_ATTRIBUTES = eINSTANCE.getMarkerLine_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_LINE__VALUE = eINSTANCE.getMarkerLine_Value();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_LINE__LABEL = eINSTANCE.getMarkerLine_Label();

		/**
		 * The meta object literal for the '<em><b>Label Anchor</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MARKER_LINE__LABEL_ANCHOR = eINSTANCE.getMarkerLine_LabelAnchor();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_LINE__FORMAT_SPECIFIER = eINSTANCE.getMarkerLine_FormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_LINE__TRIGGERS = eINSTANCE.getMarkerLine_Triggers();

		/**
		 * The meta object literal for the '<em><b>Cursor</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_LINE__CURSOR = eINSTANCE.getMarkerLine_Cursor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl
		 * <em>Marker Range</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getMarkerRange()
		 * @generated
		 */
		EClass MARKER_RANGE = eINSTANCE.getMarkerRange();

		/**
		 * The meta object literal for the '<em><b>Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__OUTLINE = eINSTANCE.getMarkerRange_Outline();

		/**
		 * The meta object literal for the '<em><b>Fill</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__FILL = eINSTANCE.getMarkerRange_Fill();

		/**
		 * The meta object literal for the '<em><b>Start Value</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__START_VALUE = eINSTANCE.getMarkerRange_StartValue();

		/**
		 * The meta object literal for the '<em><b>End Value</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__END_VALUE = eINSTANCE.getMarkerRange_EndValue();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__LABEL = eINSTANCE.getMarkerRange_Label();

		/**
		 * The meta object literal for the '<em><b>Label Anchor</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MARKER_RANGE__LABEL_ANCHOR = eINSTANCE.getMarkerRange_LabelAnchor();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__FORMAT_SPECIFIER = eINSTANCE.getMarkerRange_FormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__TRIGGERS = eINSTANCE.getMarkerRange_Triggers();

		/**
		 * The meta object literal for the '<em><b>Cursor</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER_RANGE__CURSOR = eINSTANCE.getMarkerRange_Cursor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.NeedleImpl
		 * <em>Needle</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.NeedleImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getNeedle()
		 * @generated
		 */
		EClass NEEDLE = eINSTANCE.getNeedle();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NEEDLE__LINE_ATTRIBUTES = eINSTANCE.getNeedle_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Decorator</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NEEDLE__DECORATOR = eINSTANCE.getNeedle_Decorator();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl
		 * <em>Scale</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.ScaleImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getScale()
		 * @generated
		 */
		EClass SCALE = eINSTANCE.getScale();

		/**
		 * The meta object literal for the '<em><b>Min</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SCALE__MIN = eINSTANCE.getScale_Min();

		/**
		 * The meta object literal for the '<em><b>Max</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SCALE__MAX = eINSTANCE.getScale_Max();

		/**
		 * The meta object literal for the '<em><b>Step</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__STEP = eINSTANCE.getScale_Step();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__UNIT = eINSTANCE.getScale_Unit();

		/**
		 * The meta object literal for the '<em><b>Minor Grids Per Unit</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__MINOR_GRIDS_PER_UNIT = eINSTANCE.getScale_MinorGridsPerUnit();

		/**
		 * The meta object literal for the '<em><b>Step Number</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__STEP_NUMBER = eINSTANCE.getScale_StepNumber();

		/**
		 * The meta object literal for the '<em><b>Show Outside</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__SHOW_OUTSIDE = eINSTANCE.getScale_ShowOutside();

		/**
		 * The meta object literal for the '<em><b>Tick Between Categories</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__TICK_BETWEEN_CATEGORIES = eINSTANCE.getScale_TickBetweenCategories();

		/**
		 * The meta object literal for the '<em><b>Auto Expand</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__AUTO_EXPAND = eINSTANCE.getScale_AutoExpand();

		/**
		 * The meta object literal for the '<em><b>Major Grids Step Number</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__MAJOR_GRIDS_STEP_NUMBER = eINSTANCE.getScale_MajorGridsStepNumber();

		/**
		 * The meta object literal for the '<em><b>Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCALE__FACTOR = eINSTANCE.getScale_Factor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl
		 * <em>Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getSeries()
		 * @generated
		 */
		EClass SERIES = eINSTANCE.getSeries();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SERIES__VISIBLE = eINSTANCE.getSeries_Visible();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__LABEL = eINSTANCE.getSeries_Label();

		/**
		 * The meta object literal for the '<em><b>Data Definition</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__DATA_DEFINITION = eINSTANCE.getSeries_DataDefinition();

		/**
		 * The meta object literal for the '<em><b>Series Identifier</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SERIES__SERIES_IDENTIFIER = eINSTANCE.getSeries_SeriesIdentifier();

		/**
		 * The meta object literal for the '<em><b>Data Point</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__DATA_POINT = eINSTANCE.getSeries_DataPoint();

		/**
		 * The meta object literal for the '<em><b>Data Sets</b></em>' map feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__DATA_SETS = eINSTANCE.getSeries_DataSets();

		/**
		 * The meta object literal for the '<em><b>Label Position</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SERIES__LABEL_POSITION = eINSTANCE.getSeries_LabelPosition();

		/**
		 * The meta object literal for the '<em><b>Stacked</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SERIES__STACKED = eINSTANCE.getSeries_Stacked();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__TRIGGERS = eINSTANCE.getSeries_Triggers();

		/**
		 * The meta object literal for the '<em><b>Translucent</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SERIES__TRANSLUCENT = eINSTANCE.getSeries_Translucent();

		/**
		 * The meta object literal for the '<em><b>Curve Fitting</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__CURVE_FITTING = eINSTANCE.getSeries_CurveFitting();

		/**
		 * The meta object literal for the '<em><b>Cursor</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SERIES__CURSOR = eINSTANCE.getSeries_Cursor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.component.impl.EStringToDataSetMapEntryImpl
		 * <em>EString To Data Set Map Entry</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.component.impl.EStringToDataSetMapEntryImpl
		 * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getEStringToDataSetMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DATA_SET_MAP_ENTRY = eINSTANCE.getEStringToDataSetMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ESTRING_TO_DATA_SET_MAP_ENTRY__KEY = eINSTANCE.getEStringToDataSetMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDataSetMapEntry_Value();

	}

} // ComponentPackage
