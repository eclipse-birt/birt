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

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SampleDataImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.layout.impl.BlockImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

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
	protected boolean dialSuperimpositionESet;

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
	@Override
	protected EClass eStaticClass( )
	{
		return ModelPackage.Literals.DIAL_CHART;
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				return isDialSuperimposition( ) ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				setDialSuperimposition( ( (Boolean) newValue ).booleanValue( ) );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				unsetDialSuperimposition( );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case ModelPackage.DIAL_CHART__DIAL_SUPERIMPOSITION :
				return isSetDialSuperimposition( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static DialChart copyInstance( DialChart src )
	{
		if ( src == null )
		{
			return null;
		}

		DialChartImpl dest = new DialChartImpl( );

		if ( src.getDescription( ) != null )
		{
			dest.setDescription( TextImpl.copyInstance( src.getDescription( ) ) );
		}

		if ( src.getBlock( ) != null )
		{
			dest.setBlock( BlockImpl.copyInstance( src.getBlock( ) ) );
		}

		if ( src.getExtendedProperties( ) != null )
		{
			EList<ExtendedProperty> list = dest.getExtendedProperties( );
			for ( ExtendedProperty element : src.getExtendedProperties( ) )
			{
				list.add( ExtendedPropertyImpl.copyInstance( element ) );
			}
		}

		if ( src.getSampleData( ) != null )
		{
			dest.setSampleData( SampleDataImpl.copyInstance( src.getSampleData( ) ) );
		}

		if ( src.getStyles( ) != null )
		{
			EList<StyleMap> list = dest.getStyles( );
			for ( StyleMap element : src.getStyles( ) )
			{
				list.add( StyleMapImpl.copyInstance( element ) );
			}
		}

		if ( src.getInteractivity( ) != null )
		{
			dest.setInteractivity( InteractivityImpl.copyInstance( src.getInteractivity( ) ) );
		}

		if ( src.getEmptyMessage( ) != null )
		{
			dest.setEmptyMessage( LabelImpl.copyInstance( src.getEmptyMessage( ) ) );
		}

		if ( src.getSeriesDefinitions( ) != null )
		{
			EList<SeriesDefinition> list = dest.getSeriesDefinitions( );
			for ( SeriesDefinition element : src.getSeriesDefinitions( ) )
			{
				list.add( SeriesDefinitionImpl.copyInstance( element ) );
			}
		}

		dest.version = src.getVersion( );
		dest.versionESet = src.isSetVersion( );
		dest.type = src.getType( );
		dest.subType = src.getSubType( );
		dest.dimension = src.getDimension( );
		dest.dimensionESet = src.isSetDimension( );
		dest.script = src.getScript( );
		dest.units = src.getUnits( );
		dest.seriesThickness = src.getSeriesThickness( );
		dest.seriesThicknessESet = src.isSetSeriesThickness( );
		dest.gridColumnCount = src.getGridColumnCount( );
		dest.gridColumnCountESet = src.isSetGridColumnCount( );
		dest.minSlice = src.getMinSlice( );
		dest.minSliceESet = src.isSetMinSlice( );
		dest.minSlicePercent = src.isMinSlicePercent( );
		dest.minSlicePercentESet = src.isSetMinSlicePercent( );
		dest.minSliceLabel = src.getMinSliceLabel( );
		dest.coverage = src.getCoverage( );
		dest.coverageESet = src.isSetCoverage( );
		dest.dialSuperimposition = src.isDialSuperimposition( );
		dest.dialSuperimpositionESet = src.isSetDialSuperimposition( );

		return dest;
	}

} // DialChartImpl
