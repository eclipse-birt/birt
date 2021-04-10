/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class PDFLayoutEngineContext {

	protected PDFLayoutManagerFactory factory = null;

	protected StartVisitor start;

	protected EndVisitor end;

	protected String masterPage = null;

	protected boolean allowPageBreak = true;

	protected IContent unresolvedContent = null;

	protected PDFReportLayoutEngine engine;

	protected int maxWidth;

	protected int maxHeight;

	protected String format;

	protected boolean cancel = false;

	protected IReportContent report;

	// FIXME default value should be true;
	protected boolean autoPageBreak = false;

	protected Locale locale;

	protected ArrayList hints = new ArrayList();

	protected ArrayList columnHints = new ArrayList();

	protected long pageNumber = 1;

	protected long pageCount = 1;

	/**
	 * whether emitter need to output the display:none or process it in layout
	 * engine. true: output display:none in emitter and do not process it in layout
	 * engine. false: process it in layout engine, not output it in emitter.
	 */
	protected boolean outputDisplayNone = false;

	public void setLayoutPageHint(IPageHint pageHint) {
		if (pageHint != null) {
			hints.clear();
			columnHints.clear();
			this.pageNumber = pageHint.getPageNumber();
			this.masterPage = pageHint.getMasterPage();
			int count = pageHint.getUnresolvedRowCount();
			for (int i = 0; i < count; i++) {
				hints.add(pageHint.getUnresolvedRowHint(i));
			}
			count = pageHint.getTableColumnHintCount();
			for (int i = 0; i < count; i++) {
				columnHints.add(pageHint.getTableColumnHint(i));
			}
		}
	}

	public TableColumnHint getTableColumnHint(String tableId) {
		if (columnHints.size() > 0) {
			Iterator iter = columnHints.iterator();
			while (iter.hasNext()) {
				TableColumnHint hint = (TableColumnHint) iter.next();
				if (tableId.equals(hint.getTableId())) {
					return hint;
				}
			}
		}
		return null;
	}

	public UnresolvedRowHint getUnresolvedRowHint(ITableContent table) {
		if (hints.size() > 0) {
			String idStr = table.getInstanceID().toUniqueString();
			Iterator iter = hints.iterator();
			while (iter.hasNext()) {
				UnresolvedRowHint rowHint = (UnresolvedRowHint) iter.next();
				if (idStr.equals(rowHint.getTableId())) {
					return rowHint;
				}
			}
		}
		return null;
	}

	public void addUnresolvedRowHints(Collection hints) {
		this.hints.addAll(hints);
	}

	public void setAutoPageBreak(boolean autoBreak) {
		this.autoPageBreak = autoBreak;
	}

	public boolean isAutoPageBreak() {
		return autoPageBreak;
	}

	public IReportContent getReport() {
		return report;
	}

	public void setReport(IReportContent report) {
		this.report = report;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isCancel() {
		return this.cancel;
	}

	public String getMasterPage() {
		return masterPage;
	}

	public void setMasterPage(String masterPage) {
		this.masterPage = masterPage;
	}

	public PDFLayoutEngineContext(PDFReportLayoutEngine engine) {
		this.engine = engine;
		start = new StartVisitor();
		end = new EndVisitor();
	}

	public void setFactory(PDFLayoutManagerFactory factory) {
		this.factory = factory;
	}

	public IContentVisitor getStart() {
		return start;
	}

	public IContentVisitor getEnd() {
		return end;
	}

	public PDFLayoutManagerFactory getFactory() {
		return factory;
	}

	private static class StartVisitor implements IContentVisitor {

		public Object visit(IContent content, Object value) throws BirtException {

			((IContentEmitter) value).startContent(content);
			return null;

		}

		public Object visitContent(IContent content, Object value) throws BirtException {
			((IContentEmitter) value).startContent(content);
			return null;
		}

		public Object visitPage(IPageContent page, Object value) throws BirtException {
			((IContentEmitter) value).startPage(page);
			return null;
		}

		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			((IContentEmitter) value).startContainer(container);
			return null;
		}

		public Object visitTable(ITableContent table, Object value) throws BirtException {
			((IContentEmitter) value).startTable(table);
			return null;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			// ((IContentEmitter)value).startTableBand(tableBand);
			return null;
		}

		public Object visitRow(IRowContent row, Object value) throws BirtException {
			((IContentEmitter) value).startRow(row);
			return null;
		}

		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			((IContentEmitter) value).startCell(cell);
			return null;
		}

		public Object visitText(ITextContent text, Object value) throws BirtException {
			((IContentEmitter) value).startText(text);
			return null;
		}

		public Object visitLabel(ILabelContent label, Object value) throws BirtException {
			((IContentEmitter) value).startLabel(label);
			return null;
		}

		public Object visitData(IDataContent data, Object value) throws BirtException {
			((IContentEmitter) value).startData(data);
			return null;
		}

		public Object visitImage(IImageContent image, Object value) throws BirtException {
			((IContentEmitter) value).startImage(image);
			return null;
		}

		public Object visitForeign(IForeignContent foreign, Object value) throws BirtException {
			((IContentEmitter) value).startForeign(foreign);
			return null;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
			((IContentEmitter) value).startAutoText(autoText);
			return null;
		}

		public Object visitList(IListContent list, Object value) throws BirtException {
			((IContentEmitter) value).startList(list);
			return null;

		}

		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			// ((IContentEmitter)value).startListBand( listBand );
			return null;
		}

		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			// ((IContentEmitter)value).startGroup( group );
			return null;
		}

		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			// ((IContentEmitter)value).startListGroup( group );
			return null;
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			// ((IContentEmitter)value).startTableGroup( group );
			return null;
		}

	}

	private static class EndVisitor implements IContentVisitor {

		public Object visit(IContent content, Object value) throws BirtException {
			((IContentEmitter) value).endContent(content);
			return null;
		}

		public Object visitContent(IContent content, Object value) throws BirtException {
			((IContentEmitter) value).endContent(content);
			return null;
		}

		public Object visitPage(IPageContent page, Object value) throws BirtException {
			((IContentEmitter) value).endPage(page);
			return null;
		}

		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			((IContentEmitter) value).endContainer(container);
			return null;
		}

		public Object visitTable(ITableContent table, Object value) throws BirtException {
			((IContentEmitter) value).endTable(table);
			return null;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			// ((IContentEmitter)value).endTableBand(tableBand);
			return null;
		}

		public Object visitRow(IRowContent row, Object value) throws BirtException {
			((IContentEmitter) value).endRow(row);
			return null;
		}

		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			((IContentEmitter) value).endCell(cell);
			return null;
		}

		public Object visitText(ITextContent text, Object value) throws BirtException {
			return null;
		}

		public Object visitLabel(ILabelContent label, Object value) throws BirtException {
			return null;
		}

		public Object visitData(IDataContent data, Object value) throws BirtException {
			return null;
		}

		public Object visitImage(IImageContent image, Object value) throws BirtException {
			return null;
		}

		public Object visitForeign(IForeignContent foreign, Object value) throws BirtException {
			return null;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
			return null;
		}

		public Object visitList(IListContent list, Object value) throws BirtException {
			((IContentEmitter) value).endList(list);
			return null;
		}

		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			// ((IContentEmitter)value).endListBand( listBand );
			return null;
		}

		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			// ((IContentEmitter)value).endGroup( group );
			return null;
		}

		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			// ((IContentEmitter)value).endListGroup(group) ;
			return null;
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			// ((IContentEmitter)value).endTableGroup(group) ;
			return null;
		}
	}

	public boolean allowPageBreak() {
		return this.allowPageBreak;
	}

	public void setAllowPageBreak(boolean allowPageBreak) {
		this.allowPageBreak = allowPageBreak;
	}

	public PDFReportLayoutEngine getLayoutEngine() {
		return this.engine;
	}

	public void addUnresolvedContent(IContent content) {
		this.unresolvedContent = content;
	}

	public IContent getUnresolvedContent() {
		return unresolvedContent;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxHeight(int height) {
		this.maxHeight = height;
	}

	public void setMaxWidth(int width) {
		this.maxWidth = width;
	}

	protected boolean fitToPage = false;

	public void setFitToPage(boolean fitToPage) {
		this.fitToPage = fitToPage;
	}

	public boolean fitToPage() {
		return this.fitToPage;
	}

	protected boolean pageBreakPaginationOnly = false;

	public void setPagebreakPaginationOnly(boolean pageBreakPaginationOnly) {
		this.pageBreakPaginationOnly = pageBreakPaginationOnly;
	}

	public boolean pagebreakPaginationOnly() {
		return this.pageBreakPaginationOnly;
	}

	protected int pageOverflow = IPDFRenderOption.CLIP_CONTENT;

	public int getPageOverflow() {
		return this.pageOverflow;
	}

	public void setPageOverflow(int pageOverflow) {
		this.pageOverflow = pageOverflow;
	}

	protected int preferenceWidth = 0;

	public void setPreferenceWidth(int preferenceWidth) {
		this.preferenceWidth = preferenceWidth;
	}

	public int getPreferenceWidth() {
		return this.preferenceWidth;
	}

	protected boolean textWrapping = true;

	public void setTextWrapping(boolean textWrapping) {
		this.textWrapping = textWrapping;
	}

	public boolean getTextWrapping() {
		return this.textWrapping;
	}

	protected boolean fontSubstitution = true;

	public void setFontSubstitution(boolean fontSubstitution) {
		this.fontSubstitution = fontSubstitution;
	}

	public boolean getFontSubstitution() {
		return this.fontSubstitution;
	}

	protected boolean bidiProcessing = true;

	public void setBidiProcessing(boolean bidiProcessing) {
		this.bidiProcessing = bidiProcessing;
	}

	public boolean getBidiProcessing() {
		return this.bidiProcessing;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setOutputDisplayNone(boolean outputDisplayNone) {
		this.outputDisplayNone = outputDisplayNone;
	}

	public boolean getOutputDisplayNone() {
		return outputDisplayNone;
	}

	private FontMappingManager fontManager;

	public FontMappingManager getFontManager() {
		if (fontManager == null) {
			fontManager = FontMappingManagerFactory.getInstance().getFontMappingManager(format, locale);
		}
		return fontManager;
	}

	public long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
}
