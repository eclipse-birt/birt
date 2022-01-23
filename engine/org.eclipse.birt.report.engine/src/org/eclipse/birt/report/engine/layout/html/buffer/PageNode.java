/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
