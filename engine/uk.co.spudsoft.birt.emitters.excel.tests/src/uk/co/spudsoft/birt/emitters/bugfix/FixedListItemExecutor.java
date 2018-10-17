package uk.co.spudsoft.birt.emitters.bugfix;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutorManager;
import org.eclipse.birt.report.engine.executor.ListItemExecutor;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;

public class FixedListItemExecutor extends ListItemExecutor {

	public FixedListItemExecutor(ExecutorManager manager) {
		super(manager);
	}

	@Override
	protected void initializeContent(ReportElementDesign design,
			IContent content) {
		super.initializeContent(design, content);
		pageBreakInterval = ( (ListingDesign) design )
				.getPageBreakInterval( );
	}

}
