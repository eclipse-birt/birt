/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.image.MultiActionValuesScriptGenerator;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.svg.i18n.Messages;
import org.eclipse.birt.chart.device.util.CSSHelper;
import org.eclipse.birt.chart.device.util.ScriptUtil;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AccessibilityValue;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.MenuStylesKeyType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.icu.util.ULocale;

/**
 * This is an internal class used by SVGRendererImpl to add interactivity in the
 * SVG output
 */
public class SVGInteractiveRenderer {

	private Map<Series, List<String>> labelPrimitives = SecurityUtil.newHashtable();
	private List<String> scripts = new Vector<>();
	/**
	 * Element that represents the hot spot layer
	 */
	protected Element hotspotLayer;
	private Map<Object, List<String>> componentPrimitives = SecurityUtil.newHashtable();
	private IUpdateNotifier _iun;
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.svg/trace"); //$NON-NLS-1$
	SVGGraphics2D svg_g2d;
	private IDeviceRenderer device;
	private List<CacheEvent> cacheEvents = new ArrayList<>();

	private String defaultCursor = "cursor:pointer"; //$NON-NLS-1$

	/**
	 * Indicates if onload method of data points has been added. This map is used
	 * for saving states of multiple series and data points.
	 */
	private Map<Object, Boolean> mapOnloadAdded = new HashMap<>();

	private int iFirstDataPointIndex = -1;

	public SVGInteractiveRenderer(IDeviceRenderer device) {
		this.device = device;
	}

	public void setIUpdateNotifier(IUpdateNotifier iun) {
		this._iun = iun;
	}

	public void setSVG2D(SVGGraphics2D svg2D) {
		this.svg_g2d = svg2D;
	}

	/**
	 * Groups the svg drawing instructions that represents this primitive events.
	 * Each group is assigned an id that identifies the source object of the
	 * primitive event
	 *
	 * @param pre      primitive render event
	 * @param drawText TODO
	 */
	protected void groupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		if (_iun == null) {
			logger.log(ILogger.WARNING, Messages.getString("exception.missing.component.interaction", getULocale())); //$NON-NLS-1$
			return;
		}

		// For now only group series elements
		if (pre.getSource() instanceof StructureSource) {
			StructureSource sourceObject = (StructureSource) pre.getSource();
			Series seDT = (Series) getElementFromSource(sourceObject, StructureType.SERIES);
			if (seDT != null) {
				String groupIdentifier = null;
				try {
					// Depending on legend coloring, we group differently
					if (isColoredByCategories()
							&& getElementFromSource(sourceObject, StructureType.SERIES_DATA_POINT) != null) {
						seDT = findCategorySeries(seDT);
						groupIdentifier = String.valueOf(seDT.hashCode());
						// Group by categories
						DataPointHints dph = (DataPointHints) getElementFromSource(sourceObject,
								StructureType.SERIES_DATA_POINT);
						groupIdentifier += "index"; //$NON-NLS-1$
						groupIdentifier += dph.getIndex();

						if (iFirstDataPointIndex < 0) {
							// The first index is not always 0
							iFirstDataPointIndex = dph.getIndex();
						}
					} else {
						seDT = findDesignTimeSeries(seDT);
						groupIdentifier = String.valueOf(seDT.hashCode());
					}
				} catch (ChartException e) {
					logger.log(e);
					return;
				}

				if (drawText) {
					String id = Integer.toString(pre.hashCode());
					List<String> components = labelPrimitives.get(seDT);
					if (components == null) {
						components = new ArrayList<>();
						labelPrimitives.put(seDT, components);
					}

					components.add(id);

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element outerGroup = svg_g2d.createElement("g"); //$NON-NLS-1$
					svg_g2d.pushParent(outerGroup);

					Element primGroup = svg_g2d.createElement("g"); //$NON-NLS-1$
					outerGroup.appendChild(primGroup);
					svg_g2d.pushParent(primGroup);
					primGroup.setAttribute("id", groupIdentifier + "_" + id); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute("style", "visibility:visible;"); //$NON-NLS-1$ //$NON-NLS-2$
					outerGroup.setAttribute("id", groupIdentifier + "_" + id + "_g"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					outerGroup.setAttribute("style", "visibility:visible;"); //$NON-NLS-1$ //$NON-NLS-2$
				} else
				// Non-text
				{
					String id = Integer.toString(pre.hashCode());
					List<String> components = componentPrimitives.get(seDT);
					if (components == null) {
						components = new ArrayList<>();
						componentPrimitives.put(seDT, components);
					}

					// May have to group drawing instructions that come from
					// the same primitive render events.
					String idTemp = id;
					if (components.size() > 0) {
						idTemp = id + "@" + components.size(); //$NON-NLS-1$
					}

					components.add(idTemp);

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element primGroup = svg_g2d.createElement("g"); //$NON-NLS-1$
					svg_g2d.pushParent(primGroup);
					primGroup.setAttribute("id", groupIdentifier + "_" + idTemp); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute("style", "visibility:visible;"); //$NON-NLS-1$ //$NON-NLS-2$

					svg_g2d.setDeferStrokColor(primGroup);
				}
			} else {
				Object designObject = null;
				// check to see if this is the title block
				if (getElementFromSource(sourceObject, StructureType.TITLE) != null) {
					designObject = sourceObject.getSource();
				} else if (getElementFromSource(sourceObject, StructureType.CHART_BLOCK) != null) {
					designObject = sourceObject.getSource();
				} else if (getElementFromSource(sourceObject, StructureType.PLOT) != null) {
					designObject = sourceObject.getSource();
				} else if (getElementFromSource(sourceObject, StructureType.AXIS) != null) {
					designObject = sourceObject.getSource();
				}
				if (designObject != null) {
					String groupIdentifier = String.valueOf(designObject.hashCode());
					String id = Integer.toString(pre.hashCode());
					List<String> components = componentPrimitives.get(designObject);
					if (components == null) {
						components = new ArrayList<>();
						componentPrimitives.put(designObject, components);
					}

					// May have to group drawing instructions that come from
					// the same primitive render events.
					String idTemp = id;
					if (components.size() > 0) {
						idTemp = id + "@" + components.size(); //$NON-NLS-1$
					}

					components.add(idTemp);

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element primGroup = svg_g2d.createElement("g"); //$NON-NLS-1$
					svg_g2d.pushParent(primGroup);
					primGroup.setAttribute("id", groupIdentifier + "_" + idTemp); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute("style", "visibility:visible;"); //$NON-NLS-1$ //$NON-NLS-2$
					svg_g2d.setDeferStrokColor(primGroup);
				}
			}
		}
	}

	/**
	 * UnGroups the svg drawing instructions that represents this primitive events.
	 *
	 * @param pre      primitive render event
	 * @param drawText TODO
	 */
	protected void ungroupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		if (_iun == null) {
			logger.log(ILogger.WARNING, Messages.getString("exception.missing.component.interaction", getULocale())); //$NON-NLS-1$
			return;
		}

		// For now only ungroup series elements
		if (pre.getSource() instanceof StructureSource) {
			StructureSource sourceObject = (StructureSource) pre.getSource();
			final Series series = (Series) getElementFromSource(sourceObject, StructureType.SERIES);
			if (series != null) {
				if (drawText) {
					svg_g2d.popParent();
					svg_g2d.popParent();
				} else {
					svg_g2d.setDeferStrokColor(null);
					svg_g2d.popParent();
				}
			} else // check to see if this is the title block
			if ((getElementFromSource(sourceObject, StructureType.TITLE) != null)
					|| (getElementFromSource(sourceObject, StructureType.AXIS) != null)
					|| (getElementFromSource(sourceObject, StructureType.CHART_BLOCK) != null)
					|| (getElementFromSource(sourceObject, StructureType.PLOT) != null)) {
				svg_g2d.setDeferStrokColor(null);
				svg_g2d.popParent();
			}

		}
	}

	/**
	 * Helper function that will determine if the source object is a series
	 * component of the chart.
	 *
	 * @param src StructureSource that is stored in the primitive render event.
	 * @return true if the object or its parent is a series component.
	 */
	private Object getElementFromSource(StructureSource src, StructureType type) {
		if (src instanceof WrappedStructureSource) {
			WrappedStructureSource wss = (WrappedStructureSource) src;
			while (wss != null) {
				if (wss.getType() == type) {
					return wss.getSource();
				}
				if (wss.getParent().getType() == type) {
					return wss.getParent().getSource();
				}
				if (wss.getParent() instanceof WrappedStructureSource) {
					wss = (WrappedStructureSource) wss.getParent();
				} else {
					wss = null;
				}
			}
		} else if (src.getType() == type) {
			return src.getSource();
		}
		return null;
	}

	/**
	 * Locates a category design-time series corresponding to a given cloned
	 * run-time series.
	 *
	 * @param seDT runtime Series
	 * @return category Series
	 */
	private Series findCategorySeries(Series seDT) {
		final Chart cmDT = _iun.getDesignTimeModel();
		if (cmDT instanceof ChartWithAxes) {
			return ((ChartWithAxes) cmDT).getBaseAxes()[0].getSeriesDefinitions().get(0).getRunTimeSeries().get(0);
		} else {
			return ((ChartWithoutAxes) cmDT).getSeriesDefinitions().get(0).getRunTimeSeries().get(0);
		}
	}

	/**
	 * Prepare event handling
	 */
	public void prepareInteractiveEvent(Element elm, InteractionEvent ie, Trigger[] triggers) {
		// Bug#197269: onload methods for data points should be invoked once for
		// each series
		triggers = removeAddedOnloadEvent(ie, triggers);

		// Cache events to make sure the groups are complete
		if (triggers != null && triggers.length > 0) {
			cacheEvents.add(new CacheEvent(elm, ie.getStructureSource(), triggers, ie.getCursor()));
		}
	}

	private Trigger[] removeAddedOnloadEvent(InteractionEvent ie, Trigger[] triggers) {
		int indexOnload = -1;
		boolean bDelete = false;
		if (ie.getStructureSource().getType() == StructureType.SERIES_DATA_POINT) {
			// To get the index of onload event in the array
			for (int i = 0; i < triggers.length; i++) {
				if (triggers[i].getCondition().getValue() == TriggerCondition.ONLOAD) {
					if (isColoredByCategories()
							&& (triggers[i].getAction().getType() == ActionType.TOGGLE_VISIBILITY_LITERAL || triggers[i]
									.getAction().getType() == ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL)) {
						// #195949
						// make sure onload event is still invoked for each data
						// point when color by series and toogle visibility
						Object dph = ie.getStructureSource().getSource();
						if (!mapOnloadAdded.containsKey(dph)) {
							// If the data point has not been added, onload
							// event takes effect
							mapOnloadAdded.put(dph, Boolean.TRUE);
							return triggers;
						}
						// Mark this onload event should be deleted
						bDelete = true;
					}
					indexOnload = i;
					break;
				}
			}
			if (indexOnload >= 0) {
				// To check if current series has added onload event for this
				// data point
				Object series = ((WrappedStructureSource) ie.getStructureSource()).getParent().getSource();
				if (bDelete || mapOnloadAdded.containsKey(series)) {
					// To remove the duplicate onload event from array
					if (triggers.length == 1) {
						return null;
					}
					Trigger[] newTriggers = new Trigger[triggers.length - 1];
					System.arraycopy(triggers, 0, newTriggers, 0, indexOnload);
					System.arraycopy(triggers, indexOnload + 1, newTriggers, indexOnload,
							triggers.length - indexOnload - 1);
					return newTriggers;
				}
				mapOnloadAdded.put(series, Boolean.TRUE);
			}
		}
		return triggers;
	}

	/**
	 * Process events that have been prepared and apply them to the SVG Elements
	 */
	public void addInteractivity() {
		for (Iterator<CacheEvent> iter = cacheEvents.iterator(); iter.hasNext();) {
			CacheEvent cEvent = iter.next();
			addEventHandling(cEvent.getElement(), cEvent.getSource(), cEvent.getTriggers(), cEvent.getCursor());
		}
	}

	/**
	 * Add event handling to the hotspot
	 */
	private void addEventHandling(Element elm, StructureSource src, Trigger[] triggers, Cursor cursor) {
		if (elm != null && triggers != null) {
			if (triggers != null && triggers.length > 0) {
				setCursorAttribute(elm, cursor, defaultCursor);
			}

			// now we always use an empty href element to wrap the event action
			// so the hand cursor can be shown on hot spots.
			Element aLink = svg_g2d.createElement("a"); //$NON-NLS-1$
			Element group = svg_g2d.createElement("g"); //$NON-NLS-1$
			group.appendChild(elm);
			// Create empty href
			aLink.setAttribute("xlink:href", "javascript:void(0);"); //$NON-NLS-1$ //$NON-NLS-2$
			aLink.appendChild(group);
			elm = group;
			hotspotLayer.appendChild(aLink);

			for (int x = 0; x < triggers.length; x++) {
				Trigger tg = triggers[x];

				String scriptEvent = getJsScriptEvent(tg.getCondition().getValue());
				if (scriptEvent != null) {
					// Convert double click event
					boolean bDblClick = false;
					if (scriptEvent.equals("ondblclick")) //$NON-NLS-1$
					{
						scriptEvent = "onclick";//$NON-NLS-1$
						bDblClick = true;
					}

					Action action = tg.getAction();

					if (action instanceof MultipleActions) {
						addMultiActionsJSCode(elm, src, tg, scriptEvent, bDblClick, (MultipleActions) action);

					} else {
						addActionJSCode(elm, src, tg, scriptEvent, bDblClick, action);
					}
				}
			}
		}
	}

	/**
	 * @param elm
	 * @param src
	 * @param tg
	 * @param scriptEvent
	 * @param bDblClick
	 * @param action
	 */
	private void addActionJSCode(Element elm, StructureSource src, Trigger tg, String scriptEvent, boolean bDblClick,
			Action action) {
		switch (action.getType().getValue()) {
		case ActionType.SHOW_TOOLTIP:

			addTooltipJSCode(elm, src, scriptEvent, bDblClick, action);
			break;
		case ActionType.URL_REDIRECT:
			addURLRedirectJSCode(elm, src, scriptEvent, bDblClick, action);
			break;

		case ActionType.TOGGLE_VISIBILITY:
		case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
		case ActionType.HIGHLIGHT:
			addJSCodeOnElement(src, tg, elm, scriptEvent, action.getType().getValue(), bDblClick);
			break;

		case ActionType.INVOKE_SCRIPT:

			addInvokeScriptJSCode(elm, src, tg, scriptEvent, bDblClick, action);
			break;
		}
	}

	/**
	 * @param elm
	 * @param src
	 * @param tg
	 * @param scriptEvent
	 * @param bDblClick
	 * @param action
	 */
	private void addMultiActionsJSCode(Element elm, StructureSource src, Trigger tg, String scriptEvent,
			boolean bDblClick, MultipleActions action) {
		// Still show menu if visible is true.
		boolean needMenu = action.getValue() != null && action.getValue().getLabel() != null
				&& action.getValue().getLabel().isVisible();

		List<Action> subActions = MultiActionValuesScriptGenerator.getValidActions(action);
		if (subActions.size() == 1 && !needMenu) {
			Action subAction = subActions.get(0);
			addActionJSCode(elm, src, tg, scriptEvent, bDblClick, subAction);
		} else if (subActions.size() > 1) {

			String callbackContent = getMultiActionsCallbackContent(action);
			StringBuffer callbackFunction = generateScriptCallbackFunctionName(callbackContent);
			String funcName = callbackFunction.toString();
			addCallBackScript(src, callbackFunction, false);

			boolean multipleTypes = false;
			for (Action subAction : subActions) {
				if (subAction instanceof MultipleActions) {
					continue;
				}

				// Add callback function in element.
				int typeValue = subAction.getType().getValue();

				switch (typeValue) {
				case ActionType.TOGGLE_VISIBILITY:
				case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
				case ActionType.HIGHLIGHT:
					multipleTypes = true;
					break;
				}
				if (multipleTypes) {
					break;
				}
			}
			if (multipleTypes) {
				callbackFunction.append(getJSCodeFunctionSuffix(src));
			} else {
				callbackFunction.append(")"); //$NON-NLS-1$
			}

			String jsFunction = callbackFunction.toString();
			elm.setAttribute(scriptEvent, wrapJS(bDblClick, jsFunction));

			// Add callback function in element.
			if (tg.getCondition().getValue() == TriggerCondition.ONMOUSEOVER) {
				elm.setAttribute("onmouseout", //$NON-NLS-1$
						jsFunction);
			}

			// Add script definition of callbak function.
			// function into 'script' element.
			if (!(scripts.contains(callbackContent))) {
				svg_g2d.addScript(generateCallBackMethodName(funcName) + "{" + callbackContent + "}"); //$NON-NLS-1$ //$NON-NLS-2$
				scripts.add(callbackContent);
			}
		}
	}

	/**
	 * @param elm
	 * @param src
	 * @param scriptEvent
	 * @param bDblClick
	 * @param action
	 */
	private void addTooltipJSCode(Element elm, StructureSource src, String scriptEvent, boolean bDblClick,
			Action action) {
		String tooltipText = ((TooltipValue) action.getValue()).getText();
		// make sure the tooltip text is not empty
		if ((tooltipText != null) && (tooltipText.trim().length() > 0)) {
			Element title = svg_g2d.dom.createElement("title"); //$NON-NLS-1$
			title.appendChild(svg_g2d.dom.createTextNode(tooltipText));
			elm.appendChild(title);
			// on mouse over is actually two events to
			// show
			// the tooltip
			String componentId = null;
			if (src instanceof WrappedStructureSource) {
				componentId = findFirstComponentId((WrappedStructureSource) src);
			}

			if (scriptEvent.equals("onmouseover")) {//$NON-NLS-1$
				elm.setAttribute("onmouseout", "TM.remove()"); //$NON-NLS-1$ //$NON-NLS-2$
				if (componentId != null) {
					elm.setAttribute("onmousemove", "TM.show(evt," + componentId + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} else {
					elm.setAttribute("onmousemove", "TM.show(evt)"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else if (componentId != null) {
				elm.setAttribute(scriptEvent, wrapJS(bDblClick, "TM.toggleToolTip(evt," + componentId + ")")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				elm.setAttribute(scriptEvent, wrapJS(bDblClick, "TM.toggleToolTip(evt)")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param elm
	 * @param src
	 * @param tg
	 * @param scriptEvent
	 * @param bDblClick
	 * @param action
	 */
	private void addInvokeScriptJSCode(Element elm, StructureSource src, Trigger tg, String scriptEvent,
			boolean bDblClick, Action action) {
		// lets see if we need to add accessibility
		if (tg.getCondition().equals(TriggerCondition.ACCESSIBILITY_LITERAL)) {
			AccessibilityValue accessValue = ((AccessibilityValue) action.getValue());
			if (accessValue.getText() != null) {
				Element title = svg_g2d.createElement("title"); //$NON-NLS-1$
				title.appendChild(svg_g2d.dom.createTextNode(accessValue.getText()));
				elm.appendChild(title);
			}
			if (accessValue.getAccessibility() != null) {
				Element description = svg_g2d.createElement("desc"); //$NON-NLS-1$
				description.appendChild(svg_g2d.dom.createTextNode(accessValue.getAccessibility()));
				elm.appendChild(description);
			}

		} else {
			// Add categoryData, valueData,
			// valueSeriesName
			// in callback
			String script = ((ScriptValue) action.getValue()).getScript();

			StringBuffer callbackFunction = generateScriptCallbackFunctionName(script);

			String funcName = callbackFunction.toString();
			addCallBackScript(src, callbackFunction, true);

			// Add callback function in element.
			elm.setAttribute(scriptEvent, wrapJS(bDblClick, callbackFunction.toString()));

			// Add content definition of callbak function.
			if (!(scripts.contains(script))) {
				svg_g2d.addScript(generateCallBackMethodName(funcName) + "{" + script + "}"); //$NON-NLS-1$ //$NON-NLS-2$
				scripts.add(script);
			}
		}
	}

	/**
	 * @param elm
	 * @param src
	 * @param scriptEvent
	 * @param bDblClick
	 * @param action
	 */
	private void addURLRedirectJSCode(Element elm, StructureSource src, String scriptEvent, boolean bDblClick,
			Action action) {
		ActionValue av = action.getValue();
		if (av instanceof URLValue) {
			URLValue urlValue = (URLValue) av;
			// See if this is an internal anchor link
			setURLValueAttributes(urlValue, elm, src, scriptEvent, bDblClick, null);
		} else if (av instanceof MultiURLValues) {
			MultiURLValues muv = (MultiURLValues) av;

			List<URLValue> validURLValues = MultiActionValuesScriptGenerator.getValidURLValues(muv);
			int size = validURLValues.size();
			if (size == 1) {
				// When there is only one link, set URLValue tooltip if it's
				// not null. Otherwise, set MultiUrlvalues tooltip.
				setURLValueAttributes(validURLValues.get(0), elm, src, scriptEvent, bDblClick, muv.getTooltip());
			} else if (size > 1) {
				setTooltipForURLRedirect(elm, src, muv.getTooltip());
				setMultiURLValuesAttributes(muv, elm, src, scriptEvent, bDblClick);
			}

		}
	}

	/**
	 * @param sb
	 * @param propMap
	 */
	private static StringBuilder getPropertiesJS(EMap<String, String> propMap) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : propMap.entrySet()) {
			String key = entry.getKey();
			String properties = entry.getValue();
			if (MenuStylesKeyType.MENU.getName().equals(key)) {
				sb.append("\t menuInfo.menuStyles = '" + CSSHelper.getStylingHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			} else if (MenuStylesKeyType.MENU_ITEM.getName().equals(key)) {
				sb.append("\t menuInfo.menuItemStyles = '" + CSSHelper.getStylingHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			} else if (MenuStylesKeyType.ON_MOUSE_OVER.getName().equals(key)) {
				sb.append("\t menuInfo.mouseOverStyles = '" + CSSHelper.getStylingHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			} else if (MenuStylesKeyType.ON_MOUSE_OUT.getName().equals(key)) {
				sb.append("\tmenuInfo.mouseOutStyles = '" + CSSHelper.getStylingHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return sb;
	}

	/**
	 * Returns the callback content of multiple actions.
	 *
	 * @param actions
	 * @return
	 */
	private String getMultiActionsCallbackContent(MultipleActions actions) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t var menuInfo = new BirtChartMenuInfo();\n"); //$NON-NLS-1$

		EMap<String, String> propMap = actions.getPropertiesMap();
		sb.append(getPropertiesJS(propMap).toString());
		int i = 0;

		for (Action subAction : MultiActionValuesScriptGenerator.getValidActions(actions)) {
			int typeValue = subAction.getType().getValue();
			switch (typeValue) {
			case ActionType.URL_REDIRECT:
				sb = MultiActionValuesScriptGenerator.getURLValueJS(sb, i, (URLValue) subAction.getValue(),
						SVGEncoderAdapter.getInstance());
				break;
			case ActionType.INVOKE_SCRIPT:
				sb = MultiActionValuesScriptGenerator.getScriptValueJS(sb, i, (ScriptValue) subAction.getValue(),
						getULocale());
				break;
			case ActionType.TOGGLE_VISIBILITY:
				sb = MultiActionValuesScriptGenerator.getVisualJS(sb, i, subAction.getValue(),
						"BirtChartInteractivityActions.TOGGLE_VISIBILITY"); //$NON-NLS-1$
				break;
			case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
				sb = MultiActionValuesScriptGenerator.getVisualJS(sb, i, subAction.getValue(),
						"BirtChartInteractivityActions.TOGGLE_DATA_POINT_VISIBILITY"); //$NON-NLS-1$
				break;
			case ActionType.HIGHLIGHT:
				sb = MultiActionValuesScriptGenerator.getVisualJS(sb, i, subAction.getValue(),
						"BirtChartInteractivityActions.HIGHLIGHT"); //$NON-NLS-1$
				break;
			case ActionType.SHOW_TOOLTIP:
				sb = MultiActionValuesScriptGenerator.getVisualJS(sb, i, subAction.getValue(),
						"BirtChartInteractivityActions.SHOW_TOOLTIP"); //$NON-NLS-1$
				break;
			}
			i++;
		}

		MultiActionValuesScriptGenerator.appendInteractivityVariables(sb);

		// Still show menu if visible is true.
		if (actions.getValue() != null && actions.getValue().getLabel() != null
				&& actions.getValue().getLabel().isVisible()) {
			sb.append("menuInfo.needMenu = true;"); //$NON-NLS-1$
		}

		sb.append("\n"); //$NON-NLS-1$
		sb.append("if ( menuInfo.menuItemNames.length == 1 && !menuInfo.needMenu ) {\n");//$NON-NLS-1$
		sb.append("	BirtChartActionsMenu.executeMenuActionImpl( evt, menuInfo.menuItems[0], menuInfo );\n");//$NON-NLS-1$
		sb.append("} else { \n");//$NON-NLS-1$
		sb.append("  BirtChartActionsMenu.show( evt, source, menuInfo ); "); //$NON-NLS-1$
		sb.append("}\n");//$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Set SVG attributes for multiple URL values.
	 *
	 * @param muv
	 * @param elm
	 * @param src
	 * @param scriptEvent
	 * @param bDblClick
	 */
	private void setMultiURLValuesAttributes(MultiURLValues values, Element elm, StructureSource src,
			String scriptEvent, boolean bDblClick) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t var menuInfo = new BirtChartMenuInfo();\n"); //$NON-NLS-1$

		EMap<String, String> propMap = values.getPropertiesMap();
		sb.append(getPropertiesJS(propMap).toString());
		int i = 0;
		for (URLValue uv : MultiActionValuesScriptGenerator.getValidURLValues(values)) {
			sb = MultiActionValuesScriptGenerator.getURLValueJS(sb, i, uv, SVGEncoderAdapter.getInstance());
			i++;
		}
		MultiActionValuesScriptGenerator.appendInteractivityVariables(sb);
		sb.append("\n"); //$NON-NLS-1$
		sb.append("  BirtChartActionsMenu.show( evt, source, menuInfo ); "); //$NON-NLS-1$

		String script = sb.toString();

		StringBuffer callbackFunction = generateScriptCallbackFunctionName(script);

		String funcName = callbackFunction.toString();
		addCallBackScript(src, callbackFunction, true);

		// Write JS callback function in element.
		elm.setAttribute(scriptEvent, wrapJS(bDblClick, callbackFunction.toString()));

		// Write the definition of JS callback
		// function into 'script' element.
		if (!(scripts.contains(script))) {
			svg_g2d.addScript(generateCallBackMethodName(funcName) + "{" + script + "}"); //$NON-NLS-1$ //$NON-NLS-2$
			scripts.add(script);
		}
	}

	/**
	 * @param script
	 * @return
	 */
	private StringBuffer generateScriptCallbackFunctionName(String script) {
		StringBuffer callbackFunction = new StringBuffer("callback");//$NON-NLS-1$

		int hashCode = script.hashCode();
		if (hashCode != Integer.MIN_VALUE) {
			callbackFunction.append(Math.abs(hashCode));
		} else {
			callbackFunction.append(Integer.MAX_VALUE);
		}
		return callbackFunction;
	}

	/**
	 * Set SVG attributes for URL value.
	 *
	 * @param urlValue
	 * @param elm
	 * @param src
	 * @param scriptEvent
	 * @param bDblClick
	 * @param tooltip
	 */
	private void setURLValueAttributes(URLValue urlValue, Element elm, StructureSource src, String scriptEvent,
			boolean bDblClick, String tooltip) {
		if (urlValue != null && urlValue.getTooltip() != null) {
			setTooltipForURLRedirect(elm, src, urlValue);
		} else {
			setTooltipForURLRedirect(elm, src, tooltip);
		}

		String url = ""; //$NON-NLS-1$
		if (urlValue.getBaseUrl().startsWith("#")) { //$NON-NLS-1$
			url = "top.document.location.hash='" //$NON-NLS-1$
					+ urlValue.getBaseUrl() + "';";//$NON-NLS-1$
		}
		// check if this is a javascript call
		else if (urlValue.getBaseUrl().startsWith("javascript:")) //$NON-NLS-1$
		{
			url = urlValue.getBaseUrl();
		} else {
			String target = urlValue.getTarget();
			if (target == null) {
				// Blank target in SVG is new window, so change to self for the
				// sake of consistency.
				target = "_self"; //$NON-NLS-1$
			}
			// To resolve security issue in IE7, call
			// parent method to redirect
			String jsRedirect = "redirect('"//$NON-NLS-1$
					+ target + "',_url);";//$NON-NLS-1$
			url = "var _url='" //$NON-NLS-1$
					+ urlValue.getBaseUrl() + "'; try { parent." //$NON-NLS-1$
					+ jsRedirect + " } catch(e) { "//$NON-NLS-1$
					+ jsRedirect + " }"; //$NON-NLS-1$
		}

		elm.setAttribute(scriptEvent, wrapJS(bDblClick, url));

	}

	/**
	 * Set tooltip for URLRedirect action event.
	 *
	 * @param elm
	 * @param src
	 * @param urlValue
	 * @since 2.3
	 */
	private void setTooltipForURLRedirect(Element elm, StructureSource src, URLValue urlValue) {
		setTooltipForURLRedirect(elm, src, urlValue.getTooltip());

	}

	/**
	 * Set tooltip for URLRedirect action event.
	 *
	 * @param elm
	 * @param src
	 * @param urlValue
	 * @since 2.5
	 */
	private void setTooltipForURLRedirect(Element elm, StructureSource src, String tooltipText) {
		// make sure the tooltip text is not empty
		if ((tooltipText != null) && (tooltipText.trim().length() > 0)) {
			Element title = svg_g2d.dom.createElement("title"); //$NON-NLS-1$
			title.appendChild(svg_g2d.dom.createTextNode(tooltipText));
			elm.appendChild(title);

			String componentId = null;
			if (src instanceof WrappedStructureSource) {
				componentId = findFirstComponentId((WrappedStructureSource) src);
			}

			elm.setAttribute("onmouseout", "TM.remove()"); //$NON-NLS-1$ //$NON-NLS-2$
			if (componentId != null) {
				elm.setAttribute("onmousemove", "TM.show(evt," + componentId + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				elm.setAttribute("onmousemove", "TM.show(evt)"); //$NON-NLS-1$ //$NON-NLS-2$
			}

		}
	}

	/**
	 * Locates a design-time series corresponding to a given cloned run-time series.
	 *
	 * @param seRT
	 * @return
	 */
	private final Series findDesignTimeSeries(Series seRT) throws ChartException {

		Series seDT = null;

		final Chart cmRT = _iun.getRunTimeModel();
		final Chart cmDT = _iun.getDesignTimeModel();

		if (cmDT instanceof ChartWithAxes) {
			final ChartWithAxes cwaRT = (ChartWithAxes) cmRT;
			final ChartWithAxes cwaDT = (ChartWithAxes) cmDT;

			Axis[] axaBase = cwaRT.getPrimaryBaseAxes();
			Axis axBase = axaBase[0];
			Axis[] axaOrthogonal = cwaRT.getOrthogonalAxes(axBase, true);
			EList<SeriesDefinition> elSD;
			EList<Series> elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = axaBase[0].getSeriesDefinitions();
			for (j = 0; j < elSD.size(); j++) {
				sd = elSD.get(j);
				elSE = sd.getSeries();
				for (k = 0; k < elSE.size(); k++) {
					se = elSE.get(k);
					if (seRT == se) {
						bFound = true;
						break;
					}
				}
				if (bFound) {
					break;
				}
			}

			if (!bFound) {
				// locate indexes for axis/seriesdefinition/series in runtime
				// model
				for (i = 0; i < axaOrthogonal.length; i++) {
					elSD = axaOrthogonal[i].getSeriesDefinitions();
					for (j = 0; j < elSD.size(); j++) {
						sd = elSD.get(j);
						elSE = sd.getSeries();
						for (k = 0; k < elSE.size(); k++) {
							se = elSE.get(k);
							if (seRT == se) {
								bFound = true;
								break;
							}
						}
						if (bFound) {
							break;
						}
					}
					if (bFound) {
						break;
					}
				}
			}

			if (!bFound) {
				// TODO change ResourceBundle to use ICU class
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[] { seRT },
						org.eclipse.birt.chart.device.extension.i18n.Messages.getResourceBundle(getULocale()));
			}

			// MAP TO INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN DESIGN TIME
			// MODEL
			axaBase = cwaDT.getPrimaryBaseAxes();
			axBase = axaBase[0];
			axaOrthogonal = cwaDT.getOrthogonalAxes(axBase, true);
			if (i == -1) {
				elSD = axaBase[0].getSeriesDefinitions();
			} else {
				elSD = axaOrthogonal[i].getSeriesDefinitions();
			}
			sd = elSD.get(j);
			elSE = sd.getSeries();
			seDT = elSE.get(k);
		} else if (cmDT instanceof ChartWithoutAxes) {
			final ChartWithoutAxes cwoaRT = (ChartWithoutAxes) cmRT;
			final ChartWithoutAxes cwoaDT = (ChartWithoutAxes) cmDT;

			EList<SeriesDefinition> elSD;
			EList<Series> elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = cwoaRT.getSeriesDefinitions();
			for (j = 0; j < elSD.size(); j++) {
				sd = elSD.get(j);
				elSE = sd.getSeries();
				for (k = 0; k < elSE.size(); k++) {
					se = elSE.get(k);
					if (seRT == se) {
						bFound = true;
						break;
					}
				}
				if (bFound) {
					break;
				}
			}

			if (!bFound) {
				i = 1;
				elSD = cwoaRT.getSeriesDefinitions().get(0).getSeriesDefinitions();

				for (j = 0; j < elSD.size(); j++) {
					sd = elSD.get(j);
					elSE = sd.getSeries();
					for (k = 0; k < elSE.size(); k++) {
						se = elSE.get(k);
						if (seRT == se) {
							bFound = true;
							break;
						}
					}
					if (bFound) {
						break;
					}
				}
			}

			if (!bFound) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[] { seRT },
						org.eclipse.birt.chart.device.extension.i18n.Messages.getResourceBundle(getULocale()));
			}

			if (i == -1) {
				elSD = cwoaDT.getSeriesDefinitions();
			} else {
				elSD = cwoaDT.getSeriesDefinitions().get(0).getSeriesDefinitions();
			}
			sd = elSD.get(j);
			elSE = sd.getSeries();
			seDT = elSE.get(k);
		}

		return seDT;
	}

	private String getJsScriptEvent(int condition) {
		switch (condition) {
		case TriggerCondition.MOUSE_HOVER:
			return "onmouseover"; //$NON-NLS-1$
		case TriggerCondition.MOUSE_CLICK:
			return "onclick"; //$NON-NLS-1$
		case TriggerCondition.ONCLICK:
			return "onclick"; //$NON-NLS-1$
		case TriggerCondition.ONDBLCLICK:
			return "ondblclick"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEDOWN:
			return "onmousedown"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEUP:
			return "onmouseup"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEOVER:
			return "onmouseover"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEMOVE:
			return "onmousemove"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEOUT:
			return "onmouseout"; //$NON-NLS-1$
		case TriggerCondition.ONFOCUS:
			return "onfocusin"; //$NON-NLS-1$
		case TriggerCondition.ONBLUR:
			return "onfocusout"; //$NON-NLS-1$
		case TriggerCondition.ONKEYDOWN:
			return "onkeydown"; //$NON-NLS-1$
		case TriggerCondition.ONKEYPRESS:
			return "onkeypress"; //$NON-NLS-1$
		case TriggerCondition.ONKEYUP:
			return "onkeyup"; //$NON-NLS-1$
		case TriggerCondition.ONLOAD:
			return "onload"; //$NON-NLS-1$
		}
		return null;

	}

	protected void setCursor(Element currentElement, Cursor cursor, String defaultCursor) {
		setCursorAttribute(currentElement, cursor, defaultCursor);
	}

	public void clear() {
		labelPrimitives.clear();
		componentPrimitives.clear();
		scripts.clear();
		mapOnloadAdded.clear();
	}

	public Node getHotspotLayer() {
		return hotspotLayer;
	}

	public void createHotspotLayer(Document dom) {
		hotspotLayer = dom.createElement("g"); //$NON-NLS-1$
		hotspotLayer.setAttribute("id", "hotSpots"); //$NON-NLS-1$ //$NON-NLS-2$
		hotspotLayer.setAttribute("style", "fill-opacity:0.01;fill:#FFFFFF;"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private ULocale getULocale() {
		return device.getULocale();
	}

	private boolean isColoredByCategories() {
		return this._iun.getRunTimeModel().getLegend().getItemType() == LegendItemType.CATEGORIES_LITERAL;
	}

	private String findFirstComponentId(WrappedStructureSource src) {
		final Series seRT = (Series) getElementFromSource(src.getParent(), StructureType.SERIES);
		if (seRT != null) {
			Series seDT = null;
			String groupIdentifier = null;

			// Create Group identifiers. Differs for color by categories or
			// series
			if (isColoredByCategories()) {
				seDT = findCategorySeries(seRT);
				StringBuilder sb = new StringBuilder();
				sb.append(seDT.hashCode());
				sb.append("index"); //$NON-NLS-1$
				// Bugzilla#192240 always use the first index to concatenate
				// the id which exists in script context
				sb.append(iFirstDataPointIndex);
				groupIdentifier = sb.toString();
			} else {
				try {
					seDT = findDesignTimeSeries(seRT);
				} catch (ChartException e) {
					logger.log(e);
					return null;
				}
				groupIdentifier = String.valueOf(seDT.hashCode());
			}
			List<String> components = componentPrimitives.get(seDT);
			// return the first element
			if ((components != null) && (components.size() > 0)) {
				return "'" + groupIdentifier + "_" + components.get(0) + "'"; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			}
		}
		return null;
	}

	private String getJSCodeFunctionSuffix(StructureSource src) {
		String jsFunction = null;

		final Series seRT = (Series) getElementFromSource(src, StructureType.SERIES);
		if (seRT != null) {
			Series seDT = null;
			String groupIdentifier = null;

			// Create Group identifiers. Differs for color by categories or
			// series
			if (isColoredByCategories()) {
				seDT = findCategorySeries(seRT);
				final int baseIndex = getDataPointHints(src).getIndex();
				StringBuffer sb = new StringBuffer();
				sb.append("'");//$NON-NLS-1$
				sb.append(seDT.hashCode());
				sb.append("index"); //$NON-NLS-1$
				sb.append(baseIndex);
				sb.append("'");//$NON-NLS-1$
				groupIdentifier = sb.toString();
			} else {
				try {
					seDT = findDesignTimeSeries(seRT);
				} catch (ChartException e) {
					logger.log(e);
					return null;
				}
				groupIdentifier = String.valueOf(seDT.hashCode());
			}

			boolean includeLabels = true;
			boolean includeGraphics = true;

			if (includeGraphics || includeLabels) {
				StringBuffer sb = new StringBuffer();
				sb.append(", ");//$NON-NLS-1$
				sb.append(groupIdentifier);

				sb.append(",new Array("); //$NON-NLS-1$

				List<String> labelComponents = labelPrimitives.get(seDT);
				List<String> components = componentPrimitives.get(seDT);

				Iterator<String> iter = null;
				// Apply action to graphics
				if (includeGraphics && components != null) {
					iter = components.iterator();
					appendArguments(sb, iter);
					if (includeLabels && labelComponents != null) {
						sb.append("), new Array("); //$NON-NLS-1$
					}
				}
				// Apply action to labels
				if (includeLabels && labelComponents != null) {
					iter = labelComponents.iterator();
					appendArguments(sb, iter);
				}

				sb.append(")"); //$NON-NLS-1$

				jsFunction = sb.toString() + ")";//$NON-NLS-1$
			}
		} else {
			// the source is not a series object. It may be a title, plot area
			// or axis
			Object designObject = null;
			// check to see if this is the title block
			if (getElementFromSource(src, StructureType.TITLE) != null) {
				designObject = src.getSource();
			} else if (getElementFromSource(src, StructureType.PLOT) != null) {
				designObject = src.getSource();
			} else if (getElementFromSource(src, StructureType.CHART_BLOCK) != null) {
				designObject = src.getSource();
			} else if (getElementFromSource(src, StructureType.AXIS) != null) {
				designObject = src.getSource();
			}
			if (designObject != null) {
				List<String> components = componentPrimitives.get(designObject);

				Iterator<String> iter = null;
				// Apply action to graphics
				if (components != null) {
					String groupIdentifier = String.valueOf(designObject.hashCode());
					StringBuffer sb = new StringBuffer();
					sb.append(", "); //$NON-NLS-1$
					sb.append(groupIdentifier);

					sb.append(",new Array("); //$NON-NLS-1$
					iter = components.iterator();
					appendArguments(sb, iter);

					sb.append(")"); //$NON-NLS-1$
					jsFunction = sb.toString() + ")";//$NON-NLS-1$
				}
			}
		}
		return jsFunction;
	}

	private void addJSCodeOnElement(StructureSource src, Trigger tg, Element elm, String scriptEvent, int type,
			boolean bDblClick) {
		final Series seRT = (Series) getElementFromSource(src, StructureType.SERIES);
		if (seRT != null) {
			Series seDT = null;
			String groupIdentifier = null;

			// Create Group identifiers. Differs for color by categories or
			// series
			if (isColoredByCategories()) {
				seDT = findCategorySeries(seRT);
				final int baseIndex = getDataPointHints(src).getIndex();
				StringBuffer sb = new StringBuffer();
				sb.append("'");//$NON-NLS-1$
				sb.append(seDT.hashCode());
				sb.append("index"); //$NON-NLS-1$
				sb.append(baseIndex);
				sb.append("'");//$NON-NLS-1$
				groupIdentifier = sb.toString();
			} else {
				try {
					seDT = findDesignTimeSeries(seRT);
				} catch (ChartException e) {
					logger.log(e);
					return;
				}
				groupIdentifier = String.valueOf(seDT.hashCode());
			}
			boolean includeLabels = false;
			boolean includeGraphics = false;

			String jsFunction = null;
			switch (type) {
			case ActionType.TOGGLE_VISIBILITY:
				jsFunction = "toggleVisibility(evt"; //$NON-NLS-1$
				includeLabels = true;
				includeGraphics = true;
				break;
			case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
				jsFunction = "toggleLabelsVisibility(evt"; //$NON-NLS-1$
				includeLabels = true;
				includeGraphics = false;
				break;
			case ActionType.HIGHLIGHT:
				jsFunction = "highlight(evt"; //$NON-NLS-1$
				includeLabels = true;
				includeGraphics = true;
				break;
			}

			if (jsFunction == null) {
				assert false;
				return;
			}

			if (includeGraphics || includeLabels) {
				StringBuffer sb = new StringBuffer();
				sb.append(", ");//$NON-NLS-1$
				sb.append(groupIdentifier);

				sb.append(",new Array("); //$NON-NLS-1$

				List<String> labelComponents = labelPrimitives.get(seDT);
				List<String> components = componentPrimitives.get(seDT);

				Iterator<String> iter = null;
				// Apply action to graphics
				if (includeGraphics && components != null) {
					iter = components.iterator();
					appendArguments(sb, iter);
					if (includeLabels && labelComponents != null) {
						sb.append("), new Array("); //$NON-NLS-1$
					}
				}
				// Apply action to labels
				if (includeLabels && labelComponents != null) {
					iter = labelComponents.iterator();
					appendArguments(sb, iter);
				}

				sb.append(")"); //$NON-NLS-1$

				jsFunction += sb.toString() + ")";//$NON-NLS-1$
				elm.setAttribute(scriptEvent, wrapJS(bDblClick, jsFunction));

				if (tg.getCondition().getValue() == TriggerCondition.ONMOUSEOVER) {
					elm.setAttribute("onmouseout", //$NON-NLS-1$
							jsFunction);
				}
			}
		} else {
			// the source is not a series object. It may be a title, plot area
			// or axis
			Object designObject = null;
			// check to see if this is the title block
			if (getElementFromSource(src, StructureType.TITLE) != null) {
				designObject = src.getSource();
			} else if (getElementFromSource(src, StructureType.PLOT) != null) {
				designObject = src.getSource();
			} else if (getElementFromSource(src, StructureType.CHART_BLOCK) != null) {
				designObject = src.getSource();
			} else if (getElementFromSource(src, StructureType.AXIS) != null) {
				designObject = src.getSource();
			}
			if (designObject != null) {
				String jsFunction = null;
				switch (type) {
				case ActionType.TOGGLE_VISIBILITY:
					jsFunction = "toggleVisibility(evt"; //$NON-NLS-1$
					break;
				case ActionType.HIGHLIGHT:
					jsFunction = "highlight(evt";//$NON-NLS-1$
					break;
				}
				if (jsFunction == null) {
					assert false;
					return;
				}
				List<String> components = componentPrimitives.get(designObject);

				Iterator<String> iter = null;
				// Apply action to graphics
				if (components != null) {
					String groupIdentifier = String.valueOf(designObject.hashCode());
					StringBuffer sb = new StringBuffer();
					sb.append(", "); //$NON-NLS-1$
					sb.append(groupIdentifier);

					sb.append(",new Array("); //$NON-NLS-1$
					iter = components.iterator();
					appendArguments(sb, iter);

					sb.append(")"); //$NON-NLS-1$

					elm.setAttribute(scriptEvent, wrapJS(bDblClick, jsFunction + sb.toString() + ")")); //$NON-NLS-1$

					if (tg.getCondition().getValue() == TriggerCondition.ONMOUSEOVER) {
						elm.setAttribute("onmouseout", //$NON-NLS-1$
								jsFunction + sb.toString() + ")"); //$NON-NLS-1$
					}
				}
			}
		}
	}

	private void appendArguments(StringBuffer sb, Iterator<String> iter) {
		if (iter != null) {
			while (iter.hasNext()) {
				sb.append("'").append(iter.next()).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
				if (iter.hasNext()) {
					sb.append(","); //$NON-NLS-1$
				}
			}
		}
	}

	private String wrapJS(boolean bDblClick, String js) {
		if (!bDblClick) {
			return js;
		}
		return "if ( evt.detail==2 ){" + js + "}"; //$NON-NLS-1$//$NON-NLS-2$
	}

	private void setCursorAttribute(Element elm, Cursor cursor, String defaultValue) {
		String style = elm.getAttribute("style"); //$NON-NLS-1$
		if (style == null) {
			style = ""; //$NON-NLS-1$
		}

		if (cursor == null || cursor.getType() == CursorType.AUTO) {
			return;
		}

		String value = style + CSSHelper.getCSSCursorValue(cursor);
		elm.setAttribute("style", style + value);//$NON-NLS-1$
	}

	private void addCallBackScript(StructureSource src, StringBuffer callbackFunction, boolean hasEndTag) {
		callbackFunction.append("(evt,"); //$NON-NLS-1$
		callbackFunction.append(src.getSource().hashCode());

		final DataPointHints dph;
		final LegendItemHints lerh;
		final String axisLabel;
		if (StructureType.SERIES_DATA_POINT.equals(src.getType())) {
			dph = (DataPointHints) src.getSource();
		} else {
			dph = null;
		}
		if (StructureType.LEGEND_ENTRY.equals(src.getType())) {
			lerh = (LegendItemHints) src.getSource();
		} else {
			lerh = null;
		}
		if (StructureType.AXIS_LABEL.equals(src.getType())) {
			axisLabel = (String) src.getSource();
		} else {
			axisLabel = null;
		}
		ScriptUtil.script(callbackFunction, dph, lerh, axisLabel);

		if (hasEndTag) {
			callbackFunction.append(");"); //$NON-NLS-1$
		}
	}

	private String generateCallBackMethodName(String funcName) {
		return "function " //$NON-NLS-1$
				+ funcName + "(evt,source,"//$NON-NLS-1$
				+ ScriptHandler.BASE_VALUE + ","//$NON-NLS-1$
				+ ScriptHandler.ORTHOGONAL_VALUE + ","//$NON-NLS-1$
				+ ScriptHandler.SERIES_VALUE + ","//$NON-NLS-1$
				+ IActionRenderer.LEGEND_ITEM_DATA + ","//$NON-NLS-1$
				+ IActionRenderer.LEGEND_ITEM_TEXT + ","//$NON-NLS-1$
				+ IActionRenderer.LEGEND_ITEM_VALUE + ","//$NON-NLS-1$
				+ IActionRenderer.AXIS_LABEL + ","//$NON-NLS-1$
				+ ScriptHandler.ID + ","//$NON-NLS-1$
				+ ScriptHandler.COMP_LIST + ","//$NON-NLS-1$
				+ ScriptHandler.LABEL_LIST + ")";//$NON-NLS-1$
	}

	private DataPointHints getDataPointHints(StructureSource src) {
		if (src.getType() == StructureType.SERIES_DATA_POINT || src.getType() == StructureType.SERIES_ELEMENT) {
			return (DataPointHints) src.getSource();
		}
		if (src instanceof WrappedStructureSource) {
			// Data point hints may be wrapped in legend entry
			return getDataPointHints(((WrappedStructureSource) src).getParent());
		}
		return null;
	}
}
