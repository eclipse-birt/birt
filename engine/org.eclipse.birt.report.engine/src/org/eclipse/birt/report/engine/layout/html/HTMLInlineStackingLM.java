/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;

public abstract class HTMLInlineStackingLM extends HTMLStackingLM {

	/**
	 * does the children has been intialized.
	 */
	protected boolean initializedChildren = false;
	/**
	 * all the inline children layouts
	 */
	protected List childrenLayouts = new ArrayList();
	/**
	 * children executor.
	 */
	protected List childrenExecutors = new ArrayList();

	/**
	 * the current finish status of all the chidren.
	 */
	protected List childrenFinished = new ArrayList();

	public HTMLInlineStackingLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	@Override
	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		super.initialize(parent, content, executor, emitter);
		initializedChildren = false;
	}

	@Override
	public void close() throws BirtException {
		childrenLayouts.clear();
		childrenExecutors.clear();
		childrenFinished.clear();
		super.close();
	}

	private void initalizeChildren() throws BirtException {
		IContent childContent = null;
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = (IReportItemExecutor) executor.getNextChild();
			childContent = childExecutor.execute();
			if (childContent == null) {
				childrenLayouts.add(null);
			} else {
				ILayoutManager childLayout = engine.createLayoutManager(this, childContent, childExecutor, emitter);
				childrenLayouts.add(childLayout);
			}
			childrenExecutors.add(childExecutor);
			childrenFinished.add(Boolean.FALSE);
		}
		if (childContent != null) {
			childContent.setLastChild(true);
		}
	}

	/**
	 * layout the children, return if it should create a new page after this layout.
	 *
	 * @return
	 */
	protected boolean resumeLayout() throws BirtException {
		boolean hasNextPage = false;
		int length = childrenLayouts.size();
		for (int i = 0; i < length; i++) {
			boolean childFinished = ((Boolean) childrenFinished.get(i)).booleanValue();
			if (!childFinished) {
				ILayoutManager childLayout = (ILayoutManager) childrenLayouts.get(i);
				if (childLayout != null) {
					boolean childHasNewPage = childLayout.layout();
					if (childHasNewPage) {
						hasNextPage = true;
					}
					childFinished = childLayout.isFinished();
				} else {
					childFinished = true;
				}
				if (childFinished) {
					if (childLayout != null) {
						childLayout.close();
					}
					IReportItemExecutor childExecutor = (IReportItemExecutor) childrenExecutors.get(i);
					childExecutor.close();
				}
				childrenFinished.set(i, Boolean.valueOf(childFinished));
			}
		}
		return hasNextPage;
	}

	@Override
	protected boolean isChildrenFinished() {
		int size = childrenLayouts.size();
		for (int i = 0; i < size; i++) {
			boolean childFinished = ((Boolean) childrenFinished.get(i)).booleanValue();
			if (!childFinished) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean layoutNodes() throws BirtException {
		if (!initializedChildren) {
			initializedChildren = true;
			initalizeChildren();
		}
		return resumeLayout();
	}

}
