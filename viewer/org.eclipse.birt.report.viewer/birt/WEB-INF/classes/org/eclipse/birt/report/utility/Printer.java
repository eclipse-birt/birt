/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.utility;

import java.util.HashMap;
import java.util.Map;

import javax.print.PrintService;
import javax.print.attribute.standard.Media;

/**
 * Printer bean for descripting a printer on the server.
 * <p>
 */
public class Printer {

	private String name;
	private int status;
	private String model;
	private String info;

	// Copies attribute
	private boolean copiesSupported;
	private int copies;

	// Collate attribute
	private boolean collateSupported;
	private boolean collate;

	// Mode attribute
	private boolean modeSupported;
	private int mode;

	// Duplex attribute
	private boolean duplexSupported;
	private int duplex;

	// Media size attribute
	private boolean mediaSupported;
	private String mediaSize;
	private Map mediaSizeNames = new HashMap();

	// Page settings
	private int pageSize;
	private String pageRange;

	// Print Service
	private PrintService service;

	// Constants
	public static final int MODE_MONOCHROME = 0;
	public static final int MODE_COLOR = 1;

	public static final int DUPLEX_SIMPLEX = 0;
	public static final int DUPLEX_HORIZONTAL = 1;
	public static final int DUPLEX_VERTICAL = 2;

	public static final int STATUS_ACCEPTING_JOBS = 0;
	public static final int STATUS_NOT_ACCEPTING_JOBS = 1;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the copiesSupported
	 */
	public boolean isCopiesSupported() {
		return copiesSupported;
	}

	/**
	 * @param copiesSupported the copiesSupported to set
	 */
	public void setCopiesSupported(boolean copiesSupported) {
		this.copiesSupported = copiesSupported;
	}

	/**
	 * @return the copies
	 */
	public int getCopies() {
		return copies;
	}

	/**
	 * @param copies the copies to set
	 */
	public void setCopies(int copies) {
		this.copies = copies;
	}

	/**
	 * @return the collateSupported
	 */
	public boolean isCollateSupported() {
		return collateSupported;
	}

	/**
	 * @param collateSupported the collateSupported to set
	 */
	public void setCollateSupported(boolean collateSupported) {
		this.collateSupported = collateSupported;
	}

	/**
	 * @return the collate
	 */
	public boolean isCollate() {
		return collate;
	}

	/**
	 * @param collate the collate to set
	 */
	public void setCollate(boolean collate) {
		this.collate = collate;
	}

	/**
	 * @return the modeSupported
	 */
	public boolean isModeSupported() {
		return modeSupported;
	}

	/**
	 * @param modeSupported the modeSupported to set
	 */
	public void setModeSupported(boolean modeSupported) {
		this.modeSupported = modeSupported;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the duplexSupported
	 */
	public boolean isDuplexSupported() {
		return duplexSupported;
	}

	/**
	 * @param duplexSupported the duplexSupported to set
	 */
	public void setDuplexSupported(boolean duplexSupported) {
		this.duplexSupported = duplexSupported;
	}

	/**
	 * @return the duplex
	 */
	public int getDuplex() {
		return duplex;
	}

	/**
	 * @param duplex the duplex to set
	 */
	public void setDuplex(int duplex) {
		this.duplex = duplex;
	}

	/**
	 * @return the mediaSupported
	 */
	public boolean isMediaSupported() {
		return mediaSupported;
	}

	/**
	 * @param mediaSupported the mediaSupported to set
	 */
	public void setMediaSupported(boolean mediaSupported) {
		this.mediaSupported = mediaSupported;
	}

	/**
	 * @return the mediaSize
	 */
	public String getMediaSize() {
		return mediaSize;
	}

	/**
	 * @param mediaSize the mediaSize to set
	 */
	public void setMediaSize(String mediaSize) {
		this.mediaSize = mediaSize;
	}

	/**
	 * @return the mediaSizeNames
	 */
	public Map getMediaSizeNames() {
		return mediaSizeNames;
	}

	/**
	 * Add media size name
	 *
	 * @param name
	 */
	public void addMediaSizeName(String name, Media media) {
		mediaSizeNames.put(name, media);
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageRange
	 */
	public String getPageRange() {
		return pageRange;
	}

	/**
	 * @param pageRange the pageRange to set
	 */
	public void setPageRange(String pageRange) {
		this.pageRange = pageRange;
	}

	/**
	 * @return the service
	 */
	public PrintService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(PrintService service) {
		this.service = service;
	}

}
