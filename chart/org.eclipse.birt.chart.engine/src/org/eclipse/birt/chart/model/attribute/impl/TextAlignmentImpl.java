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

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Objects;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Text
 * Alignment</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl#getHorizontalAlignment
 * <em>Horizontal Alignment</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl#getVerticalAlignment
 * <em>Vertical Alignment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextAlignmentImpl extends EObjectImpl implements TextAlignment {

	/**
	 * The default value of the '{@link #getHorizontalAlignment() <em>Horizontal
	 * Alignment</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getHorizontalAlignment()
	 * @generated
	 * @ordered
	 */
	protected static final HorizontalAlignment HORIZONTAL_ALIGNMENT_EDEFAULT = HorizontalAlignment.LEFT_LITERAL;

	/**
	 * The cached value of the '{@link #getHorizontalAlignment() <em>Horizontal
	 * Alignment</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getHorizontalAlignment()
	 * @generated
	 * @ordered
	 */
	protected HorizontalAlignment horizontalAlignment = HORIZONTAL_ALIGNMENT_EDEFAULT;

	/**
	 * This is true if the Horizontal Alignment attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean horizontalAlignmentESet;

	/**
	 * The default value of the '{@link #getVerticalAlignment() <em>Vertical
	 * Alignment</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getVerticalAlignment()
	 * @generated
	 * @ordered
	 */
	protected static final VerticalAlignment VERTICAL_ALIGNMENT_EDEFAULT = VerticalAlignment.TOP_LITERAL;

	/**
	 * The cached value of the '{@link #getVerticalAlignment() <em>Vertical
	 * Alignment</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getVerticalAlignment()
	 * @generated
	 * @ordered
	 */
	protected VerticalAlignment verticalAlignment = VERTICAL_ALIGNMENT_EDEFAULT;

	/**
	 * This is true if the Vertical Alignment attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean verticalAlignmentESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TextAlignmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.TEXT_ALIGNMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setHorizontalAlignment(HorizontalAlignment newHorizontalAlignment) {
		HorizontalAlignment oldHorizontalAlignment = horizontalAlignment;
		horizontalAlignment = newHorizontalAlignment == null ? HORIZONTAL_ALIGNMENT_EDEFAULT : newHorizontalAlignment;
		boolean oldHorizontalAlignmentESet = horizontalAlignmentESet;
		horizontalAlignmentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT,
					oldHorizontalAlignment, horizontalAlignment, !oldHorizontalAlignmentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetHorizontalAlignment() {
		HorizontalAlignment oldHorizontalAlignment = horizontalAlignment;
		boolean oldHorizontalAlignmentESet = horizontalAlignmentESet;
		horizontalAlignment = HORIZONTAL_ALIGNMENT_EDEFAULT;
		horizontalAlignmentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT, oldHorizontalAlignment,
					HORIZONTAL_ALIGNMENT_EDEFAULT, oldHorizontalAlignmentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetHorizontalAlignment() {
		return horizontalAlignmentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setVerticalAlignment(VerticalAlignment newVerticalAlignment) {
		VerticalAlignment oldVerticalAlignment = verticalAlignment;
		verticalAlignment = newVerticalAlignment == null ? VERTICAL_ALIGNMENT_EDEFAULT : newVerticalAlignment;
		boolean oldVerticalAlignmentESet = verticalAlignmentESet;
		verticalAlignmentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT_ALIGNMENT__VERTICAL_ALIGNMENT,
					oldVerticalAlignment, verticalAlignment, !oldVerticalAlignmentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetVerticalAlignment() {
		VerticalAlignment oldVerticalAlignment = verticalAlignment;
		boolean oldVerticalAlignmentESet = verticalAlignmentESet;
		verticalAlignment = VERTICAL_ALIGNMENT_EDEFAULT;
		verticalAlignmentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.TEXT_ALIGNMENT__VERTICAL_ALIGNMENT,
					oldVerticalAlignment, VERTICAL_ALIGNMENT_EDEFAULT, oldVerticalAlignmentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetVerticalAlignment() {
		return verticalAlignmentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT:
			return getHorizontalAlignment();
		case AttributePackage.TEXT_ALIGNMENT__VERTICAL_ALIGNMENT:
			return getVerticalAlignment();
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
		case AttributePackage.TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT:
			setHorizontalAlignment((HorizontalAlignment) newValue);
			return;
		case AttributePackage.TEXT_ALIGNMENT__VERTICAL_ALIGNMENT:
			setVerticalAlignment((VerticalAlignment) newValue);
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
		case AttributePackage.TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT:
			unsetHorizontalAlignment();
			return;
		case AttributePackage.TEXT_ALIGNMENT__VERTICAL_ALIGNMENT:
			unsetVerticalAlignment();
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
		case AttributePackage.TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT:
			return isSetHorizontalAlignment();
		case AttributePackage.TEXT_ALIGNMENT__VERTICAL_ALIGNMENT:
			return isSetVerticalAlignment();
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
		result.append(" (horizontalAlignment: "); //$NON-NLS-1$
		if (horizontalAlignmentESet) {
			result.append(horizontalAlignment);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", verticalAlignment: "); //$NON-NLS-1$
		if (verticalAlignmentESet) {
			result.append(verticalAlignment);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	protected void set(TextAlignment src) {

		// attributes

		horizontalAlignment = src.getHorizontalAlignment();

		horizontalAlignmentESet = src.isSetHorizontalAlignment();

		verticalAlignment = src.getVerticalAlignment();

		verticalAlignmentESet = src.isSetVerticalAlignment();

	}

	/**
	 * A convenient method to create a new TextAlignment instance and initialize its
	 * members
	 *
	 * @return
	 */
	public static final TextAlignment create() {
		final TextAlignment ta = AttributeFactory.eINSTANCE.createTextAlignment();
		((TextAlignmentImpl) ta).initialize();
		return ta;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	protected final void initialize() {
		setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
		setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
	}

	/**
	 * A convenient method to create a new TextAlignment instance and initialize its
	 * members
	 *
	 * @return
	 */
	public static final TextAlignment createDefault() {
		final TextAlignment ta = AttributeFactory.eINSTANCE.createTextAlignment();
		((TextAlignmentImpl) ta).initDefault();
		return ta;
	}

	/**
	 * A convenient method to create a new TextAlignment instance and initialize its
	 * members
	 *
	 * @return
	 */
	public static final TextAlignment createDefault(HorizontalAlignment ha, VerticalAlignment va) {
		final TextAlignment ta = AttributeFactory.eINSTANCE.createTextAlignment();
		((TextAlignmentImpl) ta).horizontalAlignment = HorizontalAlignment.LEFT_LITERAL;
		((TextAlignmentImpl) ta).verticalAlignment = VerticalAlignment.TOP_LITERAL;
		return ta;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	protected final void initDefault() {
		horizontalAlignment = HorizontalAlignment.LEFT_LITERAL;
		verticalAlignment = VerticalAlignment.TOP_LITERAL;
	}

	@Override
	public TextAlignment copyInstance() {
		TextAlignmentImpl dest = new TextAlignmentImpl();
		dest.horizontalAlignment = getHorizontalAlignment();
		dest.horizontalAlignmentESet = isSetHorizontalAlignment();
		dest.verticalAlignment = getVerticalAlignment();
		dest.verticalAlignmentESet = isSetVerticalAlignment();
		return dest;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(horizontalAlignment, verticalAlignment);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof TextAlignment)) {
			return false;
		}

		TextAlignment other = (TextAlignment) obj;

		if (horizontalAlignment.getValue() != other.getHorizontalAlignment().getValue()) {
			return false;
		}
		if (verticalAlignment.getValue() != other.getVerticalAlignment().getValue()) {
			return false;
		}
		return true;
	}

} // TextAlignmentImpl
