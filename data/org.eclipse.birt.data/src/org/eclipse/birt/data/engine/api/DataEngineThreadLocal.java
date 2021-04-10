package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.data.engine.core.security.TempPathManager;

public class DataEngineThreadLocal {
	private static DataEngineThreadLocal local = new DataEngineThreadLocal();

	public static DataEngineThreadLocal getInstance() {
		return local;
	}

	private ThreadLocal<CloseListenerManager> closeHolder = new ThreadLocal<CloseListenerManager>() {
		protected CloseListenerManager initialValue() {
			return new CloseListenerManager();
		}
	};

	private ThreadLocal<TempPathManager> pathManager = new ThreadLocal<TempPathManager>() {
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
