
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine;


import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * 
 */

public class AllTests
{

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.report.engine" );
		//$JUnit-BEGIN$

		/* in package: org.eclipse.birt.report.engine.adapter */
		suite.addTestSuite( org.eclipse.birt.report.engine.adapter.ExpressionUtilTest.class );
		
		/* in package: org.eclipse.birt.report.engine.api */
		suite.addTestSuite( org.eclipse.birt.report.engine.api.DataIDTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.DataSetIDTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.DataSourceCompareTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.EngineExceptionTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.EngineTaskCancelTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.GetParameterDefinitionTaskTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.GetParameterGroupDefnTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.HTMLActionHandlerTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.HTMLCompleteImageHandlerTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.HTMLServerImageHandlerTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.InstanceIDTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.MutipleThreadRenderTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.PageHandlerTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ParameterConverterTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ProgressiveViewingTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.RelativeHyperlinkInReportDocumentTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.RenderTaskTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ReportEngineFactoryTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ReportEngineTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ReportletTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ReportRunnableTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.ReportRunnerTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.RunTaskTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.TOCNodeTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.TOCTest.class );
		
		/* in package: org.eclipse.birt.report.engine.api.impl */
		suite.addTestSuite( org.eclipse.birt.report.engine.api.impl.DataExtractionTaskTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.impl.GetParameterDefinitionTaskTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.impl.ReportDocumentTest.class );
		
		/* in package: org.eclipse.birt.report.engine.api.script */
		suite.addTestSuite( org.eclipse.birt.report.engine.api.script.RowDataTest.class );
		
		/* in package: org.eclipse.birt.report.engine.api.script.element */
		suite.addTestSuite( org.eclipse.birt.report.engine.api.script.element.ElementTest.class );
		
		/* in package: org.eclipse.birt.report.engine.api.script.instance */
		suite.addTestSuite( org.eclipse.birt.report.engine.api.script.instance.InstanceTest.class );
		
		/* in package: org.eclipse.birt.report.engine.content */
		suite.addTestSuite( org.eclipse.birt.report.engine.content.ReportContentReaderAndWriterTest.class );
		
		/* in package: org.eclipse.birt.report.engine.css */
		suite.addTestSuite( org.eclipse.birt.report.engine.css.CSSPaserTest.class );
		
		/* in package: org.eclipse.birt.report.engine.css.dom */
		suite.addTestSuite( org.eclipse.birt.report.engine.css.dom.StyleDeclarationTest.class );
		
		/* in package: org.eclipse.birt.report.engine.css.engine */
		suite.addTestSuite( org.eclipse.birt.report.engine.css.engine.PerfectHashTest.class );
		
		/* in package: org.eclipse.birt.report.engine.data.dte */
		suite.addTestSuite( org.eclipse.birt.report.engine.data.dte.DataEngineTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.data.dte.NamedExpressionTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.data.dte.ReportQueryBuilderTest.class );
		
		/* in package: org.eclipse.birt.report.engine.emitter */
		suite.addTestSuite( org.eclipse.birt.report.engine.emitter.EmbeddedHyperlinkProcessorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.emitter.XMLWriterTest.class );
		
		/* in package: org.eclipse.birt.report.engine.executor */
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.DataItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.ExecutorManagerTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.GridItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.ImageItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.LabelItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.ListItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.MultiLineItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.TableItemExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.TextItemExecutorTest.class );
		
		/* in package: org.eclipse.birt.report.engine.executor.buffermgr */
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.buffermgr.TableTest.class );
		
		/* in package: org.eclipse.birt.report.engine.executor.css */
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.css.CssParserTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.css.HTMLProcessorTest.class );
		
		/* in package: org.eclipse.birt.report.engine.executor.template */
		suite.addTestSuite( org.eclipse.birt.report.engine.executor.template.TemplateExecutorTest.class );
		
		/* in package: org.eclipse.birt.report.engine.i18n */
		suite.addTestSuite( org.eclipse.birt.report.engine.i18n.EngineResourceHandleTest.class );
		
		/* in package: org.eclipse.birt.report.engine.impl */
		suite.addTestSuite( org.eclipse.birt.report.engine.impl.ReportRunnerTest.class );
		
		/* in package: org.eclipse.birt.report.engine.internal */
		
		/* in package: org.eclipse.birt.report.engine.internal.document */
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.document.OffsetIndexReaderWriterTest.class );
		
		/* in package: org.eclipse.birt.report.engine.internal.document.v2 */
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.document.v2.ContentTreeCacheTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.document.v2.PageHintTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.document.v2.ReportContentTest.class );
		
		/* in package: org.eclipse.birt.report.engine.internal.executor.doc */
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.executor.doc.FragmentTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.executor.doc.ReportReaderTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.executor.doc.SegmentTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.internal.executor.doc.TreeFragmentTest.class );
		
		
		/* in package: org.eclipse.birt.report.engine.ir */
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ActionTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.CellTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ColumnTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.DataItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.DimensionTypeTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.DrillThroughActionDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.FreeFormItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.GraphicMasterPageTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.GridItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.HighlightTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ImageItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.LabelItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ListBandTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ListGroupTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ListItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.MapRuleTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.MapTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.DynamicTextItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.PageSequenceTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.PageSetupTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.ReportTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.RowTypeTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.SimpleMasterPageTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.TableBandTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.TableGroupTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.TableItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.ir.TextItemTest.class );
		
		/* in package: org.eclipse.birt.report.engine.layout */
		
		/* in package: org.eclipse.birt.report.engine.layout.content */
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.content.BlockStackingExecutorTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.content.ListContainerExecutorTest.class );
		
		/* in package: org.eclipse.birt.report.engine.layout.impl */
		
		/* in package: org.eclipse.birt.report.engine.layout.pdf */
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.pdf.PDFImageLMTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.pdf.PDFLineAreaLMTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.pdf.PDFTableLMTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.pdf.PDFPageLMTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.pdf.PDFTextLMTest.class );
		
		/* in package: org.eclipse.birt.report.engine.layout.pdf.hyphen */
		suite.addTestSuite( org.eclipse.birt.report.engine.layout.pdf.hyphen.DefaultWordRecognizerTest.class );
		
		/* in package: org.eclipse.birt.report.engine.parser */
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.DataDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.FreeFormDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.GridItemDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.HighlightTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.ImageItemDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.LabelItemDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.ListDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.MapDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.DynamicTextItemDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.PageSetupTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.StyleDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.SuppressDuplicateDataItemTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.TableItemDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.api.impl.ParameterPromptTextTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.TextDesignTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.parser.TextParserTest.class );
		
		/* in package: org.eclipse.birt.report.engine.presentation */
		suite.addTestSuite( org.eclipse.birt.report.engine.presentation.HtmlPaginateEmitterTest.class );
		suite.addTestSuite( org.eclipse.birt.report.engine.presentation.XMLContentReaderWriterTest.class );
		
		/* in package: org.eclipse.birt.report.engine.toc */
		suite.addTestSuite( org.eclipse.birt.report.engine.toc.TOCBuilderTest.class );
		
		/* in package: org.eclipse.birt.report.engine.util */
		suite.addTestSuite( org.eclipse.birt.report.engine.util.FileUtilTest.class );
		
		//$JUnit-END$
		return suite;
	}

}
