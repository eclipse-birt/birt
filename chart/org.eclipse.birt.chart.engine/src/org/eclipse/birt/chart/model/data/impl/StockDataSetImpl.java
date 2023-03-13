/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Stock
 * Data Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class StockDataSetImpl extends DataSetImpl implements StockDataSet {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StockDataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.STOCK_DATA_SET;
	}

	/**
	 * A convenience method to create an initialized 'StockDataSet' instance
	 *
	 * @param oValues The Collection (of StockEntry) or StockEntry[] of values
	 *                associated with this dataset
	 *
	 * @return
	 */
	public static final StockDataSet create(Object oValues) {
		final StockDataSet sds = DataFactory.eINSTANCE.createStockDataSet();
		((StockDataSetImpl) sds).initialize();
		sds.setValues(oValues);
		return sds;
	}

	/**
	 * This method performs any initialization of the instance when created
	 *
	 * Note: Manually written
	 */
	protected void initialize() {
	}

	/**
	 * @generated
	 */
	@Override
	public StockDataSet copyInstance() {
		StockDataSetImpl dest = new StockDataSetImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(StockDataSet src) {

		super.set(src);

	}

} // StockDataSetImpl
