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

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.component.ComponentPackage;
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
 * @see org.eclipse.birt.chart.model.type.TypeFactory
 * @generated
 */
public interface TypePackage extends EPackage
{

    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNAME = "type";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_URI = "http://www.birt.eclipse.org/ChartModelType";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_PREFIX = "type";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    TypePackage eINSTANCE = org.eclipse.birt.chart.model.type.impl.TypePackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.type.impl.BarSeriesImpl <em>Bar Series</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.type.impl.BarSeriesImpl
     * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getBarSeries()
     * @generated
     */
    int BAR_SERIES = 0;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

    /**
     * The feature id for the '<em><b>Data Definition</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

    /**
     * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

    /**
     * The feature id for the '<em><b>Data Point</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

    /**
     * The feature id for the '<em><b>Data Set</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__DATA_SET = ComponentPackage.SERIES__DATA_SET;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

    /**
     * The feature id for the '<em><b>Stacked</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

    /**
     * The feature id for the '<em><b>Translucent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

    /**
     * The feature id for the '<em><b>Riser</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__RISER = ComponentPackage.SERIES_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Riser Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES__RISER_OUTLINE = ComponentPackage.SERIES_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the the '<em>Bar Series</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BAR_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl <em>Line Series</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.type.impl.LineSeriesImpl
     * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getLineSeries()
     * @generated
     */
    int LINE_SERIES = 1;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

    /**
     * The feature id for the '<em><b>Data Definition</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

    /**
     * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

    /**
     * The feature id for the '<em><b>Data Point</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

    /**
     * The feature id for the '<em><b>Data Set</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__DATA_SET = ComponentPackage.SERIES__DATA_SET;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

    /**
     * The feature id for the '<em><b>Stacked</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

    /**
     * The feature id for the '<em><b>Translucent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

    /**
     * The feature id for the '<em><b>Marker</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__MARKER = ComponentPackage.SERIES_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__CURVE = ComponentPackage.SERIES_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Shadow Color</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES__SHADOW_COLOR = ComponentPackage.SERIES_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the the '<em>Line Series</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LINE_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl <em>Pie Series</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.type.impl.PieSeriesImpl
     * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getPieSeries()
     * @generated
     */
    int PIE_SERIES = 2;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

    /**
     * The feature id for the '<em><b>Data Definition</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

    /**
     * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

    /**
     * The feature id for the '<em><b>Data Point</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

    /**
     * The feature id for the '<em><b>Data Set</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__DATA_SET = ComponentPackage.SERIES__DATA_SET;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

    /**
     * The feature id for the '<em><b>Stacked</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

    /**
     * The feature id for the '<em><b>Translucent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

    /**
     * The feature id for the '<em><b>Explosion</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__EXPLOSION = ComponentPackage.SERIES_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__TITLE = ComponentPackage.SERIES_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Title Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__TITLE_POSITION = ComponentPackage.SERIES_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Leader Line Attributes</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__LEADER_LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Leader Line Style</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__LEADER_LINE_STYLE = ComponentPackage.SERIES_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Leader Line Length</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__LEADER_LINE_LENGTH = ComponentPackage.SERIES_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Slice Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES__SLICE_OUTLINE = ComponentPackage.SERIES_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the the '<em>Pie Series</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PIE_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '
     * {@link org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl <em>Scatter Series</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl
     * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getScatterSeries()
     * @generated
     */
    int SCATTER_SERIES = 3;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__VISIBLE = LINE_SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__LABEL = LINE_SERIES__LABEL;

    /**
     * The feature id for the '<em><b>Data Definition</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__DATA_DEFINITION = LINE_SERIES__DATA_DEFINITION;

    /**
     * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__SERIES_IDENTIFIER = LINE_SERIES__SERIES_IDENTIFIER;

    /**
     * The feature id for the '<em><b>Data Point</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__DATA_POINT = LINE_SERIES__DATA_POINT;

    /**
     * The feature id for the '<em><b>Data Set</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__DATA_SET = LINE_SERIES__DATA_SET;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__LABEL_POSITION = LINE_SERIES__LABEL_POSITION;

    /**
     * The feature id for the '<em><b>Stacked</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__STACKED = LINE_SERIES__STACKED;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__TRIGGERS = LINE_SERIES__TRIGGERS;

    /**
     * The feature id for the '<em><b>Translucent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__TRANSLUCENT = LINE_SERIES__TRANSLUCENT;

    /**
     * The feature id for the '<em><b>Marker</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__MARKER = LINE_SERIES__MARKER;

    /**
     * The feature id for the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__LINE_ATTRIBUTES = LINE_SERIES__LINE_ATTRIBUTES;

    /**
     * The feature id for the '<em><b>Curve</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__CURVE = LINE_SERIES__CURVE;

    /**
     * The feature id for the '<em><b>Shadow Color</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES__SHADOW_COLOR = LINE_SERIES__SHADOW_COLOR;

    /**
     * The number of structural features of the the '<em>Scatter Series</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCATTER_SERIES_FEATURE_COUNT = LINE_SERIES_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl <em>Stock Series</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.type.impl.StockSeriesImpl
     * @see org.eclipse.birt.chart.model.type.impl.TypePackageImpl#getStockSeries()
     * @generated
     */
    int STOCK_SERIES = 4;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

    /**
     * The feature id for the '<em><b>Data Definition</b></em>' containment reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

    /**
     * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

    /**
     * The feature id for the '<em><b>Data Point</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

    /**
     * The feature id for the '<em><b>Data Set</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__DATA_SET = ComponentPackage.SERIES__DATA_SET;

    /**
     * The feature id for the '<em><b>Label Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

    /**
     * The feature id for the '<em><b>Stacked</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

    /**
     * The feature id for the '<em><b>Translucent</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

    /**
     * The feature id for the '<em><b>Fill</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__FILL = ComponentPackage.SERIES_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES__LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the the '<em>Stock Series</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int STOCK_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 2;

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.type.BarSeries <em>Bar Series</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Bar Series</em>'.
     * @see org.eclipse.birt.chart.model.type.BarSeries
     * @generated
     */
    EClass getBarSeries();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.type.BarSeries#getRiser <em>Riser</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Riser</em>'.
     * @see org.eclipse.birt.chart.model.type.BarSeries#getRiser()
     * @see #getBarSeries()
     * @generated
     */
    EAttribute getBarSeries_Riser();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.BarSeries#getRiserOutline <em>Riser Outline</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Riser Outline</em>'.
     * @see org.eclipse.birt.chart.model.type.BarSeries#getRiserOutline()
     * @see #getBarSeries()
     * @generated
     */
    EReference getBarSeries_RiserOutline();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.type.LineSeries <em>Line Series</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Line Series</em>'.
     * @see org.eclipse.birt.chart.model.type.LineSeries
     * @generated
     */
    EClass getLineSeries();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.LineSeries#getMarker <em>Marker</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Marker</em>'.
     * @see org.eclipse.birt.chart.model.type.LineSeries#getMarker()
     * @see #getLineSeries()
     * @generated
     */
    EReference getLineSeries_Marker();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.LineSeries#getLineAttributes <em>Line Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Line Attributes</em>'.
     * @see org.eclipse.birt.chart.model.type.LineSeries#getLineAttributes()
     * @see #getLineSeries()
     * @generated
     */
    EReference getLineSeries_LineAttributes();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.type.LineSeries#isCurve <em>Curve</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Curve</em>'.
     * @see org.eclipse.birt.chart.model.type.LineSeries#isCurve()
     * @see #getLineSeries()
     * @generated
     */
    EAttribute getLineSeries_Curve();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.LineSeries#getShadowColor <em>Shadow Color</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Shadow Color</em>'.
     * @see org.eclipse.birt.chart.model.type.LineSeries#getShadowColor()
     * @see #getLineSeries()
     * @generated
     */
    EReference getLineSeries_ShadowColor();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.type.PieSeries <em>Pie Series</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Pie Series</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries
     * @generated
     */
    EClass getPieSeries();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getExplosion <em>Explosion</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Explosion</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getExplosion()
     * @see #getPieSeries()
     * @generated
     */
    EAttribute getPieSeries_Explosion();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getTitle <em>Title</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Title</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getTitle()
     * @see #getPieSeries()
     * @generated
     */
    EReference getPieSeries_Title();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition <em>Title Position</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Title Position</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition()
     * @see #getPieSeries()
     * @generated
     */
    EAttribute getPieSeries_TitlePosition();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineAttributes <em>Leader Line Attributes</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Leader Line Attributes</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineAttributes()
     * @see #getPieSeries()
     * @generated
     */
    EReference getPieSeries_LeaderLineAttributes();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle <em>Leader Line Style</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Leader Line Style</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle()
     * @see #getPieSeries()
     * @generated
     */
    EAttribute getPieSeries_LeaderLineStyle();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength <em>Leader Line Length</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Leader Line Length</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength()
     * @see #getPieSeries()
     * @generated
     */
    EAttribute getPieSeries_LeaderLineLength();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.PieSeries#getSliceOutline <em>Slice Outline</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Slice Outline</em>'.
     * @see org.eclipse.birt.chart.model.type.PieSeries#getSliceOutline()
     * @see #getPieSeries()
     * @generated
     */
    EReference getPieSeries_SliceOutline();

    /**
     * Returns the meta object for class '
     * {@link org.eclipse.birt.chart.model.type.ScatterSeries <em>Scatter Series</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for class '<em>Scatter Series</em>'.
     * @see org.eclipse.birt.chart.model.type.ScatterSeries
     * @generated
     */
    EClass getScatterSeries();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.type.StockSeries <em>Stock Series</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Stock Series</em>'.
     * @see org.eclipse.birt.chart.model.type.StockSeries
     * @generated
     */
    EClass getStockSeries();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.StockSeries#getFill <em>Fill</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Fill</em>'.
     * @see org.eclipse.birt.chart.model.type.StockSeries#getFill()
     * @see #getStockSeries()
     * @generated
     */
    EReference getStockSeries_Fill();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes <em>Line Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Line Attributes</em>'.
     * @see org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes()
     * @see #getStockSeries()
     * @generated
     */
    EReference getStockSeries_LineAttributes();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TypeFactory getTypeFactory();

} //TypePackage
