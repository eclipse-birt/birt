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
package org.eclipse.birt.data.engine.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloseListenerManager {
	List<ICloseListener> list = null;

	private int activateDteCount = 0;

	public CloseListenerManager() {
		list = new ArrayList<ICloseListener>();
	}

	public void dataEngineStart() {
		activateDteCount++;
	}

	public void dataEngineShutDown() {
		activateDteCount--;
	}

	public void add(ICloseListener stream) {
		list.add(stream);
	}

	public void closeAll() throws IOException {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).close();
		}
		list.clear();
	}

	public int getActivateDteCount() {
		return activateDteCount;
	}
}
