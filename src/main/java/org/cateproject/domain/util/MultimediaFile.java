package org.cateproject.domain.util;

import java.io.File;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.MultimediaFileType;

public class MultimediaFile {

    private File file;

    private MultimediaFileType type;

    private Multimedia multimedia;

    public MultimediaFile(Multimedia multimedia, File file, MultimediaFileType type) {
        this.multimedia = multimedia;
        this.file = file;
        this.type = type;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setType(MultimediaFileType type) {
        this.type = type;
    }

    public MultimediaFileType getType() {
        return type;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public String toString() {
        return"static://" + multimedia.getType() +"/" + type + "/" + multimedia.getLocalFileName(); 
    }
}
