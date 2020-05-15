package com.guoxd.workframe.anim.study;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by guoxd on 2019/2/21.
 * 旋转效果的
 * from高德地图
 */

public class Rotate3dAnimation extends Animation{
    private final float sQ;
    private final float sR;
    private final float rU;
    private final float rV;
    private final float rW;
    private final boolean sS;
    private Camera sT;

    public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, boolean reverse) {
        this.sQ = fromDegrees;
        this.sR = toDegrees;
        this.rU = centerX;
        this.rV = centerY;
        this.rW = depthZ;
        this.sS = reverse;
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.sT = new Camera();
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float fromDegrees = this.sQ;
        float degrees = fromDegrees + (this.sR - fromDegrees) * interpolatedTime;
        float centerX = this.rU;
        float centerY = this.rV;
        Camera camera = this.sT;
        Matrix matrix = t.getMatrix();
        camera.save();
        if (this.sS) {
            camera.translate(0.0F, 0.0F, this.rW * interpolatedTime);
        } else {
            camera.translate(0.0F, 0.0F, this.rW * (1.0F - interpolatedTime));
        }

        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
