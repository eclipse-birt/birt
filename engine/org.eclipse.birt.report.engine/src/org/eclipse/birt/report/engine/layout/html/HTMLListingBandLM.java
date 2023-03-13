/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.Collection;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;

public class HTMLListingBandLM extends HTMLBlockStackingLM {
	protected boolean needSoftPageBreak = false;

	public HTMLListingBandLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	@Override
	public int getType() {
		return LAYOUT_MANAGER_LIST_BAND;
	}

	boolean repeatHeader;

	@Override
	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		super.initialize(parent, content, executor, emitter);
		needSoftPageBreak = false;
		repeatHeader = false;
		intializeHeaderContent();
	}

	@Override
	public void close() throws BirtException {
		if (repeatHeader) {
			assert executor instanceof DOMReportItemExecutor;
			executor.close();
		}
		super.close();
	}

	private void intializeHeaderContent() throws BirtException {
		assert content != null;
		IElement pContent = content.getParent();
		assert pContent != null;
		assert content instanceof IBandContent;

		int type = ((IBandContent) content).getBandType();
		repeatHeader = false;
		if (type == IBandContent.BAND_HEADER || type == IBandContent.BAND_GROUP_HEADER) {
			if (pContent instanceof IGroupContent) {
				IGroupContent groupContent = (IGroupContent) pContent;
				repeatHeader = groupContent.isHeaderRepeat();
			} else if (pContent instanceof IListContent) {
				IListContent list = (IListContent) pContent;
				repeatHeader = list.isHeaderRepeat();
			} else if (pContent instanceof ITableContent) {
				ITableContent table = (ITableContent) pContent;
				repeatHeader = table.isHeaderRepeat();
			}
		}

		if (repeatHeader) {
			Collection children = content.getChildren();
			if (children == null || children.isEmpty()) {
				// fill the contents
				execute(content, executor);
				if (!pContent.getChildren().contains(content)) {
					pContent.getChildren().add(content);
				}
			}
			executor = new DOMReportItemExecutor(content);
			executor.execute();
		}
	}

	@Override
	protected boolean allowPageBreak() {
		IBandContent band = (IBandContent) content;
		int type = band.getBandType();
		if (type == IBandContent.BAND_HEADER) {
			if (IStyle.SOFT_VALUE.equals(content.getStyle().getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE)) || IStyle.SOFT_VALUE.equals(content.getStyle().getProperty(IStyle.STYLE_PAGE_BREAK_AFTER))) {
				return true;
			}
			IElement listContent = band.getParent();
			if (listContent instanceof IListContent) {
				return !((IListContent) listContent).isHeaderRepeat();
			}
			if (listContent instanceof ITableContent) {
				return !((ITableContent) listContent).isHeaderRepeat();
			}
		} else if (type == IBandContent.BAND_GROUP_HEADER) {
			if (IStyle.SOFT_VALUE.equals(content.getStyle().getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE)) || IStyle.SOFT_VALUE.equals(content.getStyle().getProperty(IStyle.STYLE_PAGE_BREAK_AFTER))) {
				return true;
			}
			IElement groupContent = band.getParent();
			if (groupContent instanceof IGroupContent) {
				return !((IGroupContent) groupContent).isHeaderRepeat();
			}
		}
		return true;
	}

	@Override
	protected boolean needPageBreakBefore() {
		if (super.needPageBreakBefore()) {
			needSoftPageBreak = true;
		}
		return false;
	}

}
