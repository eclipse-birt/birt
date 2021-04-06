
package org.eclipse.birt.report.data.oda.excel.ui.wizards;

public interface IChangeListener {

	/**
	 * Called with false when the listener is first attached to the model, and
	 * called with true every time the model's state changes.
	 */
	void update(boolean changed);
}
