/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.layout.html.buffer.IPageBuffer;

/**
 * Layout contents with repeatable header/bands
 */
public abstract class HTMLRepeatHeaderLM extends HTMLBlockStackingLM {

	boolean isHeaderRefined = false;
	boolean isFirstLayout = true;

	public HTMLRepeatHeaderLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	@Override
	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		super.initialize(parent, content, executor, emitter);
		isFirstLayout = true;
		isHeaderRefined = false;
	}

	@Override
	protected boolean layoutChildren() throws BirtException {
		if (!isFirstLayout && shouldRepeatHeader()) {
			repeatHeader();
		}
		isFirstLayout = false;
		return super.layoutChildren();
	}

	protected abstract IBandContent getHeader();

	protected abstract boolean shouldRepeatHeader();

	protected void repeatHeader() throws BirtException {
		IBandContent header = getHeader();
		if (header != null) {
			refineBandContent(header);
			// clean the layout extension
			cleanRepeatedLayoutExtension(header);
			boolean pageBreak = context.allowPageBreak();
			context.setAllowPageBreak(false);
			IPageBuffer buffer = context.getPageBufferManager();
			boolean isRepeated = buffer.isRepeated();
			buffer.setRepeated(true);
			engine.layout(this, header, emitter);
			buffer.setRepeated(isRepeated);
			context.setAllowPageBreak(pageBreak);
		}
	}

	private void refineBandContent(IBandContent content) {
		if (isHeaderRefined) {
			return;
		}

		Collection<?> children = content.getChildren();
		ArrayList<IContent> removed = new ArrayList<>();
		if (children != null) {
			Iterator<?> itr = children.iterator();
			while (itr.hasNext()) {
				Object object = itr.next();
				if (object instanceof IRowContent) {
					IRowContent rowContent = (IRowContent) object;
					RowDesign rowDesign = (RowDesign) rowContent.getGenerateBy();
					if (rowDesign != null && !rowDesign.getRepeatable() || !rowContent.isRepeatable()) {
						removed.add(rowContent);
					}
				}
			}
			children.removeAll(removed);
		}
		isHeaderRefined = true;
	}

	private void cleanRepeatedLayoutExtension(IContent content) {
		Collection<?> children = content.getChildren();
		if (children == null) {
			return;
		}
		Iterator<?> i = children.iterator();
		while (i.hasNext()) {
			IContent child = (IContent) i.next();
			child.setExtension(IContent.LAYOUT_EXTENSION, null);

			cleanRepeatedLayoutExtension(child);
		}

	}
}
