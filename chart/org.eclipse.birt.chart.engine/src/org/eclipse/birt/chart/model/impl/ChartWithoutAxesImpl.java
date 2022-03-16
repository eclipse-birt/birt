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

package org.eclipse.birt.chart.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Chart
 * Without Axes</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#getSeriesDefinitions
 * <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#getMinSlice
 * <em>Min Slice</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#isMinSlicePercent
 * <em>Min Slice Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#getMinSliceLabel
 * <em>Min Slice Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#getCoverage
 * <em>Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChartWithoutAxesImpl extends ChartImpl implements ChartWithoutAxes {

	/**
	 * The cached value of the '{@link #getSeriesDefinitions() <em>Series
	 * Definitions</em>}' containment reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getSeriesDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<SeriesDefinition> seriesDefinitions;

	/**
	 * The default value of the '{@link #getMinSlice() <em>Min Slice</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMinSlice()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_SLICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinSlice() <em>Min Slice</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMinSlice()
	 * @generated
	 * @ordered
	 */
	protected double minSlice = MIN_SLICE_EDEFAULT;

	/**
	 * This is true if the Min Slice attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean minSliceESet;

	/**
	 * The default value of the '{@link #isMinSlicePercent() <em>Min Slice
	 * Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isMinSlicePercent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MIN_SLICE_PERCENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMinSlicePercent() <em>Min Slice
	 * Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isMinSlicePercent()
	 * @generated
	 * @ordered
	 */
	protected boolean minSlicePercent = MIN_SLICE_PERCENT_EDEFAULT;

	/**
	 * This is true if the Min Slice Percent attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean minSlicePercentESet;

	/**
	 * The default value of the '{@link #getMinSliceLabel() <em>Min Slice
	 * Label</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMinSliceLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String MIN_SLICE_LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMinSliceLabel() <em>Min Slice
	 * Label</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMinSliceLabel()
	 * @generated
	 * @ordered
	 */
	protected String minSliceLabel = MIN_SLICE_LABEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getCoverage() <em>Coverage</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCoverage()
	 * @generated
	 * @ordered
	 */
	protected static final double COVERAGE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCoverage() <em>Coverage</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCoverage()
	 * @generated
	 * @ordered
	 */
	protected double coverage = COVERAGE_EDEFAULT;

	/**
	 * This is true if the Coverage attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean coverageESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ChartWithoutAxesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.CHART_WITHOUT_AXES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<SeriesDefinition> getSeriesDefinitions() {
		if (seriesDefinitions == null) {
			seriesDefinitions = new EObjectContainmentEList<>(SeriesDefinition.class, this,
					ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS);
		}
		return seriesDefinitions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getMinSlice() {
		return minSlice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMinSlice(double newMinSlice) {
		double oldMinSlice = minSlice;
		minSlice = newMinSlice;
		boolean oldMinSliceESet = minSliceESet;
		minSliceESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE,
					oldMinSlice, minSlice, !oldMinSliceESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetMinSlice() {
		double oldMinSlice = minSlice;
		boolean oldMinSliceESet = minSliceESet;
		minSlice = MIN_SLICE_EDEFAULT;
		minSliceESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE,
					oldMinSlice, MIN_SLICE_EDEFAULT, oldMinSliceESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetMinSlice() {
		return minSliceESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isMinSlicePercent() {
		return minSlicePercent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMinSlicePercent(boolean newMinSlicePercent) {
		boolean oldMinSlicePercent = minSlicePercent;
		minSlicePercent = newMinSlicePercent;
		boolean oldMinSlicePercentESet = minSlicePercentESet;
		minSlicePercentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_PERCENT,
					oldMinSlicePercent, minSlicePercent, !oldMinSlicePercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetMinSlicePercent() {
		boolean oldMinSlicePercent = minSlicePercent;
		boolean oldMinSlicePercentESet = minSlicePercentESet;
		minSlicePercent = MIN_SLICE_PERCENT_EDEFAULT;
		minSlicePercentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_PERCENT,
					oldMinSlicePercent, MIN_SLICE_PERCENT_EDEFAULT, oldMinSlicePercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetMinSlicePercent() {
		return minSlicePercentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getMinSliceLabel() {
		return minSliceLabel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMinSliceLabel(String newMinSliceLabel) {
		String oldMinSliceLabel = minSliceLabel;
		minSliceLabel = newMinSliceLabel;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_LABEL,
					oldMinSliceLabel, minSliceLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getCoverage() {
		return coverage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCoverage(double newCoverage) {
		double oldCoverage = coverage;
		coverage = newCoverage;
		boolean oldCoverageESet = coverageESet;
		coverageESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITHOUT_AXES__COVERAGE,
					oldCoverage, coverage, !oldCoverageESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetCoverage() {
		double oldCoverage = coverage;
		boolean oldCoverageESet = coverageESet;
		coverage = COVERAGE_EDEFAULT;
		coverageESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITHOUT_AXES__COVERAGE,
					oldCoverage, COVERAGE_EDEFAULT, oldCoverageESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetCoverage() {
		return coverageESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
			return ((InternalEList<?>) getSeriesDefinitions()).basicRemove(otherEnd, msgs);
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
		case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
			return getSeriesDefinitions();
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE:
			return getMinSlice();
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_PERCENT:
			return isMinSlicePercent();
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_LABEL:
			return getMinSliceLabel();
		case ModelPackage.CHART_WITHOUT_AXES__COVERAGE:
			return getCoverage();
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
		case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
			getSeriesDefinitions().clear();
			getSeriesDefinitions().addAll((Collection<? extends SeriesDefinition>) newValue);
			return;
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE:
			setMinSlice((Double) newValue);
			return;
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_PERCENT:
			setMinSlicePercent((Boolean) newValue);
			return;
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_LABEL:
			setMinSliceLabel((String) newValue);
			return;
		case ModelPackage.CHART_WITHOUT_AXES__COVERAGE:
			setCoverage((Double) newValue);
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
		case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
			getSeriesDefinitions().clear();
			return;
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE:
			unsetMinSlice();
			return;
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_PERCENT:
			unsetMinSlicePercent();
			return;
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_LABEL:
			setMinSliceLabel(MIN_SLICE_LABEL_EDEFAULT);
			return;
		case ModelPackage.CHART_WITHOUT_AXES__COVERAGE:
			unsetCoverage();
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
		case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
			return seriesDefinitions != null && !seriesDefinitions.isEmpty();
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE:
			return isSetMinSlice();
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_PERCENT:
			return isSetMinSlicePercent();
		case ModelPackage.CHART_WITHOUT_AXES__MIN_SLICE_LABEL:
			return MIN_SLICE_LABEL_EDEFAULT == null ? minSliceLabel != null
					: !MIN_SLICE_LABEL_EDEFAULT.equals(minSliceLabel);
		case ModelPackage.CHART_WITHOUT_AXES__COVERAGE:
			return isSetCoverage();
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
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (minSlice: "); //$NON-NLS-1$
		if (minSliceESet) {
			result.append(minSlice);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", minSlicePercent: "); //$NON-NLS-1$
		if (minSlicePercentESet) {
			result.append(minSlicePercent);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", minSliceLabel: "); //$NON-NLS-1$
		result.append(minSliceLabel);
		result.append(", coverage: "); //$NON-NLS-1$
		if (coverageESet) {
			result.append(coverage);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method to create an initialized 'ChartWithoutAxes' instance
	 *
	 * @return chart model
	 */
	public static ChartWithoutAxes create() {
		final ChartWithoutAxes cwoa = ModelFactory.eINSTANCE.createChartWithoutAxes();
		((ChartWithoutAxesImpl) cwoa).initialize();
		return cwoa;
	}

	/**
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initialize() {
		// INITIALIZE SUPER'S MEMBERS
		super.initialize();
		setGridColumnCount(0);
		getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);
	}

	/**
	 * A convenience method to create an initialized 'ChartWithoutAxes' instance
	 *
	 * @return chart model
	 */
	public static ChartWithoutAxes createDefault() {
		final ChartWithoutAxes cwoa = ModelFactory.eINSTANCE.createChartWithoutAxes();
		((ChartWithoutAxesImpl) cwoa).initDefault();
		return cwoa;
	}

	/**
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initDefault() {
		// INITIALIZE SUPER'S MEMBERS
		super.initDefault();
		gridColumnCount = 0;
		try {
			ChartElementUtil.setDefaultValue(getLegend(), "itemType", LegendItemType.CATEGORIES_LITERAL); //$NON-NLS-1$
		} catch (ChartException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.ChartWithoutAxes#getRunTimeSeries()
	 */
	@Override
	public final Series[] getRunTimeSeries() {
		final ArrayList<Series> al = new ArrayList<>(8);
		final EList<SeriesDefinition> el = getSeriesDefinitions();
		recursivelyGetSeries(el, al, 0, -1);
		return al.toArray(new Series[al.size()]);
	}

	/**
	 * Walks down the series definition tree and retrieves all runtime series.
	 *
	 * @param elSDs
	 * @param al
	 * @param iLevel
	 */
	public final void recursivelyGetSeries(EList<SeriesDefinition> elSDs, ArrayList<Series> al, int iLevel,
			int iLevelToOmit) {
		for (int i = 0; i < elSDs.size(); i++) {
			SeriesDefinition sd = elSDs.get(i);
			if (iLevel != iLevelToOmit) {
				al.addAll(sd.getRunTimeSeries());
			}
			recursivelyGetSeries(sd.getSeriesDefinitions(), al, iLevel + 1, iLevelToOmit);
		}
	}

	/**
	 * Walks down the series definition tree and removes all runtime series.
	 *
	 * @param elSDs
	 * @param al
	 * @param iLevel
	 */
	private static final void recursivelyRemoveRuntimeSeries(EList<SeriesDefinition> elSDs, int iLevel,
			int iLevelToOmit) {
		for (int i = 0; i < elSDs.size(); i++) {
			SeriesDefinition sd = elSDs.get(i);
			if (iLevel != iLevelToOmit) {
				if (sd.getSeries().size() == sd.getRunTimeSeries().size()) {
					for (Series se : sd.getRunTimeSeries()) {
						se.getDataSets().clear();
					}
				} else {
					sd.getSeries().removeAll(sd.getRunTimeSeries());
				}
			}
			recursivelyRemoveRuntimeSeries(sd.getSeriesDefinitions(), iLevel + 1, iLevelToOmit);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.Chart#clearSections(int)
	 */
	@Override
	public final void clearSections(int iSectionType) {
		if ((iSectionType & IConstants.RUN_TIME) == IConstants.RUN_TIME) {
			recursivelyRemoveRuntimeSeries(getSeriesDefinitions(), 0, -1);
		}
	}

	/**
	 * @generated
	 */
	@Override
	public ChartWithoutAxes copyInstance() {
		ChartWithoutAxesImpl dest = new ChartWithoutAxesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(ChartWithoutAxes src) {

		super.set(src);

		// children

		if (src.getSeriesDefinitions() != null) {
			EList<SeriesDefinition> list = getSeriesDefinitions();
			for (SeriesDefinition element : src.getSeriesDefinitions()) {
				list.add(element.copyInstance());
			}
		}

		// attributes

		minSlice = src.getMinSlice();

		minSliceESet = src.isSetMinSlice();

		minSlicePercent = src.isMinSlicePercent();

		minSlicePercentESet = src.isSetMinSlicePercent();

		minSliceLabel = src.getMinSliceLabel();

		coverage = src.getCoverage();

		coverageESet = src.isSetCoverage();

	}

	/**
	 * Set pie chart dimension type.
	 */
	@Override
	public void setDimension(ChartDimension newDimension) {
		if (isValidDimensionNType(this.type, newDimension)) {
			super.setDimension(newDimension);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.impl.ChartImpl#setType(java.lang.String)
	 */
	@Override
	public void setType(String newType) {
		if (isValidDimensionNType(newType, this.dimension)) {
			super.setType(newType);
		}
	}

	protected boolean isValidDimensionNType(String type, ChartDimension dimension) {
		if ("Pie Chart".equals(type) && dimension == ChartDimension.THREE_DIMENSIONAL_LITERAL) //$NON-NLS-1$
		{
			// Does not support 3D for Pie chart.
			throw new UnsupportedOperationException(Messages.getString("ChartWithoutAxesImpl.Unsupported3Dimension")); //$NON-NLS-1$
		}
		return true;
	}

	@Override
	protected SeriesDefinition getBaseSeriesDefinition() {
		return getSeriesDefinitions().get(0);
	}

	@Override
	protected List<SeriesDefinition> getOrthogonalSeriesDefinitions() {
		List<SeriesDefinition> osds = new ArrayList<>();
		for (SeriesDefinition bsd : getSeriesDefinitions()) {
			osds.addAll(bsd.getSeriesDefinitions());
		}
		return osds;
	}
}
