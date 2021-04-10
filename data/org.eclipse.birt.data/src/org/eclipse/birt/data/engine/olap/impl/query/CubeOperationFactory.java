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

package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperationFactory;

/**
 * A implementation of <code>ICubeOperationFactory</code>, single instance mode
 */
public class CubeOperationFactory implements ICubeOperationFactory {

	// the only one instance in the app
	private static ICubeOperationFactory instance = null;

	private CubeOperationFactory() {

	}

	public ICubeOperation createAddingNestAggregationsOperation(IBinding[] nestAggregations) throws DataException {
		return new AddingNestAggregations(nestAggregations);
	}

	public static ICubeOperationFactory getInstance() {
		if (instance == null) {
			instance = new CubeOperationFactory();
		}
		return instance;
	}

	/**
	 * Create a IPreparedCubeOperation instance according to a ICubeOperation
	 * instance
	 * 
	 * @param operation: the original cube operation used added
	 * @return
	 * @throws DataException
	 */
	public static IPreparedCubeOperation createPreparedCubeOperation(ICubeOperation operation) throws DataException {
		assert operation != null;
		if (operation instanceof AddingNestAggregations) {
			return new PreparedAddingNestAggregations((AddingNestAggregations) operation);
		}
		// Currently, only AddingNestAggregations is provided, program never
		// goes here
		assert false;
		throw new IllegalArgumentException("Unsupported cube operation:" //$NON-NLS-1$
				+ operation);
	}
}
