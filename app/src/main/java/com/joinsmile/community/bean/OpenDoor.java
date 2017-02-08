package com.joinsmile.community.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgfcxx on 2017/2/4.
 */

public class OpenDoor extends BaseInfoVo {

    @SerializedName("CanBeOpenTheDoor")
    public boolean canBeOpenTheDoor;

    public boolean isCanBeOpenTheDoor() {
        return canBeOpenTheDoor;
    }

    public void setCanBeOpenTheDoor(boolean canBeOpenTheDoor) {
        this.canBeOpenTheDoor = canBeOpenTheDoor;
    }
}
