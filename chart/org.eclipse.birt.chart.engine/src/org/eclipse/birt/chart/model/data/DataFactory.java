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

package org.eclipse.birt.chart.model.data;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory </b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.data.DataPackage
 * @generated
 */
public interface DataFactory extends EFactory {

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	DataFactory eINSTANCE = org.eclipse.birt.chart.model.data.impl.DataFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Action</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Action</em>'.
	 * @generated
	 */
	Action createAction();

	/**
	 * Returns a new object of class '<em>Base Sample Data</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Base Sample Data</em>'.
	 * @generated
	 */
	BaseSampleData createBaseSampleData();

	/**
	 * Returns a new object of class '<em>Big Number Data Element</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Big Number Data Element</em>'.
	 * @generated
	 */
	BigNumberDataElement createBigNumberDataElement();

	/**
	 * Returns a new object of class '<em>Bubble Data Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bubble Data Set</em>'.
	 * @generated
	 */
	BubbleDataSet createBubbleDataSet();

	/**
	 * Returns a new object of class '<em>Element</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Element</em>'.
	 * @generated
	 */
	DataElement createDataElement();

	/**
	 * Returns a new object of class '<em>Set</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Set</em>'.
	 * @generated
	 */
	DataSet createDataSet();

	/**
	 * Returns a new object of class '<em>Date Time Data Element</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Date Time Data Element</em>'.
	 * @generated
	 */
	DateTimeDataElement createDateTimeDataElement();

	/**
	 * Returns a new object of class '<em>Date Time Data Set</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Date Time Data Set</em>'.
	 * @generated
	 */
	DateTimeDataSet createDateTimeDataSet();

	/**
	 * Returns a new object of class '<em>Difference Data Set</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Difference Data Set</em>'.
	 * @generated
	 */
	DifferenceDataSet createDifferenceDataSet();

	/**
	 * Returns a new object of class '<em>Gantt Data Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Gantt Data Set</em>'.
	 * @generated
	 */
	GanttDataSet createGanttDataSet();

	/**
	 * Returns a new object of class '<em>Multiple Actions</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Multiple Actions</em>'.
	 * @generated
	 */
	MultipleActions createMultipleActions();

	/**
	 * Returns a new object of class '<em>Null Data Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Null Data Set</em>'.
	 * @generated
	 */
	NullDataSet createNullDataSet();

	/**
	 * Returns a new object of class '<em>Number Data Element</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Number Data Element</em>'.
	 * @generated
	 */
	NumberDataElement createNumberDataElement();

	/**
	 * Returns a new object of class '<em>Number Data Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Number Data Set</em>'.
	 * @generated
	 */
	NumberDataSet createNumberDataSet();

	/**
	 * Returns a new object of class '<em>Orthogonal Sample Data</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Orthogonal Sample Data</em>'.
	 * @generated
	 */
	OrthogonalSampleData createOrthogonalSampleData();

	/**
	 * Returns a new object of class '<em>Query</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Query</em>'.
	 * @generated
	 */
	Query createQuery();

	/**
	 * Returns a new object of class '<em>Rule</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Rule</em>'.
	 * @deprecated only reserved for compatibility
	 */
	Rule createRule();

	/**
	 * Returns a new object of class '<em>Sample Data</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Sample Data</em>'.
	 * @generated
	 */
	SampleData createSampleData();

	/**
	 * Returns a new object of class '<em>Series Definition</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Series Definition</em>'.
	 * @generated
	 */
	SeriesDefinition createSeriesDefinition();

	/**
	 * Returns a new object of class '<em>Series Grouping</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Series Grouping</em>'.
	 * @generated
	 */
	SeriesGrouping createSeriesGrouping();

	/**
	 * Returns a new object of class '<em>Stock Data Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Stock Data Set</em>'.
	 * @generated
	 */
	StockDataSet createStockDataSet();

	/**
	 * Returns a new object of class '<em>Text Data Set</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Text Data Set</em>'.
	 * @generated
	 */
	TextDataSet createTextDataSet();

	/**
	 * Returns a new object of class '<em>Trigger</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Trigger</em>'.
	 * @generated
	 */
	Trigger createTrigger();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	DataPackage getDataPackage();

} // DataFactory
