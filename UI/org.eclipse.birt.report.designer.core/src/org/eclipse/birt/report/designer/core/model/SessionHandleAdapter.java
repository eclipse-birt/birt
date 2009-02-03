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
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.designer.core.util.mediator.ReportMediator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.util.ULocale;

/**
 * Adapter class to adpat model handle. This adapter provides convenience
 * methods to GUI requirement SessionHandleAdapter responds to model
 * SessionHandle
 */
public class SessionHandleAdapter
{

	public static final int UNKNOWFILE = -1;
	public static final int DESIGNEFILE = 0;
	public static final int LIBRARYFILE = 1;
	public static final int TEMPLATEFILE = 2;

	private int type = DESIGNEFILE;

	private IDisposeListener disposeListener = new IDisposeListener( ) {

		public void moduleDisposed( ModuleHandle targetElement, DisposeEvent ev )
		{
			ReportMediator media = (ReportMediator) mediatorMap.get( targetElement );
			if ( media != null )
			{
				media.dispose( );
			}
			mediatorMap.remove( targetElement );
			targetElement.removeDisposeListener( this );
		}
	};

	private IWindowListener pageListener = new IWindowListener( ) {

		public void windowActivated( IWorkbenchWindow window )
		{
		}

		public void windowClosed( IWorkbenchWindow window )
		{
			reportHandleMap.remove( window );
		}

		public void windowDeactivated( IWorkbenchWindow window )
		{

		}

		public void windowOpened( IWorkbenchWindow window )
		{
		}
	};

	// add field support mediator
	private Map mediatorMap = new WeakHashMap( );

	// fix bug when open in new window.
	private Map reportHandleMap = new HashMap( );

	/**
	 * constructor Mark it to private to avoid new opeartion.
	 */
	private SessionHandleAdapter( )
	{

	}

	/**
	 * Get file type
	 * 
	 * @return File type
	 * 
	 * @deprecated not used any more
	 */
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
	 * Get session handle
	 * 
	 * @return Session handle
	 */

	public SessionHandle getSessionHandle( )
	{
		if ( sessionHandle == null )
		{
			sessionHandle = new DesignEngine( new DesignConfig( ) ).newSessionHandle( ULocale.getDefault( ) );
			try
			{
				if (!CorePlugin.isUseNormalTheme( ))
				{
					sessionHandle.setDefaultValue( StyleHandle.COLOR_PROP, DEUtil.getRGBInt( CorePlugin.ReportForeground.getRGB( ) ) );
				}
			}
			catch ( PropertyValueException e )
			{
				//do nothing
			}
		}
		return sessionHandle;
	}

	/**
	 * Open a design/library file.
	 * 
	 * @param fileName
	 *            The file name
	 * @param input
	 *            The input stream
	 * @throws DesignFileException
	 */
	public ModuleHandle init( String fileName, InputStream input, Map properties )
			throws DesignFileException
	{
		ModuleHandle handle = null;
		if ( properties == null )
		{
			handle = getSessionHandle( ).openModule( fileName, input );
		}
		else
		{
			handle = getSessionHandle( ).openModule( fileName,
					input,
					new ModuleOption( properties ) );
		}
		// !!!dont set handle here, handle is set only when editor is activated.
		// setReportDesignHandle( handle );
		postInit( handle, properties );
		return handle;
	}

	/**
	 * Open a design/library file.
	 * 
	 * @param fileName
	 *            The file name
	 * @param input
	 *            The input stream
	 * @throws DesignFileException
	 */
	public ModuleHandle init( String fileName, InputStream input )
			throws DesignFileException
	{
		return init( fileName, input, null );
	}

	/**
	 * @param handle
	 *            The moudle handle
	 * @param properties
	 *            The properties
	 */
	private void postInit( ModuleHandle handle, Map properties )
	{
		if ( properties != null && !properties.isEmpty( ) )
		{
			String createInfo = handle.getCreatedBy( );

			if ( createInfo == null || createInfo.length( ) == 0 )
			{
				try
				{
					handle.initializeModule( properties );
				}
				catch ( SemanticException e )
				{
					// ignore
				}
			}
		}
		SimpleMasterPageHandle masterPage = null;
		if ( handle.getMasterPages( ).getCount( ) == 0 )
		{
			masterPage = handle.getElementFactory( ).newSimpleMasterPage( null );
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
	 * @deprecated always try find reprot handle in current context first
	 * 
	 * @return wrapped report design handle.
	 */
	public ModuleHandle getReportDesignHandle( )
	{
		if ( model == null )
		{
			IWorkbenchWindow activeWindow = PlatformUI.getWorkbench( )
					.getActiveWorkbenchWindow( );
			model = (ModuleHandle) reportHandleMap.get( activeWindow );
		}
		return model;
	}

	/**
	 * Sets report design in current session.
	 * 
	 * @param handle
	 *            the model
	 */
	public void setReportDesignHandle( ModuleHandle handle )
	{
		PlatformUI.getWorkbench( ).removeWindowListener( pageListener );
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( );
		if ( handle == null )
		{
			reportHandleMap.remove( activeWindow );
		}
		else
		{
			PlatformUI.getWorkbench( ).addWindowListener( pageListener );
		}
		if ( activeWindow != null )
		{
			reportHandleMap.put( activeWindow, handle );
		}
		model = handle;
	}

	/**
	 * @return Returns command stack of current session.
	 * 
	 * @deprecated use {@link #getCommandStack(ModuleHandle)}
	 */
	public CommandStack getCommandStack( )
	{
		if ( getReportDesignHandle( ) != null )
		{
			return getReportDesignHandle( ).getCommandStack( );
		}

		return null;
	}

	public CommandStack getCommandStack( ModuleHandle handle )
	{
		if ( handle != null )
		{
			return handle.getCommandStack( );
		}
		return null;
	}

	/**
	 * Returns the first MasterPageHandle in current module
	 * 
	 * @deprecated use {@link #getFirstMasterPageHandle(ModuleHandle)}
	 * 
	 */
	public MasterPageHandle getMasterPageHandle( )
	{
		return getFirstMasterPageHandle( getReportDesignHandle( ) );
	}

	/**
	 * Returns the first master page handle in given module
	 * 
	 * @deprecated use {@link #getFirstMasterPageHandle(ModuleHandle)}
	 */
	public MasterPageHandle getMasterPageHandle( ModuleHandle handle )
	{
		return getFirstMasterPageHandle( handle );
	}

	/**
	 * @return Returns the first master page handle in given module
	 */
	public MasterPageHandle getFirstMasterPageHandle( ModuleHandle handle )
	{
		if ( handle == null )
		{
			return null;
		}

		SlotHandle slotHandle = handle.getMasterPages( );

		if ( slotHandle.getCount( ) > 0 )
		{
			return (MasterPageHandle) slotHandle.getContents( ).get( 0 );
		}
		return null;
	}

	/**
	 * Returns the mediator associated with given report handle
	 * 
	 * @param handle
	 *            the model
	 * @return corresponding mediator
	 */
	public ReportMediator getMediator( ModuleHandle handle )
	{
		if ( handle != null )
		{
			handle.addDisposeListener( disposeListener );
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
	 * @deprecated use {{@link #getMediator(ModuleHandle)}
	 * 
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

	/**
	 * Clear the specified module handle
	 * 
	 * @param handle
	 *            The module handle
	 */
	public void clear( ModuleHandle handle )
	{
		mediatorMap.remove( handle );
		if ( handle == getReportDesignHandle( ) )
		{
			setReportDesignHandle( null );
			getSessionHandle( ).setResourceFolder( null );
		}
	}
}