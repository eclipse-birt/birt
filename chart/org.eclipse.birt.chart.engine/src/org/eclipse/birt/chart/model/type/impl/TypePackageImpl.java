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
import org.eclipse.birt.chart.model.type.DialSeries;
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
import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * @generated
 */
public class TypePackageImpl extends EPackageImpl implements TypePackage
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass areaSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass barSeriesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dialSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lineSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pieSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scatterSeriesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stockSeriesEClass = null;

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
	 * @see org.eclipse.birt.chart.model.type.TypePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TypePackageImpl( )
	{
		super( eNS_URI, TypeFactory.eINSTANCE );
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
	public static TypePackage init( )
	{
		if ( isInited )
			return (TypePackage) EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI );

		// Obtain or create and register package
		TypePackageImpl theTypePackage = (TypePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( eNS_URI ) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( eNS_URI )
				: new TypePackageImpl( ) );

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackageImpl.init( );

		// Obtain or create and register interdependencies
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI ) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI )
				: ComponentPackage.eINSTANCE );
		DataPackageImpl theDataPackage = (DataPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI ) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI )
				: DataPackage.eINSTANCE );
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI ) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI )
				: AttributePackage.eINSTANCE );
		ModelPackageImpl theModelPackage = (ModelPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI ) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI )
				: ModelPackage.eINSTANCE );
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI ) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI )
				: LayoutPackage.eINSTANCE );

		// Create package meta-data objects
		theTypePackage.createPackageContents( );
		theComponentPackage.createPackageContents( );
		theDataPackage.createPackageContents( );
		theAttributePackage.createPackageContents( );
		theModelPackage.createPackageContents( );
		theLayoutPackage.createPackageContents( );

		// Initialize created meta-data
		theTypePackage.initializePackageContents( );
		theComponentPackage.initializePackageContents( );
		theDataPackage.initializePackageContents( );
		theAttributePackage.initializePackageContents( );
		theModelPackage.initializePackageContents( );
		theLayoutPackage.initializePackageContents( );

		// Mark meta-data to indicate it can't be changed
		theTypePackage.freeze( );

		return theTypePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAreaSeries( )
	{
		return areaSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBarSeries( )
	{
		return barSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBarSeries_Riser( )
	{
		return (EAttribute) barSeriesEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBarSeries_RiserOutline( )
	{
		return (EReference) barSeriesEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDialSeries( )
	{
		return dialSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDialSeries_Dial( )
	{
		return (EReference) dialSeriesEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDialSeries_Needle( )
	{
		return (EReference) dialSeriesEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLineSeries( )
	{
		return lineSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLineSeries_Marker( )
	{
		return (EReference) lineSeriesEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLineSeries_LineAttributes( )
	{
		return (EReference) lineSeriesEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLineSeries_PaletteLineColor( )
	{
		return (EAttribute) lineSeriesEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLineSeries_Curve( )
	{
		return (EAttribute) lineSeriesEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLineSeries_ShadowColor( )
	{
		return (EReference) lineSeriesEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPieSeries( )
	{
		return pieSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPieSeries_Explosion( )
	{
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPieSeries_ExplosionExpression( )
	{
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPieSeries_Title( )
	{
		return (EReference) pieSeriesEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPieSeries_TitlePosition( )
	{
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPieSeries_LeaderLineAttributes( )
	{
		return (EReference) pieSeriesEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPieSeries_LeaderLineStyle( )
	{
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPieSeries_LeaderLineLength( )
	{
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures( ).get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPieSeries_SliceOutline( )
	{
		return (EReference) pieSeriesEClass.getEStructuralFeatures( ).get( 7 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPieSeries_Ratio( )
	{
		return (EAttribute) pieSeriesEClass.getEStructuralFeatures( ).get( 8 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScatterSeries( )
	{
		return scatterSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStockSeries( )
	{
		return stockSeriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStockSeries_Fill( )
	{
		return (EReference) stockSeriesEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStockSeries_LineAttributes( )
	{
		return (EReference) stockSeriesEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TypeFactory getTypeFactory( )
	{
		return (TypeFactory) getEFactoryInstance( );
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
		areaSeriesEClass = createEClass( AREA_SERIES );

		barSeriesEClass = createEClass( BAR_SERIES );
		createEAttribute( barSeriesEClass, BAR_SERIES__RISER );
		createEReference( barSeriesEClass, BAR_SERIES__RISER_OUTLINE );

		dialSeriesEClass = createEClass( DIAL_SERIES );
		createEReference( dialSeriesEClass, DIAL_SERIES__DIAL );
		createEReference( dialSeriesEClass, DIAL_SERIES__NEEDLE );

		lineSeriesEClass = createEClass( LINE_SERIES );
		createEReference( lineSeriesEClass, LINE_SERIES__MARKER );
		createEReference( lineSeriesEClass, LINE_SERIES__LINE_ATTRIBUTES );
		createEAttribute( lineSeriesEClass, LINE_SERIES__PALETTE_LINE_COLOR );
		createEAttribute( lineSeriesEClass, LINE_SERIES__CURVE );
		createEReference( lineSeriesEClass, LINE_SERIES__SHADOW_COLOR );

		pieSeriesEClass = createEClass( PIE_SERIES );
		createEAttribute( pieSeriesEClass, PIE_SERIES__EXPLOSION );
		createEAttribute( pieSeriesEClass, PIE_SERIES__EXPLOSION_EXPRESSION );
		createEReference( pieSeriesEClass, PIE_SERIES__TITLE );
		createEAttribute( pieSeriesEClass, PIE_SERIES__TITLE_POSITION );
		createEReference( pieSeriesEClass, PIE_SERIES__LEADER_LINE_ATTRIBUTES );
		createEAttribute( pieSeriesEClass, PIE_SERIES__LEADER_LINE_STYLE );
		createEAttribute( pieSeriesEClass, PIE_SERIES__LEADER_LINE_LENGTH );
		createEReference( pieSeriesEClass, PIE_SERIES__SLICE_OUTLINE );
		createEAttribute( pieSeriesEClass, PIE_SERIES__RATIO );

		scatterSeriesEClass = createEClass( SCATTER_SERIES );

		stockSeriesEClass = createEClass( STOCK_SERIES );
		createEReference( stockSeriesEClass, STOCK_SERIES__FILL );
		createEReference( stockSeriesEClass, STOCK_SERIES__LINE_ATTRIBUTES );
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
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI );
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI );
		XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl) EPackage.Registry.INSTANCE.getEPackage( XMLTypePackage.eNS_URI );

		// Add supertypes to classes
		areaSeriesEClass.getESuperTypes( ).add( this.getLineSeries( ) );
		barSeriesEClass.getESuperTypes( )
				.add( theComponentPackage.getSeries( ) );
		dialSeriesEClass.getESuperTypes( )
				.add( theComponentPackage.getSeries( ) );
		lineSeriesEClass.getESuperTypes( )
				.add( theComponentPackage.getSeries( ) );
		pieSeriesEClass.getESuperTypes( )
				.add( theComponentPackage.getSeries( ) );
		scatterSeriesEClass.getESuperTypes( ).add( this.getLineSeries( ) );
		stockSeriesEClass.getESuperTypes( )
				.add( theComponentPackage.getSeries( ) );

		// Initialize classes and features; add operations and parameters
		initEClass( areaSeriesEClass,
				AreaSeries.class,
				"AreaSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( barSeriesEClass,
				BarSeries.class,
				"BarSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getBarSeries_Riser( ),
				theAttributePackage.getRiserType( ),
				"riser", "Rectangle", 0, 1, BarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getBarSeries_RiserOutline( ),
				theAttributePackage.getColorDefinition( ),
				null,
				"riserOutline", null, 0, 1, BarSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( dialSeriesEClass,
				DialSeries.class,
				"DialSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getDialSeries_Dial( ),
				theComponentPackage.getDial( ),
				null,
				"dial", null, 1, 1, DialSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getDialSeries_Needle( ),
				theComponentPackage.getNeedle( ),
				null,
				"needle", null, 1, 1, DialSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( lineSeriesEClass,
				LineSeries.class,
				"LineSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getLineSeries_Marker( ),
				theAttributePackage.getMarker( ),
				null,
				"marker", null, 0, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getLineSeries_LineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"lineAttributes", null, 0, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getLineSeries_PaletteLineColor( ),
				theXMLTypePackage.getBoolean( ),
				"paletteLineColor", null, 1, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getLineSeries_Curve( ),
				theXMLTypePackage.getBoolean( ),
				"curve", null, 1, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getLineSeries_ShadowColor( ),
				theAttributePackage.getColorDefinition( ),
				null,
				"shadowColor", null, 1, 1, LineSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( pieSeriesEClass,
				PieSeries.class,
				"PieSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getPieSeries_Explosion( ),
				theXMLTypePackage.getInt( ),
				"explosion", null, 0, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getPieSeries_ExplosionExpression( ),
				theXMLTypePackage.getString( ),
				"explosionExpression", null, 0, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getPieSeries_Title( ),
				theComponentPackage.getLabel( ),
				null,
				"title", null, 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getPieSeries_TitlePosition( ),
				theAttributePackage.getPosition( ),
				"titlePosition", "Above", 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getPieSeries_LeaderLineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"leaderLineAttributes", null, 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getPieSeries_LeaderLineStyle( ),
				theAttributePackage.getLeaderLineStyle( ),
				"leaderLineStyle", "Fixed_Length", 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getPieSeries_LeaderLineLength( ),
				theAttributePackage.getPercentage( ),
				"leaderLineLength", null, 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getPieSeries_SliceOutline( ),
				theAttributePackage.getColorDefinition( ),
				null,
				"sliceOutline", null, 0, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getPieSeries_Ratio( ),
				theXMLTypePackage.getDouble( ),
				"ratio", "1", 1, 1, PieSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( scatterSeriesEClass,
				ScatterSeries.class,
				"ScatterSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( stockSeriesEClass,
				StockSeries.class,
				"StockSeries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getStockSeries_Fill( ),
				theAttributePackage.getFill( ),
				null,
				"fill", null, 0, 1, StockSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getStockSeries_LineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"lineAttributes", null, 0, 1, StockSeries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

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
		addAnnotation( areaSeriesEClass, source, new String[]{
				"name", "AreaSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( barSeriesEClass, source, new String[]{
				"name", "BarSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBarSeries_Riser( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Riser" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBarSeries_RiserOutline( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "RiserOutline" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dialSeriesEClass, source, new String[]{
				"name", "DialSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDialSeries_Dial( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Dial" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDialSeries_Needle( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Needle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( lineSeriesEClass, source, new String[]{
				"name", "LineSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineSeries_Marker( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Marker" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineSeries_LineAttributes( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineSeries_PaletteLineColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "PaletteLineColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineSeries_Curve( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Curve" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineSeries_ShadowColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShadowColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( pieSeriesEClass, source, new String[]{
				"name", "PieSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_Explosion( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Explosion" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_ExplosionExpression( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "ExplosionExpression" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getPieSeries_Title( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Title" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_TitlePosition( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "TitlePosition" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_LeaderLineAttributes( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "LeaderLineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getPieSeries_LeaderLineStyle( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LeaderLineStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_LeaderLineLength( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LeaderLineLength" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_SliceOutline( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "SliceOutline" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPieSeries_Ratio( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Ratio" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( scatterSeriesEClass, source, new String[]{
				"name", "ScatterSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( stockSeriesEClass, source, new String[]{
				"name", "StockSeries", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStockSeries_Fill( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Fill" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStockSeries_LineAttributes( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$
		} );
	}

} // TypePackageImpl
