package com.nocturne.ai;

import com.nocturne.entity.NocturnEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class NocturnMeleeAttackGoal extends MeleeAttackGoal {
    private final NocturnEntity nocturn;
    public NocturnMeleeAttackGoal(NocturnEntity mob, double speed, boolean followingTargetEvenIfNotSeen) {
        super(mob, speed, followingTargetEvenIfNotSeen);
        this.nocturn = mob;
    }
    @Override public boolean canUse() { return !nocturn.isDormant() && super.canUse(); }
    @Override public boolean canContinueToUse() { return !nocturn.isDormant() && super.canContinueToUse(); }
}
