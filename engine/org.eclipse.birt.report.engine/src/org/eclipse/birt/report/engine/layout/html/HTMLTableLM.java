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
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

public class HTMLTableLM extends HTMLRepeatHeaderLM {

	/**
	 * emitter used to layout the table
	 */
	protected HTMLTableLayoutEmitter tableEmitter;

	public HTMLTableLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	public int getType() {
		return LAYOUT_MANAGER_TABLE;
	}

	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		tableEmitter = new HTMLTableLayoutEmitter(emitter, context);
		super.initialize(parent, content, executor, tableEmitter);
	}

	protected void end(boolean finished) throws BirtException {
		context.getPageBufferManager().endContainer(content, finished, tableEmitter, true);
	}

	protected void start(boolean isFirst) throws BirtException {
		context.getPageBufferManager().startContainer(content, isFirst, tableEmitter, true);
	}

	protected IContentEmitter getEmitter() {
		return this.tableEmitter;
	}

	protected boolean shouldRepeatHeader() {
		return ((ITableContent) content).isHeaderRepeat() && getHeader() != null;
	}

	protected IBandContent getHeader() {
		return ((ITableContent) content).getHeader();
	}

}
