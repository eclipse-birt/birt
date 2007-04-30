
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;

public class TableBandExecutor extends ContainerExecutor
{

	private int nextItem;

	protected TableBandExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.TABLEBANDITEM );
		nextItem = 0;
	}

	protected IContent doCreateContent( )
	{
		throw new IllegalStateException(
				"can't re-generate content for table band" );
	}

	protected void doExecute( ) throws Exception
	{
	}

	public void close( )
	{
		nextItem = 0;
		super.close( );
	}

	private TableItemExecutor tableExecutor;

	void setTableExecutor( TableItemExecutor tableExecutor )
	{
		this.tableExecutor = tableExecutor;
	}

	TableItemExecutor getTableExecutor( )
	{
		return this.tableExecutor;
	}

	protected ReportItemExecutor doCreateExecutor( long offset )
			throws Exception
	{
		TableBandDesign bandDesign = (TableBandDesign) getDesign( );
		if ( nextItem < bandDesign.getRowCount( ) )
		{
			RowDesign rowDesign = bandDesign.getRow( nextItem );
			RowExecutor rowExecutor = (RowExecutor) manager.createExecutor(
					this, rowDesign, offset );
			int rowId = tableExecutor.getRowId( );
			rowExecutor.setRowId( rowId );
			tableExecutor.setRowId( rowId++ );
			nextItem++;
			return rowExecutor;
		}
		return null;
	}

	protected void doSkipToExecutor( InstanceID id, long offset )
	{
		TableBandDesign bandDesign = (TableBandDesign) getDesign( );
		for ( int i = 0; i < bandDesign.getRowCount( ); i++ )
		{
			ReportItemDesign childDesign = bandDesign.getRow( i );
			if ( childDesign.getID( ) == id.getComponentID( ) )
			{
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = 0;
	}
}
