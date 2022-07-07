package net.superblaubeere27.clientbase.modules.modules.combat;

import org.apache.commons.lang3.RandomUtils;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;

public class InventoryHelper extends Module {

	public InventoryHelper() {
		super("InvHelper", "An AI will help with your messy inv", ModuleCategory.COMBAT);
	}

	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		TimeHelper timer = new TimeHelper();
		double delay = 20;
        if (timer.hasReached(delay)) {
            int bestSword = this.getBestSword();
            int bestPick = this.getBestPickaxe();
            int bestAxe = this.getBestAxe();
            int bestShovel = this.getBestShovel();
            int bestArmor = this.getBestArmor();

            for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
                ItemStack is = mc.thePlayer.inventory.mainInventory[k];
                if (is != null && !(is.getItem() instanceof ItemArmor)) {
                    boolean clean = true;
                    if (clean) {
                        if (is.getItem() instanceof ItemSword) {
                            if (bestSword != -1 && bestSword != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }
                        if (is.getItem() instanceof ItemPickaxe) {
                            if (bestPick != -1 && bestPick != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }

                        if (is.getItem() instanceof ItemAxe) {
                            if (bestAxe != -1 && bestAxe != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }

                        if (this.isShovel(is.getItem())) {
                            if (bestShovel != -1 && bestShovel != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }
                        
                        if (is.getItem() instanceof ItemArmor) {
                            if (bestArmor != -1 && bestArmor != k) {
                                this.drop(k, is);
                                timer.reset();
                                return;
                            }
                        }
                    }
                    int swordSlot = mc.thePlayer.inventory.currentItem;
                    if (this.isBad(is.getItem())) {
                        this.drop(k, is);
                        timer.reset();
                        return;
                    }
                }
            }
            timer.reset();
        }
	}
	
	private int getBestArmor() {
		int bestArmor = -1;
		float bestReduceDamage = 0;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemArmor) {
				ItemArmor itemArmor = (ItemArmor) is.getItem();				
				float damage = itemArmor.damageReduceAmount;
				if (damage > bestReduceDamage) {
					bestReduceDamage = damage;
					bestArmor = k;
				}
			}
		}
		return bestArmor;
	}
	
	private int getBestSword() {
		int bestSword = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemSword) {
				ItemSword itemSword = (ItemSword) is.getItem();
				float damage = itemSword.getDamageVsEntity();
				damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, is);
				if (damage > bestDamage) {
					bestDamage = damage;
					bestSword = k;
				}
			}
		}
		return bestSword;
	}
	
	private int getBestPickaxe() {
		int bestPick = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemPickaxe) {
				ItemPickaxe itemSword = (ItemPickaxe) is.getItem();
				float damage = itemSword.getStrVsBlock(is, Block.getBlockById(4));
				if (damage > bestDamage) {
					bestDamage = damage;
					bestPick = k;
				}
			}
		}
		return bestPick;
	}
	
	private int getBestAxe() {
		int bestPick = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemAxe) {
				ItemAxe itemSword = (ItemAxe) is.getItem();
				float damage = itemSword.getStrVsBlock(is, Block.getBlockById(17));
				if (damage > bestDamage) {
					bestDamage = damage;
					bestPick = k;
				}
			}
		}
		return bestPick;
	}
	
	private int getBestShovel() {
		int bestPick = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && this.isShovel(is.getItem())) {
				ItemTool itemSword = (ItemTool) is.getItem();
				float damage = itemSword.getStrVsBlock(is, Block.getBlockById(3));
				if (damage > bestDamage) {
					bestDamage = damage;
					bestPick = k;
				}
			}
		}
		return bestPick;
	}
	
	private boolean isShovel(Item is) {
		return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
	}
	
	
	
	private void drop(int slot, ItemStack item) {
		boolean hotbar = false;
		for (int k = 0; k < 9; k++) {
			ItemStack itemK = mc.thePlayer.inventory.getStackInSlot(k);
			if (itemK != null && itemK == item) {
				hotbar = true;
				continue;
			}
		}
		
		if (hotbar) {
			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
			C07PacketPlayerDigging.Action diggingAction = C07PacketPlayerDigging.Action.DROP_ALL_ITEMS;
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(diggingAction, BlockPos.ORIGIN, EnumFacing.DOWN));
		} else {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
		}
	}
	
	private boolean isBad(Item i) {
		return i.getUnlocalizedName().contains("stick") ||
				i.getUnlocalizedName().contains("egg") ||
				i.getUnlocalizedName().contains("string") ||
				i.getUnlocalizedName().contains("flint") ||
				i.getUnlocalizedName().contains("bow") ||
				i.getUnlocalizedName().contains("bucket") ||
				i.getUnlocalizedName().contains("feather") ||
				i.getUnlocalizedName().contains("snow") ||
				i.getUnlocalizedName().contains("piston") || 
				i instanceof ItemGlassBottle ||
				i.getUnlocalizedName().contains("web") ||
				i.getUnlocalizedName().contains("slime") ||
				i.getUnlocalizedName().contains("trip") ||
				i.getUnlocalizedName().contains("wire") ||
				i.getUnlocalizedName().contains("sugar") ||
				i.getUnlocalizedName().contains("note") ||
				i.getUnlocalizedName().contains("record") ||
				i.getUnlocalizedName().contains("flower") ||
				i.getUnlocalizedName().contains("wheat") ||
				i.getUnlocalizedName().contains("fishing") ||
				i.getUnlocalizedName().contains("leather") ||
				i.getUnlocalizedName().contains("seeds") ||
				i.getUnlocalizedName().contains("skull") ||
				i.getUnlocalizedName().contains("torch") ||
				i.getUnlocalizedName().contains("anvil") ||
				i.getUnlocalizedName().contains("enchant") ||
				i.getUnlocalizedName().contains("exp") ||
				i.getUnlocalizedName().contains("shears");
	}

}
