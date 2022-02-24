/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class PageBandExecutor extends ReportItemExecutor {

	private ArrayList contents;
	private int nextItem;

	protected PageBandExecutor(MasterPageExecutor parent, ArrayList contents) {
		super(parent.manager, -1);
		this.parent = parent;
		this.contents = contents;
		nextItem = 0;
	}

	public void close() throws BirtException {
		nextItem = 0;
		contents = null;
		super.close();
	}

	public IContent execute() {
		content = report.createContainerContent();
		initializeContent(null, content);
		return content;
	}

	public boolean hasNextChild() {
		return nextItem < contents.size();
	}

	public IReportItemExecutor getNextChild() {
		int itemCount = contents.size();
		if (nextItem < itemCount) {
			ReportItemDesign itemDesign = (ReportItemDesign) contents.get(nextItem);
			nextItem++;
			return manager.createExecutor(this, itemDesign);
		}
		return null;
	}
}
