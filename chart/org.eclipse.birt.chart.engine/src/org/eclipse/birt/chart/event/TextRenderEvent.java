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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * TextRenderEvent
 */
public final class TextRenderEvent extends PrimitiveRenderEvent
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1683131722588162319L;

	/**
	 * An undefined action that will result in an error
	 */
	public static final int UNDEFINED = 0;

	/**
	 * Renders a shadow offset with the encapsulating container rectangle's
	 * corner or edge aligned with a given point
	 * 
	 * This action requires Label, Location, TextPosition to be set
	 */
	public static final int RENDER_SHADOW_AT_LOCATION = 1;

	/**
	 * Renders text (with optional insets, border, fill, etc) with the
	 * encapsulating container rectangle's corner or edge aligning with a given
	 * point
	 * 
	 * This action requires Label, Location, TextPosition to be set
	 */
	public static final int RENDER_TEXT_AT_LOCATION = 2;

	/**
	 * Renders text (with optional insets, border, fill, etc) with the
	 * encapsulating container rectangle's bounding box aligned with a parent
	 * block's bounds
	 * 
	 * This action requires Label, BlockBounds, BlockAlignment to be set
	 */
	public static final int RENDER_TEXT_IN_BLOCK = 3;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that
	 * the text is positioned to the left of the reference point 'Location'
	 */
	public static final int LEFT = IConstants.LEFT;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that
	 * the text is positioned to the right of the reference point 'Location'
	 */
	public static final int RIGHT = IConstants.RIGHT;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that
	 * the text is positioned above the reference point 'Location'
	 */
	public static final int ABOVE = IConstants.ABOVE;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that
	 * the text is positioned below the reference point 'Location'
	 */
	public static final int BELOW = IConstants.BELOW;

	/**
	 * The bounds of the enclosing block space in which the text's bounding box
	 * will be aligned
	 */
	private Bounds _boBlock;

	/**
	 *  
	 */
	private Label _la;

	/**
	 *  
	 */
	private TextAlignment _taBlock;

	/**
	 *  
	 */
	private int _iAction = UNDEFINED;

	/**
	 *  
	 */
	private Location _lo;

	/**
	 *  
	 */
	private int _iTextPosition;

	/**
	 * 
	 * @param oSource
	 */
	public TextRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * 
	 * @param boBlock
	 */
	public final void setBlockBounds( Bounds boBlock )
	{
		_boBlock = boBlock;
	}

	/**
	 * 
	 * @return
	 */
	public final Bounds getBlockBounds( )
	{
		return _boBlock;
	}

	/**
	 * 
	 * @param la
	 */
	public final void setLabel( Label la )
	{
		_la = la;
	}

	/**
	 * 
	 * @return
	 */
	public final Label getLabel( )
	{
		return _la;
	}

	/**
	 * 
	 * @param taBlock
	 */
	public final void setBlockAlignment( TextAlignment taBlock )
	{
		_taBlock = taBlock;
	}

	/**
	 * 
	 * @return
	 */
	public final TextAlignment getBlockAlignment( )
	{
		return _taBlock;
	}

	/**
	 * 
	 * @param iAction
	 */
	public final void setAction( int iAction )
	{
		_iAction = iAction;
	}

	/**
	 * 
	 * @return
	 */
	public final int getAction( )
	{
		return _iAction;
	}

	/**
	 * 
	 * @param lo
	 */
	public final void setLocation( Location lo )
	{
		_lo = lo;
	}

	/**
	 * 
	 * @return
	 */
	public final Location getLocation( )
	{
		return _lo;
	}

	/**
	 * 
	 * @param iTextPosition
	 */
	public final void setTextPosition( int iTextPosition )
	{
		_iTextPosition = iTextPosition;
	}

	/**
	 * 
	 * @return
	 */
	public final int getTextPosition( )
	{
		return _iTextPosition;
	}

	/**
	 * 
	 * @param bl
	 */
	public final String updateFrom( LabelBlock lb, double dScale,
			RunTimeContext rtc )
	{
		final String sPreviousValue = lb.getLabel( ).getCaption( ).getValue( );
		lb.getLabel( )
				.getCaption( )
				.setValue( rtc.externalizedMessage( sPreviousValue ) );
		setLabel( lb.getLabel( ) );
		
		Bounds bo = lb.getBounds( ).scaledInstance( dScale );
		bo = bo.adjustedInstance(lb.getInsets().scaledInstance(dScale));
		
		setBlockBounds( bo );
		setBlockAlignment( null );
		setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
		return sPreviousValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public final PrimitiveRenderEvent copy( )
	{
		final TextRenderEvent tre = new TextRenderEvent( source );
		if ( _boBlock != null )
		{
			tre.setBlockBounds( (Bounds) EcoreUtil.copy( _boBlock ) );
		}
		tre.setAction( _iAction );
		tre.setTextPosition( _iTextPosition );
		if ( _la != null )
		{
			tre.setLabel( (Label) EcoreUtil.copy( _la ) );
		}
		if ( _lo != null )
		{
			tre.setLocation( (Location) EcoreUtil.copy( _lo ) );
		}
		if ( _taBlock != null )
		{
			tre.setBlockAlignment( (TextAlignment) EcoreUtil.copy( _taBlock ) );
		}
		return tre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart.device.IDeviceRenderer)
	 */
	public final void draw( IDeviceRenderer idr ) throws ChartException
	{
		idr.drawText( this );
	}
}