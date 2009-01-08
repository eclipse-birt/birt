/*******************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.ppt.device.PPTPage;
import org.eclipse.birt.report.engine.emitter.ppt.device.PPTPageDevice;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The PPT render class.
 */
public class PPTRender extends PageDeviceRender
{

	private OutputStream pptOutput = null;

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.ppt"; //$NON-NLS-1$

	public PPTRender( IEmitterServices services ) throws EngineException
	{
		initialize( services );
	}

	public IPageDevice createPageDevice( String title, String author, String subject,
			String description, IReportContext context, IReportContent report )
			throws Exception
	{
		try
		{
			return new PPTPageDevice( pptOutput, title, author, description );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
		return null;
	}

	/**
	 * Returns the output format, always is "ppt".
	 * 
	 * @return the output format
	 */
	public String getOutputFormat( )
	{
		return "ppt";
	}

	/**
	 * Initializes the PPTEmitter.
	 * 
	 * @param services
	 *            the emitter services object.
	 * @throws EngineException 
	 */
	public void initialize( IEmitterServices services ) throws EngineException
	{
		this.services = services;
		IReportRunnable reportRunnable = services.getReportRunnable( );

		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable.getDesignHandle( );
		}
		this.context = services.getReportContext( );
		this.pptOutput = EmitterUtil.getOuputStream( services, REPORT_FILE );
	}

	public void visitImage( IImageArea imageArea )
	{
		PPTPage pptPage = (PPTPage)pageGraphic;
		pptPage.setLink( getHyperlink( imageArea ) );
		super.visitImage( imageArea );
		pptPage.setLink( null );
	}

	@Override
	public void visitText( ITextArea textArea )
	{
		PPTPage pptPage = (PPTPage)pageGraphic;
		pptPage.setLink( getHyperlink( textArea ) );
		super.visitText( textArea );
		pptPage.setLink( null );
	}
	
	private String getHyperlink( IArea area )
	{
		IContent content = area.getContent( );
		if ( null != content )
		{
			IHyperlinkAction hyperlinkAction = content.getHyperlinkAction( );
			if ( hyperlinkAction != null )
			{
				try
				{
					if ( hyperlinkAction.getType( ) != IHyperlinkAction.ACTION_BOOKMARK )
					{
						String link = hyperlinkAction.getHyperlink( );
						Object handler = services
								.getOption( RenderOption.ACTION_HANDLER );
						if ( handler != null
								&& handler instanceof IHTMLActionHandler )
						{
							IHTMLActionHandler actionHandler = (IHTMLActionHandler) handler;
							String systemId = reportRunnable == null
									? null
									: reportRunnable.getReportName( );
							Action action = new Action( systemId,
									hyperlinkAction );
							link = actionHandler.getURL( action, context );
						}

						return link;
					}
				}
				catch ( Exception e )
				{
					logger.log( Level.WARNING, e.getMessage( ), e );
				}
			}
		}
		return null;
	}

	protected void drawTextAt( ITextArea text, int x, int y, int width,
			int height, TextStyle textStyle )
	{
		pageGraphic.drawText( text.getLogicalOrderText( ), x, y, width, height, textStyle );
	}
}
