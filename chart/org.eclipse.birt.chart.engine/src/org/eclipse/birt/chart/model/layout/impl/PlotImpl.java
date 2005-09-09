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

package org.eclipse.birt.chart.model.layout.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Plot</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl#getHorizontalSpacing <em>Horizontal Spacing</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl#getVerticalSpacing <em>Vertical Spacing</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl#getClientArea <em>Client Area</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlotImpl extends BlockImpl implements Plot
{

	/**
	 * The default value of the '{@link #getHorizontalSpacing() <em>Horizontal Spacing</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getHorizontalSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int HORIZONTAL_SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHorizontalSpacing() <em>Horizontal Spacing</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getHorizontalSpacing()
	 * @generated
	 * @ordered
	 */
	protected int horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;

	/**
	 * This is true if the Horizontal Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean horizontalSpacingESet = false;

	/**
	 * The default value of the '
	 * {@link #getVerticalSpacing() <em>Vertical Spacing</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVerticalSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int VERTICAL_SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the '
	 * {@link #getVerticalSpacing() <em>Vertical Spacing</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVerticalSpacing()
	 * @generated
	 * @ordered
	 */
	protected int verticalSpacing = VERTICAL_SPACING_EDEFAULT;

	/**
	 * This is true if the Vertical Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean verticalSpacingESet = false;

	/**
	 * The cached value of the '{@link #getClientArea() <em>Client Area</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getClientArea()
	 * @generated
	 * @ordered
	 */
	protected ClientArea clientArea = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected PlotImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return LayoutPackage.eINSTANCE.getPlot( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getHorizontalSpacing( )
	{
		return horizontalSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setHorizontalSpacing( int newHorizontalSpacing )
	{
		int oldHorizontalSpacing = horizontalSpacing;
		horizontalSpacing = newHorizontalSpacing;
		boolean oldHorizontalSpacingESet = horizontalSpacingESet;
		horizontalSpacingESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.PLOT__HORIZONTAL_SPACING,
					oldHorizontalSpacing,
					horizontalSpacing,
					!oldHorizontalSpacingESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetHorizontalSpacing( )
	{
		int oldHorizontalSpacing = horizontalSpacing;
		boolean oldHorizontalSpacingESet = horizontalSpacingESet;
		horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;
		horizontalSpacingESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					LayoutPackage.PLOT__HORIZONTAL_SPACING,
					oldHorizontalSpacing,
					HORIZONTAL_SPACING_EDEFAULT,
					oldHorizontalSpacingESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetHorizontalSpacing( )
	{
		return horizontalSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getVerticalSpacing( )
	{
		return verticalSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setVerticalSpacing( int newVerticalSpacing )
	{
		int oldVerticalSpacing = verticalSpacing;
		verticalSpacing = newVerticalSpacing;
		boolean oldVerticalSpacingESet = verticalSpacingESet;
		verticalSpacingESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.PLOT__VERTICAL_SPACING,
					oldVerticalSpacing,
					verticalSpacing,
					!oldVerticalSpacingESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVerticalSpacing( )
	{
		int oldVerticalSpacing = verticalSpacing;
		boolean oldVerticalSpacingESet = verticalSpacingESet;
		verticalSpacing = VERTICAL_SPACING_EDEFAULT;
		verticalSpacingESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					LayoutPackage.PLOT__VERTICAL_SPACING,
					oldVerticalSpacing,
					VERTICAL_SPACING_EDEFAULT,
					oldVerticalSpacingESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVerticalSpacing( )
	{
		return verticalSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ClientArea getClientArea( )
	{
		return clientArea;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetClientArea( ClientArea newClientArea,
			NotificationChain msgs )
	{
		ClientArea oldClientArea = clientArea;
		clientArea = newClientArea;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.PLOT__CLIENT_AREA,
					oldClientArea,
					newClientArea );
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
	public void setClientArea( ClientArea newClientArea )
	{
		if ( newClientArea != clientArea )
		{
			NotificationChain msgs = null;
			if ( clientArea != null )
				msgs = ( (InternalEObject) clientArea ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- LayoutPackage.PLOT__CLIENT_AREA,
						null,
						msgs );
			if ( newClientArea != null )
				msgs = ( (InternalEObject) newClientArea ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- LayoutPackage.PLOT__CLIENT_AREA,
						null,
						msgs );
			msgs = basicSetClientArea( newClientArea, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.PLOT__CLIENT_AREA,
					newClientArea,
					newClientArea ) );
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
				case LayoutPackage.PLOT__CHILDREN :
					return ( (InternalEList) getChildren( ) ).basicRemove( otherEnd,
							msgs );
				case LayoutPackage.PLOT__BOUNDS :
					return basicSetBounds( null, msgs );
				case LayoutPackage.PLOT__INSETS :
					return basicSetInsets( null, msgs );
				case LayoutPackage.PLOT__MIN_SIZE :
					return basicSetMinSize( null, msgs );
				case LayoutPackage.PLOT__OUTLINE :
					return basicSetOutline( null, msgs );
				case LayoutPackage.PLOT__BACKGROUND :
					return basicSetBackground( null, msgs );
				case LayoutPackage.PLOT__TRIGGERS :
					return ( (InternalEList) getTriggers( ) ).basicRemove( otherEnd,
							msgs );
				case LayoutPackage.PLOT__CLIENT_AREA :
					return basicSetClientArea( null, msgs );
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
			case LayoutPackage.PLOT__CHILDREN :
				return getChildren( );
			case LayoutPackage.PLOT__BOUNDS :
				return getBounds( );
			case LayoutPackage.PLOT__ANCHOR :
				return getAnchor( );
			case LayoutPackage.PLOT__STRETCH :
				return getStretch( );
			case LayoutPackage.PLOT__INSETS :
				return getInsets( );
			case LayoutPackage.PLOT__ROW :
				return new Integer( getRow( ) );
			case LayoutPackage.PLOT__COLUMN :
				return new Integer( getColumn( ) );
			case LayoutPackage.PLOT__ROWSPAN :
				return new Integer( getRowspan( ) );
			case LayoutPackage.PLOT__COLUMNSPAN :
				return new Integer( getColumnspan( ) );
			case LayoutPackage.PLOT__MIN_SIZE :
				return getMinSize( );
			case LayoutPackage.PLOT__OUTLINE :
				return getOutline( );
			case LayoutPackage.PLOT__BACKGROUND :
				return getBackground( );
			case LayoutPackage.PLOT__VISIBLE :
				return isVisible( ) ? Boolean.TRUE : Boolean.FALSE;
			case LayoutPackage.PLOT__TRIGGERS :
				return getTriggers( );
			case LayoutPackage.PLOT__HORIZONTAL_SPACING :
				return new Integer( getHorizontalSpacing( ) );
			case LayoutPackage.PLOT__VERTICAL_SPACING :
				return new Integer( getVerticalSpacing( ) );
			case LayoutPackage.PLOT__CLIENT_AREA :
				return getClientArea( );
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
			case LayoutPackage.PLOT__CHILDREN :
				getChildren( ).clear( );
				getChildren( ).addAll( (Collection) newValue );
				return;
			case LayoutPackage.PLOT__BOUNDS :
				setBounds( (Bounds) newValue );
				return;
			case LayoutPackage.PLOT__ANCHOR :
				setAnchor( (Anchor) newValue );
				return;
			case LayoutPackage.PLOT__STRETCH :
				setStretch( (Stretch) newValue );
				return;
			case LayoutPackage.PLOT__INSETS :
				setInsets( (Insets) newValue );
				return;
			case LayoutPackage.PLOT__ROW :
				setRow( ( (Integer) newValue ).intValue( ) );
				return;
			case LayoutPackage.PLOT__COLUMN :
				setColumn( ( (Integer) newValue ).intValue( ) );
				return;
			case LayoutPackage.PLOT__ROWSPAN :
				setRowspan( ( (Integer) newValue ).intValue( ) );
				return;
			case LayoutPackage.PLOT__COLUMNSPAN :
				setColumnspan( ( (Integer) newValue ).intValue( ) );
				return;
			case LayoutPackage.PLOT__MIN_SIZE :
				setMinSize( (Size) newValue );
				return;
			case LayoutPackage.PLOT__OUTLINE :
				setOutline( (LineAttributes) newValue );
				return;
			case LayoutPackage.PLOT__BACKGROUND :
				setBackground( (Fill) newValue );
				return;
			case LayoutPackage.PLOT__VISIBLE :
				setVisible( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case LayoutPackage.PLOT__TRIGGERS :
				getTriggers( ).clear( );
				getTriggers( ).addAll( (Collection) newValue );
				return;
			case LayoutPackage.PLOT__HORIZONTAL_SPACING :
				setHorizontalSpacing( ( (Integer) newValue ).intValue( ) );
				return;
			case LayoutPackage.PLOT__VERTICAL_SPACING :
				setVerticalSpacing( ( (Integer) newValue ).intValue( ) );
				return;
			case LayoutPackage.PLOT__CLIENT_AREA :
				setClientArea( (ClientArea) newValue );
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
			case LayoutPackage.PLOT__CHILDREN :
				getChildren( ).clear( );
				return;
			case LayoutPackage.PLOT__BOUNDS :
				setBounds( (Bounds) null );
				return;
			case LayoutPackage.PLOT__ANCHOR :
				unsetAnchor( );
				return;
			case LayoutPackage.PLOT__STRETCH :
				unsetStretch( );
				return;
			case LayoutPackage.PLOT__INSETS :
				setInsets( (Insets) null );
				return;
			case LayoutPackage.PLOT__ROW :
				unsetRow( );
				return;
			case LayoutPackage.PLOT__COLUMN :
				unsetColumn( );
				return;
			case LayoutPackage.PLOT__ROWSPAN :
				unsetRowspan( );
				return;
			case LayoutPackage.PLOT__COLUMNSPAN :
				unsetColumnspan( );
				return;
			case LayoutPackage.PLOT__MIN_SIZE :
				setMinSize( (Size) null );
				return;
			case LayoutPackage.PLOT__OUTLINE :
				setOutline( (LineAttributes) null );
				return;
			case LayoutPackage.PLOT__BACKGROUND :
				setBackground( (Fill) null );
				return;
			case LayoutPackage.PLOT__VISIBLE :
				unsetVisible( );
				return;
			case LayoutPackage.PLOT__TRIGGERS :
				getTriggers( ).clear( );
				return;
			case LayoutPackage.PLOT__HORIZONTAL_SPACING :
				unsetHorizontalSpacing( );
				return;
			case LayoutPackage.PLOT__VERTICAL_SPACING :
				unsetVerticalSpacing( );
				return;
			case LayoutPackage.PLOT__CLIENT_AREA :
				setClientArea( (ClientArea) null );
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
			case LayoutPackage.PLOT__CHILDREN :
				return children != null && !children.isEmpty( );
			case LayoutPackage.PLOT__BOUNDS :
				return bounds != null;
			case LayoutPackage.PLOT__ANCHOR :
				return isSetAnchor( );
			case LayoutPackage.PLOT__STRETCH :
				return isSetStretch( );
			case LayoutPackage.PLOT__INSETS :
				return insets != null;
			case LayoutPackage.PLOT__ROW :
				return isSetRow( );
			case LayoutPackage.PLOT__COLUMN :
				return isSetColumn( );
			case LayoutPackage.PLOT__ROWSPAN :
				return isSetRowspan( );
			case LayoutPackage.PLOT__COLUMNSPAN :
				return isSetColumnspan( );
			case LayoutPackage.PLOT__MIN_SIZE :
				return minSize != null;
			case LayoutPackage.PLOT__OUTLINE :
				return outline != null;
			case LayoutPackage.PLOT__BACKGROUND :
				return background != null;
			case LayoutPackage.PLOT__VISIBLE :
				return isSetVisible( );
			case LayoutPackage.PLOT__TRIGGERS :
				return triggers != null && !triggers.isEmpty( );
			case LayoutPackage.PLOT__HORIZONTAL_SPACING :
				return isSetHorizontalSpacing( );
			case LayoutPackage.PLOT__VERTICAL_SPACING :
				return isSetVerticalSpacing( );
			case LayoutPackage.PLOT__CLIENT_AREA :
				return clientArea != null;
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
		result.append( " (horizontalSpacing: " ); //$NON-NLS-1$
		if ( horizontalSpacingESet )
			result.append( horizontalSpacing );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", verticalSpacing: " ); //$NON-NLS-1$
		if ( verticalSpacingESet )
			result.append( verticalSpacing );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isPlot( )
	{
		return true;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isCustom( )
	{
		return false;
	}

	/**
	 * A convenience method to create an initialized 'Plot' instance
	 * 
	 * @return
	 */
	public static final Block create( )
	{
		final Plot pl = LayoutFactory.eINSTANCE.createPlot( );
		( (PlotImpl) pl ).initialize( );
		return pl;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize( )
	{
		super.initialize( );

		final ClientArea ca = LayoutFactory.eINSTANCE.createClientArea( );
		( (ClientAreaImpl) ca ).initialize( );
		ca.setBackground( ColorDefinitionImpl.WHITE( ) );
		setClientArea( ca );

		setHorizontalSpacing( 5 );
		setVerticalSpacing( 5 );
	}

} // PlotImpl
