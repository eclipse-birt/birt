/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IMetadataFilter;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.internal.util.HTMLUtil;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.TableHandle;

import com.ibm.icu.util.TimeZone;

/**
 * Used to output metadata, including group collpase/expand icon, column filter
 * icon, element select handle container and instance id, element type id,etc.
 * 
 */
public class MetadataEmitter
{
	private static final String IMAGE_TYPE_NAME = "IMAGE_TYPE";
        private static final String RELATED_ENTITY_ID_NAME = "relatedEntityId";
	private static final String CONFIG_DATA_NAME = "config";

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
	private IDGenerator idGenerator;
	private List ouputInstanceIDs;
	private String imagePath;
	private String htmlIDNamespace;
	private HTMLReportEmitter htmlReportEmitter;
	/**
	 * attrNamePrefix must contain colon ':'.
	 */
	private String attrNamePrefix;
	
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
			String attrNamePrefix, IDGenerator idGenerator,
			HTMLReportEmitter htmlReportEmitter )
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
		assert idGenerator != null;
		this.idGenerator = idGenerator;
		this.attrNamePrefix = attrNamePrefix;
		initializeAttrName( attrNamePrefix );
		this.htmlReportEmitter = htmlReportEmitter;
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
			writer.attribute( "cellspacing", "0" );
			writer.attribute( "cellpadding", "0" );
			writer.openTag( HTMLTags.TAG_TR );
			//FIXME: code review: move the td outputting to "if ( needGroupIcon )". remove useless style.
			writer.openTag( HTMLTags.TAG_TD );
			IStyle cs = cell.getComputedStyle( );
			writer.attribute( HTMLTags.ATTR_VALIGN, cs.getVerticalAlign( ) );
			writer.attribute( "align", cs.getTextAlign( ) ); //$NON-NLS-1$
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
			htmlReportEmitter.outputBookmark( writer, null, htmlIDNamespace, bookmark );
			writer.attribute( attrElementType, "GROUP" );
			exportElementID( bookmark, "GROUP", -1 );
			writer.closeTag( HTMLTags.TAG_IMAGE );
			writer.closeTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_TD );
			IStyle cs = cell.getComputedStyle( );
			writer.attribute( HTMLTags.ATTR_VALIGN, cs.getVerticalAlign( ) );
			writer.attribute( "align", cs.getTextAlign( ) ); //$NON-NLS-1$
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
			htmlReportEmitter.outputBookmark( writer, null, htmlIDNamespace, bookmark );
			writer.attribute( attrElementType, "COLOUMNINFO" );
			exportElementID( bookmark, "COLOUMNINFO", -1 );
			writer.closeTag( HTMLTags.TAG_IMAGE );
		}
		if ( needColumnFilter || needGroupIcon )
		{
			writer.closeTag( HTMLTags.TAG_TD );
			writer.closeTag( HTMLTags.TAG_TR );
			writer.closeTag( HTMLTags.TAG_TABLE );
		}
	}

	private void exportElementID( String bookmark, String elementType, long componentID )
	{
		if ( ouputInstanceIDs != null )
		{

			if ( bookmark != null )
			{
				assert elementType != null;
				String htmlBookmark;
				if ( null != htmlIDNamespace )
				{
					htmlBookmark = htmlIDNamespace + bookmark;
				}
				else
				{
					htmlBookmark = bookmark;
				}
				StringBuffer buffer = new StringBuffer( );
				buffer.append( htmlBookmark );
				buffer.append( "," );
				buffer.append( elementType );
				buffer.append( "," );
				buffer.append( componentID );
				ouputInstanceIDs.add( buffer.toString( ) );
			}
		}
	}

	/**
	 * Output metadata properties.
	 * @param map
	 * @param element
	 * @param tagName
	 * @return boolean: has the bookmark been output?
	 */
	public boolean outputMetadataProperty( HashMap propertyMap, Object element,
			String tagName )
	{
		if ( propertyMap == null )
		{
			return false;
		}
		boolean iidOutput = false;
		boolean bookmarkOutput = false;
		boolean elementTypeOutput = false;
		boolean addToIIDList = false;
		InstanceID iid = null;
		String bookmark = null;
		String elementType = null;

		Iterator ite = propertyMap.entrySet( ).iterator( );
		while ( ite.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) ite.next( );
			Object keyObj = entry.getKey( );
			Object valueObj = entry.getValue( );
			if ( keyObj instanceof String )
			{
				String keyStr = (String) keyObj;
				if ( keyStr == IMetadataFilter.KEY_OUTPUT_IID )
				{
					Object genBy = null;
					if ( element instanceof IContent )
					{
						iid = ( (IContent) element ).getInstanceID( );
						genBy = ( (IContent) element ).getGenerateBy( );
					}
					else if ( element instanceof IColumn )
					{
						iid = ( (IColumn) element ).getInstanceID( );
						genBy = ( (IColumn) element ).getGenerateBy( );
					}
					if ( iid != null )
					{
						if ( ( genBy instanceof TableItemDesign )
								|| ( genBy instanceof ListItemDesign )
								|| ( genBy instanceof ExtendedItemDesign ) )
						{
							writer.attribute( attrIID, iid.toUniqueString( ) );
						}
						else
						{
							writer.attribute( attrIID, iid.toString( ) );
						}
						iidOutput = true;
					}
				}
				else if ( keyStr == IMetadataFilter.KEY_OUTPUT_BOOKMARK )
				{
					if ( element instanceof IContent )
					{
						IContent content = (IContent) element;
						bookmark = content.getBookmark( );
						if ( bookmark == null )
						{
							bookmark = idGenerator.generateUniqueID( );
							content.setBookmark( bookmark );
						}
						htmlReportEmitter.outputBookmark( writer,
								tagName,
								htmlIDNamespace,
								bookmark );
						bookmarkOutput = true;
					}
				}
				else if ( keyStr == IMetadataFilter.KEY_ATTR_ELEMENT_TYPE )
				{
					if ( valueObj == null )
					{
						continue;
					}
					elementType = (String) valueObj;
					writer.attribute( attrElementType, elementType );
					elementTypeOutput = true;
				}
				else if ( keyStr == IMetadataFilter.KEY_ADD_INTO_IID_LIST )
				{
					addToIIDList = true;
				}
				else if ( keyStr.equalsIgnoreCase( IMetadataFilter.KEY_ATTR_TYPE ) )
				{
					if ( valueObj != null )
					{
						writer.attribute( attrType, (String) valueObj );
					}
				}
				else if ( keyStr == IMetadataFilter.KEY_ATTR_ROW_TYPE )
				{
					if ( valueObj != null )
					{
						writer.attribute( attrRowType, (String) valueObj );
					}
				}
				else if ( keyStr == IMetadataFilter.KEY_OUTPUT_GOURP_ID )
				{
					if ( element instanceof IRowContent )
					{
						String groupId = ( (IRowContent) element ).getGroupId( );
						if ( groupId != null )
						{
							writer.attribute( HTMLTags.ATTR_GOURP_ID, groupId );
						}
					}
				}
				else if ( keyStr == IMetadataFilter.KEY_OUTPUT_RAW_DATA )
				{
					if ( element instanceof IDataContent )
					{
						Object value = ( (IDataContent) element ).getValue( );
						if ( value != null )
						{
							String rawData = null;
							try
							{
								// output time as GMT zone to make the value locale independent
								rawData = DataTypeUtil.toLocaleNeutralString( value, TimeZone.getTimeZone( "GMT" ) );
							}
							catch ( BirtException e )
							{
							}
							if ( rawData == null )
							{
								rawData = value.toString( );
							}
							writer.attributeAllowEmpty( HTMLTags.ATTR_RAW_DATA, rawData );
						}
					}
				} 
				else if ("OUTPUT_USER_PROPERTY".equalsIgnoreCase(keyStr)) {
				        outputRelatedData(element);
					outputConfigData(element);
					outputImageType(element);
				}
				else
				{
					if ( valueObj != null )
					{
						if ( keyStr.length( ) > 0 )
						{
							if ( attrNamePrefix != null )
							{
								writer.attribute( attrNamePrefix + keyStr,
										valueObj.toString( ) );
							}
							else
							{
								writer.attribute( keyStr, valueObj.toString( ) );
							}
						}
					}
				}
			}
		}

		if ( addToIIDList && iidOutput && bookmarkOutput && elementTypeOutput )
		{
			exportElementID( bookmark, elementType, iid.getComponentID( ) );
		}

		return bookmarkOutput;
	}

    private void outputImageType(Object element) {
        if (element instanceof IImageContent) {
            IImageContent content = (IImageContent) element;

            if (content.getUserProperties() != null) {
                Object imageType = content.getUserProperties().get(IMAGE_TYPE_NAME);
                if (imageType != null) {
                    try {
                        URLEncoder.encode((String) imageType, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    writer.attribute("data-imageType", imageType);
                }
            }
        }

    }

    private void outputRelatedData(Object element) {
		if (element instanceof IContent) {
			IContent content = (IContent)element;
			
			if (content.getUserProperties() != null) {
				Object relatedData = content.getUserProperties().get(RELATED_ENTITY_ID_NAME);
				if (relatedData != null) {
                                        try {
                                            URLEncoder.encode((String) relatedData, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
					writer.attribute("data-relatedData", relatedData);
				}
			}
		}
		
	}

	private void outputConfigData(Object element) {
		if (element instanceof IContent) {
			IContent content = (IContent)element;
			
			if (content.getUserProperties() != null) {
				Object configData = content.getUserProperties().get(CONFIG_DATA_NAME);
				if (configData != null) {
                                        try {
                                            URLEncoder.encode((String) configData, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
					writer.attribute("data-config", configData);
				}
			}
		}
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
	private boolean isTopLevelTemplateTable( IContent table )
	{
		DesignElementHandle handle = null;
		Object genBy = table.getGenerateBy( );
		if ( genBy instanceof TableItemDesign )
		{
			TableItemDesign tableDesign = (TableItemDesign) genBy;
			handle = tableDesign.getHandle( );
		}
		else if ( genBy instanceof TemplateDesign )
		{
			TemplateDesign templateDesign = (TemplateDesign) genBy;
			handle = templateDesign.getHandle( );
		}
		else if (genBy instanceof ExtendedItemDesign)
		{
			ExtendedItemDesign extendedItemDesign = (ExtendedItemDesign)genBy;
			handle = extendedItemDesign.getHandle( );
		}
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

		return false;
	}

	/**
	 * wrap the top-level template table
	 * 
	 * @param table
	 *            table content
	 */
	public void startWrapTable( IContent table )
	{
		if ( wrapTemplateTable && isTopLevelTemplateTable( table ) )
		{
			wrapperTableIID = table.getInstanceID( );

			writer.openTag( HTMLTags.TAG_TABLE );
			DimensionType width = table.getWidth( );
			if ( width != null )
			{
				StringBuffer styleBuffer = new StringBuffer( );
				styleBuffer.append( " border: medium none ; border-collapse: collapse; width: " );
				styleBuffer.append( width.toString( ) );
				styleBuffer.append( ";" );
			}
			else
			{
				writer.attribute( HTMLTags.ATTR_STYLE,
						" border: medium none ; border-collapse: collapse; width: 100%;" );
			}
			writer.openTag( HTMLTags.TAG_TBODY );
			writer.openTag( HTMLTags.TAG_TR );
			writer.attribute( HTMLTags.ATTR_STYLE, " vertical-align: top;" );
			writer.openTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_IMAGE );
			writer.attribute( HTMLTags.ATTR_SRC, imagePath
					+ "./bizRD/images/sidetab_active.png" );
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
	public void endWrapTable( IContent table )
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