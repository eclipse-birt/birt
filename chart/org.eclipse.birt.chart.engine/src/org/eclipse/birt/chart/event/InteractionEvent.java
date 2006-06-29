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

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;

/**
 * An event type for Interactivity.
 */
public final class InteractionEvent extends ChartEvent
{

	private static final long serialVersionUID = -3554746649816942383L;

	private PrimitiveRenderEvent _pre = null;

	private final LinkedHashMap _lhmTriggers = new LinkedHashMap( );

	/**
	 * The constructor.
	 */
	public InteractionEvent( Object source )
	{
		super( source );
		if ( !( source instanceof StructureSource ) )
		{
			throw new IllegalArgumentException( );
		}
	}

	/**
	 * @return Returns the structur source of current event.
	 */
	public StructureSource getStructureSource( )
	{
		return (StructureSource) super.getSource( );
	}

	/**
	 * Sets the hotspot area defined by given rendering event for current event.
	 */
	public final void setHotSpot( PrimitiveRenderEvent pre )
	{
		_pre = pre;
	}

	/**
	 * @return Returns the rendering event defining current hotspot area.
	 */
	public final PrimitiveRenderEvent getHotSpot( )
	{
		return _pre;
	}

	/**
	 * Adds trigger to current event.
	 */
	public final void addTrigger( Trigger t )
	{
		_lhmTriggers.put( t.getCondition( ), t.getAction( ) );
	}

	/**
	 * @return Returns the action for specific trigger condition.
	 */
	public final Action getAction( TriggerCondition tc )
	{
		return (Action) _lhmTriggers.get( tc );
	}

	/**
	 * @return Returns all triggers asscociated with current event.
	 */
	public final Trigger[] getTriggers( )
	{
		if ( _lhmTriggers.isEmpty( ) )
			return null;
		Trigger[] tga = new Trigger[_lhmTriggers.size( )];
		final Iterator it = _lhmTriggers.keySet( ).iterator( );
		int i = 0;
		while ( it.hasNext( ) )
		{
			TriggerCondition tcKey = (TriggerCondition) it.next( );
			Action acValue = (Action) _lhmTriggers.get( tcKey );
			tga[i++] = TriggerImpl.create( tcKey, acValue );
		}
		return tga;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#reset()
	 */
	public void reset( )
	{
		_pre = null;
		_lhmTriggers.clear( );
	}

	/**
	 * Reuses current event by given new source object.
	 */
	public final void reuse( StructureSource oNewSource )
	{
		source = oNewSource;
		_lhmTriggers.clear( );
	}
}