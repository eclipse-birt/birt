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

import java.util.EnumSet;
import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.ecore.EClass;

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

	protected BoundingBox computeBox( IDisplayServer xs, RunTimeContext rtc )
			throws ChartException
	{
		Label la = getLabel( ).copyInstance( );
		final String sPreviousValue = la.getCaption( ).getValue( );
		la.getCaption( ).setValue( rtc.externalizedMessage( sPreviousValue ) );
		// ellipsis always enabled for chart title
		la.setEllipsis( 1 );
		Map<Label, LabelLimiter> mapLimiter = rtc.getState( RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY );
		LabelLimiter lbLimiter = mapLimiter.get( getLabel( ) );
		lbLimiter.computeWrapping( xs, la );
		int iTitileAnchor = getAnchor( ).getValue( );
		EnumSet<LabelLimiter.Option> option = iTitileAnchor == Anchor.EAST
				|| iTitileAnchor == Anchor.WEST ? EnumSet.of( LabelLimiter.Option.FIX_HEIGHT )
				: EnumSet.of( LabelLimiter.Option.FIX_WIDTH );
		LabelLimiter lbLimiterNew = lbLimiter.limitLabelSize( xs, la, option );
		mapLimiter.put( getLabel( ), lbLimiterNew );
		return lbLimiterNew.getBounding( null );
		// Do not set the text back because of wrapping
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public TitleBlock copyInstance( )
	{
		TitleBlockImpl dest = new TitleBlockImpl( );
		dest.set( this );
		return dest;
	}

	protected void set( TitleBlock src )
	{
		super.set( src );

	}

} //TitleBlockImpl
