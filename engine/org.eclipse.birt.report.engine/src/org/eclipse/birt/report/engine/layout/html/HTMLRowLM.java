/***********************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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
import org.eclipse.birt.report.engine.content.IRowContent;

public class HTMLRowLM extends HTMLInlineStackingLM {
	public HTMLRowLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	@Override
	public int getType() {
		return LAYOUT_MANAGER_ROW;
	}

	@Override
	protected boolean handleVisibility() throws BirtException {
		// handle visibility in table layout
		boolean ret = super.handleVisibility();
		if (ret && ((HTMLTableLayoutEmitter) emitter).isLayoutStarted()) {
			emitter.startRow((IRowContent) content);
			emitter.endRow((IRowContent) content);
		}
		return ret;
	}

	@Override
	protected boolean isPageBreakBefore() {
		if (context.isSoftRowBreak()) {
			context.setSoftRowBreak(false);
			return true;
		}
		return super.isPageBreakBefore();
	}
}
