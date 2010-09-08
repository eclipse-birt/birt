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
package org.eclipse.birt.report.engine.emitter.odp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.layout.emitter.PageEmitter;
import org.eclipse.birt.report.engine.odf.OdfConstants;
import org.eclipse.birt.report.engine.odf.pkg.Package;
import org.eclipse.birt.report.engine.odf.writer.ContentWriter;
import org.eclipse.birt.report.engine.odf.writer.StylesWriter;

public class OdpEmitter extends PageEmitter implements OdfConstants
{
	public static final String MIME_TYPE = "application/vnd.oasis.opendocument.presentation"; //$NON-NLS-1$

	private ByteArrayOutputStream bodyOut;
	private ByteArrayOutputStream masterPageOut;
	private OutputStream out;
	private Package pkg;
	private OdpContext context;

	public void initialize( IEmitterServices service ) throws EngineException
	{
		context = new OdpContext();
		bodyOut = new ByteArrayOutputStream();
		masterPageOut = new ByteArrayOutputStream();
		
		String tempFileDir = service.getReportEngine( ).getConfig( ).getTempDir( );				
		context.setTempFileDir( service.getReportEngine( ).getConfig( )
				.getTempDir( ) );
		
		this.out = EmitterUtil.getOuputStream( service, "report.odp" ); //$NON-NLS-1$
		pkg = Package.createInstance( out, tempFileDir, MIME_TYPE );
		context.setPackage( pkg );
		super.initialize( service );
	}

	public PageDeviceRender createRender( IEmitterServices service ) throws EngineException
	{
		return new OdpRender( service, context, bodyOut, masterPageOut );
	}

	public void end( IReportContent report )
	{
		super.end( report );
		save();
	}
	
	private void save( )
	{		
		// TODO: somehow refactor with ODF's save method
		try
		{
			// output stream for real content
			ContentWriter docContentWriter = new ContentWriter( pkg.addEntry(
					FILE_CONTENT, CONTENT_TYPE_XML ).getOutputStream( ), context.getReportDpi( ) );
			docContentWriter.write( context.getStyleManager( ).getStyles( ),
					new ByteArrayInputStream( bodyOut.toByteArray( ) ) );

			StylesWriter stylesWriter = new StylesWriter( pkg.addEntry(
					FILE_STYLES, CONTENT_TYPE_XML ).getOutputStream( ), context.getReportDpi( ) );
			
			// write the styles.xml file
			// including the global styles
			stylesWriter.start( );
			stylesWriter.writeStyles( context.getGlobalStyleManager( ).getStyles( ) );
			stylesWriter.writeMasterPage( new ByteArrayInputStream( masterPageOut.toByteArray( ) ) );
			stylesWriter.end( );
			
			pkg.close( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
	}

	
}
