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

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ChartPreferences;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.impl.DataPackageImpl;
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
public class ComponentPackageImpl extends EPackageImpl implements
		ComponentPackage
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass axisEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass chartPreferencesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass curveFittingEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass gridEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass labelEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass markerLineEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass markerRangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass scaleEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass seriesEClass = null;

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
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ComponentPackageImpl( )
	{
		super( eNS_URI, ComponentFactory.eINSTANCE );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
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
	public static ComponentPackage init( )
	{
		if ( isInited )
			return (ComponentPackage) EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI );

		// Obtain or create and register package
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( eNS_URI ) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( eNS_URI )
				: new ComponentPackageImpl( ) );

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackageImpl.init( );

		// Obtain or create and register interdependencies
		DataPackageImpl theDataPackage = (DataPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI ) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI )
				: DataPackage.eINSTANCE );
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI ) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI )
				: AttributePackage.eINSTANCE );
		ModelPackageImpl theModelPackage = (ModelPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI ) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI )
				: ModelPackage.eINSTANCE );
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI ) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI )
				: LayoutPackage.eINSTANCE );
		TypePackageImpl theTypePackage = (TypePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI ) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI )
				: TypePackage.eINSTANCE );

		// Create package meta-data objects
		theComponentPackage.createPackageContents( );
		theDataPackage.createPackageContents( );
		theAttributePackage.createPackageContents( );
		theModelPackage.createPackageContents( );
		theLayoutPackage.createPackageContents( );
		theTypePackage.createPackageContents( );

		// Initialize created meta-data
		theComponentPackage.initializePackageContents( );
		theDataPackage.initializePackageContents( );
		theAttributePackage.initializePackageContents( );
		theModelPackage.initializePackageContents( );
		theLayoutPackage.initializePackageContents( );
		theTypePackage.initializePackageContents( );

		// Mark meta-data to indicate it can't be changed
		theComponentPackage.freeze( );

		return theComponentPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAxis( )
	{
		return axisEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_Type( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_Title( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_SubTitle( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_TitlePosition( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_AssociatedAxes( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_SeriesDefinitions( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_GapWidth( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_Orientation( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 7 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_LineAttributes( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 8 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_Label( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 9 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_FormatSpecifier( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 10 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_LabelPosition( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 11 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_Staggered( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 12 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_MarkerLines( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 13 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_MarkerRanges( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 14 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_MajorGrid( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 15 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_MinorGrid( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 16 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_Scale( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 17 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAxis_Origin( )
	{
		return (EReference) axisEClass.getEStructuralFeatures( ).get( 18 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_PrimaryAxis( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 19 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_CategoryAxis( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 20 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAxis_Percent( )
	{
		return (EAttribute) axisEClass.getEStructuralFeatures( ).get( 21 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getChartPreferences( )
	{
		return chartPreferencesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartPreferences_Labels( )
	{
		return (EReference) chartPreferencesEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getChartPreferences_Blocks( )
	{
		return (EReference) chartPreferencesEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getCurveFitting( )
	{
		return curveFittingEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getCurveFitting_LineAttributes( )
	{
		return (EReference) curveFittingEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getCurveFitting_Label( )
	{
		return (EReference) curveFittingEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getCurveFitting_LabelAnchor( )
	{
		return (EAttribute) curveFittingEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getGrid( )
	{
		return gridEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGrid_LineAttributes( )
	{
		return (EReference) gridEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGrid_TickStyle( )
	{
		return (EAttribute) gridEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGrid_TickAttributes( )
	{
		return (EReference) gridEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGrid_TickSize( )
	{
		return (EAttribute) gridEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getGrid_TickCount( )
	{
		return (EAttribute) gridEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLabel( )
	{
		return labelEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLabel_Caption( )
	{
		return (EReference) labelEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLabel_Background( )
	{
		return (EReference) labelEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLabel_Outline( )
	{
		return (EReference) labelEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLabel_ShadowColor( )
	{
		return (EReference) labelEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLabel_Insets( )
	{
		return (EReference) labelEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLabel_Visible( )
	{
		return (EAttribute) labelEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getMarkerLine( )
	{
		return markerLineEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerLine_LineAttributes( )
	{
		return (EReference) markerLineEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerLine_Value( )
	{
		return (EReference) markerLineEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerLine_Label( )
	{
		return (EReference) markerLineEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getMarkerLine_LabelAnchor( )
	{
		return (EAttribute) markerLineEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerLine_FormatSpecifier( )
	{
		return (EReference) markerLineEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getMarkerRange( )
	{
		return markerRangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerRange_Outline( )
	{
		return (EReference) markerRangeEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerRange_Fill( )
	{
		return (EReference) markerRangeEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerRange_StartValue( )
	{
		return (EReference) markerRangeEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerRange_EndValue( )
	{
		return (EReference) markerRangeEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerRange_Label( )
	{
		return (EReference) markerRangeEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getMarkerRange_LabelAnchor( )
	{
		return (EAttribute) markerRangeEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMarkerRange_FormatSpecifier( )
	{
		return (EReference) markerRangeEClass.getEStructuralFeatures( ).get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getScale( )
	{
		return scaleEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getScale_Min( )
	{
		return (EReference) scaleEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getScale_Max( )
	{
		return (EReference) scaleEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getScale_Step( )
	{
		return (EAttribute) scaleEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getScale_Unit( )
	{
		return (EAttribute) scaleEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getScale_MinorGridsPerUnit( )
	{
		return (EAttribute) scaleEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSeries( )
	{
		return seriesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeries_Visible( )
	{
		return (EAttribute) seriesEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeries_Label( )
	{
		return (EReference) seriesEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeries_DataDefinition( )
	{
		return (EReference) seriesEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeries_SeriesIdentifier( )
	{
		return (EAttribute) seriesEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeries_DataPoint( )
	{
		return (EReference) seriesEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeries_DataSet( )
	{
		return (EReference) seriesEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeries_LabelPosition( )
	{
		return (EAttribute) seriesEClass.getEStructuralFeatures( ).get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeries_Stacked( )
	{
		return (EAttribute) seriesEClass.getEStructuralFeatures( ).get( 7 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeries_Triggers( )
	{
		return (EReference) seriesEClass.getEStructuralFeatures( ).get( 8 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getSeries_Translucent( )
	{
		return (EAttribute) seriesEClass.getEStructuralFeatures( ).get( 9 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSeries_CurveFitting( )
	{
		return (EReference) seriesEClass.getEStructuralFeatures( ).get( 10 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ComponentFactory getComponentFactory( )
	{
		return (ComponentFactory) getEFactoryInstance( );
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
		axisEClass = createEClass( AXIS );
		createEAttribute( axisEClass, AXIS__TYPE );
		createEReference( axisEClass, AXIS__TITLE );
		createEReference( axisEClass, AXIS__SUB_TITLE );
		createEAttribute( axisEClass, AXIS__TITLE_POSITION );
		createEReference( axisEClass, AXIS__ASSOCIATED_AXES );
		createEReference( axisEClass, AXIS__SERIES_DEFINITIONS );
		createEAttribute( axisEClass, AXIS__GAP_WIDTH );
		createEAttribute( axisEClass, AXIS__ORIENTATION );
		createEReference( axisEClass, AXIS__LINE_ATTRIBUTES );
		createEReference( axisEClass, AXIS__LABEL );
		createEReference( axisEClass, AXIS__FORMAT_SPECIFIER );
		createEAttribute( axisEClass, AXIS__LABEL_POSITION );
		createEAttribute( axisEClass, AXIS__STAGGERED );
		createEReference( axisEClass, AXIS__MARKER_LINES );
		createEReference( axisEClass, AXIS__MARKER_RANGES );
		createEReference( axisEClass, AXIS__MAJOR_GRID );
		createEReference( axisEClass, AXIS__MINOR_GRID );
		createEReference( axisEClass, AXIS__SCALE );
		createEReference( axisEClass, AXIS__ORIGIN );
		createEAttribute( axisEClass, AXIS__PRIMARY_AXIS );
		createEAttribute( axisEClass, AXIS__CATEGORY_AXIS );
		createEAttribute( axisEClass, AXIS__PERCENT );

		chartPreferencesEClass = createEClass( CHART_PREFERENCES );
		createEReference( chartPreferencesEClass, CHART_PREFERENCES__LABELS );
		createEReference( chartPreferencesEClass, CHART_PREFERENCES__BLOCKS );

		curveFittingEClass = createEClass( CURVE_FITTING );
		createEReference( curveFittingEClass, CURVE_FITTING__LINE_ATTRIBUTES );
		createEReference( curveFittingEClass, CURVE_FITTING__LABEL );
		createEAttribute( curveFittingEClass, CURVE_FITTING__LABEL_ANCHOR );

		gridEClass = createEClass( GRID );
		createEReference( gridEClass, GRID__LINE_ATTRIBUTES );
		createEAttribute( gridEClass, GRID__TICK_STYLE );
		createEReference( gridEClass, GRID__TICK_ATTRIBUTES );
		createEAttribute( gridEClass, GRID__TICK_SIZE );
		createEAttribute( gridEClass, GRID__TICK_COUNT );

		labelEClass = createEClass( LABEL );
		createEReference( labelEClass, LABEL__CAPTION );
		createEReference( labelEClass, LABEL__BACKGROUND );
		createEReference( labelEClass, LABEL__OUTLINE );
		createEReference( labelEClass, LABEL__SHADOW_COLOR );
		createEReference( labelEClass, LABEL__INSETS );
		createEAttribute( labelEClass, LABEL__VISIBLE );

		markerLineEClass = createEClass( MARKER_LINE );
		createEReference( markerLineEClass, MARKER_LINE__LINE_ATTRIBUTES );
		createEReference( markerLineEClass, MARKER_LINE__VALUE );
		createEReference( markerLineEClass, MARKER_LINE__LABEL );
		createEAttribute( markerLineEClass, MARKER_LINE__LABEL_ANCHOR );
		createEReference( markerLineEClass, MARKER_LINE__FORMAT_SPECIFIER );

		markerRangeEClass = createEClass( MARKER_RANGE );
		createEReference( markerRangeEClass, MARKER_RANGE__OUTLINE );
		createEReference( markerRangeEClass, MARKER_RANGE__FILL );
		createEReference( markerRangeEClass, MARKER_RANGE__START_VALUE );
		createEReference( markerRangeEClass, MARKER_RANGE__END_VALUE );
		createEReference( markerRangeEClass, MARKER_RANGE__LABEL );
		createEAttribute( markerRangeEClass, MARKER_RANGE__LABEL_ANCHOR );
		createEReference( markerRangeEClass, MARKER_RANGE__FORMAT_SPECIFIER );

		scaleEClass = createEClass( SCALE );
		createEReference( scaleEClass, SCALE__MIN );
		createEReference( scaleEClass, SCALE__MAX );
		createEAttribute( scaleEClass, SCALE__STEP );
		createEAttribute( scaleEClass, SCALE__UNIT );
		createEAttribute( scaleEClass, SCALE__MINOR_GRIDS_PER_UNIT );

		seriesEClass = createEClass( SERIES );
		createEAttribute( seriesEClass, SERIES__VISIBLE );
		createEReference( seriesEClass, SERIES__LABEL );
		createEReference( seriesEClass, SERIES__DATA_DEFINITION );
		createEAttribute( seriesEClass, SERIES__SERIES_IDENTIFIER );
		createEReference( seriesEClass, SERIES__DATA_POINT );
		createEReference( seriesEClass, SERIES__DATA_SET );
		createEAttribute( seriesEClass, SERIES__LABEL_POSITION );
		createEAttribute( seriesEClass, SERIES__STACKED );
		createEReference( seriesEClass, SERIES__TRIGGERS );
		createEAttribute( seriesEClass, SERIES__TRANSLUCENT );
		createEReference( seriesEClass, SERIES__CURVE_FITTING );
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
		DataPackageImpl theDataPackage = (DataPackageImpl) EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI );
		XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl) EPackage.Registry.INSTANCE.getEPackage( XMLTypePackage.eNS_URI );
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI );

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass( axisEClass,
				Axis.class,
				"Axis", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getAxis_Type( ),
				theAttributePackage.getAxisType( ),
				"type", //$NON-NLS-1$
				"Linear", //$NON-NLS-1$
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_Title( ),
				this.getLabel( ),
				null,
				"title", //$NON-NLS-1$
				null,
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_SubTitle( ),
				this.getLabel( ),
				null,
				"subTitle", //$NON-NLS-1$
				null,
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_TitlePosition( ),
				theAttributePackage.getPosition( ),
				"titlePosition", //$NON-NLS-1$
				"Above", //$NON-NLS-1$
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_AssociatedAxes( ),
				this.getAxis( ),
				null,
				"associatedAxes", //$NON-NLS-1$
				null,
				0,
				-1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_SeriesDefinitions( ),
				theDataPackage.getSeriesDefinition( ),
				null,
				"seriesDefinitions", //$NON-NLS-1$
				null,
				1,
				-1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_GapWidth( ),
				theXMLTypePackage.getDouble( ),
				"gapWidth", //$NON-NLS-1$
				null,
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_Orientation( ),
				theAttributePackage.getOrientation( ),
				"orientation", //$NON-NLS-1$
				"Horizontal", //$NON-NLS-1$
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_LineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"lineAttributes", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_Label( ),
				this.getLabel( ),
				null,
				"label", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_FormatSpecifier( ),
				theAttributePackage.getFormatSpecifier( ),
				null,
				"formatSpecifier", //$NON-NLS-1$
				null,
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_LabelPosition( ),
				theAttributePackage.getPosition( ),
				"labelPosition", //$NON-NLS-1$
				"Above", //$NON-NLS-1$
				0,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_Staggered( ),
				theXMLTypePackage.getBoolean( ),
				"staggered", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_MarkerLines( ),
				this.getMarkerLine( ),
				null,
				"markerLines", //$NON-NLS-1$
				null,
				0,
				-1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_MarkerRanges( ),
				this.getMarkerRange( ),
				null,
				"markerRanges", //$NON-NLS-1$
				null,
				0,
				-1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_MajorGrid( ),
				this.getGrid( ),
				null,
				"majorGrid", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_MinorGrid( ),
				this.getGrid( ),
				null,
				"minorGrid", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_Scale( ),
				this.getScale( ),
				null,
				"scale", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getAxis_Origin( ),
				theAttributePackage.getAxisOrigin( ),
				null,
				"origin", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_PrimaryAxis( ),
				theXMLTypePackage.getBoolean( ),
				"primaryAxis", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_CategoryAxis( ),
				theXMLTypePackage.getBoolean( ),
				"categoryAxis", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getAxis_Percent( ),
				theXMLTypePackage.getBoolean( ),
				"percent", //$NON-NLS-1$
				null,
				1,
				1,
				Axis.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( chartPreferencesEClass,
				ChartPreferences.class,
				"ChartPreferences", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getChartPreferences_Labels( ),
				this.getLabel( ),
				null,
				"labels", //$NON-NLS-1$
				null,
				1,
				-1,
				ChartPreferences.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getChartPreferences_Blocks( ),
				theLayoutPackage.getBlock( ),
				null,
				"blocks", //$NON-NLS-1$
				null,
				1,
				-1,
				ChartPreferences.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( curveFittingEClass,
				CurveFitting.class,
				"CurveFitting", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getCurveFitting_LineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"lineAttributes", //$NON-NLS-1$
				null,
				1,
				1,
				CurveFitting.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getCurveFitting_Label( ),
				this.getLabel( ),
				null,
				"label", //$NON-NLS-1$
				null,
				1,
				1,
				CurveFitting.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getCurveFitting_LabelAnchor( ),
				theAttributePackage.getAnchor( ),
				"labelAnchor", //$NON-NLS-1$
				"North", //$NON-NLS-1$
				0,
				1,
				CurveFitting.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( gridEClass,
				Grid.class,
				"Grid", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getGrid_LineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"lineAttributes", //$NON-NLS-1$
				null,
				1,
				1,
				Grid.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getGrid_TickStyle( ),
				theAttributePackage.getTickStyle( ),
				"tickStyle", //$NON-NLS-1$
				"Left", //$NON-NLS-1$
				1,
				1,
				Grid.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getGrid_TickAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"tickAttributes", //$NON-NLS-1$
				null,
				0,
				1,
				Grid.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getGrid_TickSize( ),
				theXMLTypePackage.getDouble( ),
				"tickSize", //$NON-NLS-1$
				null,
				0,
				1,
				Grid.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getGrid_TickCount( ),
				theXMLTypePackage.getInt( ),
				"tickCount", //$NON-NLS-1$
				null,
				1,
				1,
				Grid.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( labelEClass,
				Label.class,
				"Label", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getLabel_Caption( ),
				theAttributePackage.getText( ),
				null,
				"caption", //$NON-NLS-1$
				null,
				1,
				1,
				Label.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getLabel_Background( ),
				theAttributePackage.getFill( ),
				null,
				"background", //$NON-NLS-1$
				null,
				1,
				1,
				Label.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getLabel_Outline( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"outline", //$NON-NLS-1$
				null,
				1,
				1,
				Label.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getLabel_ShadowColor( ),
				theAttributePackage.getColorDefinition( ),
				null,
				"shadowColor", //$NON-NLS-1$
				null,
				1,
				1,
				Label.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getLabel_Insets( ),
				theAttributePackage.getInsets( ),
				null,
				"insets", //$NON-NLS-1$
				null,
				1,
				1,
				Label.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getLabel_Visible( ),
				theXMLTypePackage.getBoolean( ),
				"visible", //$NON-NLS-1$
				null,
				1,
				1,
				Label.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( markerLineEClass,
				MarkerLine.class,
				"MarkerLine", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getMarkerLine_LineAttributes( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"lineAttributes", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerLine.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerLine_Value( ),
				theDataPackage.getDataElement( ),
				null,
				"value", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerLine.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerLine_Label( ),
				this.getLabel( ),
				null,
				"label", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerLine.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getMarkerLine_LabelAnchor( ),
				theAttributePackage.getAnchor( ),
				"labelAnchor", //$NON-NLS-1$
				"North", //$NON-NLS-1$
				1,
				1,
				MarkerLine.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerLine_FormatSpecifier( ),
				theAttributePackage.getFormatSpecifier( ),
				null,
				"formatSpecifier", //$NON-NLS-1$
				null,
				0,
				1,
				MarkerLine.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( markerRangeEClass,
				MarkerRange.class,
				"MarkerRange", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getMarkerRange_Outline( ),
				theAttributePackage.getLineAttributes( ),
				null,
				"outline", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerRange_Fill( ),
				theAttributePackage.getFill( ),
				null,
				"fill", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerRange_StartValue( ),
				theDataPackage.getDataElement( ),
				null,
				"startValue", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerRange_EndValue( ),
				theDataPackage.getDataElement( ),
				null,
				"endValue", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerRange_Label( ),
				this.getLabel( ),
				null,
				"label", //$NON-NLS-1$
				null,
				1,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getMarkerRange_LabelAnchor( ),
				theAttributePackage.getAnchor( ),
				"labelAnchor", //$NON-NLS-1$
				"North", //$NON-NLS-1$
				1,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getMarkerRange_FormatSpecifier( ),
				theAttributePackage.getFormatSpecifier( ),
				null,
				"formatSpecifier", //$NON-NLS-1$
				null,
				0,
				1,
				MarkerRange.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( scaleEClass,
				Scale.class,
				"Scale", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEReference( getScale_Min( ),
				theDataPackage.getDataElement( ),
				null,
				"min", //$NON-NLS-1$
				null,
				1,
				1,
				Scale.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getScale_Max( ),
				theDataPackage.getDataElement( ),
				null,
				"max", //$NON-NLS-1$
				null,
				1,
				1,
				Scale.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getScale_Step( ),
				theXMLTypePackage.getDouble( ),
				"step", //$NON-NLS-1$
				null,
				1,
				1,
				Scale.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getScale_Unit( ),
				theAttributePackage.getScaleUnitType( ),
				"unit", //$NON-NLS-1$
				"Seconds", //$NON-NLS-1$
				1,
				1,
				Scale.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getScale_MinorGridsPerUnit( ),
				theXMLTypePackage.getInt( ),
				"minorGridsPerUnit", //$NON-NLS-1$
				null,
				1,
				1,
				Scale.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );

		initEClass( seriesEClass,
				Series.class,
				"Series", //$NON-NLS-1$
				!IS_ABSTRACT,
				!IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS );
		initEAttribute( getSeries_Visible( ),
				theXMLTypePackage.getBoolean( ),
				"visible", //$NON-NLS-1$
				"true", //$NON-NLS-1$
				0,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeries_Label( ),
				this.getLabel( ),
				null,
				"label", //$NON-NLS-1$
				null,
				1,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeries_DataDefinition( ),
				theDataPackage.getQuery( ),
				null,
				"dataDefinition", //$NON-NLS-1$
				null,
				1,
				-1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeries_SeriesIdentifier( ),
				theXMLTypePackage.getAnySimpleType( ),
				"seriesIdentifier", //$NON-NLS-1$
				null,
				1,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeries_DataPoint( ),
				theAttributePackage.getDataPoint( ),
				null,
				"dataPoint", //$NON-NLS-1$
				null,
				1,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeries_DataSet( ),
				theDataPackage.getDataSet( ),
				null,
				"dataSet", //$NON-NLS-1$
				null,
				1,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeries_LabelPosition( ),
				theAttributePackage.getPosition( ),
				"labelPosition", //$NON-NLS-1$
				"Above", //$NON-NLS-1$
				0,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeries_Stacked( ),
				theXMLTypePackage.getBoolean( ),
				"stacked", //$NON-NLS-1$
				null,
				0,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeries_Triggers( ),
				theDataPackage.getTrigger( ),
				null,
				"triggers", //$NON-NLS-1$
				null,
				0,
				-1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE,
				IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEAttribute( getSeries_Translucent( ),
				theXMLTypePackage.getBoolean( ),
				"translucent", //$NON-NLS-1$
				null,
				1,
				1,
				Series.class,
				!IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE,
				IS_UNSETTABLE,
				!IS_ID,
				!IS_UNIQUE,
				!IS_DERIVED,
				IS_ORDERED );
		initEReference( getSeries_CurveFitting( ),
				this.getCurveFitting( ),
				null,
				"curveFitting", //$NON-NLS-1$
				null,
				0,
				1,
				Series.class,
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
		addAnnotation( axisEClass, source, new String[]{
				"name", "Axis", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Type( ), source, new String[]{
				"kind", "element", "name", "Type" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Title( ), source, new String[]{
				"kind", "element", "name", "Title" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_SubTitle( ), source, new String[]{
				"kind", "element", "name", "SubTitle" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_TitlePosition( ), source, new String[]{
				"kind", "element", "name", "TitlePosition" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_AssociatedAxes( ), source, new String[]{
				"kind", "element", "name", "AssociatedAxes" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_SeriesDefinitions( ), source, new String[]{
				"kind", "element", "name", "SeriesDefinitions" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_GapWidth( ), source, new String[]{
				"kind", "element", "name", "GapWidth" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Orientation( ), source, new String[]{
				"kind", "element", "name", "Orientation" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_LineAttributes( ), source, new String[]{
				"kind", "element", "name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Label( ), source, new String[]{
				"kind", "element", "name", "Label" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_FormatSpecifier( ), source, new String[]{
				"kind", "element", "name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_LabelPosition( ), source, new String[]{
				"kind", "element", "name", "LabelPosition" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Staggered( ), source, new String[]{
				"kind", "element", "name", "Staggered" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_MarkerLines( ), source, new String[]{
				"kind", "element", "name", "MarkerLines" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_MarkerRanges( ), source, new String[]{
				"kind", "element", "name", "MarkerRanges" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_MajorGrid( ), source, new String[]{
				"kind", "element", "name", "MajorGrid" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_MinorGrid( ), source, new String[]{
				"kind", "element", "name", "MinorGrid" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Scale( ), source, new String[]{
				"kind", "element", "name", "Scale" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Origin( ), source, new String[]{
				"kind", "element", "name", "Origin" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_PrimaryAxis( ), source, new String[]{
				"kind", "element", "name", "PrimaryAxis" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_CategoryAxis( ), source, new String[]{
				"kind", "element", "name", "CategoryAxis" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getAxis_Percent( ), source, new String[]{
				"kind", "element", "name", "Percent" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( chartPreferencesEClass, source, new String[]{
				"name", "ChartPreferences", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getChartPreferences_Labels( ), source, new String[]{
				"kind", "element", "name", "Labels" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getChartPreferences_Blocks( ), source, new String[]{
				"kind", "element", "name", "Blocks" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( curveFittingEClass, source, new String[]{
				"name", "CurveFitting", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getCurveFitting_LineAttributes( ), source, new String[]{
				"kind", "element", "name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getCurveFitting_Label( ), source, new String[]{
				"kind", "element", "name", "Label" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getCurveFitting_LabelAnchor( ), source, new String[]{
				"kind", "element", "name", "LabelAnchor" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( gridEClass, source, new String[]{
				"name", "Grid", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getGrid_LineAttributes( ), source, new String[]{
				"kind", "element", "name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getGrid_TickStyle( ), source, new String[]{
				"kind", "element", "name", "TickStyle" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getGrid_TickAttributes( ), source, new String[]{
				"kind", "element", "name", "TickAttributes" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getGrid_TickSize( ), source, new String[]{
				"kind", "element", "name", "TickSize" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getGrid_TickCount( ), source, new String[]{
				"kind", "element", "name", "TickCount" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( labelEClass, source, new String[]{
				"name", "Label", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getLabel_Caption( ), source, new String[]{
				"kind", "element", "name", "Caption" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getLabel_Background( ), source, new String[]{
				"kind", "element", "name", "Background" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getLabel_Outline( ), source, new String[]{
				"kind", "element", "name", "Outline" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getLabel_ShadowColor( ), source, new String[]{
				"kind", "element", "name", "ShadowColor" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getLabel_Insets( ), source, new String[]{
				"kind", "element", "name", "Insets" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getLabel_Visible( ), source, new String[]{
				"kind", "element", "name", "Visible" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( markerLineEClass, source, new String[]{
				"name", "MarkerLine", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerLine_LineAttributes( ), source, new String[]{
				"kind", "element", "name", "LineAttributes" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerLine_Value( ), source, new String[]{
				"kind", "element", "name", "Value" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerLine_Label( ), source, new String[]{
				"kind", "element", "name", "Label" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerLine_LabelAnchor( ), source, new String[]{
				"kind", "element", "name", "LabelAnchor" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerLine_FormatSpecifier( ), source, new String[]{
				"kind", "element", "name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( markerRangeEClass, source, new String[]{
				"name", "MarkerRange", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_Outline( ), source, new String[]{
				"kind", "element", "name", "Outline" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_Fill( ), source, new String[]{
				"kind", "element", "name", "Fill" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_StartValue( ), source, new String[]{
				"kind", "element", "name", "StartValue" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_EndValue( ), source, new String[]{
				"kind", "element", "name", "EndValue" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_Label( ), source, new String[]{
				"kind", "element", "name", "Label" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_LabelAnchor( ), source, new String[]{
				"kind", "element", "name", "LabelAnchor" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getMarkerRange_FormatSpecifier( ), source, new String[]{
				"kind", "element", "name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( scaleEClass, source, new String[]{
				"name", "Scale", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getScale_Min( ), source, new String[]{
				"kind", "element", "name", "Min" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getScale_Max( ), source, new String[]{
				"kind", "element", "name", "Max" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getScale_Step( ), source, new String[]{
				"kind", "element", "name", "Step" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getScale_Unit( ), source, new String[]{
				"kind", "element", "name", "Unit" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getScale_MinorGridsPerUnit( ), source, new String[]{
				"kind", "element", "name", "MinorGridsPerUnit" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( seriesEClass, source, new String[]{
				"name", "Series", "kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_Visible( ), source, new String[]{
				"kind", "element", "name", "Visible" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_Label( ), source, new String[]{
				"kind", "element", "name", "Label" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_DataDefinition( ), source, new String[]{
				"kind", "element", "name", "DataDefinition" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_SeriesIdentifier( ), source, new String[]{
				"kind", "element", "name", "SeriesIdentifier" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_DataPoint( ), source, new String[]{
				"kind", "element", "name", "DataPoint" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_DataSet( ), source, new String[]{
				"kind", "element", "name", "DataSet" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_LabelPosition( ), source, new String[]{
				"kind", "element", "name", "LabelPosition" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_Stacked( ), source, new String[]{
				"kind", "element", "name", "Stacked" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_Triggers( ), source, new String[]{
				"kind", "element", "name", "Triggers" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_Translucent( ), source, new String[]{
				"kind", "element", "name", "Translucent" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
		addAnnotation( getSeries_CurveFitting( ), source, new String[]{
				"kind", "element", "name", "CurveFitting" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} );
	}

} // ComponentPackageImpl
