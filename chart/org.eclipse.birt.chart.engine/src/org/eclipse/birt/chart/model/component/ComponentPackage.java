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
 * <!-- begin-user-doc --> The <b>Package </b> for the model. It contains accessors for the meta objects to represent
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
public interface ComponentPackage extends EPackage
{

    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNAME = "component";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_URI = "http://www.birt.eclipse.org/ChartModelComponent";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_PREFIX = "component";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    ComponentPackage eINSTANCE = org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.component.impl.AxisImpl <em>Axis</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.AxisImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getAxis()
     * @generated
     */
    int AXIS = 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__TYPE = 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__TITLE = 1;

    /**
     * The feature id for the '<em><b>Subtitle</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__SUBTITLE = 2;

    /**
     * The feature id for the '<em><b>Title Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__TITLE_POSITION = 3;

    /**
     * The feature id for the '<em><b>Associated Axes</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__ASSOCIATED_AXES = 4;

    /**
     * The feature id for the '<em><b>Series Definitions</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__SERIES_DEFINITIONS = 5;

    /**
     * The feature id for the '<em><b>Gap Width</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__GAP_WIDTH = 6;

    /**
     * The feature id for the '<em><b>Orientation</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__ORIENTATION = 7;

    /**
     * The feature id for the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__LINE_ATTRIBUTES = 8;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__LABEL = 9;

    /**
     * The feature id for the '<em><b>Format Specifier</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__FORMAT_SPECIFIER = 10;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__LABEL_POSITION = 11;

    /**
     * The feature id for the '<em><b>Staggered</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__STAGGERED = 12;

    /**
     * The feature id for the '<em><b>Marker Lines</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__MARKER_LINES = 13;

    /**
     * The feature id for the '<em><b>Marker Ranges</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__MARKER_RANGES = 14;

    /**
     * The feature id for the '<em><b>Major Grid</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__MAJOR_GRID = 15;

    /**
     * The feature id for the '<em><b>Minor Grid</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__MINOR_GRID = 16;

    /**
     * The feature id for the '<em><b>Scale</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__SCALE = 17;

    /**
     * The feature id for the '<em><b>Origin</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__ORIGIN = 18;

    /**
     * The feature id for the '<em><b>Primary Axis</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__PRIMARY_AXIS = 19;

    /**
     * The feature id for the '<em><b>Category Axis</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__CATEGORY_AXIS = 20;

    /**
     * The feature id for the '<em><b>Percent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AXIS__PERCENT = 21;

    /**
     * The number of structural features of the the '<em>Axis</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int AXIS_FEATURE_COUNT = 22;

    /**
     * The meta object id for the '
     * {@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl <em>Chart Preferences</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getChartPreferences()
     * @generated
     */
    int CHART_PREFERENCES = 1;

    /**
     * The feature id for the '<em><b>Labels</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHART_PREFERENCES__LABELS = 0;

    /**
     * The feature id for the '<em><b>Blocks</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHART_PREFERENCES__BLOCKS = 1;

    /**
     * The number of structural features of the the '<em>Chart Preferences</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHART_PREFERENCES_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.component.impl.LabelImpl <em>Label</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.LabelImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getLabel()
     * @generated
     */
    int LABEL = 3;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.component.impl.GridImpl <em>Grid</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.GridImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getGrid()
     * @generated
     */
    int GRID = 2;

    /**
     * The feature id for the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int GRID__LINE_ATTRIBUTES = 0;

    /**
     * The feature id for the '<em><b>Tick Style</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int GRID__TICK_STYLE = 1;

    /**
     * The feature id for the '<em><b>Tick Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int GRID__TICK_ATTRIBUTES = 2;

    /**
     * The feature id for the '<em><b>Tick Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int GRID__TICK_SIZE = 3;

    /**
     * The feature id for the '<em><b>Tick Count</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int GRID__TICK_COUNT = 4;

    /**
     * The number of structural features of the the '<em>Grid</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int GRID_FEATURE_COUNT = 5;

    /**
     * The feature id for the '<em><b>Caption</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL__CAPTION = 0;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL__BACKGROUND = 1;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL__OUTLINE = 2;

    /**
     * The feature id for the '<em><b>Shadow Color</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL__SHADOW_COLOR = 3;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL__INSETS = 4;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL__VISIBLE = 5;

    /**
     * The number of structural features of the the '<em>Label</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_FEATURE_COUNT = 6;

    /**
     * The meta object id for the '
     * {@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl <em>Marker Line</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.MarkerLineImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getMarkerLine()
     * @generated
     */
    int MARKER_LINE = 4;

    /**
     * The feature id for the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_LINE__LINE_ATTRIBUTES = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_LINE__VALUE = 1;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_LINE__LABEL = 2;

    /**
     * The feature id for the '<em><b>Label Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_LINE__LABEL_ANCHOR = 3;

    /**
     * The feature id for the '<em><b>Format Specifier</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_LINE__FORMAT_SPECIFIER = 4;

    /**
     * The number of structural features of the the '<em>Marker Line</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_LINE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '
     * {@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl <em>Marker Range</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getMarkerRange()
     * @generated
     */
    int MARKER_RANGE = 5;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__OUTLINE = 0;

    /**
     * The feature id for the '<em><b>Fill</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__FILL = 1;

    /**
     * The feature id for the '<em><b>Start Value</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__START_VALUE = 2;

    /**
     * The feature id for the '<em><b>End Value</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__END_VALUE = 3;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__LABEL = 4;

    /**
     * The feature id for the '<em><b>Label Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__LABEL_ANCHOR = 5;

    /**
     * The feature id for the '<em><b>Format Specifier</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE__FORMAT_SPECIFIER = 6;

    /**
     * The number of structural features of the the '<em>Marker Range</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MARKER_RANGE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl <em>Scale</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.ScaleImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getScale()
     * @generated
     */
    int SCALE = 6;

    /**
     * The feature id for the '<em><b>Min</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCALE__MIN = 0;

    /**
     * The feature id for the '<em><b>Max</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCALE__MAX = 1;

    /**
     * The feature id for the '<em><b>Step</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCALE__STEP = 2;

    /**
     * The feature id for the '<em><b>Unit</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCALE__UNIT = 3;

    /**
     * The feature id for the '<em><b>Minor Grids Per Unit</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCALE__MINOR_GRIDS_PER_UNIT = 4;

    /**
     * The number of structural features of the the '<em>Scale</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCALE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl <em>Series</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl
     * @see org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl#getSeries()
     * @generated
     */
    int SERIES = 7;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__VISIBLE = 0;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__LABEL = 1;

    /**
     * The feature id for the '<em><b>Data Definition</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__DATA_DEFINITION = 2;

    /**
     * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__SERIES_IDENTIFIER = 3;

    /**
     * The feature id for the '<em><b>Data Point</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__DATA_POINT = 4;

    /**
     * The feature id for the '<em><b>Data Set</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__DATA_SET = 5;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__LABEL_POSITION = 6;

    /**
     * The feature id for the '<em><b>Stacked</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__STACKED = 7;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__TRIGGERS = 8;

    /**
     * The feature id for the '<em><b>Translucent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SERIES__TRANSLUCENT = 9;

    /**
     * The number of structural features of the the '<em>Series</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SERIES_FEATURE_COUNT = 10;

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.component.Axis <em>Axis</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Axis</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis
     * @generated
     */
    EClass getAxis();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#getType <em>Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getType()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Type();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getTitle <em>Title</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Title</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getTitle()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_Title();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getSubtitle <em>Subtitle</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Subtitle</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getSubtitle()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_Subtitle();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#getTitlePosition <em>Title Position</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Title Position</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getTitlePosition()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_TitlePosition();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.Axis#getAssociatedAxes <em>Associated Axes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Associated Axes</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getAssociatedAxes()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_AssociatedAxes();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.Axis#getSeriesDefinitions <em>Series Definitions</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Series Definitions</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getSeriesDefinitions()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_SeriesDefinitions();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#getGapWidth <em>Gap Width</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Gap Width</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getGapWidth()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_GapWidth();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#getOrientation <em>Orientation</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Orientation</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getOrientation()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Orientation();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getLineAttributes <em>Line Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Line Attributes</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getLineAttributes()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_LineAttributes();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getLabel <em>Label</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Label</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getLabel()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_Label();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getFormatSpecifier <em>Format Specifier</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Format Specifier</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getFormatSpecifier()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_FormatSpecifier();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#getLabelPosition <em>Label Position</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Label Position</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getLabelPosition()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_LabelPosition();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#isStaggered <em>Staggered</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Staggered</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#isStaggered()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Staggered();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.Axis#getMarkerLines <em>Marker Lines</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Marker Lines</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getMarkerLines()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_MarkerLines();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.Axis#getMarkerRanges <em>Marker Ranges</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Marker Ranges</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getMarkerRanges()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_MarkerRanges();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getMajorGrid <em>Major Grid</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Major Grid</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getMajorGrid()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_MajorGrid();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getMinorGrid <em>Minor Grid</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Minor Grid</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getMinorGrid()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_MinorGrid();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getScale <em>Scale</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Scale</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getScale()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_Scale();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Axis#getOrigin <em>Origin</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Origin</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#getOrigin()
     * @see #getAxis()
     * @generated
     */
    EReference getAxis_Origin();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis <em>Primary Axis</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Primary Axis</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#isPrimaryAxis()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_PrimaryAxis();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#isCategoryAxis <em>Category Axis</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Category Axis</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#isCategoryAxis()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_CategoryAxis();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Axis#isPercent <em>Percent</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Percent</em>'.
     * @see org.eclipse.birt.chart.model.component.Axis#isPercent()
     * @see #getAxis()
     * @generated
     */
    EAttribute getAxis_Percent();

    /**
     * Returns the meta object for class '
     * {@link org.eclipse.birt.chart.model.component.ChartPreferences <em>Chart Preferences</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Chart Preferences</em>'.
     * @see org.eclipse.birt.chart.model.component.ChartPreferences
     * @generated
     */
    EClass getChartPreferences();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.ChartPreferences#getLabels <em>Labels</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Labels</em>'.
     * @see org.eclipse.birt.chart.model.component.ChartPreferences#getLabels()
     * @see #getChartPreferences()
     * @generated
     */
    EReference getChartPreferences_Labels();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.ChartPreferences#getBlocks <em>Blocks</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Blocks</em>'.
     * @see org.eclipse.birt.chart.model.component.ChartPreferences#getBlocks()
     * @see #getChartPreferences()
     * @generated
     */
    EReference getChartPreferences_Blocks();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.component.Grid <em>Grid</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Grid</em>'.
     * @see org.eclipse.birt.chart.model.component.Grid
     * @generated
     */
    EClass getGrid();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Grid#getLineAttributes <em>Line Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Line Attributes</em>'.
     * @see org.eclipse.birt.chart.model.component.Grid#getLineAttributes()
     * @see #getGrid()
     * @generated
     */
    EReference getGrid_LineAttributes();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Grid#getTickStyle <em>Tick Style</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Tick Style</em>'.
     * @see org.eclipse.birt.chart.model.component.Grid#getTickStyle()
     * @see #getGrid()
     * @generated
     */
    EAttribute getGrid_TickStyle();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Grid#getTickAttributes <em>Tick Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Tick Attributes</em>'.
     * @see org.eclipse.birt.chart.model.component.Grid#getTickAttributes()
     * @see #getGrid()
     * @generated
     */
    EReference getGrid_TickAttributes();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Grid#getTickSize <em>Tick Size</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Tick Size</em>'.
     * @see org.eclipse.birt.chart.model.component.Grid#getTickSize()
     * @see #getGrid()
     * @generated
     */
    EAttribute getGrid_TickSize();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Grid#getTickCount <em>Tick Count</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Tick Count</em>'.
     * @see org.eclipse.birt.chart.model.component.Grid#getTickCount()
     * @see #getGrid()
     * @generated
     */
    EAttribute getGrid_TickCount();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.component.Label <em>Label</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Label</em>'.
     * @see org.eclipse.birt.chart.model.component.Label
     * @generated
     */
    EClass getLabel();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Label#getCaption <em>Caption</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Caption</em>'.
     * @see org.eclipse.birt.chart.model.component.Label#getCaption()
     * @see #getLabel()
     * @generated
     */
    EReference getLabel_Caption();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Label#getBackground <em>Background</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Background</em>'.
     * @see org.eclipse.birt.chart.model.component.Label#getBackground()
     * @see #getLabel()
     * @generated
     */
    EReference getLabel_Background();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Label#getOutline <em>Outline</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Outline</em>'.
     * @see org.eclipse.birt.chart.model.component.Label#getOutline()
     * @see #getLabel()
     * @generated
     */
    EReference getLabel_Outline();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Label#getShadowColor <em>Shadow Color</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Shadow Color</em>'.
     * @see org.eclipse.birt.chart.model.component.Label#getShadowColor()
     * @see #getLabel()
     * @generated
     */
    EReference getLabel_ShadowColor();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Label#getInsets <em>Insets</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Insets</em>'.
     * @see org.eclipse.birt.chart.model.component.Label#getInsets()
     * @see #getLabel()
     * @generated
     */
    EReference getLabel_Insets();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Label#isVisible <em>Visible</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Visible</em>'.
     * @see org.eclipse.birt.chart.model.component.Label#isVisible()
     * @see #getLabel()
     * @generated
     */
    EAttribute getLabel_Visible();

    /**
     * Returns the meta object for class '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine <em>Marker Line</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for class '<em>Marker Line</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerLine
     * @generated
     */
    EClass getMarkerLine();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getLineAttributes <em>Line Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Line Attributes</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerLine#getLineAttributes()
     * @see #getMarkerLine()
     * @generated
     */
    EReference getMarkerLine_LineAttributes();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getValue <em>Value</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerLine#getValue()
     * @see #getMarkerLine()
     * @generated
     */
    EReference getMarkerLine_Value();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getLabel <em>Label</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Label</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerLine#getLabel()
     * @see #getMarkerLine()
     * @generated
     */
    EReference getMarkerLine_Label();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor <em>Label Anchor</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Label Anchor</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerLine#getLabelAnchor()
     * @see #getMarkerLine()
     * @generated
     */
    EAttribute getMarkerLine_LabelAnchor();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerLine#getFormatSpecifier <em>Format Specifier</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Format Specifier</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerLine#getFormatSpecifier()
     * @see #getMarkerLine()
     * @generated
     */
    EReference getMarkerLine_FormatSpecifier();

    /**
     * Returns the meta object for class '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange <em>Marker Range</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for class '<em>Marker Range</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange
     * @generated
     */
    EClass getMarkerRange();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getOutline <em>Outline</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Outline</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getOutline()
     * @see #getMarkerRange()
     * @generated
     */
    EReference getMarkerRange_Outline();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getFill <em>Fill</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Fill</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getFill()
     * @see #getMarkerRange()
     * @generated
     */
    EReference getMarkerRange_Fill();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getStartValue <em>Start Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Start Value</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getStartValue()
     * @see #getMarkerRange()
     * @generated
     */
    EReference getMarkerRange_StartValue();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getEndValue <em>End Value</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>End Value</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getEndValue()
     * @see #getMarkerRange()
     * @generated
     */
    EReference getMarkerRange_EndValue();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getLabel <em>Label</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Label</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getLabel()
     * @see #getMarkerRange()
     * @generated
     */
    EReference getMarkerRange_Label();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getLabelAnchor <em>Label Anchor</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Label Anchor</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getLabelAnchor()
     * @see #getMarkerRange()
     * @generated
     */
    EAttribute getMarkerRange_LabelAnchor();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.MarkerRange#getFormatSpecifier <em>Format Specifier</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Format Specifier</em>'.
     * @see org.eclipse.birt.chart.model.component.MarkerRange#getFormatSpecifier()
     * @see #getMarkerRange()
     * @generated
     */
    EReference getMarkerRange_FormatSpecifier();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.component.Scale <em>Scale</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Scale</em>'.
     * @see org.eclipse.birt.chart.model.component.Scale
     * @generated
     */
    EClass getScale();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Scale#getMin <em>Min</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Min</em>'.
     * @see org.eclipse.birt.chart.model.component.Scale#getMin()
     * @see #getScale()
     * @generated
     */
    EReference getScale_Min();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Scale#getMax <em>Max</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Max</em>'.
     * @see org.eclipse.birt.chart.model.component.Scale#getMax()
     * @see #getScale()
     * @generated
     */
    EReference getScale_Max();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Scale#getStep <em>Step</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Step</em>'.
     * @see org.eclipse.birt.chart.model.component.Scale#getStep()
     * @see #getScale()
     * @generated
     */
    EAttribute getScale_Step();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Scale#getUnit <em>Unit</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Unit</em>'.
     * @see org.eclipse.birt.chart.model.component.Scale#getUnit()
     * @see #getScale()
     * @generated
     */
    EAttribute getScale_Unit();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Minor Grids Per Unit</em>'.
     * @see org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit()
     * @see #getScale()
     * @generated
     */
    EAttribute getScale_MinorGridsPerUnit();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.component.Series <em>Series</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Series</em>'.
     * @see org.eclipse.birt.chart.model.component.Series
     * @generated
     */
    EClass getSeries();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Series#isVisible <em>Visible</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Visible</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#isVisible()
     * @see #getSeries()
     * @generated
     */
    EAttribute getSeries_Visible();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Series#getLabel <em>Label</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Label</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getLabel()
     * @see #getSeries()
     * @generated
     */
    EReference getSeries_Label();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.Series#getDataDefinition <em>Data Definition</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Data Definition</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getDataDefinition()
     * @see #getSeries()
     * @generated
     */
    EReference getSeries_DataDefinition();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Series#getSeriesIdentifier <em>Series Identifier</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Series Identifier</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getSeriesIdentifier()
     * @see #getSeries()
     * @generated
     */
    EAttribute getSeries_SeriesIdentifier();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Series#getDataPoint <em>Data Point</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Data Point</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getDataPoint()
     * @see #getSeries()
     * @generated
     */
    EReference getSeries_DataPoint();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.component.Series#getDataSet <em>Data Set</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Data Set</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getDataSet()
     * @see #getSeries()
     * @generated
     */
    EReference getSeries_DataSet();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Series#getLabelPosition <em>Label Position</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Label Position</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getLabelPosition()
     * @see #getSeries()
     * @generated
     */
    EAttribute getSeries_LabelPosition();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Series#isStacked <em>Stacked</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Stacked</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#isStacked()
     * @see #getSeries()
     * @generated
     */
    EAttribute getSeries_Stacked();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.component.Series#getTriggers <em>Triggers</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Triggers</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#getTriggers()
     * @see #getSeries()
     * @generated
     */
    EReference getSeries_Triggers();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.component.Series#isTranslucent <em>Translucent</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Translucent</em>'.
     * @see org.eclipse.birt.chart.model.component.Series#isTranslucent()
     * @see #getSeries()
     * @generated
     */
    EAttribute getSeries_Translucent();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ComponentFactory getComponentFactory();

} //ComponentPackage
