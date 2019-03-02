/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.StringFormatSpecifier;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>String Format Specifier</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl#getPattern <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StringFormatSpecifierImpl extends FormatSpecifierImpl implements
		StringFormatSpecifier
{

	/**
	 * The default value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected static final String PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected String pattern = PATTERN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StringFormatSpecifierImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.STRING_FORMAT_SPECIFIER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPattern( )
	{
		return pattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPattern( String newPattern )
	{
		String oldPattern = pattern;
		pattern = newPattern;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.STRING_FORMAT_SPECIFIER__PATTERN,
					oldPattern,
					pattern ) );
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
			case AttributePackage.STRING_FORMAT_SPECIFIER__PATTERN :
				return getPattern( );
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
			case AttributePackage.STRING_FORMAT_SPECIFIER__PATTERN :
				setPattern( (String) newValue );
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
			case AttributePackage.STRING_FORMAT_SPECIFIER__PATTERN :
				setPattern( PATTERN_EDEFAULT );
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
			case AttributePackage.STRING_FORMAT_SPECIFIER__PATTERN :
				return PATTERN_EDEFAULT == null ? pattern != null
						: !PATTERN_EDEFAULT.equals( pattern );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (pattern: " ); //$NON-NLS-1$
		result.append( pattern );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * @generated
	 */
	protected void set( StringFormatSpecifier src )
	{

		super.set( src );

		// attributes

		pattern = src.getPattern( );

	}

	/**
	 * @generated
	 */
	public StringFormatSpecifier copyInstance( )
	{
		StringFormatSpecifierImpl dest = new StringFormatSpecifierImpl( );
		dest.set( this );
		return dest;
	}

	/**
	 * A convenience methods provided to create an initialized
	 * NumberFormatSpecifier instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @return this instance
	 */
	public static StringFormatSpecifier create( )
	{
		final StringFormatSpecifier sfs = AttributeFactory.eINSTANCE.createStringFormatSpecifier( );
		return sfs;
	}

	public static StringFormatSpecifier create( String pattern )
	{
		final StringFormatSpecifier sfs = create( );
		sfs.setPattern( pattern );
		return sfs;
	}

	public String format( String dValue, ULocale lo )
	{
		applyPattern( pattern );
		if ( trim && dValue != null )
		{
			dValue = dValue.trim( );
		}

		if ( pattern.equals( "Unformatted" ) ) //$NON-NLS-1$
			return dValue;

		int len = dValue.length( );
		int col = natt + nand;
		int ext = 0;
		StringBuffer orig = new StringBuffer( dValue );
		StringBuffer fstr = new StringBuffer( this.pattern );
		StringBuffer ret = new StringBuffer( "" ); //$NON-NLS-1$
		int i = 0;
		// offset of the process position.
		int pos = 0;

		// length of the format string;
		int len2 = 0;

		char fc = ' ';

		String sc = null;

		if ( !dir )
		{
			if ( len > col )
			{
				ret.append( handleCase( orig.substring( 0, len - col ),
						chcase,
						lo ) );
				pos = len - col;
				len = col;

			}
			ext = col - len;
		}
		len2 = this.pattern.length( );
		for ( i = 0; i < len2; i++ )
		{

			fc = fstr.charAt( i );
			switch ( fc )
			{
				case ( '@' ) :
				case ( '&' ) :
					// character or space
					if ( ext > 0 || len == 0 )
					{
						if ( fc == '@' )
							ret.append( ' ' );
						ext--;
					}
					else
					{
						sc = orig.substring( pos, pos + 1 );
						ret.append( handleCase( sc, chcase, lo ) );
						pos++;
						len--;
					}
					break;

				case ( '<' ) :
				case ( '>' ) :
				case ( '!' ) :
				case ( '^' ) :
					// ignore
					break;

				default :
					ret.append( fc );
					break;
			}
		}

		while ( --len >= 0 )
		{
			sc = orig.substring( pos, pos + 1 );
			ret.append( handleCase( sc, chcase, lo ) );
			pos++;
		}

		return ret.toString( );
	}

	// uppercase or lowercase;
	private char chcase;

	// number of & in format string;
	private int nand;

	// number of @ in format string;
	private int natt;

	// from left to right.
	private boolean dir;

	// should we trim the space.
	private boolean trim;

	/**
	 * resets all the member variable to initial value;
	 */
	private void init( )
	{
		chcase = ' ';
		nand = 0;
		natt = 0;
		dir = false;
		trim = true;
	}

	/**
	 * @param format
	 *            the format pattern
	 */
	public void applyPattern( String format )
	{
		init( );
		if ( format == null )
		{
			return;
		}
		char c = ' ';
		StringBuffer scan = new StringBuffer( format );
		int len = scan.length( );

		for ( int i = 0; i < len; i++ )
		{
			c = scan.charAt( i );
			switch ( c )
			{
				case ( '@' ) :
					natt++;
					break;

				case ( '&' ) :
					nand++;
					break;

				case ( '<' ) :
				case ( '>' ) :
					chcase = c;
					break;

				case ( '!' ) :
					dir = true;
					break;

				case ( '^' ) :
					trim = false;
					break;
			}
		}
		if ( "Zip Code + 4".equalsIgnoreCase( format ) )//$NON-NLS-1$
		{
			applyPattern( "@@@@@-@@@@" );//$NON-NLS-1$
			return;
		}
		if ( "Phone Number".equalsIgnoreCase( format ) )//$NON-NLS-1$
		{
			applyPattern( "(@@@)@@@-@@@@" );//$NON-NLS-1$
			return;
		}
		if ( "Social Security Number".equalsIgnoreCase( format ) )//$NON-NLS-1$
		{
			applyPattern( "@@@-@@-@@@@" ); //$NON-NLS-1$
			return;
		}
		pattern = format;
	}

	/**
	 * @param val
	 *            string to be handled
	 * @param option
	 *            to upper case or to lower case
	 * @return
	 */
	private String handleCase( String val, char option, ULocale locale )
	{
		if ( option == '<' )
			return UCharacter.toLowerCase( locale, val );
		else if ( option == '>' )
			return UCharacter.toUpperCase( locale, val );
		else
			return val;

	}

} //StringFormatSpecifierImpl
