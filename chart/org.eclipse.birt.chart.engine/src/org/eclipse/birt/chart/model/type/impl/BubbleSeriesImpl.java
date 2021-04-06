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

package org.eclipse.birt.chart.model.type.impl;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Bubble
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl#getAccLineAttributes
 * <em>Acc Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl#getAccOrientation
 * <em>Acc Orientation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BubbleSeriesImpl extends ScatterSeriesImpl implements BubbleSeries {

	/**
	 * The cached value of the '{@link #getAccLineAttributes() <em>Acc Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAccLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes accLineAttributes;

	/**
	 * The default value of the '{@link #getAccOrientation() <em>Acc
	 * Orientation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAccOrientation()
	 * @generated
	 * @ordered
	 */
	protected static final Orientation ACC_ORIENTATION_EDEFAULT = Orientation.HORIZONTAL_LITERAL;

	/**
	 * The cached value of the '{@link #getAccOrientation() <em>Acc
	 * Orientation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAccOrientation()
	 * @generated
	 * @ordered
	 */
	protected Orientation accOrientation = ACC_ORIENTATION_EDEFAULT;

	/**
	 * This is true if the Acc Orientation attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean accOrientationESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BubbleSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.BUBBLE_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getAccLineAttributes() {
		return accLineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetAccLineAttributes(LineAttributes newAccLineAttributes, NotificationChain msgs) {
		LineAttributes oldAccLineAttributes = accLineAttributes;
		accLineAttributes = newAccLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES, oldAccLineAttributes, newAccLineAttributes);
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
	public void setAccLineAttributes(LineAttributes newAccLineAttributes) {
		if (newAccLineAttributes != accLineAttributes) {
			NotificationChain msgs = null;
			if (accLineAttributes != null)
				msgs = ((InternalEObject) accLineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES, null, msgs);
			if (newAccLineAttributes != null)
				msgs = ((InternalEObject) newAccLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES, null, msgs);
			msgs = basicSetAccLineAttributes(newAccLineAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES,
					newAccLineAttributes, newAccLineAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Orientation getAccOrientation() {
		return accOrientation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAccOrientation(Orientation newAccOrientation) {
		Orientation oldAccOrientation = accOrientation;
		accOrientation = newAccOrientation == null ? ACC_ORIENTATION_EDEFAULT : newAccOrientation;
		boolean oldAccOrientationESet = accOrientationESet;
		accOrientationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.BUBBLE_SERIES__ACC_ORIENTATION,
					oldAccOrientation, accOrientation, !oldAccOrientationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetAccOrientation() {
		Orientation oldAccOrientation = accOrientation;
		boolean oldAccOrientationESet = accOrientationESet;
		accOrientation = ACC_ORIENTATION_EDEFAULT;
		accOrientationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.BUBBLE_SERIES__ACC_ORIENTATION,
					oldAccOrientation, ACC_ORIENTATION_EDEFAULT, oldAccOrientationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetAccOrientation() {
		return accOrientationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES:
			return basicSetAccLineAttributes(null, msgs);
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
		case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES:
			return getAccLineAttributes();
		case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION:
			return getAccOrientation();
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
		case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES:
			setAccLineAttributes((LineAttributes) newValue);
			return;
		case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION:
			setAccOrientation((Orientation) newValue);
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
		case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES:
			setAccLineAttributes((LineAttributes) null);
			return;
		case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION:
			unsetAccOrientation();
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
		case TypePackage.BUBBLE_SERIES__ACC_LINE_ATTRIBUTES:
			return accLineAttributes != null;
		case TypePackage.BUBBLE_SERIES__ACC_ORIENTATION:
			return isSetAccOrientation();
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
		result.append(" (accOrientation: "); //$NON-NLS-1$
		if (accOrientationESet)
			result.append(accOrientation);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return series instance
	 */
	public static final Series create() {
		final BubbleSeries bs = TypeFactory.eINSTANCE.createBubbleSeries();
		((BubbleSeriesImpl) bs).initialize();
		return bs;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize() {
		super.initialize();

		LineAttributes la = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		la.setVisible(false);

		setAccLineAttributes(la);

		Marker firstMarker = getMarkers().get(0);
		firstMarker.setType(MarkerType.CIRCLE_LITERAL);
		firstMarker.setVisible(true);
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return series instance
	 */
	public static final Series createDefault() {
		final BubbleSeries bs = TypeFactory.eINSTANCE.createBubbleSeries();
		((BubbleSeriesImpl) bs).initDefault();
		return bs;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initDefault() {
		super.initDefault();

		LineAttributes la = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);

		setAccLineAttributes(la);

		Marker firstMarker = getMarkers().get(0);
		try {
			ChartElementUtil.setDefaultValue(firstMarker, "type", MarkerType.CIRCLE_LITERAL); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(firstMarker, "visible", true); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("BubbleSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#isSingleCache()
	 */
	public boolean isSingleCache() {
		return true;
	}

	/**
	 * @generated
	 */
	public BubbleSeries copyInstance() {
		BubbleSeriesImpl dest = new BubbleSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(BubbleSeries src) {

		super.set(src);

		// children

		if (src.getAccLineAttributes() != null) {
			setAccLineAttributes(src.getAccLineAttributes().copyInstance());
		}

		// attributes

		accOrientation = src.getAccOrientation();

		accOrientationESet = src.isSetAccOrientation();

	}

	@Override
	public NameSet getLabelPositionScope(ChartDimension dimension) {
		return LiteralHelper.notOutPositionSet;
	}

	@Override
	public int[] getDefinedDataDefinitionIndex() {
		return new int[] { 0, 1 };
	}

} // BubbleSeriesImpl
