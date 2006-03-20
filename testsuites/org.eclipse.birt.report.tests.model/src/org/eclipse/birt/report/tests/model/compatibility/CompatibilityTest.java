package org.eclipse.birt.report.tests.model.compatibility;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;


public class CompatibilityTest extends BaseTestCase{

	String fileName = "DynamicTextExampleAfter.xml";
	String fileName1 = "DynamicTextExampleBefore.xml";
	String fileName2 = "GroupingExampleAfter.xml";
	String fileName3 = "GroupingExampleBefore.xml";
	String fileName4 = "HighlightExampleAfter.xml";
	String fileName5 = "HighlightExampleBefore.xml";
	String fileName6 = "HyperlinkingExampleBefore.xml";
	String fileName7 = "ImageExample.xml";
	String fileName8 = "ListingExampleAfter.xml";
	String fileName9 = "ListingExampleBefore.xml";
	String fileName10 = "MappingExampleAfter.xml";
	String fileName11 = "MappingExampleBefore.xml";
	String fileName12 = "ParallelReportExampleAfter.xml";
	String fileName13= "ParametersExampleAfter.xml";
	String fileName14= "ParametersExampleBefore.xml";
	String fileName15= "ProductCatalog.xml";
	String fileName16= "SalesInvoice.xml";
	String fileName17= "SalesOfAproduct.xml";
	String fileName18= "SubReportsExampleMainAfter.xml";
	String fileName19= "SubReportsExampleMainBefore.xml";
	String fileName20= "TopNPercent.xml";
	String fileName21= "TopSellingProducts.xml";
	
	public CompatibilityTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
    public static Test suite(){
		
		return new TestSuite(CompatibilityTest.class);
	}
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		
	}
	
	public void testOpenFile( ) throws DesignFileException
	{
		openDesign(fileName);
		openDesign(fileName1);
		openDesign(fileName2);
		openDesign(fileName3);
		openDesign(fileName4);
		openDesign(fileName5);
		openDesign(fileName6);
		openDesign(fileName7);
		openDesign(fileName8);
		openDesign(fileName9);
		openDesign(fileName10);
		openDesign(fileName11);
		openDesign(fileName12);
		openDesign(fileName13);
		openDesign(fileName14);
		openDesign(fileName15);
		openDesign(fileName16);
		openDesign(fileName17);
		openDesign(fileName18);
		openDesign(fileName19);
		openDesign(fileName20);
		openDesign(fileName21);
	}

}
