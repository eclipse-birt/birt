/**
 * <copyright>
 * </copyright>
 *
 * $Id: DynamicListImpl.java,v 1.1.2.1 2010/11/29 06:23:52 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model.impl;

import org.eclipse.birt.report.model.adapter.oda.model.DynamicList;
import org.eclipse.birt.report.model.adapter.oda.model.ModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dynamic
 * List</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl#getDataSetName
 * <em>Data Set Name</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl#getEnabled
 * <em>Enabled</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl#getLabelColumn
 * <em>Label Column</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl#getValueColumn
 * <em>Value Column</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DynamicListImpl extends EObjectImpl implements DynamicList {
	/**
	 * The default value of the '{@link #getDataSetName() <em>Data Set Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDataSetName()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_SET_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataSetName() <em>Data Set Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDataSetName()
	 * @generated
	 * @ordered
	 */
	protected String dataSetName = DATA_SET_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final String ENABLED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEnabled()
	 * @generated
	 * @ordered
	 */
	protected String enabled = ENABLED_EDEFAULT;

	/**
	 * The default value of the '{@link #getLabelColumn() <em>Label Column</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelColumn()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_COLUMN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabelColumn() <em>Label Column</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelColumn()
	 * @generated
	 * @ordered
	 */
	protected String labelColumn = LABEL_COLUMN_EDEFAULT;

	/**
	 * The default value of the '{@link #getValueColumn() <em>Value Column</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValueColumn()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_COLUMN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValueColumn() <em>Value Column</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValueColumn()
	 * @generated
	 * @ordered
	 */
	protected String valueColumn = VALUE_COLUMN_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DynamicListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DYNAMIC_LIST;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDataSetName() {
		return dataSetName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDataSetName(String newDataSetName) {
		String oldDataSetName = dataSetName;
		dataSetName = newDataSetName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DYNAMIC_LIST__DATA_SET_NAME,
					oldDataSetName, dataSetName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEnabled(String newEnabled) {
		String oldEnabled = enabled;
		enabled = newEnabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DYNAMIC_LIST__ENABLED, oldEnabled,
					enabled));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLabelColumn() {
		return labelColumn;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelColumn(String newLabelColumn) {
		String oldLabelColumn = labelColumn;
		labelColumn = newLabelColumn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DYNAMIC_LIST__LABEL_COLUMN,
					oldLabelColumn, labelColumn));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getValueColumn() {
		return valueColumn;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValueColumn(String newValueColumn) {
		String oldValueColumn = valueColumn;
		valueColumn = newValueColumn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DYNAMIC_LIST__VALUE_COLUMN,
					oldValueColumn, valueColumn));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelPackage.DYNAMIC_LIST__DATA_SET_NAME:
			return getDataSetName();
		case ModelPackage.DYNAMIC_LIST__ENABLED:
			return getEnabled();
		case ModelPackage.DYNAMIC_LIST__LABEL_COLUMN:
			return getLabelColumn();
		case ModelPackage.DYNAMIC_LIST__VALUE_COLUMN:
			return getValueColumn();
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
		case ModelPackage.DYNAMIC_LIST__DATA_SET_NAME:
			setDataSetName((String) newValue);
			return;
		case ModelPackage.DYNAMIC_LIST__ENABLED:
			setEnabled((String) newValue);
			return;
		case ModelPackage.DYNAMIC_LIST__LABEL_COLUMN:
			setLabelColumn((String) newValue);
			return;
		case ModelPackage.DYNAMIC_LIST__VALUE_COLUMN:
			setValueColumn((String) newValue);
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
		case ModelPackage.DYNAMIC_LIST__DATA_SET_NAME:
			setDataSetName(DATA_SET_NAME_EDEFAULT);
			return;
		case ModelPackage.DYNAMIC_LIST__ENABLED:
			setEnabled(ENABLED_EDEFAULT);
			return;
		case ModelPackage.DYNAMIC_LIST__LABEL_COLUMN:
			setLabelColumn(LABEL_COLUMN_EDEFAULT);
			return;
		case ModelPackage.DYNAMIC_LIST__VALUE_COLUMN:
			setValueColumn(VALUE_COLUMN_EDEFAULT);
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
		case ModelPackage.DYNAMIC_LIST__DATA_SET_NAME:
			return DATA_SET_NAME_EDEFAULT == null ? dataSetName != null : !DATA_SET_NAME_EDEFAULT.equals(dataSetName);
		case ModelPackage.DYNAMIC_LIST__ENABLED:
			return ENABLED_EDEFAULT == null ? enabled != null : !ENABLED_EDEFAULT.equals(enabled);
		case ModelPackage.DYNAMIC_LIST__LABEL_COLUMN:
			return LABEL_COLUMN_EDEFAULT == null ? labelColumn != null : !LABEL_COLUMN_EDEFAULT.equals(labelColumn);
		case ModelPackage.DYNAMIC_LIST__VALUE_COLUMN:
			return VALUE_COLUMN_EDEFAULT == null ? valueColumn != null : !VALUE_COLUMN_EDEFAULT.equals(valueColumn);
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
		result.append(" (dataSetName: ");
		result.append(dataSetName);
		result.append(", enabled: ");
		result.append(enabled);
		result.append(", labelColumn: ");
		result.append(labelColumn);
		result.append(", valueColumn: ");
		result.append(valueColumn);
		result.append(')');
		return result.toString();
	}

} // DynamicListImpl
