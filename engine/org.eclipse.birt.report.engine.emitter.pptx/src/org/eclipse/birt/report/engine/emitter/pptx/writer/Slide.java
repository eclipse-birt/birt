/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx.writer;

import java.awt.Color;
import java.io.IOException;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.pptx.PPTXCanvas;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class Slide extends Component
{

	private static final String TAG_SLIDE = "p:sld";
	private int index;

	private Presentation presentation;
	private boolean isClosed = false;

	public Slide( Presentation presentation, int slideIndex )
			throws IOException
	{
		this( presentation, slideIndex, null );
	}

	public Slide( Presentation presentation, int slideIndex, Color bgColor )
			throws IOException
	{
		this.index = slideIndex;
		this.presentation = presentation;
		String uri = getSlideUri( index );
		String relationShipType = RelationshipTypes.SLIDE;
		String type = ContentTypes.SLIDE;
		initialize( presentation.getPart( ), uri, type, relationShipType );
		new SlideLayout( this, index );
		writer.startWriter( );
		writer.openTag( TAG_SLIDE );
		writer.nameSpace( "a", NameSpaces.DRAWINGML );
		writer.nameSpace( "r", NameSpaces.RELATIONSHIPS );
		writer.nameSpace( "p", NameSpaces.PRESENTATIONML );
		writer.openTag( "p:cSld" );
		if ( bgColor != null )
			drawSlideBackgroundColor( bgColor );
		writer.openTag( "p:spTree" );
		writer.openTag( "p:nvGrpSpPr" );
		writer.openTag( "p:cNvPr" );
		writer.attribute( "id", presentation.nextShapeId( ) );
		writer.attribute( "name", "" );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvGrpSpPr" );
		writer.closeTag( "p:cNvGrpSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvGrpSpPr" );
		writer.openTag( "p:grpSpPr" );
		writer.closeTag( "p:grpSpPr" );
	}

	private void drawSlideBackgroundColor( Color color )
	{
		writer.openTag( "p:bg" );
		writer.openTag( "p:bgPr" );
		writer.openTag( "a:solidFill" );
		writer.openTag( "a:srgbClr" );
		writer.attribute( "val", EmitterUtil.getColorString( color ) );
		writer.closeTag( "a:srgbClr" );
		writer.closeTag( "a:solidFill" );
		writer.openTag( "a:effectLst" );
		writer.closeTag( "a:effectLst" );
		writer.closeTag( "p:bgPr" );
		writer.closeTag( "p:bg" );
	}
	
	public Presentation getPresentation( )
	{
		return presentation;
	}

	private String getSlideUri( int slideIndex )
	{
		return "slides/slide" + slideIndex + ".xml";
	}

	public int getSlideId( )
	{
		return 255 + index;
	}

	public void dispose( )
	{
		if ( !isClosed )
		{
			writer.closeTag( "p:spTree" );
			writer.closeTag( "p:cSld" );
			writer.openTag( "p:clrMapOvr" );
			writer.openTag( "a:masterClrMapping" );
			writer.closeTag( "a:masterClrMapping" );
			writer.closeTag( "p:clrMapOvr" );
			writer.closeTag( TAG_SLIDE );
			writer.endWriter( );
			writer.close( );
			writer = null;
			isClosed = true;
		}
	}

	public OOXmlWriter getWriter( ) 
	{
		return writer;
	}

	public PPTXCanvas getCanvas()
	{
		return new PPTXCanvas( presentation, this.part, writer);
	}
}
