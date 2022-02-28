/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.html.buffer;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;

public interface IPageBuffer {

	void startContainer(IContent content, boolean isFirst, IContentEmitter emitter, boolean visible)
			throws BirtException;

	void endContainer(IContent content, boolean finished, IContentEmitter emitter, boolean visible)
			throws BirtException;

	void startContent(IContent content, IContentEmitter emitter, boolean visible) throws BirtException;

	void setRepeated(boolean isRepeated);

	boolean isRepeated();

	void flush() throws BirtException;

	boolean finished();

	void openPage(INode[] nodeList) throws BirtException;

	void closePage(INode[] nodeList) throws BirtException;

	INode[] getNodeStack();

	void addTableColumnHint(TableColumnHint hint);
}
