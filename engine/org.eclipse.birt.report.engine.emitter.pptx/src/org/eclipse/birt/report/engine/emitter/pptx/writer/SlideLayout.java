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

import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;

public class SlideLayout extends Component {

	public SlideLayout(Presentation presentation, SlideMaster slideMaster) throws IOException {

		String uri = "slideLayouts/slideLayout" + presentation.getNextSlideLayoutId() + ".xml";
		String type = ContentTypes.SLIDE_LAYOUT;
		String relationshipType = RelationshipTypes.SLIDE_LAYOUT;
		super.initialize(presentation.getPart(), uri, type, relationshipType, false);

		referTo(slideMaster);

		initialize();
	}

	private void initialize() {
		writer.startWriter();
		writer.openTag("p:sldLayout");
		writer.nameSpace("a", NameSpaces.DRAWINGML);
		writer.nameSpace("r", NameSpaces.RELATIONSHIPS);
		writer.nameSpace("p", NameSpaces.PRESENTATIONML);
		writer.attribute("type", "title");
		writer.attribute("preserve", "1");
		writer.openTag("p:cSld");
		writer.attribute("name", "Title Slide");
		writer.openTag("p:spTree");
		writer.openTag("p:nvGrpSpPr");
		writer.openTag("p:cNvPr");
		writer.attribute("id", "1");
		writer.attribute("name", "");
		writer.closeTag("p:cNvPr");
		writer.openTag("p:cNvGrpSpPr");
		writer.closeTag("p:cNvGrpSpPr");
		writer.openTag("p:nvPr");
		writer.closeTag("p:nvPr");
		writer.closeTag("p:nvGrpSpPr");
		writer.openTag("p:grpSpPr");
		writer.openTag("a:xfrm");
		writer.openTag("a:off");
		writer.attribute("x", "0");
		writer.attribute("y", "0");
		writer.closeTag("a:off");
		writer.openTag("a:ext");
		writer.attribute("cx", "0");
		writer.attribute("cy", "0");
		writer.closeTag("a:ext");
		writer.openTag("a:chOff");
		writer.attribute("x", "0");
		writer.attribute("y", "0");
		writer.closeTag("a:chOff");
		writer.openTag("a:chExt");
		writer.attribute("cx", "0");
		writer.attribute("cy", "0");
		writer.closeTag("a:chExt");
		writer.closeTag("a:xfrm");
		writer.closeTag("p:grpSpPr");
		writer.closeTag("p:spTree");
		writer.closeTag("p:cSld");
		writer.openTag("p:clrMapOvr");
		writer.openTag("a:masterClrMapping");
		writer.closeTag("a:masterClrMapping");
		writer.closeTag("p:clrMapOvr");
		writer.closeTag("p:sldLayout");
		writer.endWriter();
		writer.close();
		this.writer = null;
	}
}
