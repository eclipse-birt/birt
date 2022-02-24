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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class RegionLayout extends BlockStackingLayout {

	public RegionLayout(LayoutEngineContext context, IContent content, IContainerArea container) {
		super(context, null, content);
		currentContext = new ContainerContext();
		contextList.add(currentContext);
		if (container != null) {
			currentContext.root = (ContainerArea) container;
		} else {
			currentContext.root = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		}
		currentContext.maxAvaWidth = currentContext.root.getContentWidth();
		// set unlimited length for block direction
		currentContext.maxAvaHeight = Integer.MAX_VALUE;
	}

	protected void initialize() {
		createRoot();
		currentContext.maxAvaWidth = currentContext.root.getContentWidth();
		// set unlimited length for block direction
		currentContext.maxAvaHeight = Integer.MAX_VALUE;
	}

	public void layout() throws BirtException {
		initialize();
		PDFLayoutEmitter emitter = new PDFLayoutEmitter(context);
		emitter.current = this;
		visitContent(content, emitter);
		closeLayout();
	}

	protected void createRoot() {
		if (currentContext.root == null) {
			currentContext.root = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		}
	}

	protected void closeLayout() {
		// set dimension property for root TODO support user defined height
		currentContext.root.setHeight(Math.max(currentContext.currentBP, currentContext.root.getHeight()));
	}

}
