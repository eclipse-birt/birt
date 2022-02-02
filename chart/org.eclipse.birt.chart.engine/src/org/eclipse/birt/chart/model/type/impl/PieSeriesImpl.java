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

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Pie
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getExplosion
 * <em>Explosion</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getExplosionExpression
 * <em>Explosion Expression</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getTitle
 * <em>Title</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getTitlePosition
 * <em>Title Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getLeaderLineAttributes
 * <em>Leader Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getLeaderLineStyle
 * <em>Leader Line Style</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getLeaderLineLength
 * <em>Leader Line Length</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getSliceOutline
 * <em>Slice Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getRatio
 * <em>Ratio</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getRotation
 * <em>Rotation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#isClockwise
 * <em>Clockwise</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#getInnerRadius
 * <em>Inner Radius</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.PieSeriesImpl#isInnerRadiusPercent
 * <em>Inner Radius Percent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PieSeriesImpl extends SeriesImpl implements PieSeries {

	/**
	 * The default value of the '{@link #getExplosion() <em>Explosion</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExplosion()
	 * @generated
	 * @ordered
	 */
	protected static final int EXPLOSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getExplosion() <em>Explosion</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExplosion()
	 * @generated
	 * @ordered
	 */
	protected int explosion = EXPLOSION_EDEFAULT;

	/**
	 * This is true if the Explosion attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean explosionESet;

	/**
	 * The default value of the '{@link #getExplosionExpression() <em>Explosion
	 * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExplosionExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String EXPLOSION_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExplosionExpression() <em>Explosion
	 * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExplosionExpression()
	 * @generated
	 * @ordered
	 */
	protected String explosionExpression = EXPLOSION_EXPRESSION_EDEFAULT;

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
	 * The default value of the ' {@link #getTitlePosition() <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTitlePosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position TITLE_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

	/**
	 * The cached value of the ' {@link #getTitlePosition() <em>Title
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * The cached value of the '{@link #getLeaderLineAttributes() <em>Leader Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLeaderLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes leaderLineAttributes;

	/**
	 * The default value of the '{@link #getLeaderLineStyle() <em>Leader Line
	 * Style</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeaderLineStyle()
	 * @generated
	 * @ordered
	 */
	protected static final LeaderLineStyle LEADER_LINE_STYLE_EDEFAULT = LeaderLineStyle.FIXED_LENGTH_LITERAL;

	/**
	 * The cached value of the '{@link #getLeaderLineStyle() <em>Leader Line
	 * Style</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeaderLineStyle()
	 * @generated
	 * @ordered
	 */
	protected LeaderLineStyle leaderLineStyle = LEADER_LINE_STYLE_EDEFAULT;

	/**
	 * This is true if the Leader Line Style attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean leaderLineStyleESet;

	/**
	 * The default value of the '{@link #getLeaderLineLength() <em>Leader Line
	 * Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeaderLineLength()
	 * @generated
	 * @ordered
	 */
	protected static final double LEADER_LINE_LENGTH_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLeaderLineLength() <em>Leader Line
	 * Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeaderLineLength()
	 * @generated
	 * @ordered
	 */
	protected double leaderLineLength = LEADER_LINE_LENGTH_EDEFAULT;

	/**
	 * This is true if the Leader Line Length attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean leaderLineLengthESet;

	/**
	 * The cached value of the '{@link #getSliceOutline() <em>Slice Outline</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSliceOutline()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition sliceOutline;

	/**
	 * The default value of the '{@link #getRatio() <em>Ratio</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRatio()
	 * @generated
	 * @ordered
	 */
	protected static final double RATIO_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getRatio() <em>Ratio</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRatio()
	 * @generated
	 * @ordered
	 */
	protected double ratio = RATIO_EDEFAULT;

	/**
	 * This is true if the Ratio attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean ratioESet;

	/**
	 * The default value of the '{@link #getRotation() <em>Rotation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRotation()
	 * @generated
	 * @ordered
	 */
	protected static final double ROTATION_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRotation() <em>Rotation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRotation()
	 * @generated
	 * @ordered
	 */
	protected double rotation = ROTATION_EDEFAULT;

	/**
	 * This is true if the Rotation attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean rotationESet;

	/**
	 * The default value of the '{@link #isClockwise() <em>Clockwise</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isClockwise()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CLOCKWISE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isClockwise() <em>Clockwise</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isClockwise()
	 * @generated
	 * @ordered
	 */
	protected boolean clockwise = CLOCKWISE_EDEFAULT;

	/**
	 * This is true if the Clockwise attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean clockwiseESet;

	/**
	 * The default value of the '{@link #getInnerRadius() <em>Inner Radius</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInnerRadius()
	 * @generated
	 * @ordered
	 */
	protected static final double INNER_RADIUS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getInnerRadius() <em>Inner Radius</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInnerRadius()
	 * @generated
	 * @ordered
	 */
	protected double innerRadius = INNER_RADIUS_EDEFAULT;

	/**
	 * This is true if the Inner Radius attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean innerRadiusESet;

	/**
	 * The default value of the '{@link #isInnerRadiusPercent() <em>Inner Radius
	 * Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isInnerRadiusPercent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INNER_RADIUS_PERCENT_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isInnerRadiusPercent() <em>Inner Radius
	 * Percent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isInnerRadiusPercent()
	 * @generated
	 * @ordered
	 */
	protected boolean innerRadiusPercent = INNER_RADIUS_PERCENT_EDEFAULT;

	/**
	 * This is true if the Inner Radius Percent attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean innerRadiusPercentESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PieSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.PIE_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getExplosion() {
		return explosion;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setExplosion(int newExplosion) {
		int oldExplosion = explosion;
		explosion = newExplosion;
		boolean oldExplosionESet = explosionESet;
		explosionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__EXPLOSION, oldExplosion,
					explosion, !oldExplosionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetExplosion() {
		int oldExplosion = explosion;
		boolean oldExplosionESet = explosionESet;
		explosion = EXPLOSION_EDEFAULT;
		explosionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__EXPLOSION, oldExplosion,
					EXPLOSION_EDEFAULT, oldExplosionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetExplosion() {
		return explosionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getExplosionExpression() {
		return explosionExpression;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setExplosionExpression(String newExplosionExpression) {
		String oldExplosionExpression = explosionExpression;
		explosionExpression = newExplosionExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__EXPLOSION_EXPRESSION,
					oldExplosionExpression, explosionExpression));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.PIE_SERIES__TITLE, oldTitle, newTitle);
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
	public void setTitle(Label newTitle) {
		if (newTitle != title) {
			NotificationChain msgs = null;
			if (title != null)
				msgs = ((InternalEObject) title).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.PIE_SERIES__TITLE, null, msgs);
			if (newTitle != null)
				msgs = ((InternalEObject) newTitle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.PIE_SERIES__TITLE, null, msgs);
			msgs = basicSetTitle(newTitle, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__TITLE, newTitle, newTitle));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Position getTitlePosition() {
		return titlePosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTitlePosition(Position newTitlePosition) {
		Position oldTitlePosition = titlePosition;
		titlePosition = newTitlePosition == null ? TITLE_POSITION_EDEFAULT : newTitlePosition;
		boolean oldTitlePositionESet = titlePositionESet;
		titlePositionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__TITLE_POSITION,
					oldTitlePosition, titlePosition, !oldTitlePositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTitlePosition() {
		Position oldTitlePosition = titlePosition;
		boolean oldTitlePositionESet = titlePositionESet;
		titlePosition = TITLE_POSITION_EDEFAULT;
		titlePositionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__TITLE_POSITION,
					oldTitlePosition, TITLE_POSITION_EDEFAULT, oldTitlePositionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTitlePosition() {
		return titlePositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getLeaderLineAttributes() {
		return leaderLineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLeaderLineAttributes(LineAttributes newLeaderLineAttributes,
			NotificationChain msgs) {
		LineAttributes oldLeaderLineAttributes = leaderLineAttributes;
		leaderLineAttributes = newLeaderLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES, oldLeaderLineAttributes, newLeaderLineAttributes);
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
	public void setLeaderLineAttributes(LineAttributes newLeaderLineAttributes) {
		if (newLeaderLineAttributes != leaderLineAttributes) {
			NotificationChain msgs = null;
			if (leaderLineAttributes != null)
				msgs = ((InternalEObject) leaderLineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES, null, msgs);
			if (newLeaderLineAttributes != null)
				msgs = ((InternalEObject) newLeaderLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES, null, msgs);
			msgs = basicSetLeaderLineAttributes(newLeaderLineAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES,
					newLeaderLineAttributes, newLeaderLineAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LeaderLineStyle getLeaderLineStyle() {
		return leaderLineStyle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeaderLineStyle(LeaderLineStyle newLeaderLineStyle) {
		LeaderLineStyle oldLeaderLineStyle = leaderLineStyle;
		leaderLineStyle = newLeaderLineStyle == null ? LEADER_LINE_STYLE_EDEFAULT : newLeaderLineStyle;
		boolean oldLeaderLineStyleESet = leaderLineStyleESet;
		leaderLineStyleESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__LEADER_LINE_STYLE,
					oldLeaderLineStyle, leaderLineStyle, !oldLeaderLineStyleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLeaderLineStyle() {
		LeaderLineStyle oldLeaderLineStyle = leaderLineStyle;
		boolean oldLeaderLineStyleESet = leaderLineStyleESet;
		leaderLineStyle = LEADER_LINE_STYLE_EDEFAULT;
		leaderLineStyleESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__LEADER_LINE_STYLE,
					oldLeaderLineStyle, LEADER_LINE_STYLE_EDEFAULT, oldLeaderLineStyleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLeaderLineStyle() {
		return leaderLineStyleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getLeaderLineLength() {
		return leaderLineLength;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeaderLineLength(double newLeaderLineLength) {
		double oldLeaderLineLength = leaderLineLength;
		leaderLineLength = newLeaderLineLength;
		boolean oldLeaderLineLengthESet = leaderLineLengthESet;
		leaderLineLengthESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__LEADER_LINE_LENGTH,
					oldLeaderLineLength, leaderLineLength, !oldLeaderLineLengthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLeaderLineLength() {
		double oldLeaderLineLength = leaderLineLength;
		boolean oldLeaderLineLengthESet = leaderLineLengthESet;
		leaderLineLength = LEADER_LINE_LENGTH_EDEFAULT;
		leaderLineLengthESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__LEADER_LINE_LENGTH,
					oldLeaderLineLength, LEADER_LINE_LENGTH_EDEFAULT, oldLeaderLineLengthESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLeaderLineLength() {
		return leaderLineLengthESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ColorDefinition getSliceOutline() {
		return sliceOutline;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSliceOutline(ColorDefinition newSliceOutline, NotificationChain msgs) {
		ColorDefinition oldSliceOutline = sliceOutline;
		sliceOutline = newSliceOutline;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.PIE_SERIES__SLICE_OUTLINE, oldSliceOutline, newSliceOutline);
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
	public void setSliceOutline(ColorDefinition newSliceOutline) {
		if (newSliceOutline != sliceOutline) {
			NotificationChain msgs = null;
			if (sliceOutline != null)
				msgs = ((InternalEObject) sliceOutline).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.PIE_SERIES__SLICE_OUTLINE, null, msgs);
			if (newSliceOutline != null)
				msgs = ((InternalEObject) newSliceOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.PIE_SERIES__SLICE_OUTLINE, null, msgs);
			msgs = basicSetSliceOutline(newSliceOutline, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__SLICE_OUTLINE,
					newSliceOutline, newSliceOutline));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRatio(double newRatio) {
		double oldRatio = ratio;
		ratio = newRatio;
		boolean oldRatioESet = ratioESet;
		ratioESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__RATIO, oldRatio, ratio,
					!oldRatioESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetRatio() {
		double oldRatio = ratio;
		boolean oldRatioESet = ratioESet;
		ratio = RATIO_EDEFAULT;
		ratioESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__RATIO, oldRatio,
					RATIO_EDEFAULT, oldRatioESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetRatio() {
		return ratioESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRotation(double newRotation) {
		double oldRotation = rotation;
		rotation = newRotation;
		boolean oldRotationESet = rotationESet;
		rotationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__ROTATION, oldRotation,
					rotation, !oldRotationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetRotation() {
		double oldRotation = rotation;
		boolean oldRotationESet = rotationESet;
		rotation = ROTATION_EDEFAULT;
		rotationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__ROTATION, oldRotation,
					ROTATION_EDEFAULT, oldRotationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetRotation() {
		return rotationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isClockwise() {
		return clockwise;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setClockwise(boolean newClockwise) {
		boolean oldClockwise = clockwise;
		clockwise = newClockwise;
		boolean oldClockwiseESet = clockwiseESet;
		clockwiseESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__CLOCKWISE, oldClockwise,
					clockwise, !oldClockwiseESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetClockwise() {
		boolean oldClockwise = clockwise;
		boolean oldClockwiseESet = clockwiseESet;
		clockwise = CLOCKWISE_EDEFAULT;
		clockwiseESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__CLOCKWISE, oldClockwise,
					CLOCKWISE_EDEFAULT, oldClockwiseESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetClockwise() {
		return clockwiseESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getInnerRadius() {
		return innerRadius;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInnerRadius(double newInnerRadius) {
		double oldInnerRadius = innerRadius;
		innerRadius = newInnerRadius;
		boolean oldInnerRadiusESet = innerRadiusESet;
		innerRadiusESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__INNER_RADIUS, oldInnerRadius,
					innerRadius, !oldInnerRadiusESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetInnerRadius() {
		double oldInnerRadius = innerRadius;
		boolean oldInnerRadiusESet = innerRadiusESet;
		innerRadius = INNER_RADIUS_EDEFAULT;
		innerRadiusESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__INNER_RADIUS,
					oldInnerRadius, INNER_RADIUS_EDEFAULT, oldInnerRadiusESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetInnerRadius() {
		return innerRadiusESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isInnerRadiusPercent() {
		return innerRadiusPercent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInnerRadiusPercent(boolean newInnerRadiusPercent) {
		boolean oldInnerRadiusPercent = innerRadiusPercent;
		innerRadiusPercent = newInnerRadiusPercent;
		boolean oldInnerRadiusPercentESet = innerRadiusPercentESet;
		innerRadiusPercentESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PIE_SERIES__INNER_RADIUS_PERCENT,
					oldInnerRadiusPercent, innerRadiusPercent, !oldInnerRadiusPercentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetInnerRadiusPercent() {
		boolean oldInnerRadiusPercent = innerRadiusPercent;
		boolean oldInnerRadiusPercentESet = innerRadiusPercentESet;
		innerRadiusPercent = INNER_RADIUS_PERCENT_EDEFAULT;
		innerRadiusPercentESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.PIE_SERIES__INNER_RADIUS_PERCENT,
					oldInnerRadiusPercent, INNER_RADIUS_PERCENT_EDEFAULT, oldInnerRadiusPercentESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetInnerRadiusPercent() {
		return innerRadiusPercentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TypePackage.PIE_SERIES__TITLE:
			return basicSetTitle(null, msgs);
		case TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES:
			return basicSetLeaderLineAttributes(null, msgs);
		case TypePackage.PIE_SERIES__SLICE_OUTLINE:
			return basicSetSliceOutline(null, msgs);
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
		case TypePackage.PIE_SERIES__EXPLOSION:
			return getExplosion();
		case TypePackage.PIE_SERIES__EXPLOSION_EXPRESSION:
			return getExplosionExpression();
		case TypePackage.PIE_SERIES__TITLE:
			return getTitle();
		case TypePackage.PIE_SERIES__TITLE_POSITION:
			return getTitlePosition();
		case TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES:
			return getLeaderLineAttributes();
		case TypePackage.PIE_SERIES__LEADER_LINE_STYLE:
			return getLeaderLineStyle();
		case TypePackage.PIE_SERIES__LEADER_LINE_LENGTH:
			return getLeaderLineLength();
		case TypePackage.PIE_SERIES__SLICE_OUTLINE:
			return getSliceOutline();
		case TypePackage.PIE_SERIES__RATIO:
			return getRatio();
		case TypePackage.PIE_SERIES__ROTATION:
			return getRotation();
		case TypePackage.PIE_SERIES__CLOCKWISE:
			return isClockwise();
		case TypePackage.PIE_SERIES__INNER_RADIUS:
			return getInnerRadius();
		case TypePackage.PIE_SERIES__INNER_RADIUS_PERCENT:
			return isInnerRadiusPercent();
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
		case TypePackage.PIE_SERIES__EXPLOSION:
			setExplosion((Integer) newValue);
			return;
		case TypePackage.PIE_SERIES__EXPLOSION_EXPRESSION:
			setExplosionExpression((String) newValue);
			return;
		case TypePackage.PIE_SERIES__TITLE:
			setTitle((Label) newValue);
			return;
		case TypePackage.PIE_SERIES__TITLE_POSITION:
			setTitlePosition((Position) newValue);
			return;
		case TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES:
			setLeaderLineAttributes((LineAttributes) newValue);
			return;
		case TypePackage.PIE_SERIES__LEADER_LINE_STYLE:
			setLeaderLineStyle((LeaderLineStyle) newValue);
			return;
		case TypePackage.PIE_SERIES__LEADER_LINE_LENGTH:
			setLeaderLineLength((Double) newValue);
			return;
		case TypePackage.PIE_SERIES__SLICE_OUTLINE:
			setSliceOutline((ColorDefinition) newValue);
			return;
		case TypePackage.PIE_SERIES__RATIO:
			setRatio((Double) newValue);
			return;
		case TypePackage.PIE_SERIES__ROTATION:
			setRotation((Double) newValue);
			return;
		case TypePackage.PIE_SERIES__CLOCKWISE:
			setClockwise((Boolean) newValue);
			return;
		case TypePackage.PIE_SERIES__INNER_RADIUS:
			setInnerRadius((Double) newValue);
			return;
		case TypePackage.PIE_SERIES__INNER_RADIUS_PERCENT:
			setInnerRadiusPercent((Boolean) newValue);
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
		case TypePackage.PIE_SERIES__EXPLOSION:
			unsetExplosion();
			return;
		case TypePackage.PIE_SERIES__EXPLOSION_EXPRESSION:
			setExplosionExpression(EXPLOSION_EXPRESSION_EDEFAULT);
			return;
		case TypePackage.PIE_SERIES__TITLE:
			setTitle((Label) null);
			return;
		case TypePackage.PIE_SERIES__TITLE_POSITION:
			unsetTitlePosition();
			return;
		case TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES:
			setLeaderLineAttributes((LineAttributes) null);
			return;
		case TypePackage.PIE_SERIES__LEADER_LINE_STYLE:
			unsetLeaderLineStyle();
			return;
		case TypePackage.PIE_SERIES__LEADER_LINE_LENGTH:
			unsetLeaderLineLength();
			return;
		case TypePackage.PIE_SERIES__SLICE_OUTLINE:
			setSliceOutline((ColorDefinition) null);
			return;
		case TypePackage.PIE_SERIES__RATIO:
			unsetRatio();
			return;
		case TypePackage.PIE_SERIES__ROTATION:
			unsetRotation();
			return;
		case TypePackage.PIE_SERIES__CLOCKWISE:
			unsetClockwise();
			return;
		case TypePackage.PIE_SERIES__INNER_RADIUS:
			unsetInnerRadius();
			return;
		case TypePackage.PIE_SERIES__INNER_RADIUS_PERCENT:
			unsetInnerRadiusPercent();
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
		case TypePackage.PIE_SERIES__EXPLOSION:
			return isSetExplosion();
		case TypePackage.PIE_SERIES__EXPLOSION_EXPRESSION:
			return EXPLOSION_EXPRESSION_EDEFAULT == null ? explosionExpression != null
					: !EXPLOSION_EXPRESSION_EDEFAULT.equals(explosionExpression);
		case TypePackage.PIE_SERIES__TITLE:
			return title != null;
		case TypePackage.PIE_SERIES__TITLE_POSITION:
			return isSetTitlePosition();
		case TypePackage.PIE_SERIES__LEADER_LINE_ATTRIBUTES:
			return leaderLineAttributes != null;
		case TypePackage.PIE_SERIES__LEADER_LINE_STYLE:
			return isSetLeaderLineStyle();
		case TypePackage.PIE_SERIES__LEADER_LINE_LENGTH:
			return isSetLeaderLineLength();
		case TypePackage.PIE_SERIES__SLICE_OUTLINE:
			return sliceOutline != null;
		case TypePackage.PIE_SERIES__RATIO:
			return isSetRatio();
		case TypePackage.PIE_SERIES__ROTATION:
			return isSetRotation();
		case TypePackage.PIE_SERIES__CLOCKWISE:
			return isSetClockwise();
		case TypePackage.PIE_SERIES__INNER_RADIUS:
			return isSetInnerRadius();
		case TypePackage.PIE_SERIES__INNER_RADIUS_PERCENT:
			return isSetInnerRadiusPercent();
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
		result.append(" (explosion: "); //$NON-NLS-1$
		if (explosionESet)
			result.append(explosion);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", explosionExpression: "); //$NON-NLS-1$
		result.append(explosionExpression);
		result.append(", titlePosition: "); //$NON-NLS-1$
		if (titlePositionESet)
			result.append(titlePosition);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", leaderLineStyle: "); //$NON-NLS-1$
		if (leaderLineStyleESet)
			result.append(leaderLineStyle);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", leaderLineLength: "); //$NON-NLS-1$
		if (leaderLineLengthESet)
			result.append(leaderLineLength);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", ratio: "); //$NON-NLS-1$
		if (ratioESet)
			result.append(ratio);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", rotation: "); //$NON-NLS-1$
		if (rotationESet)
			result.append(rotation);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", clockwise: "); //$NON-NLS-1$
		if (clockwiseESet)
			result.append(clockwise);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", innerRadius: "); //$NON-NLS-1$
		if (innerRadiusESet)
			result.append(innerRadius);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", innerRadiusPercent: "); //$NON-NLS-1$
		if (innerRadiusPercentESet)
			result.append(innerRadiusPercent);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return series instance
	 */
	public static final Series create() {
		final PieSeries se = TypeFactory.eINSTANCE.createPieSeries();
		((PieSeriesImpl) se).initialize();
		return se;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#initialize()
	 */
	protected final void initialize() {
		super.initialize();
		setExplosion(0);
		setLabelPosition(Position.OUTSIDE_LITERAL);
		setLeaderLineAttributes(LineAttributesImpl.create(null, LineStyle.SOLID_LITERAL, 1));
		setLeaderLineLength(10);
		// setLeaderLineStyle( LeaderLineStyle.STRETCH_TO_SIDE_LITERAL );
		// setSliceOutline(ColorDefinitionImpl.BLACK()); // UNDEFINED SUGGESTS
		// THAT OUTLINE IS RENDERED IN DARKER SLICE FILL COLOR
		getLabel().setVisible(true);
		final Label la = LabelImpl.create();
		la.getCaption().getFont().setSize(16);
		la.getCaption().getFont().setBold(true);
		setTitle(la);
		setTitlePosition(Position.BELOW_LITERAL);
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return series instance
	 */
	public static final Series createDefault() {
		final PieSeries se = TypeFactory.eINSTANCE.createPieSeries();
		((PieSeriesImpl) se).initDefault();
		return se;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#initialize()
	 */
	protected final void initDefault() {
		super.initDefault();
		explosion = 0;
		labelPosition = Position.OUTSIDE_LITERAL;
		setLeaderLineAttributes(LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1));
		leaderLineLength = 10;
		// leaderLineStyle = LeaderLineStyle.STRETCH_TO_SIDE_LITERAL;
		// setSliceOutline(ColorDefinitionImpl.BLACK()); // UNDEFINED SUGGESTS
		// THAT OUTLINE IS RENDERED IN DARKER SLICE FILL COLOR
		try {
			ChartElementUtil.setDefaultValue(getLabel(), "visible", true); //$NON-NLS-1$
			final Label la = LabelImpl.createDefault(true);
			ChartElementUtil.setDefaultValue(la.getCaption().getFont(), "size", 16); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(la.getCaption().getFont(), "bold", true); //$NON-NLS-1$
			setTitle(la);
		} catch (ChartException e) {
			// Do nothing.
		}
		titlePosition = Position.BELOW_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("PieSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public PieSeries copyInstance() {
		PieSeriesImpl dest = new PieSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(PieSeries src) {

		super.set(src);

		// children

		if (src.getTitle() != null) {
			setTitle(src.getTitle().copyInstance());
		}

		if (src.getLeaderLineAttributes() != null) {
			setLeaderLineAttributes(src.getLeaderLineAttributes().copyInstance());
		}

		if (src.getSliceOutline() != null) {
			setSliceOutline(src.getSliceOutline().copyInstance());
		}

		// attributes

		explosion = src.getExplosion();

		explosionESet = src.isSetExplosion();

		explosionExpression = src.getExplosionExpression();

		titlePosition = src.getTitlePosition();

		titlePositionESet = src.isSetTitlePosition();

		leaderLineStyle = src.getLeaderLineStyle();

		leaderLineStyleESet = src.isSetLeaderLineStyle();

		leaderLineLength = src.getLeaderLineLength();

		leaderLineLengthESet = src.isSetLeaderLineLength();

		ratio = src.getRatio();

		ratioESet = src.isSetRatio();

		rotation = src.getRotation();

		rotationESet = src.isSetRotation();

		clockwise = src.isClockwise();

		clockwiseESet = src.isSetClockwise();

		innerRadius = src.getInnerRadius();

		innerRadiusESet = src.isSetInnerRadius();

		innerRadiusPercent = src.isInnerRadiusPercent();

		innerRadiusPercentESet = src.isSetInnerRadiusPercent();

	}

	@Override
	public NameSet getLabelPositionScope(ChartDimension dimension) {
		return LiteralHelper.inoutPositionSet;
	}

} // PieSeriesImpl
