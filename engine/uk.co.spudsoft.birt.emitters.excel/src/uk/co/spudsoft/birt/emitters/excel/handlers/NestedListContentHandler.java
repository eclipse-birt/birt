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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;

import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class NestedListContentHandler extends CellContentHandler {

	protected int column;

	public NestedListContentHandler(IContentEmitter emitter, Logger log, IHandler parent, IListContent list,
			int column) {
		super(emitter, log, parent, null);
		this.element = list;
		this.column = column;
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		String textText = text.getText();
		log.debug("text:", textText);
		emitContent(state, text, textText,
				(!"inline".equals(getStyleProperty(text, StyleConstants.STYLE_DISPLAY, "block"))));
		state.setHandler(parent);
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		emitContent(state, data, data.getValue(),
				(!"inline".equals(getStyleProperty(data, StyleConstants.STYLE_DISPLAY, "block"))));
		state.setHandler(parent);
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		// String labelText = ( label.getLabelText() != null ) ? label.getLabelText() :
		// label.getText();
		String labelText = (label.getText() != null) ? label.getText() : label.getLabelText();
		log.debug("labelText:", labelText);
		emitContent(state, label, labelText,
				(!"inline".equals(getStyleProperty(label, StyleConstants.STYLE_DISPLAY, "block"))));
		state.setHandler(parent);
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		emitContent(state, autoText, autoText.getText(),
				(!"inline".equals(getStyleProperty(autoText, StyleConstants.STYLE_DISPLAY, "block"))));
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
		recordImage(state, new Coordinate(state.rowNum, column), image, false);
		lastElement = image;
		state.setHandler(parent);
	}

}
