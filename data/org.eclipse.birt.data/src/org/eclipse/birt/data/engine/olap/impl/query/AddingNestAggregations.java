/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * Adding nest aggregations operation
 */
public class AddingNestAggregations implements ICubeOperation {

	// used to define new nest aggregations
	private IBinding[] nestAggregations;

	/**
	 * @param nestAggregations: new nest aggregations
	 * @throws DataException
	 */
	public AddingNestAggregations(IBinding[] nestAggregations) throws DataException {
		if (nestAggregations == null || nestAggregations.length == 0) {
			throw new IllegalArgumentException("nestAggregations is null or empty"); //$NON-NLS-1$
		}

		for (IBinding addedBinding : nestAggregations) {
			if (addedBinding == null) {
				throw new IllegalArgumentException("nestAggregations contains null member"); //$NON-NLS-1$
			}
			String bindingName = addedBinding.getBindingName();
			if (bindingName == null || bindingName.equals("")) //$NON-NLS-1$
			{

				throw new DataException(ResourceConstants.UNSPECIFIED_BINDING_NAME);
			}
			// Here, only check whether it's an aggregation binding
			// Whether it's a nest aggregation binding is checked during the
			// cube query execution
			if (!isExpressionValid(addedBinding) || !OlapExpressionUtil.isAggregationBinding(addedBinding)) {
				throw new DataException(ResourceConstants.NOT_NEST_AGGREGATION_BINDING, addedBinding.getBindingName());
			}
		}
		this.nestAggregations = nestAggregations;
	}

	@Override
	public IBinding[] getNewBindings() {
		return nestAggregations;
	}

	/**
	 * Check whether the expression text of binding matches "data["xxxx"]"
	 *
	 * @param binding
	 * @return
	 * @throws DataException
	 */
	private boolean isExpressionValid(IBinding binding) throws DataException {
		if (!(binding.getExpression() instanceof IScriptExpression)) {
			return false;
		}
		String expression = ((IScriptExpression) binding.getExpression()).getText();
		if (expression == null || expression.trim().equals("")) //$NON-NLS-1$
		{
			return false;
		}
		return true;
	}
}
