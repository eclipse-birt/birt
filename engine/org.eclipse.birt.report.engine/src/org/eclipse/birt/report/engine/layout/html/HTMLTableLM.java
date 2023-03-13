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

	@Override
	public int getType() {
		return LAYOUT_MANAGER_TABLE;
	}

	@Override
	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		tableEmitter = new HTMLTableLayoutEmitter(emitter, context);
		super.initialize(parent, content, executor, tableEmitter);
	}

	@Override
	protected void end(boolean finished) throws BirtException {
		context.getPageBufferManager().endContainer(content, finished, tableEmitter, true);
	}

	@Override
	protected void start(boolean isFirst) throws BirtException {
		context.getPageBufferManager().startContainer(content, isFirst, tableEmitter, true);
	}

	@Override
	protected IContentEmitter getEmitter() {
		return this.tableEmitter;
	}

	@Override
	protected boolean shouldRepeatHeader() {
		return ((ITableContent) content).isHeaderRepeat() && getHeader() != null;
	}

	@Override
	protected IBandContent getHeader() {
		return ((ITableContent) content).getHeader();
	}

}
