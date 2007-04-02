
package org.eclipse.birt.report.engine.internal.executor.l18n;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.ExecutorManager;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportItemExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExecutorContext;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class LocalizedReportExecutor implements IReportExecutor
{

	IReportExecutor executor;
	LocalizedContentVisitor l18nVisitor;
	LocalizedReportItemExecutorManager manager;
	ExecutionContext context;
	ExecutorContext executorContext;

	public LocalizedReportExecutor( ExecutionContext context,
			IReportExecutor executor )
	{
		this.l18nVisitor = new LocalizedContentVisitor( context );
		this.manager = new LocalizedReportItemExecutorManager( l18nVisitor );
		this.executor = executor;
		this.context = context;
		this.executorContext = new ExecutorContext( context );
	}

	public void close( )
	{
		executor.close( );
	}

	public IReportContent execute( )
	{
		return executor.execute( );
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return manager.createExecutor( childExecutor );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return executor.hasNextChild( );
	}

	public IPageContent createPage( long pageNumber, MasterPageDesign pageDesign )
	{
		return createLocalizedPage(pageNumber, pageDesign);
	}
	
	public IPageContent createLocalizedPage( long pageNo, MasterPageDesign masterPage )
	{
		IReportContent reportContent = context.getReportContent( );
		IPageContent pageContent = reportContent.createPageContent( );
		
		pageContent.setGenerateBy( masterPage );
		pageContent.setPageNumber( pageNo );
		context.setPageNumber( pageNo );
		
		if ( masterPage instanceof SimpleMasterPageDesign )
		{
			// disable the tocBuilder
			TOCBuilder tocBuilder = context.getTOCBuilder( );
			context.setTOCBuilder( null );
			context.setExecutingMasterPage( true );
			SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) masterPage;
			InstanceID iid = new InstanceID( null, pageDesign.getID( ), null );
			pageContent.setInstanceID( iid );
			
			//creat header, footer and body
			IContent header = reportContent.createContainerContent( ) ;
			pageContent.setPageHeader( header );
			header.setParent( pageContent );
			
			ExecutorManager manager = new ExecutorManager( context, null, executorContext );
			for ( int i = 0; i < pageDesign.getHeaderCount( ); i++ )
			{
				ReportItemDesign design = pageDesign.getHeader( i );
				ReportItemExecutor executor = manager.createExecutor( null, design );
				IContent content = executor.execute( );
				l18nVisitor.localize( content );
				header.getChildren( ).add( content );
				content.setParent( header );
				execute( executor, content );
			}
			
			//create body
			IContent body = reportContent.createContainerContent( ) ;
			pageContent.setPageBody( body );
			body.setParent( pageContent );


			//create footer
			IContent footer = reportContent.createContainerContent( ) ;
			pageContent.setPageFooter( footer );
			footer.setParent( pageContent );

			manager = new ExecutorManager( context, null, null);
			for ( int i = 0; i < pageDesign.getFooterCount( ); i++ )
			{
				ReportItemDesign design = pageDesign.getFooter( i );
				ReportItemExecutor executor = manager.createExecutor( null, design );
				IContent content = executor.execute( );
				l18nVisitor.localize( content );
				footer.getChildren( ).add( content );
				content.setParent( footer );
				execute( executor, content );
			}

			context.setExecutingMasterPage( false );
			// reenable the TOC
			context.setTOCBuilder( tocBuilder );
		}
		return pageContent;
	}
	
	protected void execute( IReportItemExecutor executor, IContent content )
	{
		if ( executor != null )
		{
			while ( executor.hasNextChild( ) )
			{
				IReportItemExecutor childExecutor = executor.getNextChild( );
				if ( childExecutor != null )
				{
					IContent childContent = childExecutor.execute( );
					childContent = l18nVisitor.localize( childContent );
					content.getChildren( ).add( childContent );
					execute( childExecutor, childContent );
					childExecutor.close( );
				}
			}
		}
	}

	public void execute( ReportDesignHandle reportDesign,
			IContentEmitter emitter )
	{
		IReportContent reportContent = execute( );
		if ( emitter != null )
		{
			emitter.start( reportContent );
		}
		while ( hasNextChild( ) )
		{
			IReportItemExecutor executor = getNextChild( );
			execute( executor, emitter );
			executor.close( );
		}
		if ( emitter != null )
		{
			emitter.end( reportContent );
		}
		close( );
	}

	protected void execute( IReportItemExecutor executor,
			IContentEmitter emitter )
	{
		IContent content = executor.execute( );
		if ( emitter != null )
		{
			ContentEmitterUtil.startContent( content, emitter );
		}
		while ( executor.hasNextChild( ) )
		{
			IReportItemExecutor child = executor.getNextChild( );
			execute( child, emitter );
			child.close( );
		}
		if ( emitter != null )
		{
			ContentEmitterUtil.endContent( content, emitter );
		}
	}
}
