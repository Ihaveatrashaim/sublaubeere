package net.superblaubeere27.clientbase.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.utils.Utils;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import org.lwjgl.input.Keyboard;

import java.util.Iterator;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright Â© 2017 | CCBlueX | All rights reserved.
 */
public class KillAura extends Module {

    private EntityLivingBase target;

    private NumberValue<Float> rangeValue = new NumberValue<>("Range", 4.6F, 1f, 6f);

    private BooleanValue Rotation = new BooleanValue("Rotation" ,false);
    private BooleanValue Mobs = new BooleanValue("Attack Mobs" ,false);
    
    private NumberValue<Integer> targetChangeDelay = new NumberValue<>("TargetChangeDelay", 100, 0, 1000);

    private TimeHelper cpsTimer = new TimeHelper();
    private TimeHelper targetChangeTimer = new TimeHelper();
    private float[] facing;

    public KillAura() {
        super("KillAura", "Attack entites around you!", ModuleCategory.COMBAT, true, false, Keyboard.KEY_R);
    }


    @EventTarget
    public void onMotionUpdate(MotionUpdateEvent event) {
        if(!getState())
            return;

        if(event.getEventType() != EventType.PRE) return;

        for(Iterator<Entity> entities = mc.theWorld.loadedEntityList.iterator(); entities.hasNext();) {
            Object theObject = entities.next();
            if (theObject instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) theObject;

               if(Mobs.getObject() && !(target instanceof EntityAnimal)) return;

                if (mc.thePlayer.getDistanceToEntity(target) <= rangeValue.getObject()) {
                    if (isValid(target) && mc.inGameHasFocus && mc.thePlayer.canEntityBeSeen(target)) {
                        if (cpsTimer.hasReached(1000 / 10)) {
                            this.target = target;
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, target);
                            cpsTimer.reset();
                        }
                        if(Rotation.getObject()) {
                        	float r[] = Utils.getNeededRotations(Utils.getRandomCenter(target.getEntityBoundingBox()));;
                            mc.thePlayer.rotationYawHead = r[0];
                            mc.thePlayer.renderYawOffset = r[1];
                            event.setPitch(r[1]);
                            event.setYaw(r[0]);
                        }
                        continue;
                    } else {
                        target = null;
                    }


                }
            }
        }
    }

    private boolean isValid(Entity entity) {
        return entity instanceof EntityPlayer && entity != mc.thePlayer && ((EntityLivingBase) entity).getHealth() > 0F && entity.getDistanceToEntity(mc.thePlayer) <= rangeValue.getObject();
    }
}
