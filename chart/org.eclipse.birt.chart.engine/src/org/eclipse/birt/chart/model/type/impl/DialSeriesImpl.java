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

import java.util.Map;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.attribute.impl.CursorImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.DialImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.NeedleImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.DataSetImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dial Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl#getDial <em>Dial</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl#getNeedle <em>Needle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DialSeriesImpl extends SeriesImpl implements DialSeries
{

	/**
	 * The cached value of the '{@link #getDial() <em>Dial</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDial()
	 * @generated
	 * @ordered
	 */
	protected Dial dial;

	/**
	 * The cached value of the '{@link #getNeedle() <em>Needle</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNeedle()
	 * @generated
	 * @ordered
	 */
	protected Needle needle;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DialSeriesImpl( )
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
		return TypePackage.Literals.DIAL_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Dial getDial( )
	{
		return dial;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDial( Dial newDial, NotificationChain msgs )
	{
		Dial oldDial = dial;
		dial = newDial;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__DIAL,
					oldDial,
					newDial );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDial( Dial newDial )
	{
		if ( newDial != dial )
		{
			NotificationChain msgs = null;
			if ( dial != null )
				msgs = ( (InternalEObject) dial ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__DIAL,
						null,
						msgs );
			if ( newDial != null )
				msgs = ( (InternalEObject) newDial ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__DIAL,
						null,
						msgs );
			msgs = basicSetDial( newDial, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__DIAL,
					newDial,
					newDial ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Needle getNeedle( )
	{
		return needle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNeedle( Needle newNeedle,
			NotificationChain msgs )
	{
		Needle oldNeedle = needle;
		needle = newNeedle;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__NEEDLE,
					oldNeedle,
					newNeedle );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setNeedle( Needle newNeedle )
	{
		if ( newNeedle != needle )
		{
			NotificationChain msgs = null;
			if ( needle != null )
				msgs = ( (InternalEObject) needle ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.DIAL_SERIES__NEEDLE,
						null,
						msgs );
			if ( newNeedle != null )
				msgs = ( (InternalEObject) newNeedle ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.DIAL_SERIES__NEEDLE,
						null,
						msgs );
			msgs = basicSetNeedle( newNeedle, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__NEEDLE,
					newNeedle,
					newNeedle ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case TypePackage.DIAL_SERIES__DIAL :
				return basicSetDial( null, msgs );
			case TypePackage.DIAL_SERIES__NEEDLE :
				return basicSetNeedle( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
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
			case TypePackage.DIAL_SERIES__DIAL :
				return getDial( );
			case TypePackage.DIAL_SERIES__NEEDLE :
				return getNeedle( );
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
			case TypePackage.DIAL_SERIES__DIAL :
				setDial( (Dial) newValue );
				return;
			case TypePackage.DIAL_SERIES__NEEDLE :
				setNeedle( (Needle) newValue );
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
			case TypePackage.DIAL_SERIES__DIAL :
				setDial( (Dial) null );
				return;
			case TypePackage.DIAL_SERIES__NEEDLE :
				setNeedle( (Needle) null );
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
			case TypePackage.DIAL_SERIES__DIAL :
				return dial != null;
			case TypePackage.DIAL_SERIES__NEEDLE :
				return needle != null;
		}
		return super.eIsSet( featureID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#create()
	 */
	public static final Series create( )
	{
		final DialSeries ds = TypeFactory.eINSTANCE.createDialSeries( );
		( (DialSeriesImpl) ds ).initialize( );
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	protected final void initialize( )
	{
		super.initialize( );

		getLabel( ).setVisible( true );

		setDial( DialImpl.create( ) );
		setNeedle( NeedleImpl.create( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "DialSeriesImpl.displayName" ); //$NON-NLS-1$
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static DialSeries copyInstance( DialSeries src )
	{
		if ( src == null )
		{
			return null;
		}

		DialSeriesImpl dest = new DialSeriesImpl( );

		if ( src.getLabel( ) != null )
		{
			dest.setLabel( LabelImpl.copyInstance( src.getLabel( ) ) );
		}

		if ( src.getDataDefinition( ) != null )
		{
			EList<Query> list = dest.getDataDefinition( );
			for ( Query element : src.getDataDefinition( ) )
			{
				list.add( QueryImpl.copyInstance( element ) );
			}
		}

		if ( src.getDataPoint( ) != null )
		{
			dest.setDataPoint( DataPointImpl.copyInstance( src.getDataPoint( ) ) );
		}

		if ( src.getDataSets( ) != null )
		{
			EMap<String, DataSet> map = dest.getDataSets( );
			for ( Map.Entry<String, DataSet> entry : src.getDataSets( )
					.entrySet( ) )
			{
				map.put( entry.getKey( ),
						DataSetImpl.copyInstance( entry.getValue( ) ) );
			}
		}

		if ( src.getTriggers( ) != null )
		{
			EList<Trigger> list = dest.getTriggers( );
			for ( Trigger element : src.getTriggers( ) )
			{
				list.add( TriggerImpl.copyInstance( element ) );
			}
		}

		if ( src.getCurveFitting( ) != null )
		{
			dest.setCurveFitting( CurveFittingImpl.copyInstance( src.getCurveFitting( ) ) );
		}

		if ( src.getCursor( ) != null )
		{
			dest.setCursor( CursorImpl.copyInstance( src.getCursor( ) ) );
		}

		if ( src.getDial( ) != null )
		{
			dest.setDial( DialImpl.copyInstance( src.getDial( ) ) );
		}

		if ( src.getNeedle( ) != null )
		{
			dest.setNeedle( NeedleImpl.copyInstance( src.getNeedle( ) ) );
		}

		dest.visible = src.isVisible( );
		dest.visibleESet = src.isSetVisible( );
		dest.seriesIdentifier = src.getSeriesIdentifier( );
		dest.labelPosition = src.getLabelPosition( );
		dest.labelPositionESet = src.isSetLabelPosition( );
		dest.stacked = src.isStacked( );
		dest.stackedESet = src.isSetStacked( );
		dest.translucent = src.isTranslucent( );
		dest.translucentESet = src.isSetTranslucent( );

		return dest;
	}

} // DialSeriesImpl
