/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.NullDataSet;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Null
 * Data Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class NullDataSetImpl extends DataSetImpl implements NullDataSet {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected NullDataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.NULL_DATA_SET;
	}

	/**
	 * This method performs any initialization of the instance when created
	 * 
	 * Note: Manually written
	 */
	protected void initialize() {
	}

	/**
	 * A convenience method to create an initialized 'NullDataSet' instance
	 * 
	 * @param iRowCount represents the count of null in the dataset
	 * @return
	 */
	public static final NullDataSet create(int iRowCount) {
		final NullDataSet nds = DataFactory.eINSTANCE.createNullDataSet();
		((NullDataSetImpl) nds).initialize();
		nds.setValues(new Object[iRowCount]);
		return nds;
	}

	/**
	 * @generated
	 */
	public NullDataSet copyInstance() {
		NullDataSetImpl dest = new NullDataSetImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(NullDataSet src) {

		super.set(src);

	}

} // NullDataSetImpl
