/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors;

import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * 
 */
//Don't try to implements this interface.
public interface IRelatedFileChangeResolve {
	boolean acceptType(int type);

	void notifySaveFile(ModuleHandle owner);

	boolean reload(ModuleHandle owner);

	boolean reset();

	boolean isReload(IReportResourceChangeEvent event, ModuleHandle owner);

	boolean isReset(IReportResourceChangeEvent event, ModuleHandle owner);
}
