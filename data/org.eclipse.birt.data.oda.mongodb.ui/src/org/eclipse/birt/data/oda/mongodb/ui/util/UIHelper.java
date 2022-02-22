/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.mongodb.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.data.oda.mongodb.ui.Activator;
import org.eclipse.birt.data.oda.mongodb.ui.i18n.Messages;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class UIHelper {

	/**
	 * Set context-sensitive help
	 *
	 * @param control
	 * @param contextId
	 */
	public static void setSystemHelp(Control control, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control, contextId);
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 *
	 * @param sPluginRelativePath The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * @throws IOException
	 * @see #setImageCached(boolean )
	 */
	public static Image getImage(String sPluginRelativePath) throws IOException {
		ImageRegistry registry = JFaceResources.getImageRegistry();
		Image image = registry.get(sPluginRelativePath);
		if (image == null) {
			image = createImage(sPluginRelativePath);
			registry.put(sPluginRelativePath, image);
		}
		return image;
	}

	private static Image createImage(String sPluginRelativePath) throws IOException {
		Image img = null;
		try {
			img = new Image(Display.getCurrent(), getURL(sPluginRelativePath).openStream());
		} catch (MalformedURLException e1) {
			img = new Image(Display.getCurrent(), new FileInputStream(getURL(sPluginRelativePath).toString()));
		}
		// If still can't load, return a dummy image.
		if (img == null) {
			img = new Image(Display.getCurrent(), 1, 1);
		}
		return img;
	}

	/**
	 * This method returns an URL for a resource given its plugin relative path. It
	 * is intended to be used to abstract out the usage of the UI as a plugin or
	 * standalone component when it comes to accessing resources.
	 *
	 * @param sPluginRelativePath The path to the resource relative to the plugin
	 *                            location.
	 * @return URL representing the location of the resource.
	 */
	public static URL getURL(String sPluginRelativePath) throws MalformedURLException {
		URL url = null;
		if (Platform.getExtensionRegistry() != null) {
			url = new URL(Activator.getDefault().getBundle().getEntry("/"), sPluginRelativePath); //$NON-NLS-1$
		} else {
			url = new URL("file:///" + new File(sPluginRelativePath).getAbsolutePath()); //$NON-NLS-1$
		}

		return url;
	}

	/**
	 * Appends the localized exception message to the specified msgKey for use as an
	 * user error message.
	 *
	 * @param userMsgKey
	 * @param ex
	 * @return
	 */
	public static String getUserErrorMessage(String userMsgKey, Exception ex) {
		StringBuilder msg = new StringBuilder().append(userMsgKey != null ? Messages.getString(userMsgKey) : ""); //$NON-NLS-1$
		String exMsg = ex.getLocalizedMessage();
		if (exMsg != null) {
			msg.append("\n").append(exMsg); //$NON-NLS-1$
		}
		return msg.toString();
	}

	/**
	 * Utility method to get the warning icon.
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getEmbeddedWarningImage() throws IOException {
		return getImage("icons/warning.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the button image of "Validate Syntax".
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getDatabaseDisplayImage() throws IOException {
		return getImage("icons/database.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the item icon of a collection.
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getCollectionDisplayImage() throws IOException {
		return getImage("icons/collection.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the item icon of a field.
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getFieldDisplayImage() throws IOException {
		return getImage("icons/field.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the item icon of a document that has not been selected.
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getDocumentDisplayImage() throws IOException {
		return getImage("icons/document.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the item icon of a document that has been selected.
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getSelectedDocumentDisplayImage() throws IOException {
		return getImage("icons/document_selected.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the item icon of a field that has been selected.
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getSelectedFieldDisplayImage() throws IOException {
		return getImage("icons/field_selected.gif"); //$NON-NLS-1$
	}

	/**
	 * Utility method to get the button image of "Validate Syntax".
	 *
	 * @return
	 * @throws IOException
	 */
	public static Image getSyntaxValidationImage() throws IOException {
		return getImage("icons/validate.gif"); //$NON-NLS-1$
	}

	/**
	 * Test the text to see if it can be parsed to an integer.
	 *
	 * @param text
	 * @return
	 */
	public static boolean isNumber(String text) {
		if (isEmptyString(text)) {
			return false;
		}

		return text.matches("^[0-9]*[1-9][0-9]*$"); //$NON-NLS-1$
	}

	/**
	 * Test the text to see if it can be parsed to an integer.
	 *
	 * @param text
	 * @return
	 */
	public static boolean isNumberOrZero(String text) {
		if (isEmptyString(text)) {
			return false;
		}

		return text.matches("^[0-9]*[1-9][0-9]*$") || text.matches("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Test the text to see if it is empty
	 *
	 * @param text
	 * @return
	 */
	public static boolean isEmptyString(String text) {
		return text == null || text.trim().length() == 0;
	}

}
