
package org.eclipse.birt.report.engine.api.impl;

import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.IReportDocument;

public class ReportDocumentReaderTest extends EngineCase
{

	static final String DOCUMENT_V0 = "org/eclipse/birt/report/engine/api/impl/reportdocument_v0.rptdocument";
	static final String DOCUMENT_V1 = "org/eclipse/birt/report/engine/api/impl/reportdocument_v1.rptdocument";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DOCUMENT );
	}

	public void tearDown( ) throws Exception
	{
		removeFile( REPORT_DOCUMENT );
		super.tearDown();
	}

	public void testVersion0( ) throws Exception
	{
		copyResource( DOCUMENT_V0, REPORT_DOCUMENT );
		ReportDocumentReader docReader = null;
		try
		{
			IReportDocument document = engine
					.openReportDocument( REPORT_DOCUMENT );
			if ( document instanceof ReportDocumentReader )
			{
				docReader = (ReportDocumentReader) document;
				List list = docReader.getBookmarks( );
				assertTrue( list.size( ) > 0 );
				list = docReader.getBookmarkContents( );
				assertTrue( list.size( ) == 0 );
			}
		}
		finally
		{
			if ( docReader != null )
			{
				docReader.close( );
			}
		}
	}

	public void testVersion1( ) throws Exception
	{
		copyResource( DOCUMENT_V1, REPORT_DOCUMENT );
		ReportDocumentReader docReader = null;
		try
		{
			IReportDocument document = engine
					.openReportDocument( REPORT_DOCUMENT );
			if ( document instanceof ReportDocumentReader )
			{
				docReader = (ReportDocumentReader) document;
				List list = docReader.getBookmarks( );
				assertTrue( list.size( ) > 0 );
				list = docReader.getBookmarkContents( );
				assertTrue( list.size( ) > 0 );
			}
		}
		finally
		{
			if ( docReader != null )
			{
				docReader.close( );
			}
		}
	}
}
