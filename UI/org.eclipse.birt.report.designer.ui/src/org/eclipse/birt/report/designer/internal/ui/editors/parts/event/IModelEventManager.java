/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
