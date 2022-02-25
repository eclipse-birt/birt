/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.PLSUtil;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

/**
 * This class is an implementation of IDataSetPopulator. It wrapped a
 * document.ResultIterator.
 *
 * The wrapping is executed by following means: 1.For all the columns in
 * ResultIterator's enclosed DataSetResultSet (which represents the data from a
 * data set), the ResultClass provided by this class will include them. 2.For
 * all the non-aggr bindings that not directly referred a data set column, the
 * ResultClass provide access to them as well. The names of those bindings in
 * ResultClass are specified in constructNonReCalBindingDataSetName() method.
 * 3.For all the aggregation bindings that higher than the highest group level
 * defined in List<IGroupInstanceInfo>, the ResultClass provides access to them
 * as well. The naming convention is same as that of 2.
 */

public class PLSEnabledDataSetPopulator implements IDataSetPopulator {

	//
	private IPLSDataPopulator populator = null;
	private IResultClass resultClass;
	private List<String> originalBindingNames;

	/**
	 * Constructor
	 *
	 * @param query
	 * @param targetGroups
	 * @param docIt
	 * @throws DataException
	 */
	public PLSEnabledDataSetPopulator(IQueryDefinition query, List<IGroupInstanceInfo> targetGroups,
			ResultIterator docIt) throws DataException {
		if (query.isSummaryQuery()) {
			this.populator = new PLSDataPopulator2(targetGroups, docIt);
		} else {
			this.populator = new PLSDataPopulator(targetGroups, docIt);
		}
		try {
			assert docIt.getExprResultSet().getDataSetResultSet() != null;
			this.resultClass = populateResultClass(query, targetGroups,
					docIt.getExprResultSet().getDataSetResultSet().getResultClass());
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	@Override
	public IResultObject next() throws DataException {
		if (!this.populator.next()) {
			return null;
		}
		Object[] field = new Object[this.resultClass.getFieldCount()];
		IResultObject curr = this.populator.getDocumentIterator().getExprResultSet().getDataSetResultSet()
				.getResultObject();

		if (curr == null) {
			return null;
		}

		for (int i = 0; i < curr.getResultClass().getFieldCount(); i++) {
			field[i] = curr.getFieldValue(i + 1);
		}

		for (int i = curr.getResultClass().getFieldCount(); i < field.length; i++) {
			try {
				field[i] = this.populator.getDocumentIterator()
						.getValue(this.originalBindingNames.get(i - curr.getResultClass().getFieldCount()));
			} catch (BirtException e) {
				throw new DataException(ResourceConstants.INVALID_AGGREGATION_BINDING_FOR_PLS,
						this.originalBindingNames.get(i - curr.getResultClass().getFieldCount()));
			}
		}
		return new ResultObject(this.resultClass, field);
	}

	/**
	 * Return the result class.
	 *
	 * @return
	 */
	public IResultClass getResultClass() {
		return this.resultClass;
	}

	/**
	 *
	 * @param query
	 * @param targetGroups
	 * @param original
	 * @return
	 * @throws BirtException
	 */
	private IResultClass populateResultClass(IQueryDefinition query, List<IGroupInstanceInfo> targetGroups,
			IResultClass original) throws BirtException {
		List<ResultFieldMetadata> list = new ArrayList<>();
		for (int i = 1; i <= original.getFieldCount(); i++) {
			list.add(original.getFieldMetaData(i));
		}

		this.originalBindingNames = new ArrayList<>();
		Iterator<IBinding> bindings = query.getBindings().values().iterator();
		while (bindings.hasNext()) {
			IBinding binding = bindings.next();
			if (PLSUtil.isPLSProcessedBinding(binding)) {
				ResultFieldMetadata rfmeta = new ResultFieldMetadata(-1,
						PLSUtil.constructNonReCalBindingDataSetName(binding.getBindingName()), null,
						DataType.getClass(binding.getDataType()), null, false, -1);
				list.add(rfmeta);
				this.originalBindingNames.add(binding.getBindingName());
			}
		}
		return new ResultClass(list);
	}
}
