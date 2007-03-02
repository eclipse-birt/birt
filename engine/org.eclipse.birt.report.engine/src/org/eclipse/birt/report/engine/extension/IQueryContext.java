
package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.model.api.ReportElementHandle;

public interface IQueryContext
{
	/**
	 * delegate to report engine to build query, the extened item may call this
	 * api to build children¡¯s query
	 */
	IBaseQueryDefinition[] buildQuery( IBaseQueryDefinition parent, ReportElementHandle handle );
}
