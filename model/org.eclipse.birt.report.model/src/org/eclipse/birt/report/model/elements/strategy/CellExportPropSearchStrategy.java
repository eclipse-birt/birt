
package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

public class CellExportPropSearchStrategy extends CellPropSearchStrategy {

	private static final CellExportPropSearchStrategy instance = new CellExportPropSearchStrategy();

	protected CellExportPropSearchStrategy() {
	}

	public static CellExportPropSearchStrategy getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getPropertyRelatedToContainer(Module module, DesignElement cell, ElementPropertyDefn prop) {
		// When exporting cells, should not export the properties related to
		// container
		return null;
	}
}
