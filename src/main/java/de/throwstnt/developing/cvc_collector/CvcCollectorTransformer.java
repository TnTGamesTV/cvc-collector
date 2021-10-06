package de.throwstnt.developing.cvc_collector;

import net.labymod.addon.AddonTransformer;
import net.labymod.api.TransformerType;

public class CvcCollectorTransformer extends AddonTransformer {

  @Override
  public void registerTransformers() {
    this.registerTransformer(TransformerType.VANILLA, "main.mixin.json");
  }
}
