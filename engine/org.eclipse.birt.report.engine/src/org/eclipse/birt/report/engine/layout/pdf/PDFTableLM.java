/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.IPDFTableLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class PDFTableLM extends PDFBlockStackingLM
		implements
			IPDFTableLayoutManager,
			IBlockStackingLayoutManager
{

	/**
	 * hold all unresolved drop cell area
	 */
	protected ArrayList dropList = new ArrayList( );

	/**
	 * Border conflict resolver
	 */
	protected BorderConflictResolver bcr = new BorderConflictResolver( );

	/**
	 * table content
	 */
	private ITableContent tableContent;

	/**
	 * identify if repeat header
	 */
	protected boolean repeatHeader;

	/**
	 * number of table column
	 */
	protected int columnNumber;

	/**
	 * table width
	 */
	protected int tableWidth;

	protected CellWrapper[] lastRowContent = null;

	protected CellWrapper[] currentRowContent = null;

	protected RowWrapper lastRow = null;

	protected RowWrapper currentRow = null;

	protected RowArea lastRowArea = null;

	protected TableLayoutInfo layoutInfo = null;

	protected PDFTableRegionLM regionLM = null;

	/**
	 * current row id
	 */
	protected int currentRowID = 0;

	protected int hiddenRowCount = 0;

	protected ITableBandContent currentBand = null;

	protected int repeatRowCount = 0;
	
	protected boolean tablepaginated = false;
	
	protected ITableCloseState pageBreakState;
	
	protected final ITableCloseState FORCED_PAGE_BREAK_STATE = new ForcedPageBreakState();
	
	protected final ITableCloseState AUTO_PAGE_BREAK_STATE = new AutoPageBreakState();
	
	protected final ITableCloseState TABLE_TERMINATED_STATE = new TableTerminatedState();

	protected Stack groupStack = new Stack( );
	
	protected ColumnWidthResolver columnWidthResolver;

	protected int rowCount = 0;
	public PDFTableLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		tableContent = (ITableContent) content;
		columnWidthResolver = new ColumnWidthResolver(tableContent);
		repeatHeader = tableContent.isHeaderRepeat( );
		columnNumber = tableContent.getColumnCount( );
		lastRowContent = new CellWrapper[columnNumber];
		currentRowContent = new CellWrapper[columnNumber];
		pageBreakState = AUTO_PAGE_BREAK_STATE;
	}

	public int getRepeatCount( )
	{
		return this.repeatRowCount;
	}

	public void setRepeatCount( int repeatRowCount )
	{
		this.repeatRowCount = repeatRowCount;
	}

	protected boolean traverseChildren( )
	{
		repeat( );
		return super.traverseChildren( );
	}

	protected void repeat( )
	{
		addCaption( tableContent.getCaption( ) );
		repeatHeader( );
	}

	public void startGroup( IGroupContent groupContent )
	{
		int groupLevel = groupContent.getGroupLevel( );
		groupStack.push( new Integer( groupLevel ) );
	}

	public void endGroup( IGroupContent groupContent )
	{
	    // if there is no group footer, we still need to do with the drop.
		int groupLevel = groupContent.getGroupLevel( );
		updateUnresolvedCell( groupLevel, false );
		updateUnresolvedCell( groupLevel, true );
		assert ( !groupStack.isEmpty( ) );
		groupStack.pop( );
	}

	private int getGroupLevel( )
	{
		if ( !groupStack.isEmpty( ) )
		{
			return ( (Integer) groupStack.peek( ) ).intValue( );
		}
		return -1;
	}

	/**
	 * start row update content cache
	 * 
	 * @param row
	 */
	public void startRow( IRowContent row )
	{
		currentRowID++;
		rowCount++;
		CellWrapper[] newRowContent = new CellWrapper[columnNumber];
		if ( currentRowID > 0 )
		{
			ArrayList removedDropCells = new ArrayList();
			for ( int i = 0; i < columnNumber; i++ )
			{
				
				if ( currentRowContent[i] != null )
				{
					int rowSpan = currentRowContent[i].rowSpan;
					if ( rowSpan > 0
							&& currentRowID < rowSpan
									+ currentRowContent[i].rowID || rowSpan < 0 )// Grid
					{
						// grid row span or table drop
						newRowContent[i] = currentRowContent[i];
					}
					else
					{
						// end span
						newRowContent[i] = null;
						removedDropCells.add(new Integer(i));						
					}
				}
			}
			if(removedDropCells.size( )>0)
			{
				removeDropCells(removedDropCells);
			}
		}
		lastRowContent = currentRowContent;
		lastRow = currentRow;
		currentRowContent = newRowContent;
		currentRow = new RowWrapper( row, currentRowID );
	}
	
	protected void removeDropCells(ArrayList list)
	{
		int maxHeight = 0;
		Iterator iter = dropList.iterator( );
		ArrayList removedList = new ArrayList();
		while ( iter.hasNext( ) )
		{
			DropCellInfo dropCell = (DropCellInfo) iter.next( );
			int columnID = dropCell.cell.getColumnID( );
			if(list.contains( new Integer(columnID) ))
			{
				verticalAlign( dropCell.cell );
				iter.remove( );
				maxHeight = Math.max( maxHeight, dropCell.leftHeight );
				removedList.add( dropCell);
				
			}
		}
		iter = dropList.iterator();
		while(iter.hasNext())
		{
			DropCellInfo dropCell = (DropCellInfo) iter.next( );
			int left = dropCell.leftHeight;
			dropCell.leftHeight -= maxHeight;
			if(dropCell.leftHeight < 0)
			{
				dropCell.cell.setHeight(dropCell.cell.getHeight( ) + maxHeight - left);
				dropCell.leftHeight = 0;
			}
		}
		if(maxHeight>0)
		{
			if(lastRowArea!=null)
			{
				lastRowArea.setHeight( lastRowArea.getHeight( ) + maxHeight );
				Iterator rowIter = lastRowArea.getChildren( );
				while(rowIter.hasNext( ))
				{
					CellArea cell = (CellArea)rowIter.next( );
					if(cell.getRowSpan() == 1)
						cell.setHeight( cell.getHeight( ) + maxHeight );
				}
			}
			Iterator removedIter = removedList.iterator( );
			while(removedIter.hasNext( ))
			{
				DropCellInfo cell = (DropCellInfo)removedIter.next( );
				cell.cell.setHeight( cell.cell.getHeight( ) + maxHeight - cell.leftHeight );
			}
			currentBP += maxHeight;
		}
	}

	protected void removeDropAreaBySpan( int rowSpan )
	{
		int maxHeight = 0;
		Iterator iter = dropList.iterator( );
		ArrayList removedList = new ArrayList();
		while ( iter.hasNext( ) )
		{
			DropCellInfo dropCell = (DropCellInfo) iter.next( );
			if ( dropCell.rowSpan == rowSpan )
			{
				verticalAlign( dropCell.cell );
				iter.remove( );
				removedList.add( dropCell );
				maxHeight = Math.max( maxHeight, dropCell.leftHeight );
			}
		}
		if(maxHeight>0)
		{
			if(lastRowArea!=null)
			{
				lastRowArea.setHeight( lastRowArea.getHeight( ) + maxHeight );
				Iterator rowIter = lastRowArea.getChildren( );
				while(rowIter.hasNext( ))
				{
					CellArea cell = (CellArea)rowIter.next( );
					cell.setHeight( cell.getHeight( ) + maxHeight );
				}
			}
			Iterator removedIter = removedList.iterator( );
			while(removedIter.hasNext( ))
			{
				DropCellInfo cell = (DropCellInfo)removedIter.next( );
				cell.cell.setHeight( cell.cell.getHeight( ) + maxHeight - cell.leftHeight );
			}
			currentBP += maxHeight;
		}
	}

	private int createDropID( int groupIndex, String dropType )
	{
		int dropId = -10 * ( groupIndex + 1 );
		if ( "all".equals( dropType ) ) //$NON-NLS-1$
		{
			dropId--;
		}
		return dropId;
	}

	/**
	 * start cell update content cache
	 * 
	 * @param cell
	 */
	public void startCell( ICellContent cell )
	{
		int groupLevel = getGroupLevel( );
		int rowSpan = cell.getRowSpan( );
		if ( groupLevel >= 0 )
		{
			CellDesign cellDesign = (CellDesign) cell.getGenerateBy( );
			if ( cellDesign != null )
			{
				String dropType = cellDesign.getDrop( );
				if ( dropType != null && !"none".equals( dropType ) ) //$NON-NLS-1$
				{
					rowSpan = createDropID( groupLevel, dropType );
				}
			}
		}
		for ( int i = cell.getColumn( ); i < cell.getColumn( )
				+ cell.getColSpan( ); i++ )
		{
			if ( currentRowContent[i] == null )
			{
				currentRowContent[i] = new CellWrapper( cell, currentRowID,
						rowSpan );
			}
		}

	}

	// drop cell before takes precedence
	public boolean isCellVisible( ICellContent cell )
	{
		if ( cell == null )
		{
			return false;
		}
		return currentRowContent[cell.getColumn( )].cell == cell;
	}

	protected void createRoot( )
	{
		root = (ContainerArea) AreaFactory
				.createTableArea( (ITableContent) content );
		root.setWidth( tableWidth );
		if ( !isFirst )
		{
			root.getStyle( ).setMarginTop( "0" ); //$NON-NLS-1$
		}
	}

	public TableLayoutInfo getLayoutInfo( )
	{
		return this.layoutInfo;
	}

	protected void buildTableLayoutInfo( )
	{
		//this.layoutInfo = new TableLayoutInfo( resolveColumnWidth( ) );
		//this.layoutInfo = resolveTableLayoutInfo( (TableArea)root );
		this.layoutInfo = resolveTableFixedLayout((TableArea)root );
	}

	protected void newContext( )
	{
		createRoot( );
		buildTableLayoutInfo( );
		root.setWidth( layoutInfo.getTableWidth( ) );
		setMaxAvaHeight( getAvaHeight( ) );
		setMaxAvaWidth( layoutInfo.getTableWidth( ) );
		setCurrentIP( 0 );
		setCurrentBP( 0 );
		repeatRowCount = 0;
		rowCount = 0;
		lastRowArea = null;
		setCurrentIP( 0 );
		
	}

	protected int getAvaHeight( )
	{
		root.setAllocatedHeight( parent.getMaxAvaHeight( )
				- parent.getCurrentBP( ) );
		return root.getContentHeight( );

	}

	private void closeLayout( IBottomBorderResolverStrategy bbr )
	{
 		if ( root.getChildrenCount( ) == 0 )
		{
			return;
		}
 		
 		ArrayList lastDropCells = new ArrayList();
 		Iterator it = dropList.iterator( );
 		while ( it.hasNext( ) )
		{
			DropCellInfo dropCell = (DropCellInfo) it.next( );
			for (int columnID = dropCell.cell.getColumnID(); columnID < dropCell.cell
					.getColumnID()
					+ dropCell.cell.getColSpan(); columnID++)
				lastDropCells.add(new Integer(columnID));
		}
 		
		// resolve bottom border for last row of this table
		ArrayList list = this.dropList;
		int bottomMaxBorder = 0;
		ArrayList changed = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			DropCellInfo cell = (DropCellInfo) list.get( i );
			bottomMaxBorder = Math.max( bbr.resolveBottomBorder( cell.cell ),
					bottomMaxBorder );
			changed.add( cell.cell );
		}
		
		updateAllUnresolvedCellArea(lastDropCells);

		if ( lastRowArea != null )
		{
			Iterator iter = lastRowArea.getChildren( );
			while ( iter.hasNext( ) )
			{
				CellArea cell = (CellArea) iter.next( );
				if ( !changed.contains( cell ) )
				{
					bottomMaxBorder = Math.max( bbr.resolveBottomBorder( cell ),
							bottomMaxBorder );
					changed.add( cell );
				}
			}
		}
		if ( bottomMaxBorder > 0 )
		{
			// update dimension of each cell in list
			for ( int i = 0; i < changed.size( ); i++ )
			{
				CellArea cell = (CellArea) changed.get( i );
				cell.setHeight( cell.getHeight( ) + bottomMaxBorder );
			}

			// update height of last row
			if ( lastRowArea != null )
			{
				lastRowArea.setHeight( lastRowArea.getHeight( ) + bottomMaxBorder );
				if(currentBP + bottomMaxBorder < maxAvaHeight)
				{
					currentBP += bottomMaxBorder;
				}
			}

		}
		// update dimension of table area
		root.setHeight( getCurrentBP( ) + getOffsetY( ));

	}
	
	public class ForcedPageBreakState
			implements
				IPDFTableLayoutManager.ITableCloseState
	{

		public void closeLayout( )
		{
			PDFTableLM.this.closeForcedPageBreak( );
		}
	}

	public class AutoPageBreakState
			implements
				IPDFTableLayoutManager.ITableCloseState
	{

		public void closeLayout( )
		{
			PDFTableLM.this.closeAutoPageBreak( );
		}
	}

	public class TableTerminatedState
			implements
				IPDFTableLayoutManager.ITableCloseState
	{

		public void closeLayout( )
		{
			PDFTableLM.this.closeTableLayout( );
		}
	}

	protected void changeTableCloseState( ITableCloseState tableCloseState )
	{
		pageBreakState = tableCloseState;
	}

	public void setTableCloseStateAsForced( )
	{
		changeTableCloseState( FORCED_PAGE_BREAK_STATE );
	}

	public void setTableCloseStateAsAuto( )
	{
		changeTableCloseState( AUTO_PAGE_BREAK_STATE );
	}

	public void setTableCloseStateAsTerminated( )
	{
		changeTableCloseState( TABLE_TERMINATED_STATE );
	}

	protected void closeLayout( )
	{
		if ( isLast )
			changeTableCloseState( TABLE_TERMINATED_STATE );
		pageBreakState.closeLayout( );
		changeTableCloseState( AUTO_PAGE_BREAK_STATE );
	}

	interface IBottomBorderResolverStrategy
	{
		int resolveBottomBorder( CellArea cell );
	}

	protected void closeTableLayout( )
	{
		closeLayout( new IBottomBorderResolverStrategy( ) {

			public int resolveBottomBorder( CellArea cell )
			{
				IStyle tableStyle = tableContent.getComputedStyle( );
				IContent cellContent = cell.getContent( );
				IStyle rowStyle = ((IContent)cellContent.getParent()).getComputedStyle( ) ;
				IStyle columnStyle = getColumnStyle( cell.getColumnID( ) );
				IStyle cellContentStyle = cellContent.getComputedStyle( );
				IStyle cellAreaStyle = cell.getStyle( );
				bcr.resolveTableBottomBorder( tableStyle, rowStyle,
						columnStyle, cellContentStyle, cellAreaStyle );
				return getDimensionValue( cellAreaStyle
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
			}
		} );
	}

	protected void closeForcedPageBreak( )
	{
		closeLayout( new IBottomBorderResolverStrategy( ) {

			public int resolveBottomBorder( CellArea cell )
			{
				IContent cellContent = cell.getContent( );
				IStyle rowStyle = ((IContent)cellContent.getParent()).getComputedStyle( ) ;
				IStyle cellContentStyle = cellContent.getComputedStyle( );
				IStyle cellAreaStyle = cell.getStyle( );
				bcr.resolvePagenatedTableBottomBorder( rowStyle,
						cellContentStyle, cellAreaStyle );
				return getDimensionValue( cellAreaStyle
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
			}
		} );
		tablepaginated = true;
	}

	protected void closeAutoPageBreak( )
	{
		closeLayout( new IBottomBorderResolverStrategy( ) {

			public int resolveBottomBorder( CellArea cell )
			{
				return 0;
			}
		} );
	}
	
	public int getColumnNumber( )
	{
		return columnNumber;
	}

	/**
	 * resolve cell border conflict
	 * 
	 * @param cellArea
	 */
	public void resolveBorderConflict( CellArea cellArea )
	{
		IContent cellContent = cellArea.getContent( );
		int columnID = cellArea.getColumnID( );
		int rowID = cellArea.getRowID( );
		int colSpan = cellArea.getColSpan( );
		IRowContent row = (IRowContent) cellContent.getParent( );
		IStyle cellContentStyle = cellContent.getComputedStyle( );
		IStyle cellAreaStyle = cellArea.getStyle( );
		IStyle tableStyle = tableContent.getComputedStyle( );
		IStyle rowStyle = row.getComputedStyle( );
		IStyle columnStyle = getColumnStyle( columnID );
		IStyle preRowStyle = null;
		IStyle preColumnStyle = getColumnStyle( columnID - 1 );
		IStyle leftCellContentStyle = null;
		IStyle topCellStyle = null;

		if ( columnID > 0 && currentRowContent[columnID - 1] != null )
		{
			leftCellContentStyle = currentRowContent[columnID - 1].cell
					.getComputedStyle( );
		}
		if(rowCount>1 && lastRow!=null)
		{
			preRowStyle = lastRow.row.getComputedStyle( );
			if ( lastRowContent[columnID] != null )
			{
				topCellStyle = lastRowContent[columnID].cell
						.getComputedStyle( );
			}
		}
		else
		{
			if ( lastRowArea != null )
			{
				preRowStyle = lastRowArea.getContent( ).getComputedStyle( );
				Iterator iter = lastRowArea.getChildren( );
				while ( iter.hasNext( ) )
				{
					CellArea cell = (CellArea) iter.next( );
					ICellContent cc = (ICellContent) cell.getContent( );
					if ( cc != null && cc.getColumn( ) == columnID )
					{
						topCellStyle = cc.getComputedStyle( );
						break;
					}
				}
			}
		}
					
		if ( rowID == 0 )
		{
			// resolve top border
			if ( tablepaginated )
			{
				bcr.resolvePagenatedTableTopBorder( rowStyle, cellContentStyle,
						cellAreaStyle );
				tablepaginated = false;
			}
			else
			{
				bcr.resolveTableTopBorder( tableStyle, rowStyle, columnStyle,
					cellContentStyle, cellAreaStyle );
			}

			// resolve left border
			if ( columnID == 0 )
			{
				bcr.resolveTableLeftBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}
			else
			{
				bcr.resolveCellLeftBorder( preColumnStyle, columnStyle,
						leftCellContentStyle, cellContentStyle, cellAreaStyle );
			}

			// resovle right border

			if ( columnID + colSpan == columnNumber )
			{
				bcr.resolveTableRightBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}

		}
		else
		{
			bcr.resolveCellTopBorder( preRowStyle, rowStyle, topCellStyle,
						cellContentStyle, cellAreaStyle );
			// resolve left border
			if ( columnID == 0 )
			{
				// first column
				bcr.resolveTableLeftBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}
			else
			{
				// TODO fix row span conflict
				bcr.resolveCellLeftBorder( preColumnStyle, columnStyle,
						leftCellContentStyle, cellContentStyle, cellAreaStyle );
			}
			// resolve right border
			if ( columnID + colSpan == columnNumber )
			{
				bcr.resolveTableRightBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}
		}

	}
	
	
	/**
	 * get column style
	 * 
	 * @param columnID
	 * @return
	 */
	private IStyle getColumnStyle( int columnID )
	{
		// current not support column style
		return null;
	}

	private class ColumnWidthResolver
	{
		ITableContent table;
		
		public ColumnWidthResolver(ITableContent table)
		{
			this.table = table;
		}
		
		protected void formalize(DimensionType[] columns, int tableWidth)
		{
			ArrayList percentageList = new ArrayList();
			ArrayList unsetList = new ArrayList();
			double total = 0.0f;
			int fixedLength = 0;
			for(int i=0; i<columns.length; i++)
			{
				if(columns[i]==null)
				{
					unsetList.add(new Integer(i));
				}
				else if( EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits()))
				{
					percentageList.add(new Integer(i));
					total += columns[i].getMeasure();
				}
				else
				{
					int len = PDFTableLM.this.getDimensionValue(columns[i], tableWidth);
					fixedLength += len;
				}
			}
			
			if(fixedLength>=tableWidth)
			{
				for(int i=0; i<unsetList.size(); i++)
				{
					Integer index = (Integer)unsetList.get(i);
					columns[index.intValue()] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
				}
				for(int i=0; i<percentageList.size(); i++)
				{
					Integer index = (Integer)percentageList.get(i);
					columns[index.intValue()] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
				}
			}
			else
			{
				float leftPercentage = (((float)(tableWidth - fixedLength)) /tableWidth)*100.0f;
				if(unsetList.isEmpty())
				{
					double ratio = leftPercentage/total;
					for(int i=0; i<percentageList.size(); i++)
					{
						Integer index = (Integer)percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index
								.intValue()].getMeasure()
								* ratio, columns[index.intValue()].getUnits());
					}
				}
				else
				{
					
					if(total<leftPercentage)
					{
						double delta = leftPercentage - total;
						for(int i=0; i<unsetList.size(); i++)
						{
							Integer index = (Integer)unsetList.get(i);
							columns[index.intValue()] = new DimensionType(delta
									/ (double) unsetList.size(),
									EngineIRConstants.UNITS_PERCENTAGE);
						}
					}
					else
					{
						double ratio = leftPercentage/total;
						for(int i=0; i<unsetList.size(); i++)
						{
							Integer index = (Integer)unsetList.get(i);
							columns[index.intValue()] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
						}
						for(int i=0; i<percentageList.size(); i++)
						{
							Integer index = (Integer)percentageList.get(i);
							columns[index.intValue()] = new DimensionType(columns[index
									.intValue()].getMeasure()
									* ratio, columns[index.intValue()].getUnits());
						}
					}
				}
			}
		}
		
		protected int[] resolve(int tableWidth, DimensionType[] columns)
		{
			int[] cols = new int[columns.length];
			int total = 0;
			for(int i=0; i<columns.length; i++)
			{
				if(!EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits()))
				{
					cols[i] = PDFTableLM.this.getDimensionValue(columns[i], tableWidth);
					total += cols[i];
				}
			}
			
			if(total > tableWidth)
			{
				for(int i=0; i<columns.length; i++)
				{
					if(EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits()))
					{
						cols[i] = 0;
					}
				}
			}
			else
			{
				int delta = tableWidth - total;
				boolean hasPercentage = false;
				for(int i=0; i<columns.length; i++)
				{
					if(EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits()))
					{
						cols[i] = (int)(tableWidth * columns[i].getMeasure()/100.0d);
						hasPercentage = true;
					}
				}
				if(!hasPercentage)
				{
					int size = 0;
					for(int i=0; i<columns.length; i++)
					{
						if(cols[i]>0)
						{
							size++;
						}
					}
					for(int i=0; i<columns.length; i++)
					{
						if(cols[i]>0)
						{
							cols[i] += delta/size;
						}
					}
				}
			}
			return cols;
		}
		
		public int[] resolveFixedLayout(int maxWidth)
		{
			int columnNumber = table.getColumnCount( );
			DimensionType[] columns = new DimensionType[columnNumber];
			
			//handle visibility
			for(int i=0; i<columnNumber; i++)
			{
				IColumn column = (IColumn) table.getColumn( i );
				DimensionType w = column.getWidth();
				if(PDFTableLM.this.isColumnHidden(column))
				{
					columns[i] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
				}
				else
				{
					if(w==null)
					{
						columns[i] = null;
					}
					else
					{
						columns[i] = new DimensionType(w.getMeasure(), w.getUnits()); 
					}
				}
			}
			
			int specifiedWidth = getDimensionValue( tableContent.getWidth( ), maxWidth );
			int tableWidth;
			if(specifiedWidth>0)
			{
				tableWidth = specifiedWidth;
			}
			else
			{
				tableWidth = maxWidth;
			}
			formalize(columns, tableWidth);
			return resolve(tableWidth, columns);
		}
		
		public int[] resolve(int specifiedWidth, int maxWidth)
		{
			assert(specifiedWidth<=maxWidth);
			int columnNumber = table.getColumnCount( );
			int[] columns = new int[columnNumber];
			int columnWithWidth = 0;
			int colSum = 0;
			
			for ( int j = 0; j < table.getColumnCount( ); j++ )
			{
				IColumn column = (IColumn) table.getColumn( j );
				int columnWidth = getDimensionValue( column.getWidth( ), tableWidth );
				if ( columnWidth > 0 )
				{
					columns[j] = columnWidth;
					colSum += columnWidth;
					columnWithWidth++;
				}
				else
				{
					columns[j] = -1;
				}
			}

			if(columnWithWidth==columnNumber)
			{
				if(colSum<=maxWidth)
				{
					return columns;
				}
				else
				{
					float delta = colSum - maxWidth;
					for ( int i = 0; i < columnNumber; i++ )
					{
						columns[i] -= (int)(delta * columns[i]  / colSum) ;
					}
					return columns;
				}
			}
			else
			{
				if(specifiedWidth==0)
				{
					if(colSum<maxWidth)
					{
						distributeLeftWidth( columns, (maxWidth - colSum)/( columnNumber - columnWithWidth));
					}
					else
					{
						redistributeWidth(columns, colSum - maxWidth + (columnNumber - columnWithWidth) * maxWidth / columnNumber, maxWidth, colSum);
					}
				}
				else
				{
					if(colSum<specifiedWidth)
					{
						distributeLeftWidth( columns, (specifiedWidth - colSum)/(columnNumber - columnWithWidth));
					}
					else 
					{
						if(colSum<maxWidth)
						{
							distributeLeftWidth( columns, (maxWidth - colSum)/(columnNumber - columnWithWidth));
						}
						else
						{
							redistributeWidth(columns, colSum - specifiedWidth + (columnNumber - columnWithWidth) * specifiedWidth / columnNumber, specifiedWidth, colSum);
						}
					}
					
				}
				
			}
			return columns;
		}
		
		private void redistributeWidth(int cols[], int delta,  int sum, int currentSum)
		{
			int avaWidth = sum/cols.length;
			for(int i=0; i<cols.length; i++)
			{
				if(cols[i]<0)
				{
					cols[i] = avaWidth;
				}
				else
				{
					cols[i] -= (int)(((float)cols[i])*delta/currentSum);
				}
			}
			
		}
		private void distributeLeftWidth(int cols[], int avaWidth)
		{
			for(int i=0; i<cols.length; i++)
			{
				if(cols[i]<0)
				{
					cols[i] = avaWidth;
				}
			}
		}		
	}
	
	private boolean isColumnHidden( IColumn column )
	{
		String format = context.getFormat( );
		String formats = column.getVisibleFormat( );
		if ( formats != null
				&& formats.length( ) > 0
				&& ( formats.toUpperCase( ).indexOf( format.toUpperCase( ) ) >= 0 || formats.toUpperCase( )
						.indexOf( BIRTConstants.BIRT_ALL_VALUE.toUpperCase( ) ) >= 0 ) )
		{
			return true;
		}
		return false;
	}

	public boolean addArea( IArea area )
	{
		if( area instanceof RowArea )
		{
			lastRowArea = (RowArea) area;
		}
		return super.addArea( area );
	}

	protected void updateAllUnresolvedCellArea(ArrayList lastDropCells )
	{
		removeDropCells(lastDropCells);
	}

	public void updateUnresolvedCell( int groupLevel, boolean dropAll )
	{
		String dropType = dropAll ? "all" : "detail"; //$NON-NLS-1$ //$NON-NLS-2$
		int dropValue = this.createDropID( groupLevel, dropType );
		removeDropAreaBySpan( dropValue );
		for ( int i = 0; i < this.columnNumber; i++ )
		{
			if ( currentRowContent[i] != null )
			{
				if ( dropValue == currentRowContent[i].rowSpan )
				{
					currentRowContent[i] = null;
				}
			}
		}
	}
	
	protected void validateBoxProperty( IStyle style )
	{
		int maxWidth = 0;
		if ( parent != null )
		{
			maxWidth = parent.getMaxAvaWidth( );
		}
		// support negative margin
		int leftMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_LEFT ), maxWidth );
		int rightMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_RIGHT ), maxWidth );
		int topMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_TOP ), maxWidth );
		int bottomMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_BOTTOM ), maxWidth );


		int[] vs = new int[]{rightMargin, leftMargin};
		resolveBoxConflict( vs, maxWidth );

		int[] hs = new int[]{bottomMargin, topMargin};
		resolveBoxConflict( hs, context.getMaxHeight( ) );

		style.setProperty( IStyle.STYLE_MARGIN_LEFT, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[1] ) );
		style.setProperty( IStyle.STYLE_MARGIN_RIGHT, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[0] ) );
		style.setProperty( IStyle.STYLE_MARGIN_TOP, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[1] ) );
		style.setProperty( IStyle.STYLE_MARGIN_BOTTOM, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[0] ) );
	}
	
	private TableLayoutInfo resolveTableFixedLayout(TableArea area)
	{
		assert(parent!=null);
		int parentMaxWidth = parent.getMaxAvaWidth( );
		IStyle style = area.getStyle( );
		validateBoxProperty( style );
		int marginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );

		return  new TableLayoutInfo(
				 columnWidthResolver.resolveFixedLayout(
						parentMaxWidth - marginWidth )  );
	}
	
	private TableLayoutInfo resolveTableLayoutInfo(TableArea area)
	{
		assert(parent!=null);
		int avaWidth = parent.getMaxAvaWidth( ) - parent.getCurrentIP( );
		int parentMaxWidth = parent.getMaxAvaWidth( );
		IStyle style = area.getStyle( );
		validateBoxProperty( style );
		int marginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );
		int specifiedWidth = getDimensionValue( tableContent.getWidth( ), parentMaxWidth );
		if(specifiedWidth + marginWidth > parentMaxWidth)
		{
			specifiedWidth = 0;
		} 
		
		boolean isInline = PropertyUtil.isInlineElement( content );
		if ( specifiedWidth == 0 )
		{
			if(isInline)
			{
				if ( avaWidth - marginWidth > parentMaxWidth / 4 )
				{
					tableWidth = avaWidth - marginWidth;
				}
				else
				{
					tableWidth = parentMaxWidth - marginWidth;
				}
			}
			else
			{
				tableWidth = avaWidth - marginWidth;
			}
			return new TableLayoutInfo(handleColummVisibity(columnWidthResolver.resolve( tableWidth, tableWidth )));
		}
		else
		{
			if ( !isInline )
			{
				tableWidth = Math.min( specifiedWidth, avaWidth - marginWidth );
				return new TableLayoutInfo(
						handleColummVisibity( columnWidthResolver.resolve(
								tableWidth, avaWidth - marginWidth ) ) );
			}
			else
			{
				tableWidth = Math.min( specifiedWidth, parentMaxWidth - marginWidth );
				return new TableLayoutInfo(
						handleColummVisibity( columnWidthResolver.resolve(
								tableWidth, parentMaxWidth - marginWidth ) ) );
			}
		}
	}
	
	private int[] handleColummVisibity(int[] columns)
	{
		//enable visibility
		int colWidth[] = new int[columnNumber];
		for ( int i = 0; i < columnNumber; i++ )
		{
			IColumn column = tableContent.getColumn( i );
			if ( isColumnHidden( column ) )
			{
				colWidth[i] = 0;
			}
			else
			{
				colWidth[i] = columns[i];
			}
		}
		return colWidth;
	}

	/**
	 * update row height
	 * 
	 * @param row
	 */
	public void updateRow( RowArea row, int specifiedHeight )
	{
		ArrayList dropCells = this.dropList;
		CellWrapper[] cells = currentRowContent;
		// first calculate row height
		Iterator iter = row.getChildren( );
		int height = specifiedHeight;
		boolean[] hasCell = new boolean[columnNumber];
		while ( iter.hasNext( ) )
		{
			CellArea cell = (CellArea) iter.next( );
			// FIXME
			int colID = cell.getColumnID( );
			if ( currentRowContent[colID]!=null && currentRowContent[colID].rowSpan==1)
			{
				height = Math.max( height, cell.getHeight( ) );
			}
			 
			
			for ( int i = colID; i < cell.getColumnID( ) + cell.getColSpan( ); i++ )
			{
				hasCell[i] = true;
			}
			if ( cells[colID] != null && ( cells[colID].rowSpan != 1 ) )
			{
				dropCells.add( new DropCellInfo( cell, cells[colID].rowSpan ) );
			}
		}

		Iterator iterator = dropCells.iterator( );
		while ( iterator.hasNext( ) )
		{
			DropCellInfo dropCell = (DropCellInfo) iterator.next( );
			for ( int i = dropCell.cell.getColumnID( ); i < dropCell.cell
					.getColumnID( )
					+ dropCell.cell.getColSpan( ); i++ )
			{
				hasCell[i] = true;
			}
		}

		//
		if ( height > 0 )
		{

			// udpate drop cell height information
			for ( int i = 0; i < dropCells.size( ); i++ )
			{
				DropCellInfo dropCell = (DropCellInfo) dropCells.get( i );
				dropCell.leftHeight -= height;
				// adjust height of drop cell
				if ( dropCell.leftHeight <= 0 )
				{
					dropCell.cell.setHeight( dropCell.cell.getHeight( )
							- dropCell.leftHeight );
					dropCell.leftHeight = 0;
				}
			}

			// row formalize
			for ( int i = 0; i < columnNumber; i++ )
			{
				if ( !hasCell[i] )
				{
					// FIXME clone a cell area
					ICellContent cellContent = null;
					if ( cells[i] != null )
					{
						cellContent = cells[i].cell;
					}
					if ( cellContent == null )
					{
						cellContent = tableContent.getReportContent( )
								.createCellContent( );
						cellContent.setColumn( i );
						cellContent.setColSpan( 1 );
						cellContent.setRowSpan( 1 );
						cellContent.setParent( currentRow.row );
					}
					int startColumn = cellContent.getColumn( );
					int endColumn = cellContent.getColSpan( ) + startColumn;
					CellArea emptyCell = AreaFactory
							.createCellArea( cellContent );

					resolveBorderConflict( emptyCell );
					IStyle areaStyle = emptyCell.getStyle( );
					
					areaStyle.setProperty( IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0 );
					emptyCell.setWidth( getCellWidth( startColumn, endColumn ) );
					emptyCell.setPosition( layoutInfo.getXPosition( i ), 0 );
					row.addChild( emptyCell );
					emptyCell.setHeight( height );
					for ( int j = startColumn; j < endColumn; j++ )
					{
						hasCell[j] = true;
					}
					if ( cells[i] != null )
					{
						if ( cells[i].rowSpan != 1 )
						{
							// resolved drop cell
							dropList.add( new DropCellInfo( emptyCell, 0,
									cells[i].rowSpan ) );
						}
					}
				}
			}

			iter = row.getChildren( );
			while ( iter.hasNext( ) )
			{
				CellArea cell = (CellArea) iter.next( );
				int colID = cell.getColumnID( );
				if ( currentRowContent[colID]!=null && currentRowContent[colID].rowSpan==1)
				{
					cell.setHeight( height );
					verticalAlign( cell );
				}
			}
			row.setHeight( height );
		}

	}

	public int getXPos( int columnID )
	{
		if ( layoutInfo != null )
		{
			return layoutInfo.getXPosition( columnID );
		}
		return 0;
	}

	public int getCellWidth( int startColumn, int endColumn )
	{
		if ( layoutInfo != null )
		{
			return layoutInfo.getCellWidth( startColumn, endColumn );
		}
		return 0;
	}

	protected void verticalAlign( CellArea cell )
	{
		IContent content = cell.getContent( );
		if ( content == null )
		{
			return;
		}
		CSSValue verticalAlign = content.getComputedStyle( ).getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( IStyle.BOTTOM_VALUE.equals( verticalAlign )
				|| IStyle.MIDDLE_VALUE.equals( verticalAlign ) )
		{
			int totalHeight = 0;
			Iterator iter = cell.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea child = (AbstractArea) iter.next( );
				totalHeight += child.getAllocatedHeight( );
			}
			int offset = cell.getContentHeight( ) - totalHeight;
			if ( offset > 0 )
			{
				if ( IStyle.BOTTOM_VALUE.equals( verticalAlign ) )
				{
					iter = cell.getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setAllocatedPosition( child.getAllocatedX( ),
								child.getAllocatedY( ) + offset );
					}
				}
				else if ( IStyle.MIDDLE_VALUE.equals( verticalAlign ) )
				{
					iter = cell.getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setAllocatedPosition( child.getAllocatedX( ),
								child.getAllocatedY( ) + offset / 2 );
					}
				}

			}
		}
		
		CSSValue align = content.getComputedStyle( ).getProperty( IStyle.STYLE_TEXT_ALIGN );
		// single line
		if ( ( IStyle.RIGHT_VALUE.equals(  align ) || IStyle.CENTER_VALUE
				.equals( align ) ) )
		{
			
			Iterator iter = cell.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				int spacing = cell.getContentWidth( ) - area.getAllocatedWidth( ) ;
				if(spacing>0)
				{
					if ( IStyle.RIGHT_VALUE.equals( align ) )
					{
						area.setAllocatedPosition( spacing + area.getAllocatedX( ),
								area.getAllocatedY( ) );
					}
					else if ( IStyle.CENTER_VALUE
							.equals( align ) )
					{
						area.setAllocatedPosition( spacing / 2
								+ area.getAllocatedX( ), area.getAllocatedY( ) );
					}
				}
			}

		}
	}

	/**
	 * Class represents drop cell information
	 * 
	 * 
	 */
	class DropCellInfo
	{

		CellArea cell;
		int leftHeight;
		int rowSpan;

		public DropCellInfo( CellArea cell, int rowSpan )
		{
			this.cell = cell;
			this.rowSpan = rowSpan;
			this.leftHeight = cell.getHeight( );
		}

		public DropCellInfo( CellArea cell, int height, int rowSpan )
		{
			this.cell = cell;
			this.rowSpan = rowSpan;
			this.leftHeight = height;
		}
	}

	protected void repeatHeader( )
	{
		if ( isFirst )
		{
			isFirst = false;
			return;
		}
		ITableBandContent header = (ITableBandContent) tableContent.getHeader( );
		if ( !repeatHeader || header == null )
		{
			return;
		}
		if ( header.getChildren( ).isEmpty( ) )
		{
			return;
		}
		if ( child != null )
		{
			IContent content = child.getContent( );
			if ( content instanceof ITableBandContent )
			{
				if ( ( (ITableBandContent) content ).getBandType( ) == IBandContent.BAND_HEADER )
				{
					return;
				}

			}
		}
		PDFReportLayoutEngine engine = context.getLayoutEngine( );
		PDFLayoutEngineContext con = new PDFLayoutEngineContext( engine );
		con.setFactory( new PDFLayoutManagerFactory( con ) );
		con.setFormat( context.getFormat( ) );
		con.setReport( context.getReport( ) );
		con.setMaxHeight( context.getMaxHeight( ) );
		con.setAllowPageBreak( false );
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor( header );
		headerExecutor.execute( );
		PDFTableRegionLM regionLM = new PDFTableRegionLM( con, tableContent,
				layoutInfo );
		regionLM.initialize( header, null );
		regionLM.layout( );
		TableArea tableRegion = (TableArea) tableContent
				.getExtension( IContent.LAYOUT_EXTENSION );
		if ( tableRegion != null
				&& tableRegion.getHeight( ) < this.getMaxAvaHeight( )
						- currentBP )
		{
			// add to root
			Iterator iter = tableRegion.getChildren( );
			RowArea row = null;
			while ( iter.hasNext( ) )
			{
				row = (RowArea) iter.next( );
				addArea( row );
				repeatRowCount++;
			}
			if ( row != null )
			{
				removeBottomBorder( row );
			}
		}
		tableContent.setExtension( IContent.LAYOUT_EXTENSION, null );
	}

	protected void addCaption( String caption )
	{
		if ( caption == null || "".equals( caption ) ) //$NON-NLS-1$
		{
			return;
		}
		IReportContent report = tableContent.getReportContent( );
		ILabelContent captionLabel = report.createLabelContent( );
		captionLabel.setText( caption );
		captionLabel.getStyle( ).setProperty( IStyle.STYLE_TEXT_ALIGN,
				IStyle.CENTER_VALUE );
		ICellContent cell = report.createCellContent( );
		cell.setColSpan( tableContent.getColumnCount( ) );
		cell.setRowSpan( 1 );
		cell.setColumn( 0 );
		captionLabel.setParent( cell );
		cell.getChildren( ).add( captionLabel );
		IRowContent row = report.createRowContent( );
		row.getChildren( ).add( cell );
		cell.setParent( row );
		ITableBandContent band = report.createTableBandContent( );
		band.getChildren( ).add( row );
		row.setParent( band );
		band.setParent( tableContent );
		PDFLayoutEngineContext con = new PDFLayoutEngineContext( context
				.getLayoutEngine( ) );
		con.setFactory( context.getFactory( ) );
		con.setFormat( context.getFormat( ) );
		con.setMaxHeight( context.getMaxHeight( ) );
		con.setAllowPageBreak( false );
		PDFTableRegionLM regionLM = new PDFTableRegionLM( con, content,
				layoutInfo );
		regionLM.initialize( band, null );
		regionLM.layout( );
		TableArea tableRegion = (TableArea) content
				.getExtension( IContent.LAYOUT_EXTENSION );
		if ( tableRegion != null
				&& tableRegion.getHeight( ) < this.getMaxAvaHeight( )
						- currentBP )
		{
			// add to root
			Iterator iter = tableRegion.getChildren( );
			while ( iter.hasNext( ) )
			{
				RowArea rowArea = (RowArea) iter.next( );
				root.addChild( rowArea );
				rowArea.setPosition( 0, currentBP );
				setCurrentBP( currentBP + rowArea.getHeight( ) );
				repeatRowCount++;
			}
		}
		content.setExtension( IContent.LAYOUT_EXTENSION, null );
	}

	protected class CellWrapper
	{

		ICellContent cell;
		int rowID;
		int rowSpan;

		public CellWrapper( ICellContent cell, int rowID, int rowSpan )
		{
			this.cell = cell;
			this.rowID = rowID;
			this.rowSpan = rowSpan;
		}
	}

	protected class RowWrapper
	{

		IRowContent row;
		int rowID;

		public RowWrapper( IRowContent row, int rowID )
		{
			this.row = row;
			this.rowID = rowID;
		}
	}

	protected IReportItemExecutor createExecutor( )
	{
		return executor;
	}

	protected boolean isRootEmpty( )
	{
		return !( root != null && root.getChildrenCount( ) > repeatRowCount );
	}

}
