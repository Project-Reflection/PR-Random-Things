package prrandomthings.materials;

import gregtech.api.unification.Elements;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.ToolProperty;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import prrandomthings.constants.RTConstants;
import prrandomthings.config.RTConfig;

public class RTMaterials {
    //Element materials
    public static Material SPACE;
    public static Material MAGIC;
    //First degree materials
    public static Material SLAG;
    public static Material FERROUS_SLAG;
    public static Material CUPROUS_SLAG;
    public static Material UNINSPECTED;
    //Botania
    public static Material MANASTEEEL;
    public static Material MANA_PEARL;
    public static Material MANA_DIAMOND;
    //End Botania
    public static Material SILICON_BRONZE;
    //Ender IO
    public static Material ELECTRICAL_STEEL;

    //Second degree materials
    public static Material TERRASTEEL;
    public static Material ELVEN_ELEMENTIUM;
    public static void register() {
        int id= RTConfig.startMaterialID;
        SPACE=new Material.Builder(id++,RTConstants.RTID("space"))
                .element(Elements.Sp)
                .build();
        MAGIC=new Material.Builder(id++,RTConstants.RTID("magic"))
                .element(Elements.Ma)
                .build();
        //First degree materials
        FERROUS_SLAG=new Material.Builder(id++,RTConstants.RTID("ferrous_slag"))
                .colorAverage()
                .components(Materials.Iron,1,Materials.Silicon,1,Materials.Oxygen,3)
                .gem()
                .iconSet(MaterialIconSet.FLINT)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .build();
        CUPROUS_SLAG=new Material.Builder(id++,RTConstants.RTID("cuprous_slag"))
                .colorAverage()
                .components(Materials.Copper,2,Materials.Silicon,1,Materials.Oxygen,3)
                .gem()
                .iconSet(MaterialIconSet.FLINT)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .build();
        SLAG=new Material.Builder(id++,RTConstants.RTID("slag"))
                .colorAverage()
                .components(FERROUS_SLAG,1,CUPROUS_SLAG,1,Materials.Grossular,1,Materials.Ash,1)
                .gem()
                .iconSet(MaterialIconSet.FLINT)
                .flags(MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING)
                .build();
        UNINSPECTED=new Material.Builder(id++,RTConstants.RTID("uninspected"))
                .color(0x808080)
                .gem(2)
                .iconSet(MaterialIconSet.DIAMOND)
                .ore()
                .flags(MaterialFlags.NO_SMELTING,MaterialFlags.MORTAR_GRINDABLE,MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_ROD,MaterialFlags.GENERATE_BOLT_SCREW)
                .toolStats(new ToolProperty(8f,6f,256,3))
                .build();
        //Botania
        MANASTEEEL=new Material.Builder(id++,RTConstants.RTID("manasteel"))
                .color(0x6666ff)
                .components(new MaterialStack(Materials.Steel,1),new MaterialStack(MAGIC,1))
                .ingot()
                .iconSet(MaterialIconSet.SHINY)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION,MaterialFlags.GENERATE_PLATE,MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_BOLT_SCREW)
                .toolStats(ToolProperty.Builder.of(6.2f,2f,300,3)
                        .enchantability(20).build())
                .build();
        MANA_PEARL=new Material.Builder(id++,RTConstants.RTID("mana_pearl"))
                .color(0x66ccff)
                .components(new MaterialStack(Materials.EnderPearl,1),new MaterialStack(MAGIC,1))
                .gem()
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .build();
        MANA_DIAMOND=new Material.Builder(id++,RTConstants.RTID("mana_diamond"))
                .color(0x66ccff)
                .components(new MaterialStack(Materials.Diamond,1),new MaterialStack(MAGIC,1))
                .iconSet(MaterialIconSet.DIAMOND)
                .gem()
                .flags(MaterialFlags.DISABLE_DECOMPOSITION,MaterialFlags.GENERATE_PLATE,MaterialFlags.GENERATE_LENS)
                .build();
        //End Botania
        SILICON_BRONZE=new Material.Builder(id++,RTConstants.RTID("silicon_bronze"))
                .components(Materials.Copper,1,Materials.Silicon,1)
                .colorAverage()
                .ingot()
                .flags(MaterialFlags.GENERATE_PLATE)
                .build();
        //EnderIO
        ELECTRICAL_STEEL =new Material.Builder(id++,RTConstants.RTID("electrical_steel"))
                .components(Materials.Steel,1,Materials.Silicon,1)
                .ingot()
                .colorAverage()
                .flags(MaterialFlags.GENERATE_PLATE)
                .build();
        //End EnderIO
        //Second degree materials
        TERRASTEEL=new Material.Builder(id++,RTConstants.RTID("terrasteel"))
                .ingot()
                .color(0xccff66)
                .components(
                        new MaterialStack(MANASTEEEL,1)
                        ,new MaterialStack(MANA_PEARL,1)
                        ,new MaterialStack(MANA_DIAMOND,1)
                        ,new MaterialStack(MAGIC,1))
                .iconSet(MaterialIconSet.SHINY)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION,MaterialFlags.GENERATE_PLATE,MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_BOLT_SCREW)
                .toolStats(ToolProperty.Builder.of(9f,3f,2300,4)
                        .enchantability(26)
                        .build())
                .build();
        ELVEN_ELEMENTIUM=new Material.Builder(id++,RTConstants.RTID("elven_elementium"))
                .ingot()
                .color(0xff66cc)
                .components(MANASTEEEL,2,MAGIC,1)
                .iconSet(MaterialIconSet.SHINY)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION,MaterialFlags.GENERATE_PLATE,MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_BOLT_SCREW)
                .toolStats(ToolProperty.Builder.of(6.2f,2f,720,3)
                        .enchantability(20).build())
                .build();

        //dealIntegration();
    }
    public static void postRegister()
    {
        if(RTConfig.enableExtraFlintTools) {
            Materials.Flint.addFlags(MaterialFlags.GENERATE_BOLT_SCREW, MaterialFlags.GENERATE_ROD);
            Materials.Flint.getProperty(PropertyKey.TOOL)
                    .setShouldIgnoreCraftingTools(false);
        }
        if(Loader.isModLoaded("botania")){
            ignore(OrePrefix.ingot,MANASTEEEL,TERRASTEEL,ELVEN_ELEMENTIUM);
            ignore(OrePrefix.nugget,MANASTEEEL,TERRASTEEL,ELVEN_ELEMENTIUM);
            ignore(OrePrefix.gem,MANA_PEARL,MANA_DIAMOND);
            ignore(OrePrefix.block,MANASTEEEL,TERRASTEEL,ELVEN_ELEMENTIUM,MANA_DIAMOND);
        }
        if(Loader.isModLoaded("enderio")){
            ignore(OrePrefix.nugget,ELECTRICAL_STEEL);
            ignore(OrePrefix.ingot,ELECTRICAL_STEEL);
            ignore(OrePrefix.block,ELECTRICAL_STEEL);
        }
    }
    public static void onMaterialInfo()
    {
        if(RTConstants.Environment.botaniaLoaded)
        {

            Item manaResource=Item.getByNameOrId("botania:manaresource");
            assert manaResource != null;
            OreDictUnifier.registerOre(new ItemStack(manaResource,1,2),
                    OrePrefix.gem,MANA_DIAMOND);
            OreDictUnifier.registerOre(new ItemStack(manaResource,1,1),
                    OrePrefix.gem,MANA_PEARL);
            Item storageBlock=Item.getByNameOrId("botania:storage");
            assert storageBlock !=null;
            OreDictUnifier.registerOre(new ItemStack(storageBlock,1,3),
                    OrePrefix.block,MANA_DIAMOND);
        }
    }
    private static void ignore(OrePrefix prefix,Material... materials)
    {
        for(var mat:materials)
        {
            prefix.setIgnored(mat);
        }
    }
}
