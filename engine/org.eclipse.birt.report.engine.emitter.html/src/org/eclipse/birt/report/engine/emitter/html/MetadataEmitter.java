/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.internal.util.HTMLUtil;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * Used to output metadata, including group collpase/expand icon, column filter
 * icon, element select handle container and instance id, element type id,etc.
 * 
 */
public class MetadataEmitter
{
	/**
	 * Stores row state used to check if current row is start of detail rows.
	 */
	private Stack detailRowStateStack = new Stack( );

	/**
	 * Strores tags so that tags can be ouput in pairs.
	 */
	private Stack tagStack = new Stack( );
	private HTMLWriter writer;
	private boolean displayFilterIcon;
	private boolean displayGroupIcon;
	private boolean wrapTemplateTable;
	private IDGenerator idGenerator;
	private List ouputInstanceIDs;
	/**
	 * the instance ID of current wrapping table.
	 */
	private InstanceID wrapperTableIID = null;
	
	public MetadataEmitter( HTMLWriter writer, HTMLRenderOption htmlOption,
			IDGenerator idGenerator )
	{
		this.writer = writer;
		this.displayFilterIcon = htmlOption.getDisplayFilterIcon( );
		this.displayGroupIcon = htmlOption.getDisplayGroupIcon( );
		this.wrapTemplateTable = htmlOption.getWrapTemplateTable( );
		this.ouputInstanceIDs = htmlOption.getInstanceIDs( );
		this.idGenerator = idGenerator;
	}

	/**
	 * Starts a table. To ensure column filter is output in the first detail
	 * row, a state is used to record if first detail row is reached and if
	 * column filter icon is ouput.
	 * 
	 * @param table
	 */
	public void startTable( ITableContent table )
	{
		Object generateBy = table.getGenerateBy( );
		DetailRowState state = null;
		if ( generateBy instanceof TableItemDesign )
		{
			state = new DetailRowState( false, false, true );
		}
		else
		{
			state = new DetailRowState( false, false, false );
		}

		detailRowStateStack.push( state );
	}

	/**
	 * Ends a table. Pop the detail row state for this table.
	 * 
	 * @param table
	 */
	public void endTable( ITableContent table )
	{
		detailRowStateStack.pop( );
	}

	/**
	 * Starts a row. If the row is the first row of the table, the
	 * <code>isStartOfDetail</code> is set so that column filter icon will be
	 * input into the cells in this row.
	 * 
	 * @param row
	 */
	public void startRow( IRowContent row )
	{
		//FIXME: code view: hasOutput has same meaning as isStartOfDetail?
//		if (!state.hasOutput)
//		{
//			if (isRowInDtealBand(row))
//			{
//				hasOutput = true;
//				isStartOfDeatil = true;
//			}
//		}
		if ( isRowInDetailBand( row ) )
		{
			DetailRowState state = (DetailRowState) detailRowStateStack.peek( );
			if ( !state.hasOutput && !state.isStartOfDetail && state.isTable )
			{
				state.isStartOfDetail = true;
				state.hasOutput = true;
			}
		}
	}

	public void endRow( IRowContent row )
	{
		DetailRowState state = (DetailRowState) detailRowStateStack.peek( );
		if ( state.isStartOfDetail )
		{
			state.isStartOfDetail = false;
		}
	}

	/**
	 * Starts a cell. Output a wrap table if group icon or column filter need to
	 * be output in this cell. Output group icon before the items in this cell.
	 * 
	 * @param cell
	 */
	public void startCell( ICellContent cell )
	{
		boolean needColumnFilter = needColumnFilter(cell);
		boolean needGroupIcon = needGroupIcon(cell);
		if ( needColumnFilter || needGroupIcon )
		{
			writer.openTag( HTMLTags.TAG_TABLE );
			writer.attribute( HTMLTags.ATTR_HEIGHT, "100%" );
			writer.attribute( HTMLTags.ATTR_WIDTH, "100%" );
			writer.openTag( HTMLTags.TAG_TR );
			//FIXME: code review: move the td outputting to "if ( needGroupIcon )". remove useless style.
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( "align", cell.getComputedStyle( ).getTextAlign( ) ); //$NON-NLS-1$
		}
		if ( needGroupIcon )
		{
			// include select handle table
			writer.attribute( HTMLTags.ATTR_STYLE, "vertical-align:top"
					+ ";text-align:right" );
			//FIXME: code review: in performance mode the computedStyel can't be used.
			writer.attribute( "align", cell.getComputedStyle( ).getTextAlign( ) ); //$NON-NLS-1$
			writer.attribute( HTMLTags.ATTR_WIDTH, "16px" );
			writer.openTag( HTMLTags.TAG_IMAGE );
			writer.attribute( HTMLTags.ATTR_SRC, "iv/images/collapsexpand.gif" );
			writer.attribute( HTMLTags.ATTR_STYLE, "cursor:pointer" );
			String bookmark = idGenerator.generateUniqueID( );
			HTMLEmitterUtil.setBookmark( writer, null, bookmark );
			setActiveIDTypeIID( bookmark, "GROUP", null, -1 );
			writer.closeTag( HTMLTags.TAG_IMAGE );
			writer.closeTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( "align", cell.getComputedStyle( ).getTextAlign( ) ); //$NON-NLS-1$
		}
	}

	/**
	 * Ends a cell. Complete the wrap table for group icon and column filter
	 * icon and output the column filter icon.
	 * 
	 * @param cell
	 */
	public void endCell( ICellContent cell )
	{
		boolean needColumnFilter = needColumnFilter(cell);
		boolean needGroupIcon = needGroupIcon(cell);
		if ( needColumnFilter )
		{
			// include select handle table
			writer.closeTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( HTMLTags.ATTR_STYLE, "vertical-align:top" );
			writer.openTag( HTMLTags.TAG_IMAGE );
			//FIXME: code review: output the width?
			writer.attribute( HTMLTags.ATTR_SRC, "iv/images/columnicon.gif" );
			writer.attribute( HTMLTags.ATTR_ALT, getColumnFilterText( cell ) );
			writer.attribute( HTMLTags.ATTR_STYLE, "cursor:pointer" );
			writer.attribute( HTMLTags.ATTR_COLUMN, cell.getColumnInstance( )
					.getInstanceID( ).toString( ) );
			String bookmark = idGenerator.generateUniqueID( );
			HTMLEmitterUtil.setBookmark( writer, null, bookmark );
			setActiveIDTypeIID( bookmark, "COLOUMNINFO", null, -1 );
			writer.closeTag( HTMLTags.TAG_IMAGE );
		}
		if ( needColumnFilter || needGroupIcon )
		{
			writer.closeTag( HTMLTags.TAG_TD );
			writer.closeTag( HTMLTags.TAG_TR );
			writer.closeTag( HTMLTags.TAG_TABLE );
		}
	}

	/**
	 * Output instance id and bookmark for text items.
	 * 
	 * @param text
	 * @param tag
	 * @return
	 */
	public boolean startText( ITextContent text, String tag )
	{
		if ( needMetadata( text ) )
		{
			String styleName = getStyleClass( text );
			startContent( text, tag, styleName );
			return true;
		}
		return false;
	}

	public void endText( ITextContent text )
	{
		if ( needMetadata( text ) )
		{
			endContent( text );
		}
	}

	/**
	 * Outputs instance id and bookmark for foreign contents.
	 * 
	 * @param foreign
	 * @param tag
	 * @return
	 */
	public boolean startForeign ( IForeignContent foreign, String tag )
	{
		if ( needMetadata( foreign ) )
		{
			String styleName = getStyleClass( foreign );;
			startContent( foreign, tag, styleName );
			return true;
		}
		return false;
	}
	
	public void endForeign( IForeignContent foreign )
	{
		if ( needMetadata( foreign ) )
		{
			endContent( foreign );
		}
	}

	/**
	 * Outputs instance id and bookmark for charts.
	 * 
	 * @param image
	 * @return
	 */
	public boolean startImage( IImageContent image )
	{
		if ( image.getGenerateBy( ) instanceof ExtendedItemDesign )
		{
			startSelectHandle( HTMLEmitterUtil.getElementType( image ),
					HTMLEmitterUtil.DISPLAY_BLOCK, "birt-chart-design" ); //$NON-NLS-1$
			// If the image is a chart, add it to active id list, and output type ��iid to html
			String bookmark = image.getBookmark( );				
			if ( bookmark == null )
			{
				bookmark = idGenerator.generateUniqueID( );
				image.setBookmark( bookmark );
			}
			setActiveIDTypeIID( image);				
			HTMLEmitterUtil.setBookmark(  writer, HTMLTags.ATTR_IMAGE, bookmark ); //$NON-NLS-1$
			return true;
		}
		return false;
	}
	
	public void endImage( IImageContent image )
	{
		if ( image.getGenerateBy( ) instanceof ExtendedItemDesign )
		{
			endSelectHandle( HTMLEmitterUtil.getElementType( image ), HTMLEmitterUtil.DISPLAY_BLOCK);
		}
	}

	/**
	 * Outputs instance id.
	 * 
	 * @param bookmark
	 * @param type
	 * @param iid
	 * @param elementId
	 */
	private void setActiveIDTypeIID( String bookmark, String type,
			InstanceID iid, int elementId )
	{
		HTMLEmitterUtil.setActiveIDTypeIID( writer, ouputInstanceIDs,
				bookmark, type, iid, elementId );
	}

	private void setActiveIDTypeIID( IContent content )
	{
		HTMLEmitterUtil.setActiveIDTypeIID( writer, ouputInstanceIDs, content );
	}

	private void startSelectHandle( int display, int blockType, String cssClass )
	{
		writer.openTag( HTMLEmitterUtil.getTagByType( display, blockType ) );
		writer.attribute( HTMLTags.ATTR_CLASS, cssClass );
	}

	private void endSelectHandle( int display, int blockType )
	{
		writer.closeTag( HTMLEmitterUtil.getTagByType( display, blockType ) );
	}

	/**
	 * Output instance id and bookmark for a content.
	 * 
	 * @param content
	 * @param tag
	 * @param styleName
	 */
	private void startContent( IContent content, String tag, String styleName )
	{
		tagStack.push( tag );
		writer.openTag( tag );
		if ( content.getBookmark( ) == null )
		{
			content.setBookmark( idGenerator.generateUniqueID( ) );
		}
		writer.attribute( HTMLTags.ATTR_CLASS, styleName ); //$NON-NLS-1$
		setActiveIDTypeIID( content );
		HTMLEmitterUtil.setBookmark( writer, tag, content.getBookmark( ) );
	}

	private void endContent( IContent content )
	{
		writer.closeTag( (String) tagStack.pop( ) );
	}

	/**
	 * A TextContent needs metadata when it's a :
	 * <li>label item.
	 * <li>template item.
	 * <li>data item in table header/footer or table group header/footer and
	 * using the query extended from the table.
	 * 
	 * @param text
	 *            the text content.
	 * @return true if and only if the metadata of the content needs to be
	 *         output.
	 */
	private boolean needMetadata( ITextContent text )
	{
		Object generateBy = text.getGenerateBy( );
		if ( generateBy instanceof LabelItemDesign )
		{
			return true;
		}
		// Meta data of data items which are in table header, ta
		if ( generateBy instanceof DataItemDesign )
		{
			return isAggregatable( text );
		}
		return false;
	}

	/**
	 * A ForeignContent needs metadata when it's a template item.
	 * 
	 * @param text
	 *            the text content.
	 * @return true if and only if the metadata of the content needs to be
	 *         output.
	 */
	private boolean needMetadata( IForeignContent foreign )
	{
		if ( foreign.getGenerateBy( ) instanceof TemplateDesign )
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks if a row is in detail band.
	 * 
	 * @param row
	 * @return
	 */
	private boolean isRowInDetailBand( IRowContent row )
	{
		IElement parent = row.getParent( );
		if ( !( parent instanceof IBandContent ) )
		{
			return false;
		}
		IBandContent band = (IBandContent) parent;
		if ( band.getBandType( ) == IBandContent.BAND_DETAIL )
		{
			return true;
		}
		return false;
	}

	private HashMap aggregatables = new HashMap();
	/**
	 * Checks if the text is a data content in table header/footer or table
	 * group header/footer and uses the query of the table.
	 * 
	 * @param text
	 *            the text content.
	 */
	private boolean isAggregatable( ITextContent text )
	{
		//FIXME: code review: getGenerateBy may return a null value.
		Object generateBy = text.getGenerateBy( );
		Boolean isAggregate = (Boolean)aggregatables.get(generateBy);  
		if ( isAggregate != null)
		{
			return isAggregate.booleanValue();
		}
		//The data item should not have query of itself.
		DataItemDesign data = ( DataItemDesign )generateBy;
		if ( data.getQuery( ) != null )
		{
			aggregatables.put(generateBy, Boolean.FALSE);
			return false;
		}
		IElement parent = text.getParent( );
		while( parent != null )
		{
			// The data item should not extends from a container which is
			// not a table.
			if ( parent instanceof IContent )
			{
				IContent content = (IContent)parent;
				ReportItemDesign design = ( ReportItemDesign )content.getGenerateBy( );
				if ( design != null && design.getQuery( ) != null )
				{
					aggregatables.put(generateBy, Boolean.FALSE);
					return false;
				}
			}

			// The data item should be in table header/footer or group
			// header/footer and its query extends from this table
			if ( parent instanceof IBandContent )
			{
				IBandContent bandContent = (IBandContent )parent;
				int bandType = bandContent.getBandType( );
				if ( bandType == IBandContent.BAND_HEADER
						|| bandType == IBandContent.BAND_FOOTER
						|| bandType == IBandContent.BAND_GROUP_HEADER
						|| bandType == IBandContent.BAND_GROUP_FOOTER )
				{
					IElement bandParent = bandContent.getParent( );
					while ( bandParent instanceof IGroupContent )
					{
						bandParent = bandParent.getParent( );
					}
					if ( bandParent instanceof ITableContent )
					{
						aggregatables.put( generateBy, Boolean.TRUE );
						return true;
					}
					// FIXME: code review: needs return a false value?
				}
			}
			parent = parent.getParent( );
		}
		aggregatables.put(generateBy, Boolean.FALSE);
		return false;
	}
	
	//FIXME: code review: rename to getPredefineStyle or other names????
	private String getStyleClass( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		String styleName = null;
		if ( generateBy instanceof LabelItemDesign )
		{
			styleName = "birt-label-design";
		}
		else if ( generateBy instanceof DataItemDesign )
		{
			styleName = "birt-data-design";
		}
		else
		{
			//TODO: chang to "birt-data-design".
			styleName = "birt-label-design";
		}
		return styleName;
	}

	/**
	 * Checks if column filter icons need to be output. Column filter icons need
	 * to be output when:
	 * <ol>
	 * <li><code>displayGroupIcon</code> is set to true. And
	 * <li>The cell is in the first row of detail. And
	 * <li>The cell has child(any items). And
	 * <li>The column containing the cell has column filters.
	 * </ol>
	 * 
	 * @param cell
	 * @return
	 */
	private boolean needColumnFilter( ICellContent cell )
	{
		//FIXME: code review: do the action only when displayFilterIcon is true.
		IColumn columnInstance = cell.getColumnInstance( );
		if ( columnInstance == null )
		{
			return false;
		}
		
		DetailRowState state = (DetailRowState) detailRowStateStack.peek( );
		return state.isStartOfDetail
				&& columnInstance.hasDataItemsInDetail( )
				&& displayFilterIcon
				&& getFilterConditions( cell ).size() > 0;
	}

	/**
	 * Checks if group icon needs to be displayed in this cell.
	 * 
	 * @param cell
	 * @return
	 */
	private boolean needGroupIcon( ICellContent cell )
	{
		//FIXME: code view: put displayGroupIcon ahead.
		return cell.getDisplayGroupIcon( ) && displayGroupIcon;
	}
	
	/**
	 * judge the table content is a top-level template table or not.
	 * 
	 * @param table
	 *            table content
	 */
	private boolean isTopLevelTemplateTable( ITableContent table )
	{

		Object genBy = table.getGenerateBy( );
		if ( genBy instanceof TableItemDesign )
		{
			TableItemDesign tableDesign = (TableItemDesign) genBy;
			DesignElementHandle handle = tableDesign.getHandle( );
			// judge the content is belong table template element or not.
			if ( ( null != handle ) && handle.isTemplateParameterValue( ) )
			{
				// judge the content is the top-level template table or not.
				DesignElementHandle parentHandle = handle.getContainer( );
				while ( null != parentHandle )
				{
					if ( ( parentHandle instanceof TableHandle )
							&& parentHandle.isTemplateParameterValue( ) )
					{
						return false;
					}
					parentHandle = parentHandle.getContainer( );
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * wrap the top-level template table
	 * 
	 * @param table
	 *            table content
	 */
	public void startWrapTable( ITableContent table )
	{
		if ( wrapTemplateTable && isTopLevelTemplateTable( table ) )
		{
			wrapperTableIID = table.getInstanceID( );

			writer.openTag( HTMLTags.TAG_TABLE );
			writer.attribute( HTMLTags.ATTR_STYLE,
			" border: medium none ; border-collapse: collapse; width: 100%;" );
			writer.openTag( HTMLTags.TAG_TBODY );
			writer.openTag( HTMLTags.TAG_TR );
			writer.attribute( HTMLTags.ATTR_STYLE, " vertical-align: top;" );
			writer.openTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_IMAGE );
			writer.attribute( HTMLTags.ATTR_SRC,
					"bizRD/images/report/sidetab_active.gif" );
			writer.attribute( HTMLTags.ATTR_STYLE,
					" width: 20px; height: 60px;" );
			writer.closeTag( HTMLTags.TAG_IMAGE );
			writer.closeTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( HTMLTags.ATTR_STYLE, " border: 2px solid black;" );
		}
	}

	/**
	 * wrap the top-level template table
	 * 
	 * @param table
	 *            table content
	 */
	public void endWrapTable( ITableContent table )
	{
		if ( wrapTemplateTable && ( table.getInstanceID( ) == wrapperTableIID ) )
		{
			wrapperTableIID = null;
			writer.closeTag( HTMLTags.TAG_TD );
			writer.closeTag( HTMLTags.TAG_TR );
			writer.closeTag( HTMLTags.TAG_TBODY );
			writer.closeTag( HTMLTags.TAG_TABLE );
		}
	}
	
	/**
	 * Generates description text for the filters of a column which contains the
	 * specified cell.
	 * 
	 * @param cell
	 *            the cell.
	 * @return the description text.
	 */
	private String getColumnFilterText( ICellContent cell )
	{
		List filterConditions = getFilterConditions( cell );
		StringBuffer conditionString = new StringBuffer( );
		for ( int i = 0; i < filterConditions.size( ); i++)
		{
			if ( i != 0 )
			{
				conditionString.append( ';' );
			}
			FilterConditionHandle condition = (FilterConditionHandle) filterConditions
					.get( i );
			conditionString.append( HTMLUtil.getFilterDescription( condition ) );
		}
		return conditionString.toString( );
	}

	private HashMap filterConditions = new HashMap();
	/**
	 * Gets filter conditions of the column which contains the specified cell.
	 * 
	 * @param cell
	 *            the cell.
	 * @return the column filter conditions. Empty list is returned when the
	 *         column has no filter conditions.
	 */
	private List getFilterConditions( ICellContent cell )
	{
		IRowContent row = (IRowContent) cell.getParent( );
		ITableContent table = row.getTable( );
		List filters = null;
		if ( table != null )
		{
			Object genBy = table.getGenerateBy( );
			if ( genBy instanceof TableItemDesign )
			{
				TableHandle tableHandle = (TableHandle) ( (TableItemDesign) genBy )
						.getHandle( );
				int columnCount = tableHandle.getColumnCount();
				List[] tableFilters = (List[])filterConditions.get(tableHandle);
				if (tableFilters == null)
				{
					tableFilters = new List[columnCount];
					filterConditions.put(tableHandle, tableFilters);
				}
				//FIXME: code view: column id should be gotten from design. 
				int columnId = cell.getColumn();
				if (columnId < columnCount)
				{
					filters = tableFilters[columnId];
					if (filters == null)
					{
						filters = tableHandle.getFilters( cell.getColumn( ) );
						tableFilters[columnId] = filters;
					}
				}
				
			}
		}
		return filters == null ? Collections.EMPTY_LIST : filters;
	}
}

class DetailRowState
{
	public boolean isStartOfDetail;
	public boolean hasOutput;
	public boolean isTable;
	// FIXME: code view: remove isStartOfDetail and hasOutput parameters since
	// they are both false from start.
	public DetailRowState( boolean isStartOfDetail, boolean hasOutput,
			boolean isTable )
	{
		this.isStartOfDetail = isStartOfDetail;
		this.hasOutput = hasOutput;
		this.isTable = isTable;
	}
}