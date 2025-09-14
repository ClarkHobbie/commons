package com.ltsllc.commons.io;

import com.ltsllc.commons.LtsllcException;

import java.io.*;
import java.net.URI;

/**
 * A {@link File} with some useful methods.
 */
public class ImprovedFile extends File {
    public ImprovedFile (File file) {
        super(String.valueOf(file));
    }

    public ImprovedFile (String parent,String child) {
        super(parent, child);
    }

    public ImprovedFile (String pathname) {
        super(pathname);
    }

    public ImprovedFile (URI uri) {
        super(uri);
    }

    public static int BUFFER_SIZE = 8192;

    /**
     * Create an ImprovedFile (File.createTempFile returns File).
     *
     * @param prefix       The prefix for the new temp file.
     * @return             The new temp file as an ImprovedFile.
     * @throws IOException File.createTempFile throws this
     * @see    File#createTempFile
     */
    public static ImprovedFile createImprovedTempFile (String prefix) throws IOException {
        ImprovedFile temp = new ImprovedFile(File.createTempFile(prefix,""));
        return temp;
    }

    /**
     * Are two files content equivalent?
     * @param other The other file to compare with.
     * @return Whether the two files are equivalent.
     * @throws IOException If an error is encountered with a file.
     */
    public boolean contentsEquals (ImprovedFile other) throws IOException {
        FileInputStream fileInputStream1 = new FileInputStream(this);
        FileInputStream fileInputStream2 = new FileInputStream(other);

        try {
            byte[] buffer1 = new byte[BUFFER_SIZE];
            byte[] buffer2 = new byte[BUFFER_SIZE];

            int bytesRead1 = fileInputStream1.read(buffer1);
            int bytesRead2 = fileInputStream2.read(buffer2);

            for (bytesRead1 = fileInputStream1.read(buffer1); bytesRead1 != -1; bytesRead1 = fileInputStream1.read(buffer1)) {
                bytesRead2 = fileInputStream2.read(buffer2);

                if (bytesRead1 != bytesRead2) {
                    return false;
                }

                for (int index = 0; index < bytesRead1; index++) {
                    if (buffer1[index] != buffer2[index]) {
                        return false;
                    }
                }
            }

            return true;
        } finally {
            fileInputStream1.close();
            fileInputStream2.close();
        }
    }

   /**
     * Copy the file to another file that the method creates
     *
     * @param newFile The file to copy to
     */
    public void copyTo(ImprovedFile newFile) throws LtsllcException {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;

        try {
            fileInputStream = new FileInputStream(this);
            fileOutputStream = new FileOutputStream(newFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new LtsllcException("error opening file or newFile", fileNotFoundException);
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        try {
            for (bytesRead = fileInputStream.read(buffer); bytesRead > 0; bytesRead = fileInputStream.read(buffer)) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException ioException) {
            throw new LtsllcException("exception reading from file or writing to file", ioException);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                    fileInputStream = null;
                }
            }
            catch (IOException ioException) {
                throw new LtsllcException("exception trying to close input file", ioException);
            }
            try {

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
            } catch (IOException ioException) {
                throw new LtsllcException("exception trying to close newFile", ioException);
            }
        }
    }

    /**
     * Change the file's time of last modification
     * <p>
     * This method creates a file if not present, and updates its time of last write if the file does exist.
     *
     * @throws IOException If there is a problem with the file
     */
    public void touch () throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(this,true);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    /**
     * Copy the file to one with the name equal to ".temp" appended to it
     *
     * @return The new file.
     * @throws LtsllcException If there is a problem copying the file.
     */
    public ImprovedFile copy () throws LtsllcException {
        String newFileName = this.toString() + ".temp";
        ImprovedFile newFile = new ImprovedFile(newFileName);

        copyTo(newFile);

        return newFile;
    }

    /**
     * Attempt to back up a file
     * <P>
     *     This method is just shorthand for renaming a file to another with a specified suffix.  The method first
     *     checks to see if this file already exists, and, if it does then it throws an exception.
     * </P>
     * <P>
     *     The backup file is just the original file with the suffix appended to it.
     * </P>
     * @param suffix The suffix for the backup file.
     * @return The new file
     * @throws LtsllcException This is thrown if the backup file already exists.
     */
    public ImprovedFile backup (String suffix) throws LtsllcException {
        ImprovedFile backup = new ImprovedFile(getName() + suffix);
        if (backup.exists()) {
            throw new LtsllcException("file exists " + backup);
        }

        renameTo(backup);

        return backup;
    }

    public void backup (ImprovedFile backup) throws LtsllcException {
        if (backup.exists()) {
            backup.delete();
        }

        copyTo(backup);
    }

    public void clear () {
        ImprovedFile backup = new ImprovedFile(getName() + ".backup");
        if (backup.exists()) {
            backup.delete();
        }

        renameTo(backup);

        try {
            touch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
