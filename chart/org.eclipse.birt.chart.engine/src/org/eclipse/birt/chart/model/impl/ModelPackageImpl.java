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

package org.eclipse.birt.chart.model.impl;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.impl.DataPackageImpl;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.type.impl.TypePackageImpl;
import org.eclipse.birt.chart.model.util.ModelValidator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ModelPackageImpl extends EPackageImpl implements ModelPackage {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass chartEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass chartWithAxesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass chartWithoutAxesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dialChartEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType coverageTypeEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType coverageTypeObjectEDataType = null;

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
	 * @see org.eclipse.birt.chart.model.ModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModelPackageImpl() {
		super(eNS_URI, ModelFactory.eINSTANCE);
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
	 * This method is used to initialize {@link ModelPackage#eINSTANCE} when that
	 * field is accessed. Clients should not invoke it directly. Instead, they
	 * should simply access that field to obtain the package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ModelPackage init() {
		if (isInited)
			return (ModelPackage) EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

		// Obtain or create and register package
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new ModelPackageImpl());

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
		TypePackageImpl theTypePackage = (TypePackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(TypePackage.eNS_URI) instanceof TypePackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI)
						: TypePackage.eINSTANCE);
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(LayoutPackage.eNS_URI) instanceof LayoutPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(LayoutPackage.eNS_URI)
						: LayoutPackage.eINSTANCE);

		// Create package meta-data objects
		theModelPackage.createPackageContents();
		theAttributePackage.createPackageContents();
		theComponentPackage.createPackageContents();
		theDataPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theLayoutPackage.createPackageContents();

		// Initialize created meta-data
		theModelPackage.initializePackageContents();
		theAttributePackage.initializePackageContents();
		theComponentPackage.initializePackageContents();
		theDataPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theLayoutPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put(theModelPackage, new EValidator.Descriptor() {

			public EValidator getEValidator() {
				return ModelValidator.INSTANCE;
			}
		});

		// Mark meta-data to indicate it can't be changed
		theModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, theModelPackage);
		return theModelPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getChart() {
		return chartEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_Version() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_Type() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_SubType() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_Description() {
		return (EReference) chartEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_Block() {
		return (EReference) chartEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_Dimension() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_Script() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_Units() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_SeriesThickness() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChart_GridColumnCount() {
		return (EAttribute) chartEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_ExtendedProperties() {
		return (EReference) chartEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_SampleData() {
		return (EReference) chartEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_Styles() {
		return (EReference) chartEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_Interactivity() {
		return (EReference) chartEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChart_EmptyMessage() {
		return (EReference) chartEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getChartWithAxes() {
		return chartWithAxesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartWithAxes_Axes() {
		return (EReference) chartWithAxesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartWithAxes_WallFill() {
		return (EReference) chartWithAxesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartWithAxes_FloorFill() {
		return (EReference) chartWithAxesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithAxes_Orientation() {
		return (EAttribute) chartWithAxesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithAxes_UnitSpacing() {
		return (EAttribute) chartWithAxesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartWithAxes_Rotation() {
		return (EReference) chartWithAxesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithAxes_ReverseCategory() {
		return (EAttribute) chartWithAxesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithAxes_StudyLayout() {
		return (EAttribute) chartWithAxesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getChartWithoutAxes() {
		return chartWithoutAxesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartWithoutAxes_SeriesDefinitions() {
		return (EReference) chartWithoutAxesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithoutAxes_MinSlice() {
		return (EAttribute) chartWithoutAxesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithoutAxes_MinSlicePercent() {
		return (EAttribute) chartWithoutAxesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithoutAxes_MinSliceLabel() {
		return (EAttribute) chartWithoutAxesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getChartWithoutAxes_Coverage() {
		return (EAttribute) chartWithoutAxesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDialChart() {
		return dialChartEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDialChart_DialSuperimposition() {
		return (EAttribute) dialChartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getCoverageType() {
		return coverageTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getCoverageTypeObject() {
		return coverageTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelFactory getModelFactory() {
		return (ModelFactory) getEFactoryInstance();
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
		chartEClass = createEClass(CHART);
		createEAttribute(chartEClass, CHART__VERSION);
		createEAttribute(chartEClass, CHART__TYPE);
		createEAttribute(chartEClass, CHART__SUB_TYPE);
		createEReference(chartEClass, CHART__DESCRIPTION);
		createEReference(chartEClass, CHART__BLOCK);
		createEAttribute(chartEClass, CHART__DIMENSION);
		createEAttribute(chartEClass, CHART__SCRIPT);
		createEAttribute(chartEClass, CHART__UNITS);
		createEAttribute(chartEClass, CHART__SERIES_THICKNESS);
		createEAttribute(chartEClass, CHART__GRID_COLUMN_COUNT);
		createEReference(chartEClass, CHART__EXTENDED_PROPERTIES);
		createEReference(chartEClass, CHART__SAMPLE_DATA);
		createEReference(chartEClass, CHART__STYLES);
		createEReference(chartEClass, CHART__INTERACTIVITY);
		createEReference(chartEClass, CHART__EMPTY_MESSAGE);

		chartWithAxesEClass = createEClass(CHART_WITH_AXES);
		createEReference(chartWithAxesEClass, CHART_WITH_AXES__AXES);
		createEReference(chartWithAxesEClass, CHART_WITH_AXES__WALL_FILL);
		createEReference(chartWithAxesEClass, CHART_WITH_AXES__FLOOR_FILL);
		createEAttribute(chartWithAxesEClass, CHART_WITH_AXES__ORIENTATION);
		createEAttribute(chartWithAxesEClass, CHART_WITH_AXES__UNIT_SPACING);
		createEReference(chartWithAxesEClass, CHART_WITH_AXES__ROTATION);
		createEAttribute(chartWithAxesEClass, CHART_WITH_AXES__REVERSE_CATEGORY);
		createEAttribute(chartWithAxesEClass, CHART_WITH_AXES__STUDY_LAYOUT);

		chartWithoutAxesEClass = createEClass(CHART_WITHOUT_AXES);
		createEReference(chartWithoutAxesEClass, CHART_WITHOUT_AXES__SERIES_DEFINITIONS);
		createEAttribute(chartWithoutAxesEClass, CHART_WITHOUT_AXES__MIN_SLICE);
		createEAttribute(chartWithoutAxesEClass, CHART_WITHOUT_AXES__MIN_SLICE_PERCENT);
		createEAttribute(chartWithoutAxesEClass, CHART_WITHOUT_AXES__MIN_SLICE_LABEL);
		createEAttribute(chartWithoutAxesEClass, CHART_WITHOUT_AXES__COVERAGE);

		dialChartEClass = createEClass(DIAL_CHART);
		createEAttribute(dialChartEClass, DIAL_CHART__DIAL_SUPERIMPOSITION);

		// Create data types
		coverageTypeEDataType = createEDataType(COVERAGE_TYPE);
		coverageTypeObjectEDataType = createEDataType(COVERAGE_TYPE_OBJECT);
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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage) EPackage.Registry.INSTANCE
				.getEPackage(XMLTypePackage.eNS_URI);
		AttributePackage theAttributePackage = (AttributePackage) EPackage.Registry.INSTANCE
				.getEPackage(AttributePackage.eNS_URI);
		LayoutPackage theLayoutPackage = (LayoutPackage) EPackage.Registry.INSTANCE.getEPackage(LayoutPackage.eNS_URI);
		DataPackage theDataPackage = (DataPackage) EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI);
		ComponentPackage theComponentPackage = (ComponentPackage) EPackage.Registry.INSTANCE
				.getEPackage(ComponentPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		chartWithAxesEClass.getESuperTypes().add(this.getChart());
		chartWithoutAxesEClass.getESuperTypes().add(this.getChart());
		dialChartEClass.getESuperTypes().add(this.getChartWithoutAxes());

		// Initialize classes and features; add operations and parameters
		initEClass(chartEClass, Chart.class, "Chart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getChart_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, Chart.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_Type(), theXMLTypePackage.getString(), "type", null, 1, 1, Chart.class, !IS_TRANSIENT, //$NON-NLS-1$
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_SubType(), theXMLTypePackage.getString(), "subType", null, 1, 1, Chart.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_Description(), theAttributePackage.getText(), null, "description", null, 0, 1, //$NON-NLS-1$
				Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_Block(), theLayoutPackage.getBlock(), null, "block", null, 1, 1, Chart.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_Dimension(), theAttributePackage.getChartDimension(), "dimension", "Two_Dimensional", 1, //$NON-NLS-1$ //$NON-NLS-2$
				1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_Script(), theXMLTypePackage.getString(), "script", null, 1, 1, Chart.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_Units(), theXMLTypePackage.getString(), "units", null, 0, 1, Chart.class, !IS_TRANSIENT, //$NON-NLS-1$
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_SeriesThickness(), theXMLTypePackage.getDouble(), "seriesThickness", "10", 0, 1, //$NON-NLS-1$ //$NON-NLS-2$
				Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getChart_GridColumnCount(), theXMLTypePackage.getInt(), "gridColumnCount", null, 1, 1, //$NON-NLS-1$
				Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getChart_ExtendedProperties(), theAttributePackage.getExtendedProperty(), null,
				"extendedProperties", null, 1, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_SampleData(), theDataPackage.getSampleData(), null, "sampleData", null, 0, 1, //$NON-NLS-1$
				Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_Styles(), theAttributePackage.getStyleMap(), null, "styles", null, 0, -1, Chart.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_Interactivity(), theAttributePackage.getInteractivity(), null, "interactivity", null, 1, //$NON-NLS-1$
				1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_EmptyMessage(), theComponentPackage.getLabel(), null, "emptyMessage", null, 0, 1, //$NON-NLS-1$
				Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(chartWithAxesEClass, ChartWithAxes.class, "ChartWithAxes", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChartWithAxes_Axes(), theComponentPackage.getAxis(), null, "axes", null, 1, -1, //$NON-NLS-1$
				ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChartWithAxes_WallFill(), theAttributePackage.getFill(), null, "wallFill", null, 1, 1, //$NON-NLS-1$
				ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChartWithAxes_FloorFill(), theAttributePackage.getFill(), null, "floorFill", null, 1, 1, //$NON-NLS-1$
				ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithAxes_Orientation(), theAttributePackage.getOrientation(), "orientation", "Vertical", //$NON-NLS-1$ //$NON-NLS-2$
				1, 1, ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithAxes_UnitSpacing(), theAttributePackage.getPercentage(), "unitSpacing", "50", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getChartWithAxes_Rotation(), theAttributePackage.getRotation3D(), null, "rotation", null, 0, 1, //$NON-NLS-1$
				ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithAxes_ReverseCategory(), theXMLTypePackage.getBoolean(), "reverseCategory", "false", //$NON-NLS-1$ //$NON-NLS-2$
				1, 1, ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithAxes_StudyLayout(), theXMLTypePackage.getBoolean(), "studyLayout", null, 1, 1, //$NON-NLS-1$
				ChartWithAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(chartWithoutAxesEClass, ChartWithoutAxes.class, "ChartWithoutAxes", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChartWithoutAxes_SeriesDefinitions(), theDataPackage.getSeriesDefinition(), null,
				"seriesDefinitions", null, 1, -1, ChartWithoutAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithoutAxes_MinSlice(), theXMLTypePackage.getDouble(), "minSlice", null, 0, 1, //$NON-NLS-1$
				ChartWithoutAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithoutAxes_MinSlicePercent(), theXMLTypePackage.getBoolean(), "minSlicePercent", null, //$NON-NLS-1$
				1, 1, ChartWithoutAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithoutAxes_MinSliceLabel(), theXMLTypePackage.getString(), "minSliceLabel", null, 1, 1, //$NON-NLS-1$
				ChartWithoutAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getChartWithoutAxes_Coverage(), this.getCoverageType(), "coverage", null, 1, 1, //$NON-NLS-1$
				ChartWithoutAxes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(dialChartEClass, DialChart.class, "DialChart", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDialChart_DialSuperimposition(), theXMLTypePackage.getBoolean(), "dialSuperimposition", //$NON-NLS-1$
				"true", 0, 1, DialChart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, //$NON-NLS-1$
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(coverageTypeEDataType, double.class, "CoverageType", IS_SERIALIZABLE, //$NON-NLS-1$
				!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(coverageTypeObjectEDataType, Double.class, "CoverageTypeObject", IS_SERIALIZABLE, //$NON-NLS-1$
				!IS_GENERATED_INSTANCE_CLASS);

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
		addAnnotation(chartEClass, source, new String[] { "name", "Chart", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Version(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Version" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Type(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_SubType(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "SubType" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Description(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Description" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Block(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Block" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Dimension(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Dimension" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Script(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Script" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Units(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Units" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_SeriesThickness(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "SeriesThickness" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_GridColumnCount(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "GridColumnCount" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_ExtendedProperties(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ExtendedProperties" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_SampleData(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "SampleData" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Styles(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Styles" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_Interactivity(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Interactivity" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChart_EmptyMessage(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "EmptyMessage" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(chartWithAxesEClass, source, new String[] { "name", "ChartWithAxes", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_Axes(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Axes" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_WallFill(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "WallFill" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_FloorFill(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "FloorFill" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_Orientation(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Orientation" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_UnitSpacing(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "UnitSpacing" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_Rotation(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Rotation" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_ReverseCategory(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ReverseCategory" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithAxes_StudyLayout(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "StudyLayout" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(chartWithoutAxesEClass, source, new String[] { "name", "ChartWithoutAxes", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithoutAxes_SeriesDefinitions(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "SeriesDefinitions" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithoutAxes_MinSlice(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "MinSlice" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithoutAxes_MinSlicePercent(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "MinSlicePercent" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithoutAxes_MinSliceLabel(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "MinSliceLabel" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getChartWithoutAxes_Coverage(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Coverage" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(coverageTypeEDataType, source, new String[] { "name", "Coverage_._type", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#double", //$NON-NLS-1$ //$NON-NLS-2$
				"minInclusive", "0", //$NON-NLS-1$ //$NON-NLS-2$
				"maxInclusive", "1" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(coverageTypeObjectEDataType, source, new String[] { "name", "Coverage_._type:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Coverage_._type" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(dialChartEClass, source, new String[] { "name", "DialChart", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getDialChart_DialSuperimposition(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "DialSuperimposition" //$NON-NLS-1$ //$NON-NLS-2$
		});
	}

} // ModelPackageImpl
