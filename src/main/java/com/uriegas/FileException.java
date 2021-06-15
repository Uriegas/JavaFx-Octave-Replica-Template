package com.uriegas;
/**
 * Errors about files, as File not found and similar
 */
public class FileException extends Exception{
    public FileException(String m){
        super(m);
    }
}
