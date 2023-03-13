/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: PatternImageImpl.java,v 1.1 2010/03/30 10:26:11 ywang1 Exp $
 */

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Objects;

import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Pattern
 * Image</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl#getBitmap
 * <em>Bitmap</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl#getForeColor
 * <em>Fore Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl#getBackColor
 * <em>Back Color</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PatternImageImpl extends ImageImpl implements PatternImage {

	/**
	 * The default value of the '{@link #getBitmap() <em>Bitmap</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getBitmap()
	 * @generated
	 * @ordered
	 */
	protected static final long BITMAP_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBitmap() <em>Bitmap</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getBitmap()
	 * @generated
	 * @ordered
	 */
	protected long bitmap = BITMAP_EDEFAULT;

	/**
	 * This is true if the Bitmap attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean bitmapESet;

	/**
	 * The cached value of the '{@link #getForeColor() <em>Fore Color</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getForeColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition foreColor;

	/**
	 * The cached value of the '{@link #getBackColor() <em>Back Color</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getBackColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition backColor;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	protected PatternImageImpl() {
		super();
		initialize();
	}

	protected void initialize() {
		backColor = ColorDefinitionImpl.WHITE();
		foreColor = ColorDefinitionImpl.BLACK();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.PATTERN_IMAGE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public long getBitmap() {
		return bitmap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setBitmap(long newBitmap) {
		long oldBitmap = bitmap;
		bitmap = newBitmap;
		boolean oldBitmapESet = bitmapESet;
		bitmapESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.PATTERN_IMAGE__BITMAP, oldBitmap,
					bitmap, !oldBitmapESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetBitmap() {
		long oldBitmap = bitmap;
		boolean oldBitmapESet = bitmapESet;
		bitmap = BITMAP_EDEFAULT;
		bitmapESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.PATTERN_IMAGE__BITMAP, oldBitmap,
					BITMAP_EDEFAULT, oldBitmapESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetBitmap() {
		return bitmapESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ColorDefinition getForeColor() {
		return foreColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetForeColor(ColorDefinition newForeColor, NotificationChain msgs) {
		ColorDefinition oldForeColor = foreColor;
		foreColor = newForeColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.PATTERN_IMAGE__FORE_COLOR, oldForeColor, newForeColor);
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
	public void setForeColor(ColorDefinition newForeColor) {
		if (newForeColor != foreColor) {
			NotificationChain msgs = null;
			if (foreColor != null) {
				msgs = ((InternalEObject) foreColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.PATTERN_IMAGE__FORE_COLOR, null, msgs);
			}
			if (newForeColor != null) {
				msgs = ((InternalEObject) newForeColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.PATTERN_IMAGE__FORE_COLOR, null, msgs);
			}
			msgs = basicSetForeColor(newForeColor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.PATTERN_IMAGE__FORE_COLOR,
					newForeColor, newForeColor));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ColorDefinition getBackColor() {
		return backColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetBackColor(ColorDefinition newBackColor, NotificationChain msgs) {
		ColorDefinition oldBackColor = backColor;
		backColor = newBackColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.PATTERN_IMAGE__BACK_COLOR, oldBackColor, newBackColor);
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
	public void setBackColor(ColorDefinition newBackColor) {
		if (newBackColor != backColor) {
			NotificationChain msgs = null;
			if (backColor != null) {
				msgs = ((InternalEObject) backColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.PATTERN_IMAGE__BACK_COLOR, null, msgs);
			}
			if (newBackColor != null) {
				msgs = ((InternalEObject) newBackColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.PATTERN_IMAGE__BACK_COLOR, null, msgs);
			}
			msgs = basicSetBackColor(newBackColor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.PATTERN_IMAGE__BACK_COLOR,
					newBackColor, newBackColor));
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
		case AttributePackage.PATTERN_IMAGE__FORE_COLOR:
			return basicSetForeColor(null, msgs);
		case AttributePackage.PATTERN_IMAGE__BACK_COLOR:
			return basicSetBackColor(null, msgs);
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
		case AttributePackage.PATTERN_IMAGE__BITMAP:
			return getBitmap();
		case AttributePackage.PATTERN_IMAGE__FORE_COLOR:
			return getForeColor();
		case AttributePackage.PATTERN_IMAGE__BACK_COLOR:
			return getBackColor();
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
		case AttributePackage.PATTERN_IMAGE__BITMAP:
			setBitmap((Long) newValue);
			return;
		case AttributePackage.PATTERN_IMAGE__FORE_COLOR:
			setForeColor((ColorDefinition) newValue);
			return;
		case AttributePackage.PATTERN_IMAGE__BACK_COLOR:
			setBackColor((ColorDefinition) newValue);
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
		case AttributePackage.PATTERN_IMAGE__BITMAP:
			unsetBitmap();
			return;
		case AttributePackage.PATTERN_IMAGE__FORE_COLOR:
			setForeColor((ColorDefinition) null);
			return;
		case AttributePackage.PATTERN_IMAGE__BACK_COLOR:
			setBackColor((ColorDefinition) null);
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
		case AttributePackage.PATTERN_IMAGE__BITMAP:
			return isSetBitmap();
		case AttributePackage.PATTERN_IMAGE__FORE_COLOR:
			return foreColor != null;
		case AttributePackage.PATTERN_IMAGE__BACK_COLOR:
			return backColor != null;
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
		result.append(" (bitmap: "); //$NON-NLS-1$
		if (bitmapESet) {
			result.append(bitmap);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	protected void set(PatternImage src) {

		super.set(src);

		// children

		if (src.getForeColor() != null) {
			setForeColor(src.getForeColor().copyInstance());
		}

		if (src.getBackColor() != null) {
			setBackColor(src.getBackColor().copyInstance());
		}

		// attributes

		bitmap = src.getBitmap();

		bitmapESet = src.isSetBitmap();

	}

	/**
	 * @generated
	 */
	@Override
	public PatternImage copyInstance() {
		PatternImageImpl dest = new PatternImageImpl();
		dest.set(this);
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((backColor == null) ? 0 : backColor.hashCode());
		result = prime * result + (int) (bitmap ^ (bitmap >>> 32));
		result = prime * result + (bitmapESet ? 1231 : 1237);
		result = prime * result + ((foreColor == null) ? 0 : foreColor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj) || (getClass() != obj.getClass())) {
			return false;
		}
		PatternImageImpl other = (PatternImageImpl) obj;
		if (!Objects.equals(backColor, other.backColor)) {
			return false;
		}
		if (bitmap != other.bitmap) {
			return false;
		}
		if (bitmapESet != other.bitmapESet) {
			return false;
		}
		if (!Objects.equals(foreColor, other.foreColor)) {
			return false;
		}
		return true;
	}

} // PatternImageImpl
