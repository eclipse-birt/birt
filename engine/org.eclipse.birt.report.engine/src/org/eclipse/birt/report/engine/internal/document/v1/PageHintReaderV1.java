
package org.eclipse.birt.report.engine.internal.document.v1;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentReader;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.presentation.IPageHint;

public class PageHintReaderV1 implements IPageHintReader
{

	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );

	protected IReportDocument document;
	ArrayList pageHints = new ArrayList( );

	public PageHintReaderV1( IReportDocument document )
	{
		this.document = document;
	}

	public void open( ) throws IOException
	{

		RAInputStream in = null;
		try
		{
			IDocArchiveReader reader = document.getArchive( );

			in = reader.getStream( ReportDocumentConstants.PAGEHINT_STREAM );
			if ( in != null )
			{
				DataInputStream di = new DataInputStream(
						new BufferedInputStream( in ) );
				long pageCount = IOUtil.readLong( di );
				for ( long i = 0; i < pageCount; i++ )
				{
					PageHintV1 hint = new PageHintV1( );
					hint.readObject( di );
					pageHints.add( hint );
				}
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the page hints", ex ); //$NON-NLS-1$
		}
		finally
		{
			try
			{
				if ( in != null )
				{
					in.close( );
				}
			}
			catch ( Exception ex )
			{
			}
		}
	}

	public void close( )
	{
	}

	public long getTotalPage( ) throws IOException
	{
		return pageHints.size( );
	}

	public IPageHint getPageHint( long pageNumber ) throws IOException
	{
		return (PageHintV1) pageHints.get( (int) pageNumber );
	}
	
	public long findPage(long offset) throws IOException
	{
		for (int i = 0; i < pageHints.size( ); i++)
		{
			IPageHint hint = (PageHintV1)pageHints.get( i );
			if (hint.getSectionStart( 0 ) > offset)
			{
				return i + 1;
			}
		}
		return pageHints.size( );
	}

}
