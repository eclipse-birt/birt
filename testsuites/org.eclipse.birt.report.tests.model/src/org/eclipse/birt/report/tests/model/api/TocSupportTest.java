package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class TocSupportTest extends BaseTestCase
{
	String fileName = "TocSupportTest.xml";
	String fileName1 = "TocSupportTest_1.xml";
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
			removeResource( );
			
			copyInputToFile ( INPUT_FOLDER + "/" + fileName1 );
			copyInputToFile ( INPUT_FOLDER + "/" + fileName1 );
			
		}
		public void tearDown( )
		{
			removeResource( );
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
		public void testGetAllToc( ) throws Exception
		{
			openDesign( fileName1 );
			assertEquals(3,designHandle.getAllTocs().size());
			
			//add toc
            TextItemHandle text = (TextItemHandle)designHandle.findElement("mytext");
			text.setTocExpression("Mytext");
			assertEquals(4,designHandle.getAllTocs().size());
			designHandle.getCommandStack().undo();
			assertEquals(3,designHandle.getAllTocs().size());
			designHandle.getCommandStack().redo();
			
			//remove toc
			text.clearProperty(IReportItemModel.TOC_PROP);
			assertEquals(3,designHandle.getAllTocs().size());
		}
}
