package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllApiTests {

	public static Test suite() {
		TestSuite test = new TestSuite();

//      add all test classes here
//		org.eclipse.birt.report.tests.engine.api
		test.addTestSuite(ActionHandleTest.class);
		test.addTestSuite(AutotextHandleTest.class);
		test.addTestSuite(ColumnHandleTest.class);
		test.addTestSuite(DefaultSearchFileAlgorithmTest.class);
		test.addTestSuite(DesignElementHandleTest.class);
		test.addTestSuite(DesignIncludeLibraryTest.class);
		test.addTestSuite(DynamicParameterTest.class);
		test.addTestSuite(EmbeddedImageHandleTest.class);
		test.addTestSuite(ExternalCssStyleSheet1Test.class);
		test.addTestSuite(ExternalCssStyleSheet3Test.class);
		test.addTestSuite(ExternalCssStyleSheet4Test.class);
		test.addTestSuite(GroupHandleTest.class);
		test.addTestSuite(IncludeLibraryRuleTest.class);
		test.addTestSuite(InputStreamURITest.class);
		test.addTestSuite(JointDataSetHandleTest.class);
		test.addTestSuite(LibraryAddTest.class);
		test.addTestSuite(LibraryCreateTest.class);
		test.addTestSuite(LibraryImportTest.class);
		test.addTestSuite(LibraryIncludeLibraryTest.class);
		test.addTestSuite(ModuleUtilTest.class);
		test.addTestSuite(MoveLibraryTest.class);
		test.addTestSuite(OpenDesignTest.class);
		test.addTestSuite(ReportDesignHandleTest.class);
		test.addTestSuite(ReportItemHandleTest.class);
		test.addTestSuite(SlotHandleTest.class);
		test.addTestSuite(TableItemHandleTest.class);
		test.addTestSuite(TemplateElementHandleTest.class);
		test.addTestSuite(ThemeTest.class);
		test.addTestSuite(TocSupportTest.class);
		test.addTestSuite(DimensionValueUtilTest.class);
		test.addTestSuite(StringUtilTest.class);

		/*
		 * Removed api case test.addTestSuite( CascadingParameterGroupTest.class );
		 * test.addTestSuite( CellHandleTest.class ); test.addTestSuite(
		 * ClientsDerivedIteratorTest.class ); test.addTestSuite( ColorHandleTest.class
		 * ); test.addTestSuite( ComponentsInGridHandleTest.class ); test.addTestSuite(
		 * CustomColorHandleTest.class ); test.addTestSuite( DataSetHandleTest.class );
		 * test.addTestSuite( DesignElementHandleGetXPathTest.class );
		 * test.addTestSuite( DimensionHandleTest.class ); test.addTestSuite(
		 * ExternalCssStyleSheet5Test.class ); test.addTestSuite(
		 * FactoryPropertyHandleTest.class ); test.addTestSuite(
		 * GridItemHandleTest.class ); test.addTestSuite( GroupElementHandleTest.class
		 * ); test.addTestSuite( GroupPropertyHandleTest.class ); test.addTestSuite(
		 * LabelHandleTest.class ); test.addTestSuite( MasterPageHandleTest.class );
		 * test.addTestSuite( OpenLibraryTest.class ); test.addTestSuite(
		 * PropertyHandleTest.class ); test.addTestSuite( PropertySortingTest.class );
		 * test.addTestSuite( ReportElementHandleTest.class ); test.addTestSuite(
		 * ScalarParameterHandleTest.class ); test.addTestSuite(
		 * SimpleMasterPageHandleTest.class ); test.addTestSuite(
		 * TranslationHandleTest.class ); test.addTestSuite(
		 * UserPropertyHandleTest.class );
		 */

		return test;
	}
}