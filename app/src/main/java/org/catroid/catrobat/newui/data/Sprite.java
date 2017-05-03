package org.catroid.catrobat.newui.data;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sprite implements Serializable, Cloneable {

    public static final String TAG = Sprite.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    //TODO: uncomment after XStream integration
    //@XStreamAsAttribute
    private String name;

    private List<LookInfo> mLookList = new ArrayList<>();
    private List<SoundInfo> mSoundList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<LookInfo> getLookList() {
        return mLookList;
    }

    public List<SoundInfo> getSoundList() {
        return mSoundList;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sprite)) {
            return false;
        }

        if (object == this) {
            return true;
        }

        Sprite sprite = (Sprite) object;

        //TODO: check when a sprite is equal to another.
        return this.name.equals(sprite.name);
    }

    public Bitmap getThumbnail() {
        Bitmap thumbnail = null;

        if (mLookList.size() > 0) {
            thumbnail = mLookList.get(0).getThumbnail();
        }

        return thumbnail;
    }
}
