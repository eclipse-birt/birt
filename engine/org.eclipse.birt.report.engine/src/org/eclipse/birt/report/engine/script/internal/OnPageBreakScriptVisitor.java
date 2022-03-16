/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal;

import java.util.Collection;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 *
 */

public class OnPageBreakScriptVisitor extends DefaultReportItemVisitorImpl {

	protected static Logger logger = Logger.getLogger(LocalizedContentVisitor.class.getName());

	private ExecutionContext context;
	private OnPageBreakExecutor executor;

	public OnPageBreakScriptVisitor(ExecutionContext context) {
		this.context = context;
		this.executor = new OnPageBreakExecutor();
	}

	public void onPageStart(Report report, PageContent pageContent, Collection<IContent> contents) {
		ReportScriptExecutor.handleOnPageStartScript(report, context, pageContent, contents);
	}

	public void onPageStart(PageContent pageContent, Collection<IContent> contents) {
		PageScriptExecutor.handleOnPageStartScript(context, pageContent, contents);
	}

	public void onPageEnd(Report report, PageContent pageContent, Collection<IContent> contents) {
		ReportScriptExecutor.handleOnPageEndScript(report, context, pageContent, contents);
	}

	public void onPageEnd(PageContent pageContent, Collection<IContent> contents) {
		PageScriptExecutor.handleOnPageEndScript(context, pageContent, contents);
	}

	public IContent onPageBreak(IContent content) {
		ReportItemDesign design = getGenerateDesign(content);
		if (design != null) {
			Object value = ((ReportItemDesign) design).accept(executor, content);
			return (IContent) value;
		}
		return content;
	}

	private ReportItemDesign getGenerateDesign(IContent content) {
		Object design = content.getGenerateBy();
		if (design instanceof ReportItemDesign) {
			return (ReportItemDesign) design;
		}
		if (design instanceof ReportItemHandle) {
			IReportContent reportContent = content.getReportContent();
			Report reportDesign = reportContent.getDesign();
			return reportDesign.findDesign((ReportItemHandle) design);
		}
		return null;
	}

	private class OnPageBreakExecutor extends DefaultReportItemVisitorImpl {

		@Override
		public Object visitAutoTextItem(AutoTextItemDesign autoText, Object value) {
			AutoTextScriptExecutor.handleOnPageBreak((IAutoTextContent) value, context);
			return value;
		}

		@Override
		public Object visitBand(BandDesign band, Object value) {
			return visitReportItem(band, value);
		}

		@Override
		public Object visitCell(CellDesign cell, Object value) {
			CellScriptExecutor.handleOnPageBreak((ICellContent) value, context);
			return value;
		}

		@Override
		public Object visitDataItem(DataItemDesign data, Object value) {
			DataItemScriptExecutor.handleOnPageBreak((IDataContent) value, context);
			return value;
		}

		@Override
		public Object visitExtendedItem(ExtendedItemDesign item, Object value) {
			return visitReportItem(item, value);
		}

		@Override
		public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
			return visitReportItem(container, value);
		}

		@Override
		public Object visitGridItem(GridItemDesign grid, Object value) {
			GridScriptExecutor.handleOnPageBreak((ITableContent) value, context);
			return value;
		}

		@Override
		public Object visitGroup(GroupDesign group, Object value) {
			return visitReportItem(group, value);
		}

		@Override
		public Object visitImageItem(ImageItemDesign image, Object value) {
			ImageScriptExecutor.handleOnPageBreak((IContent) value, context);
			return value;
		}

		@Override
		public Object visitLabelItem(LabelItemDesign label, Object value) {
			LabelScriptExecutor.handleOnPageBreak((ILabelContent) value, context);
			return value;
		}

		@Override
		public Object visitListBand(ListBandDesign band, Object value) {
			return visitReportItem(band, value);
		}

		@Override
		public Object visitListGroup(ListGroupDesign group, Object value) {
			ListGroupScriptExecutor.handleOnPageBreak((IListGroupContent) value, context);
			return value;
		}

		@Override
		public Object visitListItem(ListItemDesign list, Object value) {
			ListScriptExecutor.handleOnPageBreak((IListContent) value, context);
			return value;
		}

		@Override
		public Object visitListing(ListingDesign listing, Object value) {
			return visitReportItem(listing, value);
		}

		@Override
		public Object visitDynamicTextItem(DynamicTextItemDesign dynamicText, Object value) {
			DynamicTextScriptExecutor.handleOnPageBreak((IContent) value, context);
			return value;
		}

		@Override
		public Object visitRow(RowDesign row, Object value) {
			RowScriptExecutor.handleOnPageBreak((IRowContent) value, context);
			return value;
		}

		@Override
		public Object visitTableBand(TableBandDesign band, Object value) {
			return visitReportItem(band, value);
		}

		@Override
		public Object visitTableGroup(TableGroupDesign group, Object value) {
			TableGroupScriptExecutor.handleOnPageBreak((ITableGroupContent) value, context);
			return value;
		}

		@Override
		public Object visitTableItem(TableItemDesign table, Object value) {
			TableScriptExecutor.handleOnPageBreak((ITableContent) value, context);
			return value;
		}

		@Override
		public Object visitTemplate(TemplateDesign template, Object value) {
			TextItemScriptExecutor.handleOnPageBreak((IContent) value, context);
			return value;
		}

		@Override
		public Object visitTextItem(TextItemDesign text, Object value) {
			TextItemScriptExecutor.handleOnPageBreak((IContent) value, context);
			return value;
		}
	}
}
