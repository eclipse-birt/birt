/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
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
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;

/**
 *
 */

public class OnCreateScriptVisitor extends DefaultReportItemVisitorImpl {

	protected static Logger logger = Logger.getLogger(LocalizedContentVisitor.class.getName());

	private ExecutionContext context;

	public OnCreateScriptVisitor(ExecutionContext context) {
		this.context = context;
	}

	public IContent onCreate(IContent content) {
		Object design = content.getGenerateBy();
		assert design instanceof ReportItemDesign;
		Object value = ((ReportItemDesign) design).accept(this, content);
		return (IContent) value;
	}

	@Override
	public Object visitAutoTextItem(AutoTextItemDesign autoText, Object value) {
		AutoTextScriptExecutor.handleOnCreate((IAutoTextContent) value, context);
		return value;
	}

	@Override
	public Object visitBand(BandDesign band, Object value) {
		return visitReportItem(band, value);
	}

	@Override
	public Object visitCell(CellDesign cell, Object value) {
		CellScriptExecutor.handleOnCreate((ICellContent) value, context, true);
		return value;
	}

	@Override
	public Object visitDataItem(DataItemDesign data, Object value) {
		DataItemScriptExecutor.handleOnCreate((IDataContent) value, context);
		return value;
	}

	@Override
	public Object visitExtendedItem(ExtendedItemDesign item, Object value) {
		ExtendedItemScriptExecutor.handleOnCreate(item, (IContent) value, context);
		return value;
	}

	@Override
	public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
		return visitReportItem(container, value);
	}

	@Override
	public Object visitGridItem(GridItemDesign grid, Object value) {
		GridScriptExecutor.handleOnCreate((ITableContent) value, context);
		return value;
	}

	@Override
	public Object visitGroup(GroupDesign group, Object value) {
		return visitReportItem(group, value);
	}

	@Override
	public Object visitImageItem(ImageItemDesign image, Object value) {
		ImageScriptExecutor.handleOnCreate((IContent) value, context);
		return value;
	}

	@Override
	public Object visitLabelItem(LabelItemDesign label, Object value) {
		LabelScriptExecutor.handleOnCreate((ILabelContent) value, context);
		return value;
	}

	@Override
	public Object visitListBand(ListBandDesign band, Object value) {
		return visitReportItem(band, value);
	}

	@Override
	public Object visitListGroup(ListGroupDesign group, Object value) {
		ListGroupScriptExecutor.handleOnCreate((IListGroupContent) value, context);
		return value;
	}

	@Override
	public Object visitListItem(ListItemDesign list, Object value) {
		ListScriptExecutor.handleOnCreate((IListContent) value, context);
		return value;
	}

	@Override
	public Object visitListing(ListingDesign listing, Object value) {
		return visitReportItem(listing, value);
	}

	@Override
	public Object visitDynamicTextItem(DynamicTextItemDesign dynamicText, Object value) {
		DynamicTextScriptExecutor.handleOnCreate((IContent) value, context);
		return value;
	}

	@Override
	public Object visitRow(RowDesign row, Object value) {
		RowScriptExecutor.handleOnCreate((IRowContent) value, context);
		return value;
	}

	@Override
	public Object visitTableBand(TableBandDesign band, Object value) {
		return visitReportItem(band, value);
	}

	@Override
	public Object visitTableGroup(TableGroupDesign group, Object value) {
		TableGroupScriptExecutor.handleOnCreate((ITableGroupContent) value, context);
		return value;
	}

	@Override
	public Object visitTableItem(TableItemDesign table, Object value) {
		TableScriptExecutor.handleOnCreate((ITableContent) value, context);
		return value;
	}

	@Override
	public Object visitTemplate(TemplateDesign template, Object value) {
		TextItemScriptExecutor.handleOnCreate((IContent) value, context);
		return value;
	}

	@Override
	public Object visitTextItem(TextItemDesign text, Object value) {
		TextItemScriptExecutor.handleOnCreate((IContent) value, context);
		return value;
	}

}
