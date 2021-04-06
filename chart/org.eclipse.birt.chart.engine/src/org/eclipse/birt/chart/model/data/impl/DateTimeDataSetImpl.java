/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Date
 * Time Data Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DateTimeDataSetImpl extends DataSetImpl implements DateTimeDataSet {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DateTimeDataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.DATE_TIME_DATA_SET;
	}

	/**
	 * A convenience method to create an initialized 'DateTimeDataSet' instance
	 * 
	 * @param oValues The Collection (of Calendar(s)), Calendar[] or long[] of
	 *                values associated with this dataset
	 * 
	 * @return
	 */
	public static final DateTimeDataSet create(Object oValues) {
		final DateTimeDataSet dtds = DataFactory.eINSTANCE.createDateTimeDataSet();
		((DateTimeDataSetImpl) dtds).initialize();
		dtds.setValues(oValues);
		return dtds;
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
	public DateTimeDataSet copyInstance() {
		DateTimeDataSetImpl dest = new DateTimeDataSetImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(DateTimeDataSet src) {

		super.set(src);

	}

} // DateTimeDataSetImpl
