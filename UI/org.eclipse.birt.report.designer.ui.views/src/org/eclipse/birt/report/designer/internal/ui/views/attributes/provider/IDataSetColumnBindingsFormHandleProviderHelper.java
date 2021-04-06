package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DataSetHandle;

public interface IDataSetColumnBindingsFormHandleProviderHelper {

	public Iterator getResultSetIterator(DataSetHandle datasetHandle);

}
