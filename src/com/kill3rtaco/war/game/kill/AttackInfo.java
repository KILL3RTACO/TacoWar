package com.kill3rtaco.war.game.kill;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.player.Player;
import com.kill3rtaco.war.util.WarUtil;

public class AttackInfo {
	
	private LivingEntity	_attacker, _victim;
	private Player			_attackerPlayer, _victimPlayer;
	private DamageCause		_cause;
	private String			_toolActionDisplay;
	private double			_damage;
	
	public AttackInfo(EntityDamageEvent event) {
		setVictim((LivingEntity) event.getEntity());
		_cause = event.getCause();
		_damage = event.getDamage();
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity entity = e.getDamager();
			if(entity instanceof Projectile) {
				Projectile projectile = (Projectile) entity;
				_attacker = projectile.getShooter();
				if(projectile.getType() == EntityType.ARROW) {
					_toolActionDisplay = "Bow + Arrow";
				} else {
					_toolActionDisplay = TacoAPI.getChatUtils().toProperCase(projectile.getType().name());
				}
			} else if(entity instanceof Vehicle) {
				Vehicle vehicle = (Vehicle) entity;
				if(vehicle.getPassenger() instanceof LivingEntity) {
					_attacker = (LivingEntity) vehicle.getPassenger();
				}
				_toolActionDisplay = (_attacker == _victim ? "Run" : "Ran") + " Over";
			} else if(entity instanceof FallingBlock) {
				FallingBlock b = (FallingBlock) entity;
				_toolActionDisplay = "Squished : " + TacoAPI.getChatUtils().toProperCase(b.getMaterial().name());
			} else {
				//assume living entity
				setAttacker((LivingEntity) entity);
				if(_toolActionDisplay == null && _attackerPlayer != null) {
					ItemStack item = _attackerPlayer.getBukkitPlayer().getItemInHand();
					if(item == null || item.getType() == Material.AIR) {
						_toolActionDisplay = "Fist";
					} else {
						_toolActionDisplay = TacoAPI.getChatUtils().toProperCase(item.getType().name());
					}
				}
			}
		} else {
			_toolActionDisplay = WarUtil.getDamageCauseName(_cause);
		}
	}
	
	private void setAttacker(LivingEntity attacker) {
		_attacker = attacker;
		if(attacker != null && attacker instanceof org.bukkit.entity.Player) {
			org.bukkit.entity.Player p = (org.bukkit.entity.Player) attacker;
			if(TacoWar.plugin.currentGame() != null && TacoWar.plugin.currentGame().isPlaying(p)) {
				_attackerPlayer = TacoWar.plugin.currentGame().getPlayer(p);
			}
		}
	}
	
	private void setVictim(LivingEntity victim) {
		_victim = victim;
		if(victim != null && victim instanceof org.bukkit.entity.Player) {
			org.bukkit.entity.Player p = (org.bukkit.entity.Player) victim;
			if(TacoWar.plugin.currentGame() != null && TacoWar.plugin.currentGame().isPlaying(p)) {
				_victimPlayer = TacoWar.plugin.currentGame().getPlayer(p);
			}
		}
	}
	
	public LivingEntity getAttacker() {
		return _attacker;
	}
	
	public LivingEntity getVictim() {
		return _victim;
	}
	
	public String getToolActionDisplay() {
		return _toolActionDisplay;
	}
	
	public boolean isSuicide() {
		return WarUtil.isSuicide(_cause);
	}
	
	public double getDamage() {
		return _damage;
	}
	
	public void setToolActionDisplay(String display) {
		_toolActionDisplay = display;
	}
	
	public void setDamage(double damage) {
		_damage = damage;
	}
	
	public Player getAttackerAsPlayer() {
		return _attackerPlayer;
	}
	
	public Player getVictimAsPlayer() {
		return _victimPlayer;
	}
	
}
