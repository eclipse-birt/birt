package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;

public class ItemExecutorWrapper extends ReportItemExecutorBase {
	protected IReportItemExecutor executor;
	protected IContent content;

	public ItemExecutorWrapper(IReportItemExecutor executor, IContent content) {
		this.executor = executor;
		this.content = content;
	}

	public void close() throws BirtException {
		executor.close();
	}

	public IContent execute() {
		return content;
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		return executor.getNextChild();
	}

	public boolean hasNextChild() throws BirtException {
		return executor.hasNextChild();
	}

}
