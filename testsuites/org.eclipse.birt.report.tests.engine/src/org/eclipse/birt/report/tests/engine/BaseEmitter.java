
package org.eclipse.birt.report.tests.engine;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
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

	private String inPath = getClassFolder( ) + "/" + INPUT_FOLDER + "/";

	protected final static String EMITTER_HTML = "emitter_html";
	protected final static String EMITTER_PDF = "emitter_pdf";

	protected abstract String getReportName( );

	protected void runandrender_emitter( String format ) throws EngineException
	{
		IReportRunnable reportRunnable = engine.openReportDesign( inPath
				+ getReportName( ) );
		IRunAndRenderTask task = engine.createRunAndRenderTask( reportRunnable );
		RenderOptionBase options = new HTMLRenderOption( );
		options.setOutputFormat( format );
		HashMap appContext = new HashMap( );
		appContext.put( "emitter_class", this );
		task.setAppContext( appContext );
		task.setRenderOption( options );
		task.run( );
		task.close( );
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

}
