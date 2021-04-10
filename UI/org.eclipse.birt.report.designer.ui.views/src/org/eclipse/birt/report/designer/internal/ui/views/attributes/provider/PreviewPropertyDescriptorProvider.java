
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public interface PreviewPropertyDescriptorProvider extends IDescriptorProvider {

	public DesignElementHandle getDesignElementHandle();

	public void setInput(Object input);

	public LabelProvider getLabelProvider();

	public IStructuredContentProvider getContentProvider(IModelEventProcessor processor);

	public String getColumnText(Object element, int columnIndex);

	public String getText(int index);

	public boolean moveDown(int index);

	public boolean edit(Object data, int itemCount);

	public boolean moveUp(int index);

	public boolean delete(int idx);

	public boolean add(int itemCount);

	public boolean duplicate(int idx);

}
