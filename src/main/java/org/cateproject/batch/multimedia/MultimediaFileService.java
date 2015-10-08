package org.cateproject.batch.multimedia;

import java.io.File;

import org.cateproject.domain.Multimedia;

public class MultimediaFileService {

    public Multimedia extractFileInfo(File file, Multimedia multimedia) {
        return multimedia;
    }

    public boolean filesUnchanged(Multimedia m1, Multimedia m2) {
        return false;
    }

    public void copyFileInfo(Multimedia from, Multimedia to) {
        
    }
}
