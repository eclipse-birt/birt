/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.element.IDesignElement;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.DataItem;
import org.eclipse.birt.report.engine.script.internal.element.DesignElement;
import org.eclipse.birt.report.engine.script.internal.element.DynamicText;
import org.eclipse.birt.report.engine.script.internal.element.Grid;
import org.eclipse.birt.report.engine.script.internal.element.Image;
import org.eclipse.birt.report.engine.script.internal.element.Label;
import org.eclipse.birt.report.engine.script.internal.element.List;
import org.eclipse.birt.report.engine.script.internal.element.MasterPage;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.engine.script.internal.element.ReportElement;
import org.eclipse.birt.report.engine.script.internal.element.ReportItem;
import org.eclipse.birt.report.engine.script.internal.element.Table;
import org.eclipse.birt.report.engine.script.internal.element.TextItem;
import org.eclipse.birt.report.engine.script.internal.instance.CellInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DataItemInstance;
import org.eclipse.birt.report.engine.script.internal.instance.GridInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ImageInstance;
import org.eclipse.birt.report.engine.script.internal.instance.LabelInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ListInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ReportElementInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RowInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.engine.script.internal.instance.TableInstance;
import org.eclipse.birt.report.engine.script.internal.instance.TextItemInstance;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.simpleapi.IDataItem;
import org.eclipse.birt.report.model.api.simpleapi.IDynamicText;
import org.eclipse.birt.report.model.api.simpleapi.IGrid;
import org.eclipse.birt.report.model.api.simpleapi.IImage;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.api.simpleapi.IList;
import org.eclipse.birt.report.model.api.simpleapi.IMasterPage;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportElement;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.api.simpleapi.ITextItem;

public class ElementUtil {

	static InstanceBuilder instanceBuilder = new InstanceBuilder();

	public static class InstanceBuilder extends ContentVisitorAdapter {

		private RunningState runningState;

		@Override
		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		@Override
		public Object visitContent(IContent content, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new ReportElementInstance(content, context, runningState);
		}

		@Override
		public Object visitCell(ICellContent cell, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new CellInstance(cell, context, runningState, false);
		}

		@Override
		public Object visitData(IDataContent data, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new DataItemInstance(data, context, runningState);
		}

		@Override
		public Object visitForeign(IForeignContent foreign, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			if (IForeignContent.HTML_TYPE.equals(foreign.getRawType())
					|| IForeignContent.TEXT_TYPE.equals(foreign.getRawType())
					|| IForeignContent.TEMPLATE_TYPE.equals(foreign.getRawType())) {
				return new TextItemInstance(foreign, context, runningState);
			}
			return null;
		}

		@Override
		public Object visitImage(IImageContent image, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new ImageInstance(image, context, runningState);
		}

		@Override
		public Object visitLabel(ILabelContent label, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new LabelInstance(label, context, runningState);

		}

		@Override
		public Object visitList(IListContent list, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new ListInstance(list, context, runningState);
		}

		@Override
		public Object visitRow(IRowContent row, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new RowInstance(row, context, runningState);
		}

		@Override
		public Object visitTable(ITableContent table, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			Object genBy = table.getGenerateBy();
			if (genBy instanceof TableItemDesign) {
				return new TableInstance(table, context, runningState);
			} else if (genBy instanceof GridItemDesign) {
				return new GridInstance(table, context, runningState);
			}
			return null;
		}

		@Override
		public Object visitText(ITextContent text, Object value) {
			ExecutionContext context = (ExecutionContext) value;
			return new TextItemInstance(text, context, runningState);
		}

		public void setRunningState(RunningState runningState) {
			this.runningState = runningState;
		}
	}

	public static IReportElementInstance getInstance(IElement element, ExecutionContext context,
			RunningState runningState) throws BirtException {
		if (element == null) {
			return null;
		}

		if (element instanceof IContent) {
			instanceBuilder.setRunningState(runningState);
			return (IReportElementInstance) instanceBuilder.visit((IContent) element, context);
		}
		return null;
	}

	public static IDesignElement getElement(DesignElementHandle element) {
		if (element == null) {
			return null;
		}
		if (element instanceof ReportDesignHandle) {
			return new ReportDesign((ReportDesignHandle) element);
		}

		if (!(element instanceof ReportElementHandle)) {
			return null;
		}

		if (element instanceof DataItemHandle) {
			return new DataItem((DataItemHandle) element);
		}

		if (element instanceof GridHandle) {
			return new Grid((GridHandle) element);
		}

		if (element instanceof ImageHandle) {
			return new Image((ImageHandle) element);
		}

		if (element instanceof LabelHandle) {
			return new Label((LabelHandle) element);
		}

		if (element instanceof ListHandle) {
			return new List((ListHandle) element);
		}

		if (element instanceof TableHandle) {
			return new Table((TableHandle) element);
		}

		if (element instanceof TextDataHandle) {
			return new DynamicText((TextDataHandle) element);
		}

		if (element instanceof MasterPageHandle) {
			return new MasterPage((MasterPageHandle) element);
		}

		if (element instanceof TextItemHandle) {
			return new TextItem((TextItemHandle) element);
		}

		return new ReportElement((ReportElementHandle) element);

	}

	public static IDesignElement getElement(org.eclipse.birt.report.model.api.simpleapi.IDesignElement element) {
		if (element == null) {
			return null;
		}

		if (element instanceof IReportDesign) {
			return new ReportDesign((IReportDesign) element);
		}

		if (element instanceof IDataItem) {
			return new DataItem((IDataItem) element);
		}

		if (element instanceof IGrid) {
			return new Grid((IGrid) element);
		}

		if (element instanceof IImage) {
			return new Image((IImage) element);
		}

		if (element instanceof ILabel) {
			return new Label((ILabel) element);
		}

		if (element instanceof IList) {
			return new List((IList) element);
		}

		if (element instanceof ITable) {
			return new Table((ITable) element);
		}

		if (element instanceof IDynamicText) {
			return new DynamicText((IDynamicText) element);
		}

		if (element instanceof ITextItem) {
			return new TextItem((ITextItem) element);
		}

		if (element instanceof IMasterPage) {
			return new MasterPage((IMasterPage) element);
		}

		if (element instanceof IReportItem) {
			return new ReportItem((IReportItem) element);
		}

		if (element instanceof IReportElement) {
			return new ReportElement((IReportElement) element);
		}

		return new DesignElement(element);

	}

}
