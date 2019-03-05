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
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.ImageSourceType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Image</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.ImageImpl#getURL <em>URL</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.ImageImpl#getSource <em>Source</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImageImpl extends FillImpl implements Image
{

	/**
	 * The default value of the '{@link #getURL() <em>URL</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getURL()
	 * @generated
	 * @ordered
	 */
	protected static final String URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getURL() <em>URL</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getURL()
	 * @generated
	 * @ordered
	 */
	protected String uRL = URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected static final ImageSourceType SOURCE_EDEFAULT = ImageSourceType.STATIC;

	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected ImageSourceType source = SOURCE_EDEFAULT;

	/**
	 * This is true if the Source attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean sourceESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ImageImpl( )
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
		return AttributePackage.Literals.IMAGE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getURL( )
	{
		return uRL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setURL( String newURL )
	{
		String oldURL = uRL;
		uRL = newURL;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.IMAGE__URL,
					oldURL,
					uRL ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImageSourceType getSource( )
	{
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource( ImageSourceType newSource )
	{
		ImageSourceType oldSource = source;
		source = newSource == null ? SOURCE_EDEFAULT : newSource;
		boolean oldSourceESet = sourceESet;
		sourceESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.IMAGE__SOURCE,
					oldSource,
					source,
					!oldSourceESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSource( )
	{
		ImageSourceType oldSource = source;
		boolean oldSourceESet = sourceESet;
		source = SOURCE_EDEFAULT;
		sourceESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.IMAGE__SOURCE,
					oldSource,
					SOURCE_EDEFAULT,
					oldSourceESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSource( )
	{
		return sourceESet;
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
			case AttributePackage.IMAGE__URL :
				return getURL( );
			case AttributePackage.IMAGE__SOURCE :
				return getSource( );
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
			case AttributePackage.IMAGE__URL :
				setURL( (String) newValue );
				return;
			case AttributePackage.IMAGE__SOURCE :
				setSource( (ImageSourceType) newValue );
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
			case AttributePackage.IMAGE__URL :
				setURL( URL_EDEFAULT );
				return;
			case AttributePackage.IMAGE__SOURCE :
				unsetSource( );
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
			case AttributePackage.IMAGE__URL :
				return URL_EDEFAULT == null ? uRL != null
						: !URL_EDEFAULT.equals( uRL );
			case AttributePackage.IMAGE__SOURCE :
				return isSetSource( );
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
		result.append( " (uRL: " ); //$NON-NLS-1$
		result.append( uRL );
		result.append( ", source: " ); //$NON-NLS-1$
		if ( sourceESet )
			result.append( source );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenient method to create and initialize an Image instance.
	 * 
	 * NOTE: Manually written
	 * 
	 * @param sURL
	 * @return image
	 */
	public static final Image create( String sURL )
	{
		final Image i = AttributeFactory.eINSTANCE.createImage( );
		i.setURL( sURL );
		return i;
	}
	
	/**
	 * A convenient method to create and initialize an Image instance.
	 * 
	 * NOTE: Manually written
	 * @param sURL
	 * @param imageSourceType
	 * @return image
	 */
	public static final Image create( String sURL, ImageSourceType source )
	{
		final Image i = AttributeFactory.eINSTANCE.createImage( );
		i.setURL( sURL );
		i.setSource( source );
		return i;
	}

	@Override
	public int hashCode( )
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ( ( source == null ) ? 0 : source.hashCode( ) );
		result = prime * result + ( ( uRL == null ) ? 0 : uRL.hashCode( ) );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass( ) != obj.getClass( ) )
			return false;
		ImageImpl other = (ImageImpl) obj;
		if ( source != other.source )
			return false;
		if ( uRL == null )
		{
			if ( other.uRL != null )
				return false;
		}
		else if ( !uRL.equals( other.uRL ) )
			return false;
		return true;
	}

	
	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public Image copyInstance( )
	{
		ImageImpl dest = new ImageImpl( );
		dest.uRL = getURL( );
		dest.source = getSource( );
		dest.sourceESet = isSetSource( );

		return dest;
	}

	protected void set( Image src )
	{
		uRL = src.getURL( );
		source=src.getSource( );
		sourceESet = src.isSetSource( );
	}

} // ImageImpl
