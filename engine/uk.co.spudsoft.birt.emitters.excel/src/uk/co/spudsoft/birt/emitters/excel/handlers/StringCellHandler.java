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

import org.apache.poi.ss.usermodel.Cell;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;

import uk.co.spudsoft.birt.emitters.excel.Area;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.StylePropertyIndexes;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class StringCellHandler extends CellContentHandler {

	private String result = null;
	
	public StringCellHandler(IContentEmitter emitter, Logger log, IHandler parent, ICellContent cell) {
		super(emitter, log, parent, cell);
	}

	@Override
	public void endCell(HandlerState state, ICellContent cell) throws BirtException {
		endCellContent(state, cell, lastElement, null, null );
	}
	
	@Override
	protected void endCellContent(HandlerState state, ICellContent birtCell, IContent element, Cell cell, Area area) {
		if( lastValue != null ) {
			if( lastValue instanceof String ) {
				result = (String)lastValue;
			} else {
				result = lastValue.toString();
			}
		}
	}

	public void visit( Object obj ) throws BirtException {
		if( obj instanceof ICellContent ) {
			contentVisitor.visitCell( (ICellContent)obj, null );
		} else if( obj instanceof ILabelContent ) {
			contentVisitor.visitLabel( (ILabelContent)obj, null );
		} else if( obj instanceof ITextContent ) {
			contentVisitor.visitText( (ITextContent)obj, null );
		}  else if( obj instanceof IAutoTextContent ) {
			contentVisitor.visitAutoText( (IAutoTextContent)obj, null );
		}  else if( obj instanceof IForeignContent ) {
			contentVisitor.visitForeign( (IForeignContent)obj, null );
		} else {
			log.warn(0, "Not visiting " + obj.getClass(), null);
		}
	}

	public String getString() {
		if( result == null ) {
			if( lastValue != null ) {
				if( lastValue instanceof String ) {
					result = (String)lastValue;
				} else {
					result = lastValue.toString();
				}
			}
		} 
		if( result == null ) {
			result = "";
		}
		return result;
	}
	

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		state.setHandler(new FlattenedTableHandler(this, log, this, table));
		state.getHandler().startTable(state, table);
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		String textText = text.getText();
		log.debug( "text:", textText );
		emitContent(state,text,textText, ( ! "inline".equals( getStyleProperty(text, StylePropertyIndexes.STYLE_DISPLAY, "block") ) ) );
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		emitContent(state,data,data.getValue(), ( ! "inline".equals( getStyleProperty(data, StylePropertyIndexes.STYLE_DISPLAY, "block") ) ) );
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		String labelText = ( label.getLabelText() != null ) ? label.getLabelText() : label.getText();
		log.debug( "labelText:" + labelText );
		emitContent(state,label,labelText, ( ! "inline".equals( getStyleProperty(label, StylePropertyIndexes.STYLE_DISPLAY, "block") ) ));
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		emitContent(state,autoText,autoText.getText(), ( ! "inline".equals( getStyleProperty(autoText, StylePropertyIndexes.STYLE_DISPLAY, "block") ) ) );
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {

		log.debug( "Handling foreign content of type " + foreign.getRawType() );
		if ( IForeignContent.HTML_TYPE.equalsIgnoreCase( foreign.getRawType( ) ) )
		{
			HTML2Content.html2Content( foreign );
			contentVisitor.visitChildren( foreign, null );			
		}
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		log.debug( "image:" + image.getName() );
		emitContent(state, image, "&G", false);
	}

	@Override
	public void endContainer(HandlerState state, IContainerContent container) throws BirtException {
		lastCellContentsWasBlock = ( ! "inline".equals( getStyleProperty(container, StylePropertyIndexes.STYLE_DISPLAY, "block") ) );
	}
			
	
	
}
