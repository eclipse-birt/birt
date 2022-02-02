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

package org.eclipse.birt.chart.device.swing;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.device.ICallBackNotifier;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.util.DeviceUtil;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.render.InteractiveRenderer;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.ULocale;

/**
 * Provides a reference implementation into handling events generated on a SWING
 * JComponent with a rendered chart.
 */
public final class SwingEventHandler implements MouseListener, MouseMotionListener, KeyListener, FocusListener {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/swing"); //$NON-NLS-1$

	private Action acTooltip = null;

	// private ShapedAction saHighlighted = null;

	private final Map<TriggerCondition, List<ShapedAction>> lhmAllTriggers;

	private final IUpdateNotifier iun;

	private final ULocale lcl;

	private InteractiveRenderer iv = null;

	private StructureSource srcHighlight;

	private StructureSource srcToggleDataPoint;

	private StructureSource srcToggleVisibility;

	private JPopupMenu popupMenu;

	/**
	 * The constructor.
	 * 
	 * @param _lhmAllTriggers
	 * @param _jc
	 * @param _lcl
	 */
	SwingEventHandler(InteractiveRenderer iv, Map<TriggerCondition, List<ShapedAction>> _lhmAllTriggers,
			IUpdateNotifier _jc, ULocale _lcl) {
		lhmAllTriggers = _lhmAllTriggers;
		iun = _jc;
		lcl = _lcl;
		this.iv = iv;
	}

	private final boolean isLeftButton(MouseEvent e) {
		return (e.getButton() == MouseEvent.BUTTON1);
	}

	private final ShapedAction getShapedActionForConditionPoint(TriggerCondition[] tca, Point p) {
		if (tca == null || tca.length == 0) {
			return null;
		}
		for (int i = 0; i < tca.length; i++) {
			List<ShapedAction> tal = lhmAllTriggers.get(tca[i]);

			if (tal != null) {
				// iterate backwards to get the latest painted shape
				for (int j = tal.size() - 1; j >= 0; j--) {
					ShapedAction sa = tal.get(j);
					if (p == null || sa.getShape().contains(p)) {
						return sa;
					}
				}

			}
		}

		return null;
	}

	private void handleAction(TriggerCondition[] tg, ComponentEvent event) {
		handleAction(tg, event, true);
	}

	private synchronized void handleAction(TriggerCondition[] tg, ComponentEvent event, boolean cleanState) {
		if (tg == null || event == null) {
			return;
		}
		Point p = null;

		if (event instanceof MouseEvent) {
			p = ((MouseEvent) event).getPoint();
		}

		if (event instanceof KeyEvent) {
			// TODO filter key ?
		}

		ShapedAction sa = getShapedActionForConditionPoint(tg, p);
		if (sa == null) {
			// If the event matches a trigger, disable its previous action.
			for (int i = 0; i < tg.length; i++) {
				if (lhmAllTriggers.get(tg[i]) != null)
					disableActions(getActionTypesForConditions(tg));
			}
			return;
		}

		final StructureSource src = sa.getSource();
		Action ac = null;
		for (int i = 0; i < tg.length; i++) {
			ac = sa.getActionForCondition(tg[i]);
			if (ac != null)
				break;
		}
		if (ac == null) {

			return;
		}

		switch (ac.getType().getValue()) {
		case ActionType.URL_REDIRECT:
			ActionValue av = ac.getValue();
			if (av instanceof URLValue) {
				final URLValue uv = (URLValue) ac.getValue();
				openURL(uv);
			} else if (av instanceof MultiURLValues) {
				MultiURLValues muv = (MultiURLValues) av;
				int size = muv.getURLValues().size();
				if (size == 0) {
					break;
				} else if (size == 1) {
					openURL(muv.getURLValues().get(0));
				} else {
					// Create a popup menu and show it for multiple hyperlinks.
					openMultiULRs(muv, p);
				}
			}

			break;

		case ActionType.SHOW_TOOLTIP:

			if (ac != acTooltip) {
				hideTooltip();
			}

			acTooltip = ac;
			showTooltip(ac);
			break;

		case ActionType.TOGGLE_VISIBILITY:

			srcToggleVisibility = handleGraphicAction(src, srcToggleVisibility, ActionType.TOGGLE_VISIBILITY_LITERAL,
					cleanState);

			break;

		case ActionType.TOGGLE_DATA_POINT_VISIBILITY:

			srcToggleDataPoint = handleGraphicAction(src, srcToggleDataPoint,
					ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL, cleanState);

			break;

		case ActionType.HIGHLIGHT:

			srcHighlight = handleGraphicAction(src, srcHighlight, ActionType.HIGHLIGHT_LITERAL, cleanState);

			break;

		case ActionType.CALL_BACK:
			if (iun instanceof ICallBackNotifier) {
				final CallBackValue cv = (CallBackValue) ac.getValue();
				((ICallBackNotifier) iun).callback(event, sa.getSource(), cv);
			} else {
				logger.log(ILogger.WARNING, Messages.getString("SwingEventHandler.info.improper.callback.notifier", //$NON-NLS-1$
						new Object[] { iun }, lcl));
			}
			break;
		}

	}
// if the event was fired without new action disable the previous action

	/**
	 * URLItemModel
	 */
	static class URLMenuItemModel extends DefaultButtonModel {

		private static final long serialVersionUID = 1L;
		private URLValue fUrlValue;

		void setURLValue(URLValue uv) {
			fUrlValue = uv;
		}

		URLValue getURLValue() {
			return fUrlValue;
		}
	}

	/**
	 * @param muv
	 * @param point
	 */
	private void openMultiULRs(MultiURLValues muv, Point point) {
		if (popupMenu != null && popupMenu.isValid()) {
			// Remove previous menu object.
			popupMenu.setVisible(false);
			((JComponent) iun.peerInstance()).remove(popupMenu);
		}

		popupMenu = new JPopupMenu();

		// Create popup menu items.
		for (URLValue uv : muv.getURLValues()) {
			JMenuItem menuItem = new JMenuItem();
			popupMenu.add(menuItem);
			menuItem.setText(uv.getLabel().getCaption().getValue());
			if (uv.getTooltip() != null && uv.getTooltip().length() > 0)
				menuItem.setToolTipText(uv.getTooltip());
			URLMenuItemModel uim = new URLMenuItemModel();
			uim.setURLValue(uv);
			menuItem.setModel(uim);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					URLValue urlValue = ((URLMenuItemModel) ((JMenuItem) e.getSource()).getModel()).getURLValue();
					openURL(urlValue);
				}
			});

		}

		// Show menu.
		popupMenu.show((JComponent) iun.peerInstance(), point.x, point.y);
	}

	/**
	 * @param uv
	 */
	private void openURL(final URLValue uv) {
		logger.log(ILogger.INFORMATION, Messages.getString("SwingEventHandler.info.redirect.url", lcl) //$NON-NLS-1$
				+ uv.getBaseUrl());
		DeviceUtil.openURL(uv.getBaseUrl());
	}

	private void disableActions(Set<ActionType> actions) {
		if (actions == null)
			return;

		for (Iterator<ActionType> iter = actions.iterator(); iter.hasNext();) {
			ActionType action = iter.next();
			if (action == null)
				continue;
			switch (action.getValue()) {
			case ActionType.SHOW_TOOLTIP:
				if (acTooltip != null) {
					hideTooltip();
					acTooltip = null;
				}
				break;
			case ActionType.HIGHLIGHT:
				if (srcHighlight != null) {
					iv.unregisterAction(srcHighlight, ActionType.HIGHLIGHT_LITERAL);
					srcHighlight = null;
					iun.repaintChart();
				}
				break;
			case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
				if (srcToggleDataPoint != null) {
					iv.unregisterAction(srcToggleDataPoint, ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL);
					srcToggleDataPoint = null;
					iun.repaintChart();
				}
				break;
			case ActionType.TOGGLE_VISIBILITY:
				if (srcToggleVisibility != null) {
					iv.unregisterAction(srcToggleVisibility, ActionType.TOGGLE_VISIBILITY_LITERAL);
					srcToggleVisibility = null;
					iun.repaintChart();
				}
				break;
			}
		}
	}

	private StructureSource handleGraphicAction(StructureSource src, StructureSource previousSrc, ActionType actionType,
			boolean cleanState) {
		if (previousSrc == null) {
			previousSrc = src;
			iv.registerAction(src, actionType);
			iun.repaintChart();
		} else if (!iv.getSource(src).equals(iv.getSource(previousSrc))) {
			if (actionType == ActionType.HIGHLIGHT_LITERAL) {
				// unhighlight previous region
				iv.unregisterAction(previousSrc, actionType);
				previousSrc = src;
				iv.registerAction(src, actionType);
			} else {
				previousSrc = src;
				// Register action only once.
				if (iv.isRegisteredAction(src, actionType)) {
					iv.unregisterAction(src, actionType);
				} else {
					iv.registerAction(src, actionType);
				}
			}
			iun.repaintChart();
		} else {
			if (cleanState) {
				iv.unregisterAction(previousSrc, actionType);
				previousSrc = null;
				iun.repaintChart();
			}
		}
		return previousSrc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (isLeftButton(e)) {
			handleAction(
					new TriggerCondition[] { TriggerCondition.MOUSE_CLICK_LITERAL, TriggerCondition.ONCLICK_LITERAL },
					e);
		} else {
			handleAction(new TriggerCondition[] { TriggerCondition.ONRIGHTCLICK_LITERAL }, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// FILTER OUT ALL TRIGGERS FOR MOUSE OUT ONLY
		handleAction(new TriggerCondition[] { TriggerCondition.ONMOUSEOUT_LITERAL }, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (!isLeftButton(e)) {
			return;
		}

		handleAction(new TriggerCondition[] { TriggerCondition.ONMOUSEDOWN_LITERAL }, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (!isLeftButton(e)) {
			return;
		}

		handleAction(new TriggerCondition[] { TriggerCondition.ONMOUSEUP_LITERAL }, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		final Point p = e.getPoint();

		// 1. CHECK FOR MOUSE-CLICK TRIGGERS
		ShapedAction sa = getShapedActionForConditionPoint(
				new TriggerCondition[] { TriggerCondition.MOUSE_CLICK_LITERAL, TriggerCondition.ONCLICK_LITERAL,
						TriggerCondition.ONMOUSEDOWN_LITERAL, TriggerCondition.ONMOUSEOVER_LITERAL,
						TriggerCondition.ONMOUSEMOVE_LITERAL },
				p);

		if (sa != null) {
			setCursor((JComponent) iun.peerInstance(), sa.getCursor(), Cursor.getDefaultCursor());
		} else {
			setCursor((JComponent) iun.peerInstance(), null, Cursor.getDefaultCursor());
		}

		// 2. CHECK FOR MOUSE-HOVER CONDITION

		handleAction(new TriggerCondition[] { TriggerCondition.MOUSE_HOVER_LITERAL,
				TriggerCondition.ONMOUSEMOVE_LITERAL, TriggerCondition.ONMOUSEOVER_LITERAL }, e, false);
	}

	public void focusGained(FocusEvent e) {
		handleAction(new TriggerCondition[] { TriggerCondition.ONFOCUS_LITERAL }, e);
	}

	public void focusLost(FocusEvent e) {
		handleAction(new TriggerCondition[] { TriggerCondition.ONBLUR_LITERAL }, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		handleAction(new TriggerCondition[] { TriggerCondition.ONKEYDOWN_LITERAL }, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		handleAction(new TriggerCondition[] { TriggerCondition.ONKEYUP_LITERAL, TriggerCondition.ONKEYPRESS_LITERAL },
				e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}

	private final void hideTooltip() {
		((JComponent) iun.peerInstance()).setToolTipText(null);
	}

	private final void showTooltip(Action ac) {
		TooltipValue tv = (TooltipValue) ac.getValue();

		if (tv.getText() == null) {
			return;
		}
		// Handle character conversion of \n in the tooltip
		String tooltip = tv.getText().replaceAll("\\\n", "<br>"); //$NON-NLS-1$//$NON-NLS-2$
		if (!tooltip.equals(tv.getText())) {
			tooltip = "<html>" + tooltip + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			tooltip = tv.getText();
		}
		((JComponent) iun.peerInstance()).setToolTipText(tooltip);
	}

	protected Set<ActionType> getActionTypesForConditions(TriggerCondition[] tca) {
		if (tca == null || tca.length == 0) {
			return null;
		}
		Set<ActionType> set = new HashSet<ActionType>();

		for (int i = 0; i < tca.length; i++) {
			List<ShapedAction> tal = lhmAllTriggers.get(tca[i]);
			if (tal == null)
				continue;
			for (Iterator<ShapedAction> iter = tal.iterator(); iter.hasNext();) {
				ShapedAction sa = iter.next();
				ActionType actionType = sa.getActionForCondition(tca[i]).getType();
				set.add(actionType);
			}
		}
		return set;

	}

	private void setCursor(JComponent composite, org.eclipse.birt.chart.model.attribute.Cursor cursor,
			Cursor defaultCursor) {
		if (cursor == null || cursor.getType() == CursorType.AUTO) {
			composite.setCursor(defaultCursor);
			return;
		} else if (cursor.getType() == CursorType.CUSTOM) {
			// Find the first valid image as custom cursor.
			EList<org.eclipse.birt.chart.model.attribute.Image> uris = cursor.getImage();
			for (org.eclipse.birt.chart.model.attribute.Image uri : uris) {
				try {
					Image image = null;
					if (uri instanceof EmbeddedImage) {
						try {
							byte[] data = Base64.decodeBase64(((EmbeddedImage) uri).getData().getBytes());

							image = new ImageIcon(data).getImage();
						} catch (Exception ilex) {
							logger.log(ilex);
						}
					} else {
						URI u = new URI(uri.getURL());
						image = composite.getToolkit().createImage(SecurityUtil.toURL(u));
					}

					if (image != null) {
						composite.setCursor(composite.getToolkit().createCustomCursor(image, new Point(0, 0), ""));//$NON-NLS-1$
						return;
					}
				} catch (URISyntaxException e) {
					// Do not process exception here.
				} catch (MalformedURLException e) {
					// Do not process exception here.
				}
			}

			// No valid image is found, set default cursor.
			composite.setCursor(defaultCursor);
			return;
		}

		composite.setCursor(Cursor.getPredefinedCursor(SwingHelper.CURSOR_MAP.get(cursor.getType()).intValue()));
	}
}
