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

package org.eclipse.birt.chart.model;

import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.SeriesHint;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Chart</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This is the basic type from which all charts should be extended. It defines the basic elements that are expected to
 * be present for all charts.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getVersion <em>Version</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getSubType <em>Sub Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getDescription <em>Description</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getBlock <em>Block</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getDimension <em>Dimension</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getScript <em>Script</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getUnits <em>Units</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getSeriesThickness <em>Series Thickness</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getSeriesHints <em>Series Hints</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getGridColumnCount <em>Grid Column Count</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getExtendedProperties <em>Extended Properties</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.Chart#getSampleData <em>Sample Data</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.ModelPackage#getChart()
 * @model
 * @generated
 */
public interface Chart extends EObject
{

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Specifies the version number of this chart. (This will be linked to the version number of the chart library.)
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Version()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getVersion <em>Version</em>}' attribute. <!--
     * begin-user-doc --> Specifies the version number of this chart. (This will be linked to the version number of the
     * chart library.) <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> Gets the type of this
     * chart. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the type of this chart.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Type()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getType <em>Type</em>}' attribute. <!--
     * begin-user-doc --> Sets the type of this chart. This is primarily used in the UI to enable re-entrant dialogs.
     * <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Returns the value of the '<em><b>Sub Type</b></em>' attribute. <!-- begin-user-doc --> Gets the sub-type of
     * this chart. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the sub-type of this chart.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Sub Type</em>' attribute.
     * @see #setSubType(String)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_SubType()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getSubType();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getSubType <em>Sub Type</em>}' attribute. <!--
     * begin-user-doc --> Sets the sub-type of this chart. This is primarily used in the UI to enable re-entrant
     * dialogs. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Sub Type</em>' attribute.
     * @see #getSubType()
     * @generated
     */
    void setSubType(String value);

    /**
     * Returns the value of the '<em><b>Description</b></em>' containment reference. <!-- begin-user-doc --> Gets
     * the description string for the chart. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Description of the chart...could be used as tooltip value for example.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Description</em>' containment reference.
     * @see #setDescription(Text)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Description()
     * @model containment="true" resolveProxies="false"
     * @generated
     */
    Text getDescription();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getDescription <em>Description</em>}'
     * containment reference. <!-- begin-user-doc --> Sets the description string for the chart. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Description</em>' containment reference.
     * @see #getDescription()
     * @generated
     */
    void setDescription(Text value);

    /**
     * Returns the value of the '<em><b>Block</b></em>' containment reference. <!-- begin-user-doc --> Gets the
     * {@link org.eclipse.birt.chart.model.layout.Block}instance for the chart. <!-- end-user-doc --> <!--
     * begin-model-doc -->
     * 
     * Defines a virtual entity that is independent of any chart element, but contains them and can be moved around in
     * the chart area for rendering.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Block</em>' containment reference.
     * @see #setBlock(Block)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Block()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    Block getBlock();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getBlock <em>Block</em>}' containment
     * reference. <!-- begin-user-doc --> Sets the {@link org.eclipse.birt.chart.model.layout.Block}instance
     * representing the chart as a whole. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Block</em>' containment reference.
     * @see #getBlock()
     * @generated
     */
    void setBlock(Block value);

    /**
     * Returns the value of the '<em><b>Dimension</b></em>' attribute. The default value is
     * <code>"Two_Dimensional"</code>. The literals are from the enumeration
     * {@link org.eclipse.birt.chart.model.attribute.ChartDimension}. <!-- begin-user-doc --> Gets the dimensions for
     * the chart. The dimensions here are essentially an indicator of how the chart will appear when rendered and do not
     * have a bearing on the number of variables or facets to the data. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Defines the dimensions the chart has.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Dimension</em>' attribute.
     * @see org.eclipse.birt.chart.model.attribute.ChartDimension
     * @see #isSetDimension()
     * @see #unsetDimension()
     * @see #setDimension(ChartDimension)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Dimension()
     * @model default="Two_Dimensional" unique="false" unsettable="true" required="true"
     * @generated
     */
    ChartDimension getDimension();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getDimension <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc --> Sets the dimensions for this chart. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Dimension</em>' attribute.
     * @see org.eclipse.birt.chart.model.attribute.ChartDimension
     * @see #isSetDimension()
     * @see #unsetDimension()
     * @see #getDimension()
     * @generated
     */
    void setDimension(ChartDimension value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.Chart#getDimension <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetDimension()
     * @see #getDimension()
     * @see #setDimension(ChartDimension)
     * @generated
     */
    void unsetDimension();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.Chart#getDimension <em>Dimension</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Dimension</em>' attribute is set.
     * @see #unsetDimension()
     * @see #getDimension()
     * @see #setDimension(ChartDimension)
     * @generated
     */
    boolean isSetDimension();

    /**
     * Returns the value of the '<em><b>Script</b></em>' attribute. <!-- begin-user-doc --> Gets the script text
     * associated with the chart. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Holds the script for the chart.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Script</em>' attribute.
     * @see #setScript(String)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Script()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getScript();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getScript <em>Script</em>}' attribute. <!--
     * begin-user-doc --> Sets the script text associated with the chart. For the script contents to be used at runtime
     * a mechanism will need to be in place that understands and can evaluate the contents of the script. <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Script</em>' attribute.
     * @see #getScript()
     * @generated
     */
    void setScript(String value);

    /**
     * Returns the value of the '<em><b>Units</b></em>' attribute. <!-- begin-user-doc --> Gets the Units of
     * Measurement being used for this chart. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Holds the units of measurement for the model.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Units</em>' attribute.
     * @see #setUnits(String)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_Units()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     * @generated
     */
    String getUnits();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getUnits <em>Units</em>}' attribute. <!--
     * begin-user-doc --> Sets the Units of Measurement to be used for this chart. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Units</em>' attribute.
     * @see #getUnits()
     * @generated
     */
    void setUnits(String value);

    /**
     * Returns the value of the '<em><b>Series Thickness</b></em>' attribute. <!-- begin-user-doc --> Gets the
     * series thickness as a percentage. This is the thickness used to render the shape representing the series when the
     * chart is rendered in 3D or 2D with Depth formats. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the thickness to be used while rendering the chart with depth or in 3D.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Series Thickness</em>' attribute.
     * @see #isSetSeriesThickness()
     * @see #unsetSeriesThickness()
     * @see #setSeriesThickness(double)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_SeriesThickness()
     * @model unique="false" unsettable="true" dataType="org.eclipse.birt.chart.model.attribute.Percentage"
     * @generated
     */
    double getSeriesThickness();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getSeriesThickness <em>Series Thickness</em>}'
     * attribute. <!-- begin-user-doc --> Sets the series thickness as a percentage. This is the thickness used to
     * render the shape representing the series when the chart is rendered in 3D or 2D with Depth formats. <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Series Thickness</em>' attribute.
     * @see #isSetSeriesThickness()
     * @see #unsetSeriesThickness()
     * @see #getSeriesThickness()
     * @generated
     */
    void setSeriesThickness(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.Chart#getSeriesThickness <em>Series Thickness</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetSeriesThickness()
     * @see #getSeriesThickness()
     * @see #setSeriesThickness(double)
     * @generated
     */
    void unsetSeriesThickness();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.Chart#getSeriesThickness <em>Series Thickness</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Series Thickness</em>' attribute is set.
     * @see #unsetSeriesThickness()
     * @see #getSeriesThickness()
     * @see #setSeriesThickness(double)
     * @generated
     */
    boolean isSetSeriesThickness();

    /**
     * Returns the value of the '<em><b>Series Hints</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Defines 'Hints' that should be used to process new series instances. (Intended primarily for use in the UI).
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Series Hints</em>' containment reference.
     * @see #setSeriesHints(SeriesHint)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_SeriesHints()
     * @model containment="true" resolveProxies="false"
     * @generated
     */
    SeriesHint getSeriesHints();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getSeriesHints <em>Series Hints</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Series Hints</em>' containment reference.
     * @see #getSeriesHints()
     * @generated
     */
    void setSeriesHints(SeriesHint value);

    /**
     * Returns the value of the '<em><b>Grid Column Count</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the number of columns to use if multiple series are to be plotted in a single chart.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Grid Column Count</em>' attribute.
     * @see #isSetGridColumnCount()
     * @see #unsetGridColumnCount()
     * @see #setGridColumnCount(int)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_GridColumnCount()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
     * @generated
     */
    int getGridColumnCount();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getGridColumnCount <em>Grid Column Count</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Grid Column Count</em>' attribute.
     * @see #isSetGridColumnCount()
     * @see #unsetGridColumnCount()
     * @see #getGridColumnCount()
     * @generated
     */
    void setGridColumnCount(int value);

    /**
     * Unsets the value of the '
     * {@link org.eclipse.birt.chart.model.Chart#getGridColumnCount <em>Grid Column Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetGridColumnCount()
     * @see #getGridColumnCount()
     * @see #setGridColumnCount(int)
     * @generated
     */
    void unsetGridColumnCount();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.Chart#getGridColumnCount <em>Grid Column Count</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Grid Column Count</em>' attribute is set.
     * @see #unsetGridColumnCount()
     * @see #getGridColumnCount()
     * @see #setGridColumnCount(int)
     * @generated
     */
    boolean isSetGridColumnCount();

    /**
     * Returns the value of the '<em><b>Extended Properties</b></em>' containment reference list. The list contents
     * are of type {@link org.eclipse.birt.chart.model.attribute.ExtendedProperty}. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Holds a generic list of properties (as name-value pairs) for minor extensions to the chart. Preferred way to add
     * properties remains through modification of schema.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Extended Properties</em>' containment reference list.
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_ExtendedProperties()
     * @model type="org.eclipse.birt.chart.model.attribute.ExtendedProperty" containment="true" resolveProxies="false"
     *        required="true"
     * @generated
     */
    EList getExtendedProperties();

    /**
     * Returns the value of the '<em><b>Sample Data</b></em>' containment reference. <!-- begin-user-doc --> Gets
     * the sample data for the chart. The sample data is used to display the chart at design-time e.g. in the Preview
     * window of the Chart Builder. <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Holds a collection of sample values for use by the chart. This type is likely to be highly version dependent.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Sample Data</em>' containment reference.
     * @see #setSampleData(SampleData)
     * @see org.eclipse.birt.chart.model.ModelPackage#getChart_SampleData()
     * @model containment="true" resolveProxies="false"
     * @generated
     */
    SampleData getSampleData();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.Chart#getSampleData <em>Sample Data</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Sample Data</em>' containment reference.
     * @see #getSampleData()
     * @generated
     */
    void setSampleData(SampleData value);

    /**
     * Convenience method that gets the Legend Block for the chart.
     * 
     * Note: Manually written
     * 
     * @return Legend The layout block that represents the legend area in the chart.
     */
    Legend getLegend();

    /**
     * Convenience method that gets the Plot Block for the chart.
     * 
     * Note: Manually written
     * 
     * @return Plot The layout block that represents the plot area in the chart.
     */
    Plot getPlot();

    /**
     * Convenience method that gets the Title Block for the chart.
     * 
     * Note: Manually written
     * 
     * @return TitleBlock The layout block that represents the title area in the chart.
     */
    TitleBlock getTitle();

    /**
     * This method returns all series whose captions/markers are to be rendered in the Legend content
     * 
     * @return
     */
    SeriesDefinition[] getSeriesForLegend();

    /**
     * This method walks through the model and clears specific model sections that are not required at deployment time.
     * 
     * @param iSectionType
     *            Possible values are IConstants.RUN_TIME and IConstants.USER_INTERFACE
     */
    void clearSections(int iSectionType);
    
    /**
     * 
     * @param sh
     */
    void setScriptHandler(ScriptHandler sh);
    
    /**
     * 
     * @return
     */
    ScriptHandler getScriptHandler();
    
    /**
     * Builds runtime series instances for each design-time series based on the sample data
     * contained in the model 
     */
    void createSampleRuntimeSeries();
} // Chart
