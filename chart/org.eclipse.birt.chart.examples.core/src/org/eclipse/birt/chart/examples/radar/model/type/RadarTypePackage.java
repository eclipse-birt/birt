/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.radar.model.type;

import org.eclipse.birt.chart.model.component.ComponentPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
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
 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypeFactory
 * @model kind="package"
 * @generated
 */
public interface RadarTypePackage extends EPackage {
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
	String eNS_URI = "http://www.birt.eclipse.org/RadarChartModelType";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "type";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	RadarTypePackage eINSTANCE = org.eclipse.birt.chart.examples.radar.model.type.impl.RadarTypePackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl
	 * <em>Radar Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl
	 * @see org.eclipse.birt.chart.examples.radar.model.type.impl.RadarTypePackageImpl#getRadarSeries()
	 * @generated
	 */
	int RADAR_SERIES = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__VISIBLE = ComponentPackage.SERIES__VISIBLE;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__LABEL = ComponentPackage.SERIES__LABEL;

	/**
	 * The feature id for the '<em><b>Data Definition</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__DATA_DEFINITION = ComponentPackage.SERIES__DATA_DEFINITION;

	/**
	 * The feature id for the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__SERIES_IDENTIFIER = ComponentPackage.SERIES__SERIES_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__DATA_POINT = ComponentPackage.SERIES__DATA_POINT;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' map. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__DATA_SETS = ComponentPackage.SERIES__DATA_SETS;

	/**
	 * The feature id for the '<em><b>Label Position</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__LABEL_POSITION = ComponentPackage.SERIES__LABEL_POSITION;

	/**
	 * The feature id for the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__STACKED = ComponentPackage.SERIES__STACKED;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__TRIGGERS = ComponentPackage.SERIES__TRIGGERS;

	/**
	 * The feature id for the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__TRANSLUCENT = ComponentPackage.SERIES__TRANSLUCENT;

	/**
	 * The feature id for the '<em><b>Curve Fitting</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__CURVE_FITTING = ComponentPackage.SERIES__CURVE_FITTING;

	/**
	 * The feature id for the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__CURSOR = ComponentPackage.SERIES__CURSOR;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__MARKER = ComponentPackage.SERIES_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__PALETTE_LINE_COLOR = ComponentPackage.SERIES_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Background Oval Transparent</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT = ComponentPackage.SERIES_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Web Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__WEB_LINE_ATTRIBUTES = ComponentPackage.SERIES_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Show Web Labels</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__SHOW_WEB_LABELS = ComponentPackage.SERIES_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Show Cat Labels</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__SHOW_CAT_LABELS = ComponentPackage.SERIES_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Radar Auto Scale</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__RADAR_AUTO_SCALE = ComponentPackage.SERIES_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Web Label Max</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__WEB_LABEL_MAX = ComponentPackage.SERIES_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Web Label Min</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__WEB_LABEL_MIN = ComponentPackage.SERIES_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Web Label Unit</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__WEB_LABEL_UNIT = ComponentPackage.SERIES_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Fill Polys</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__FILL_POLYS = ComponentPackage.SERIES_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Connect Endpoints</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__CONNECT_ENDPOINTS = ComponentPackage.SERIES_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Web Label</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__WEB_LABEL = ComponentPackage.SERIES_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Cat Label</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__CAT_LABEL = ComponentPackage.SERIES_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Web Label Format Specifier</b></em>'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER = ComponentPackage.SERIES_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Cat Label Format Specifier</b></em>'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER = ComponentPackage.SERIES_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Plot Steps</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES__PLOT_STEPS = ComponentPackage.SERIES_FEATURE_COUNT + 17;

	/**
	 * The number of structural features of the '<em>Radar Series</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RADAR_SERIES_FEATURE_COUNT = ComponentPackage.SERIES_FEATURE_COUNT + 18;

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries
	 * <em>Radar Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Radar Series</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries
	 * @generated
	 */
	EClass getRadarSeries();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getMarker
	 * <em>Marker</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Marker</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getMarker()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_Marker();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getLineAttributes
	 * <em>Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getLineAttributes()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_LineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Palette Line Color</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isPaletteLineColor()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_PaletteLineColor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isBackgroundOvalTransparent
	 * <em>Background Oval Transparent</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Background Oval
	 *         Transparent</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isBackgroundOvalTransparent()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_BackgroundOvalTransparent();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLineAttributes
	 * <em>Web Line Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Web Line
	 *         Attributes</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLineAttributes()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_WebLineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowWebLabels
	 * <em>Show Web Labels</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show Web Labels</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowWebLabels()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_ShowWebLabels();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowCatLabels
	 * <em>Show Cat Labels</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Show Cat Labels</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowCatLabels()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_ShowCatLabels();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isRadarAutoScale
	 * <em>Radar Auto Scale</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Radar Auto Scale</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isRadarAutoScale()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_RadarAutoScale();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMax
	 * <em>Web Label Max</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Web Label Max</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMax()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_WebLabelMax();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMin
	 * <em>Web Label Min</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Web Label Min</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMin()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_WebLabelMin();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelUnit
	 * <em>Web Label Unit</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Web Label Unit</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelUnit()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_WebLabelUnit();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isFillPolys
	 * <em>Fill Polys</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Fill Polys</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isFillPolys()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_FillPolys();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isConnectEndpoints
	 * <em>Connect Endpoints</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Connect Endpoints</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isConnectEndpoints()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_ConnectEndpoints();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabel
	 * <em>Web Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Web Label</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabel()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_WebLabel();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabel
	 * <em>Cat Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cat Label</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabel()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_CatLabel();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelFormatSpecifier
	 * <em>Web Label Format Specifier</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Web Label Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelFormatSpecifier()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_WebLabelFormatSpecifier();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabelFormatSpecifier
	 * <em>Cat Label Format Specifier</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Cat Label Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabelFormatSpecifier()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EReference getRadarSeries_CatLabelFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getPlotSteps
	 * <em>Plot Steps</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Plot Steps</em>'.
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getPlotSteps()
	 * @see #getRadarSeries()
	 * @generated
	 */
	EAttribute getRadarSeries_PlotSteps();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RadarTypeFactory getRadarTypeFactory();

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
		 * '{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl
		 * <em>Radar Series</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl
		 * @see org.eclipse.birt.chart.examples.radar.model.type.impl.RadarTypePackageImpl#getRadarSeries()
		 * @generated
		 */
		EClass RADAR_SERIES = eINSTANCE.getRadarSeries();

		/**
		 * The meta object literal for the '<em><b>Marker</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__MARKER = eINSTANCE.getRadarSeries_Marker();

		/**
		 * The meta object literal for the '<em><b>Line Attributes</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__LINE_ATTRIBUTES = eINSTANCE.getRadarSeries_LineAttributes();

		/**
		 * The meta object literal for the '<em><b>Palette Line Color</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__PALETTE_LINE_COLOR = eINSTANCE.getRadarSeries_PaletteLineColor();

		/**
		 * The meta object literal for the '<em><b>Background Oval Transparent</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT = eINSTANCE.getRadarSeries_BackgroundOvalTransparent();

		/**
		 * The meta object literal for the '<em><b>Web Line Attributes</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__WEB_LINE_ATTRIBUTES = eINSTANCE.getRadarSeries_WebLineAttributes();

		/**
		 * The meta object literal for the '<em><b>Show Web Labels</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__SHOW_WEB_LABELS = eINSTANCE.getRadarSeries_ShowWebLabels();

		/**
		 * The meta object literal for the '<em><b>Show Cat Labels</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__SHOW_CAT_LABELS = eINSTANCE.getRadarSeries_ShowCatLabels();

		/**
		 * The meta object literal for the '<em><b>Radar Auto Scale</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__RADAR_AUTO_SCALE = eINSTANCE.getRadarSeries_RadarAutoScale();

		/**
		 * The meta object literal for the '<em><b>Web Label Max</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__WEB_LABEL_MAX = eINSTANCE.getRadarSeries_WebLabelMax();

		/**
		 * The meta object literal for the '<em><b>Web Label Min</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__WEB_LABEL_MIN = eINSTANCE.getRadarSeries_WebLabelMin();

		/**
		 * The meta object literal for the '<em><b>Web Label Unit</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__WEB_LABEL_UNIT = eINSTANCE.getRadarSeries_WebLabelUnit();

		/**
		 * The meta object literal for the '<em><b>Fill Polys</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__FILL_POLYS = eINSTANCE.getRadarSeries_FillPolys();

		/**
		 * The meta object literal for the '<em><b>Connect Endpoints</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__CONNECT_ENDPOINTS = eINSTANCE.getRadarSeries_ConnectEndpoints();

		/**
		 * The meta object literal for the '<em><b>Web Label</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__WEB_LABEL = eINSTANCE.getRadarSeries_WebLabel();

		/**
		 * The meta object literal for the '<em><b>Cat Label</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__CAT_LABEL = eINSTANCE.getRadarSeries_CatLabel();

		/**
		 * The meta object literal for the '<em><b>Web Label Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER = eINSTANCE.getRadarSeries_WebLabelFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Cat Label Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER = eINSTANCE.getRadarSeries_CatLabelFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Plot Steps</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RADAR_SERIES__PLOT_STEPS = eINSTANCE.getRadarSeries_PlotSteps();

	}

} // RadarTypePackage
