/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontHandler;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public class TemplateAreaLayout implements ILayout {

	protected IContent content;
	protected LayoutContext context;
	protected ContainerArea parent;

	public TemplateAreaLayout(ContainerArea parent, LayoutContext context, IContent content) {
		this.content = content;
		this.parent = parent;
		this.context = context;
	}

	@Override
	public void layout() throws BirtException {
		boolean isInline = parent instanceof InlineStackingArea;
		if (isInline) {
			if (parent instanceof LineArea) {
				InlineContainerArea inlineContainer = new InlineContainerArea(parent, context, content);
				inlineContainer.initialize();
				addTemplateArea(inlineContainer, true);
				inlineContainer.close();
			} else {
				addTemplateArea(parent, true);
			}
		} else {
			assert (parent instanceof BlockContainerArea);
			boolean inlineElement = PropertyUtil.isInlineElement(content);
			if (!inlineElement) {
				BlockTextArea t = new BlockTextArea(parent, context, content);
				t.initialize();
				LineArea line = new TextLineArea(t, context);
				line.initialize();
				addTemplateArea(line, false);
				line.close();
				t.close();
			}
		}

	}

	protected TemplateArea createTemplateArea(IContent content, FontInfo fontInfo, int type) {
		TextStyle textStyle = TextAreaLayout.buildTextStyle(content, fontInfo);
		TemplateArea area = new TemplateArea(null, textStyle, type);
		area.setAction(content.getHyperlinkAction());
		/* area.setBookmark( content.getBookmark( ) ); */
		return area;
	}

	protected void addTemplateArea(ContainerArea parent, boolean isInline) throws BirtException {
		IAutoTextContent autoText = (IAutoTextContent) content;
		FontHandler handler = new FontHandler(context.getFontManager(), autoText, false);
		FontInfo fontInfo = handler.getFontInfo();

		TemplateArea templateArea = createTemplateArea(content, fontInfo, autoText.getType());
		templateArea.setParent(parent);
		// get max available width
		int maxWidth = parent.getCurrentMaxContentWidth();
		templateArea.setWidth(maxWidth - parent.getCurrentIP());
		int maxAvaWidth = templateArea.getWidth();
		// get user defined width
		int width = PropertyUtil.getDimensionValue(content, autoText.getWidth(), maxWidth);

		if (width == 0) {
			// the default content width
			int defaultWidth = templateArea.getTextStyle().getFontSize() * 4;
			width = Math.min(maxAvaWidth, defaultWidth);
		} else if (width > maxAvaWidth) {
			width = maxAvaWidth;
		}
		templateArea.setWidth(width);
		context.setTotalPageTemplateWidth(templateArea.getWidth());

		int height = PropertyUtil.getDimensionValue(content, autoText.getHeight(), 0);
		templateArea.setHeight(Math.max((int) (fontInfo.getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO), height));

		templateArea.setBaseLine(fontInfo.getBaseline() + templateArea.getY());
		parent.add(templateArea);
		templateArea.setParent(parent);
		parent.update(templateArea);
	}

}
