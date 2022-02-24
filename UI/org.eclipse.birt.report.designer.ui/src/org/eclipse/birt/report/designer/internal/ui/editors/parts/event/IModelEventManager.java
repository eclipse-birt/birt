/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.parts.event;

import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;

/**
 * IModelEventManager
 */
public interface IModelEventManager {

	void addModelEventProcessor(IModelEventProcessor processor);

	void removeModelEventProcessor(IModelEventProcessor processor);

	void hookRoot(Object obj);

	void unhookRoot(Object obj);

	void hookCommandStack(WrapperCommandStack stack);

	void unhookCommandStack(WrapperCommandStack stack);
}
