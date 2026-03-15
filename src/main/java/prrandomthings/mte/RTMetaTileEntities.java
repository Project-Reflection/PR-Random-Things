package prrandomthings.mte;

import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import prrandomthings.api.PRMultiblockAbility;
import prrandomthings.constants.RTConstants;
import prrandomthings.constants.RTTextures;
import prrandomthings.mte.multiblock.MteThermalMultiblock;
import prrandomthings.recipes.RTRecipeMaps;

@SuppressWarnings("deprecation")
public class RTMetaTileEntities {
    public static final MteCustomGenerator[] METALLURGIC_GENERATORS = new MteCustomGenerator[3];
    public static final MteCustomGenerator[] REACTANT_GENERATORS = new MteCustomGenerator[3];

    public static final MetaTileEntity COMPOSTING_BARREL = new MteCustomPrimitiveSingleblock(RTConstants.RTID("composting_barrel"),
            RTRecipeMaps.COMPOSTING_BARREL, RTTextures.WOODEN_PLANKS, RTTextures.ITEM_COLLECTOR_OVERLAY);

    public static final MetaTileEntity SIEVE = new MteCustomPrimitiveSingleblock(RTConstants.RTID("sieve"
    ), RTRecipeMaps.SIEVE, RTTextures.WOODEN_PLANKS, Textures.SIFTER_OVERLAY);
    public static final MetaTileEntity BRICK_BARREL = new MteCustomPrimitiveSingleblock(RTConstants.RTID("brick_barrel"),
            RTRecipeMaps.STONE_BARREL, RTTextures.BRICKS, RTTextures.ITEM_COLLECTOR_OVERLAY);



    public static final MetaTileEntity DIRT_FURNACE = new MteThermalMultiblock(RTConstants.RTID("dirt_furnace"),
            RTRecipeMaps.DIRT_FURNACE, RTTextures.DIRT, Textures.ALLOY_SMELTER_OVERLAY,
            selfPredicate -> FactoryBlockPattern.start().aisle("CCC", "CCC", " C ").aisle("CCC", "C#C", " C ").aisle("CDC", "C@C", " C ")
                    .where('C', MultiblockControllerBase.states(Blocks.DIRT.getDefaultState()))
                    .where('#', MultiblockControllerBase.air())
                    .where('@', selfPredicate)
                    .where('D', MultiblockControllerBase.abilities(PRMultiblockAbility.HEATER))
                    .build());

    public static final MetaTileEntity CRUCIBLE=new MteThermalMultiblock(RTConstants.RTID("crucible"),
            RTRecipeMaps.CRUCIBLE,RTTextures.BRICKS,Textures.EXTRACTOR_OVERLAY,
            selfPredicate->FactoryBlockPattern.start().aisle("D","@")
                    .where('D',MultiblockControllerBase.abilities(PRMultiblockAbility.HEATER))
                    .where('@',selfPredicate).build());

    static {
        for (int i = 0; i < METALLURGIC_GENERATORS.length; i++) {
            METALLURGIC_GENERATORS[i] = new MteCustomGenerator(new ResourceLocation(RTConstants.MODID,
                    String.format("metallurgic_generator_%s", GTValues.VN[i + 1].toLowerCase())),
                    RTRecipeMaps.METALLURGIC_FUELS, Textures.MULTIBLOCK_WORKABLE_OVERLAY, i + 1);
        }
        for (int i = 0; i < RTMetaTileEntities.REACTANT_GENERATORS.length; i++) {
            REACTANT_GENERATORS[i] = new MteCustomGenerator(
                    new ResourceLocation(RTConstants.MODID, String.format("reactant_generator_%s",
                            GTValues.VN[i + 1].toLowerCase())),
                    RTRecipeMaps.REACTANT_FUELS, Textures.MULTIBLOCK_WORKABLE_OVERLAY, i + 1);
        }

    }
}