package level.devlevel;

import contrib.components.AIComponent;
import contrib.components.CollideComponent;
import contrib.components.HealthComponent;
import contrib.components.SpikyComponent;
import contrib.entities.DungeonMonster;
import contrib.entities.EntityFactory;
import contrib.utils.components.health.Damage;
import contrib.utils.components.health.DamageType;
import core.Entity;
import core.Game;
import core.components.DrawComponent;
import core.components.PositionComponent;
import core.components.VelocityComponent;
import core.level.DungeonLevel;
import core.level.utils.DesignLabel;
import core.level.utils.LevelElement;
import core.utils.Direction;
import core.utils.Point;
import core.utils.TriConsumer;
import core.utils.components.draw.state.CharacterStateFactory;
import core.utils.components.draw.state.StateMachine;
import core.utils.components.path.SimpleIPath;
import level.DevDungeonLevel;

import java.io.IOException;
import java.util.Map;

public class RandomMobBehaviourLevel extends DevDungeonLevel {


  /**
   * Constructs a new DevDungeonLevel with the given layout, design label, and custom points.
   *
   * @param layout      The layout of the level, represented as a 2D array of LevelElements.
   * @param designLabel The design label of the level.
   * @param namedPoints A list of custom points to be added to the level.
   */
  public RandomMobBehaviourLevel(LevelElement[][] layout, DesignLabel designLabel, Map<String, Point> namedPoints) {
    super(layout, designLabel, namedPoints, "test", "description");
  }

  @Override
  protected void onFirstTick() {
    Entity monster = new Entity("monster");
    monster.add(new PositionComponent(new Point(10,10)));
    StateMachine stateMachine = new StateMachine(new SimpleIPath("character/monster/goblin"));
    monster.add(new DrawComponent(stateMachine));
    monster.add(new VelocityComponent(3f));
    TriConsumer<Entity, Entity, Direction> onEnter = (me, other, dir) -> {
      if(other.equals(Game.player().get())) {
        other.fetch(HealthComponent.class).get().receiveHit(new Damage(1, DamageType.PHYSICAL, me));
      }
    };
    CollideComponent cc = new CollideComponent(onEnter, CollideComponent.DEFAULT_COLLIDER);
    monster.add(cc);
    monster.add(new AIComponent());
    Game.add(monster);
  }

  @Override
  protected void onTick() {

  }
}
