/***********************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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
import org.eclipse.birt.report.engine.content.IRowContent;

public class HTMLRowLM extends HTMLInlineStackingLM {
	public HTMLRowLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	public int getType() {
		return LAYOUT_MANAGER_ROW;
	}

	protected boolean handleVisibility() throws BirtException {
		// handle visibility in table layout
		boolean ret = super.handleVisibility();
		if (ret && ((HTMLTableLayoutEmitter) emitter).isLayoutStarted()) {
			emitter.startRow((IRowContent) content);
			emitter.endRow((IRowContent) content);
		}
		return ret;
	}

	protected boolean isPageBreakBefore() {
		if (context.isSoftRowBreak()) {
			context.setSoftRowBreak(false);
			return true;
		}
		return super.isPageBreakBefore();
	}
}
