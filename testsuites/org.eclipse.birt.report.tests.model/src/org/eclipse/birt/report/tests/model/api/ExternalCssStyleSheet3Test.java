package org.eclipse.birt.report.tests.model.api;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

public class ExternalCssStyleSheet3Test extends BaseTestCase {

	
	private String fileName = "ExternalCssStyleSheet3Test.css";
	
	public ExternalCssStyleSheet3Test(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(ExternalCssStyleSheet3Test.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource( );
		
		copyInputToFile ( INPUT_FOLDER + "/" + fileName );
	
	//	Platform.initialize( null );
		SessionHandle session = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = session.createDesign( );
	}

	public void testImportExternalCssStyleSheet() throws Exception {
			
		//open a external style sheet with relative filename
		designHandle.setBase(PLUGIN_PATH);
		
		CssStyleSheetHandle stylesheet = loadStyleSheet( getTempFolder()+"/"+INPUT_FOLDER+"/"+fileName );
		SharedStyleHandle style1 = stylesheet.findStyle("STYLE1");
		SharedStyleHandle style2 = stylesheet.findStyle("STYLE2");
		assertNotNull(style1);
		assertNotNull(style2);
		ArrayList styleList = new ArrayList();
		styleList.add(0, style1);
		styleList.add(1, style2);
	
		//import a external style sheet into a report design
		designHandle.importCssStyles(stylesheet, styleList);
		assertEquals(2,designHandle.getStyles().getCount());
	}
		public void testImportExternalCssStyleSheetWithFile() throws Exception {
			
	  //open a external style sheet with absolute filename and import it into a report
		
		//fileName = INPUT_FOLDER + "/" + fileName;
	    CssStyleSheetHandle stylesheet2 = loadStyleSheet( getTempFolder()+"/"+INPUT_FOLDER+"/"+fileName );
		SharedStyleHandle style2_1 = stylesheet2.findStyle("STYLE1");
		SharedStyleHandle style2_2 = stylesheet2.findStyle("STYLE2");
		assertNotNull(style2_1);
		assertNotNull(style2_2);
		ArrayList styleList2 = new ArrayList();
		styleList2.add(0, style2_1);
		styleList2.add(1, style2_2);
		designHandle.importCssStyles(stylesheet2, styleList2);
		assertEquals(2,designHandle.getStyles().getCount());
		
	   //open a no-existing external style
		try{
		 //CssStyleSheetHandle stylesheet3 = loadStyleSheet(fileName+"NoCssStyleSheet.xml");
			CssStyleSheetHandle stylesheet3 = loadStyleSheet(fileName);
			fail();
		}
		catch(Exception e)
		{
		 assertNotNull(e);
		}
		
	}
    
	private CssStyleSheetHandle loadStyleSheet( String fileName )
	throws Exception
       {
		//fileName = INPUT_FOLDER + "/" + fileName;
		return designHandle.openCssStyleSheet( fileName );
       }

	
}
