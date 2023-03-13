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

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.pdf.cache.TableAreaLayout;

public class PDFTableRegionLM extends PDFTableLM implements IBlockStackingLayoutManager

{
	protected int groupLevel = 0;

	public PDFTableRegionLM(PDFLayoutEngineContext context, IContent content, TableLayoutInfo layoutInfo,
			TableAreaLayout regionLayout) {
		super(context, null, content, null);
		this.layoutInfo = layoutInfo;
		this.layout = regionLayout;
	}

	@Override
	protected int getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	public void initialize(ITableBandContent content) throws BirtException {
		this.executor = new DOMReportItemExecutor(content);
		this.executor.execute();
		status = STATUS_START;
	}

	@Override
	protected void initialize() {
		super.initialize();
		maxAvaHeight = getAvaHeight();
		// this.layout.setUnresolvedRow( lastRow );
	}

	protected int getAvaHeight() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected void buildTableLayoutInfo() {

	}

	@Override
	protected void closeLayout() {
		// FIXME
		if (root == null) {
			return;
		}

		root.setHeight(getCurrentBP() + getOffsetY());

	}

	@Override
	protected IReportItemExecutor createExecutor() {
		return this.executor;
	}

	@Override
	protected void repeat() {

	}
}
