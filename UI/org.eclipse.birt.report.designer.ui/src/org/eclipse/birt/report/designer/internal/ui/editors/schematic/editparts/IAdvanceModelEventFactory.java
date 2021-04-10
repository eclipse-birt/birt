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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractModelEventProcessor.IModelEventFactory;

/**
 * Optimize the event dispatch.Mark the event start and end.
 */
//Now use the cross tab.
public interface IAdvanceModelEventFactory extends IModelEventFactory {
	/**
	 * Event start
	 */
	void eventDispathStart();

	/**
	 * Event end
	 */
	void eventDispathEnd();
}
