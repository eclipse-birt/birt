
package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.emitter.excel.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;

public class XlsContainer
{
	private StyleEntry style;
	private ContainerSizeInfo sizeInfo;
	private HyperlinkDef link;
	private int startRowId;
	private boolean empty;
	private XlsContainer parent;

	private int rowIndex;

	public int getRowIndex( )
	{
		return rowIndex;
	}

	public void setRowIndex( int rowIndex )
	{
		this.rowIndex = rowIndex;
	}
	
	public XlsContainer( StyleEntry style, XlsContainer parent )
	{
		this(style, parent.getSizeInfo( ), parent);
	}	
	
	public XlsContainer( StyleEntry style, ContainerSizeInfo sizeInfo, XlsContainer parent )
	{
		this.style = style;
		this.sizeInfo = sizeInfo;
		this.parent = parent;
		this.rowIndex = parent != null ? parent.rowIndex : 0;
		empty = true;
		this.startRowId = rowIndex;
	}	
	
	public boolean isEmpty( )
	{
		return empty;
	}
	
	public void setEmpty( boolean empty )
	{
		this.empty = empty;
	}


	public StyleEntry getStyle( )
	{
		return style;
	}

	
	public void setStyle( StyleEntry style )
	{
		this.style = style;
	}

	
	public ContainerSizeInfo getSizeInfo( )
	{
		return sizeInfo;
	}

	
	public void setSizeInfo( ContainerSizeInfo sizeInfo )
	{
		this.sizeInfo = sizeInfo;
	}

	
	public HyperlinkDef getLink( )
	{
		return link;
	}

	
	public void setLink( HyperlinkDef link )
	{
		this.link = link;
	}

	public int getStartRowId( )
	{
		return startRowId;
	}
	
	public void setStartRowId( int startRowId )
	{
		this.startRowId = startRowId;
	}
	
	public XlsContainer getParent( )
	{
		return parent;
	}
}
