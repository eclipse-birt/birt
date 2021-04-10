/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

		public Object visit(IContent content, Object value) {
			return visitContent(content, value);
		}

		public Object visitContent(IContent content, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(content);
			if (isInline) {
				return new InlineBlockLayout(context, parent, content);
			} else {
				return new BlockStackingLayout(context, parent, content);
			}
		}

		public Object visitPage(IPageContent page, Object value) {
			return new PageLayout(executor, context, parent, page);
		}

		public Object visitContainer(IContainerContent container, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(container);
			if (isInline) {
				return new InlineContainerLayout(context, parent, container);
			} else {
				return new BlockStackingLayout(context, parent, container);
			}
		}

		public Object visitTable(ITableContent table, Object value) {
			return new TableLayout(context, parent, table);
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) {
			return new TableBandLayout(context, parent, tableBand);
		}

		public Object visitRow(IRowContent row, Object value) {
			return new RowLayout(context, parent, row);
		}

		public Object visitCell(ICellContent cell, Object value) {
			return new CellLayout(context, parent, cell);
		}

		public Object visitText(ITextContent text, Object value) {
			// FIXME
			return handleText(text);
		}

		public Object visitLabel(ILabelContent label, Object value) {
			return handleText(label);
		}

		public Object visitData(IDataContent data, Object value) {
			return handleText(data);
		}

		public Object visitImage(IImageContent image, Object value) {
			return new ImageLayout(context, parent, image);
		}

		public Object visitForeign(IForeignContent foreign, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(foreign);
			if (isInline) {
				return new InlineContainerLayout(context, parent, foreign);
			} else {
				return new BlockStackingLayout(context, parent, foreign);
			}
		}

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

		public Object visitList(IListContent list, Object value) {
			return new ListLayout(context, parent, list);

		}

		public Object visitListBand(IListBandContent listBand, Object value) {
			assert (false);
			return null;
			// return new PDFListBandLM(context, parent, listBand, emitter,
			// executor);

		}

		public Object visitListGroup(IListGroupContent group, Object value) {
			return new ListGroupLayout(context, parent, group);
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) {
			return new TableGroupLayout(context, parent, group);
		}

		public Object visitGroup(IGroupContent group, Object value) {
			return new BlockStackingLayout(context, parent, group);
		}

	}

}
