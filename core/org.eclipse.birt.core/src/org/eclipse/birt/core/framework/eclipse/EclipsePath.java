/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/*
 * Created on 2005-3-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.core.framework.eclipse;

import org.eclipse.birt.core.framework.IPlatformPath;
import org.eclipse.core.runtime.IPath;

/**
 *
 */
public class EclipsePath implements IPlatformPath {
	protected IPath path;

	public EclipsePath(IPath path) {
		this.path = path;
	}

	public String toString() {
		return path.toString();
	}
}
