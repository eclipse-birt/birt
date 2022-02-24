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

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Label
 * Block</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl#getLabel
 * <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LabelBlockImpl extends BlockImpl implements LabelBlock {

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected LabelBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LayoutPackage.Literals.LABEL_BLOCK;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs) {
		Label oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.LABEL_BLOCK__LABEL, oldLabel, newLabel);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLabel(Label newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null) {
				msgs = ((InternalEObject) label).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LABEL_BLOCK__LABEL, null, msgs);
			}
			if (newLabel != null) {
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LABEL_BLOCK__LABEL, null, msgs);
			}
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LABEL_BLOCK__LABEL, newLabel,
					newLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case LayoutPackage.LABEL_BLOCK__LABEL:
			return basicSetLabel(null, msgs);
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
		case LayoutPackage.LABEL_BLOCK__LABEL:
			return getLabel();
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
		case LayoutPackage.LABEL_BLOCK__LABEL:
			setLabel((Label) newValue);
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
		case LayoutPackage.LABEL_BLOCK__LABEL:
			setLabel((Label) null);
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
		case LayoutPackage.LABEL_BLOCK__LABEL:
			return label != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * A convenience method to create an initialized 'LabelBlock' instance
	 *
	 * @return
	 */
	public static Block create() {
		final LabelBlock lb = LayoutFactory.eINSTANCE.createLabelBlock();
		((LabelBlockImpl) lb).initialize();
		return lb;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initialize() {
		super.initialize();
		setLabel(LabelImpl.create());
	}

	/**
	 * A convenience method to create an initialized 'LabelBlock' instance
	 *
	 * @return
	 */
	public static Block createDefault() {
		final LabelBlock lb = LayoutFactory.eINSTANCE.createLabelBlock();
		((LabelBlockImpl) lb).initDefault();
		return lb;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initDefault() {
		super.initDefault();
		setLabel(LabelImpl.createDefault());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.layout.Block#getPreferredSize(org.eclipse.birt.
	 * chart.device.IDisplayServer, org.eclipse.birt.chart.model.Chart,
	 * org.eclipse.birt.chart.factory.RunTimeContext)
	 */
	@Override
	public final Size getPreferredSize(IDisplayServer xs, Chart cm, RunTimeContext rtc) throws ChartException {
		BoundingBox bb = computeBox(xs, rtc);

		final Size sz = SizeImpl.create(bb.getWidth(), bb.getHeight());
		sz.scale(72d / xs.getDpiResolution());
		final Insets ins = getInsets();
		sz.setHeight(sz.getHeight() + ins.getTop() + ins.getBottom());
		sz.setWidth(sz.getWidth() + ins.getLeft() + ins.getRight());
		return sz;
	}

	protected BoundingBox computeBox(IDisplayServer xs, RunTimeContext rtc) throws ChartException {
		Label la = getLabel().copyInstance();
		final String sPreviousValue = getLabel().getCaption().getValue();
		la.getCaption().setValue(rtc.externalizedMessage(sPreviousValue));
		IChartComputation cComp = rtc.getState(StateKey.CHART_COMPUTATION_KEY);
		return cComp.computeBox(xs, IConstants.TOP, la, 0, 0);
	}

	/**
	 * @generated
	 */
	@Override
	public LabelBlock copyInstance() {
		LabelBlockImpl dest = new LabelBlockImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(LabelBlock src) {

		super.set(src);

		// children

		if (src.getLabel() != null) {
			setLabel(src.getLabel().copyInstance());
		}

	}

} // LabelBlockImpl
