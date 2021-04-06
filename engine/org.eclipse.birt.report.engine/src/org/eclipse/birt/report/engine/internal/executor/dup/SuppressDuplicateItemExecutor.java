/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.executor.dup;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportItemExecutor;

public class SuppressDuplicateItemExecutor extends WrappedReportItemExecutor {

	boolean executed;
	IContent content;

	SuppressDuplicateItemExecutor(SuppressDuplciateReportExecutor reportExecutor, IReportItemExecutor executor) {
		super(reportExecutor, executor);
	}

	public void close() throws BirtException {
		content = null;
		executed = false;
		super.close();
	}

	public IContent execute() throws BirtException {
		if (executed == false) {
			content = executor.execute();
			if (content != null) {
				int type = content.getContentType();
				switch (type) {
				case IContent.TABLE_GROUP_CONTENT:
				case IContent.GROUP_CONTENT:
				case IContent.LIST_GROUP_CONTENT:
				case IContent.LIST_CONTENT:
				case IContent.TABLE_CONTENT:
					((SuppressDuplciateReportExecutor) reportExecutor).clearDuplicateFlags(content);
					break;
				case IContent.DATA_CONTENT:
					content = ((SuppressDuplciateReportExecutor) reportExecutor).suppressDuplicate(content);
				}
			}
		}
		return content;
	}

	public IContent getContent() {
		return content;
	}
}
