/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentRootImpl.java,v 1.1.28.1 2010/11/29 06:23:52 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model.impl;

import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter;
import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot;
import org.eclipse.birt.report.model.adapter.oda.model.DynamicList;
import org.eclipse.birt.report.model.adapter.oda.model.ModelPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Document Root</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getXMLNSPrefixMap
 * <em>XMLNS Prefix Map</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getXSISchemaLocation
 * <em>XSI Schema Location</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getDataSetParameter
 * <em>Data Set Parameter</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getDataSetParameters
 * <em>Data Set Parameters</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getDesignValues
 * <em>Design Values</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl#getDynamicList
 * <em>Dynamic List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mixed;

	/**
	 * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix
	 * Map</em>}' map. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xMLNSPrefixMap;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema
	 * Location</em>}' map. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xSISchemaLocation;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, ModelPackage.DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, String> getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap<String, String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this, ModelPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, String> getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap<String, String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this, ModelPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataSetParameter getDataSetParameter() {
		return (DataSetParameter) getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__DATA_SET_PARAMETER, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDataSetParameter(DataSetParameter newDataSetParameter, NotificationChain msgs) {
		return ((FeatureMap.Internal) getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__DATA_SET_PARAMETER,
				newDataSetParameter, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDataSetParameter(DataSetParameter newDataSetParameter) {
		((FeatureMap.Internal) getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__DATA_SET_PARAMETER,
				newDataSetParameter);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataSetParameters getDataSetParameters() {
		return (DataSetParameters) getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__DATA_SET_PARAMETERS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDataSetParameters(DataSetParameters newDataSetParameters, NotificationChain msgs) {
		return ((FeatureMap.Internal) getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__DATA_SET_PARAMETERS,
				newDataSetParameters, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDataSetParameters(DataSetParameters newDataSetParameters) {
		((FeatureMap.Internal) getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__DATA_SET_PARAMETERS,
				newDataSetParameters);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DesignValues getDesignValues() {
		return (DesignValues) getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__DESIGN_VALUES, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDesignValues(DesignValues newDesignValues, NotificationChain msgs) {
		return ((FeatureMap.Internal) getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__DESIGN_VALUES,
				newDesignValues, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDesignValues(DesignValues newDesignValues) {
		((FeatureMap.Internal) getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__DESIGN_VALUES, newDesignValues);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DynamicList getDynamicList() {
		return (DynamicList) getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__DYNAMIC_LIST, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDynamicList(DynamicList newDynamicList, NotificationChain msgs) {
		return ((FeatureMap.Internal) getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__DYNAMIC_LIST,
				newDynamicList, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDynamicList(DynamicList newDynamicList) {
		((FeatureMap.Internal) getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__DYNAMIC_LIST, newDynamicList);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.DOCUMENT_ROOT__MIXED:
			return ((InternalEList<?>) getMixed()).basicRemove(otherEnd, msgs);
		case ModelPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			return ((InternalEList<?>) getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
		case ModelPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			return ((InternalEList<?>) getXSISchemaLocation()).basicRemove(otherEnd, msgs);
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETER:
			return basicSetDataSetParameter(null, msgs);
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETERS:
			return basicSetDataSetParameters(null, msgs);
		case ModelPackage.DOCUMENT_ROOT__DESIGN_VALUES:
			return basicSetDesignValues(null, msgs);
		case ModelPackage.DOCUMENT_ROOT__DYNAMIC_LIST:
			return basicSetDynamicList(null, msgs);
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
		case ModelPackage.DOCUMENT_ROOT__MIXED:
			if (coreType)
				return getMixed();
			return ((FeatureMap.Internal) getMixed()).getWrapper();
		case ModelPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			if (coreType)
				return getXMLNSPrefixMap();
			else
				return getXMLNSPrefixMap().map();
		case ModelPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			if (coreType)
				return getXSISchemaLocation();
			else
				return getXSISchemaLocation().map();
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETER:
			return getDataSetParameter();
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETERS:
			return getDataSetParameters();
		case ModelPackage.DOCUMENT_ROOT__DESIGN_VALUES:
			return getDesignValues();
		case ModelPackage.DOCUMENT_ROOT__DYNAMIC_LIST:
			return getDynamicList();
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
		case ModelPackage.DOCUMENT_ROOT__MIXED:
			((FeatureMap.Internal) getMixed()).set(newValue);
			return;
		case ModelPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			((EStructuralFeature.Setting) getXMLNSPrefixMap()).set(newValue);
			return;
		case ModelPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			((EStructuralFeature.Setting) getXSISchemaLocation()).set(newValue);
			return;
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETER:
			setDataSetParameter((DataSetParameter) newValue);
			return;
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETERS:
			setDataSetParameters((DataSetParameters) newValue);
			return;
		case ModelPackage.DOCUMENT_ROOT__DESIGN_VALUES:
			setDesignValues((DesignValues) newValue);
			return;
		case ModelPackage.DOCUMENT_ROOT__DYNAMIC_LIST:
			setDynamicList((DynamicList) newValue);
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
		case ModelPackage.DOCUMENT_ROOT__MIXED:
			getMixed().clear();
			return;
		case ModelPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			getXMLNSPrefixMap().clear();
			return;
		case ModelPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			getXSISchemaLocation().clear();
			return;
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETER:
			setDataSetParameter((DataSetParameter) null);
			return;
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETERS:
			setDataSetParameters((DataSetParameters) null);
			return;
		case ModelPackage.DOCUMENT_ROOT__DESIGN_VALUES:
			setDesignValues((DesignValues) null);
			return;
		case ModelPackage.DOCUMENT_ROOT__DYNAMIC_LIST:
			setDynamicList((DynamicList) null);
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
		case ModelPackage.DOCUMENT_ROOT__MIXED:
			return mixed != null && !mixed.isEmpty();
		case ModelPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
		case ModelPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETER:
			return getDataSetParameter() != null;
		case ModelPackage.DOCUMENT_ROOT__DATA_SET_PARAMETERS:
			return getDataSetParameters() != null;
		case ModelPackage.DOCUMENT_ROOT__DESIGN_VALUES:
			return getDesignValues() != null;
		case ModelPackage.DOCUMENT_ROOT__DYNAMIC_LIST:
			return getDynamicList() != null;
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
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} // DocumentRootImpl
