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

import java.util.List;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Series
 * Definition</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type represents design-time definition for a series.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery
 * <em>Query</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette
 * <em>Series Palette</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesDefinitions
 * <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeries
 * <em>Series</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping
 * <em>Grouping</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting
 * <em>Sorting</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortKey
 * <em>Sort Key</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortLocale
 * <em>Sort Locale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortStrength
 * <em>Sort Strength</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getZOrder
 * <em>ZOrder</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition()
 * @model extendedMetaData="name='SeriesDefinition' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface SeriesDefinition extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Query</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Query</em>' containment reference.
	 * @see #setQuery(Query)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Query()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Query'"
	 * @generated
	 */
	Query getQuery();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery
	 * <em>Query</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Query</em>' containment reference.
	 * @see #getQuery()
	 * @generated
	 */
	void setQuery(Query value);

	/**
	 * Returns the value of the '<em><b>Series Palette</b></em>' containment
	 * reference. <!-- begin-user-doc --> Gets the palette associated with the
	 * series definiton instance. This palette will be used to determine the
	 * sequence of colors for the series that are represented by this definition.
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Series Palette</em>' containment reference.
	 * @see #setSeriesPalette(Palette)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SeriesPalette()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='SeriesPalette'"
	 * @generated
	 */
	Palette getSeriesPalette();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette
	 * <em>Series Palette</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Series Palette</em>' containment
	 *              reference.
	 * @see #getSeriesPalette()
	 * @generated
	 */
	void setSeriesPalette(Palette value);

	/**
	 * Returns the value of the '<em><b>Series Definitions</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.SeriesDefinition}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Series Definitions</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SeriesDefinitions()
	 * @model type="org.eclipse.birt.chart.model.data.SeriesDefinition"
	 *        containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='SeriesDefinitions'"
	 * @generated
	 */
	EList<SeriesDefinition> getSeriesDefinitions();

	/**
	 * Returns the value of the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Format Specifier</em>' containment reference.
	 * @see #setFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_FormatSpecifier()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='FormatSpecifier'"
	 * @generated
	 */
	FormatSpecifier getFormatSpecifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier
	 * <em>Format Specifier</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Format Specifier</em>' containment
	 *              reference.
	 * @see #getFormatSpecifier()
	 * @generated
	 */
	void setFormatSpecifier(FormatSpecifier value);

	/**
	 * Returns the value of the '<em><b>Series</b></em>' containment reference list.
	 * The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.Series}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds all the series for the chart.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Series</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Series()
	 * @model type="org.eclipse.birt.chart.model.component.Series"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList<Series> getSeries();

	/**
	 * Returns the value of the '<em><b>Grouping</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines if and how the series data is to be grouped for display. This should
	 * only be applied to Base Series.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Grouping</em>' containment reference.
	 * @see #setGrouping(SeriesGrouping)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Grouping()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	SeriesGrouping getGrouping();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping
	 * <em>Grouping</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Grouping</em>' containment reference.
	 * @see #getGrouping()
	 * @generated
	 */
	void setGrouping(SeriesGrouping value);

	/**
	 * Returns the value of the '<em><b>Sorting</b></em>' attribute. The default
	 * value is <code>"Ascending"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.SortOption}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines if and how the series data is to be sorted for display. This should
	 * only be applied to Base Series.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Sorting</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see #isSetSorting()
	 * @see #unsetSorting()
	 * @see #setSorting(SortOption)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Sorting()
	 * @model default="Ascending" unique="false" unsettable="true"
	 * @generated
	 */
	SortOption getSorting();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting
	 * <em>Sorting</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Sorting</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see #isSetSorting()
	 * @see #unsetSorting()
	 * @see #getSorting()
	 * @generated
	 */
	void setSorting(SortOption value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting
	 * <em>Sorting</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetSorting()
	 * @see #getSorting()
	 * @see #setSorting(SortOption)
	 * @generated
	 */
	void unsetSorting();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting
	 * <em>Sorting</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Sorting</em>' attribute is set.
	 * @see #unsetSorting()
	 * @see #getSorting()
	 * @see #setSorting(SortOption)
	 * @generated
	 */
	boolean isSetSorting();

	/**
	 * Returns the value of the '<em><b>Sort Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sort Key</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Sort Key</em>' containment reference.
	 * @see #setSortKey(Query)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SortKey()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='SortKey'"
	 * @generated
	 */
	Query getSortKey();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortKey
	 * <em>Sort Key</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Sort Key</em>' containment reference.
	 * @see #getSortKey()
	 * @generated
	 */
	void setSortKey(Query value);

	/**
	 * Returns the value of the '<em><b>Sort Locale</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the locale on which the sorting will base, the value is the locale ID of
	 * <code xmlns=
	 * "http://www.birt.eclipse.org/ChartModelData">com.ibm.icu.util.ULocale</code>.
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Sort Locale</em>' attribute.
	 * @see #setSortLocale(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SortLocale()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='SortLocale'"
	 * @generated
	 */
	String getSortLocale();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortLocale
	 * <em>Sort Locale</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Sort Locale</em>' attribute.
	 * @see #getSortLocale()
	 * @generated
	 */
	void setSortLocale(String value);

	/**
	 * Returns the value of the '<em><b>Sort Strength</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the strength of sorting, this attribute just uses the field values defined in
	 * <code xmlns=
	 * "http://www.birt.eclipse.org/ChartModelData">com.ibm.icu.text.Collator</code>.
	 * The available values are ASCII=-1, PRIMARY=0,
	 * SECONDARY=1,TERTIARY=2,QUATERNARY=3,IDENTICAL=15. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Sort Strength</em>' attribute.
	 * @see #isSetSortStrength()
	 * @see #unsetSortStrength()
	 * @see #setSortStrength(int)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SortStrength()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        required="true" extendedMetaData="kind='element' name='SortStrength'"
	 * @generated
	 */
	int getSortStrength();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortStrength
	 * <em>Sort Strength</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Sort Strength</em>' attribute.
	 * @see #isSetSortStrength()
	 * @see #unsetSortStrength()
	 * @see #getSortStrength()
	 * @generated
	 */
	void setSortStrength(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortStrength
	 * <em>Sort Strength</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #isSetSortStrength()
	 * @see #getSortStrength()
	 * @see #setSortStrength(int)
	 * @generated
	 */
	void unsetSortStrength();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSortStrength
	 * <em>Sort Strength</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Sort Strength</em>' attribute is set.
	 * @see #unsetSortStrength()
	 * @see #getSortStrength()
	 * @see #setSortStrength(int)
	 * @generated
	 */
	boolean isSetSortStrength();

	/**
	 * Returns the value of the '<em><b>ZOrder</b></em>' attribute. The default
	 * value is <code>"0"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Specifies Z order among multiple series renderers.
	 * Default value is 0. The series renderer with higher z order value will
	 * overlay the one with lower value. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>ZOrder</em>' attribute.
	 * @see #isSetZOrder()
	 * @see #unsetZOrder()
	 * @see #setZOrder(int)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_ZOrder()
	 * @model default="0" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='ZOrder'"
	 * @generated
	 */
	int getZOrder();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getZOrder
	 * <em>ZOrder</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>ZOrder</em>' attribute.
	 * @see #isSetZOrder()
	 * @see #unsetZOrder()
	 * @see #getZOrder()
	 * @generated
	 */
	void setZOrder(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getZOrder
	 * <em>ZOrder</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetZOrder()
	 * @see #getZOrder()
	 * @see #setZOrder(int)
	 * @generated
	 */
	void unsetZOrder();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getZOrder
	 * <em>ZOrder</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return whether the value of the '<em>ZOrder</em>' attribute is set.
	 * @see #unsetZOrder()
	 * @see #getZOrder()
	 * @see #setZOrder(int)
	 * @generated
	 */
	boolean isSetZOrder();

	/**
	 *
	 * @return The design-time series associated with the series definition
	 */
	Series getDesignTimeSeries();

	/**
	 *
	 * @return The runtime-time series' associated with the series definition
	 */
	List<Series> getRunTimeSeries();

	/**
	 *
	 */
	SeriesDefinition[] EMPTY_ARRAY = {};

	/**
	 * @generated
	 */
	@Override
	SeriesDefinition copyInstance();

} // SeriesDefinition
