Hello! This is a plugin I made for 1.21.4! Basically it lets you customize mobs with a config file and spawn them. There is also a feature that lets you make groups from the custom mobs. 

**Current Features:**

-*Config File customizations*:

-Attributes: which are health, damage, knockback resistance, and movement speed.
-Equipment: including armor and offhand
-Mob Name
      
Example on how to setup a mob
```
entities:
  test:
    mob_type: HUSK
    name: '&c&lThe Scary'
    health: 10
    equipment:
      helmet: LEATHER_HELMET
      chestplate: DIAMOND_CHESTPLATE
      leggings: IRON_LEGGINGS
      boots: IRON_BOOTS
      main_hand: IRON_SWORD
      off_hand: SHIELD
```

-*Spawning*:
      Syntax: /spawn <id> [quantity (optional)].
      
-*Group spawner*:
       I wanted to have a squad system for the mobs, because that would be cool so I made it so you can bundle the custom mobs in a group with varying quantity. So far I've only added marching in a square formation. My objective is to have the mobs be "smarter" by working together as a group. You'll also notice that theres a leader thing that is not optional. That is for positioning or something.

Example in config file
```
group:
  testGroup:
    members:
      test: 40
      test1: 10
    leader: testLeader
```
Syntax: /group 

**PLANNED FEATURES**

-Bossfights: This ones simple, just add a bossfight tag that puts a bossbar on the hud.

-Skript compatibility: So you can automate spawns or link it with some other function.

      
