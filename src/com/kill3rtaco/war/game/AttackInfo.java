package com.kill3rtaco.war.game;

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
import com.kill3rtaco.war.util.WarUtil;

public class AttackInfo {
	
	private LivingEntity	_attacker, _victim;
	private DamageCause		_cause;
	private String			_toolActionDisplay;
	private double			_damage;
	
	public AttackInfo(EntityDamageEvent event) {
		_victim = (LivingEntity) event.getEntity();
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
				_toolActionDisplay = "Squished";
			} else {
				//assume living entity
				_attacker = (LivingEntity) entity;
				if(_toolActionDisplay == null && _attacker instanceof org.bukkit.entity.Player) {
					org.bukkit.entity.Player player = (org.bukkit.entity.Player) _attacker;
					ItemStack item = player.getItemInHand();
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
	
}
