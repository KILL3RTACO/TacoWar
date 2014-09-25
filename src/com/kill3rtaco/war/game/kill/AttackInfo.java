package com.kill3rtaco.war.game.kill;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.Weapon;
import com.kill3rtaco.war.util.WarUtil;

public class AttackInfo {

	private LivingEntity	_attacker, _victim;
	private WarPlayer		_attackerPlayer, _victimPlayer;
	private DamageCause		_cause;
	private String			_toolActionDisplay;
	private double			_damage;

	public AttackInfo(EntityDamageEvent event) {
		setVictim((LivingEntity) event.getEntity());
		_cause = event.getCause();
		_damage = event.getDamage();
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity entity = e.getDamager();
			if (entity instanceof Projectile) {
				Projectile projectile = (Projectile) entity;
				_attacker = projectile.getShooter();
				if (projectile.hasMetadata(Weapon.METADATA_KEY)) {
					Weapon w = (Weapon) projectile.getMetadata(Weapon.METADATA_KEY).get(0).value();
					_toolActionDisplay = w.getConfig().getString(Weapon.KEY_NAME);
				} else {
					_toolActionDisplay = TacoAPI.getChatUtils().toProperCase(projectile.getType().name());
				}
			} else if (entity instanceof Vehicle) {
				Vehicle vehicle = (Vehicle) entity;
				if (vehicle.getPassenger() instanceof LivingEntity) {
					_attacker = (LivingEntity) vehicle.getPassenger();
				}
				_toolActionDisplay = (_attacker == _victim ? "Run" : "Ran") + " Over";
			} else if (entity instanceof FallingBlock) {
				FallingBlock b = (FallingBlock) entity;
				_toolActionDisplay = "Squished : " + TacoAPI.getChatUtils().toProperCase(b.getMaterial().name());
			} else if (entity instanceof LightningStrike) {
				LightningStrike strike = (LightningStrike) entity;
				if (strike.hasMetadata(Weapon.METADATA_KEY)) {
					Weapon w = (Weapon) strike.getMetadata(Weapon.METADATA_KEY).get(0).value();
					_toolActionDisplay = w.getConfig().getString(Weapon.KEY_NAME);
				} else {
					_toolActionDisplay = WarUtil.getDamageCauseName(DamageCause.LIGHTNING);
				}
			} else {
				//assume living entity
				setAttacker((LivingEntity) entity);
				if (_toolActionDisplay == null && _attackerPlayer != null) {
					ItemStack item = _attackerPlayer.getBukkitPlayer().getItemInHand();
					if (item == null || item.getType() == Material.AIR) {
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
		if (attacker != null && attacker instanceof org.bukkit.entity.Player) {
			org.bukkit.entity.Player p = (org.bukkit.entity.Player) attacker;
			if (TacoWar.currentGame() != null && TacoWar.currentGame().isPlaying(p)) {
				_attackerPlayer = TacoWar.currentGame().getPlayers().get(p);
			}
		}
	}

	private void setVictim(LivingEntity victim) {
		_victim = victim;
		if (victim != null && victim instanceof org.bukkit.entity.Player) {
			org.bukkit.entity.Player p = (org.bukkit.entity.Player) victim;
			if (TacoWar.currentGame() != null && TacoWar.currentGame().isPlaying(p)) {
				_victimPlayer = TacoWar.currentGame().getPlayers().get(p);
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

	public WarPlayer getAttackerAsPlayer() {
		return _attackerPlayer;
	}

	public WarPlayer getVictimAsPlayer() {
		return _victimPlayer;
	}

}
