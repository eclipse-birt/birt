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

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Grid</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.GridImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.GridImpl#getTickStyle
 * <em>Tick Style</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.GridImpl#getTickAttributes
 * <em>Tick Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.GridImpl#getTickSize
 * <em>Tick Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.GridImpl#getTickCount
 * <em>Tick Count</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GridImpl extends EObjectImpl implements Grid {

	/**
	 * The cached value of the '{@link #getLineAttributes() <em>Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes lineAttributes;

	/**
	 * The default value of the '{@link #getTickStyle() <em>Tick Style</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTickStyle()
	 * @generated
	 * @ordered
	 */
	protected static final TickStyle TICK_STYLE_EDEFAULT = TickStyle.ACROSS_LITERAL;

	/**
	 * The cached value of the '{@link #getTickStyle() <em>Tick Style</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTickStyle()
	 * @generated
	 * @ordered
	 */
	protected TickStyle tickStyle = TICK_STYLE_EDEFAULT;

	/**
	 * This is true if the Tick Style attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean tickStyleESet;

	/**
	 * The cached value of the '{@link #getTickAttributes() <em>Tick
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getTickAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes tickAttributes;

	/**
	 * The default value of the '{@link #getTickSize() <em>Tick Size</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTickSize()
	 * @generated
	 * @ordered
	 */
	protected static final double TICK_SIZE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTickSize() <em>Tick Size</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTickSize()
	 * @generated
	 * @ordered
	 */
	protected double tickSize = TICK_SIZE_EDEFAULT;

	/**
	 * This is true if the Tick Size attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean tickSizeESet;

	/**
	 * The default value of the '{@link #getTickCount() <em>Tick Count</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTickCount()
	 * @generated
	 * @ordered
	 */
	protected static final int TICK_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTickCount() <em>Tick Count</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTickCount()
	 * @generated
	 * @ordered
	 */
	protected int tickCount = TICK_COUNT_EDEFAULT;

	/**
	 * This is true if the Tick Count attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean tickCountESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected GridImpl() {
		super();
	}

	public static Grid create(boolean visible, ColorDefinition cd, LineStyle ls, int iThickness, TickStyle tickStyle) {
		GridImpl gr = (GridImpl) ComponentFactory.eINSTANCE.createGrid();
		gr.initialize(visible, cd, ls, iThickness, tickStyle);
		return gr;
	}

	void initialize(boolean visible, ColorDefinition cd, LineStyle ls, int iThickness, TickStyle tickStyle) {
		LineAttributes lia = LineAttributesImpl.create(cd, ls, iThickness);
		setLineAttributes(lia);
		setTickStyle(tickStyle);
	}

	public static Grid createDefault(boolean visible, ColorDefinition cd, LineStyle ls, int iThickness,
			TickStyle tickStyle) {
		GridImpl gr = (GridImpl) ComponentFactory.eINSTANCE.createGrid();
		gr.initDefault(visible, cd, ls, iThickness, tickStyle);
		return gr;
	}

	void initDefault(boolean visible, ColorDefinition cd, LineStyle ls, int iThickness, TickStyle tickStyle) {
		lineAttributes = LineAttributesImpl.createDefault(cd, ls, iThickness, visible);
		this.tickStyle = tickStyle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.GRID;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getLineAttributes() {
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLineAttributes(LineAttributes newLineAttributes, NotificationChain msgs) {
		LineAttributes oldLineAttributes = lineAttributes;
		lineAttributes = newLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.GRID__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
	public void setLineAttributes(LineAttributes newLineAttributes) {
		if (newLineAttributes != lineAttributes) {
			NotificationChain msgs = null;
			if (lineAttributes != null)
				msgs = ((InternalEObject) lineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.GRID__LINE_ATTRIBUTES, null, msgs);
			if (newLineAttributes != null)
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.GRID__LINE_ATTRIBUTES, null, msgs);
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.GRID__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TickStyle getTickStyle() {
		return tickStyle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTickStyle(TickStyle newTickStyle) {
		TickStyle oldTickStyle = tickStyle;
		tickStyle = newTickStyle == null ? TICK_STYLE_EDEFAULT : newTickStyle;
		boolean oldTickStyleESet = tickStyleESet;
		tickStyleESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.GRID__TICK_STYLE, oldTickStyle,
					tickStyle, !oldTickStyleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTickStyle() {
		TickStyle oldTickStyle = tickStyle;
		boolean oldTickStyleESet = tickStyleESet;
		tickStyle = TICK_STYLE_EDEFAULT;
		tickStyleESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.GRID__TICK_STYLE, oldTickStyle,
					TICK_STYLE_EDEFAULT, oldTickStyleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTickStyle() {
		return tickStyleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getTickAttributes() {
		return tickAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetTickAttributes(LineAttributes newTickAttributes, NotificationChain msgs) {
		LineAttributes oldTickAttributes = tickAttributes;
		tickAttributes = newTickAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.GRID__TICK_ATTRIBUTES, oldTickAttributes, newTickAttributes);
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
	public void setTickAttributes(LineAttributes newTickAttributes) {
		if (newTickAttributes != tickAttributes) {
			NotificationChain msgs = null;
			if (tickAttributes != null)
				msgs = ((InternalEObject) tickAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.GRID__TICK_ATTRIBUTES, null, msgs);
			if (newTickAttributes != null)
				msgs = ((InternalEObject) newTickAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.GRID__TICK_ATTRIBUTES, null, msgs);
			msgs = basicSetTickAttributes(newTickAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.GRID__TICK_ATTRIBUTES,
					newTickAttributes, newTickAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getTickSize() {
		return tickSize;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTickSize(double newTickSize) {
		double oldTickSize = tickSize;
		tickSize = newTickSize;
		boolean oldTickSizeESet = tickSizeESet;
		tickSizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.GRID__TICK_SIZE, oldTickSize,
					tickSize, !oldTickSizeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTickSize() {
		double oldTickSize = tickSize;
		boolean oldTickSizeESet = tickSizeESet;
		tickSize = TICK_SIZE_EDEFAULT;
		tickSizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.GRID__TICK_SIZE, oldTickSize,
					TICK_SIZE_EDEFAULT, oldTickSizeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTickSize() {
		return tickSizeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getTickCount() {
		return tickCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTickCount(int newTickCount) {
		int oldTickCount = tickCount;
		tickCount = newTickCount;
		boolean oldTickCountESet = tickCountESet;
		tickCountESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.GRID__TICK_COUNT, oldTickCount,
					tickCount, !oldTickCountESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTickCount() {
		int oldTickCount = tickCount;
		boolean oldTickCountESet = tickCountESet;
		tickCount = TICK_COUNT_EDEFAULT;
		tickCountESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.GRID__TICK_COUNT, oldTickCount,
					TICK_COUNT_EDEFAULT, oldTickCountESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTickCount() {
		return tickCountESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.GRID__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case ComponentPackage.GRID__TICK_ATTRIBUTES:
			return basicSetTickAttributes(null, msgs);
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
		case ComponentPackage.GRID__LINE_ATTRIBUTES:
			return getLineAttributes();
		case ComponentPackage.GRID__TICK_STYLE:
			return getTickStyle();
		case ComponentPackage.GRID__TICK_ATTRIBUTES:
			return getTickAttributes();
		case ComponentPackage.GRID__TICK_SIZE:
			return getTickSize();
		case ComponentPackage.GRID__TICK_COUNT:
			return getTickCount();
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
		case ComponentPackage.GRID__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.GRID__TICK_STYLE:
			setTickStyle((TickStyle) newValue);
			return;
		case ComponentPackage.GRID__TICK_ATTRIBUTES:
			setTickAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.GRID__TICK_SIZE:
			setTickSize((Double) newValue);
			return;
		case ComponentPackage.GRID__TICK_COUNT:
			setTickCount((Integer) newValue);
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
		case ComponentPackage.GRID__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case ComponentPackage.GRID__TICK_STYLE:
			unsetTickStyle();
			return;
		case ComponentPackage.GRID__TICK_ATTRIBUTES:
			setTickAttributes((LineAttributes) null);
			return;
		case ComponentPackage.GRID__TICK_SIZE:
			unsetTickSize();
			return;
		case ComponentPackage.GRID__TICK_COUNT:
			unsetTickCount();
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
		case ComponentPackage.GRID__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case ComponentPackage.GRID__TICK_STYLE:
			return isSetTickStyle();
		case ComponentPackage.GRID__TICK_ATTRIBUTES:
			return tickAttributes != null;
		case ComponentPackage.GRID__TICK_SIZE:
			return isSetTickSize();
		case ComponentPackage.GRID__TICK_COUNT:
			return isSetTickCount();
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
		result.append(" (tickStyle: "); //$NON-NLS-1$
		if (tickStyleESet)
			result.append(tickStyle);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", tickSize: "); //$NON-NLS-1$
		if (tickSizeESet)
			result.append(tickSize);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", tickCount: "); //$NON-NLS-1$
		if (tickCountESet)
			result.append(tickCount);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	public Grid copyInstance() {
		GridImpl dest = new GridImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Grid src) {

		// children

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getTickAttributes() != null) {
			setTickAttributes(src.getTickAttributes().copyInstance());
		}

		// attributes

		tickStyle = src.getTickStyle();

		tickStyleESet = src.isSetTickStyle();

		tickSize = src.getTickSize();

		tickSizeESet = src.isSetTickSize();

		tickCount = src.getTickCount();

		tickCountESet = src.isSetTickCount();

	}

} // GridImpl
