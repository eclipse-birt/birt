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

import org.eclipse.birt.report.engine.emitter.pptx.PPTXCanvas;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;

public class SlideMaster extends Component {

	private final Presentation presentation;
	private final SlideLayout slideLayout;
	private final PageArea masterarea;

	public SlideMaster(Presentation presentation, PageArea area) throws IOException {
		this.presentation = presentation;
		String type = ContentTypes.SLIDE_MASTER;
		String relationshipType = RelationshipTypes.SLIDE_MASTER;
		String uri = "slideMasters/slideMaster" + presentation.getNextSlideMasterId() + ".xml";
		initialize(presentation.getPart(), uri, type, relationshipType);
		masterarea = area;
		writePage(area);

		slideLayout = new SlideLayout(presentation, this);
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void close() throws IOException {
		writer.closeTag("p:spTree");
		writer.closeTag("p:cSld");
		writer.openTag("p:clrMap");
		writer.attribute("bg1", "lt1");
		writer.attribute("tx1", "dk1");
		writer.attribute("bg2", "lt2");
		writer.attribute("tx2", "dk2");
		writer.attribute("accent1", "accent1");
		writer.attribute("accent2", "accent2");
		writer.attribute("accent3", "accent3");
		writer.attribute("accent4", "accent4");
		writer.attribute("accent5", "accent5");
		writer.attribute("accent6", "accent6");
		writer.attribute("hlink", "hlink");
		writer.attribute("folHlink", "folHlink");
		writer.closeTag("p:clrMap");
		outputSlideLayouts();
		writer.openTag("p:txStyles");
		writer.openTag("p:titleStyle");
		writer.closeTag("p:titleStyle");
		writer.openTag("p:bodyStyle");
		writer.closeTag("p:bodyStyle");
		writer.openTag("p:otherStyle");
		writer.openTag("a:lvl1pPr");
		writer.closeTag("a:lvl1pPr");
		writer.closeTag("p:otherStyle");
		writer.closeTag("p:txStyles");
		writer.closeTag("p:sldMaster");
		writer.endWriter();
		writer.close();
		this.writer = null;
	}

	private void outputSlideLayouts() {
		writer.openTag("p:sldLayoutIdLst");

		long id = presentation.getNextGlobalId();
		String relationshipId = referTo(slideLayout).getRelationshipId();
		writer.openTag("p:sldLayoutId");
		writer.attribute("id", String.valueOf(id));
		writer.attribute("r:id", relationshipId);
		writer.closeTag("p:sldLayoutId");

		writer.closeTag("p:sldLayoutIdLst");
	}

	private void drawSlideBackground(PageArea pageArea) {
		writer.openTag("p:bg");
		BoxStyle style = pageArea.getBoxStyle();
		Color bgColor = style.getBackgroundColor();
		PPTXCanvas canvas = getCanvas();
		String imageRelationship = canvas.getImageRelationship(style.getBackgroundImage());
		if (imageRelationship != null || bgColor != null) {
			writer.openTag("p:bgPr");
			if (imageRelationship != null) {
				canvas.setBackgroundImg(imageRelationship, 0, 0);
			} else {
				canvas.setBackgroundColor(bgColor);
			}
			writer.openTag("a:effectLst");
			writer.closeTag("a:effectLst");
			writer.closeTag("p:bgPr");

		} else {
			// slidemaster need some defalut color
			writer.openTag("p:bgRef");
			writer.attribute("idx", "1001");
			writer.openTag("a:schemeClr");
			writer.attribute("val", "bg1");
			writer.closeTag("a:schemeClr");
			writer.closeTag("p:bgRef");
		}
		writer.closeTag("p:bg");
	}

	public void writePage(PageArea pageArea) {
		writer.startWriter();
		writer.openTag("p:sldMaster");
		writer.nameSpace("a", NameSpaces.DRAWINGML);
		writer.nameSpace("r", NameSpaces.RELATIONSHIPS);
		writer.nameSpace("p", NameSpaces.PRESENTATIONML);
		writer.openTag("p:cSld");
		drawSlideBackground(pageArea);
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
	}

	public SlideLayout getSlideLayout() {
		return slideLayout;
	}

	public PPTXCanvas getCanvas() {
		return new PPTXCanvas(presentation, this.part, writer);
	}

	public PageArea getPageArea() {
		return masterarea;
	}
}
