/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RomDoc
{
    /**
     * 
     * @param args
     */
    
	public static void main( String[] args )
	{
        Generator generator = new Generator( );
        
        if( args.length > 0 )
        {
            // Output folder is specified
            
        	String arg0 = args[0];
            generator.setOutputDir( arg0 );
        }
        
		
		try
		{
			generator.generate( );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
