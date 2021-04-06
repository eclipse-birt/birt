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
