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
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Style</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl#getFont
 * <em>Font</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl#getColor
 * <em>Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl#getBackgroundColor
 * <em>Background Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl#getBackgroundImage
 * <em>Background Image</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl#getPadding
 * <em>Padding</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StyleImpl extends EObjectImpl implements Style {

	/**
	 * The cached value of the '{@link #getFont() <em>Font</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getFont()
	 * @generated
	 * @ordered
	 */
	protected FontDefinition font;

	/**
	 * The cached value of the '{@link #getColor() <em>Color</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition color;

	/**
	 * The cached value of the '{@link #getBackgroundColor() <em>Background
	 * Color</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getBackgroundColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition backgroundColor;

	/**
	 * The cached value of the '{@link #getBackgroundImage() <em>Background
	 * Image</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getBackgroundImage()
	 * @generated
	 * @ordered
	 */
	protected Image backgroundImage;

	/**
	 * The cached value of the '{@link #getPadding() <em>Padding</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPadding()
	 * @generated
	 * @ordered
	 */
	protected Insets padding;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StyleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.STYLE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FontDefinition getFont() {
		return font;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetFont(FontDefinition newFont, NotificationChain msgs) {
		FontDefinition oldFont = font;
		font = newFont;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE__FONT,
					oldFont, newFont);
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
	public void setFont(FontDefinition newFont) {
		if (newFont != font) {
			NotificationChain msgs = null;
			if (font != null) {
				msgs = ((InternalEObject) font).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__FONT, null, msgs);
			}
			if (newFont != null) {
				msgs = ((InternalEObject) newFont).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__FONT, null, msgs);
			}
			msgs = basicSetFont(newFont, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE__FONT, newFont, newFont));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ColorDefinition getColor() {
		return color;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetColor(ColorDefinition newColor, NotificationChain msgs) {
		ColorDefinition oldColor = color;
		color = newColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.STYLE__COLOR, oldColor, newColor);
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
	public void setColor(ColorDefinition newColor) {
		if (newColor != color) {
			NotificationChain msgs = null;
			if (color != null) {
				msgs = ((InternalEObject) color).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__COLOR, null, msgs);
			}
			if (newColor != null) {
				msgs = ((InternalEObject) newColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__COLOR, null, msgs);
			}
			msgs = basicSetColor(newColor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE__COLOR, newColor, newColor));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ColorDefinition getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetBackgroundColor(ColorDefinition newBackgroundColor, NotificationChain msgs) {
		ColorDefinition oldBackgroundColor = backgroundColor;
		backgroundColor = newBackgroundColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.STYLE__BACKGROUND_COLOR, oldBackgroundColor, newBackgroundColor);
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
	public void setBackgroundColor(ColorDefinition newBackgroundColor) {
		if (newBackgroundColor != backgroundColor) {
			NotificationChain msgs = null;
			if (backgroundColor != null) {
				msgs = ((InternalEObject) backgroundColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__BACKGROUND_COLOR, null, msgs);
			}
			if (newBackgroundColor != null) {
				msgs = ((InternalEObject) newBackgroundColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__BACKGROUND_COLOR, null, msgs);
			}
			msgs = basicSetBackgroundColor(newBackgroundColor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE__BACKGROUND_COLOR,
					newBackgroundColor, newBackgroundColor));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Image getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetBackgroundImage(Image newBackgroundImage, NotificationChain msgs) {
		Image oldBackgroundImage = backgroundImage;
		backgroundImage = newBackgroundImage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.STYLE__BACKGROUND_IMAGE, oldBackgroundImage, newBackgroundImage);
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
	public void setBackgroundImage(Image newBackgroundImage) {
		if (newBackgroundImage != backgroundImage) {
			NotificationChain msgs = null;
			if (backgroundImage != null) {
				msgs = ((InternalEObject) backgroundImage).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__BACKGROUND_IMAGE, null, msgs);
			}
			if (newBackgroundImage != null) {
				msgs = ((InternalEObject) newBackgroundImage).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__BACKGROUND_IMAGE, null, msgs);
			}
			msgs = basicSetBackgroundImage(newBackgroundImage, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE__BACKGROUND_IMAGE,
					newBackgroundImage, newBackgroundImage));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Insets getPadding() {
		return padding;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetPadding(Insets newPadding, NotificationChain msgs) {
		Insets oldPadding = padding;
		padding = newPadding;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.STYLE__PADDING, oldPadding, newPadding);
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
	public void setPadding(Insets newPadding) {
		if (newPadding != padding) {
			NotificationChain msgs = null;
			if (padding != null) {
				msgs = ((InternalEObject) padding).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__PADDING, null, msgs);
			}
			if (newPadding != null) {
				msgs = ((InternalEObject) newPadding).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE__PADDING, null, msgs);
			}
			msgs = basicSetPadding(newPadding, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE__PADDING, newPadding,
					newPadding));
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
		case AttributePackage.STYLE__FONT:
			return basicSetFont(null, msgs);
		case AttributePackage.STYLE__COLOR:
			return basicSetColor(null, msgs);
		case AttributePackage.STYLE__BACKGROUND_COLOR:
			return basicSetBackgroundColor(null, msgs);
		case AttributePackage.STYLE__BACKGROUND_IMAGE:
			return basicSetBackgroundImage(null, msgs);
		case AttributePackage.STYLE__PADDING:
			return basicSetPadding(null, msgs);
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
		case AttributePackage.STYLE__FONT:
			return getFont();
		case AttributePackage.STYLE__COLOR:
			return getColor();
		case AttributePackage.STYLE__BACKGROUND_COLOR:
			return getBackgroundColor();
		case AttributePackage.STYLE__BACKGROUND_IMAGE:
			return getBackgroundImage();
		case AttributePackage.STYLE__PADDING:
			return getPadding();
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
		case AttributePackage.STYLE__FONT:
			setFont((FontDefinition) newValue);
			return;
		case AttributePackage.STYLE__COLOR:
			setColor((ColorDefinition) newValue);
			return;
		case AttributePackage.STYLE__BACKGROUND_COLOR:
			setBackgroundColor((ColorDefinition) newValue);
			return;
		case AttributePackage.STYLE__BACKGROUND_IMAGE:
			setBackgroundImage((Image) newValue);
			return;
		case AttributePackage.STYLE__PADDING:
			setPadding((Insets) newValue);
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
		case AttributePackage.STYLE__FONT:
			setFont((FontDefinition) null);
			return;
		case AttributePackage.STYLE__COLOR:
			setColor((ColorDefinition) null);
			return;
		case AttributePackage.STYLE__BACKGROUND_COLOR:
			setBackgroundColor((ColorDefinition) null);
			return;
		case AttributePackage.STYLE__BACKGROUND_IMAGE:
			setBackgroundImage((Image) null);
			return;
		case AttributePackage.STYLE__PADDING:
			setPadding((Insets) null);
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
		case AttributePackage.STYLE__FONT:
			return font != null;
		case AttributePackage.STYLE__COLOR:
			return color != null;
		case AttributePackage.STYLE__BACKGROUND_COLOR:
			return backgroundColor != null;
		case AttributePackage.STYLE__BACKGROUND_IMAGE:
			return backgroundImage != null;
		case AttributePackage.STYLE__PADDING:
			return padding != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @param font
	 * @param color
	 * @param backcolor
	 * @param backimage
	 * @param padding
	 * @return
	 */
	public static Style create(FontDefinition font, ColorDefinition color, ColorDefinition backcolor, Image backimage,
			Insets padding) {
		Style ss = AttributeFactory.eINSTANCE.createStyle();
		ss.setFont(font);
		ss.setColor(color);
		ss.setBackgroundColor(backcolor);
		ss.setBackgroundImage(backimage);
		ss.setPadding(padding);
		return ss;
	}

	/**
	 * @generated
	 */
	@Override
	public Style copyInstance() {
		StyleImpl dest = new StyleImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Style src) {

		// children

		if (src.getFont() != null) {
			setFont(src.getFont().copyInstance());
		}

		if (src.getColor() != null) {
			setColor(src.getColor().copyInstance());
		}

		if (src.getBackgroundColor() != null) {
			setBackgroundColor(src.getBackgroundColor().copyInstance());
		}

		if (src.getBackgroundImage() != null) {
			setBackgroundImage(src.getBackgroundImage().copyInstance());
		}

		if (src.getPadding() != null) {
			setPadding(src.getPadding().copyInstance());
		}

	}

} // StyleImpl
