/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.html;

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.RegionLayoutEngine;
import org.eclipse.birt.report.engine.nLayout.area.impl.HtmlRegionArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.SizeBasedContent;

public class HTMLLeafItemLM extends HTMLAbstractLM implements ILayoutManager {

	public HTMLLeafItemLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	public int getType() {
		return LAYOUT_MANAGER_LEAF;
	}

	protected boolean layoutChildren() {
		return false;
	}

	protected boolean isChildrenFinished() {
		return true;
	}

	protected void end(boolean finished) {

	}

	protected void start(boolean isFirst) throws BirtException {
		if (content instanceof TextContent || content instanceof LabelContent || content instanceof DataContent) {
			splitText();
		}
		if (emitter != null) {
			context.getPageBufferManager().startContent(content, emitter, true);
		}
	}

	/**
	 * Splits text for fixed layout reports.
	 */
	private void splitText() {
		Integer taskType = (Integer) engine.getOption(EngineTask.TASK_TYPE);
		if (taskType.intValue() == IEngineTask.TASK_RENDER && context.isFixedLayout()) {
			SizeBasedContent sizeBasedContent = context.getPageHintManager().getSizeBasedContentMapping()
					.get(content.getInstanceID().toUniqueString());
			if (sizeBasedContent == null || sizeBasedContent.dimension == -1) {
				return;
			}
			HtmlRegionArea container = new HtmlRegionArea();
			container.setWidth(sizeBasedContent.width);
			IReportContent report = content.getReportContent();
			IContainerContent containerContent = report.createContainerContent();
			containerContent.getChildren().add(content);
			LayoutContext pdfLayoutContext = new LayoutContext();
			pdfLayoutContext.setFormat("pdf");
			pdfLayoutContext.setFixedLayout(true);
			pdfLayoutContext.setInHtmlRender(true);
			pdfLayoutContext.setLocale(engine.locale);
			pdfLayoutContext.setHtmlLayoutContext(context);
			pdfLayoutContext.setMaxBP(Integer.MAX_VALUE);
			pdfLayoutContext.setMaxHeight(Integer.MAX_VALUE);
			pdfLayoutContext.setReport(report);
			RegionLayoutEngine rle = new RegionLayoutEngine(container, pdfLayoutContext);
			try {
				rle.layout(containerContent);
			} catch (BirtException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}

	}
}
