/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.model.type.impl;

import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.RadarTypeFactory;
import org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage;

import org.eclipse.birt.chart.model.ModelPackage;

import org.eclipse.birt.chart.model.attribute.AttributePackage;

import org.eclipse.birt.chart.model.component.ComponentPackage;

import org.eclipse.birt.chart.model.data.DataPackage;

import org.eclipse.birt.chart.model.layout.LayoutPackage;

import org.eclipse.birt.chart.model.type.TypePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class RadarTypePackageImpl extends EPackageImpl implements RadarTypePackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass radarSeriesEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method
	 * {@link #init init()}, which also performs initialization of the package, or
	 * returns the registered package, if one already exists. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RadarTypePackageImpl() {
		super(eNS_URI, RadarTypeFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and
	 * for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link RadarTypePackage#eINSTANCE} when
	 * that field is accessed. Clients should not invoke it directly. Instead, they
	 * should simply access that field to obtain the package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RadarTypePackage init() {
		if (isInited)
			return (RadarTypePackage) EPackage.Registry.INSTANCE.getEPackage(RadarTypePackage.eNS_URI);

		// Obtain or create and register package
		RadarTypePackageImpl theRadarTypePackage = (RadarTypePackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof RadarTypePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new RadarTypePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		AttributePackage.eINSTANCE.eClass();
		ComponentPackage.eINSTANCE.eClass();
		DataPackage.eINSTANCE.eClass();
		TypePackage.eINSTANCE.eClass();
		LayoutPackage.eINSTANCE.eClass();
		ModelPackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theRadarTypePackage.createPackageContents();

		// Initialize created meta-data
		theRadarTypePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRadarTypePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RadarTypePackage.eNS_URI, theRadarTypePackage);
		return theRadarTypePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getRadarSeries() {
		return radarSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_Marker() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_LineAttributes() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_PaletteLineColor() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_BackgroundOvalTransparent() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_WebLineAttributes() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_ShowWebLabels() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_ShowCatLabels() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_RadarAutoScale() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_WebLabelMax() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_WebLabelMin() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_WebLabelUnit() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_FillPolys() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_ConnectEndpoints() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_WebLabel() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_CatLabel() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_WebLabelFormatSpecifier() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRadarSeries_CatLabelFormatSpecifier() {
		return (EReference) radarSeriesEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRadarSeries_PlotSteps() {
		return (EAttribute) radarSeriesEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public RadarTypeFactory getRadarTypeFactory() {
		return (RadarTypeFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		radarSeriesEClass = createEClass(RADAR_SERIES);
		createEReference(radarSeriesEClass, RADAR_SERIES__MARKER);
		createEReference(radarSeriesEClass, RADAR_SERIES__LINE_ATTRIBUTES);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__PALETTE_LINE_COLOR);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT);
		createEReference(radarSeriesEClass, RADAR_SERIES__WEB_LINE_ATTRIBUTES);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__SHOW_WEB_LABELS);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__SHOW_CAT_LABELS);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__RADAR_AUTO_SCALE);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__WEB_LABEL_MAX);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__WEB_LABEL_MIN);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__WEB_LABEL_UNIT);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__FILL_POLYS);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__CONNECT_ENDPOINTS);
		createEReference(radarSeriesEClass, RADAR_SERIES__WEB_LABEL);
		createEReference(radarSeriesEClass, RADAR_SERIES__CAT_LABEL);
		createEReference(radarSeriesEClass, RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER);
		createEReference(radarSeriesEClass, RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER);
		createEAttribute(radarSeriesEClass, RADAR_SERIES__PLOT_STEPS);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is
	 * guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ComponentPackage theComponentPackage = (ComponentPackage) EPackage.Registry.INSTANCE
				.getEPackage(ComponentPackage.eNS_URI);
		AttributePackage theAttributePackage = (AttributePackage) EPackage.Registry.INSTANCE
				.getEPackage(AttributePackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage) EPackage.Registry.INSTANCE
				.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		radarSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());

		// Initialize classes and features; add operations and parameters
		initEClass(radarSeriesEClass, RadarSeries.class, "RadarSeries", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRadarSeries_Marker(), theAttributePackage.getMarker(), null, "marker", null, 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRadarSeries_LineAttributes(), theAttributePackage.getLineAttributes(), null, "lineAttributes",
				null, 0, 1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_PaletteLineColor(), theXMLTypePackage.getBoolean(), "paletteLineColor", null, 1,
				1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_BackgroundOvalTransparent(), theXMLTypePackage.getBoolean(),
				"backgroundOvalTransparent", null, 1, 1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRadarSeries_WebLineAttributes(), theAttributePackage.getLineAttributes(), null,
				"webLineAttributes", null, 0, 1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_ShowWebLabels(), theXMLTypePackage.getBoolean(), "showWebLabels", "false", 0, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_ShowCatLabels(), theXMLTypePackage.getBoolean(), "showCatLabels", "true", 0, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_RadarAutoScale(), theXMLTypePackage.getBoolean(), "radarAutoScale", null, 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_WebLabelMax(), theXMLTypePackage.getDouble(), "webLabelMax", "100", 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_WebLabelMin(), theXMLTypePackage.getDouble(), "webLabelMin", "0", 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_WebLabelUnit(), theXMLTypePackage.getString(), "webLabelUnit", "%", 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_FillPolys(), theXMLTypePackage.getBoolean(), "fillPolys", null, 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_ConnectEndpoints(), theXMLTypePackage.getBoolean(), "connectEndpoints", null, 1,
				1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getRadarSeries_WebLabel(), theComponentPackage.getLabel(), null, "webLabel", null, 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRadarSeries_CatLabel(), theComponentPackage.getLabel(), null, "catLabel", null, 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRadarSeries_WebLabelFormatSpecifier(), theAttributePackage.getFormatSpecifier(), null,
				"webLabelFormatSpecifier", null, 1, 1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRadarSeries_CatLabelFormatSpecifier(), theAttributePackage.getFormatSpecifier(), null,
				"catLabelFormatSpecifier", null, 1, 1, RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRadarSeries_PlotSteps(), theXMLTypePackage.getInteger(), "plotSteps", "5", 1, 1,
				RadarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for
	 * <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
		addAnnotation(radarSeriesEClass, source, new String[] { "name", "RadarSeries", "kind", "elementOnly" });
		addAnnotation(getRadarSeries_Marker(), source, new String[] { "kind", "element", "name", "Marker" });
		addAnnotation(getRadarSeries_LineAttributes(), source,
				new String[] { "kind", "element", "name", "LineAttributes" });
		addAnnotation(getRadarSeries_PaletteLineColor(), source,
				new String[] { "kind", "element", "name", "PaletteLineColor" });
		addAnnotation(getRadarSeries_BackgroundOvalTransparent(), source,
				new String[] { "kind", "element", "name", "BackgroundOvalTransparent" });
		addAnnotation(getRadarSeries_WebLineAttributes(), source,
				new String[] { "kind", "element", "name", "WebLineAttributes" });
		addAnnotation(getRadarSeries_ShowWebLabels(), source,
				new String[] { "kind", "element", "name", "ShowWebLabels" });
		addAnnotation(getRadarSeries_ShowCatLabels(), source,
				new String[] { "kind", "element", "name", "ShowCatLabels" });
		addAnnotation(getRadarSeries_RadarAutoScale(), source,
				new String[] { "kind", "element", "name", "RadarAutoScale" });
		addAnnotation(getRadarSeries_WebLabelMax(), source, new String[] { "kind", "element", "name", "WebLabelMax" });
		addAnnotation(getRadarSeries_WebLabelMin(), source, new String[] { "kind", "element", "name", "WebLabelMin" });
		addAnnotation(getRadarSeries_WebLabelUnit(), source,
				new String[] { "kind", "element", "name", "WebLabelUnit" });
		addAnnotation(getRadarSeries_FillPolys(), source, new String[] { "kind", "element", "name", "FillPolys" });
		addAnnotation(getRadarSeries_ConnectEndpoints(), source,
				new String[] { "kind", "element", "name", "ConnectEndpoints" });
		addAnnotation(getRadarSeries_WebLabel(), source, new String[] { "kind", "element", "name", "WebLabel" });
		addAnnotation(getRadarSeries_CatLabel(), source, new String[] { "kind", "element", "name", "CatLabel" });
		addAnnotation(getRadarSeries_WebLabelFormatSpecifier(), source,
				new String[] { "kind", "element", "name", "WebLabelFormatSpecifier" });
		addAnnotation(getRadarSeries_CatLabelFormatSpecifier(), source,
				new String[] { "kind", "element", "name", "CatLabelFormatSpecifier" });
		addAnnotation(getRadarSeries_PlotSteps(), source, new String[] { "kind", "element", "name", "PlotSteps" });
	}

} // RadarTypePackageImpl
