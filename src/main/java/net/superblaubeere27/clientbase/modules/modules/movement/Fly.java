/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.Vec3;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.notifications.Notification;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.notifications.NotificationType;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import net.superblaubeere27.clientbase.valuesystem.Value;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import scala.Int;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Fly extends Module {

    public Fly() {
        super("Fly", "Help you flying around :)", ModuleCategory.MOVEMENT, true, false, Keyboard.KEY_F);
    }

    private NumberValue<Float> speed = new NumberValue<Float>("Speed", 1f, 1f, 10f);
    private ModeValue modes = new ModeValue("Fly modes", "Vanilla", "Hypixel", "Vanilla", "CubeCraft", "MatrixDMG", "NCP");

    private double boostTicks = 27;
    private int randomAmount = RandomUtils.nextInt(5,30);

    private double randomNum = 0.2;
    public boolean velocitypacket = false;
    public double packetymotion = 0.0;
    public int tickFly = 0;

    private int tick = 0;

    public void update() {
        tick++;
    }

    public void reset() {
        tick = 0;
    }

    public boolean hasTimePassed(int ticks){
        return tick >= ticks;
    }

    @Override
    protected void onDisable(){
        setTimerSpeed(1F);
        velocitypacket = false;
        packetymotion = 0.0;
        tick = 0;
        tickFly = 0;
    }

    @Override
    protected void onEnable(){
        int Mode = modes.getObject();
        if(Mode == 4){
            mc.thePlayer.fallDistance = 4;
        }
    }

    public String getMode(){
        int Mode = modes.getObject();
        if(Mode == 0) return "Hypixel";
        if (Mode == 1) return "Vanilla";
        if (Mode == 2) return "CubeCraft";
        if (Mode == 3) return "MatrixDMG";
        if(Mode == 4) return "NCP";

        else return null;
    }

    @EventTarget
    private void onMove(MotionUpdateEvent event) {
        if(!getState())
            return;

        setTimerSpeed(1F);

        int Mode = modes.getObject();

        this.setName("Fly " + "§7" + getMode());

        if(Mode == 1){
            mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY += speed.getObject();
            if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY -= speed.getObject();
            if (!isMoving()) return;
            double yaw = direction();
            EntityPlayer thePlayer = mc.thePlayer;
            thePlayer.motionX = -sin(yaw) * speed.getObject();
            thePlayer.motionZ = cos(yaw) * speed.getObject();
        }

        if(Mode == 0){
            double ydiff;
            mc.thePlayer.cameraYaw *= 0.2;
            mc.thePlayer.motionY = -0.0;
            if(!mc.thePlayer.onGround){
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                ydiff = mc.thePlayer.posY + 1.0e-10d;
                if(mc.thePlayer.fallDistance > 0.000001){
                    mc.thePlayer.onGround = true;
                    mc.thePlayer.setPosition(mc.thePlayer.posX, ydiff, mc.thePlayer.posZ);
                }
            }
        }

        if(Mode == 2){
            setTimerSpeed(0.29F);
            HClip2(7.0);
            HClip1(2.0);
            redeskySpeed(1);
            mc.thePlayer.motionY = -0.01;
        }

        if(Mode == 3){
            float Flyspeed = speed.getObject();
            if(velocitypacket) {
                setTimerSpeed(speed.getObject());
                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                mc.thePlayer.motionX += (-sin(yaw) * (0.3 + (Flyspeed / 10 ) + randomNum));
                mc.thePlayer.motionZ += (cos(yaw) * (0.3 + (Flyspeed / 10 ) + randomNum));
                mc.thePlayer.motionY = packetymotion;
                tickFly++;
                if(tick>=boostTicks) {
                    setTimerSpeed(1F);
                    velocitypacket = false;
                    packetymotion = 0.0;
                    tick = 0;
                }
            }
        }

        if(Mode == 4){
            mc.thePlayer.motionY = -0;
            if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -0.5;
            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = +0.5;
            strafe(1F);
        }

    }


	@EventTarget
    private void onPacket(PacketEvent event) {
        Packet packet = event.getPacket();
        if(getMode().equalsIgnoreCase("Matrix")) {
        	if (packet instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity entityPacket = (S12PacketEntityVelocity) event.getPacket();
                if (mc.thePlayer == null || (mc.theWorld.getEntityByID(entityPacket.getEntityID())) != mc.thePlayer) return;
                if(((S12PacketEntityVelocity) packet).getMotionX() / 8000.0 > 0.2) {
                    velocitypacket = true;
                    packetymotion = ((S12PacketEntityVelocity) packet).getMotionY() / 8000.0;
                }
            }
        }
        
    }

    public static void strafe(float speed) {
        if (!isMoving()) return;
        double yaw = direction();
        mc.thePlayer.motionX = -sin(yaw) * speed;
        mc.thePlayer.motionZ = cos(yaw) * speed;
    }

    public static void setTimerSpeed(float timerSpeed){
        try{
            Class<?> mc = Minecraft.class;
            Field timerField = mc.getDeclaredField("timer");
            timerField.setAccessible(true);

            try{
                Object timer = timerField.get(Minecraft.getMinecraft());
                Class<?> timerClass = timer.getClass();
                Field timerSpeedField = timerClass.getDeclaredField("timerSpeed");
                timerSpeedField.setAccessible(true);
                timerSpeedField.setFloat(timer, timerSpeed);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }

        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void HClip1(double horizontal) {
        double playerYaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.setPosition(
                mc.thePlayer.posX + horizontal * -sin(playerYaw),
                mc.thePlayer.posY,
                mc.thePlayer.posZ + horizontal * cos(playerYaw)
        );
    }

    private void redeskySpeed(int speed) {
        double playerYaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.motionX = speed * -sin(playerYaw);
        mc.thePlayer.motionZ = speed * cos(playerYaw);
    }

    private void HClip2(double horizontal) {
        double playerYaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.getNetHandler().addToSendQueue(
                new C03PacketPlayer.C04PacketPlayerPosition(
                        mc.thePlayer.posX + horizontal * -sin(
                                playerYaw
                        ), mc.thePlayer.posY, mc.thePlayer.posZ + horizontal * cos(playerYaw), false
                )
        );
    }

    public static double direction() {
        EntityPlayer thePlayer = mc.thePlayer;
        double rotationYaw = thePlayer.rotationYaw;
        if (thePlayer.moveForward < 0f) rotationYaw += 180f;
        float forward = 1f;
        if (thePlayer.moveForward < 0f) forward = -0.5f; else if (thePlayer.moveForward > 0f) forward = 0.5f;
        if (thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward;
        return Math.toRadians(rotationYaw);
    }

    public static boolean isMoving() {
        if (mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f)) {
            return true;
        }else {
            return false;
        }
    }

}
