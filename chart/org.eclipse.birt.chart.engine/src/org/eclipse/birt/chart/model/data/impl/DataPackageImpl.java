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

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.BubbleDataSet;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.DifferenceDataSet;
import org.eclipse.birt.chart.model.data.GanttDataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Rule;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.impl.ModelPackageImpl;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.type.impl.TypePackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * @generated
 */
public class DataPackageImpl extends EPackageImpl implements DataPackage
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseSampleDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass bubbleDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dateTimeDataElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dateTimeDataSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass differenceDataSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ganttDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass numberDataElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass numberDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass orthogonalSampleDataEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sampleDataEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass seriesDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass seriesGroupingEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stockDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass textDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass triggerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dataEDataType = null;

	/**
	 * Creates an instance of the model <b>Package </b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry}by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.birt.chart.model.data.DataPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DataPackageImpl( )
	{
		super( eNS_URI, DataFactory.eINSTANCE );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DataPackage init( )
	{
		if ( isInited )
			return (DataPackage) EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI );

		// Obtain or create and register package
		DataPackageImpl theDataPackage = (DataPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( eNS_URI ) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( eNS_URI )
				: new DataPackageImpl( ) );

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass( );

		// Obtain or create and register interdependencies
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI ) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI )
				: ComponentPackage.eINSTANCE );
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI ) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI )
				: AttributePackage.eINSTANCE );
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI ) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI )
				: LayoutPackage.eINSTANCE );
		TypePackageImpl theTypePackage = (TypePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI ) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI )
				: TypePackage.eINSTANCE );
		ModelPackageImpl theModelPackage = (ModelPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI ) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI )
				: ModelPackage.eINSTANCE );

		// Create package meta-data objects
		theDataPackage.createPackageContents( );
		theComponentPackage.createPackageContents( );
		theAttributePackage.createPackageContents( );
		theLayoutPackage.createPackageContents( );
		theTypePackage.createPackageContents( );
		theModelPackage.createPackageContents( );

		// Initialize created meta-data
		theDataPackage.initializePackageContents( );
		theComponentPackage.initializePackageContents( );
		theAttributePackage.initializePackageContents( );
		theLayoutPackage.initializePackageContents( );
		theTypePackage.initializePackageContents( );
		theModelPackage.initializePackageContents( );

		// Mark meta-data to indicate it can't be changed
		theDataPackage.freeze( );

		return theDataPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAction( )
	{
		return actionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAction_Type( )
	{
		return (EAttribute) actionEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAction_Value( )
	{
		return (EReference) actionEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseSampleData( )
	{
		return baseSampleDataEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBaseSampleData_DataSetRepresentation( )
	{
		return (EAttribute) baseSampleDataEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBubbleDataSet( )
	{
		return bubbleDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataElement( )
	{
		return dataElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataSet( )
	{
		return dataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataSet_Values( )
	{
		return (EAttribute) dataSetEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDateTimeDataElement( )
	{
		return dateTimeDataElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDateTimeDataElement_Value( )
	{
		return (EAttribute) dateTimeDataElementEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDateTimeDataSet( )
	{
		return dateTimeDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDifferenceDataSet( )
	{
		return differenceDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGanttDataSet( )
	{
		return ganttDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNumberDataElement( )
	{
		return numberDataElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumberDataElement_Value( )
	{
		return (EAttribute) numberDataElementEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNumberDataSet( )
	{
		return numberDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOrthogonalSampleData( )
	{
		return orthogonalSampleDataEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrthogonalSampleData_DataSetRepresentation( )
	{
		return (EAttribute) orthogonalSampleDataEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrthogonalSampleData_SeriesDefinitionIndex( )
	{
		return (EAttribute) orthogonalSampleDataEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQuery( )
	{
		return queryEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuery_Definition( )
	{
		return (EAttribute) queryEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQuery_Rules( )
	{
		return (EReference) queryEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRule( )
	{
		return ruleEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRule_Type( )
	{
		return (EAttribute) ruleEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRule_Value( )
	{
		return (EAttribute) ruleEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSampleData( )
	{
		return sampleDataEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSampleData_BaseSampleData( )
	{
		return (EReference) sampleDataEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSampleData_OrthogonalSampleData( )
	{
		return (EReference) sampleDataEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSampleData_AncillarySampleData( )
	{
		return (EReference) sampleDataEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSeriesDefinition( )
	{
		return seriesDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesDefinition_Query( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesDefinition_SeriesPalette( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesDefinition_SeriesDefinitions( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesDefinition_FormatSpecifier( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesDefinition_Series( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesDefinition_Grouping( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesDefinition_Sorting( )
	{
		return (EAttribute) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSeriesGrouping( )
	{
		return seriesGroupingEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesGrouping_Enabled( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesGrouping_GroupingUnit( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSeriesGrouping_GroupingOrigin( )
	{
		return (EReference) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesGrouping_GroupingInterval( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesGrouping_GroupType( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesGrouping_AggregateExpression( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStockDataSet( )
	{
		return stockDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTextDataSet( )
	{
		return textDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTrigger( )
	{
		return triggerEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTrigger_Condition( )
	{
		return (EAttribute) triggerEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTrigger_Action( )
	{
		return (EReference) triggerEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTrigger_TriggerFlow( )
	{
		return (EAttribute) triggerEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getData( )
	{
		return dataEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataFactory getDataFactory( )
	{
		return (DataFactory) getEFactoryInstance( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents( )
	{
		if ( isCreated )
			return;
		isCreated = true;

		// Create classes and their features
		actionEClass = createEClass( ACTION );
		createEAttribute( actionEClass, ACTION__TYPE );
		createEReference( actionEClass, ACTION__VALUE );

		baseSampleDataEClass = createEClass( BASE_SAMPLE_DATA );
		createEAttribute( baseSampleDataEClass,
				BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION );

		bubbleDataSetEClass = createEClass( BUBBLE_DATA_SET );

		dataElementEClass = createEClass( DATA_ELEMENT );

		dataSetEClass = createEClass( DATA_SET );
		createEAttribute( dataSetEClass, DATA_SET__VALUES );

		dateTimeDataElementEClass = createEClass( DATE_TIME_DATA_ELEMENT );
		createEAttribute( dateTimeDataElementEClass,
				DATE_TIME_DATA_ELEMENT__VALUE );

		dateTimeDataSetEClass = createEClass( DATE_TIME_DATA_SET );

		differenceDataSetEClass = createEClass( DIFFERENCE_DATA_SET );

		ganttDataSetEClass = createEClass( GANTT_DATA_SET );

		numberDataElementEClass = createEClass( NUMBER_DATA_ELEMENT );
		createEAttribute( numberDataElementEClass, NUMBER_DATA_ELEMENT__VALUE );

		numberDataSetEClass = createEClass( NUMBER_DATA_SET );

		orthogonalSampleDataEClass = createEClass( ORTHOGONAL_SAMPLE_DATA );
		createEAttribute( orthogonalSampleDataEClass,
				ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION );
		createEAttribute( orthogonalSampleDataEClass,
				ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX );

		queryEClass = createEClass( QUERY );
		createEAttribute( queryEClass, QUERY__DEFINITION );
		createEReference( queryEClass, QUERY__RULES );

		ruleEClass = createEClass( RULE );
		createEAttribute( ruleEClass, RULE__TYPE );
		createEAttribute( ruleEClass, RULE__VALUE );

		sampleDataEClass = createEClass( SAMPLE_DATA );
		createEReference( sampleDataEClass, SAMPLE_DATA__BASE_SAMPLE_DATA );
		createEReference( sampleDataEClass, SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA );
		createEReference( sampleDataEClass, SAMPLE_DATA__ANCILLARY_SAMPLE_DATA );

		seriesDefinitionEClass = createEClass( SERIES_DEFINITION );
		createEReference( seriesDefinitionEClass, SERIES_DEFINITION__QUERY );
		createEReference( seriesDefinitionEClass,
				SERIES_DEFINITION__SERIES_PALETTE );
		createEReference( seriesDefinitionEClass,
				SERIES_DEFINITION__SERIES_DEFINITIONS );
		createEReference( seriesDefinitionEClass,
				SERIES_DEFINITION__FORMAT_SPECIFIER );
		createEReference( seriesDefinitionEClass, SERIES_DEFINITION__SERIES );
		createEReference( seriesDefinitionEClass, SERIES_DEFINITION__GROUPING );
		createEAttribute( seriesDefinitionEClass, SERIES_DEFINITION__SORTING );

		seriesGroupingEClass = createEClass( SERIES_GROUPING );
		createEAttribute( seriesGroupingEClass, SERIES_GROUPING__ENABLED );
		createEAttribute( seriesGroupingEClass, SERIES_GROUPING__GROUPING_UNIT );
		createEReference( seriesGroupingEClass,
				SERIES_GROUPING__GROUPING_ORIGIN );
		createEAttribute( seriesGroupingEClass,
				SERIES_GROUPING__GROUPING_INTERVAL );
		createEAttribute( seriesGroupingEClass, SERIES_GROUPING__GROUP_TYPE );
		createEAttribute( seriesGroupingEClass,
				SERIES_GROUPING__AGGREGATE_EXPRESSION );

		stockDataSetEClass = createEClass( STOCK_DATA_SET );

		textDataSetEClass = createEClass( TEXT_DATA_SET );

		triggerEClass = createEClass( TRIGGER );
		createEAttribute( triggerEClass, TRIGGER__CONDITION );
		createEReference( triggerEClass, TRIGGER__ACTION );
		createEAttribute( triggerEClass, TRIGGER__TRIGGER_FLOW );

		// Create data types
		dataEDataType = createEDataType( DATA );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents( )
	{
		if ( isInitialized )
			return;
		isInitialized = true;

		// Initialize package
		setName( eNAME );
		setNsPrefix( eNS_PREFIX );
		setNsURI( eNS_URI );

		// Obtain other dependent packages
		AttributePackage theAttributePackage = (AttributePackage) EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI );
		XMLTypePackage theXMLTypePackage = (XMLTypePackage) EPackage.Registry.INSTANCE.getEPackage( XMLTypePackage.eNS_URI );
		ComponentPackage theComponentPackage = (ComponentPackage) EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI );

		// Add supertypes to classes
		bubbleDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		dateTimeDataElementEClass.getESuperTypes( )
				.add( this.getDataElement( ) );
		dateTimeDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		differenceDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		ganttDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		numberDataElementEClass.getESuperTypes( ).add( this.getDataElement( ) );
		numberDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		stockDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		textDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );

		// Initialize classes and features; add operations and parameters
		initEClass( actionEClass,
				Action.class,
				"Action", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getAction_Type( ),
				theAttributePackage.getActionType( ),
				"type", "URL_Redirect", 1, 1, Action.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getAction_Value( ),
				theAttributePackage.getActionValue( ),
				null,
				"value", null, 1, 1, Action.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( baseSampleDataEClass,
				BaseSampleData.class,
				"BaseSampleData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getBaseSampleData_DataSetRepresentation( ),
				theXMLTypePackage.getString( ),
				"dataSetRepresentation", null, 1, 1, BaseSampleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( bubbleDataSetEClass,
				BubbleDataSet.class,
				"BubbleDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( dataElementEClass,
				DataElement.class,
				"DataElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( dataSetEClass,
				DataSet.class,
				"DataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getDataSet_Values( ),
				this.getData( ),
				"values", null, 1, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( dateTimeDataElementEClass,
				DateTimeDataElement.class,
				"DateTimeDataElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getDateTimeDataElement_Value( ),
				theXMLTypePackage.getLong( ),
				"value", null, 1, 1, DateTimeDataElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( dateTimeDataSetEClass,
				DateTimeDataSet.class,
				"DateTimeDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( differenceDataSetEClass,
				DifferenceDataSet.class,
				"DifferenceDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( ganttDataSetEClass,
				GanttDataSet.class,
				"GanttDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( numberDataElementEClass,
				NumberDataElement.class,
				"NumberDataElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getNumberDataElement_Value( ),
				theXMLTypePackage.getDouble( ),
				"value", null, 1, 1, NumberDataElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( numberDataSetEClass,
				NumberDataSet.class,
				"NumberDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( orthogonalSampleDataEClass,
				OrthogonalSampleData.class,
				"OrthogonalSampleData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getOrthogonalSampleData_DataSetRepresentation( ),
				theXMLTypePackage.getString( ),
				"dataSetRepresentation", null, 1, 1, OrthogonalSampleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getOrthogonalSampleData_SeriesDefinitionIndex( ),
				theXMLTypePackage.getInt( ),
				"seriesDefinitionIndex", null, 1, 1, OrthogonalSampleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( queryEClass,
				Query.class,
				"Query", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getQuery_Definition( ),
				theXMLTypePackage.getString( ),
				"definition", null, 1, 1, Query.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getQuery_Rules( ),
				this.getRule( ),
				null,
				"rules", null, 1, -1, Query.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( ruleEClass,
				Rule.class,
				"Rule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getRule_Type( ),
				theAttributePackage.getRuleType( ),
				"type", "Filter", 1, 1, Rule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getRule_Value( ),
				theXMLTypePackage.getString( ),
				"value", null, 1, 1, Rule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( sampleDataEClass,
				SampleData.class,
				"SampleData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getSampleData_BaseSampleData( ),
				this.getBaseSampleData( ),
				null,
				"baseSampleData", null, 1, -1, SampleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSampleData_OrthogonalSampleData( ),
				this.getOrthogonalSampleData( ),
				null,
				"orthogonalSampleData", null, 1, -1, SampleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSampleData_AncillarySampleData( ),
				this.getBaseSampleData( ),
				null,
				"ancillarySampleData", null, 1, -1, SampleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( seriesDefinitionEClass,
				SeriesDefinition.class,
				"SeriesDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getSeriesDefinition_Query( ),
				this.getQuery( ),
				null,
				"query", null, 1, 1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSeriesDefinition_SeriesPalette( ),
				theAttributePackage.getPalette( ),
				null,
				"seriesPalette", null, 1, 1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSeriesDefinition_SeriesDefinitions( ),
				this.getSeriesDefinition( ),
				null,
				"seriesDefinitions", null, 1, -1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSeriesDefinition_FormatSpecifier( ),
				theAttributePackage.getFormatSpecifier( ),
				null,
				"formatSpecifier", null, 1, 1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSeriesDefinition_Series( ),
				theComponentPackage.getSeries( ),
				null,
				"series", null, 1, -1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getSeriesDefinition_Grouping( ),
				this.getSeriesGrouping( ),
				null,
				"grouping", null, 0, 1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getSeriesDefinition_Sorting( ),
				theAttributePackage.getSortOption( ),
				"sorting", "Ascending", 0, 1, SeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( seriesGroupingEClass,
				SeriesGrouping.class,
				"SeriesGrouping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getSeriesGrouping_Enabled( ),
				theXMLTypePackage.getBoolean( ),
				"enabled", null, 1, 1, SeriesGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getSeriesGrouping_GroupingUnit( ),
				theAttributePackage.getGroupingUnitType( ),
				"groupingUnit", "Seconds", 1, 1, SeriesGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getSeriesGrouping_GroupingOrigin( ),
				this.getDataElement( ),
				null,
				"groupingOrigin", null, 1, 1, SeriesGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getSeriesGrouping_GroupingInterval( ),
				theXMLTypePackage.getInt( ),
				"groupingInterval", null, 1, 1, SeriesGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getSeriesGrouping_GroupType( ),
				theAttributePackage.getDataType( ),
				"groupType", "Numeric", 1, 1, SeriesGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getSeriesGrouping_AggregateExpression( ),
				theXMLTypePackage.getString( ),
				"aggregateExpression", null, 1, 1, SeriesGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( stockDataSetEClass,
				StockDataSet.class,
				"StockDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( textDataSetEClass,
				TextDataSet.class,
				"TextDataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( triggerEClass,
				Trigger.class,
				"Trigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getTrigger_Condition( ),
				theAttributePackage.getTriggerCondition( ),
				"condition", "Mouse_Hover", 1, 1, Trigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getTrigger_Action( ),
				this.getAction( ),
				null,
				"action", null, 1, 1, Trigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getTrigger_TriggerFlow( ),
				theAttributePackage.getTriggerFlow( ),
				"triggerFlow", "Capture", 0, 1, Trigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		// Initialize data types
		initEDataType( dataEDataType,
				Object.class,
				"Data", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		// Create resource
		createResource( eNS_URI );

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations( );
	}

	/**
	 * Initializes the annotations for
	 * <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData </b>. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations( )
	{
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$				
		addAnnotation( actionEClass, source, new String[]{
				"name", "Action", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAction_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAction_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( baseSampleDataEClass, source, new String[]{
				"name", "BaseSampleData", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBaseSampleData_DataSetRepresentation( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "DataSetRepresentation" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( bubbleDataSetEClass, source, new String[]{
				"name", "BubbleDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataEDataType, source, new String[]{
				"name", "Data" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataElementEClass, source, new String[]{
				"name", "DataElement", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "empty" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataSetEClass, source, new String[]{
				"name", "DataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataSet_Values( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Values" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateTimeDataElementEClass, source, new String[]{
				"name", "DateTimeDataElement", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDateTimeDataElement_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateTimeDataSetEClass, source, new String[]{
				"name", "DateTimeDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( differenceDataSetEClass, source, new String[]{
				"name", "DifferenceDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( ganttDataSetEClass, source, new String[]{
				"name", "GanttDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( numberDataElementEClass, source, new String[]{
				"name", "NumberDataElement", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getNumberDataElement_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( numberDataSetEClass, source, new String[]{
				"name", "NumberDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( orthogonalSampleDataEClass, source, new String[]{
				"name", "OrthogonalSampleData", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getOrthogonalSampleData_DataSetRepresentation( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "DataSetRepresentation" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getOrthogonalSampleData_SeriesDefinitionIndex( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "SeriesDefinitionIndex" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( queryEClass, source, new String[]{
				"name", "Query", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getQuery_Definition( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Definition" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getQuery_Rules( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Rules" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( ruleEClass, source, new String[]{
				"name", "Rule", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getRule_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getRule_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( sampleDataEClass, source, new String[]{
				"name", "SampleData", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSampleData_BaseSampleData( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "BaseSampleData" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSampleData_OrthogonalSampleData( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "OrthogonalSampleData" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getSampleData_AncillarySampleData( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "AncillarySampleData" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( seriesDefinitionEClass, source, new String[]{
				"name", "SeriesDefinition", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesDefinition_Query( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Query" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesDefinition_SeriesPalette( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "SeriesPalette" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getSeriesDefinition_SeriesDefinitions( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "SeriesDefinitions" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getSeriesDefinition_FormatSpecifier( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getSeriesDefinition_Series( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Series" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesDefinition_Grouping( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Grouping" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesDefinition_Sorting( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Sorting" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( seriesGroupingEClass, source, new String[]{
				"name", "SeriesGrouping", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesGrouping_Enabled( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Enabled" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesGrouping_GroupingUnit( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "GroupingUnit" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesGrouping_GroupingOrigin( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "GroupingOrigin" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getSeriesGrouping_GroupingInterval( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "GroupingInterval" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getSeriesGrouping_GroupType( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "GroupType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesGrouping_AggregateExpression( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "AggregateExpression" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( stockDataSetEClass, source, new String[]{
				"name", "StockDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( textDataSetEClass, source, new String[]{
				"name", "TextDataSet", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( triggerEClass, source, new String[]{
				"name", "Trigger", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTrigger_Condition( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Condition" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTrigger_Action( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Action" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTrigger_TriggerFlow( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "TriggerFlow" //$NON-NLS-1$ //$NON-NLS-2$
		} );
	}

} //DataPackageImpl
