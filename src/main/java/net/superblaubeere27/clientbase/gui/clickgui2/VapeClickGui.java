package net.superblaubeere27.clientbase.gui.clickgui2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.gui.clickgui2.theme.Theme;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.utils.liquid.RenderUtil;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import net.superblaubeere27.clientbase.valuesystem.Value;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VapeClickGui extends GuiScreen {

    private boolean close = false;
    private boolean closed;

//    class GetConfigs extends Thread {
//        @Override
//        public void run() {
//            try {
//                for (String s : CloudConfigsUtil.getAllConfigsNameList()) {
//                    configs.add(new Config(s, "Cloud", true));
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    class LoadCloudConfig extends Thread {
//        private String configName;
//
//        public LoadCloudConfig(String configName) {
//            this.configName = configName;
//        }
//
//        @Override
//        public void run() {
//            try {
//                CloudConfigsUtil.loadCloudConfig(configName);
//            } catch (IOException e) {
//                ErrorUtil.printException(e);
//            }
//        }
//    }


    private float dragX, dragY;
    private boolean drag = false;
    private int valuemodx = 0;
    private static float modsRole, modsRoleNow;
    private static float valueRoleNow, valueRole;

    public static ArrayList<Config> configs = new ArrayList<Config>();
    public static EmptyInputBox configInputBox;


    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;
    /*
    主窗口宽度 = 500
    主窗口高度 = 310
    功能列表起始位置 = 100
    功能宽度 = 325(未开values)
    功能起始高度 = 60
     */


    static float windowX = 200, windowY = 200;
    static float width = 500, height = 310;

    static ClickType selectType = ClickType.Home;
    static ModuleCategory modCategory = ModuleCategory.COMBAT;
    static Module selectMod;

    float[] typeXAnim = new float[]{windowX + 10, windowX + 10, windowX + 10, windowX + 10};

    float hy = windowY + 40;

    TimeHelper valuetimer = new TimeHelper();

    public static Theme theme = new Theme();

    @Override
    public void initGui() {
        super.initGui();
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;
        valuetimer.reset();
        configs.clear();

//        String configFiles = Client.configFolder.getAbsolutePath();
//        File configFile = new File(configFiles);
//
//        for (int i = 0; i < configFile.listFiles().length; i++) {
//            if (configFile.listFiles()[i].isDirectory()) {
//                configs.add(new Config(configFile.listFiles()[i].getName(), "Location", true));
//            }
//        }
//
//        configInputBox = new EmptyInputBox(2, mc.fontRendererObj, 0, 0, 85, 10);
    }


    public float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 10));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sResolution = new ScaledResolution(mc);
        ScaledResolution sr = new ScaledResolution(mc);


        float outro = smoothTrans(this.outro, lastOutro);
        if (mc.currentScreen == null) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(outro, outro, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        }


        //animation
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);


        if (percent > 0.98) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(percent, percent, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        } else {
            if (percent2 <= 1) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(percent2, percent2, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            }
        }


        if (percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }

        if (percent >= 1.4 && close) {
            percent = 1.5f;
            closed = true;
            mc.currentScreen = null;
        }


//        if (HUD.mod.getValue() == HUD.HUDMode.Flux) {
//            RenderUtils.drawGradientRect(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(255, 130, 164, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());
//        } else {
//            RenderUtils.drawGradientRect(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(107, 147, 255, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());
//        }


//        if (inAnim > 0) {
//            inAnim -= 5000f / mc.debugFPS;
//        } else {
//            inAnim += 0.1f;
//        }
//        if (inAnim > 5) {
//            GlStateManager.translate(inAnim, 0, 0);
//        }
//        Client.musicPanel.render(mouseX,mouseY);

        //拖动
        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
            drag = true;
        } else if (dragX != 0 || dragY != 0) {
            dragX = 0;
            dragY = 0;
        }

        //滚动
        ClientBase Client = ClientBase.INSTANCE;
        int dWheel = Mouse.getDWheel();
        //绘制主窗口
        RenderUtil.drawRoundedRect(windowX, windowY, windowX + width, windowY + height, 4, theme.BG.getRGB());
        if (selectMod == null) {
            mc.fontRendererObj.drawString(Client.CLIENT_NAME + " " + Client.CLIENT_VERSION, windowX + 15, windowY + height - 20, theme.FONT.getRGB(), false);
        }
        //绘制顶部图标
        float typeX = windowX + 20;
        int i = 0;
        for (Enum<?> e : ClickType.values()) {
            if (!isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] += (typeX - typeXAnim[i]) / 20;
                }
            } else {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] = typeX;
                }
            }
            if (e != ClickType.Settings) {
                if (e == selectType) {
//                    RenderUtil.drawCustomImageAlpha(typeXAnim[i], windowY + 10, 16, 16, new ResourceLocation("client/" + e.name() + ".png"), theme.FONT_C.getRGB(),255);
                    mc.fontRendererObj.drawString(e.name(), typeXAnim[i] + 20, windowY + 15, theme.FONT_C.getRGB(),false);
                    typeX += (32 + mc.fontRendererObj.getStringWidth(e.name() + " "));
                } else {
//                    RenderUtil.drawCustomImageAlpha(typeXAnim[i], windowY + 10, 16, 16, new ResourceLocation("client/" + e.name() + ".png"), theme.FONT.getRGB(),255);
                    mc.fontRendererObj.drawString(e.name(), typeXAnim[i] + 20, windowY + 15, theme.FONT_C.getRGB(),false);
                    typeX += (32 + mc.fontRendererObj.getStringWidth(e.name() + " "));
                }
            }
//            } else {
//                RenderUtil.drawCustomImageAlpha(windowX + width - 20, windowY + 10, 16, 16, new ResourceLocation("client/" + e.name() + ".png"), e == selectType ? new Color(255, 255, 255).getRGB() : new Color(79, 80, 86).getRGB(),255);
//            }
            i++;
        }


        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, 2 * ((int) (sr.getScaledHeight_double() - (windowY + height))) + 50, (int) (sr.getScaledWidth_double() * 2), (int) ((height) * 2) - 160);
        if (selectType == ClickType.Home) {
            if (selectMod == null) {
                //绘制类型列表
                float cateY = windowY + 65;
                for (ModuleCategory m : ModuleCategory.values()) {
                    if (m == modCategory) {
                        RenderUtil.drawRect(0, 0, 0, 0, -1);
                        RenderUtil.drawRoundedRect(windowX + 7, hy - 8, windowX + 90, hy + (mc.fontRendererObj.FONT_HEIGHT / 2) + 6, 0.15f, theme.BG_4);
                        RenderUtil.drawRoundedRect(windowX + 7, hy - 2, windowX + 9, hy + (mc.fontRendererObj.FONT_HEIGHT / 2), 0.15f, theme.BG_3);

                        mc.fontRendererObj.drawString(m.name(), windowX + 20, cateY, theme.FONT_C.getRGB(), false);

                        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            hy = cateY;
                        } else {
                            if (hy != cateY) {
                                hy += (cateY - hy) / 20;
                            }
                        }
                    } else {
                        mc.fontRendererObj.drawString(m.name(), windowX + 20, cateY, theme.FONT.getRGB(),false);
                    }


                    cateY += 25;
                }

                
                

                mc.fontRendererObj.drawString(mc.thePlayer.getName(), windowX + 35, windowY + height - 60, theme.FONT_C.getRGB(), false);
                mc.fontRendererObj.drawString("#" + RandomUtils.nextInt(1000, 9999), windowX + 35, windowY + height - 50, theme.FONT.getRGB(), false);


            }
            if (selectMod != null) {
                if (valuemodx > -80) {
                    valuemodx -= 5;
                }
            } else {
                if (valuemodx < 20) {
                    valuemodx += 5;
                }
            }

            if (selectMod != null) {

                RenderUtil.drawRoundedRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + height - 20, 3, theme.Modules.getRGB());
                RenderUtil.drawGradientSideways(windowX + 428 + valuemodx, windowY + 60, windowX + 430 + valuemodx, windowY + height - 20, theme.Modules.getRGB(), theme.BG_4.getRGB());
                RenderUtil.drawGradientRect(windowX + 430 + valuemodx, windowY + height - 20, windowX + width, windowY + height - 17, theme.Modules.getRGB(), theme.BG_4.getRGB());

                RenderUtil.drawRoundedRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + 85, 3, theme.Modules_C);
                RenderUtil.drawGradientRect(windowX + 430 + valuemodx, windowY + 85, windowX + width, windowY + 87, theme.Modules.getRGB(), theme.BG_4.getRGB());

                mc.fontRendererObj.drawString(modCategory.name() + " > " + selectMod.getName(), windowX + 460 + valuemodx, windowY + 70, theme.FONT.getRGB(), false);

                if (isHovered(windowX + 430 + (int) valuemodx, windowY + 60, windowX + width, windowY + height - 20, mouseX, mouseY)) {
                    if (dWheel < 0 && Math.abs(valueRole) + 170 < (ClientBase.INSTANCE.valueManager.getAllValuesFrom(selectMod.getName()).size() * 25)) {
                        valueRole -= 32;
                    }
                    if (dWheel > 0 && valueRole < 0) {
                        valueRole += 32;
                    }
                }

                if (valueRoleNow != valueRole) {
                    valueRoleNow += (valueRole - valueRoleNow) / 20;
                    valueRoleNow = (int) valueRoleNow;
                }

                float valuey = windowY + 100 + valueRoleNow;

                if (selectMod == null) {
                    return;
                }
                
                
//                for (NewMode v : selectMod.getNewModes()) {
//                    NewMode modeValue = (NewMode) v;
//
//                    if (valuey + 4 > windowY + 100 & valuey < (windowY + height)) {
//                        RenderUtil.drawRoundedRect(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
//                        RenderUtil.drawRoundedRect(windowX + 446 + valuemodx, valuey + 3, windowX + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
//                        FontLoaders.arial16.drawString(v.getName() + ":" + modeValue.getModeAsString(), windowX + 455 + valuemodx, valuey + 10, new Color(230, 230, 230).getRGB());
//                        mc.fontRendererObj(">", windowX + width - 15, valuey + 9, new Color(73, 72, 76).getRGB());
//                        if (isHovered(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {
//                            if (Arrays.binarySearch(modeValue.getModes(), (v.getValue()))
//                                    + 1 < modeValue.getModes().length) {
//                                v.setValue(modeValue
//                                        .getModes()[Arrays.binarySearch(modeValue.getModes(), (v.getValue())) + 1]);
//                            } else {
//                                v.setValue(modeValue.getModes()[0]);
//                            }
//                            valuetimer.reset();
//                        }
//                    }
//                    valuey += 25;
//                }
            }
            if (isHovered(windowX + 435 + valuemodx, windowY + 65, windowX + 435 + valuemodx + 16, windowY + 65 + 16, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                selectMod = null;
                valuetimer.reset();
            }
            float modY = windowY + 70 + modsRoleNow;
            for (Module m : ClientBase.INSTANCE.moduleManager.getModules()) {
                if (m.getCategory() != modCategory)
                    continue;

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (valuetimer.delay(300) && modY + 40 > (windowY + 70) && modY < (windowY + height)) {
                        m.setState(!m.getState());
                        valuetimer.reset();
                    }
                } else if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    if (valuetimer.delay(300)) {
                        if (selectMod != m) {
                            valueRole = 0;
                            selectMod = m;
                        } else if (selectMod == m) {
                            selectMod = null;
                        }
                        valuetimer.reset();
                    }
                }

//                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY)) {
//                    if (m.isEnabled()) {
//                        RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25,4, new Color(43, 41, 45).getRGB());
//                    } else {
//                        RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 4,new Color(35, 35, 35).getRGB());
//                    }
//                } else {
                if (m.getState()) {
//                    RenderUtil.drawRoundedRect(windowX + 99.9f + valuemodx, modY - 10.1f, windowX + 425.1f + valuemodx, modY + 25.1f, 4, theme.BG_2);
                    RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 4, theme.Modules_C);
                } else {
//                    RenderUtil.drawRoundedRect(windowX + 99.9f + valuemodx, modY - 10.1f, windowX + 425.1f + valuemodx, modY + 25.1f, 4, theme.BG_2);
                    RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 4, theme.Modules);
                    RenderUtil.drawGradientRect(windowX + 105 + valuemodx, modY + 25, windowX + 420 + valuemodx, modY + 27, theme.Modules.getRGB(), theme.BG_4.getRGB());
                }
//                }

                //三个点
                RenderUtil.drawRoundedRect(windowX + 410 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 4, theme.BG_4);
                mc.fontRendererObj.drawString(".", windowX + 416 + valuemodx, modY - 5, theme.FONT.getRGB(), false);
                mc.fontRendererObj.drawString(".", windowX + 416 + valuemodx, modY - 1, theme.FONT.getRGB(), false);
                mc.fontRendererObj.drawString(".", windowX + 416 + valuemodx, modY + 3, theme.FONT.getRGB(), false);

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) || m.getState()) {
                    mc.fontRendererObj.drawString(m.getName(), windowX + 141 + valuemodx, modY + 5, theme.FONT_C.getRGB(), false);
                } else {
                    mc.fontRendererObj.drawString(m.getName(), windowX + 140 + valuemodx, modY + 5, theme.FONT.getRGB(), false);
                }

                mc.fontRendererObj.drawString(m.getDescription(), windowX + 220 + valuemodx, modY + 5, theme.FONT.getRGB(), false);

                if (m.getState()) {
                    RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, 4, theme.BG_3.getRGB());
                    m.optionAnim = 100;

                    RenderUtil.drawRoundedRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, theme.Option_B.getRGB());
                    RenderUtil.smoothCircle(windowX + 385 + 10 * m.optionAnimNow / 100 + valuemodx, modY + 7, 3.5f, theme.Option_C);
                } else {
                    RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, 4, theme.BG_2);
                    m.optionAnim = 0;

                    RenderUtil.drawRoundedRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, theme.BG_2);
//                    RenderUtil.drawRoundedRect(windowX + 381 + valuemodx, modY + 3, windowX + 399 + valuemodx, modY + 11, 4, new Color(29, 27, 31).getRGB());
                    RenderUtil.smoothCircle(windowX + 385 + 10 * m.optionAnimNow / 100 + valuemodx, modY + 7, 3.5f, theme.Option_U_C);
                }

                if (m.optionAnimNow != m.optionAnim) {
                    m.optionAnimNow += (m.optionAnim - m.optionAnimNow) / 20;
                }


                modY += 40;
            }
            //滚动
            if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
                if (dWheel < 0 && Math.abs(modsRole) + 220 < (Client.INSTANCE.moduleManager.getModuleByCategory(modCategory).size() * 40)) {
                    modsRole -= 32;
                }
                if (dWheel > 0 && modsRole < 0) {
                    modsRole += 32;
                }
            }

            if (modsRoleNow != modsRole) {
                modsRoleNow += (modsRole - modsRoleNow) / 20;
                modsRoleNow = (int) modsRoleNow;
            }


        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        int dWheel2 = Mouse.getDWheel();
        if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
            if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (Client.moduleManager.getModuleByCategory(modCategory).size() * 40)) {
                modsRole -= 16;
            }
            if (dWheel2 > 0 && modsRole < 0) {
                modsRole += 16;
            }
        }

        if (modsRoleNow != modsRole) {
            modsRoleNow += (modsRole - modsRoleNow) / 20;
            modsRoleNow = (int) modsRoleNow;
        }
//        //处理输入框
//        if (selectType == ClickType.Config) {
//            if (isHovered(windowX + 95, windowY + 95, windowX + 105, windowY + 105, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(500) && configs.size() < 6) {
//                boolean has = false;
//                for (Config c : configs) {
//                    if (c.name.equals(configInputBox.getText())) {
//                        Client.instance.getModuleManager().saveSettings(configInputBox.getText());
//
//                        valuetimer.reset();
//                        has = true;
//                    }
//                }
//                if (!has) {
//                    configs.add(new Config(configInputBox.getText(), "Location", false));
//                    Client.instance.getModuleManager().saveSettings(configInputBox.getText());
//                    valuetimer.reset();
//                }
//            } else if (isHovered(windowX + 95, windowY + 95, windowX + 105, windowY + 105, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(500) && configs.size() >= 6) {
//                Helper.sendMessage("You can only save 6 configs!");
//                valuetimer.reset();
//            }
//
//
//            if (isHovered(configInputBox.xPosition, configInputBox.yPosition, configInputBox.xPosition + configInputBox.getWidth(), configInputBox.yPosition + 10, mouseX, mouseY) && Mouse.isButtonDown(0)) {
//                configInputBox.mouseClicked(mouseX, mouseY, Mouse.getButtonCount());
//            } else if (Mouse.isButtonDown(0) && !isHovered(configInputBox.xPosition, configInputBox.yPosition, configInputBox.xPosition + configInputBox.getWidth(), configInputBox.yPosition + 10, mouseX, mouseY)) {
//                configInputBox.setFocused(false);
//            }
//        }


    }

    public int findArray(float[] a, float b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
//        Client.musicPanel.onMouse(mouseX,mouseY,mouseButton);
        //顶部图标
        float typeX = windowX + 20;
        for (Enum<?> e : ClickType.values()) {
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    if (isHovered(typeX, windowY + 10, typeX + 16 + mc.fontRendererObj.getStringWidth(e.name() + " "), windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32 + mc.fontRendererObj.getStringWidth(e.name() + " "));
                } else {
                    if (isHovered(typeX, windowY + 10, typeX + 16, windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32 + mc.fontRendererObj.getStringWidth(e.name() + " "));
                }
            } else {
                if (isHovered(windowX + width - 32, windowY + 10, windowX + width, windowY + 10 + 16, mouseX, mouseY)) {
                    selectType = (ClickType) e;
                }
            }
        }

        if (selectType == ClickType.Home) {
            //类型列表
            float cateY = windowY + 65;
            for (ModuleCategory m : ModuleCategory.values()) {

                if (isHovered(windowX, cateY - 8, windowX + 50, cateY + (mc.fontRendererObj.FONT_HEIGHT / 2) + 8, mouseX, mouseY)) {
                    if (modCategory != m) {
                        modsRole = 0;
                    }

                    modCategory = m;
                    for (Module mod : ClientBase.INSTANCE.moduleManager.getModules()) {
                        mod.optionAnim = 0;
                        mod.optionAnimNow = 0;

                    }
                }

                cateY += 25;
            }

        }


    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (!closed && keyCode == Keyboard.KEY_ESCAPE) {
            close = true;
            mc.mouseHelper.grabMouseCursor();
            mc.inGameHasFocus = true;
            return;
        }

        if (close) {
            this.mc.displayGuiScreen((GuiScreen) null);
        }

        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (configInputBox.isFocused()) {
//            configInputBox.textboxKeyTyped(typedChar, keyCode);
//        }

//        if (MusicPanel.inputBox.isFocused()) {
//            MusicPanel.inputBox.textboxKeyTyped(typedChar, keyCode);
//            if(keyCode == Keyboard.KEY_RETURN){
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Client.musicPanel.getMusics();
////                    }
////                }).start();
//            }
//        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed() {

    }
}
