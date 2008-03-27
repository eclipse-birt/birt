
package org.eclipse.birt.report.engine.emitter.excel;
import java.io.Serializable;
import java.lang.*;

public class HyperlinkDef implements Serializable, Cloneable
{
	private String url;
	private int type;
    private String bookmark;
    private String toolTip;
    
	public HyperlinkDef( String url, int type, String bookmark ,String toolTip)
	{
		this.url = url;
		this.type = type;
		this.bookmark = bookmark;
		this.toolTip = toolTip;
	}

	public String getUrl( )
	{
		return url;
	}
    
	public String getBookmark( ) 
	{
		return bookmark;
	}
	public int getType( )
	{
		return type;
	}
	
	public void setUrl( String url) 
	{
	   	this.url = url;
	}
	
	public String getToolTip()
	{
		return toolTip;
	}
	
	public void setToolTip(String toolTip)
	{
		this.toolTip = toolTip;
	}
}
