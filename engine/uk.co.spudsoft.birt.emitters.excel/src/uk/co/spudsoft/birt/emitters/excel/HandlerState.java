/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

import uk.co.spudsoft.birt.emitters.excel.framework.Logger;
import uk.co.spudsoft.birt.emitters.excel.handlers.IHandler;

public class HandlerState {

	/**
	 * The emitter itself
	 */
	private IContentEmitter emitter;
	/**
	 * Logger.
	 */
	private Logger log;
	/**
	 * Set of functions for carrying out conversions between BIRT and POI.
	 */
	private StyleManagerUtils smu;

	/**
	 * The current handler to pass on the processing to. Effectively this is the
	 * state machine for the emitter.
	 */
	private IHandler handler;

	/**
	 * The workbook being generated.
	 */
	private Workbook wb;
	/**
	 * Style cache, to enable reuse of styles between cells.
	 */
	private StyleManager sm;
	/**
	 * Render options
	 */
	private IRenderOption renderOptions;
	/**
	 * Report engine
	 */
	private ReportEngine reportEngine;

	/**
	 * The current POI sheet being processed.
	 */
	public Sheet currentSheet;
	/**
	 * Collection of CellImage objects for the current sheet.
	 */
	public List<CellImage> images = new ArrayList<CellImage>();
	/**
	 * Possible name for the current sheet
	 */
	public String sheetName;
	/**
	 * Possible password for the current sheet
	 */
	public String sheetPassword;
	/**
	 * The index of the row that should be created next
	 */
	public int rowNum;
	/**
	 * The index of the column in which the next data should begin
	 */
	public int colNum;
	/**
	 * The minimum row height required for this top level row
	 */
	public float requiredRowHeightInPoints;
	public int rowOffset;
	public int colOffset;
	/**
	 * Set to true when end() is called and pageEnd has to be called
	 */
	public boolean reportEnding;

	/**
	 * Border overrides for the current row/table
	 */
	public List<AreaBorders> areaBorders = new ArrayList<AreaBorders>();

	/**
	 * List of Current Spans We could probably use CellRangeAdresses inside the
	 * sheet, but this way we keep the tests to a minimum.
	 */
	public List<Area> rowSpans = new ArrayList<Area>();

	/**
	 * List of sheet names This map contains the names of sheets created by the
	 * emitter along with a count of sheets with that name Sheets after the first
	 * have the count appended to the name Any other sheets that exist in the
	 * workbook may be overwritten
	 */
	public Map<String, Integer> sheetNames = new HashMap<String, Integer>();

	/**
	 * Constructor
	 *
	 * @param log
	 * @param smu
	 * @param wb
	 * @param sm
	 */
	public HandlerState(IContentEmitter emitter, Logger log, StyleManagerUtils smu, Workbook wb, StyleManager sm,
			IRenderOption renderOptions) {
		super();
		this.emitter = emitter;
		this.log = log;
		this.smu = smu;
		this.wb = wb;
		this.sm = sm;
		this.renderOptions = renderOptions;
	}

	public IContentEmitter getEmitter() {
		return emitter;
	}

	public Logger getLog() {
		return log;
	}

	public StyleManagerUtils getSmu() {
		return smu;
	}

	public Workbook getWb() {
		return wb;
	}

	public StyleManager getSm() {
		return sm;
	}

	public IRenderOption getRenderOptions() {
		return renderOptions;
	}

	public ReportEngine getReportEngine() {
		return reportEngine;
	}

	public IHandler getHandler() {
		return handler;
	}

	public void setHandler(IHandler handler) {
		this.handler = handler;
		this.handler.notifyHandler(this);
	}

	public void insertBorderOverload(AreaBorders defn) {
		if (areaBorders == null) {
			areaBorders = new ArrayList<AreaBorders>();
		}
		areaBorders.add(defn);
	}

	public void removeBorderOverload(AreaBorders defn) {
		if (areaBorders != null) {
			areaBorders.remove(defn);
		}
	}

	public boolean cellIsMergedWithBorders(int row, int column) {
		if (areaBorders != null) {
			for (AreaBorders areaBorder : areaBorders) {
				if ((areaBorder.isMergedCells) && (areaBorder.top == row) && (areaBorder.left == column)) {
					return true;
				}
			}

		}
		return false;
	}

	public boolean rowHasMergedCellsWithBorders(int row) {
		if (areaBorders != null) {
			for (AreaBorders areaBorder : areaBorders) {
				if ((areaBorder.isMergedCells) && (areaBorder.top <= row) && (areaBorder.bottom >= row)) {
					return true;
				}
			}
		}
		return false;
	}

	public Area addRowSpan(int rowX, int colX, int rowY, int colY) {
		log.debug("addRowSpan [" + rowX + "," + colX + "] - [" + rowY + "," + colY + "]");
		Area area = new Area(new Coordinate(rowX, colX), new Coordinate(rowY, colY));
		rowSpans.add(area);
		return area;
	}

	public int computeNumberSpanBefore(int row, int col) {
		int i = 0;
		for (Area a : rowSpans) {
			log.debug("Considering span [ ", a.x.getRow(), ",", a.x.getCol(), "]-[", a.y.getRow(), ",", a.y.getCol(),
					"] for ", row, ",", col);

			// I'm now not removing passed spans, so do check a.y.row()
			if (a.y.getRow() < row) {
				continue;
			}

			// Correct this col to know the real col number
			if (a.x.getCol() <= col) {
				col += (a.y.getCol() - a.x.getCol()) + 1;
			}
			if (row > a.x.getRow() // Span on first appearance is ok.
					&& a.x.getCol() <= col // This span is before this column
			) {
				i += (a.y.getCol() - a.x.getCol()) + 1;
			}
		}
		return i;
	}

	public boolean rowHasSpans(int row) {
		for (Area a : rowSpans) {
			// I'm now not removing passed spans, so do check a.y.row()
			if (a.y.getRow() < row) {
				continue;
			}
			return true;
		}

		return false;
	}

	public float calculateRowSpanHeightRequirement(int row) {
		float result = 0F;

		for (Area a : rowSpans) {
			// I'm now not removing passed spans, so do check a.y.row()
			if (a.y.getRow() < row) {
				continue;
			}

			float heightUnaccountedFor = a.height;
			for (int componentRow = a.x.getRow(); componentRow < row; ++componentRow) {
				heightUnaccountedFor -= currentSheet.getRow(componentRow).getHeightInPoints();
			}
			float heightPerRowRemainig = heightUnaccountedFor / (1 + a.y.getRow() - row);
			if (heightPerRowRemainig > result) {
				result = heightPerRowRemainig;
			}
		}

		return result;
	}

	public void clearRowSpans() {
		rowSpans.clear();
	}

	public int findRowsSpanned(int rowX, int colX) {
		for (Area a : rowSpans) {
			if ((a.x.getRow() == rowX) && (a.x.getCol() == colX)) {
				return a.y.getRow() - a.x.getRow();
			}
		}
		return 0;
	}

	/**
	 * Maximum number of characters that Excel will accept for a sheet name.
	 */
	public static final int MAX_SHEET_NAME_LENGTH = 31;
	/**
	 * Characters that Excel will not accept in a sheet name.
	 */
	public static final String[] ILLEGAL_SHEET_NAME_CHARACTERS = new String[] { "\\", //$NON-NLS-1$
			"/", //$NON-NLS-1$
			"*", //$NON-NLS-1$
			"[", //$NON-NLS-1$
			"]", //$NON-NLS-1$
			":", //$NON-NLS-1$
			"?" //$NON-NLS-1$
	};

	/**
	 * Remove illegal characters from the sheet name and make sure it's not too
	 * long.
	 *
	 * @param sheetName
	 * @return corrected sheet name
	 */
	public String correctSheetName(final String sheetName) {
		// https://www.accountingweb.com/technology/excel/seven-characters-you-cant-use-in-worksheet-names
		if (sheetName == null) {
			return null;
		}
		String correctedSheetName = sheetName;
		for (String illegalChar : ILLEGAL_SHEET_NAME_CHARACTERS) {
			correctedSheetName = correctedSheetName.replace(illegalChar, ""); //$NON-NLS-1$
		}
		if ("history".equalsIgnoreCase(correctedSheetName)) { //$NON-NLS-1$
			return "history-sheet"; //$NON-NLS-1$
		}
		if (correctedSheetName.length() > MAX_SHEET_NAME_LENGTH) {
			correctedSheetName = correctedSheetName.substring(0, MAX_SHEET_NAME_LENGTH);
		}
		return correctedSheetName;
	}

	/**
	 * Add an index to the end of the sheet name if necessary. Shorten the sheet
	 * name if necessary. The sheet name is assumed to not be too long to start
	 * with.
	 *
	 * @return the prepared sheet name
	 * @throws BirtException
	 */
	public String prepareSheetName() throws BirtException {
		String sheetName = this.sheetName;
		if (sheetName == null) {
			return null;
		}
		String preparedName = sheetName;
		Integer previousNameCount = 1;
		while (true) {
			Integer nameCount = this.sheetNames.get(sheetName);
			if (nameCount == null) {
				this.sheetNames.put(sheetName, previousNameCount);
				return preparedName;
			}
			++nameCount;
			preparedName = sheetName + " " + nameCount; //$NON-NLS-1$
			int correction = preparedName.length() - MAX_SHEET_NAME_LENGTH;
			if (correction <= 0) {
				this.sheetNames.put(sheetName, nameCount);
				return preparedName;
			}
			if (correction > sheetName.length()) {
				throw new BirtException(EmitterServices.getPluginName(),
						"Unable to fit sheet name into the maximum allowed length", //$NON-NLS-1$
						null);
			}
			sheetName = sheetName.substring(0, sheetName.length() - correction);
			preparedName = sheetName + " " + nameCount; //$NON-NLS-1$
			previousNameCount = nameCount;
		}
	}
}
