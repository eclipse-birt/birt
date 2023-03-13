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

import org.eclipse.birt.data.engine.core.security.TempPathManager;

public class DataEngineThreadLocal {
	private static DataEngineThreadLocal local = new DataEngineThreadLocal();

	public static DataEngineThreadLocal getInstance() {
		return local;
	}

	private ThreadLocal<CloseListenerManager> closeHolder = new ThreadLocal<>() {
		@Override
		protected CloseListenerManager initialValue() {
			return new CloseListenerManager();
		}
	};

	private ThreadLocal<TempPathManager> pathManager = new ThreadLocal<>() {
		@Override
		protected TempPathManager initialValue() {
			return new TempPathManager();
		}
	};

	public TempPathManager getPathManager() {
		return pathManager.get();
	}

	public CloseListenerManager getCloseListener() {
		return closeHolder.get();
	}

	public void removeCloseListener() {
		closeHolder.remove();
	}

	public void removeTempPathManger() {
		pathManager.remove();
	}

}
