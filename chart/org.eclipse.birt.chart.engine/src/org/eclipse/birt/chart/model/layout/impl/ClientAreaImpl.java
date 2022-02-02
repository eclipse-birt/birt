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

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Client
 * Area</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl#getBackground
 * <em>Background</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl#getShadowColor
 * <em>Shadow Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.ClientAreaImpl#getInsets
 * <em>Insets</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ClientAreaImpl extends EObjectImpl implements ClientArea {

	/**
	 * The cached value of the '{@link #getBackground() <em>Background</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBackground()
	 * @generated
	 * @ordered
	 */
	protected Fill background;

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
	 * The cached value of the '{@link #getShadowColor() <em>Shadow Color</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getShadowColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition shadowColor;

	/**
	 * The cached value of the '{@link #getInsets() <em>Insets</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInsets()
	 * @generated
	 * @ordered
	 */
	protected Insets insets;

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ClientAreaImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LayoutPackage.Literals.CLIENT_AREA;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getBackground() {
		return background;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetBackground(Fill newBackground, NotificationChain msgs) {
		Fill oldBackground = background;
		background = newBackground;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.CLIENT_AREA__BACKGROUND, oldBackground, newBackground);
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
	public void setBackground(Fill newBackground) {
		if (newBackground != background) {
			NotificationChain msgs = null;
			if (background != null)
				msgs = ((InternalEObject) background).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__BACKGROUND, null, msgs);
			if (newBackground != null)
				msgs = ((InternalEObject) newBackground).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__BACKGROUND, null, msgs);
			msgs = basicSetBackground(newBackground, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.CLIENT_AREA__BACKGROUND, newBackground,
					newBackground));
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
					LayoutPackage.CLIENT_AREA__OUTLINE, oldOutline, newOutline);
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
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__OUTLINE, null, msgs);
			if (newOutline != null)
				msgs = ((InternalEObject) newOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__OUTLINE, null, msgs);
			msgs = basicSetOutline(newOutline, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.CLIENT_AREA__OUTLINE, newOutline,
					newOutline));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ColorDefinition getShadowColor() {
		return shadowColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetShadowColor(ColorDefinition newShadowColor, NotificationChain msgs) {
		ColorDefinition oldShadowColor = shadowColor;
		shadowColor = newShadowColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.CLIENT_AREA__SHADOW_COLOR, oldShadowColor, newShadowColor);
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
	public void setShadowColor(ColorDefinition newShadowColor) {
		if (newShadowColor != shadowColor) {
			NotificationChain msgs = null;
			if (shadowColor != null)
				msgs = ((InternalEObject) shadowColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__SHADOW_COLOR, null, msgs);
			if (newShadowColor != null)
				msgs = ((InternalEObject) newShadowColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__SHADOW_COLOR, null, msgs);
			msgs = basicSetShadowColor(newShadowColor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.CLIENT_AREA__SHADOW_COLOR,
					newShadowColor, newShadowColor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Insets getInsets() {
		return insets;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetInsets(Insets newInsets, NotificationChain msgs) {
		Insets oldInsets = insets;
		insets = newInsets;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.CLIENT_AREA__INSETS, oldInsets, newInsets);
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
	public void setInsets(Insets newInsets) {
		if (newInsets != insets) {
			NotificationChain msgs = null;
			if (insets != null)
				msgs = ((InternalEObject) insets).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__INSETS, null, msgs);
			if (newInsets != null)
				msgs = ((InternalEObject) newInsets).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.CLIENT_AREA__INSETS, null, msgs);
			msgs = basicSetInsets(newInsets, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.CLIENT_AREA__INSETS, newInsets,
					newInsets));
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
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.CLIENT_AREA__VISIBLE, oldVisible,
					visible, !oldVisibleESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.CLIENT_AREA__VISIBLE, oldVisible,
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case LayoutPackage.CLIENT_AREA__BACKGROUND:
			return basicSetBackground(null, msgs);
		case LayoutPackage.CLIENT_AREA__OUTLINE:
			return basicSetOutline(null, msgs);
		case LayoutPackage.CLIENT_AREA__SHADOW_COLOR:
			return basicSetShadowColor(null, msgs);
		case LayoutPackage.CLIENT_AREA__INSETS:
			return basicSetInsets(null, msgs);
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
		case LayoutPackage.CLIENT_AREA__BACKGROUND:
			return getBackground();
		case LayoutPackage.CLIENT_AREA__OUTLINE:
			return getOutline();
		case LayoutPackage.CLIENT_AREA__SHADOW_COLOR:
			return getShadowColor();
		case LayoutPackage.CLIENT_AREA__INSETS:
			return getInsets();
		case LayoutPackage.CLIENT_AREA__VISIBLE:
			return isVisible();
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
		case LayoutPackage.CLIENT_AREA__BACKGROUND:
			setBackground((Fill) newValue);
			return;
		case LayoutPackage.CLIENT_AREA__OUTLINE:
			setOutline((LineAttributes) newValue);
			return;
		case LayoutPackage.CLIENT_AREA__SHADOW_COLOR:
			setShadowColor((ColorDefinition) newValue);
			return;
		case LayoutPackage.CLIENT_AREA__INSETS:
			setInsets((Insets) newValue);
			return;
		case LayoutPackage.CLIENT_AREA__VISIBLE:
			setVisible((Boolean) newValue);
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
		case LayoutPackage.CLIENT_AREA__BACKGROUND:
			setBackground((Fill) null);
			return;
		case LayoutPackage.CLIENT_AREA__OUTLINE:
			setOutline((LineAttributes) null);
			return;
		case LayoutPackage.CLIENT_AREA__SHADOW_COLOR:
			setShadowColor((ColorDefinition) null);
			return;
		case LayoutPackage.CLIENT_AREA__INSETS:
			setInsets((Insets) null);
			return;
		case LayoutPackage.CLIENT_AREA__VISIBLE:
			unsetVisible();
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
		case LayoutPackage.CLIENT_AREA__BACKGROUND:
			return background != null;
		case LayoutPackage.CLIENT_AREA__OUTLINE:
			return outline != null;
		case LayoutPackage.CLIENT_AREA__SHADOW_COLOR:
			return shadowColor != null;
		case LayoutPackage.CLIENT_AREA__INSETS:
			return insets != null;
		case LayoutPackage.CLIENT_AREA__VISIBLE:
			return isSetVisible();
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
		result.append(" (visible: "); //$NON-NLS-1$
		if (visibleESet)
			result.append(visible);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	public final void initialize() {
		// setBackground( ColorDefinitionImpl.WHITE( ) );
		final LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 0);
		lia.setVisible(false);
		setOutline(lia);
		setInsets(InsetsImpl.create(0, 0, 0, 0));
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	public final void initDefault() {
		// setBackground( ColorDefinitionImpl.WHITE( ) );
		final LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 0, false);
		setOutline(lia);
		setInsets(InsetsImpl.createDefault(0, 0, 0, 0));
	}

	/**
	 * @generated
	 */
	public ClientArea copyInstance() {
		ClientAreaImpl dest = new ClientAreaImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(ClientArea src) {

		// children

		if (src.getBackground() != null) {
			setBackground(src.getBackground().copyInstance());
		}

		if (src.getOutline() != null) {
			setOutline(src.getOutline().copyInstance());
		}

		if (src.getShadowColor() != null) {
			setShadowColor(src.getShadowColor().copyInstance());
		}

		if (src.getInsets() != null) {
			setInsets(src.getInsets().copyInstance());
		}

		// attributes

		visible = src.isVisible();

		visibleESet = src.isSetVisible();

	}

} // ClientAreaImpl
