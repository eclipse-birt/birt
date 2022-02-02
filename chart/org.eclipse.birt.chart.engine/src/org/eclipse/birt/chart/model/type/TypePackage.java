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

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.component.ComponentPackage;
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
 * @see org.eclipse.birt.chart.model.type.TypeFactory
 * @generated
 */
public interface TypePackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "type"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelType"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "type"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	TypePackage eINSTANCE = org.eclipse.birt.chart.model.type.impl.TypePackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.BarSeriesImpl <em>Bar
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.BarSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getBarSeries()
	 * @generated
	 */
	int BAR_SERIES = 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl <em>Line
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.LineSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getLineSeries()
	 * @generated
	 */
	int LINE_SERIES = 6;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Markers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__MARKERS = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__MARKER = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__PALETTE_LINE_COLOR = ComponentPackage.SERIES_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__CURVE = ComponentPackage.SERIES_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__SHADOW_COLOR = ComponentPackage.SERIES_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Connect Missing Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES__CONNECT_MISSING_VALUE = ComponentPackage.SERIES_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Line Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl <em>Area
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getAreaSeries()
	 * @generated
	 */
	int AREA_SERIES = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__VISIBLE = LINE_SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__LABEL = LINE_SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__DATA_DEFINITION = LINE_SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__SERIES_IDENTIFIER = LINE_SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__DATA_POINT = LINE_SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__DATA_SETS = LINE_SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__LABEL_POSITION = LINE_SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__STACKED = LINE_SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__TRIGGERS = LINE_SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__TRANSLUCENT = LINE_SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__CURVE_FITTING = LINE_SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__CURSOR = LINE_SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Markers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__MARKERS = LINE_SERIES__MARKERS;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__MARKER = LINE_SERIES__MARKER;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__LINE_ATTRIBUTES = LINE_SERIES__LINE_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__PALETTE_LINE_COLOR = LINE_SERIES__PALETTE_LINE_COLOR;

	/**
	 * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__CURVE = LINE_SERIES__CURVE;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__SHADOW_COLOR = LINE_SERIES__SHADOW_COLOR;

	/**
	 * The feature id for the '<em><b>Connect Missing Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES__CONNECT_MISSING_VALUE = LINE_SERIES__CONNECT_MISSING_VALUE;

	/**
	 * The number of structural features of the '<em>Area Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AREA_SERIES_FEATURE_COUNT = LINE_SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Riser</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__RISER = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Riser Outline</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES__RISER_OUTLINE = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Bar Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BAR_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl <em>Dial
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.DialSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getDialSeries()
	 * @generated
	 */
	int DIAL_SERIES = 3;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl
	 * <em>Difference Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getDifferenceSeries()
	 * @generated
	 */
	int DIFFERENCE_SERIES = 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl <em>Gantt
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getGanttSeries()
	 * @generated
	 */
	int GANTT_SERIES = 5;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl <em>Pie
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.PieSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getPieSeries()
	 * @generated
	 */
	int PIE_SERIES = 7;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl <em>Scatter
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getScatterSeries()
	 * @generated
	 */
	int SCATTER_SERIES = 8;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__VISIBLE = LINE_SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__LABEL = LINE_SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__DATA_DEFINITION = LINE_SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__SERIES_IDENTIFIER = LINE_SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__DATA_POINT = LINE_SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__DATA_SETS = LINE_SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__LABEL_POSITION = LINE_SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__STACKED = LINE_SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__TRIGGERS = LINE_SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__TRANSLUCENT = LINE_SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__CURVE_FITTING = LINE_SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__CURSOR = LINE_SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Markers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__MARKERS = LINE_SERIES__MARKERS;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__MARKER = LINE_SERIES__MARKER;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__LINE_ATTRIBUTES = LINE_SERIES__LINE_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__PALETTE_LINE_COLOR = LINE_SERIES__PALETTE_LINE_COLOR;

	/**
	 * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__CURVE = LINE_SERIES__CURVE;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__SHADOW_COLOR = LINE_SERIES__SHADOW_COLOR;

	/**
	 * The feature id for the '<em><b>Connect Missing Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES__CONNECT_MISSING_VALUE = LINE_SERIES__CONNECT_MISSING_VALUE;

	/**
	 * The number of structural features of the '<em>Scatter Series</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCATTER_SERIES_FEATURE_COUNT = LINE_SERIES_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl <em>Bubble
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getBubbleSeries()
	 * @generated
	 */
	int BUBBLE_SERIES = 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__VISIBLE = SCATTER_SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__LABEL = SCATTER_SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__DATA_DEFINITION = SCATTER_SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__SERIES_IDENTIFIER = SCATTER_SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__DATA_POINT = SCATTER_SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__DATA_SETS = SCATTER_SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__LABEL_POSITION = SCATTER_SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__STACKED = SCATTER_SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__TRIGGERS = SCATTER_SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__TRANSLUCENT = SCATTER_SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__CURVE_FITTING = SCATTER_SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__CURSOR = SCATTER_SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Markers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__MARKERS = SCATTER_SERIES__MARKERS;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__MARKER = SCATTER_SERIES__MARKER;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__LINE_ATTRIBUTES = SCATTER_SERIES__LINE_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__PALETTE_LINE_COLOR = SCATTER_SERIES__PALETTE_LINE_COLOR;

	/**
	 * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__CURVE = SCATTER_SERIES__CURVE;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__SHADOW_COLOR = SCATTER_SERIES__SHADOW_COLOR;

	/**
	 * The feature id for the '<em><b>Connect Missing Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__CONNECT_MISSING_VALUE = SCATTER_SERIES__CONNECT_MISSING_VALUE;

	/**
	 * The feature id for the '<em><b>Acc Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__ACC_LINE_ATTRIBUTES = SCATTER_SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Acc Orientation</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES__ACC_ORIENTATION = SCATTER_SERIES_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Bubble Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BUBBLE_SERIES_FEATURE_COUNT = SCATTER_SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Dial</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__DIAL = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Needle</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES__NEEDLE = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Dial Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAL_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__VISIBLE = AREA_SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__LABEL = AREA_SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__DATA_DEFINITION = AREA_SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__SERIES_IDENTIFIER = AREA_SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__DATA_POINT = AREA_SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__DATA_SETS = AREA_SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__LABEL_POSITION = AREA_SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__STACKED = AREA_SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__TRIGGERS = AREA_SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__TRANSLUCENT = AREA_SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__CURVE_FITTING = AREA_SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__CURSOR = AREA_SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Markers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__MARKERS = AREA_SERIES__MARKERS;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__MARKER = AREA_SERIES__MARKER;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__LINE_ATTRIBUTES = AREA_SERIES__LINE_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__PALETTE_LINE_COLOR = AREA_SERIES__PALETTE_LINE_COLOR;

	/**
	 * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__CURVE = AREA_SERIES__CURVE;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__SHADOW_COLOR = AREA_SERIES__SHADOW_COLOR;

	/**
	 * The feature id for the '<em><b>Connect Missing Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__CONNECT_MISSING_VALUE = AREA_SERIES__CONNECT_MISSING_VALUE;

	/**
	 * The feature id for the '<em><b>Negative Markers</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__NEGATIVE_MARKERS = AREA_SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Negative Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES = AREA_SERIES_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Difference Series</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_SERIES_FEATURE_COUNT = AREA_SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Start Marker</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__START_MARKER = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Start Marker Position</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__START_MARKER_POSITION = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>End Marker</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__END_MARKER = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>End Marker Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__END_MARKER_POSITION = ComponentPackage.SERIES_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Connection Line</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__CONNECTION_LINE = ComponentPackage.SERIES_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__OUTLINE = ComponentPackage.SERIES_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Outline Fill</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__OUTLINE_FILL = ComponentPackage.SERIES_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Use Decoration Label Value</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__USE_DECORATION_LABEL_VALUE = ComponentPackage.SERIES_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Decoration Label</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__DECORATION_LABEL = ComponentPackage.SERIES_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Decoration Label Position</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__DECORATION_LABEL_POSITION = ComponentPackage.SERIES_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES__PALETTE_LINE_COLOR = ComponentPackage.SERIES_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Gantt Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GANTT_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Explosion</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__EXPLOSION = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Explosion Expression</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__EXPLOSION_EXPRESSION = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__TITLE = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Title Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__TITLE_POSITION = ComponentPackage.SERIES_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Leader Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__LEADER_LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Leader Line Style</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__LEADER_LINE_STYLE = ComponentPackage.SERIES_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Leader Line Length</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__LEADER_LINE_LENGTH = ComponentPackage.SERIES_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Slice Outline</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__SLICE_OUTLINE = ComponentPackage.SERIES_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Ratio</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__RATIO = ComponentPackage.SERIES_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Rotation</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__ROTATION = ComponentPackage.SERIES_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Clockwise</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__CLOCKWISE = ComponentPackage.SERIES_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Inner Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__INNER_RADIUS = ComponentPackage.SERIES_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Inner Radius Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES__INNER_RADIUS_PERCENT = ComponentPackage.SERIES_FEATURE_COUNT + 12;

	/**
	 * The number of structural features of the '<em>Pie Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PIE_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 13;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl <em>Stock
	 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.StockSeriesImpl
	 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getStockSeries()
	 * @generated
	 */
	int STOCK_SERIES = 9;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__FILL = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Show As Bar Stick</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__SHOW_AS_BAR_STICK = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Stick Length</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES__STICK_LENGTH = ComponentPackage.SERIES_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Stock Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 4;

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.AreaSeries <em>Area Series</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Area Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.AreaSeries
	 * @generated
	 */
	EClass getAreaSeries();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries <em>Bar Series</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Bar Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.BarSeries
	 * @generated
	 */
	EClass getBarSeries();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries#getRiser
	 * <em>Riser</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Riser</em>'.
	 * @see org.eclipse.birt.chart.model.type.BarSeries#getRiser()
	 * @see #getBarSeries()
	 * @generated
	 */
	EAttribute getBarSeries_Riser();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries#getRiserOutline <em>Riser
	 * Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Riser
	 *         Outline</em>'.
	 * @see org.eclipse.birt.chart.model.type.BarSeries#getRiserOutline()
	 * @see #getBarSeries()
	 * @generated
	 */
	EReference getBarSeries_RiserOutline();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries <em>Bubble
	 * Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Bubble Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.BubbleSeries
	 * @generated
	 */
	EClass getBubbleSeries();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccLineAttributes
	 * <em>Acc Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Acc Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.type.BubbleSeries#getAccLineAttributes()
	 * @see #getBubbleSeries()
	 * @generated
	 */
	EReference getBubbleSeries_AccLineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccOrientation
	 * <em>Acc Orientation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Acc Orientation</em>'.
	 * @see org.eclipse.birt.chart.model.type.BubbleSeries#getAccOrientation()
	 * @see #getBubbleSeries()
	 * @generated
	 */
	EAttribute getBubbleSeries_AccOrientation();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.DialSeries <em>Dial Series</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dial Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.DialSeries
	 * @generated
	 */
	EClass getDialSeries();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.DialSeries#getDial <em>Dial</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Dial</em>'.
	 * @see org.eclipse.birt.chart.model.type.DialSeries#getDial()
	 * @see #getDialSeries()
	 * @generated
	 */
	EReference getDialSeries_Dial();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.DialSeries#getNeedle
	 * <em>Needle</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Needle</em>'.
	 * @see org.eclipse.birt.chart.model.type.DialSeries#getNeedle()
	 * @see #getDialSeries()
	 * @generated
	 */
	EReference getDialSeries_Needle();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.DifferenceSeries <em>Difference
	 * Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Difference Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.DifferenceSeries
	 * @generated
	 */
	EClass getDifferenceSeries();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeMarkers
	 * <em>Negative Markers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Negative
	 *         Markers</em>'.
	 * @see org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeMarkers()
	 * @see #getDifferenceSeries()
	 * @generated
	 */
	EReference getDifferenceSeries_NegativeMarkers();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeLineAttributes
	 * <em>Negative Line Attributes</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Negative Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeLineAttributes()
	 * @see #getDifferenceSeries()
	 * @generated
	 */
	EReference getDifferenceSeries_NegativeLineAttributes();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries <em>Gantt
	 * Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Gantt Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries
	 * @generated
	 */
	EClass getGanttSeries();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarker
	 * <em>Start Marker</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Start
	 *         Marker</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getStartMarker()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EReference getGanttSeries_StartMarker();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarkerPosition
	 * <em>Start Marker Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Start Marker Position</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getStartMarkerPosition()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EAttribute getGanttSeries_StartMarkerPosition();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarker <em>End
	 * Marker</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>End Marker</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getEndMarker()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EReference getGanttSeries_EndMarker();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarkerPosition
	 * <em>End Marker Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>End Marker Position</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getEndMarkerPosition()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EAttribute getGanttSeries_EndMarkerPosition();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getConnectionLine
	 * <em>Connection Line</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Connection
	 *         Line</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getConnectionLine()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EReference getGanttSeries_ConnectionLine();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getOutline
	 * <em>Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getOutline()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EReference getGanttSeries_Outline();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getOutlineFill
	 * <em>Outline Fill</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline
	 *         Fill</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getOutlineFill()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EReference getGanttSeries_OutlineFill();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isUseDecorationLabelValue
	 * <em>Use Decoration Label Value</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Use Decoration Label
	 *         Value</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#isUseDecorationLabelValue()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EAttribute getGanttSeries_UseDecorationLabelValue();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabel
	 * <em>Decoration Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Decoration
	 *         Label</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabel()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EReference getGanttSeries_DecorationLabel();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabelPosition
	 * <em>Decoration Label Position</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Decoration Label
	 *         Position</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabelPosition()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EAttribute getGanttSeries_DecorationLabelPosition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Palette Line Color</em>'.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries#isPaletteLineColor()
	 * @see #getGanttSeries()
	 * @generated
	 */
	EAttribute getGanttSeries_PaletteLineColor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries <em>Line Series</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Line Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries
	 * @generated
	 */
	EClass getLineSeries();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getMarkers
	 * <em>Markers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Markers</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#getMarkers()
	 * @see #getLineSeries()
	 * @generated
	 */
	EReference getLineSeries_Markers();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getMarker
	 * <em>Marker</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Marker</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#getMarker()
	 * @see #getLineSeries()
	 * @generated
	 */
	EReference getLineSeries_Marker();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#getLineAttributes()
	 * @see #getLineSeries()
	 * @generated
	 */
	EReference getLineSeries_LineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Palette Line Color</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#isPaletteLineColor()
	 * @see #getLineSeries()
	 * @generated
	 */
	EAttribute getLineSeries_PaletteLineColor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isCurve
	 * <em>Curve</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Curve</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#isCurve()
	 * @see #getLineSeries()
	 * @generated
	 */
	EAttribute getLineSeries_Curve();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getShadowColor
	 * <em>Shadow Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Shadow
	 *         Color</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#getShadowColor()
	 * @see #getLineSeries()
	 * @generated
	 */
	EReference getLineSeries_ShadowColor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isConnectMissingValue
	 * <em>Connect Missing Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Connect Missing Value</em>'.
	 * @see org.eclipse.birt.chart.model.type.LineSeries#isConnectMissingValue()
	 * @see #getLineSeries()
	 * @generated
	 */
	EAttribute getLineSeries_ConnectMissingValue();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries <em>Pie Series</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Pie Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries
	 * @generated
	 */
	EClass getPieSeries();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosion
	 * <em>Explosion</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Explosion</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getExplosion()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_Explosion();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosionExpression
	 * <em>Explosion Expression</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Explosion Expression</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getExplosionExpression()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_ExplosionExpression();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getTitle
	 * <em>Title</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Title</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getTitle()
	 * @see #getPieSeries()
	 * @generated
	 */
	EReference getPieSeries_Title();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition
	 * <em>Title Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Title Position</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_TitlePosition();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineAttributes
	 * <em>Leader Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference '<em>Leader Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineAttributes()
	 * @see #getPieSeries()
	 * @generated
	 */
	EReference getPieSeries_LeaderLineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle
	 * <em>Leader Line Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Leader Line Style</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_LeaderLineStyle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength
	 * <em>Leader Line Length</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Leader Line Length</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_LeaderLineLength();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getSliceOutline <em>Slice
	 * Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Slice
	 *         Outline</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getSliceOutline()
	 * @see #getPieSeries()
	 * @generated
	 */
	EReference getPieSeries_SliceOutline();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getRatio
	 * <em>Ratio</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Ratio</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getRatio()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_Ratio();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getRotation
	 * <em>Rotation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Rotation</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getRotation()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_Rotation();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#isClockwise
	 * <em>Clockwise</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Clockwise</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#isClockwise()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_Clockwise();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#getInnerRadius <em>Inner
	 * Radius</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Inner Radius</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#getInnerRadius()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_InnerRadius();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries#isInnerRadiusPercent
	 * <em>Inner Radius Percent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Inner Radius Percent</em>'.
	 * @see org.eclipse.birt.chart.model.type.PieSeries#isInnerRadiusPercent()
	 * @see #getPieSeries()
	 * @generated
	 */
	EAttribute getPieSeries_InnerRadiusPercent();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.ScatterSeries <em>Scatter
	 * Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Scatter Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.ScatterSeries
	 * @generated
	 */
	EClass getScatterSeries();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries <em>Stock
	 * Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Stock Series</em>'.
	 * @see org.eclipse.birt.chart.model.type.StockSeries
	 * @generated
	 */
	EClass getStockSeries();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getFill
	 * <em>Fill</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Fill</em>'.
	 * @see org.eclipse.birt.chart.model.type.StockSeries#getFill()
	 * @see #getStockSeries()
	 * @generated
	 */
	EReference getStockSeries_Fill();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes()
	 * @see #getStockSeries()
	 * @generated
	 */
	EReference getStockSeries_LineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#isShowAsBarStick
	 * <em>Show As Bar Stick</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show As Bar Stick</em>'.
	 * @see org.eclipse.birt.chart.model.type.StockSeries#isShowAsBarStick()
	 * @see #getStockSeries()
	 * @generated
	 */
	EAttribute getStockSeries_ShowAsBarStick();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries#getStickLength
	 * <em>Stick Length</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Stick Length</em>'.
	 * @see org.eclipse.birt.chart.model.type.StockSeries#getStickLength()
	 * @see #getStockSeries()
	 * @generated
	 */
	EAttribute getStockSeries_StickLength();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TypeFactory getTypeFactory();

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
		 * '{@link org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl <em>Area
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getAreaSeries()
		 * @generated
		 */
		EClass AREA_SERIES = eINSTANCE.getAreaSeries();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.BarSeriesImpl <em>Bar
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.BarSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getBarSeries()
		 * @generated
		 */
		EClass BAR_SERIES = eINSTANCE.getBarSeries();

		/**
		 * The meta object literal for the '<em><b>Riser</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BAR_SERIES__RISER = eINSTANCE.getBarSeries_Riser();

		/**
		 * The meta object literal for the '<em><b>Riser Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BAR_SERIES__RISER_OUTLINE = eINSTANCE.getBarSeries_RiserOutline();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl <em>Bubble
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getBubbleSeries()
		 * @generated
		 */
		EClass BUBBLE_SERIES = eINSTANCE.getBubbleSeries();

		/**
		 * The meta object literal for the '<em><b>Acc Line Attributes</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BUBBLE_SERIES__ACC_LINE_ATTRIBUTES = eINSTANCE.getBubbleSeries_AccLineAttributes();

		/**
		 * The meta object literal for the '<em><b>Acc Orientation</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BUBBLE_SERIES__ACC_ORIENTATION = eINSTANCE.getBubbleSeries_AccOrientation();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl <em>Dial
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.DialSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getDialSeries()
		 * @generated
		 */
		EClass DIAL_SERIES = eINSTANCE.getDialSeries();

		/**
		 * The meta object literal for the '<em><b>Dial</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL_SERIES__DIAL = eINSTANCE.getDialSeries_Dial();

		/**
		 * The meta object literal for the '<em><b>Needle</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIAL_SERIES__NEEDLE = eINSTANCE.getDialSeries_Needle();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl
		 * <em>Difference Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getDifferenceSeries()
		 * @generated
		 */
		EClass DIFFERENCE_SERIES = eINSTANCE.getDifferenceSeries();

		/**
		 * The meta object literal for the '<em><b>Negative Markers</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference DIFFERENCE_SERIES__NEGATIVE_MARKERS = eINSTANCE.getDifferenceSeries_NegativeMarkers();

		/**
		 * The meta object literal for the '<em><b>Negative Line Attributes</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES = eINSTANCE.getDifferenceSeries_NegativeLineAttributes();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl <em>Gantt
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getGanttSeries()
		 * @generated
		 */
		EClass GANTT_SERIES = eINSTANCE.getGanttSeries();

		/**
		 * The meta object literal for the '<em><b>Start Marker</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GANTT_SERIES__START_MARKER = eINSTANCE.getGanttSeries_StartMarker();

		/**
		 * The meta object literal for the '<em><b>Start Marker Position</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GANTT_SERIES__START_MARKER_POSITION = eINSTANCE.getGanttSeries_StartMarkerPosition();

		/**
		 * The meta object literal for the '<em><b>End Marker</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GANTT_SERIES__END_MARKER = eINSTANCE.getGanttSeries_EndMarker();

		/**
		 * The meta object literal for the '<em><b>End Marker Position</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GANTT_SERIES__END_MARKER_POSITION = eINSTANCE.getGanttSeries_EndMarkerPosition();

		/**
		 * The meta object literal for the '<em><b>Connection Line</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GANTT_SERIES__CONNECTION_LINE = eINSTANCE.getGanttSeries_ConnectionLine();

		/**
		 * The meta object literal for the '<em><b>Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GANTT_SERIES__OUTLINE = eINSTANCE.getGanttSeries_Outline();

		/**
		 * The meta object literal for the '<em><b>Outline Fill</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GANTT_SERIES__OUTLINE_FILL = eINSTANCE.getGanttSeries_OutlineFill();

		/**
		 * The meta object literal for the '<em><b>Use Decoration Label Value</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GANTT_SERIES__USE_DECORATION_LABEL_VALUE = eINSTANCE.getGanttSeries_UseDecorationLabelValue();

		/**
		 * The meta object literal for the '<em><b>Decoration Label</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GANTT_SERIES__DECORATION_LABEL = eINSTANCE.getGanttSeries_DecorationLabel();

		/**
		 * The meta object literal for the '<em><b>Decoration Label Position</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GANTT_SERIES__DECORATION_LABEL_POSITION = eINSTANCE.getGanttSeries_DecorationLabelPosition();

		/**
		 * The meta object literal for the '<em><b>Palette Line Color</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GANTT_SERIES__PALETTE_LINE_COLOR = eINSTANCE.getGanttSeries_PaletteLineColor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl <em>Line
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.LineSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getLineSeries()
		 * @generated
		 */
		EClass LINE_SERIES = eINSTANCE.getLineSeries();

		/**
		 * The meta object literal for the '<em><b>Markers</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LINE_SERIES__MARKERS = eINSTANCE.getLineSeries_Markers();

		/**
		 * The meta object literal for the '<em><b>Marker</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LINE_SERIES__MARKER = eINSTANCE.getLineSeries_Marker();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LINE_SERIES__LINE_ATTRIBUTES = eINSTANCE.getLineSeries_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Palette Line Color</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LINE_SERIES__PALETTE_LINE_COLOR = eINSTANCE.getLineSeries_PaletteLineColor();

		/**
		 * The meta object literal for the '<em><b>Curve</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LINE_SERIES__CURVE = eINSTANCE.getLineSeries_Curve();

		/**
		 * The meta object literal for the '<em><b>Shadow Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LINE_SERIES__SHADOW_COLOR = eINSTANCE.getLineSeries_ShadowColor();

		/**
		 * The meta object literal for the '<em><b>Connect Missing Value</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LINE_SERIES__CONNECT_MISSING_VALUE = eINSTANCE.getLineSeries_ConnectMissingValue();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl <em>Pie
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.PieSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getPieSeries()
		 * @generated
		 */
		EClass PIE_SERIES = eINSTANCE.getPieSeries();

		/**
		 * The meta object literal for the '<em><b>Explosion</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__EXPLOSION = eINSTANCE.getPieSeries_Explosion();

		/**
		 * The meta object literal for the '<em><b>Explosion Expression</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__EXPLOSION_EXPRESSION = eINSTANCE.getPieSeries_ExplosionExpression();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PIE_SERIES__TITLE = eINSTANCE.getPieSeries_Title();

		/**
		 * The meta object literal for the '<em><b>Title Position</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__TITLE_POSITION = eINSTANCE.getPieSeries_TitlePosition();

		/**
		 * The meta object literal for the '<em><b>Leader Line Attributes</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PIE_SERIES__LEADER_LINE_ATTRIBUTES = eINSTANCE.getPieSeries_LeaderLineAttributes();

		/**
		 * The meta object literal for the '<em><b>Leader Line Style</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__LEADER_LINE_STYLE = eINSTANCE.getPieSeries_LeaderLineStyle();

		/**
		 * The meta object literal for the '<em><b>Leader Line Length</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__LEADER_LINE_LENGTH = eINSTANCE.getPieSeries_LeaderLineLength();

		/**
		 * The meta object literal for the '<em><b>Slice Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PIE_SERIES__SLICE_OUTLINE = eINSTANCE.getPieSeries_SliceOutline();

		/**
		 * The meta object literal for the '<em><b>Ratio</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__RATIO = eINSTANCE.getPieSeries_Ratio();

		/**
		 * The meta object literal for the '<em><b>Rotation</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__ROTATION = eINSTANCE.getPieSeries_Rotation();

		/**
		 * The meta object literal for the '<em><b>Clockwise</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__CLOCKWISE = eINSTANCE.getPieSeries_Clockwise();

		/**
		 * The meta object literal for the '<em><b>Inner Radius</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__INNER_RADIUS = eINSTANCE.getPieSeries_InnerRadius();

		/**
		 * The meta object literal for the '<em><b>Inner Radius Percent</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PIE_SERIES__INNER_RADIUS_PERCENT = eINSTANCE.getPieSeries_InnerRadiusPercent();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl <em>Scatter
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getScatterSeries()
		 * @generated
		 */
		EClass SCATTER_SERIES = eINSTANCE.getScatterSeries();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl <em>Stock
		 * Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.type.impl.StockSeriesImpl
		 * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getStockSeries()
		 * @generated
		 */
		EClass STOCK_SERIES = eINSTANCE.getStockSeries();

		/**
		 * The meta object literal for the '<em><b>Fill</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STOCK_SERIES__FILL = eINSTANCE.getStockSeries_Fill();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STOCK_SERIES__LINE_ATTRIBUTES = eINSTANCE.getStockSeries_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Show As Bar Stick</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute STOCK_SERIES__SHOW_AS_BAR_STICK = eINSTANCE.getStockSeries_ShowAsBarStick();

		/**
		 * The meta object literal for the '<em><b>Stick Length</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute STOCK_SERIES__STICK_LENGTH = eINSTANCE.getStockSeries_StickLength();

	}

} // TypePackage
