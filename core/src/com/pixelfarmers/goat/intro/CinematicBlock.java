package com.pixelfarmers.goat.intro;


import com.badlogic.gdx.graphics.Camera;

import java.util.List;

public class CinematicBlock {
    public interface CameraControl {
        void returnCameraControl();
    }

    private final List<Cinematic> cinematicList;
    private CameraControl cameraControl;
    private int currentCinematicIndex;

    public CinematicBlock(List<Cinematic> cinematicList, CameraControl cameraControl) {
        this.cinematicList = cinematicList;
        this.cameraControl = cameraControl;
        this.currentCinematicIndex = 0;
    }

    public void update(float delta, Camera camera) {
        if (isFinished()) {
            return;
        }

        Cinematic currentCinematic = cinematicList.get(currentCinematicIndex);
        currentCinematic.update(delta, camera);

        if (currentCinematic.isFinished()) {
            currentCinematicIndex++;
            if (isFinished()) {
                cameraControl.returnCameraControl();
            }
        }
    }

    public boolean isFinished() {
        return currentCinematicIndex == cinematicList.size();
    }

    public void skipAll() {
        currentCinematicIndex = cinematicList.size();
        cameraControl.returnCameraControl();
    }
}
