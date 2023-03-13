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
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Color
 * Definition</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl#getTransparency
 * <em>Transparency</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl#getRed
 * <em>Red</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl#getGreen
 * <em>Green</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl#getBlue
 * <em>Blue</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ColorDefinitionImpl extends FillImpl implements ColorDefinition {

	/**
	 * The default value of the ' {@link #getTransparency() <em>Transparency</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTransparency()
	 * @generated
	 * @ordered
	 */
	protected static final int TRANSPARENCY_EDEFAULT = 255;

	/**
	 * The cached value of the ' {@link #getTransparency() <em>Transparency</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTransparency()
	 * @generated
	 * @ordered
	 */
	protected int transparency = TRANSPARENCY_EDEFAULT;

	/**
	 * This is true if the Transparency attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean transparencyESet;

	/**
	 * The default value of the '{@link #getRed() <em>Red</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getRed()
	 * @generated
	 * @ordered
	 */
	protected static final int RED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRed() <em>Red</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getRed()
	 * @generated
	 * @ordered
	 */
	protected int red = RED_EDEFAULT;

	/**
	 * This is true if the Red attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean redESet;

	/**
	 * The default value of the '{@link #getGreen() <em>Green</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getGreen()
	 * @generated
	 * @ordered
	 */
	protected static final int GREEN_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getGreen() <em>Green</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getGreen()
	 * @generated
	 * @ordered
	 */
	protected int green = GREEN_EDEFAULT;

	/**
	 * This is true if the Green attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean greenESet;

	/**
	 * The default value of the '{@link #getBlue() <em>Blue</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getBlue()
	 * @generated
	 * @ordered
	 */
	protected static final int BLUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBlue() <em>Blue</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getBlue()
	 * @generated
	 * @ordered
	 */
	protected int blue = BLUE_EDEFAULT;

	/**
	 * This is true if the Blue attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean blueESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ColorDefinitionImpl() {
		super();
	}

	/**
	 * A convenient method provided to initialize member variables (without
	 * transparency)
	 *
	 * Note: Manually written
	 *
	 * @param iRed
	 * @param iGreen
	 * @param iBlue
	 */
	@Override
	public void set(int iRed, int iGreen, int iBlue) {
		setRed(iRed);
		setGreen(iGreen);
		setBlue(iBlue);
		setTransparency(255);
	}

	/**
	 * A convenient method provided to initialize all member variables (including
	 * transparency)
	 *
	 * Note: Manually written
	 *
	 * @param iRed
	 * @param iGreen
	 * @param iBlue
	 * @param iAlpha
	 */
	@Override
	public void set(int iRed, int iGreen, int iBlue, int iAlpha) {
		setRed(iRed);
		setGreen(iGreen);
		setBlue(iBlue);
		setTransparency(iAlpha);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.COLOR_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getTransparency() {
		return transparency;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setTransparency(int newTransparency) {
		int oldTransparency = transparency;
		transparency = newTransparency;
		boolean oldTransparencyESet = transparencyESet;
		transparencyESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.COLOR_DEFINITION__TRANSPARENCY,
					oldTransparency, transparency, !oldTransparencyESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetTransparency() {
		int oldTransparency = transparency;
		boolean oldTransparencyESet = transparencyESet;
		transparency = TRANSPARENCY_EDEFAULT;
		transparencyESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.COLOR_DEFINITION__TRANSPARENCY,
					oldTransparency, TRANSPARENCY_EDEFAULT, oldTransparencyESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetTransparency() {
		return transparencyESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getRed() {
		return red;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRed(int newRed) {
		int oldRed = red;
		red = newRed;
		boolean oldRedESet = redESet;
		redESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.COLOR_DEFINITION__RED, oldRed, red,
					!oldRedESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetRed() {
		int oldRed = red;
		boolean oldRedESet = redESet;
		red = RED_EDEFAULT;
		redESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.COLOR_DEFINITION__RED, oldRed,
					RED_EDEFAULT, oldRedESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetRed() {
		return redESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getBlue() {
		return blue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setBlue(int newBlue) {
		int oldBlue = blue;
		blue = newBlue;
		boolean oldBlueESet = blueESet;
		blueESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.COLOR_DEFINITION__BLUE, oldBlue,
					blue, !oldBlueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetBlue() {
		int oldBlue = blue;
		boolean oldBlueESet = blueESet;
		blue = BLUE_EDEFAULT;
		blueESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.COLOR_DEFINITION__BLUE, oldBlue,
					BLUE_EDEFAULT, oldBlueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetBlue() {
		return blueESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.COLOR_DEFINITION__TRANSPARENCY:
			return getTransparency();
		case AttributePackage.COLOR_DEFINITION__RED:
			return getRed();
		case AttributePackage.COLOR_DEFINITION__GREEN:
			return getGreen();
		case AttributePackage.COLOR_DEFINITION__BLUE:
			return getBlue();
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
		case AttributePackage.COLOR_DEFINITION__TRANSPARENCY:
			setTransparency((Integer) newValue);
			return;
		case AttributePackage.COLOR_DEFINITION__RED:
			setRed((Integer) newValue);
			return;
		case AttributePackage.COLOR_DEFINITION__GREEN:
			setGreen((Integer) newValue);
			return;
		case AttributePackage.COLOR_DEFINITION__BLUE:
			setBlue((Integer) newValue);
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
		case AttributePackage.COLOR_DEFINITION__TRANSPARENCY:
			unsetTransparency();
			return;
		case AttributePackage.COLOR_DEFINITION__RED:
			unsetRed();
			return;
		case AttributePackage.COLOR_DEFINITION__GREEN:
			unsetGreen();
			return;
		case AttributePackage.COLOR_DEFINITION__BLUE:
			unsetBlue();
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
		case AttributePackage.COLOR_DEFINITION__TRANSPARENCY:
			return isSetTransparency();
		case AttributePackage.COLOR_DEFINITION__RED:
			return isSetRed();
		case AttributePackage.COLOR_DEFINITION__GREEN:
			return isSetGreen();
		case AttributePackage.COLOR_DEFINITION__BLUE:
			return isSetBlue();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getGreen() {
		return green;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setGreen(int newGreen) {
		int oldGreen = green;
		green = newGreen;
		boolean oldGreenESet = greenESet;
		greenESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.COLOR_DEFINITION__GREEN, oldGreen,
					green, !oldGreenESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetGreen() {
		int oldGreen = green;
		boolean oldGreenESet = greenESet;
		green = GREEN_EDEFAULT;
		greenESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.COLOR_DEFINITION__GREEN, oldGreen,
					GREEN_EDEFAULT, oldGreenESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetGreen() {
		return greenESet;
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
		result.append(" (transparency: "); //$NON-NLS-1$
		if (transparencyESet) {
			result.append(transparency);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", red: "); //$NON-NLS-1$
		if (redESet) {
			result.append(red);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", green: "); //$NON-NLS-1$
		if (greenESet) {
			result.append(green);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", blue: "); //$NON-NLS-1$
		if (blueESet) {
			result.append(blue);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	protected void set(ColorDefinition src) {

		// attributes

		transparency = src.getTransparency();

		transparencyESet = src.isSetTransparency();

		red = src.getRed();

		redESet = src.isSetRed();

		green = src.getGreen();

		greenESet = src.isSetGreen();

		blue = src.getBlue();

		blueESet = src.isSetBlue();

	}

	/**
	 * NOTE: Manually written
	 *
	 * @param iRed
	 * @param iGreen
	 * @param iBlue
	 * @param iAlpha
	 *
	 * @return color definition instance with setting 'isSet' flag.
	 */
	public static final ColorDefinition create(int iRed, int iGreen, int iBlue, int iAlpha) {
		final ColorDefinition cd = AttributeFactory.eINSTANCE.createColorDefinition();
		cd.set(iRed, iGreen, iBlue, iAlpha);
		return cd;
	}

	/**
	 * NOTE: Manually written
	 *
	 * @param iRed
	 * @param iGreen
	 * @param iBlue
	 * @return color definition instance with setting 'isSet' flag.
	 */
	public static final ColorDefinition create(int iRed, int iGreen, int iBlue) {
		final ColorDefinition cd = AttributeFactory.eINSTANCE.createColorDefinition();
		cd.set(iRed, iGreen, iBlue);
		return cd;
	}

	/**
	 * A transparent color constant that should be used wherever applicable
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition TRANSPARENT() {
		return ColorDefinitionImpl.create(255, 255, 255, 0);
	}

	/**
	 * A light opaque red color with (r=255;g=0;b=0)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition RED() {
		return ColorDefinitionImpl.create(255, 0, 0);
	}

	/**
	 * A light opaque green color with (r=0;g=255;b=0)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition GREEN() {
		return ColorDefinitionImpl.create(0, 255, 0);
	}

	/**
	 * A light opaque blue color with (r=0;g=0;b=255)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition BLUE() {
		return ColorDefinitionImpl.create(0, 0, 255);
	}

	/**
	 * An opaque black color with (r=0;g=0;b=0)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition BLACK() {
		return ColorDefinitionImpl.create(0, 0, 0);
	}

	/**
	 * An opaque white color with (r=255;g=255;b=255)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition WHITE() {
		return ColorDefinitionImpl.create(255, 255, 255);
	}

	/**
	 * An opaque yellow color with (r=255;g=255;b=0)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition YELLOW() {
		return ColorDefinitionImpl.create(255, 255, 0);
	}

	/**
	 * An opaque cyan color with (r=225;g=225;b=255)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition CYAN() {
		return ColorDefinitionImpl.create(225, 225, 255);
	}

	/**
	 * A light opaque grey color with (r=127;g=127;b=127)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition GREY() {
		return ColorDefinitionImpl.create(127, 127, 127);
	}

	/**
	 * An opaque orange color with (r=223;g=197;b=41)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition ORANGE() {
		return ColorDefinitionImpl.create(255, 197, 41);
	}

	/**
	 * An opaque creamy color with (r=249;g=225;b=191)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition CREAM() {
		return ColorDefinitionImpl.create(249, 225, 191);
	}

	/**
	 * An opaque orange color with (r=255;g=205;b=225)
	 *
	 * Note: Manually written
	 */
	public static final ColorDefinition PINK() {
		return ColorDefinitionImpl.create(255, 205, 225);
	}

	/**
	 * Internally used NOTE: Manually written
	 */
	private static final double FACTOR = 0.7;

	/**
	 * NOTE: Manually written
	 *
	 * @return A new brighter color instance of this color
	 */
	@Override
	public final ColorDefinition brighter() {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		int a = getTransparency();

		int i = (int) (1.0 / (1.0 - FACTOR));
		if (r == 0 && g == 0 && b == 0) {
			return ColorDefinitionImpl.create(i, i, i, a);
		}
		if (r >= 0 && r < i) {
			r = i;
		}
		if (g >= 0 && g < i) {
			g = i;
		}
		if (b >= 0 && b < i) {
			b = i;
		}

		return ColorDefinitionImpl.create(Math.min((int) (r / FACTOR), 255), Math.min((int) (g / FACTOR), 255),
				Math.min((int) (b / FACTOR), 255), a);
	}

	/**
	 * NOTE: Manually written
	 *
	 * @return A new darker color instance of this color
	 */
	@Override
	public final ColorDefinition darker() {
		return ColorDefinitionImpl.create(Math.max((int) (getRed() * FACTOR), 0),
				Math.max((int) (getGreen() * FACTOR), 0), Math.max((int) (getBlue() * FACTOR), 0), getTransparency());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#translucent()
	 */
	@Override
	public final ColorDefinition translucent() {
		final ColorDefinition cd = copyInstance();
		cd.setTransparency(127);
		return cd;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#transparent()
	 */
	@Override
	public final ColorDefinition transparent() {
		final ColorDefinition cd = copyInstance();
		cd.setTransparency(0);
		return cd;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#opaque()
	 */
	@Override
	public final ColorDefinition opaque() {
		final ColorDefinition cd = copyInstance();
		cd.setTransparency(255);
		return cd;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#invert()
	 */
	@Override
	public void invert() {
		setRed(getRed() ^ 0xff);
		setGreen(getGreen() ^ 0xff);
		setBlue(getBlue() ^ 0xff);
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	@Override
	public ColorDefinition copyInstance() {
		ColorDefinitionImpl dest = new ColorDefinitionImpl();
		dest.transparency = getTransparency();
		dest.transparencyESet = isSetTransparency();
		dest.red = getRed();
		dest.redESet = isSetRed();
		dest.green = getGreen();
		dest.greenESet = isSetGreen();
		dest.blue = getBlue();
		dest.blueESet = isSetBlue();
		return dest;
	}

	@Override
	public int hashCode() {
		return Objects.hash(blue, green, red, transparency);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		ColorDefinitionImpl other = (ColorDefinitionImpl) obj;
		if (blue != other.blue) {
			return false;
		}
		if (green != other.green) {
			return false;
		}
		if (red != other.red) {
			return false;
		}
		if (transparency != other.transparency) {
			return false;
		}
		return true;
	}

} // ColorDefinitionImpl
