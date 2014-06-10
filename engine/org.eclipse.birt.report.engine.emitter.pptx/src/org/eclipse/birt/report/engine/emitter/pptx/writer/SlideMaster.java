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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;


public class SlideMaster extends Component
{

	private List<SlideLayout> slideLayouts = new ArrayList<SlideLayout>( );

	public SlideMaster( Presentation presentation ) throws IOException
	{
		String type = ContentTypes.SLIDE_MASTER;
		String relationshipType = RelationshipTypes.SLIDE_MASTER;
		String uri = "slideMasters/slideMaster1.xml";
		initialize( presentation.getPart( ), uri, type, relationshipType );
		writer.startWriter( );
		writer.openTag( "p:sldMaster" );
		writer.nameSpace( "a", NameSpaces.DRAWINGML );
		writer.nameSpace( "r", NameSpaces.RELATIONSHIPS );
		writer.nameSpace( "p", NameSpaces.PRESENTATIONML );
		writer.openTag( "p:cSld" );
		writer.openTag( "p:bg" );
		writer.openTag( "p:bgRef" );
		writer.attribute( "idx", "1001" );
		writer.openTag( "a:schemeClr" );
		writer.attribute( "val", "bg1" );
		writer.closeTag( "a:schemeClr" );
		writer.closeTag( "p:bgRef" );
		writer.closeTag( "p:bg" );
		writer.openTag( "p:spTree" );
		writer.openTag( "p:nvGrpSpPr" );
		writer.openTag( "p:cNvPr" );
		writer.attribute( "id", "1" );
		writer.attribute( "name", "" );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvGrpSpPr" );
		writer.closeTag( "p:cNvGrpSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvGrpSpPr" );
		writer.openTag( "p:grpSpPr" );
		writer.openTag( "a:xfrm" );
		writer.openTag( "a:off" );
		writer.attribute( "x", "0" );
		writer.attribute( "y", "0" );
		writer.closeTag( "a:off" );
		writer.openTag( "a:ext" );
		writer.attribute( "cx", "0" );
		writer.attribute( "cy", "0" );
		writer.closeTag( "a:ext" );
		writer.openTag( "a:chOff" );
		writer.attribute( "x", "0" );
		writer.attribute( "y", "0" );
		writer.closeTag( "a:chOff" );
		writer.openTag( "a:chExt" );
		writer.attribute( "cx", "0" );
		writer.attribute( "cy", "0" );
		writer.closeTag( "a:chExt" );
		writer.closeTag( "a:xfrm" );
		writer.closeTag( "p:grpSpPr" );
		writer.closeTag( "p:spTree" );
		writer.closeTag( "p:cSld" );
		writer.openTag( "p:clrMap" );
		writer.attribute( "bg1", "lt1" );
		writer.attribute( "tx1", "dk1" );
		writer.attribute( "bg2", "lt2" );
		writer.attribute( "tx2", "dk2" );
		writer.attribute( "accent1", "accent1" );
		writer.attribute( "accent2", "accent2" );
		writer.attribute( "accent3", "accent3" );
		writer.attribute( "accent4", "accent4" );
		writer.attribute( "accent5", "accent5" );
		writer.attribute( "accent6", "accent6" );
		writer.attribute( "hlink", "hlink" );
		writer.attribute( "folHlink", "folHlink" );
		writer.closeTag( "p:clrMap" );
	}

	public void addSlideLayout( SlideLayout slideLayout ) throws IOException
	{
		slideLayouts.add( slideLayout );
	}

	public void close( ) throws IOException
	{
		outputSlideLayouts( );
		writer.openTag( "p:txStyles" );
		writer.openTag( "p:titleStyle" );
		writer.closeTag( "p:titleStyle" );
		writer.openTag( "p:bodyStyle" );
		writer.closeTag( "p:bodyStyle" );
		writer.openTag( "p:otherStyle" );
		writer.openTag( "a:lvl1pPr" );
		writer.closeTag( "a:lvl1pPr" );
		writer.closeTag( "p:otherStyle" );
		writer.closeTag( "p:txStyles" );
		writer.closeTag( "p:sldMaster" );
		writer.endWriter( );
		writer.close( );
		this.writer = null;
	}

	private void outputSlideLayouts( )
	{
		writer.openTag( "p:sldLayoutIdLst" );
		int layoutCount = 1;
		for ( SlideLayout slideLayout : slideLayouts )
		{
			long id = 2147483648l + layoutCount;
			String relationshipId = referTo( slideLayout ).getRelationshipId( );
			writer.openTag( "p:sldLayoutId" );
			writer.attribute( "id", String.valueOf( id ) );
			writer.attribute( "r:id", relationshipId );
			writer.closeTag( "p:sldLayoutId" );
			layoutCount++;
		}
		writer.closeTag( "p:sldLayoutIdLst" );
	}
}
