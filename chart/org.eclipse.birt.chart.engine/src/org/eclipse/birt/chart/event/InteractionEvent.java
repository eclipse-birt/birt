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
 * InteractionEvent
 */
public final class InteractionEvent extends PrimitiveRenderEvent
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3554746649816942383L;

	private PrimitiveRenderEvent _pre = null;

	private final LinkedHashMap _lhmTriggers = new LinkedHashMap( );

	/**
	 * @param source
	 */
	public InteractionEvent( Object source )
	{
		super( source );
	}

	/**
	 * 
	 * @param pre
	 */
	public final void setHotSpot( PrimitiveRenderEvent pre )
	{
		_pre = pre;
	}

	/**
	 * 
	 * @return
	 */
	public final PrimitiveRenderEvent getHotSpot( )
	{
		return _pre;
	}

	/**
	 * 
	 * @param t
	 */
	public final void addTrigger( Trigger t )
	{
		_lhmTriggers.put( t.getCondition( ), t.getAction( ) );
	}

	/**
	 * 
	 * @param tc
	 * @return
	 */
	public final Action getAction( TriggerCondition tc )
	{
		return (Action) _lhmTriggers.get( tc );
	}

	/**
	 * 
	 * @param tc
	 * @return
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

	/**
	 *  
	 */
	public final void reuse( Object oNewSource )
	{
		source = oNewSource;
		_lhmTriggers.clear( );
	}
}