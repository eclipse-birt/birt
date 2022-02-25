/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import org.eclipse.birt.report.engine.content.IImageContent;

/**
 * <p>
 * CellImage is used to cache all the required data for inserting images so that
 * they can be processed after all other spreadsheet contents has been inserted.
 * </p>
 * <p>
 * Processing images after all other spreadsheet contents means that the images
 * will be unaffected by any column resizing that may be required. Images
 * usually cause row resizing (the emitter never allows an image to spread onto
 * multiple rows), but never cause column resizing.
 * </p>
 *
 * @author Jim Talbut
 *
 */
public class CellImage {
	public Coordinate location;
	public int imageIdx;
	public IImageContent image;
	public boolean spanColumns;

	public CellImage(Coordinate location, int imageIdx, IImageContent image, boolean spanColumns) {
		this.location = location;
		this.imageIdx = imageIdx;
		this.image = image;
		this.spanColumns = spanColumns;
	}
}
