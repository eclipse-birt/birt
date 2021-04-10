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

package org.eclipse.birt.chart.model.data.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
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
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Series
 * Definition</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getQuery
 * <em>Query</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSeriesPalette
 * <em>Series Palette</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSeriesDefinitions
 * <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSeries
 * <em>Series</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getGrouping
 * <em>Grouping</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSorting
 * <em>Sorting</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSortKey
 * <em>Sort Key</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSortLocale
 * <em>Sort Locale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSortStrength
 * <em>Sort Strength</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getZOrder
 * <em>ZOrder</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SeriesDefinitionImpl extends EObjectImpl implements SeriesDefinition {

	/**
	 * The cached value of the '{@link #getQuery() <em>Query</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected Query query;

	/**
	 * The cached value of the '{@link #getSeriesPalette() <em>Series Palette</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSeriesPalette()
	 * @generated
	 * @ordered
	 */
	protected Palette seriesPalette;

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
	 * The cached value of the '{@link #getFormatSpecifier() <em>Format
	 * Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier formatSpecifier;

	/**
	 * The cached value of the '{@link #getSeries() <em>Series</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSeries()
	 * @generated
	 * @ordered
	 */
	protected EList<Series> series;

	/**
	 * The cached value of the '{@link #getGrouping() <em>Grouping</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGrouping()
	 * @generated
	 * @ordered
	 */
	protected SeriesGrouping grouping;

	/**
	 * The default value of the '{@link #getSorting() <em>Sorting</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSorting()
	 * @generated
	 * @ordered
	 */
	protected static final SortOption SORTING_EDEFAULT = SortOption.ASCENDING_LITERAL;

	/**
	 * The cached value of the '{@link #getSorting() <em>Sorting</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSorting()
	 * @generated
	 * @ordered
	 */
	protected SortOption sorting = SORTING_EDEFAULT;

	/**
	 * This is true if the Sorting attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean sortingESet;

	/**
	 * The cached value of the '{@link #getSortKey() <em>Sort Key</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSortKey()
	 * @generated
	 * @ordered
	 */
	protected Query sortKey;

	/**
	 * The default value of the '{@link #getSortLocale() <em>Sort Locale</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSortLocale()
	 * @generated
	 * @ordered
	 */
	protected static final String SORT_LOCALE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSortLocale() <em>Sort Locale</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSortLocale()
	 * @generated
	 * @ordered
	 */
	protected String sortLocale = SORT_LOCALE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSortStrength() <em>Sort Strength</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSortStrength()
	 * @generated
	 * @ordered
	 */
	protected static final int SORT_STRENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSortStrength() <em>Sort Strength</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSortStrength()
	 * @generated
	 * @ordered
	 */
	protected int sortStrength = SORT_STRENGTH_EDEFAULT;

	/**
	 * This is true if the Sort Strength attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean sortStrengthESet;

	/**
	 * The default value of the '{@link #getZOrder() <em>ZOrder</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZOrder()
	 * @generated
	 * @ordered
	 */
	protected static final int ZORDER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getZOrder() <em>ZOrder</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZOrder()
	 * @generated
	 * @ordered
	 */
	protected int zOrder = ZORDER_EDEFAULT;

	/**
	 * This is true if the ZOrder attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean zOrderESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected SeriesDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.SERIES_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetQuery(Query newQuery, NotificationChain msgs) {
		Query oldQuery = query;
		query = newQuery;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DataPackage.SERIES_DEFINITION__QUERY, oldQuery, newQuery);
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
	public void setQuery(Query newQuery) {
		if (newQuery != query) {
			NotificationChain msgs = null;
			if (query != null)
				msgs = ((InternalEObject) query).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__QUERY, null, msgs);
			if (newQuery != null)
				msgs = ((InternalEObject) newQuery).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__QUERY, null, msgs);
			msgs = basicSetQuery(newQuery, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__QUERY, newQuery,
					newQuery));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Palette getSeriesPalette() {
		return seriesPalette;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSeriesPalette(Palette newSeriesPalette, NotificationChain msgs) {
		Palette oldSeriesPalette = seriesPalette;
		seriesPalette = newSeriesPalette;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DataPackage.SERIES_DEFINITION__SERIES_PALETTE, oldSeriesPalette, newSeriesPalette);
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
	public void setSeriesPalette(Palette newSeriesPalette) {
		if (newSeriesPalette != seriesPalette) {
			NotificationChain msgs = null;
			if (seriesPalette != null)
				msgs = ((InternalEObject) seriesPalette).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__SERIES_PALETTE, null, msgs);
			if (newSeriesPalette != null)
				msgs = ((InternalEObject) newSeriesPalette).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__SERIES_PALETTE, null, msgs);
			msgs = basicSetSeriesPalette(newSeriesPalette, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SERIES_PALETTE,
					newSeriesPalette, newSeriesPalette));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<SeriesDefinition> getSeriesDefinitions() {
		if (seriesDefinitions == null) {
			seriesDefinitions = new EObjectContainmentEList<SeriesDefinition>(SeriesDefinition.class, this,
					DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS);
		}
		return seriesDefinitions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FormatSpecifier getFormatSpecifier() {
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs) {
		FormatSpecifier oldFormatSpecifier = formatSpecifier;
		formatSpecifier = newFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
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
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null)
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER, null, msgs);
			if (newFormatSpecifier != null)
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER, null, msgs);
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER,
					newFormatSpecifier, newFormatSpecifier));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Series> getSeries() {
		if (series == null) {
			series = new EObjectContainmentEList<Series>(Series.class, this, DataPackage.SERIES_DEFINITION__SERIES);
		}
		return series;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SeriesGrouping getGrouping() {
		return grouping;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetGrouping(SeriesGrouping newGrouping, NotificationChain msgs) {
		SeriesGrouping oldGrouping = grouping;
		grouping = newGrouping;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DataPackage.SERIES_DEFINITION__GROUPING, oldGrouping, newGrouping);
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
	public void setGrouping(SeriesGrouping newGrouping) {
		if (newGrouping != grouping) {
			NotificationChain msgs = null;
			if (grouping != null)
				msgs = ((InternalEObject) grouping).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__GROUPING, null, msgs);
			if (newGrouping != null)
				msgs = ((InternalEObject) newGrouping).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__GROUPING, null, msgs);
			msgs = basicSetGrouping(newGrouping, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__GROUPING, newGrouping,
					newGrouping));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SortOption getSorting() {
		return sorting;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSorting(SortOption newSorting) {
		SortOption oldSorting = sorting;
		sorting = newSorting == null ? SORTING_EDEFAULT : newSorting;
		boolean oldSortingESet = sortingESet;
		sortingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SORTING, oldSorting,
					sorting, !oldSortingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetSorting() {
		SortOption oldSorting = sorting;
		boolean oldSortingESet = sortingESet;
		sorting = SORTING_EDEFAULT;
		sortingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_DEFINITION__SORTING, oldSorting,
					SORTING_EDEFAULT, oldSortingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetSorting() {
		return sortingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Query getSortKey() {
		return sortKey;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSortKey(Query newSortKey, NotificationChain msgs) {
		Query oldSortKey = sortKey;
		sortKey = newSortKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DataPackage.SERIES_DEFINITION__SORT_KEY, oldSortKey, newSortKey);
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
	public void setSortKey(Query newSortKey) {
		if (newSortKey != sortKey) {
			NotificationChain msgs = null;
			if (sortKey != null)
				msgs = ((InternalEObject) sortKey).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__SORT_KEY, null, msgs);
			if (newSortKey != null)
				msgs = ((InternalEObject) newSortKey).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_DEFINITION__SORT_KEY, null, msgs);
			msgs = basicSetSortKey(newSortKey, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SORT_KEY, newSortKey,
					newSortKey));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getSortLocale() {
		return sortLocale;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSortLocale(String newSortLocale) {
		String oldSortLocale = sortLocale;
		sortLocale = newSortLocale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SORT_LOCALE,
					oldSortLocale, sortLocale));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getSortStrength() {
		return sortStrength;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSortStrength(int newSortStrength) {
		int oldSortStrength = sortStrength;
		sortStrength = newSortStrength;
		boolean oldSortStrengthESet = sortStrengthESet;
		sortStrengthESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SORT_STRENGTH,
					oldSortStrength, sortStrength, !oldSortStrengthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetSortStrength() {
		int oldSortStrength = sortStrength;
		boolean oldSortStrengthESet = sortStrengthESet;
		sortStrength = SORT_STRENGTH_EDEFAULT;
		sortStrengthESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_DEFINITION__SORT_STRENGTH,
					oldSortStrength, SORT_STRENGTH_EDEFAULT, oldSortStrengthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetSortStrength() {
		return sortStrengthESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getZOrder() {
		return zOrder;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZOrder(int newZOrder) {
		int oldZOrder = zOrder;
		zOrder = newZOrder;
		boolean oldZOrderESet = zOrderESet;
		zOrderESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__ZORDER, oldZOrder,
					zOrder, !oldZOrderESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetZOrder() {
		int oldZOrder = zOrder;
		boolean oldZOrderESet = zOrderESet;
		zOrder = ZORDER_EDEFAULT;
		zOrderESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_DEFINITION__ZORDER, oldZOrder,
					ZORDER_EDEFAULT, oldZOrderESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetZOrder() {
		return zOrderESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DataPackage.SERIES_DEFINITION__QUERY:
			return basicSetQuery(null, msgs);
		case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
			return basicSetSeriesPalette(null, msgs);
		case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
			return ((InternalEList<?>) getSeriesDefinitions()).basicRemove(otherEnd, msgs);
		case DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
		case DataPackage.SERIES_DEFINITION__SERIES:
			return ((InternalEList<?>) getSeries()).basicRemove(otherEnd, msgs);
		case DataPackage.SERIES_DEFINITION__GROUPING:
			return basicSetGrouping(null, msgs);
		case DataPackage.SERIES_DEFINITION__SORT_KEY:
			return basicSetSortKey(null, msgs);
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
		case DataPackage.SERIES_DEFINITION__QUERY:
			return getQuery();
		case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
			return getSeriesPalette();
		case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
			return getSeriesDefinitions();
		case DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER:
			return getFormatSpecifier();
		case DataPackage.SERIES_DEFINITION__SERIES:
			return getSeries();
		case DataPackage.SERIES_DEFINITION__GROUPING:
			return getGrouping();
		case DataPackage.SERIES_DEFINITION__SORTING:
			return getSorting();
		case DataPackage.SERIES_DEFINITION__SORT_KEY:
			return getSortKey();
		case DataPackage.SERIES_DEFINITION__SORT_LOCALE:
			return getSortLocale();
		case DataPackage.SERIES_DEFINITION__SORT_STRENGTH:
			return getSortStrength();
		case DataPackage.SERIES_DEFINITION__ZORDER:
			return getZOrder();
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
		case DataPackage.SERIES_DEFINITION__QUERY:
			setQuery((Query) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
			setSeriesPalette((Palette) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
			getSeriesDefinitions().clear();
			getSeriesDefinitions().addAll((Collection<? extends SeriesDefinition>) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SERIES:
			getSeries().clear();
			getSeries().addAll((Collection<? extends Series>) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__GROUPING:
			setGrouping((SeriesGrouping) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SORTING:
			setSorting((SortOption) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SORT_KEY:
			setSortKey((Query) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SORT_LOCALE:
			setSortLocale((String) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__SORT_STRENGTH:
			setSortStrength((Integer) newValue);
			return;
		case DataPackage.SERIES_DEFINITION__ZORDER:
			setZOrder((Integer) newValue);
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
		case DataPackage.SERIES_DEFINITION__QUERY:
			setQuery((Query) null);
			return;
		case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
			setSeriesPalette((Palette) null);
			return;
		case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
			getSeriesDefinitions().clear();
			return;
		case DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
			return;
		case DataPackage.SERIES_DEFINITION__SERIES:
			getSeries().clear();
			return;
		case DataPackage.SERIES_DEFINITION__GROUPING:
			setGrouping((SeriesGrouping) null);
			return;
		case DataPackage.SERIES_DEFINITION__SORTING:
			unsetSorting();
			return;
		case DataPackage.SERIES_DEFINITION__SORT_KEY:
			setSortKey((Query) null);
			return;
		case DataPackage.SERIES_DEFINITION__SORT_LOCALE:
			setSortLocale(SORT_LOCALE_EDEFAULT);
			return;
		case DataPackage.SERIES_DEFINITION__SORT_STRENGTH:
			unsetSortStrength();
			return;
		case DataPackage.SERIES_DEFINITION__ZORDER:
			unsetZOrder();
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
		case DataPackage.SERIES_DEFINITION__QUERY:
			return query != null;
		case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
			return seriesPalette != null;
		case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
			return seriesDefinitions != null && !seriesDefinitions.isEmpty();
		case DataPackage.SERIES_DEFINITION__FORMAT_SPECIFIER:
			return formatSpecifier != null;
		case DataPackage.SERIES_DEFINITION__SERIES:
			return series != null && !series.isEmpty();
		case DataPackage.SERIES_DEFINITION__GROUPING:
			return grouping != null;
		case DataPackage.SERIES_DEFINITION__SORTING:
			return isSetSorting();
		case DataPackage.SERIES_DEFINITION__SORT_KEY:
			return sortKey != null;
		case DataPackage.SERIES_DEFINITION__SORT_LOCALE:
			return SORT_LOCALE_EDEFAULT == null ? sortLocale != null : !SORT_LOCALE_EDEFAULT.equals(sortLocale);
		case DataPackage.SERIES_DEFINITION__SORT_STRENGTH:
			return isSetSortStrength();
		case DataPackage.SERIES_DEFINITION__ZORDER:
			return isSetZOrder();
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
		result.append(" (sorting: "); //$NON-NLS-1$
		if (sortingESet)
			result.append(sorting);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", sortLocale: "); //$NON-NLS-1$
		result.append(sortLocale);
		result.append(", sortStrength: "); //$NON-NLS-1$
		if (sortStrengthESet)
			result.append(sortStrength);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", zOrder: "); //$NON-NLS-1$
		if (zOrderESet)
			result.append(zOrder);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to create a series definition instance and
	 * initialize its member variables
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public static final SeriesDefinition create() {
		final SeriesDefinition sd = DataFactory.eINSTANCE.createSeriesDefinition();
		sd.setQuery(QueryImpl.create(IConstants.EMPTY_STRING));
		sd.setSeriesPalette(PaletteImpl.create(ColorDefinitionImpl.GREY()));
		sd.getSeriesPalette().getEntries().clear();
		sd.setGrouping(SeriesGroupingImpl.create());
		return sd;
	}

	/**
	 * A convenience method provided to create a series definition instance and
	 * initialize its member variables
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public static final SeriesDefinition createDefault() {
		final SeriesDefinition sd = DataFactory.eINSTANCE.createSeriesDefinition();
		((SeriesDefinitionImpl) sd).initDefault();
		return sd;
	}

	private void initDefault() {
		query = QueryImpl.create(IConstants.EMPTY_STRING);
		seriesPalette = PaletteImpl.create(ColorDefinitionImpl.GREY());
		seriesPalette.getEntries().clear();
		grouping = SeriesGroupingImpl.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getDesignTimeSeries()
	 */
	public final Series getDesignTimeSeries() {
		final EList el = getSeries();
		Series se;
		for (int i = 0; i < el.size(); i++) {
			se = (Series) el.get(i);
			if (se.getDataSet() == null) {
				return se;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getRunTimeSeries()
	 */
	public final List<Series> getRunTimeSeries() {
		final List<Series> alRTS = new ArrayList<Series>();
		final EList el = getSeries();
		Series se;
		for (int i = 0; i < el.size(); i++) {
			se = (Series) el.get(i);
			if (se.getDataSet() != null) {
				alRTS.add(se);
			}
		}
		return alRTS;
	}

	/**
	 * @generated
	 */
	public SeriesDefinition copyInstance() {
		SeriesDefinitionImpl dest = new SeriesDefinitionImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(SeriesDefinition src) {

		// children

		if (src.getQuery() != null) {
			setQuery(src.getQuery().copyInstance());
		}

		if (src.getSeriesPalette() != null) {
			setSeriesPalette(src.getSeriesPalette().copyInstance());
		}

		if (src.getSeriesDefinitions() != null) {
			EList<SeriesDefinition> list = getSeriesDefinitions();
			for (SeriesDefinition element : src.getSeriesDefinitions()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getFormatSpecifier() != null) {
			setFormatSpecifier(src.getFormatSpecifier().copyInstance());
		}

		if (src.getSeries() != null) {
			EList<Series> list = getSeries();
			for (Series element : src.getSeries()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getGrouping() != null) {
			setGrouping(src.getGrouping().copyInstance());
		}

		if (src.getSortKey() != null) {
			setSortKey(src.getSortKey().copyInstance());
		}

		// attributes

		sorting = src.getSorting();

		sortingESet = src.isSetSorting();

		sortLocale = src.getSortLocale();

		sortStrength = src.getSortStrength();

		sortStrengthESet = src.isSetSortStrength();

		zOrder = src.getZOrder();

		zOrderESet = src.isSetZOrder();

	}

} // SeriesDefinitionImpl
