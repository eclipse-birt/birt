/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.device.swing;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.g2d.G2dRendererBase;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.render.InteractiveRenderer;
import org.eclipse.birt.chart.util.PluginSettings;

/**
 * Provides a reference implementation of a SWING device renderer. It translates
 * chart primitives into standard J2SDK AWT/SWING rendering primitives.
 */
public class SwingRendererImpl extends G2dRendererBase {

	/**
	 * KEY = TRIGGER_CONDITION VAL = COLLECTION OF SHAPE-ACTION INSTANCES
	 */
	private final Map<TriggerCondition, List<ShapedAction>> _lhmAllTriggers = new HashMap<>();
	/**
	 * key = ShapeAction, val = collection of trigger conditions
	 */
	private final List<ShapedAction> _allShapes = new LinkedList<>();

	private IUpdateNotifier _iun = null;

	private SwingEventHandler _eh = null;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/swing"); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public SwingRendererImpl() {
		init();
	}

	protected void init() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			_ids = ps.getDisplayServer("ds.SWING"); //$NON-NLS-1$
			_tr = new SwingTextRenderer(_ids);
			iv = new InteractiveRenderer();
		} catch (ChartException pex) {
			logger.log(pex);
		}
	}

	/**
	 * Free all allocated system resources.
	 */
	@Override
	public void dispose() {
		super.dispose();

		_lhmAllTriggers.clear();

		if (_iun != null) {
			Object obj = _iun.peerInstance();

			if (obj instanceof JComponent) {
				JComponent jc = (JComponent) obj;

				if (_eh != null) {
					jc.removeMouseListener(_eh);
					jc.removeMouseMotionListener(_eh);
					jc.removeKeyListener(_eh);
					jc.removeFocusListener(_eh);

					_eh = null;
				}
			}
		}
	}

	@Override
	public void setProperty(String sProperty, Object oValue) {
		// InteractiveRenderer(iv) is only for Swing
		if (sProperty.equals(IDeviceRenderer.UPDATE_NOTIFIER) && iv != null) {
			_iun = (IUpdateNotifier) oValue;
			iv.setUpdateNotifier(_iun);
			_lhmAllTriggers.clear();
			Object obj = _iun.peerInstance();

			if (obj instanceof JComponent) {
				JComponent jc = (JComponent) obj;

				if (_eh != null) {
					// We can't promise to remove all the old swtEventHandler
					// due to SWT limitation here, so be sure to just attach the
					// update_notifier only to one renderer.

					jc.removeMouseListener(_eh);
					jc.removeMouseMotionListener(_eh);
					jc.removeKeyListener(_eh);
					jc.removeFocusListener(_eh);
				}

				_eh = new SwingEventHandler(iv, _lhmAllTriggers, _iun, getULocale());
				jc.addMouseListener(_eh);
				jc.addMouseMotionListener(_eh);
				jc.addKeyListener(_eh);
				jc.addFocusListener(_eh);
			}
		}

		super.setProperty(sProperty, oValue);
	}

	@Override
	protected void prepareGraphicsContext() {
		super.prepareGraphicsContext();
		logger.log(ILogger.INFORMATION, Messages.getString("SwingRendererImpl.info.using.graphics.context", //$NON-NLS-1$
				new Object[] { _g2d }, getULocale()));

	}

	protected void registerTriggers(Trigger[] tga, ShapedAction sa) {
		TriggerCondition tc;
		Action ac;
		for (int i = 0; i < tga.length; i++) {
			tc = tga[i].getCondition();
			ac = tga[i].getAction();
			sa.add(tc, ac);
			List<ShapedAction> al = _lhmAllTriggers.get(tc);
			if (al == null) {
				al = new ArrayList<>(4); // UNDER NORMAL
														// CONDITIONS
				_lhmAllTriggers.put(tc, al);
			}
			al.add(sa);
		}
		this._allShapes.add(sa);
	}

	@Override
	public void enableInteraction(InteractionEvent iev) throws ChartException {
		if (_iun == null) {
			logger.log(ILogger.INFORMATION,
					Messages.getString("SwingRendererImpl.exception.missing.component.interaction", getULocale())); //$NON-NLS-1$
			return;
		}

		final Trigger[] tga = iev.getTriggers();
		if (tga == null) {
			return;
		}

		// Get the shape Action for the event
		ShapedAction sa = getShapedAction(iev);

		if (sa != null) {
			sa.setCursor(iev.getCursor());
			sa.setZOrder(iev.getZOrder());
			// Register the triggers in the shape and renderer.
			registerTriggers(tga, sa);
		}
	}

	protected ShapedAction getShapedAction(InteractionEvent iev) {
		Shape clipping = _g2d.getClip();
		final PrimitiveRenderEvent pre = iev.getHotSpot();
		if (pre instanceof PolygonRenderEvent) {
			final Location[] loa = ((PolygonRenderEvent) pre).getPoints();
			return new ShapedAction(iev.getStructureSource(), loa, clipping);
		} else if (pre instanceof RectangleRenderEvent) {
			final Bounds bo = ((RectangleRenderEvent) pre).getBounds();

			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(bo.getLeft(), bo.getTop());
			loa[1] = goFactory.createLocation(bo.getLeft(), bo.getTop() + bo.getHeight());
			loa[2] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight());
			loa[3] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop());
			return new ShapedAction(iev.getStructureSource(), loa, clipping);
		} else if (pre instanceof OvalRenderEvent) {
			final Bounds boEllipse = ((OvalRenderEvent) pre).getBounds();
			return new ShapedAction(iev.getStructureSource(), boEllipse, clipping);
		} else if (pre instanceof ArcRenderEvent) {
			final ArcRenderEvent are = (ArcRenderEvent) pre;
			final Bounds boEllipse = are.getEllipseBounds();
			double dStart = are.getStartAngle();
			double dExtent = are.getAngleExtent();
			int iArcType = toG2dArcType(are.getStyle());
			return new ShapedAction(iev.getStructureSource(), boEllipse, dStart, dExtent, iArcType, clipping);
		} else if (pre instanceof AreaRenderEvent) {
			final Bounds bo = ((AreaRenderEvent) pre).getBounds();

			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(bo.getLeft(), bo.getTop());
			loa[1] = goFactory.createLocation(bo.getLeft(), bo.getTop() + bo.getHeight());
			loa[2] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight());
			loa[3] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop());
			return new ShapedAction(iev.getStructureSource(), loa, clipping);
		}
		return null;
	}

	/**
	 *
	 * @param s
	 * @param sWordToReplace
	 * @param sReplaceWith
	 * @return result
	 */
	public static String csSearchAndReplace(String s, String sWordToReplace, String sReplaceWith) {
		int i = 0;
		do {
			i = s.indexOf(sWordToReplace, i);
			if (i != -1) {
				s = s.substring(0, i) + sReplaceWith + s.substring(i + sWordToReplace.length());
				i += sReplaceWith.length();
			}
		} while (i != -1);
		return s;
	}

	/**
	 * Returns the triggers associated with current renderer.
	 *
	 * @return
	 */
	protected Map<TriggerCondition, List<ShapedAction>> getTriggers() {
		return _lhmAllTriggers;
	}

	protected List<ShapedAction> getShapeActions() {
		return _allShapes;
	}

	@Override
	public void before() throws ChartException {
		// Clean previous status.
		_lhmAllTriggers.clear();
	}

	@Override
	public void after() throws ChartException {
		// FLUSH ALL IMAGES USED IN RENDERING THE CHART CONTENT
		((SwingDisplayServer) _ids).getImageCache().flush();
	}
}
