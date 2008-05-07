/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

import java.net.URLEncoder;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 */

public class ReportDocumentEditor  extends EditorPart
{
	private IReportEngine engine;
	private EngineConfig engineConfig;
	private Color fBackgroundColor;
	private Color fForegroundColor;
	private Color fSeparatorColor;
	private String fileName = ""; //$NON-NLS-1$
	private Composite fComposite;
	@Override
	public void doSave( IProgressMonitor monitor )
	{
		//do nothing
		
	}

	@Override
	public void doSaveAs( )
	{
		//do nothing
		
	}

	@Override
	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		setSite( site );
		setInput( input );
	}

	@Override
	public boolean isDirty( )
	{
		return false;
	}

	@Override
	public boolean isSaveAsAllowed( )
	{
		return false;
	}

	@Override
	public void createPartControl( Composite parent )
	{
		if (engine == null)
		{
			init();
		}
		Display display= parent.getDisplay();
		fBackgroundColor= display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		fForegroundColor= display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		fSeparatorColor= ColorManager.getColor( 152, 170, 203);
		
		fComposite= createComposite(parent);
		fComposite.setLayout(new GridLayout());
		
		
		createTitleLabel(fComposite, Messages.getString("ReportDocumentEditor.1")); //$NON-NLS-1$
		createLabel(fComposite, null);
		createLabel(fComposite, null);
		
		createHeadingLabel(fComposite, Messages.getString("ReportDocumentEditor.2")); //$NON-NLS-1$
		
		Composite separator= createCompositeSeparator(fComposite);
		GridData data= new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint= 2;
		separator.setLayoutData(data);
		
		createInfomation( fComposite );
	}
	
	private void createInfomation(Composite parent)
	{
		
		Font font = parent.getFont( );
		Composite continer = createComposite( parent, font,
				2,
				2,
				GridData.FILL_BOTH,
				0,
				0 );
		continer.setBackground(fBackgroundColor);
		try
		{
			IReportDocument document = engine.openReportDocument( getFileName( ) );
			Label nameScript =  createScriptgLabel( continer, Messages.getString("ReportDocumentEditor.3") ); //$NON-NLS-1$
			Label name =  createScriptgLabel( continer, document.getName( ) );
			
			Label versionScript =  createScriptgLabel( continer, Messages.getString("ReportDocumentEditor.4") ); //$NON-NLS-1$
			Label version =  createScriptgLabel( continer, document.getVersion( ) );
			
			Label pageCountScript =  createScriptgLabel( continer, Messages.getString("ReportDocumentEditor.5") ); //$NON-NLS-1$
			Label pageCount =  createScriptgLabel( continer, "" + document.getPageCount( ) ); //$NON-NLS-1$
			
		}
		catch ( EngineException e )
		{
			e.printStackTrace( );
		}
		
	}
	
	private Composite createCompositeSeparator(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(fSeparatorColor);
		return composite;
	}
	
	private Label createTitleLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null)
			label.setText(text);
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		label.setFont(JFaceResources.getHeaderFont());
		
		return label;
	}
	
	private Label createLabel(Composite parent, String text) {
		Label label= new Label(parent, SWT.WRAP);
		if (text != null)
			label.setText(text);
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		GridData gd= new GridData(SWT.FILL, SWT.FILL, true, false);
		label.setLayoutData(gd);
		return label;
	}
	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(fBackgroundColor);
		
		return composite;
	}
	
	private Label createHeadingLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null)
			label.setText(text);
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		label.setFont(JFaceResources.getBannerFont());
		
		return label;
	}
	
	private Label createScriptgLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null)
			label.setText(text);
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		//label.setFont(JFaceResources.getBannerFont());
		
		return label;
	}
	
	protected void display()
	{
		
	}
	@Override
	public void setFocus( )
	{
		//do nothing
		
	}

	
	public String getFileName( )
	{
		return fileName;
	}

	
	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}
	
	private  Composite createComposite( Composite parent, Font font,
			int columns, int hspan, int fill, int marginwidth, int marginheight )
	{
		Composite g = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( columns, false );
		layout.marginWidth = marginwidth;
		layout.marginHeight = marginheight;
		g.setLayout( layout );
		g.setFont( font );
		GridData gd = new GridData( fill );
		gd.horizontalSpan = hspan;
		g.setLayoutData( gd );
		return g;
	}
	
	private void init( )
	{
		engineConfig = new LauncherEngineConfig( );
		
		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );

		configEngine( );
		this.engine = factory.createReportEngine( engineConfig );
		engine.changeLogLevel( Level.WARNING );
	}
	private void configEngine( )
	{
		HTMLRenderOption emitterConfig = new HTMLRenderOption( );

		emitterConfig.setActionHandler( new HTMLActionHandler( ) {

			public String getURL( IAction actionDefn, Object context )
			{
				if ( actionDefn.getType( ) == IAction.ACTION_DRILLTHROUGH )
					return "birt://" //$NON-NLS-1$
							+ URLEncoder.encode( super.getURL( actionDefn,
									context ) );
				return super.getURL( actionDefn, context );
			}

		} );

		engineConfig.getEmitterConfigs( ).put( RenderOption.OUTPUT_FORMAT_HTML,
				emitterConfig );
	}
	
	static class LauncherEngineConfig extends EngineConfig
	{
		/**
		 * constructor
		 */
		public LauncherEngineConfig( )
		{
			super( );

			HTMLRenderOption emitterConfig = (HTMLRenderOption) getEmitterConfigs( ).get( RenderOption.OUTPUT_FORMAT_HTML );

			emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
		}

	}
}
