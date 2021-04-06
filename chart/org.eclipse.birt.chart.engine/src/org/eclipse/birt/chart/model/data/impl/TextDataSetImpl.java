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
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Text
 * Data Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class TextDataSetImpl extends DataSetImpl implements TextDataSet {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TextDataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.TEXT_DATA_SET;
	}

	/**
	 * A convenience method to create an initialized 'TextDataSet' instance
	 * 
	 * @param oValues The Collection (of String(s)) or String[] of values associated
	 *                with this dataset
	 * 
	 * @return
	 */
	public static final TextDataSet create(Object oValues) {
		final TextDataSet tds = DataFactory.eINSTANCE.createTextDataSet();
		((TextDataSetImpl) tds).initialize();
		tds.setValues(oValues);
		return tds;
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
	public TextDataSet copyInstance() {
		TextDataSetImpl dest = new TextDataSetImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(TextDataSet src) {

		super.set(src);

	}

} // TextDataSetImpl
