package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class TocSupportTest extends BaseTestCase
{
	String fileName = "TocSupportTest.xml";
	public TocSupportTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	 public static Test suite()
	    {
			
			return new TestSuite(TocSupportTest.class);
		}
		protected void setUp( ) throws Exception
		{
			super.setUp( );
		}
		public void testTocProperty( ) throws Exception
		{
			openDesign( fileName );

			TableHandle table = (TableHandle) designHandle
					.findElement( "MyTable" ); 
			assertNotNull("should not be null", table);
			assertNull(table.getTocExpression());
			table.setTocExpression("This Section");
			assertEquals("This Section",table.getTocExpression());
		}
}
