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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Location</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.LocationImpl#getX <em>X</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.LocationImpl#getY <em>Y</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocationImpl extends EObjectImpl implements Location
{

	/**
	 * The default value of the '{@link #getX() <em>X</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected static final double X_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getX() <em>X</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected double x = X_EDEFAULT;

	/**
	 * This is true if the X attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean xESet = false;

	/**
	 * The default value of the '{@link #getY() <em>Y</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getY()
	 * @generated
	 * @ordered
	 */
	protected static final double Y_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getY() <em>Y</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getY()
	 * @generated
	 * @ordered
	 */
	protected double y = Y_EDEFAULT;

	/**
	 * This is true if the Y attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean yESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected LocationImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.LOCATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getX( )
	{
		return x;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setX( double newX )
	{
		double oldX = x;
		x = newX;
		boolean oldXESet = xESet;
		xESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.LOCATION__X,
					oldX,
					x,
					!oldXESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetX( )
	{
		double oldX = x;
		boolean oldXESet = xESet;
		x = X_EDEFAULT;
		xESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.LOCATION__X,
					oldX,
					X_EDEFAULT,
					oldXESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetX( )
	{
		return xESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getY( )
	{
		return y;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setY( double newY )
	{
		double oldY = y;
		y = newY;
		boolean oldYESet = yESet;
		yESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.LOCATION__Y,
					oldY,
					y,
					!oldYESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetY( )
	{
		double oldY = y;
		boolean oldYESet = yESet;
		y = Y_EDEFAULT;
		yESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.LOCATION__Y,
					oldY,
					Y_EDEFAULT,
					oldYESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetY( )
	{
		return yESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case AttributePackage.LOCATION__X :
				return new Double( getX( ) );
			case AttributePackage.LOCATION__Y :
				return new Double( getY( ) );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case AttributePackage.LOCATION__X :
				setX( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.LOCATION__Y :
				setY( ( (Double) newValue ).doubleValue( ) );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case AttributePackage.LOCATION__X :
				unsetX( );
				return;
			case AttributePackage.LOCATION__Y :
				unsetY( );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case AttributePackage.LOCATION__X :
				return isSetX( );
			case AttributePackage.LOCATION__Y :
				return isSetY( );
		}
		return super.eIsSet( featureID );
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
		result.append( " (x: " ); //$NON-NLS-1$
		if ( xESet )
			result.append( x );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", y: " ); //$NON-NLS-1$
		if ( yESet )
			result.append( y );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * 
	 * @param dX
	 * @param dY
	 * @return
	 */
	public static final Location create( double dX, double dY )
	{
		final Location lo = AttributeFactory.eINSTANCE.createLocation( );
		lo.setX( dX );
		lo.setY( dY );
		return lo;
	}

	/**
	 * @param xa
	 * @param ya
	 * @return
	 */
	public static final Location[] create( double[] xa, double[] ya )
	{
		Location[] loa = new Location[xa.length];
		for ( int i = 0; i < loa.length; i++ )
		{
			loa[i] = create( xa[i], ya[i] );
		}
		return loa;
	}

	/**
	 * @param loc
	 * @return
	 */
	public static double[] getXArray( Location[] loc )
	{
		double[] xa = new double[loc.length];
		for ( int i = 0; i < loc.length; i++ )
		{
			xa[i] = loc[i].getX( );
		}
		return xa;
	}

	/**
	 * @param loc
	 * @return
	 */
	public static double[] getYArray( Location[] loc )
	{
		double[] ya = new double[loc.length];
		for ( int i = 0; i < loc.length; i++ )
		{
			ya[i] = loc[i].getY( );
		}
		return ya;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Location#set(double, double)
	 */
	public void set( double dX, double dY )
	{
		setX( dX );
		setY( dY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Location#translate(double,
	 *      double)
	 */
	public void translate( double dTranslateX, double dTranslateY )
	{
		setX( getX( ) + dTranslateX );
		setY( getY( ) + dTranslateY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Location#scale(double)
	 */
	public void scale( double dScale )
	{
		setX( getX( ) * dScale );
		setY( getY( ) * dScale );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( obj instanceof Location )
		{
			Location lo = (Location) obj;
			return getX( ) == lo.getX( ) && getY( ) == lo.getY( );
		}
		return super.equals( obj );
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static Location copyInstance( Location src )
	{
		if ( src == null )
		{
			return null;
		}
		LocationImpl lo = new LocationImpl( );
		lo.x = src.getX( );
		lo.y = src.getY( );
		lo.xESet = src.isSetX( );
		lo.yESet = src.isSetY( );
		return lo;
	}

} // LocationImpl
