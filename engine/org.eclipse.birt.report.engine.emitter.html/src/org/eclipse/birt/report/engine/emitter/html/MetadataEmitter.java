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

package org.eclipse.birt.report.engine.emitter.html;

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
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.internal.util.HTMLUtil;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;

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
	private IDGenerator idGenerator;
	private List ouputInstanceIDs;
	
	public MetadataEmitter( HTMLWriter writer, HTMLRenderOption htmlOption,
			IDGenerator idGenerator )
	{
		this.writer = writer;
		this.displayFilterIcon = htmlOption.getDisplayFilterIcon( );
		this.displayGroupIcon = htmlOption.getDisplayGroupIcon( );
		this.ouputInstanceIDs = htmlOption.getInstanceIDs( );
		this.idGenerator = idGenerator;
	}

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

	public void endTable( ITableContent table )
	{
		detailRowStateStack.pop( );
	}

	public void startRow( IRowContent row )
	{
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

	public void endRow( IRowContent row )
	{
		DetailRowState state = (DetailRowState) detailRowStateStack.peek( );
		if ( state.isStartOfDetail )
		{
			state.isStartOfDetail = false;
		}
	}

	public void startCell( ICellContent cell )
	{
		if ( needColumnFilter( cell ) || needGroupIcon( cell ) )
		{
			writer.openTag( HTMLTags.TAG_TABLE );
			writer.attribute( HTMLTags.ATTR_HEIGHT, "100%" );
			writer.attribute( HTMLTags.ATTR_WIDTH, "100%" );
			if ( needGroupIcon( cell ) )
			{
				writer.openTag( HTMLTags.TAG_COL );
				writer.attribute( "style", "width:" + getRowIndent( cell ) );
				writer.closeNoEndTag( );
				writer.openTag( HTMLTags.TAG_COL );
				writer.closeNoEndTag( );
				writer.openTag( HTMLTags.TAG_COL );
				writer.closeNoEndTag( );
			}
			writer.openTag( HTMLTags.TAG_TR );
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( "align", cell.getComputedStyle( ).getTextAlign( ) ); //$NON-NLS-1$
		}
		if ( needGroupIcon( cell ) )
		{
			// include select handle table
			writer.attribute( HTMLTags.ATTR_STYLE, "vertical-align:top"
					+ ";text-align:right" );
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

	private boolean needColumnFilter( ICellContent cell )
	{
		DetailRowState state = (DetailRowState) detailRowStateStack.peek( );
		IColumn columnInstance = cell.getColumnInstance( );
		if ( columnInstance == null )
		{
			return false;
		}
		return state.isStartOfDetail
				&& columnInstance.hasDataItemsInDetail( )
				&& displayFilterIcon
				&& HTMLUtil.getFilterConditions( cell ).size() > 0;
	}

	private boolean needGroupIcon( ICellContent cell )
	{
		return cell.isStartOfGroup( ) && displayGroupIcon;
	}
	
	private String getRowIndent( ICellContent cell )
	{
		IRowContent row = ( RowContent )cell.getParent( );
		int groupLevel = HTMLUtil.getGroupLevel( row );
		if ( groupLevel >= 0 )
		{
			return String.valueOf( HTMLUtil.getGroupLevel( row ) * 16 ) + "px";
		}
		return "0px";
	}

	public void endCell( ICellContent cell )
	{
		if ( needColumnFilter( cell ) )
		{
			// include select handle table
			writer.closeTag( HTMLTags.TAG_TD );
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( HTMLTags.ATTR_STYLE, "vertical-align:top" );
			writer.openTag( HTMLTags.TAG_IMAGE );
			writer.attribute( HTMLTags.ATTR_SRC, "iv/images/columnicon.gif" );
			writer.attribute( HTMLTags.ATTR_ALT, HTMLUtil
					.getColumnFilterText( cell ) );
			writer.attribute( HTMLTags.ATTR_STYLE, "cursor:pointer" );
			writer.attribute( HTMLTags.ATTR_COLUMN, cell.getColumnInstance( )
					.getInstanceID( ).toString( ) );
			String bookmark = idGenerator.generateUniqueID( );
			HTMLEmitterUtil.setBookmark( writer, null, bookmark );
			setActiveIDTypeIID( bookmark, "COLOUMNINFO", null, -1 );
			writer.closeTag( HTMLTags.TAG_IMAGE );
		}
		if ( needColumnFilter( cell ) || needGroupIcon( cell ) )
		{
			writer.closeTag( HTMLTags.TAG_TD );
			writer.closeTag( HTMLTags.TAG_TR );
			writer.closeTag( HTMLTags.TAG_TABLE );
		}
	}

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
	 * Checks if the text is a data content in table header/footer or table
	 * group header/footer and uses the query of the table.
	 * 
	 * @param text
	 *            the text content.
	 */
	private boolean isAggregatable( ITextContent text )
	{
		Object generateBy = text.getGenerateBy( );
		//The data item should not have query of itself.
		DataItemDesign data = ( DataItemDesign )generateBy;
		if ( data.getQuery( ) != null )
		{
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
				if ( design.getQuery( ) != null )
				{
					return false;
				}
			}

			// The data item should be in table header/footer or group
			// header/footer and its query extends from this table
			if ( parent instanceof IBandContent )
			{
				IBandContent bandContent = (IBandContent )parent;
				BandDesign bandDesing = (BandDesign) bandContent
						.getGenerateBy( );
				if ( bandDesing.getBandType( ) == BandDesign.BAND_HEADER
					 || bandDesing.getBandType( ) == BandDesign.BAND_FOOTER
					 || bandDesing.getBandType( ) == BandDesign.GROUP_HEADER
					 || bandDesing.getBandType( ) == BandDesign.GROUP_FOOTER)
				{
					
					IElement bandParent = bandContent.getParent( );
					while ( bandParent instanceof IGroupContent )
					{
						bandParent = bandParent.getParent( );
					}
					if ( bandParent instanceof ITableContent )
					{
						return true;
					}
				}
			}
			parent = parent.getParent( );
		}
		return false;
	}
	
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
}

class DetailRowState
{
	public boolean isStartOfDetail;
	public boolean hasOutput;
	public boolean isTable;
	public DetailRowState( boolean isStartOfDetail, boolean hasOutput,
			boolean isTable )
	{
		this.isStartOfDetail = isStartOfDetail;
		this.hasOutput = hasOutput;
		this.isTable = isTable;
	}
}