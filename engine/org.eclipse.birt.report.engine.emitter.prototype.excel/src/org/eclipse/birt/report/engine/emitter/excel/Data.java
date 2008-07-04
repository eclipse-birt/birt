
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	int rspan = 0;
	
	boolean processed = false;
	
	Rule rule;

	HyperlinkDef url;
	
	BookmarkDef bookmark;

	boolean isTxtData = true;
	
	Logger log = Logger.getLogger( Data.class.getName( ) );

	public static Data WASTE = new Data(null, null );
	
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
		if(txt == null)
			return " ";
		return txt.toString( );
	}
	
	public void formatTxt( )
	{
		if ( txt == null )
		{
			return;
		}
		else if ( datatype.equals( Data.DATE ) )
		{
			txt = ExcelUtil.formatDate( txt );
		}
		else if ( datatype.equals( Data.NUMBER ) )
		{
			Number number = (Number) txt;
			if ( ExcelUtil.isBigNumber( number ) )
			{
				txt = ExcelUtil.formatNumberAsScienceNotation( number );
			}
			else if ( txt.toString( ).length( ) > 31 )
			{
				txt = ExcelUtil.formatNumberAsDecimal( number );
			}
		}
	}
	
	public boolean isBigNumber()
	{
		if(txt==null)
		{
			return false;
		}
		else if(datatype.equals( Data.NUMBER ))
		{
			return ExcelUtil.isBigNumber( txt );
		}
		return false;
	}
	
	public Object getValue( )
	{
		return txt;
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
			log.log( Level.WARNING, "clone data failed" );
//			e.printStackTrace( );
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
	
	public int getRowSpan() {
		return rspan;
	}
	
	public void setRowSpan(int rs) {
		if(rs > 0) {
			this.rspan = rs;
		}
	}
	
	public void setProcessed(boolean pro) {
		this.processed = pro;
	}
	
	public boolean isProcessed() {
		return processed;
	}
	
	public BookmarkDef getBookmark( )
	{
		return bookmark;
	}

	
	public void setBookmark( BookmarkDef bookmark )
	{
		this.bookmark = bookmark;
	}
}