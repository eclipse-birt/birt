/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Location3 D</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.Location3DImpl#getZ <em>Z</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class Location3DImpl extends LocationImpl implements Location3D
{

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Location3D create( double x, double y, double z )
	{
		final Location3D lo = AttributeFactory.eINSTANCE.createLocation3D( );
		lo.setX( x );
		lo.setY( y );
		lo.setZ( z );
		return lo;
	}

	/**
	 * @param xa
	 * @param ya
	 * @param za
	 * @return
	 */
	public static final Location3D[] create( double[] xa, double[] ya,
			double[] za )
	{
		Location3D[] loa = new Location3D[xa.length];
		for ( int i = 0; i < loa.length; i++ )
		{
			loa[i] = create( xa[i], ya[i], za[i] );
		}
		return loa;
	}

	/**
	 * @param loc
	 * @return
	 */
	public static double[] getXArray( Location3D[] loc )
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
	public static double[] getYArray( Location3D[] loc )
	{
		double[] ya = new double[loc.length];
		for ( int i = 0; i < loc.length; i++ )
		{
			ya[i] = loc[i].getY( );
		}
		return ya;
	}

	/**
	 * @param loc
	 * @return
	 */
	public static double[] getZArray( Location3D[] loc )
	{
		double[] za = new double[loc.length];
		for ( int i = 0; i < loc.length; i++ )
		{
			za[i] = loc[i].getZ( );
		}
		return za;
	}

	/**
	 * The default value of the '{@link #getZ() <em>Z</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZ()
	 * @generated
	 * @ordered
	 */
	protected static final double Z_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getZ() <em>Z</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZ()
	 * @generated
	 * @ordered
	 */
	protected double z = Z_EDEFAULT;

	/**
	 * This is true if the Z attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean zESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Location3DImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getLocation3D( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getZ( )
	{
		return z;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZ( double newZ )
	{
		double oldZ = z;
		z = newZ;
		boolean oldZESet = zESet;
		zESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.LOCATION3_D__Z,
					oldZ,
					z,
					!oldZESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetZ( )
	{
		double oldZ = z;
		boolean oldZESet = zESet;
		z = Z_EDEFAULT;
		zESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.LOCATION3_D__Z,
					oldZ,
					Z_EDEFAULT,
					oldZESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetZ( )
	{
		return zESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.LOCATION3_D__X :
				return new Double( getX( ) );
			case AttributePackage.LOCATION3_D__Y :
				return new Double( getY( ) );
			case AttributePackage.LOCATION3_D__Z :
				return new Double( getZ( ) );
		}
		return eDynamicGet( eFeature, resolve );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet( EStructuralFeature eFeature, Object newValue )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.LOCATION3_D__X :
				setX( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.LOCATION3_D__Y :
				setY( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.LOCATION3_D__Z :
				setZ( ( (Double) newValue ).doubleValue( ) );
				return;
		}
		eDynamicSet( eFeature, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.LOCATION3_D__X :
				unsetX( );
				return;
			case AttributePackage.LOCATION3_D__Y :
				unsetY( );
				return;
			case AttributePackage.LOCATION3_D__Z :
				unsetZ( );
				return;
		}
		eDynamicUnset( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.LOCATION3_D__X :
				return isSetX( );
			case AttributePackage.LOCATION3_D__Y :
				return isSetY( );
			case AttributePackage.LOCATION3_D__Z :
				return isSetZ( );
		}
		return eDynamicIsSet( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (z: " ); //$NON-NLS-1$
		if ( zESet )
			result.append( z );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Location3D#translate(double,
	 *      double, double)
	 */
	public void translate( double dTranslateX, double dTranslateY,
			double dTranslateZ )
	{
		setX( getX( ) + dTranslateX );
		setY( getY( ) + dTranslateY );
		setZ( getZ( ) + dTranslateZ );
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
		setZ( getZ( ) * dScale );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Location3D#set(double,
	 *      double, double)
	 */
	public void set( double dX, double dY, double dZ )
	{
		setX( dX );
		setY( dY );
		setZ( dZ );
	}

} // Location3DImpl
