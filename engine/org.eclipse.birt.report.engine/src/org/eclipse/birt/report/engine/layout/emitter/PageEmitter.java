/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.emitter;

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;

public abstract class PageEmitter extends ContentEmitterAdapter {
	protected static Logger logger = Logger.getLogger(PageEmitter.class.getName());

	protected PageDeviceRender render;

	public abstract PageDeviceRender createRender(IEmitterServices service) throws EngineException;

	public String getOutputFormat() {
		return render.getOutputFormat();
	}

	public void initialize(IEmitterServices service) throws EngineException {
		render = createRender(service);
	}

	public void startPage(IPageContent page) {
		PageArea pageArea = (PageArea) page.getExtension(IContent.LAYOUT_EXTENSION);
		if (pageArea != null) {
			pageArea.accept(render);
		}

	}

	public void start(IReportContent report) {
		render.start(report);
	}

	public void end(IReportContent report) {
		render.end(report);

	}

	public void startAutoText(IAutoTextContent autoText) {
		ITextArea totalPage = (ITextArea) autoText.getExtension(IContent.LAYOUT_EXTENSION);
		render.setTotalPage(totalPage);
	}
}