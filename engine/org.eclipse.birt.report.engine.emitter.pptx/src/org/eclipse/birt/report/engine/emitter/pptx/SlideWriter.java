/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx;

import org.eclipse.birt.report.engine.emitter.pptx.writer.SlideMaster;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;

public class SlideWriter {

	private final PPTXRender render;

	public SlideWriter(PPTXRender render) {
		this.render = render;
	}

	public void writeSlideMaster(SlideMaster slide) {
		PageArea pageArea = slide.getPageArea();
		PPTXRender masterRender = new PPTXRender(this.render, slide.getCanvas());
		masterRender.setCurrentX(pageArea.getRoot().getX());
		masterRender.setCurrentY(pageArea.getRoot().getY());
		IContainerArea pageHeader = pageArea.getHeader();
		if (pageHeader != null) {
			pageHeader.accept(masterRender);
		}
		IContainerArea pageFooter = pageArea.getFooter();
		if (pageFooter != null) {
			pageFooter.accept(masterRender);
		}
	}

	public void writeSlide(PageArea pageArea) {
		render.setCurrentX(pageArea.getRoot().getX());
		render.setCurrentY(pageArea.getRoot().getY());
		pageArea.getBody().accept(render);
	}
}
