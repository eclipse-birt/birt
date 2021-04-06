/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;

public class TableRegionLayout extends TableLayout {

	public TableRegionLayout(LayoutEngineContext context, IContent content, TableLayoutInfo layoutInfo,
			TableAreaLayout areaLayout) {
		super(context, null, content);
		this.layoutInfo = layoutInfo;
		TableContext tableContext = new TableContext();
		contextList.add(tableContext);
		currentContext = tableContext;
		tableContext.layout = areaLayout;
	}

	IContent layoutContent;

	public void initialize(IContent layoutContent) {

		this.layoutContent = layoutContent;
	}

	protected void initialize() {
		tableContext = (TableContext) currentContext;
		createRoot();
		currentContext.root.setWidth(layoutInfo.getTableWidth());
		currentContext.maxAvaWidth = layoutInfo.getTableWidth();
		currentContext.maxAvaHeight = Integer.MAX_VALUE;

	}

	public TableAreaLayout getTableAreaLayout() {
		return tableContext.layout;
	}

	public void layout() throws BirtException {
		initialize();
		PDFLayoutEmitter emitter = new PDFLayoutEmitter(context);
		emitter.current = this;
		visitContent(layoutContent, emitter);
		closeLayout();
	}

	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
		currentContext.root.setHeight(currentContext.currentBP + getOffsetY());
		this.layoutContent.setExtension(IContent.LAYOUT_EXTENSION, currentContext.root);
	}

}
