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

package org.eclipse.birt.report.engine.executor;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IExtendedItemContent;
import org.eclipse.birt.report.engine.content.IImageItemContent;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.extension.ExtensionManager;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Processes an extented item.
 */
public class ExtendedItemExecutor extends StyledItemExecutor
{

	/**
	 * @param context
	 *            the engine execution context
	 * @param visitor
	 *            visitor class used to visit the extended item
	 */
	public ExtendedItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		IExtendedItemContent content = ContentFactory.createExtendedItemContent( );

		// handle common properties, such as
		//
		//1) style (Not supported now as we only support image extension)
		//2) highlight (Not supported now as we only support image extension)
		//3) x,y
		//4) actions
		//
		// other report item-supported features.

		//create user-defined generation-time helper object
		ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle( );
		String tagName = handle.getExtensionName( );

		IReportItemGeneration itemGeneration = ExtensionManager.getInstance( )
				.createGenerationItem( tagName );
		if ( itemGeneration == null )
		{
			// skip this element if we can not create generation-time object
			// Add Log
			return;
		}

		// handle the parameters passed to extension writers
		HashMap parameters = new HashMap( );
		parameters.put( IReportItemGeneration.MODEL_OBJ, handle );
		// TODO Add other parameters, i.e., bounds, dpi and scaling factor
		itemGeneration.initialize( parameters );

		itemGeneration.pushPreparedQuery( item.getQuery( ), null );

		// Get the dirty work done
		itemGeneration.process( context.getDataEngine( ) );

		// No serialization support now
		itemGeneration.serialize( null );

		// call getSize
		// Size size = getSize();

		// clean up
		itemGeneration.finish( );

		//call the presentation peer to create the content object
		IReportItemPresentation itemPresentation = ExtensionManager
				.getInstance( ).createPresentationItem( tagName );
		if ( itemPresentation == null )
		{
			// skip this element if we can not create generation-time object
			// Add Log
			return;
		}

		HashMap parameters2 = new HashMap( );
		parameters2.put( IReportItemPresentation.MODEL_OBJ, handle );
		parameters2.put( IReportItemPresentation.SUPPORTED_FILE_FORMATS,
				"GIF;PNG;JPG;BMP" ); // $NON-NLS-1$
		// TODO Add other parameters, i.e., bounds, dpi and scaling factor
		itemPresentation.initialize( parameters2 );

		// No de-serialization support for now
		itemPresentation.restore( null );

		// Do the dirty work
		Object output = itemPresentation.process( );

		String format = emitter.getOutputFormat( );
		String mimeType = ""; // $NON-NLS-1$
		int type = itemPresentation.getOutputType( format, mimeType );
		switch ( type )
		{
			case IReportItemPresentation.OUTPUT_NONE :
				break;
			case IReportItemPresentation.OUTPUT_AS_IMAGE :
				// the output object is a image, so create a image content
				// object
				IImageItemContent image = ContentFactory.createImageContent( null );
				image.setData( (byte[]) output );
				image.setImageSource( ImageItemDesign.IMAGE_EXPRESSION );
				IReportItemEmitter imageEmitter = emitter.getEmitter( "image" ); // $NON-NLS-1$
				if ( imageEmitter != null )
				{
					imageEmitter.start( image );
					imageEmitter.end( );
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_CUSTOM :
				IExtendedItemContent extContent = ContentFactory
						.createExtendedItemContent( );
				content.setItemName( tagName );
				content.setContent( content );
				//get the emmiter type, and give it to others type
				IReportItemEmitter itemEmitter = emitter
						.getEmitter( "extendedItem" ); // $NON-NLS-1$
				if ( itemEmitter != null )
				{
					itemEmitter.start( content );
					itemEmitter.end( );
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_DRAWING :
			case IReportItemPresentation.OUTPUT_AS_HTML_TEXT :
			case IReportItemPresentation.OUTPUT_AS_TEXT :
				assert false;
				break;
		}
		itemPresentation.finish( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		// TODO Auto-generated method stub

	}

}