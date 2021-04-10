/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuListener;

/**
 * 
 */

public interface ISchematicMenuListener extends IMenuListener {
	void setActionRegistry(ActionRegistry actionRegistry);
}
