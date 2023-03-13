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

package org.eclipse.birt.chart.model.type.impl;

import java.util.Iterator;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Area
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class AreaSeriesImpl extends LineSeriesImpl implements AreaSeries {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AreaSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.AREA_SERIES;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#translateFrom(org.eclipse.birt.
	 * chart.model.component.Series, int, org.eclipse.birt.chart.model.Chart)
	 */
	@Override
	public void translateFrom(Series series, int iSeriesDefinitionIndex, Chart chart) {
		super.translateFrom(series, iSeriesDefinitionIndex, chart);

		for (Iterator<Marker> itr = getMarkers().iterator(); itr.hasNext();) {
			Marker mk = itr.next();
			mk.unsetVisible();
		}
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 *
	 * @return series instance with setting 'isSet' flag.
	 */
	public static Series create() {
		final AreaSeries as = TypeFactory.eINSTANCE.createAreaSeries();
		((AreaSeriesImpl) as).initialize();
		return as;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();

		for (Iterator<Marker> itr = getMarkers().iterator(); itr.hasNext();) {
			Marker mk = itr.next();
			mk.setVisible(false);
		}
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 *
	 * @return instance of ares series without setting 'isSet' flag.
	 */
	public static Series createDefault() {
		final AreaSeries as = TypeFactory.eINSTANCE.createAreaSeries();
		((AreaSeriesImpl) as).initDefault();
		return as;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	@Override
	protected void initDefault() {
		super.initDefault();

		for (Iterator<Marker> itr = getMarkers().iterator(); itr.hasNext();) {
			Marker mk = itr.next();
			try {
				ChartElementUtil.setDefaultValue(mk, "visible", false); //$NON-NLS-1$
			} catch (ChartException e) {
				// Do nothing.
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("AreaSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	@Override
	public AreaSeries copyInstance() {
		AreaSeriesImpl dest = new AreaSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(AreaSeries src) {

		super.set(src);

	}

} // AreaSeriesImpl
