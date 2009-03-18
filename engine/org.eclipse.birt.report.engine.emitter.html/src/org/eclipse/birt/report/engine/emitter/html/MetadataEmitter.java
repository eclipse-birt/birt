/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.internal.util.HTMLUtil;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
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
	private HTMLWriter writer;
	private boolean displayFilterIcon;
	private boolean displayGroupIcon;
	private boolean wrapTemplateTable;
	private MetadataIDGenerator idGenerator = new MetadataIDGenerator( );
	private List ouputInstanceIDs;
	private String imagePath;
	private String htmlIDNamespace;
	
	/**
	 * the instance ID of current wrapping table.
	 */
	private InstanceID wrapperTableIID = null;
	
	protected String attrType, attrRowType, attrElementType, attrIID;
	
	/**
	 * 
	 * @param writer
	 * @param htmlOption
	 * @param idGenerator
	 * @param attrNamePrefix
	 *            : the prefix of the attribute name.
	 */
	public MetadataEmitter( HTMLWriter writer, HTMLRenderOption htmlOption,
			String attrNamePrefix )
	{
		this.writer = writer;
		this.displayFilterIcon = htmlOption.getDisplayFilterIcon( );
		this.displayGroupIcon = htmlOption.getDisplayGroupIcon( );
		this.wrapTemplateTable = htmlOption.getWrapTemplateTable( );
		this.ouputInstanceIDs = htmlOption.getInstanceIDs( );
		this.imagePath = htmlOption.getAppBaseURL( );
		if ( imagePath != null )
		{
			if ( !imagePath.endsWith( "/" ) )
			{
				imagePath = imagePath + "/";
			}
		}
		else
		{
			imagePath = "";
		}
		initializeAttrName( attrNamePrefix );
	}
	
	/**
	 * Initialize the attribute name for metadata.
	 * 
	 * @param prefix
	 *            : the prefix of the attribute name.
	 */
	public void initializeAttrName( String prefix )
	{
		if ( prefix == null )
		{
			attrType = HTMLTags.ATTR_TYPE;
			attrRowType = HTMLTags.ATTR_ROW_TYPE;
			attrElementType = "element_type";
			attrIID = "iid";
		}
		else
		{
			attrType = prefix + HTMLTags.ATTR_TYPE;
			attrRowType = prefix + HTMLTags.ATTR_ROW_TYPE;
			attrElementType = prefix + "element_type";
			attrIID = prefix + "iid";
		}
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
		outputRowMetaData( row );
	}
	
	protected void outputRowMetaData( IRowContent rowContent )
	{
		Object parent = rowContent.getParent( );
		if ( parent instanceof ITableBandContent )
		{
			ITableBandContent bandContent = (ITableBandContent) parent;
			IGroupContent group = rowContent.getGroup( );
			String groupId = rowContent.getGroupId( );
			if ( groupId != null )
			{
				writer.attribute( HTMLTags.ATTR_GOURP_ID, groupId );
			}
			String rowType = null;
			String metaType = null;

			int bandType = bandContent.getBandType( );
			if ( bandType == ITableBandContent.BAND_HEADER )
			{
				metaType = "wrth";
				rowType = "header";
			}
			else if ( bandType == ITableBandContent.BAND_FOOTER )
			{
				metaType = "wrtf";
				rowType = "footer";
			}
			else if ( bandType == ITableBandContent.BAND_GROUP_HEADER )
			{
				rowType = "group-header";
				if ( group != null )
				{
					metaType = "wrgh" + group.getGroupLevel( );
				}
			}
			else if ( bandType == ITableBandContent.BAND_GROUP_FOOTER )
			{
				rowType = "group-footer";
				if ( group != null )
				{
					metaType = "wrgf" + group.getGroupLevel( );
				}
			}
			writer.attribute( attrType, metaType );
			writer.attribute( attrRowType, rowType );
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
	 * output the cell's iid
	 * @param cell
	 */
	public void outputCellIID( ICellContent cell )
	{
		if ( cell != null )
		{
			// Instance ID
			InstanceID iid = cell.getInstanceID( );
			if ( iid != null )
			{
				writer.attribute( attrIID, iid.toString( ) );
			}
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
			writer.attribute( HTMLTags.ATTR_WIDTH, "1px" );
			writer.openTag( HTMLTags.TAG_IMAGE );
			writer.attribute( HTMLTags.ATTR_SRC, imagePath + "./images/iv/collapsexpand.gif" );
			writer.attribute( HTMLTags.ATTR_STYLE, "cursor:pointer" );
			String bookmark = idGenerator.generateUniqueID( );
			HTMLEmitterUtil.setBookmark( writer, null, htmlIDNamespace, bookmark );
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
			writer.attribute( HTMLTags.ATTR_SRC, imagePath + "./images/iv/columnicon.gif" );
			writer.attribute( HTMLTags.ATTR_ALT, getColumnFilterText( cell ) );
			writer.attribute( HTMLTags.ATTR_STYLE, "cursor:pointer" );
			writer.attribute( HTMLTags.ATTR_COLUMN, cell.getColumnInstance( )
					.getInstanceID( ).toString( ) );
			String bookmark = idGenerator.generateUniqueID( );
			HTMLEmitterUtil.setBookmark( writer, null, htmlIDNamespace, bookmark );
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
			startContent( text, tag );
			return true;
		}
		return false;
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
			startContent( foreign, tag );
			return true;
		}
		return false;
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
			// If the image is a chart, add it to active id list, and output type ��iid to html
			String bookmark = image.getBookmark( );
			assert bookmark != null;
			setActiveIDTypeIID( image, bookmark );
			HTMLEmitterUtil.setBookmark(  writer, HTMLTags.ATTR_IMAGE, htmlIDNamespace, bookmark ); //$NON-NLS-1$
			return true;
		}
		return false;
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
			InstanceID iid, long elementId )
	{
		String htmlBookmark;
		if ( null != htmlIDNamespace )
		{
			htmlBookmark = htmlIDNamespace + bookmark;
		}
		else
		{
			htmlBookmark = bookmark;
		}
		exportElementID( ouputInstanceIDs, htmlBookmark, type, elementId );
		
		// type
		writer.attribute( attrElementType, type );
		if ( iid != null )
		{
			if ( type != TYPE_LABEL
					&& type != TYPE_TEMPLATE && type != TYPE_DATA
					&& type != TYPE_TEXT && type != TYPE_UNKNOWN )
			{
				writer.attribute( attrIID, iid.toUniqueString( ) );
			}
			else
			{
				writer.attribute( attrIID, iid.toString( ) );
			}
		}
	}
	
	private void exportElementID( List ouputInstanceIDs, String bookmark,
			String type, long componentID )
	{
		if ( ouputInstanceIDs != null )
		{
			if ( bookmark != null )
			{
				assert type != null;
				StringBuffer buffer = new StringBuffer();
				buffer.append(bookmark);
				buffer.append(",");
				buffer.append(type);
				buffer.append(",");
				buffer.append(componentID);
				ouputInstanceIDs.add( buffer.toString() );
			}
		}
	}

	public void setActiveIDTypeIID( IContent content )
	{
		setActiveIDTypeIID( content, content.getBookmark( ) );
	}

	private void setActiveIDTypeIID( IContent content, String bookmark )
	{
		// If content is generated by LabelItemDesign or TemplateDesign,
		// ExtendedItemDesign, TableItemDesign
		// add it to active id list, and output type & iid to html
		String type = getActiveIdType( content );
		if ( type != null )
		{
			// Instance ID
			InstanceID iid = content.getInstanceID( );
			long componentID = ( iid != null ) ? iid.getComponentID( ) : 0;
			setActiveIDTypeIID( bookmark, type, iid, componentID );
		}
	}
	
	static final String TYPE_LABEL = "LABEL";
	static final String TYPE_TEMPLATE = "TEMPLATE";
	static final String TYPE_EXTENDED = "EXTENDED";
	static final String TYPE_TABLE = "TABLE";
	static final String TYPE_LIST = "LIST";
	static final String TYPE_DATA = "DATA";
	static final String TYPE_TEXT = "TEXT";
	static final String TYPE_UNKNOWN = null;

	private static String getActiveIdType( IContent content )
	{
		Object genBy = content.getGenerateBy( );
		if ( genBy instanceof LabelItemDesign )
		{
			return TYPE_LABEL;
		}
		if ( genBy instanceof TemplateDesign )
		{
			return TYPE_TEMPLATE;
		}

		if ( genBy instanceof ExtendedItemDesign )
		{
			DesignElementHandle handle = ( (ExtendedItemDesign) genBy ).getHandle( );
			if ( handle instanceof ExtendedItemHandle )
			{
				return ( (ExtendedItemHandle) handle ).getExtensionName( );
			}
			return TYPE_EXTENDED;
		}
		if ( genBy instanceof TableItemDesign )
		{
			return TYPE_TABLE;
		}
		if ( genBy instanceof ListItemDesign )
		{
			return TYPE_LIST;
		}
		if ( genBy instanceof DataItemDesign )
		{
			return TYPE_DATA;
		}
		if ( genBy instanceof TextItemDesign )
		{
			return TYPE_TEXT;
		}
		return TYPE_UNKNOWN;
	}
	
	public void outputColumnIID( IColumn column )
	{
		if ( null != column )
		{
			// Instance ID
			InstanceID iid = column.getInstanceID( );
			if ( iid != null )
			{
				writer.attribute( attrIID, iid.toString( ) );
			}
		}
	}

	/**
	 * Output instance id and bookmark for a content.
	 * 
	 * @param content
	 * @param tag
	 */
	private void startContent( IContent content, String tag )
	{
		String bookmark = content.getBookmark( );
		if ( bookmark == null )
		{
			bookmark = idGenerator.generateUniqueID( );
		}
		setActiveIDTypeIID( content, bookmark );
		HTMLEmitterUtil.setBookmark( writer, tag, htmlIDNamespace, bookmark );
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
		if ( generateBy instanceof TextItemDesign )
		{
			if ( isTopLevelContent( text ) )
			{
				return true;
			}
			return isInHeaderFooter( text );
		}
		// Meta data of data items which are in table header, ta
		if ( generateBy instanceof DataItemDesign )
		{
			if ( isTopLevelContent( text ) )
			{
				return true;
			}
			return isAggregatable( text );
		}
		return false;
	}
	
	private boolean isTopLevelContent( IContent content )
	{
		if ( null == content.getParent( ) )
		{
			return true;
		}
		else
		{
			IReportContent report = content.getReportContent( );
			if ( null != report )
			{
				if ( report.getRoot( ) == content.getParent( ) )
				{
					return true;
				}
			}
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
		Object generator = foreign.getGenerateBy( );
		if ( generator instanceof TemplateDesign
				|| generator instanceof ExtendedItemDesign )
		{
			return true;
		}
		else if ( generator instanceof TextItemDesign )
		{
			if ( isTopLevelContent( foreign ) )
			{
				return true;
			}
			return isInHeaderFooter( foreign );
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
	private HashMap inHeaderFooter = new HashMap();
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
		
		return isInHeaderFooter( text );
	}
	
	/**
	 * Checks if a text is in a table header/footer or group header/footer.
	 * 
	 * @param text
	 * @return
	 */
	private boolean isInHeaderFooter( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		IElement parent = content.getParent( );
		while( parent != null )
		{
			// The item should not extends from a container which is
			// not a table.
			if ( parent instanceof IContent )
			{
				IContent parentContent = (IContent) parent;
				Object parentGenerateBy = parentContent.getGenerateBy( );
				if ( parentGenerateBy instanceof ReportItemDesign )
				{
					ReportItemDesign design = (ReportItemDesign) parentGenerateBy;
					if ( design.getQuery( ) != null )
					{
						inHeaderFooter.put( generateBy, Boolean.FALSE );
						return false;
					}
				}
				else if ( null != parentGenerateBy )
				{
					inHeaderFooter.put( generateBy, Boolean.FALSE );
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
						inHeaderFooter.put( generateBy, Boolean.TRUE );
						return true;
					}
					// FIXME: code review: needs return a false value?
				}
			}
			parent = parent.getParent( );
		}
		inHeaderFooter.put(generateBy, Boolean.FALSE);
		return false;
	}
	
	//FIXME: code review: rename to getPredefineStyle or other names????
	public String getMetadataStyleClass( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		String styleName = null;
		if ( content instanceof ITextContent )
		{
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
				styleName = "birt-label-design";
			}
		}
		else if( content instanceof IImageContent)
		{
			if ( generateBy instanceof ExtendedItemDesign )
			{
				styleName = "birt-chart-design";
			}
		}
		//FIXME: should we still ouput "birt-label-design" for ForeignContent
		else if ( content instanceof IForeignContent )
		{
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
			writer.attribute( HTMLTags.ATTR_SRC, imagePath
					+ "./images/bizRD/sidetab_active.gif" );
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
	
	public void setHTMLIDNamespace( String namespace)
	{
		this.htmlIDNamespace = namespace;
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

class MetadataIDGenerator
{
	protected int bookmarkId = 0;
	MetadataIDGenerator( )
	{
		this.bookmarkId = 0;
	}
	protected String generateUniqueID( )
	{
		bookmarkId ++;
		return "AUTOGENMETADATABOOKMARK_" + bookmarkId;
	}
}