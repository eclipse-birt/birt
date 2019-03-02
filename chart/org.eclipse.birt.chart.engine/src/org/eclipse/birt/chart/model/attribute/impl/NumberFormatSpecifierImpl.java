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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.util.NumberUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Number Format Specifier</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl#getSuffix <em>Suffix</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl#getFractionDigits <em>Fraction Digits</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NumberFormatSpecifierImpl extends FormatSpecifierImpl implements
		NumberFormatSpecifier
{

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
	 * The default value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double MULTIPLIER_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double multiplier = MULTIPLIER_EDEFAULT;

	/**
	 * This is true if the Multiplier attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean multiplierESet;

	/**
	 * The default value of the '
	 * {@link #getFractionDigits() <em>Fraction Digits</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFractionDigits()
	 * @generated
	 * @ordered
	 */
	protected static final int FRACTION_DIGITS_EDEFAULT = 0;

	/**
	 * The cached value of the '
	 * {@link #getFractionDigits() <em>Fraction Digits</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFractionDigits()
	 * @generated
	 * @ordered
	 */
	protected int fractionDigits = FRACTION_DIGITS_EDEFAULT;

	/**
	 * This is true if the Fraction Digits attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean fractionDigitsESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected NumberFormatSpecifierImpl( )
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
		return AttributePackage.Literals.NUMBER_FORMAT_SPECIFIER;
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
					AttributePackage.NUMBER_FORMAT_SPECIFIER__PREFIX,
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
					AttributePackage.NUMBER_FORMAT_SPECIFIER__SUFFIX,
					oldSuffix,
					suffix ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getMultiplier( )
	{
		return multiplier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplier( double newMultiplier )
	{
		double oldMultiplier = multiplier;
		multiplier = newMultiplier;
		boolean oldMultiplierESet = multiplierESet;
		multiplierESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.NUMBER_FORMAT_SPECIFIER__MULTIPLIER,
					oldMultiplier,
					multiplier,
					!oldMultiplierESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMultiplier( )
	{
		double oldMultiplier = multiplier;
		boolean oldMultiplierESet = multiplierESet;
		multiplier = MULTIPLIER_EDEFAULT;
		multiplierESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.NUMBER_FORMAT_SPECIFIER__MULTIPLIER,
					oldMultiplier,
					MULTIPLIER_EDEFAULT,
					oldMultiplierESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMultiplier( )
	{
		return multiplierESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getFractionDigits( )
	{
		return fractionDigits;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setFractionDigits( int newFractionDigits )
	{
		int oldFractionDigits = fractionDigits;
		fractionDigits = newFractionDigits;
		boolean oldFractionDigitsESet = fractionDigitsESet;
		fractionDigitsESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS,
					oldFractionDigits,
					fractionDigits,
					!oldFractionDigitsESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFractionDigits( )
	{
		int oldFractionDigits = fractionDigits;
		boolean oldFractionDigitsESet = fractionDigitsESet;
		fractionDigits = FRACTION_DIGITS_EDEFAULT;
		fractionDigitsESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS,
					oldFractionDigits,
					FRACTION_DIGITS_EDEFAULT,
					oldFractionDigitsESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFractionDigits( )
	{
		return fractionDigitsESet;
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
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__PREFIX :
				return getPrefix( );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__SUFFIX :
				return getSuffix( );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__MULTIPLIER :
				return getMultiplier( );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS :
				return getFractionDigits( );
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
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__PREFIX :
				setPrefix( (String) newValue );
				return;
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__SUFFIX :
				setSuffix( (String) newValue );
				return;
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__MULTIPLIER :
				setMultiplier( (Double) newValue );
				return;
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS :
				setFractionDigits( (Integer) newValue );
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
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__PREFIX :
				setPrefix( PREFIX_EDEFAULT );
				return;
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__SUFFIX :
				setSuffix( SUFFIX_EDEFAULT );
				return;
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__MULTIPLIER :
				unsetMultiplier( );
				return;
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS :
				unsetFractionDigits( );
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
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__PREFIX :
				return PREFIX_EDEFAULT == null ? prefix != null
						: !PREFIX_EDEFAULT.equals( prefix );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__SUFFIX :
				return SUFFIX_EDEFAULT == null ? suffix != null
						: !SUFFIX_EDEFAULT.equals( suffix );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__MULTIPLIER :
				return isSetMultiplier( );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS :
				return isSetFractionDigits( );
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
		result.append( " (prefix: " ); //$NON-NLS-1$
		result.append( prefix );
		result.append( ", suffix: " ); //$NON-NLS-1$
		result.append( suffix );
		result.append( ", multiplier: " ); //$NON-NLS-1$
		if ( multiplierESet )
			result.append( multiplier );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", fractionDigits: " ); //$NON-NLS-1$
		if ( fractionDigitsESet )
			result.append( fractionDigits );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience methods provided to create an initialized
	 * NumberFormatSpecifier instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @return this instance
	 */
	public static NumberFormatSpecifier create( )
	{
		final NumberFormatSpecifier nfs = AttributeFactory.eINSTANCE.createNumberFormatSpecifier( );
		nfs.setFractionDigits( 2 );
		// jnfs.setMultiplier(1); // UNDEFINED SUGGESTS A DEFAULT OF '1'
		return nfs;
	}

	public String format( double dValue, Locale lo )
	{
		return format( dValue, ULocale.forLocale( lo ) );
	}

	public String format( double dValue, ULocale lo )
	{
		final DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance( lo );
		if ( isSetFractionDigits( ) )
		{
			df.setMinimumFractionDigits( getFractionDigits( ) );
			df.setMaximumFractionDigits( getFractionDigits( ) );
		}

		df.applyLocalizedPattern( df.toLocalizedPattern( ) );

		final StringBuffer sb = new StringBuffer( );
		if ( getPrefix( ) != null )
		{
			sb.append( getPrefix( ) );
		}
		sb.append( isSetMultiplier( ) ? df.format( dValue * getMultiplier( ) )
				: df.format( dValue ) );
		if ( getSuffix( ) != null )
		{
			sb.append( getSuffix( ) );
		}

		return sb.toString( );
	}

	public String format( Number number, ULocale lo )
	{
		Number n = NumberUtil.transformNumber( number );
		if ( n instanceof Double )
		{
			return format( ( (Double) number ).doubleValue( ), lo );
		}

		// Format big decimal.
		BigDecimal bdNum = (BigDecimal) n;
		final DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance( lo );
		if ( isSetFractionDigits( ) )
		{
			df.setMinimumFractionDigits( getFractionDigits( ) );
			df.setMaximumFractionDigits( getFractionDigits( ) );
		}
		String pattern = NumberUtil.adjustBigNumberFormatPattern( df.toLocalizedPattern( ) );
		if ( pattern.indexOf( 'E' ) < 0 )
		{
			pattern = pattern + NumberUtil.BIG_DECIMAL_FORMAT_SUFFIX;
		}

		df.applyLocalizedPattern( pattern );

		final StringBuffer sb = new StringBuffer( );
		if ( getPrefix( ) != null )
		{
			sb.append( getPrefix( ) );
		}
		sb.append( isSetMultiplier( ) ? df.format( bdNum.multiply( BigDecimal.valueOf( getMultiplier( ) ),
				NumberUtil.DEFAULT_MATHCONTEXT ) )
				: df.format( bdNum ) );
		if ( getSuffix( ) != null )
		{
			sb.append( getSuffix( ) );
		}

		return sb.toString( );
	}

	/**
	 * @generated
	 */
	public NumberFormatSpecifier copyInstance( )
	{
		NumberFormatSpecifierImpl dest = new NumberFormatSpecifierImpl( );
		dest.set( this );
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set( NumberFormatSpecifier src )
	{

		super.set( src );

		// attributes

		prefix = src.getPrefix( );

		suffix = src.getSuffix( );

		multiplier = src.getMultiplier( );

		multiplierESet = src.isSetMultiplier( );

		fractionDigits = src.getFractionDigits( );

		fractionDigitsESet = src.isSetFractionDigits( );

	}

} // NumberFormatSpecifierImpl
