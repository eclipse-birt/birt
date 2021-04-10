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

package org.eclipse.birt.chart.model.type.impl;

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.impl.DataPackageImpl;
import org.eclipse.birt.chart.model.impl.ModelPackageImpl;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class TypePackageImpl extends EPackageImpl implements TypePackage {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass areaSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass barSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass bubbleSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dialSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass differenceSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass ganttSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass lineSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass pieSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass scatterSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass stockSeriesEClass = null;

	/**
	 * Creates an instance of the model <b>Package </b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry}by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method
	 * {@link #init init()}, which also performs initialization of the package, or
	 * returns the registered package, if one already exists. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.birt.chart.model.type.TypePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TypePackageImpl() {
		super(eNS_URI, TypeFactory.eINSTANCE);
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
	 * This method is used to initialize {@link TypePackage#eINSTANCE} when that
	 * field is accessed. Clients should not invoke it directly. Instead, they
	 * should simply access that field to obtain the package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TypePackage init() {
		if (isInited)
			return (TypePackage) EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);

		// Obtain or create and register package
		TypePackageImpl theTypePackage = (TypePackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new TypePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(AttributePackage.eNS_URI) instanceof AttributePackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(AttributePackage.eNS_URI)
						: AttributePackage.eINSTANCE);
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(ComponentPackage.eNS_URI) instanceof ComponentPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(ComponentPackage.eNS_URI)
						: ComponentPackage.eINSTANCE);
		DataPackageImpl theDataPackage = (DataPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(DataPackage.eNS_URI) instanceof DataPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI)
						: DataPackage.eINSTANCE);
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(LayoutPackage.eNS_URI) instanceof LayoutPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(LayoutPackage.eNS_URI)
						: LayoutPackage.eINSTANCE);
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI)
						: ModelPackage.eINSTANCE);

		// Create package meta-data objects
		theTypePackage.createPackageContents();
		theAttributePackage.createPackageContents();
		theComponentPackage.createPackageContents();
		theDataPackage.createPackageContents();
		theLayoutPackage.createPackageContents();
		theModelPackage.createPackageContents();

		// Initialize created meta-data
		theTypePackage.initializePackageContents();
		theAttributePackage.initializePackageContents();
		theComponentPackage.initializePackageContents();
		theDataPackage.initializePackageContents();
		theLayoutPackage.initializePackageContents();
		theModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTypePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TypePackage.eNS_URI, theTypePackage);
		return theTypePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAreaSeries() {
		return areaSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBarSeries() {
		return barSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBarSeries_Riser() {
		return (EAttribute) barSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBarSeries_RiserOutline() {
		return (EReference) barSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBubbleSeries() {
		return bubbleSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBubbleSeries_AccLineAttributes() {
		return (EReference) bubbleSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBubbleSeries_AccOrientation() {
		return (EAttribute) bubbleSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDialSeries() {
		return dialSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDialSeries_Dial() {
		return (EReference) dialSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDialSeries_Needle() {
		return (EReference) dialSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDifferenceSeries() {
		return differenceSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDifferenceSeries_NegativeMarkers() {
		return (EReference) differenceSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDifferenceSeries_NegativeLineAttributes() {
		return (EReference) differenceSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getGanttSeries() {
		return ganttSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGanttSeries_StartMarker() {
		return (EReference) ganttSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGanttSeries_StartMarkerPosition() {
		return (EAttribute) ganttSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGanttSeries_EndMarker() {
		return (EReference) ganttSeriesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGanttSeries_EndMarkerPosition() {
		return (EAttribute) ganttSeriesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGanttSeries_ConnectionLine() {
		return (EReference) ganttSeriesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGanttSeries_Outline() {
		return (EReference) ganttSeriesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGanttSeries_OutlineFill() {
		return (EReference) ganttSeriesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGanttSeries_UseDecorationLabelValue() {
		return (EAttribute) ganttSeriesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGanttSeries_DecorationLabel() {
		return (EReference) ganttSeriesEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGanttSeries_DecorationLabelPosition() {
		return (EAttribute) ganttSeriesEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGanttSeries_PaletteLineColor() {
		return (EAttribute) ganttSeriesEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLineSeries() {
		return lineSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLineSeries_Markers() {
		return (EReference) lineSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLineSeries_Marker() {
		return (EReference) lineSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLineSeries_LineAttributes() {
		return (EReference) lineSeriesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLineSeries_PaletteLineColor() {
		return (EAttribute) lineSeriesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLineSeries_Curve() {
		return (EAttribute) lineSeriesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLineSeries_ShadowColor() {
		return (EReference) lineSeriesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLineSeries_ConnectMissingValue() {
		return (EAttribute) lineSeriesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getPieSeries() {
		return pieSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_Explosion() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_ExplosionExpression() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPieSeries_Title() {
		return (EReference) pieSeriesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_TitlePosition() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPieSeries_LeaderLineAttributes() {
		return (EReference) pieSeriesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_LeaderLineStyle() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_LeaderLineLength() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPieSeries_SliceOutline() {
		return (EReference) pieSeriesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_Ratio() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_Rotation() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_Clockwise() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_InnerRadius() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPieSeries_InnerRadiusPercent() {
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getScatterSeries() {
		return scatterSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getStockSeries() {
		return stockSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getStockSeries_Fill() {
		return (EReference) stockSeriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getStockSeries_LineAttributes() {
		return (EReference) stockSeriesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getStockSeries_ShowAsBarStick() {
		return (EAttribute) stockSeriesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getStockSeries_StickLength() {
		return (EAttribute) stockSeriesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypeFactory getTypeFactory() {
		return (TypeFactory) getEFactoryInstance();
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
		areaSeriesEClass = createEClass(AREA_SERIES);

		barSeriesEClass = createEClass(BAR_SERIES);
		createEAttribute(barSeriesEClass, BAR_SERIES__RISER);
		createEReference(barSeriesEClass, BAR_SERIES__RISER_OUTLINE);

		bubbleSeriesEClass = createEClass(BUBBLE_SERIES);
		createEReference(bubbleSeriesEClass, BUBBLE_SERIES__ACC_LINE_ATTRIBUTES);
		createEAttribute(bubbleSeriesEClass, BUBBLE_SERIES__ACC_ORIENTATION);

		dialSeriesEClass = createEClass(DIAL_SERIES);
		createEReference(dialSeriesEClass, DIAL_SERIES__DIAL);
		createEReference(dialSeriesEClass, DIAL_SERIES__NEEDLE);

		differenceSeriesEClass = createEClass(DIFFERENCE_SERIES);
		createEReference(differenceSeriesEClass, DIFFERENCE_SERIES__NEGATIVE_MARKERS);
		createEReference(differenceSeriesEClass, DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES);

		ganttSeriesEClass = createEClass(GANTT_SERIES);
		createEReference(ganttSeriesEClass, GANTT_SERIES__START_MARKER);
		createEAttribute(ganttSeriesEClass, GANTT_SERIES__START_MARKER_POSITION);
		createEReference(ganttSeriesEClass, GANTT_SERIES__END_MARKER);
		createEAttribute(ganttSeriesEClass, GANTT_SERIES__END_MARKER_POSITION);
		createEReference(ganttSeriesEClass, GANTT_SERIES__CONNECTION_LINE);
		createEReference(ganttSeriesEClass, GANTT_SERIES__OUTLINE);
		createEReference(ganttSeriesEClass, GANTT_SERIES__OUTLINE_FILL);
		createEAttribute(ganttSeriesEClass, GANTT_SERIES__USE_DECORATION_LABEL_VALUE);
		createEReference(ganttSeriesEClass, GANTT_SERIES__DECORATION_LABEL);
		createEAttribute(ganttSeriesEClass, GANTT_SERIES__DECORATION_LABEL_POSITION);
		createEAttribute(ganttSeriesEClass, GANTT_SERIES__PALETTE_LINE_COLOR);

		lineSeriesEClass = createEClass(LINE_SERIES);
		createEReference(lineSeriesEClass, LINE_SERIES__MARKERS);
		createEReference(lineSeriesEClass, LINE_SERIES__MARKER);
		createEReference(lineSeriesEClass, LINE_SERIES__LINE_ATTRIBUTES);
		createEAttribute(lineSeriesEClass, LINE_SERIES__PALETTE_LINE_COLOR);
		createEAttribute(lineSeriesEClass, LINE_SERIES__CURVE);
		createEReference(lineSeriesEClass, LINE_SERIES__SHADOW_COLOR);
		createEAttribute(lineSeriesEClass, LINE_SERIES__CONNECT_MISSING_VALUE);

		pieSeriesEClass = createEClass(PIE_SERIES);
		createEAttribute(pieSeriesEClass, PIE_SERIES__EXPLOSION);
		createEAttribute(pieSeriesEClass, PIE_SERIES__EXPLOSION_EXPRESSION);
		createEReference(pieSeriesEClass, PIE_SERIES__TITLE);
		createEAttribute(pieSeriesEClass, PIE_SERIES__TITLE_POSITION);
		createEReference(pieSeriesEClass, PIE_SERIES__LEADER_LINE_ATTRIBUTES);
		createEAttribute(pieSeriesEClass, PIE_SERIES__LEADER_LINE_STYLE);
		createEAttribute(pieSeriesEClass, PIE_SERIES__LEADER_LINE_LENGTH);
		createEReference(pieSeriesEClass, PIE_SERIES__SLICE_OUTLINE);
		createEAttribute(pieSeriesEClass, PIE_SERIES__RATIO);
		createEAttribute(pieSeriesEClass, PIE_SERIES__ROTATION);
		createEAttribute(pieSeriesEClass, PIE_SERIES__CLOCKWISE);
		createEAttribute(pieSeriesEClass, PIE_SERIES__INNER_RADIUS);
		createEAttribute(pieSeriesEClass, PIE_SERIES__INNER_RADIUS_PERCENT);

		scatterSeriesEClass = createEClass(SCATTER_SERIES);

		stockSeriesEClass = createEClass(STOCK_SERIES);
		createEReference(stockSeriesEClass, STOCK_SERIES__FILL);
		createEReference(stockSeriesEClass, STOCK_SERIES__LINE_ATTRIBUTES);
		createEAttribute(stockSeriesEClass, STOCK_SERIES__SHOW_AS_BAR_STICK);
		createEAttribute(stockSeriesEClass, STOCK_SERIES__STICK_LENGTH);
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
		areaSeriesEClass.getESuperTypes().add(this.getLineSeries());
		barSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());
		bubbleSeriesEClass.getESuperTypes().add(this.getScatterSeries());
		dialSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());
		differenceSeriesEClass.getESuperTypes().add(this.getAreaSeries());
		ganttSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());
		lineSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());
		pieSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());
		scatterSeriesEClass.getESuperTypes().add(this.getLineSeries());
		stockSeriesEClass.getESuperTypes().add(theComponentPackage.getSeries());

		// Initialize classes and features; add operations and parameters
		initEClass(areaSeriesEClass, AreaSeries.class, "AreaSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(barSeriesEClass, BarSeries.class, "BarSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBarSeries_Riser(), theAttributePackage.getRiserType(), "riser", "Rectangle", 0, 1, //$NON-NLS-1$ //$NON-NLS-2$
				BarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getBarSeries_RiserOutline(), theAttributePackage.getColorDefinition(), null, "riserOutline", //$NON-NLS-1$
				null, 0, 1, BarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(bubbleSeriesEClass, BubbleSeries.class, "BubbleSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBubbleSeries_AccLineAttributes(), theAttributePackage.getLineAttributes(), null,
				"accLineAttributes", null, 0, 1, BubbleSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBubbleSeries_AccOrientation(), theAttributePackage.getOrientation(), "accOrientation", //$NON-NLS-1$
				"Horizontal", 0, 1, BubbleSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, //$NON-NLS-1$
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dialSeriesEClass, DialSeries.class, "DialSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDialSeries_Dial(), theComponentPackage.getDial(), null, "dial", null, 1, 1, DialSeries.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDialSeries_Needle(), theComponentPackage.getNeedle(), null, "needle", null, 1, 1, //$NON-NLS-1$
				DialSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(differenceSeriesEClass, DifferenceSeries.class, "DifferenceSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDifferenceSeries_NegativeMarkers(), theAttributePackage.getMarker(), null, "negativeMarkers", //$NON-NLS-1$
				null, 0, -1, DifferenceSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDifferenceSeries_NegativeLineAttributes(), theAttributePackage.getLineAttributes(), null,
				"negativeLineAttributes", null, 0, 1, DifferenceSeries.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ganttSeriesEClass, GanttSeries.class, "GanttSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGanttSeries_StartMarker(), theAttributePackage.getMarker(), null, "startMarker", null, 1, 1, //$NON-NLS-1$
				GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGanttSeries_StartMarkerPosition(), theAttributePackage.getPosition(), "startMarkerPosition", //$NON-NLS-1$
				"Above", 1, 1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, //$NON-NLS-1$
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGanttSeries_EndMarker(), theAttributePackage.getMarker(), null, "endMarker", null, 1, 1, //$NON-NLS-1$
				GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGanttSeries_EndMarkerPosition(), theAttributePackage.getPosition(), "endMarkerPosition", //$NON-NLS-1$
				"Above", 1, 1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, //$NON-NLS-1$
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGanttSeries_ConnectionLine(), theAttributePackage.getLineAttributes(), null, "connectionLine", //$NON-NLS-1$
				null, 1, 1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGanttSeries_Outline(), theAttributePackage.getLineAttributes(), null, "outline", null, 1, 1, //$NON-NLS-1$
				GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGanttSeries_OutlineFill(), theAttributePackage.getFill(), null, "outlineFill", null, 0, 1, //$NON-NLS-1$
				GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGanttSeries_UseDecorationLabelValue(), theXMLTypePackage.getBoolean(),
				"useDecorationLabelValue", null, 1, 1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
				IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGanttSeries_DecorationLabel(), theComponentPackage.getLabel(), null, "decorationLabel", null, //$NON-NLS-1$
				1, 1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGanttSeries_DecorationLabelPosition(), theAttributePackage.getPosition(),
				"decorationLabelPosition", "Above", 1, 1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$ //$NON-NLS-2$
				IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGanttSeries_PaletteLineColor(), theXMLTypePackage.getBoolean(), "paletteLineColor", null, 1, //$NON-NLS-1$
				1, GanttSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(lineSeriesEClass, LineSeries.class, "LineSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLineSeries_Markers(), theAttributePackage.getMarker(), null, "markers", null, 0, -1, //$NON-NLS-1$
				LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLineSeries_Marker(), theAttributePackage.getMarker(), null, "marker", null, 0, 1, //$NON-NLS-1$
				LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLineSeries_LineAttributes(), theAttributePackage.getLineAttributes(), null, "lineAttributes", //$NON-NLS-1$
				null, 0, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLineSeries_PaletteLineColor(), theXMLTypePackage.getBoolean(), "paletteLineColor", null, 1, 1, //$NON-NLS-1$
				LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getLineSeries_Curve(), theXMLTypePackage.getBoolean(), "curve", null, 1, 1, LineSeries.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLineSeries_ShadowColor(), theAttributePackage.getColorDefinition(), null, "shadowColor", null, //$NON-NLS-1$
				1, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLineSeries_ConnectMissingValue(), theXMLTypePackage.getBoolean(), "connectMissingValue", //$NON-NLS-1$
				"true", 0, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, //$NON-NLS-1$
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pieSeriesEClass, PieSeries.class, "PieSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPieSeries_Explosion(), theXMLTypePackage.getInt(), "explosion", null, 0, 1, PieSeries.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_ExplosionExpression(), theXMLTypePackage.getString(), "explosionExpression", null, //$NON-NLS-1$
				0, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getPieSeries_Title(), theComponentPackage.getLabel(), null, "title", null, 1, 1, PieSeries.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_TitlePosition(), theAttributePackage.getPosition(), "titlePosition", "Above", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getPieSeries_LeaderLineAttributes(), theAttributePackage.getLineAttributes(), null,
				"leaderLineAttributes", null, 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_LeaderLineStyle(), theAttributePackage.getLeaderLineStyle(), "leaderLineStyle", //$NON-NLS-1$
				"Fixed_Length", 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, //$NON-NLS-1$
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_LeaderLineLength(), theAttributePackage.getPercentage(), "leaderLineLength", null, //$NON-NLS-1$
				1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getPieSeries_SliceOutline(), theAttributePackage.getColorDefinition(), null, "sliceOutline", //$NON-NLS-1$
				null, 0, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_Ratio(), theXMLTypePackage.getDouble(), "ratio", "1", 1, 1, PieSeries.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_Rotation(), theXMLTypePackage.getDouble(), "rotation", "0", 1, 1, PieSeries.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_Clockwise(), theXMLTypePackage.getBoolean(), "clockwise", "false", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_InnerRadius(), theXMLTypePackage.getDouble(), "innerRadius", "0", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getPieSeries_InnerRadiusPercent(), theXMLTypePackage.getBoolean(), "innerRadiusPercent", "true", //$NON-NLS-1$ //$NON-NLS-2$
				1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(scatterSeriesEClass, ScatterSeries.class, "ScatterSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(stockSeriesEClass, StockSeries.class, "StockSeries", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStockSeries_Fill(), theAttributePackage.getFill(), null, "fill", null, 0, 1, //$NON-NLS-1$
				StockSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStockSeries_LineAttributes(), theAttributePackage.getLineAttributes(), null, "lineAttributes", //$NON-NLS-1$
				null, 0, 1, StockSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStockSeries_ShowAsBarStick(), theXMLTypePackage.getBoolean(), "showAsBarStick", "false", 0, 1, //$NON-NLS-1$ //$NON-NLS-2$
				StockSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getStockSeries_StickLength(), theXMLTypePackage.getInt(), "stickLength", "5", 0, 1, //$NON-NLS-1$ //$NON-NLS-2$
				StockSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for
	 * <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData </b>. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
		addAnnotation(areaSeriesEClass, source, new String[] { "name", "AreaSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(barSeriesEClass, source, new String[] { "name", "BarSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBarSeries_Riser(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Riser" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBarSeries_RiserOutline(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "RiserOutline" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(bubbleSeriesEClass, source, new String[] { "name", "BubbleSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBubbleSeries_AccLineAttributes(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "AccLineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBubbleSeries_AccOrientation(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "AccOrientation" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(dialSeriesEClass, source, new String[] { "name", "DialSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getDialSeries_Dial(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Dial" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getDialSeries_Needle(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Needle" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(differenceSeriesEClass, source, new String[] { "name", "DifferenceSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getDifferenceSeries_NegativeMarkers(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "NegativeMarkers" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getDifferenceSeries_NegativeLineAttributes(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "NegativeLineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(ganttSeriesEClass, source, new String[] { "name", "GanttSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_StartMarker(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "StartMarker" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_StartMarkerPosition(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "StartMarkerPosition" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_EndMarker(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "EndMarker" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_EndMarkerPosition(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "EndMarkerPosition" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_ConnectionLine(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ConnectionLine" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_Outline(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Outline" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_OutlineFill(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "OutlineFill" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_UseDecorationLabelValue(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "UseDecorationLabelValue" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_DecorationLabel(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "DecorationLabel" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_DecorationLabelPosition(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "DecorationLabelPosition" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getGanttSeries_PaletteLineColor(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "PaletteLineColor" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(lineSeriesEClass, source, new String[] { "name", "LineSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_Markers(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Markers" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_Marker(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Marker" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_LineAttributes(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_PaletteLineColor(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "PaletteLineColor" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_Curve(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Curve" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_ShadowColor(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShadowColor" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLineSeries_ConnectMissingValue(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ConnectMissingValue" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(pieSeriesEClass, source, new String[] { "name", "PieSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_Explosion(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Explosion" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_ExplosionExpression(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ExplosionExpression" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_Title(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Title" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_TitlePosition(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "TitlePosition" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_LeaderLineAttributes(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LeaderLineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_LeaderLineStyle(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LeaderLineStyle" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_LeaderLineLength(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LeaderLineLength" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_SliceOutline(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "SliceOutline" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_Ratio(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Ratio" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_Rotation(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Rotation" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_Clockwise(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Clockwise" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_InnerRadius(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "InnerRadius" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPieSeries_InnerRadiusPercent(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "InnerRadiusPercent" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(scatterSeriesEClass, source, new String[] { "name", "ScatterSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(stockSeriesEClass, source, new String[] { "name", "StockSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getStockSeries_Fill(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Fill" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getStockSeries_LineAttributes(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getStockSeries_ShowAsBarStick(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShowAsBarStick" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getStockSeries_StickLength(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "StickLength" //$NON-NLS-1$ //$NON-NLS-2$
		});
	}

} // TypePackageImpl
