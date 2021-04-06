/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IContent;

/**
 * Defines execution logic for a List report item.
 * 
 */
public class ListItemExecutor extends ListingElementExecutor {

	/**
	 * @param context execution context
	 * @param visitor visitor object for driving the execution
	 */
	protected ListItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LISTITEM);
	}

	protected IContent doCreateContent() {
		return report.createListContent();
	}

	protected void doExecute() throws Exception {
		executeQuery();

		boolean showIfBlank = "true".equalsIgnoreCase(content.getStyle().getShowIfBlank());
		if (showIfBlank && rsetEmpty) {
			createQueryForShowIfBlank();
		}

	}

	public void close() {
		closeQuery();
		super.close();
	}
}
