/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension.experimental;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * EditpartExtensionManager
 */
public class EditpartExtensionManager {

	protected static final Logger logger = Logger.getLogger(EditpartExtensionManager.class.getName());

	private static Map<Expression, IConfigurationElement> extensionMap = new HashMap<>();
	private static List<PaletteEntryExtension> palettes = new ArrayList<>();

	static {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint("org.eclipse.birt.report.designer.ui.reportItemEditpart"); //$NON-NLS-1$
		if (extensionPoint != null) {
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement[] enablements = elements[i].getChildren("enablement"); //$NON-NLS-1$
				if (enablements.length == 0) {
					continue;// log message
				}
				try {
					extensionMap.put(ExpressionConverter.getDefault().perform(enablements[0]), elements[i]);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				IConfigurationElement[] paletteEntries = elements[i].getChildren("paletteEntry"); //$NON-NLS-1$
				if (paletteEntries.length == 1) {
					PaletteEntryExtension entry = new PaletteEntryExtension();
					entry.setItemName(paletteEntries[0].getAttribute("itemName")); //$NON-NLS-1$

					String displayName = DEUtil.getMetaDataDictionary().getExtension(entry.getItemName())
							.getDisplayName();

					entry.setLabel(displayName);
					entry.setMenuLabel(paletteEntries[0].getAttribute("menuLabel")); //$NON-NLS-1$
					entry.setDescription(paletteEntries[0].getAttribute("description")); //$NON-NLS-1$
					entry.setIcon(getImageDescriptor(paletteEntries[0], paletteEntries[0].getAttribute("icon"))); //$NON-NLS-1$
					entry.setIconLarge(
							getImageDescriptor(paletteEntries[0], paletteEntries[0].getAttribute("largeIcon"))); //$NON-NLS-1$
					// TODO category can't be empty
					entry.setCategory(paletteEntries[0].getAttribute("category")); //$NON-NLS-1$
					entry.setCategoryDisplayName(paletteEntries[0].getAttribute("categoryDisplayName")); //$NON-NLS-1$
					// TODO command can't be empty
					entry.setCommand(paletteEntries[0].getAttribute("createCommand")); //$NON-NLS-1$

					registerImage(entry);

					palettes.add(entry);
				}
			}
		}
	}

	private static ImageDescriptor getImageDescriptor(IConfigurationElement extension, String iconPath) {
		if (iconPath == null) {
			return null;
		}
		URL path = Platform.getBundle(extension.getNamespace()).getEntry("/"); //$NON-NLS-1$
		try {
			return ImageDescriptor.createFromURL(new URL(path, iconPath));
		} catch (MalformedURLException e) {
		}
		return null;
	}

	// backward compatible see bug 184371
	private static void registerImage(PaletteEntryExtension entry) {
		String symbolName = ReportPlatformUIImages.getIconSymbolName(entry.getItemName(),
				IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON);
		ReportPlatformUIImages.declareImage(symbolName, entry.getIcon());
		symbolName = ReportPlatformUIImages.getIconSymbolName(entry.getItemName(),
				IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON);
		ReportPlatformUIImages.declareImage(symbolName, entry.getIcon());
	}

	/**
	 * Create the edit part
	 *
	 * @param context context of the part
	 * @param model   model
	 * @return Return the edit part
	 */
	public static EditPart createEditPart(EditPart context, Object model) {
		EvaluationContext econtext = new EvaluationContext(null, model);
		for (Iterator<Expression> iterator = extensionMap.keySet().iterator(); iterator.hasNext();) {
			try {
				Expression expression = iterator.next();
				if (expression.evaluate(econtext) == EvaluationResult.TRUE) {
					EditPart editPart = (EditPart) extensionMap.get(expression).createExecutableExtension("type"); //$NON-NLS-1$
					editPart.setModel(model);
					return editPart;
				}
			} catch (CoreException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Get the palette entry
	 *
	 * @param extensionName extension name
	 * @return Return the palette entry
	 */
	public static PaletteEntryExtension getPaletteEntry(String extensionName) {
		for (Iterator<PaletteEntryExtension> itr = palettes.iterator(); itr.hasNext();) {
			PaletteEntryExtension entry = itr.next();

			if (entry.getItemName().equals(extensionName)) {
				return entry;
			}
		}

		return null;
	}

	/**
	 * Get a list of the palette entries
	 *
	 * @return Return a list of the palette entries
	 */
	public static PaletteEntryExtension[] getPaletteEntries() {
		return palettes.toArray(new PaletteEntryExtension[palettes.size()]);
	}
}
