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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.eclipse.birt.report.engine.content.ExtendedItemContent;
import org.eclipse.birt.report.engine.content.ImageItemContent;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.extension.ExtensionManager;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;


/**
 */
public class ExtendedItemExecutor extends StyledItemExecutor
{

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
		ExtendedItemContent content = new ExtendedItemContent();
		//actually, we can handle other commons properties, such as:
		//1) style
		//2) highlight
		//3) x,y
		//4) actions
		// other report item supported features.
		

		//then call user defined generator to create state object.
		//search the execution
		ExtendedItemHandle handle = (ExtendedItemHandle)item.getHandle();
		String tagName = handle.getExtensionName();
		
		IReportItemGeneration gPeer = ExtensionManager.getInstance().createGenerationItem(tagName);
		if (gPeer == null)
		{
			//we can't find the proper generator, so simple skip this element
			return;
		}
		//don't know how to get the parameters
		HashMap parameters = new HashMap();
		parameters.put(IReportItemGeneration.MODEL_OBJ, handle);
		gPeer.initialize(parameters);
		gPeer.process(context.getDataEngine());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gPeer.serialize(out);
		gPeer.finish();

		//call the presentation peer to create the content object
		IReportItemPresentation pPeer = ExtensionManager.getInstance().createPresentationItem(tagName);
		if (pPeer == null)
		{
			//can't find the proper presentation, so skip this element
			return;
		}
		parameters.clear();
		parameters.put(IReportItemPresentation.MODEL_OBJ, handle);
		pPeer.initialize(parameters);
		pPeer.restore(new ByteArrayInputStream(out.toByteArray()));
		Object contentObject = pPeer.process();
		
		//FIXME How can we get the emitter type? 
		String format = "";
		String mimiType = "";
		int type = pPeer.getOutputType(null, null);
		switch(type)
		{
			case IReportItemPresentation.OUTPUT_AS_IMAGE:
				//the output object is a image, so create a image
				ImageItemContent image = new ImageItemContent(null);
				image.setData((byte[])contentObject);
				image.setImageSource(ImageItemDesign.IMAGE_EXPRESSION);
				IReportItemEmitter imageEmitter = emitter.getEmitter("image");
				if (imageEmitter != null)
				{
					imageEmitter.start(image);
					imageEmitter.end();
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_CUSTOM:
				ExtendedItemContent extContent = new ExtendedItemContent();
				content.setItemName(tagName);
				content.setContent(content);
				//get the emmiter type, and give it to others type
				IReportItemEmitter itemEmitter = emitter.getEmitter( "extendedItem" );
				if (itemEmitter != null)
				{
					itemEmitter.start( content );
					itemEmitter.end( );
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_DRAWING:
			case IReportItemPresentation.OUTPUT_AS_HTML_TEXT:
			case IReportItemPresentation.OUTPUT_AS_TEXT:
			case IReportItemPresentation.OUTPUT_NONE:
				assert false;
				break;
		}
		pPeer.finish();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		// TODO Auto-generated method stub

	}
	
}
