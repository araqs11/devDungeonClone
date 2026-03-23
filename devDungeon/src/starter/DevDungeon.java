package starter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import contrib.components.CollideComponent;
import contrib.components.InventoryComponent;
import contrib.components.SkillComponent;
import contrib.crafting.Crafting;
import contrib.entities.EntityFactory;
import contrib.entities.MiscFactory;
import contrib.hud.DialogUtils;
import contrib.item.HealthPotionType;
import contrib.item.concreteItem.ItemPotionHealth;
import contrib.systems.*;
import contrib.utils.components.Debugger;
import contrib.utils.components.item.ItemGenerator;
import contrib.utils.components.skill.SkillTools;
import contrib.utils.components.skill.projectileSkill.BurningFireballSkill;
import contrib.utils.components.skill.projectileSkill.DamageProjectileSkill;
import contrib.utils.components.skill.projectileSkill.FireballSkill;
import core.Entity;
import core.Game;
import core.System;
import core.level.loader.DungeonLoader;
import core.utils.Tuple;
import core.utils.components.path.SimpleIPath;
import item.concreteItem.ItemPotionWater;
import item.concreteItem.ItemResourceBerry;
import item.concreteItem.ItemResourceMushroomRed;
import java.io.IOException;
import level.devlevel.*;
import systems.*;

/**
 * Starter class for the DevDungeon game.
 *
 * <p>Usage: run with the Gradle task {@code runDevDungeon}.
 */
public class DevDungeon {
  /**
   * Main method to start the game.
   *
   * @param args The arguments passed to the game.
   * @throws IOException If an I/O error occurs.
   */
  public static void main(String[] args) throws IOException {
    configGame();
    onSetup();

    // build and start game
    Game.run();
    Game.windowTitle("Dev Dungeon");
  }

  private static void onSetup() {
    Game.userOnSetup(
        () -> {
          DungeonLoader.addLevel(Tuple.of("tutorial", RandomMobBehaviourLevel.class));
          createSystems();
          createHero();
        });
  }

  private static void createHero() {
    Entity hero = EntityFactory.newHero();
    hero.fetch(SkillComponent.class)
        .ifPresent(
            sc -> {
              sc.removeAll();
              sc.addSkill(new FireballSkill(SkillTools::cursorPositionAsPoint));
            });
    Game.add(hero);
  }

  private static void configGame() throws IOException {
    Game.loadConfig(
        new SimpleIPath("dungeon_config.json"),
        contrib.configuration.KeyboardConfig.class,
        core.configuration.KeyboardConfig.class);
    Game.frameRate(30);
    Game.disableAudio(false);
    Game.windowTitle("DevDungeon");

    // Set up random item generator for chests and monsters
    ItemGenerator ig = new ItemGenerator();
    ig.addItem(() -> new ItemPotionHealth(HealthPotionType.randomType()), 1);
    ig.addItem(ItemPotionWater::new, 1);
    ig.addItem(ItemResourceBerry::new, 2);
    ig.addItem(ItemResourceMushroomRed::new, 2);

    MiscFactory.randomItemGenerator(ig);
    MiscFactory.randomItemGenerator(ig);
  }

  private static void createSystems() {
    Game.add(new AISystem());
    Game.add(new LevelTickSystem());
    Game.add(new CollisionSystem());
    Game.add(new HealthSystem());
    Game.add(new HealthBarSystem());
  }
}
