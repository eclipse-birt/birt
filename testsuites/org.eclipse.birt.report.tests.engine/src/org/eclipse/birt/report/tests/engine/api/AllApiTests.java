package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.tests.engine.api.script.ColumnMetaDataTest;
import org.eclipse.birt.report.tests.engine.api.script.DataSetRowTest;
import org.eclipse.birt.report.tests.engine.api.script.ReportContextTest;
import org.eclipse.birt.report.tests.engine.api.script.RowDataTest;
import org.eclipse.birt.report.tests.engine.api.script.ScriptedDataSetMetaDataTest;
import org.eclipse.birt.report.tests.engine.api.script.UpdatableDataSetRowTest;
import org.eclipse.birt.report.tests.engine.api.script.element.ElementTest;
import org.eclipse.birt.report.tests.engine.api.script.eventadapter.CellEventAdapterTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.CellEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.DataItemEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.DataSetEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.DataSourceEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.DynamicTextEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.GridEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.ImageEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.LabelEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.ListEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.ListGroupEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.ReportEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.RowEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.ScriptedDataSourceEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.TableEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.TableGroupEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.eventhandler.TextItemEventHandlerTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IActionInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.ICellInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IDataItemInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IDataSetInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IDataSourceInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IDynamicTextInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IGridInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IImageInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.ILabelInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.IListInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.RowInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.ScriptStyleTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.TableInstanceTest;
import org.eclipse.birt.report.tests.engine.api.script.instance.TextItemInstanceTest;

public class AllApiTests
{

	public static Test suite( )
	{
		TestSuite test = new TestSuite( );

        //add all test classes here
		//org.eclipse.birt.report.tests.engine.api
		test.addTestSuite( Bug128854PrompttextTest.class );
		test.addTestSuite( Bug128854PrompttextTest.class );
		test.addTestSuite( DataExtractionTaskTest.class );
		test.addTestSuite( DataIDTest.class );
		test.addTestSuite( DataSetIDTest.class );
		test.addTestSuite( DefaultStatusHandlerTest.class );
		test.addTestSuite( EngineConfigTest.class );
		test.addTestSuite( HTMLActionHandlerTest.class );
		test.addTestSuite( HTMLCompleteImageHandlerTest.class );
		test.addTestSuite( HTMLEmitterConfigTest.class );
		test.addTestSuite( HTMLRenderContextTest.class );
		test.addTestSuite( HTMLRenderOptionTest.class );
		test.addTestSuite( HTMLServerImageHandlerTest.class );
		test.addTestSuite( IActionTest.class );
		test.addTestSuite( IAutoTextContentTest.class );
		test.addTestSuite( IBandContentTest.class );
		test.addTestSuite( ICellContentTest.class );
		test.addTestSuite( IColumnTest.class );
		test.addTestSuite( IContentTest.class );
		test.addTestSuite( IDataContentTest.class );
		test.addTestSuite( IDataIteratorTest.class );
		test.addTestSuite( IElementTest.class );
		test.addTestSuite( IEmitterServicesTest.class );
		test.addTestSuite( IGetParameterDefinitionTaskTest.class );
		test.addTestSuite( IGroupContentTest.class );
		test.addTestSuite( IScalarParameterDefnTest.class );
		test.addTestSuite( RenderFolderDocumentTest.class );
		test.addTestSuite( RenderOptionBaseTest.class );
		test.addTestSuite( RenderTaskTest.class );
		test.addTestSuite( RenderUnfinishedReportDoc.class );
		test.addTestSuite( ReportDocumentTest.class );
		test.addTestSuite( ReportEngineTest.class );
		test.addTestSuite( ReportParameterConverterTest.class );
		test.addTestSuite( ResourceLocatorTest.class );
		test.addTestSuite( RunAndRenderTaskTest.class );
		test.addTestSuite( RunTaskTest.class );
		
		//org.eclipse.birt.report.tests.engine.api.script
		test.addTestSuite( ColumnMetaDataTest.class );
		test.addTestSuite( DataSetRowTest.class );
		test.addTestSuite( ReportContextTest.class );
		test.addTestSuite( RowDataTest.class );
		test.addTestSuite( ScriptedDataSetMetaDataTest.class );
		test.addTestSuite( UpdatableDataSetRowTest.class );
		
		//org.eclipse.birt.report.tests.engine.api.script.element
		test.addTestSuite( ElementTest.class );
		
		//org.eclipse.birt.report.tests.engine.api.script.eventadapter
		test.addTestSuite( CellEventAdapterTest.class );
		
		//org.eclipse.birt.report.tests.engine.api.script.eventhandler
		test.addTestSuite( CellEventHandlerTest.class );
		test.addTestSuite( DataItemEventHandlerTest.class );
		test.addTestSuite( DataSetEventHandlerTest.class );
		test.addTestSuite( DataSourceEventHandlerTest.class );
		test.addTestSuite( DynamicTextEventHandlerTest.class );
		test.addTestSuite( GridEventHandlerTest.class );
		test.addTestSuite( ImageEventHandlerTest.class );
		test.addTestSuite( LabelEventHandlerTest.class );
		test.addTestSuite( ListEventHandlerTest.class );
		test.addTestSuite( ListGroupEventHandlerTest.class );
		test.addTestSuite( ReportEventHandlerTest.class );
		test.addTestSuite( RowEventHandlerTest.class );
		test.addTestSuite( ScriptedDataSourceEventHandlerTest.class );
		test.addTestSuite( TableEventHandlerTest.class );
		test.addTestSuite( TableGroupEventHandlerTest.class );
		test.addTestSuite( TextItemEventHandlerTest.class );
		
		//org.eclipse.birt.report.tests.engine.api.script.instance
		test.addTestSuite( IActionInstanceTest.class );
		test.addTestSuite( ICellInstanceTest.class );
		test.addTestSuite( IDataItemInstanceTest.class );
		test.addTestSuite( IDataSetInstanceTest.class );
		test.addTestSuite( IDataSourceInstanceTest.class );
		test.addTestSuite( IDynamicTextInstanceTest.class );
		test.addTestSuite( IGridInstanceTest.class );
		test.addTestSuite( IImageInstanceTest.class );
		test.addTestSuite( ILabelInstanceTest.class );
		test.addTestSuite( IListInstanceTest.class );
		test.addTestSuite( RowInstanceTest.class );
		test.addTestSuite( ScriptStyleTest.class );
		test.addTestSuite( TableInstanceTest.class );
		test.addTestSuite( TextItemInstanceTest.class );
		
		return test;
	}
}