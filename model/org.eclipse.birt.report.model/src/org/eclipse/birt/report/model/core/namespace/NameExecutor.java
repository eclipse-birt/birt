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
