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

import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Needle</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.NeedleImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.NeedleImpl#getDecorator
 * <em>Decorator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NeedleImpl extends EObjectImpl implements Needle {

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
	 * The default value of the '{@link #getDecorator() <em>Decorator</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getDecorator()
	 * @generated
	 * @ordered
	 */
	protected static final LineDecorator DECORATOR_EDEFAULT = LineDecorator.ARROW_LITERAL;

	/**
	 * The cached value of the '{@link #getDecorator() <em>Decorator</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getDecorator()
	 * @generated
	 * @ordered
	 */
	protected LineDecorator decorator = DECORATOR_EDEFAULT;

	/**
	 * This is true if the Decorator attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean decoratorESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected NeedleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.NEEDLE;
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
					ComponentPackage.NEEDLE__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
						EOPPOSITE_FEATURE_BASE - ComponentPackage.NEEDLE__LINE_ATTRIBUTES, null, msgs);
			}
			if (newLineAttributes != null) {
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.NEEDLE__LINE_ATTRIBUTES, null, msgs);
			}
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.NEEDLE__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineDecorator getDecorator() {
		return decorator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDecorator(LineDecorator newDecorator) {
		LineDecorator oldDecorator = decorator;
		decorator = newDecorator == null ? DECORATOR_EDEFAULT : newDecorator;
		boolean oldDecoratorESet = decoratorESet;
		decoratorESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.NEEDLE__DECORATOR, oldDecorator,
					decorator, !oldDecoratorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetDecorator() {
		LineDecorator oldDecorator = decorator;
		boolean oldDecoratorESet = decoratorESet;
		decorator = DECORATOR_EDEFAULT;
		decoratorESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.NEEDLE__DECORATOR, oldDecorator,
					DECORATOR_EDEFAULT, oldDecoratorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetDecorator() {
		return decoratorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.NEEDLE__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
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
		case ComponentPackage.NEEDLE__LINE_ATTRIBUTES:
			return getLineAttributes();
		case ComponentPackage.NEEDLE__DECORATOR:
			return getDecorator();
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
		case ComponentPackage.NEEDLE__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.NEEDLE__DECORATOR:
			setDecorator((LineDecorator) newValue);
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
		case ComponentPackage.NEEDLE__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case ComponentPackage.NEEDLE__DECORATOR:
			unsetDecorator();
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
		case ComponentPackage.NEEDLE__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case ComponentPackage.NEEDLE__DECORATOR:
			return isSetDecorator();
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
		result.append(" (decorator: "); //$NON-NLS-1$
		if (decoratorESet) {
			result.append(decorator);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @return instance of <code>Needle</code>.
	 */
	public static final Needle create() {
		Needle nl = ComponentFactory.eINSTANCE.createNeedle();
		((NeedleImpl) nl).initialize();
		return nl;
	}

	/**
	 *
	 */
	public final void initialize() {
		LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		setLineAttributes(lia);
	}

	/**
	 * @return instance of <code>Needle</code> without setting 'isSet' flag.
	 */
	public static final Needle createDefault() {
		Needle nl = ComponentFactory.eINSTANCE.createNeedle();
		((NeedleImpl) nl).initDefault();
		return nl;
	}

	/**
	 *
	 */
	public final void initDefault() {
		LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setLineAttributes(lia);
	}

	/**
	 * @generated
	 */
	@Override
	public Needle copyInstance() {
		NeedleImpl dest = new NeedleImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Needle src) {

		// children

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		// attributes

		decorator = src.getDecorator();

		decoratorESet = src.isSetDecorator();

	}

} // NeedleImpl
