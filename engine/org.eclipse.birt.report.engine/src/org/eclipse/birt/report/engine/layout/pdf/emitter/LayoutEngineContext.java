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

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;

public class LayoutEngineContext {

	protected int maxWidth;

	protected int maxHeight;

	protected String format;

	protected IReportContent report;

	protected IContent unresolvedContent;

	protected Locale locale;

	protected long totalPage = 0;
	protected long pageCount = 0;
	protected long pageNumber = 0;

	protected boolean autoPageBreak = true;

	protected LayoutEmitterAdapter emitter;

	protected boolean isFinished;

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public void setEmitter(LayoutEmitterAdapter emitter) {
		this.emitter = emitter;
	}

	public void setAutoPageBreak(boolean autoPageBreak) {
		this.autoPageBreak = autoPageBreak;
	}

	public void addUnresolvedContent(IContent content) {
		this.unresolvedContent = content;
	}

	public IContent getUnresolvedContent() {
		return unresolvedContent;
	}

	public IReportContent getReport() {
		return report;
	}

	public void setReport(IReportContent report) {
		this.report = report;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
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
		setAutoPageBreak(!pageBreakPaginationOnly);
	}

	public boolean pagebreakPaginationOnly() {
		return this.pageBreakPaginationOnly;
	}

	protected int pageOverflow = IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES;

	public int getPageOverflow() {
		return this.pageOverflow;
	}

	public void setPageOverflow(int pageOverflow) {
		this.pageOverflow = pageOverflow;
		if (pageOverflow != IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
			autoPageBreak = false;
		}
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

	protected boolean enableWordbreak = false;

	public boolean isEnableWordbreak() {
		return enableWordbreak;
	}

	public void setEnableWordbreak(boolean enableWordbreak) {
		this.enableWordbreak = enableWordbreak;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private FontMappingManager fontManager;

	public FontMappingManager getFontManager() {
		if (fontManager == null) {
			fontManager = FontMappingManagerFactory.getInstance().getFontMappingManager(format, locale);
		}
		return fontManager;
	}

	// the dpi used to calculate image size.
	private int dpi = 0;

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

	private int totalPageTemplateWidth;

	public void setTotalPageTemplateWidth(int totalPageTemplateWidth) {
		this.totalPageTemplateWidth = totalPageTemplateWidth;
	}

	public int getTotalPageTemplateWidth() {
		return this.totalPageTemplateWidth;
	}

	private HashMap cachedTableHeaders = null;
	private HashMap cachedGroupHeaders = null;

	public void setCachedHeaderMap(HashMap tableHeaders, HashMap groupHeaders) {
		this.cachedTableHeaders = tableHeaders;
		this.cachedGroupHeaders = groupHeaders;
	}

	protected ITableBandContent getWrappedTableHeader(InstanceID id) {
		if (null != cachedTableHeaders) {
			Object cachedHeaders = cachedTableHeaders.get(id);
			if (cachedHeaders != null) {
				return (ITableBandContent) cachedHeaders;
			}
		}
		return null;
	}

	protected ITableBandContent getWrappedGroupHeader(InstanceID id) {
		if (null != cachedGroupHeaders) {
			Object cachedHeaders = cachedGroupHeaders.get(id);
			if (cachedHeaders != null) {
				return (ITableBandContent) cachedHeaders;
			}
		}
		return null;
	}
}
