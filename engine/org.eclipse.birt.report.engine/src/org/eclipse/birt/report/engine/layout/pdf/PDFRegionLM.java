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
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFRegionLM extends PDFBlockContainerLM {

	public PDFRegionLM(PDFLayoutEngineContext context, IContainerArea container, IContent content,
			IReportItemExecutor executor) {
		super(context, null, content, executor);
		if (container != null) {
			root = (ContainerArea) container;
		} else {
			root = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		}
		maxAvaWidth = root.getContentWidth();
		// set unlimited length for block direction
		maxAvaHeight = Integer.MAX_VALUE;
	}

	protected void initialize() {
		createRoot();
		maxAvaWidth = root.getContentWidth();
		// set unlimited length for block direction
		maxAvaHeight = Integer.MAX_VALUE;
	}

	protected void createRoot() {
		if (root == null) {
			root = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		}
	}

	protected void closeLayout() {
		// set dimension property for root TODO suppport user defined height
		root.setHeight(Math.max(getCurrentBP(), root.getHeight()));
	}
}
