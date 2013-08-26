
package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public interface ILinkedDataSetHelper
{

	public List<String> getVisibleLinkedDataSets( );

	public boolean setLinkedDataModel( ReportItemHandle handle, Object value );

	public Iterator getResultSetIterator( String datasetName );
	
	public List<DataSetHandle> getVisibleLinkedDataSetsDataSetHandles(ModuleHandle handle);

}
