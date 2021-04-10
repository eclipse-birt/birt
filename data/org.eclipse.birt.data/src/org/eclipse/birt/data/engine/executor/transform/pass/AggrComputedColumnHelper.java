
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IAggrValueHolder;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * 
 */

public class AggrComputedColumnHelper implements IResultObjectEvent {
	private IAggrValueHolder holder;
	private List aggrCCNames;

	/**
	 * 
	 * @param holder
	 * @param aggrCCNames
	 */
	public AggrComputedColumnHelper(IAggrValueHolder holder, List aggrCCNames) {
		this.holder = holder;
		this.aggrCCNames = aggrCCNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.
	 * data.engine.odi.IResultObject, int)
	 */
	public boolean process(IResultObject resultObject, int rowIndex) throws DataException {
		for (int i = 0; i < aggrCCNames.size(); i++) {
			String name = this.aggrCCNames.get(i).toString();
			resultObject.setCustomFieldValue(name, holder.getAggrValue(name));
		}

		return true;
	}

}
