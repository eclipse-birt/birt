/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.report.designer.internal.ui.util.UIHelper;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.preview.Activator;
import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.impl.EmitterConfigurationManager;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.viewer.utilities.IWebAppInfo;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.osgi.framework.Bundle;

/**
 * PreviewSupport
 */
abstract class PreviewSupport
{

	protected static final String TYPE_HTML = "html"; //$NON-NLS-1$

	private static final Map<String, String> typeMap = new HashMap<String, String>( );

	private static final String IMG_FILE_DEFAULT = "icons/etool16/preview_web.gif"; //$NON-NLS-1$
	private static final String IMG_FILE_WEB = "icons/etool16/preview_web.gif"; //$NON-NLS-1$

	static
	{
		typeMap.put( "doc", "icons/etool16/preview_doc.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "html", "icons/etool16/preview_html.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "pdf", "icons/etool16/preview_pdf.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "ppt", "icons/etool16/preview_ppt.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "postscript", "icons/etool16/preview_ps.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "xls", "icons/etool16/preview_xls.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "docx", "icons/etool16/preview_docx.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "pptx", "icons/etool16/preview_pptx.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
		typeMap.put( "xhtml", "icons/etool16/preview_xhtml.gif" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected Menu getPreviewMenu( Object parent, boolean fullLabel )
	{
		ReportEngine engine = new ReportEngine( new EngineConfig( ) );

		EmitterInfo[] emitters = engine.getEmitterInfo( );

		if ( emitters == null )
		{
			return null;
		}

		TreeMap<String, List<EmitterInfo>> supportedFormats = new TreeMap<String, List<EmitterInfo>>( );
		TreeMap<String, List<EmitterInfo>> deprecatedFormats = new TreeMap<String, List<EmitterInfo>>( );

		for ( EmitterInfo ei : emitters )
		{
			if ( !ei.isHidden( ) )
			{
				List<EmitterInfo> list = null;
				
				if ( !ei.isFormatDeprecated( ) )
				{
					list = supportedFormats.get( ei.getFormat( ) );
	
					if ( list == null )
					{
						list = new ArrayList<EmitterInfo>( );
						supportedFormats.put( ei.getFormat( ), list );
					}
				}
				else 
				{
					list = deprecatedFormats.get( ei.getFormat( ) );
					
					if ( list == null )
					{
						list = new ArrayList<EmitterInfo>( );
						deprecatedFormats.put( ei.getFormat( ), list );
					}
				}

				list.add( ei );
			}
		}

		Menu menu;

		if ( parent instanceof Control )
		{
			menu = new Menu( (Control) parent );
		}
		else if ( parent instanceof Menu )
		{
			menu = new Menu( (Menu) parent );
		}
		else
		{
			return null;
		}

		MenuItem previewWebViewer = new MenuItem( menu, SWT.PUSH );
		//remove "&1"
		previewWebViewer.setText( "" //$NON-NLS-1$
				+ Messages.getString( fullLabel ? "designer.preview.previewaction.label.webviewer" //$NON-NLS-1$
						: "designer.preview.run.webviewer" ) ); //$NON-NLS-1$
		previewWebViewer.setImage( UIHelper.getImage( Activator.getDefault( )
				.getBundle( ), IMG_FILE_WEB ) );
		previewWebViewer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				preview( TYPE_HTML, true );
			}
		} );

		// TODO should share same manager for one UI enviroment
		EmitterConfigurationManager configManager = new EmitterConfigurationManager( );

		int i = 0;

		for ( Entry<String, List<EmitterInfo>> ent : supportedFormats.entrySet( ) )
		{
			final String format = ent.getKey( );
			final List<EmitterInfo> emits = ent.getValue( );

			MenuItem previewOption = new MenuItem( menu,
					emits.size( ) > 1 ? SWT.CASCADE : SWT.PUSH );

			//remove indexPrefix
			previewOption.setText(  Messages.getFormattedString( fullLabel ? "designer.preview.previewaction.label" //$NON-NLS-1$
							: "designer.preview.run", //$NON-NLS-1$
							new Object[]{
								format.toUpperCase( )
							} ) );

			previewOption.setImage( getFormatIcon( format, emits ) );

			processEmits( previewOption, emits, configManager );
		}
		
		for ( Entry<String, List<EmitterInfo>> ent : deprecatedFormats.entrySet( ) )
		{
			final String format = ent.getKey( );
			final List<EmitterInfo> emits = ent.getValue( );

			MenuItem previewOption = new MenuItem( menu,
					emits.size( ) > 1 ? SWT.CASCADE : SWT.PUSH );

			previewOption.setText(  Messages.getFormattedString( fullLabel ? "designer.preview.previewaction.label" //$NON-NLS-1$
							: "designer.preview.run", //$NON-NLS-1$
							new Object[]{
								format.toUpperCase( ) + " " + Messages.getString( "designer.preview.deprecated.label" )
							} ) );

			previewOption.setImage( getFormatIcon( format, emits ) );

			processEmits( previewOption, emits, configManager );
		}

		return menu;
	}
	
	private void processEmits( MenuItem previewOption, List<EmitterInfo> emits, EmitterConfigurationManager configManager )
	{
		if ( emits.size( ) > 1 )
		{
			Menu subMenu = new Menu( previewOption );
			previewOption.setMenu( subMenu );

			int j = 1;
			for ( final EmitterInfo ei : emits )
			{
				MenuItem sub1 = new MenuItem( subMenu, SWT.PUSH );

				final IEmitterDescriptor emitterDescriptor = configManager.getEmitterDescriptor( ei.getID( ) );

				String label = null;

				if ( emitterDescriptor != null
						&& emitterDescriptor.getDisplayName( ) != null )
				{
					label = emitterDescriptor.getDisplayName( );
				}

				if ( label == null )
				{
					label = getDefaultLabel( ei );
				}

				sub1.setText( "&" + ( j++ ) + " " + label ); //$NON-NLS-1$ //$NON-NLS-2$

				sub1.addSelectionListener( new SelectionAdapter( ) {

					public void widgetSelected( SelectionEvent e )
					{
						preview( ei, emitterDescriptor );
					}
				} );
			}
		}
		else
		{
			final EmitterInfo ei = emits.get( 0 );
			final IEmitterDescriptor emitterDescriptor = configManager.getEmitterDescriptor( ei.getID( ) );

			previewOption.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					preview( ei, emitterDescriptor );
				}
			} );
		}
	}

	private String getDefaultLabel( EmitterInfo ei )
	{
		String format = ei.getFormat( ).toUpperCase( );
		String formatDetail = ei.getID( );

		return formatDetail == null ? format
				: ( format + " (" + formatDetail + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private Image getFormatIcon( String format, List<EmitterInfo> emits )
	{
		Image icon = null;

		for ( EmitterInfo ei : emits )
		{
			String path = ei.getIcon( );

			IConfigurationElement confElem = ei.getEmitter( );

			if ( confElem != null && path != null )
			{
				String pluginId = confElem.getDeclaringExtension( )
						.getNamespace( );

				Bundle bundle = Platform.getBundle( pluginId );

				icon = UIHelper.getImage( bundle, path, false );
			}
		}

		if ( icon == null )
		{
			// keep old compatibility
			if ( typeMap.containsKey( format ) )
			{
				icon = UIHelper.getImage( Activator.getDefault( ).getBundle( ),
						typeMap.get( format ) );
			}
			else
			{
				icon = UIHelper.getImage( Activator.getDefault( ).getBundle( ),
						IMG_FILE_DEFAULT );
			}
		}

		return icon;
	}

	protected boolean prePreview( )
	{
		PreviewUtil.clearSystemProperties();

		return true;
	}

	protected void preview( String format, boolean allowPage )
	{
		if ( !prePreview( ) )
		{
			return;
		}

		FormEditor editor = UIUtil.getActiveReportEditor( false );
		ModuleHandle model = null;

		if ( editor instanceof MultiPageReportEditor )
		{
			model = ( (MultiPageReportEditor) editor ).getModel( );
		}

		if ( !UIUtil.canPreviewWithErrors( model ) )
			return;

		if ( editor != null )
		{
			IFormPage activePageInstance=editor.getActivePageInstance();
			if ( model.needsSave( ) ||(activePageInstance!=null && activePageInstance.isDirty()))//Do save when current active page is dirty.
			{
				editor.doSave( null );
			}
		}
		Map<String, Object> options = new HashMap<String, Object>( );
		options.put( WebViewer.FORMAT_KEY, format );
		options.put( WebViewer.ALLOW_PAGE_KEY, Boolean.valueOf( allowPage ) );
		options.put( WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault( )
				.getResourceFolder( ) );
		if (hasParameters(model)) {
			options.put(WebViewer.SHOW_PARAMETER_PAGE_KEY, "true");
		}
		WebViewer.display( model.getFileName( ), options );
	}
	private boolean hasParameters(ModuleHandle model )
	{
		IWebAppInfo webapp = WebViewer.getCurrentWebApp( );

		if ( webapp != null && webapp.useCustomParamHandling( ) )
		{
			return false;
		}

		List parameters = model.getFlattenParameters( );

		if ( parameters != null )
		{
			for ( Object p : parameters )
			{
				if ( p instanceof ParameterHandle
						&& !( (ParameterHandle) p ).isHidden( ) )
				{
					return true;
				}
			}
		}

		return false;
	}


	protected void preview( EmitterInfo ei, IEmitterDescriptor descriptor )
	{
		if ( !prePreview( ) )
		{
			return;
		}

		FormEditor editor = UIUtil.getActiveReportEditor( false );
		ModuleHandle model = null;

		if ( editor instanceof MultiPageReportEditor )
		{
			model = ( (MultiPageReportEditor) editor ).getModel( );
		}

		if ( !UIUtil.canPreviewWithErrors( model ) )
			return;

		if ( editor != null )
		{
			if ( model.needsSave( ) )
			{
				editor.doSave( null );
			}
		}

		Map<String, Object> options = new HashMap<String, Object>( );
		options.put( WebViewer.EMITTER_ID_KEY, ei.getID( ) );
		options.put( WebViewer.FORMAT_KEY, ei.getFormat( ) );
		options.put( WebViewer.ALLOW_PAGE_KEY, Boolean.valueOf( false ) );
		options.put( WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault( )
				.getResourceFolder( ) );

		WebViewer.display( model.getFileName( ), options );
	}
}
