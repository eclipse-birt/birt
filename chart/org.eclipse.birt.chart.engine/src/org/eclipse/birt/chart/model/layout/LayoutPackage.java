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

package org.eclipse.birt.chart.model.layout;

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
 * @see org.eclipse.birt.chart.model.layout.LayoutFactory
 * @generated
 */
public interface LayoutPackage extends EPackage
{

    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNAME = "layout";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_URI = "http://www.birt.eclipse.org/ChartModelLayout";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_PREFIX = "layout";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    LayoutPackage eINSTANCE = org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl <em>Block</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.layout.impl.BlockImpl
     * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getBlock()
     * @generated
     */
    int BLOCK = 0;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__CHILDREN = 0;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__BOUNDS = 1;

    /**
     * The feature id for the '<em><b>Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__ANCHOR = 2;

    /**
     * The feature id for the '<em><b>Stretch</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__STRETCH = 3;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__INSETS = 4;

    /**
     * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__ROW = 5;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__COLUMN = 6;

    /**
     * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__ROWSPAN = 7;

    /**
     * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__COLUMNSPAN = 8;

    /**
     * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__MIN_SIZE = 9;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__OUTLINE = 10;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__BACKGROUND = 11;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__VISIBLE = 12;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK__TRIGGERS = 13;

    /**
     * The number of structural features of the the '<em>Block</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int BLOCK_FEATURE_COUNT = 14;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl <em>Client Area</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl
     * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getClientArea()
     * @generated
     */
    int CLIENT_AREA = 1;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLIENT_AREA__BACKGROUND = 0;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLIENT_AREA__OUTLINE = 1;

    /**
     * The feature id for the '<em><b>Shadow Color</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLIENT_AREA__SHADOW_COLOR = 2;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLIENT_AREA__INSETS = 3;

    /**
     * The number of structural features of the the '<em>Client Area</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLIENT_AREA_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl <em>Label Block</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl
     * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getLabelBlock()
     * @generated
     */
    int LABEL_BLOCK = 2;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__CHILDREN = BLOCK__CHILDREN;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__BOUNDS = BLOCK__BOUNDS;

    /**
     * The feature id for the '<em><b>Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__ANCHOR = BLOCK__ANCHOR;

    /**
     * The feature id for the '<em><b>Stretch</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__STRETCH = BLOCK__STRETCH;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__INSETS = BLOCK__INSETS;

    /**
     * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__ROW = BLOCK__ROW;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__COLUMN = BLOCK__COLUMN;

    /**
     * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__ROWSPAN = BLOCK__ROWSPAN;

    /**
     * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__COLUMNSPAN = BLOCK__COLUMNSPAN;

    /**
     * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__MIN_SIZE = BLOCK__MIN_SIZE;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__OUTLINE = BLOCK__OUTLINE;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__BACKGROUND = BLOCK__BACKGROUND;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__VISIBLE = BLOCK__VISIBLE;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__TRIGGERS = BLOCK__TRIGGERS;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK__LABEL = BLOCK_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the the '<em>Label Block</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LABEL_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl <em>Legend</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.layout.impl.LegendImpl
     * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getLegend()
     * @generated
     */
    int LEGEND = 3;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__CHILDREN = BLOCK__CHILDREN;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__BOUNDS = BLOCK__BOUNDS;

    /**
     * The feature id for the '<em><b>Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__ANCHOR = BLOCK__ANCHOR;

    /**
     * The feature id for the '<em><b>Stretch</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__STRETCH = BLOCK__STRETCH;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__INSETS = BLOCK__INSETS;

    /**
     * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__ROW = BLOCK__ROW;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__COLUMN = BLOCK__COLUMN;

    /**
     * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__ROWSPAN = BLOCK__ROWSPAN;

    /**
     * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__COLUMNSPAN = BLOCK__COLUMNSPAN;

    /**
     * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__MIN_SIZE = BLOCK__MIN_SIZE;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__OUTLINE = BLOCK__OUTLINE;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__BACKGROUND = BLOCK__BACKGROUND;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__VISIBLE = BLOCK__VISIBLE;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__TRIGGERS = BLOCK__TRIGGERS;

    /**
     * The feature id for the '<em><b>Horizontal Spacing</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__HORIZONTAL_SPACING = BLOCK_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Vertical Spacing</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__VERTICAL_SPACING = BLOCK_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Client Area</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__CLIENT_AREA = BLOCK_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Text</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__TEXT = BLOCK_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Orientation</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__ORIENTATION = BLOCK_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Direction</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__DIRECTION = BLOCK_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Separator</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__SEPARATOR = BLOCK_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__POSITION = BLOCK_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Item Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND__ITEM_TYPE = BLOCK_FEATURE_COUNT + 8;

    /**
     * The number of structural features of the the '<em>Legend</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int LEGEND_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 9;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl <em>Plot</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.layout.impl.PlotImpl
     * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getPlot()
     * @generated
     */
    int PLOT = 4;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__CHILDREN = BLOCK__CHILDREN;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__BOUNDS = BLOCK__BOUNDS;

    /**
     * The feature id for the '<em><b>Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__ANCHOR = BLOCK__ANCHOR;

    /**
     * The feature id for the '<em><b>Stretch</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__STRETCH = BLOCK__STRETCH;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__INSETS = BLOCK__INSETS;

    /**
     * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__ROW = BLOCK__ROW;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__COLUMN = BLOCK__COLUMN;

    /**
     * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__ROWSPAN = BLOCK__ROWSPAN;

    /**
     * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__COLUMNSPAN = BLOCK__COLUMNSPAN;

    /**
     * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__MIN_SIZE = BLOCK__MIN_SIZE;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__OUTLINE = BLOCK__OUTLINE;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__BACKGROUND = BLOCK__BACKGROUND;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__VISIBLE = BLOCK__VISIBLE;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__TRIGGERS = BLOCK__TRIGGERS;

    /**
     * The feature id for the '<em><b>Horizontal Spacing</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__HORIZONTAL_SPACING = BLOCK_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Vertical Spacing</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__VERTICAL_SPACING = BLOCK_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Client Area</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int PLOT__CLIENT_AREA = BLOCK_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the the '<em>Plot</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int PLOT_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl <em>Title Block</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl
     * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitleBlock()
     * @generated
     */
    int TITLE_BLOCK = 5;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__CHILDREN = LABEL_BLOCK__CHILDREN;

    /**
     * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__BOUNDS = LABEL_BLOCK__BOUNDS;

    /**
     * The feature id for the '<em><b>Anchor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__ANCHOR = LABEL_BLOCK__ANCHOR;

    /**
     * The feature id for the '<em><b>Stretch</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__STRETCH = LABEL_BLOCK__STRETCH;

    /**
     * The feature id for the '<em><b>Insets</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__INSETS = LABEL_BLOCK__INSETS;

    /**
     * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__ROW = LABEL_BLOCK__ROW;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__COLUMN = LABEL_BLOCK__COLUMN;

    /**
     * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__ROWSPAN = LABEL_BLOCK__ROWSPAN;

    /**
     * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__COLUMNSPAN = LABEL_BLOCK__COLUMNSPAN;

    /**
     * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__MIN_SIZE = LABEL_BLOCK__MIN_SIZE;

    /**
     * The feature id for the '<em><b>Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__OUTLINE = LABEL_BLOCK__OUTLINE;

    /**
     * The feature id for the '<em><b>Background</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__BACKGROUND = LABEL_BLOCK__BACKGROUND;

    /**
     * The feature id for the '<em><b>Visible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__VISIBLE = LABEL_BLOCK__VISIBLE;

    /**
     * The feature id for the '<em><b>Triggers</b></em>' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__TRIGGERS = LABEL_BLOCK__TRIGGERS;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK__LABEL = LABEL_BLOCK__LABEL;

    /**
     * The number of structural features of the the '<em>Title Block</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int TITLE_BLOCK_FEATURE_COUNT = LABEL_BLOCK_FEATURE_COUNT + 0;

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.layout.Block <em>Block</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Block</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block
     * @generated
     */
    EClass getBlock();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.layout.Block#getChildren <em>Children</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Children</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getChildren()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_Children();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Block#getBounds <em>Bounds</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Bounds</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getBounds()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_Bounds();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Block#getAnchor <em>Anchor</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Anchor</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getAnchor()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Anchor();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Block#getStretch <em>Stretch</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Stretch</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getStretch()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Stretch();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Block#getInsets <em>Insets</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Insets</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getInsets()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_Insets();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.layout.Block#getRow <em>Row</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Row</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getRow()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Row();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Block#getColumn <em>Column</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Column</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getColumn()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Column();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Block#getRowspan <em>Rowspan</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Rowspan</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getRowspan()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Rowspan();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Block#getColumnspan <em>Columnspan</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Columnspan</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getColumnspan()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Columnspan();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Block#getMinSize <em>Min Size</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Min Size</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getMinSize()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_MinSize();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Block#getOutline <em>Outline</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Outline</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getOutline()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_Outline();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Block#getBackground <em>Background</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Background</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getBackground()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_Background();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Block#isVisible <em>Visible</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Visible</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#isVisible()
     * @see #getBlock()
     * @generated
     */
    EAttribute getBlock_Visible();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.eclipse.birt.chart.model.layout.Block#getTriggers <em>Triggers</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Triggers</em>'.
     * @see org.eclipse.birt.chart.model.layout.Block#getTriggers()
     * @see #getBlock()
     * @generated
     */
    EReference getBlock_Triggers();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.layout.ClientArea <em>Client Area</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Client Area</em>'.
     * @see org.eclipse.birt.chart.model.layout.ClientArea
     * @generated
     */
    EClass getClientArea();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.ClientArea#getBackground <em>Background</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Background</em>'.
     * @see org.eclipse.birt.chart.model.layout.ClientArea#getBackground()
     * @see #getClientArea()
     * @generated
     */
    EReference getClientArea_Background();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.ClientArea#getOutline <em>Outline</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Outline</em>'.
     * @see org.eclipse.birt.chart.model.layout.ClientArea#getOutline()
     * @see #getClientArea()
     * @generated
     */
    EReference getClientArea_Outline();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.ClientArea#getShadowColor <em>Shadow Color</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Shadow Color</em>'.
     * @see org.eclipse.birt.chart.model.layout.ClientArea#getShadowColor()
     * @see #getClientArea()
     * @generated
     */
    EReference getClientArea_ShadowColor();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.ClientArea#getInsets <em>Insets</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Insets</em>'.
     * @see org.eclipse.birt.chart.model.layout.ClientArea#getInsets()
     * @see #getClientArea()
     * @generated
     */
    EReference getClientArea_Insets();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.layout.LabelBlock <em>Label Block</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Label Block</em>'.
     * @see org.eclipse.birt.chart.model.layout.LabelBlock
     * @generated
     */
    EClass getLabelBlock();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.LabelBlock#getLabel <em>Label</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Label</em>'.
     * @see org.eclipse.birt.chart.model.layout.LabelBlock#getLabel()
     * @see #getLabelBlock()
     * @generated
     */
    EReference getLabelBlock_Label();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.layout.Legend <em>Legend</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Legend</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend
     * @generated
     */
    EClass getLegend();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Horizontal Spacing</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing()
     * @see #getLegend()
     * @generated
     */
    EAttribute getLegend_HorizontalSpacing();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing <em>Vertical Spacing</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Vertical Spacing</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing()
     * @see #getLegend()
     * @generated
     */
    EAttribute getLegend_VerticalSpacing();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getClientArea <em>Client Area</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Client Area</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getClientArea()
     * @see #getLegend()
     * @generated
     */
    EReference getLegend_ClientArea();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getText <em>Text</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Text</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getText()
     * @see #getLegend()
     * @generated
     */
    EReference getLegend_Text();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getOrientation <em>Orientation</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Orientation</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getOrientation()
     * @see #getLegend()
     * @generated
     */
    EAttribute getLegend_Orientation();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getDirection <em>Direction</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Direction</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getDirection()
     * @see #getLegend()
     * @generated
     */
    EAttribute getLegend_Direction();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getSeparator <em>Separator</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Separator</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getSeparator()
     * @see #getLegend()
     * @generated
     */
    EReference getLegend_Separator();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getPosition <em>Position</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Position</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getPosition()
     * @see #getLegend()
     * @generated
     */
    EAttribute getLegend_Position();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Item Type</em>'.
     * @see org.eclipse.birt.chart.model.layout.Legend#getItemType()
     * @see #getLegend()
     * @generated
     */
    EAttribute getLegend_ItemType();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.layout.Plot <em>Plot</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Plot</em>'.
     * @see org.eclipse.birt.chart.model.layout.Plot
     * @generated
     */
    EClass getPlot();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing <em>Horizontal Spacing</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Horizontal Spacing</em>'.
     * @see org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing()
     * @see #getPlot()
     * @generated
     */
    EAttribute getPlot_HorizontalSpacing();

    /**
     * Returns the meta object for the attribute '
     * {@link org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing <em>Vertical Spacing</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Vertical Spacing</em>'.
     * @see org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing()
     * @see #getPlot()
     * @generated
     */
    EAttribute getPlot_VerticalSpacing();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.eclipse.birt.chart.model.layout.Plot#getClientArea <em>Client Area</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Client Area</em>'.
     * @see org.eclipse.birt.chart.model.layout.Plot#getClientArea()
     * @see #getPlot()
     * @generated
     */
    EReference getPlot_ClientArea();

    /**
     * Returns the meta object for class '{@link org.eclipse.birt.chart.model.layout.TitleBlock <em>Title Block</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Title Block</em>'.
     * @see org.eclipse.birt.chart.model.layout.TitleBlock
     * @generated
     */
    EClass getTitleBlock();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the factory that creates the instances of the model.
     * @generated
     */
    LayoutFactory getLayoutFactory();

} //LayoutPackage
