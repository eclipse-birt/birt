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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Label</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#getCaption
 * <em>Caption</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#getBackground
 * <em>Background</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#getShadowColor
 * <em>Shadow Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#getInsets
 * <em>Insets</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.LabelImpl#getEllipsis
 * <em>Ellipsis</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LabelImpl extends EObjectImpl implements Label {

	/**
	 * The cached value of the '{@link #getCaption() <em>Caption</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCaption()
	 * @generated
	 * @ordered
	 */
	protected Text caption;

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
	 * The default value of the '{@link #getEllipsis() <em>Ellipsis</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEllipsis()
	 * @generated
	 * @ordered
	 */
	protected static final int ELLIPSIS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEllipsis() <em>Ellipsis</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEllipsis()
	 * @generated
	 * @ordered
	 */
	protected int ellipsis = ELLIPSIS_EDEFAULT;

	/**
	 * This is true if the Ellipsis attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean ellipsisESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected LabelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.LABEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Text getCaption() {
		return caption;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetCaption(Text newCaption, NotificationChain msgs) {
		Text oldCaption = caption;
		caption = newCaption;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.LABEL__CAPTION, oldCaption, newCaption);
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
	public void setCaption(Text newCaption) {
		if (newCaption != caption) {
			NotificationChain msgs = null;
			if (caption != null) {
				msgs = ((InternalEObject) caption).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__CAPTION, null, msgs);
			}
			if (newCaption != null) {
				msgs = ((InternalEObject) newCaption).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__CAPTION, null, msgs);
			}
			msgs = basicSetCaption(newCaption, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__CAPTION, newCaption,
					newCaption));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
					ComponentPackage.LABEL__BACKGROUND, oldBackground, newBackground);
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
	public void setBackground(Fill newBackground) {
		if (newBackground != background) {
			NotificationChain msgs = null;
			if (background != null) {
				msgs = ((InternalEObject) background).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__BACKGROUND, null, msgs);
			}
			if (newBackground != null) {
				msgs = ((InternalEObject) newBackground).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__BACKGROUND, null, msgs);
			}
			msgs = basicSetBackground(newBackground, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__BACKGROUND, newBackground,
					newBackground));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
					ComponentPackage.LABEL__OUTLINE, oldOutline, newOutline);
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
	public void setOutline(LineAttributes newOutline) {
		if (newOutline != outline) {
			NotificationChain msgs = null;
			if (outline != null) {
				msgs = ((InternalEObject) outline).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__OUTLINE, null, msgs);
			}
			if (newOutline != null) {
				msgs = ((InternalEObject) newOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__OUTLINE, null, msgs);
			}
			msgs = basicSetOutline(newOutline, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__OUTLINE, newOutline,
					newOutline));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
					ComponentPackage.LABEL__SHADOW_COLOR, oldShadowColor, newShadowColor);
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
	public void setShadowColor(ColorDefinition newShadowColor) {
		if (newShadowColor != shadowColor) {
			NotificationChain msgs = null;
			if (shadowColor != null) {
				msgs = ((InternalEObject) shadowColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__SHADOW_COLOR, null, msgs);
			}
			if (newShadowColor != null) {
				msgs = ((InternalEObject) newShadowColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__SHADOW_COLOR, null, msgs);
			}
			msgs = basicSetShadowColor(newShadowColor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__SHADOW_COLOR, newShadowColor,
					newShadowColor));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
					ComponentPackage.LABEL__INSETS, oldInsets, newInsets);
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
	public void setInsets(Insets newInsets) {
		if (newInsets != insets) {
			NotificationChain msgs = null;
			if (insets != null) {
				msgs = ((InternalEObject) insets).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__INSETS, null, msgs);
			}
			if (newInsets != null) {
				msgs = ((InternalEObject) newInsets).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.LABEL__INSETS, null, msgs);
			}
			msgs = basicSetInsets(newInsets, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__INSETS, newInsets,
					newInsets));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		boolean oldVisibleESet = visibleESet;
		visibleESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__VISIBLE, oldVisible, visible,
					!oldVisibleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetVisible() {
		boolean oldVisible = visible;
		boolean oldVisibleESet = visibleESet;
		visible = VISIBLE_EDEFAULT;
		visibleESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.LABEL__VISIBLE, oldVisible,
					VISIBLE_EDEFAULT, oldVisibleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetVisible() {
		return visibleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getEllipsis() {
		return ellipsis;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setEllipsis(int newEllipsis) {
		int oldEllipsis = ellipsis;
		ellipsis = newEllipsis;
		boolean oldEllipsisESet = ellipsisESet;
		ellipsisESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.LABEL__ELLIPSIS, oldEllipsis,
					ellipsis, !oldEllipsisESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetEllipsis() {
		int oldEllipsis = ellipsis;
		boolean oldEllipsisESet = ellipsisESet;
		ellipsis = ELLIPSIS_EDEFAULT;
		ellipsisESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.LABEL__ELLIPSIS, oldEllipsis,
					ELLIPSIS_EDEFAULT, oldEllipsisESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetEllipsis() {
		return ellipsisESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.LABEL__CAPTION:
			return basicSetCaption(null, msgs);
		case ComponentPackage.LABEL__BACKGROUND:
			return basicSetBackground(null, msgs);
		case ComponentPackage.LABEL__OUTLINE:
			return basicSetOutline(null, msgs);
		case ComponentPackage.LABEL__SHADOW_COLOR:
			return basicSetShadowColor(null, msgs);
		case ComponentPackage.LABEL__INSETS:
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
		case ComponentPackage.LABEL__CAPTION:
			return getCaption();
		case ComponentPackage.LABEL__BACKGROUND:
			return getBackground();
		case ComponentPackage.LABEL__OUTLINE:
			return getOutline();
		case ComponentPackage.LABEL__SHADOW_COLOR:
			return getShadowColor();
		case ComponentPackage.LABEL__INSETS:
			return getInsets();
		case ComponentPackage.LABEL__VISIBLE:
			return isVisible();
		case ComponentPackage.LABEL__ELLIPSIS:
			return getEllipsis();
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
		case ComponentPackage.LABEL__CAPTION:
			setCaption((Text) newValue);
			return;
		case ComponentPackage.LABEL__BACKGROUND:
			setBackground((Fill) newValue);
			return;
		case ComponentPackage.LABEL__OUTLINE:
			setOutline((LineAttributes) newValue);
			return;
		case ComponentPackage.LABEL__SHADOW_COLOR:
			setShadowColor((ColorDefinition) newValue);
			return;
		case ComponentPackage.LABEL__INSETS:
			setInsets((Insets) newValue);
			return;
		case ComponentPackage.LABEL__VISIBLE:
			setVisible((Boolean) newValue);
			return;
		case ComponentPackage.LABEL__ELLIPSIS:
			setEllipsis((Integer) newValue);
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
		case ComponentPackage.LABEL__CAPTION:
			setCaption((Text) null);
			return;
		case ComponentPackage.LABEL__BACKGROUND:
			setBackground((Fill) null);
			return;
		case ComponentPackage.LABEL__OUTLINE:
			setOutline((LineAttributes) null);
			return;
		case ComponentPackage.LABEL__SHADOW_COLOR:
			setShadowColor((ColorDefinition) null);
			return;
		case ComponentPackage.LABEL__INSETS:
			setInsets((Insets) null);
			return;
		case ComponentPackage.LABEL__VISIBLE:
			unsetVisible();
			return;
		case ComponentPackage.LABEL__ELLIPSIS:
			unsetEllipsis();
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
		case ComponentPackage.LABEL__CAPTION:
			return caption != null;
		case ComponentPackage.LABEL__BACKGROUND:
			return background != null;
		case ComponentPackage.LABEL__OUTLINE:
			return outline != null;
		case ComponentPackage.LABEL__SHADOW_COLOR:
			return shadowColor != null;
		case ComponentPackage.LABEL__INSETS:
			return insets != null;
		case ComponentPackage.LABEL__VISIBLE:
			return isSetVisible();
		case ComponentPackage.LABEL__ELLIPSIS:
			return isSetEllipsis();
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
		result.append(" (visible: "); //$NON-NLS-1$
		if (visibleESet) {
			result.append(visible);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", ellipsis: "); //$NON-NLS-1$
		if (ellipsisESet) {
			result.append(ellipsis);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method to create an initialized 'Label' instance
	 *
	 * @return label instance with setting 'isSet' flag.
	 */
	public static final Label create() {
		final Label la = ComponentFactory.eINSTANCE.createLabel();
		((LabelImpl) la).initialize();
		return la;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	protected final void initialize() {
		setCaption(TextImpl.create((String) null));

		setBackground(ColorDefinitionImpl.TRANSPARENT());

		final Insets ins = AttributeFactory.eINSTANCE.createInsets();
		((InsetsImpl) ins).set(0, 2, 0, 3);
		setInsets(ins);

		final LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) lia).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		setOutline(lia);

		setVisible(true);
	}

	/**
	 * A convenience method to create an initialized 'Label' instance
	 *
	 * @return label instance without setting 'isSet' flag.
	 */
	public static final Label createDefault() {
		final Label la = ComponentFactory.eINSTANCE.createLabel();
		((LabelImpl) la).initDefault(true);
		return la;
	}

	/**
	 * A convenience method to create an initialized 'Label' instance
	 *
	 * @return label instance without setting 'isSet' flag.
	 */
	public static final Label createDefault(boolean visible) {
		final Label la = ComponentFactory.eINSTANCE.createLabel();
		((LabelImpl) la).initDefault(visible);
		return la;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	protected final void initDefault(boolean visible) {
		setCaption(TextImpl.createDefault((String) null));

		final Insets ins = InsetsImpl.createDefault(0, 2, 0, 3);
		setInsets(ins);

		final LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1);
		setOutline(lia);

		this.visible = visible;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	@Override
	public Label copyInstance() {
		LabelImpl dest = new LabelImpl();

		Text tCaption = getCaption();
		if (tCaption != null) {
			dest.caption = tCaption.copyInstance();
		}

		Fill tBackground = getBackground();
		if (tBackground != null) {
			dest.background = tBackground.copyInstance();
		}

		LineAttributes tOutline = getOutline();
		if (tOutline != null) {
			dest.outline = tOutline.copyInstance();
		}

		ColorDefinition tShadowColor = getShadowColor();
		if (tShadowColor != null) {
			dest.shadowColor = tShadowColor.copyInstance();
		}

		Insets tInsets = getInsets();
		if (tInsets != null) {
			dest.insets = tInsets.copyInstance();
		}

		dest.visible = isVisible();
		dest.visibleESet = isSetVisible();
		dest.ellipsis = getEllipsis();
		dest.ellipsisESet = isSetEllipsis();
		return dest;
	}

	protected void set(Label src) {
		if (src.getCaption() != null) {
			setCaption(src.getCaption().copyInstance());
		}

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

		visible = src.isVisible();
		visibleESet = src.isSetVisible();
		ellipsis = src.getEllipsis();
		ellipsisESet = src.isSetEllipsis();
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 *
	 * Note this method only copies those working properties of the label. e.g.
	 * which could affect renderer. This is different with the copyInstance()
	 * method, which copies all attributes.
	 *
	 * @param src
	 * @return new label instance.
	 */
	public static Label copyCompactInstance(Label src) {
		if (src == null) {
			return null;
		}
		LabelImpl lb = new LabelImpl();
		lb.visible = src.isVisible();
		lb.visibleESet = src.isSetVisible();

		if (src.getBackground() != null) {
			lb.background = FillUtil.copyOf(src.getBackground());
		}
		lb.caption = src.getCaption().copyInstance();
		lb.insets = src.getInsets().copyInstance();
		if (src.getOutline() != null && src.getOutline().isVisible()) {
			lb.outline = src.getOutline().copyInstance();
		}
		if (ChartUtil.isShadowDefined(src)) {
			lb.shadowColor = src.getShadowColor().copyInstance();
		}
		return lb;
	}

} // LabelImpl
