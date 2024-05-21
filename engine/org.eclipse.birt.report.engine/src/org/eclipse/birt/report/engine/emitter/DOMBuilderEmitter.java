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

package org.eclipse.birt.report.engine.emitter;

import java.util.Collection;

import org.eclipse.birt.report.engine.content.IContent;

/**
 * receive the input and construct the DOM strcuture of the received contents.
 *
 */
public class DOMBuilderEmitter extends ContentEmitterAdapter {

	protected IContent root;
	protected IContent parent;

	/**
	 * the following contnet will be add under the root content.
	 *
	 * @param root root content.
	 */
	public DOMBuilderEmitter(IContent root) {
		this.root = root;
		this.parent = null;
	}

	@Override
	public void startContent(IContent content) {
		if (parent != null) {
			Collection<IContent> children = parent.getChildren();
			if (!children.contains(content)) {
				children.add(content);
			}
			content.setParent(parent);

		} else {
			Collection<IContent> children = root.getChildren();
			if (!children.contains(content)) {
				children.add(content);
			}
			content.setParent(root);
		}
		parent = content;
	}

	@Override
	public void endContent(IContent content) {
		if (parent != null) {
			parent = (IContent) parent.getParent();
		}
	}
}
