package prrandomthings.constants;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleCubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import net.minecraft.util.EnumFacing;

public class RTTextures {
    public static final ICubeRenderer WOODEN_PLANKS = new SimpleCubeRenderer("minecraft:blocks/planks_oak");
    public static final ICubeRenderer STONE_BRICKS=new SimpleCubeRenderer("minecraft:blocks/stonebrick");
    public static final ICubeRenderer BRICKS=new SimpleCubeRenderer("minecraft:blocks/brick");
    public static final ICubeRenderer DIRT = new SimpleCubeRenderer("minecraft:blocks/dirt");

    public static final ICubeRenderer ITEM_COLLECTOR_OVERLAY=new SimpleOverlayRenderer("overlay/machine/overlay_blower"){
        @Override
        public void renderOrientedState(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, EnumFacing frontFacing, boolean isActive, boolean isWorkingEnabled) {
            //super.renderOrientedState(renderState, translation, pipeline, frontFacing, isActive, isWorkingEnabled);
            (isActive? Textures.BLOWER_ACTIVE_OVERLAY:Textures.BLOWER_OVERLAY).renderOrientedState(renderState,translation,pipeline,EnumFacing.UP,
                    isActive,isWorkingEnabled);
        }
    };
}