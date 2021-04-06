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
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;

public abstract class PDFGroupLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {

	protected IGroupContent groupContent;

	protected int repeatCount = 0;

	public PDFGroupLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		groupContent = (IGroupContent) content;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	protected boolean traverseChildren() throws BirtException {
		repeatHeader();
		boolean hasNextPage = super.traverseChildren();
		return hasNextPage;
	}

	protected boolean isRepeatHeader() {
		return ((IGroupContent) content).isHeaderRepeat();
	}

	protected abstract void repeatHeader() throws BirtException;

	protected boolean isRootEmpty() {
		return !((root != null && root.getChildrenCount() > repeatCount) || isLast);
	}

}
