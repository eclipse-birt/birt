/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;


public class TableLayout extends BlockStackingLayout
{

	
	
	/**
	 * table content
	 */
	private ITableContent tableContent;


	/**
	 * number of table column
	 */
	protected int columnNumber;
	
	/**
	 * the first visible column id of the table.
	 */
	protected int startCol = -1;
	
	/**
	 * the last visible column id of the table.
	 */
	protected int endCol = -1;

	/**
	 * table width
	 */
	protected int tableWidth;

	protected TableLayoutInfo layoutInfo = null;

	protected ITableBandContent currentBand = null;

	protected Stack groupStack = new Stack( );

	protected ColumnWidthResolver columnWidthResolver;

	protected int rowCount = 0;


	protected TableAreaLayout layout;
	
	protected TableAreaLayout regionLayout = null;;

	public TableLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
		tableContent = (ITableContent) content;
		columnWidthResolver = new ColumnWidthResolver( tableContent );
		columnNumber = tableContent.getColumnCount( );
	}

	protected void createRoot( )
	{
		root = AreaFactory.createTableArea( (ITableContent) content );
		root.setWidth( tableWidth );
	}

	public TableLayoutInfo getLayoutInfo( )
	{
		return layoutInfo;
	}

	protected void buildTableLayoutInfo( )
	{
		this.layoutInfo = resolveTableFixedLayout((TableArea)root );

	}

	protected void initialize( )
	{
		createRoot( );
		buildTableLayoutInfo( );
		root.setWidth( layoutInfo.getTableWidth( ) );
		maxAvaWidth = layoutInfo.getTableWidth( );
		rowCount = 0;

		if ( parent != null )
		{
			root.setAllocatedHeight( parent.getCurrentMaxContentHeight( ) );
		}
		else
		{
			root.setAllocatedHeight( context.getMaxHeight( ) );
		}
		if ( layout == null )
		{
			int start = 0;
			int end = tableContent.getColumnCount( ) -1;
			layout = new TableAreaLayout( tableContent, layoutInfo, start,
					end );
			//layout.initTableLayout( context.getUnresolvedRowHint( tableContent ) );
		}
		//maxAvaHeight = root.getContentHeight( ) - getBottomBorderWidth( );

	}

	protected void closeLayout( )
	{
		/*
		 * 1. resolve all unresolved cell 2. resolve table bottom border 3.
		 * update height of Root area 4. update the status of TableAreaLayout
		 */
		int borderHeight = 0;
		if ( layout != null )
		{
			int height = layout.resolveAll( );
			if ( 0 != height)
			{
				currentBP = currentBP + height;
			}
			borderHeight = layout.resolveBottomBorder( );
			layout.remove( (TableArea) root );
		}
		root.setHeight( getCurrentBP( ) + getOffsetY( ) + borderHeight );
		parent.addArea( root );
	}

	private int getBottomBorderWidth( )
	{
		IStyle style = root.getContent( ).getComputedStyle( );
		int borderHeight = PropertyUtil.getDimensionValue( style
				.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
		return borderHeight;
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
	public void resolveBorderConflict( CellArea cellArea, boolean isFirst )
	{
		if ( layout != null )
		{
			layout.resolveBorderConflict( cellArea, isFirst );
		}
	}

	private class ColumnWidthResolver
	{
		
		ITableContent table;

		public ColumnWidthResolver( ITableContent table )
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
				else if( EngineIRConstants.UNITS_EM.equals(columns[i].getUnits())
						||EngineIRConstants.UNITS_EX.equals(columns[i].getUnits()) )
				{
					int len = TableLayout.this.getDimensionValue(columns[i], 
							PropertyUtil.getDimensionValue( table.getComputedStyle().getProperty( StyleConstants.STYLE_FONT_SIZE ) ) );
					fixedLength += len;
				}
				else
				{
					int len = TableLayout.this.getDimensionValue(columns[i], tableWidth);
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
					if( EngineIRConstants.UNITS_EM.equals(columns[i].getUnits())
							||EngineIRConstants.UNITS_EX.equals(columns[i].getUnits()) )
					{
						cols[i]= TableLayout.this.getDimensionValue(columns[i], 
								PropertyUtil.getDimensionValue( table.getComputedStyle().getProperty( StyleConstants.STYLE_FONT_SIZE ) ) );
					}
					else
					{
						cols[i] = TableLayout.this.getDimensionValue(columns[i], tableWidth);
					}
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
				IColumn column = table.getColumn( i );
				DimensionType w = column.getWidth();
				if ( startCol < 0 )
				{
					startCol = i;
				}
				endCol = i;
				if(w==null)
				{
					columns[i] = null;
				}
				else
				{
					columns[i] = new DimensionType(w.getMeasure(), w.getUnits());
					
				}
			}
			if ( startCol < 0 )
				startCol = 0;
			if ( endCol < 0 )
				endCol = 0;
			
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
		


		public int[] resolve( int specifiedWidth, int maxWidth )
		{
			assert ( specifiedWidth <= maxWidth );
			int columnNumber = table.getColumnCount( );
			int[] columns = new int[columnNumber];
			int columnWithWidth = 0;
			int colSum = 0;

			for ( int j = 0; j < table.getColumnCount( ); j++ )
			{
				IColumn column = table.getColumn( j );
				int columnWidth = getDimensionValue( column.getWidth( ),
						tableWidth );
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

			if ( columnWithWidth == columnNumber )
			{
				if ( colSum <= maxWidth )
				{
					return columns;
				}
				else
				{
					float delta = colSum - maxWidth;
					for ( int i = 0; i < columnNumber; i++ )
					{
						columns[i] -= (int) ( delta * columns[i] / colSum );
					}
					return columns;
				}
			}
			else
			{
				if ( specifiedWidth == 0 )
				{
					if ( colSum < maxWidth )
					{
						distributeLeftWidth( columns, ( maxWidth - colSum )
								/ ( columnNumber - columnWithWidth ) );
					}
					else
					{
						redistributeWidth( columns, colSum - maxWidth
								+ ( columnNumber - columnWithWidth ) * maxWidth
								/ columnNumber, maxWidth, colSum );
					}
				}
				else
				{
					if ( colSum < specifiedWidth )
					{
						distributeLeftWidth( columns,
								( specifiedWidth - colSum )
										/ ( columnNumber - columnWithWidth ) );
					}
					else
					{
						if ( colSum < maxWidth )
						{
							distributeLeftWidth( columns, ( maxWidth - colSum )
									/ ( columnNumber - columnWithWidth ) );
						}
						else
						{
							redistributeWidth( columns, colSum - specifiedWidth
									+ ( columnNumber - columnWithWidth )
									* specifiedWidth / columnNumber,
									specifiedWidth, colSum );
						}
					}

				}

			}
			return columns;
		}

		private void redistributeWidth( int cols[], int delta, int sum,
				int currentSum )
		{
			int avaWidth = sum / cols.length;
			for ( int i = 0; i < cols.length; i++ )
			{
				if ( cols[i] < 0 )
				{
					cols[i] = avaWidth;
				}
				else
				{
					cols[i] -= (int) ( ( (float) cols[i] ) * delta / currentSum );
				}
			}

		}

		private void distributeLeftWidth( int cols[], int avaWidth )
		{
			for ( int i = 0; i < cols.length; i++ )
			{
				if ( cols[i] < 0 )
				{
					cols[i] = avaWidth;
				}
			}
		}
	}


	public void skipRow( RowArea row )
	{
		if ( layout != null )
		{
			layout.skipRow( row );
		}
	}

	
	private TableLayoutInfo resolveTableFixedLayout(TableArea area)
	{
		assert(parent!=null);
		int parentMaxWidth = parent.maxAvaWidth;
		IStyle style = area.getStyle( );
		int marginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );

		return new TableLayoutInfo(
				 columnWidthResolver.resolveFixedLayout(
						parentMaxWidth - marginWidth )  );
	}
	


	private TableLayoutInfo resolveTableLayoutInfo( TableArea area )
	{
		assert ( parent != null );
		int avaWidth = parent.getCurrentMaxContentWidth( )
				- parent.getCurrentIP( );
		int parentMaxWidth = parent.getCurrentMaxContentWidth( );
		IStyle style = area.getStyle( );
		int marginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );
		int specifiedWidth = getDimensionValue( tableContent.getWidth( ),
				parentMaxWidth );
		if ( specifiedWidth + marginWidth > parentMaxWidth )
		{
			specifiedWidth = 0;
		}

		boolean isInline = PropertyUtil.isInlineElement( content );
		if ( specifiedWidth == 0 )
		{
			if ( isInline )
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
			return new TableLayoutInfo(
					 columnWidthResolver.resolve(
							tableWidth, tableWidth ) ) ;
		}
		else
		{
			if ( !isInline )
			{
				tableWidth = Math.min( specifiedWidth, avaWidth - marginWidth );
				return new TableLayoutInfo(
						columnWidthResolver.resolve(
								tableWidth, avaWidth - marginWidth ) ) ;
			}
			else
			{
				tableWidth = Math.min( specifiedWidth, parentMaxWidth
						- marginWidth );
				return new TableLayoutInfo(
						 columnWidthResolver.resolve(
								tableWidth, parentMaxWidth - marginWidth ) ) ;
			}
		}
	}


	/**
	 * update row height
	 * 
	 * @param row
	 */
	public void updateRow( RowArea row, int specifiedHeight )
	{

		if ( layout != null )
		{
			layout.updateRow( row, specifiedHeight );
		}
	}

	public void addRow( RowArea row )
	{
		if ( layout != null )
		{
			layout.addRow( row );
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
		
		Layout regionLayout = new RegionLayout(context, band, null);
		regionLayout.layout( );
		TableArea tableRegion = (TableArea) content
				.getExtension( IContent.LAYOUT_EXTENSION );
		if ( tableRegion != null
				&& tableRegion.getHeight( ) < getCurrentMaxContentHeight( ) )
		{
			// add to root
			Iterator iter = tableRegion.getChildren( );
			while ( iter.hasNext( ) )
			{
				RowArea rowArea = (RowArea) iter.next( );
				addArea( rowArea );
			}
		}
		content.setExtension( IContent.LAYOUT_EXTENSION, null );
	}


	public class TableLayoutInfo
	{

		public TableLayoutInfo( int[] colWidth )
		{
			this.colWidth = colWidth;
			this.columnNumber = colWidth.length;
			this.xPositions = new int[columnNumber];
			this.tableWidth = 0;
			for ( int i = 0; i < columnNumber; i++ )
			{
				xPositions[i] = tableWidth;
				tableWidth += colWidth[i];
			}

		}

		public int getTableWidth( )
		{
			return this.tableWidth;
		}

		public int getXPosition( int index )
		{
			return xPositions[index];
		}

		/**
		 * get cell width
		 * 
		 * @param startColumn
		 * @param endColumn
		 * @return
		 */
		public int getCellWidth( int startColumn, int endColumn )
		{
			assert ( startColumn < endColumn );
			assert ( colWidth != null );
			int sum = 0;
			for ( int i = startColumn; i < endColumn; i++ )
			{
				sum += colWidth[i];
			}
			return sum;
		}

		protected int columnNumber;

		protected int tableWidth;
		/**
		 * Array of column width
		 */
		protected int[] colWidth = null;

		/**
		 * array of position for each column
		 */
		protected int[] xPositions = null;

	}


	public boolean addArea( AbstractArea area )
	{
		return super.addArea( area );
	}

}
