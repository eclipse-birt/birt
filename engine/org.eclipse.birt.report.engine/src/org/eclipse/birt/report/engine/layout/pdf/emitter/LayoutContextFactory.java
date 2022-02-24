/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class LayoutContextFactory {

	private ContentVisitor visitor = new ContentVisitor();

	ContainerLayout layoutContext = null;

	ContainerLayout parent = null;

	private LayoutEngineContext context = null;

	protected IReportExecutor executor;

	public LayoutContextFactory(IReportExecutor executor, LayoutEngineContext context) {
		this.context = context;
		this.executor = executor;
	}

	public Layout createLayoutManager(ContainerLayout parent, IContent content) throws BirtException {
		this.parent = parent;
		if (content == null) {
			return new LineLayout(context, parent);
		} else {
			return (Layout) content.accept(visitor, null);
		}
	}

	private class ContentVisitor implements IContentVisitor {

		@Override
		public Object visit(IContent content, Object value) {
			return visitContent(content, value);
		}

		@Override
		public Object visitContent(IContent content, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(content);
			if (isInline) {
				return new InlineBlockLayout(context, parent, content);
			} else {
				return new BlockStackingLayout(context, parent, content);
			}
		}

		@Override
		public Object visitPage(IPageContent page, Object value) {
			return new PageLayout(executor, context, parent, page);
		}

		@Override
		public Object visitContainer(IContainerContent container, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(container);
			if (isInline) {
				return new InlineContainerLayout(context, parent, container);
			} else {
				return new BlockStackingLayout(context, parent, container);
			}
		}

		@Override
		public Object visitTable(ITableContent table, Object value) {
			return new TableLayout(context, parent, table);
		}

		@Override
		public Object visitTableBand(ITableBandContent tableBand, Object value) {
			return new TableBandLayout(context, parent, tableBand);
		}

		@Override
		public Object visitRow(IRowContent row, Object value) {
			return new RowLayout(context, parent, row);
		}

		@Override
		public Object visitCell(ICellContent cell, Object value) {
			return new CellLayout(context, parent, cell);
		}

		@Override
		public Object visitText(ITextContent text, Object value) {
			// FIXME
			return handleText(text);
		}

		@Override
		public Object visitLabel(ILabelContent label, Object value) {
			return handleText(label);
		}

		@Override
		public Object visitData(IDataContent data, Object value) {
			return handleText(data);
		}

		@Override
		public Object visitImage(IImageContent image, Object value) {
			return new ImageLayout(context, parent, image);
		}

		@Override
		public Object visitForeign(IForeignContent foreign, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(foreign);
			if (isInline) {
				return new InlineContainerLayout(context, parent, foreign);
			} else {
				return new BlockStackingLayout(context, parent, foreign);
			}
		}

		@Override
		public Object visitAutoText(IAutoTextContent autoText, Object value) {
			int type = autoText.getType();
			if (type == IAutoTextContent.TOTAL_PAGE || type == IAutoTextContent.UNFILTERED_TOTAL_PAGE) {
				context.addUnresolvedContent(autoText);
				return new TemplateLayout(context, parent, autoText);
			}
			return handleText(autoText);
		}

		private Object handleText(ITextContent content) {
			boolean isInline = parent instanceof IInlineStackingLayout;
			if (isInline) {
				return new InlineTextLayout(context, parent, content);
			} else {
				String text = content.getText();
				if (text == null || "".equals(text)) //$NON-NLS-1$
				{
					content.setText(" "); //$NON-NLS-1$
				}
				return new BlockTextLayout(context, parent, content);
			}
		}

		@Override
		public Object visitList(IListContent list, Object value) {
			return new ListLayout(context, parent, list);

		}

		@Override
		public Object visitListBand(IListBandContent listBand, Object value) {
			assert (false);
			return null;
			// return new PDFListBandLM(context, parent, listBand, emitter,
			// executor);

		}

		@Override
		public Object visitListGroup(IListGroupContent group, Object value) {
			return new ListGroupLayout(context, parent, group);
		}

		@Override
		public Object visitTableGroup(ITableGroupContent group, Object value) {
			return new TableGroupLayout(context, parent, group);
		}

		@Override
		public Object visitGroup(IGroupContent group, Object value) {
			return new BlockStackingLayout(context, parent, group);
		}

	}

}
