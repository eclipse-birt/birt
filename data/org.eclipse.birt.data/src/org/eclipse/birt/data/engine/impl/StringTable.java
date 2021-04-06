package org.eclipse.birt.data.engine.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

public class StringTable {
	private DataOutputStream dataOutputStream = null;
	private StreamManager manager = null;
	private String fieldName = null;
	private int currentIndex;

	private Map<String, Integer> stringIndexMap = null;
	private List<String> stringList = null;

	public StringTable() {
		this.currentIndex = 0;
		this.stringIndexMap = new HashMap<String, Integer>();
		this.stringList = new ArrayList<String>();
	}

	/**
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public void loadFrom(InputStream inputStream) throws IOException {
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		this.currentIndex = 0;
		while (true) {
			try {
				String key = dataInputStream.readUTF();
				this.stringList.add(key);
				this.stringIndexMap.put(key, currentIndex);
				this.currentIndex++;
			} catch (EOFException e) {
				dataInputStream.close();
				return;
			}
		}
	}

	/**
	 * 
	 * @param outputStream
	 * @throws IOException
	 * @throws DataException
	 */
	public void setStreamManager(StreamManager manager, String fieldName) {
		this.manager = manager;
		this.fieldName = fieldName;

		try {
			RAInputStream inputStream = this.manager.getInStream("StringTable/" + this.fieldName);
			if (inputStream != null) {
				loadFrom(inputStream);
				RAOutputStream outputStream = this.manager.getOutStream("StringTable/" + this.fieldName);
				outputStream.seek(outputStream.length());
				this.dataOutputStream = new DataOutputStream(outputStream);
			}
		} catch (DataException e) {
		} catch (IOException e) {
		}

	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public int getIndex(String str) throws IOException, DataException {
		if (str == null)
			return -1;
		Integer index = this.stringIndexMap.get(str);
		if (index == null) {
			this.stringIndexMap.put(str, this.currentIndex);
			this.currentIndex++;
			this.stringList.add(str);
			if (dataOutputStream != null) {
				this.dataOutputStream.writeUTF(str);
			} else if (this.manager != null) {
				this.dataOutputStream = new DataOutputStream(
						this.manager.getOutStream("StringTable/" + this.fieldName));
				this.dataOutputStream.writeUTF(str);
			}
			return this.currentIndex - 1;
		} else {
			return index;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getStringValue(int index) {
		if (index < 0 || index > this.stringList.size())
			return null;
		return this.stringList.get(index);
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (this.dataOutputStream != null) {
			this.dataOutputStream.close();
			this.dataOutputStream = null;
		}
	}
}
