/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Angle3 D</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getXAngle <em>XAngle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getYAngle <em>YAngle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getZAngle <em>ZAngle</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class Angle3DImpl extends EObjectImpl implements Angle3D
{

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Angle3D create( double x, double y, double z )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setXAngle( x );
		angle.setYAngle( y );
		angle.setZAngle( z );
		( (Angle3DImpl) angle ).axis = AXIS_NONE;
		return angle;
	}

	/**
	 * @param val
	 * @return
	 */
	public static Angle3D createX( double val )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setXAngle( val );
		( (Angle3DImpl) angle ).axis = AXIS_X;
		return angle;
	}

	/**
	 * @param val
	 * @return
	 */
	public static Angle3D createY( double val )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setYAngle( val );
		( (Angle3DImpl) angle ).axis = AXIS_Y;
		return angle;
	}

	/**
	 * @param val
	 * @return
	 */
	public static Angle3D createZ( double val )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setZAngle( val );
		( (Angle3DImpl) angle ).axis = AXIS_Z;
		return angle;
	}

	protected int axis = AXIS_NONE;

	/**
	 * The default value of the '{@link #getXAngle() <em>XAngle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getXAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double XANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getXAngle() <em>XAngle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getXAngle()
	 * @generated
	 * @ordered
	 */
	protected double xAngle = XANGLE_EDEFAULT;

	/**
	 * This is true if the XAngle attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean xAngleESet = false;

	/**
	 * The default value of the '{@link #getYAngle() <em>YAngle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getYAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double YANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getYAngle() <em>YAngle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getYAngle()
	 * @generated
	 * @ordered
	 */
	protected double yAngle = YANGLE_EDEFAULT;

	/**
	 * This is true if the YAngle attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean yAngleESet = false;

	/**
	 * The default value of the '{@link #getZAngle() <em>ZAngle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double ZANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getZAngle() <em>ZAngle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZAngle()
	 * @generated
	 * @ordered
	 */
	protected double zAngle = ZANGLE_EDEFAULT;

	/**
	 * This is true if the ZAngle attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean zAngleESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Angle3DImpl( )
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
		return AttributePackage.eINSTANCE.getAngle3D( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getXAngle( )
	{
		return xAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setXAngle( double newXAngle )
	{
		double oldXAngle = xAngle;
		xAngle = newXAngle;
		boolean oldXAngleESet = xAngleESet;
		xAngleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__XANGLE,
					oldXAngle,
					xAngle,
					!oldXAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetXAngle( )
	{
		double oldXAngle = xAngle;
		boolean oldXAngleESet = xAngleESet;
		xAngle = XANGLE_EDEFAULT;
		xAngleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__XANGLE,
					oldXAngle,
					XANGLE_EDEFAULT,
					oldXAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetXAngle( )
	{
		return xAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getYAngle( )
	{
		return yAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setYAngle( double newYAngle )
	{
		double oldYAngle = yAngle;
		yAngle = newYAngle;
		boolean oldYAngleESet = yAngleESet;
		yAngleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__YANGLE,
					oldYAngle,
					yAngle,
					!oldYAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetYAngle( )
	{
		double oldYAngle = yAngle;
		boolean oldYAngleESet = yAngleESet;
		yAngle = YANGLE_EDEFAULT;
		yAngleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__YANGLE,
					oldYAngle,
					YANGLE_EDEFAULT,
					oldYAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetYAngle( )
	{
		return yAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getZAngle( )
	{
		return zAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZAngle( double newZAngle )
	{
		double oldZAngle = zAngle;
		zAngle = newZAngle;
		boolean oldZAngleESet = zAngleESet;
		zAngleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__ZANGLE,
					oldZAngle,
					zAngle,
					!oldZAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetZAngle( )
	{
		double oldZAngle = zAngle;
		boolean oldZAngleESet = zAngleESet;
		zAngle = ZANGLE_EDEFAULT;
		zAngleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__ZANGLE,
					oldZAngle,
					ZANGLE_EDEFAULT,
					oldZAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetZAngle( )
	{
		return zAngleESet;
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
			case AttributePackage.ANGLE3_D__XANGLE :
				return new Double( getXAngle( ) );
			case AttributePackage.ANGLE3_D__YANGLE :
				return new Double( getYAngle( ) );
			case AttributePackage.ANGLE3_D__ZANGLE :
				return new Double( getZAngle( ) );
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
			case AttributePackage.ANGLE3_D__XANGLE :
				setXAngle( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.ANGLE3_D__YANGLE :
				setYAngle( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.ANGLE3_D__ZANGLE :
				setZAngle( ( (Double) newValue ).doubleValue( ) );
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
			case AttributePackage.ANGLE3_D__XANGLE :
				unsetXAngle( );
				return;
			case AttributePackage.ANGLE3_D__YANGLE :
				unsetYAngle( );
				return;
			case AttributePackage.ANGLE3_D__ZANGLE :
				unsetZAngle( );
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
			case AttributePackage.ANGLE3_D__XANGLE :
				return isSetXAngle( );
			case AttributePackage.ANGLE3_D__YANGLE :
				return isSetYAngle( );
			case AttributePackage.ANGLE3_D__ZANGLE :
				return isSetZAngle( );
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
		result.append( " (xAngle: " ); //$NON-NLS-1$
		if ( xAngleESet )
			result.append( xAngle );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", yAngle: " ); //$NON-NLS-1$
		if ( yAngleESet )
			result.append( yAngle );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", zAngle: " ); //$NON-NLS-1$
		if ( zAngleESet )
			result.append( zAngle );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * Returns the angle axis if set.
	 * 
	 * @return
	 */
	public int getAxisType( )
	{
		return axis;
	}

	/**
	 * Returns the specific axis angle value if axis specified, or just returns
	 * Zero.
	 * 
	 * @return
	 */
	public double getAxisAngle( )
	{
		if ( axis == AXIS_X )
		{
			return getXAngle( );
		}
		else if ( axis == AXIS_Y )
		{
			return getYAngle( );
		}
		else if ( axis == AXIS_Z )
		{
			return getZAngle( );
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#set(double, double,
	 *      double)
	 */
	public void set( double x, double y, double z )
	{
		setXAngle( x );
		setYAngle( y );
		setZAngle( z );
		axis = AXIS_NONE;
	}

} // Angle3DImpl
