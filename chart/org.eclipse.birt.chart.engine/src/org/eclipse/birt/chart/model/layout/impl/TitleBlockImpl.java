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

import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Title Block</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class TitleBlockImpl extends LabelBlockImpl implements TitleBlock
{

	/**
	 * The default value of the '{@link #isAuto() <em>Auto</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAuto()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTO_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isAuto() <em>Auto</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAuto()
	 * @generated
	 * @ordered
	 */
	protected boolean auto = AUTO_EDEFAULT;
	/**
	 * This is true if the Auto attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean autoESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected TitleBlockImpl( )
	{
		super( );
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isTitle( )
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return LayoutPackage.Literals.TITLE_BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAuto( )
	{
		return auto;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAuto( boolean newAuto )
	{
		boolean oldAuto = auto;
		auto = newAuto;
		boolean oldAutoESet = autoESet;
		autoESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					LayoutPackage.TITLE_BLOCK__AUTO,
					oldAuto,
					auto,
					!oldAutoESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAuto( )
	{
		boolean oldAuto = auto;
		boolean oldAutoESet = autoESet;
		auto = AUTO_EDEFAULT;
		autoESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					LayoutPackage.TITLE_BLOCK__AUTO,
					oldAuto,
					AUTO_EDEFAULT,
					oldAutoESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAuto( )
	{
		return autoESet;
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
			case LayoutPackage.TITLE_BLOCK__AUTO :
				return isAuto( );
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
			case LayoutPackage.TITLE_BLOCK__AUTO :
				setAuto( (Boolean) newValue );
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
			case LayoutPackage.TITLE_BLOCK__AUTO :
				unsetAuto( );
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
			case LayoutPackage.TITLE_BLOCK__AUTO :
				return isSetAuto( );
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
		result.append( " (auto: " ); //$NON-NLS-1$
		if ( autoESet )
			result.append( auto );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience method to create an initialized 'TitleBlock' instance
	 * 
	 * @return
	 */
	public static Block create( )
	{
		final TitleBlock tb = LayoutFactory.eINSTANCE.createTitleBlock( );
		( (TitleBlockImpl) tb ).initialize( );
		return tb;
	}
	
	/**
	 * A convenience method to create an initialized 'TitleBlock' instance
	 * 
	 * @return
	 */
	public static Block createDefault( )
	{
		final TitleBlock tb = LayoutFactory.eINSTANCE.createTitleBlock( );
		( (TitleBlockImpl) tb ).initDefault( );
		return tb;
	}

	protected BoundingBox computeBox( IDisplayServer xs, RunTimeContext rtc )
			throws ChartException
	{
		Label la = goFactory.copyOf( getLabel( ) );
		final String sPreviousValue = la.getCaption( ).getValue( );
		la.getCaption( ).setValue( rtc.externalizedMessage( sPreviousValue ) );
		// ellipsis always enabled for chart title
		la.setEllipsis( 1 );
		Map<Label, LabelLimiter> mapLimiter = rtc.getState( RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY );
		LabelLimiter lbLimiter = mapLimiter.get( getLabel( ) );
		lbLimiter.computeWrapping( xs, la );
		// int iTitileAnchor = getAnchor( ).getValue( );
		// EnumSet<LabelLimiter.Option> option = iTitileAnchor == Anchor.EAST
		// || iTitileAnchor == Anchor.WEST ? EnumSet.of(
		// LabelLimiter.Option.FIX_HEIGHT )
		// : EnumSet.of( LabelLimiter.Option.FIX_WIDTH );
		IChartComputation cComp = rtc.getState( StateKey.CHART_COMPUTATION_KEY );
		LabelLimiter lbLimiterNew = lbLimiter.limitLabelSize( cComp, xs, la );
		mapLimiter.put( getLabel( ), lbLimiterNew );
		return lbLimiterNew.getBounding( null );
		// Do not set the text back because of wrapping
	}

	/**
	 * @generated
	 */
	public TitleBlock copyInstance( )
	{
		TitleBlockImpl dest = new TitleBlockImpl( );
		dest.set( this );
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set( TitleBlock src )
	{

		super.set( src );

		// attributes

		auto = src.isAuto( );

		autoESet = src.isSetAuto( );

	}

} //TitleBlockImpl
