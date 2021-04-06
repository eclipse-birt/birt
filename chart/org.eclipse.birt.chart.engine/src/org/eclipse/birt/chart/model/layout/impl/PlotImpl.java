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

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Plot</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl#getHorizontalSpacing
 * <em>Horizontal Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl#getVerticalSpacing
 * <em>Vertical Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.PlotImpl#getClientArea
 * <em>Client Area</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlotImpl extends BlockImpl implements Plot {

	/**
	 * The default value of the '{@link #getHorizontalSpacing() <em>Horizontal
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHorizontalSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int HORIZONTAL_SPACING_EDEFAULT = 5;

	/**
	 * The cached value of the '{@link #getHorizontalSpacing() <em>Horizontal
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHorizontalSpacing()
	 * @generated
	 * @ordered
	 */
	protected int horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;

	/**
	 * This is true if the Horizontal Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean horizontalSpacingESet;

	/**
	 * The default value of the ' {@link #getVerticalSpacing() <em>Vertical
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVerticalSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int VERTICAL_SPACING_EDEFAULT = 5;

	/**
	 * The cached value of the ' {@link #getVerticalSpacing() <em>Vertical
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVerticalSpacing()
	 * @generated
	 * @ordered
	 */
	protected int verticalSpacing = VERTICAL_SPACING_EDEFAULT;

	/**
	 * This is true if the Vertical Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean verticalSpacingESet;

	/**
	 * The cached value of the '{@link #getClientArea() <em>Client Area</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getClientArea()
	 * @generated
	 * @ordered
	 */
	protected ClientArea clientArea;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LayoutPackage.Literals.PLOT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setHorizontalSpacing(int newHorizontalSpacing) {
		int oldHorizontalSpacing = horizontalSpacing;
		horizontalSpacing = newHorizontalSpacing;
		boolean oldHorizontalSpacingESet = horizontalSpacingESet;
		horizontalSpacingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.PLOT__HORIZONTAL_SPACING,
					oldHorizontalSpacing, horizontalSpacing, !oldHorizontalSpacingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetHorizontalSpacing() {
		int oldHorizontalSpacing = horizontalSpacing;
		boolean oldHorizontalSpacingESet = horizontalSpacingESet;
		horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;
		horizontalSpacingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.PLOT__HORIZONTAL_SPACING,
					oldHorizontalSpacing, HORIZONTAL_SPACING_EDEFAULT, oldHorizontalSpacingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetHorizontalSpacing() {
		return horizontalSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVerticalSpacing(int newVerticalSpacing) {
		int oldVerticalSpacing = verticalSpacing;
		verticalSpacing = newVerticalSpacing;
		boolean oldVerticalSpacingESet = verticalSpacingESet;
		verticalSpacingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.PLOT__VERTICAL_SPACING,
					oldVerticalSpacing, verticalSpacing, !oldVerticalSpacingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetVerticalSpacing() {
		int oldVerticalSpacing = verticalSpacing;
		boolean oldVerticalSpacingESet = verticalSpacingESet;
		verticalSpacing = VERTICAL_SPACING_EDEFAULT;
		verticalSpacingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.PLOT__VERTICAL_SPACING,
					oldVerticalSpacing, VERTICAL_SPACING_EDEFAULT, oldVerticalSpacingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetVerticalSpacing() {
		return verticalSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ClientArea getClientArea() {
		return clientArea;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetClientArea(ClientArea newClientArea, NotificationChain msgs) {
		ClientArea oldClientArea = clientArea;
		clientArea = newClientArea;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.PLOT__CLIENT_AREA, oldClientArea, newClientArea);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setClientArea(ClientArea newClientArea) {
		if (newClientArea != clientArea) {
			NotificationChain msgs = null;
			if (clientArea != null)
				msgs = ((InternalEObject) clientArea).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.PLOT__CLIENT_AREA, null, msgs);
			if (newClientArea != null)
				msgs = ((InternalEObject) newClientArea).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.PLOT__CLIENT_AREA, null, msgs);
			msgs = basicSetClientArea(newClientArea, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.PLOT__CLIENT_AREA, newClientArea,
					newClientArea));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case LayoutPackage.PLOT__CLIENT_AREA:
			return basicSetClientArea(null, msgs);
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
		case LayoutPackage.PLOT__HORIZONTAL_SPACING:
			return getHorizontalSpacing();
		case LayoutPackage.PLOT__VERTICAL_SPACING:
			return getVerticalSpacing();
		case LayoutPackage.PLOT__CLIENT_AREA:
			return getClientArea();
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
		case LayoutPackage.PLOT__HORIZONTAL_SPACING:
			setHorizontalSpacing((Integer) newValue);
			return;
		case LayoutPackage.PLOT__VERTICAL_SPACING:
			setVerticalSpacing((Integer) newValue);
			return;
		case LayoutPackage.PLOT__CLIENT_AREA:
			setClientArea((ClientArea) newValue);
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
		case LayoutPackage.PLOT__HORIZONTAL_SPACING:
			unsetHorizontalSpacing();
			return;
		case LayoutPackage.PLOT__VERTICAL_SPACING:
			unsetVerticalSpacing();
			return;
		case LayoutPackage.PLOT__CLIENT_AREA:
			setClientArea((ClientArea) null);
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
		case LayoutPackage.PLOT__HORIZONTAL_SPACING:
			return isSetHorizontalSpacing();
		case LayoutPackage.PLOT__VERTICAL_SPACING:
			return isSetVerticalSpacing();
		case LayoutPackage.PLOT__CLIENT_AREA:
			return clientArea != null;
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (horizontalSpacing: "); //$NON-NLS-1$
		if (horizontalSpacingESet)
			result.append(horizontalSpacing);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", verticalSpacing: "); //$NON-NLS-1$
		if (verticalSpacingESet)
			result.append(verticalSpacing);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isPlot() {
		return true;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isCustom() {
		return false;
	}

	/**
	 * A convenience method to create an initialized 'Plot' instance
	 * 
	 * @return
	 */
	public static final Block create() {
		final Plot pl = LayoutFactory.eINSTANCE.createPlot();
		((PlotImpl) pl).initialize();
		return pl;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize() {
		super.initialize();

		final ClientArea ca = LayoutFactory.eINSTANCE.createClientArea();
		((ClientAreaImpl) ca).initialize();
		setClientArea(ca);

		setHorizontalSpacing(5);
		setVerticalSpacing(5);
	}

	/**
	 * A convenience method to create an initialized 'Plot' instance
	 * 
	 * @return
	 */
	public static final Block createDefault() {
		final Plot pl = LayoutFactory.eINSTANCE.createPlot();
		((PlotImpl) pl).initDefault();
		return pl;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initDefault() {
		super.initDefault();

		final ClientArea ca = LayoutFactory.eINSTANCE.createClientArea();
		((ClientAreaImpl) ca).initDefault();
		setClientArea(ca);

		horizontalSpacing = 5;
		verticalSpacing = 5;
	}

	/**
	 * @generated
	 */
	public Plot copyInstance() {
		PlotImpl dest = new PlotImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Plot src) {

		super.set(src);

		// children

		if (src.getClientArea() != null) {
			setClientArea(src.getClientArea().copyInstance());
		}

		// attributes

		horizontalSpacing = src.getHorizontalSpacing();

		horizontalSpacingESet = src.isSetHorizontalSpacing();

		verticalSpacing = src.getVerticalSpacing();

		verticalSpacingESet = src.isSetVerticalSpacing();

	}

} // PlotImpl
