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
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.birt.report.designer.core.util.mediator.ReportMediator;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;

import com.ibm.icu.util.ULocale;

/**
 * Adapter class to adpat model handle. This adapter provides convenience
 * methods to GUI requirement SessionHandleAdapter responds to model
 * SessionHandle
 * 
 */

/**
 * @author Actuate
 *
 */
public class SessionHandleAdapter
{

	public static final int UNKNOWFILE = -1;
	public static int DESIGNEFILE = 0;
	public static int LIBRARYFILE = 1;
	public static int TEMPLATEFILE = 2;

	private int type = DESIGNEFILE;
	IDisposeListener disposeLitener = new IDisposeListener( ) {

		public void moduleDisposed( ModuleHandle targetElement, DisposeEvent ev )
		{
			ReportMediator media = (ReportMediator) mediatorMap
					.get( targetElement );
			if ( media != null )
			{
				media.dispose( );
			}
			mediatorMap.remove( targetElement );
			targetElement.removeDisposeListener( this );
		}
	};

	// add field support mediator
	private Map mediatorMap = new WeakHashMap( );

	/**
	 * constructor Mark it to private to avoid new opeartion.
	 */
	private SessionHandleAdapter( )
	{

	}

	public int getFileType( )
	{
		return type;
	}

	private static SessionHandleAdapter sessionAdapter;

	private SessionHandle sessionHandle;
	private ModuleHandle model;

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
			sessionHandle = DesignEngine.newSession( ULocale.getDefault( ) );
			IMetaDataDictionary metadata = DesignEngine.getMetaDataDictionary( );
			metadata.enableElementID( );
		}
		sessionHandle.activate( );
		return sessionHandle;
	}

	/**
	 * Open a design/library file.
	 * @param fileName
	 * @param input
	 * @return
	 * @throws DesignFileException
	 */
	public ModuleHandle init( String fileName, InputStream input,Map properties) throws DesignFileException
	{
		ModuleHandle handle = init(fileName,input);
		
		postInit( handle,properties );
		setReportDesignHandle(handle);
		return handle;
	}
	
	
	/**
	 * Open a design/library file.
	 * @param fileName
	 * @param input
	 * @return
	 * @throws DesignFileException
	 */
	public ModuleHandle init( String fileName, InputStream input) throws DesignFileException
	{
		ModuleHandle handle = null;
		handle = getSessionHandle( ).openModule( fileName, input );
		
		postInit( handle ,null);
		setReportDesignHandle(handle);
		return handle;
	}

	/**
	 * @param handle
	 */
	private void postInit( ModuleHandle handle ,Map properties)
	{
		if(properties!= null && !properties.isEmpty( ))
		{
			String createInfo = model.getCreatedBy( );

			if ( createInfo == null || createInfo.length( ) == 0 )
			{
				try
				{
					handle.initializeModule( properties );
				}
				catch ( SemanticException e )
				{
					//ignore
				}
			}
		}
		SimpleMasterPageHandle masterPage = null;
		if ( handle.getMasterPages( ).getCount( ) == 0 )
		{
			masterPage = handle.getElementFactory( ).newSimpleMasterPage(
					"Simple MasterPage" ); //$NON-NLS-1$
			try
			{
				handle.getMasterPages( ).add( masterPage );
			}
			catch ( ContentException e )
			{
				new DesignFileException( handle.getFileName( ), e );
			}
			catch ( NameException e )
			{
				new DesignFileException( handle.getFileName( ), e );
			}
		}
	}

	/**
	 * Create report design instance
	 * 
	 * @return created report design instance
	 */
	public ModuleHandle creatReportDesign( )
	{
		return getSessionHandle( ).createDesign( );
	}

	/**
	 * @deprecated
	 * @return wrapped report design handle.
	 */
	public ModuleHandle getReportDesignHandle( )
	{
		return model;
	}

	 /**
		 * Sets report design.
		 * 
		 * @param handle
		 *            the model
		 */
	public void setReportDesignHandle( ModuleHandle handle )
	{
		model = handle;

	}

	/**
	 * @deprecated
	 * @return Command stack of current session.
	 */
	public CommandStack getCommandStack( )
	{
		if ( getReportDesignHandle( ) != null )
		{
			return getReportDesignHandle( ).getCommandStack( );
		}

		return null;
	}

	/**
	 * @deprecated
	 * Gets the first MasterPageHandle
	 * 
	 */
	public MasterPageHandle getMasterPageHandle( )
	{
		return getMasterPageHandle( getReportDesignHandle( ) );
	}

	/**
	 * @deprecated
	 * Gets the first MasterPageHandle
	 * 
	 * @param handle
	 * @return
	 */
	public MasterPageHandle getMasterPageHandle( ModuleHandle handle )
	{
		SlotHandle slotHandle = handle.getMasterPages( );
		Iterator iter = slotHandle.iterator( );
		return (MasterPageHandle) iter.next( );
	}

	/**
	 * 
	 * @param handle
	 *            the model
	 * @return get corresponding mediator
	 */
	public ReportMediator getMediator( ModuleHandle handle )
	{
		if ( handle != null )
		{
			handle.addDisposeListener( disposeLitener );
		}
		ReportMediator mediator = (ReportMediator) mediatorMap.get( handle );
		if ( mediator == null )
		{
			mediator = new ReportMediator( );
			mediatorMap.put( handle, mediator );
		}
		return mediator;
	}

	/**
	 * @return the current mediator
	 */
	public ReportMediator getMediator( )
	{
		return getMediator( getReportDesignHandle( ) );
	}

	/**
	 * @param oldObj
	 *            old model
	 * @param newObj
	 *            new model
	 */
	public void resetReportDesign( Object oldObj, Object newObj )
	{
		ReportMediator mediator = (ReportMediator) mediatorMap.get( oldObj );
		if ( mediator == null )
		{
			return;
		}
		mediatorMap.remove( oldObj );
		mediatorMap.put( newObj, mediator );
	}
	
	public void clear(ModuleHandle handle )
	{
		mediatorMap.remove( handle );
		if(handle == getReportDesignHandle( ))
		{
			setReportDesignHandle( null );
		}
	}
}