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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * XlsxEmitter is the leaf class for implementing the ExcelEmitter with
 * XSSFWorkbook.
 *
 * @author Jim Talbut
 *
 */
public class XlsxEmitter extends ExcelEmitter {

	/**
	 */
	public XlsxEmitter() {
		super(StyleManagerXUtils.getFactory());
		log.debug("Constructed XlsxEmitter");
	}

	@Override
	public String getOutputFormat() {
		return "xlsx";
	}

	@Override
	protected Workbook createWorkbook() {
		return new XSSFWorkbook();
	}

	@Override
	protected Workbook openWorkbook(File templateFile) throws IOException {
		InputStream stream = new FileInputStream(templateFile);
		try (stream) {
			return new XSSFWorkbook(stream);
		}
	}

}
