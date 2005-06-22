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
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class DataPackageImpl extends EPackageImpl implements DataPackage
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass actionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass baseSampleDataEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dataElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dateTimeDataElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dateTimeDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass numberDataElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass numberDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass orthogonalSampleDataEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass queryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass ruleEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sampleDataEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass seriesDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass seriesGroupingEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass stockDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass textDataSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass triggerEClass = null;

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
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package </b> for this model,
	 * and for any others upon which it depends. Simple dependencies are
	 * satisfied by calling this method on all dependent packages before doing
	 * anything else. This method drives initialization for interdependent
	 * packages directly, in parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not
	 * yet been registered by their URI values are first created and registered.
	 * The packages are then initialized in two steps: meta-model objects for
	 * all of the packages are created before any are initialized, since one
	 * package's meta-model objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already
	 * been initialized. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
		XMLTypePackageImpl.init( );

		// Obtain or create and register interdependencies
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI ) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI )
				: LayoutPackageImpl.eINSTANCE );
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI ) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI )
				: ComponentPackageImpl.eINSTANCE );
		TypePackageImpl theTypePackage = (TypePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI ) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI )
				: TypePackageImpl.eINSTANCE );
		ModelPackageImpl theModelPackage = (ModelPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI ) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI )
				: ModelPackageImpl.eINSTANCE );
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI ) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI )
				: AttributePackageImpl.eINSTANCE );

		// Create package meta-data objects
		theDataPackage.createPackageContents( );
		theLayoutPackage.createPackageContents( );
		theComponentPackage.createPackageContents( );
		theTypePackage.createPackageContents( );
		theModelPackage.createPackageContents( );
		theAttributePackage.createPackageContents( );

		// Initialize created meta-data
		theDataPackage.initializePackageContents( );
		theLayoutPackage.initializePackageContents( );
		theComponentPackage.initializePackageContents( );
		theTypePackage.initializePackageContents( );
		theModelPackage.initializePackageContents( );
		theAttributePackage.initializePackageContents( );

		// Mark meta-data to indicate it can't be changed
		theDataPackage.freeze( );

		return theDataPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAction( )
	{
		return actionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAction_Type( )
	{
		return (EAttribute) actionEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAction_Value( )
	{
		return (EReference) actionEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBaseSampleData( )
	{
		return baseSampleDataEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBaseSampleData_DataSetRepresentation( )
	{
		return (EAttribute) baseSampleDataEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDataElement( )
	{
		return dataElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDataSet( )
	{
		return dataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDataSet_Values( )
	{
		return (EAttribute) dataSetEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDateTimeDataElement( )
	{
		return dateTimeDataElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDateTimeDataElement_Value( )
	{
		return (EAttribute) dateTimeDataElementEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDateTimeDataSet( )
	{
		return dateTimeDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNumberDataElement( )
	{
		return numberDataElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getNumberDataElement_Value( )
	{
		return (EAttribute) numberDataElementEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNumberDataSet( )
	{
		return numberDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getOrthogonalSampleData( )
	{
		return orthogonalSampleDataEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getOrthogonalSampleData_DataSetRepresentation( )
	{
		return (EAttribute) orthogonalSampleDataEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getOrthogonalSampleData_SeriesDefinitionIndex( )
	{
		return (EAttribute) orthogonalSampleDataEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getQuery( )
	{
		return queryEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getQuery_Definition( )
	{
		return (EAttribute) queryEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getQuery_Rules( )
	{
		return (EReference) queryEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getRule( )
	{
		return ruleEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRule_Type( )
	{
		return (EAttribute) ruleEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getRule_Value( )
	{
		return (EAttribute) ruleEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSampleData( )
	{
		return sampleDataEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSampleData_BaseSampleData( )
	{
		return (EReference) sampleDataEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSampleData_OrthogonalSampleData( )
	{
		return (EReference) sampleDataEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSeriesDefinition( )
	{
		return seriesDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesDefinition_Query( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesDefinition_SeriesPalette( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesDefinition_SeriesDefinitions( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesDefinition_FormatSpecifier( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesDefinition_Series( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesDefinition_Grouping( )
	{
		return (EReference) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeriesDefinition_Sorting( )
	{
		return (EAttribute) seriesDefinitionEClass.getEStructuralFeatures( )
				.get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSeriesGrouping( )
	{
		return seriesGroupingEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeriesGrouping_Enabled( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeriesGrouping_GroupingUnit( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeriesGrouping_GroupingOrigin( )
	{
		return (EReference) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeriesGrouping_GroupingInterval( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeriesGrouping_GroupType( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeriesGrouping_AggregateExpression( )
	{
		return (EAttribute) seriesGroupingEClass.getEStructuralFeatures( )
				.get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getStockDataSet( )
	{
		return stockDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTextDataSet( )
	{
		return textDataSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTrigger( )
	{
		return triggerEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTrigger_Condition( )
	{
		return (EAttribute) triggerEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getTrigger_Action( )
	{
		return (EReference) triggerEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataFactory getDataFactory( )
	{
		return (DataFactory) getEFactoryInstance( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
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

		dataElementEClass = createEClass( DATA_ELEMENT );

		dataSetEClass = createEClass( DATA_SET );
		createEAttribute( dataSetEClass, DATA_SET__VALUES );

		dateTimeDataElementEClass = createEClass( DATE_TIME_DATA_ELEMENT );
		createEAttribute( dateTimeDataElementEClass,
				DATE_TIME_DATA_ELEMENT__VALUE );

		dateTimeDataSetEClass = createEClass( DATE_TIME_DATA_SET );

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
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI );
		XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl) EPackage.Registry.INSTANCE.getEPackage( XMLTypePackage.eNS_URI );
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI );

		// Add supertypes to classes
		dateTimeDataElementEClass.getESuperTypes( )
				.add( this.getDataElement( ) );
		dateTimeDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		numberDataElementEClass.getESuperTypes( ).add( this.getDataElement( ) );
		numberDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		stockDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );
		textDataSetEClass.getESuperTypes( ).add( this.getDataSet( ) );

		// Initialize classes and features; add operations and parameters
		initEClass( actionEClass,
				Action.class,
				"Action", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getAction_Type( ),
				theAttributePackage.getActionType( ),
				"type", //$NON-NLS-1$
				"URL_Redirect", //$NON-NLS-1$
				1,
				1,
				Action.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAction_Value( ),
				theAttributePackage.getActionValue( ),
				null,
				"value", //$NON-NLS-1$
				null,
				1,
				1,
				Action.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( baseSampleDataEClass,
				BaseSampleData.class,
				"BaseSampleData", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getBaseSampleData_DataSetRepresentation( ),
				theXMLTypePackage.getString( ),
				"dataSetRepresentation", //$NON-NLS-1$
				null,
				1,
				1,
				BaseSampleData.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( dataElementEClass,
				DataElement.class,
				"DataElement", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );

		initEClass( dataSetEClass,
				DataSet.class,
				"DataSet", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getDataSet_Values( ),
				theXMLTypePackage.getAnySimpleType( ),
				"values", //$NON-NLS-1$
				null,
				1,
				1,
				DataSet.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( dateTimeDataElementEClass,
				DateTimeDataElement.class,
				"DateTimeDataElement", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getDateTimeDataElement_Value( ),
				theXMLTypePackage.getLong( ),
				"value", //$NON-NLS-1$
				null,
				1,
				1,
				DateTimeDataElement.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( dateTimeDataSetEClass,
				DateTimeDataSet.class,
				"DateTimeDataSet", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );

		initEClass( numberDataElementEClass,
				NumberDataElement.class,
				"NumberDataElement", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getNumberDataElement_Value( ),
				theXMLTypePackage.getDouble( ),
				"value", //$NON-NLS-1$
				null,
				1,
				1,
				NumberDataElement.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( numberDataSetEClass,
				NumberDataSet.class,
				"NumberDataSet", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );

		initEClass( orthogonalSampleDataEClass,
				OrthogonalSampleData.class,
				"OrthogonalSampleData", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getOrthogonalSampleData_DataSetRepresentation( ),
				theXMLTypePackage.getString( ),
				"dataSetRepresentation", //$NON-NLS-1$
				null,
				1,
				1,
				OrthogonalSampleData.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getOrthogonalSampleData_SeriesDefinitionIndex( ),
				theXMLTypePackage.getInt( ),
				"seriesDefinitionIndex", //$NON-NLS-1$
				null,
				1,
				1,
				OrthogonalSampleData.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( queryEClass,
				Query.class,
				"Query", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getQuery_Definition( ),
				theXMLTypePackage.getString( ),
				"definition", //$NON-NLS-1$
				null,
				1,
				1,
				Query.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getQuery_Rules( ),
				this.getRule( ),
				null,
				"rules", //$NON-NLS-1$
				null,
				1,
				-1,
				Query.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( ruleEClass,
				Rule.class,
				"Rule", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getRule_Type( ),
				theAttributePackage.getRuleType( ),
				"type", //$NON-NLS-1$
				"Filter", //$NON-NLS-1$
				1,
				1,
				Rule.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getRule_Value( ),
				theXMLTypePackage.getString( ),
				"value", //$NON-NLS-1$
				null,
				1,
				1,
				Rule.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( sampleDataEClass,
				SampleData.class,
				"SampleData", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getSampleData_BaseSampleData( ),
				this.getBaseSampleData( ),
				null,
				"baseSampleData", //$NON-NLS-1$
				null,
				1,
				-1,
				SampleData.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSampleData_OrthogonalSampleData( ),
				this.getOrthogonalSampleData( ),
				null,
				"orthogonalSampleData", //$NON-NLS-1$
				null,
				1,
				-1,
				SampleData.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( seriesDefinitionEClass,
				SeriesDefinition.class,
				"SeriesDefinition", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getSeriesDefinition_Query( ),
				this.getQuery( ),
				null,
				"query", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeriesDefinition_SeriesPalette( ),
				theAttributePackage.getPalette( ),
				null,
				"seriesPalette", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeriesDefinition_SeriesDefinitions( ),
				this.getSeriesDefinition( ),
				null,
				"seriesDefinitions", //$NON-NLS-1$
				null,
				1,
				-1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeriesDefinition_FormatSpecifier( ),
				theAttributePackage.getFormatSpecifier( ),
				null,
				"formatSpecifier", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeriesDefinition_Series( ),
				theComponentPackage.getSeries( ),
				null,
				"series", //$NON-NLS-1$
				null,
				1,
				-1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeriesDefinition_Grouping( ),
				this.getSeriesGrouping( ),
				null,
				"grouping", //$NON-NLS-1$
				null,
				0,
				1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeriesDefinition_Sorting( ),
				theAttributePackage.getSortOption( ),
				"sorting", //$NON-NLS-1$
				"Ascending", //$NON-NLS-1$
				0,
				1,
				SeriesDefinition.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( seriesGroupingEClass,
				SeriesGrouping.class,
				"SeriesGrouping", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getSeriesGrouping_Enabled( ),
				theXMLTypePackage.getBoolean( ),
				"enabled", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesGrouping.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeriesGrouping_GroupingUnit( ),
				theAttributePackage.getGroupingUnitType( ),
				"groupingUnit", //$NON-NLS-1$
				"Seconds", //$NON-NLS-1$
				1,
				1,
				SeriesGrouping.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeriesGrouping_GroupingOrigin( ),
				this.getDataElement( ),
				null,
				"groupingOrigin", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesGrouping.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeriesGrouping_GroupingInterval( ),
				theXMLTypePackage.getInt( ),
				"groupingInterval", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesGrouping.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeriesGrouping_GroupType( ),
				theAttributePackage.getDataType( ),
				"groupType", //$NON-NLS-1$
				"Numeric", //$NON-NLS-1$
				1,
				1,
				SeriesGrouping.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeriesGrouping_AggregateExpression( ),
				theXMLTypePackage.getString( ),
				"aggregateExpression", //$NON-NLS-1$
				null,
				1,
				1,
				SeriesGrouping.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( stockDataSetEClass,
				StockDataSet.class,
				"StockDataSet", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );

		initEClass( textDataSetEClass,
				TextDataSet.class,
				"TextDataSet", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );

		initEClass( triggerEClass,
				Trigger.class,
				"Trigger", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getTrigger_Condition( ),
				theAttributePackage.getTriggerCondition( ),
				"condition", //$NON-NLS-1$
				"Mouse_Hover", //$NON-NLS-1$
				1,
				1,
				Trigger.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getTrigger_Action( ),
				this.getAction( ),
				null,
				"action", //$NON-NLS-1$
				null,
				1,
				1,
				Trigger.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

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
				"name", "Action", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getAction_Type( ), source, new String[]{
				"kind", "element", "name", "Type"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getAction_Value( ), source, new String[]{
				"kind", "element", "name", "Value"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( baseSampleDataEClass, source, new String[]{
				"name", "BaseSampleData", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getBaseSampleData_DataSetRepresentation( ),
				source,
				new String[]{
						"kind", "element", "name", "DataSetRepresentation"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( dataElementEClass, source, new String[]{
				"name", "DataElement", "kind", "empty"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( dataSetEClass, source, new String[]{
				"name", "DataSet", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getDataSet_Values( ), source, new String[]{
				"kind", "element", "name", "Values"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( dateTimeDataElementEClass, source, new String[]{
				"name", "DateTimeDataElement", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getDateTimeDataElement_Value( ), source, new String[]{
				"kind", "element", "name", "Value"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( dateTimeDataSetEClass, source, new String[]{
				"name", "DateTimeDataSet", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( numberDataElementEClass, source, new String[]{
				"name", "NumberDataElement", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getNumberDataElement_Value( ), source, new String[]{
				"kind", "element", "name", "Value"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( numberDataSetEClass, source, new String[]{
				"name", "NumberDataSet", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( orthogonalSampleDataEClass, source, new String[]{
				"name", "OrthogonalSampleData", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getOrthogonalSampleData_DataSetRepresentation( ),
				source,
				new String[]{
						"kind", "element", "name", "DataSetRepresentation"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getOrthogonalSampleData_SeriesDefinitionIndex( ),
				source,
				new String[]{
						"kind", "element", "name", "SeriesDefinitionIndex"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( queryEClass, source, new String[]{
				"name", "Query", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getQuery_Definition( ), source, new String[]{
				"kind", "element", "name", "Definition"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getQuery_Rules( ), source, new String[]{
				"kind", "element", "name", "Rules"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( ruleEClass, source, new String[]{
				"name", "Rule", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getRule_Type( ), source, new String[]{
				"kind", "element", "name", "Type"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getRule_Value( ), source, new String[]{
				"kind", "element", "name", "Value"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( sampleDataEClass, source, new String[]{
				"name", "SampleData", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSampleData_BaseSampleData( ), source, new String[]{
				"kind", "element", "name", "BaseSampleData"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSampleData_OrthogonalSampleData( ),
				source,
				new String[]{
						"kind", "element", "name", "OrthogonalSampleData"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( seriesDefinitionEClass, source, new String[]{
				"name", "SeriesDefinition", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_Query( ), source, new String[]{
				"kind", "element", "name", "Query"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_SeriesPalette( ),
				source,
				new String[]{
						"kind", "element", "name", "SeriesPalette"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_SeriesDefinitions( ),
				source,
				new String[]{
						"kind", "element", "name", "SeriesDefinitions"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_FormatSpecifier( ),
				source,
				new String[]{
						"kind", "element", "name", "FormatSpecifier"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_Series( ), source, new String[]{
				"kind", "element", "name", "Series"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_Grouping( ), source, new String[]{
				"kind", "element", "name", "Grouping"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesDefinition_Sorting( ), source, new String[]{
				"kind", "element", "name", "Sorting"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( seriesGroupingEClass, source, new String[]{
				"name", "SeriesGrouping", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesGrouping_Enabled( ), source, new String[]{
				"kind", "element", "name", "Enabled"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesGrouping_GroupingUnit( ), source, new String[]{
				"kind", "element", "name", "GroupingUnit"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesGrouping_GroupingOrigin( ),
				source,
				new String[]{
						"kind", "element", "name", "GroupingOrigin"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesGrouping_GroupingInterval( ),
				source,
				new String[]{
						"kind", "element", "name", "GroupingInterval"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesGrouping_GroupType( ), source, new String[]{
				"kind", "element", "name", "GroupType"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getSeriesGrouping_AggregateExpression( ),
				source,
				new String[]{
						"kind", "element", "name", "AggregateExpression"
				} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( stockDataSetEClass, source, new String[]{
				"name", "StockDataSet", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( textDataSetEClass, source, new String[]{
				"name", "TextDataSet", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( triggerEClass, source, new String[]{
				"name", "Trigger", "kind", "elementOnly"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getTrigger_Condition( ), source, new String[]{
				"kind", "element", "name", "Condition"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		addAnnotation( getTrigger_Action( ), source, new String[]{
				"kind", "element", "name", "Action"
		} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

} //DataPackageImpl
