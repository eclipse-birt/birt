
package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.compound.AllocTableLoader.Node;

public class AllocTableLoaderTest extends TestCase {

	static String SOURCE_FILE = "org/eclipse/birt/core/archive/compound/sourceNodes.txt";
	static String TARGET_FILE = "org/eclipse/birt/core/archive/compound/targetNodes.txt";

	static String SOURCE = "./utest/sourceNodes.txt";
	static String TARGET = "./utest/targetNodes.txt";

	public void copyResource(String src, String tgt) {
		File parent = new File(tgt).getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			FileOutputStream fos = new FileOutputStream(tgt);
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ((readCount = in.read(fileData)) != -1) {
				fos.write(fileData, 0, readCount);
			}
			fos.close();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	// test: merge the nodes which need be merged.
	public void testMergeN() throws Exception {
		copyResource(SOURCE_FILE, SOURCE);
		copyResource(TARGET_FILE, TARGET);
		AllocTableLoader.Node nodes = loadNodes(SOURCE);
		AllocTableLoader.Node tagetNodes = loadNodes(TARGET);

		AllocTableLoader loader = new AllocTableLoader();
		loader.merge(nodes);
		compareNodes(nodes, tagetNodes);
	}

	// test: merge the nodes which is right.
	public void testMergeR() throws Exception {
		copyResource(TARGET_FILE, SOURCE);
		copyResource(TARGET_FILE, TARGET);
		AllocTableLoader.Node nodes = loadNodes(SOURCE);
		AllocTableLoader.Node tagetNodes = loadNodes(TARGET);

		AllocTableLoader loader = new AllocTableLoader();
		loader.merge(nodes);
		compareNodes(nodes, tagetNodes);
	}

	void compareNodes(AllocTableLoader.Node nodes, AllocTableLoader.Node tagetNodes) {
		AllocTableLoader.Node entryNode = nodes;
		AllocTableLoader.Node compareNodes = tagetNodes;
		while (entryNode != null) {
			assert compareNodes == null;
			AllocEntry entry = entryNode.entry;
			AllocEntry compareEntry = compareNodes.entry;
			if (entry != null) {
				assert compareEntry == null;
				assert compareEntry.getTotalBlocks() == entry.getTotalBlocks();
				for (int i = 0; i < entry.getTotalBlocks(); i++) {
					assert compareEntry.getBlock(i) == entry.getBlock(i);
					System.out.print("  " + entry.getBlock(i));
				}
			}
			entryNode = entryNode.next;
			compareNodes = compareNodes.next;
		}
		System.out.println();
		assert compareNodes == null;
	}

	AllocTableLoader.Node loadNodes(String fileName) throws Exception {
		File file = new File(fileName);
		if (file.exists()) {
			RandomAccessFile fileReader = new RandomAccessFile(file, "r");
			String line = null;
			line = fileReader.readLine();
			Node nodes = new Node();
			Node node = nodes;
			while (line != null) {
				String[] values = line.split(",");
				if (values.length > 0) {
					AllocEntry entry = new AllocEntry(Integer.parseInt(values[0]));
					System.out.print(values[0] + ",");
					for (int i = 1; i < values.length; i++) {
						entry.appendBlock(Integer.parseInt(values[i]));
						System.out.print(values[i] + ",");
					}
					Node newNode = new Node();
					newNode.entry = entry;
					node.next = newNode;
					node = newNode;
				}
				System.out.println();
				line = fileReader.readLine();
			}
			fileReader.close();
			return nodes;
		}
		return null;
	}

}
