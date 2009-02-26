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
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.CursorImpl;
import org.eclipse.birt.chart.model.attribute.impl.FillImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.common.util.EList;
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
		Label la = LabelImpl.copyInstance( getLabel( ) );
		final String sPreviousValue = la.getCaption( ).getValue( );
		la.getCaption( ).setValue( rtc.externalizedMessage( sPreviousValue ) );
		// ellipsis always enabled for chart title
		la.setEllipsis( 1 );
		Map<Label, LabelLimiter> mapLimiter = rtc.getState( RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY );
		LabelLimiter lbLimiter = mapLimiter.get( getLabel( ) );
		lbLimiter.computeWrapping( xs, la );
		LabelLimiter lbLimiterNew = lbLimiter.limitLabelSize( xs,
				la,
				EnumSet.of( LabelLimiter.Option.FIX_WIDTH ) );
		mapLimiter.put( getLabel( ), lbLimiterNew );
		return lbLimiterNew.getBounding( null );
		// Do not set the text back because of wrapping
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static TitleBlock copyInstance( TitleBlock src )
	{
		if ( src == null )
		{
			return null;
		}

		TitleBlockImpl dest = new TitleBlockImpl( );

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

		if ( src.getLabel( ) != null )
		{
			dest.setLabel( LabelImpl.copyInstance( src.getLabel( ) ) );
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

		return dest;
	}

} //TitleBlockImpl
