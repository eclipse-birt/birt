
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

public interface IComboDescriptorProvider extends IDescriptorProvider {

	String[] getItems();

	String getDisplayName(String key);

	boolean isReadOnly();
}
