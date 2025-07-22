package bristopher.swapcore;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;
import net.minecraft.world.entity.ai.goal.PathfinderGoalZombieAttack;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalUniversalAngerReset;
import net.minecraft.world.entity.monster.EntityPigZombie;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;

public class AutoAggroPigman extends EntityPigZombie {
    public AutoAggroPigman(EntityTypes<? extends EntityPigZombie> entitytypes, World world) {
        super(entitytypes, world);
    }
@Override
    protected void m() {
        this.ch.a(2, new PathfinderGoalZombieAttack(this, 1.0, false));
        this.ch.a(7, new PathfinderGoalRandomStrollLand(this, 1.0));
        this.ci.a(1, (new PathfinderGoalHurtByTarget(this, new Class[0])).a(new Class[0]));
        this.ci.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 10, true, false, this::a));
        this.ci.a(3, new PathfinderGoalUniversalAngerReset(this, true));
    }
}

