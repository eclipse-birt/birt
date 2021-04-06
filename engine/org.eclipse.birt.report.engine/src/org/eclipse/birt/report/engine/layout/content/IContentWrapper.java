package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

public interface IContentWrapper {
	IContent getContent();

	IReportItemExecutor getExecutor();
}
