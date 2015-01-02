
package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * ILinkedDataSetHelper
 */
public interface ILinkedDataSetHelper
{

	List<String> getVisibleLinkedDataSets( );

	boolean setLinkedDataModel( ReportItemHandle handle, Object value );

	Iterator getResultSetIterator( String datasetName );

	Map<String, List<ResultSetColumnHandle>> getGroupedResultSetColumns(
			String datasetName );

	List<DataSetHandle> getVisibleLinkedDataSetsDataSetHandles(
			ModuleHandle handle );

	List<CubeHandle> getVisibleLinkedDataSetsCubeHandles( ModuleHandle handle );

}
