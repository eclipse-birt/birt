/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Rotation3D;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Rotation3 D</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl#getAngles <em>Angles</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class Rotation3DImpl extends EObjectImpl implements Rotation3D
{

	/**
	 * The cached value of the '{@link #getAngles() <em>Angles</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAngles()
	 * @generated
	 * @ordered
	 */
	protected EList angles = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Rotation3DImpl( )
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
		return AttributePackage.eINSTANCE.getRotation3D( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getAngles( )
	{
		if ( angles == null )
		{
			angles = new EObjectContainmentEList( Angle3D.class,
					this,
					AttributePackage.ROTATION3_D__ANGLES );
		}
		return angles;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, Class baseClass, NotificationChain msgs )
	{
		if ( featureID >= 0 )
		{
			switch ( eDerivedStructuralFeatureID( featureID, baseClass ) )
			{
				case AttributePackage.ROTATION3_D__ANGLES :
					return ( (InternalEList) getAngles( ) ).basicRemove( otherEnd,
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
	 * 
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.ROTATION3_D__ANGLES :
				return getAngles( );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				getAngles( ).clear( );
				getAngles( ).addAll( (Collection) newValue );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				getAngles( ).clear( );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				return angles != null && !angles.isEmpty( );
		}
		return eDynamicIsSet( eFeature );
	}

	/**
	 * Creates an empty Roatation3D object.
	 * 
	 * @param ala
	 * @return
	 */
	public static Rotation3D create( )
	{
		return create( null );
	}

	/**
	 * Creates a Rotation3D object using given Angle3D array.
	 * 
	 * @param ala
	 * @return
	 */
	public static Rotation3D create( Angle3D[] ala )
	{
		Rotation3D rt = AttributeFactory.eINSTANCE.createRotation3D( );
		if ( ala != null )
		{
			for ( int i = 0; i < ala.length; i++ )
			{
				rt.getAngles( ).add( ala[i] );
			}
		}
		return rt;
	}

} // Rotation3DImpl
