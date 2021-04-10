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

package org.eclipse.birt.chart.model.component.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getDataDefinition
 * <em>Data Definition</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getSeriesIdentifier
 * <em>Series Identifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getDataPoint
 * <em>Data Point</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getDataSets
 * <em>Data Sets</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getLabelPosition
 * <em>Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#isStacked
 * <em>Stacked</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#isTranslucent
 * <em>Translucent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getCurveFitting
 * <em>Curve Fitting</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getCursor
 * <em>Cursor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SeriesImpl extends EObjectImpl implements Series {

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * This is true if the Visible attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean visibleESet;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * The cached value of the '{@link #getDataDefinition() <em>Data
	 * Definition</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getDataDefinition()
	 * @generated
	 * @ordered
	 */
	protected EList<Query> dataDefinition;

	/**
	 * The default value of the '{@link #getSeriesIdentifier() <em>Series
	 * Identifier</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSeriesIdentifier()
	 * @generated
	 * @ordered
	 */
	protected static final Object SERIES_IDENTIFIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSeriesIdentifier() <em>Series
	 * Identifier</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSeriesIdentifier()
	 * @generated
	 * @ordered
	 */
	protected Object seriesIdentifier = SERIES_IDENTIFIER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDataPoint() <em>Data Point</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDataPoint()
	 * @generated
	 * @ordered
	 */
	protected DataPoint dataPoint;

	/**
	 * The cached value of the '{@link #getDataSets() <em>Data Sets</em>}' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDataSets()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DataSet> dataSets;

	/**
	 * The default value of the ' {@link #getLabelPosition() <em>Label
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position LABEL_POSITION_EDEFAULT = Position.OUTSIDE_LITERAL;

	/**
	 * The cached value of the ' {@link #getLabelPosition() <em>Label
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabelPosition()
	 * @generated
	 * @ordered
	 */
	protected Position labelPosition = LABEL_POSITION_EDEFAULT;

	/**
	 * This is true if the Label Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean labelPositionESet;

	/**
	 * The default value of the '{@link #isStacked() <em>Stacked</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStacked()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STACKED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isStacked() <em>Stacked</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStacked()
	 * @generated
	 * @ordered
	 */
	protected boolean stacked = STACKED_EDEFAULT;

	/**
	 * This is true if the Stacked attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean stackedESet;

	/**
	 * The cached value of the '{@link #getTriggers() <em>Triggers</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTriggers()
	 * @generated
	 * @ordered
	 */
	protected EList<Trigger> triggers;

	/**
	 * The default value of the '{@link #isTranslucent() <em>Translucent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isTranslucent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TRANSLUCENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTranslucent() <em>Translucent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isTranslucent()
	 * @generated
	 * @ordered
	 */
	protected boolean translucent = TRANSLUCENT_EDEFAULT;

	/**
	 * This is true if the Translucent attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean translucentESet;

	/**
	 * The cached value of the '{@link #getCurveFitting() <em>Curve Fitting</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCurveFitting()
	 * @generated
	 * @ordered
	 */
	protected CurveFitting curveFitting;

	/**
	 * The cached value of the '{@link #getCursor() <em>Cursor</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCursor()
	 * @generated
	 * @ordered
	 */
	protected Cursor cursor;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected SeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		boolean oldVisibleESet = visibleESet;
		visibleESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__VISIBLE, oldVisible, visible,
					!oldVisibleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetVisible() {
		boolean oldVisible = visible;
		boolean oldVisibleESet = visibleESet;
		visible = VISIBLE_EDEFAULT;
		visibleESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__VISIBLE, oldVisible,
					VISIBLE_EDEFAULT, oldVisibleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetVisible() {
		return visibleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs) {
		Label oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.SERIES__LABEL, oldLabel, newLabel);
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
	public void setLabel(Label newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null)
				msgs = ((InternalEObject) label).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__LABEL, null, msgs);
			if (newLabel != null)
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__LABEL, null, msgs);
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__LABEL, newLabel, newLabel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Query> getDataDefinition() {
		if (dataDefinition == null) {
			dataDefinition = new EObjectContainmentEList<Query>(Query.class, this,
					ComponentPackage.SERIES__DATA_DEFINITION);
		}
		return dataDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getSeriesIdentifier() {
		return seriesIdentifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSeriesIdentifier(Object newSeriesIdentifier) {
		Object oldSeriesIdentifier = seriesIdentifier;
		seriesIdentifier = newSeriesIdentifier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__SERIES_IDENTIFIER,
					oldSeriesIdentifier, seriesIdentifier));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataPoint getDataPoint() {
		return dataPoint;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDataPoint(DataPoint newDataPoint, NotificationChain msgs) {
		DataPoint oldDataPoint = dataPoint;
		dataPoint = newDataPoint;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.SERIES__DATA_POINT, oldDataPoint, newDataPoint);
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
	public void setDataPoint(DataPoint newDataPoint) {
		if (newDataPoint != dataPoint) {
			NotificationChain msgs = null;
			if (dataPoint != null)
				msgs = ((InternalEObject) dataPoint).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__DATA_POINT, null, msgs);
			if (newDataPoint != null)
				msgs = ((InternalEObject) newDataPoint).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__DATA_POINT, null, msgs);
			msgs = basicSetDataPoint(newDataPoint, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__DATA_POINT, newDataPoint,
					newDataPoint));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, DataSet> getDataSets() {
		if (dataSets == null) {
			dataSets = new EcoreEMap<String, DataSet>(ComponentPackage.Literals.ESTRING_TO_DATA_SET_MAP_ENTRY,
					EStringToDataSetMapEntryImpl.class, this, ComponentPackage.SERIES__DATA_SETS);
		}
		return dataSets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDataSet()
	 */
	public DataSet getDataSet() {
		return getDataSets().get(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#setDataSet(org.eclipse.birt.
	 * chart.model.data.DataSet)
	 */
	public void setDataSet(DataSet newDataSet) {
		getDataSets().put(null, newDataSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#getDataSet(java.lang.String)
	 */
	public DataSet getDataSet(String userkey) {
		return getDataSets().get(userkey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#setDataSet(java.lang.String,
	 * org.eclipse.birt.chart.model.data.DataSet)
	 */
	public void setDataSet(String userKey, DataSet newDataSet) {
		getDataSets().put(userKey, newDataSet);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Position getLabelPosition() {
		return labelPosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelPosition(Position newLabelPosition) {
		Position oldLabelPosition = labelPosition;
		labelPosition = newLabelPosition == null ? LABEL_POSITION_EDEFAULT : newLabelPosition;
		boolean oldLabelPositionESet = labelPositionESet;
		labelPositionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__LABEL_POSITION,
					oldLabelPosition, labelPosition, !oldLabelPositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLabelPosition() {
		Position oldLabelPosition = labelPosition;
		boolean oldLabelPositionESet = labelPositionESet;
		labelPosition = LABEL_POSITION_EDEFAULT;
		labelPositionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__LABEL_POSITION,
					oldLabelPosition, LABEL_POSITION_EDEFAULT, oldLabelPositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLabelPosition() {
		return labelPositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isStacked() {
		return stacked;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStacked(boolean newStacked) {
		boolean oldStacked = stacked;
		stacked = newStacked;
		boolean oldStackedESet = stackedESet;
		stackedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__STACKED, oldStacked, stacked,
					!oldStackedESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetStacked() {
		boolean oldStacked = stacked;
		boolean oldStackedESet = stackedESet;
		stacked = STACKED_EDEFAULT;
		stackedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__STACKED, oldStacked,
					STACKED_EDEFAULT, oldStackedESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetStacked() {
		return stackedESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, ComponentPackage.SERIES__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isTranslucent() {
		return translucent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTranslucent(boolean newTranslucent) {
		boolean oldTranslucent = translucent;
		translucent = newTranslucent;
		boolean oldTranslucentESet = translucentESet;
		translucentESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__TRANSLUCENT, oldTranslucent,
					translucent, !oldTranslucentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTranslucent() {
		boolean oldTranslucent = translucent;
		boolean oldTranslucentESet = translucentESet;
		translucent = TRANSLUCENT_EDEFAULT;
		translucentESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__TRANSLUCENT,
					oldTranslucent, TRANSLUCENT_EDEFAULT, oldTranslucentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTranslucent() {
		return translucentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CurveFitting getCurveFitting() {
		return curveFitting;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetCurveFitting(CurveFitting newCurveFitting, NotificationChain msgs) {
		CurveFitting oldCurveFitting = curveFitting;
		curveFitting = newCurveFitting;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.SERIES__CURVE_FITTING, oldCurveFitting, newCurveFitting);
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
	public void setCurveFitting(CurveFitting newCurveFitting) {
		if (newCurveFitting != curveFitting) {
			NotificationChain msgs = null;
			if (curveFitting != null)
				msgs = ((InternalEObject) curveFitting).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__CURVE_FITTING, null, msgs);
			if (newCurveFitting != null)
				msgs = ((InternalEObject) newCurveFitting).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__CURVE_FITTING, null, msgs);
			msgs = basicSetCurveFitting(newCurveFitting, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__CURVE_FITTING,
					newCurveFitting, newCurveFitting));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetCursor(Cursor newCursor, NotificationChain msgs) {
		Cursor oldCursor = cursor;
		cursor = newCursor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.SERIES__CURSOR, oldCursor, newCursor);
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
	public void setCursor(Cursor newCursor) {
		if (newCursor != cursor) {
			NotificationChain msgs = null;
			if (cursor != null)
				msgs = ((InternalEObject) cursor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__CURSOR, null, msgs);
			if (newCursor != null)
				msgs = ((InternalEObject) newCursor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SERIES__CURSOR, null, msgs);
			msgs = basicSetCursor(newCursor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__CURSOR, newCursor,
					newCursor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.SERIES__LABEL:
			return basicSetLabel(null, msgs);
		case ComponentPackage.SERIES__DATA_DEFINITION:
			return ((InternalEList<?>) getDataDefinition()).basicRemove(otherEnd, msgs);
		case ComponentPackage.SERIES__DATA_POINT:
			return basicSetDataPoint(null, msgs);
		case ComponentPackage.SERIES__DATA_SETS:
			return ((InternalEList<?>) getDataSets()).basicRemove(otherEnd, msgs);
		case ComponentPackage.SERIES__TRIGGERS:
			return ((InternalEList<?>) getTriggers()).basicRemove(otherEnd, msgs);
		case ComponentPackage.SERIES__CURVE_FITTING:
			return basicSetCurveFitting(null, msgs);
		case ComponentPackage.SERIES__CURSOR:
			return basicSetCursor(null, msgs);
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
		case ComponentPackage.SERIES__VISIBLE:
			return isVisible();
		case ComponentPackage.SERIES__LABEL:
			return getLabel();
		case ComponentPackage.SERIES__DATA_DEFINITION:
			return getDataDefinition();
		case ComponentPackage.SERIES__SERIES_IDENTIFIER:
			return getSeriesIdentifier();
		case ComponentPackage.SERIES__DATA_POINT:
			return getDataPoint();
		case ComponentPackage.SERIES__DATA_SETS:
			if (coreType)
				return getDataSets();
			else
				return getDataSets().map();
		case ComponentPackage.SERIES__LABEL_POSITION:
			return getLabelPosition();
		case ComponentPackage.SERIES__STACKED:
			return isStacked();
		case ComponentPackage.SERIES__TRIGGERS:
			return getTriggers();
		case ComponentPackage.SERIES__TRANSLUCENT:
			return isTranslucent();
		case ComponentPackage.SERIES__CURVE_FITTING:
			return getCurveFitting();
		case ComponentPackage.SERIES__CURSOR:
			return getCursor();
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
		case ComponentPackage.SERIES__VISIBLE:
			setVisible((Boolean) newValue);
			return;
		case ComponentPackage.SERIES__LABEL:
			setLabel((Label) newValue);
			return;
		case ComponentPackage.SERIES__DATA_DEFINITION:
			getDataDefinition().clear();
			getDataDefinition().addAll((Collection<? extends Query>) newValue);
			return;
		case ComponentPackage.SERIES__SERIES_IDENTIFIER:
			setSeriesIdentifier(newValue);
			return;
		case ComponentPackage.SERIES__DATA_POINT:
			setDataPoint((DataPoint) newValue);
			return;
		case ComponentPackage.SERIES__DATA_SETS:
			((EStructuralFeature.Setting) getDataSets()).set(newValue);
			return;
		case ComponentPackage.SERIES__LABEL_POSITION:
			setLabelPosition((Position) newValue);
			return;
		case ComponentPackage.SERIES__STACKED:
			setStacked((Boolean) newValue);
			return;
		case ComponentPackage.SERIES__TRIGGERS:
			getTriggers().clear();
			getTriggers().addAll((Collection<? extends Trigger>) newValue);
			return;
		case ComponentPackage.SERIES__TRANSLUCENT:
			setTranslucent((Boolean) newValue);
			return;
		case ComponentPackage.SERIES__CURVE_FITTING:
			setCurveFitting((CurveFitting) newValue);
			return;
		case ComponentPackage.SERIES__CURSOR:
			setCursor((Cursor) newValue);
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
		case ComponentPackage.SERIES__VISIBLE:
			unsetVisible();
			return;
		case ComponentPackage.SERIES__LABEL:
			setLabel((Label) null);
			return;
		case ComponentPackage.SERIES__DATA_DEFINITION:
			getDataDefinition().clear();
			return;
		case ComponentPackage.SERIES__SERIES_IDENTIFIER:
			setSeriesIdentifier(SERIES_IDENTIFIER_EDEFAULT);
			return;
		case ComponentPackage.SERIES__DATA_POINT:
			setDataPoint((DataPoint) null);
			return;
		case ComponentPackage.SERIES__DATA_SETS:
			getDataSets().clear();
			return;
		case ComponentPackage.SERIES__LABEL_POSITION:
			unsetLabelPosition();
			return;
		case ComponentPackage.SERIES__STACKED:
			unsetStacked();
			return;
		case ComponentPackage.SERIES__TRIGGERS:
			getTriggers().clear();
			return;
		case ComponentPackage.SERIES__TRANSLUCENT:
			unsetTranslucent();
			return;
		case ComponentPackage.SERIES__CURVE_FITTING:
			setCurveFitting((CurveFitting) null);
			return;
		case ComponentPackage.SERIES__CURSOR:
			setCursor((Cursor) null);
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
		case ComponentPackage.SERIES__VISIBLE:
			return isSetVisible();
		case ComponentPackage.SERIES__LABEL:
			return label != null;
		case ComponentPackage.SERIES__DATA_DEFINITION:
			return dataDefinition != null && !dataDefinition.isEmpty();
		case ComponentPackage.SERIES__SERIES_IDENTIFIER:
			return SERIES_IDENTIFIER_EDEFAULT == null ? seriesIdentifier != null
					: !SERIES_IDENTIFIER_EDEFAULT.equals(seriesIdentifier);
		case ComponentPackage.SERIES__DATA_POINT:
			return dataPoint != null;
		case ComponentPackage.SERIES__DATA_SETS:
			return dataSets != null && !dataSets.isEmpty();
		case ComponentPackage.SERIES__LABEL_POSITION:
			return isSetLabelPosition();
		case ComponentPackage.SERIES__STACKED:
			return isSetStacked();
		case ComponentPackage.SERIES__TRIGGERS:
			return triggers != null && !triggers.isEmpty();
		case ComponentPackage.SERIES__TRANSLUCENT:
			return isSetTranslucent();
		case ComponentPackage.SERIES__CURVE_FITTING:
			return curveFitting != null;
		case ComponentPackage.SERIES__CURSOR:
			return cursor != null;
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
		result.append(" (visible: "); //$NON-NLS-1$
		if (visibleESet)
			result.append(visible);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", seriesIdentifier: "); //$NON-NLS-1$
		result.append(seriesIdentifier);
		result.append(", labelPosition: "); //$NON-NLS-1$
		if (labelPositionESet)
			result.append(labelPosition);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", stacked: "); //$NON-NLS-1$
		if (stackedESet)
			result.append(stacked);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", translucent: "); //$NON-NLS-1$
		if (translucentESet)
			result.append(translucent);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#canParticipateInCombination()
	 */
	public boolean canParticipateInCombination() {
		return false;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return series instance
	 */
	public static Series create() {
		final Series se = ComponentFactory.eINSTANCE.createSeries();
		((SeriesImpl) se).initialize();
		return se;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#initialize()
	 */
	protected void initialize() {
		setStacked(false);
		setVisible(true);
		final Label la = LabelImpl.create();
		LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		la.setOutline(lia);
		lia.setVisible(false);
		// la.setBackground(ColorDefinitionImpl.YELLOW());
		setLabel(la);
		la.setVisible(false);
		setLabelPosition(Position.OUTSIDE_LITERAL);
		setSeriesIdentifier(IConstants.UNDEFINED_STRING);
		setDataPoint(DataPointImpl.create(null, null, ", ")); //$NON-NLS-1$
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return series instance
	 */
	public static Series createDefault() {
		final Series se = ComponentFactory.eINSTANCE.createSeries();
		((SeriesImpl) se).initDefault();
		return se;
	}

	protected void initDefault() {
		label = LabelImpl.createDefault(false);
		labelPosition = Position.OUTSIDE_LITERAL;
		seriesIdentifier = IConstants.UNDEFINED_STRING;
		setDataPoint(DataPointImpl.createDefault());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
	 */
	public boolean canBeStacked() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canShareAxisUnit()
	 */
	public boolean canShareAxisUnit() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#translateFrom(org.eclipse.birt.
	 * chart.model.component.Series, org.eclipse.birt.chart.model.Chart)
	 */
	public void translateFrom(Series sourceSeries, int iSeriesDefinitionIndex, Chart chart) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("SeriesImpl.displayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#isSingleCache()
	 */
	public boolean isSingleCache() {
		return false;
	}

	/**
	 * @generated
	 */
	public Series copyInstance() {
		SeriesImpl dest = new SeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Series src) {

		// children

		if (src.getLabel() != null) {
			setLabel(src.getLabel().copyInstance());
		}

		if (src.getDataDefinition() != null) {
			EList<Query> list = getDataDefinition();
			for (Query element : src.getDataDefinition()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getDataPoint() != null) {
			setDataPoint(src.getDataPoint().copyInstance());
		}

		if (src.getDataSets() != null) {
			EMap<String, DataSet> map = getDataSets();
			for (Map.Entry<String, DataSet> entry : src.getDataSets().entrySet()) {

				DataSet entryValue = entry.getValue() != null ? entry.getValue().copyInstance() : null;

				map.put(entry.getKey(), entryValue);

			}
		}

		if (src.getTriggers() != null) {
			EList<Trigger> list = getTriggers();
			for (Trigger element : src.getTriggers()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getCurveFitting() != null) {
			setCurveFitting(src.getCurveFitting().copyInstance());
		}

		if (src.getCursor() != null) {
			setCursor(src.getCursor().copyInstance());
		}

		// attributes

		visible = src.isVisible();

		visibleESet = src.isSetVisible();

		seriesIdentifier = src.getSeriesIdentifier();

		labelPosition = src.getLabelPosition();

		labelPositionESet = src.isSetLabelPosition();

		stacked = src.isStacked();

		stackedESet = src.isSetStacked();

		translucent = src.isTranslucent();

		translucentESet = src.isSetTranslucent();

	}

	public NameSet getLabelPositionScope(ChartDimension dimension) {
		// Default implementation is vertical and horizontal positions.
		return LiteralHelper.directionPositionSet;
	}

	public int[] getDefinedDataDefinitionIndex() {
		// Default data definition has only one query, which must be defined.
		return new int[] { 0 };
	}

} // SeriesImpl
