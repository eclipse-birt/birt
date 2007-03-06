/*
 * Java(TM) OLAP Interface
 */

package javax.olap;

public class OLAPException extends java.lang.Exception
{

	public OLAPException( )
	{
		super( );
	}

	public OLAPException( String reason )
	{
		super( reason );
	}

	public OLAPException( String reason, String OLAPState )
	{
		super( reason );
	}

	public OLAPException( String reason, String OLAPState, int vendorCode )
	{
		super( reason );
	}

	public String getOLAPState( )
	{
		return ( new String( "return implementation of error text" ) );
	}

	public int getErrorCode( )
	{
		int retval = 0;
		return ( retval );
	}

	public OLAPException getNextException( )
	{
		return ( new OLAPException( ) );
	}

	public void setNextException( OLAPException exception )
	{
	}
}
