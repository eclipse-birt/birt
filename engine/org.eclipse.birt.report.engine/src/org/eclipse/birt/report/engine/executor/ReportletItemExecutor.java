
package org.eclipse.birt.report.engine.executor;

import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class ReportletItemExecutor extends ReportItemExecutor
{

	LinkedList<Query> queries = new LinkedList<Query>( );
	boolean hasNext = true;

	protected ReportletItemExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.REPORTLETITEM );
	}

	public void close( )
	{
		try
		{
			closeQueries( );
		}
		catch ( EngineException ex )
		{
			context.addException( ex );
		}
	}

	public IContent execute( )
	{
		try
		{
			openQueries( );
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return hasNext;
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNext )
		{
			hasNext = false;
			return manager.createExecutor( this, design );
		}
		return null;
	}

	void openQueries( ) throws BirtException
	{
		DocumentDataSource ds = context.getDataSource( );
		InstanceID iid = ds.getInstanceID( );
		// setup the unique id so the report let has the same sequence number.
		uniqueId = iid.getUniqueID( );

		// get all the parents
		LinkedList<InstanceID> parents = new LinkedList<InstanceID>( );
		InstanceID parentId = iid.getParentID( );
		while ( parentId != null )
		{
			parents.addFirst( parentId );
			parentId = parentId.getParentID( );
		}

		Report report = context.getReport( );
		for ( InstanceID pid : parents )
		{
			DataID dataId = pid.getDataID( );
			if ( dataId != null )
			{
				if ( !queries.isEmpty( ) )
				{
					Query lastQuery = queries.getLast( );
					lastQuery.rowId = dataId.getRowID( );
					lastQuery.cellId = dataId.getCellID( );
				}
			}
			// we need add the parent query to the query
			ReportElementDesign design = report.getReportItemByID( pid
					.getComponentID( ) );
			// set the parents
			if ( design instanceof ReportItemDesign )
			{
				IDataQueryDefinition[] qs = ( (ReportItemDesign) design )
						.getQueries( );
				if ( qs != null )
				{
					queries.add( new Query( qs ) );
				}
			}
		}

		DataID dataId = iid.getDataID( );
		if ( !queries.isEmpty( ) )
		{
			Query lastQuery = queries.getLast( );
			lastQuery.rowId = dataId.getRowID( );
			lastQuery.cellId = dataId.getCellID( );
		}

		executeQueries( queries );
	}

	void executeQueries( LinkedList<Query> queries ) throws BirtException
	{
		IBaseResultSet rset = null;
		for ( Query query : queries )
		{
			query.rsets = new IBaseResultSet[query.queries.length];
			for ( int i = 0; i < query.queries.length; i++ )
			{
				query.rsets[i] = context.executeQuery( rset, query.queries[i],
						false );
			}

			rset = query.rsets[0];
			if ( query.cellId != null )
			{
				( (ICubeResultSet) rset ).skipTo( query.cellId );
			}
			if ( query.rowId != -1 )
			{
				if ( rset.getType( ) == IBaseResultSet.QUERY_RESULTSET )
				{
					( (IQueryResultSet) rset ).skipTo( query.rowId );
				}
			}
		}
	}

	void closeQueries( ) throws EngineException
	{
		for ( Query query : queries )
		{
			if ( query.rsets != null )
			{
				for ( IBaseResultSet rset : query.rsets )
				{
					rset.close( );
				}
			}
		}
		queries.clear( );
	}

	class Query
	{

		IDataQueryDefinition[] queries;
		IBaseResultSet[] rsets;
		long rowId;
		String cellId;

		Query( IDataQueryDefinition[] qs )
		{
			this.queries = qs;
		}
	}
}
