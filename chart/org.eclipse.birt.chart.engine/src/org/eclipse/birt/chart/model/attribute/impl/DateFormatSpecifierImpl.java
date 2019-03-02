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

import java.util.Locale;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

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
	protected boolean typeESet;

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
	protected boolean detailESet;

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
	@Override
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.DATE_FORMAT_SPECIFIER;
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				return getType( );
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				return getDetail( );
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				setType( (DateFormatType) newValue );
				return;
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				setDetail( (DateFormatDetail) newValue );
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				unsetType( );
				return;
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				unsetDetail( );
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
			case AttributePackage.DATE_FORMAT_SPECIFIER__TYPE :
				return isSetType( );
			case AttributePackage.DATE_FORMAT_SPECIFIER__DETAIL :
				return isSetDetail( );
		}
		return super.eIsSet( featureID );
	}

	public String format( Calendar c, ULocale lcl )
	{
		DateFormat df = null;
		if ( getDetail( ).getValue( ) == DateFormatDetail.DATE_TIME )
		{
			try
			{
				df = DateFormat.getDateTimeInstance( getJavaType( ),
						getJavaType( ),
						lcl );
				// Only Datetime supports TimeZone
				if ( c instanceof CDateTime
						&& ( (CDateTime) c ).isFullDateTime( ) )
				{
					df.setTimeZone( c.getTimeZone( ) );
				}
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
		if ( df == null )
		{
			return c.toString( );
		}

		return df.format( c.getTime( ) );
	}

	@SuppressWarnings("deprecation")
	public String format( Calendar c, Locale lcl )
	{
		return format( c, ULocale.forLocale( lcl ) );
	}

	/**
	 * @generated
	 */
	public DateFormatSpecifier copyInstance( )
	{
		DateFormatSpecifierImpl dest = new DateFormatSpecifierImpl( );
		dest.set( this );
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set( DateFormatSpecifier src )
	{

		super.set( src );

		// attributes

		type = src.getType( );

		typeESet = src.isSetType( );

		detail = src.getDetail( );

		detailESet = src.isSetDetail( );

	}

} // DateFormatSpecifierImpl
