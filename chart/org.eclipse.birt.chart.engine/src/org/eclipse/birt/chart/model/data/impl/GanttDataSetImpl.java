/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.GanttDataSet;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Gantt
 * Data Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class GanttDataSetImpl extends DataSetImpl implements GanttDataSet {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected GanttDataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.GANTT_DATA_SET;
	}

	/**
	 * A convenience method to create an initialized 'GanttDataSet' instance
	 *
	 * @param oValues The Collection (of GanttEntry) or GanttEntry[] of values
	 *                associated with this dataset
	 *
	 * @return
	 */
	public static final GanttDataSet create(Object oValues) {
		final GanttDataSet gds = DataFactory.eINSTANCE.createGanttDataSet();
		((GanttDataSetImpl) gds).initialize();
		gds.setValues(oValues);
		return gds;
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
	public GanttDataSet copyInstance() {
		GanttDataSetImpl dest = new GanttDataSetImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(GanttDataSet src) {

		super.set(src);

	}

} // GanttDataSetImpl
