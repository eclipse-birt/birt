
package org.eclipse.birt.report.tests.engine;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

public abstract class BaseEmitter extends EngineCase implements IContentEmitter
{

	private String inPath = this.genInputFolder( ) + "/";
	private String outPath = this.genOutputFolder( ) + "/";

	protected final static String EMITTER_HTML = "html";
	protected final static String EMITTER_PDF = "emitter_pdf";

	protected abstract String getReportName( );

	/**
	 * @param format
	 *            render format
	 * @param pagination
	 *            For html output only, decide whether generate report with page
	 *            break or not.
	 * @throws EngineException
	 */
	protected ArrayList runandrender_emitter( String format, boolean pagination )
			throws EngineException
	{
		IReportRunnable reportRunnable = engine.openReportDesign( inPath
				+ getReportName( ) );
		IRunAndRenderTask task = engine.createRunAndRenderTask( reportRunnable );
		RenderOption options = new HTMLRenderOption( );
		options.setOutputFormat( format );
		if ( format.equals( EMITTER_HTML ) )
		{
			( (HTMLRenderOption) options ).setHtmlPagination( pagination );
		}
		HashMap appContext = new HashMap( );
		appContext.put( "emitter_class", this );
		task.setAppContext( appContext );
		task.setRenderOption( options );
		task.run( );
		ArrayList errors = (ArrayList) task.getErrors( );
		task.close( );
		return errors;
	}

	protected ArrayList runandthenrender_emitter( String format )
			throws EngineException
	{
		ArrayList errors = new ArrayList( );
		this.run( getReportName( ) + ".rptdesign", getReportName( )
				+ ".rptdocument" );
		IReportDocument document = engine.openReportDocument( outPath
				+ getReportName( ) + ".rptdocument" );
		IRenderTask task = engine.createRenderTask( document );
		RenderOption options = new HTMLRenderOption( );
		options.setOutputFormat( format );
		HashMap appContext = new HashMap( );
		appContext.put( "emitter_class", this );
		task.setAppContext( appContext );
		task.setRenderOption( options );
		task.render( );
		errors = (ArrayList) task.getErrors( );
		task.close( );
		return errors;

	}

	public void end( IReportContent report )
	{
	}

	public void endCell( ICellContent cell )
	{
	}

	public void endContainer( IContainerContent container )
	{
	}

	public void endContent( IContent content )
	{
	}

	public void endGroup( IGroupContent group )
	{
	}

	public void endList( IListContent list )
	{
	}

	public void endListBand( IListBandContent listBand )
	{
	}

	public void endListGroup( IListGroupContent group )
	{
	}

	public void endPage( IPageContent page )
	{
	}

	public void endRow( IRowContent row )
	{
	}

	public void endTable( ITableContent table )
	{
	}

	public void endTableBand( ITableBandContent band )
	{
	}

	public void endTableGroup( ITableGroupContent group )
	{
	}

	public String getOutputFormat( )
	{
		return null;
	}

	public void initialize( IEmitterServices service )
	{
	}

	public void start( IReportContent report )
	{
	}

	public void startAutoText( IAutoTextContent autoText )
	{
	}

	public void startCell( ICellContent cell )
	{
	}

	public void startContainer( IContainerContent container )
	{
	}

	public void startContent( IContent content )
	{
	}

	public void startData( IDataContent data )
	{
	}

	public void startForeign( IForeignContent foreign )
	{
	}

	public void startGroup( IGroupContent group )
	{
	}

	public void startImage( IImageContent image )
	{
	}

	public void startLabel( ILabelContent label )
	{
	}

	public void startList( IListContent list )
	{
	}

	public void startListBand( IListBandContent listBand )
	{
	}

	public void startListGroup( IListGroupContent group )
	{
	}

	public void startPage( IPageContent page )
	{
	}

	public void startRow( IRowContent row )
	{
	}

	public void startTable( ITableContent table )
	{
	}

	public void startTableBand( ITableBandContent band )
	{
	}

	public void startTableGroup( ITableGroupContent group )
	{
	}

	public void startText( ITextContent text )
	{
	}

	// protected String genOutputFile( String output )
	// {
	// String outputFile = this.genOutputFile( output );
	// return outputFile;
	// }

	// protected String getFullQualifiedClassName( )
	// {
	// String className = this.getClass( ).getName( );
	//		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
	// className = className.substring( 0, lastDotIndex );
	//
	// return className;
	// }

}
