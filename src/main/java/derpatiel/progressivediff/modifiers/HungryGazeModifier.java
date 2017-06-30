package derpatiel.progressivediff.modifiers;

import derpatiel.progressivediff.DifficultyManager;
import derpatiel.progressivediff.DifficultyModifier;
import derpatiel.progressivediff.MobUpkeepController;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * Created by Jim on 4/30/2017.
 */
public class HungryGazeModifier extends DifficultyModifier {

    private static final String IDENTIFIER = "MOD_HUNGRY_GAZE";

    private int maxLevel;
    private int diffCostPerLevel;
    private double selectionWeight;

    public HungryGazeModifier(int maxLevel, int diffCostPerLevel, double selectionWeight){
        this.maxLevel = maxLevel;
        this.diffCostPerLevel = diffCostPerLevel;
        this.selectionWeight = selectionWeight;
    }

    @Override
    public int getMaxInstances() {
        return maxLevel;
    }

    @Override
    public void handleUpkeepEvent(int numChanges, EntityLiving entity) {
        if(entity.getAttackTarget() instanceof EntityPlayer && entity.canEntityBeSeen(entity.getAttackTarget())){
            EntityPlayer player = (EntityPlayer)entity.getAttackTarget();
            player.addPotionEffect(new PotionEffect(MobEffects.HUNGER,MobUpkeepController.POTION_EFFECT_LENGTH,numChanges,false,true));
        }
    }

    @Override
    public int costPerChange() {
        return diffCostPerLevel;
    }

    @Override
    public double getWeight() {
        return selectionWeight;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    public static void readConfig(Configuration config) {
        Property modifierEnabledProp = config.get(IDENTIFIER,
                "EnableHungryGazeModifier",true,"Enable the Hungry Gaze modifier.  This modifier adds the hunger potion effect to the target player of the mob, if the mob can see the player");
        boolean modifierEnabled = modifierEnabledProp.getBoolean();
        Property SpeedLevelMaxLevelProp = config.get(IDENTIFIER,
                "HungerModifierMaxLevel",3,"Maximum Hunger level added to the target player when this is triggered.");
        int maxLevel = SpeedLevelMaxLevelProp.getInt();
        Property difficultyCostPerSpeedLevelProp = config.get(IDENTIFIER,
                "DifficultyCostPerHungerLevel",10,"Cost of each level of Hunger applied to the target player.");
        int diffCostPerLevel = difficultyCostPerSpeedLevelProp.getInt();
        Property selectionWeightProp = config.get(IDENTIFIER,
                "ModifierWeight",1.0d,"Weight that affects how often this modifier is selected.");
        double selectionWeight = selectionWeightProp.getDouble();
        if(modifierEnabled && maxLevel>0 && diffCostPerLevel>0 && selectionWeight>0) {
            DifficultyManager.addDifficultyModifier(new HungryGazeModifier(maxLevel,diffCostPerLevel,selectionWeight));
        }


    }
}
