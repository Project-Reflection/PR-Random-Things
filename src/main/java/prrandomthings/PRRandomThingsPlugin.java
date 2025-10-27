package prrandomthings;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import twelvefold.twelvefoldbooter.api.TwelvefoldRegistryAPI;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class PRRandomThingsPlugin implements IFMLLoadingPlugin {

	public PRRandomThingsPlugin() {
		MixinBootstrap.init();
		//False for Vanilla/Coremod mixins, true for regular mod mixins
		TwelvefoldRegistryAPI.enqueueEarlyMixin("mixins.prrandomthings.early.json");
		//FermiumRegistryAPI.enqueueMixin(true, "mixins.prrandomthings.late.json", () -> Loader.isModLoaded("jei"));
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}