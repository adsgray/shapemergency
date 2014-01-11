package com.adsg0186.shapemergency.testgame1.blobs;

import com.github.adsgray.gdxtry1.engine.accel.AccelIF;
import com.github.adsgray.gdxtry1.engine.blob.BaseTextBlob;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.Renderer.RenderConfigIF;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

public class FlashMessage extends BaseTextBlob {

    public FlashMessage(PositionIF posin, VelocityIF velin, AccelIF accel,
            Renderer gdx, RenderConfigIF rc) {
        super(posin, velin, accel, gdx, rc);
    }
   
}
