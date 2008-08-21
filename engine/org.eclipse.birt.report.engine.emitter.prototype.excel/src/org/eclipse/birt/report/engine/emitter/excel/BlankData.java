package org.eclipse.birt.report.engine.emitter.excel;



public class BlankData extends Data
{

	private Data data;
	
	public static BlankData BLANK = new BlankData(null);
	
	public BlankData( Data data )
	{
		super( null, null, null );
		this.data = data;
	}

	public boolean isBlank()
	{
		return true;
	}
	
    public Data getData()
    {
    	return data;
    }

    public int getRowSpan( )
    {
    	if ( data != null )
    	{
    		return data.getRowSpan( );
    	}
    	return 0;
    }
    
    public void setRowSpan( int rowSpan )
    {
    	if ( data != null )
    	{
    		data.setRowSpan( rowSpan );
    	}
    }
    public int getRowSpanInDesign( )
    {
    	if ( data != null )
    	{
    		return data.getRowSpanInDesign( );
    	}
    	return 0;
    }
    
	public void decreasRowSpanInDesign( )
	{
		if ( data != null )
		{
			data.decreasRowSpanInDesign( );
		}
	}
}
