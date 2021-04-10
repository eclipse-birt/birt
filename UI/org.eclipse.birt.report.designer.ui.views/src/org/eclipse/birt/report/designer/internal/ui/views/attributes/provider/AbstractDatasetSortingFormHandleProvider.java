
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.viewers.TableViewer;

public abstract class AbstractDatasetSortingFormHandleProvider extends AbstractSortingFormHandleProvider {

	public abstract void clearAllBindingColumns();

	public abstract boolean isClearEnable();

	public abstract void setBindingObject(DesignElementHandle bindingObject);

	public abstract void setTableViewer(TableViewer tableViewer);

	public abstract void generateAllBindingColumns();

}
