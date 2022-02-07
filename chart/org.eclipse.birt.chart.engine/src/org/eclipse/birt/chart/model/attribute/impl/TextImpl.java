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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Text</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl#getValue
 * <em>Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl#getFont
 * <em>Font</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl#getColor
 * <em>Color</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextImpl extends EObjectImpl implements Text {

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TextImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.TEXT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT__FONT,
					oldFont, newFont);
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
	public void setFont(FontDefinition newFont) {
		if (newFont != font) {
			NotificationChain msgs = null;
			if (font != null)
				msgs = ((InternalEObject) font).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__FONT, null, msgs);
			if (newFont != null)
				msgs = ((InternalEObject) newFont).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__FONT, null, msgs);
			msgs = basicSetFont(newFont, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT__FONT, newFont, newFont));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT__COLOR,
					oldColor, newColor);
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
	public void setColor(ColorDefinition newColor) {
		if (newColor != color) {
			NotificationChain msgs = null;
			if (color != null)
				msgs = ((InternalEObject) color).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__COLOR, null, msgs);
			if (newColor != null)
				msgs = ((InternalEObject) newColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.TEXT__COLOR, null, msgs);
			msgs = basicSetColor(newColor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.TEXT__COLOR, newColor, newColor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.TEXT__FONT:
			return basicSetFont(null, msgs);
		case AttributePackage.TEXT__COLOR:
			return basicSetColor(null, msgs);
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
		case AttributePackage.TEXT__VALUE:
			return getValue();
		case AttributePackage.TEXT__FONT:
			return getFont();
		case AttributePackage.TEXT__COLOR:
			return getColor();
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
		case AttributePackage.TEXT__VALUE:
			setValue((String) newValue);
			return;
		case AttributePackage.TEXT__FONT:
			setFont((FontDefinition) newValue);
			return;
		case AttributePackage.TEXT__COLOR:
			setColor((ColorDefinition) newValue);
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
		case AttributePackage.TEXT__VALUE:
			setValue(VALUE_EDEFAULT);
			return;
		case AttributePackage.TEXT__FONT:
			setFont((FontDefinition) null);
			return;
		case AttributePackage.TEXT__COLOR:
			setColor((ColorDefinition) null);
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
		case AttributePackage.TEXT__VALUE:
			return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
		case AttributePackage.TEXT__FONT:
			return font != null;
		case AttributePackage.TEXT__COLOR:
			return color != null;
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
		result.append(" (value: "); //$NON-NLS-1$
		result.append(value);
		result.append(')');
		return result.toString();
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initialize() {
		// only initialize empty font here.
		setFont(FontDefinitionImpl.createEmpty());
	}

	/**
	 * A convenience method provided to create a Text instance
	 * 
	 * @param sValue
	 */
	public static final Text create(String sValue) {
		final Text tx = AttributeFactory.eINSTANCE.createText();
		((TextImpl) tx).initialize();
		if (sValue == null) {
			sValue = IConstants.UNDEFINED_STRING;
		}
		tx.setValue(sValue);
		return tx;
	}

	/**
	 * A convenience method provided to create a Text instance
	 * 
	 * @param sValue
	 */
	public static final Text createDefault(String sValue) {
		final Text tx = AttributeFactory.eINSTANCE.createText();
		((TextImpl) tx).initDefault();
		tx.setValue(sValue);
		return tx;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected final void initDefault() {
		// only initialize empty font here.
		font = FontDefinitionImpl.createEmptyDefault();
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public Text copyInstance() {
		TextImpl dest = new TextImpl();

		FontDefinition tFont = getFont();
		if (tFont != null) {
			dest.font = tFont.copyInstance();
		}

		ColorDefinition tColor = getColor();
		if (tColor != null) {
			dest.color = tColor.copyInstance();
		}

		dest.value = getValue();
		return dest;
	}

	protected void set(Text src) {
		if (src.getFont() != null) {
			setFont(src.getFont().copyInstance());
		}

		if (src.getColor() != null) {
			setColor(src.getColor().copyInstance());
		}

		value = src.getValue();
	}

} // TextImpl
