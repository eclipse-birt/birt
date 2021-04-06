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

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class ContainerBufferNode extends AbstractNode implements IContainerNode {

	protected ArrayList children = new ArrayList();

	public ContainerBufferNode(IContent content, IContentEmitter emitter, PageHintGenerator generator,
			boolean isVisible) {
		super(content, emitter, generator, isVisible);
	}

	protected void flushChildren() throws BirtException {
		int size = children.size();
		for (int i = 0; i < size; i++) {
			INode child = (INode) children.get(i);
			child.flush();
		}
	}

	public void flush() throws BirtException {
		if (!isStarted) {
			start();
		}
		flushChildren();
		end();

	}

	protected void flushUnStartedChildren() throws BirtException {
		int flushSize = children.size() - 1;
		if (flushSize > 0) {
			for (int i = 0; i < flushSize; i++) {
				INode child = (INode) children.get(i);
				child.flush();
			}
			Object lastChild = children.get(flushSize);
			children.clear();
			children.add(lastChild);
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
		generator.start(content, isFirst);
		isStarted = true;
		flushUnStartedChildren();
	}

	public void addChild(INode node) {
		children.add(node);
	}

	public void removeChildren() {
		children.clear();
	}

}
