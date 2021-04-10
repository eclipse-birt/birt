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

package org.eclipse.birt.chart.model.util;

import java.util.List;

import org.eclipse.birt.chart.model.*;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Switch </b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}to
 * invoke the <code>caseXXX</code> method for each class of the model, starting
 * with the actual class of the object and proceeding up the inheritance
 * hierarchy until a non-null result is returned, which is the result of the
 * switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.ModelPackage
 * @generated
 */
public class ModelSwitch<T> {

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ModelPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public ModelSwitch() {
		if (modelPackage == null) {
			modelPackage = ModelPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a
	 * non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a
	 * non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		} else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a
	 * non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case ModelPackage.CHART: {
			Chart chart = (Chart) theEObject;
			T result = caseChart(chart);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelPackage.CHART_WITH_AXES: {
			ChartWithAxes chartWithAxes = (ChartWithAxes) theEObject;
			T result = caseChartWithAxes(chartWithAxes);
			if (result == null)
				result = caseChart(chartWithAxes);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelPackage.CHART_WITHOUT_AXES: {
			ChartWithoutAxes chartWithoutAxes = (ChartWithoutAxes) theEObject;
			T result = caseChartWithoutAxes(chartWithoutAxes);
			if (result == null)
				result = caseChart(chartWithoutAxes);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelPackage.DIAL_CHART: {
			DialChart dialChart = (DialChart) theEObject;
			T result = caseDialChart(dialChart);
			if (result == null)
				result = caseChartWithoutAxes(dialChart);
			if (result == null)
				result = caseChart(dialChart);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Chart</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Chart</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChart(Chart object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Chart
	 * With Axes</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Chart
	 *         With Axes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChartWithAxes(ChartWithAxes object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Chart
	 * Without Axes</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Chart
	 *         Without Axes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChartWithoutAxes(ChartWithoutAxes object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dial
	 * Chart</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dial
	 *         Chart</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDialChart(DialChart object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>EObject</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last
	 * case anyway. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} // ModelSwitch
