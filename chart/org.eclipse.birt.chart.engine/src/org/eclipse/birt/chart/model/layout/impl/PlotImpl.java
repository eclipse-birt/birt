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

import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.CursorImpl;
import org.eclipse.birt.chart.model.attribute.impl.FillImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

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
	protected boolean horizontalSpacingESet;

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
	protected boolean verticalSpacingESet;

	/**
	 * The cached value of the '{@link #getClientArea() <em>Client Area</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getClientArea()
	 * @generated
	 * @ordered
	 */
	protected ClientArea clientArea;

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
	@Override
	protected EClass eStaticClass( )
	{
		return LayoutPackage.Literals.PLOT;
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case LayoutPackage.PLOT__CLIENT_AREA :
				return basicSetClientArea( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
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
			case LayoutPackage.PLOT__HORIZONTAL_SPACING :
				return new Integer( getHorizontalSpacing( ) );
			case LayoutPackage.PLOT__VERTICAL_SPACING :
				return new Integer( getVerticalSpacing( ) );
			case LayoutPackage.PLOT__CLIENT_AREA :
				return getClientArea( );
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
			case LayoutPackage.PLOT__HORIZONTAL_SPACING :
				return isSetHorizontalSpacing( );
			case LayoutPackage.PLOT__VERTICAL_SPACING :
				return isSetVerticalSpacing( );
			case LayoutPackage.PLOT__CLIENT_AREA :
				return clientArea != null;
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
		setClientArea( ca );

		setHorizontalSpacing( 5 );
		setVerticalSpacing( 5 );
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static Plot copyInstance( Plot src )
	{
		if ( src == null )
		{
			return null;
		}

		PlotImpl dest = new PlotImpl( );

		if ( src.getChildren( ) != null )
		{
			EList<Block> list = dest.getChildren( );
			for ( Block element : src.getChildren( ) )
			{
				list.add( BlockImpl.copyInstance( element ) );
			}
		}

		if ( src.getBounds( ) != null )
		{
			dest.setBounds( BoundsImpl.copyInstance( src.getBounds( ) ) );
		}

		if ( src.getInsets( ) != null )
		{
			dest.setInsets( InsetsImpl.copyInstance( src.getInsets( ) ) );
		}

		if ( src.getMinSize( ) != null )
		{
			dest.setMinSize( SizeImpl.copyInstance( src.getMinSize( ) ) );
		}

		if ( src.getOutline( ) != null )
		{
			dest.setOutline( LineAttributesImpl.copyInstance( src.getOutline( ) ) );
		}

		if ( src.getBackground( ) != null )
		{
			dest.setBackground( FillImpl.copyInstance( src.getBackground( ) ) );
		}

		if ( src.getTriggers( ) != null )
		{
			EList<Trigger> list = dest.getTriggers( );
			for ( Trigger element : src.getTriggers( ) )
			{
				list.add( TriggerImpl.copyInstance( element ) );
			}
		}

		if ( src.getCursor( ) != null )
		{
			dest.setCursor( CursorImpl.copyInstance( src.getCursor( ) ) );
		}

		if ( src.getClientArea( ) != null )
		{
			dest.setClientArea( ClientAreaImpl.copyInstance( src.getClientArea( ) ) );
		}

		dest.anchor = src.getAnchor( );
		dest.anchorESet = src.isSetAnchor( );
		dest.stretch = src.getStretch( );
		dest.stretchESet = src.isSetStretch( );
		dest.row = src.getRow( );
		dest.rowESet = src.isSetRow( );
		dest.column = src.getColumn( );
		dest.columnESet = src.isSetColumn( );
		dest.rowspan = src.getRowspan( );
		dest.rowspanESet = src.isSetRowspan( );
		dest.columnspan = src.getColumnspan( );
		dest.columnspanESet = src.isSetColumnspan( );
		dest.visible = src.isVisible( );
		dest.visibleESet = src.isSetVisible( );
		dest.widthHint = src.getWidthHint( );
		dest.widthHintESet = src.isSetWidthHint( );
		dest.heightHint = src.getHeightHint( );
		dest.heightHintESet = src.isSetHeightHint( );
		dest.horizontalSpacing = src.getHorizontalSpacing( );
		dest.horizontalSpacingESet = src.isSetHorizontalSpacing( );
		dest.verticalSpacing = src.getVerticalSpacing( );
		dest.verticalSpacingESet = src.isSetVerticalSpacing( );

		return dest;
	}

} // PlotImpl
