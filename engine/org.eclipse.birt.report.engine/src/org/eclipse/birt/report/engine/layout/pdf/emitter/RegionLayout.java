/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/


package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;


public class RegionLayout extends BlockStackingLayout
{

	public RegionLayout( LayoutEngineContext context, IContent content,
			IContainerArea container )
	{
		super( context, null, content );
		currentContext = new ContainerContext();
		contextList.add(currentContext);
		if ( container != null )
		{
			currentContext.root = (ContainerArea) container;
		}
		else
		{
			currentContext.root = (ContainerArea) AreaFactory.createLogicContainer( content.getReportContent( ));
		}
		currentContext.maxAvaWidth = currentContext.root.getContentWidth( );
		// set unlimited length for block direction
		currentContext.maxAvaHeight = Integer.MAX_VALUE ;
	}
	
	protected void initialize( )
	{
		createRoot( );
		currentContext.maxAvaWidth = currentContext.root.getContentWidth( );
		// set unlimited length for block direction
		currentContext.maxAvaHeight = Integer.MAX_VALUE;
	}
	
	public void layout()
	{
		initialize( );
		PDFLayoutEmitter emitter = new PDFLayoutEmitter( context );
		emitter.current = this;
		visitContent( content, emitter );
		closeLayout( );
		
	}
	
	
	
	

	protected void createRoot( )
	{
		if ( currentContext.root == null )
		{
			currentContext.root = (ContainerArea) AreaFactory.createLogicContainer( content.getReportContent( ));
		}
	}

	protected void closeLayout( )
	{
		// set dimension property for root TODO suppport user defined height
		currentContext.root.setHeight( Math.max( currentContext.currentBP, currentContext.root.getHeight( ) ) );
	}
	


}
