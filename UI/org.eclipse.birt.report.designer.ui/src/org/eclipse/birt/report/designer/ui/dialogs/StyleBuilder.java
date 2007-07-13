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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BackgroundPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BlockPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BorderPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BoxPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FontPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatDateTimePreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatNumberPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatStringPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.GeneralPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.HighlightsPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.MapPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.PageBreakPreferencePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.widgets.Shell;

/**
 * Presents style builder dialog.
 */

public class StyleBuilder extends PreferenceDialog
{

	public static final String DLG_TITLE_NEW = Messages.getString( "SytleBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "SytleBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	protected String title;

	/**
	 * Constructor
	 * 
	 * @param parentShell
	 * @param handle
	 */
	public StyleBuilder( Shell parentShell, ReportElementHandle handle,
			String title )
	{
		this( parentShell, handle, null, title );
	}

	/**
	 * Constructor
	 * 
	 * @param parentShell
	 * @param handle
	 */
	public StyleBuilder( Shell parentShell, ReportElementHandle handle,
			ThemeHandle theme, String title )
	{
		super( parentShell, createPreferenceManager( handle, theme ) );
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open( )
	{
		setSelectedNode( "General" ); //$NON-NLS-1$

		return super.open( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell( Shell newShell )
	{
		super.configureShell( newShell );
		newShell.setText( title );
	}

	private static PreferenceManager createPreferenceManager(
			ReportElementHandle handle, ThemeHandle theme )
	{
		PreferenceManager preferenceManager = new PreferenceManager( '/' );

		// Get the pages from the registry
		List pageContributions = new ArrayList( );

		// adds preference pages into page contributions.
		pageContributions.add( new PreferenceNode( "General", //$NON-NLS-1$
				new GeneralPreferencePage( handle, theme ) ) );
		pageContributions.add( new PreferenceNode( "Font", //$NON-NLS-1$
				new FontPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Background", //$NON-NLS-1$
				new BackgroundPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Block", //$NON-NLS-1$
				new BlockPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Box", //$NON-NLS-1$
				new BoxPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Border", //$NON-NLS-1$
				new BorderPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Number Format", //$NON-NLS-1$
				new FormatNumberPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "DateTime Format", //$NON-NLS-1$
				new FormatDateTimePreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "String Format", //$NON-NLS-1$
				new FormatStringPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "PageBreak", //$NON-NLS-1$
				new PageBreakPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Map", //$NON-NLS-1$
				new MapPreferencePage( handle ) ) );
		pageContributions.add( new PreferenceNode( "Highlights", //$NON-NLS-1$
				new HighlightsPreferencePage( handle ) ) );

		// Add the contributions to the manager
		Iterator it = pageContributions.iterator( );
		while ( it.hasNext( ) )
		{
			preferenceManager.addToRoot( (IPreferenceNode) it.next( ) );
		}
		return preferenceManager;
	}

	private void saveAll( final boolean closeDialog )
	{
		Platform.run( new SafeRunnable( ) {

			private boolean errorOccurred;

			private boolean invalid;

			public void run( )
			{
				errorOccurred = false;
				invalid = false;
				try
				{
					// Notify all the pages and give them a chance to abort
					Iterator nodes = getPreferenceManager( ).getElements( PreferenceManager.PRE_ORDER )
							.iterator( );
					while ( nodes.hasNext( ) )
					{
						IPreferenceNode node = (IPreferenceNode) nodes.next( );
						IPreferencePage page = node.getPage( );
						if ( page != null )
						{
							if ( !page.performOk( ) )
							{
								invalid = true;
								return;
							}
						}
					}
				}
				catch ( Exception e )
				{
					handleException( e );
				}
				finally
				{
					// Give subclasses the choice to save the state of the
					// preference pages.
					if ( !errorOccurred )
					{
						handleSave( );
					}

					// Need to restore state
					if ( !invalid && closeDialog )
					{
						close( );
					}

				}
			}

			public void handleException( Throwable e )
			{
				errorOccurred = true;
				if ( Platform.isRunning( ) )
				{
					String bundle = Platform.PI_RUNTIME;
					Platform.getLog( Platform.getBundle( bundle ) )
							.log( new Status( IStatus.ERROR,
									bundle,
									0,
									e.toString( ),
									e ) );
				}
				else
				{
					e.printStackTrace( );
				}

				setSelectedNodePreference( null );
				String message = JFaceResources.getString( "SafeRunnable.errorMessage" ); //$NON-NLS-1$
				MessageDialog.openError( getShell( ),
						JFaceResources.getString( "Error" ), message ); //$NON-NLS-1$

			}
		} );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferenceDialog#okPressed()
	 */
	protected void okPressed( )
	{
		saveAll( true );
	}

}