
package org.eclipse.birt.report.engine.layout.pdf;

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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.NumberFormatter;
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
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.content.LineStackingExecutor;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

import com.ibm.icu.util.ULocale;

public class PDFLayoutManagerFactory {

	private ContentVisitor visitor = new ContentVisitor();

	PDFAbstractLM layoutManager = null;

	PDFStackingLM parent = null;

	private PDFLayoutEngineContext context = null;

	protected IReportItemExecutor executor;

	public PDFLayoutManagerFactory(PDFLayoutEngineContext context) {
		this.context = context;
	}

	public PDFAbstractLM createLayoutManager(PDFStackingLM parent, IContent content, IReportItemExecutor executor)
			throws BirtException {
		this.parent = parent;
		this.executor = executor;
		if (executor instanceof LineStackingExecutor) {
			return new PDFLineAreaLM(context, parent, executor);
		}
		if (content != null) {
			return (PDFAbstractLM) content.accept(visitor, null);
		}
		assert (false);
		return null;
	}

	private class ContentVisitor implements IContentVisitor {

		public Object visit(IContent content, Object value) {
			return visitContent(content, value);
		}

		public Object visitContent(IContent content, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(content);
			if (isInline) {
				return new PDFTextInlineBlockLM(context, parent, content, executor);
			} else {
				return new PDFBlockContainerLM(context, parent, content, executor);
			}
		}

		public Object visitPage(IPageContent page, Object value) {
			assert (false);
			return null;
		}

		public Object visitContainer(IContainerContent container, Object value) {
			boolean isInline = PropertyUtil.isInlineElement(container);
			if (isInline) {
				return new PDFInlineContainerLM(context, parent, container, executor);
			} else {
				return new PDFBlockContainerLM(context, parent, container, executor);
			}
		}

		public Object visitTable(ITableContent table, Object value) {
			return new PDFTableLM(context, parent, table, executor);
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			return new PDFTableBandLM(context, parent, tableBand, executor);
		}

		public Object visitRow(IRowContent row, Object value) {
			return new PDFRowLM(context, parent, row, executor);
		}

		public Object visitCell(ICellContent cell, Object value) {
			return new PDFCellLM(context, parent, cell, executor);
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
			return new PDFImageLM(context, parent, image, executor);
		}

		public Object visitForeign(IForeignContent foreign, Object value) throws BirtException {
			if (IForeignContent.HTML_TYPE.equals(foreign.getRawType())) {
				// build content DOM tree for HTML text
				HTML2Content.html2Content(foreign);
				executor = new DOMReportItemExecutor(foreign);
				executor.execute();
				boolean isInline = PropertyUtil.isInlineElement(foreign);
				if (isInline) {
					return new PDFInlineContainerLM(context, parent, foreign, executor);
				} else {
					return new PDFBlockContainerLM(context, parent, foreign, executor);
				}
			}
			LabelContent label = (LabelContent) foreign.getReportContent().createLabelContent();
			return handleText(label);
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) {
			if (IAutoTextContent.PAGE_NUMBER == autoText.getType()) {
				if (parent instanceof PDFLineAreaLM) {
					String originalPageNumber = autoText.getText();
					DataFormatValue format = autoText.getComputedStyle().getDataFormat();
					NumberFormatter nf = null;
					if (format == null)
						nf = new NumberFormatter();
					else {
						String pattern = format.getNumberPattern();
						String locale = format.getNumberLocale();
						if (locale == null)
							nf = new NumberFormatter(pattern);
						else
							nf = new NumberFormatter(pattern, new ULocale(locale));
					}

					try {
						autoText.setText(nf.format(Integer.parseInt(originalPageNumber)));
					} catch (NumberFormatException nfe) {
						autoText.setText(originalPageNumber);
					}
				}
				return handleText(autoText);
			}
			return new PDFTemplateLM(context, parent, autoText, executor);
		}

		private Object handleText(ITextContent content) {
			boolean isInline = parent instanceof ILineStackingLayoutManager;
			if (isInline) {
				return new PDFTextLM(context, parent, content, executor);
				/*
				 * assert ( parent instanceof PDFLineAreaLM ); DimensionType width =
				 * content.getWidth( ); // if text contains line break or width is specified,
				 * this text // will be regard as a inline-block area if ( width != null ||
				 * text.indexOf( '\n' )>=0 ) { return new PDFTextInlineBlockLM( context, parent,
				 * content, executor ); } else { return new PDFTextLM( context, parent, content,
				 * executor ); }
				 */
			} else {
				String text = content.getText();
				if (text == null || "".equals(text)) //$NON-NLS-1$
				{
					content.setText(" "); //$NON-NLS-1$
				}
				return new PDFTextBlockContainerLM(context, parent, content, executor);
			}
		}

		public Object visitList(IListContent list, Object value) {
			return new PDFListLM(context, parent, list, executor);

		}

		public Object visitListBand(IListBandContent listBand, Object value) {
			assert (false);
			return null;
			// return new PDFListBandLM(context, parent, listBand, emitter,
			// executor);

		}

		public Object visitListGroup(IListGroupContent group, Object value) {
			return new PDFListGroupLM(context, parent, group, executor);
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) {
			return new PDFTableGroupLM(context, parent, group, executor);
		}

		public Object visitGroup(IGroupContent group, Object value) {
			return new PDFListGroupLM(context, parent, group, executor);
		}

	}

}
