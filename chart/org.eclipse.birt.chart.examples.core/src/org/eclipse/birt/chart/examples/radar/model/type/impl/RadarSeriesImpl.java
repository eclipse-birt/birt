/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.examples.radar.model.type.impl;

import java.math.BigInteger;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Radar Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getMarker <em>Marker</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getLineAttributes <em>Line Attributes</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isPaletteLineColor <em>Palette Line Color</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isBackgroundOvalTransparent <em>Background Oval Transparent</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLineAttributes <em>Web Line Attributes</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isShowWebLabels <em>Show Web Labels</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelMax <em>Web Label Max</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelMin <em>Web Label Min</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelUnit <em>Web Label Unit</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isFillPolys <em>Fill Polys</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isConnectEndpoints <em>Connect Endpoints</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabel <em>Web Label</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getPlotSteps <em>Plot Steps</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RadarSeriesImpl extends SeriesImpl implements RadarSeries
{

	/**
	 * The cached value of the '{@link #getMarker() <em>Marker</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarker()
	 * @generated
	 * @ordered
	 */
	protected Marker marker;

	/**
	 * The cached value of the '{@link #getLineAttributes() <em>Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes lineAttributes;

	/**
	 * The default value of the '{@link #isPaletteLineColor() <em>Palette Line Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPaletteLineColor()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PALETTE_LINE_COLOR_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPaletteLineColor() <em>Palette Line Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPaletteLineColor()
	 * @generated
	 * @ordered
	 */
	protected boolean paletteLineColor = PALETTE_LINE_COLOR_EDEFAULT;

	/**
	 * This is true if the Palette Line Color attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean paletteLineColorESet;

	/**
	 * The default value of the '{@link #isBackgroundOvalTransparent() <em>Background Oval Transparent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBackgroundOvalTransparent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BACKGROUND_OVAL_TRANSPARENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBackgroundOvalTransparent() <em>Background Oval Transparent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBackgroundOvalTransparent()
	 * @generated
	 * @ordered
	 */
	protected boolean backgroundOvalTransparent = BACKGROUND_OVAL_TRANSPARENT_EDEFAULT;

	/**
	 * This is true if the Background Oval Transparent attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean backgroundOvalTransparentESet;

	/**
	 * The cached value of the '{@link #getWebLineAttributes() <em>Web Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes webLineAttributes;

	/**
	 * The default value of the '{@link #isShowWebLabels() <em>Show Web Labels</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowWebLabels()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_WEB_LABELS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowWebLabels() <em>Show Web Labels</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowWebLabels()
	 * @generated
	 * @ordered
	 */
	protected boolean showWebLabels = SHOW_WEB_LABELS_EDEFAULT;

	/**
	 * This is true if the Show Web Labels attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean showWebLabelsESet;

	/**
	 * The default value of the '{@link #getWebLabelMax() <em>Web Label Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabelMax()
	 * @generated
	 * @ordered
	 */
	protected static final double WEB_LABEL_MAX_EDEFAULT = 100.0;

	/**
	 * The cached value of the '{@link #getWebLabelMax() <em>Web Label Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabelMax()
	 * @generated
	 * @ordered
	 */
	protected double webLabelMax = WEB_LABEL_MAX_EDEFAULT;

	/**
	 * This is true if the Web Label Max attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean webLabelMaxESet;

	/**
	 * The default value of the '{@link #getWebLabelMin() <em>Web Label Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabelMin()
	 * @generated
	 * @ordered
	 */
	protected static final double WEB_LABEL_MIN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWebLabelMin() <em>Web Label Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabelMin()
	 * @generated
	 * @ordered
	 */
	protected double webLabelMin = WEB_LABEL_MIN_EDEFAULT;

	/**
	 * This is true if the Web Label Min attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean webLabelMinESet;

	/**
	 * The default value of the '{@link #getWebLabelUnit() <em>Web Label Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabelUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String WEB_LABEL_UNIT_EDEFAULT = "%";

	/**
	 * The cached value of the '{@link #getWebLabelUnit() <em>Web Label Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabelUnit()
	 * @generated
	 * @ordered
	 */
	protected String webLabelUnit = WEB_LABEL_UNIT_EDEFAULT;

	/**
	 * This is true if the Web Label Unit attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean webLabelUnitESet;

	/**
	 * The default value of the '{@link #isFillPolys() <em>Fill Polys</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFillPolys()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FILL_POLYS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFillPolys() <em>Fill Polys</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFillPolys()
	 * @generated
	 * @ordered
	 */
	protected boolean fillPolys = FILL_POLYS_EDEFAULT;

	/**
	 * This is true if the Fill Polys attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fillPolysESet;

	/**
	 * The default value of the '{@link #isConnectEndpoints() <em>Connect Endpoints</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConnectEndpoints()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONNECT_ENDPOINTS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isConnectEndpoints() <em>Connect Endpoints</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConnectEndpoints()
	 * @generated
	 * @ordered
	 */
	protected boolean connectEndpoints = CONNECT_ENDPOINTS_EDEFAULT;

	/**
	 * This is true if the Connect Endpoints attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean connectEndpointsESet;

	/**
	 * The cached value of the '{@link #getWebLabel() <em>Web Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebLabel()
	 * @generated
	 * @ordered
	 */
	protected Label webLabel;

	/**
	 * The default value of the '{@link #getPlotSteps() <em>Plot Steps</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlotSteps()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger PLOT_STEPS_EDEFAULT = new BigInteger( "5" );

	/**
	 * The cached value of the '{@link #getPlotSteps() <em>Plot Steps</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlotSteps()
	 * @generated
	 * @ordered
	 */
	protected BigInteger plotSteps = PLOT_STEPS_EDEFAULT;

	/**
	 * This is true if the Plot Steps attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean plotStepsESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RadarSeriesImpl( )
	{
		super( );
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return radar series instance
	 */
	public static final RadarSeries create( )
	{
		final RadarSeries se = org.eclipse.birt.chart.examples.radar.model.type.RadarTypeFactory.eINSTANCE.createRadarSeries( );
		( (RadarSeriesImpl) se ).initialize( );
		return se;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initialize( )
	{
		super.initialize( );

		final LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes( );
		( (LineAttributesImpl) lia ).set( null,
				LineStyle.SOLID_LITERAL,
				1 );
		lia.setVisible( true );
		setLineAttributes( lia );

		final LineAttributes weblia = AttributeFactory.eINSTANCE.createLineAttributes( );
		( (LineAttributesImpl) weblia ).set( null,
				LineStyle.SOLID_LITERAL,
				1 );
		weblia.setVisible( true );
		setWebLineAttributes( weblia );

		final Marker m = AttributeFactory.eINSTANCE.createMarker( );
		m.setType( MarkerType.BOX_LITERAL );
		m.setSize( 4 );
		m.setVisible( true );
		LineAttributes la = AttributeFactory.eINSTANCE.createLineAttributes( );
		la.setVisible( true );
		m.setOutline( la );
		setMarker( m );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return RadarTypePackage.Literals.RADAR_SERIES;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Marker getMarker( )
	{
		return marker;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMarker( Marker newMarker,
			NotificationChain msgs )
	{
		Marker oldMarker = marker;
		marker = newMarker;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__MARKER,
					oldMarker,
					newMarker );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarker( Marker newMarker )
	{
		if ( newMarker != marker )
		{
			NotificationChain msgs = null;
			if ( marker != null )
				msgs = ( (InternalEObject) marker ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__MARKER,
						null,
						msgs );
			if ( newMarker != null )
				msgs = ( (InternalEObject) newMarker ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__MARKER,
						null,
						msgs );
			msgs = basicSetMarker( newMarker, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__MARKER,
					newMarker,
					newMarker ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LineAttributes getLineAttributes( )
	{
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLineAttributes(
			LineAttributes newLineAttributes, NotificationChain msgs )
	{
		LineAttributes oldLineAttributes = lineAttributes;
		lineAttributes = newLineAttributes;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES,
					oldLineAttributes,
					newLineAttributes );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLineAttributes( LineAttributes newLineAttributes )
	{
		if ( newLineAttributes != lineAttributes )
		{
			NotificationChain msgs = null;
			if ( lineAttributes != null )
				msgs = ( (InternalEObject) lineAttributes ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES,
						null,
						msgs );
			if ( newLineAttributes != null )
				msgs = ( (InternalEObject) newLineAttributes ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES,
						null,
						msgs );
			msgs = basicSetLineAttributes( newLineAttributes, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES,
					newLineAttributes,
					newLineAttributes ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isPaletteLineColor( )
	{
		return paletteLineColor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPaletteLineColor( boolean newPaletteLineColor )
	{
		boolean oldPaletteLineColor = paletteLineColor;
		paletteLineColor = newPaletteLineColor;
		boolean oldPaletteLineColorESet = paletteLineColorESet;
		paletteLineColorESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR,
					oldPaletteLineColor,
					paletteLineColor,
					!oldPaletteLineColorESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPaletteLineColor( )
	{
		boolean oldPaletteLineColor = paletteLineColor;
		boolean oldPaletteLineColorESet = paletteLineColorESet;
		paletteLineColor = PALETTE_LINE_COLOR_EDEFAULT;
		paletteLineColorESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR,
					oldPaletteLineColor,
					PALETTE_LINE_COLOR_EDEFAULT,
					oldPaletteLineColorESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPaletteLineColor( )
	{
		return paletteLineColorESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isBackgroundOvalTransparent( )
	{
		return backgroundOvalTransparent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBackgroundOvalTransparent(
			boolean newBackgroundOvalTransparent )
	{
		boolean oldBackgroundOvalTransparent = backgroundOvalTransparent;
		backgroundOvalTransparent = newBackgroundOvalTransparent;
		boolean oldBackgroundOvalTransparentESet = backgroundOvalTransparentESet;
		backgroundOvalTransparentESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT,
					oldBackgroundOvalTransparent,
					backgroundOvalTransparent,
					!oldBackgroundOvalTransparentESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBackgroundOvalTransparent( )
	{
		boolean oldBackgroundOvalTransparent = backgroundOvalTransparent;
		boolean oldBackgroundOvalTransparentESet = backgroundOvalTransparentESet;
		backgroundOvalTransparent = BACKGROUND_OVAL_TRANSPARENT_EDEFAULT;
		backgroundOvalTransparentESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT,
					oldBackgroundOvalTransparent,
					BACKGROUND_OVAL_TRANSPARENT_EDEFAULT,
					oldBackgroundOvalTransparentESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBackgroundOvalTransparent( )
	{
		return backgroundOvalTransparentESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LineAttributes getWebLineAttributes( )
	{
		return webLineAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWebLineAttributes(
			LineAttributes newWebLineAttributes, NotificationChain msgs )
	{
		LineAttributes oldWebLineAttributes = webLineAttributes;
		webLineAttributes = newWebLineAttributes;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES,
					oldWebLineAttributes,
					newWebLineAttributes );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWebLineAttributes( LineAttributes newWebLineAttributes )
	{
		if ( newWebLineAttributes != webLineAttributes )
		{
			NotificationChain msgs = null;
			if ( webLineAttributes != null )
				msgs = ( (InternalEObject) webLineAttributes ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES,
						null,
						msgs );
			if ( newWebLineAttributes != null )
				msgs = ( (InternalEObject) newWebLineAttributes ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES,
						null,
						msgs );
			msgs = basicSetWebLineAttributes( newWebLineAttributes, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES,
					newWebLineAttributes,
					newWebLineAttributes ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isShowWebLabels( )
	{
		return showWebLabels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShowWebLabels( boolean newShowWebLabels )
	{
		boolean oldShowWebLabels = showWebLabels;
		showWebLabels = newShowWebLabels;
		boolean oldShowWebLabelsESet = showWebLabelsESet;
		showWebLabelsESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS,
					oldShowWebLabels,
					showWebLabels,
					!oldShowWebLabelsESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetShowWebLabels( )
	{
		boolean oldShowWebLabels = showWebLabels;
		boolean oldShowWebLabelsESet = showWebLabelsESet;
		showWebLabels = SHOW_WEB_LABELS_EDEFAULT;
		showWebLabelsESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS,
					oldShowWebLabels,
					SHOW_WEB_LABELS_EDEFAULT,
					oldShowWebLabelsESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetShowWebLabels( )
	{
		return showWebLabelsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWebLabelMax( )
	{
		return webLabelMax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWebLabelMax( double newWebLabelMax )
	{
		double oldWebLabelMax = webLabelMax;
		webLabelMax = newWebLabelMax;
		boolean oldWebLabelMaxESet = webLabelMaxESet;
		webLabelMaxESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX,
					oldWebLabelMax,
					webLabelMax,
					!oldWebLabelMaxESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetWebLabelMax( )
	{
		double oldWebLabelMax = webLabelMax;
		boolean oldWebLabelMaxESet = webLabelMaxESet;
		webLabelMax = WEB_LABEL_MAX_EDEFAULT;
		webLabelMaxESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX,
					oldWebLabelMax,
					WEB_LABEL_MAX_EDEFAULT,
					oldWebLabelMaxESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetWebLabelMax( )
	{
		return webLabelMaxESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWebLabelMin( )
	{
		return webLabelMin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWebLabelMin( double newWebLabelMin )
	{
		double oldWebLabelMin = webLabelMin;
		webLabelMin = newWebLabelMin;
		boolean oldWebLabelMinESet = webLabelMinESet;
		webLabelMinESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN,
					oldWebLabelMin,
					webLabelMin,
					!oldWebLabelMinESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetWebLabelMin( )
	{
		double oldWebLabelMin = webLabelMin;
		boolean oldWebLabelMinESet = webLabelMinESet;
		webLabelMin = WEB_LABEL_MIN_EDEFAULT;
		webLabelMinESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN,
					oldWebLabelMin,
					WEB_LABEL_MIN_EDEFAULT,
					oldWebLabelMinESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetWebLabelMin( )
	{
		return webLabelMinESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWebLabelUnit( )
	{
		return webLabelUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWebLabelUnit( String newWebLabelUnit )
	{
		String oldWebLabelUnit = webLabelUnit;
		webLabelUnit = newWebLabelUnit;
		boolean oldWebLabelUnitESet = webLabelUnitESet;
		webLabelUnitESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT,
					oldWebLabelUnit,
					webLabelUnit,
					!oldWebLabelUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetWebLabelUnit( )
	{
		String oldWebLabelUnit = webLabelUnit;
		boolean oldWebLabelUnitESet = webLabelUnitESet;
		webLabelUnit = WEB_LABEL_UNIT_EDEFAULT;
		webLabelUnitESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT,
					oldWebLabelUnit,
					WEB_LABEL_UNIT_EDEFAULT,
					oldWebLabelUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetWebLabelUnit( )
	{
		return webLabelUnitESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFillPolys( )
	{
		return fillPolys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFillPolys( boolean newFillPolys )
	{
		boolean oldFillPolys = fillPolys;
		fillPolys = newFillPolys;
		boolean oldFillPolysESet = fillPolysESet;
		fillPolysESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__FILL_POLYS,
					oldFillPolys,
					fillPolys,
					!oldFillPolysESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFillPolys( )
	{
		boolean oldFillPolys = fillPolys;
		boolean oldFillPolysESet = fillPolysESet;
		fillPolys = FILL_POLYS_EDEFAULT;
		fillPolysESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__FILL_POLYS,
					oldFillPolys,
					FILL_POLYS_EDEFAULT,
					oldFillPolysESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFillPolys( )
	{
		return fillPolysESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isConnectEndpoints( )
	{
		return connectEndpoints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnectEndpoints( boolean newConnectEndpoints )
	{
		boolean oldConnectEndpoints = connectEndpoints;
		connectEndpoints = newConnectEndpoints;
		boolean oldConnectEndpointsESet = connectEndpointsESet;
		connectEndpointsESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS,
					oldConnectEndpoints,
					connectEndpoints,
					!oldConnectEndpointsESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetConnectEndpoints( )
	{
		boolean oldConnectEndpoints = connectEndpoints;
		boolean oldConnectEndpointsESet = connectEndpointsESet;
		connectEndpoints = CONNECT_ENDPOINTS_EDEFAULT;
		connectEndpointsESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS,
					oldConnectEndpoints,
					CONNECT_ENDPOINTS_EDEFAULT,
					oldConnectEndpointsESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetConnectEndpoints( )
	{
		return connectEndpointsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Label getWebLabel( )
	{
		return webLabel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWebLabel( Label newWebLabel,
			NotificationChain msgs )
	{
		Label oldWebLabel = webLabel;
		webLabel = newWebLabel;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL,
					oldWebLabel,
					newWebLabel );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWebLabel( Label newWebLabel )
	{
		if ( newWebLabel != webLabel )
		{
			NotificationChain msgs = null;
			if ( webLabel != null )
				msgs = ( (InternalEObject) webLabel ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__WEB_LABEL,
						null,
						msgs );
			if ( newWebLabel != null )
				msgs = ( (InternalEObject) newWebLabel ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- RadarTypePackage.RADAR_SERIES__WEB_LABEL,
						null,
						msgs );
			msgs = basicSetWebLabel( newWebLabel, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL,
					newWebLabel,
					newWebLabel ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getPlotSteps( )
	{
		return plotSteps;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPlotSteps( BigInteger newPlotSteps )
	{
		BigInteger oldPlotSteps = plotSteps;
		plotSteps = newPlotSteps;
		boolean oldPlotStepsESet = plotStepsESet;
		plotStepsESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					RadarTypePackage.RADAR_SERIES__PLOT_STEPS,
					oldPlotSteps,
					plotSteps,
					!oldPlotStepsESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPlotSteps( )
	{
		BigInteger oldPlotSteps = plotSteps;
		boolean oldPlotStepsESet = plotStepsESet;
		plotSteps = PLOT_STEPS_EDEFAULT;
		plotStepsESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__PLOT_STEPS,
					oldPlotSteps,
					PLOT_STEPS_EDEFAULT,
					oldPlotStepsESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPlotSteps( )
	{
		return plotStepsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case RadarTypePackage.RADAR_SERIES__MARKER :
				return basicSetMarker( null, msgs );
			case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES :
				return basicSetLineAttributes( null, msgs );
			case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES :
				return basicSetWebLineAttributes( null, msgs );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL :
				return basicSetWebLabel( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case RadarTypePackage.RADAR_SERIES__MARKER :
				return getMarker( );
			case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES :
				return getLineAttributes( );
			case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR :
				return isPaletteLineColor( );
			case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT :
				return isBackgroundOvalTransparent( );
			case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES :
				return getWebLineAttributes( );
			case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS :
				return isShowWebLabels( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX :
				return getWebLabelMax( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN :
				return getWebLabelMin( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT :
				return getWebLabelUnit( );
			case RadarTypePackage.RADAR_SERIES__FILL_POLYS :
				return isFillPolys( );
			case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS :
				return isConnectEndpoints( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL :
				return getWebLabel( );
			case RadarTypePackage.RADAR_SERIES__PLOT_STEPS :
				return getPlotSteps( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case RadarTypePackage.RADAR_SERIES__MARKER :
				setMarker( (Marker) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES :
				setLineAttributes( (LineAttributes) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR :
				setPaletteLineColor( (Boolean) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT :
				setBackgroundOvalTransparent( (Boolean) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES :
				setWebLineAttributes( (LineAttributes) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS :
				setShowWebLabels( (Boolean) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX :
				setWebLabelMax( (Double) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN :
				setWebLabelMin( (Double) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT :
				setWebLabelUnit( (String) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__FILL_POLYS :
				setFillPolys( (Boolean) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS :
				setConnectEndpoints( (Boolean) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL :
				setWebLabel( (Label) newValue );
				return;
			case RadarTypePackage.RADAR_SERIES__PLOT_STEPS :
				setPlotSteps( (BigInteger) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case RadarTypePackage.RADAR_SERIES__MARKER :
				setMarker( (Marker) null );
				return;
			case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES :
				setLineAttributes( (LineAttributes) null );
				return;
			case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR :
				unsetPaletteLineColor( );
				return;
			case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT :
				unsetBackgroundOvalTransparent( );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES :
				setWebLineAttributes( (LineAttributes) null );
				return;
			case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS :
				unsetShowWebLabels( );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX :
				unsetWebLabelMax( );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN :
				unsetWebLabelMin( );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT :
				unsetWebLabelUnit( );
				return;
			case RadarTypePackage.RADAR_SERIES__FILL_POLYS :
				unsetFillPolys( );
				return;
			case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS :
				unsetConnectEndpoints( );
				return;
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL :
				setWebLabel( (Label) null );
				return;
			case RadarTypePackage.RADAR_SERIES__PLOT_STEPS :
				unsetPlotSteps( );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case RadarTypePackage.RADAR_SERIES__MARKER :
				return marker != null;
			case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES :
				return lineAttributes != null;
			case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR :
				return isSetPaletteLineColor( );
			case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT :
				return isSetBackgroundOvalTransparent( );
			case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES :
				return webLineAttributes != null;
			case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS :
				return isSetShowWebLabels( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX :
				return isSetWebLabelMax( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN :
				return isSetWebLabelMin( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT :
				return isSetWebLabelUnit( );
			case RadarTypePackage.RADAR_SERIES__FILL_POLYS :
				return isSetFillPolys( );
			case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS :
				return isSetConnectEndpoints( );
			case RadarTypePackage.RADAR_SERIES__WEB_LABEL :
				return webLabel != null;
			case RadarTypePackage.RADAR_SERIES__PLOT_STEPS :
				return isSetPlotSteps( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (paletteLineColor: " );
		if ( paletteLineColorESet )
			result.append( paletteLineColor );
		else
			result.append( "<unset>" );
		result.append( ", backgroundOvalTransparent: " );
		if ( backgroundOvalTransparentESet )
			result.append( backgroundOvalTransparent );
		else
			result.append( "<unset>" );
		result.append( ", showWebLabels: " );
		if ( showWebLabelsESet )
			result.append( showWebLabels );
		else
			result.append( "<unset>" );
		result.append( ", webLabelMax: " );
		if ( webLabelMaxESet )
			result.append( webLabelMax );
		else
			result.append( "<unset>" );
		result.append( ", webLabelMin: " );
		if ( webLabelMinESet )
			result.append( webLabelMin );
		else
			result.append( "<unset>" );
		result.append( ", webLabelUnit: " );
		if ( webLabelUnitESet )
			result.append( webLabelUnit );
		else
			result.append( "<unset>" );
		result.append( ", fillPolys: " );
		if ( fillPolysESet )
			result.append( fillPolys );
		else
			result.append( "<unset>" );
		result.append( ", connectEndpoints: " );
		if ( connectEndpointsESet )
			result.append( connectEndpoints );
		else
			result.append( "<unset>" );
		result.append( ", plotSteps: " );
		if ( plotStepsESet )
			result.append( plotSteps );
		else
			result.append( "<unset>" );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * @generated
	 */
	protected void set( RadarSeries src )
	{

		super.set( src );

		// children

		if ( src.getMarker( ) != null )
		{
			setMarker( src.getMarker( ).copyInstance( ) );
		}

		if ( src.getLineAttributes( ) != null )
		{
			setLineAttributes( src.getLineAttributes( ).copyInstance( ) );
		}

		if ( src.getWebLineAttributes( ) != null )
		{
			setWebLineAttributes( src.getWebLineAttributes( ).copyInstance( ) );
		}

		if ( src.getWebLabel( ) != null )
		{
			setWebLabel( src.getWebLabel( ).copyInstance( ) );
		}

		// attributes

		paletteLineColor = src.isPaletteLineColor( );

		paletteLineColorESet = src.isSetPaletteLineColor( );

		backgroundOvalTransparent = src.isBackgroundOvalTransparent( );

		backgroundOvalTransparentESet = src.isSetBackgroundOvalTransparent( );

		showWebLabels = src.isShowWebLabels( );

		showWebLabelsESet = src.isSetShowWebLabels( );

		webLabelMax = src.getWebLabelMax( );

		webLabelMaxESet = src.isSetWebLabelMax( );

		webLabelMin = src.getWebLabelMin( );

		webLabelMinESet = src.isSetWebLabelMin( );

		webLabelUnit = src.getWebLabelUnit( );

		webLabelUnitESet = src.isSetWebLabelUnit( );

		fillPolys = src.isFillPolys( );

		fillPolysESet = src.isSetFillPolys( );

		connectEndpoints = src.isConnectEndpoints( );

		connectEndpointsESet = src.isSetConnectEndpoints( );

		plotSteps = src.getPlotSteps( );

		plotStepsESet = src.isSetPlotSteps( );

	}

	/**
	 * @generated
	 */
	public RadarSeries copyInstance( )
	{
		RadarSeriesImpl dest = new RadarSeriesImpl( );
		dest.set( this );
		return dest;
	}
	
	@Override
	public String getDisplayName( )
	{
		return Messages.getString( "RadarSeriesImpl.displayName" ); //$NON-NLS-1$
	}
	
	@Override
	public NameSet getLabelPositionScope( ChartDimension dimension )
	{
		return LiteralHelper.outPositionSet;
	}

} //RadarSeriesImpl
