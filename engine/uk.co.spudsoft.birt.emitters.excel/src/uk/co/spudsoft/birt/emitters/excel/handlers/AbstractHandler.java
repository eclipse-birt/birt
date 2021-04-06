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

package uk.co.spudsoft.birt.emitters.excel.handlers;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellReference.NameType;
import org.eclipse.birt.core.exception.BirtException;
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
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyledElement;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.dom.css.CSSValue;

import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class AbstractHandler implements IHandler {

	protected Logger log;
	protected IContent element;
	protected IHandler parent;
	private CSSValue backgroundColour;

	public AbstractHandler(Logger log, IHandler parent, IContent element) {
		this.log = log;
		this.parent = parent;
		this.element = element;
	}

	public void notifyHandler(HandlerState state) {
	}

	public String getPath() {
		if (parent != null) {
			return this.getClass().getSimpleName() + "/" + parent.getPath();
		} else {
			return this.getClass().getSimpleName();
		}
	}

	public IHandler getParent() {
		return parent;
	}

	@SuppressWarnings("unchecked")
	public <T extends IHandler> T getAncestor(Class<T> clazz) {
		if (parent != null) {
			if (clazz.isInstance(parent)) {
				return (T) parent;
			} else {
				return parent.getAncestor(clazz);
			}
		}
		return null;
	}

	public CSSValue getBackgroundColour() {
		if (backgroundColour != null) {
			return backgroundColour;
		}
		if (element != null) {
			CSSValue elemColour = element.getComputedStyle().getProperty(StyleConstants.STYLE_BACKGROUND_COLOR);
			if ((elemColour != null) && !CSSConstants.CSS_TRANSPARENT_VALUE.equals(elemColour.getCssText())) {
				backgroundColour = elemColour;
			}
		}
		if ((parent != null) && (backgroundColour == null)) {
			backgroundColour = parent.getBackgroundColour();
		}
		return backgroundColour;
	}

	protected static String getStyleProperty(IStyledElement element, int property, String defaultValue) {
		CSSValue value = element.getComputedStyle().getProperty(property);
		if (value != null) {
			return value.getCssText();
		} else {
			return defaultValue;
		}
	}

	protected static String prepareName(String name) {
		char c = name.charAt(0);
		boolean requirePreparation = (!(c == '_' || Character.isLetter(c)) || name.indexOf(' ') != -1);
		if (!requirePreparation) {
			for (int i = 1; i < name.length(); ++i) {
				c = name.charAt(i);
				if (!(Character.isLetter(c) || Character.isDigit(c) || c == '_')) {
					requirePreparation = true;
					break;
				}
			}
		}

		if (requirePreparation) {
			name = name.trim();
			char chars[] = name.toCharArray();
			for (int i = 0; i < name.length(); ++i) {
				c = chars[i];
				if (!(Character.isLetter(c) || Character.isDigit(c) || c == '_')) {
					chars[i] = '_';
				}
			}
			name = new String(chars);
		}

		NameType refType = CellReference.classifyCellReference(name, SpreadsheetVersion.EXCEL2007);
		if ((NameType.CELL == refType) || (NameType.COLUMN == refType) || (NameType.ROW == refType)) {
			name = "_" + name;
		}

		return name;
	}

	protected void createName(HandlerState state, String bookmark, int row1, int col1, int row2, int col2) {
		CellReference crFirst = new CellReference(state.currentSheet.getSheetName(), row1, col1, true, true);
		CellReference crLast = new CellReference(row2, col2, true, true);
		String formula = crFirst.formatAsString() + ":" + crLast.formatAsString();

		Name name = state.currentSheet.getWorkbook().getName(bookmark);
		if (name == null) {
			name = state.currentSheet.getWorkbook().createName();
			name.setNameName(bookmark);
			name.setRefersToFormula(formula);
		} else {
			String existingFormula = name.getRefersToFormula();
			try {
				name.setRefersToFormula(existingFormula + "," + formula);
			} catch (FormulaParseException ex) {
				log.warn(0, "Unable to add \"" + formula + "\" to name (\"" + bookmark + "\") with existing formula: "
						+ existingFormula, ex);
			}
		}
	}

	public void startPage(HandlerState state, IPageContent page) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startPage");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endPage(HandlerState state, IPageContent page) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endPage");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startTable");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endTable(HandlerState state, ITableContent table) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endTable");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startTableBand(HandlerState state, ITableBandContent band) throws BirtException {
		// NoSuchMethodError ex = new NoSuchMethodError( "Method not implemented: " +
		// this.getClass().getSimpleName() + ".startTableBand" );
		// log.error(0, "Method not implemented", ex);
		// throw ex;
	}

	public void endTableBand(HandlerState state, ITableBandContent band) throws BirtException {
		// NoSuchMethodError ex = new NoSuchMethodError( "Method not implemented: " +
		// this.getClass().getSimpleName() + ".endTableBand" );
		// log.error(0, "Method not implemented", ex);
		// throw ex;
	}

	public void startRow(HandlerState state, IRowContent row) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startRow");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endRow(HandlerState state, IRowContent row) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endRow");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startCell(HandlerState state, ICellContent cell) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startCell");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endCell(HandlerState state, ICellContent cell) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endCell");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startList(HandlerState state, IListContent list) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startList");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endList(HandlerState state, IListContent list) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endList");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startListBand(HandlerState state, IListBandContent listBand) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startListBand");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endListBand(HandlerState state, IListBandContent listBand) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endListBand");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startContainer(HandlerState state, IContainerContent container) throws BirtException {
		// NoSuchMethodError ex = new NoSuchMethodError( "Method not implemented: " +
		// this.getClass().getSimpleName() + ".startContainer" );
		// log.error(0, "Method not implemented", ex);
		// throw ex;
	}

	public void endContainer(HandlerState state, IContainerContent container) throws BirtException {
		// NoSuchMethodError ex = new NoSuchMethodError( "Method not implemented: " +
		// this.getClass().getSimpleName() + ".endContainer" );
		// log.error(0, "Method not implemented", ex);
		// throw ex;
	}

	public void startContent(HandlerState state, IContent content) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startContent");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endContent(HandlerState state, IContent content) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endContent");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startGroup(HandlerState state, IGroupContent group) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startGroup");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endGroup(HandlerState state, IGroupContent group) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endGroup");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startTableGroup");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endTableGroup");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void startListGroup(HandlerState state, IListGroupContent group) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".startListGroup");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void endListGroup(HandlerState state, IListGroupContent group) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".endListGroup");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".emitText");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".emitData");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".emitLabel");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".emitAutoText");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".emitForeign");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		NoSuchMethodError ex = new NoSuchMethodError(
				"Method not implemented: " + this.getClass().getSimpleName() + ".emitImage");
		log.error(0, "Method not implemented", ex);
		throw ex;
	}

}
