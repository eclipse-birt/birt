/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.commands.Command;

/**
 * Add the item to the crosscell handle
 */
public class CrosstabCellCreateCommand extends Command {

	private Object parent;

	private Object after;

	private Map extendsData;

	/**
	 * Constructor
	 * 
	 * @param extendsData
	 */
	public CrosstabCellCreateCommand(Map extendsData) {
		this.extendsData = extendsData;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is not
	 * executable.
	 */
	public void execute() {
		try {
			if (parent instanceof CrosstabCellAdapter) {
				CrosstabCellAdapter cellAdapter = (CrosstabCellAdapter) parent;
				CrosstabCellHandle handle = cellAdapter.getCrosstabCellHandle();
				int pos = findInsertPosition(handle, after);
				Object obj = getNewObject();
				if (obj instanceof DesignElementHandle && ((DesignElementHandle) obj).getContainer() == null) {
					handle.addContent((DesignElementHandle) obj, pos);
					DEUtil.setDefaultTheme((DesignElementHandle) obj);
				} else if (obj instanceof Object[]) {
					Object[] objs = (Object[]) obj;
					for (int i = 0; i < objs.length; i++) {
						if (objs[i] instanceof DesignElementHandle) {
							handle.addContent((DesignElementHandle) objs[i], pos);
							DEUtil.setDefaultTheme((DesignElementHandle) objs[i]);
							pos = pos + 1;
						}
					}
				}
			}
		} catch (SemanticException e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("CreateCommand >> Failed"); //$NON-NLS-1$
			}
			ExceptionUtil.handle(e);
		}
	}

	private int findInsertPosition(CrosstabCellHandle handle, Object after) {
		if (after == null) {
			return handle.getContents().size();
		}
		return handle.getContents().indexOf(after);
	}

	/**
	 * Gets the parent edit part the new report element be added on.
	 * 
	 * @return Returns the parent.
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * Sets the parent edit part the new report element be added on.
	 * 
	 * @param parent The parent to set.
	 */
	public void setParent(Object parent) {
		this.parent = parent;
	}

	/**
	 * Gets the edit part the new report element be added after.
	 * 
	 * @param model The model after the new element
	 */
	public void setAfter(Object model) {
		this.after = model;

	}

	/**
	 * Get the new object
	 * 
	 * @return Return the object
	 */
	public Object getNewObject() {
		return extendsData.get(DesignerConstants.KEY_NEWOBJECT);
	}
}
