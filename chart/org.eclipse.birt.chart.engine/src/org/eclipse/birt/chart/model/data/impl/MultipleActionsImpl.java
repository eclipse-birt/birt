/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.data.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.impl.EStringToStringMapEntryImpl;
import org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Multiple Actions</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl#getActions
 * <em>Actions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl#getPropertiesMap
 * <em>Properties Map</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleActionsImpl extends ActionImpl implements MultipleActions {

	/**
	 * The cached value of the '{@link #getActions() <em>Actions</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getActions()
	 * @generated
	 * @ordered
	 */
	protected EList<Action> actions;

	/**
	 * The cached value of the '{@link #getPropertiesMap() <em>Properties Map</em>}'
	 * map. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPropertiesMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> propertiesMap;

	/**
	 * Returns a new instance of MultiURLValues.
	 * 
	 * @return
	 */
	public static MultipleActions create() {
		MultipleActions ma = DataFactory.eINSTANCE.createMultipleActions();
		ma.setType(ActionType.URL_REDIRECT_LITERAL);
		ma.getPropertiesMap().putAll(MultiURLValuesImpl.DEFAULT_PROPERTIES_MAP);
		return ma;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MultipleActionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.MULTIPLE_ACTIONS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Action> getActions() {
		if (actions == null) {
			actions = new EObjectContainmentEList<Action>(Action.class, this, DataPackage.MULTIPLE_ACTIONS__ACTIONS);
		}
		return actions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, String> getPropertiesMap() {
		if (propertiesMap == null) {
			propertiesMap = new EcoreEMap<String, String>(AttributePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this, DataPackage.MULTIPLE_ACTIONS__PROPERTIES_MAP);
		}
		return propertiesMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DataPackage.MULTIPLE_ACTIONS__ACTIONS:
			return ((InternalEList<?>) getActions()).basicRemove(otherEnd, msgs);
		case DataPackage.MULTIPLE_ACTIONS__PROPERTIES_MAP:
			return ((InternalEList<?>) getPropertiesMap()).basicRemove(otherEnd, msgs);
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
		case DataPackage.MULTIPLE_ACTIONS__ACTIONS:
			return getActions();
		case DataPackage.MULTIPLE_ACTIONS__PROPERTIES_MAP:
			if (coreType)
				return getPropertiesMap();
			else
				return getPropertiesMap().map();
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
		case DataPackage.MULTIPLE_ACTIONS__ACTIONS:
			getActions().clear();
			getActions().addAll((Collection<? extends Action>) newValue);
			return;
		case DataPackage.MULTIPLE_ACTIONS__PROPERTIES_MAP:
			((EStructuralFeature.Setting) getPropertiesMap()).set(newValue);
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
		case DataPackage.MULTIPLE_ACTIONS__ACTIONS:
			getActions().clear();
			return;
		case DataPackage.MULTIPLE_ACTIONS__PROPERTIES_MAP:
			getPropertiesMap().clear();
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
		case DataPackage.MULTIPLE_ACTIONS__ACTIONS:
			return actions != null && !actions.isEmpty();
		case DataPackage.MULTIPLE_ACTIONS__PROPERTIES_MAP:
			return propertiesMap != null && !propertiesMap.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated
	 */
	protected void set(MultipleActions src) {

		super.set(src);

		// children

		if (src.getActions() != null) {
			EList<Action> list = getActions();
			for (Action element : src.getActions()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getPropertiesMap() != null) {
			EMap<String, String> map = getPropertiesMap();
			for (Map.Entry<String, String> entry : src.getPropertiesMap().entrySet()) {

				map.put(entry.getKey(), entry.getValue());

			}
		}

	}

	/**
	 * @generated
	 */
	public MultipleActions copyInstance() {
		MultipleActionsImpl dest = new MultipleActionsImpl();
		dest.set(this);
		return dest;
	}

} // MultipleActionsImpl
