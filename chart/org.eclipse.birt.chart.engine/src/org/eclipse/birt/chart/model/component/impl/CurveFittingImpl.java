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

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Curve
 * Fitting</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.CurveFittingImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.CurveFittingImpl#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.CurveFittingImpl#getLabelAnchor
 * <em>Label Anchor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CurveFittingImpl extends EObjectImpl implements CurveFitting {

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
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * The default value of the '{@link #getLabelAnchor() <em>Label Anchor</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabelAnchor()
	 * @generated
	 * @ordered
	 */
	protected static final Anchor LABEL_ANCHOR_EDEFAULT = Anchor.NORTH_LITERAL;

	/**
	 * The cached value of the '{@link #getLabelAnchor() <em>Label Anchor</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabelAnchor()
	 * @generated
	 * @ordered
	 */
	protected Anchor labelAnchor = LABEL_ANCHOR_EDEFAULT;

	/**
	 * This is true if the Label Anchor attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean labelAnchorESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected CurveFittingImpl() {
		super();
	}

	/**
	 * Returns a curveFitting instance.
	 *
	 * @return instance of <code>CurveFitting</code>.
	 */
	public static CurveFitting create() {
		final CurveFitting cf = ComponentFactory.eINSTANCE.createCurveFitting();
		((CurveFittingImpl) cf).initialize();
		return cf;
	}

	public static CurveFitting createDefault() {
		final CurveFitting cf = ComponentFactory.eINSTANCE.createCurveFitting();
		((CurveFittingImpl) cf).initDefault();
		return cf;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.CURVE_FITTING;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
					ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
	public void setLineAttributes(LineAttributes newLineAttributes) {
		if (newLineAttributes != lineAttributes) {
			NotificationChain msgs = null;
			if (lineAttributes != null) {
				msgs = ((InternalEObject) lineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES, null, msgs);
			}
			if (newLineAttributes != null) {
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES, null, msgs);
			}
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
		}
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
					ComponentPackage.CURVE_FITTING__LABEL, oldLabel, newLabel);
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
						EOPPOSITE_FEATURE_BASE - ComponentPackage.CURVE_FITTING__LABEL, null, msgs);
			}
			if (newLabel != null) {
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.CURVE_FITTING__LABEL, null, msgs);
			}
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.CURVE_FITTING__LABEL, newLabel,
					newLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Anchor getLabelAnchor() {
		return labelAnchor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLabelAnchor(Anchor newLabelAnchor) {
		Anchor oldLabelAnchor = labelAnchor;
		labelAnchor = newLabelAnchor == null ? LABEL_ANCHOR_EDEFAULT : newLabelAnchor;
		boolean oldLabelAnchorESet = labelAnchorESet;
		labelAnchorESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.CURVE_FITTING__LABEL_ANCHOR,
					oldLabelAnchor, labelAnchor, !oldLabelAnchorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetLabelAnchor() {
		Anchor oldLabelAnchor = labelAnchor;
		boolean oldLabelAnchorESet = labelAnchorESet;
		labelAnchor = LABEL_ANCHOR_EDEFAULT;
		labelAnchorESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.CURVE_FITTING__LABEL_ANCHOR,
					oldLabelAnchor, LABEL_ANCHOR_EDEFAULT, oldLabelAnchorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetLabelAnchor() {
		return labelAnchorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case ComponentPackage.CURVE_FITTING__LABEL:
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
		case ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES:
			return getLineAttributes();
		case ComponentPackage.CURVE_FITTING__LABEL:
			return getLabel();
		case ComponentPackage.CURVE_FITTING__LABEL_ANCHOR:
			return getLabelAnchor();
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
		case ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.CURVE_FITTING__LABEL:
			setLabel((Label) newValue);
			return;
		case ComponentPackage.CURVE_FITTING__LABEL_ANCHOR:
			setLabelAnchor((Anchor) newValue);
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
		case ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case ComponentPackage.CURVE_FITTING__LABEL:
			setLabel((Label) null);
			return;
		case ComponentPackage.CURVE_FITTING__LABEL_ANCHOR:
			unsetLabelAnchor();
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
		case ComponentPackage.CURVE_FITTING__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case ComponentPackage.CURVE_FITTING__LABEL:
			return label != null;
		case ComponentPackage.CURVE_FITTING__LABEL_ANCHOR:
			return isSetLabelAnchor();
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
		result.append(" (labelAnchor: "); //$NON-NLS-1$
		if (labelAnchorESet) {
			result.append(labelAnchor);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * Initialize the curve setting.
	 */
	protected void initialize() {
		final Label la = LabelImpl.create();
		LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		la.setOutline(lia);
		la.setVisible(false);
		setLabel(la);
		setLabelAnchor(Anchor.NORTH_LITERAL);

		lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(true);
		setLineAttributes(lia);
	}

	/**
	 * Initialize the curve setting without setting 'isSet' flag.
	 */
	protected void initDefault() {
		final Label la = LabelImpl.createDefault();
		LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		la.setOutline(lia);
		setLabel(la);
		labelAnchor = Anchor.NORTH_LITERAL;

		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setLineAttributes(lia);
	}

	/**
	 * @generated
	 */
	@Override
	public CurveFitting copyInstance() {
		CurveFittingImpl dest = new CurveFittingImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(CurveFitting src) {

		// children

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getLabel() != null) {
			setLabel(src.getLabel().copyInstance());
		}

		// attributes

		labelAnchor = src.getLabelAnchor();

		labelAnchorESet = src.isSetLabelAnchor();

	}

} // CurveFittingImpl
