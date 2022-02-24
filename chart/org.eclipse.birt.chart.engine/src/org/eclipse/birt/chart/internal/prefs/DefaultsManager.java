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

package org.eclipse.birt.chart.internal.prefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * A utility class manages the chart default settings.
 */
public final class DefaultsManager {

	private static final String sLocation = "charts.ini"; //$NON-NLS-1$

	private transient Preferences pr = null;

	private static transient DefaultsManager dm = null;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/model.prefs"); //$NON-NLS-1$

	public static final void main(String[] sa) throws Exception {
		/*
		 * DefaultsManager dm = DefaultsManager.instance(); String s =
		 * dm.pr.get("/label/datapoint/font", null);
		 */
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static DefaultsManager instance() {
		if (dm == null) {
			dm = new DefaultsManager();
			if (!dm.exists()) {
				try {
					dm.createSample();
				} catch (Exception ex) {
					logger.log(ex);
				}
			} else {
				try {
					dm.read();
				} catch (Exception ex) {
					logger.log(ex);
				}
			}
		}
		return dm;
	}

	/**
	 * 
	 */
	private DefaultsManager() {
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BackingStoreException
	 */
	private final void createSample() throws IOException, BackingStoreException {
		DefaultsManager dm = DefaultsManager.instance();
		dm.samplePreferences();
		dm.write();
	}

	/**
	 * 
	 */
	private final void samplePreferences() {
		pr = DefaultsManager.create(null, null);
		Preferences prLabel = DefaultsManager.create(pr, PreferenceKey.N_LABEL);
		Preferences prLabelDatapoint = DefaultsManager.create(prLabel, PreferenceKey.N_DATAPOINT);
		FontDefinition fd = new FontDefinitionImpl("Arial", 12); //$NON-NLS-1$
		prLabelDatapoint.put(PreferenceKey.PK_FONT, fd.toString());
		ColorDefinition cdText = ColorDefinitionImpl.BLACK();
		prLabelDatapoint.put(PreferenceKey.PK_TEXTCOLOR, cdText.toString());
		Fill ifBackground = ColorDefinitionImpl.TRANSPARENT();
		prLabelDatapoint.put(PreferenceKey.PK_FILL, ifBackground.toString());
		LineAttributes laOutline = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) laOutline).set(ColorDefinitionImpl.BLACK(), LineStyle.DOTTED_LITERAL, 1);
		prLabelDatapoint.put(PreferenceKey.PK_OUTLINE, laOutline.toString());

		Preferences prLabelAxis = DefaultsManager.create(prLabel, PreferenceKey.N_AXIS);
		fd = new FontDefinitionImpl("Arial", 12); //$NON-NLS-1$
		prLabelAxis.put(PreferenceKey.PK_FONT, fd.toString());
		cdText = ColorDefinitionImpl.BLACK();
		prLabelAxis.put(PreferenceKey.PK_TEXTCOLOR, cdText.toString());
		ifBackground = ColorDefinitionImpl.TRANSPARENT();
		prLabelAxis.put(PreferenceKey.PK_FILL, ifBackground.toString());
		laOutline = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) laOutline).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		prLabelAxis.put(PreferenceKey.PK_OUTLINE, laOutline.toString());

		Preferences prLabelLegend = DefaultsManager.create(prLabel, PreferenceKey.N_LEGEND);
		fd = new FontDefinitionImpl("Arial", 12); //$NON-NLS-1$
		prLabelLegend.put(PreferenceKey.PK_FONT, fd.toString());
		cdText = ColorDefinitionImpl.BLACK();
		prLabelLegend.put(PreferenceKey.PK_TEXTCOLOR, cdText.toString());
		ifBackground = ColorDefinitionImpl.TRANSPARENT();
		prLabelLegend.put(PreferenceKey.PK_FILL, ifBackground.toString());
		laOutline = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) laOutline).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		prLabelLegend.put(PreferenceKey.PK_OUTLINE, laOutline.toString());

		Preferences prTitle = DefaultsManager.create(pr, PreferenceKey.N_TITLE);
		fd = new FontDefinitionImpl("Arial", 12); //$NON-NLS-1$
		prTitle.put(PreferenceKey.PK_FONT, fd.toString());
		cdText = ColorDefinitionImpl.BLACK();
		prTitle.put(PreferenceKey.PK_TEXTCOLOR, cdText.toString());
		ifBackground = ColorDefinitionImpl.TRANSPARENT();
		prTitle.put(PreferenceKey.PK_FILL, ifBackground.toString());
		laOutline = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) laOutline).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		prTitle.put(PreferenceKey.PK_OUTLINE, laOutline.toString());

		Preferences prTitleAxis = DefaultsManager.create(prTitle, PreferenceKey.N_AXIS);
		fd = new FontDefinitionImpl("Arial", 12); //$NON-NLS-1$
		prTitleAxis.put(PreferenceKey.PK_FONT, fd.toString());
		cdText = ColorDefinitionImpl.BLACK();
		prTitleAxis.put(PreferenceKey.PK_TEXTCOLOR, cdText.toString());
		ifBackground = ColorDefinitionImpl.TRANSPARENT();
		prTitleAxis.put(PreferenceKey.PK_FILL, ifBackground.toString());
		laOutline = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) laOutline).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		prTitleAxis.put(PreferenceKey.PK_OUTLINE, laOutline.toString());

		Preferences prTitleSeries = DefaultsManager.create(prTitle, PreferenceKey.N_SERIES);
		fd = new FontDefinitionImpl("Arial", 12); //$NON-NLS-1$
		prTitleSeries.put(PreferenceKey.PK_FONT, fd.toString());
		cdText = ColorDefinitionImpl.BLACK();
		prTitleSeries.put(PreferenceKey.PK_TEXTCOLOR, cdText.toString());
		ifBackground = ColorDefinitionImpl.TRANSPARENT();
		prTitleSeries.put(PreferenceKey.PK_FILL, ifBackground.toString());
		laOutline = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) laOutline).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		prTitleSeries.put(PreferenceKey.PK_OUTLINE, laOutline.toString());
	}

	/**
	 * 
	 * @param pParent
	 * @param pk
	 * @return
	 */
	private static final Preferences create(Preferences pParent, PreferenceKey pk) {
		return (pParent == null) ? (Preferences.userRoot()) : pParent.node(pk.getKey());
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BackingStoreException
	 */
	public final void write() throws IOException, BackingStoreException {
		FileOutputStream fos = null;
		try {
			fos = SecurityUtil.newFileOutputStream(sLocation);
			pr.exportSubtree(fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws InvalidPreferencesFormatException
	 */
	public final void read() throws IOException, InvalidPreferencesFormatException {
		try {
			pr = AccessController.doPrivileged(new PrivilegedExceptionAction<Preferences>() {

				public Preferences run() throws Exception {
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(sLocation);
						Preferences.importPreferences(fis);
						return Preferences.userRoot();
					} finally {
						if (fis != null) {
							fis.close();
						}
					}
				}

			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			} else if (typedException instanceof InvalidPreferencesFormatException) {
				throw (InvalidPreferencesFormatException) typedException;
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private final boolean exists() {
		final File f = new File(sLocation);

		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				return (f.exists() && f.isFile());
			}

		});
	}
}
