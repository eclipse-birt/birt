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

import java.util.Collection;

import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Sample
 * Data</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl#getBaseSampleData
 * <em>Base Sample Data</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl#getOrthogonalSampleData
 * <em>Orthogonal Sample Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SampleDataImpl extends EObjectImpl implements SampleData {

	/**
	 * The cached value of the '{@link #getBaseSampleData() <em>Base Sample
	 * Data</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getBaseSampleData()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseSampleData> baseSampleData;

	/**
	 * The cached value of the '{@link #getOrthogonalSampleData() <em>Orthogonal
	 * Sample Data</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getOrthogonalSampleData()
	 * @generated
	 * @ordered
	 */
	protected EList<OrthogonalSampleData> orthogonalSampleData;

	/**
	 * The cached value of the '{@link #getAncillarySampleData() <em>Ancillary
	 * Sample Data</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getAncillarySampleData()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseSampleData> ancillarySampleData;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SampleDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.SAMPLE_DATA;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<BaseSampleData> getBaseSampleData() {
		if (baseSampleData == null) {
			baseSampleData = new EObjectContainmentEList<>(BaseSampleData.class, this,
					DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA);
		}
		return baseSampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<OrthogonalSampleData> getOrthogonalSampleData() {
		if (orthogonalSampleData == null) {
			orthogonalSampleData = new EObjectContainmentEList<>(OrthogonalSampleData.class, this,
					DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA);
		}
		return orthogonalSampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<BaseSampleData> getAncillarySampleData() {
		if (ancillarySampleData == null) {
			ancillarySampleData = new EObjectContainmentEList<>(BaseSampleData.class, this,
					DataPackage.SAMPLE_DATA__ANCILLARY_SAMPLE_DATA);
		}
		return ancillarySampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
			return ((InternalEList<?>) getBaseSampleData()).basicRemove(otherEnd, msgs);
		case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
			return ((InternalEList<?>) getOrthogonalSampleData()).basicRemove(otherEnd, msgs);
		case DataPackage.SAMPLE_DATA__ANCILLARY_SAMPLE_DATA:
			return ((InternalEList<?>) getAncillarySampleData()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
			return getBaseSampleData();
		case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
			return getOrthogonalSampleData();
		case DataPackage.SAMPLE_DATA__ANCILLARY_SAMPLE_DATA:
			return getAncillarySampleData();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
			getBaseSampleData().clear();
			getBaseSampleData().addAll((Collection<? extends BaseSampleData>) newValue);
			return;
		case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
			getOrthogonalSampleData().clear();
			getOrthogonalSampleData().addAll((Collection<? extends OrthogonalSampleData>) newValue);
			return;
		case DataPackage.SAMPLE_DATA__ANCILLARY_SAMPLE_DATA:
			getAncillarySampleData().clear();
			getAncillarySampleData().addAll((Collection<? extends BaseSampleData>) newValue);
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
		case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
			getBaseSampleData().clear();
			return;
		case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
			getOrthogonalSampleData().clear();
			return;
		case DataPackage.SAMPLE_DATA__ANCILLARY_SAMPLE_DATA:
			getAncillarySampleData().clear();
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
		case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
			return baseSampleData != null && !baseSampleData.isEmpty();
		case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
			return orthogonalSampleData != null && !orthogonalSampleData.isEmpty();
		case DataPackage.SAMPLE_DATA__ANCILLARY_SAMPLE_DATA:
			return ancillarySampleData != null && !ancillarySampleData.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated
	 */
	@Override
	public SampleData copyInstance() {
		SampleDataImpl dest = new SampleDataImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(SampleData src) {

		// children

		if (src.getBaseSampleData() != null) {
			EList<BaseSampleData> list = getBaseSampleData();
			for (BaseSampleData element : src.getBaseSampleData()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getOrthogonalSampleData() != null) {
			EList<OrthogonalSampleData> list = getOrthogonalSampleData();
			for (OrthogonalSampleData element : src.getOrthogonalSampleData()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getAncillarySampleData() != null) {
			EList<BaseSampleData> list = getAncillarySampleData();
			for (BaseSampleData element : src.getAncillarySampleData()) {
				list.add(element.copyInstance());
			}
		}

	}

} // SampleDataImpl
