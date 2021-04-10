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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Data
 * Point Component</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl#getOrthogonalType
 * <em>Orthogonal Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataPointComponentImpl extends EObjectImpl implements DataPointComponent {

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final DataPointComponentType TYPE_EDEFAULT = DataPointComponentType.BASE_VALUE_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected DataPointComponentType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * The cached value of the '{@link #getFormatSpecifier() <em>Format
	 * Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier formatSpecifier;

	/**
	 * The default value of the '{@link #getOrthogonalType() <em>Orthogonal
	 * Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrthogonalType()
	 * @generated
	 * @ordered
	 */
	protected static final String ORTHOGONAL_TYPE_EDEFAULT = ""; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getOrthogonalType() <em>Orthogonal
	 * Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrthogonalType()
	 * @generated
	 * @ordered
	 */
	protected String orthogonalType = ORTHOGONAL_TYPE_EDEFAULT;

	/**
	 * This is true if the Orthogonal Type attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean orthogonalTypeESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DataPointComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.DATA_POINT_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataPointComponentType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(DataPointComponentType newType) {
		DataPointComponentType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.DATA_POINT_COMPONENT__TYPE, oldType,
					type, !oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetType() {
		DataPointComponentType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.DATA_POINT_COMPONENT__TYPE,
					oldType, TYPE_EDEFAULT, oldTypeESet));
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
	public FormatSpecifier getFormatSpecifier() {
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs) {
		FormatSpecifier oldFormatSpecifier = formatSpecifier;
		formatSpecifier = newFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
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
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null)
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER, null, msgs);
			if (newFormatSpecifier != null)
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER, null, msgs);
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER, newFormatSpecifier, newFormatSpecifier));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getOrthogonalType() {
		return orthogonalType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOrthogonalType(String newOrthogonalType) {
		String oldOrthogonalType = orthogonalType;
		orthogonalType = newOrthogonalType;
		boolean oldOrthogonalTypeESet = orthogonalTypeESet;
		orthogonalTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.DATA_POINT_COMPONENT__ORTHOGONAL_TYPE, oldOrthogonalType, orthogonalType,
					!oldOrthogonalTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetOrthogonalType() {
		String oldOrthogonalType = orthogonalType;
		boolean oldOrthogonalTypeESet = orthogonalTypeESet;
		orthogonalType = ORTHOGONAL_TYPE_EDEFAULT;
		orthogonalTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.DATA_POINT_COMPONENT__ORTHOGONAL_TYPE, oldOrthogonalType, ORTHOGONAL_TYPE_EDEFAULT,
					oldOrthogonalTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetOrthogonalType() {
		return orthogonalTypeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
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
		case AttributePackage.DATA_POINT_COMPONENT__TYPE:
			return getType();
		case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER:
			return getFormatSpecifier();
		case AttributePackage.DATA_POINT_COMPONENT__ORTHOGONAL_TYPE:
			return getOrthogonalType();
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
		case AttributePackage.DATA_POINT_COMPONENT__TYPE:
			setType((DataPointComponentType) newValue);
			return;
		case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
			return;
		case AttributePackage.DATA_POINT_COMPONENT__ORTHOGONAL_TYPE:
			setOrthogonalType((String) newValue);
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
		case AttributePackage.DATA_POINT_COMPONENT__TYPE:
			unsetType();
			return;
		case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
			return;
		case AttributePackage.DATA_POINT_COMPONENT__ORTHOGONAL_TYPE:
			unsetOrthogonalType();
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
		case AttributePackage.DATA_POINT_COMPONENT__TYPE:
			return isSetType();
		case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER:
			return formatSpecifier != null;
		case AttributePackage.DATA_POINT_COMPONENT__ORTHOGONAL_TYPE:
			return isSetOrthogonalType();
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
		result.append(", orthogonalType: "); //$NON-NLS-1$
		if (orthogonalTypeESet)
			result.append(orthogonalType);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience methods provided to create an initialized DataPointComponent
	 * instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @param dpct
	 * @param fs
	 * @return
	 */
	public static final DataPointComponent create(DataPointComponentType dpct, FormatSpecifier fs) {
		final DataPointComponent dpc = AttributeFactory.eINSTANCE.createDataPointComponent();
		dpc.setFormatSpecifier(fs);
		dpc.setType(dpct);
		return dpc;
	}

	/**
	 * @generated
	 */
	public DataPointComponent copyInstance() {
		DataPointComponentImpl dest = new DataPointComponentImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(DataPointComponent src) {

		// children

		if (src.getFormatSpecifier() != null) {
			setFormatSpecifier(src.getFormatSpecifier().copyInstance());
		}

		// attributes

		type = src.getType();

		typeESet = src.isSetType();

		orthogonalType = src.getOrthogonalType();

		orthogonalTypeESet = src.isSetOrthogonalType();

	}

} // DataPointComponentImpl
