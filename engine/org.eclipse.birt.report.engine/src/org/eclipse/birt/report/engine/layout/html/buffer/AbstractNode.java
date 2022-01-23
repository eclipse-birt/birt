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

public abstract class AbstractNode implements INode {

	protected IContent content;
	protected IContentEmitter emitter;
	boolean isFirst = true;
	protected boolean finished = true;
	protected IContainerNode parent;
	protected boolean isStarted = false;
	protected PageHintGenerator generator;
	protected boolean isVisible;

	AbstractNode(IContent content, IContentEmitter emitter, PageHintGenerator generator, boolean isVisible) {
		this.content = content;
		this.emitter = emitter;
		this.generator = generator;
		this.isVisible = isVisible;
	}

	public IContent getContent() {
		return content;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void end() throws BirtException {
		if (isVisible) {
			ContentEmitterUtil.endContent(content, emitter);
		}
		generator.end(content, finished);
	}

	public void setParent(IContainerNode parent) {
		this.parent = parent;
	}

	public IContainerNode getParent() {
		return parent;
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
		generator.start(content, isFirst);
		isStarted = true;

	}

}
