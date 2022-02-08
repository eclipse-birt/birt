/***********************************************************************
 * Copyright (c) 2004-2011 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSubtaskEntry;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;

public class DefaultRegisteredSubtaskEntryImpl extends DefaultRegisteredEntry<ISubtaskSheet>
		implements IRegisteredSubtaskEntry {

	private int nodeIndex = 0;

	private int priority = 0;

	private String sNodePath = ""; //$NON-NLS-1$

	public DefaultRegisteredSubtaskEntryImpl(String sNodeIndex, String sNodePath, String sDisplayName,
			ISubtaskSheet sheet) {
		super(sheet, sDisplayName, sNodePath);
		try {
			double nodeIndexWithPriority = Double.valueOf(sNodeIndex);
			nodeIndex = (int) nodeIndexWithPriority;
			priority = (int) (nodeIndexWithPriority * 10 - nodeIndex * 10);
		} catch (NumberFormatException e) {
		}
		this.sNodePath = sNodePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSheetEntry#
	 * getRegisteredNodePath()
	 */
	public int getNodeIndex() {
		return nodeIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSheetEntry#
	 * getRegisteredNodePath()
	 */
	public String getNodePath() {
		return sNodePath;
	}

	public ISubtaskSheet getSheet() {
		return getInstance();
	}

	public String getDisplayName() {
		return getName();
	}

	/**
	 * Returns the priority when multiple entries has the same node index. The
	 * values are between 0 to 9. For instance, nodeIndex in extension point is
	 * 10.1, which means priority is 1.
	 * 
	 * @return priority value
	 * @since 3.7
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public int hashCode() {
		// Use node index as unique key in hash map or set
		return getNodeIndex();
	}
}
