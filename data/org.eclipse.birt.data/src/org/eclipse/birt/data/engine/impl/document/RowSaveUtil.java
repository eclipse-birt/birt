/*
 * Created on 2006-5-18
 * 
 * Copyright mol.com allright reserved.
 */
package org.eclipse.birt.data.engine.impl.document;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

public class RowSaveUtil {
	
	private int lastRowIndex;
	private int currentOffset;
	private int currRowBytes;

	private byte[] zeroBytes;

	private int rowCount;

	private boolean rowStart = true;

	//
	private OutputStream rowExprsOs;
	private OutputStream rowLenOs;
	
	//
	private DataOutputStream rowExprsDos;
	private DataOutputStream rowLenDos;

	private boolean inited;
	
	private Set exprNameSet;

	/**
	 * 
	 */
	public RowSaveUtil(int rowCount, OutputStream rowExprsOs,
			OutputStream rowLenOs, Set exprNameSet) {
		this.rowCount = rowCount;
		this.currRowBytes = 0;
		this.rowExprsOs = rowExprsOs;
		this.rowLenOs = rowLenOs;
		this.exprNameSet = exprNameSet;
	}

	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 * @throws DataException
	 */
	public void saveExprValue(int currIndex, String exprID, Object exprValue)
			throws DataException {
		initSave(false);

		if (currIndex != lastRowIndex) {
			this.saveWhenEndOneRow(currIndex);
			lastRowIndex = currIndex;
		}

		saveWhenInOneRow(currIndex, exprID, exprValue);
	}

	/**
	 * @param currIndex
	 * @throws DataException
	 */
	private void saveWhenEndOneRow(int currIndex) throws DataException {
		try {
			if (currIndex > 0)
				this.currRowBytes += 4;

			saveEndOfCurrRow(lastRowIndex, currIndex);
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e,
					"Result Data");
		}
	}

	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 * @throws DataException
	 */
	private void saveWhenInOneRow(int currIndex, String exprID, Object exprValue)
			throws DataException {
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
		BufferedOutputStream tempBos = new BufferedOutputStream(tempBaos);
		DataOutputStream tempDos = new DataOutputStream(tempBos);

		try {
			if (rowStart == true) {
				if (currIndex > 0)
					IOUtil.writeInt(this.rowExprsDos, RDIOUtil.RowSeparator);

				IOUtil.writeInt(tempDos, RDIOUtil.ColumnSeparator);
			} else {
				IOUtil.writeInt(tempDos, RDIOUtil.ColumnSeparator);
			}

			IOUtil.writeString(tempDos, exprID);
			IOUtil.writeObject(tempDos, exprValue);

			tempDos.flush();
			tempBos.flush();
			tempBaos.flush();

			byte[] bytes = tempBaos.toByteArray();
			currRowBytes += bytes.length;
			IOUtil.writeRawBytes(this.rowExprsDos, bytes);

			tempBaos = null;
			tempBos = null;
			tempDos = null;

			rowStart = false;
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e,
					"Result Data");
		}

		exprNameSet.add(exprID);
	}

	/**
	 * @param currIndex
	 * @throws DataException
	 */
	public void saveFinish(int currIndex) throws DataException {
		initSave(true);

		try {
			saveEndOfCurrRow(lastRowIndex, currIndex);

			rowExprsDos.close();
			rowLenDos.close();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e,
					"Result Data");
		}
	}

	/**
	 * init save environment
	 */
	private void initSave(boolean finish) throws DataException {
		if (inited == true)
			return;

		inited = true;
		try {
			rowExprsDos = new DataOutputStream(rowExprsOs);
			rowLenDos = new DataOutputStream(rowLenOs);
						
			int totalRowCount = 0;
			if (finish == true)
				totalRowCount = rowCount;
			else
				totalRowCount = rowCount == 0 ? 1 : rowCount;

			// TODO: enhance me
			IOUtil.writeInt(this.rowExprsDos, totalRowCount);
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e);
		}
	}

	/**
	 * @param rowIndex
	 * @throws IOException
	 */
	private void saveEndOfCurrRow(int lastRowIndex, int currIndex)
			throws IOException {
		IOUtil.writeInt(this.rowLenDos, currentOffset);
		currentOffset += currRowBytes;
		this.rowStart = true;
		this.currRowBytes = 0;

		saveNullRowsBetween(lastRowIndex, currIndex);
	}

	/**
	 * @param lastRowIndex
	 * @param currIndex
	 * @throws IOException
	 */
	private void saveNullRowsBetween(int lastRowIndex, int currIndex)
			throws IOException {
		initZeroBytes();

		int gapRows = currIndex - lastRowIndex - 1;
		for (int i = 0; i < gapRows; i++) {
			IOUtil.writeRawBytes(this.rowExprsDos, zeroBytes);
			IOUtil.writeInt(this.rowLenDos, currentOffset);
			currentOffset += zeroBytes.length;
		}
	}

	/**
	 * @throws IOException
	 */
	private void initZeroBytes() throws IOException {
		if (this.zeroBytes == null)
			this.zeroBytes = RDIOUtil.getZeroBytes();
	}

}
