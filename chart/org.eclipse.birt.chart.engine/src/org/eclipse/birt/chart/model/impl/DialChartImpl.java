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

package org.eclipse.birt.chart.model.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;

import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Text;

import org.eclipse.birt.chart.model.data.SampleData;

import org.eclipse.birt.chart.model.layout.Block;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dial Chart</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.impl.DialChartImpl#isDialSuperimposition <em>Dial Superimposition</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DialChartImpl extends ChartWithoutAxesImpl implements DialChart
{

	/**
	 * The default value of the '{@link #isDialSuperimposition() <em>Dial Superimposition</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isDialSuperimposition()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DIAL_SUPERIMPOSITION_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isDialSuperimposition() <em>Dial Superimposition</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isDialSuperimposition()
	 * @generated
	 * @ordered
	 */
	protected boolean dialSuperimposition = DIAL_SUPERIMPOSITION_EDEFAULT;

	/**
	 * This is true if the Dial Superimposition attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean dialSuperimpositionESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DialChartImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return ModelPackage.eINSTANCE.getDialChart( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDialSuperimposition( )
	{
		return dialSuperimposition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDialSuperimposition( boolean newDialSuperimposition )
	{
		boolean oldDialSuperimposition = dialSuperimposition;
		dialSuperimposition = newDialSuperimposition;
		boolean oldDialSuperimpositionESet = dialSuperimpositionESet;
		dialSuperimpositionESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION,
					oldDialSuperimposition,
					dialSuperimposition,
					!oldDialSuperimpositionESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDialSuperimposition( )
	{
		boolean oldDialSuperimposition = dialSuperimposition;
		boolean oldDialSuperimpositionESet = dialSuperimpositionESet;
		dialSuperimposition = DIAL_SUPERIMPOSITION_EDEFAULT;
		dialSuperimpositionESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION,
					oldDialSuperimposition,
					DIAL_SUPERIMPOSITION_EDEFAULT,
					oldDialSuperimpositionESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDialSuperimposition( )
	{
		return dialSuperimpositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, Class baseClass, NotificationChain msgs )
	{
		if ( featureID >= 0 )
		{
			switch ( eDerivedStructuralFeatureID( featureID, baseClass ) )
			{
				case ModelPackage.DIAL_CHART__DESCRIPTION :
					return basicSetDescription( null, msgs );
				case ModelPackage.DIAL_CHART__BLOCK :
					return basicSetBlock( null, msgs );
				case ModelPackage.DIAL_CHART__EXTENDED_PROPERTIES :
					return ( (InternalEList) getExtendedProperties( ) ).basicRemove( otherEnd,
							msgs );
				case ModelPackage.DIAL_CHART__SAMPLE_DATA :
					return basicSetSampleData( null, msgs );
				case ModelPackage.DIAL_CHART__STYLES :
					return ( (InternalEList) getStyles( ) ).basicRemove( otherEnd,
							msgs );
				case ModelPackage.DIAL_CHART__SERIES_DEFINITIONS :
					return ( (InternalEList) getSeriesDefinitions( ) ).basicRemove( otherEnd,
							msgs );
				default :
					return eDynamicInverseRemove( otherEnd,
							featureID,
							baseClass,
							msgs );
			}
		}
		return eBasicSetContainer( null, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ModelPackage.DIAL_CHART__VERSION :
				return getVersion( );
			case ModelPackage.DIAL_CHART__TYPE :
				return getType( );
			case ModelPackage.DIAL_CHART__SUB_TYPE :
				return getSubType( );
			case ModelPackage.DIAL_CHART__DESCRIPTION :
				return getDescription( );
			case ModelPackage.DIAL_CHART__BLOCK :
				return getBlock( );
			case ModelPackage.DIAL_CHART__DIMENSION :
				return getDimension( );
			case ModelPackage.DIAL_CHART__SCRIPT :
				return getScript( );
			case ModelPackage.DIAL_CHART__UNITS :
				return getUnits( );
			case ModelPackage.DIAL_CHART__SERIES_THICKNESS :
				return new Double( getSeriesThickness( ) );
			case ModelPackage.DIAL_CHART__GRID_COLUMN_COUNT :
				return new Integer( getGridColumnCount( ) );
			case ModelPackage.DIAL_CHART__EXTENDED_PROPERTIES :
				return getExtendedProperties( );
			case ModelPackage.DIAL_CHART__SAMPLE_DATA :
				return getSampleData( );
			case ModelPackage.DIAL_CHART__STYLES :
				return getStyles( );
			case ModelPackage.DIAL_CHART__SERIES_DEFINITIONS :
				return getSeriesDefinitions( );
			case ModelPackage.DIAL_CHART__MIN_SLICE :
				return new Double( getMinSlice( ) );
			case ModelPackage.DIAL_CHART__MIN_SLICE_PERCENT :
				return isMinSlicePercent( ) ? Boolean.TRUE : Boolean.FALSE;
			case ModelPackage.DIAL_CHART__MIN_SLICE_LABEL :
				return getMinSliceLabel( );
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				return isDialSuperimposition( ) ? Boolean.TRUE : Boolean.FALSE;
		}
		return eDynamicGet( eFeature, resolve );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( EStructuralFeature eFeature, Object newValue )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ModelPackage.DIAL_CHART__VERSION :
				setVersion( (String) newValue );
				return;
			case ModelPackage.DIAL_CHART__TYPE :
				setType( (String) newValue );
				return;
			case ModelPackage.DIAL_CHART__SUB_TYPE :
				setSubType( (String) newValue );
				return;
			case ModelPackage.DIAL_CHART__DESCRIPTION :
				setDescription( (Text) newValue );
				return;
			case ModelPackage.DIAL_CHART__BLOCK :
				setBlock( (Block) newValue );
				return;
			case ModelPackage.DIAL_CHART__DIMENSION :
				setDimension( (ChartDimension) newValue );
				return;
			case ModelPackage.DIAL_CHART__SCRIPT :
				setScript( (String) newValue );
				return;
			case ModelPackage.DIAL_CHART__UNITS :
				setUnits( (String) newValue );
				return;
			case ModelPackage.DIAL_CHART__SERIES_THICKNESS :
				setSeriesThickness( ( (Double) newValue ).doubleValue( ) );
				return;
			case ModelPackage.DIAL_CHART__GRID_COLUMN_COUNT :
				setGridColumnCount( ( (Integer) newValue ).intValue( ) );
				return;
			case ModelPackage.DIAL_CHART__EXTENDED_PROPERTIES :
				getExtendedProperties( ).clear( );
				getExtendedProperties( ).addAll( (Collection) newValue );
				return;
			case ModelPackage.DIAL_CHART__SAMPLE_DATA :
				setSampleData( (SampleData) newValue );
				return;
			case ModelPackage.DIAL_CHART__STYLES :
				getStyles( ).clear( );
				getStyles( ).addAll( (Collection) newValue );
				return;
			case ModelPackage.DIAL_CHART__SERIES_DEFINITIONS :
				getSeriesDefinitions( ).clear( );
				getSeriesDefinitions( ).addAll( (Collection) newValue );
				return;
			case ModelPackage.DIAL_CHART__MIN_SLICE :
				setMinSlice( ( (Double) newValue ).doubleValue( ) );
				return;
			case ModelPackage.DIAL_CHART__MIN_SLICE_PERCENT :
				setMinSlicePercent( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case ModelPackage.DIAL_CHART__MIN_SLICE_LABEL :
				setMinSliceLabel( (String) newValue );
				return;
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				setDialSuperimposition( ( (Boolean) newValue ).booleanValue( ) );
				return;
		}
		eDynamicSet( eFeature, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ModelPackage.DIAL_CHART__VERSION :
				unsetVersion( );
				return;
			case ModelPackage.DIAL_CHART__TYPE :
				setType( TYPE_EDEFAULT );
				return;
			case ModelPackage.DIAL_CHART__SUB_TYPE :
				setSubType( SUB_TYPE_EDEFAULT );
				return;
			case ModelPackage.DIAL_CHART__DESCRIPTION :
				setDescription( (Text) null );
				return;
			case ModelPackage.DIAL_CHART__BLOCK :
				setBlock( (Block) null );
				return;
			case ModelPackage.DIAL_CHART__DIMENSION :
				unsetDimension( );
				return;
			case ModelPackage.DIAL_CHART__SCRIPT :
				setScript( SCRIPT_EDEFAULT );
				return;
			case ModelPackage.DIAL_CHART__UNITS :
				setUnits( UNITS_EDEFAULT );
				return;
			case ModelPackage.DIAL_CHART__SERIES_THICKNESS :
				unsetSeriesThickness( );
				return;
			case ModelPackage.DIAL_CHART__GRID_COLUMN_COUNT :
				unsetGridColumnCount( );
				return;
			case ModelPackage.DIAL_CHART__EXTENDED_PROPERTIES :
				getExtendedProperties( ).clear( );
				return;
			case ModelPackage.DIAL_CHART__SAMPLE_DATA :
				setSampleData( (SampleData) null );
				return;
			case ModelPackage.DIAL_CHART__STYLES :
				getStyles( ).clear( );
				return;
			case ModelPackage.DIAL_CHART__SERIES_DEFINITIONS :
				getSeriesDefinitions( ).clear( );
				return;
			case ModelPackage.DIAL_CHART__MIN_SLICE :
				unsetMinSlice( );
				return;
			case ModelPackage.DIAL_CHART__MIN_SLICE_PERCENT :
				unsetMinSlicePercent( );
				return;
			case ModelPackage.DIAL_CHART__MIN_SLICE_LABEL :
				setMinSliceLabel( MIN_SLICE_LABEL_EDEFAULT );
				return;
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				unsetDialSuperimposition( );
				return;
		}
		eDynamicUnset( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ModelPackage.DIAL_CHART__VERSION :
				return isSetVersion( );
			case ModelPackage.DIAL_CHART__TYPE :
				return TYPE_EDEFAULT == null ? type != null
						: !TYPE_EDEFAULT.equals( type );
			case ModelPackage.DIAL_CHART__SUB_TYPE :
				return SUB_TYPE_EDEFAULT == null ? subType != null
						: !SUB_TYPE_EDEFAULT.equals( subType );
			case ModelPackage.DIAL_CHART__DESCRIPTION :
				return description != null;
			case ModelPackage.DIAL_CHART__BLOCK :
				return block != null;
			case ModelPackage.DIAL_CHART__DIMENSION :
				return isSetDimension( );
			case ModelPackage.DIAL_CHART__SCRIPT :
				return SCRIPT_EDEFAULT == null ? script != null
						: !SCRIPT_EDEFAULT.equals( script );
			case ModelPackage.DIAL_CHART__UNITS :
				return UNITS_EDEFAULT == null ? units != null
						: !UNITS_EDEFAULT.equals( units );
			case ModelPackage.DIAL_CHART__SERIES_THICKNESS :
				return isSetSeriesThickness( );
			case ModelPackage.DIAL_CHART__GRID_COLUMN_COUNT :
				return isSetGridColumnCount( );
			case ModelPackage.DIAL_CHART__EXTENDED_PROPERTIES :
				return extendedProperties != null
						&& !extendedProperties.isEmpty( );
			case ModelPackage.DIAL_CHART__SAMPLE_DATA :
				return sampleData != null;
			case ModelPackage.DIAL_CHART__STYLES :
				return styles != null && !styles.isEmpty( );
			case ModelPackage.DIAL_CHART__SERIES_DEFINITIONS :
				return seriesDefinitions != null
						&& !seriesDefinitions.isEmpty( );
			case ModelPackage.DIAL_CHART__MIN_SLICE :
				return isSetMinSlice( );
			case ModelPackage.DIAL_CHART__MIN_SLICE_PERCENT :
				return isSetMinSlicePercent( );
			case ModelPackage.DIAL_CHART__MIN_SLICE_LABEL :
				return MIN_SLICE_LABEL_EDEFAULT == null ? minSliceLabel != null
						: !MIN_SLICE_LABEL_EDEFAULT.equals( minSliceLabel );
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				return isSetDialSuperimposition( );
		}
		return eDynamicIsSet( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (dialSuperimposition: " ); //$NON-NLS-1$
		if ( dialSuperimpositionESet )
			result.append( dialSuperimposition );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#create()
	 */
	public static final ChartWithoutAxes create( )
	{
		final DialChart dc = ModelFactory.eINSTANCE.createDialChart( );
		( (DialChartImpl) dc ).initialize( );
		return dc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.model.impl.ChartImpl#initialize()
	 */
	protected void initialize( )
	{
		super.initialize( );
	}

} // DialChartImpl
