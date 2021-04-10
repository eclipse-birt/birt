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

package org.eclipse.birt.chart.model.component.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.component.ChartPreferences;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Chart
 * Preferences</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl#getLabels
 * <em>Labels</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl#getBlocks
 * <em>Blocks</em>}</li>
 * </ul>
 * </p>
 *
 * @deprecated only reserved for compatibility
 */
public class ChartPreferencesImpl extends EObjectImpl implements ChartPreferences {

	/**
	 * The cached value of the '{@link #getLabels() <em>Labels</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabels()
	 * @generated
	 * @ordered
	 */
	protected EList<Label> labels;

	/**
	 * The cached value of the '{@link #getBlocks() <em>Blocks</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBlocks()
	 * @generated
	 * @ordered
	 */
	protected EList<Block> blocks;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ChartPreferencesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.CHART_PREFERENCES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Label> getLabels() {
		if (labels == null) {
			labels = new EObjectContainmentEList<Label>(Label.class, this, ComponentPackage.CHART_PREFERENCES__LABELS);
		}
		return labels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Block> getBlocks() {
		if (blocks == null) {
			blocks = new EObjectContainmentEList<Block>(Block.class, this, ComponentPackage.CHART_PREFERENCES__BLOCKS);
		}
		return blocks;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.CHART_PREFERENCES__LABELS:
			return ((InternalEList<?>) getLabels()).basicRemove(otherEnd, msgs);
		case ComponentPackage.CHART_PREFERENCES__BLOCKS:
			return ((InternalEList<?>) getBlocks()).basicRemove(otherEnd, msgs);
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
		case ComponentPackage.CHART_PREFERENCES__LABELS:
			return getLabels();
		case ComponentPackage.CHART_PREFERENCES__BLOCKS:
			return getBlocks();
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
		case ComponentPackage.CHART_PREFERENCES__LABELS:
			getLabels().clear();
			getLabels().addAll((Collection<? extends Label>) newValue);
			return;
		case ComponentPackage.CHART_PREFERENCES__BLOCKS:
			getBlocks().clear();
			getBlocks().addAll((Collection<? extends Block>) newValue);
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
		case ComponentPackage.CHART_PREFERENCES__LABELS:
			getLabels().clear();
			return;
		case ComponentPackage.CHART_PREFERENCES__BLOCKS:
			getBlocks().clear();
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
		case ComponentPackage.CHART_PREFERENCES__LABELS:
			return labels != null && !labels.isEmpty();
		case ComponentPackage.CHART_PREFERENCES__BLOCKS:
			return blocks != null && !blocks.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated
	 */
	public ChartPreferences copyInstance() {
		ChartPreferencesImpl dest = new ChartPreferencesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(ChartPreferences src) {

		// children

		if (src.getLabels() != null) {
			EList<Label> list = getLabels();
			for (Label element : src.getLabels()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getBlocks() != null) {
			EList<Block> list = getBlocks();
			for (Block element : src.getBlocks()) {
				list.add(element.copyInstance());
			}
		}

	}

} // ChartPreferencesImpl
