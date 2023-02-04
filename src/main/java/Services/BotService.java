package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class BotService {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;

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
        playerAction.heading = new Random().nextInt(360);

        if (!this.gameState.getGameObjects().isEmpty()) {
            var foodList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            playerAction.heading = getHeadingBetween(foodList.get(0));
        }
        this.playerAction = playerAction;

        if (!this.gameState.getPlayerGameObjects().isEmpty()) {
            var enemyList = gameState.getPlayerGameObjects()
                    .stream().filter(enemy -> enemy.getGameObjectType() == ObjectTypes.PLAYER)
                    .sorted(Comparator
                            .comparing(enemy -> getDistanceBetween(bot, enemy)))
                    .collect(Collectors.toList());

            if (enemyList.size() > getBot().getSize()) {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = getHeadingBetween(enemyList.get(0));
            } else {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = getHeadingBetween(enemyList.get(0));
                if (getDistanceBetween(bot, enemyList.get(0)) < 100) {
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(enemyList.get(0)) + 180;
                
                } else {
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(enemyList.get(0));
                }
            }
        }

        if (!this.gameState.getGameObjects().isEmpty()) {
            var obstacleList = gameState.getGameObjects()
                    .stream().filter(obstacle -> obstacle.getGameObjectType() == ObjectTypes.GAS_CLOUD
                    || obstacle.getGameObjectType() == ObjectTypes.ASTEROID_FIELD)
                    .sorted(Comparator
                            .comparing(obstacle -> getDistanceBetween(bot, obstacle)))
                    .collect(Collectors.toList());

            if (obstacleList.size() > 0) {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = getHeadingBetween(obstacleList.get(0)) + 180;
            } else {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = getHeadingBetween(obstacleList.get(0));
            }

        
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
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }


}
