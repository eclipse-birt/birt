
package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

public interface IPropertyList {

	void setElements(Map categoryLabels);

	void setSelection(String key, int index);

	int getSelectionIndex();

	Control getControl();

	void addListener(int selection, Listener listener);

	String getSelectionKey();

	Object getTabList();

	Control getItem(int index);

}
