package org.eclipse.birt.data.engine.impl.index;

import org.eclipse.birt.data.engine.core.DataException;

public interface IIndexSerializer {
	public void close() throws DataException;

	public Object put(Object o1, Object o2) throws DataException;
}
