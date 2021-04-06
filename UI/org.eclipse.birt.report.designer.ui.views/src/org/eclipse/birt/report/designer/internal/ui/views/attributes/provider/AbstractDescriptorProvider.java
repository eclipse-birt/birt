
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.activity.SemanticException;

public abstract class AbstractDescriptorProvider implements IDescriptorProvider {

	private boolean canReset = false;

	public boolean canReset() {
		return canReset;
	}

	public void enableReset(boolean canReset) {
		this.canReset = canReset;
	}

	public void reset() throws SemanticException {
		if (canReset())
			save(null);
	}

}
