
package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.emitter.excel.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;

public class XlsContainer
{
	private StyleEntry style;
	private Rule rule;
	private HyperlinkDef link;
	private int start;
	private boolean empty;

	public XlsContainer( StyleEntry style, Rule rule )
	{
		this.style = style;
		this.rule = rule;	
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

	
	public Rule getRule( )
	{
		return rule;
	}

	
	public void setRule( Rule rule )
	{
		this.rule = rule;
	}

	
	public HyperlinkDef getLink( )
	{
		return link;
	}

	
	public void setLink( HyperlinkDef link )
	{
		this.link = link;
	}


	
	public int getStart( )
	{
		return start;
	}
	
	public void setStart( int start )
	{
		this.start = start;
	}	
}
