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
