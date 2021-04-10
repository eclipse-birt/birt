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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFBlockContainerLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {

	public PDFBlockContainerLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
	}

	protected void createRoot() {
		root = (ContainerArea) createBlockContainer(content, isFirst, true);
	}

	protected boolean traverseSingleChild() throws BirtException {
		if (child != null) {
			boolean childBreak = false;
			childBreak = child.layout();
			if (childBreak) {
				if (child.isFinished()) {
					child = null;
				} else {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	/**
	 * create block container area by content
	 * 
	 * @param content the content object
	 * @param isFirst if this area is the first area of the content
	 * @param isLast  if this area is the last area of the content
	 * @return
	 */
	public static IContainerArea createBlockContainer(IContent content, boolean isFirst, boolean isLast) {
		IContainerArea containerArea = AreaFactory.createBlockContainer(content);
		IStyle style = containerArea.getStyle();
		// remove top padding, border and margin if it is not the first child
		if (!isFirst) {
			style.setProperty(IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		}
		// remove bottom padding, border and margin if it is not the last child
		if (!isLast) {
			style.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_BOTTOM, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);
		}
		return containerArea;
	}

}
