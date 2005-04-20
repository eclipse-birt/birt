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

package org.eclipse.birt.report.designer.core.model;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.birt.report.designer.core.util.mediator.ReportMediator;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStack;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Adapter class to adpat model handle. This adapter provides convenience
 * methods to GUI requirement SessionHandleAdapter responds to model
 * SessionHandle
 *  
 */

public class SessionHandleAdapter
{
	//add field support mediator
	private Map mediatorMap = new WeakHashMap();
	

	/**
	 * constructor
	 * Mark it to private to avoid new opeartion.
	 */
	private SessionHandleAdapter( )
	{

	}

	private static SessionHandleAdapter sessionAdapter;

	private SessionHandle sessionHandle;

	private ReportDesignHandleAdapter designHandleAdapter;

	/**
	 * Gets singleton instance method
	 * 
	 * @return return SessionHandleAdapter instance
	 */

	public static SessionHandleAdapter getInstance( )
	{
		if ( sessionAdapter == null )
		{
			sessionAdapter = new SessionHandleAdapter( );
		}
		return sessionAdapter;
	}

	/**
	 * @return Session handle
	 */

	public SessionHandle getSessionHandle( )
	{
		if ( sessionHandle == null )
		{
			sessionHandle = DesignEngine.newSession( Locale.getDefault( ) );
			IMetaDataDictionary metadata = DesignEngine.getMetaDataDictionary();
			metadata.enableElementID( );
		}
		sessionHandle.activate( );
		return sessionHandle;
	}

	/**
	 * Initialize a report design file
	 * 
	 * @param fileName
	 *            design file name
	 * @param input
	 *            File input stream
	 * @throws DesignFileException
	 */

	public void init( String fileName, InputStream input )
			throws DesignFileException
	{
		try
		{
			ReportDesignHandle handle = getSessionHandle( ).openDesign( fileName,
					input );
			
			postInit(handle);

			designHandleAdapter = new ReportDesignHandleAdapter( handle );
		}
		catch ( DesignFileException e )
		{
			throw e;
		}
	}

	/**
	 * @param handle
	 */
	private void postInit( ReportDesignHandle handle )
	{
		SimpleMasterPageHandle masterPage = null;
		if ( handle.getMasterPages( ).getCount( ) == 0 )
		{
			masterPage = handle.getElementFactory( )
					.newSimpleMasterPage( "Simple MasterPage" ); //$NON-NLS-1$
			try
			{
				handle.getMasterPages( ).add( masterPage );
			}
			catch ( ContentException e )
			{
				new DesignFileException(handle.getFileName(),e);
			}
			catch ( NameException e )
			{
				new DesignFileException(handle.getFileName(),e);
			}
		}
	}

	/**
	 * Create report design instance
	 * 
	 * @return created report design instance
	 */
	public ReportDesignHandle creatReportDesign( )
	{
		return getSessionHandle( ).createDesign( );
	}

	/**
	 * Gets the report design instance for opening design file
	 * 
	 * @return wrapped report design
	 */

	public ReportDesign getReportDesign( )
	{
		if ( designHandleAdapter != null )
		{
			return designHandleAdapter.getReportDesignHandle( ).getDesign( );
		}
		return null;
	}

	/**
	 * @return wrapped report design handle.
	 */
	public ReportDesignHandle getReportDesignHandle( )
	{
		if ( designHandleAdapter != null )
		{
			return designHandleAdapter.getReportDesignHandle( );
		}
		return null;

	}

	/**
	 * Sets report design.
	 * 
	 * @param obj
	 */
	public void setReportDesignHandle( ReportDesignHandle handle )
	{

		designHandleAdapter.setReportDesignHandle( handle );
	}

	/**
	 * @return activity stack of current session.
	 */
	public ActivityStack getActivityStack( )
	{
		if ( getReportDesign( ) != null )
		{
			return getReportDesign( ).getActivityStack( );
		}

		return null;
	}

	/**
	 * @return Command stack of current session.
	 */
	public CommandStack getCommandStack( )
	{
		if ( getReportDesign( ) != null )
		{
			return getReportDesignHandle( ).getCommandStack( );
		}

		return null;
	}
	
	/**
	 * Gets the first MasterPageHandle
	 * 
	 * @return
	 */
	public MasterPageHandle getMasterPageHandle( )
	{
		SlotHandle slotHandle = getReportDesignHandle( ).getMasterPages( );
		Iterator iter = slotHandle.iterator( );
		return (MasterPageHandle) iter.next( );
	}
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public ReportMediator getMediator(ReportDesignHandle handle)
	{
		ReportMediator mediator = (ReportMediator)mediatorMap.get(handle);
		if (mediator == null)
		{
			mediator = new ReportMediator();
			mediatorMap.put(handle, mediator);
		}
		return mediator;
	}
	
	/**
	 * @return
	 */
	public ReportMediator getMediator()
	{
		return getMediator(getReportDesignHandle());
	}
}