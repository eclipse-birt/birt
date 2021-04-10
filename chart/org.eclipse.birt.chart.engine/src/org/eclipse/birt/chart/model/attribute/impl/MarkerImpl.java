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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Marker</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getSize
 * <em>Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getFill
 * <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getIconPalette
 * <em>Icon Palette</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl#getOutline
 * <em>Outline</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarkerImpl extends EObjectImpl implements Marker {

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final MarkerType TYPE_EDEFAULT = MarkerType.CROSSHAIR_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected MarkerType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final int SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected int size = SIZE_EDEFAULT;

	/**
	 * This is true if the Size attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean sizeESet;

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * This is true if the Visible attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean visibleESet;

	/**
	 * The cached value of the '{@link #getFill() <em>Fill</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFill()
	 * @generated
	 * @ordered
	 */
	protected Fill fill;

	/**
	 * The cached value of the '{@link #getIconPalette() <em>Icon Palette</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getIconPalette()
	 * @generated
	 * @ordered
	 */
	protected Palette iconPalette;

	/**
	 * The cached value of the '{@link #getOutline() <em>Outline</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOutline()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes outline;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MarkerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.MARKER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MarkerType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(MarkerType newType) {
		MarkerType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MARKER__TYPE, oldType, type,
					!oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetType() {
		MarkerType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.MARKER__TYPE, oldType,
					TYPE_EDEFAULT, oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetType() {
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSize(int newSize) {
		int oldSize = size;
		size = newSize;
		boolean oldSizeESet = sizeESet;
		sizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MARKER__SIZE, oldSize, size,
					!oldSizeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetSize() {
		int oldSize = size;
		boolean oldSizeESet = sizeESet;
		size = SIZE_EDEFAULT;
		sizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.MARKER__SIZE, oldSize,
					SIZE_EDEFAULT, oldSizeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetSize() {
		return sizeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		boolean oldVisibleESet = visibleESet;
		visibleESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MARKER__VISIBLE, oldVisible, visible,
					!oldVisibleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetVisible() {
		boolean oldVisible = visible;
		boolean oldVisibleESet = visibleESet;
		visible = VISIBLE_EDEFAULT;
		visibleESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.MARKER__VISIBLE, oldVisible,
					VISIBLE_EDEFAULT, oldVisibleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetVisible() {
		return visibleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getFill() {
		return fill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs) {
		Fill oldFill = fill;
		fill = newFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.MARKER__FILL, oldFill, newFill);
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
	public void setFill(Fill newFill) {
		if (newFill != fill) {
			NotificationChain msgs = null;
			if (fill != null)
				msgs = ((InternalEObject) fill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.MARKER__FILL, null, msgs);
			if (newFill != null)
				msgs = ((InternalEObject) newFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.MARKER__FILL, null, msgs);
			msgs = basicSetFill(newFill, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MARKER__FILL, newFill, newFill));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Palette getIconPalette() {
		return iconPalette;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetIconPalette(Palette newIconPalette, NotificationChain msgs) {
		Palette oldIconPalette = iconPalette;
		iconPalette = newIconPalette;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.MARKER__ICON_PALETTE, oldIconPalette, newIconPalette);
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
	public void setIconPalette(Palette newIconPalette) {
		if (newIconPalette != iconPalette) {
			NotificationChain msgs = null;
			if (iconPalette != null)
				msgs = ((InternalEObject) iconPalette).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.MARKER__ICON_PALETTE, null, msgs);
			if (newIconPalette != null)
				msgs = ((InternalEObject) newIconPalette).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.MARKER__ICON_PALETTE, null, msgs);
			msgs = basicSetIconPalette(newIconPalette, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MARKER__ICON_PALETTE, newIconPalette,
					newIconPalette));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getOutline() {
		return outline;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetOutline(LineAttributes newOutline, NotificationChain msgs) {
		LineAttributes oldOutline = outline;
		outline = newOutline;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.MARKER__OUTLINE, oldOutline, newOutline);
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
	public void setOutline(LineAttributes newOutline) {
		if (newOutline != outline) {
			NotificationChain msgs = null;
			if (outline != null)
				msgs = ((InternalEObject) outline).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.MARKER__OUTLINE, null, msgs);
			if (newOutline != null)
				msgs = ((InternalEObject) newOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.MARKER__OUTLINE, null, msgs);
			msgs = basicSetOutline(newOutline, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MARKER__OUTLINE, newOutline,
					newOutline));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.MARKER__FILL:
			return basicSetFill(null, msgs);
		case AttributePackage.MARKER__ICON_PALETTE:
			return basicSetIconPalette(null, msgs);
		case AttributePackage.MARKER__OUTLINE:
			return basicSetOutline(null, msgs);
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
		case AttributePackage.MARKER__TYPE:
			return getType();
		case AttributePackage.MARKER__SIZE:
			return getSize();
		case AttributePackage.MARKER__VISIBLE:
			return isVisible();
		case AttributePackage.MARKER__FILL:
			return getFill();
		case AttributePackage.MARKER__ICON_PALETTE:
			return getIconPalette();
		case AttributePackage.MARKER__OUTLINE:
			return getOutline();
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
		case AttributePackage.MARKER__TYPE:
			setType((MarkerType) newValue);
			return;
		case AttributePackage.MARKER__SIZE:
			setSize((Integer) newValue);
			return;
		case AttributePackage.MARKER__VISIBLE:
			setVisible((Boolean) newValue);
			return;
		case AttributePackage.MARKER__FILL:
			setFill((Fill) newValue);
			return;
		case AttributePackage.MARKER__ICON_PALETTE:
			setIconPalette((Palette) newValue);
			return;
		case AttributePackage.MARKER__OUTLINE:
			setOutline((LineAttributes) newValue);
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
		case AttributePackage.MARKER__TYPE:
			unsetType();
			return;
		case AttributePackage.MARKER__SIZE:
			unsetSize();
			return;
		case AttributePackage.MARKER__VISIBLE:
			unsetVisible();
			return;
		case AttributePackage.MARKER__FILL:
			setFill((Fill) null);
			return;
		case AttributePackage.MARKER__ICON_PALETTE:
			setIconPalette((Palette) null);
			return;
		case AttributePackage.MARKER__OUTLINE:
			setOutline((LineAttributes) null);
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
		case AttributePackage.MARKER__TYPE:
			return isSetType();
		case AttributePackage.MARKER__SIZE:
			return isSetSize();
		case AttributePackage.MARKER__VISIBLE:
			return isSetVisible();
		case AttributePackage.MARKER__FILL:
			return fill != null;
		case AttributePackage.MARKER__ICON_PALETTE:
			return iconPalette != null;
		case AttributePackage.MARKER__OUTLINE:
			return outline != null;
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
		result.append(" (type: "); //$NON-NLS-1$
		if (typeESet)
			result.append(type);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", size: "); //$NON-NLS-1$
		if (sizeESet)
			result.append(size);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", visible: "); //$NON-NLS-1$
		if (visibleESet)
			result.append(visible);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	public static final Marker create(MarkerType markerType, int size) {
		Marker marker = AttributeFactory.eINSTANCE.createMarker();
		marker.setType(markerType);
		marker.setSize(size);
		marker.setVisible(true);
		marker.setOutline(AttributeFactory.eINSTANCE.createLineAttributes());
		marker.getOutline().setVisible(true);

		return marker;
	}

	public static final Marker createDefault(MarkerType markerType, int size, boolean visible) {
		Marker marker = AttributeFactory.eINSTANCE.createMarker();
		((MarkerImpl) marker).type = markerType;
		((MarkerImpl) marker).size = size;
		((MarkerImpl) marker).visible = visible;
		marker.setOutline(AttributeFactory.eINSTANCE.createLineAttributes());
		try {
			ChartElementUtil.setDefaultValue(marker.getOutline(), "visible", true); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}
		return marker;
	}

	/**
	 * For accelerating graphic purpose make a fast copy of a Marker without
	 * iconPalette, which is obsolete, and the fill, which will be changed in many
	 * cases. Using the setFillSimple to set the fill faster.
	 * 
	 * @param src
	 * @return marker instance
	 */
	public static final Marker copyInstanceNoFill(Marker src) {
		if (src == null) {
			return null;
		}

		MarkerImpl mk = new MarkerImpl();
		mk.type = src.getType();

		mk.size = src.getSize();
		mk.sizeESet = src.isSetSize();

		mk.visible = src.isVisible();
		mk.visibleESet = src.isSetVisible();

		LineAttributes tOutline = src.getOutline();
		if (tOutline != null) {
			mk.outline = tOutline.copyInstance();
		}

		return mk;
	}

	/**
	 * To set the fill without EMF notifying for accelerating graphic purpose.
	 * 
	 * @param marker
	 * @param fill
	 */
	public static final void setFillSimple(Marker marker, Fill fill) {
		if (marker instanceof MarkerImpl) {
			((MarkerImpl) marker).fill = fill;
		}

	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public Marker copyInstance() {
		MarkerImpl dest = new MarkerImpl();

		Fill tFill = getFill();
		if (tFill != null) {
			dest.fill = tFill.copyInstance();
		}

		Palette tIconPalette = getIconPalette();
		if (tIconPalette != null) {
			dest.setIconPalette(tIconPalette.copyInstance());
		}

		LineAttributes tOutline = getOutline();
		if (tOutline != null) {
			dest.outline = tOutline.copyInstance();
		}

		dest.type = getType();
		dest.typeESet = isSetType();
		dest.size = getSize();
		dest.sizeESet = isSetSize();
		dest.visible = isVisible();
		dest.visibleESet = isSetVisible();
		return dest;
	}

	protected void set(Marker src) {
		if (src.getFill() != null) {
			setFill(src.getFill().copyInstance());
		}

		if (src.getIconPalette() != null) {
			setIconPalette(src.getIconPalette().copyInstance());
		}

		if (src.getOutline() != null) {
			setOutline(src.getOutline().copyInstance());
		}

		type = src.getType();
		typeESet = src.isSetType();
		size = src.getSize();
		sizeESet = src.isSetSize();
		visible = src.isVisible();
		visibleESet = src.isSetVisible();
	}

} // MarkerImpl
