/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
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
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

import uk.co.spudsoft.birt.emitters.excel.framework.ExcelEmitterPlugin;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;
import uk.co.spudsoft.birt.emitters.excel.handlers.PageHandler;

/**
 * Create the excel emitter for output
 *
 * @since 3.3
 *
 */
public abstract class ExcelEmitter implements IContentEmitter {

	/** property: ExcelEmitter.DEBUG */
	public static final String DEBUG = "ExcelEmitter.DEBUG";

	/** property: ExcelEmitter.RemoveBlankRows */
	public static final String REMOVE_BLANK_ROWS = "ExcelEmitter.RemoveBlankRows";

	/** property: ExcelEmitter.Rotation */
	public static final String ROTATION_PROP = "ExcelEmitter.Rotation";

	/** property: ExcelEmitter.ForceAutoColWidths */
	public static final String FORCEAUTOCOLWIDTHS_PROP = "ExcelEmitter.ForceAutoColWidths";

	/** property: ExcelEmitter.SingleSheet */
	public static final String SINGLE_SHEET = "ExcelEmitter.SingleSheet";

	/** property: ExcelEmitter.SingleSheetWithPageBreaks */
	public static final String SINGLE_SHEET_PAGE_BREAKS = "ExcelEmitter.SingleSheetWithPageBreaks";

	/** property: ExcelEmitter.InsertPrintBreakAfter */
	public static final String PRINT_BREAK_AFTER = "ExcelEmitter.InsertPrintBreakAfter";

	/** property: ExcelEmitter.DisableGrouping */
	public static final String DISABLE_GROUPING = "ExcelEmitter.DisableGrouping";

	/** property: ExcelEmitter.StructuredHeader */
	public static final String STRUCTURED_HEADER = "ExcelEmitter.StructuredHeader";

	/** property: ExcelEmitter.CustomNumberFormat */
	public static final String CUSTOM_NUMBER_FORMAT = "ExcelEmitter.CustomNumberFormat";

	/** property: ExcelEmitter.AutoFilter */
	public static final String AUTO_FILTER = "ExcelEmitter.AutoFilter";

	/** property: ExcelEmitter.SheetProtectPassword */
	public static final String SHEET_PASSWORD = "ExcelEmitter.SheetProtectPassword";

	/** property: ExcelEmitter.GroupSummaryHeader */
	public static final String GROUP_SUMMARY_HEADER = "ExcelEmitter.GroupSummaryHeader";

	/** property: ExcelEmitter.FreezePanes */
	public static final String FREEZE_PANES = "ExcelEmitter.FreezePanes";

	/** property: ExcelEmitter.BlankRowAfterTopLevelTable */
	public static final String BLANK_ROW_AFTER_TOP_LEVEL_TABLE = "ExcelEmitter.BlankRowAfterTopLevelTable";

	/** property: ExcelEmitter.SpannedRowHeight */
	public static final String SPANNED_ROW_HEIGHT = "ExcelEmitter.SpannedRowHeight";

	/** property: ExcelEmitter.NestedTableInLastCell */
	public static final String NEST_TABLE_IN_LAST_CELL = "ExcelEmitter.NestedTableInLastCell";

	/** property: spanned row height spread */
	public static final int SPANNED_ROW_HEIGHT_SPREAD = 0;

	/** property: spanned row height first */
	public static final int SPANNED_ROW_HEIGHT_FIRST = 1;

	/** property: spanned row height ignored */
	public static final int SPANNED_ROW_HEIGHT_IGNORED = 2;

	/** property: ExcelEmitter.PrintScale */
	public static final String PRINT_SCALE = "ExcelEmitter.PrintScale";

	/** property: ExcelEmitter.PrintPagesWide */
	public static final String PRINT_PAGES_WIDE = "ExcelEmitter.PrintPagesWide";

	/** property: ExcelEmitter.PrintPagesHigh */
	public static final String PRINT_PAGES_HIGH = "ExcelEmitter.PrintPagesHigh";

	/** property: ExcelEmitter.DisplayFormulas */
	public static final String DISPLAYFORMULAS_PROP = "ExcelEmitter.DisplayFormulas";

	/** property: ExcelEmitter.DisplayGridlines */
	public static final String DISPLAYGRIDLINES_PROP = "ExcelEmitter.DisplayGridlines";

	/** property: ExcelEmitter.DisplayRowColHeadings */
	public static final String DISPLAYROWCOLHEADINGS_PROP = "ExcelEmitter.DisplayRowColHeadings";

	/** property: ExcelEmitter.DisplayZeros */
	public static final String DISPLAYZEROS_PROP = "ExcelEmitter.DisplayZeros";

	/** property: ExcelEmitter.ValueAsFormula */
	public static final String VALUE_AS_FORMULA = "ExcelEmitter.ValueAsFormula";

	/** property: ExcelEmitter.Formula */
	public static final String FORMULA = "ExcelEmitter.Formula";

	/** property: ExcelEmitter.TemplateFile */
	public static final String TEMPLATE_FILE = "ExcelEmitter.TemplateFile";

	/** property: ExcelEmitter.StreamingXlsx */
	public static final String STREAMING_XLSX = "ExcelEmitter.StreamingXlsx";

	/** property: ExcelEmitter.ForceRecalculation */
	public static final String FORCE_RECALCULATION = "ExcelEmitter.ForceRecalculation";

	/** property: ExcelEmitter.PrintGridlines */
	public static final String PRINTGRIDLINES_PROP = "ExcelEmitter.PrintGridlines";

	/** property: ExcelEmitter.PrintRowColHeadings */
	public static final String PRINTROWCOLHEADINGS_PROP = "ExcelEmitter.PrintRowColHeadings";

	/** property: ExcelEmitter.PrintFitToPage */
	public static final String PRINTFITTOPAGE_PROP = "ExcelEmitter.PrintFitToPage";

	/** property: ExcelEmitter.DisplaySheetZoom */
	public static final String DISPLAY_SHEET_ZOOM = "ExcelEmitter.DisplaySheetZoom";

	/** property: ExcelEmitter.PagePreview */
	public static final String PAGE_PREVIEW = "ExcelEmitter.PagePreview";

	/** property: ExcelEmitter.DisplayTextIndent */
	public static final String DISPLAY_TEXT_INDENT = "ExcelEmitter.DisplayTextIndent";

	/** property: minimum zoom value of excel sheet */
	public static final short poiExcelDisplaySheetZoomScaleMin = 10;

	/** property: maximum zoom value of excel sheet */
	public static final short poiExcelDisplaySheetZoomScaleMax = 400;

	/** property: minimum scale of the excel print out */
	public static final short poiExcelPrintScaleMin = 10;

	/** property: maximum scale of the excel print out */
	public static final short poiExcelPrintScaleMax = 400;

	/** property: minimum zoom value of excel sheet */
	public static final String poiExcelPreviewPageLayout = "PageLayout";

	/** property: minimum zoom value of excel sheet */
	public static final String poiExcelPreviewPageBreak = "PageBreak";

	/**
	 * Logger.
	 */
	protected Logger log;
	/**
	 * <p>
	 * Output stream that the report is to be written to.
	 * </p>
	 * <p>
	 * This is set in initialize() and reset in end() and must not be set anywhere
	 * else.
	 * </p>
	 */
	protected OutputStream reportOutputStream;
	/**
	 * <p>
	 * Record of whether the emitter opened the report output stream itself, and it
	 * thus responsible for closing it.
	 * </p>
	 */
	protected boolean outputStreamOpened;
	/**
	 * <p>
	 * Name of the file that the report is to be written to (for tracking only).
	 * </p>
	 * <p>
	 * This is set in initialize() and reset in end() and must not be set anywhere
	 * else.
	 * </p>
	 */
	protected String reportOutputFilename;
	/**
	 * The state date passed around the handlers.
	 */
	private HandlerState handlerState;

	private IRenderOption renderOptions;
	/**
	 * The last page seen, cached so it can be used to call endPage
	 *
	 */
	private IPageContent lastPage;

	/**
	 * Factory for creating the appropriate StyleManagerUtils object
	 */
	private StyleManagerUtils.Factory utilsFactory;

	protected ExcelEmitter(StyleManagerUtils.Factory utilsFactory) {
		this.utilsFactory = utilsFactory;
		try {
			if (ExcelEmitterPlugin.getDefault() != null) {
				log = ExcelEmitterPlugin.getDefault().getLogger();
			} else {
				log = new Logger(this.getClass().getPackage().getName());
			}
			log.debug("ExcelEmitter");
		} catch (Exception ex) {
			Throwable t = ex;
			while (t != null) {
				log.debug(t.getMessage());
				t.printStackTrace();
				t = t.getCause();
			}
		}
	}

	/**
	 * Constructs a new workbook to be processed by the emitter.
	 *
	 * @return The new workbook.
	 */
	protected abstract Workbook createWorkbook();

	/**
	 * Constructs a new workbook to be processed by the emitter.
	 *
	 * @return The new (streaming) workbook.
	 *
	 * @since 4.14
	 */
	protected abstract Workbook createSWorkbook();

	/**
	 * Constructs a new workbook to be processed by the emitter.
	 *
	 * @param templateFile The file to open as a template for the output file
	 * @return The new workbook.
	 */
	protected abstract Workbook openWorkbook(File templateFile) throws IOException;

	@Override
	public void initialize(IEmitterServices service) throws BirtException {
		renderOptions = service.getRenderOption();
		boolean debug = EmitterServices.booleanOption(renderOptions, (IContent) null, DEBUG, false);
		log.setDebug(debug);

		log.debug("inintialize");
		reportOutputStream = service.getRenderOption().getOutputStream();
		reportOutputFilename = service.getRenderOption().getOutputFileName();
		if ((reportOutputStream == null) && ((reportOutputFilename == null) || reportOutputFilename.isEmpty())) {
			throw new BirtException(EmitterServices.getPluginName(),
					"Neither output stream nor output filename have been specified", null);
		}
	}

	@Override
	public void start(IReportContent report) throws BirtException {
		log.addPrefix('>');
		log.info(0, "start:" + report.toString(), null);

		String templatePath = EmitterServices.stringOption(renderOptions, report, TEMPLATE_FILE, null);
		Workbook wb;
		if (templatePath != null) {
			URL templateURL = report.getReportContext().getResource(templatePath);
			File templateFile;
			try {
				templateFile = new File(templateURL.toURI());
			} catch (URISyntaxException ex) {
				throw new BirtException(EmitterServices.getPluginName(),
						"Unable locate template resource for " + templatePath, ex);
			}
			try {
				wb = openWorkbook(templateFile);
			} catch (IOException ex) {
				throw new BirtException(EmitterServices.getPluginName(),
						"Unable to open template workbook for " + templateFile.toString(), ex);
			}
		} else {
			if (EmitterServices.booleanOption(renderOptions, report, ExcelEmitter.STREAMING_XLSX, false)) {
				wb = createSWorkbook();
			} else {
				wb = createWorkbook();
			}
		}

		CSSEngine cssEngine = report.getRoot().getCSSEngine();
		StyleManagerUtils smu = utilsFactory.create(log);

		StyleManager sm = new StyleManager(wb, log, smu, cssEngine, report.getReportContext().getLocale());

		handlerState = new HandlerState(this, log, smu, wb, sm, renderOptions);
		handlerState.setHandler(new PageHandler(log, null));

		if (EmitterServices.booleanOption(handlerState.getRenderOptions(), report,
				ExcelEmitter.SINGLE_SHEET_PAGE_BREAKS, false)) {
			handlerState.getRenderOptions().setOption(ExcelEmitter.SINGLE_SHEET, Boolean.TRUE);
		}

		if (EmitterServices.booleanOption(renderOptions, report, ExcelEmitter.FORCE_RECALCULATION, false)) {
			wb.setForceFormulaRecalculation(true);
		}
	}

	@Override
	public void end(IReportContent report) throws BirtException {

		if (EmitterServices.booleanOption(handlerState.getRenderOptions(), report, ExcelEmitter.SINGLE_SHEET, false)) {
			handlerState.reportEnding = true;
			handlerState.getHandler().endPage(handlerState, lastPage);
		}

		log.removePrefix('>');
		log.debug("end:", report);

		String reportTitle = handlerState.correctSheetName(report.getTitle());
		if ((handlerState.getWb().getNumberOfSheets() == 1) && (reportTitle != null)) {
			handlerState.getWb().setSheetName(0, reportTitle);
		}

		OutputStream outputStream = reportOutputStream;
		try {
			if (outputStream == null) {
				if ((reportOutputFilename != null) && !reportOutputFilename.isEmpty()) {
					try {
						outputStream = new FileOutputStream(reportOutputFilename);
					} catch (IOException ex) {
						log.warn(0, "File \"" + reportOutputFilename + "\" cannot be opened for writing", ex);
						throw new BirtException(EmitterServices.getPluginName(),
								"Unable to open file (\"{}\") for writing", new Object[] { reportOutputFilename }, null,
								ex);
					}
				}
			}
			handlerState.getWb().write(outputStream);
		} catch (Throwable ex) {
			log.debug("ex:", ex.toString());
			ex.printStackTrace();

			throw new BirtException(EmitterServices.getPluginName(), "Unable to save file (\"{}\")",
					new Object[] { reportOutputFilename }, null, ex);
		} finally {
			if (reportOutputStream == null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					log.debug("ex:", ex.toString());
				}
			}
			if (handlerState.getWb() instanceof SXSSFWorkbook) {
				((SXSSFWorkbook) handlerState.getWb()).dispose();
			}
			handlerState = null;
			reportOutputFilename = null;
			reportOutputStream = null;
		}

	}

	@Override
	public void startPage(IPageContent page) throws BirtException {
		log.addPrefix('P');
		log.debug(handlerState, "startPage: ");
		handlerState.getHandler().startPage(handlerState, page);
	}

	@Override
	public void endPage(IPageContent page) throws BirtException {
		lastPage = page;
		log.debug(handlerState, "endPage: ");
		handlerState.getHandler().endPage(handlerState, page);
		log.removePrefix('P');
	}

	@Override
	public void startTable(ITableContent table) throws BirtException {
		log.addPrefix('T');
		log.debug(handlerState, "startTable: ");
		handlerState.getHandler().startTable(handlerState, table);
	}

	@Override
	public void endTable(ITableContent table) throws BirtException {
		log.debug(handlerState, "endTable: ");
		handlerState.getHandler().endTable(handlerState, table);
		log.removePrefix('T');
	}

	@Override
	public void startTableBand(ITableBandContent band) throws BirtException {
		log.addPrefix('B');
		log.debug(handlerState, "startTableBand: ");
		handlerState.getHandler().startTableBand(handlerState, band);
	}

	@Override
	public void endTableBand(ITableBandContent band) throws BirtException {
		log.debug(handlerState, "endTableBand: ");
		handlerState.getHandler().endTableBand(handlerState, band);
		log.removePrefix('B');
	}

	@Override
	public void startRow(IRowContent row) throws BirtException {
		log.addPrefix('R');
		log.debug(handlerState, "startRow: ");
		handlerState.getHandler().startRow(handlerState, row);
	}

	@Override
	public void endRow(IRowContent row) throws BirtException {
		log.debug(handlerState, "endRow: ");
		handlerState.getHandler().endRow(handlerState, row);
		log.removePrefix('R');
	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		log.addPrefix('C');
		log.debug(handlerState, "startCell: ");
		handlerState.getHandler().startCell(handlerState, cell);
	}

	@Override
	public void endCell(ICellContent cell) throws BirtException {
		log.debug(handlerState, "endCell: ");
		handlerState.getHandler().endCell(handlerState, cell);
		log.removePrefix('C');
	}

	@Override
	public void startList(IListContent list) throws BirtException {
		log.addPrefix('L');
		log.debug(handlerState, "startList: ");
		handlerState.getHandler().startList(handlerState, list);
	}

	@Override
	public void endList(IListContent list) throws BirtException {
		log.debug(handlerState, "endList: ");
		handlerState.getHandler().endList(handlerState, list);
		log.removePrefix('L');
	}

	@Override
	public void startListBand(IListBandContent listBand) throws BirtException {
		log.addPrefix('B');
		log.debug(handlerState, "startListBand: ");
		handlerState.getHandler().startListBand(handlerState, listBand);
	}

	@Override
	public void endListBand(IListBandContent listBand) throws BirtException {
		log.debug(handlerState, "endListBand: ");
		handlerState.getHandler().endListBand(handlerState, listBand);
		log.removePrefix('B');
	}

	@Override
	public void startContainer(IContainerContent container) throws BirtException {
		log.addPrefix('O');
		log.debug(handlerState, "startContainer: ");
		handlerState.getHandler().startContainer(handlerState, container);
	}

	@Override
	public void endContainer(IContainerContent container) throws BirtException {
		log.debug(handlerState, "endContainer: ");
		handlerState.getHandler().endContainer(handlerState, container);
		log.removePrefix('O');
	}

	@Override
	public void startText(ITextContent text) throws BirtException {
		log.debug(handlerState, "startText: ");
		handlerState.getHandler().emitText(handlerState, text);
	}

	@Override
	public void startData(IDataContent data) throws BirtException {
		log.debug(handlerState, "startData: ");
		handlerState.getHandler().emitData(handlerState, data);
	}

	@Override
	public void startLabel(ILabelContent label) throws BirtException {
		log.debug(handlerState, "startLabel: ");
		handlerState.getHandler().emitLabel(handlerState, label);
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		log.debug(handlerState, "startAutoText: ");
		handlerState.getHandler().emitAutoText(handlerState, autoText);
	}

	@Override
	public void startForeign(IForeignContent foreign) throws BirtException {
		log.debug(handlerState, "startForeign: ");
		handlerState.getHandler().emitForeign(handlerState, foreign);
	}

	@Override
	public void startImage(IImageContent image) throws BirtException {
		log.debug(handlerState, "startImage: ");
		handlerState.getHandler().emitImage(handlerState, image);
	}

	@Override
	public void startContent(IContent content) throws BirtException {
		log.addPrefix('N');
		log.debug(handlerState, "startContent: ");
		handlerState.getHandler().startContent(handlerState, content);
	}

	@Override
	public void endContent(IContent content) throws BirtException {
		log.debug(handlerState, "endContent: ");
		handlerState.getHandler().endContent(handlerState, content);
		log.removePrefix('N');
	}

	@Override
	public void startGroup(IGroupContent group) throws BirtException {
		log.debug(handlerState, "startGroup: ");
		handlerState.getHandler().startGroup(handlerState, group);
	}

	@Override
	public void endGroup(IGroupContent group) throws BirtException {
		log.debug(handlerState, "endGroup: ");
		handlerState.getHandler().endGroup(handlerState, group);
	}

	@Override
	public void startTableGroup(ITableGroupContent group) throws BirtException {
		log.addPrefix('G');
		log.debug(handlerState, "startTableGroup: ");
		handlerState.getHandler().startTableGroup(handlerState, group);
	}

	@Override
	public void endTableGroup(ITableGroupContent group) throws BirtException {
		log.debug(handlerState, "endTableGroup: ");
		handlerState.getHandler().endTableGroup(handlerState, group);
		log.removePrefix('G');
	}

	@Override
	public void startListGroup(IListGroupContent group) throws BirtException {
		log.addPrefix('G');
		log.debug(handlerState, "startListGroup: ");
		handlerState.getHandler().startListGroup(handlerState, group);
	}

	@Override
	public void endListGroup(IListGroupContent group) throws BirtException {
		log.debug(handlerState, "endListGroup: ");
		handlerState.getHandler().endListGroup(handlerState, group);
		log.removePrefix('G');
	}

}
