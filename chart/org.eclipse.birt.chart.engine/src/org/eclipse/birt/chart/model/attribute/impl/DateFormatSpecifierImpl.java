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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Date Format Specifier</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl#getDetail <em>Detail</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DateFormatSpecifierImpl extends FormatSpecifierImpl implements
		DateFormatSpecifier
{

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final DateFormatType TYPE_EDEFAULT = DateFormatType.LONG_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected DateFormatType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet = false;

	/**
	 * The default value of the '{@link #getDetail() <em>Detail</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDetail()
	 * @generated
	 * @ordered
	 */
	protected static final DateFormatDetail DETAIL_EDEFAULT = DateFormatDetail.DATE_LITERAL;

	/**
	 * The cached value of the '{@link #getDetail() <em>Detail</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDetail()
	 * @generated
	 * @ordered
	 */
	protected DateFormatDetail detail = DETAIL_EDEFAULT;

	/**
	 * This is true if the Detail attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean detailESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DateFormatSpecifierImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getDateFormatSpecifier( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				return getType( );
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				return getDetail( );
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				setType( (DateFormatType) newValue );
				return;
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				setDetail( (DateFormatDetail) newValue );
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				unsetType( );
				return;
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				unsetDetail( );
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				return isSetType( );
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				return isSetDetail( );
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
		result.append( ", detail: " ); //$NON-NLS-1$
		if ( detailESet )
			result.append( detail );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateFormatType getType( )
	{
		return type;
	}

	/**
	 * 
	 * @return
	 * @throws UndefinedValueException
	 */
	private final int getJavaType( ) throws ChartException
	{
		if ( !isSetType( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.UNDEFINED_VALUE,
					Messages.getString( "error.type.not.set", Locale.getDefault( ) ) ); //$NON-NLS-1$
		}
		switch ( getType( ).getValue( ) )
		{
			case DateFormatType.SHORT :
				return DateFormat.SHORT;
			case DateFormatType.MEDIUM :
				return DateFormat.MEDIUM;
			case DateFormatType.LONG :
				return DateFormat.LONG;
			case DateFormatType.FULL :
				return DateFormat.FULL;
		}
		return 0;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setType( DateFormatType newType )
	{
		DateFormatType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATE_FORMAT_SPECIFIER__TYPE,
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
		DateFormatType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.DATE_FORMAT_SPECIFIER__TYPE,
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
	public DateFormatDetail getDetail( )
	{
		return detail;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDetail( DateFormatDetail newDetail )
	{
		DateFormatDetail oldDetail = detail;
		detail = newDetail == null ? DETAIL_EDEFAULT : newDetail;
		boolean oldDetailESet = detailESet;
		detailESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL,
					oldDetail,
					detail,
					!oldDetailESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDetail( )
	{
		DateFormatDetail oldDetail = detail;
		boolean oldDetailESet = detailESet;
		detail = DETAIL_EDEFAULT;
		detailESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL,
					oldDetail,
					DETAIL_EDEFAULT,
					oldDetailESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDetail( )
	{
		return detailESet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#format(java.util.Calendar)
	 */
	public String format( Calendar c, Locale lcl )
	{
		DateFormat df = null;
		if ( getDetail( ).getValue( ) == DateFormatDetail.DATE_TIME )
		{
			try
			{
				df = DateFormat.getDateTimeInstance( getJavaType( ),
						getJavaType( ),
						lcl );
			}
			catch ( ChartException uex )
			{
				return c.toString( );
			}
		}
		else if ( getDetail( ).getValue( ) == DateFormatDetail.DATE )
		{
			try
			{
				df = DateFormat.getDateInstance( getJavaType( ), lcl );
			}
			catch ( ChartException uex )
			{
				return c.toString( );
			}
		}
		return df.format( c.getTime( ) );
	}

} // DateFormatSpecifierImpl
