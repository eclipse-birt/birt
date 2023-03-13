/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.TemplateArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontHandler;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class TemplateLayout extends Layout {

	public TemplateLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
	}

	@Override
	protected void closeLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void layout() throws BirtException {
		boolean isInline = parent instanceof IInlineStackingLayout;
		if (isInline) {
			if (parent instanceof LineLayout) {
				ContainerLayout inlineContainer = new InlineContainerLayout(context, parent, content);
				inlineContainer.initialize();
				addTemplateArea(inlineContainer, true);
				inlineContainer.closeLayout();
			} else {
				addTemplateArea(parent, true);
			}
		} else {
			assert (parent instanceof BlockStackingLayout);
			boolean inlineElement = PropertyUtil.isInlineElement(content);
			if (!inlineElement) {
				BlockTextLayout tLayout = new BlockTextLayout(context, parent, content);
				tLayout.initialize();
				LineLayout line = new LineLayout(context, tLayout);
				line.initialize();
				addTemplateArea(line, false);
				line.closeLayout();
				tLayout.closeLayout();
			}
		}

	}

	protected void addTemplateArea(ContainerLayout parent, boolean isInline) {
		IAutoTextContent autoText = (IAutoTextContent) content;
		TemplateArea templateArea = (TemplateArea) AreaFactory.createTemplateArea(autoText);

		// get max available width
		int maxWidth = parent.getCurrentMaxContentWidth();
		templateArea.setAllocatedWidth(maxWidth - parent.currentContext.currentIP);
		int maxAvaWidth = templateArea.getWidth();
		// get user defined width
		int width = getDimensionValue(autoText.getWidth(), maxWidth);

		if (width == 0) {
			// the default content width
			int defaultWidth = getDimensionValue(templateArea.getStyle().getFontSize()) * 4;
			width = Math.min(maxAvaWidth, defaultWidth);
		} else if (width > maxAvaWidth) {
			width = maxAvaWidth;
		}
		templateArea.setWidth(width);
		context.setTotalPageTemplateWidth(templateArea.getContentWidth());

		FontHandler handler = new FontHandler(context.getFontManager(), autoText, false);
		FontInfo fontInfo = handler.getFontInfo();

		int height = getDimensionValue(autoText.getHeight(), 0);
		templateArea.setContentHeight(
				Math.max((int) (fontInfo.getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO), height));

		templateArea.setBaseLine(fontInfo.getBaseline() + templateArea.getContentY());
		parent.addToRoot(templateArea);
	}
}
