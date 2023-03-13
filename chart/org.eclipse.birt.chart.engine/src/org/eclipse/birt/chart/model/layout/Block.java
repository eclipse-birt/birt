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

package org.eclipse.birt.chart.model.layout;

import java.util.Enumeration;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Block</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Block is the most important component to layout the
 * contents of a chart. It represents a rectangular area that bounds an
 * individual chart element, which is used to determine the layout of the chart.
 * The user can define its Bounds, Insets, Outline and Background among other
 * properties. A Block can also contain other Blocks as its children. Use
 * Block.getChildren( ) which returns a list of type Block to navigate through
 * the children hierarchy of Blocks.
 * <p xmlns="http://www.birt.eclipse.org/ChartModelLayout">
 * The most important block is the chart's block, all contents of a chart are
 * rendered inside this block, we can get the instance of a chart's block
 * using:<br/>
 * Chart.getBlock( );
 * </p>
 * A chart graphically consists of 3 parts, TitleBlock, Plot and Legend, which
 * are also sub-type of Block and organized as children of the chart's Block.
 * Besides the general approach of using Chart.getBlock( ).getChildren( ) we can
 * also access them using the convenient
 * methods:<br xmlns="http://www.birt.eclipse.org/ChartModelLayout"/>
 * Chart.getTitle( );<br xmlns="http://www.birt.eclipse.org/ChartModelLayout"/>
 * Chart.getPlot( );<br xmlns="http://www.birt.eclipse.org/ChartModelLayout"/>
 * Chart.getLegend( );<br xmlns="http://www.birt.eclipse.org/ChartModelLayout"/>
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getChildren
 * <em>Children</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getBounds
 * <em>Bounds</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getAnchor
 * <em>Anchor</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getStretch
 * <em>Stretch</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getInsets
 * <em>Insets</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getRow
 * <em>Row</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getColumn
 * <em>Column</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getRowspan
 * <em>Rowspan</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getColumnspan
 * <em>Columnspan</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getMinSize <em>Min
 * Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getBackground
 * <em>Background</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getWidthHint <em>Width
 * Hint</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getHeightHint <em>Height
 * Hint</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Block#getCursor
 * <em>Cursor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock()
 * @model extendedMetaData="name='Block' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Block extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.layout.Block}. <!-- begin-user-doc -->
	 * Gets the blocks contained within this block. <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Children()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Children'"
	 * @generated
	 */
	EList<Block> getChildren();

	/**
	 * Returns the value of the '<em><b>Bounds</b></em>' containment reference. <!--
	 * begin-user-doc --> Gets the bounds for the block. Bounds of a block are only
	 * used if it is added to a block that has the Null layout. <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 *
	 * Defines the position and size of the block. (These will usually be relative
	 * to the TLC of the container block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Bounds</em>' containment reference.
	 * @see #setBounds(Bounds)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Bounds()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Bounds getBounds();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getBounds <em>Bounds</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Bounds</em>' containment reference.
	 * @see #getBounds()
	 * @generated
	 */
	void setBounds(Bounds value);

	/**
	 * Returns the value of the '<em><b>Anchor</b></em>' attribute. The default
	 * value is <code>"North"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Anchor}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies where the content of the block is anchored.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Anchor</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see #isSetAnchor()
	 * @see #unsetAnchor()
	 * @see #setAnchor(Anchor)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Anchor()
	 * @model default="North" unique="false" unsettable="true"
	 * @generated
	 */
	Anchor getAnchor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getAnchor <em>Anchor</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Anchor</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see #isSetAnchor()
	 * @see #unsetAnchor()
	 * @see #getAnchor()
	 * @generated
	 */
	void setAnchor(Anchor value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getAnchor <em>Anchor</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetAnchor()
	 * @see #getAnchor()
	 * @see #setAnchor(Anchor)
	 * @generated
	 */
	void unsetAnchor();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getAnchor <em>Anchor</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Anchor</em>' attribute is set.
	 * @see #unsetAnchor()
	 * @see #getAnchor()
	 * @see #setAnchor(Anchor)
	 * @generated
	 */
	boolean isSetAnchor();

	/**
	 * Returns the value of the '<em><b>Stretch</b></em>' attribute. The default
	 * value is <code>"Horizontal"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Stretch}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies how the content of the block fills up available space.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Stretch</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @see #isSetStretch()
	 * @see #unsetStretch()
	 * @see #setStretch(Stretch)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Stretch()
	 * @model default="Horizontal" unique="false" unsettable="true"
	 * @generated
	 */
	Stretch getStretch();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getStretch
	 * <em>Stretch</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Stretch</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @see #isSetStretch()
	 * @see #unsetStretch()
	 * @see #getStretch()
	 * @generated
	 */
	void setStretch(Stretch value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getStretch
	 * <em>Stretch</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetStretch()
	 * @see #getStretch()
	 * @see #setStretch(Stretch)
	 * @generated
	 */
	void unsetStretch();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getStretch
	 * <em>Stretch</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Stretch</em>' attribute is set.
	 * @see #unsetStretch()
	 * @see #getStretch()
	 * @see #setStretch(Stretch)
	 * @generated
	 */
	boolean isSetStretch();

	/**
	 * Returns the value of the '<em><b>Insets</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the insets to be used for this block's contents.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Insets</em>' containment reference.
	 * @see #setInsets(Insets)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Insets()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Insets getInsets();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getInsets <em>Insets</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Insets</em>' containment reference.
	 * @see #getInsets()
	 * @generated
	 */
	void setInsets(Insets value);

	/**
	 * Returns the value of the '<em><b>Row</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the row index for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Row</em>' attribute.
	 * @see #isSetRow()
	 * @see #unsetRow()
	 * @see #setRow(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Row()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getRow();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRow <em>Row</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Row</em>' attribute.
	 * @see #isSetRow()
	 * @see #unsetRow()
	 * @see #getRow()
	 * @generated
	 */
	void setRow(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRow <em>Row</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetRow()
	 * @see #getRow()
	 * @see #setRow(int)
	 * @generated
	 */
	void unsetRow();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRow <em>Row</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Row</em>' attribute is set.
	 * @see #unsetRow()
	 * @see #getRow()
	 * @see #setRow(int)
	 * @generated
	 */
	boolean isSetRow();

	/**
	 * Returns the value of the '<em><b>Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the column index for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Column</em>' attribute.
	 * @see #isSetColumn()
	 * @see #unsetColumn()
	 * @see #setColumn(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Column()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getColumn();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumn <em>Column</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Column</em>' attribute.
	 * @see #isSetColumn()
	 * @see #unsetColumn()
	 * @see #getColumn()
	 * @generated
	 */
	void setColumn(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumn <em>Column</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetColumn()
	 * @see #getColumn()
	 * @see #setColumn(int)
	 * @generated
	 */
	void unsetColumn();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumn <em>Column</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Column</em>' attribute is set.
	 * @see #unsetColumn()
	 * @see #getColumn()
	 * @see #setColumn(int)
	 * @generated
	 */
	boolean isSetColumn();

	/**
	 * Returns the value of the '<em><b>Rowspan</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the rowspan value for the block. This specifies the
	 * number of rows that the block spans in its container's layout. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the rowspan value for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Rowspan</em>' attribute.
	 * @see #isSetRowspan()
	 * @see #unsetRowspan()
	 * @see #setRowspan(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Rowspan()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getRowspan();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRowspan
	 * <em>Rowspan</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Rowspan</em>' attribute.
	 * @see #isSetRowspan()
	 * @see #unsetRowspan()
	 * @see #getRowspan()
	 * @generated
	 */
	void setRowspan(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRowspan
	 * <em>Rowspan</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetRowspan()
	 * @see #getRowspan()
	 * @see #setRowspan(int)
	 * @generated
	 */
	void unsetRowspan();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getRowspan
	 * <em>Rowspan</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Rowspan</em>' attribute is set.
	 * @see #unsetRowspan()
	 * @see #getRowspan()
	 * @see #setRowspan(int)
	 * @generated
	 */
	boolean isSetRowspan();

	/**
	 * Returns the value of the '<em><b>Columnspan</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the columnspan value for the block. This specifies
	 * the number of columns that the block spans in its container's layout. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the columnspan value for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Columnspan</em>' attribute.
	 * @see #isSetColumnspan()
	 * @see #unsetColumnspan()
	 * @see #setColumnspan(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Columnspan()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getColumnspan();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumnspan
	 * <em>Columnspan</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Columnspan</em>' attribute.
	 * @see #isSetColumnspan()
	 * @see #unsetColumnspan()
	 * @see #getColumnspan()
	 * @generated
	 */
	void setColumnspan(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumnspan
	 * <em>Columnspan</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #isSetColumnspan()
	 * @see #getColumnspan()
	 * @see #setColumnspan(int)
	 * @generated
	 */
	void unsetColumnspan();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getColumnspan
	 * <em>Columnspan</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Columnspan</em>' attribute is set.
	 * @see #unsetColumnspan()
	 * @see #getColumnspan()
	 * @see #setColumnspan(int)
	 * @generated
	 */
	boolean isSetColumnspan();

	/**
	 * Returns the value of the '<em><b>Min Size</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the minimum size for the block. This used to
	 * determine the minimum size required to render this block without degradation
	 * in quality of output. It is used when the block is added to a container with
	 * Elastic layout. <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the minimum size for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Min Size</em>' containment reference.
	 * @see #setMinSize(Size)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_MinSize()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Size getMinSize();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getMinSize <em>Min
	 * Size</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Min Size</em>' containment reference.
	 * @see #getMinSize()
	 * @generated
	 */
	void setMinSize(Size value);

	/**
	 * Returns the value of the '<em><b>Outline</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the line properties of the border for the block.
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Defines the outline for the chart element.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Outline</em>' containment reference.
	 * @see #setOutline(LineAttributes)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Outline()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getOutline();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getOutline
	 * <em>Outline</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Outline</em>' containment reference.
	 * @see #getOutline()
	 * @generated
	 */
	void setOutline(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Background</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Background for the Block...Can be transparent, solid color, gradient, pattern
	 * OR image
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Background</em>' containment reference.
	 * @see #setBackground(Fill)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Background()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Fill getBackground();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getBackground
	 * <em>Background</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Background</em>' containment
	 *              reference.
	 * @see #getBackground()
	 * @generated
	 */
	void setBackground(Fill value);

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies whether the block is visible.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #isSetVisible()
	 * @see #unsetVisible()
	 * @see #setVisible(boolean)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Visible()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#isVisible
	 * <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * '{@link org.eclipse.birt.chart.model.layout.Block#isVisible
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
	 * '{@link org.eclipse.birt.chart.model.layout.Block#isVisible
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
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.Trigger}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds the actions for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Triggers()
	 * @model type="org.eclipse.birt.chart.model.data.Trigger" containment="true"
	 *        resolveProxies="false"
	 * @generated
	 */
	EList<Trigger> getTriggers();

	/**
	 * Returns the value of the '<em><b>Width Hint</b></em>' attribute. The default
	 * value is <code>"-1"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc --> WidthHint specifies a hinted width for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Width Hint</em>' attribute.
	 * @see #isSetWidthHint()
	 * @see #unsetWidthHint()
	 * @see #setWidthHint(double)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_WidthHint()
	 * @model default="-1" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='WidthHint'"
	 * @generated
	 */
	double getWidthHint();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getWidthHint <em>Width
	 * Hint</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Width Hint</em>' attribute.
	 * @see #isSetWidthHint()
	 * @see #unsetWidthHint()
	 * @see #getWidthHint()
	 * @generated
	 */
	void setWidthHint(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getWidthHint <em>Width
	 * Hint</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetWidthHint()
	 * @see #getWidthHint()
	 * @see #setWidthHint(double)
	 * @generated
	 */
	void unsetWidthHint();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getWidthHint <em>Width
	 * Hint</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Width Hint</em>' attribute is set.
	 * @see #unsetWidthHint()
	 * @see #getWidthHint()
	 * @see #setWidthHint(double)
	 * @generated
	 */
	boolean isSetWidthHint();

	/**
	 * Returns the value of the '<em><b>Height Hint</b></em>' attribute. The default
	 * value is <code>"-1"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc --> HeightHint pecifies a hinted height for the block.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Height Hint</em>' attribute.
	 * @see #isSetHeightHint()
	 * @see #unsetHeightHint()
	 * @see #setHeightHint(double)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_HeightHint()
	 * @model default="-1" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='HeightHint'"
	 * @generated
	 */
	double getHeightHint();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getHeightHint <em>Height
	 * Hint</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Height Hint</em>' attribute.
	 * @see #isSetHeightHint()
	 * @see #unsetHeightHint()
	 * @see #getHeightHint()
	 * @generated
	 */
	void setHeightHint(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getHeightHint <em>Height
	 * Hint</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetHeightHint()
	 * @see #getHeightHint()
	 * @see #setHeightHint(double)
	 * @generated
	 */
	void unsetHeightHint();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getHeightHint <em>Height
	 * Hint</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Height Hint</em>' attribute is set.
	 * @see #unsetHeightHint()
	 * @see #getHeightHint()
	 * @see #setHeightHint(double)
	 * @generated
	 */
	boolean isSetHeightHint();

	/**
	 * Returns the value of the '<em><b>Cursor</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Element
	 * "Cursor" represents cursor for the block. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Cursor</em>' containment reference.
	 * @see #setCursor(Cursor)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getBlock_Cursor()
	 * @model containment="true" extendedMetaData="kind='element' name='Cursor'"
	 * @generated
	 */
	Cursor getCursor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Block#getCursor <em>Cursor</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Cursor</em>' containment reference.
	 * @see #getCursor()
	 * @generated
	 */
	void setCursor(Cursor value);

	/**
	 * @param bRecursive
	 */
	Enumeration<Block> children(boolean bRecursive);

	/**
	 * Computes and returns the size of the title block based on the text content
	 * and its attributes
	 *
	 * @param xs  The Xserver used in computing the scaling factor (pixels =>
	 *            points)
	 * @param cm  The chart model instance (if needed)
	 * @param rtc
	 *
	 * @return The preferred size of the title block 'in points'
	 *
	 * @throws GenerationException
	 */
	Size getPreferredSize(IDisplayServer xs, Chart cm, RunTimeContext rtc) throws ChartException;

	/**
	 * @return 'true' if this block type is 'a custom defined block'
	 */
	boolean isCustom();

	/**
	 * @return 'true' if this block type is 'the plot block'
	 */
	boolean isPlot();

	/**
	 *
	 * @return 'true' if this block type is 'the legend block'
	 */
	boolean isLegend();

	/**
	 *
	 * @return 'true' if this block type is 'a text block'
	 */
	boolean isText();

	/**
	 * @return 'true' if this block type is 'the title block'
	 */
	boolean isTitle();

	/**
	 * Adds a block into the children hierarchy
	 *
	 * @param bl
	 */
	void add(Block bl);

	/**
	 * Removes a block from the children hierarchy
	 *
	 * @param bl
	 */
	void remove(Block bl);

	/**
	 * @generated
	 */
	@Override
	Block copyInstance();

} // Block
