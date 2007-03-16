
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.util.HTMLUtil;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

public class RowExecutor extends QueryItemExecutor
{
	protected RowExecutor( ExecutorManager manager )
	{
		super( manager );
	}

	int rowId;

	public void reset( )
	{
		super.reset( );
		this.rowId = 0;
	}

	void setRowId( int rowId )
	{
		this.rowId = rowId;
	}

	int getRowId( )
	{
		return rowId;
	}

	/**
	 * execute the row. The execution process is:
	 * <li> create a row content
	 * <li> push it into the context
	 * <li> intialize the content.
	 * <li> process bookmark, action, style and visibility
	 * <li> call onCreate if necessary
	 * <li> call emitter to start the row
	 * <li> for each cell, execute the cell
	 * <li> call emitter to close the row
	 * <li> pop up the row.
	 * 
	 * @param curRowContent
	 *            row id.
	 * @param body
	 *            table body.
	 * @param row
	 *            row design
	 */

	public IContent execute( )
	{
		RowDesign rowDesign = (RowDesign) getDesign( );
		IRowContent rowContent = report.createRowContent( );
		setContent( rowContent );

		executeQuery( );
		initializeContent( rowDesign, rowContent );

		processAction( rowDesign, rowContent );
		processBookmark( rowDesign, rowContent );
		processStyle( rowDesign, rowContent );
		processVisibility( rowDesign, rowContent );

		rowContent.setRowID( rowId );
		setGroupId( rowContent );

		if ( context.isInFactory( ) )
		{
			handleOnCreate( rowContent );
		}

		startTOCEntry( rowContent );
		if ( emitter != null )
		{
			emitter.startRow( rowContent );
		}

		// prepare to execute the children
		currentCell = 0;
		return rowContent;
	}

	private void setGroupId( IRowContent rowContent )
	{
		int groupLevel = HTMLUtil.getGroupLevel( rowContent );
		IBaseResultSet resultSet = getParentResultSet( );
		if ( groupLevel >= 0 && resultSet != null
				&& resultSet.getType( ) == IBaseResultSet.QUERY_RESULTSET )
		{
			rowContent.setGroupId( ( (IQueryResultSet) resultSet )
					.getGroupId( groupLevel ) );
		}
	}

	public void close( )
	{
		IRowContent rowContent = (IRowContent) getContent( );
		if ( emitter != null )
		{
			emitter.endRow( rowContent );
		}
		finishTOCEntry( );
		closeQuery( );
		manager.releaseExecutor( ExecutorManager.ROWITEM, this );
	}

	int currentCell;

	public boolean hasNextChild( )
	{
		RowDesign rowDesign = (RowDesign) design;
		return currentCell < rowDesign.getCellCount( );
	}

	public IReportItemExecutor getNextChild( )
	{
		RowDesign rowDesign = (RowDesign) design;
		if ( currentCell < rowDesign.getCellCount( ) )
		{
			CellDesign cellDesign = rowDesign.getCell( currentCell++ );
			CellExecutor executor = (CellExecutor) manager.createExecutor(
					this, cellDesign );
			return executor;
		}
		return null;
	}
}