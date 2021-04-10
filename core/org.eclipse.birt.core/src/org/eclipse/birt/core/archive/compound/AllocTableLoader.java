/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.birt.core.archive.ArchiveUtil;

class AllocTableLoader implements ArchiveConstants {

	static class Node {

		AllocEntry entry;
		Node next;
	}

	protected Node nodes = new Node();
	protected AllocEntry fatEntry;

	AllocTableLoader() {
		fatEntry = new AllocEntry(ALLOC_TABLE_BLOCK);
		Node fatNode = new Node();
		fatNode.entry = fatEntry;
		nodes.next = fatNode;
	}

	ArrayList<AllocEntry> getEntryies() {
		ArrayList<AllocEntry> entries = new ArrayList<AllocEntry>();
		Node entryNode = nodes.next;
		while (entryNode != null) {
			entries.add(entryNode.entry);
			entryNode = entryNode.next;
		}
		return entries;
	}

	private void appendBlock(int blockIndex, int blockId) {
		Node prevNode = null;
		Node entryNode = nodes.next;
		do {
			if (entryNode.entry.getLastBlock() == blockIndex) {
				entryNode.entry.appendBlock(blockId);
				// move the node to the first element
				if (entryNode != nodes.next) {
					prevNode.next = entryNode.next;
					entryNode.next = nodes.next;
					nodes.next = entryNode;
				}
				return;
			}
			prevNode = entryNode;
			entryNode = entryNode.next;
		} while (entryNode != null);
		// create a new entry
		entryNode = new Node();
		entryNode.entry = new AllocEntry(blockIndex);
		entryNode.entry.appendBlock(blockId);
		entryNode.next = nodes.next;
		nodes.next = entryNode;
	}

	void load(ArchiveFileV2 af) throws IOException {
		int BLOCK_SIZE = af.BLOCK_SIZE;
		int INDEX_PER_BLOCK = BLOCK_SIZE / 4;
		// initialize the FAT entry

		// load the FAT block one by one
		byte buffer[] = new byte[BLOCK_SIZE];
		int readBlocks = 0;
		int blockIndex = 0;
		while (readBlocks < fatEntry.getTotalBlocks()) {
			int fatBlockId = fatEntry.getBlock(readBlocks);
			af.read(fatBlockId, 0, buffer, 0, BLOCK_SIZE);
			for (int i = 0; i < INDEX_PER_BLOCK; i++) {
				int blockId = ArchiveUtil.bytesToInteger(buffer, i * 4);
				// -1 means the last block while 0 means unused block.
				if (blockId > 0) {
					appendBlock(blockIndex, blockId);
				}
				blockIndex++;
			}
			readBlocks++;
		}

		// merge the forward reference links
		merge(nodes);
	}

	void merge(Node nodes) {
		// the node to be merged
		Node entryNode = nodes.next;
		// the node before the node to be merged
		Node prevNode = nodes;

		// node compared
		while (entryNode != null) {
			boolean hasMerged = false;
			Node compareNode = nodes.next;
			int blockIndex = entryNode.entry.getFirstBlock();
			while (compareNode != null) {
				if (compareNode == entryNode) {
					compareNode = compareNode.next;
					continue;
				}

				if (compareNode.entry.getLastBlock() == blockIndex) {
					// merge nodes.
					for (int i = 1; i < entryNode.entry.getTotalBlocks(); i++) {
						int blockId = entryNode.entry.getBlock(i);
						compareNode.entry.appendBlock(blockId);
					}

					// remove the node which has been merged.
					prevNode.next = entryNode.next;
					hasMerged = true;
					break;
				}
				compareNode = compareNode.next;
			}
			// If the node has been merged, the preNode needn't been changed
			// since we change the current node to it's next which will be
			// merged.
			// Otherwise, it means the node needn't be merged, so current node
			// will be set to its next and the preNode be itself.
			if (!hasMerged) {
				prevNode = entryNode;
			}
			entryNode = entryNode.next;
		}
	}
}
