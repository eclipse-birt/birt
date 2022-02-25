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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Palette</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl#getName
 * <em>Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl#getEntries
 * <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PaletteImpl extends EObjectImpl implements Palette {

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<Fill> entries;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/model.attribute.impl"); //$NON-NLS-1$

	private static List colorLib = new ArrayList(32);
	static {
		colorLib.add(ColorDefinitionImpl.create(80, 166, 218));
		colorLib.add(ColorDefinitionImpl.create(242, 88, 106));
		colorLib.add(ColorDefinitionImpl.create(232, 172, 57));
		colorLib.add(ColorDefinitionImpl.create(128, 255, 128));
		colorLib.add(ColorDefinitionImpl.create(64, 128, 128));
		colorLib.add(ColorDefinitionImpl.create(128, 128, 192));
		colorLib.add(ColorDefinitionImpl.create(170, 85, 85));
		colorLib.add(ColorDefinitionImpl.create(128, 128, 0));

		colorLib.add(ColorDefinitionImpl.create(192, 192, 192));
		colorLib.add(ColorDefinitionImpl.create(255, 255, 128));
		colorLib.add(ColorDefinitionImpl.create(128, 192, 128));
		colorLib.add(ColorDefinitionImpl.create(7, 146, 94));
		colorLib.add(ColorDefinitionImpl.create(0, 128, 255));
		colorLib.add(ColorDefinitionImpl.create(255, 128, 192));
		colorLib.add(ColorDefinitionImpl.create(0, 255, 255));
		colorLib.add(ColorDefinitionImpl.create(255, 128, 128));

		colorLib.add(ColorDefinitionImpl.create(0, 128, 192));
		colorLib.add(ColorDefinitionImpl.create(128, 128, 192));
		colorLib.add(ColorDefinitionImpl.create(255, 0, 255));
		colorLib.add(ColorDefinitionImpl.create(128, 64, 64));
		colorLib.add(ColorDefinitionImpl.create(255, 128, 64));
		colorLib.add(ColorDefinitionImpl.create(80, 240, 120));
		colorLib.add(ColorDefinitionImpl.create(0, 64, 128));
		colorLib.add(ColorDefinitionImpl.create(128, 0, 64));

		colorLib.add(ColorDefinitionImpl.create(255, 0, 128));
		colorLib.add(ColorDefinitionImpl.create(128, 128, 64));
		colorLib.add(ColorDefinitionImpl.create(128, 128, 128));
		colorLib.add(ColorDefinitionImpl.create(255, 128, 255));
		colorLib.add(ColorDefinitionImpl.create(0, 64, 0));
		colorLib.add(ColorDefinitionImpl.create(0, 0, 0));
		colorLib.add(ColorDefinitionImpl.create(255, 255, 255));
		colorLib.add(ColorDefinitionImpl.create(255, 128, 0));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected PaletteImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.PALETTE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.PALETTE__NAME, oldName, name));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<Fill> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<>(Fill.class, this, AttributePackage.PALETTE__ENTRIES);
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.PALETTE__ENTRIES:
			return ((InternalEList<?>) getEntries()).basicRemove(otherEnd, msgs);
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
		case AttributePackage.PALETTE__NAME:
			return getName();
		case AttributePackage.PALETTE__ENTRIES:
			return getEntries();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AttributePackage.PALETTE__NAME:
			setName((String) newValue);
			return;
		case AttributePackage.PALETTE__ENTRIES:
			getEntries().clear();
			getEntries().addAll((Collection<? extends Fill>) newValue);
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
		case AttributePackage.PALETTE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case AttributePackage.PALETTE__ENTRIES:
			getEntries().clear();
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
		case AttributePackage.PALETTE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case AttributePackage.PALETTE__ENTRIES:
			return entries != null && !entries.isEmpty();
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
		result.append(" (name: "); //$NON-NLS-1$
		result.append(name);
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to create an empty or pre-initialized palette
	 *
	 * NOTE: Manually written
	 *
	 * @param bEmpty
	 */
	public static final Palette create(int iIndex, boolean bEmpty) {
		final Palette p = AttributeFactory.eINSTANCE.createPalette();

		if (!bEmpty) {
			p.shift(iIndex);
		}
		return p;
	}

	/**
	 * A convenience method provided to create a palette with a single color entry
	 *
	 * NOTE: Manually written
	 *
	 * @param f
	 */
	public static final Palette create(Fill f) {
		final Palette p = AttributeFactory.eINSTANCE.createPalette();
		p.getEntries().add(f);
		return p;
	}

	/**
	 * Shift the list content from tail to head.
	 *
	 * @param lst
	 * @param pos
	 */
	private static final void shiftList(final List lst, int pos) {
		int size = lst.size();

		if (pos < 1) {
			pos = 0;
		}

		if (pos >= size) {
			pos = pos % size;
		}

		if (pos == 0) {
			return;
		}

		Object[] array = lst.toArray();

		lst.clear();

		for (int i = pos; i < array.length; i++) {
			lst.add(array[i]);
		}

		for (int i = 0; i < pos; i++) {
			lst.add(array[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.attribute.Palette#update(int)
	 */
	@Override
	public final void update(int iIndex) {
		final EList el = getEntries();
		el.clear();
		if (iIndex < 0) {
			// a rotation version of palette-0, rataion pos is the negatvie
			// index.
			ArrayList al = new ArrayList();

			al.add(ColorDefinitionImpl.create(80, 166, 218));
			al.add(ColorDefinitionImpl.create(242, 88, 106));
			al.add(ColorDefinitionImpl.create(232, 172, 57));
			al.add(ColorDefinitionImpl.create(128, 255, 128));
			al.add(ColorDefinitionImpl.create(64, 128, 128));
			al.add(ColorDefinitionImpl.create(128, 128, 192));
			al.add(ColorDefinitionImpl.create(170, 85, 85));
			al.add(ColorDefinitionImpl.create(128, 128, 0));

			shiftList(al, -iIndex);

			el.addAll(al);
		} else if (iIndex == 0) {
			el.add(ColorDefinitionImpl.create(80, 166, 218));
			el.add(ColorDefinitionImpl.create(242, 88, 106));
			el.add(ColorDefinitionImpl.create(232, 172, 57));
			el.add(ColorDefinitionImpl.create(128, 255, 128));
			el.add(ColorDefinitionImpl.create(64, 128, 128));
			el.add(ColorDefinitionImpl.create(128, 128, 192));
			el.add(ColorDefinitionImpl.create(170, 85, 85));
			el.add(ColorDefinitionImpl.create(128, 128, 0));
		} else if (iIndex == 1) {
			el.add(ColorDefinitionImpl.create(225, 225, 255));
			el.add(ColorDefinitionImpl.create(223, 197, 41));
			el.add(ColorDefinitionImpl.create(249, 225, 191));
			el.add(ColorDefinitionImpl.create(255, 205, 225));
			el.add(ColorDefinitionImpl.create(225, 255, 225));
			el.add(ColorDefinitionImpl.create(255, 191, 255));
			el.add(ColorDefinitionImpl.create(185, 185, 221));
			el.add(ColorDefinitionImpl.create(40, 255, 148));
		} else {
			logger.log(ILogger.WARNING, Messages.getString("error.unknown.palette", //$NON-NLS-1$
					new Object[] { Integer.valueOf(iIndex) }, ULocale.getDefault()));
			update(0);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.attribute.Palette#update(org.eclipse.birt.chart.
	 * model.attribute.Fill)
	 */
	@Override
	public final void update(Fill f) {
		final EList el = getEntries();
		el.clear();
		el.add(f);
	}

	@Override
	public void shift(int step) {
		shift(step, colorLib.size());
	}

	@Override
	public void shift(int step, int size) {
		if (size <= 0 || size > colorLib.size()) {
			size = colorLib.size();
		}

		final EList<Fill> el = getEntries();
		el.clear();

		if (step == 0 || Math.abs(step) >= size) {
			// Do nothing
			step = 0;
		} else if (step < 0) {
			// Move to the left side
			step = -step;
		} else if (step > 0) {
			// Move to the right side
			step = size - step;
		}

		for (int i = step; i < size; i++) {
			el.add(((ColorDefinition) colorLib.get(i)).copyInstance());
		}
		for (int i = 0; i < step; i++) {
			el.add(((ColorDefinition) colorLib.get(i)).copyInstance());
		}
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	@Override
	public Palette copyInstance() {
		PaletteImpl dest = new PaletteImpl();
		dest.set(this);
		return dest;
	}

	protected void set(Palette src) {
		if (src.getEntries() != null) {
			EList<Fill> list = getEntries();
			for (Fill element : src.getEntries()) {
				list.add(element.copyInstance());
			}
		}
		name = src.getName();
	}

} // PaletteImpl
