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

import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Orthogonal Sample Data</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl#getDataSetRepresentation
 * <em>Data Set Representation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl#getSeriesDefinitionIndex
 * <em>Series Definition Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OrthogonalSampleDataImpl extends EObjectImpl implements OrthogonalSampleData {

	/**
	 * The default value of the '{@link #getDataSetRepresentation() <em>Data Set
	 * Representation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getDataSetRepresentation()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_SET_REPRESENTATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataSetRepresentation() <em>Data Set
	 * Representation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getDataSetRepresentation()
	 * @generated
	 * @ordered
	 */
	protected String dataSetRepresentation = DATA_SET_REPRESENTATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getSeriesDefinitionIndex() <em>Series
	 * Definition Index</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getSeriesDefinitionIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SERIES_DEFINITION_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSeriesDefinitionIndex() <em>Series
	 * Definition Index</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getSeriesDefinitionIndex()
	 * @generated
	 * @ordered
	 */
	protected int seriesDefinitionIndex = SERIES_DEFINITION_INDEX_EDEFAULT;

	/**
	 * This is true if the Series Definition Index attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean seriesDefinitionIndexESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OrthogonalSampleDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.ORTHOGONAL_SAMPLE_DATA;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getDataSetRepresentation() {
		return dataSetRepresentation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDataSetRepresentation(String newDataSetRepresentation) {
		String oldDataSetRepresentation = dataSetRepresentation;
		dataSetRepresentation = newDataSetRepresentation;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					DataPackage.ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION, oldDataSetRepresentation,
					dataSetRepresentation));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getSeriesDefinitionIndex() {
		return seriesDefinitionIndex;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setSeriesDefinitionIndex(int newSeriesDefinitionIndex) {
		int oldSeriesDefinitionIndex = seriesDefinitionIndex;
		seriesDefinitionIndex = newSeriesDefinitionIndex;
		boolean oldSeriesDefinitionIndexESet = seriesDefinitionIndexESet;
		seriesDefinitionIndexESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					DataPackage.ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX, oldSeriesDefinitionIndex,
					seriesDefinitionIndex, !oldSeriesDefinitionIndexESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetSeriesDefinitionIndex() {
		int oldSeriesDefinitionIndex = seriesDefinitionIndex;
		boolean oldSeriesDefinitionIndexESet = seriesDefinitionIndexESet;
		seriesDefinitionIndex = SERIES_DEFINITION_INDEX_EDEFAULT;
		seriesDefinitionIndexESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					DataPackage.ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX, oldSeriesDefinitionIndex,
					SERIES_DEFINITION_INDEX_EDEFAULT, oldSeriesDefinitionIndexESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetSeriesDefinitionIndex() {
		return seriesDefinitionIndexESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			return getDataSetRepresentation();
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX:
			return getSeriesDefinitionIndex();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			setDataSetRepresentation((String) newValue);
			return;
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX:
			setSeriesDefinitionIndex((Integer) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			setDataSetRepresentation(DATA_SET_REPRESENTATION_EDEFAULT);
			return;
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX:
			unsetSeriesDefinitionIndex();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			return DATA_SET_REPRESENTATION_EDEFAULT == null ? dataSetRepresentation != null
					: !DATA_SET_REPRESENTATION_EDEFAULT.equals(dataSetRepresentation);
		case DataPackage.ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX:
			return isSetSeriesDefinitionIndex();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (dataSetRepresentation: "); //$NON-NLS-1$
		result.append(dataSetRepresentation);
		result.append(", seriesDefinitionIndex: "); //$NON-NLS-1$
		if (seriesDefinitionIndexESet) {
			result.append(seriesDefinitionIndex);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	@Override
	public OrthogonalSampleData copyInstance() {
		OrthogonalSampleDataImpl dest = new OrthogonalSampleDataImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(OrthogonalSampleData src) {

		// attributes

		dataSetRepresentation = src.getDataSetRepresentation();

		seriesDefinitionIndex = src.getSeriesDefinitionIndex();

		seriesDefinitionIndexESet = src.isSetSeriesDefinitionIndex();

	}

} // OrthogonalSampleDataImpl
