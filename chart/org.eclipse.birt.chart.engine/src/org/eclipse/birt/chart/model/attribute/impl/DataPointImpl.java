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

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Data Point</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl#getComponents <em>Components</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl#getSuffix <em>Suffix</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl#getSeparator <em>Separator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataPointImpl extends EObjectImpl implements DataPoint
{

	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList components = null;

	/**
	 * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected String prefix = PREFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getSuffix() <em>Suffix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSuffix()
	 * @generated
	 * @ordered
	 */
	protected static final String SUFFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSuffix() <em>Suffix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSuffix()
	 * @generated
	 * @ordered
	 */
	protected String suffix = SUFFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected static final String SEPARATOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected String separator = SEPARATOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DataPointImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getDataPoint( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getComponents( )
	{
		if ( components == null )
		{
			components = new EObjectContainmentEList( DataPointComponent.class,
					this,
					AttributePackage.DATA_POINT__COMPONENTS );
		}
		return components;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrefix( )
	{
		return prefix;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrefix( String newPrefix )
	{
		String oldPrefix = prefix;
		prefix = newPrefix;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATA_POINT__PREFIX,
					oldPrefix,
					prefix ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getSuffix( )
	{
		return suffix;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSuffix( String newSuffix )
	{
		String oldSuffix = suffix;
		suffix = newSuffix;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATA_POINT__SUFFIX,
					oldSuffix,
					suffix ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getSeparator( )
	{
		return separator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSeparator( String newSeparator )
	{
		String oldSeparator = separator;
		separator = newSeparator;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATA_POINT__SEPARATOR,
					oldSeparator,
					separator ) );
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
				case AttributePackage.DATA_POINT__COMPONENTS :
					return ( (InternalEList) getComponents( ) ).basicRemove( otherEnd,
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
			case AttributePackage.DATA_POINT__COMPONENTS :
				return getComponents( );
			case AttributePackage.DATA_POINT__PREFIX :
				return getPrefix( );
			case AttributePackage.DATA_POINT__SUFFIX :
				return getSuffix( );
			case AttributePackage.DATA_POINT__SEPARATOR :
				return getSeparator( );
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
			case AttributePackage.DATA_POINT__COMPONENTS :
				getComponents( ).clear( );
				getComponents( ).addAll( (Collection) newValue );
				return;
			case AttributePackage.DATA_POINT__PREFIX :
				setPrefix( (String) newValue );
				return;
			case AttributePackage.DATA_POINT__SUFFIX :
				setSuffix( (String) newValue );
				return;
			case AttributePackage.DATA_POINT__SEPARATOR :
				setSeparator( (String) newValue );
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
			case AttributePackage.DATA_POINT__COMPONENTS :
				getComponents( ).clear( );
				return;
			case AttributePackage.DATA_POINT__PREFIX :
				setPrefix( PREFIX_EDEFAULT );
				return;
			case AttributePackage.DATA_POINT__SUFFIX :
				setSuffix( SUFFIX_EDEFAULT );
				return;
			case AttributePackage.DATA_POINT__SEPARATOR :
				setSeparator( SEPARATOR_EDEFAULT );
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
			case AttributePackage.DATA_POINT__COMPONENTS :
				return components != null && !components.isEmpty( );
			case AttributePackage.DATA_POINT__PREFIX :
				return PREFIX_EDEFAULT == null ? prefix != null
						: !PREFIX_EDEFAULT.equals( prefix );
			case AttributePackage.DATA_POINT__SUFFIX :
				return SUFFIX_EDEFAULT == null ? suffix != null
						: !SUFFIX_EDEFAULT.equals( suffix );
			case AttributePackage.DATA_POINT__SEPARATOR :
				return SEPARATOR_EDEFAULT == null ? separator != null
						: !SEPARATOR_EDEFAULT.equals( separator );
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
		result.append( " (prefix: " ); //$NON-NLS-1$
		result.append( prefix );
		result.append( ", suffix: " ); //$NON-NLS-1$
		result.append( suffix );
		result.append( ", separator: " ); //$NON-NLS-1$
		result.append( separator );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience methods provided to create an initialized DataPoint
	 * instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @param sPrefix
	 * @param sSuffix
	 * @param sSeparator
	 * @return
	 */
	public static final DataPoint create( String sPrefix, String sSuffix,
			String sSeparator )
	{
		final DataPoint dp = AttributeFactory.eINSTANCE.createDataPoint( );
		dp.setPrefix( sPrefix );
		dp.setSuffix( sSuffix );
		dp.setSeparator( sSeparator );
		dp.getComponents( )
				.add( DataPointComponentImpl.create( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
						null ) );
		return dp;
	}
} // DataPointImpl
