package uk.co.spudsoft.birt.emitters.bugfix;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.ReportExecutor;

public class FixedReportExecutor extends ReportExecutor {

	public FixedReportExecutor(ExecutionContext context) {
		super(context);
		this.manager = new FixedExecutorManager( this );
	}

	
}
