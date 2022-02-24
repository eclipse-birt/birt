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

package org.eclipse.birt.report.model.core.namespace;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * 
 */
public class NameExecutor extends NameExecutorImpl {

	public NameExecutor(Module module, DesignElement element) {
		super(module, (DesignElement) null, element);
	}

	public NameExecutor(Module module, ContainerContext container, DesignElement element) {
		super(module, container, element);
	}

	public NameExecutor(Module module, DesignElement container, DesignElement element) {
		super(module, container, element);
	}

}
