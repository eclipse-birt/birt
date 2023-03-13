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

import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;

public class PDFLayoutEmitterProxy extends LayoutEmitterAdapter implements IContentEmitter {

	private LayoutEmitterAdapter layoutEmitterImpl = null;
	private IContentEmitter outputEmitter = null;
	private IReportExecutor executor;
	private LayoutEngineContext context;

	public PDFLayoutEmitterProxy(IReportExecutor executor, IContentEmitter emitter, IRenderOption renderOptions,
			Locale locale, long totalPage) {
		this.executor = executor;
		this.outputEmitter = emitter;

		context = new LayoutEngineContext();
		setupLayoutOptions(renderOptions);
		if (renderOptions != null) {
			String format = renderOptions.getOutputFormat();
			context.setFormat(format);
		}
		context.setLocale(locale);
		context.totalPage = totalPage;
		createLayoutEmitterImpl(context);
		context.setEmitter(layoutEmitterImpl);
	}

	@Override
	public void initialize(IEmitterServices service) throws BirtException {
		layoutEmitterImpl.initialize(service);
	}

	private void createLayoutEmitterImpl(LayoutEngineContext context) {
		if (context.autoPageBreak) {
			layoutEmitterImpl = new WrappedPDFLayoutEmitter(executor, outputEmitter, context);
		} else {
			layoutEmitterImpl = new PDFLayoutEmitter(executor, outputEmitter, context);
		}
	}

	@Override
	public String getOutputFormat() {
		return layoutEmitterImpl.getOutputFormat();
	}

	@Override
	public void setPageHandler(ILayoutPageHandler pageHandler) {
		layoutEmitterImpl.setPageHandler(pageHandler);
	}

	protected void setupLayoutOptions(IRenderOption renderOptions) {
		Map options = null;
		if (renderOptions != null) {
			options = renderOptions.getOptions();
		}
		if (options != null) {
			Object fitToPage = options.get(IPDFRenderOption.FIT_TO_PAGE);
			if (fitToPage instanceof Boolean) {
				if (((Boolean) fitToPage).booleanValue()) {
					context.setFitToPage(true);
				}
			}
			Object pageBreakOnly = options.get(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY);
			if (pageBreakOnly instanceof Boolean) {
				if (((Boolean) pageBreakOnly).booleanValue()) {
					context.setPagebreakPaginationOnly(true);
				}
			}
			Object pageOverflow = options.get(IPDFRenderOption.PAGE_OVERFLOW);
			if (pageOverflow instanceof Integer) {
				int pageOverflowType = ((Integer) pageOverflow).intValue();
				context.setPageOverflow(pageOverflowType);
				if (pageOverflowType == IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
					context.setPagebreakPaginationOnly(false);
				} else {
					context.setPagebreakPaginationOnly(true);
				}
			} else if (context.fitToPage()) {
				context.setPageOverflow(IPDFRenderOption.FIT_TO_PAGE_SIZE);
				context.setPagebreakPaginationOnly(true);
			}

			Object textWrapping = options.get(IPDFRenderOption.PDF_TEXT_WRAPPING);
			if (textWrapping instanceof Boolean) {
				if (!((Boolean) textWrapping).booleanValue()) {
					context.setTextWrapping(false);
				}
			}
			Object fontSubstitution = options.get(IPDFRenderOption.PDF_FONT_SUBSTITUTION);
			if (fontSubstitution instanceof Boolean) {
				if (!((Boolean) fontSubstitution).booleanValue()) {
					context.setFontSubstitution(false);
				}
			}
			Object bidiProcessing = options.get(IPDFRenderOption.PDF_BIDI_PROCESSING);
			if (bidiProcessing instanceof Boolean) {
				if (!((Boolean) bidiProcessing).booleanValue()) {
					context.setBidiProcessing(false);
				}
			}
			/*
			 * bidi_hcg: Only disable Bidi processing when the rtl flag is null, i.e. Bidi
			 * support is disabled.
			 */
			// if ( options.get( IRenderOption.RTL_FLAG ) == null )
			// {
			// context.setBidiProcessing( false );
			// }
			Object wordbreak = options.get(IPDFRenderOption.PDF_WORDBREAK);
			if (wordbreak instanceof Boolean) {
				if (((Boolean) wordbreak).booleanValue()) {
					context.setEnableWordbreak(true);
				}
			}

			Object dpi = options.get(IPDFRenderOption.DPI);
			if (dpi instanceof Integer) {
				int screenDpi = ((Integer) dpi).intValue();
				context.setDpi(screenDpi);
			}

			// Object rtlFlag = options.get( IRenderOption.RTL_FLAG );
			// if (rtlFlag != null && rtlFlag instanceof Boolean)
			// {
			// if (((Boolean)rtlFlag).booleanValue())
			// {
			// context.setRtl( true );
			// }
			// }
		}
	}

	@Override
	public void start(IReportContent report) throws BirtException {
		layoutEmitterImpl.start(report);
	}

	@Override
	public void end(IReportContent report) throws BirtException {
		layoutEmitterImpl.end(report);
	}

	@Override
	public void startTable(ITableContent table) throws BirtException {
		layoutEmitterImpl.startTable(table);
	}

	@Override
	public void endTable(ITableContent table) throws BirtException {
		layoutEmitterImpl.endTable(table);
	}

	@Override
	public void startContainer(IContainerContent container) throws BirtException {
		layoutEmitterImpl.startContainer(container);
	}

	@Override
	public void endContainer(IContainerContent container) throws BirtException {
		layoutEmitterImpl.endContainer(container);
	}

	@Override
	public void startContent(IContent content) throws BirtException {
		layoutEmitterImpl.startContent(content);
	}

	@Override
	public void endContent(IContent content) throws BirtException {
		layoutEmitterImpl.endContent(content);
	}

	@Override
	public void startListBand(IListBandContent listBand) throws BirtException {
		layoutEmitterImpl.startListBand(listBand);
	}

	@Override
	public void startListGroup(IListGroupContent listGroup) throws BirtException {
		layoutEmitterImpl.startListGroup(listGroup);
	}

	@Override
	public void endListBand(IListBandContent listBand) throws BirtException {
		layoutEmitterImpl.endListBand(listBand);
	}

	@Override
	public void outputPage(IPageContent page) throws BirtException {
		layoutEmitterImpl.outputPage(page);
	}

	@Override
	public void startPage(IPageContent page) throws BirtException {
		layoutEmitterImpl.startPage(page);
	}

	@Override
	public void endPage(IPageContent page) throws BirtException {
		layoutEmitterImpl.endPage(page);
	}

	@Override
	public void startRow(IRowContent row) throws BirtException {
		layoutEmitterImpl.startRow(row);
	}

	@Override
	public void endRow(IRowContent row) throws BirtException {
		layoutEmitterImpl.endRow(row);
	}

	@Override
	public void startTableBand(ITableBandContent band) throws BirtException {
		layoutEmitterImpl.startTableBand(band);
	}

	@Override
	public void startTableGroup(ITableGroupContent group) throws BirtException {
		layoutEmitterImpl.startTableGroup(group);
	}

	@Override
	public void endTableBand(ITableBandContent band) throws BirtException {
		layoutEmitterImpl.endTableBand(band);
	}

	@Override
	public void endTableGroup(ITableGroupContent group) throws BirtException {
		layoutEmitterImpl.endTableGroup(group);
	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		layoutEmitterImpl.startCell(cell);
	}

	@Override
	public void endCell(ICellContent cell) throws BirtException {
		layoutEmitterImpl.endCell(cell);
	}

	@Override
	public void startForeign(IForeignContent foreign) throws BirtException {
		layoutEmitterImpl.startForeign(foreign);
	}

	@Override
	public ILayoutPageHandler getPageHandler() {
		return layoutEmitterImpl.getPageHandler();
	}
}
