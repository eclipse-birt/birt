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

package org.eclipse.birt.chart.model.type.impl;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialImpl;
import org.eclipse.birt.chart.model.component.impl.NeedleImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dial
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl#getDial
 * <em>Dial</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl#getNeedle
 * <em>Needle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DialSeriesImpl extends SeriesImpl implements DialSeries {

	/**
	 * The cached value of the '{@link #getDial() <em>Dial</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getDial()
	 * @generated
	 * @ordered
	 */
	protected Dial dial;

	/**
	 * The cached value of the '{@link #getNeedle() <em>Needle</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getNeedle()
	 * @generated
	 * @ordered
	 */
	protected Needle needle;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DialSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.DIAL_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Dial getDial() {
		return dial;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetDial(Dial newDial, NotificationChain msgs) {
		Dial oldDial = dial;
		dial = newDial;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.DIAL_SERIES__DIAL, oldDial, newDial);
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
	public void setDial(Dial newDial) {
		if (newDial != dial) {
			NotificationChain msgs = null;
			if (dial != null) {
				msgs = ((InternalEObject) dial).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__DIAL, null, msgs);
			}
			if (newDial != null) {
				msgs = ((InternalEObject) newDial).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__DIAL, null, msgs);
			}
			msgs = basicSetDial(newDial, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.DIAL_SERIES__DIAL, newDial, newDial));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Needle getNeedle() {
		return needle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetNeedle(Needle newNeedle, NotificationChain msgs) {
		Needle oldNeedle = needle;
		needle = newNeedle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.DIAL_SERIES__NEEDLE, oldNeedle, newNeedle);
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
	public void setNeedle(Needle newNeedle) {
		if (newNeedle != needle) {
			NotificationChain msgs = null;
			if (needle != null) {
				msgs = ((InternalEObject) needle).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__NEEDLE, null, msgs);
			}
			if (newNeedle != null) {
				msgs = ((InternalEObject) newNeedle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__NEEDLE, null, msgs);
			}
			msgs = basicSetNeedle(newNeedle, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.DIAL_SERIES__NEEDLE, newNeedle,
					newNeedle));
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
		case TypePackage.DIAL_SERIES__DIAL:
			return basicSetDial(null, msgs);
		case TypePackage.DIAL_SERIES__NEEDLE:
			return basicSetNeedle(null, msgs);
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
		case TypePackage.DIAL_SERIES__DIAL:
			return getDial();
		case TypePackage.DIAL_SERIES__NEEDLE:
			return getNeedle();
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
		case TypePackage.DIAL_SERIES__DIAL:
			setDial((Dial) newValue);
			return;
		case TypePackage.DIAL_SERIES__NEEDLE:
			setNeedle((Needle) newValue);
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
		case TypePackage.DIAL_SERIES__DIAL:
			setDial((Dial) null);
			return;
		case TypePackage.DIAL_SERIES__NEEDLE:
			setNeedle((Needle) null);
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
		case TypePackage.DIAL_SERIES__DIAL:
			return dial != null;
		case TypePackage.DIAL_SERIES__NEEDLE:
			return needle != null;
		}
		return super.eIsSet(featureID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#create()
	 */
	public static final Series create() {
		final DialSeries ds = TypeFactory.eINSTANCE.createDialSeries();
		((DialSeriesImpl) ds).initialize();
		return ds;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	@Override
	protected final void initialize() {
		super.initialize();

		getLabel().setVisible(true);

		setDial(DialImpl.create());
		setNeedle(NeedleImpl.create());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#create()
	 */
	public static final Series createDefault() {
		final DialSeries ds = TypeFactory.eINSTANCE.createDialSeries();
		((DialSeriesImpl) ds).initDefault();
		return ds;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	@Override
	protected final void initDefault() {
		super.initDefault();

		try {
			ChartElementUtil.setDefaultValue(getLabel(), "visible", true); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}

		dial = DialImpl.createDefault();
		needle = NeedleImpl.createDefault();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("DialSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	@Override
	public DialSeries copyInstance() {
		DialSeriesImpl dest = new DialSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(DialSeries src) {

		super.set(src);

		// children

		if (src.getDial() != null) {
			setDial(src.getDial().copyInstance());
		}

		if (src.getNeedle() != null) {
			setNeedle(src.getNeedle().copyInstance());
		}

	}

} // DialSeriesImpl
