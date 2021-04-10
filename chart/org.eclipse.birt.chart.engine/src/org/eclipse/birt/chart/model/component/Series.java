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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type defines a single (related) group of data
 * to be plotted in a chart. A series is usually expected to be associated with
 * a numerical axis. The series includes a name, visualization settings, and a
 * query definition used to retrieve the data point values. At designtime, the
 * series data set is null, while at runtime, each series' data set gets
 * populated with the data for that series. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getDataDefinition
 * <em>Data Definition</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getSeriesIdentifier
 * <em>Series Identifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getDataPoint
 * <em>Data Point</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getDataSets <em>Data
 * Sets</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getLabelPosition
 * <em>Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#isStacked
 * <em>Stacked</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#isTranslucent
 * <em>Translucent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getCurveFitting
 * <em>Curve Fitting</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Series#getCursor
 * <em>Cursor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries()
 * @model extendedMetaData="name='Series' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Series extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute. The default
	 * value is <code>"true"</code>. <!-- begin-user-doc --> Gets visibility of the
	 * series. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Determines visibility of the series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #setVisible(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_Visible()
	 * @model default="true" unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> Sets visibility of the
	 * series. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetVisible()
	 * @see #isVisible()
	 * @see #setVisible(boolean)
	 * @generated
	 */
	void unsetVisible();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isVisible
	 * <em>Visible</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Visible</em>' attribute is set.
	 * @see #unsetVisible()
	 * @see #isVisible()
	 * @see #setVisible(boolean)
	 * @generated
	 */
	boolean isSetVisible();

	/**
	 * Returns the value of the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the properties for the Series data points.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_Label()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Label getLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getLabel
	 * <em>Label</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label</em>' containment reference.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Data Definition</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.DataDefinition}. <!-- begin-user-doc
	 * --> Gets the list of data source definitions for this series. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the source of the data for the series. (e.g. Database field which
	 * will provide the values for the series).
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Data Definition</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_DataDefinition()
	 * @model type="org.eclipse.birt.chart.model.data.DataDefinition"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList<Query> getDataDefinition();

	/**
	 * Returns the value of the '<em><b>Series Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Returns an identifier that represents the seriesKey runtime value which is
	 * rendered in the legend (if grouped by series)
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Series Identifier</em>' attribute.
	 * @see #setSeriesIdentifier(String)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_SeriesIdentifier()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	Object getSeriesIdentifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getSeriesIdentifier
	 * <em>Series Identifier</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Series Identifier</em>' attribute.
	 * @see #getSeriesIdentifier()
	 * @generated
	 */
	void setSeriesIdentifier(Object value);

	/**
	 * Returns the value of the '<em><b>Data Point</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies what (and how) information is shown in the data label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Data Point</em>' containment reference.
	 * @see #setDataPoint(DataPoint)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_DataPoint()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	DataPoint getDataPoint();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getDataPoint <em>Data
	 * Point</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Data Point</em>' containment
	 *              reference.
	 * @see #getDataPoint()
	 * @generated
	 */
	void setDataPoint(DataPoint value);

	/**
	 * Returns the value of the '<em><b>Data Sets</b></em>' map. The key is of type
	 * {@link java.lang.String}, and the value is of type
	 * {@link org.eclipse.birt.chart.model.data.DataSet}, <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the runtime data for the series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Data Sets</em>' map.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_DataSets()
	 * @model mapType="org.eclipse.birt.chart.model.component.EStringToDataSetMapEntry"
	 *        keyType="java.lang.String"
	 *        valueType="org.eclipse.birt.chart.model.data.DataSet"
	 *        extendedMetaData="kind='element' name='DataSets'"
	 * @generated
	 */
	EMap<String, DataSet> getDataSets();

	/**
	 * Returns the value of the '<em><b>Label Position</b></em>' attribute. The
	 * default value is <code>"Above"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Position}. <!-- begin-user-doc
	 * --> Gets where the data label for the series should be displayed. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies where the data label for the series should be displayed.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Label Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetLabelPosition()
	 * @see #unsetLabelPosition()
	 * @see #setLabelPosition(Position)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_LabelPosition()
	 * @model default="Above" unique="false" unsettable="true"
	 * @generated
	 */
	Position getLabelPosition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getLabelPosition
	 * <em>Label Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetLabelPosition()
	 * @see #unsetLabelPosition()
	 * @see #getLabelPosition()
	 * @generated
	 */
	void setLabelPosition(Position value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getLabelPosition
	 * <em>Label Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetLabelPosition()
	 * @see #getLabelPosition()
	 * @see #setLabelPosition(Position)
	 * @generated
	 */
	void unsetLabelPosition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getLabelPosition
	 * <em>Label Position</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Label Position</em>' attribute is set.
	 * @see #unsetLabelPosition()
	 * @see #getLabelPosition()
	 * @see #setLabelPosition(Position)
	 * @generated
	 */
	boolean isSetLabelPosition();

	/**
	 * Returns the value of the '<em><b>Stacked</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether the series is to be rendered stacked.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Stacked</em>' attribute.
	 * @see #isSetStacked()
	 * @see #unsetStacked()
	 * @see #setStacked(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_Stacked()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='Stacked'"
	 * @generated
	 */
	boolean isStacked();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isStacked
	 * <em>Stacked</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Stacked</em>' attribute.
	 * @see #isSetStacked()
	 * @see #unsetStacked()
	 * @see #isStacked()
	 * @generated
	 */
	void setStacked(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isStacked
	 * <em>Stacked</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetStacked()
	 * @see #isStacked()
	 * @see #setStacked(boolean)
	 * @generated
	 */
	void unsetStacked();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isStacked
	 * <em>Stacked</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Stacked</em>' attribute is set.
	 * @see #unsetStacked()
	 * @see #isStacked()
	 * @see #setStacked(boolean)
	 * @generated
	 */
	boolean isSetStacked();

	/**
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.Trigger}. <!-- begin-user-doc
	 * -->Triggers are the elements that define interactivity for chart components.
	 * They include the action to be performed as well as parameters that define the
	 * details of the action. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the triggers for the series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_Triggers()
	 * @model type="org.eclipse.birt.chart.model.data.Trigger" containment="true"
	 *        resolveProxies="false"
	 * @generated
	 */
	EList<Trigger> getTriggers();

	/**
	 * Returns the value of the '<em><b>Translucent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether the series elements are to be rendered translucent. This
	 * setting is only applicable for elements with solid color fills.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Translucent</em>' attribute.
	 * @see #isSetTranslucent()
	 * @see #unsetTranslucent()
	 * @see #setTranslucent(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_Translucent()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isTranslucent();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isTranslucent
	 * <em>Translucent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Translucent</em>' attribute.
	 * @see #isSetTranslucent()
	 * @see #unsetTranslucent()
	 * @see #isTranslucent()
	 * @generated
	 */
	void setTranslucent(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isTranslucent
	 * <em>Translucent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetTranslucent()
	 * @see #isTranslucent()
	 * @see #setTranslucent(boolean)
	 * @generated
	 */
	void unsetTranslucent();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#isTranslucent
	 * <em>Translucent</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Translucent</em>' attribute is set.
	 * @see #unsetTranslucent()
	 * @see #isTranslucent()
	 * @see #setTranslucent(boolean)
	 * @generated
	 */
	boolean isSetTranslucent();

	/**
	 * Returns the value of the '<em><b>Curve Fitting</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Specifies the curve fitting attributes for the series. <!-- end-model-doc
	 * -->
	 * 
	 * @return the value of the '<em>Curve Fitting</em>' containment reference.
	 * @see #setCurveFitting(CurveFitting)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_CurveFitting()
	 * @model containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='CurveFitting'"
	 * @generated
	 */
	CurveFitting getCurveFitting();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getCurveFitting
	 * <em>Curve Fitting</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Curve Fitting</em>' containment
	 *              reference.
	 * @see #getCurveFitting()
	 * @generated
	 */
	void setCurveFitting(CurveFitting value);

	/**
	 * Returns the value of the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The element
	 * represents cursor for series area. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Cursor</em>' containment reference.
	 * @see #setCursor(Cursor)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getSeries_Cursor()
	 * @model containment="true" extendedMetaData="kind='element' name='Cursor'"
	 * @generated
	 */
	Cursor getCursor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Series#getCursor
	 * <em>Cursor</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Cursor</em>' containment reference.
	 * @see #getCursor()
	 * @generated
	 */
	void setCursor(Cursor value);

	/**
	 * Indicates if this series type may be rendered stacked with other series
	 * instances of the same type
	 * 
	 * NOTE: Manually written
	 * 
	 * @return true means this series can be stacked
	 */
	boolean canBeStacked();

	/**
	 * Indicates if this series graphic element may share a scale unit with other
	 * similar elements.
	 * 
	 * NOTE: Manually written
	 * 
	 * @return true means this series can share scale unit
	 */
	boolean canShareAxisUnit();

	/**
	 * This method should return true if the series type can be used in a
	 * combination chart.
	 * 
	 * NOTE: Manually written
	 * 
	 * @return true means this series can be used in combination chart
	 */
	boolean canParticipateInCombination();

	/**
	 * This method should modify the series instance to extract whatever information
	 * it can from the series provided...updating the model if necessary. This
	 * method should be overridden by each series type implementation in particular
	 * if the series type can participate in a combination. It will be called when
	 * converting from one series type to another.
	 * 
	 * The iSeriesDefinitionIndex value is the index of the series definition among
	 * all series definitions at the same level (base or orthogonal)
	 * 
	 * NOTE: Manually written
	 */
	void translateFrom(Series sourceSeries, int iSeriesDefinitionIndex, Chart chart);

	/**
	 * This method should return a user-friendly name for the series type.
	 * 
	 * NOTE: Manually written
	 * 
	 * @return display name for the series type
	 */
	String getDisplayName();

	/**
	 * Returns supported label positions scope in current series. Label position set
	 * in series must be in one of these types.
	 * 
	 * @param dimension chart dimension
	 * @return NameSet with supported label positions.
	 */
	NameSet getLabelPositionScope(ChartDimension dimension);

	/**
	 * Sets the value dataSet.
	 * 
	 * @param dataSet
	 */
	void setDataSet(DataSet dataSet);

	/**
	 * Returns the value dataSet.
	 * 
	 * @return value data set
	 */
	DataSet getDataSet();

	/**
	 * Sets the user dataSet.
	 * 
	 * @param userKey
	 * @param dataSet
	 */
	void setDataSet(String userKey, DataSet dataSet);

	/**
	 * Returns the user dataSet.
	 * 
	 * @param userkey
	 * @return user data set
	 */
	DataSet getDataSet(String userkey);

	/**
	 * The method indicates if painting requests of series should be added to a
	 * single cache.
	 * 
	 * @return <code>true</code> if painting requests of series should be added to a
	 *         single cache. False means creating a new Cache for each series.
	 */
	boolean isSingleCache();

	/**
	 * Returns the index array of data definitions which must be defined. For
	 * instance, if there are two data definitions in series, and the first one can
	 * be not defined, this method will return 1 in integer array; if two of them
	 * must be defined, this method will return 0 and 1 in integer array.
	 * 
	 * @return index array
	 * @since 2.6.1
	 */
	int[] getDefinedDataDefinitionIndex();

	/**
	 * @generated
	 */
	Series copyInstance();

} // Series
