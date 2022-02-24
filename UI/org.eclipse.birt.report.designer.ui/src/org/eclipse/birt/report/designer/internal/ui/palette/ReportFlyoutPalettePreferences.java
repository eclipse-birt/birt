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

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.birt.report.designer.ui.IPreferenceConstants;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;

/**
 * JRPFlyoutPalettePreferences is used to save/load the preferences for the
 * flyout palette.
 * 
 * 
 */
public class ReportFlyoutPalettePreferences implements FlyoutPreferences {

	/**
	 * Gets the location of the palette
	 * 
	 * @return the saved dock location of the Palette
	 */
	public int getDockLocation() {
		return ReportPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.PALETTE_DOCK_LOCATION);
	}

	/**
	 * Gets the palette state
	 * 
	 * @return the saved state of the palette (collapsed or pinned open)
	 */
	public int getPaletteState() {
		return ReportPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.PALETTE_STATE);
	}

	/**
	 * Returns the width of the palette
	 * 
	 * @return the saved width of the palette
	 */
	public int getPaletteWidth() {
		return ReportPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.PALETTE_SIZE);
	}

	/**
	 * This method is invoked when the flyout palette's dock location is changed.
	 * The provided dock location should be persisted and returned in
	 * {@link #getDockLocation()}.
	 * 
	 * @param location an int representing the dock location
	 */
	public void setDockLocation(int location) {
		ReportPlugin.getDefault().getPreferenceStore().setValue(IPreferenceConstants.PALETTE_DOCK_LOCATION, location);
	}

	/**
	 * This method is invoked when the flyout palette's default state is changed.
	 * The provided state should be persisted and returned in
	 * {@link #getPaletteState()}.
	 * 
	 * @param state an int the state of the flyout palette
	 */
	public void setPaletteState(int state) {
		ReportPlugin.getDefault().getPreferenceStore().setValue(IPreferenceConstants.PALETTE_STATE, state);
	}

	/**
	 * This method is invoked when the flyout palette is resized. The provided width
	 * should be persisted and returned in {@link #getPaletteWidth()}.
	 * 
	 * @param width the new size of the flyout palette
	 */
	public void setPaletteWidth(int width) {
		ReportPlugin.getDefault().getPreferenceStore().setValue(IPreferenceConstants.PALETTE_SIZE, width);
	}

}
