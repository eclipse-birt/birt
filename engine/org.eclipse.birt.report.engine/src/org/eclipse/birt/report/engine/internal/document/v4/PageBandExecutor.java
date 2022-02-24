/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * execute the page header and footer in the master page
 */
public class PageBandExecutor extends ContainerExecutor {

	private ArrayList contents;
	private int nextItem;

	protected PageBandExecutor(MasterPageExecutor parent, ArrayList contents) {
		super(parent.manager, -1);
		this.parent = parent;
		this.contents = contents;
		nextItem = 0;
	}

	public void close() {
		nextItem = 0;
		this.contents = null;
		super.close();
	}

	protected IContent doCreateContent() {
		return report.createContainerContent();
	}

	protected void doExecute() throws Exception {

	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		int itemCount = contents.size();
		if (nextItem < itemCount) {
			ReportItemDesign itemDesign = (ReportItemDesign) contents.get(nextItem);
			nextItem++;
			return manager.createExecutor(this, itemDesign, offset);
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		int itemCount = contents.size();
		long designId = id.getComponentID();
		for (int i = 0; i < itemCount; i++) {
			ReportItemDesign itemDesign = (ReportItemDesign) contents.get(i);
			if (designId == itemDesign.getID()) {
				nextItem = i;
				return;
			}
		}
		nextItem = itemCount;
	}
}
