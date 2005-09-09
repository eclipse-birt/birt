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

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dial Region</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.DialRegionImpl#getInnerRadius <em>Inner Radius</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.DialRegionImpl#getOuterRadius <em>Outer Radius</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DialRegionImpl extends MarkerRangeImpl implements DialRegion
{

	/**
	 * The default value of the '{@link #getInnerRadius() <em>Inner Radius</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getInnerRadius()
	 * @generated
	 * @ordered
	 */
	protected static final double INNER_RADIUS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getInnerRadius() <em>Inner Radius</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getInnerRadius()
	 * @generated
	 * @ordered
	 */
	protected double innerRadius = INNER_RADIUS_EDEFAULT;

	/**
	 * This is true if the Inner Radius attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean innerRadiusESet = false;

	/**
	 * The default value of the '{@link #getOuterRadius() <em>Outer Radius</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOuterRadius()
	 * @generated
	 * @ordered
	 */
	protected static final double OUTER_RADIUS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getOuterRadius() <em>Outer Radius</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOuterRadius()
	 * @generated
	 * @ordered
	 */
	protected double outerRadius = OUTER_RADIUS_EDEFAULT;

	/**
	 * This is true if the Outer Radius attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean outerRadiusESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DialRegionImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return ComponentPackage.eINSTANCE.getDialRegion( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getInnerRadius( )
	{
		return innerRadius;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setInnerRadius( double newInnerRadius )
	{
		double oldInnerRadius = innerRadius;
		innerRadius = newInnerRadius;
		boolean oldInnerRadiusESet = innerRadiusESet;
		innerRadiusESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.DIAL_REGION__INNER_RADIUS,
					oldInnerRadius,
					innerRadius,
					!oldInnerRadiusESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInnerRadius( )
	{
		double oldInnerRadius = innerRadius;
		boolean oldInnerRadiusESet = innerRadiusESet;
		innerRadius = INNER_RADIUS_EDEFAULT;
		innerRadiusESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.DIAL_REGION__INNER_RADIUS,
					oldInnerRadius,
					INNER_RADIUS_EDEFAULT,
					oldInnerRadiusESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInnerRadius( )
	{
		return innerRadiusESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getOuterRadius( )
	{
		return outerRadius;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setOuterRadius( double newOuterRadius )
	{
		double oldOuterRadius = outerRadius;
		outerRadius = newOuterRadius;
		boolean oldOuterRadiusESet = outerRadiusESet;
		outerRadiusESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.DIAL_REGION__OUTER_RADIUS,
					oldOuterRadius,
					outerRadius,
					!oldOuterRadiusESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOuterRadius( )
	{
		double oldOuterRadius = outerRadius;
		boolean oldOuterRadiusESet = outerRadiusESet;
		outerRadius = OUTER_RADIUS_EDEFAULT;
		outerRadiusESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.DIAL_REGION__OUTER_RADIUS,
					oldOuterRadius,
					OUTER_RADIUS_EDEFAULT,
					oldOuterRadiusESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOuterRadius( )
	{
		return outerRadiusESet;
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
				case ComponentPackage.DIAL_REGION__OUTLINE :
					return basicSetOutline( null, msgs );
				case ComponentPackage.DIAL_REGION__FILL :
					return basicSetFill( null, msgs );
				case ComponentPackage.DIAL_REGION__START_VALUE :
					return basicSetStartValue( null, msgs );
				case ComponentPackage.DIAL_REGION__END_VALUE :
					return basicSetEndValue( null, msgs );
				case ComponentPackage.DIAL_REGION__LABEL :
					return basicSetLabel( null, msgs );
				case ComponentPackage.DIAL_REGION__FORMAT_SPECIFIER :
					return basicSetFormatSpecifier( null, msgs );
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
			case ComponentPackage.DIAL_REGION__OUTLINE :
				return getOutline( );
			case ComponentPackage.DIAL_REGION__FILL :
				return getFill( );
			case ComponentPackage.DIAL_REGION__START_VALUE :
				return getStartValue( );
			case ComponentPackage.DIAL_REGION__END_VALUE :
				return getEndValue( );
			case ComponentPackage.DIAL_REGION__LABEL :
				return getLabel( );
			case ComponentPackage.DIAL_REGION__LABEL_ANCHOR :
				return getLabelAnchor( );
			case ComponentPackage.DIAL_REGION__FORMAT_SPECIFIER :
				return getFormatSpecifier( );
			case ComponentPackage.DIAL_REGION__INNER_RADIUS :
				return new Double( getInnerRadius( ) );
			case ComponentPackage.DIAL_REGION__OUTER_RADIUS :
				return new Double( getOuterRadius( ) );
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
			case ComponentPackage.DIAL_REGION__OUTLINE :
				setOutline( (LineAttributes) newValue );
				return;
			case ComponentPackage.DIAL_REGION__FILL :
				setFill( (Fill) newValue );
				return;
			case ComponentPackage.DIAL_REGION__START_VALUE :
				setStartValue( (DataElement) newValue );
				return;
			case ComponentPackage.DIAL_REGION__END_VALUE :
				setEndValue( (DataElement) newValue );
				return;
			case ComponentPackage.DIAL_REGION__LABEL :
				setLabel( (Label) newValue );
				return;
			case ComponentPackage.DIAL_REGION__LABEL_ANCHOR :
				setLabelAnchor( (Anchor) newValue );
				return;
			case ComponentPackage.DIAL_REGION__FORMAT_SPECIFIER :
				setFormatSpecifier( (FormatSpecifier) newValue );
				return;
			case ComponentPackage.DIAL_REGION__INNER_RADIUS :
				setInnerRadius( ( (Double) newValue ).doubleValue( ) );
				return;
			case ComponentPackage.DIAL_REGION__OUTER_RADIUS :
				setOuterRadius( ( (Double) newValue ).doubleValue( ) );
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
			case ComponentPackage.DIAL_REGION__OUTLINE :
				setOutline( (LineAttributes) null );
				return;
			case ComponentPackage.DIAL_REGION__FILL :
				setFill( (Fill) null );
				return;
			case ComponentPackage.DIAL_REGION__START_VALUE :
				setStartValue( (DataElement) null );
				return;
			case ComponentPackage.DIAL_REGION__END_VALUE :
				setEndValue( (DataElement) null );
				return;
			case ComponentPackage.DIAL_REGION__LABEL :
				setLabel( (Label) null );
				return;
			case ComponentPackage.DIAL_REGION__LABEL_ANCHOR :
				unsetLabelAnchor( );
				return;
			case ComponentPackage.DIAL_REGION__FORMAT_SPECIFIER :
				setFormatSpecifier( (FormatSpecifier) null );
				return;
			case ComponentPackage.DIAL_REGION__INNER_RADIUS :
				unsetInnerRadius( );
				return;
			case ComponentPackage.DIAL_REGION__OUTER_RADIUS :
				unsetOuterRadius( );
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
			case ComponentPackage.DIAL_REGION__OUTLINE :
				return outline != null;
			case ComponentPackage.DIAL_REGION__FILL :
				return fill != null;
			case ComponentPackage.DIAL_REGION__START_VALUE :
				return startValue != null;
			case ComponentPackage.DIAL_REGION__END_VALUE :
				return endValue != null;
			case ComponentPackage.DIAL_REGION__LABEL :
				return label != null;
			case ComponentPackage.DIAL_REGION__LABEL_ANCHOR :
				return isSetLabelAnchor( );
			case ComponentPackage.DIAL_REGION__FORMAT_SPECIFIER :
				return formatSpecifier != null;
			case ComponentPackage.DIAL_REGION__INNER_RADIUS :
				return isSetInnerRadius( );
			case ComponentPackage.DIAL_REGION__OUTER_RADIUS :
				return isSetOuterRadius( );
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
		result.append( " (innerRadius: " ); //$NON-NLS-1$
		if ( innerRadiusESet )
			result.append( innerRadius );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", outerRadius: " ); //$NON-NLS-1$
		if ( outerRadiusESet )
			result.append( outerRadius );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * @return
	 */
	public static final DialRegion create( )
	{
		DialRegion dr = ComponentFactory.eINSTANCE.createDialRegion( );
		( (DialRegionImpl) dr ).initialize( );
		return dr;
	}

	/**
	 * 
	 */
	public final void initialize( )
	{
		LineAttributes liaOutline = LineAttributesImpl.create( ColorDefinitionImpl.BLACK( ),
				LineStyle.SOLID_LITERAL,
				1 );
		liaOutline.setVisible( false );
		setOutline( liaOutline );
		setLabel( LabelImpl.create( ) );
		setLabelAnchor( Anchor.NORTH_LITERAL );
	}

} // DialRegionImpl
