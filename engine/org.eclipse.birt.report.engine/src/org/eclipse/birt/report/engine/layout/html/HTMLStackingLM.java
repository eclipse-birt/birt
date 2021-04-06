/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

public abstract class HTMLStackingLM extends HTMLAbstractLM {

	public HTMLStackingLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	protected boolean layoutChildren() throws BirtException {
		boolean hasNext = layoutNodes();
		if (hasNext) {
			context.getPageHintManager().addLayoutHint(content, false);
		} else {
			context.getPageHintManager().removeLayoutHint(content);
		}
		return hasNext;
	}

	protected void end(boolean finished) throws BirtException {
		if (emitter != null) {
			context.getPageBufferManager().endContainer(content, finished, emitter, true);
		}
	}

	protected void start(boolean isFirst) throws BirtException {
		if (emitter != null) {
			context.getPageBufferManager().startContainer(content, isFirst, emitter, true);
		}
	}

	protected abstract boolean layoutNodes() throws BirtException;
}