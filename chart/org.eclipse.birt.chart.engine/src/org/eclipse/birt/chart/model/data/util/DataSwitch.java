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

package org.eclipse.birt.chart.model.data.util;

import java.util.List;

import org.eclipse.birt.chart.model.data.*;

import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Rule;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
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
 * @see org.eclipse.birt.chart.model.data.DataPackage
 * @generated
 */
public class DataSwitch<T> {

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static DataPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public DataSwitch() {
		if (modelPackage == null) {
			modelPackage = DataPackage.eINSTANCE;
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
		case DataPackage.ACTION: {
			Action action = (Action) theEObject;
			T result = caseAction(action);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.BASE_SAMPLE_DATA: {
			BaseSampleData baseSampleData = (BaseSampleData) theEObject;
			T result = caseBaseSampleData(baseSampleData);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.BIG_NUMBER_DATA_ELEMENT: {
			BigNumberDataElement bigNumberDataElement = (BigNumberDataElement) theEObject;
			T result = caseBigNumberDataElement(bigNumberDataElement);
			if (result == null)
				result = caseDataElement(bigNumberDataElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.BUBBLE_DATA_SET: {
			BubbleDataSet bubbleDataSet = (BubbleDataSet) theEObject;
			T result = caseBubbleDataSet(bubbleDataSet);
			if (result == null)
				result = caseDataSet(bubbleDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.DATA_ELEMENT: {
			DataElement dataElement = (DataElement) theEObject;
			T result = caseDataElement(dataElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.DATA_SET: {
			DataSet dataSet = (DataSet) theEObject;
			T result = caseDataSet(dataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.DATE_TIME_DATA_ELEMENT: {
			DateTimeDataElement dateTimeDataElement = (DateTimeDataElement) theEObject;
			T result = caseDateTimeDataElement(dateTimeDataElement);
			if (result == null)
				result = caseDataElement(dateTimeDataElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.DATE_TIME_DATA_SET: {
			DateTimeDataSet dateTimeDataSet = (DateTimeDataSet) theEObject;
			T result = caseDateTimeDataSet(dateTimeDataSet);
			if (result == null)
				result = caseDataSet(dateTimeDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.DIFFERENCE_DATA_SET: {
			DifferenceDataSet differenceDataSet = (DifferenceDataSet) theEObject;
			T result = caseDifferenceDataSet(differenceDataSet);
			if (result == null)
				result = caseDataSet(differenceDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.GANTT_DATA_SET: {
			GanttDataSet ganttDataSet = (GanttDataSet) theEObject;
			T result = caseGanttDataSet(ganttDataSet);
			if (result == null)
				result = caseDataSet(ganttDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.MULTIPLE_ACTIONS: {
			MultipleActions multipleActions = (MultipleActions) theEObject;
			T result = caseMultipleActions(multipleActions);
			if (result == null)
				result = caseAction(multipleActions);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.NULL_DATA_SET: {
			NullDataSet nullDataSet = (NullDataSet) theEObject;
			T result = caseNullDataSet(nullDataSet);
			if (result == null)
				result = caseDataSet(nullDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.NUMBER_DATA_ELEMENT: {
			NumberDataElement numberDataElement = (NumberDataElement) theEObject;
			T result = caseNumberDataElement(numberDataElement);
			if (result == null)
				result = caseDataElement(numberDataElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.NUMBER_DATA_SET: {
			NumberDataSet numberDataSet = (NumberDataSet) theEObject;
			T result = caseNumberDataSet(numberDataSet);
			if (result == null)
				result = caseDataSet(numberDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.ORTHOGONAL_SAMPLE_DATA: {
			OrthogonalSampleData orthogonalSampleData = (OrthogonalSampleData) theEObject;
			T result = caseOrthogonalSampleData(orthogonalSampleData);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.QUERY: {
			Query query = (Query) theEObject;
			T result = caseQuery(query);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.RULE: {
			Rule rule = (Rule) theEObject;
			T result = caseRule(rule);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.SAMPLE_DATA: {
			SampleData sampleData = (SampleData) theEObject;
			T result = caseSampleData(sampleData);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.SERIES_DEFINITION: {
			SeriesDefinition seriesDefinition = (SeriesDefinition) theEObject;
			T result = caseSeriesDefinition(seriesDefinition);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.SERIES_GROUPING: {
			SeriesGrouping seriesGrouping = (SeriesGrouping) theEObject;
			T result = caseSeriesGrouping(seriesGrouping);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.STOCK_DATA_SET: {
			StockDataSet stockDataSet = (StockDataSet) theEObject;
			T result = caseStockDataSet(stockDataSet);
			if (result == null)
				result = caseDataSet(stockDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.TEXT_DATA_SET: {
			TextDataSet textDataSet = (TextDataSet) theEObject;
			T result = caseTextDataSet(textDataSet);
			if (result == null)
				result = caseDataSet(textDataSet);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case DataPackage.TRIGGER: {
			Trigger trigger = (Trigger) theEObject;
			T result = caseTrigger(trigger);
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
	 * '<em>Action</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAction(Action object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Base
	 * Sample Data</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Base
	 *         Sample Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseSampleData(BaseSampleData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Big
	 * Number Data Element</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Big
	 *         Number Data Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBigNumberDataElement(BigNumberDataElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bubble
	 * Data Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bubble
	 *         Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBubbleDataSet(BubbleDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Element</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataElement(DataElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataSet(DataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Date
	 * Time Data Element</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Date
	 *         Time Data Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDateTimeDataElement(DateTimeDataElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Date
	 * Time Data Set</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Date
	 *         Time Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDateTimeDataSet(DateTimeDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Difference Data Set</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Difference Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDifferenceDataSet(DifferenceDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Gantt
	 * Data Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Gantt
	 *         Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGanttDataSet(GanttDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multiple
	 * Actions</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multiple
	 *         Actions</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMultipleActions(MultipleActions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Null
	 * Data Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Null
	 *         Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNullDataSet(NullDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Number
	 * Data Element</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Number
	 *         Data Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNumberDataElement(NumberDataElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Number
	 * Data Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Number
	 *         Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNumberDataSet(NumberDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of
	 * '<em>Orthogonal Sample Data</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of
	 *         '<em>Orthogonal Sample Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOrthogonalSampleData(OrthogonalSampleData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Query</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQuery(Query object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Rule</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRule(Rule object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Sample
	 * Data</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Sample
	 *         Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSampleData(SampleData object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Series
	 * Definition</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Series
	 *         Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSeriesDefinition(SeriesDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Series
	 * Grouping</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Series
	 *         Grouping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSeriesGrouping(SeriesGrouping object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Stock
	 * Data Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Stock
	 *         Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStockDataSet(StockDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Text
	 * Data Set</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Text
	 *         Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTextDataSet(TextDataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of
	 * '<em>Trigger</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of
	 *         '<em>Trigger</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTrigger(Trigger object) {
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

} // DataSwitch
