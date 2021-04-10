/**
 * <copyright>
 * </copyright>
 *
 * $Id: DesignValuesImpl.java,v 1.1.28.1 2010/11/29 06:23:52 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model.impl;

import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelPackage;

import org.eclipse.datatools.connectivity.oda.design.ResultSets;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Design
 * Values</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl#getVersion
 * <em>Version</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl#getDataSetParameters
 * <em>Data Set Parameters</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl#getDataSetParameters1
 * <em>Data Set Parameters1</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl#getResultSets
 * <em>Result Sets</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DesignValuesImpl extends EObjectImpl implements DesignValues {
	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "2.0";

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * This is true if the Version attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean versionESet;

	/**
	 * The cached value of the '{@link #getDataSetParameters() <em>Data Set
	 * Parameters</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getDataSetParameters()
	 * @generated
	 * @ordered
	 */
	protected DataSetParameters dataSetParameters;

	/**
	 * The cached value of the '{@link #getDataSetParameters1() <em>Data Set
	 * Parameters1</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getDataSetParameters1()
	 * @generated
	 * @ordered
	 */
	protected org.eclipse.datatools.connectivity.oda.design.DataSetParameters dataSetParameters1;

	/**
	 * The cached value of the '{@link #getResultSets() <em>Result Sets</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getResultSets()
	 * @generated
	 * @ordered
	 */
	protected ResultSets resultSets;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DesignValuesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DESIGN_VALUES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DESIGN_VALUES__VERSION, oldVersion,
					version, !oldVersionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.DESIGN_VALUES__VERSION, oldVersion,
					VERSION_EDEFAULT, oldVersionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetVersion() {
		return versionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataSetParameters getDataSetParameters() {
		return dataSetParameters;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDataSetParameters(DataSetParameters newDataSetParameters, NotificationChain msgs) {
		DataSetParameters oldDataSetParameters = dataSetParameters;
		dataSetParameters = newDataSetParameters;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS, oldDataSetParameters, newDataSetParameters);
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
	public void setDataSetParameters(DataSetParameters newDataSetParameters) {
		if (newDataSetParameters != dataSetParameters) {
			NotificationChain msgs = null;
			if (dataSetParameters != null)
				msgs = ((InternalEObject) dataSetParameters).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS, null, msgs);
			if (newDataSetParameters != null)
				msgs = ((InternalEObject) newDataSetParameters).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS, null, msgs);
			msgs = basicSetDataSetParameters(newDataSetParameters, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS,
					newDataSetParameters, newDataSetParameters));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResultSets getResultSets() {
		return resultSets;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetResultSets(ResultSets newResultSets, NotificationChain msgs) {
		ResultSets oldResultSets = resultSets;
		resultSets = newResultSets;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.DESIGN_VALUES__RESULT_SETS, oldResultSets, newResultSets);
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
	public void setResultSets(ResultSets newResultSets) {
		if (newResultSets != resultSets) {
			NotificationChain msgs = null;
			if (resultSets != null)
				msgs = ((InternalEObject) resultSets).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DESIGN_VALUES__RESULT_SETS, null, msgs);
			if (newResultSets != null)
				msgs = ((InternalEObject) newResultSets).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DESIGN_VALUES__RESULT_SETS, null, msgs);
			msgs = basicSetResultSets(newResultSets, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DESIGN_VALUES__RESULT_SETS,
					newResultSets, newResultSets));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public org.eclipse.datatools.connectivity.oda.design.DataSetParameters getDataSetParameters1() {
		return dataSetParameters1;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDataSetParameters1(
			org.eclipse.datatools.connectivity.oda.design.DataSetParameters newDataSetParameters1,
			NotificationChain msgs) {
		org.eclipse.datatools.connectivity.oda.design.DataSetParameters oldDataSetParameters1 = dataSetParameters1;
		dataSetParameters1 = newDataSetParameters1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1, oldDataSetParameters1, newDataSetParameters1);
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
	public void setDataSetParameters1(
			org.eclipse.datatools.connectivity.oda.design.DataSetParameters newDataSetParameters1) {
		if (newDataSetParameters1 != dataSetParameters1) {
			NotificationChain msgs = null;
			if (dataSetParameters1 != null)
				msgs = ((InternalEObject) dataSetParameters1).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1, null, msgs);
			if (newDataSetParameters1 != null)
				msgs = ((InternalEObject) newDataSetParameters1).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1, null, msgs);
			msgs = basicSetDataSetParameters1(newDataSetParameters1, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1,
					newDataSetParameters1, newDataSetParameters1));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS:
			return basicSetDataSetParameters(null, msgs);
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1:
			return basicSetDataSetParameters1(null, msgs);
		case ModelPackage.DESIGN_VALUES__RESULT_SETS:
			return basicSetResultSets(null, msgs);
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
		case ModelPackage.DESIGN_VALUES__VERSION:
			return getVersion();
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS:
			return getDataSetParameters();
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1:
			return getDataSetParameters1();
		case ModelPackage.DESIGN_VALUES__RESULT_SETS:
			return getResultSets();
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
		case ModelPackage.DESIGN_VALUES__VERSION:
			setVersion((String) newValue);
			return;
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS:
			setDataSetParameters((DataSetParameters) newValue);
			return;
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1:
			setDataSetParameters1((org.eclipse.datatools.connectivity.oda.design.DataSetParameters) newValue);
			return;
		case ModelPackage.DESIGN_VALUES__RESULT_SETS:
			setResultSets((ResultSets) newValue);
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
		case ModelPackage.DESIGN_VALUES__VERSION:
			unsetVersion();
			return;
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS:
			setDataSetParameters((DataSetParameters) null);
			return;
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1:
			setDataSetParameters1((org.eclipse.datatools.connectivity.oda.design.DataSetParameters) null);
			return;
		case ModelPackage.DESIGN_VALUES__RESULT_SETS:
			setResultSets((ResultSets) null);
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
		case ModelPackage.DESIGN_VALUES__VERSION:
			return isSetVersion();
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS:
			return dataSetParameters != null;
		case ModelPackage.DESIGN_VALUES__DATA_SET_PARAMETERS1:
			return dataSetParameters1 != null;
		case ModelPackage.DESIGN_VALUES__RESULT_SETS:
			return resultSets != null;
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
		result.append(" (version: ");
		if (versionESet)
			result.append(version);
		else
			result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // DesignValuesImpl
