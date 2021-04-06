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

import java.text.ParseException;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.StringTokenizer;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Stock
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl#getFill
 * <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl#isShowAsBarStick
 * <em>Show As Bar Stick</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl#getStickLength
 * <em>Stick Length</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StockSeriesImpl extends SeriesImpl implements StockSeries {

	/**
	 * The cached value of the '{@link #getFill() <em>Fill</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFill()
	 * @generated
	 * @ordered
	 */
	protected Fill fill;

	/**
	 * The cached value of the '{@link #getLineAttributes() <em>Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes lineAttributes;

	/**
	 * The default value of the '{@link #isShowAsBarStick() <em>Show As Bar
	 * Stick</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isShowAsBarStick()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_AS_BAR_STICK_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowAsBarStick() <em>Show As Bar
	 * Stick</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isShowAsBarStick()
	 * @generated
	 * @ordered
	 */
	protected boolean showAsBarStick = SHOW_AS_BAR_STICK_EDEFAULT;

	/**
	 * This is true if the Show As Bar Stick attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean showAsBarStickESet;

	/**
	 * The default value of the '{@link #getStickLength() <em>Stick Length</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStickLength()
	 * @generated
	 * @ordered
	 */
	protected static final int STICK_LENGTH_EDEFAULT = 5;

	/**
	 * The cached value of the '{@link #getStickLength() <em>Stick Length</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStickLength()
	 * @generated
	 * @ordered
	 */
	protected int stickLength = STICK_LENGTH_EDEFAULT;

	/**
	 * This is true if the Stick Length attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean stickLengthESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected StockSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.STOCK_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getFill() {
		return fill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs) {
		Fill oldFill = fill;
		fill = newFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.STOCK_SERIES__FILL, oldFill, newFill);
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
	public void setFill(Fill newFill) {
		if (newFill != fill) {
			NotificationChain msgs = null;
			if (fill != null)
				msgs = ((InternalEObject) fill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.STOCK_SERIES__FILL, null, msgs);
			if (newFill != null)
				msgs = ((InternalEObject) newFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.STOCK_SERIES__FILL, null, msgs);
			msgs = basicSetFill(newFill, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.STOCK_SERIES__FILL, newFill, newFill));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getLineAttributes() {
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLineAttributes(LineAttributes newLineAttributes, NotificationChain msgs) {
		LineAttributes oldLineAttributes = lineAttributes;
		lineAttributes = newLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.STOCK_SERIES__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
	public void setLineAttributes(LineAttributes newLineAttributes) {
		if (newLineAttributes != lineAttributes) {
			NotificationChain msgs = null;
			if (lineAttributes != null)
				msgs = ((InternalEObject) lineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.STOCK_SERIES__LINE_ATTRIBUTES, null, msgs);
			if (newLineAttributes != null)
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.STOCK_SERIES__LINE_ATTRIBUTES, null, msgs);
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.STOCK_SERIES__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isShowAsBarStick() {
		return showAsBarStick;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setShowAsBarStick(boolean newShowAsBarStick) {
		boolean oldShowAsBarStick = showAsBarStick;
		showAsBarStick = newShowAsBarStick;
		boolean oldShowAsBarStickESet = showAsBarStickESet;
		showAsBarStickESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.STOCK_SERIES__SHOW_AS_BAR_STICK,
					oldShowAsBarStick, showAsBarStick, !oldShowAsBarStickESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetShowAsBarStick() {
		boolean oldShowAsBarStick = showAsBarStick;
		boolean oldShowAsBarStickESet = showAsBarStickESet;
		showAsBarStick = SHOW_AS_BAR_STICK_EDEFAULT;
		showAsBarStickESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.STOCK_SERIES__SHOW_AS_BAR_STICK,
					oldShowAsBarStick, SHOW_AS_BAR_STICK_EDEFAULT, oldShowAsBarStickESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetShowAsBarStick() {
		return showAsBarStickESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getStickLength() {
		return stickLength;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStickLength(int newStickLength) {
		int oldStickLength = stickLength;
		stickLength = newStickLength;
		boolean oldStickLengthESet = stickLengthESet;
		stickLengthESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.STOCK_SERIES__STICK_LENGTH,
					oldStickLength, stickLength, !oldStickLengthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetStickLength() {
		int oldStickLength = stickLength;
		boolean oldStickLengthESet = stickLengthESet;
		stickLength = STICK_LENGTH_EDEFAULT;
		stickLengthESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.STOCK_SERIES__STICK_LENGTH,
					oldStickLength, STICK_LENGTH_EDEFAULT, oldStickLengthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetStickLength() {
		return stickLengthESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TypePackage.STOCK_SERIES__FILL:
			return basicSetFill(null, msgs);
		case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
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
		case TypePackage.STOCK_SERIES__FILL:
			return getFill();
		case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
			return getLineAttributes();
		case TypePackage.STOCK_SERIES__SHOW_AS_BAR_STICK:
			return isShowAsBarStick();
		case TypePackage.STOCK_SERIES__STICK_LENGTH:
			return getStickLength();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case TypePackage.STOCK_SERIES__FILL:
			setFill((Fill) newValue);
			return;
		case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case TypePackage.STOCK_SERIES__SHOW_AS_BAR_STICK:
			setShowAsBarStick((Boolean) newValue);
			return;
		case TypePackage.STOCK_SERIES__STICK_LENGTH:
			setStickLength((Integer) newValue);
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
		case TypePackage.STOCK_SERIES__FILL:
			setFill((Fill) null);
			return;
		case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case TypePackage.STOCK_SERIES__SHOW_AS_BAR_STICK:
			unsetShowAsBarStick();
			return;
		case TypePackage.STOCK_SERIES__STICK_LENGTH:
			unsetStickLength();
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
		case TypePackage.STOCK_SERIES__FILL:
			return fill != null;
		case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case TypePackage.STOCK_SERIES__SHOW_AS_BAR_STICK:
			return isSetShowAsBarStick();
		case TypePackage.STOCK_SERIES__STICK_LENGTH:
			return isSetStickLength();
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
		result.append(" (showAsBarStick: "); //$NON-NLS-1$
		if (showAsBarStickESet)
			result.append(showAsBarStick);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", stickLength: "); //$NON-NLS-1$
		if (stickLengthESet)
			result.append(stickLength);
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
	public final boolean canParticipateInCombination() {
		return true;
	}

	public void translateFrom(Series series, int iSeriesDefinitionIndex, Chart chart) {

		// Copy generic series properties
		this.setLabel(series.getLabel());
		if (series.isSetLabelPosition()) {
			if (series.getLabelPosition().equals(Position.INSIDE_LITERAL)
					|| series.getLabelPosition().equals(Position.OUTSIDE_LITERAL)) {
				this.setLabelPosition(series.getLabelPosition());
			} else {
				this.setLabelPosition(Position.OUTSIDE_LITERAL);
			}
		}
		if (series.isSetVisible()) {
			this.setVisible(series.isVisible());
		}
		if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers())) {
			this.getTriggers().addAll(series.getTriggers());
		}
		if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint())) {
			this.setDataPoint(series.getDataPoint());
		}
		if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition())) {
			this.getDataDefinition().addAll(series.getDataDefinition());
			// Add blank definition if old series' is less than expected
			if (!(series instanceof StockSeries)) {
				for (int length = this.getDataDefinition().size(); length < 4; length++) {
					this.getDataDefinition().add(QueryImpl.create("")); //$NON-NLS-1$
				}
			}
		}

		// Copy series specific properties
		if (series instanceof BarSeries && ((BarSeries) series).getRiserOutline() != null) {
			this.getLineAttributes().setColor(((BarSeries) series).getRiserOutline());
		} else if (series instanceof LineSeries && ((LineSeries) series).getLineAttributes() != null) {
			this.setLineAttributes(((LineSeries) series).getLineAttributes());
		}

		// Update the chart dimensions to 2D
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);

		// Update the base axis to type text if it isn't already
		if (chart instanceof ChartWithAxes) {
			// !Don't change to dateTime type, keep the original setting.
			// ( (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 ) ).setType(
			// AxisType.DATE_TIME_LITERAL );
			EList<Axis> axes = ((ChartWithAxes) chart).getAxes().get(0).getAssociatedAxes();
			for (int i = 0; i < axes.size(); i++) {
				axes.get(i).setType(AxisType.LINEAR_LITERAL);
				axes.get(i).setPercent(false);
			}
		} else {
			throw new IllegalArgumentException(Messages.getString("error.invalid.argument.for.stockSeries", //$NON-NLS-1$
					new Object[] { chart.getClass().getName() }, ULocale.getDefault()));
		}

		// Update the sampledata in the model
		chart.setSampleData(getConvertedSampleData(chart.getSampleData(), iSeriesDefinitionIndex));
	}

	private SampleData getConvertedSampleData(SampleData currentSampleData, int iSeriesDefinitionIndex) {
		// !Base sample data should NOT be converted since base Axis type is not
		// being changed now.

		// Convert base sample data
		// EList bsdList = currentSampleData.getBaseSampleData( );
		// Vector vNewBaseSampleData = new Vector( );
		// for ( int i = 0; i < bsdList.size( ); i++ )
		// {
		// BaseSampleData bsd = (BaseSampleData) bsdList.get( i );
		// bsd.setDataSetRepresentation(
		// getConvertedBaseSampleDataRepresentation(
		// bsd.getDataSetRepresentation( ) ) );
		// vNewBaseSampleData.add( bsd );
		// }
		// currentSampleData.getBaseSampleData( ).clear( );
		// currentSampleData.getBaseSampleData( ).addAll( vNewBaseSampleData );

		// Convert orthogonal sample data
		EList<OrthogonalSampleData> osdList = currentSampleData.getOrthogonalSampleData();
		for (int i = 0; i < osdList.size(); i++) {
			if (i == iSeriesDefinitionIndex) {
				OrthogonalSampleData osd = osdList.get(i);
				osd.setDataSetRepresentation(
						getConvertedOrthogonalSampleDataRepresentation(osd.getDataSetRepresentation()));
				currentSampleData.getOrthogonalSampleData().set(i, osd);
			}
		}
		return currentSampleData;
	}

	// private String getConvertedBaseSampleDataRepresentation(
	// String sOldRepresentation )
	// {
	// StringTokenizer strtok = new StringTokenizer( sOldRepresentation, "," );
	// //$NON-NLS-1$
	// StringBuffer sbNewRepresentation = new StringBuffer( "" ); //$NON-NLS-1$
	// SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", //$NON-NLS-1$
	// Locale.getDefault( ) );
	// int iValueCount = 0;
	// while ( strtok.hasMoreTokens( ) )
	// {
	// String sElement = strtok.nextToken( ).trim( );
	// if ( !sElement.startsWith( "'" ) ) //$NON-NLS-1$
	// {
	// Calendar cal = Calendar.getInstance( );
	// // Increment the date once for each entry so that you get a
	// // sequence of dates
	// cal.set( Calendar.DATE, cal.get( Calendar.DATE ) + iValueCount );
	// sbNewRepresentation.append( sdf.format( cal.getTime( ) ) );
	// iValueCount++;
	// }
	// else
	// {
	// sElement = sElement.substring( 1, sElement.length( ) - 1 );
	// try
	// {
	// sdf.parse( sElement );
	// sbNewRepresentation.append( sElement );
	// }
	// catch ( ParseException e )
	// {
	// Calendar cal = Calendar.getInstance( );
	// // Increment the date once for each entry so that you get a
	// // sequence of dates
	// cal.set( Calendar.DATE, cal.get( Calendar.DATE )
	// + iValueCount );
	// sbNewRepresentation.append( sdf.format( cal.getTime( ) ) );
	// iValueCount++;
	// }
	// }
	// sbNewRepresentation.append( "," ); //$NON-NLS-1$
	// }
	// return sbNewRepresentation.toString( ).substring( 0,
	// sbNewRepresentation.length( ) - 1 );
	// }

	private String getConvertedOrthogonalSampleDataRepresentation(String sOldRepresentation) {
		StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ","); //$NON-NLS-1$
		NumberFormat nf = NumberFormat.getNumberInstance();
		StringBuffer sbNewRepresentation = new StringBuffer(""); //$NON-NLS-1$
		int iValueCount = 0;
		while (strtok.hasMoreTokens()) {
			String sElement = strtok.nextToken().trim();
			try {
				if (nf.parse(sElement).doubleValue() < 0) {
					// If the value is negative, use an arbitrary positive value
					sElement = String.valueOf(4.0 + iValueCount);
					iValueCount++;
				}
			} catch (ParseException e) {
				sElement = String.valueOf(4.0 + iValueCount);
				iValueCount++;
			}
			sbNewRepresentation.append("H"); //$NON-NLS-1$
			sbNewRepresentation.append(sElement);
			sbNewRepresentation.append(" "); //$NON-NLS-1$

			sbNewRepresentation.append(" L"); //$NON-NLS-1$
			sbNewRepresentation.append(sElement);
			sbNewRepresentation.append(" "); //$NON-NLS-1$

			sbNewRepresentation.append(" O"); //$NON-NLS-1$
			sbNewRepresentation.append(sElement);
			sbNewRepresentation.append(" "); //$NON-NLS-1$

			sbNewRepresentation.append(" C"); //$NON-NLS-1$
			sbNewRepresentation.append(sElement);
			sbNewRepresentation.append(","); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canShareAxisUnit()
	 */
	public boolean canShareAxisUnit() {
		return true;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance NOTE:
	 * Manually written
	 * 
	 */
	public static final Series create() {
		final StockSeries ss = TypeFactory.eINSTANCE.createStockSeries();
		((StockSeriesImpl) ss).initialize();
		return ss;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#initialize()
	 */
	protected final void initialize() {
		super.initialize();
		setLabelPosition(Position.ABOVE_LITERAL);
		final LineAttributes lia = LineAttributesImpl.create(null, LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(true);
		setLineAttributes(lia);
	}

	/**
	 * A convenience method to create an initialized 'Series' instance NOTE:
	 * Manually written
	 * 
	 */
	public static final Series createDefault() {
		final StockSeries ss = TypeFactory.eINSTANCE.createStockSeries();
		((StockSeriesImpl) ss).initDefault();
		return ss;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#initialize()
	 */
	protected final void initDefault() {
		super.initDefault();
		labelPosition = Position.ABOVE_LITERAL;
		final LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setLineAttributes(lia);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("StockSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public StockSeries copyInstance() {
		StockSeriesImpl dest = new StockSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(StockSeries src) {

		super.set(src);

		// children

		if (src.getFill() != null) {
			setFill(src.getFill().copyInstance());
		}

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		// attributes

		showAsBarStick = src.isShowAsBarStick();

		showAsBarStickESet = src.isSetShowAsBarStick();

		stickLength = src.getStickLength();

		stickLengthESet = src.isSetStickLength();

	}

	@Override
	public int[] getDefinedDataDefinitionIndex() {
		return new int[] { 0, 1, 2, 3 };
	}

} // StockSeriesImpl
