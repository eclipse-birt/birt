
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

public interface ISimpleComboDescriptorProvider extends IDescriptorProvider {

	public String[] getItems();

	public boolean isEditable();

	public boolean isSpecialProperty();
}
