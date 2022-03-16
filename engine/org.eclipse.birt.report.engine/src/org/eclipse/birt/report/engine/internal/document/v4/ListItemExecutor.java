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

	@Override
	protected IContent doCreateContent() {
		return report.createListContent();
	}

	@Override
	protected void doExecute() throws Exception {
		executeQuery();

		boolean showIfBlank = "true".equalsIgnoreCase(content.getStyle().getShowIfBlank());
		if (showIfBlank && rsetEmpty) {
			createQueryForShowIfBlank();
		}

	}

	@Override
	public void close() {
		closeQuery();
		super.close();
	}
}
