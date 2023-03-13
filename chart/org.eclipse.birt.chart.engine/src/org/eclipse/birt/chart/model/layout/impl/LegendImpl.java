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

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.computation.LegendBuilder;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Legend</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getHorizontalSpacing
 * <em>Horizontal Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getVerticalSpacing
 * <em>Vertical Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getClientArea
 * <em>Client Area</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getText
 * <em>Text</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getOrientation
 * <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getDirection
 * <em>Direction</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getSeparator
 * <em>Separator</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getPosition
 * <em>Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getItemType
 * <em>Item Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getTitle
 * <em>Title</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getTitlePosition
 * <em>Title Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#isShowValue
 * <em>Show Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#isShowPercent
 * <em>Show Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#isShowTotal
 * <em>Show Total</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getWrappingSize
 * <em>Wrapping Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getMaxPercent
 * <em>Max Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getTitlePercent
 * <em>Title Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getEllipsis
 * <em>Ellipsis</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LegendImpl extends BlockImpl implements Legend {

	/**
	 * The default value of the '{@link #getHorizontalSpacing() <em>Horizontal
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getHorizontalSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int HORIZONTAL_SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHorizontalSpacing() <em>Horizontal
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getHorizontalSpacing()
	 * @generated
	 * @ordered
	 */
	protected int horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;

	/**
	 * This is true if the Horizontal Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean horizontalSpacingESet;

	/**
	 * The default value of the ' {@link #getVerticalSpacing() <em>Vertical
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getVerticalSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int VERTICAL_SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the ' {@link #getVerticalSpacing() <em>Vertical
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getVerticalSpacing()
	 * @generated
	 * @ordered
	 */
	protected int verticalSpacing = VERTICAL_SPACING_EDEFAULT;

	/**
	 * This is true if the Vertical Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean verticalSpacingESet;

	/**
	 * The cached value of the '{@link #getClientArea() <em>Client Area</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getClientArea()
	 * @generated
	 * @ordered
	 */
	protected ClientArea clientArea;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected Text text;

	/**
	 * The default value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected static final Orientation ORIENTATION_EDEFAULT = Orientation.VERTICAL_LITERAL;

	/**
	 * The cached value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected Orientation orientation = ORIENTATION_EDEFAULT;

	/**
	 * This is true if the Orientation attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean orientationESet;

	/**
	 * The default value of the '{@link #getDirection() <em>Direction</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getDirection()
	 * @ordered
	 */
	protected static final Direction DIRECTION_EDEFAULT = Direction.TOP_BOTTOM_LITERAL;

	/**
	 * The cached value of the '{@link #getDirection() <em>Direction</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected Direction direction = DIRECTION_EDEFAULT;

	/**
	 * This is true if the Direction attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean directionESet;

	/**
	 * The cached value of the '{@link #getSeparator() <em>Separator</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes separator;

	/**
	 * The default value of the '{@link #getPosition() <em>Position</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position POSITION_EDEFAULT = Position.RIGHT_LITERAL;

	/**
	 * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPosition()
	 * @generated
	 * @ordered
	 */
	protected Position position = POSITION_EDEFAULT;

	/**
	 * This is true if the Position attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean positionESet;

	/**
	 * The default value of the '{@link #getItemType() <em>Item Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getItemType()
	 * @generated
	 * @ordered
	 */
	protected static final LegendItemType ITEM_TYPE_EDEFAULT = LegendItemType.SERIES_LITERAL;

	/**
	 * The cached value of the '{@link #getItemType() <em>Item Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getItemType()
	 * @generated
	 * @ordered
	 */
	protected LegendItemType itemType = ITEM_TYPE_EDEFAULT;

	/**
	 * This is true if the Item Type attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean itemTypeESet;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected Label title;

	/**
	 * The default value of the '{@link #getTitlePosition() <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTitlePosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position TITLE_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

	/**
	 * The cached value of the '{@link #getTitlePosition() <em>Title Position</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTitlePosition()
	 * @generated
	 * @ordered
	 */
	protected Position titlePosition = TITLE_POSITION_EDEFAULT;

	/**
	 * This is true if the Title Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean titlePositionESet;

	/**
	 * The default value of the '{@link #isShowValue() <em>Show Value</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowValue()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_VALUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowValue() <em>Show Value</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowValue()
	 * @generated
	 * @ordered
	 */
	protected boolean showValue = SHOW_VALUE_EDEFAULT;

	/**
	 * This is true if the Show Value attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean showValueESet;

	/**
	 * The default value of the '{@link #isShowPercent() <em>Show Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowPercent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_PERCENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowPercent() <em>Show Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowPercent()
	 * @generated
	 * @ordered
	 */
	protected boolean showPercent = SHOW_PERCENT_EDEFAULT;

	/**
	 * This is true if the Show Percent attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean showPercentESet;

	/**
	 * The default value of the '{@link #isShowTotal() <em>Show Total</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowTotal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_TOTAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowTotal() <em>Show Total</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowTotal()
	 * @generated
	 * @ordered
	 */
	protected boolean showTotal = SHOW_TOTAL_EDEFAULT;

	/**
	 * This is true if the Show Total attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean showTotalESet;

	/**
	 * The default value of the '{@link #getWrappingSize() <em>Wrapping Size</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWrappingSize()
	 * @generated
	 * @ordered
	 */
	protected static final double WRAPPING_SIZE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWrappingSize() <em>Wrapping Size</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWrappingSize()
	 * @generated
	 * @ordered
	 */
	protected double wrappingSize = WRAPPING_SIZE_EDEFAULT;

	/**
	 * This is true if the Wrapping Size attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean wrappingSizeESet;

	/**
	 * The default value of the '{@link #getMaxPercent() <em>Max Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMaxPercent()
	 * @generated
	 * @ordered
	 */
	protected static final double MAX_PERCENT_EDEFAULT = 0.33333333;

	/**
	 * The cached value of the '{@link #getMaxPercent() <em>Max Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMaxPercent()
	 * @generated
	 * @ordered
	 */
	protected double maxPercent = MAX_PERCENT_EDEFAULT;

	/**
	 * This is true if the Max Percent attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean maxPercentESet;

	/**
	 * The default value of the '{@link #getTitlePercent() <em>Title Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTitlePercent()
	 * @generated
	 * @ordered
	 */
	protected static final double TITLE_PERCENT_EDEFAULT = 0.6;

	/**
	 * The cached value of the '{@link #getTitlePercent() <em>Title Percent</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTitlePercent()
	 * @generated
	 * @ordered
	 */
	protected double titlePercent = TITLE_PERCENT_EDEFAULT;

	/**
	 * This is true if the Title Percent attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean titlePercentESet;

	/**
	 * The default value of the '{@link #getEllipsis() <em>Ellipsis</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEllipsis()
	 * @generated
	 * @ordered
	 */
	protected static final int ELLIPSIS_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getEllipsis() <em>Ellipsis</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEllipsis()
	 * @generated
	 * @ordered
	 */
	protected int ellipsis = ELLIPSIS_EDEFAULT;

	/**
	 * This is true if the Ellipsis attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean ellipsisESet;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected LegendImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LayoutPackage.Literals.LEGEND;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setHorizontalSpacing(int newHorizontalSpacing) {
		int oldHorizontalSpacing = horizontalSpacing;
		horizontalSpacing = newHorizontalSpacing;
		boolean oldHorizontalSpacingESet = horizontalSpacingESet;
		horizontalSpacingESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__HORIZONTAL_SPACING,
					oldHorizontalSpacing, horizontalSpacing, !oldHorizontalSpacingESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetHorizontalSpacing() {
		int oldHorizontalSpacing = horizontalSpacing;
		boolean oldHorizontalSpacingESet = horizontalSpacingESet;
		horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;
		horizontalSpacingESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__HORIZONTAL_SPACING,
					oldHorizontalSpacing, HORIZONTAL_SPACING_EDEFAULT, oldHorizontalSpacingESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetHorizontalSpacing() {
		return horizontalSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setVerticalSpacing(int newVerticalSpacing) {
		int oldVerticalSpacing = verticalSpacing;
		verticalSpacing = newVerticalSpacing;
		boolean oldVerticalSpacingESet = verticalSpacingESet;
		verticalSpacingESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__VERTICAL_SPACING,
					oldVerticalSpacing, verticalSpacing, !oldVerticalSpacingESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetVerticalSpacing() {
		int oldVerticalSpacing = verticalSpacing;
		boolean oldVerticalSpacingESet = verticalSpacingESet;
		verticalSpacing = VERTICAL_SPACING_EDEFAULT;
		verticalSpacingESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__VERTICAL_SPACING,
					oldVerticalSpacing, VERTICAL_SPACING_EDEFAULT, oldVerticalSpacingESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetVerticalSpacing() {
		return verticalSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ClientArea getClientArea() {
		return clientArea;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetClientArea(ClientArea newClientArea, NotificationChain msgs) {
		ClientArea oldClientArea = clientArea;
		clientArea = newClientArea;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.LEGEND__CLIENT_AREA, oldClientArea, newClientArea);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setClientArea(ClientArea newClientArea) {
		if (newClientArea != clientArea) {
			NotificationChain msgs = null;
			if (clientArea != null) {
				msgs = ((InternalEObject) clientArea).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__CLIENT_AREA, null, msgs);
			}
			if (newClientArea != null) {
				msgs = ((InternalEObject) newClientArea).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__CLIENT_AREA, null, msgs);
			}
			msgs = basicSetClientArea(newClientArea, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__CLIENT_AREA, newClientArea,
					newClientArea));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Text getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetText(Text newText, NotificationChain msgs) {
		Text oldText = text;
		text = newText;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TEXT,
					oldText, newText);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setText(Text newText) {
		if (newText != text) {
			NotificationChain msgs = null;
			if (text != null) {
				msgs = ((InternalEObject) text).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__TEXT, null, msgs);
			}
			if (newText != null) {
				msgs = ((InternalEObject) newText).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__TEXT, null, msgs);
			}
			msgs = basicSetText(newText, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TEXT, newText, newText));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOrientation(Orientation newOrientation) {
		Orientation oldOrientation = orientation;
		orientation = newOrientation == null ? ORIENTATION_EDEFAULT : newOrientation;
		boolean oldOrientationESet = orientationESet;
		orientationESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__ORIENTATION, oldOrientation,
					orientation, !oldOrientationESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetOrientation() {
		Orientation oldOrientation = orientation;
		boolean oldOrientationESet = orientationESet;
		orientation = ORIENTATION_EDEFAULT;
		orientationESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__ORIENTATION, oldOrientation,
					ORIENTATION_EDEFAULT, oldOrientationESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetOrientation() {
		return orientationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Direction getDirection() {
		return direction;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDirection(Direction newDirection) {
		Direction oldDirection = direction;
		direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
		boolean oldDirectionESet = directionESet;
		directionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__DIRECTION, oldDirection,
					direction, !oldDirectionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetDirection() {
		Direction oldDirection = direction;
		boolean oldDirectionESet = directionESet;
		direction = DIRECTION_EDEFAULT;
		directionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__DIRECTION, oldDirection,
					DIRECTION_EDEFAULT, oldDirectionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetDirection() {
		return directionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes getSeparator() {
		return separator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetSeparator(LineAttributes newSeparator, NotificationChain msgs) {
		LineAttributes oldSeparator = separator;
		separator = newSeparator;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.LEGEND__SEPARATOR, oldSeparator, newSeparator);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setSeparator(LineAttributes newSeparator) {
		if (newSeparator != separator) {
			NotificationChain msgs = null;
			if (separator != null) {
				msgs = ((InternalEObject) separator).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__SEPARATOR, null, msgs);
			}
			if (newSeparator != null) {
				msgs = ((InternalEObject) newSeparator).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__SEPARATOR, null, msgs);
			}
			msgs = basicSetSeparator(newSeparator, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__SEPARATOR, newSeparator,
					newSeparator));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setPosition(Position newPosition) {
		Position oldPosition = position;
		position = newPosition == null ? POSITION_EDEFAULT : newPosition;
		boolean oldPositionESet = positionESet;
		positionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__POSITION, oldPosition, position,
					!oldPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetPosition() {
		Position oldPosition = position;
		boolean oldPositionESet = positionESet;
		position = POSITION_EDEFAULT;
		positionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__POSITION, oldPosition,
					POSITION_EDEFAULT, oldPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetPosition() {
		return positionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LegendItemType getItemType() {
		return itemType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setItemType(LegendItemType newItemType) {
		LegendItemType oldItemType = itemType;
		itemType = newItemType == null ? ITEM_TYPE_EDEFAULT : newItemType;
		boolean oldItemTypeESet = itemTypeESet;
		itemTypeESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__ITEM_TYPE, oldItemType,
					itemType, !oldItemTypeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetItemType() {
		LegendItemType oldItemType = itemType;
		boolean oldItemTypeESet = itemTypeESet;
		itemType = ITEM_TYPE_EDEFAULT;
		itemTypeESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__ITEM_TYPE, oldItemType,
					ITEM_TYPE_EDEFAULT, oldItemTypeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetItemType() {
		return itemTypeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetTitle(Label newTitle, NotificationChain msgs) {
		Label oldTitle = title;
		title = newTitle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TITLE,
					oldTitle, newTitle);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setTitle(Label newTitle) {
		if (newTitle != title) {
			NotificationChain msgs = null;
			if (title != null) {
				msgs = ((InternalEObject) title).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__TITLE, null, msgs);
			}
			if (newTitle != null) {
				msgs = ((InternalEObject) newTitle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__TITLE, null, msgs);
			}
			msgs = basicSetTitle(newTitle, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TITLE, newTitle, newTitle));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Position getTitlePosition() {
		return titlePosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setTitlePosition(Position newTitlePosition) {
		Position oldTitlePosition = titlePosition;
		titlePosition = newTitlePosition == null ? TITLE_POSITION_EDEFAULT : newTitlePosition;
		boolean oldTitlePositionESet = titlePositionESet;
		titlePositionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TITLE_POSITION,
					oldTitlePosition, titlePosition, !oldTitlePositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetTitlePosition() {
		Position oldTitlePosition = titlePosition;
		boolean oldTitlePositionESet = titlePositionESet;
		titlePosition = TITLE_POSITION_EDEFAULT;
		titlePositionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__TITLE_POSITION,
					oldTitlePosition, TITLE_POSITION_EDEFAULT, oldTitlePositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetTitlePosition() {
		return titlePositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isShowValue() {
		return showValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setShowValue(boolean newShowValue) {
		boolean oldShowValue = showValue;
		showValue = newShowValue;
		boolean oldShowValueESet = showValueESet;
		showValueESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__SHOW_VALUE, oldShowValue,
					showValue, !oldShowValueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetShowValue() {
		boolean oldShowValue = showValue;
		boolean oldShowValueESet = showValueESet;
		showValue = SHOW_VALUE_EDEFAULT;
		showValueESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__SHOW_VALUE, oldShowValue,
					SHOW_VALUE_EDEFAULT, oldShowValueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetShowValue() {
		return showValueESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isShowPercent() {
		return showPercent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setShowPercent(boolean newShowPercent) {
		boolean oldShowPercent = showPercent;
		showPercent = newShowPercent;
		boolean oldShowPercentESet = showPercentESet;
		showPercentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__SHOW_PERCENT, oldShowPercent,
					showPercent, !oldShowPercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetShowPercent() {
		boolean oldShowPercent = showPercent;
		boolean oldShowPercentESet = showPercentESet;
		showPercent = SHOW_PERCENT_EDEFAULT;
		showPercentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__SHOW_PERCENT, oldShowPercent,
					SHOW_PERCENT_EDEFAULT, oldShowPercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetShowPercent() {
		return showPercentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isShowTotal() {
		return showTotal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setShowTotal(boolean newShowTotal) {
		boolean oldShowTotal = showTotal;
		showTotal = newShowTotal;
		boolean oldShowTotalESet = showTotalESet;
		showTotalESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__SHOW_TOTAL, oldShowTotal,
					showTotal, !oldShowTotalESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetShowTotal() {
		boolean oldShowTotal = showTotal;
		boolean oldShowTotalESet = showTotalESet;
		showTotal = SHOW_TOTAL_EDEFAULT;
		showTotalESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__SHOW_TOTAL, oldShowTotal,
					SHOW_TOTAL_EDEFAULT, oldShowTotalESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetShowTotal() {
		return showTotalESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getWrappingSize() {
		return wrappingSize;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setWrappingSize(double newWrappingSize) {
		double oldWrappingSize = wrappingSize;
		wrappingSize = newWrappingSize;
		boolean oldWrappingSizeESet = wrappingSizeESet;
		wrappingSizeESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__WRAPPING_SIZE, oldWrappingSize,
					wrappingSize, !oldWrappingSizeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetWrappingSize() {
		double oldWrappingSize = wrappingSize;
		boolean oldWrappingSizeESet = wrappingSizeESet;
		wrappingSize = WRAPPING_SIZE_EDEFAULT;
		wrappingSizeESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__WRAPPING_SIZE,
					oldWrappingSize, WRAPPING_SIZE_EDEFAULT, oldWrappingSizeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetWrappingSize() {
		return wrappingSizeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getMaxPercent() {
		return maxPercent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMaxPercent(double newMaxPercent) {
		double oldMaxPercent = maxPercent;
		maxPercent = newMaxPercent;
		boolean oldMaxPercentESet = maxPercentESet;
		maxPercentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__MAX_PERCENT, oldMaxPercent,
					maxPercent, !oldMaxPercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetMaxPercent() {
		double oldMaxPercent = maxPercent;
		boolean oldMaxPercentESet = maxPercentESet;
		maxPercent = MAX_PERCENT_EDEFAULT;
		maxPercentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__MAX_PERCENT, oldMaxPercent,
					MAX_PERCENT_EDEFAULT, oldMaxPercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetMaxPercent() {
		return maxPercentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getTitlePercent() {
		return titlePercent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setTitlePercent(double newTitlePercent) {
		double oldTitlePercent = titlePercent;
		titlePercent = newTitlePercent;
		boolean oldTitlePercentESet = titlePercentESet;
		titlePercentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TITLE_PERCENT, oldTitlePercent,
					titlePercent, !oldTitlePercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetTitlePercent() {
		double oldTitlePercent = titlePercent;
		boolean oldTitlePercentESet = titlePercentESet;
		titlePercent = TITLE_PERCENT_EDEFAULT;
		titlePercentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__TITLE_PERCENT,
					oldTitlePercent, TITLE_PERCENT_EDEFAULT, oldTitlePercentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetTitlePercent() {
		return titlePercentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getEllipsis() {
		return ellipsis;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setEllipsis(int newEllipsis) {
		int oldEllipsis = ellipsis;
		ellipsis = newEllipsis;
		boolean oldEllipsisESet = ellipsisESet;
		ellipsisESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__ELLIPSIS, oldEllipsis, ellipsis,
					!oldEllipsisESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetEllipsis() {
		int oldEllipsis = ellipsis;
		boolean oldEllipsisESet = ellipsisESet;
		ellipsis = ELLIPSIS_EDEFAULT;
		ellipsisESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__ELLIPSIS, oldEllipsis,
					ELLIPSIS_EDEFAULT, oldEllipsisESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetEllipsis() {
		return ellipsisESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
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
					LayoutPackage.LEGEND__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null) {
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__FORMAT_SPECIFIER, null, msgs);
			}
			if (newFormatSpecifier != null) {
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.LEGEND__FORMAT_SPECIFIER, null, msgs);
			}
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__FORMAT_SPECIFIER,
					newFormatSpecifier, newFormatSpecifier));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case LayoutPackage.LEGEND__CLIENT_AREA:
			return basicSetClientArea(null, msgs);
		case LayoutPackage.LEGEND__TEXT:
			return basicSetText(null, msgs);
		case LayoutPackage.LEGEND__SEPARATOR:
			return basicSetSeparator(null, msgs);
		case LayoutPackage.LEGEND__TITLE:
			return basicSetTitle(null, msgs);
		case LayoutPackage.LEGEND__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
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
		case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
			return getHorizontalSpacing();
		case LayoutPackage.LEGEND__VERTICAL_SPACING:
			return getVerticalSpacing();
		case LayoutPackage.LEGEND__CLIENT_AREA:
			return getClientArea();
		case LayoutPackage.LEGEND__TEXT:
			return getText();
		case LayoutPackage.LEGEND__ORIENTATION:
			return getOrientation();
		case LayoutPackage.LEGEND__DIRECTION:
			return getDirection();
		case LayoutPackage.LEGEND__SEPARATOR:
			return getSeparator();
		case LayoutPackage.LEGEND__POSITION:
			return getPosition();
		case LayoutPackage.LEGEND__ITEM_TYPE:
			return getItemType();
		case LayoutPackage.LEGEND__TITLE:
			return getTitle();
		case LayoutPackage.LEGEND__TITLE_POSITION:
			return getTitlePosition();
		case LayoutPackage.LEGEND__SHOW_VALUE:
			return isShowValue();
		case LayoutPackage.LEGEND__SHOW_PERCENT:
			return isShowPercent();
		case LayoutPackage.LEGEND__SHOW_TOTAL:
			return isShowTotal();
		case LayoutPackage.LEGEND__WRAPPING_SIZE:
			return getWrappingSize();
		case LayoutPackage.LEGEND__MAX_PERCENT:
			return getMaxPercent();
		case LayoutPackage.LEGEND__TITLE_PERCENT:
			return getTitlePercent();
		case LayoutPackage.LEGEND__ELLIPSIS:
			return getEllipsis();
		case LayoutPackage.LEGEND__FORMAT_SPECIFIER:
			return getFormatSpecifier();
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
		case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
			setHorizontalSpacing((Integer) newValue);
			return;
		case LayoutPackage.LEGEND__VERTICAL_SPACING:
			setVerticalSpacing((Integer) newValue);
			return;
		case LayoutPackage.LEGEND__CLIENT_AREA:
			setClientArea((ClientArea) newValue);
			return;
		case LayoutPackage.LEGEND__TEXT:
			setText((Text) newValue);
			return;
		case LayoutPackage.LEGEND__ORIENTATION:
			setOrientation((Orientation) newValue);
			return;
		case LayoutPackage.LEGEND__DIRECTION:
			setDirection((Direction) newValue);
			return;
		case LayoutPackage.LEGEND__SEPARATOR:
			setSeparator((LineAttributes) newValue);
			return;
		case LayoutPackage.LEGEND__POSITION:
			setPosition((Position) newValue);
			return;
		case LayoutPackage.LEGEND__ITEM_TYPE:
			setItemType((LegendItemType) newValue);
			return;
		case LayoutPackage.LEGEND__TITLE:
			setTitle((Label) newValue);
			return;
		case LayoutPackage.LEGEND__TITLE_POSITION:
			setTitlePosition((Position) newValue);
			return;
		case LayoutPackage.LEGEND__SHOW_VALUE:
			setShowValue((Boolean) newValue);
			return;
		case LayoutPackage.LEGEND__SHOW_PERCENT:
			setShowPercent((Boolean) newValue);
			return;
		case LayoutPackage.LEGEND__SHOW_TOTAL:
			setShowTotal((Boolean) newValue);
			return;
		case LayoutPackage.LEGEND__WRAPPING_SIZE:
			setWrappingSize((Double) newValue);
			return;
		case LayoutPackage.LEGEND__MAX_PERCENT:
			setMaxPercent((Double) newValue);
			return;
		case LayoutPackage.LEGEND__TITLE_PERCENT:
			setTitlePercent((Double) newValue);
			return;
		case LayoutPackage.LEGEND__ELLIPSIS:
			setEllipsis((Integer) newValue);
			return;
		case LayoutPackage.LEGEND__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
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
		case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
			unsetHorizontalSpacing();
			return;
		case LayoutPackage.LEGEND__VERTICAL_SPACING:
			unsetVerticalSpacing();
			return;
		case LayoutPackage.LEGEND__CLIENT_AREA:
			setClientArea((ClientArea) null);
			return;
		case LayoutPackage.LEGEND__TEXT:
			setText((Text) null);
			return;
		case LayoutPackage.LEGEND__ORIENTATION:
			unsetOrientation();
			return;
		case LayoutPackage.LEGEND__DIRECTION:
			unsetDirection();
			return;
		case LayoutPackage.LEGEND__SEPARATOR:
			setSeparator((LineAttributes) null);
			return;
		case LayoutPackage.LEGEND__POSITION:
			unsetPosition();
			return;
		case LayoutPackage.LEGEND__ITEM_TYPE:
			unsetItemType();
			return;
		case LayoutPackage.LEGEND__TITLE:
			setTitle((Label) null);
			return;
		case LayoutPackage.LEGEND__TITLE_POSITION:
			unsetTitlePosition();
			return;
		case LayoutPackage.LEGEND__SHOW_VALUE:
			unsetShowValue();
			return;
		case LayoutPackage.LEGEND__SHOW_PERCENT:
			unsetShowPercent();
			return;
		case LayoutPackage.LEGEND__SHOW_TOTAL:
			unsetShowTotal();
			return;
		case LayoutPackage.LEGEND__WRAPPING_SIZE:
			unsetWrappingSize();
			return;
		case LayoutPackage.LEGEND__MAX_PERCENT:
			unsetMaxPercent();
			return;
		case LayoutPackage.LEGEND__TITLE_PERCENT:
			unsetTitlePercent();
			return;
		case LayoutPackage.LEGEND__ELLIPSIS:
			unsetEllipsis();
			return;
		case LayoutPackage.LEGEND__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
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
		case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
			return isSetHorizontalSpacing();
		case LayoutPackage.LEGEND__VERTICAL_SPACING:
			return isSetVerticalSpacing();
		case LayoutPackage.LEGEND__CLIENT_AREA:
			return clientArea != null;
		case LayoutPackage.LEGEND__TEXT:
			return text != null;
		case LayoutPackage.LEGEND__ORIENTATION:
			return isSetOrientation();
		case LayoutPackage.LEGEND__DIRECTION:
			return isSetDirection();
		case LayoutPackage.LEGEND__SEPARATOR:
			return separator != null;
		case LayoutPackage.LEGEND__POSITION:
			return isSetPosition();
		case LayoutPackage.LEGEND__ITEM_TYPE:
			return isSetItemType();
		case LayoutPackage.LEGEND__TITLE:
			return title != null;
		case LayoutPackage.LEGEND__TITLE_POSITION:
			return isSetTitlePosition();
		case LayoutPackage.LEGEND__SHOW_VALUE:
			return isSetShowValue();
		case LayoutPackage.LEGEND__SHOW_PERCENT:
			return isSetShowPercent();
		case LayoutPackage.LEGEND__SHOW_TOTAL:
			return isSetShowTotal();
		case LayoutPackage.LEGEND__WRAPPING_SIZE:
			return isSetWrappingSize();
		case LayoutPackage.LEGEND__MAX_PERCENT:
			return isSetMaxPercent();
		case LayoutPackage.LEGEND__TITLE_PERCENT:
			return isSetTitlePercent();
		case LayoutPackage.LEGEND__ELLIPSIS:
			return isSetEllipsis();
		case LayoutPackage.LEGEND__FORMAT_SPECIFIER:
			return formatSpecifier != null;
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
		result.append(" (horizontalSpacing: "); //$NON-NLS-1$
		if (horizontalSpacingESet) {
			result.append(horizontalSpacing);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", verticalSpacing: "); //$NON-NLS-1$
		if (verticalSpacingESet) {
			result.append(verticalSpacing);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", orientation: "); //$NON-NLS-1$
		if (orientationESet) {
			result.append(orientation);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", direction: "); //$NON-NLS-1$
		if (directionESet) {
			result.append(direction);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", position: "); //$NON-NLS-1$
		if (positionESet) {
			result.append(position);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", itemType: "); //$NON-NLS-1$
		if (itemTypeESet) {
			result.append(itemType);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", titlePosition: "); //$NON-NLS-1$
		if (titlePositionESet) {
			result.append(titlePosition);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", showValue: "); //$NON-NLS-1$
		if (showValueESet) {
			result.append(showValue);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", showPercent: "); //$NON-NLS-1$
		if (showPercentESet) {
			result.append(showPercent);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", showTotal: "); //$NON-NLS-1$
		if (showTotalESet) {
			result.append(showTotal);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", wrappingSize: "); //$NON-NLS-1$
		if (wrappingSizeESet) {
			result.append(wrappingSize);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", maxPercent: "); //$NON-NLS-1$
		if (maxPercentESet) {
			result.append(maxPercent);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", titlePercent: "); //$NON-NLS-1$
		if (titlePercentESet) {
			result.append(titlePercent);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", ellipsis: "); //$NON-NLS-1$
		if (ellipsisESet) {
			result.append(ellipsis);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 *
	 * Note: Manually written
	 *
	 * @return true if this block is legend.
	 */
	@Override
	public boolean isLegend() {
		return true;
	}

	/**
	 *
	 * Note: Manually written
	 *
	 * @return true if this is custom block.
	 */
	@Override
	public boolean isCustom() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.layout.Block#getPreferredSize(org.eclipse.birt.
	 * chart.device.IDisplayServer, org.eclipse.birt.chart.model.Chart,
	 * org.eclipse.birt.chart.factory.RunTimeContext)
	 */
	@Override
	public final Size getPreferredSize(IDisplayServer xs, Chart cm, RunTimeContext rtc) throws ChartException {
		Size sz;

		// TODO check context consistency.

		if (rtc != null && rtc.getLegendLayoutHints() != null && rtc.getLegendLayoutHints().getLegendSize() != null) {
			sz = rtc.getLegendLayoutHints().getLegendSize();
		} else {
			// COMPUTE THE LEGEND CONTENT (TO ENSURE THAT THE PREFERRED SIZE IS
			// OBTAINED)
			final LegendBuilder lb = new LegendBuilder();
			final SeriesDefinition[] seda = cm.getSeriesForLegend();
			sz = lb.compute(xs, cm, seda, rtc);
		}

		// CONVERT TO POINTS
		sz = sz.scaleInstance(72d / xs.getDpiResolution());
		final Insets ins = this.getInsets();
		sz.setWidth(sz.getWidth() + ins.getLeft() + ins.getRight());
		sz.setHeight(sz.getHeight() + ins.getTop() + ins.getBottom());
		return sz;
	}

	/**
	 * A convenience method to create an initialized 'Legend' instance
	 *
	 * @return legend instance with setting 'isSet' flag.
	 */
	public static final Block create() {
		final Legend lg = LayoutFactory.eINSTANCE.createLegend();
		((LegendImpl) lg).initialize();
		return lg;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected final void initialize() {
		super.initialize();
		setPosition(Position.RIGHT_LITERAL);
		setOrientation(Orientation.VERTICAL_LITERAL);
		setDirection(Direction.TOP_BOTTOM_LITERAL);
		setItemType(LegendItemType.SERIES_LITERAL);

		Label la = LabelImpl.create();
		LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		la.setOutline(lia);
		la.setVisible(false);
		setTitle(la);
		setTitlePosition(Position.ABOVE_LITERAL);

		LineAttributes separator = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		separator.setVisible(true);
		setSeparator(separator);

		final ClientArea ca = LayoutFactory.eINSTANCE.createClientArea();
		((ClientAreaImpl) ca).initialize();
		ca.getInsets().set(2, 2, 2, 2);
		setClientArea(ca);

		setText(TextImpl.create((String) null));
	}

	/**
	 * A convenience method to create an initialized 'Legend' instance
	 *
	 * @return legend instance without setting 'isSet' flag.
	 */
	public static final Block createDefault() {
		final Legend lg = LayoutFactory.eINSTANCE.createLegend();
		((LegendImpl) lg).initDefault();
		return lg;
	}

	/**
	 * Resets all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected final void initDefault() {
		super.initDefault();
		position = Position.RIGHT_LITERAL;
		orientation = Orientation.VERTICAL_LITERAL;
		direction = Direction.TOP_BOTTOM_LITERAL;
		itemType = LegendItemType.SERIES_LITERAL;

		Label la = LabelImpl.createDefault(false);
		LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		la.setOutline(lia);
		la.getCaption().setValue(null);
		setTitle(la);
		titlePosition = Position.ABOVE_LITERAL;

		LineAttributes separator = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1);
		setSeparator(separator);

		final ClientArea ca = LayoutFactory.eINSTANCE.createClientArea();
		((ClientAreaImpl) ca).initDefault();

		ca.setInsets(InsetsImpl.createDefault(2, 2, 2, 2));
		setClientArea(ca);

		setText(TextImpl.createDefault((String) null));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.layout.Legend#updateLayout(org.eclipse.birt.
	 * chart.model.Chart)
	 */
	@Override
	public final void updateLayout(Chart cm) {
		final Legend lg = this;
		final Plot pl = cm.getPlot();
		final EList<Block> el = pl.getChildren();
		final Position p = lg.getPosition();
		final boolean bLegendInsidePlot = p.getValue() == Position.INSIDE;
		final boolean bPlotContainsLegend = el.indexOf(lg) >= 0;

		// IF PLOT DOESNT CONTAIN LEGEND AND LEGEND IS SUPPOSED TO BE INSIDE
		if (!bPlotContainsLegend && bLegendInsidePlot) {
			el.add(lg); // ADD LEGEND TO PLOT
		} else if (bPlotContainsLegend && !bLegendInsidePlot) {
			cm.getBlock().getChildren().add(lg); // ADD LEGEND TO BLOCK
		} else {
			// PLOT/LEGEND RELATIONSHIP IS FINE; DON'T DO ANYTHING
		}
	}

	/**
	 * @generated
	 */
	@Override
	public Legend copyInstance() {
		LegendImpl dest = new LegendImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Legend src) {

		super.set(src);

		// children

		if (src.getClientArea() != null) {
			setClientArea(src.getClientArea().copyInstance());
		}

		if (src.getText() != null) {
			setText(src.getText().copyInstance());
		}

		if (src.getSeparator() != null) {
			setSeparator(src.getSeparator().copyInstance());
		}

		if (src.getTitle() != null) {
			setTitle(src.getTitle().copyInstance());
		}

		// attributes

		horizontalSpacing = src.getHorizontalSpacing();

		horizontalSpacingESet = src.isSetHorizontalSpacing();

		verticalSpacing = src.getVerticalSpacing();

		verticalSpacingESet = src.isSetVerticalSpacing();

		orientation = src.getOrientation();

		orientationESet = src.isSetOrientation();

		direction = src.getDirection();

		directionESet = src.isSetDirection();

		position = src.getPosition();

		positionESet = src.isSetPosition();

		itemType = src.getItemType();

		itemTypeESet = src.isSetItemType();

		titlePosition = src.getTitlePosition();

		titlePositionESet = src.isSetTitlePosition();

		showValue = src.isShowValue();

		showValueESet = src.isSetShowValue();

		showPercent = src.isShowPercent();

		showPercentESet = src.isSetShowPercent();

		showTotal = src.isShowTotal();

		showTotalESet = src.isSetShowTotal();

		wrappingSize = src.getWrappingSize();

		wrappingSizeESet = src.isSetWrappingSize();

		maxPercent = src.getMaxPercent();

		maxPercentESet = src.isSetMaxPercent();

		titlePercent = src.getTitlePercent();

		titlePercentESet = src.isSetTitlePercent();

		ellipsis = src.getEllipsis();

		ellipsisESet = src.isSetEllipsis();

		formatSpecifier = src.getFormatSpecifier();

	}

} // LegendImpl
