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

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SheetUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;

import uk.co.spudsoft.birt.emitters.excel.AreaBorders;
import uk.co.spudsoft.birt.emitters.excel.BirtStyle;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.FilteredSheet;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class AbstractRealTableHandler extends AbstractHandler implements ITableHandler, NestedTableContainer {

	protected int startRow;
	protected int startCol;
	protected int startDetailsRow = -1;
	protected int endDetailsRow;
	protected int startHeaderRow = -1;
	
	@SuppressWarnings("unused")
	private ITableGroupContent currentGroup;
	@SuppressWarnings("unused")
	private ITableBandContent currentBand;
	
	private BirtStyle tableStyle;
	private AreaBorders borderDefn;
	
	private boolean repeatHeader = false;
	
	private List< NestedTableHandler > nestedTables;

	public AbstractRealTableHandler(Logger log, IHandler parent, ITableContent table) {
		super(log, parent, table);
	}

	public int getColumnCount() {
		return ((ITableContent)this.element).getColumnCount();
	}
	
	public void addNestedTable( NestedTableHandler nestedTableHandler ) {
		if( nestedTables == null ) {
			nestedTables = new ArrayList<NestedTableHandler>();
		}
		log.debug( "Adding nested table: ", nestedTableHandler );
		nestedTables.add(nestedTableHandler);
	}
	
	public boolean rowHasNestedTable( int rowNum ) {
		if( nestedTables != null ) {
			for( NestedTableHandler nestedTableHandler : nestedTables ) {
				if( nestedTableHandler.includesRow( rowNum ) ) {
					log.debug( "Row ", rowNum, " has nested table ", nestedTableHandler );
					return true;
				}
			}
		}
		log.debug( "Row ", rowNum, " has no nested tables" );
		return false;
	}
	
	public int extendRowBy( int rowNum ) {
		int offset = 1;
		if( nestedTables != null ) {
			for( NestedTableHandler nestedTableHandler : nestedTables ) {
				int nestedTablesOffset = nestedTableHandler.extendParentsRowBy( rowNum );
				if( nestedTablesOffset > offset ) {
					log.debug( "Row ", rowNum, " is extended by ", nestedTablesOffset, " thanks to ", nestedTableHandler );
					offset = nestedTablesOffset;
				}
			}
		}
		return offset;
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		startRow = state.rowNum;
		startCol = state.colNum;
		
		log.debug( "startTable @ [", startRow, ",", startCol, "]" );

		repeatHeader = false;
		Object genBy = ((IContent)table).getGenerateBy();
		if( genBy instanceof TableItemDesign ) {
			TableItemDesign design = (TableItemDesign)genBy;
			repeatHeader = ( design.getPageBreakInterval() == 0 ) && design.isRepeatHeader();
		} else if( genBy instanceof ExtendedItemDesign ) {
			ExtendedItemDesign extDesign = (ExtendedItemDesign)genBy;
			DesignElementHandle handle = extDesign.getHandle();
			Object rowPageBreakInterval = handle.getProperty( "rowPageBreakInterval" );
			if( rowPageBreakInterval == null ) {
				rowPageBreakInterval = 40;
			}
			Object repeatRowHeader      = handle.getProperty( "repeatRowHeader" );
			if( repeatRowHeader == null ) {
				repeatRowHeader = Boolean.TRUE;
			}
			
			if( ( rowPageBreakInterval instanceof Integer ) && ( repeatRowHeader instanceof Boolean ) ) {
				repeatHeader = (((Integer)rowPageBreakInterval).intValue() == 0) && (((Boolean)repeatRowHeader).booleanValue());
			}
		}
		
		
		for( int col = 0; col < table.getColumnCount(); ++col ) {
			DimensionType width = table.getColumn(col).getWidth();
			if( width != null ) {
				log.debug( "BIRT table column width: ", col, " = ", width);
				int newWidth = state.getSmu().poiColumnWidthFromDimension(width);
				int oldWidth = state.currentSheet.getColumnWidth(startCol + col);
				if( ( oldWidth == 256 * state.currentSheet.getDefaultColumnWidth() ) || ( newWidth > oldWidth ) ) {
					state.currentSheet.setColumnWidth(startCol + col, newWidth);
				}
			}
		}
		
		tableStyle = new BirtStyle( table );
		borderDefn = AreaBorders.create( -1, startCol, startCol + table.getColumnCount() - 1, startRow, tableStyle );
		if( borderDefn != null ) {
			state.insertBorderOverload(borderDefn);
		}
		
		if( table.getGenerateBy() instanceof GridItemDesign ) {
			startDetailsRow = state.rowNum;
		}
	}
	
	@Override
	public void endTable(HandlerState state, ITableContent table) throws BirtException {
		if( table.getGenerateBy() instanceof GridItemDesign ) {
			endDetailsRow = state.rowNum - 1;
		}
		
		log.debug( "Applying bottom border to [", state.rowNum - 1, ",", startCol, "] - [", state.rowNum - 1, ",", startCol + table.getColumnCount() - 1, "]" );
		state.getSmu().applyBottomBorderToRow( state.getSm(), state.currentSheet, startCol, startCol + table.getColumnCount() - 1, state.rowNum - 1, tableStyle );
		
		if( borderDefn != null ) {
			state.removeBorderOverload(borderDefn);
		}
		
		log.debug( "Details rows from ", startDetailsRow, " to ", endDetailsRow );
		
		int autoWidthStartRow = startDetailsRow;
		if( EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.AUTO_COL_WIDTHS_HEADER, false ) ) {
			autoWidthStartRow = startRow;
		}
		int autoWidthEndRow = endDetailsRow;
		if( EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.AUTO_COL_WIDTHS_FOOTER, false ) ) {
			autoWidthEndRow = state.rowNum - 1;
		}
		
		if( ( autoWidthStartRow >= 0 ) && ( autoWidthEndRow > autoWidthStartRow ) ) {
			boolean forceAutoColWidths = EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.FORCEAUTOCOLWIDTHS_PROP, false );
			for( int col = 0; col < table.getColumnCount(); ++col ) {
				int oldWidth = state.currentSheet.getColumnWidth(col);
				if( forceAutoColWidths || ( oldWidth == 256 * state.currentSheet.getDefaultColumnWidth() ) ) {
					FilteredSheet filteredSheet = new FilteredSheet( state.currentSheet, autoWidthStartRow, Math.min(autoWidthEndRow, autoWidthStartRow + 12) );
			        double calcWidth = SheetUtil.getColumnWidth( filteredSheet, col, false );

			        if (calcWidth > 1.0) {
			        	calcWidth *= 256;
			            int maxColumnWidth = 255*256; // The maximum column width for an individual cell is 255 characters
			            if (calcWidth > maxColumnWidth) {
			            	calcWidth = maxColumnWidth;
			            }
			            if( calcWidth > oldWidth ) {
			            	state.currentSheet.setColumnWidth( col, (int)(calcWidth) );
			            }
			        }
				}
			}
		}
		
		if( ( table.getBookmark() != null ) && ( state.rowNum > startRow ) && ( table.getColumnCount() > 1 ) ) {
			createName(state, prepareName( table.getBookmark() ), startRow, 0, state.rowNum - 1, table.getColumnCount() - 1);
		}
		
		if( EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.DISPLAYFORMULAS_PROP, false ) ) {
			state.currentSheet.setDisplayFormulas(true);
		}
		if( ! EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.DISPLAYGRIDLINES_PROP, true ) ) {
			state.currentSheet.setDisplayGridlines(false);
		}
		if( ! EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.DISPLAYROWCOLHEADINGS_PROP, true ) ) {
			state.currentSheet.setDisplayRowColHeadings(false);
		}
		if( ! EmitterServices.booleanOption( state.getRenderOptions(), table, ExcelEmitter.DISPLAYZEROS_PROP, true ) ) {
			state.currentSheet.setDisplayZeros(false);
		}		
	}

	@Override
	public void startTableBand(HandlerState state, ITableBandContent band) throws BirtException {
		if( ( band.getBandType() == ITableBandContent.BAND_DETAIL ) && ( startDetailsRow < 0 ) ) {
			startDetailsRow = state.rowNum;
		}
		if( ( band.getBandType() == ITableBandContent.BAND_HEADER ) && ( startHeaderRow < 0 )) {
			startHeaderRow = state.rowNum;
		}
		currentBand = band;
	}

	@Override
	public void endTableBand(HandlerState state, ITableBandContent band) throws BirtException {
		if( band.getBandType() == ITableBandContent.BAND_DETAIL ) {
			endDetailsRow = state.rowNum - 1;
		}
		if( ( band.getBandType() == ITableBandContent.BAND_HEADER ) && repeatHeader && (state.rowNum > startHeaderRow) ) {
			int endHeaderRow = state.rowNum - 1;
			
			if( state.currentSheet.getRepeatingRows() == null ) {
				CellRangeAddress repeatingRows = new CellRangeAddress(startHeaderRow, endHeaderRow, -1, -1);
				// repeatingRows = CellRangeAddress.valueOf( Integer.toString(startHeaderRow+1) + ":" + Integer.toString(endHeaderRow+1) );
				state.currentSheet.setRepeatingRows(repeatingRows);
			}
		}
		currentBand = null;
	}

	@Override
	public void startTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
		currentGroup = group;
	}

	@Override
	public void endTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
		currentGroup = null;
	}

}
