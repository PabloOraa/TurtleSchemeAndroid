package com.example.turtlescheme.Multimedia;

import java.io.Serializable;
import java.util.List;

public class MultimediaSerializable implements Serializable
{
    private List<Multimedia> multimediaList;

    public MultimediaSerializable(List<Multimedia> media)
    {
        multimediaList = media;
    }

    public List<Multimedia> getMultimediaList()
    {
        return multimediaList;
    }

    public void setMultimediaList(List<Multimedia> multimediaList)
    {
        this.multimediaList = multimediaList;
    }
}
