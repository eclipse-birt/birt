/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.radar.model.type;

import java.math.BigInteger;

import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object ' <em><b>Radar
 * Series</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc --> This is a Series type that, during design time,
 * holds the query data for Line charts, and during run time, holds the value
 * for each data point in the line. When rendered, a line connects each data
 * point. <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getMarker
 * <em>Marker</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isPaletteLineColor
 * <em>Palette Line Color</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isBackgroundOvalTransparent
 * <em>Background Oval Transparent</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLineAttributes
 * <em>Web Line Attributes</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowWebLabels
 * <em>Show Web Labels</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowCatLabels
 * <em>Show Cat Labels</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isRadarAutoScale
 * <em>Radar Auto Scale</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMax
 * <em>Web Label Max</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMin
 * <em>Web Label Min</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelUnit
 * <em>Web Label Unit</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isFillPolys
 * <em>Fill Polys</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isConnectEndpoints
 * <em>Connect Endpoints</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabel
 * <em>Web Label</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabel
 * <em>Cat Label</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelFormatSpecifier
 * <em>Web Label Format Specifier</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabelFormatSpecifier
 * <em>Cat Label Format Specifier</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getPlotSteps
 * <em>Plot Steps</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries()
 * @model extendedMetaData="name='RadarSeries' kind='elementOnly'"
 * @generated
 */
public interface RadarSeries extends Series {

	/**
	 * Returns the value of the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the marker to be used for displaying the data point on the line in the chart.
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Marker</em>' containment reference.
	 * @see #setMarker(Marker)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_Marker()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Marker'"
	 * @generated
	 */
	Marker getMarker();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getMarker
	 * <em>Marker</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Marker</em>' containment reference.
	 * @see #getMarker()
	 * @generated
	 */
	void setMarker(Marker value);

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the attributes for the line used to represent this series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_LineAttributes()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='LineAttributes'"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getLineAttributes
	 * <em>Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Line Attributes</em>' containment
	 *              reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
	void setLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Indicates if use the series palette color to draw the line instead of the
	 * color in LineAttributes
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Palette Line Color</em>' attribute.
	 * @see #isSetPaletteLineColor()
	 * @see #unsetPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_PaletteLineColor()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='PaletteLineColor'"
	 * @generated
	 */
	boolean isPaletteLineColor();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Palette Line Color</em>' attribute.
	 * @see #isSetPaletteLineColor()
	 * @see #unsetPaletteLineColor()
	 * @see #isPaletteLineColor()
	 * @generated
	 */
	void setPaletteLineColor(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetPaletteLineColor()
	 * @see #isPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @generated
	 */
	void unsetPaletteLineColor();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Palette Line Color</em>' attribute is
	 *         set.
	 * @see #unsetPaletteLineColor()
	 * @see #isPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @generated
	 */
	boolean isSetPaletteLineColor();

	/**
	 * Returns the value of the '<em><b>Background Oval Transparent</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * If using background oval fills, should they be transparent.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Background Oval Transparent</em>' attribute.
	 * @see #isSetBackgroundOvalTransparent()
	 * @see #unsetBackgroundOvalTransparent()
	 * @see #setBackgroundOvalTransparent(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_BackgroundOvalTransparent()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='BackgroundOvalTransparent'"
	 * @generated
	 */
	boolean isBackgroundOvalTransparent();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isBackgroundOvalTransparent
	 * <em>Background Oval Transparent</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Background Oval Transparent</em>'
	 *              attribute.
	 * @see #isSetBackgroundOvalTransparent()
	 * @see #unsetBackgroundOvalTransparent()
	 * @see #isBackgroundOvalTransparent()
	 * @generated
	 */
	void setBackgroundOvalTransparent(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isBackgroundOvalTransparent
	 * <em>Background Oval Transparent</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isSetBackgroundOvalTransparent()
	 * @see #isBackgroundOvalTransparent()
	 * @see #setBackgroundOvalTransparent(boolean)
	 * @generated
	 */
	void unsetBackgroundOvalTransparent();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isBackgroundOvalTransparent
	 * <em>Background Oval Transparent</em>}' attribute is set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Background Oval Transparent</em>'
	 *         attribute is set.
	 * @see #unsetBackgroundOvalTransparent()
	 * @see #isBackgroundOvalTransparent()
	 * @see #setBackgroundOvalTransparent(boolean)
	 * @generated
	 */
	boolean isSetBackgroundOvalTransparent();

	/**
	 * Returns the value of the '<em><b>Web Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies spider grid Line Attributes.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Web Line Attributes</em>' containment
	 *         reference.
	 * @see #setWebLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_WebLineAttributes()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='WebLineAttributes'"
	 * @generated
	 */
	LineAttributes getWebLineAttributes();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLineAttributes
	 * <em>Web Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Web Line Attributes</em>' containment
	 *              reference.
	 * @see #getWebLineAttributes()
	 * @generated
	 */
	void setWebLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Show Web Labels</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether to show the spider grid labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Show Web Labels</em>' attribute.
	 * @see #isSetShowWebLabels()
	 * @see #unsetShowWebLabels()
	 * @see #setShowWebLabels(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_ShowWebLabels()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ShowWebLabels'"
	 * @generated
	 */
	boolean isShowWebLabels();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowWebLabels
	 * <em>Show Web Labels</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Show Web Labels</em>' attribute.
	 * @see #isSetShowWebLabels()
	 * @see #unsetShowWebLabels()
	 * @see #isShowWebLabels()
	 * @generated
	 */
	void setShowWebLabels(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowWebLabels
	 * <em>Show Web Labels</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetShowWebLabels()
	 * @see #isShowWebLabels()
	 * @see #setShowWebLabels(boolean)
	 * @generated
	 */
	void unsetShowWebLabels();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowWebLabels
	 * <em>Show Web Labels</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Show Web Labels</em>' attribute is set.
	 * @see #unsetShowWebLabels()
	 * @see #isShowWebLabels()
	 * @see #setShowWebLabels(boolean)
	 * @generated
	 */
	boolean isSetShowWebLabels();

	/**
	 * Returns the value of the '<em><b>Show Cat Labels</b></em>' attribute. The
	 * default value is <code>"true"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether to show the category grid labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Show Cat Labels</em>' attribute.
	 * @see #isSetShowCatLabels()
	 * @see #unsetShowCatLabels()
	 * @see #setShowCatLabels(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_ShowCatLabels()
	 * @model default="true" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ShowCatLabels'"
	 * @generated
	 */
	boolean isShowCatLabels();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowCatLabels
	 * <em>Show Cat Labels</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Show Cat Labels</em>' attribute.
	 * @see #isSetShowCatLabels()
	 * @see #unsetShowCatLabels()
	 * @see #isShowCatLabels()
	 * @generated
	 */
	void setShowCatLabels(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowCatLabels
	 * <em>Show Cat Labels</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetShowCatLabels()
	 * @see #isShowCatLabels()
	 * @see #setShowCatLabels(boolean)
	 * @generated
	 */
	void unsetShowCatLabels();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isShowCatLabels
	 * <em>Show Cat Labels</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Show Cat Labels</em>' attribute is set.
	 * @see #unsetShowCatLabels()
	 * @see #isShowCatLabels()
	 * @see #setShowCatLabels(boolean)
	 * @generated
	 */
	boolean isSetShowCatLabels();

	/**
	 * Returns the value of the '<em><b>Radar Auto Scale</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Indicates if use the series should automatically scale or use scale min max
	 * settings.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Radar Auto Scale</em>' attribute.
	 * @see #isSetRadarAutoScale()
	 * @see #unsetRadarAutoScale()
	 * @see #setRadarAutoScale(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_RadarAutoScale()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='RadarAutoScale'"
	 * @generated
	 */
	boolean isRadarAutoScale();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isRadarAutoScale
	 * <em>Radar Auto Scale</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Radar Auto Scale</em>' attribute.
	 * @see #isSetRadarAutoScale()
	 * @see #unsetRadarAutoScale()
	 * @see #isRadarAutoScale()
	 * @generated
	 */
	void setRadarAutoScale(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isRadarAutoScale
	 * <em>Radar Auto Scale</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetRadarAutoScale()
	 * @see #isRadarAutoScale()
	 * @see #setRadarAutoScale(boolean)
	 * @generated
	 */
	void unsetRadarAutoScale();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isRadarAutoScale
	 * <em>Radar Auto Scale</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Radar Auto Scale</em>' attribute is
	 *         set.
	 * @see #unsetRadarAutoScale()
	 * @see #isRadarAutoScale()
	 * @see #setRadarAutoScale(boolean)
	 * @generated
	 */
	boolean isSetRadarAutoScale();

	/**
	 * Returns the value of the '<em><b>Web Label Max</b></em>' attribute. The
	 * default value is <code>"100"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Web Label Scale Max
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Web Label Max</em>' attribute.
	 * @see #isSetWebLabelMax()
	 * @see #unsetWebLabelMax()
	 * @see #setWebLabelMax(double)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_WebLabelMax()
	 * @model default="100" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='WebLabelMax'"
	 * @generated
	 */
	double getWebLabelMax();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMax
	 * <em>Web Label Max</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Web Label Max</em>' attribute.
	 * @see #isSetWebLabelMax()
	 * @see #unsetWebLabelMax()
	 * @see #getWebLabelMax()
	 * @generated
	 */
	void setWebLabelMax(double value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMax
	 * <em>Web Label Max</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetWebLabelMax()
	 * @see #getWebLabelMax()
	 * @see #setWebLabelMax(double)
	 * @generated
	 */
	void unsetWebLabelMax();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMax
	 * <em>Web Label Max</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Web Label Max</em>' attribute is set.
	 * @see #unsetWebLabelMax()
	 * @see #getWebLabelMax()
	 * @see #setWebLabelMax(double)
	 * @generated
	 */
	boolean isSetWebLabelMax();

	/**
	 * Returns the value of the '<em><b>Web Label Min</b></em>' attribute. The
	 * default value is <code>"0"</code>. <!-- begin-user-doc --> <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 * 
	 * Web Label Scale Min
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Web Label Min</em>' attribute.
	 * @see #isSetWebLabelMin()
	 * @see #unsetWebLabelMin()
	 * @see #setWebLabelMin(double)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_WebLabelMin()
	 * @model default="0" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='WebLabelMin'"
	 * @generated
	 */
	double getWebLabelMin();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMin
	 * <em>Web Label Min</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Web Label Min</em>' attribute.
	 * @see #isSetWebLabelMin()
	 * @see #unsetWebLabelMin()
	 * @see #getWebLabelMin()
	 * @generated
	 */
	void setWebLabelMin(double value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMin
	 * <em>Web Label Min</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetWebLabelMin()
	 * @see #getWebLabelMin()
	 * @see #setWebLabelMin(double)
	 * @generated
	 */
	void unsetWebLabelMin();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelMin
	 * <em>Web Label Min</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Web Label Min</em>' attribute is set.
	 * @see #unsetWebLabelMin()
	 * @see #getWebLabelMin()
	 * @see #setWebLabelMin(double)
	 * @generated
	 */
	boolean isSetWebLabelMin();

	/**
	 * Returns the value of the '<em><b>Web Label Unit</b></em>' attribute. The
	 * default value is <code>"%"</code>. <!-- begin-user-doc --> <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 * 
	 * Web Label Scale Unit
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Web Label Unit</em>' attribute.
	 * @see #isSetWebLabelUnit()
	 * @see #unsetWebLabelUnit()
	 * @see #setWebLabelUnit(String)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_WebLabelUnit()
	 * @model default="%" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='WebLabelUnit'"
	 * @generated
	 */
	String getWebLabelUnit();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelUnit
	 * <em>Web Label Unit</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Web Label Unit</em>' attribute.
	 * @see #isSetWebLabelUnit()
	 * @see #unsetWebLabelUnit()
	 * @see #getWebLabelUnit()
	 * @generated
	 */
	void setWebLabelUnit(String value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelUnit
	 * <em>Web Label Unit</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetWebLabelUnit()
	 * @see #getWebLabelUnit()
	 * @see #setWebLabelUnit(String)
	 * @generated
	 */
	void unsetWebLabelUnit();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelUnit
	 * <em>Web Label Unit</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Web Label Unit</em>' attribute is set.
	 * @see #unsetWebLabelUnit()
	 * @see #getWebLabelUnit()
	 * @see #setWebLabelUnit(String)
	 * @generated
	 */
	boolean isSetWebLabelUnit();

	/**
	 * Returns the value of the '<em><b>Fill Polys</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Should polygons be filled or just use lines.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Fill Polys</em>' attribute.
	 * @see #isSetFillPolys()
	 * @see #unsetFillPolys()
	 * @see #setFillPolys(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_FillPolys()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element' name='FillPolys'"
	 * @generated
	 */
	boolean isFillPolys();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isFillPolys
	 * <em>Fill Polys</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Fill Polys</em>' attribute.
	 * @see #isSetFillPolys()
	 * @see #unsetFillPolys()
	 * @see #isFillPolys()
	 * @generated
	 */
	void setFillPolys(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isFillPolys
	 * <em>Fill Polys</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetFillPolys()
	 * @see #isFillPolys()
	 * @see #setFillPolys(boolean)
	 * @generated
	 */
	void unsetFillPolys();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isFillPolys
	 * <em>Fill Polys</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Fill Polys</em>' attribute is set.
	 * @see #unsetFillPolys()
	 * @see #isFillPolys()
	 * @see #setFillPolys(boolean)
	 * @generated
	 */
	boolean isSetFillPolys();

	/**
	 * Returns the value of the '<em><b>Connect Endpoints</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * If using unfilled polys should first and last data points be connected.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Connect Endpoints</em>' attribute.
	 * @see #isSetConnectEndpoints()
	 * @see #unsetConnectEndpoints()
	 * @see #setConnectEndpoints(boolean)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_ConnectEndpoints()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='ConnectEndpoints'"
	 * @generated
	 */
	boolean isConnectEndpoints();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isConnectEndpoints
	 * <em>Connect Endpoints</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Connect Endpoints</em>' attribute.
	 * @see #isSetConnectEndpoints()
	 * @see #unsetConnectEndpoints()
	 * @see #isConnectEndpoints()
	 * @generated
	 */
	void setConnectEndpoints(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isConnectEndpoints
	 * <em>Connect Endpoints</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetConnectEndpoints()
	 * @see #isConnectEndpoints()
	 * @see #setConnectEndpoints(boolean)
	 * @generated
	 */
	void unsetConnectEndpoints();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#isConnectEndpoints
	 * <em>Connect Endpoints</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Connect Endpoints</em>' attribute is
	 *         set.
	 * @see #unsetConnectEndpoints()
	 * @see #isConnectEndpoints()
	 * @see #setConnectEndpoints(boolean)
	 * @generated
	 */
	boolean isSetConnectEndpoints();

	/**
	 * Returns the value of the '<em><b>Web Label</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the properties for web labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Web Label</em>' containment reference.
	 * @see #setWebLabel(Label)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_WebLabel()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='WebLabel'"
	 * @generated
	 */
	Label getWebLabel();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabel
	 * <em>Web Label</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Web Label</em>' containment reference.
	 * @see #getWebLabel()
	 * @generated
	 */
	void setWebLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Cat Label</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the properties for category labels.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Cat Label</em>' containment reference.
	 * @see #setCatLabel(Label)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_CatLabel()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='CatLabel'"
	 * @generated
	 */
	Label getCatLabel();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabel
	 * <em>Cat Label</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Cat Label</em>' containment reference.
	 * @see #getCatLabel()
	 * @generated
	 */
	void setCatLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Web Label Format Specifier</b></em>'
	 * containment reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Web Label Format Specifier</em>' containment
	 * reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Web Label Format Specifier</em>' containment
	 *         reference.
	 * @see #setWebLabelFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_WebLabelFormatSpecifier()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='WebLabelFormatSpecifier'"
	 * @generated
	 */
	FormatSpecifier getWebLabelFormatSpecifier();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getWebLabelFormatSpecifier
	 * <em>Web Label Format Specifier</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Web Label Format Specifier</em>'
	 *              containment reference.
	 * @see #getWebLabelFormatSpecifier()
	 * @generated
	 */
	void setWebLabelFormatSpecifier(FormatSpecifier value);

	/**
	 * Returns the value of the '<em><b>Cat Label Format Specifier</b></em>'
	 * containment reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cat Label Format Specifier</em>' containment
	 * reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Cat Label Format Specifier</em>' containment
	 *         reference.
	 * @see #setCatLabelFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_CatLabelFormatSpecifier()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='CatLabelFormatSpecifier'"
	 * @generated
	 */
	FormatSpecifier getCatLabelFormatSpecifier();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getCatLabelFormatSpecifier
	 * <em>Cat Label Format Specifier</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Cat Label Format Specifier</em>'
	 *              containment reference.
	 * @see #getCatLabelFormatSpecifier()
	 * @generated
	 */
	void setCatLabelFormatSpecifier(FormatSpecifier value);

	/**
	 * Returns the value of the '<em><b>Plot Steps</b></em>' attribute. The default
	 * value is <code>"5"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Defines how many steps are in the web/radar
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Plot Steps</em>' attribute.
	 * @see #isSetPlotSteps()
	 * @see #unsetPlotSteps()
	 * @see #setPlotSteps(BigInteger)
	 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage#getRadarSeries_PlotSteps()
	 * @model default="5" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Integer" required="true"
	 *        extendedMetaData="kind='element' name='PlotSteps'"
	 * @generated
	 */
	BigInteger getPlotSteps();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getPlotSteps
	 * <em>Plot Steps</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Plot Steps</em>' attribute.
	 * @see #isSetPlotSteps()
	 * @see #unsetPlotSteps()
	 * @see #getPlotSteps()
	 * @generated
	 */
	void setPlotSteps(BigInteger value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getPlotSteps
	 * <em>Plot Steps</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetPlotSteps()
	 * @see #getPlotSteps()
	 * @see #setPlotSteps(BigInteger)
	 * @generated
	 */
	void unsetPlotSteps();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.examples.radar.model.type.RadarSeries#getPlotSteps
	 * <em>Plot Steps</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Plot Steps</em>' attribute is set.
	 * @see #unsetPlotSteps()
	 * @see #getPlotSteps()
	 * @see #setPlotSteps(BigInteger)
	 * @generated
	 */
	boolean isSetPlotSteps();

	/**
	 * @generated
	 */
	RadarSeries copyInstance();

} // RadarSeries
