package uk.co.spudsoft.birt.emitters.bugfix;

import org.eclipse.birt.report.engine.executor.ExecutorManager;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportItemExecutor;

public class FixedExecutorManager extends ExecutorManager {

	public FixedExecutorManager(ReportExecutor executor) {
		super(executor);
	}

	@Override
	protected ReportItemExecutor getItemExecutor(int type) {
		if ((type == LISTITEM)||(type == TABLEITEM)) {
			assert ( type >= 0 ) && ( type < NUMBER );
			if ( !freeList[type].isEmpty( ) )
			{
				// the free list is non-empty
				return (ReportItemExecutor) freeList[type].remove( );
			}
			if(type == LISTITEM) {
				return new FixedListItemExecutor( this );
			} else {
				return new FixedTableItemExecutor( this );
			}
		} else {
			return super.getItemExecutor(type);
		}
	}
	
	
	
	

}
