// package Services;

// import Enums.*;
// import Models.*;

// import java.util.*;
// import java.util.stream.*;

// public class BotService {
//     private GameObject bot;
//     private PlayerAction playerAction;
//     private GameState gameState;



//     public BotService() {
//         this.playerAction = new PlayerAction();
//         this.gameState = new GameState();
//     }

//     public GameObject getBot() {
//         return this.bot;
//     }

//     public void setBot(GameObject bot) {
//         this.bot = bot;
//     }

//     public PlayerAction getPlayerAction() {
//         return this.playerAction;
//     }

//     public void setPlayerAction(PlayerAction playerAction) {
//         this.playerAction = playerAction;

//     }

//     // method getClosestFood
//     private GameObject getClosestFood() {
//         List<GameObject> foods = getFoods();
//         GameObject closestFood = null;
//         double closestFoodDistance = Double.MAX_VALUE;
//         for (GameObject food : foods) {
//             double distance = getDistanceBetween(getBot(), food);
//             if (distance < closestFoodDistance) {
//                 closestFood = food;
//                 closestFoodDistance = distance;
//             }
//         }
//         return closestFood;
//     }

//     // method getFoods
//     private List<GameObject> getFoods() {
//         // FOOD and SuperFood
//         return this.gameState.getGameObjects().stream()
//                 .filter(gameObject -> gameObject.getGameObjectType() == ObjectTypes.FOOD
//                         || gameObject.getGameObjectType() == ObjectTypes.SUPERFOOD)
//                 .collect(Collectors.toList());
//     }

//     private void eatStrategy() {
//         GameObject closestFood = getClosestFood();
//         if (closestFood != null) {
//             playerAction.setAction(PlayerActions.FORWARD);
//             playerAction.setHeading(getHeadingBetween(closestFood));
//         }
//         else {
//             playerAction.setAction(PlayerActions.FORWARD);
//             playerAction.setHeading(getBot().currentHeading);
//         }
//     }
        
//     public void computeNextPlayerAction(PlayerAction playerAction) {
//         if (getBot().getSize() > 10) {
//                 eatStrategy();
//         }
//         else {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(getBot().currentHeading);
//             }
//         avoidDanger();
//         detectTorpedoSalvo();
//         attackStrategy();
//         fireTorpedoes();
//         this.playerAction = playerAction;
        

//     }

//     private GameObject getClosestEnemy() {
//         List<GameObject> enemies = getEnemies();
//         GameObject closestEnemy = null;
//         double closestEnemyDistance = Double.MAX_VALUE;
//         for (GameObject enemy : enemies) {
//             double distance = getDistanceBetween(getBot(), enemy);
//             if (distance < closestEnemyDistance) {
//                 closestEnemy = enemy;
//                 closestEnemyDistance = distance;
//             }
//         }
//         return closestEnemy;
//     }

//     private List<GameObject> getEnemies() {
//         // another player
//         return this.gameState.getGameObjects().stream()
//                 .filter(gameObject -> gameObject.getGameObjectType() == ObjectTypes.PLAYER)
//                 .filter(gameObject -> !gameObject.getId().equals(getBot().getId()))
//                 .collect(Collectors.toList());
//     }

//     private void attackStrategy() {
//         // get closest enemy
//         // if enemy size bigger than mybot size, runaway
//         // if enemy size smaller than mybot size, attack
//         // if enemy size equal to mybot size, attack

//         GameObject closestEnemy = getClosestEnemy();
//         if (closestEnemy != null) {
//             if (closestEnemy.getSize() > getBot().getSize()) {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(getHeadingBetween(closestEnemy) + 180);
//             }
//             else {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(getHeadingBetween(closestEnemy));
//             }
//         }
//         else {
//             playerAction.setAction(PlayerActions.FORWARD);
//             playerAction.setHeading(getBot().currentHeading);
//         }

//     }
//     private void avoidDanger() {
//         // move away from danger
//         var gasClouds = gameState.getGameObjects().stream().filter(gameObject -> gameObject.getGameObjectType() == ObjectTypes.GAS_CLOUD).collect(Collectors.toList());
//         var asteroidField = gameState.getGameObjects().stream().filter(gameObject -> gameObject.getGameObjectType() == ObjectTypes.ASTEROID_FIELD).collect(Collectors.toList());
//         var danger = Stream.concat(gasClouds.stream(), asteroidField.stream()).collect(Collectors.toList());
//         if (!danger.isEmpty()) {
//             // move away from danger
//             var closestDanger = danger.get(0);
//             var distance = getDistanceBetween(bot, closestDanger);
//             if (distance < 100) {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(getHeadingBetween(closestDanger) + 180);
//             }
//         }
//     }


//     public void detectTorpedoSalvo() {
//         var torpedoSalvo = gameState.getGameObjects().stream()
//                 .filter(gameObject -> gameObject.getGameObjectType() == ObjectTypes.TORPEDO_SALVO)
//                 .collect(Collectors.toList());
//         if (torpedoSalvo.size() > 0) {
//             // if there is a torpedoSalvo, move away from it
//             var closestTorpedoSalvo = torpedoSalvo.get(0);
//             var distance = getDistanceBetween(bot, closestTorpedoSalvo);
//             if (distance < 100) {
//                 // determine activate shield or move away from it
//                 if (getShield() != null) {
//                     playerAction.setAction(PlayerActions.ACTIVATE_SHIELD);
//                     playerAction.setHeading(getHeadingBetween(closestTorpedoSalvo) + 180);
//                 } else {
//                     playerAction.setAction(PlayerActions.FORWARD);
//                     playerAction.setHeading(getHeadingBetween(closestTorpedoSalvo) + 180);
//                 }
//             } else {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(0);
//             }
//         }
//     }

//     public void fireTorpedoes() {
//         // fire torpedo if enemy is in range
//         var enemies = getEnemies();
//         if (enemies.size() > 0) {
//             var closestEnemy = enemies.get(0);
//             var distance = getDistanceBetween(bot, closestEnemy);
//             if (distance < 100) {
//                 playerAction.setAction(PlayerActions.FIRE_TORPEDOES);
//                 playerAction.setHeading(getHeadingBetween(closestEnemy));
//             }
//         }
//     }


//     public void protectBot() {
//         // check if my bot is in danger if size decrease 
//         // if my bot is in danger, ActiveShield, Teleport, StartAfterBurner, StopAfterburner, Forward
//         if (bot.getSize() < 100) {
//             var otherBots = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id != bot.id).collect(Collectors.toList());
//             var otherBotsSize = otherBots.stream().mapToInt(gameObject -> gameObject.getSize()).sum();
//             if (otherBotsSize > bot.getSize()) {
//                 if (getShield() != null) {
//                     playerAction.setAction(PlayerActions.ACTIVATE_SHIELD);
//                 } else {
//                     playerAction.setAction(PlayerActions.TELEPORT);
//                     playerAction.setHeading(getHeadingBetween(otherBots.get(0)));
//                 }
//             } else {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(0);
//             }
//         }
//     }

//     private GameObject getShield() {
//         for (GameObject gameObject : gameState.getPlayerGameObjects()) {
//             if (gameObject.getGameObjectType() == ObjectTypes.SHIELD) {
//                 return gameObject;
//             }
//         }
//         return null;
//     }

//     public GameState getGameState() {
//         return this.gameState;
//     }

//     public void setGameState(GameState gameState) {
//         this.gameState = gameState;
//         updateSelfState();
//     }

//     private void updateSelfState() {  
//         Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
//         optionalBot.ifPresent(bot -> this.bot = bot);
//     }

//     private double getDistanceBetween(GameObject object1, GameObject object2) { // distance between two objects
//         var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
//         var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
//         return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
//     }

//     private int getHeadingBetween(GameObject otherObject) { // heading between two objects
//         var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
//                 otherObject.getPosition().x - bot.getPosition().x));
//         return (direction + 360) % 360;
//     }

//     private int toDegrees(double v) {
//         return (int) (v * (180 / Math.PI));
//     }
// }




package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class BotService {
    private GameObject bot;
    private GameState gameState;
    private GameObject target;
    private GameObject worldCenter;
    private PlayerAction playerAction;
    private PlayerAction lastAction;
    private int timeSinceLastAction;
    private boolean targetIsPlayer = false;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
    }

    public GameObject getBot() {
        return this.bot;
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public void computeNextPlayerAction(PlayerAction playerAction) {
        PlayerActions actionID = PlayerActions.FORWARD;
        playerAction.action = actionID;
        int heading = new Random().nextInt(360);
        playerAction.heading = heading;

        List<GameObject> playerGameObjects;
        playerGameObjects = gameState.getPlayerGameObjects();

        if (!playerGameObjects.contains(bot)) {
            System.out.println("I am no longer in the game state, and have been consumed");
        }

        if (target == null || target == worldCenter) {
            System.out.println("No Current Target, resolving new target");
            heading = resolveNewTarget();
        } else {
            GameObject defaultTarget = target;
            GameObject targetWithNewValues = gameState.getGameObjects()
                .stream().filter(item -> item.getId() == target.getId())
                .findFirst().orElseGet(() -> defaultTarget);
            if (targetWithNewValues == defaultTarget) {
                System.out.println("Old Target Invalid, resolving new target");
                heading = resolveNewTarget();
            } else {
                System.out.println("Previous Target exists, updating resolution");
                target = targetWithNewValues;
                if (target.size < bot.size) {
                    heading = getHeadingBetween(target);
                } else {
                    System.out.println("Previous Target larger than me, resolving new target");
                    heading = resolveNewTarget();
                }
            }
        }

        Position centerPosition = new Position();
        var distanceFromWorldCenter = getDistanceBetween(bot,new GameObject(null, null, null, null, centerPosition, null, null, null, null, null, null));

        World world = gameState.getWorld();
        if (world.radius == null) {
            world.radius = 0;
        } else {
            world.radius = world.radius;
        }
        if (distanceFromWorldCenter + (1.5 * bot.size) > world.radius) {
            worldCenter = new GameObject(null, null, null, null, centerPosition, null, null, null, null, null, null);
            heading = getHeadingBetween(worldCenter);
            System.out.println("Near the edge, going to the center");
            target = worldCenter;
        }

        if ((targetIsPlayer) && bot.size > 20 && bot.torpedoSalvoCount > 0)
            {
                System.out.println("Firing Torpedoes at target");
                actionID = PlayerActions.FIRE_TORPEDOES;
            }

        playerAction.action = actionID;
        playerAction.heading = heading;

        lastAction = playerAction;
        timeSinceLastAction = 0;

        System.out.println("Player action:" + playerAction.action + ":" + playerAction.heading);

        this.playerAction = playerAction;
    }  

    private int resolveNewTarget() {
        var heading = new Random().nextInt(360);
        if (!gameState.getGameObjects().isEmpty()) {
            var nearestFood = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD
                    || item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
            var nearestPlayer = gameState.getPlayerGameObjects()
                    .stream().filter(player -> player.getId() != bot.getId())
                    .sorted(Comparator
                            .comparing(player -> getDistanceBetween(bot, player)))
                    .collect(Collectors.toList());

            // JIKA UKURAN PLAYER TERDEKAT LEBIH BESAR DARI BOT, MAKA JAUHI PLAYER TERDEKAT                                                                                        
            if (nearestPlayer.get(0).size > bot.getSize()) {
                playerAction.action = PlayerActions.FIRE_TORPEDOES;
                System.out.println("cok musuhnya gede bgt");
                targetIsPlayer = false;
                // ketika musuhnya masih besar
                if(nearestPlayer.get(0).size > bot.getSize()){
                    playerAction.action = PlayerActions.FORWARD;    
                    playerAction.heading = getHeadingBetween(nearestFood.get(0));
                    System.out.println("duh musuhnya masih gede");}
                    targetIsPlayer = false;
                 //ketika musuhnya mengecil
                if(nearestPlayer.get(0).size < bot.getSize()) {
                    playerAction.action = PlayerActions.FIRE_TORPEDOES;
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(nearestPlayer.get(0));
                    System.out.println("yey doi mengecil");
                    targetIsPlayer = true;

            }
            else if ((nearestPlayer.get(0)).size < bot.size) {
                playerAction.action = PlayerActions.FIRE_TORPEDOES;
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = getHeadingBetween(nearestPlayer.get(0));
                System.out.println("mari makan lawan");
                targetIsPlayer = true;
            }
            else if (nearestFood != null) {
                heading = getHeadingBetween(nearestFood.get(0));
                target = nearestFood.get(0);
                targetIsPlayer = false;
            }

            else {
                target = worldCenter;
                heading = getHeadingBetween(worldCenter);
                targetIsPlayer = false;

            }

            if (target == worldCenter) {
                heading = getHeadingBetween(nearestPlayer.get(0));
            }

            hindariObjekJahat();
        }
        }   
        return heading;
        
    }

    private void hindariObjekJahat() {
        var badList = gameState.getGameObjects()
                .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GAS_CLOUD)
                .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                .collect(Collectors.toList());
        badList.addAll(gameState.getGameObjects()
                .stream().filter(item -> item.getGameObjectType() == ObjectTypes.ASTEROID_FIELD)
                .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                .collect(Collectors.toList()));
        badList.addAll(gameState.getGameObjects()
                .stream().filter(item -> item.getGameObjectType() == ObjectTypes.TORPEDO_SALVO)
                .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                .collect(Collectors.toList()));

        if (badList.size() > 0) {
            var badObject = badList.get(0);
            var oppositeHeading = getOppositeHeading(badObject);
            var distance = getDistanceBetween(bot, badObject);
            if (distance < 100) {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = oppositeHeading;
            }
        }
    }

    private int getAttackerResolution(GameObject attacker, GameObject closestFood) {
        if (closestFood == null) {
            return getOppositeHeading(attacker);
        }

        var distanceToAttacker = getDistanceBetween(bot, attacker);
        var distanceBetweenAttackerAndFood = getDistanceBetween(attacker, closestFood);

        if ((distanceToAttacker > attacker.speed) && (distanceBetweenAttackerAndFood > distanceToAttacker)) {
            return getHeadingBetween(closestFood);
        }
        else {
            return getOppositeHeading(attacker);
        }
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    private double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    private int getHeadingBetween(GameObject otherObject) {
        System.out.println("test3");
        int direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int getOppositeHeading(GameObject otherObject) {
        return toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
        otherObject.getPosition().x - bot.getPosition().x));
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
}

// package Services;

// import Enums.*;
// import Models.*;

// import java.util.*;
// import java.util.stream.*;

// import org.w3c.dom.ranges.Range;

// public class BotService {
//     private GameObject bot;
//     private GameObject _closestFood;
//     private PlayerAction playerAction;
//     private GameState gameState;
//     private int _searchRadiusModifier = 200;
//     private boolean _afterburnerOn = false;
//     private PlayerAction lastAction;
//     private int timeSinceLastAction;
//     private GameObject _target;
//     private boolean _targetIsPlayer = false;
//     private GameObject _worldCenter;

//     public BotService() {
//         this.playerAction = new PlayerAction();
//         this.gameState = new GameState();
//     }


//     public GameObject getBot() {
//         return this.bot;
//     }

//     public void setBot(GameObject bot) {
//         this.bot = bot;
//     }

//     public PlayerAction getPlayerAction() {
//         return this.playerAction;
//     }

//     public void setPlayerAction(PlayerAction playerAction) {
//         this.playerAction = playerAction;
//     }

//     public void computeNextPlayerAction(PlayerAction playerAction) {
//         playerAction.heading = new Random().nextInt(360);
//         playerAction.action = PlayerActions.FORWARD;
//         _worldCenter = new GameObject(null, null, null, null, null, null);
//         _worldCenter.setPosition(new Position(500, 500));
        
//         if (_target == null || _target == _worldCenter) {
//             System.out.println("No Current Target, resolving new target");
//             resolveNewTarget();
//             if (getClosestPlayer().size > bot.size || ) {
//                 _targetIsPlayer = true;
//             }
//         } else {
//             resolveNewTarget();
//             System.out.println("Current Target: " + _target.getId());
//         }
        
//     }

//     private GameObject getClosesTorpedoSalvo() {
//         var closestTorpedoSalvo = gameState.getGameObjects().stream()
//             .filter(x -> x.getGameObjectType() == ObjectTypes.TORPEDO_SALVO)
//             .min(Comparator
//                 .comparing(x -> getDistanceBetween(x, bot)))
//             .orElse(null);

//         return closestTorpedoSalvo;
//     }
        
//     private GameObject getClosestFood() {
//         var closestFood = gameState.getGameObjects().stream()
//             .filter(x -> x.getGameObjectType() == ObjectTypes.FOOD)
//             .min(Comparator
//                 .comparing(x -> getDistanceBetween(x, bot)))
//             .orElse(null);

//         return closestFood;
//     }

//     private GameObject getClosestPlayer() {
//         var closestPlayer = gameState.getGameObjects().stream()
//             .filter(x -> x.getGameObjectType() == ObjectTypes.PLAYER)
//             .filter(x -> x.getId() != bot.getId())
//             .min(Comparator
//                 .comparing(x -> getDistanceBetween(x, getBot())))
//             .orElse(null);

//         return closestPlayer;
//     }

//     private GameObject getClosestAttacker() {
//         var closestAttacker = gameState.getGameObjects().stream()
//             .filter(x -> x.getGameObjectType() == ObjectTypes.PLAYER)
//             .filter(x -> x.getId() != bot.getId())
//             .filter(x -> getDistanceBetween(x, bot) < x.getSpeed())
//             .min(Comparator
//                 .comparing(x -> getDistanceBetween(x, getBot())))
//             .orElse(null);

//         return closestAttacker;
//     }

//     private void resolveNewTarget() {
//         System.out.println("New Target: " + _target.getId());

//         if (getClosestFood() != null) {
//             _target = getClosestFood();
//             _targetIsPlayer = false;
//             playerAction.setAction(PlayerActions.FORWARD);
//             playerAction.setHeading(getHeadingBetween(getClosestFood()));
//         }
//         else if (getClosestPlayer() != null) {
//             _target = getClosestPlayer();
//             _targetIsPlayer = true;
//             playerAction.setAction(PlayerActions.FORWARD);
//             playerAction.setHeading(getHeadingBetween(getClosestPlayer()));
//         }
//         else if (getClosestAttacker() != null) {
//             _target = getClosestAttacker();
//             _targetIsPlayer = true;
//             if (_target.getSize() > bot.getSize() && _targetIsPlayer==true) {
//                 playerAction.setAction(PlayerActions.FORWARD);
//                 playerAction.setHeading(GetAttackerResolution(bot, getClosestAttacker(), getClosestFood())); 
//             } else {
//                 playerAction.setHeading(getHeadingBetween(getClosestAttacker()));
//             }
//         }
//         else {
//             _target = _worldCenter;
//             _targetIsPlayer = false;
//         }

//     }

//     public void protectBot() {
//         if (getClosesTorpedoSalvo() != null) {
//             playerAction.setAction(PlayerActions.FORWARD);
//             playerAction.setAction(PlayerActions.ACTIVATE_SHIELD);
//         }
//     }

//     private int GetAttackerResolution(GameObject bot, GameObject attacker, GameObject closestFood) {
//         if (closestFood == null) {
//             return GetOppositeDirection(bot, attacker);
//         }

//     var distanceToAttacker = getDistanceBetween(bot, attacker);
//     var distanceBetweenAttackerAndFood = getDistanceBetween(attacker, closestFood);

//     if (distanceToAttacker > attacker.getSpeed() && distanceBetweenAttackerAndFood > distanceToAttacker) {
//         System.out.println("Atk is far, going for food");
//         return getHeadingBetween(closestFood);
//     }
//     else {
//         System.out.println("Running");
//         return GetOppositeDirection(bot, attacker);
//     }
//     }

//     private int GetOppositeDirection(GameObject gameObject1, GameObject gameObject2) {
//         return toDegrees(Math.atan2(gameObject2.getPosition().y - gameObject1.getPosition().y, gameObject2.getPosition().x - gameObject1.getPosition().x));
//     }

//     public GameState getGameState() {
//         return this.gameState;
//     }

//     public void setGameState(GameState gameState) {
//         this.gameState = gameState;
//         updateSelfState();
//     }

//     private void updateSelfState() {
//         Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
//         optionalBot.ifPresent(bot -> this.bot = bot);
//     }

//     private double getDistanceBetween(GameObject object1, GameObject object2) {
//         var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
//         var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
//         return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
//     }

//     private int getHeadingBetween(GameObject otherObject) {
//         var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,otherObject.getPosition().x - bot.getPosition().x));
//         return (direction + 360) % 360;
//     }

//     private int toDegrees(double v) {
//         return (int) (v * (180 / Math.PI));
//     }


// }
