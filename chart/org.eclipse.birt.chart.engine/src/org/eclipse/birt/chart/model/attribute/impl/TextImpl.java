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
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Text</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl#getFont <em>Font</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl#getColor <em>Color</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextImpl extends EObjectImpl implements Text
{

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFont() <em>Font</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFont()
	 * @generated
	 * @ordered
	 */
	protected FontDefinition font = null;

	/**
	 * The cached value of the '{@link #getColor() <em>Color</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition color = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected TextImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getText( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue( )
	{
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue( String newValue )
	{
		String oldValue = value;
		value = newValue;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TEXT__VALUE,
					oldValue,
					value ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public FontDefinition getFont( )
	{
		return font;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFont( FontDefinition newFont,
			NotificationChain msgs )
	{
		FontDefinition oldFont = font;
		font = newFont;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TEXT__FONT,
					oldFont,
					newFont );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setFont( FontDefinition newFont )
	{
		if ( newFont != font )
		{
			NotificationChain msgs = null;
			if ( font != null )
				msgs = ( (InternalEObject) font ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__FONT,
						null,
						msgs );
			if ( newFont != null )
				msgs = ( (InternalEObject) newFont ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__FONT,
						null,
						msgs );
			msgs = basicSetFont( newFont, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TEXT__FONT,
					newFont,
					newFont ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ColorDefinition getColor( )
	{
		return color;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetColor( ColorDefinition newColor,
			NotificationChain msgs )
	{
		ColorDefinition oldColor = color;
		color = newColor;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TEXT__COLOR,
					oldColor,
					newColor );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setColor( ColorDefinition newColor )
	{
		if ( newColor != color )
		{
			NotificationChain msgs = null;
			if ( color != null )
				msgs = ( (InternalEObject) color ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__COLOR,
						null,
						msgs );
			if ( newColor != null )
				msgs = ( (InternalEObject) newColor ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__COLOR,
						null,
						msgs );
			msgs = basicSetColor( newColor, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TEXT__COLOR,
					newColor,
					newColor ) );
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
				case AttributePackage.TEXT__FONT :
					return basicSetFont( null, msgs );
				case AttributePackage.TEXT__COLOR :
					return basicSetColor( null, msgs );
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
			case AttributePackage.TEXT__VALUE :
				return getValue( );
			case AttributePackage.TEXT__FONT :
				return getFont( );
			case AttributePackage.TEXT__COLOR :
				return getColor( );
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
			case AttributePackage.TEXT__VALUE :
				setValue( (String) newValue );
				return;
			case AttributePackage.TEXT__FONT :
				setFont( (FontDefinition) newValue );
				return;
			case AttributePackage.TEXT__COLOR :
				setColor( (ColorDefinition) newValue );
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
			case AttributePackage.TEXT__VALUE :
				setValue( VALUE_EDEFAULT );
				return;
			case AttributePackage.TEXT__FONT :
				setFont( (FontDefinition) null );
				return;
			case AttributePackage.TEXT__COLOR :
				setColor( (ColorDefinition) null );
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
			case AttributePackage.TEXT__VALUE :
				return VALUE_EDEFAULT == null ? value != null
						: !VALUE_EDEFAULT.equals( value );
			case AttributePackage.TEXT__FONT :
				return font != null;
			case AttributePackage.TEXT__COLOR :
				return color != null;
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
		result.append( " (value: " ); //$NON-NLS-1$
		result.append( value );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize( )
	{
		// only initialize empty font here.
		setFont( FontDefinitionImpl.createEmpty( ) );
	}

	/**
	 * A convenience method provided to create a Text instance
	 * 
	 * @param sValue
	 */
	public static final Text create( String sValue )
	{
		final Text tx = AttributeFactory.eINSTANCE.createText( );
		( (TextImpl) tx ).initialize( );
		if ( sValue == null )
		{
			sValue = "<undefined>"; //$NON-NLS-1$
		}
		tx.setValue( sValue );
		return tx;
	}

} // TextImpl
