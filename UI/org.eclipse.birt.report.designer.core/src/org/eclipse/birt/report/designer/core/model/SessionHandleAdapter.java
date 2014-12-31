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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.mediator.IMediatorState;
import org.eclipse.birt.report.designer.core.mediator.IMediatorStateConverter;
import org.eclipse.birt.report.designer.core.mediator.MediatorManager;
import org.eclipse.birt.report.designer.core.util.mediator.ModuleMediatorTarget;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
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
public class SessionHandleAdapter implements IMediatorStateConverter
{

	private ThreadLocal threadSession = new ThreadLocal( );

	/**
	 * @deprecated not used anymore
	 */
	public static final int UNKNOWFILE = -1;
	/**
	 * @deprecated not used anymore
	 */
	public static final int DESIGNEFILE = 0;
	/**
	 * @deprecated not used anymore
	 */
	public static final int LIBRARYFILE = 1;
	/**
	 * @deprecated not used anymore
	 */
	public static final int TEMPLATEFILE = 2;

	private int type = DESIGNEFILE;

	private IWindowListener pageListener = new IWindowListener( ) {

		public void windowActivated( IWorkbenchWindow window )
		{
		}

		public void windowClosed( IWorkbenchWindow window )
		{
			moduleHandleMap.remove( window );
		}

		public void windowDeactivated( IWorkbenchWindow window )
		{

		}

		public void windowOpened( IWorkbenchWindow window )
		{
		}
	};

	// fix bug when open in new window.
	private Map<IWorkbenchWindow, ModuleHandle> moduleHandleMap = new HashMap<IWorkbenchWindow, ModuleHandle>( );

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

	public synchronized static SessionHandleAdapter getInstance( )
	{
		if ( sessionAdapter == null )
		{
			sessionAdapter = new SessionHandleAdapter( );
		}
		return sessionAdapter;
	}

	public SessionHandle getSessionHandle( boolean useThreadLocal )
	{
		if ( useThreadLocal )
			return getThreadLocalSessionHandle( );
		else
			return getSessionHandle( );
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
				if ( !CorePlugin.isUseNormalTheme( ) )
				{
					sessionHandle.setDefaultValue( StyleHandle.COLOR_PROP,
							DEUtil.getRGBInt( CorePlugin.ReportForeground.getRGB( ) ) );
				}
			}
			catch ( PropertyValueException e )
			{
				// do nothing
			}
		}
		return sessionHandle;
	}

	public SessionHandle getThreadLocalSessionHandle( )
	{
		SessionHandle s = (SessionHandle) threadSession.get( );
		if ( s == null )
		{
			s = new DesignEngine( new DesignConfig( ) ).newSessionHandle( ULocale.getDefault( ) );
			try
			{
				if ( !CorePlugin.isUseNormalTheme( ) )
				{
					s.setDefaultValue( StyleHandle.COLOR_PROP,
							DEUtil.getRGBInt( CorePlugin.ReportForeground.getRGB( ) ) );
				}
			}
			catch ( PropertyValueException e )
			{
				// do nothing
			}
			threadSession.set( s );
		}
		return s;
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

		return init( fileName, input, properties, false );
	}

	public ModuleHandle init( String fileName, InputStream input,
			Map properties, boolean useThreadLocal ) throws DesignFileException
	{
		ModuleHandle handle = null;
		if ( properties == null )
		{
			handle = getSessionHandle( useThreadLocal ).openModule( fileName,
					input );
		}
		else
		{
			handle = getSessionHandle( useThreadLocal ).openModule( fileName,
					input,
					new ModuleOption( properties ) );
		}
		// !!!dont set handle here, handle is set only when editor is activated.
		// setReportDesignHandle( handle );
		postInit( handle, properties );

		// flush any init state change which cannot be undone.
		handle.getCommandStack( ).flush( );

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

		// TODO these legacy logic should be move into individual module
		// counterpart.
		SimpleMasterPageHandle masterPage = null;
		if ( handle.getMasterPages( ) != null
				&& handle.getMasterPages( ).getCount( ) == 0 )
		{
			masterPage = handle.getElementFactory( ).newSimpleMasterPage( null );
			try
			{
				handle.getMasterPages( ).add( masterPage );
			}
			catch ( ContentException e )
			{
				// ignore
			}
			catch ( NameException e )
			{
				// ignore
			}
		}
	}

	/**
	 * Create an empty report design instance
	 * 
	 * @return An empty report design instance
	 * 
	 * @deprecated should handled by individual module counterpart.
	 */
	public ModuleHandle creatReportDesign( )
	{
		return getSessionHandle( ).createDesign( );
	}

	/**
	 * @deprecated Always try find reprot handle in current context first. If
	 *             have to use, use {@link #getModule()} instead.
	 * 
	 * @return wrapped report design handle.
	 */
	public ModuleHandle getReportDesignHandle( )
	{
		return getModule( );
	}

	/**
	 * @return Returns the active moudle in current session
	 * 
	 * @deprecated It's better to find module from relevant context instead
	 *             here.
	 */
	public ModuleHandle getModule( )
	{
		if ( model == null )
		{
			IWorkbenchWindow activeWindow = PlatformUI.getWorkbench( )
					.getActiveWorkbenchWindow( );
			model = moduleHandleMap.get( activeWindow );
		}
		return model;
	}

	/**
	 * Sets report design in current session.
	 * 
	 * @param handle
	 *            the model
	 * @deprecated use {@link #setModule(ModuleHandle)} instead.
	 */
	public void setReportDesignHandle( ModuleHandle handle )
	{
		setModule( handle );
	}

	/**
	 * Sets the active module in current session.
	 * 
	 * @param handle
	 */
	public void setModule( ModuleHandle handle )
	{
		PlatformUI.getWorkbench( ).removeWindowListener( pageListener );
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( );
		if ( handle == null )
		{
			moduleHandleMap.remove( activeWindow );
		}
		else
		{
			PlatformUI.getWorkbench( ).addWindowListener( pageListener );
		}
		if ( activeWindow != null )
		{
			moduleHandleMap.put( activeWindow, handle );
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
	 * 
	 * @deprecated should be handled by individual module counterpart.
	 */
	public MasterPageHandle getFirstMasterPageHandle( ModuleHandle handle )
	{
		if ( handle == null )
		{
			return null;
		}

		SlotHandle slotHandle = handle.getMasterPages( );

		if ( slotHandle != null && slotHandle.getCount( ) > 0 )
		{
			return (MasterPageHandle) slotHandle.getContents( ).get( 0 );
		}
		return null;
	}

	public IMediator getMediator( ModuleHandle handle )
	{
		return getMediator( handle, true );
	}

	/**
	 * Returns the mediator associated with given report handle
	 * 
	 * @param handle
	 *            the model
	 * @return corresponding mediator
	 */
	public IMediator getMediator( ModuleHandle handle, boolean force )
	{
		IMediator mt = MediatorManager.getInstance( )
				.getMediator( new ModuleMediatorTarget( handle ), force );

		if ( mt != null )
		{
			mt.setStateConverter( this );
		}

		return mt;
	}

	/**
	 * @deprecated use {{@link #getMediator(ModuleHandle)}
	 * 
	 * @return the current mediator
	 */
	public IMediator getMediator( )
	{
		return getMediator( getReportDesignHandle( ) );
	}

	/**
	 * @param oldObj
	 *            old model
	 * @param newObj
	 *            new model
	 * 
	 * @deprecated use {@link #resetModule(ModuleHandle, ModuleHandle)} instead
	 */
	public void resetReportDesign( ModuleHandle oldObj, ModuleHandle newObj )
	{
		resetModule( oldObj, newObj );
	}

	/**
	 * Resets the module in the mediator registry.
	 * 
	 * @param oldObj
	 * @param newObj
	 */
	public void resetModule( ModuleHandle oldObj, ModuleHandle newObj )
	{
		MediatorManager.getInstance( )
				.resetTarget( new ModuleMediatorTarget( oldObj ),
						new ModuleMediatorTarget( newObj ) );
	}

	/**
	 * Clear the specified module handle
	 * 
	 * @param handle
	 *            The module handle
	 */
	public void clear( ModuleHandle handle )
	{
		MediatorManager.getInstance( )
				.removeMediator( new ModuleMediatorTarget( handle ) );

		if ( handle == getReportDesignHandle( ) )
		{
			setReportDesignHandle( null );
			getSessionHandle( ).setResourceFolder( null );
		}
	}

	public IMediatorRequest convertStateToRequest( IMediatorState state )
	{
		ReportRequest request = new ReportRequest( state.getSource( ),
				state.getType( ) );
		if ( state.getData( ) instanceof List )
		{
			request.setSelectionObject( (List) state.getData( ) );
		}
		else if ( state.getData( ) != null )
		{
			List<Object> lst = new ArrayList<Object>( );
			lst.add( state.getData( ) );
			request.setSelectionObject( lst );
		}
		return request;
	}
}