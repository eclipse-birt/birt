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

import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

/**
 * Page break handler which notifies the context to fire a page break event;
 *
 */
public class ContextPageBreakHandler implements ILayoutPageHandler {
	ExecutionContext context;

	public ContextPageBreakHandler(ExecutionContext context) {
		this.context = context;
	}

	public void onPage(long page, Object pageContext) {
		if (pageContext instanceof HTMLLayoutContext) {
			context.firePageBreakEvent(((HTMLLayoutContext) pageContext).isHorizontalPageBreak(), false);
		} else {
			// Move cursor to reduce row count in this page only if the page
			// break is triggered by last page content size overflow
			context.firePageBreakEvent(false,
					pageContext instanceof LayoutContext && ((LayoutContext) pageContext).isSizeOverflowPageBreak());
		}
	}

}
