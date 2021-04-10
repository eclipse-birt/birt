/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html.buffer;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class PageNode extends ContainerBufferNode implements IContainerNode {

	public PageNode(IContent content, IContentEmitter emitter, PageHintGenerator generator, boolean isVisible) {
		super(content, emitter, generator, isVisible);
	}

	public void end() throws BirtException {
		flushUnStartedChildren();
		if (isVisible) {
			ContentEmitterUtil.endContent(content, emitter);
		}
	}

	public void start() throws BirtException {
		if (isStarted) {
			return;
		}
		if (parent != null && !parent.isStarted()) {
			parent.start();
		}
		if (isVisible) {
			ContentEmitterUtil.startContent(content, emitter);
		}
		isStarted = true;
		flushUnStartedChildren();
	}

}
