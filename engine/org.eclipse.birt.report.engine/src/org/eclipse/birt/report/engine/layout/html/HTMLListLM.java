/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.html.buffer.IPageBuffer;

public class HTMLListLM extends HTMLBlockStackingLM {

	public HTMLListLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	public int getType() {
		return LAYOUT_MANAGER_LIST;
	}

	boolean isFirstLayout;

	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		super.initialize(parent, content, executor, emitter);
		isFirstLayout = true;
	}

	protected void repeatHeader() throws BirtException {
		if (!isFirstLayout) {
			IListContent list = (IListContent) content;
			if (list.isHeaderRepeat()) {
				IBandContent header = list.getHeader();
				if (header != null) {
					boolean pageBreak = context.allowPageBreak();
					context.setAllowPageBreak(pageBreak);
					IPageBuffer buffer = context.getPageBufferManager();
					boolean isRepeated = buffer.isRepeated();
					buffer.setRepeated(true);
					engine.layout(this, header, emitter);
					buffer.setRepeated(isRepeated);
					context.setAllowPageBreak(pageBreak);
				}
			}
		}
		isFirstLayout = false;
	}

	protected boolean layoutChildren() throws BirtException {
		repeatHeader();
		boolean hasNext = super.layoutChildren();
		return hasNext;
	}

}
