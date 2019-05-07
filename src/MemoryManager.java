import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Keeps track of free space in memory and handles requests to memory.
 * 
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 */

public class MemoryManager {
    // Variables...............................................................
    // private LinkedList
    private LinkedList<String> duplicateList;
    private LinkedList<FreeBlock> freeBlocks; // should this be int?
    private RandomAccessFile binFile;
    private int binSize;


    public MemoryManager(RandomAccessFile bin) {
        duplicateList = new LinkedList<String>();
        freeBlocks = new LinkedList<FreeBlock>();
        binFile = bin;
        binSize = 0;
    }


    public DNARecord insert(String sequenceID, String sequence)
        throws IOException {
        for (int i = 0; i < duplicateList.size(); i++) {
            if (sequenceID.equals(duplicateList.get(i))) {
                return null;
            }
        }
        duplicateList.add(sequenceID);

        DNARecord record = addToMem(sequenceID, sequence);
        return record;
    }


    public byte[] getID(DNARecord record) throws IOException {
        byte[] bytes = new byte[record.getIDLength() / 4];
        binFile.read(bytes, record.getIDOffset(), record.getIDLength() / 4);
        return bytes;
    }


    public DNARecord addToMem(String seqID, String seq) throws IOException {
        int idL = seqID.length();
        int sL = seq.length();
        int idO = 0;
        int sO = 0;
        boolean added = false;
        byte[] idBytes = DNAtoBinary(seqID);
        for (FreeBlock block : freeBlocks) {
            if (seqID.length() <= block.getSize()) {
                freeBlocks.remove(block);
                idO = block.getOffset();
                binFile.write(idBytes, idO, idBytes.length);
                binSize += idBytes.length;
                added = true;
            }
        }
        if (!added) {
            idO = (int)binFile.length();
            binFile.write(idBytes, (int)binFile.length(), idBytes.length);
            binSize += idBytes.length;
        }
        Collections.sort(freeBlocks);
        added = false;
        byte[] seqBytes = DNAtoBinary(seq);
        for (FreeBlock block : freeBlocks) {
            if (seq.length() <= block.getSize()) {
                freeBlocks.remove(block);
                sO = block.getOffset();
                binFile.write(seqBytes, idO, seqBytes.length);
                binSize += seqBytes.length;
                added = true;
            }
        }
        if (!added) {
            sO = (int)binFile.length();
            binFile.write(seqBytes, (int)binFile.length(), seqBytes.length);
            binSize += seqBytes.length;
        }
        Collections.sort(freeBlocks);
        return new DNARecord(idO, sO, idL, sL);
    }


    public String remove(DNARecord record) throws IOException {
        int idSize = (int)Math.ceil(((double)record.getIDLength()) / 4);
        int seqSize = (int)Math.ceil(((double)record.getSeqLength()) / 4);
        byte[] sequence = new byte[seqSize];
        binFile.read(sequence, record.getSeqOffset(), seqSize);
        binSize -= (idSize + seqSize);
        FreeBlock idBlock = new FreeBlock(record.getIDOffset(), idSize);
        FreeBlock seqBlock = new FreeBlock(record.getSeqOffset(), seqSize);
        if (!freeBlocks.isEmpty()) {
            FreeBlock left = null;
            FreeBlock right = null;
            for (int i = 0; i < freeBlocks.size(); i++) {
                if (freeBlocks.get(i).compareTo(idBlock) < 0) {
                    left = freeBlocks.get(i);
                }
            }
            for (int j = freeBlocks.size() - 1; j > 0; j--) {
                if (freeBlocks.get(j).compareTo(idBlock) > 0) {
                    right = freeBlocks.get(j);
                }
            }
            int leftEnd = left.getOffset() + left.getSize();
            int idEnd = idBlock.getOffset() + idBlock.getSize();
            if (leftEnd == idBlock.getOffset() && idEnd == right.getOffset()) {
                idBlock = combine(left, idBlock);
                idBlock = combine(idBlock, right);
            }
            else if (leftEnd == idBlock.getOffset()) {
                idBlock = combine(left, idBlock);
            }
            else if (idEnd == right.getOffset()) {
                idBlock = combine(idBlock, right);
            }
            else {
                freeBlocks.add(idBlock);
            }
            Collections.sort(freeBlocks);
            left = null;
            right = null;
            for (int i = 0; i < freeBlocks.size(); i++) {
                if (freeBlocks.get(i).compareTo(seqBlock) < 0) {
                    left = freeBlocks.get(i);
                }
            }
            for (int j = freeBlocks.size() - 1; j > 0; j--) {
                if (freeBlocks.get(j).compareTo(seqBlock) > 0) {
                    right = freeBlocks.get(j);
                }
            }
            leftEnd = left.getOffset() + left.getSize();
            int seqEnd = seqBlock.getOffset() + seqBlock.getSize();
            if (leftEnd == seqBlock.getOffset() && seqEnd == right.getOffset()) {
                seqBlock = combine(left, seqBlock);
                seqBlock = combine(seqBlock, right);
            }
            else if (leftEnd == seqBlock.getOffset()) {
                seqBlock = combine(left, seqBlock);
            }
            else if (seqEnd == right.getOffset()) {
                seqBlock = combine(idBlock, right);
            }
            else {
                freeBlocks.add(seqBlock);
            }
        }
        else {
            freeBlocks.add(idBlock);
            int idEnd = idBlock.getOffset() + idBlock.getSize();
            if (idEnd == seqBlock.getOffset()) {
                combine(idBlock, seqBlock);
            }
        }

        Collections.sort(freeBlocks);

        // IF removing from end, resize using setLength()
        return null;
    }
    
    public FreeBlock combine(FreeBlock left, FreeBlock right) {
        freeBlocks.remove(left);
        FreeBlock newBlock = new FreeBlock(left.getOffset(), left.getSize() + right.getSize());
        freeBlocks.add(newBlock);
        Collections.sort(freeBlocks);
        return newBlock;
    }


    public byte[] DNAtoBinary(String seq) {
        StringBuilder build = new StringBuilder();
        int arraySize = (int)Math.ceil(((double)seq.length()) / 4);
        byte[] bytes = new byte[arraySize];
        int i = 0;
        while (i < seq.length()) {
            char next = seq.charAt(i);
            switch (next) {
                case 'A':
                    build.append("00");
                    break;
                case 'C':
                    build.append("01");
                    break;
                case 'G':
                    build.append("10");
                    break;
                default:
                    build.append("11");
            }
            i++;
            if (i % 4 == 0) {
                bytes[i / 4] = Byte.parseByte(build.toString());
                build = new StringBuilder();
            }
        }
        while (i % 4 != 0) {
            build.append("00");
            i++;
        }
        bytes[i / 4] = Byte.parseByte(build.toString());
        return bytes;
    }
}
