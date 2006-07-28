/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventFilter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * 
 */

public abstract class AbstractModelEventProcessor
{
	private List infoList = new ArrayList( );
	private IModelEventFactory factory;
	private IModelEventFilter filter;
	private ModelEventInfoFactory eventFactory = createModelEventInfoFactory( );
	//private Comparator postSequence = createPostSequence();
	/**
	 * @param factory
	 */
	public AbstractModelEventProcessor( IModelEventFactory factory )
	{
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor#addElementEvent(org.eclipse.birt.report.model.api.DesignElementHandle, org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void addElementEvent( DesignElementHandle focus, NotificationEvent ev )
	{
		boolean isAccept = false;
		List temp = new ArrayList(infoList);
		int size = temp.size( );
		ModelEventInfo newInfo = eventFactory.createModelEventInfo( focus, ev );
		for ( int i = 0; i < size; i++ )
		{
			ModelEventInfo info = (ModelEventInfo) temp.get( i );
			if ( info.canAcceptModelEvent( newInfo) )
			{
				info.addModelEvent( newInfo);
				isAccept = true;
				break;
			}
			if (newInfo.canAcceptModelEvent( info ))
			{
				newInfo.addModelEvent( info);
				infoList.remove( info );
				continue;
			}
		}
		if ( !isAccept )
		{
			infoList.add( newInfo );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor#clear()
	 */
	public void clear( )
	{
		infoList.clear( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor#postElementEvent()
	 */
	public void postElementEvent( )
	{
		int size = infoList.size( );
		for ( int i = 0; i < size; i++ )
		{
			ModelEventInfo info = (ModelEventInfo) infoList.get( i );
			Runnable run = factory.createModelEventRunnable( info.getTarget( ),
					info.getType( ),
					info.getOtherInfo( ) );
			if ( run != null )
			{
				run.run( );
			}

		}
		infoList.clear( );
	}

	/**
	 * IModelEventFactory
	 */
	public interface IModelEventFactory
	{
		/**Gets the reportrunnable from the model event infomation.
		 * @param focus
		 * @param type
		 * @param args
		 * @return
		 */
		Runnable createModelEventRunnable( Object focus, int type,
				Map args );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		if ( adapter == IModelEventFilter.class )
		{
			if ( filter == null )
			{
				filter = new IModelEventFilter( ) {

					public boolean filterModelEvent( DesignElementHandle focus,
							NotificationEvent ev )
					{
						if (includeEventType( ev.getEventType( ) ))
						{
							return false;
						}
						else
						{
							return true;
						}
					}

				};
			}
			return filter;
		}
		return null;
	}

	/**Filter the event.
	 * @param type
	 * @return
	 */
	protected boolean includeEventType( int type )
	{
		return true;
	}

	/**
	 * Because the model event, Be care the target and type.Maybe in the future,
	 * need the detail infomation.
	 */
	protected static class ModelEventInfo
	{

		private DesignElementHandle target;
		private int type;
		private Map otherInfo = new HashMap();

		/**
		 * @param focus
		 * @param ev
		 */
		public ModelEventInfo( DesignElementHandle focus, NotificationEvent ev )
		{
			setTarget( focus );
			setType( ev.getEventType( ) );
		}

		/**
		 * @param focus
		 * @param ev
		 * @return
		 */
		public boolean canAcceptModelEvent( ModelEventInfo info)
		{
			return getTarget( ).equals( info.getTarget( ) )
					&& info.getType( ) == getType( );
		}

		/**
		 * @param focus
		 * @param ev
		 */
		public void addModelEvent( ModelEventInfo info )
		{
			// do nothing now
		}

		/**
		 * @return
		 */
		public DesignElementHandle getTarget( )
		{
			return target;
		}

		/**
		 * @param target
		 */
		public void setTarget( DesignElementHandle target )
		{
			this.target = target;
		}

		/**
		 * @return
		 */
		public int getType( )
		{
			return type;
		}

		/**
		 * @param type
		 */
		public void setType( int type )
		{
			this.type = type;
		}

		/**
		 * @return
		 */
		public Map getOtherInfo( )
		{
			// now retrun null
			return otherInfo;
		}
	}
	
	
	/**Creat the factor to ctreat the report runnable.
	 * @return
	 */
	protected abstract ModelEventInfoFactory createModelEventInfoFactory();
	
	
	/**
	 * ModelEventInfoFactory
	 */
	public interface  ModelEventInfoFactory
	{
		/**Creat the report runnable for the ReportEditorWithPalette.
		 * @param focus
		 * @param ev
		 * @return
		 */
		public  ModelEventInfo createModelEventInfo(DesignElementHandle focus, NotificationEvent ev);
		
	}

	
	public IModelEventFactory getFactory( )
	{
		return factory;
	}
}
