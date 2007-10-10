
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.Serializable;

import org.eclipse.birt.report.engine.emitter.excel.layout.Rule;

public class Data implements Serializable, Cloneable
{
	private static final long serialVersionUID = -316995334044186083L;

	private static int ID = 0;
	public static final String DATE = "DATE";
	public static final String NUMBER = "NUMBER";
	public static final String STRING = "STRING";
	public static final String CALENDAR = "CALENDAR";
	public static final String CDATETIME = "CDATETIME";
	//String txt;
    Object txt;

    int styleId, id;
    
    String datatype = Data.STRING;
    
	StyleEntry style;

	Span span;
	
	Rule rule;

	HyperlinkDef url;

	boolean isTxtData = true;

	public Data(final Object txt, final String datatype)
	{
	   this(txt, null, datatype);
	}
	
	public Data( final Object txt, final StyleEntry s, final String datatype )
	{
		this.txt = txt;		
		this.style = s;
		this.datatype = datatype;
		id = ID++;
		
	}
    
	protected void setNotTxtData( )
	{
		this.isTxtData = false;
	}
    
	public String getText()
    {
       if (txt == null) 
       {
          return " "; 
       }
       else if (datatype.equals( Data.DATE )) 
       { 
    	  return ExcelUtil.formatDate( txt );
       }
       else if(datatype.equals(Data.NUMBER) && txt.toString( ).length( ) > 31)
       {
    	  return ExcelUtil.formatNumber(txt);    
       }
       else
       {
    	   return txt.toString( );
       }
    }
	
	public int hashCode( )
	{
		return id;
	}
    public void setDatatype(String type) 
    {
       this.datatype = type;	
    }
    
    public String getDatatype()
    {
       return this.datatype;	
    }
	// shallow copy is necessary and sufficient
	protected Object clone( )
	{
		Object o = null;
		try
		{
			o = super.clone( );
		}
		catch ( final CloneNotSupportedException e )
		{
			e.printStackTrace( );
		}
		return o;
	}

	public boolean equals( final Object o )
	{
		if ( o == this )
		{
			return true;
		}
		if ( !( o instanceof Data ) )
		{
			return false;
		}
		final Data data = (Data) o;
		if ( data.id == id )
		{
			return true;
		}
		return false;
	}
	
	public void setStyleId(int id)
	{
		this.styleId = id;
	}
	
	public int getStyleId()
	{
		return styleId;
	}
	
	public void setStyleEntry(StyleEntry entry)
	{
		this.style = entry;
	}
	
	public StyleEntry getStyleEntry()
	{
		return style;
	}
 
	public HyperlinkDef getHyperlinkDef( ) {
	   return url;
	}
	
	public void setHyperlinkDef( HyperlinkDef def ) {
	   this.url = def;
	}
	
	public void setRule(Rule rule)
	{
		this.rule = rule;
	}
	
	public Rule getRule()
	{
		return rule;
	}
	
	public void setSpan(Span span)
	{
		this.span = span;
	}
	
	public Span getSpan()
	{
		return span;
	}    	
}