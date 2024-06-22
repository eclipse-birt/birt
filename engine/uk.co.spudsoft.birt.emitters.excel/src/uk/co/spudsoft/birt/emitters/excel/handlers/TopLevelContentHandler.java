/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2024 James Talbut and others
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

package uk.co.spudsoft.birt.emitters.excel.handlers;

import org.apache.poi.ss.usermodel.Cell;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;

import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class TopLevelContentHandler extends CellContentHandler {

	public TopLevelContentHandler(IContentEmitter emitter, Logger log, IHandler parent) {
		super(emitter, log, parent, null);
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		log.debug("Creating row ", state.rowNum, " for text");
		state.currentSheet.createRow(state.rowNum);

		emitContent(state, text, text.getText(),
				(!"inline".equals(getStyleProperty(text, StyleConstants.STYLE_DISPLAY, "block"))));

		Cell currentCell = state.currentSheet.getRow(state.rowNum).createCell(0);
		// currentCell.setCellType(Cell.CELL_TYPE_BLANK);
		currentCell.setBlank();

		endCellContent(state, null, text, currentCell, null);

		++state.rowNum;
		state.setHandler(parent);
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		log.debug("Creating row ", state.rowNum, " for data");
		state.currentSheet.createRow(state.rowNum);

		emitContent(state, data, data.getValue(),
				(!"inline".equals(getStyleProperty(data, StyleConstants.STYLE_DISPLAY, "block"))));

		Cell currentCell = state.currentSheet.getRow(state.rowNum).createCell(0);
		// currentCell.setCellType(Cell.CELL_TYPE_BLANK);
		currentCell.setBlank();

		endCellContent(state, null, data, currentCell, null);

		++state.rowNum;
		state.setHandler(parent);
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		log.debug("Creating row ", state.rowNum, " for label");
		state.currentSheet.createRow(state.rowNum);

		String labelText = (label.getText() != null) ? label.getText() : label.getLabelText();
		emitContent(state, label, labelText,
				(!"inline".equals(getStyleProperty(label, StyleConstants.STYLE_DISPLAY, "block"))));

		Cell currentCell = state.currentSheet.getRow(state.rowNum).createCell(0);
		// currentCell.setCellType(Cell.CELL_TYPE_BLANK);
		currentCell.setBlank();

		endCellContent(state, null, label, currentCell, null);

		++state.rowNum;
		state.setHandler(parent);
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {

		log.debug("Handling foreign content of type ", foreign.getRawType());
		if (IForeignContent.HTML_TYPE.equalsIgnoreCase(foreign.getRawType())) {
			HTML2Content.html2Content(foreign);
			contentVisitor.visitChildren(foreign, null);
		}

		state.setHandler(parent);
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		log.debug("Creating row ", state.rowNum, " for image");
		state.currentSheet.createRow(state.rowNum);

		recordImage(state, new Coordinate(state.rowNum, 0), image, true);
		Cell currentCell = state.currentSheet.getRow(state.rowNum).createCell(0);
		// currentCell.setCellType(Cell.CELL_TYPE_BLANK);
		currentCell.setBlank();

		endCellContent(state, null, image, currentCell, null);

		++state.rowNum;
		state.setHandler(parent);
	}

}
