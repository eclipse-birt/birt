
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

	public XlsContainer( StyleEntry style, ContainerSizeInfo sizeInfo )
	{
		this.style = style;
		this.sizeInfo = sizeInfo;	
		empty = true;
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
}
