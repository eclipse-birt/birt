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

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;

/**
 * a buffered report emitter that allows content objects from the engine to be
 * buffered before output to a specific format. Buffering is needed sometimes,
 * for handling drop, table, etc.
 *
 */
public class BufferedReportEmitter extends ContentEmitterAdapter {

	LinkedList events = new LinkedList();

	/**
	 * refers to the non-buffered emitter
	 */
	protected IContentEmitter emitter;

	/**
	 * constructor
	 *
	 * @param emitter
	 */
	public BufferedReportEmitter(IContentEmitter emitter) {
		this.emitter = emitter;
	}

	public boolean isEmpty() {
		return events.isEmpty();
	}

	public void flush() throws BirtException {
		if (emitter instanceof BufferedReportEmitter) {
			((BufferedReportEmitter) emitter).events.addAll(events);
		} else {
			Iterator eventIter = events.iterator();
			while (eventIter.hasNext()) {
				BufferedNode node = (BufferedNode) eventIter.next();
				if (node.start) {
					ContentEmitterUtil.startContent(node.content, emitter);
				} else {
					ContentEmitterUtil.endContent(node.content, emitter);
				}
			}
		}
		events.clear();
	}

	@Override
	public void startContent(IContent content) {
		events.add(new BufferedNode(content, true));
	}

	@Override
	public void endContent(IContent content) {
		events.add(new BufferedNode(content, false));
	}

	public static class BufferedNode {

		boolean start;
		/**
		 * The content object stored in this node
		 */
		protected IContent content;

		/**
		 * @param item the content object
		 */
		BufferedNode(IContent content, boolean start) {
			this.content = content;
			this.start = start;
		}

		/**
		 * @return Returns the content object stored in this node
		 */
		public IContent getContent() {
			return content;
		}

		public boolean isStart() {
			return start;
		}
	}
}
