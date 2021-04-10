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

package org.eclipse.birt.chart.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.layout.impl.BlockImpl;
import org.eclipse.birt.chart.model.layout.impl.LegendImpl;
import org.eclipse.birt.chart.model.layout.impl.PlotImpl;
import org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Chart</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getVersion
 * <em>Version</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getSubType <em>Sub
 * Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getDescription
 * <em>Description</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getBlock
 * <em>Block</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getDimension
 * <em>Dimension</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getScript
 * <em>Script</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getUnits
 * <em>Units</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getSeriesThickness
 * <em>Series Thickness</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getGridColumnCount
 * <em>Grid Column Count</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getExtendedProperties
 * <em>Extended Properties</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getSampleData
 * <em>Sample Data</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getStyles
 * <em>Styles</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getInteractivity
 * <em>Interactivity</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartImpl#getEmptyMessage
 * <em>Empty Message</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChartImpl extends EObjectImpl implements Chart {

	/**
	 * logger for this class
	 */
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/model.impl"); //$NON-NLS-1$

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "1.0.0"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * This is true if the Version attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean versionESet;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSubType() <em>Sub Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSubType()
	 * @generated
	 * @ordered
	 */
	protected static final String SUB_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSubType() <em>Sub Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSubType()
	 * @generated
	 * @ordered
	 */
	protected String subType = SUB_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected Text description;

	/**
	 * The cached value of the '{@link #getBlock() <em>Block</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBlock()
	 * @generated
	 * @ordered
	 */
	protected Block block;

	/**
	 * The default value of the '{@link #getDimension() <em>Dimension</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDimension()
	 * @generated
	 * @ordered
	 */
	protected static final ChartDimension DIMENSION_EDEFAULT = ChartDimension.TWO_DIMENSIONAL_LITERAL;

	/**
	 * The cached value of the '{@link #getDimension() <em>Dimension</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDimension()
	 * @generated
	 * @ordered
	 */
	protected ChartDimension dimension = DIMENSION_EDEFAULT;

	/**
	 * This is true if the Dimension attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean dimensionESet;

	/**
	 * The default value of the '{@link #getScript() <em>Script</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getScript()
	 * @generated
	 * @ordered
	 */
	protected static final String SCRIPT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getScript() <em>Script</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getScript()
	 * @generated
	 * @ordered
	 */
	protected String script = SCRIPT_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnits() <em>Units</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnits()
	 * @generated
	 * @ordered
	 */
	protected static final String UNITS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnits()
	 * @generated
	 * @ordered
	 */
	protected String units = UNITS_EDEFAULT;

	/**
	 * The default value of the ' {@link #getSeriesThickness() <em>Series
	 * Thickness</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSeriesThickness()
	 * @generated
	 * @ordered
	 */
	protected static final double SERIES_THICKNESS_EDEFAULT = 10.0;

	/**
	 * The cached value of the ' {@link #getSeriesThickness() <em>Series
	 * Thickness</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSeriesThickness()
	 * @generated
	 * @ordered
	 */
	protected double seriesThickness = SERIES_THICKNESS_EDEFAULT;

	/**
	 * This is true if the Series Thickness attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean seriesThicknessESet;

	/**
	 * The default value of the '{@link #getGridColumnCount() <em>Grid Column
	 * Count</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGridColumnCount()
	 * @generated
	 * @ordered
	 */
	protected static final int GRID_COLUMN_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getGridColumnCount() <em>Grid Column
	 * Count</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGridColumnCount()
	 * @generated
	 * @ordered
	 */
	protected int gridColumnCount = GRID_COLUMN_COUNT_EDEFAULT;

	/**
	 * This is true if the Grid Column Count attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean gridColumnCountESet;

	/**
	 * The cached value of the '{@link #getExtendedProperties() <em>Extended
	 * Properties</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getExtendedProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<ExtendedProperty> extendedProperties;

	/**
	 * The cached value of the '{@link #getSampleData() <em>Sample Data</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSampleData()
	 * @generated
	 * @ordered
	 */
	protected SampleData sampleData;

	/**
	 * The cached value of the '{@link #getStyles() <em>Styles</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStyles()
	 * @generated
	 * @ordered
	 */
	protected EList<StyleMap> styles;

	/**
	 * The cached value of the '{@link #getInteractivity() <em>Interactivity</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInteractivity()
	 * @generated
	 * @ordered
	 */
	protected Interactivity interactivity;

	/**
	 * The cached value of the '{@link #getEmptyMessage() <em>Empty Message</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEmptyMessage()
	 * @generated
	 * @ordered
	 */
	protected Label emptyMessage;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ChartImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.CHART;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__VERSION, oldVersion, version,
					!oldVersionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART__VERSION, oldVersion,
					VERSION_EDEFAULT, oldVersionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetVersion() {
		return versionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSubType(String newSubType) {
		String oldSubType = subType;
		subType = newSubType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__SUB_TYPE, oldSubType, subType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Text getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDescription(Text newDescription, NotificationChain msgs) {
		Text oldDescription = description;
		description = newDescription;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART__DESCRIPTION, oldDescription, newDescription);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(Text newDescription) {
		if (newDescription != description) {
			NotificationChain msgs = null;
			if (description != null)
				msgs = ((InternalEObject) description).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__DESCRIPTION, null, msgs);
			if (newDescription != null)
				msgs = ((InternalEObject) newDescription).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__DESCRIPTION, null, msgs);
			msgs = basicSetDescription(newDescription, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__DESCRIPTION, newDescription,
					newDescription));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetBlock(Block newBlock, NotificationChain msgs) {
		Block oldBlock = block;
		block = newBlock;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__BLOCK,
					oldBlock, newBlock);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setBlock(Block newBlock) {
		if (newBlock != block) {
			NotificationChain msgs = null;
			if (block != null)
				msgs = ((InternalEObject) block).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__BLOCK, null, msgs);
			if (newBlock != null)
				msgs = ((InternalEObject) newBlock).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__BLOCK, null, msgs);
			msgs = basicSetBlock(newBlock, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__BLOCK, newBlock, newBlock));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ChartDimension getDimension() {
		return dimension;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDimension(ChartDimension newDimension) {
		ChartDimension oldDimension = dimension;
		dimension = newDimension == null ? DIMENSION_EDEFAULT : newDimension;
		boolean oldDimensionESet = dimensionESet;
		dimensionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__DIMENSION, oldDimension,
					dimension, !oldDimensionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetDimension() {
		ChartDimension oldDimension = dimension;
		boolean oldDimensionESet = dimensionESet;
		dimension = DIMENSION_EDEFAULT;
		dimensionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART__DIMENSION, oldDimension,
					DIMENSION_EDEFAULT, oldDimensionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetDimension() {
		return dimensionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getScript() {
		return script;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setScript(String newScript) {
		String oldScript = script;
		script = newScript;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__SCRIPT, oldScript, script));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUnits(String newUnits) {
		String oldUnits = units;
		units = newUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__UNITS, oldUnits, units));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getSeriesThickness() {
		return seriesThickness;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSeriesThickness(double newSeriesThickness) {
		double oldSeriesThickness = seriesThickness;
		seriesThickness = newSeriesThickness;
		boolean oldSeriesThicknessESet = seriesThicknessESet;
		seriesThicknessESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__SERIES_THICKNESS,
					oldSeriesThickness, seriesThickness, !oldSeriesThicknessESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetSeriesThickness() {
		double oldSeriesThickness = seriesThickness;
		boolean oldSeriesThicknessESet = seriesThicknessESet;
		seriesThickness = SERIES_THICKNESS_EDEFAULT;
		seriesThicknessESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART__SERIES_THICKNESS,
					oldSeriesThickness, SERIES_THICKNESS_EDEFAULT, oldSeriesThicknessESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetSeriesThickness() {
		return seriesThicknessESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getGridColumnCount() {
		return gridColumnCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGridColumnCount(int newGridColumnCount) {
		int oldGridColumnCount = gridColumnCount;
		gridColumnCount = newGridColumnCount;
		boolean oldGridColumnCountESet = gridColumnCountESet;
		gridColumnCountESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__GRID_COLUMN_COUNT,
					oldGridColumnCount, gridColumnCount, !oldGridColumnCountESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetGridColumnCount() {
		int oldGridColumnCount = gridColumnCount;
		boolean oldGridColumnCountESet = gridColumnCountESet;
		gridColumnCount = GRID_COLUMN_COUNT_EDEFAULT;
		gridColumnCountESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART__GRID_COLUMN_COUNT,
					oldGridColumnCount, GRID_COLUMN_COUNT_EDEFAULT, oldGridColumnCountESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetGridColumnCount() {
		return gridColumnCountESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ExtendedProperty> getExtendedProperties() {
		if (extendedProperties == null) {
			extendedProperties = new EObjectContainmentEList<ExtendedProperty>(ExtendedProperty.class, this,
					ModelPackage.CHART__EXTENDED_PROPERTIES);
		}
		return extendedProperties;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SampleData getSampleData() {
		return sampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSampleData(SampleData newSampleData, NotificationChain msgs) {
		SampleData oldSampleData = sampleData;
		sampleData = newSampleData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART__SAMPLE_DATA, oldSampleData, newSampleData);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSampleData(SampleData newSampleData) {
		if (newSampleData != sampleData) {
			NotificationChain msgs = null;
			if (sampleData != null)
				msgs = ((InternalEObject) sampleData).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__SAMPLE_DATA, null, msgs);
			if (newSampleData != null)
				msgs = ((InternalEObject) newSampleData).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__SAMPLE_DATA, null, msgs);
			msgs = basicSetSampleData(newSampleData, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__SAMPLE_DATA, newSampleData,
					newSampleData));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<StyleMap> getStyles() {
		if (styles == null) {
			styles = new EObjectContainmentEList<StyleMap>(StyleMap.class, this, ModelPackage.CHART__STYLES);
		}
		return styles;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Interactivity getInteractivity() {
		return interactivity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetInteractivity(Interactivity newInteractivity, NotificationChain msgs) {
		Interactivity oldInteractivity = interactivity;
		interactivity = newInteractivity;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART__INTERACTIVITY, oldInteractivity, newInteractivity);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInteractivity(Interactivity newInteractivity) {
		if (newInteractivity != interactivity) {
			NotificationChain msgs = null;
			if (interactivity != null)
				msgs = ((InternalEObject) interactivity).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__INTERACTIVITY, null, msgs);
			if (newInteractivity != null)
				msgs = ((InternalEObject) newInteractivity).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__INTERACTIVITY, null, msgs);
			msgs = basicSetInteractivity(newInteractivity, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__INTERACTIVITY, newInteractivity,
					newInteractivity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Label getEmptyMessage() {
		return emptyMessage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetEmptyMessage(Label newEmptyMessage, NotificationChain msgs) {
		Label oldEmptyMessage = emptyMessage;
		emptyMessage = newEmptyMessage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART__EMPTY_MESSAGE, oldEmptyMessage, newEmptyMessage);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEmptyMessage(Label newEmptyMessage) {
		if (newEmptyMessage != emptyMessage) {
			NotificationChain msgs = null;
			if (emptyMessage != null)
				msgs = ((InternalEObject) emptyMessage).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__EMPTY_MESSAGE, null, msgs);
			if (newEmptyMessage != null)
				msgs = ((InternalEObject) newEmptyMessage).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART__EMPTY_MESSAGE, null, msgs);
			msgs = basicSetEmptyMessage(newEmptyMessage, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART__EMPTY_MESSAGE, newEmptyMessage,
					newEmptyMessage));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.CHART__DESCRIPTION:
			return basicSetDescription(null, msgs);
		case ModelPackage.CHART__BLOCK:
			return basicSetBlock(null, msgs);
		case ModelPackage.CHART__EXTENDED_PROPERTIES:
			return ((InternalEList<?>) getExtendedProperties()).basicRemove(otherEnd, msgs);
		case ModelPackage.CHART__SAMPLE_DATA:
			return basicSetSampleData(null, msgs);
		case ModelPackage.CHART__STYLES:
			return ((InternalEList<?>) getStyles()).basicRemove(otherEnd, msgs);
		case ModelPackage.CHART__INTERACTIVITY:
			return basicSetInteractivity(null, msgs);
		case ModelPackage.CHART__EMPTY_MESSAGE:
			return basicSetEmptyMessage(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelPackage.CHART__VERSION:
			return getVersion();
		case ModelPackage.CHART__TYPE:
			return getType();
		case ModelPackage.CHART__SUB_TYPE:
			return getSubType();
		case ModelPackage.CHART__DESCRIPTION:
			return getDescription();
		case ModelPackage.CHART__BLOCK:
			return getBlock();
		case ModelPackage.CHART__DIMENSION:
			return getDimension();
		case ModelPackage.CHART__SCRIPT:
			return getScript();
		case ModelPackage.CHART__UNITS:
			return getUnits();
		case ModelPackage.CHART__SERIES_THICKNESS:
			return getSeriesThickness();
		case ModelPackage.CHART__GRID_COLUMN_COUNT:
			return getGridColumnCount();
		case ModelPackage.CHART__EXTENDED_PROPERTIES:
			return getExtendedProperties();
		case ModelPackage.CHART__SAMPLE_DATA:
			return getSampleData();
		case ModelPackage.CHART__STYLES:
			return getStyles();
		case ModelPackage.CHART__INTERACTIVITY:
			return getInteractivity();
		case ModelPackage.CHART__EMPTY_MESSAGE:
			return getEmptyMessage();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelPackage.CHART__VERSION:
			setVersion((String) newValue);
			return;
		case ModelPackage.CHART__TYPE:
			setType((String) newValue);
			return;
		case ModelPackage.CHART__SUB_TYPE:
			setSubType((String) newValue);
			return;
		case ModelPackage.CHART__DESCRIPTION:
			setDescription((Text) newValue);
			return;
		case ModelPackage.CHART__BLOCK:
			setBlock((Block) newValue);
			return;
		case ModelPackage.CHART__DIMENSION:
			setDimension((ChartDimension) newValue);
			return;
		case ModelPackage.CHART__SCRIPT:
			setScript((String) newValue);
			return;
		case ModelPackage.CHART__UNITS:
			setUnits((String) newValue);
			return;
		case ModelPackage.CHART__SERIES_THICKNESS:
			setSeriesThickness((Double) newValue);
			return;
		case ModelPackage.CHART__GRID_COLUMN_COUNT:
			setGridColumnCount((Integer) newValue);
			return;
		case ModelPackage.CHART__EXTENDED_PROPERTIES:
			getExtendedProperties().clear();
			getExtendedProperties().addAll((Collection<? extends ExtendedProperty>) newValue);
			return;
		case ModelPackage.CHART__SAMPLE_DATA:
			setSampleData((SampleData) newValue);
			return;
		case ModelPackage.CHART__STYLES:
			getStyles().clear();
			getStyles().addAll((Collection<? extends StyleMap>) newValue);
			return;
		case ModelPackage.CHART__INTERACTIVITY:
			setInteractivity((Interactivity) newValue);
			return;
		case ModelPackage.CHART__EMPTY_MESSAGE:
			setEmptyMessage((Label) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ModelPackage.CHART__VERSION:
			unsetVersion();
			return;
		case ModelPackage.CHART__TYPE:
			setType(TYPE_EDEFAULT);
			return;
		case ModelPackage.CHART__SUB_TYPE:
			setSubType(SUB_TYPE_EDEFAULT);
			return;
		case ModelPackage.CHART__DESCRIPTION:
			setDescription((Text) null);
			return;
		case ModelPackage.CHART__BLOCK:
			setBlock((Block) null);
			return;
		case ModelPackage.CHART__DIMENSION:
			unsetDimension();
			return;
		case ModelPackage.CHART__SCRIPT:
			setScript(SCRIPT_EDEFAULT);
			return;
		case ModelPackage.CHART__UNITS:
			setUnits(UNITS_EDEFAULT);
			return;
		case ModelPackage.CHART__SERIES_THICKNESS:
			unsetSeriesThickness();
			return;
		case ModelPackage.CHART__GRID_COLUMN_COUNT:
			unsetGridColumnCount();
			return;
		case ModelPackage.CHART__EXTENDED_PROPERTIES:
			getExtendedProperties().clear();
			return;
		case ModelPackage.CHART__SAMPLE_DATA:
			setSampleData((SampleData) null);
			return;
		case ModelPackage.CHART__STYLES:
			getStyles().clear();
			return;
		case ModelPackage.CHART__INTERACTIVITY:
			setInteractivity((Interactivity) null);
			return;
		case ModelPackage.CHART__EMPTY_MESSAGE:
			setEmptyMessage((Label) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ModelPackage.CHART__VERSION:
			return isSetVersion();
		case ModelPackage.CHART__TYPE:
			return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
		case ModelPackage.CHART__SUB_TYPE:
			return SUB_TYPE_EDEFAULT == null ? subType != null : !SUB_TYPE_EDEFAULT.equals(subType);
		case ModelPackage.CHART__DESCRIPTION:
			return description != null;
		case ModelPackage.CHART__BLOCK:
			return block != null;
		case ModelPackage.CHART__DIMENSION:
			return isSetDimension();
		case ModelPackage.CHART__SCRIPT:
			return SCRIPT_EDEFAULT == null ? script != null : !SCRIPT_EDEFAULT.equals(script);
		case ModelPackage.CHART__UNITS:
			return UNITS_EDEFAULT == null ? units != null : !UNITS_EDEFAULT.equals(units);
		case ModelPackage.CHART__SERIES_THICKNESS:
			return isSetSeriesThickness();
		case ModelPackage.CHART__GRID_COLUMN_COUNT:
			return isSetGridColumnCount();
		case ModelPackage.CHART__EXTENDED_PROPERTIES:
			return extendedProperties != null && !extendedProperties.isEmpty();
		case ModelPackage.CHART__SAMPLE_DATA:
			return sampleData != null;
		case ModelPackage.CHART__STYLES:
			return styles != null && !styles.isEmpty();
		case ModelPackage.CHART__INTERACTIVITY:
			return interactivity != null;
		case ModelPackage.CHART__EMPTY_MESSAGE:
			return emptyMessage != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (version: "); //$NON-NLS-1$
		if (versionESet)
			result.append(version);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", type: "); //$NON-NLS-1$
		result.append(type);
		result.append(", subType: "); //$NON-NLS-1$
		result.append(subType);
		result.append(", dimension: "); //$NON-NLS-1$
		if (dimensionESet)
			result.append(dimension);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", script: "); //$NON-NLS-1$
		result.append(script);
		result.append(", units: "); //$NON-NLS-1$
		result.append(units);
		result.append(", seriesThickness: "); //$NON-NLS-1$
		if (seriesThicknessESet)
			result.append(seriesThickness);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", gridColumnCount: "); //$NON-NLS-1$
		if (gridColumnCountESet)
			result.append(gridColumnCount);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return Legend
	 */
	public final Legend getLegend() {
		return (Legend) find(block, LegendImpl.class);
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return Plot
	 */
	public final Plot getPlot() {
		return (Plot) find(block, PlotImpl.class);
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return TitleBlock
	 */
	public final TitleBlock getTitle() {
		return (TitleBlock) find(block, TitleBlockImpl.class);
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @param blStart
	 * @param c
	 * @return Block
	 */
	private static final Block find(Block bl, Class<?> c) {
		if (c.isInstance(bl)) {
			return bl;
		}
		Block blFound = null;
		final EList<Block> el = bl.getChildren();
		for (int iC = 0; iC < el.size(); iC++) {
			blFound = find(el.get(iC), c);
			if (blFound != null)
				return blFound;
		}
		return null;
	}

	/**
	 * 
	 * Note: Manually written
	 */
	protected void initialize() {
		setVersion(VERSION);
		// 1. CREATE AND INITIALIZE BLOCKS
		block = BlockImpl.create(); // OUTERMOST BLOCK
		// block.setBackground( ColorDefinitionImpl.TRANSPARENT( ) );
		// TED 12117-- default background is white color.
		// block.setBackground( ColorDefinitionImpl.WHITE( ) );

		TitleBlock tb = (TitleBlock) TitleBlockImpl.create(); // TITLE
		Plot pl = (Plot) PlotImpl.create(); // PLOT
		Legend lg = (Legend) LegendImpl.create(); // LEGEND

		// 2. ADD THEM TO THE LAYOUT
		block.add(tb);
		block.add(pl);
		block.add(lg);

		// 3. INITIALIZE THE CHART TITLE
		Text txtChartTitle = tb.getLabel().getCaption();
		txtChartTitle.setValue(Messages.getString("ChartImpl.ChartTitle")); //$NON-NLS-1$
		txtChartTitle.getFont().setSize(16);
		txtChartTitle.getFont().setBold(true);
		TextAlignment taTitle = TextAlignmentImpl.create();
		taTitle.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		taTitle.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		txtChartTitle.getFont().setAlignment(taTitle);

		// 4. SETUP OTHER BASIC PROPERTIES
		setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		setSeriesThickness(10);

		// 5. SETUP INTERACTIVITY
		setInteractivity(InteractivityImpl.create());

		// 6. SETUP ALTTEXT
		setEmptyMessage(newEmptyMessage());

		// 7. Setup default extended properties
		ChartModelHelper.instance().updateExtendedProperties(getExtendedProperties());

		units = "Points"; //$NON-NLS-1$
	}

	/**
	 * 
	 * Note: Manually written
	 */
	protected void initDefault() {
		version = VERSION;
		// 1. CREATE AND INITIALIZE BLOCKS
		block = BlockImpl.createDefault(); // OUTERMOST BLOCK
		// block.setBackground( ColorDefinitionImpl.TRANSPARENT( ) );
		// TED 12117-- default background is white color.
		// block.setBackground( ColorDefinitionImpl.WHITE( ) );

		TitleBlock tb = (TitleBlock) TitleBlockImpl.createDefault(); // TITLE
		Plot pl = (Plot) PlotImpl.createDefault(); // PLOT
		Legend lg = (Legend) LegendImpl.createDefault(); // LEGEND

		// 2. ADD THEM TO THE LAYOUT
		block.add(tb);
		block.add(pl);
		block.add(lg);

		// 3. INITIALIZE THE CHART TITLE
		Text txtChartTitle = tb.getLabel().getCaption();
		txtChartTitle.setValue(null);
		try {
			ChartElementUtil.setDefaultValue(txtChartTitle.getFont(), "size", 16); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(txtChartTitle.getFont(), "bold", true); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}

		TextAlignment taTitle = TextAlignmentImpl.createDefault(HorizontalAlignment.CENTER_LITERAL,
				VerticalAlignment.CENTER_LITERAL);
		try {
			ChartElementUtil.setDefaultValue(txtChartTitle.getFont(), "alignment", taTitle); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}

		// 4. SETUP OTHER BASIC PROPERTIES
		dimension = ChartDimension.TWO_DIMENSIONAL_LITERAL;
		seriesThickness = 10;

		// 5. SETUP INTERACTIVITY
		interactivity = InteractivityImpl.create();

		// 6. SETUP ALTTEXT
		setEmptyMessage(newEmptyMessageDefault());

		// 7. Setup default extended properties
		ChartModelHelper.instance().updateExtendedProperties(getExtendedProperties());
	}

	private Label newEmptyMessageDefault() {
		Label laAltText = LabelImpl.createDefault(false);

		laAltText.setCaption(TextImpl.createDefault(Messages.getString("ChartImpl.AltText"))); //$NON-NLS-1$
		laAltText.getCaption().getFont().setAlignment(
				TextAlignmentImpl.createDefault(HorizontalAlignment.CENTER_LITERAL, VerticalAlignment.CENTER_LITERAL));

		ColorDefinition bgColor = ColorDefinitionImpl.GREY();
		try {
			ChartElementUtil.setDefaultValue(bgColor, "transparency", 64); //$NON-NLS-1$
			laAltText.setBackground(bgColor);
			laAltText.setInsets(InsetsImpl.createDefault(10, 10, 10, 10));

			ColorDefinition outlineColor = ColorDefinitionImpl.GREY();
			ChartElementUtil.setDefaultValue(outlineColor, "transparency", 128); //$NON-NLS-1$
			LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes();
			lia.setColor(outlineColor);
			ChartElementUtil.setDefaultValue(lia, "visible", true); //$NON-NLS-1$
			laAltText.setOutline(lia);
		} catch (ChartException e) {
		}
		return laAltText;
	}

	private Label newEmptyMessage() {
		Label laAltText = ComponentFactory.eINSTANCE.createLabel();
		laAltText.setVisible(false);

		laAltText.setCaption(TextImpl.create(Messages.getString("ChartImpl.AltText"))); //$NON-NLS-1$
		TextAlignment ta = laAltText.getCaption().getFont().getAlignment();
		ta.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		ta.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);

		ColorDefinition bgColor = ColorDefinitionImpl.GREY();
		bgColor.setTransparency(64);
		laAltText.setBackground(bgColor);

		laAltText.setInsets(InsetsImpl.create(10, 10, 10, 10));

		ColorDefinition outlineColor = ColorDefinitionImpl.GREY();
		outlineColor.setTransparency(128);
		LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes();
		lia.setColor(outlineColor);
		lia.setVisible(true);
		laAltText.setOutline(lia);

		return laAltText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.Chart#getSeriesForLegend()
	 */
	public SeriesDefinition[] getSeriesForLegend() {
		final List<SeriesDefinition> al = getOrthogonalSeriesDefinitions();
		return al.toArray(new SeriesDefinition[al.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.Chart#clearSections(int)
	 */
	public void clearSections(int iSectionType) {
		// To override
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.Chart#createSampleRuntimeSeries()
	 */
	public final void createSampleRuntimeSeries() {
		Chart chart = this;
		SampleData sd = chart.getSampleData();
		if (sd == null || sd.getBaseSampleData().size() == 0 || sd.getOrthogonalSampleData().size() == 0) {
			return;
		}

		try {
			// Process Base SeriesDefinitions
			SeriesDefinition bsd = getBaseSeriesDefinition();
			Series seriesBaseRuntime = bsd.getDesignTimeSeries().copyInstance();

			// Clear existing values from the dataset
			seriesBaseRuntime.setDataSet(null);

			// Clear any existing Runtime Series
			chart.clearSections(IConstants.RUN_TIME);

			String baseDataSetRepresentation = sd.getBaseSampleData().get(0).getDataSetRepresentation();
			if (bsd.eContainer() instanceof Axis) {
				baseDataSetRepresentation = ChartUtil.getNewSampleData(((Axis) (bsd.eContainer())).getType(), 0);
			} else {
				baseDataSetRepresentation = ChartUtil.getNewSampleData(AxisType.TEXT_LITERAL, 0);
			}
			// Get the BaseSampleData and use it to construct dataset
			seriesBaseRuntime
					.setDataSet((PluginSettings.instance().getDataSetProcessor(bsd.getDesignTimeSeries().getClass()))
							.fromString(baseDataSetRepresentation, seriesBaseRuntime.getDataSet()));
			bsd.getSeries().add(seriesBaseRuntime);

			// Set sample series identifier
			seriesBaseRuntime.setSeriesIdentifier("Category"); //$NON-NLS-1$

			// Process Orthogonal SeriesDefinitions
			List<SeriesDefinition> vOSD = getOrthogonalSeriesDefinitions();
			int[] iOSD = new int[vOSD.size()];
			SeriesDefinition sdTmp = null;
			Series seriesOrthogonalRuntime = null;

			// Initialize position array and clear any existing runtime series
			for (int i = 0; i < iOSD.length; i++) {
				iOSD[i] = 0;
				sdTmp = vOSD.get(i);
			}

			// Fetch the DataSetRepresentations for orthogonal sample data
			for (int iO = 0; iO < sd.getOrthogonalSampleData().size(); iO++) {
				OrthogonalSampleData osd = sd.getOrthogonalSampleData().get(iO);
				int iSDIndex = osd.getSeriesDefinitionIndex();

				// If the series definition has been removed
				if (iSDIndex > vOSD.size() - 1) {
					// Remove the Orthogonal Sample Data entry (since it is no
					// longer valid)
					sd.getOrthogonalSampleData().remove(iO);

					// Reset the loop counter to not lose an entry
					iO--;

					// continue with the next iteration
					continue;
				}

				// Create runtime series for SeriesDefinition index
				sdTmp = vOSD.get(iSDIndex);
				seriesOrthogonalRuntime = sdTmp.getDesignTimeSeries().copyInstance();

				// Clear existing values from the dataset
				seriesOrthogonalRuntime.setDataSet(null);

				String orthogonalDataSetRepresentation = osd.getDataSetRepresentation();
				if (sdTmp.eContainer() instanceof Axis) {
					orthogonalDataSetRepresentation = ChartUtil
							.getNewSampleData(((Axis) (sdTmp.eContainer())).getType(), iO);
				} else {
					orthogonalDataSetRepresentation = ChartUtil.getNewSampleData(AxisType.LINEAR_LITERAL, iO);
				}

				// Set the new dataset with sample values
				seriesOrthogonalRuntime.setDataSet(
						(PluginSettings.instance().getDataSetProcessor(sdTmp.getDesignTimeSeries().getClass()))
								.fromString(orthogonalDataSetRepresentation, seriesOrthogonalRuntime.getDataSet()));

				// Set sample series identifiers
				// Use the design time settings without consideration of group
				seriesOrthogonalRuntime.setSeriesIdentifier(sdTmp.getDesignTimeSeries().getSeriesIdentifier());

				sdTmp.getSeries().add(seriesOrthogonalRuntime);
			}

			if (chart.getDimension() == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
				// Process Ancillary Base SeriesDefinitions
				SeriesDefinition sdZ = getAncillaryBaseSeriesDefinition();

				if (sdZ != null && sd.getAncillarySampleData().size() > 0) {
					Series seriesZRuntime = sdZ.getDesignTimeSeries().copyInstance();

					seriesZRuntime.setDataSet(null);

					String ancillaryDataSetRepresentation = getNewAncillarySampleData(vOSD);
					seriesZRuntime.setDataSet(
							(PluginSettings.instance().getDataSetProcessor(sdZ.getDesignTimeSeries().getClass()))
									.fromString(ancillaryDataSetRepresentation, seriesZRuntime.getDataSet()));
					sdZ.getSeries().add(seriesZRuntime);

					seriesZRuntime.setSeriesIdentifier("Series"); //$NON-NLS-1$
				}
			}

		} catch (Exception e1) {
			logger.log(e1);
		}
	}

	private String getNewAncillarySampleData(List<SeriesDefinition> vOSD) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < vOSD.size(); i++) {
			sb.append(vOSD.get(i).getDesignTimeSeries().getSeriesIdentifier());
			if (i < vOSD.size() - 1) {
				sb.append(","); //$NON-NLS-1$
			}
		}
		return sb.toString();
	}

	protected SeriesDefinition getBaseSeriesDefinition() {
		// To override
		return null;
	}

	protected SeriesDefinition getAncillaryBaseSeriesDefinition() {
		// To override
		return null;
	}

	protected List<SeriesDefinition> getOrthogonalSeriesDefinitions() {
		// To override
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public Chart copyInstance() {
		ChartImpl dest = new ChartImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Chart src) {

		// children

		if (src.getDescription() != null) {
			setDescription(src.getDescription().copyInstance());
		}

		if (src.getBlock() != null) {
			setBlock(src.getBlock().copyInstance());
		}

		if (src.getExtendedProperties() != null) {
			EList<ExtendedProperty> list = getExtendedProperties();
			for (ExtendedProperty element : src.getExtendedProperties()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getSampleData() != null) {
			setSampleData(src.getSampleData().copyInstance());
		}

		if (src.getStyles() != null) {
			EList<StyleMap> list = getStyles();
			for (StyleMap element : src.getStyles()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getInteractivity() != null) {
			setInteractivity(src.getInteractivity().copyInstance());
		}

		if (src.getEmptyMessage() != null) {
			setEmptyMessage(src.getEmptyMessage().copyInstance());
		}

		// attributes

		version = src.getVersion();

		versionESet = src.isSetVersion();

		type = src.getType();

		subType = src.getSubType();

		dimension = src.getDimension();

		dimensionESet = src.isSetDimension();

		script = src.getScript();

		units = src.getUnits();

		seriesThickness = src.getSeriesThickness();

		seriesThicknessESet = src.isSetSeriesThickness();

		gridColumnCount = src.getGridColumnCount();

		gridColumnCountESet = src.isSetGridColumnCount();

	}

} // ChartImpl
