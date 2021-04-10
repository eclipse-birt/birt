
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 * 
 */

public interface IAggregationCalculator {
	/**
	 * 
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IAggregationResultSet execute(StopSign stopSign) throws IOException, DataException;
}
