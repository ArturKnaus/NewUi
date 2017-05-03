package org.catroid.catrobat.newui.data;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectItem implements Serializable {

    private static final transient int THUMBNAIL_SIZE = 200;
    private int id;
    private String infoText;
    private List<Sprite> mSpriteList = new ArrayList<>();
    private transient Bitmap thumbnail;

    public ProjectItem(int id, Bitmap thumbnail, String infoText) {
        this.thumbnail = thumbnail;
        this.infoText = infoText;
        this.id = id;
    }

    public List<Sprite> getSpriteList() {
        return mSpriteList;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void addSprite(Sprite sprite) {
        if (sprite != null) {
            mSpriteList.add(sprite);
        }
    }
}
