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

package org.eclipse.birt.chart.model.type.util;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.*;

import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc --> The <b>Switch </b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}to
 * invoke the <code>caseXXX</code> method for each class of the model, starting
 * with the actual class of the object and proceeding up the inheritance
 * hierarchy until a non-null result is returned, which is the result of the
 * switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.type.TypePackage
 * @generated
 */
public class TypeSwitch<T> extends Switch<T> {

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static TypePackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public TypeSwitch() {
		if (modelPackage == null) {
			modelPackage = TypePackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a
	 * non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case TypePackage.AREA_SERIES: {
			AreaSeries areaSeries = (AreaSeries) theEObject;
			T result = caseAreaSeries(areaSeries);
			if (result == null)
				result = caseLineSeries(areaSeries);
			if (result == null)
				result = caseSeries(areaSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.BAR_SERIES: {
			BarSeries barSeries = (BarSeries) theEObject;
			T result = caseBarSeries(barSeries);
			if (result == null)
				result = caseSeries(barSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.BUBBLE_SERIES: {
			BubbleSeries bubbleSeries = (BubbleSeries) theEObject;
			T result = caseBubbleSeries(bubbleSeries);
			if (result == null)
				result = caseScatterSeries(bubbleSeries);
			if (result == null)
				result = caseLineSeries(bubbleSeries);
			if (result == null)
				result = caseSeries(bubbleSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.DIAL_SERIES: {
			DialSeries dialSeries = (DialSeries) theEObject;
			T result = caseDialSeries(dialSeries);
			if (result == null)
				result = caseSeries(dialSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.DIFFERENCE_SERIES: {
			DifferenceSeries differenceSeries = (DifferenceSeries) theEObject;
			T result = caseDifferenceSeries(differenceSeries);
			if (result == null)
				result = caseAreaSeries(differenceSeries);
			if (result == null)
				result = caseLineSeries(differenceSeries);
			if (result == null)
				result = caseSeries(differenceSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.GANTT_SERIES: {
			GanttSeries ganttSeries = (GanttSeries) theEObject;
			T result = caseGanttSeries(ganttSeries);
			if (result == null)
				result = caseSeries(ganttSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.LINE_SERIES: {
			LineSeries lineSeries = (LineSeries) theEObject;
			T result = caseLineSeries(lineSeries);
			if (result == null)
				result = caseSeries(lineSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.PIE_SERIES: {
			PieSeries pieSeries = (PieSeries) theEObject;
			T result = casePieSeries(pieSeries);
			if (result == null)
				result = caseSeries(pieSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.SCATTER_SERIES: {
			ScatterSeries scatterSeries = (ScatterSeries) theEObject;
			T result = caseScatterSeries(scatterSeries);
			if (result == null)
				result = caseLineSeries(scatterSeries);
			if (result == null)
				result = caseSeries(scatterSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TypePackage.STOCK_SERIES: {
			StockSeries stockSeries = (StockSeries) theEObject;
			T result = caseStockSeries(stockSeries);
			if (result == null)
				result = caseSeries(stockSeries);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Area
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Area
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAreaSeries(AreaSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Bar
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Bar
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBarSeries(BarSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bubble
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bubble
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBubbleSeries(BubbleSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dial
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dial
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDialSeries(DialSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Difference Series</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Difference Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDifferenceSeries(DifferenceSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Gantt
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Gantt
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGanttSeries(GanttSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Line
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Line
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLineSeries(LineSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Pie
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Pie
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePieSeries(PieSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Scatter
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Scatter
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScatterSeries(ScatterSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Stock
	 * Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Stock
	 *         Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStockSeries(StockSeries object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Series</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Series</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSeries(Series object) {
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
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // TypeSwitch
