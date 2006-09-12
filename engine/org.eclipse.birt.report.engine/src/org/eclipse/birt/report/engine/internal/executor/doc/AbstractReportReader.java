
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.toc.DocumentTOCTree;
import org.eclipse.birt.report.engine.toc.TOCTree;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public abstract class AbstractReportReader implements IReportExecutor
{

	protected static Logger logger = Logger
			.getLogger( AbstractReportReader.class.getName( ) );

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	protected CachedReportContentReaderV3 reader;

	protected Report report;
	protected IReportDocument reportDoc;
	protected ReportContent reportContent;
	protected IContent dummyReportContent;

	ReportItemReaderManager manager;

	public AbstractReportReader( ExecutionContext context )
	{
		assert context.getDesign( ) != null;
		assert context.getReportDocument( ) != null;

		this.context = context;

		report = context.getReport( );

		reportContent = (ReportContent) ContentFactory
				.createReportContent( report );
		context.setReportContent( reportContent );

		dummyReportContent = new LabelContent( (ReportContent) reportContent );
		dummyReportContent.setStyleClass( report.getRootStyleName( ) );

		reportDoc = context.getReportDocument( );

		TOCTree tocTree = new DocumentTOCTree(reportDoc);
		reportContent.setTOCTree(tocTree);
		
		long totalPage = reportDoc.getPageCount( );
		context.setTotalPage( totalPage );
		reportContent.setTotalPage( totalPage );

		dataEngine = context.getDataEngine( );
		dataEngine.prepare( report, context.getAppContext( ) );

		manager = new ReportItemReaderManager( this );
	}

	public void close( )
	{
		closeReaders( );
	}

	IContent loadContent( long offset )
	{
		try
		{
			IContent content = reader.loadContent( offset );
			if ( content != null )
			{
				if ( content.getParent( ) == null )
				{
					content.setParent( dummyReportContent );
				}
			}
			return reader.loadContent( offset );
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, "Can't load the content", ex );
		}
		return null;
	}

	void unloadContent( long offset )
	{
		reader.unloadContent( offset );
	}

	protected void openReaders( ) throws IOException
	{
		IDocArchiveReader archive = reportDoc.getArchive( );
		RAInputStream in = archive
				.getStream( ReportDocumentConstants.CONTENT_STREAM );
		reader = new CachedReportContentReaderV3( reportContent, in );
	}

	protected void closeReaders( )
	{
		if ( reader != null )
		{
			reader.close( );
			reader = null;
		}
	}

	protected void initializeContent( IContent content )
	{
		// set up the report content
		content.setReportContent( reportContent );

		// set up the design object
		InstanceID id = content.getInstanceID( );
		if ( id != null )
		{
			long designId = id.getComponentID( );
			if ( designId != -1 )
			{
				Object generateBy = report.getReportItemByID( designId );
				content.setGenerateBy( generateBy );
				// System.out.println( generateBy.getClass( ));
				if ( generateBy instanceof ReportItemDesign )
				{
					context.setItemDesign( (ReportItemDesign) generateBy );
				}
			}
		}
		context.setContent( content );
	}

	protected IResultSet openQuery( IResultSet rset, IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		// open the query associated with the current report item
		if ( generateBy instanceof ReportItemDesign )
		{
			ReportItemDesign design = (ReportItemDesign) generateBy;
			IBaseQueryDefinition query = design.getQuery( );
			if ( query != null )
			{
				InstanceID iid = content.getInstanceID( );
				if ( iid != null )
				{
					// To the current report item,
					// if the dataId exist and it's deteSet id is not null,
					// and we can find it has parent,
					// we'll try to skip to the current row of the parent
					// query.
					DataID dataId = iid.getDataID( );
					if ( dataId != null )
					{
						DataSetID dataSetId = dataId.getDataSetID( );
						if ( dataSetId != null )
						{
							DataSetID parentSetId = dataSetId.getParentID( );
							long parentRowId = dataSetId.getRowID( );
							if ( parentSetId != null && parentRowId != -1 )
							{
								// the parent exist.
								if ( rset != null )
								{
									// the parent query's result set is
									// not null, skip to the right row
									// according row id.
									if ( parentRowId != rset
											.getCurrentPosition( ) )
									{
										rset.skipTo( parentRowId );
									}
								}
							}
						}
					}
				}
				// execute query
				try
				{
					rset = context.executeQuery( rset, query );
				}
				catch ( BirtException ex )
				{
					context.addException( ex );
				}
			}
		}
		// locate the row position to the current position
		InstanceID iid = content.getInstanceID( );
		if ( iid != null )
		{
			DataID dataId = iid.getDataID( );
			if ( dataId != null )
			{
				if ( rset != null )
				{
					long rowId = dataId.getRowID( );
					
					//rowId should not be -1. If rowId equals to -1 that means the result set is empty.
					//call IResultIterator.next() to force result set start.
					if ( rowId == -1 )
					{
						rset.next( );
					}
					if ( rowId != -1 && rowId != rset.getCurrentPosition( ) )
					{
						rset.skipTo( rowId );
					}
				}
			}
		}
		return rset;
	}

	protected void closeQuery( IResultSet rset )
	{
		if ( rset != null )
		{
			rset.close( );
		}
	}

	protected IContentVisitor initalizeContentVisitor = new ContentVisitorAdapter( ) {

		public Object visitLabel( ILabelContent label, Object value )
		{
			if ( label.getGenerateBy( ) instanceof TemplateDesign )
			{
				TemplateDesign design = (TemplateDesign) label.getGenerateBy( );
				label.setLabelKey( design.getPromptTextKey( ) );
				label.setLabelText( design.getPromptText( ) );
			}
			return value;
		}

		public Object visitAutoText( IAutoTextContent autoText, Object value )
		{
			if ( autoText.getType( ) == IAutoTextContent.TOTAL_PAGE )
			{
				autoText.setText( String.valueOf( reportDoc.getPageCount( ) ) );
			}
			return value;
		}

		public Object visitTable( ITableContent table, Object value )
		{
			int colCount = table.getColumnCount( );
			for ( int i = 0; i < colCount; i++ )
			{
				IColumn col = table.getColumn( i );
				InstanceID id = col.getInstanceID( );
				if ( id != null )
				{
					long cid = id.getComponentID( );
					ColumnDesign colDesign = (ColumnDesign) report
							.getReportItemByID( cid );
					col.setGenerateBy( colDesign );
				}
			}

			return value;

		}

		public Object visitData( IDataContent data, Object value )
		{
			if ( data.getGenerateBy( ) instanceof DataItemDesign )
			{
				DataItemDesign design = (DataItemDesign) data.getGenerateBy( );
				if ( design.getMap( ) == null )
				{
					String valueExpr = design.getValue( );
					if ( valueExpr != null )
					{
						Object dataValue = context.evaluate( valueExpr );
						data.setValue( dataValue );
					}
				}
			}
			return value;
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			int bandType = tableBand.getBandType( );
			switch ( bandType )
			{
				case IBandContent.BAND_HEADER :
					ITableContent table = getParentTable( tableBand );
					Object genObj = table.getGenerateBy( );
					if ( genObj instanceof TableItemDesign )
					{
						TableItemDesign tableDesign = (TableItemDesign) genObj;
						tableBand.setGenerateBy( tableDesign.getHeader( ) );
					}
					break;

				case IBandContent.BAND_FOOTER :
					table = getParentTable( tableBand );
					genObj = table.getGenerateBy( );
					if ( genObj instanceof TableItemDesign )
					{
						TableItemDesign tableDesign = (TableItemDesign) genObj;
						tableBand.setGenerateBy( tableDesign.getFooter( ) );
					}
					break;
				case IBandContent.BAND_DETAIL :
					table = getParentTable( tableBand );
					genObj = table.getGenerateBy( );
					if ( genObj instanceof TableItemDesign )
					{
						TableItemDesign tableDesign = (TableItemDesign) genObj;
						tableBand.setGenerateBy( tableDesign.getDetail( ) );
					}
					break;

				case IBandContent.BAND_GROUP_FOOTER :
				case IBandContent.BAND_GROUP_HEADER :
					setupGroupBand( tableBand );
					break;
				default :
					assert false;
			}
			return value;
		}

		ITableContent getParentTable( ITableBandContent band )
		{
			IContent parent = (IContent) band.getParent( );
			while ( parent != null )
			{
				if ( parent instanceof ITableContent )
				{
					return (ITableContent) parent;
				}
				parent = (IContent) parent.getParent( );
			}
			return null;
		}

		IListContent getParentList( IListBandContent band )
		{
			IContent parent = (IContent) band.getParent( );
			while ( parent != null )
			{
				if ( parent instanceof IListContent )
				{
					return (IListContent) parent;
				}
				parent = (IContent) parent.getParent( );
			}
			return null;
		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			int bandType = listBand.getBandType( );
			switch ( bandType )
			{
				case IBandContent.BAND_HEADER :
					IListContent list = getParentList( listBand );
					Object genObj = list.getGenerateBy( );
					if ( genObj instanceof ListItemDesign )
					{
						ListItemDesign listDesign = (ListItemDesign) genObj;
						listBand.setGenerateBy( listDesign.getHeader( ) );
					}
					break;

				case IBandContent.BAND_FOOTER :
					list = getParentList( listBand );
					genObj = list.getGenerateBy( );
					if ( genObj instanceof ListItemDesign )
					{
						ListItemDesign listDesign = (ListItemDesign) genObj;
						listBand.setGenerateBy( listDesign.getFooter( ) );
					}
					break;

				case IBandContent.BAND_DETAIL :
					list = getParentList( listBand );
					genObj = list.getGenerateBy( );
					if ( genObj instanceof ListItemDesign )
					{
						ListItemDesign listDesign = (ListItemDesign) genObj;
						listBand.setGenerateBy( listDesign.getDetail( ) );
					}
					break;

				case IBandContent.BAND_GROUP_FOOTER :
				case IBandContent.BAND_GROUP_HEADER :
					setupGroupBand( listBand );
					break;
				default :
					assert false;
			}
			return value;
		}

		protected void setupGroupBand( IBandContent bandContent )
		{
			IContent parent = (IContent) bandContent.getParent( );
			if ( parent instanceof IGroupContent )
			{
				IGroupContent group = (IGroupContent) parent;
				Object genBy = group.getGenerateBy( );
				if ( genBy instanceof GroupDesign )
				{
					GroupDesign groupDesign = (GroupDesign) genBy;
					int bandType = bandContent.getBandType( );
					if ( bandType == IBandContent.BAND_GROUP_FOOTER )
					{
						bandContent.setGenerateBy( groupDesign.getHeader( ) );
					}
					else
					{
						bandContent.setGenerateBy( groupDesign.getFooter( ) );
					}
				}
			}
		}
	};

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
		}
		if ( emitter != null )
		{
			ContentEmitterUtil.endContent( content, emitter );
		}
	}
}
