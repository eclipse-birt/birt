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

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

import uk.co.spudsoft.birt.emitters.excel.CellImage;
import uk.co.spudsoft.birt.emitters.excel.ClientAnchorConversions;
import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.StyleManagerUtils;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class PageHandler extends AbstractHandler {
	
	private static Pattern INVALID_CHARS_REGEX = Pattern.compile( "[/\\\\*'?\\[\\]:]+" );
	
	boolean created;
	
	public PageHandler(Logger log, IPageContent page) {
		super(log, null, page);
	}

	private void setupPageSize(HandlerState state, IPageContent page) {
		PrintSetup printSetup = state.currentSheet.getPrintSetup();
		printSetup.setPaperSize(state.getSmu().getPaperSizeFromString(page.getPageType()));
		if( page.getOrientation() != null ) {
			if( "landscape".equals(page.getOrientation())) {
				printSetup.setLandscape(true);
			}
		}
	}
	
	private String contentAsString( HandlerState state, Object obj ) throws BirtException {
		
		StringCellHandler stringCellHandler = new StringCellHandler( state.getEmitter(), log, this, 
				obj instanceof CellContent ? (CellContent)obj : null );
		
		state.setHandler(stringCellHandler);
		
		stringCellHandler.visit(obj);
		
		state.setHandler(this);
		
		return stringCellHandler.getString();
	}
	
	@SuppressWarnings("rawtypes") 
	private void processHeaderFooter( HandlerState state, Collection birtHeaderFooter, HeaderFooter poiHeaderFooter ) throws BirtException {
		boolean handledAsGrid = false;
		for( Object ftrObject : birtHeaderFooter ) {
			if( ftrObject instanceof ITableContent ) {
				ITableContent ftrTable = (ITableContent)ftrObject;
				if( ftrTable.getChildren().size() == 1 ) {
					Object child = ftrTable.getChildren().toArray()[ 0 ];
					if( child instanceof IRowContent ) {
						IRowContent row = (IRowContent)child;
						if( ftrTable.getColumnCount() <= 3 ) {
							Object[] cellObjects = row.getChildren().toArray();
							if( ftrTable.getColumnCount() == 1 ) {
								poiHeaderFooter.setLeft( contentAsString( state, cellObjects[ 0 ] ) );
								handledAsGrid = true;
							} else if( ftrTable.getColumnCount() == 2 ) {
								poiHeaderFooter.setLeft( contentAsString( state, cellObjects[ 0 ] ) );
								poiHeaderFooter.setRight( contentAsString( state, cellObjects[ 1 ] ) );
								handledAsGrid = true;
							} else if( ftrTable.getColumnCount() == 3 ) {
								poiHeaderFooter.setLeft( contentAsString( state, cellObjects[ 0 ] ) );
								poiHeaderFooter.setCenter( contentAsString( state, cellObjects[ 1 ] ) );
								poiHeaderFooter.setRight( contentAsString( state, cellObjects[ 2 ] ) );
								handledAsGrid = true;
							}
						}
					}
				}
			}
			if( ! handledAsGrid ) {
				poiHeaderFooter.setLeft( contentAsString( state, ftrObject ) );
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void outputStructuredHeaderFooter( HandlerState state, Collection birtHeaderFooter ) throws BirtException {
		ContentEmitterVisitor visitor = new ContentEmitterVisitor(state.getEmitter());
		for( Object content : birtHeaderFooter ) {
			if( content instanceof IContent ) {
				visitor.visit((IContent)content, null);
			}
		}
		
	}
	
	private void cleanSheet( Sheet sheet ) {
		for( int i = 0; i < sheet.getNumMergedRegions(); ++i ) {
			sheet.removeMergedRegion(i);
		}
		while( sheet.getPhysicalNumberOfRows() > 0 ) {
			Row row = sheet.getRow(sheet.getLastRowNum());
			sheet.removeRow(row);
		}
	}
	
	@Override
	public void startPage(HandlerState state, IPageContent page) throws BirtException {
		
		element = page;
		
		if( state.getWb().getNumberOfSheets() > 0 ) {
			if( EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET_PAGE_BREAKS, false ) ) {
				state.currentSheet.setRowBreak( state.rowNum - 1 );
			}
			if( EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET, false )  ) {
				return ;
			}
		}
		
	    state.currentSheet = null;
	    created = false;
	}
	
	private void createSheet(HandlerState state, IContent newElement) throws BirtException {
		if( created ) {
			return ;
		}
		IPageContent page = (IPageContent)element;
		if( EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET, false )  
				&& state.getWb().getNumberOfSheets() > 0 ) {
			return ;
		}		
		
		String pageLabel = null;
		IReportContext reportContext = page.getReportContent().getReportContext();
		if( reportContext != null ) {
			Object pageLabelObject = reportContext.getPageVariable( IReportContext.PAGE_VAR_PAGE_LABEL );
			if( pageLabelObject instanceof String ) {
				pageLabel = (String)pageLabel;
			}
		}
		if( pageLabel == null ) {
			String name = newElement.getName();
			if( ( name != null ) && ! name.isEmpty() ) {
				pageLabel = name;
			}
		}
		if( pageLabel == null ) {
			String name = page.getReportContent().getTitle();
			if( ( name != null ) && ! name.isEmpty() ) {
				pageLabel = name;
			}
		}
			
		if( pageLabel != null ) {
			state.sheetName = null;
			String sheetName = prepareSheetName(state, pageLabel);
			int sheetIndex = findNamedSheetIndex( state.getWb(), sheetName );
			if( sheetIndex >= 0 ) {
				state.currentSheet = state.getWb().getSheetAt( sheetIndex );
				cleanSheet( state.currentSheet );
			} else {
				state.currentSheet = state.getWb().createSheet( sheetName );
			}
		} else {
			state.currentSheet = state.getWb().createSheet();
		}
		created = true;
		
		log.debug("Page type: ", page.getPageType());
		
		if( page.getPageType() != null ) {
			setupPageSize(state, page);
		}
		
		if( EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.DISPLAYFORMULAS_PROP, false ) ) {
			state.currentSheet.setDisplayFormulas(true);
		}
		if( ! EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.DISPLAYGRIDLINES_PROP, true ) ) {
			state.currentSheet.setDisplayGridlines(false);
		}
		if( ! EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.DISPLAYROWCOLHEADINGS_PROP, true ) ) {
			state.currentSheet.setDisplayRowColHeadings(false);
		}
		if( ! EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.DISPLAYZEROS_PROP, true ) ) {
			state.currentSheet.setDisplayZeros(false);
		}
		int pagesHigh = EmitterServices.integerOption( state.getRenderOptions(), page, ExcelEmitter.PRINT_PAGES_HIGH, -1 );
		if( ( pagesHigh > 0 ) && ( pagesHigh < Short.MAX_VALUE ) ) {
			state.currentSheet.getPrintSetup().setFitHeight((short)pagesHigh);
			state.currentSheet.setAutobreaks(true);
		}
		int pagesWide = EmitterServices.integerOption( state.getRenderOptions(), page, ExcelEmitter.PRINT_PAGES_WIDE, -1 );
		if( ( pagesWide > 0 ) && ( pagesWide < Short.MAX_VALUE ) ) {
			state.currentSheet.getPrintSetup().setFitWidth((short)pagesWide);
			state.currentSheet.setAutobreaks(true);
		}
		int printScale = EmitterServices.integerOption( state.getRenderOptions(), page, ExcelEmitter.PRINT_SCALE, -1 );
		if( ( printScale > 0 ) && ( printScale < Short.MAX_VALUE ) ) {
			state.currentSheet.getPrintSetup().setScale((short)printScale);
		}
		
		boolean structuredHeader =
				! state.getEmitter().isExtractMode()
				&& EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.STRUCTURED_HEADER, false );
		if( structuredHeader ) {
			outputStructuredHeaderFooter(state, page.getHeader());
		} else {
			processHeaderFooter(state, page.getHeader(), state.currentSheet.getHeader() );
			processHeaderFooter(state, page.getFooter(), state.currentSheet.getFooter() );
		} 
		
		state.getSmu().prepareMarginDimensions(state.currentSheet, page, !structuredHeader);
	}
	
	private String prepareSheetName( HandlerState state, String proposedName ) {
		// Strip invalid chars
		proposedName = INVALID_CHARS_REGEX.matcher(proposedName).replaceAll(" ");
		
		String preparedName = proposedName.length() > 31 ? proposedName.substring(0,31) : proposedName;
		
		// Check whether there are really any sheets with that name
		boolean found = false;
		Workbook wb = state.getWb();
		for( int i = 0; i < wb.getNumberOfSheets(); ++i ) {
			if( wb.getSheetName( i ).startsWith( preparedName ) ) {
				found = true;
				break;
			}
		}
		if( ! found ) {
			state.sheetNames.remove(preparedName);
		}
				
		Integer nameCount = state.sheetNames.get(preparedName);
		if( nameCount != null ) {
			++nameCount;
			String suffix = " " + nameCount;			
			state.sheetNames.put(preparedName, nameCount);
			if( preparedName.length() > 31 - suffix.length() ) {
				preparedName = preparedName.substring(0,31 - suffix.length()) + " " + nameCount;
			} else {
				preparedName = preparedName + " " + nameCount;
			}
		} else {
			state.sheetNames.put(preparedName,1);
		}
		return preparedName;
	}
	
	private int findNamedSheetIndex( Workbook workbook, String sheetName ) {
		for( int i = 0; i < workbook.getNumberOfSheets(); ++i ) {
			if( workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
				log.debug("Found matching sheet at ", i, " \"", workbook.getSheetName(i), "\"" );
				return i;
			}
		}
		return -1;
	}
	
	private int sheetIndex( Workbook workbook, Sheet sheet ) {
		for( int i = 0; i < workbook.getNumberOfSheets(); ++i ) {
			if( workbook.getSheetAt(i) == sheet ) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void endPage(HandlerState state, IPageContent page) throws BirtException {
		
		if( ( state.sheetName != null ) && ! state.sheetName.isEmpty() ) {
			String newSheetName = prepareSheetName( state, state.sheetName );
			state.getWb().setSheetName( sheetIndex( state.getWb(), state.currentSheet ), newSheetName );
		}
		
		if( EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.SINGLE_SHEET, false )  
			&& ! state.reportEnding ) {
			return ;
		}		
		
		if( EmitterServices.booleanOption( state.getRenderOptions(), page, ExcelEmitter.STRUCTURED_HEADER, false ) ) {
			outputStructuredHeaderFooter(state, page.getFooter());
		} 
		
		if( state.sheetPassword != null ) {
			log.debug("Attempting to protect sheet ", ( state.getWb().getNumberOfSheets() - 1 ) );
			state.currentSheet.protectSheet( state.sheetPassword );
			state.sheetPassword = null;
		}

		Drawing drawing = null;
		if( ! state.images.isEmpty() ) {
			drawing = state.currentSheet.createDrawingPatriarch();
		}
		for( CellImage cellImage : state.images ) {
			processCellImage(state,drawing,cellImage);
		}
		state.images.clear();
		state.rowNum = 0;
		state.colNum = 0;
		state.clearRowSpans();
		state.areaBorders.clear();
		
		created = false;		
		state.currentSheet = null;
		state.sheetName = null;
	}
	
	private CellRangeAddress getMergedRegionBegunBy( Sheet sheet, int row, int col ) {
		for( int i = 0; i < sheet.getNumMergedRegions(); ++i ) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if( ( range.getFirstColumn() == col ) && ( range.getFirstRow() == row ) ) {
				return range;
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Process a CellImage from the images list and place the image on the sheet.
	 * </p><p>
	 * This involves changing the row height as necesssary and determining the column spread of the image.
	 * </p>
	 * @param cellImage
	 * The image to be placed on the sheet.
	 */
	private void processCellImage( HandlerState state, Drawing drawing, CellImage cellImage ) {
		Coordinate location = cellImage.location;
		
		if( state.currentSheet.getRow( location.getRow() ) == null ) {
			state.currentSheet.createRow( location.getRow() );
		}
		if( state.currentSheet.getRow( location.getRow() ).getCell( location.getCol() ) == null ) {
			state.currentSheet.getRow( location.getRow() ).createCell( location.getCol() );
		}
		Cell cell = state.currentSheet.getRow( location.getRow() ).getCell( location.getCol() );

		IImageContent image = cellImage.image;		
		
		StyleManagerUtils smu = state.getSmu();
		float ptHeight = cell.getRow().getHeightInPoints();
		if( image.getHeight() != null ) {
			ptHeight = smu.fontSizeInPoints( image.getHeight().toString() );
		}

		// Get image width
		int endCol = cell.getColumnIndex();
        double lastColWidth = ClientAnchorConversions.widthUnits2Millimetres( (short)state.currentSheet.getColumnWidth( endCol ) )
        		+ 2.0;
        double mmWidth = 0.0;
        if( smu.isAbsolute(image.getWidth())) {
            mmWidth = image.getWidth().convertTo(DimensionType.UNITS_MM);
        } else if(smu.isPixels(image.getWidth())) {
            mmWidth = ClientAnchorConversions.pixels2Millimetres( image.getWidth().getMeasure() );
        }
        int dx = smu.anchorDxFromMM( mmWidth, lastColWidth );
		// Allow image to span multiple columns
		CellRangeAddress mergedRegion = getMergedRegionBegunBy( state.currentSheet, location.getRow(), location.getCol() );
		if( (cellImage.spanColumns) || ( mergedRegion != null ) ) {
	        log.debug( "Image size: ", image.getWidth(), " translates as mmWidth = ", mmWidth );
	        if( mmWidth > 0) {
	            double mmAccumulatedWidth = 0;
	            int endColLimit = cellImage.spanColumns ? 256 : mergedRegion.getLastColumn();
	            for( endCol = cell.getColumnIndex(); mmAccumulatedWidth < mmWidth && endCol < endColLimit; ++ endCol ) {
	                lastColWidth = ClientAnchorConversions.widthUnits2Millimetres( (short)state.currentSheet.getColumnWidth( endCol ) )
	                		+ 2.0;
	                mmAccumulatedWidth += lastColWidth;
	                log.debug( "lastColWidth = ", lastColWidth, "; mmAccumulatedWidth = ", mmAccumulatedWidth);
	            }
	            if( mmAccumulatedWidth > mmWidth ) {
	                mmAccumulatedWidth -= lastColWidth;
	                --endCol;
	                double mmShort = mmWidth - mmAccumulatedWidth;
	                dx = smu.anchorDxFromMM( mmShort, lastColWidth );
	            }
	        }
		}

		int rowsSpanned = state.findRowsSpanned( cell.getRowIndex(), cell.getColumnIndex() );
		float neededRowHeightPoints = ptHeight;
		
		for( int i = 0; i < rowsSpanned; ++i ) {
			int rowIndex = cell.getRowIndex() + 1 + i;
			if( state.currentSheet.getRow(rowIndex) != null ) {
				neededRowHeightPoints -= state.currentSheet.getRow(rowIndex).getHeightInPoints();
			}
		}
		
		if( neededRowHeightPoints > cell.getRow().getHeightInPoints()) {
			cell.getRow().setHeightInPoints( neededRowHeightPoints );
		}
		
		// ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
		ClientAnchor anchor = state.getWb().getCreationHelper().createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setCol2(endCol);
        anchor.setRow2(cell.getRowIndex() + rowsSpanned);
        anchor.setDx2(dx);
        anchor.setDy2( smu.anchorDyFromPoints( ptHeight, cell.getRow().getHeightInPoints() ) );
        anchor.setAnchorType(ClientAnchor.MOVE_DONT_RESIZE);
	    drawing.createPicture(anchor, cellImage.imageIdx);
	}
	
	
	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		createSheet( state, list );
		state.setHandler(new TopLevelListHandler(log,this,list));
		state.getHandler().startList(state, list);
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		createSheet( state, table );
		state.setHandler(new TopLevelTableHandler(log,this,table));
		state.getHandler().startTable(state, table);
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		createSheet( state, text );
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitText(state, text);
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		createSheet( state, data );
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitData(state, data);
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		createSheet( state, label );
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitLabel(state, label);
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		createSheet( state, autoText );
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitAutoText(state, autoText);
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {
		createSheet( state, foreign );
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitForeign(state, foreign);
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		createSheet( state, image );
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitImage(state, image);
	}
	
	
	
	
}
