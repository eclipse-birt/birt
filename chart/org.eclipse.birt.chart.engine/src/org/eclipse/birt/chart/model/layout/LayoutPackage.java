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
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.birt.chart.model.layout.LayoutFactory
 * @generated
 */
public interface LayoutPackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "layout"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelLayout"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "layout"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	LayoutPackage eINSTANCE = org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl <em>Block</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.BlockImpl
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getBlock()
	 * @generated
	 */
	int BLOCK = 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__CHILDREN = 0;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__BOUNDS = 1;

	/**
	 * The feature id for the '<em><b>Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__ANCHOR = 2;

	/**
	 * The feature id for the '<em><b>Stretch</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__STRETCH = 3;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__INSETS = 4;

	/**
	 * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__ROW = 5;

	/**
	 * The feature id for the '<em><b>Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__COLUMN = 6;

	/**
	 * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__ROWSPAN = 7;

	/**
	 * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__COLUMNSPAN = 8;

	/**
	 * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__MIN_SIZE = 9;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__OUTLINE = 10;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__BACKGROUND = 11;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__VISIBLE = 12;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__TRIGGERS = 13;

	/**
	 * The feature id for the '<em><b>Width Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__WIDTH_HINT = 14;

	/**
	 * The feature id for the '<em><b>Height Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__HEIGHT_HINT = 15;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK__CURSOR = 16;

	/**
	 * The number of structural features of the '<em>Block</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BLOCK_FEATURE_COUNT = 17;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl <em>Client
	 * Area</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getClientArea()
	 * @generated
	 */
	int CLIENT_AREA = 1;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLIENT_AREA__BACKGROUND = 0;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLIENT_AREA__OUTLINE = 1;

	/**
	 * The feature id for the '<em><b>Shadow Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLIENT_AREA__SHADOW_COLOR = 2;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLIENT_AREA__INSETS = 3;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLIENT_AREA__VISIBLE = 4;

	/**
	 * The number of structural features of the '<em>Client Area</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLIENT_AREA_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl <em>Label
	 * Block</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getLabelBlock()
	 * @generated
	 */
	int LABEL_BLOCK = 2;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__CHILDREN = BLOCK__CHILDREN;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__BOUNDS = BLOCK__BOUNDS;

	/**
	 * The feature id for the '<em><b>Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__ANCHOR = BLOCK__ANCHOR;

	/**
	 * The feature id for the '<em><b>Stretch</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__STRETCH = BLOCK__STRETCH;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__INSETS = BLOCK__INSETS;

	/**
	 * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__ROW = BLOCK__ROW;

	/**
	 * The feature id for the '<em><b>Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__COLUMN = BLOCK__COLUMN;

	/**
	 * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__ROWSPAN = BLOCK__ROWSPAN;

	/**
	 * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__COLUMNSPAN = BLOCK__COLUMNSPAN;

	/**
	 * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__MIN_SIZE = BLOCK__MIN_SIZE;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__OUTLINE = BLOCK__OUTLINE;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__BACKGROUND = BLOCK__BACKGROUND;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__VISIBLE = BLOCK__VISIBLE;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__TRIGGERS = BLOCK__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Width Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__WIDTH_HINT = BLOCK__WIDTH_HINT;

	/**
	 * The feature id for the '<em><b>Height Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__HEIGHT_HINT = BLOCK__HEIGHT_HINT;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__CURSOR = BLOCK__CURSOR;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK__LABEL = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Label Block</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LABEL_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl <em>Legend</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.LegendImpl
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getLegend()
	 * @generated
	 */
	int LEGEND = 3;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__CHILDREN = BLOCK__CHILDREN;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__BOUNDS = BLOCK__BOUNDS;

	/**
	 * The feature id for the '<em><b>Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__ANCHOR = BLOCK__ANCHOR;

	/**
	 * The feature id for the '<em><b>Stretch</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__STRETCH = BLOCK__STRETCH;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__INSETS = BLOCK__INSETS;

	/**
	 * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__ROW = BLOCK__ROW;

	/**
	 * The feature id for the '<em><b>Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__COLUMN = BLOCK__COLUMN;

	/**
	 * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__ROWSPAN = BLOCK__ROWSPAN;

	/**
	 * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__COLUMNSPAN = BLOCK__COLUMNSPAN;

	/**
	 * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__MIN_SIZE = BLOCK__MIN_SIZE;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__OUTLINE = BLOCK__OUTLINE;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__BACKGROUND = BLOCK__BACKGROUND;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__VISIBLE = BLOCK__VISIBLE;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__TRIGGERS = BLOCK__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Width Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__WIDTH_HINT = BLOCK__WIDTH_HINT;

	/**
	 * The feature id for the '<em><b>Height Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__HEIGHT_HINT = BLOCK__HEIGHT_HINT;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__CURSOR = BLOCK__CURSOR;

	/**
	 * The feature id for the '<em><b>Horizontal Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__HORIZONTAL_SPACING = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vertical Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__VERTICAL_SPACING = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Client Area</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__CLIENT_AREA = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Text</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__TEXT = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Orientation</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__ORIENTATION = BLOCK_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__DIRECTION = BLOCK_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Separator</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__SEPARATOR = BLOCK_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__POSITION = BLOCK_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Item Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__ITEM_TYPE = BLOCK_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__TITLE = BLOCK_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Title Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__TITLE_POSITION = BLOCK_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Show Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__SHOW_VALUE = BLOCK_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Show Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__SHOW_PERCENT = BLOCK_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Show Total</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__SHOW_TOTAL = BLOCK_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Wrapping Size</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__WRAPPING_SIZE = BLOCK_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Max Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__MAX_PERCENT = BLOCK_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Title Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__TITLE_PERCENT = BLOCK_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Ellipsis</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__ELLIPSIS = BLOCK_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND__FORMAT_SPECIFIER = BLOCK_FEATURE_COUNT + 18;

	/**
	 * The number of structural features of the '<em>Legend</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LEGEND_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 19;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl <em>Plot</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.PlotImpl
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getPlot()
	 * @generated
	 */
	int PLOT = 4;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__CHILDREN = BLOCK__CHILDREN;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__BOUNDS = BLOCK__BOUNDS;

	/**
	 * The feature id for the '<em><b>Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__ANCHOR = BLOCK__ANCHOR;

	/**
	 * The feature id for the '<em><b>Stretch</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__STRETCH = BLOCK__STRETCH;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__INSETS = BLOCK__INSETS;

	/**
	 * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__ROW = BLOCK__ROW;

	/**
	 * The feature id for the '<em><b>Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__COLUMN = BLOCK__COLUMN;

	/**
	 * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__ROWSPAN = BLOCK__ROWSPAN;

	/**
	 * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__COLUMNSPAN = BLOCK__COLUMNSPAN;

	/**
	 * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__MIN_SIZE = BLOCK__MIN_SIZE;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__OUTLINE = BLOCK__OUTLINE;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__BACKGROUND = BLOCK__BACKGROUND;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__VISIBLE = BLOCK__VISIBLE;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__TRIGGERS = BLOCK__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Width Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__WIDTH_HINT = BLOCK__WIDTH_HINT;

	/**
	 * The feature id for the '<em><b>Height Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__HEIGHT_HINT = BLOCK__HEIGHT_HINT;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__CURSOR = BLOCK__CURSOR;

	/**
	 * The feature id for the '<em><b>Horizontal Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__HORIZONTAL_SPACING = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vertical Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__VERTICAL_SPACING = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Client Area</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT__CLIENT_AREA = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Plot</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLOT_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl <em>Title
	 * Block</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitleBlock()
	 * @generated
	 */
	int TITLE_BLOCK = 5;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__CHILDREN = LABEL_BLOCK__CHILDREN;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__BOUNDS = LABEL_BLOCK__BOUNDS;

	/**
	 * The feature id for the '<em><b>Anchor</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__ANCHOR = LABEL_BLOCK__ANCHOR;

	/**
	 * The feature id for the '<em><b>Stretch</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__STRETCH = LABEL_BLOCK__STRETCH;

	/**
	 * The feature id for the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__INSETS = LABEL_BLOCK__INSETS;

	/**
	 * The feature id for the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__ROW = LABEL_BLOCK__ROW;

	/**
	 * The feature id for the '<em><b>Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__COLUMN = LABEL_BLOCK__COLUMN;

	/**
	 * The feature id for the '<em><b>Rowspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__ROWSPAN = LABEL_BLOCK__ROWSPAN;

	/**
	 * The feature id for the '<em><b>Columnspan</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__COLUMNSPAN = LABEL_BLOCK__COLUMNSPAN;

	/**
	 * The feature id for the '<em><b>Min Size</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__MIN_SIZE = LABEL_BLOCK__MIN_SIZE;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__OUTLINE = LABEL_BLOCK__OUTLINE;

	/**
	 * The feature id for the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__BACKGROUND = LABEL_BLOCK__BACKGROUND;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__VISIBLE = LABEL_BLOCK__VISIBLE;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__TRIGGERS = LABEL_BLOCK__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Width Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__WIDTH_HINT = LABEL_BLOCK__WIDTH_HINT;

	/**
	 * The feature id for the '<em><b>Height Hint</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__HEIGHT_HINT = LABEL_BLOCK__HEIGHT_HINT;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__CURSOR = LABEL_BLOCK__CURSOR;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__LABEL = LABEL_BLOCK__LABEL;

	/**
	 * The feature id for the '<em><b>Auto</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK__AUTO = LABEL_BLOCK_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Title Block</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TITLE_BLOCK_FEATURE_COUNT = LABEL_BLOCK_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '<em>Ellipsis Type</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getEllipsisType()
	 * @generated
	 */
	int ELLIPSIS_TYPE = 6;

	/**
	 * The meta object id for the '<em>Ellipsis Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.Integer
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getEllipsisTypeObject()
	 * @generated
	 */
	int ELLIPSIS_TYPE_OBJECT = 7;

	/**
	 * The meta object id for the '<em>Title Percent Type</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitlePercentType()
	 * @generated
	 */
	int TITLE_PERCENT_TYPE = 8;

	/**
	 * The meta object id for the '<em>Title Percent Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.Double
	 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitlePercentTypeObject()
	 * @generated
	 */
	int TITLE_PERCENT_TYPE_OBJECT = 9;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.layout.Block <em>Block</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Block</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block
	 * @generated
	 */
	EClass getBlock();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getChildren
	 * <em>Children</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Children</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getChildren()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Children();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getBounds
	 * <em>Bounds</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Bounds</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getBounds()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Bounds();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getAnchor
	 * <em>Anchor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Anchor</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getAnchor()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Anchor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getStretch
	 * <em>Stretch</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Stretch</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getStretch()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Stretch();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getInsets
	 * <em>Insets</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Insets</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getInsets()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Insets();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRow <em>Row</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Row</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getRow()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Row();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumn
	 * <em>Column</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Column</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getColumn()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Column();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRowspan
	 * <em>Rowspan</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Rowspan</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getRowspan()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Rowspan();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumnspan
	 * <em>Columnspan</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Columnspan</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getColumnspan()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Columnspan();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getMinSize <em>Min
	 * Size</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Min Size</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getMinSize()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_MinSize();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getOutline
	 * <em>Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getOutline()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Outline();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getBackground
	 * <em>Background</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Background</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getBackground()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Background();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#isVisible
	 * <em>Visible</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#isVisible()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_Visible();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getTriggers
	 * <em>Triggers</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Triggers</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getTriggers()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Triggers();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getWidthHint <em>Width
	 * Hint</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Width Hint</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getWidthHint()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_WidthHint();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getHeightHint <em>Height
	 * Hint</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Height Hint</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getHeightHint()
	 * @see #getBlock()
	 * @generated
	 */
	EAttribute getBlock_HeightHint();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getCursor
	 * <em>Cursor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cursor</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Block#getCursor()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Cursor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea <em>Client
	 * Area</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Client Area</em>'.
	 * @see org.eclipse.birt.chart.model.layout.ClientArea
	 * @generated
	 */
	EClass getClientArea();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getBackground
	 * <em>Background</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Background</em>'.
	 * @see org.eclipse.birt.chart.model.layout.ClientArea#getBackground()
	 * @see #getClientArea()
	 * @generated
	 */
	EReference getClientArea_Background();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getOutline
	 * <em>Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline</em>'.
	 * @see org.eclipse.birt.chart.model.layout.ClientArea#getOutline()
	 * @see #getClientArea()
	 * @generated
	 */
	EReference getClientArea_Outline();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getShadowColor
	 * <em>Shadow Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Shadow
	 *         Color</em>'.
	 * @see org.eclipse.birt.chart.model.layout.ClientArea#getShadowColor()
	 * @see #getClientArea()
	 * @generated
	 */
	EReference getClientArea_ShadowColor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#getInsets
	 * <em>Insets</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Insets</em>'.
	 * @see org.eclipse.birt.chart.model.layout.ClientArea#getInsets()
	 * @see #getClientArea()
	 * @generated
	 */
	EReference getClientArea_Insets();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.ClientArea#isVisible
	 * <em>Visible</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.layout.ClientArea#isVisible()
	 * @see #getClientArea()
	 * @generated
	 */
	EAttribute getClientArea_Visible();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.layout.LabelBlock <em>Label
	 * Block</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Label Block</em>'.
	 * @see org.eclipse.birt.chart.model.layout.LabelBlock
	 * @generated
	 */
	EClass getLabelBlock();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.LabelBlock#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.layout.LabelBlock#getLabel()
	 * @see #getLabelBlock()
	 * @generated
	 */
	EReference getLabelBlock_Label();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.layout.Legend <em>Legend</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Legend</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend
	 * @generated
	 */
	EClass getLegend();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing
	 * <em>Horizontal Spacing</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Horizontal Spacing</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_HorizontalSpacing();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing
	 * <em>Vertical Spacing</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Vertical Spacing</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_VerticalSpacing();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getClientArea <em>Client
	 * Area</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Client Area</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getClientArea()
	 * @see #getLegend()
	 * @generated
	 */
	EReference getLegend_ClientArea();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getText <em>Text</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Text</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getText()
	 * @see #getLegend()
	 * @generated
	 */
	EReference getLegend_Text();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getOrientation
	 * <em>Orientation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Orientation</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getOrientation()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_Orientation();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getDirection
	 * <em>Direction</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getDirection()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_Direction();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getSeparator
	 * <em>Separator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Separator</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getSeparator()
	 * @see #getLegend()
	 * @generated
	 */
	EReference getLegend_Separator();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getPosition
	 * <em>Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Position</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getPosition()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_Position();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Item Type</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getItemType()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_ItemType();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Title</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getTitle()
	 * @see #getLegend()
	 * @generated
	 */
	EReference getLegend_Title();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePosition <em>Title
	 * Position</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Title Position</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getTitlePosition()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_TitlePosition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#isShowValue <em>Show
	 * Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show Value</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#isShowValue()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_ShowValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#isShowPercent <em>Show
	 * Percent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show Percent</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#isShowPercent()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_ShowPercent();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#isShowTotal <em>Show
	 * Total</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show Total</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#isShowTotal()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_ShowTotal();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getWrappingSize
	 * <em>Wrapping Size</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Wrapping Size</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getWrappingSize()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_WrappingSize();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getMaxPercent <em>Max
	 * Percent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Max Percent</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getMaxPercent()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_MaxPercent();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePercent <em>Title
	 * Percent</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Title Percent</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getTitlePercent()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_TitlePercent();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getEllipsis
	 * <em>Ellipsis</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Ellipsis</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getEllipsis()
	 * @see #getLegend()
	 * @generated
	 */
	EAttribute getLegend_Ellipsis();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Legend#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Legend#getFormatSpecifier()
	 * @see #getLegend()
	 * @generated
	 */
	EReference getLegend_FormatSpecifier();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.layout.Plot <em>Plot</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Plot</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Plot
	 * @generated
	 */
	EClass getPlot();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing
	 * <em>Horizontal Spacing</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Horizontal Spacing</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_HorizontalSpacing();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing
	 * <em>Vertical Spacing</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Vertical Spacing</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_VerticalSpacing();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getClientArea <em>Client
	 * Area</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Client Area</em>'.
	 * @see org.eclipse.birt.chart.model.layout.Plot#getClientArea()
	 * @see #getPlot()
	 * @generated
	 */
	EReference getPlot_ClientArea();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.layout.TitleBlock <em>Title
	 * Block</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Title Block</em>'.
	 * @see org.eclipse.birt.chart.model.layout.TitleBlock
	 * @generated
	 */
	EClass getTitleBlock();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.layout.TitleBlock#isAuto
	 * <em>Auto</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Auto</em>'.
	 * @see org.eclipse.birt.chart.model.layout.TitleBlock#isAuto()
	 * @see #getTitleBlock()
	 * @generated
	 */
	EAttribute getTitleBlock_Auto();

	/**
	 * Returns the meta object for data type '<em>Ellipsis Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Ellipsis Type</em>'.
	 * @model instanceClass="int" extendedMetaData="name='Ellipsis_._type'
	 *        baseType='http://www.eclipse.org/emf/2003/XMLType#int'
	 *        minInclusive='0'"
	 * @generated
	 */
	EDataType getEllipsisType();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Ellipsis
	 * Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Ellipsis Type Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='Ellipsis_._type:Object'
	 *        baseType='Ellipsis_._type'"
	 * @generated
	 */
	EDataType getEllipsisTypeObject();

	/**
	 * Returns the meta object for data type '<em>Title Percent Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Title Percent Type</em>'.
	 * @model instanceClass="double" extendedMetaData="name='TitlePercent_._type'
	 *        baseType='http://www.eclipse.org/emf/2003/XMLType#double'
	 *        minInclusive='0' maxInclusive='1'"
	 * @generated
	 */
	EDataType getTitlePercentType();

	/**
	 * Returns the meta object for data type '{@link java.lang.Double <em>Title
	 * Percent Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Title Percent Type Object</em>'.
	 * @see java.lang.Double
	 * @model instanceClass="java.lang.Double"
	 *        extendedMetaData="name='TitlePercent_._type:Object'
	 *        baseType='TitlePercent_._type'"
	 * @generated
	 */
	EDataType getTitlePercentTypeObject();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	LayoutFactory getLayoutFactory();

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
		 * '{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl <em>Block</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.BlockImpl
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getBlock()
		 * @generated
		 */
		EClass BLOCK = eINSTANCE.getBlock();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__CHILDREN = eINSTANCE.getBlock_Children();

		/**
		 * The meta object literal for the '<em><b>Bounds</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__BOUNDS = eINSTANCE.getBlock_Bounds();

		/**
		 * The meta object literal for the '<em><b>Anchor</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__ANCHOR = eINSTANCE.getBlock_Anchor();

		/**
		 * The meta object literal for the '<em><b>Stretch</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__STRETCH = eINSTANCE.getBlock_Stretch();

		/**
		 * The meta object literal for the '<em><b>Insets</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__INSETS = eINSTANCE.getBlock_Insets();

		/**
		 * The meta object literal for the '<em><b>Row</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__ROW = eINSTANCE.getBlock_Row();

		/**
		 * The meta object literal for the '<em><b>Column</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__COLUMN = eINSTANCE.getBlock_Column();

		/**
		 * The meta object literal for the '<em><b>Rowspan</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__ROWSPAN = eINSTANCE.getBlock_Rowspan();

		/**
		 * The meta object literal for the '<em><b>Columnspan</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__COLUMNSPAN = eINSTANCE.getBlock_Columnspan();

		/**
		 * The meta object literal for the '<em><b>Min Size</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__MIN_SIZE = eINSTANCE.getBlock_MinSize();

		/**
		 * The meta object literal for the '<em><b>Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__OUTLINE = eINSTANCE.getBlock_Outline();

		/**
		 * The meta object literal for the '<em><b>Background</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__BACKGROUND = eINSTANCE.getBlock_Background();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__VISIBLE = eINSTANCE.getBlock_Visible();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__TRIGGERS = eINSTANCE.getBlock_Triggers();

		/**
		 * The meta object literal for the '<em><b>Width Hint</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__WIDTH_HINT = eINSTANCE.getBlock_WidthHint();

		/**
		 * The meta object literal for the '<em><b>Height Hint</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BLOCK__HEIGHT_HINT = eINSTANCE.getBlock_HeightHint();

		/**
		 * The meta object literal for the '<em><b>Cursor</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BLOCK__CURSOR = eINSTANCE.getBlock_Cursor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl <em>Client
		 * Area</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getClientArea()
		 * @generated
		 */
		EClass CLIENT_AREA = eINSTANCE.getClientArea();

		/**
		 * The meta object literal for the '<em><b>Background</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CLIENT_AREA__BACKGROUND = eINSTANCE.getClientArea_Background();

		/**
		 * The meta object literal for the '<em><b>Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CLIENT_AREA__OUTLINE = eINSTANCE.getClientArea_Outline();

		/**
		 * The meta object literal for the '<em><b>Shadow Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CLIENT_AREA__SHADOW_COLOR = eINSTANCE.getClientArea_ShadowColor();

		/**
		 * The meta object literal for the '<em><b>Insets</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CLIENT_AREA__INSETS = eINSTANCE.getClientArea_Insets();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CLIENT_AREA__VISIBLE = eINSTANCE.getClientArea_Visible();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl <em>Label
		 * Block</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getLabelBlock()
		 * @generated
		 */
		EClass LABEL_BLOCK = eINSTANCE.getLabelBlock();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LABEL_BLOCK__LABEL = eINSTANCE.getLabelBlock_Label();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl <em>Legend</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.LegendImpl
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getLegend()
		 * @generated
		 */
		EClass LEGEND = eINSTANCE.getLegend();

		/**
		 * The meta object literal for the '<em><b>Horizontal Spacing</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__HORIZONTAL_SPACING = eINSTANCE.getLegend_HorizontalSpacing();

		/**
		 * The meta object literal for the '<em><b>Vertical Spacing</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__VERTICAL_SPACING = eINSTANCE.getLegend_VerticalSpacing();

		/**
		 * The meta object literal for the '<em><b>Client Area</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LEGEND__CLIENT_AREA = eINSTANCE.getLegend_ClientArea();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LEGEND__TEXT = eINSTANCE.getLegend_Text();

		/**
		 * The meta object literal for the '<em><b>Orientation</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__ORIENTATION = eINSTANCE.getLegend_Orientation();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__DIRECTION = eINSTANCE.getLegend_Direction();

		/**
		 * The meta object literal for the '<em><b>Separator</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LEGEND__SEPARATOR = eINSTANCE.getLegend_Separator();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__POSITION = eINSTANCE.getLegend_Position();

		/**
		 * The meta object literal for the '<em><b>Item Type</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__ITEM_TYPE = eINSTANCE.getLegend_ItemType();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LEGEND__TITLE = eINSTANCE.getLegend_Title();

		/**
		 * The meta object literal for the '<em><b>Title Position</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__TITLE_POSITION = eINSTANCE.getLegend_TitlePosition();

		/**
		 * The meta object literal for the '<em><b>Show Value</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__SHOW_VALUE = eINSTANCE.getLegend_ShowValue();

		/**
		 * The meta object literal for the '<em><b>Show Percent</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__SHOW_PERCENT = eINSTANCE.getLegend_ShowPercent();

		/**
		 * The meta object literal for the '<em><b>Show Total</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__SHOW_TOTAL = eINSTANCE.getLegend_ShowTotal();

		/**
		 * The meta object literal for the '<em><b>Wrapping Size</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__WRAPPING_SIZE = eINSTANCE.getLegend_WrappingSize();

		/**
		 * The meta object literal for the '<em><b>Max Percent</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__MAX_PERCENT = eINSTANCE.getLegend_MaxPercent();

		/**
		 * The meta object literal for the '<em><b>Title Percent</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__TITLE_PERCENT = eINSTANCE.getLegend_TitlePercent();

		/**
		 * The meta object literal for the '<em><b>Ellipsis</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LEGEND__ELLIPSIS = eINSTANCE.getLegend_Ellipsis();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LEGEND__FORMAT_SPECIFIER = eINSTANCE.getLegend_FormatSpecifier();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl <em>Plot</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.PlotImpl
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getPlot()
		 * @generated
		 */
		EClass PLOT = eINSTANCE.getPlot();

		/**
		 * The meta object literal for the '<em><b>Horizontal Spacing</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PLOT__HORIZONTAL_SPACING = eINSTANCE.getPlot_HorizontalSpacing();

		/**
		 * The meta object literal for the '<em><b>Vertical Spacing</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PLOT__VERTICAL_SPACING = eINSTANCE.getPlot_VerticalSpacing();

		/**
		 * The meta object literal for the '<em><b>Client Area</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PLOT__CLIENT_AREA = eINSTANCE.getPlot_ClientArea();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl <em>Title
		 * Block</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitleBlock()
		 * @generated
		 */
		EClass TITLE_BLOCK = eINSTANCE.getTitleBlock();

		/**
		 * The meta object literal for the '<em><b>Auto</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TITLE_BLOCK__AUTO = eINSTANCE.getTitleBlock_Auto();

		/**
		 * The meta object literal for the '<em>Ellipsis Type</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getEllipsisType()
		 * @generated
		 */
		EDataType ELLIPSIS_TYPE = eINSTANCE.getEllipsisType();

		/**
		 * The meta object literal for the '<em>Ellipsis Type Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.Integer
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getEllipsisTypeObject()
		 * @generated
		 */
		EDataType ELLIPSIS_TYPE_OBJECT = eINSTANCE.getEllipsisTypeObject();

		/**
		 * The meta object literal for the '<em>Title Percent Type</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitlePercentType()
		 * @generated
		 */
		EDataType TITLE_PERCENT_TYPE = eINSTANCE.getTitlePercentType();

		/**
		 * The meta object literal for the '<em>Title Percent Type Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.Double
		 * @see org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl#getTitlePercentTypeObject()
		 * @generated
		 */
		EDataType TITLE_PERCENT_TYPE_OBJECT = eINSTANCE.getTitlePercentTypeObject();

	}

} // LayoutPackage
