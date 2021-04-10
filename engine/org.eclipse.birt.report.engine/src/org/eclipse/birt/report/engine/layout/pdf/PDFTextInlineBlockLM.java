/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFTextInlineBlockLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {

	public PDFTextInlineBlockLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
	}

	protected void initialize() {
		boolean isNewArea = (root == null);
		// TODO refactor
		createRoot();
		if (isNewArea) {
			IStyle areaStyle = root.getStyle();
			validateBoxProperty(areaStyle, parent.getCurrentMaxContentWidth(), context.getMaxHeight());
			// initialize offsetX and offsetY
			setOffsetX(root.getContentX());
			setOffsetY(isFirst ? root.getContentY() : 0);
			// support user defined width
			int maxWidth = parent.getCurrentMaxContentWidth();
			// int leftWidth = maxWidth - parent.getCurrentIP( );
			calculateSpecifiedWidth();
			int width = maxWidth;
			if (specifiedWidth > 0) {
				width = Math.min(specifiedWidth, maxWidth);
			}
			root.setAllocatedWidth(width);
			// can be removed?
			setCurrentBP(0);
			setCurrentIP(0);
		}
		maxAvaWidth = root.getContentWidth();
		root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		maxAvaHeight = root.getContentHeight();

	}

	protected void createRoot() {
		if (root == null) {
			root = (ContainerArea) AreaFactory.createBlockContainer(content);
		}
	}

}
