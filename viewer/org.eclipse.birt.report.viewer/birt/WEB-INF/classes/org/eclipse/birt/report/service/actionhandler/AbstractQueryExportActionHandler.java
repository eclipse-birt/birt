package org.eclipse.birt.report.service.actionhandler;

import java.util.List;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.ExportedColumn;
import org.eclipse.birt.report.service.api.ExportedResultSet;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.Column;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.ResultSet;
import org.eclipse.birt.report.soapengine.api.ResultSets;

public abstract class AbstractQueryExportActionHandler extends
		AbstractBaseActionHandler
{
	/**
	 * Abstract method.
	 * 
	 * @param resultSets
	 */
	protected abstract void handleUpdate( ResultSets resultSets );

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public AbstractQueryExportActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	/**
	 * Action handler entry point.
	 * 
	 * @return
	 */
	protected void __execute( ) throws Exception
	{
		BaseAttributeBean attrBean = ( BaseAttributeBean ) context.getBean( );
		String docName = attrBean.getReportDocumentName( );

		List exportedResultSets;
		String instanceID = operation.getTarget().getId();

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		//exportedResultSets = getReportService( ).getResultSetsMetadata(
			//	docName, options );
		
		if(instanceID.equals("Document")) //$NON-NLS-1$
			exportedResultSets = getReportService( ).getResultSetsMetadata(
					docName, options );
		else
			exportedResultSets = getReportService( ).getResultSetsMetadata(
					docName, instanceID, options ); 

		if ( exportedResultSets == null )
		{
			// No result sets available
			AxisFault fault = new AxisFault( );
			fault.setFaultReason( "no result sets available." ); //$NON-NLS-1$
			throw fault;
		}

		ResultSet[] resultSetArray = getResultSetArray( exportedResultSets );
		ResultSets resultSets = new ResultSets( );
		resultSets.setResultSet( resultSetArray );
		handleUpdate( resultSets );
	}

	/**
	 * Prepare returned result set.
	 * 
	 * @param exportedResultSets
	 * @return
	 */
	private ResultSet[] getResultSetArray( List exportedResultSets )
	{
		assert exportedResultSets != null;

		ResultSet[] rsArray = new ResultSet[exportedResultSets.size( )];
		for ( int i = 0; i < exportedResultSets.size( ); i++ )
		{
			ExportedResultSet rs = ( ExportedResultSet ) exportedResultSets
					.get( i );
			List columns = rs.getColumns( );
			Column[] colArray = new Column[columns.size( )];
			for ( int j = 0; j < columns.size( ); j++ )
			{
				ExportedColumn col = ( ExportedColumn ) columns.get( j );
				colArray[j] = new Column( col.getName( ), col.getLabel( ),
						Boolean.valueOf( col.getVisibility( ) ) );
			}
			rsArray[i] = new ResultSet( rs.getQueryName( ), colArray );
		}

		return rsArray;
	}
}