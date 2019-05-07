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
        int byteLength = (int)Math.ceil(((double)record.getIDLength()) / 4);
        byte[] bytes = new byte[byteLength];
        binFile.seek(record.getIDOffset());
        binFile.read(bytes);
        return bytes;
    }


    public byte[] getSequence(DNARecord record) throws IOException {
        int byteLength = (int)Math.ceil(((double)record.getSeqLength()) / 4);
        byte[] bytes = new byte[byteLength];
        binFile.seek(record.getSeqOffset());
        binFile.read(bytes);
        return bytes;
    }


    public DNARecord addToMem(String seqID, String seq) throws IOException {
        int idL = seqID.length();
        int sL = seq.length();
        int idSize = (int)Math.ceil(((double)seqID.length()) / 4);
        int seqSize = (int)Math.ceil(((double)seq.length()) / 4);
        int idO = 0;
        int sO = 0;
        boolean added = false;
        byte[] idBytes = DNAtoBinary(seqID);
        for (FreeBlock block : freeBlocks) {
            if (idSize <= block.getSize()) {
                writeToBlock(block, seqID);
                idO = block.getOffset();
                binFile.seek(idO);
                binFile.write(idBytes);
                binSize += idBytes.length;
                added = true;
                break;
            }
        }
        if (!added) {
            idO = (int)binFile.length();
            binFile.seek(binFile.length());
            binFile.write(idBytes);
            binSize += idBytes.length;
        }
        Collections.sort(freeBlocks);
        added = false;
        byte[] seqBytes = DNAtoBinary(seq);
        for (FreeBlock block : freeBlocks) {
            if (seqSize <= block.getSize()) {
                writeToBlock(block, seq);
                sO = block.getOffset();
                binFile.seek(sO);
                binFile.write(seqBytes);
                binSize += seqBytes.length;
                added = true;
                break;
            }
        }
        if (!added) {
            sO = (int)binFile.length();
            binFile.seek(binFile.length());
            binFile.write(seqBytes);
            binSize += seqBytes.length;
        }
        Collections.sort(freeBlocks);
        return new DNARecord(idO, idL, sO, sL);
    }


    private void writeToBlock(FreeBlock block, String str) {
        int size = (int)Math.ceil(((double)str.length()) / 4);
        if (block.getSize() == size) {
            freeBlocks.remove(block);
        }
        else {
            int off = block.getOffset() + size;
            int newSize = block.getSize() - size;
            freeBlocks.remove(block);
            freeBlocks.add(new FreeBlock(off, newSize));
            Collections.sort(freeBlocks);
        }
    }


    public byte[] remove(DNARecord record, String seqID) throws IOException {
        int idSize = (int)Math.ceil(((double)record.getIDLength()) / 4);
        int seqSize = (int)Math.ceil(((double)record.getSeqLength()) / 4);
        byte[] sequence = new byte[seqSize];
        binFile.seek(record.getSeqOffset());
        binFile.read(sequence);
        binSize -= (idSize + seqSize);
        duplicateList.remove(seqID);
        FreeBlock idBlock = new FreeBlock(record.getIDOffset(), idSize);
        FreeBlock seqBlock = new FreeBlock(record.getSeqOffset(), seqSize);
        if (!freeBlocks.isEmpty()) {
            FreeBlock idLeft = freeBlocks.getFirst();
            FreeBlock idRight = freeBlocks.getLast();
            FreeBlock seqLeft = freeBlocks.getFirst();
            FreeBlock seqRight = freeBlocks.getLast();
            for (int i = 0; i < freeBlocks.size(); i++) {
                if (freeBlocks.get(i).compareTo(idBlock) < 0) {
                    idLeft = freeBlocks.get(i);
                }
                if (freeBlocks.get(i).compareTo(seqBlock) < 0) {
                    seqLeft = freeBlocks.get(i);
                }
            }
            for (int j = freeBlocks.size() - 1; j > 0; j--) {
                if (freeBlocks.get(j).compareTo(idBlock) > 0) {
                    idRight = freeBlocks.get(j);
                }
                if (freeBlocks.get(j).compareTo(seqBlock) > 0) {
                    seqRight = freeBlocks.get(j);
                }
            }
            int idLeftEnd = idLeft.getEnd();
            int idEnd = idBlock.getEnd();
            int seqLeftEnd = seqLeft.getEnd();
            int seqEnd = seqBlock.getEnd();
            if (idLeftEnd == idBlock.getOffset()) {
                idBlock = combine(idLeft, idBlock);
            }
            else {
                freeBlocks.add(idBlock);
            }
            if (idEnd == seqBlock.getOffset()) {
                idBlock = combine(idBlock, seqBlock);
                if (seqEnd == seqRight.getOffset()) {
                    idBlock = combine(idBlock, seqRight);
                    freeBlocks.remove(seqRight);
                }
            }
            else {
                if (idEnd == idRight.getOffset()) {
                    idBlock = combine(idBlock, idRight);
                    freeBlocks.remove(idRight);
                }
                if (seqLeftEnd == seqBlock.getOffset()) {
                    seqBlock = combine(seqLeft, seqBlock);
                }
                else {
                    freeBlocks.add(seqBlock);
                }
                if (seqEnd == seqRight.getOffset()) {
                    seqBlock = combine(seqBlock, seqRight);
                    freeBlocks.remove(seqRight);
                }
            }
            /*
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
            left = freeBlocks.getFirst();
            right = freeBlocks.getLast();
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
            if (leftEnd == seqBlock.getOffset() && seqEnd == right
                .getOffset()) {
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
            */
        }
        else {
            freeBlocks.add(idBlock);
            int idEnd = idBlock.getOffset() + idBlock.getSize();
            if (idEnd == seqBlock.getOffset()) {
                idBlock = combine(idBlock, seqBlock);
            }
        }
        int idEnd = idBlock.getEnd();
        int seqEnd = seqBlock.getEnd();
        if (idEnd == (int)binFile.length()) {
            binFile.setLength(binFile.length() - idBlock.getSize());
            freeBlocks.remove(idBlock);
        }
        else if (seqEnd == (int)binFile.length()) {
            binFile.setLength(binFile.length() - seqBlock.getSize());
            freeBlocks.remove(seqBlock);
        }
        Collections.sort(freeBlocks);
        return sequence;
    }


    public FreeBlock combine(FreeBlock left, FreeBlock right) {
        freeBlocks.remove(left);
        FreeBlock newBlock = new FreeBlock(left.getOffset(), left.getSize()
            + right.getSize());
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
                // int parsed = Integer.parseInt(build.toString(), 2);
                int b = Integer.parseInt(build.toString(), 2);
                bytes[(i / 4) - 1] = (byte)b;
                build = new StringBuilder();
            }
        }
        while (i % 4 != 0) {
            build.append("00");
            i++;
        }
        if (!build.toString().isEmpty()) {
            int b = Integer.parseInt(build.toString(), 2);
            bytes[(i / 4) - 1] = (byte)b;
        }
        return bytes;
    }


    public LinkedList<String> getDuplicates() {
        return duplicateList;
    }


    public LinkedList<FreeBlock> getFreeBlocks() {
        return freeBlocks;
    }
    
    public void clear() throws IOException {
        binFile.setLength(0);
        duplicateList.clear();
        freeBlocks.clear();
    }
}
