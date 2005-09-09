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

import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Marker</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getSize <em>Size</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#isVisible <em>Visible</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarkerImpl extends EObjectImpl implements Marker
{

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final MarkerType TYPE_EDEFAULT = MarkerType.CROSSHAIR_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected MarkerType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet = false;

	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final int SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected int size = SIZE_EDEFAULT;

	/**
	 * This is true if the Size attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean sizeESet = false;

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * This is true if the Visible attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean visibleESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected MarkerImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getMarker( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MarkerType getType( )
	{
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setType( MarkerType newType )
	{
		MarkerType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.MARKER__TYPE,
					oldType,
					type,
					!oldTypeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetType( )
	{
		MarkerType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.MARKER__TYPE,
					oldType,
					TYPE_EDEFAULT,
					oldTypeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetType( )
	{
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getSize( )
	{
		return size;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSize( int newSize )
	{
		int oldSize = size;
		size = newSize;
		boolean oldSizeESet = sizeESet;
		sizeESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.MARKER__SIZE,
					oldSize,
					size,
					!oldSizeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSize( )
	{
		int oldSize = size;
		boolean oldSizeESet = sizeESet;
		size = SIZE_EDEFAULT;
		sizeESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.MARKER__SIZE,
					oldSize,
					SIZE_EDEFAULT,
					oldSizeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSize( )
	{
		return sizeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVisible( )
	{
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setVisible( boolean newVisible )
	{
		boolean oldVisible = visible;
		visible = newVisible;
		boolean oldVisibleESet = visibleESet;
		visibleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.MARKER__VISIBLE,
					oldVisible,
					visible,
					!oldVisibleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVisible( )
	{
		boolean oldVisible = visible;
		boolean oldVisibleESet = visibleESet;
		visible = VISIBLE_EDEFAULT;
		visibleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.MARKER__VISIBLE,
					oldVisible,
					VISIBLE_EDEFAULT,
					oldVisibleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVisible( )
	{
		return visibleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.MARKER__TYPE :
				return getType( );
			case AttributePackage.MARKER__SIZE :
				return new Integer( getSize( ) );
			case AttributePackage.MARKER__VISIBLE :
				return isVisible( ) ? Boolean.TRUE : Boolean.FALSE;
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
			case AttributePackage.MARKER__TYPE :
				setType( (MarkerType) newValue );
				return;
			case AttributePackage.MARKER__SIZE :
				setSize( ( (Integer) newValue ).intValue( ) );
				return;
			case AttributePackage.MARKER__VISIBLE :
				setVisible( ( (Boolean) newValue ).booleanValue( ) );
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
			case AttributePackage.MARKER__TYPE :
				unsetType( );
				return;
			case AttributePackage.MARKER__SIZE :
				unsetSize( );
				return;
			case AttributePackage.MARKER__VISIBLE :
				unsetVisible( );
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
			case AttributePackage.MARKER__TYPE :
				return isSetType( );
			case AttributePackage.MARKER__SIZE :
				return isSetSize( );
			case AttributePackage.MARKER__VISIBLE :
				return isSetVisible( );
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
		result.append( " (type: " ); //$NON-NLS-1$
		if ( typeESet )
			result.append( type );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", size: " ); //$NON-NLS-1$
		if ( sizeESet )
			result.append( size );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", visible: " ); //$NON-NLS-1$
		if ( visibleESet )
			result.append( visible );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

} // MarkerImpl
