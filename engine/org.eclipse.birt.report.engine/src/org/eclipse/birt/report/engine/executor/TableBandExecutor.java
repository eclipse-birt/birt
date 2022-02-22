/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.w3c.dom.css.CSSValue;

public class TableBandExecutor extends StyledItemExecutor {

	protected TableBandExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TABLEBANDITEM);
	}

	@Override
	public IContent execute() {
		// start table band
		TableBandDesign bandDesign = (TableBandDesign) getDesign();
		ITableBandContent bandContent = report.createTableBandContent();
		setContent(bandContent);

		restoreResultSet();

		initializeContent(bandDesign, bandContent);
		startTOCEntry(bandContent);
		handlePageBreakInterval();
		// prepare to execute the row in the band
		currentRow = 0;

		context.getProgressMonitor().onProgress(IProgressMonitor.FETCH_ROW, tableExecutor.rowId);

		return content;
	}

	protected void handlePageBreakInterval() {
		if (tableExecutor.breakOnDetailBand) {
			BandDesign band = (BandDesign) design;
			if (band.getBandType() == BandDesign.BAND_DETAIL) {
				if (tableExecutor.softBreakBefore) {
					IStyle style = content.getStyle();
					if (style != null) {
						CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
						if (pageBreak == null || IStyle.AUTO_VALUE.equals(pageBreak)) {
							style.setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.SOFT_VALUE);
						}
					}
					tableExecutor.softBreakBefore = false;
					tableExecutor.addAfterBreak = true;
					tableExecutor.pageRowCount = 0;
				}
				tableExecutor.next();
				if (tableExecutor.needSoftBreakAfter()) {
					tableExecutor.softBreakBefore = true;
				}
			}
			if (band.getBandType() == BandDesign.GROUP_HEADER) {
				if (tableExecutor.softBreakBefore) {
					IStyle style = content.getStyle();
					if (style != null) {
						CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
						if (pageBreak == null || IStyle.AUTO_VALUE.equals(pageBreak)) {
							style.setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.SOFT_VALUE);
						}
					}
					tableExecutor.softBreakBefore = false;
					tableExecutor.addAfterBreak = false;
					tableExecutor.pageRowCount = 0;
				}
			}
		}
	}

	@Override
	public void close() throws BirtException {
		finishTOCEntry();
		super.close();
	}

	int currentRow;

	@Override
	public boolean hasNextChild() {
		TableBandDesign bandDesign = (TableBandDesign) getDesign();
		return currentRow < bandDesign.getRowCount();
	}

	@Override
	public IReportItemExecutor getNextChild() {
		TableBandDesign bandDesign = (TableBandDesign) getDesign();
		// TableItemExecutor tableExecutor = (TableItemExecutor) getParent( );

		if (currentRow < bandDesign.getRowCount()) {
			RowDesign rowDesign = bandDesign.getRow(currentRow++);
			ReportItemExecutor childExecutor = manager.createExecutor(this, rowDesign);
			if (childExecutor instanceof RowExecutor) {
				RowExecutor rowExecutor = (RowExecutor) childExecutor;
				rowExecutor.setRowId(tableExecutor.rowId++);
			} else {
				tableExecutor.rowId++;
			}
			return childExecutor;
		}
		return null;
	}

	TableItemExecutor tableExecutor;

	void setTableExecutor(TableItemExecutor tableExecutor) {
		this.tableExecutor = tableExecutor;
	}
}
