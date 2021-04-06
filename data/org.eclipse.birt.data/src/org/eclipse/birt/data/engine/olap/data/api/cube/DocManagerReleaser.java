
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.api.cube;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IShutdownListener;

/**
 * 
 */

public class DocManagerReleaser implements IShutdownListener {
	private DataEngine dataEngine = null;

	public DocManagerReleaser(DataEngine dataEngine) {
		this.dataEngine = dataEngine;
	}

	/**
	 * 
	 */
	public void dataEngineShutdown() {
		DocManagerMap.getDocManagerMap().close(String.valueOf(dataEngine.hashCode()));
	}

}
