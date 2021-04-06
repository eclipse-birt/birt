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

package org.eclipse.birt.chart.model.type.impl;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Scatter Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class ScatterSeriesImpl extends LineSeriesImpl implements ScatterSeries {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ScatterSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.SCATTER_SERIES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
	 */
	public final boolean canBeStacked() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#canParticipateInCombination()
	 */
	public final boolean canParticipateInCombination() {
		return false;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static Series create() {
		final ScatterSeries ss = TypeFactory.eINSTANCE.createScatterSeries();
		((ScatterSeriesImpl) ss).initialize();
		return ss;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initialize() {
		super.initialize();
		getLineAttributes().setVisible(false);
		getMarkers().get(0).setType(MarkerType.CROSSHAIR_LITERAL);
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static Series createDefault() {
		final ScatterSeries ss = TypeFactory.eINSTANCE.createScatterSeries();
		((ScatterSeriesImpl) ss).initDefault();
		return ss;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initDefault() {
		super.initDefault();
		try {
			ChartElementUtil.setDefaultValue(getLineAttributes(), "visible", false); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(getMarkers().get(0), "type", MarkerType.CROSSHAIR_LITERAL); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("ScatterSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public ScatterSeries copyInstance() {
		ScatterSeriesImpl dest = new ScatterSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(ScatterSeries src) {

		super.set(src);

	}

} // ScatterSeriesImpl
