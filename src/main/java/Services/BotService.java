package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;
import java.util.Scanner;

public class BotService {
    private GameObject bot;
    private GameState gameState;
    private GameObject target;
    private GameObject worldCenter;
    private PlayerAction playerAction;
    private PlayerAction lastAction;
    private int timeSinceLastAction;
    private boolean targetIsPlayer = false;
    private boolean afterBurner = false;
    private boolean tembakTeleport = false;
    private int teleportTick = 0;
    private GameObject playerTerdekat;

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
            playerAction.action = PlayerActions.FORWARD;
            // playerAction.heading = new Random().nextInt(360);
            //ini untuk superfood yang nanti dia harus cari food terdekat
            if (!this.gameState.getGameObjects().isEmpty()) {
                var goodList = gameState.getGameObjects()
                        .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD
                        || item.getGameObjectType() == ObjectTypes.SUPERFOOD
                        || item.getGameObjectType() == ObjectTypes.SUPERNOVAPICKUP
                        || item.getGameObjectType() == ObjectTypes.WORMHOLE)
                        .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                        .collect(Collectors.toList());
                var badList = gameState.getGameObjects()
                        .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD
                        || item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD
                        || item.getGameObjectType() == ObjectTypes.TORPEDOSALVO
                        || item.getGameObjectType() == ObjectTypes.WORMHOLE
                        )
                        .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                        .collect(Collectors.toList());
                var enemyList = gameState.getPlayerGameObjects()
                        .stream().filter(enemy -> enemy.getGameObjectType() == ObjectTypes.PLAYER && enemy.id != bot.id )
                        .sorted(Comparator
                        .comparing(enemy -> getDistanceBetween(bot, enemy)))
                        .collect(Collectors.toList());
                playerTerdekat = enemyList.get(0);

                //ByOne
                if (enemyList.size() == 1){
                    //ketika bertemu dgn musuh lebih besar
                    if (enemyList.get(0).size >= bot.getSize() && bot.getSize() > 50) {
                        if(afterBurner = true){
                            afterBurner = false;
                            // System.out.println("matiin afterburner");
                            playerAction.action = PlayerActions.STOPAFTERBURNER;
                        }
                        playerAction.heading = getHeadingBetween(enemyList.get(0));
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        playerAction.heading = getHeadingBetween(enemyList.get(0)) + 90 % 360;
                        playerAction.action = PlayerActions.FORWARD;
                        // musuh masih lebih besar setelah ditembak
                        if(enemyList.get(0).size >= bot.getSize()){
                            playerAction.action = PlayerActions.FORWARD;    
                            playerAction.heading = getHeadingBetween(enemyList.get(0)) + 90 %360;
                            // System.out.println("musuhnya masi besar");
                            // jaraknya musuh gede deket banget
                            if(bot.getSize() > 60 && getDistanceBetween(bot, enemyList.get(0)) - bot.getSize() - enemyList.get(0).size < 100){
                                playerAction.heading = getHeadingBetween(enemyList.get(0)) + 90 % 360;
                                playerAction.action = PlayerActions.FORWARD;
                                playerAction.heading = getHeadingBetween(enemyList.get(0));
                                playerAction.action = PlayerActions.FIRETORPEDOES;
                                playerAction.action = PlayerActions.FIRETORPEDOES;
                                playerAction.action = PlayerActions.FIRETORPEDOES;
                                afterBurner = true;
                                // System.out.println("lariiii");
                            }
                        } 

                        // musuh mengecil
                        else if (enemyList.get(0).size < bot.getSize()) {
                            playerAction.heading = getHeadingBetween(enemyList.get(0));
                            // System.out.println("musuhnya mengecil mari makan");
                        }
                    
                    }

                    // ketika musuhnya kecil
                    if(enemyList.get(0).size < bot.getSize()) {
                        playerAction.action = PlayerActions.FORWARD;
                        playerAction.heading = getHeadingBetween(enemyList.get(0));
                        // System.out.println("yey doi kecil"); 
                        if (getDistanceBetween(bot, enemyList.get(0)) < 50 && tembakTeleport == false) {
                            playerAction.heading = getHeadingBetween(enemyList.get(0));
                            playerAction.action = PlayerActions.FIRETELEPORT;
                            tembakTeleport = true;
                        }
                        else if (tembakTeleport == true) {
                            teleportTick++;
                            System.out.println("tik tik: "+teleportTick);
                        }
                        else if (teleportTick == 5) {
                            playerAction.heading = getHeadingBetween(enemyList.get(0));
                            playerAction.action = PlayerActions.TELEPORT;
                            // System.out.println("teleport");
                            tembakTeleport = false;
                            teleportTick = 0;
                        }
                    }

                    // nembak musuh yang deket pas byone
                    if(getDistanceBetween(bot,enemyList.get(0))-bot.getSize()-enemyList.get(0).getSize() < 100){
                        playerAction.heading = getHeadingBetween(enemyList.get(0));
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        // System.out.println("duh ada musuh");
                    }

                    // nyalain shield ketika ada musuh yang nembakin fire torpedo
                    if (bot.shieldCount > 0 && playerTerdekat.torpedoSalvoCount > 0 && getDistanceBetween(bot, playerTerdekat) - 2 * playerTerdekat.size < 100) {
                        playerAction.action = PlayerActions.ACTIVATESHIELD;
                        // System.out.println("shield aktif");
                    }
                }
                //makan makanan
                if(getDistanceBetween(bot,goodList.get(0))<10+bot.getSize()){
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(goodList.get(0));
                    // System.out.println("makan");
                }
                //ketika bertemu dgn musuh lebih besar
                if (enemyList.get(0).size >= bot.getSize()) {
                    if(afterBurner = true){
                        afterBurner = false;
                        // System.out.println("matiin afterburner");
                        playerAction.action = PlayerActions.STOPAFTERBURNER;
                    }
                    // ketika musuhnya masih besar
                    if(enemyList.get(0).size >= bot.getSize()){
                        playerAction.action = PlayerActions.FORWARD;    
                        playerAction.heading = getOppositeBetween(enemyList.get(0));// + 69 % 360;
                        // System.out.println("musuhnya besar");
                        if(bot.getSize() > 85 && getDistanceBetween(bot, enemyList.get(0)) - bot.getSize() - enemyList.get(0).size < 150){
                            playerAction.heading = getHeadingBetween(enemyList.get(0)) + 200 % 360;
                            playerAction.action = PlayerActions.STARTAFTERBURNER;
                            afterBurner = true;
                            // System.out.println("nyalain afterburner");
                        }
                        //ketika musuhnya mengecil
                        if(enemyList.get(0).size < bot.getSize()) {
                            playerAction.action = PlayerActions.FIRETORPEDOES;
                            playerAction.action = PlayerActions.FORWARD;
                            playerAction.heading = getHeadingBetween(enemyList.get(0));
                            // System.out.println("yey doi mengecil");
                        }
                    }   
                    
                } 
                System.out.println("enemy =" + enemyList.get(0).size + "    bot = " + bot.getSize());
                //ketika dpt musuh lbh kecil
                if (enemyList.get(0).size < bot.getSize()) {
                    playerAction.action = PlayerActions.FIRETORPEDOES;
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(enemyList.get(0));
                    // System.out.println("mari makan lawan");
                }
                Position centerPosition = new Position();
                var distanceFromWorldCenter = getDistanceBetween(bot,new GameObject(null, null, null, null, centerPosition, null, null, null, null, null, null));

                World world = gameState.getWorld();
                if (world.radius == null) {
                    world.radius = 0;
                } else {
                    world.radius = world.radius;
                }
                if (distanceFromWorldCenter + (2 * bot.size) > world.radius) {
                    worldCenter = new GameObject(null, null, null, null, centerPosition, null, null, null, null, null, null);
                    playerAction.heading = getHeadingBetween(worldCenter);
                    // System.out.println("duh jangan sampe ke luar ring");
                    target = worldCenter;
                }
                // ini untuk mengindarin yang jelek jelek (asteroid, gascloud)
                if (getDistanceBetween(bot, badList.get(0)) - badList.get(0).size - bot.getSize() < 100) {
                    // playerAction.heading = (getHeadingBetween(goodList.get(0)));
                    int i = 0;
                    while(getDistanceBetween(badList.get(0),goodList.get(i)) < badList.get(0).size){
                        i++;
                    }
                    playerAction.heading = getHeadingBetween(goodList.get(i));
                    playerAction.action = PlayerActions.FORWARD;
                    // System.out.println("duh ada gas cloud");
                    // nembak kalo musuhnya deket
                    if(getDistanceBetween(bot,enemyList.get(0))-bot.getSize()-enemyList.get(0).getSize()<100){
                        playerAction.heading = getHeadingBetween(enemyList.get(0));
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                        // System.out.println("duh ada musuh");
                        //ketika musuhnya mengecil
                        if(enemyList.get(0).size < bot.getSize()) {
                            playerAction.action = PlayerActions.FIRETORPEDOES;
                            playerAction.action = PlayerActions.FORWARD;
                            playerAction.heading = getHeadingBetween(enemyList.get(0));
                            // System.out.println("yey doi mengecil");
                        }
                    }
                    if (getDistanceBetween(bot, enemyList.get(0)) < 50 && bot.shieldCount > 0 && playerTerdekat.torpedoSalvoCount > 0) {
                        playerAction.action = PlayerActions.ACTIVATESHIELD;
                        // System.out.println("duh ada torpedo, nyalain shield y");
                    }
                }
                
            }
            this.playerAction = playerAction;
        
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

    private int getOppositeBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 90) % 360;
    }

    private int getHeadingBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
}