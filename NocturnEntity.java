package com.nocturne.entity;

import com.nocturne.ai.NocturnMeleeAttackGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NocturnEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean dormantLastTick = true;
    private static final RawAnimation DORMANT = RawAnimation.begin().thenLoop("animation.nocturn.dormant");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.nocturn.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.nocturn.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.nocturn.run");

    public NocturnEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.dormantLastTick = isDormant();
        this.setNoAi(this.dormantLastTick);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 64.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.26D)
            .add(Attributes.ATTACK_DAMAGE, 7.0D)
            .add(Attributes.FOLLOW_RANGE, 28.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D)
            .add(Attributes.ARMOR, 10.0D);
    }

    @Override protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new NocturnMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isDormant() {
        long t = this.level().getDayTime() % 24000L;
        return t < 12500L || t >= 23000L;
    }

    public double getNightPower() {
        long t = this.level().getDayTime() % 24000L;
        if (t < 12500L || t >= 23000L) return 0.0D;
        if (t < 18000L) return smooth((t - 12500.0D) / 5500.0D);
        return smooth((23000.0D - t) / 5000.0D);
    }
    private static double smooth(double x) { x = Math.max(0.0D, Math.min(1.0D, x)); return x*x*(3.0D-2.0D*x); }

    @Override public void tick() {
        super.tick();
        boolean dormant = isDormant();
        if (!this.level().isClientSide) {
            if (dormant != dormantLastTick) {
                this.setNoAi(dormant);
                dormantLastTick = dormant;
                if (!dormant) {
                    this.setTarget(null);
                    this.triggerAnim("movement", "awaken");
                } else {
                    this.triggerAnim("movement", "petrify");
                }
            }
            double p = getNightPower();
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(dormant ? 0.0D : 0.20D + 0.20D*p);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D + 7.0D*p);
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(18.0D + 26.0D*p);
        }
    }

    @Override public boolean hurt(DamageSource source, float amount) {
        if (isDormant() && source.getEntity() instanceof Player player) {
            ItemStack held = player.getMainHandItem();
            if (!held.is(Items.IRON_PICKAXE) && !held.is(Items.DIAMOND_PICKAXE) && !held.is(Items.NETHERITE_PICKAXE)
                && !held.is(Items.STONE_PICKAXE) && !held.is(Items.GOLDEN_PICKAXE) && !held.is(Items.WOODEN_PICKAXE)) {
                return false;
            }
            amount *= 2.5F;
        }
        return super.hurt(source, amount);
    }

    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 4, state -> {
            if (isDormant()) return state.setAndContinue(DORMANT);
            if (state.isMoving()) return state.setAndContinue(getNightPower() > 0.72D ? RUN : WALK);
            return state.setAndContinue(IDLE);
        }).triggerableAnim("awaken", RawAnimation.begin().thenPlay("animation.nocturn.awaken"))
          .triggerableAnim("petrify", RawAnimation.begin().thenPlay("animation.nocturn.petrify"))
          .triggerableAnim("slam", RawAnimation.begin().thenPlay("animation.nocturn.slam")));
    }
    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    @Override public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && !this.level().isClientSide) {
            this.triggerAnim("movement", "slam");
        }
        return hit;
    }

    @Override public boolean removeWhenFarAway(double distanceToClosestPlayer) { return false; }
}
