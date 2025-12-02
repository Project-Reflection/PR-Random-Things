package prrandomthings.mte;

import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import prrandomthings.constants.RTConstants;
import prrandomthings.constants.RTTextures;
import prrandomthings.recipes.RTRecipeMaps;

public class RTMetaTileEntities {
    public static final MteCustomGenerator[] METALLURGIC_GENERATORS = new MteCustomGenerator[3];
    public static final MteCustomGenerator[] REACTANT_GENERATORS = new MteCustomGenerator[3];
    public static final MteCustomPrimitiveMultiblock COMPOSTING_BARREL =new MteCustomPrimitiveMultiblock(RTConstants.RTID("composting_barrel"),
            RTRecipeMaps.COMPOSTING_BARREL, RTTextures.WOODEN_PLANKS,Textures.FERMENTER_OVERLAY,
            selfPredicate-> FactoryBlockPattern.start()
            .aisle("CCC","CCC","CCC","CCC")
            .aisle("CCC","C#C","C#C","C#C")
            .aisle("CCC","C@C","CCC","CCC")
            .where('C', MultiblockControllerBase.states(Blocks.PLANKS.getDefaultState()))
            .where('#', MultiblockControllerBase.air())
            .where('@',selfPredicate)
            .build());
    public static final MteCustomPrimitiveMultiblock SIEVE= new MteCustomPrimitiveMultiblock(RTConstants.RTID("sieve"
    ), RTRecipeMaps.SIEVE, RTTextures.WOODEN_PLANKS, Textures.SIFTER_OVERLAY, selfPredicate->FactoryBlockPattern.start()
            .aisle("C   C","C   C","CCCCC","CCCCC","CCCCC")
            .aisle("     ","     ","CSSSC","C###C","C###C")
            .aisle("     ","     ","CSSSC","C###C","C###C")
            .aisle("     ","     ","CSSSC","C###C","C###C")
            .aisle("C   C","C   C","CC@CC","CCCCC","CCCCC")
            .where('C', MultiblockControllerBase.states(Blocks.PLANKS.getDefaultState()))
            .where('S', MultiblockControllerBase.states(Blocks.WOODEN_SLAB.getDefaultState()))
            .where(' ', MultiblockControllerBase.any())
            .where('#', MultiblockControllerBase.air())
            .where('@',selfPredicate)
            .build());
    public static final MetaTileEntity BRICK_BARREL =new MteCustomPrimitiveMultiblock(RTConstants.RTID("brick_barrel"),
            RTRecipeMaps.STONE_BARREL,RTTextures.BRICKS,Textures.EXTRACTOR_OVERLAY,
            selfPredicate-> FactoryBlockPattern.start()
                    .aisle("CCC","CCC","CCC","CCC")
                    .aisle("CCC","C#C","C#C","C#C")
                    .aisle("CCC","C@C","CCC","CCC")
                    .where('C', MultiblockControllerBase.states(Blocks.BRICK_BLOCK.getDefaultState()))
                    .where('#', MultiblockControllerBase.air())
                    .where('@',selfPredicate)
                    .build());
    static {
        for (int i = 0; i < METALLURGIC_GENERATORS.length; i++) {
            METALLURGIC_GENERATORS[i]=new MteCustomGenerator(new ResourceLocation(RTConstants.MODID,
                    String.format("metallurgic_generator_%s", GTValues.VN[i+1].toLowerCase())),
                    RTRecipeMaps.METALLURGIC_FUELS, Textures.MULTIBLOCK_WORKABLE_OVERLAY,i+1);
        }
        for (int i = 0; i < RTMetaTileEntities.REACTANT_GENERATORS.length; i++) {
            REACTANT_GENERATORS[i]=new MteCustomGenerator(
                    new ResourceLocation(RTConstants.MODID,String.format("reactant_generator_%s",
                            GTValues.VN[i+1].toLowerCase())),
                    RTRecipeMaps.REACTANT_FUELS, Textures.MULTIBLOCK_WORKABLE_OVERLAY,i+1);
        }

    }
}