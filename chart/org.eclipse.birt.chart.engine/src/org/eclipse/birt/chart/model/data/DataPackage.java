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

package org.eclipse.birt.chart.model.data;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package </b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * Schema file for the chart.model package.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.data.DataFactory
 * @generated
 */
public interface DataPackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNAME = "data"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelData"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_PREFIX = "data"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	DataPackage eINSTANCE = org.eclipse.birt.chart.model.data.impl.DataPackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.ActionImpl <em>Action</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.ActionImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getAction()
	 * @generated
	 */
	int ACTION = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ACTION__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ACTION__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Action</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ACTION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl <em>Base
	 * Sample Data</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBaseSampleData()
	 * @generated
	 */
	int BASE_SAMPLE_DATA = 1;

	/**
	 * The feature id for the '<em><b>Data Set Representation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION = 0;

	/**
	 * The number of structural features of the '<em>Base Sample Data</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int BASE_SAMPLE_DATA_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.DataElementImpl
	 * <em>Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.DataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDataElement()
	 * @generated
	 */
	int DATA_ELEMENT = 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.DataSetImpl <em>Set</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.DataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDataSet()
	 * @generated
	 */
	int DATA_SET = 5;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl <em>Bubble
	 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBubbleDataSet()
	 * @generated
	 */
	int BUBBLE_DATA_SET = 3;

	/**
	 * The number of structural features of the '<em>Element</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATA_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.BigNumberDataElementImpl
	 * <em>Big Number Data Element</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.BigNumberDataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBigNumberDataElement()
	 * @generated
	 */
	int BIG_NUMBER_DATA_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int BIG_NUMBER_DATA_ELEMENT__VALUE = DATA_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Big Number Data Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int BIG_NUMBER_DATA_ELEMENT_FEATURE_COUNT = DATA_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATA_SET__VALUES = 0;

	/**
	 * The number of structural features of the '<em>Set</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATA_SET_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int BUBBLE_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Bubble Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int BUBBLE_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl
	 * <em>Date Time Data Element</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDateTimeDataElement()
	 * @generated
	 */
	int DATE_TIME_DATA_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_ELEMENT__VALUE = DATA_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Date Time Data Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_ELEMENT_FEATURE_COUNT = DATA_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl <em>Date
	 * Time Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDateTimeDataSet()
	 * @generated
	 */
	int DATE_TIME_DATA_SET = 7;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Date Time Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl
	 * <em>Difference Data Set</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDifferenceDataSet()
	 * @generated
	 */
	int DIFFERENCE_DATA_SET = 8;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Difference Data Set</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.GanttDataSetImpl <em>Gantt
	 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.GanttDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getGanttDataSet()
	 * @generated
	 */
	int GANTT_DATA_SET = 9;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int GANTT_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Gantt Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int GANTT_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl
	 * <em>Multiple Actions</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getMultipleActions()
	 * @generated
	 */
	int MULTIPLE_ACTIONS = 10;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_ACTIONS__TYPE = ACTION__TYPE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_ACTIONS__VALUE = ACTION__VALUE;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_ACTIONS__ACTIONS = ACTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Properties Map</b></em>' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_ACTIONS__PROPERTIES_MAP = ACTION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Multiple Actions</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_ACTIONS_FEATURE_COUNT = ACTION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.NullDataSetImpl <em>Null Data
	 * Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.NullDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNullDataSet()
	 * @generated
	 */
	int NULL_DATA_SET = 11;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int NULL_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Null Data Set</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int NULL_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl
	 * <em>Number Data Element</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNumberDataElement()
	 * @generated
	 */
	int NUMBER_DATA_ELEMENT = 12;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_ELEMENT__VALUE = DATA_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Number Data Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_ELEMENT_FEATURE_COUNT = DATA_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl <em>Number
	 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNumberDataSet()
	 * @generated
	 */
	int NUMBER_DATA_SET = 13;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Number Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl
	 * <em>Orthogonal Sample Data</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getOrthogonalSampleData()
	 * @generated
	 */
	int ORTHOGONAL_SAMPLE_DATA = 14;

	/**
	 * The feature id for the '<em><b>Data Set Representation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION = 0;

	/**
	 * The feature id for the '<em><b>Series Definition Index</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX = 1;

	/**
	 * The number of structural features of the '<em>Orthogonal Sample Data</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ORTHOGONAL_SAMPLE_DATA_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.QueryImpl <em>Query</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.QueryImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getQuery()
	 * @generated
	 */
	int QUERY = 15;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int QUERY__DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int QUERY__RULES = 1;

	/**
	 * The feature id for the '<em><b>Grouping</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int QUERY__GROUPING = 2;

	/**
	 * The number of structural features of the '<em>Query</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int QUERY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.RuleImpl <em>Rule</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.RuleImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getRule()
	 * @generated
	 */
	int RULE = 16;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int RULE__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int RULE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Rule</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int RULE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl <em>Sample
	 * Data</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.SampleDataImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSampleData()
	 * @generated
	 */
	int SAMPLE_DATA = 17;

	/**
	 * The feature id for the '<em><b>Base Sample Data</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA__BASE_SAMPLE_DATA = 0;

	/**
	 * The feature id for the '<em><b>Orthogonal Sample Data</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA = 1;

	/**
	 * The feature id for the '<em><b>Ancillary Sample Data</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA__ANCILLARY_SAMPLE_DATA = 2;

	/**
	 * The number of structural features of the '<em>Sample Data</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl
	 * <em>Series Definition</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSeriesDefinition()
	 * @generated
	 */
	int SERIES_DEFINITION = 18;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__QUERY = 0;

	/**
	 * The feature id for the '<em><b>Series Palette</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SERIES_PALETTE = 1;

	/**
	 * The feature id for the '<em><b>Series Definitions</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SERIES_DEFINITIONS = 2;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__FORMAT_SPECIFIER = 3;

	/**
	 * The feature id for the '<em><b>Series</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SERIES = 4;

	/**
	 * The feature id for the '<em><b>Grouping</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__GROUPING = 5;

	/**
	 * The feature id for the '<em><b>Sorting</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SORTING = 6;

	/**
	 * The feature id for the '<em><b>Sort Key</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SORT_KEY = 7;

	/**
	 * The feature id for the '<em><b>Sort Locale</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SORT_LOCALE = 8;

	/**
	 * The feature id for the '<em><b>Sort Strength</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SORT_STRENGTH = 9;

	/**
	 * The feature id for the '<em><b>ZOrder</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__ZORDER = 10;

	/**
	 * The number of structural features of the '<em>Series Definition</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl <em>Series
	 * Grouping</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSeriesGrouping()
	 * @generated
	 */
	int SERIES_GROUPING = 19;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__ENABLED = 0;

	/**
	 * The feature id for the '<em><b>Grouping Unit</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUPING_UNIT = 1;

	/**
	 * The feature id for the '<em><b>Grouping Origin</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUPING_ORIGIN = 2;

	/**
	 * The feature id for the '<em><b>Grouping Interval</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUPING_INTERVAL = 3;

	/**
	 * The feature id for the '<em><b>Group Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUP_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Aggregate Expression</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__AGGREGATE_EXPRESSION = 5;

	/**
	 * The feature id for the '<em><b>Aggregate Parameters</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__AGGREGATE_PARAMETERS = 6;

	/**
	 * The number of structural features of the '<em>Series Grouping</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.StockDataSetImpl <em>Stock
	 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.StockDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getStockDataSet()
	 * @generated
	 */
	int STOCK_DATA_SET = 20;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int STOCK_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Stock Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int STOCK_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.TextDataSetImpl <em>Text Data
	 * Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.TextDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getTextDataSet()
	 * @generated
	 */
	int TEXT_DATA_SET = 21;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TEXT_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the '<em>Text Data Set</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TEXT_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.data.impl.TriggerImpl <em>Trigger</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.birt.chart.model.data.impl.TriggerImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getTrigger()
	 * @generated
	 */
	int TRIGGER = 22;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TRIGGER__CONDITION = 0;

	/**
	 * The feature id for the '<em><b>Action</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TRIGGER__ACTION = 1;

	/**
	 * The feature id for the '<em><b>Trigger Flow</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TRIGGER__TRIGGER_FLOW = 2;

	/**
	 * The number of structural features of the '<em>Trigger</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int TRIGGER_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '<em>Data</em>' data type. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see java.lang.Object
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getData()
	 * @generated
	 */
	int DATA = 23;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.Action <em>Action</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Action</em>'.
	 * @see org.eclipse.birt.chart.model.data.Action
	 * @generated
	 */
	EClass getAction();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.Action#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.data.Action#getType()
	 * @see #getAction()
	 * @generated
	 */
	EAttribute getAction_Type();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.Action#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.Action#getValue()
	 * @see #getAction()
	 * @generated
	 */
	EReference getAction_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.BaseSampleData <em>Base Sample
	 * Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Base Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.BaseSampleData
	 * @generated
	 */
	EClass getBaseSampleData();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.BaseSampleData#getDataSetRepresentation
	 * <em>Data Set Representation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the attribute '<em>Data Set Representation</em>'.
	 * @see org.eclipse.birt.chart.model.data.BaseSampleData#getDataSetRepresentation()
	 * @see #getBaseSampleData()
	 * @generated
	 */
	EAttribute getBaseSampleData_DataSetRepresentation();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.BigNumberDataElement <em>Big Number
	 * Data Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Big Number Data Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.BigNumberDataElement
	 * @generated
	 */
	EClass getBigNumberDataElement();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.BigNumberDataElement#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.BigNumberDataElement#getValue()
	 * @see #getBigNumberDataElement()
	 * @generated
	 */
	EAttribute getBigNumberDataElement_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.BubbleDataSet <em>Bubble Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Bubble Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.BubbleDataSet
	 * @generated
	 */
	EClass getBubbleDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.DataElement <em>Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.DataElement
	 * @generated
	 */
	EClass getDataElement();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.DataSet <em>Set</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.DataSet
	 * @generated
	 */
	EClass getDataSet();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.DataSet#getValues
	 * <em>Values</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Values</em>'.
	 * @see org.eclipse.birt.chart.model.data.DataSet#getValues()
	 * @see #getDataSet()
	 * @generated
	 */
	EAttribute getDataSet_Values();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement <em>Date Time
	 * Data Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Date Time Data Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.DateTimeDataElement
	 * @generated
	 */
	EClass getDateTimeDataElement();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue()
	 * @see #getDateTimeDataElement()
	 * @generated
	 */
	EAttribute getDateTimeDataElement_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.DateTimeDataSet <em>Date Time Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Date Time Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.DateTimeDataSet
	 * @generated
	 */
	EClass getDateTimeDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.DifferenceDataSet <em>Difference
	 * Data Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Difference Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.DifferenceDataSet
	 * @generated
	 */
	EClass getDifferenceDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.GanttDataSet <em>Gantt Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Gantt Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.GanttDataSet
	 * @generated
	 */
	EClass getGanttDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.MultipleActions <em>Multiple
	 * Actions</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Multiple Actions</em>'.
	 * @see org.eclipse.birt.chart.model.data.MultipleActions
	 * @generated
	 */
	EClass getMultipleActions();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.MultipleActions#getActions
	 * <em>Actions</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference list
	 *         '<em>Actions</em>'.
	 * @see org.eclipse.birt.chart.model.data.MultipleActions#getActions()
	 * @see #getMultipleActions()
	 * @generated
	 */
	EReference getMultipleActions_Actions();

	/**
	 * Returns the meta object for the map
	 * '{@link org.eclipse.birt.chart.model.data.MultipleActions#getPropertiesMap
	 * <em>Properties Map</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Properties Map</em>'.
	 * @see org.eclipse.birt.chart.model.data.MultipleActions#getPropertiesMap()
	 * @see #getMultipleActions()
	 * @generated
	 */
	EReference getMultipleActions_PropertiesMap();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.NullDataSet <em>Null Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Null Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.NullDataSet
	 * @generated
	 */
	EClass getNullDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.NumberDataElement <em>Number Data
	 * Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Number Data Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.NumberDataElement
	 * @generated
	 */
	EClass getNumberDataElement();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.NumberDataElement#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.NumberDataElement#getValue()
	 * @see #getNumberDataElement()
	 * @generated
	 */
	EAttribute getNumberDataElement_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.NumberDataSet <em>Number Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Number Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.NumberDataSet
	 * @generated
	 */
	EClass getNumberDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData <em>Orthogonal
	 * Sample Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Orthogonal Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData
	 * @generated
	 */
	EClass getOrthogonalSampleData();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getDataSetRepresentation
	 * <em>Data Set Representation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the attribute '<em>Data Set Representation</em>'.
	 * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData#getDataSetRepresentation()
	 * @see #getOrthogonalSampleData()
	 * @generated
	 */
	EAttribute getOrthogonalSampleData_DataSetRepresentation();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex
	 * <em>Series Definition Index</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the attribute '<em>Series Definition Index</em>'.
	 * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex()
	 * @see #getOrthogonalSampleData()
	 * @generated
	 */
	EAttribute getOrthogonalSampleData_SeriesDefinitionIndex();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.Query <em>Query</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Query</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query
	 * @generated
	 */
	EClass getQuery();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.Query#getDefinition
	 * <em>Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Definition</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query#getDefinition()
	 * @see #getQuery()
	 * @generated
	 */
	EAttribute getQuery_Definition();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.Query#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference list '<em>Rules</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query#getRules()
	 * @see #getQuery()
	 * @generated
	 */
	EReference getQuery_Rules();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.Query#getGrouping
	 * <em>Grouping</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Grouping</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query#getGrouping()
	 * @see #getQuery()
	 * @generated
	 */
	EReference getQuery_Grouping();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.Rule <em>Rule</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Rule</em>'.
	 * @see org.eclipse.birt.chart.model.data.Rule
	 * @generated
	 */
	EClass getRule();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.Rule#getType <em>Type</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.data.Rule#getType()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_Type();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.Rule#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.Rule#getValue()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.SampleData <em>Sample Data</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData
	 * @generated
	 */
	EClass getSampleData();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.SampleData#getBaseSampleData
	 * <em>Base Sample Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference list '<em>Base Sample
	 *         Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData#getBaseSampleData()
	 * @see #getSampleData()
	 * @generated
	 */
	EReference getSampleData_BaseSampleData();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.SampleData#getOrthogonalSampleData
	 * <em>Orthogonal Sample Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the containment reference list '<em>Orthogonal
	 *         Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData#getOrthogonalSampleData()
	 * @see #getSampleData()
	 * @generated
	 */
	EReference getSampleData_OrthogonalSampleData();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.SampleData#getAncillarySampleData
	 * <em>Ancillary Sample Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the containment reference list '<em>Ancillary
	 *         Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData#getAncillarySampleData()
	 * @see #getSampleData()
	 * @generated
	 */
	EReference getSampleData_AncillarySampleData();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition <em>Series
	 * Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Series Definition</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition
	 * @generated
	 */
	EClass getSeriesDefinition();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery
	 * <em>Query</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Query</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_Query();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette
	 * <em>Series Palette</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Series
	 *         Palette</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_SeriesPalette();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesDefinitions
	 * <em>Series Definitions</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference list '<em>Series
	 *         Definitions</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesDefinitions()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_SeriesDefinitions();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_FormatSpecifier();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeries
	 * <em>Series</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference list '<em>Series</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSeries()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_Series();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping
	 * <em>Grouping</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Grouping</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_Grouping();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting
	 * <em>Sorting</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Sorting</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EAttribute getSeriesDefinition_Sorting();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortKey
	 * <em>Sort Key</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Sort Key</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSortKey()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_SortKey();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortLocale
	 * <em>Sort Locale</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Sort Locale</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSortLocale()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EAttribute getSeriesDefinition_SortLocale();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortStrength
	 * <em>Sort Strength</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Sort Strength</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSortStrength()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EAttribute getSeriesDefinition_SortStrength();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getZOrder
	 * <em>ZOrder</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>ZOrder</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getZOrder()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EAttribute getSeriesDefinition_ZOrder();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping <em>Series
	 * Grouping</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Series Grouping</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping
	 * @generated
	 */
	EClass getSeriesGrouping();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled
	 * <em>Enabled</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_Enabled();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit
	 * <em>Grouping Unit</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Grouping Unit</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_GroupingUnit();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingOrigin
	 * <em>Grouping Origin</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Grouping
	 *         Origin</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingOrigin()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EReference getSeriesGrouping_GroupingOrigin();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval
	 * <em>Grouping Interval</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Grouping Interval</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_GroupingInterval();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType
	 * <em>Group Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Group Type</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_GroupType();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression
	 * <em>Aggregate Expression</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the attribute '<em>Aggregate Expression</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_AggregateExpression();

	/**
	 * Returns the meta object for the attribute list
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateParameters
	 * <em>Aggregate Parameters</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return the meta object for the attribute list '<em>Aggregate
	 *         Parameters</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateParameters()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_AggregateParameters();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.StockDataSet <em>Stock Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Stock Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.StockDataSet
	 * @generated
	 */
	EClass getStockDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.TextDataSet <em>Text Data
	 * Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Text Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.TextDataSet
	 * @generated
	 */
	EClass getTextDataSet();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.data.Trigger <em>Trigger</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Trigger</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger
	 * @generated
	 */
	EClass getTrigger();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getCondition
	 * <em>Condition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Condition</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger#getCondition()
	 * @see #getTrigger()
	 * @generated
	 */
	EAttribute getTrigger_Condition();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getAction
	 * <em>Action</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Action</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger#getAction()
	 * @see #getTrigger()
	 * @generated
	 */
	EReference getTrigger_Action();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getTriggerFlow <em>Trigger
	 * Flow</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Trigger Flow</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger#getTriggerFlow()
	 * @see #getTrigger()
	 * @generated
	 */
	EAttribute getTrigger_TriggerFlow();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object
	 * <em>Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the meta object for data type '<em>Data</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object" extendedMetaData="name='Data'"
	 * @generated
	 */
	EDataType getData();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DataFactory getDataFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	interface Literals {

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.ActionImpl <em>Action</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.ActionImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getAction()
		 * @generated
		 */
		EClass ACTION = eINSTANCE.getAction();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ACTION__TYPE = eINSTANCE.getAction_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ACTION__VALUE = eINSTANCE.getAction_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl <em>Base
		 * Sample Data</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBaseSampleData()
		 * @generated
		 */
		EClass BASE_SAMPLE_DATA = eINSTANCE.getBaseSampleData();

		/**
		 * The meta object literal for the '<em><b>Data Set Representation</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION = eINSTANCE.getBaseSampleData_DataSetRepresentation();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.BigNumberDataElementImpl
		 * <em>Big Number Data Element</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.BigNumberDataElementImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBigNumberDataElement()
		 * @generated
		 */
		EClass BIG_NUMBER_DATA_ELEMENT = eINSTANCE.getBigNumberDataElement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute BIG_NUMBER_DATA_ELEMENT__VALUE = eINSTANCE.getBigNumberDataElement_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl <em>Bubble
		 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBubbleDataSet()
		 * @generated
		 */
		EClass BUBBLE_DATA_SET = eINSTANCE.getBubbleDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.DataElementImpl
		 * <em>Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.DataElementImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDataElement()
		 * @generated
		 */
		EClass DATA_ELEMENT = eINSTANCE.getDataElement();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.DataSetImpl <em>Set</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.DataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDataSet()
		 * @generated
		 */
		EClass DATA_SET = eINSTANCE.getDataSet();

		/**
		 * The meta object literal for the '<em><b>Values</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute DATA_SET__VALUES = eINSTANCE.getDataSet_Values();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl
		 * <em>Date Time Data Element</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDateTimeDataElement()
		 * @generated
		 */
		EClass DATE_TIME_DATA_ELEMENT = eINSTANCE.getDateTimeDataElement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute DATE_TIME_DATA_ELEMENT__VALUE = eINSTANCE.getDateTimeDataElement_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl <em>Date
		 * Time Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDateTimeDataSet()
		 * @generated
		 */
		EClass DATE_TIME_DATA_SET = eINSTANCE.getDateTimeDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl
		 * <em>Difference Data Set</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDifferenceDataSet()
		 * @generated
		 */
		EClass DIFFERENCE_DATA_SET = eINSTANCE.getDifferenceDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.GanttDataSetImpl <em>Gantt
		 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.GanttDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getGanttDataSet()
		 * @generated
		 */
		EClass GANTT_DATA_SET = eINSTANCE.getGanttDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl
		 * <em>Multiple Actions</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getMultipleActions()
		 * @generated
		 */
		EClass MULTIPLE_ACTIONS = eINSTANCE.getMultipleActions();

		/**
		 * The meta object literal for the '<em><b>Actions</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference MULTIPLE_ACTIONS__ACTIONS = eINSTANCE.getMultipleActions_Actions();

		/**
		 * The meta object literal for the '<em><b>Properties Map</b></em>' map feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference MULTIPLE_ACTIONS__PROPERTIES_MAP = eINSTANCE.getMultipleActions_PropertiesMap();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.NullDataSetImpl <em>Null Data
		 * Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.NullDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNullDataSet()
		 * @generated
		 */
		EClass NULL_DATA_SET = eINSTANCE.getNullDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl
		 * <em>Number Data Element</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNumberDataElement()
		 * @generated
		 */
		EClass NUMBER_DATA_ELEMENT = eINSTANCE.getNumberDataElement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute NUMBER_DATA_ELEMENT__VALUE = eINSTANCE.getNumberDataElement_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl <em>Number
		 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNumberDataSet()
		 * @generated
		 */
		EClass NUMBER_DATA_SET = eINSTANCE.getNumberDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl
		 * <em>Orthogonal Sample Data</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getOrthogonalSampleData()
		 * @generated
		 */
		EClass ORTHOGONAL_SAMPLE_DATA = eINSTANCE.getOrthogonalSampleData();

		/**
		 * The meta object literal for the '<em><b>Data Set Representation</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION = eINSTANCE
				.getOrthogonalSampleData_DataSetRepresentation();

		/**
		 * The meta object literal for the '<em><b>Series Definition Index</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX = eINSTANCE
				.getOrthogonalSampleData_SeriesDefinitionIndex();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.QueryImpl <em>Query</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.QueryImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getQuery()
		 * @generated
		 */
		EClass QUERY = eINSTANCE.getQuery();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute QUERY__DEFINITION = eINSTANCE.getQuery_Definition();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' containment reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference QUERY__RULES = eINSTANCE.getQuery_Rules();

		/**
		 * The meta object literal for the '<em><b>Grouping</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference QUERY__GROUPING = eINSTANCE.getQuery_Grouping();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.RuleImpl <em>Rule</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.RuleImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getRule()
		 * @generated
		 */
		EClass RULE = eINSTANCE.getRule();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute RULE__TYPE = eINSTANCE.getRule_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute RULE__VALUE = eINSTANCE.getRule_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl <em>Sample
		 * Data</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.SampleDataImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSampleData()
		 * @generated
		 */
		EClass SAMPLE_DATA = eINSTANCE.getSampleData();

		/**
		 * The meta object literal for the '<em><b>Base Sample Data</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 *
		 * @generated
		 */
		EReference SAMPLE_DATA__BASE_SAMPLE_DATA = eINSTANCE.getSampleData_BaseSampleData();

		/**
		 * The meta object literal for the '<em><b>Orthogonal Sample Data</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 *
		 * @generated
		 */
		EReference SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA = eINSTANCE.getSampleData_OrthogonalSampleData();

		/**
		 * The meta object literal for the '<em><b>Ancillary Sample Data</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 *
		 * @generated
		 */
		EReference SAMPLE_DATA__ANCILLARY_SAMPLE_DATA = eINSTANCE.getSampleData_AncillarySampleData();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl
		 * <em>Series Definition</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSeriesDefinition()
		 * @generated
		 */
		EClass SERIES_DEFINITION = eINSTANCE.getSeriesDefinition();

		/**
		 * The meta object literal for the '<em><b>Query</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__QUERY = eINSTANCE.getSeriesDefinition_Query();

		/**
		 * The meta object literal for the '<em><b>Series Palette</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__SERIES_PALETTE = eINSTANCE.getSeriesDefinition_SeriesPalette();

		/**
		 * The meta object literal for the '<em><b>Series Definitions</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__SERIES_DEFINITIONS = eINSTANCE.getSeriesDefinition_SeriesDefinitions();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__FORMAT_SPECIFIER = eINSTANCE.getSeriesDefinition_FormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Series</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__SERIES = eINSTANCE.getSeriesDefinition_Series();

		/**
		 * The meta object literal for the '<em><b>Grouping</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__GROUPING = eINSTANCE.getSeriesDefinition_Grouping();

		/**
		 * The meta object literal for the '<em><b>Sorting</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_DEFINITION__SORTING = eINSTANCE.getSeriesDefinition_Sorting();

		/**
		 * The meta object literal for the '<em><b>Sort Key</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_DEFINITION__SORT_KEY = eINSTANCE.getSeriesDefinition_SortKey();

		/**
		 * The meta object literal for the '<em><b>Sort Locale</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_DEFINITION__SORT_LOCALE = eINSTANCE.getSeriesDefinition_SortLocale();

		/**
		 * The meta object literal for the '<em><b>Sort Strength</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_DEFINITION__SORT_STRENGTH = eINSTANCE.getSeriesDefinition_SortStrength();

		/**
		 * The meta object literal for the '<em><b>ZOrder</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_DEFINITION__ZORDER = eINSTANCE.getSeriesDefinition_ZOrder();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl <em>Series
		 * Grouping</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSeriesGrouping()
		 * @generated
		 */
		EClass SERIES_GROUPING = eINSTANCE.getSeriesGrouping();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_GROUPING__ENABLED = eINSTANCE.getSeriesGrouping_Enabled();

		/**
		 * The meta object literal for the '<em><b>Grouping Unit</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_GROUPING__GROUPING_UNIT = eINSTANCE.getSeriesGrouping_GroupingUnit();

		/**
		 * The meta object literal for the '<em><b>Grouping Origin</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SERIES_GROUPING__GROUPING_ORIGIN = eINSTANCE.getSeriesGrouping_GroupingOrigin();

		/**
		 * The meta object literal for the '<em><b>Grouping Interval</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_GROUPING__GROUPING_INTERVAL = eINSTANCE.getSeriesGrouping_GroupingInterval();

		/**
		 * The meta object literal for the '<em><b>Group Type</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_GROUPING__GROUP_TYPE = eINSTANCE.getSeriesGrouping_GroupType();

		/**
		 * The meta object literal for the '<em><b>Aggregate Expression</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_GROUPING__AGGREGATE_EXPRESSION = eINSTANCE.getSeriesGrouping_AggregateExpression();

		/**
		 * The meta object literal for the '<em><b>Aggregate Parameters</b></em>'
		 * attribute list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SERIES_GROUPING__AGGREGATE_PARAMETERS = eINSTANCE.getSeriesGrouping_AggregateParameters();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.StockDataSetImpl <em>Stock
		 * Data Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.StockDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getStockDataSet()
		 * @generated
		 */
		EClass STOCK_DATA_SET = eINSTANCE.getStockDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.TextDataSetImpl <em>Text Data
		 * Set</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.TextDataSetImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getTextDataSet()
		 * @generated
		 */
		EClass TEXT_DATA_SET = eINSTANCE.getTextDataSet();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.data.impl.TriggerImpl <em>Trigger</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see org.eclipse.birt.chart.model.data.impl.TriggerImpl
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getTrigger()
		 * @generated
		 */
		EClass TRIGGER = eINSTANCE.getTrigger();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute TRIGGER__CONDITION = eINSTANCE.getTrigger_Condition();

		/**
		 * The meta object literal for the '<em><b>Action</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference TRIGGER__ACTION = eINSTANCE.getTrigger_Action();

		/**
		 * The meta object literal for the '<em><b>Trigger Flow</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute TRIGGER__TRIGGER_FLOW = eINSTANCE.getTrigger_TriggerFlow();

		/**
		 * The meta object literal for the '<em>Data</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 *
		 * @see java.lang.Object
		 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getData()
		 * @generated
		 */
		EDataType DATA = eINSTANCE.getData();

	}

} // DataPackage
