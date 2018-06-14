package listeners;

import config.ApplicationConstants;
import config.MonsterConstants;
import database.PlayerDatabase;
import handlers.CombatHandler;
import handlers.TrainingHandler;
import models.CombatResult;
import models.Monster;
import models.Player;
import models.Stamina;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        }
        else {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                    event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay());
        }

        if(event.getAuthor().isBot()){
            return;
        }
        handleCommand(event);
    }

    public void handleCommand(MessageReceivedEvent event){
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.

        String msg = message.getContentDisplay().toLowerCase();
        String[] msgArr = msg.split(" ");

        if(msgArr.length == 0 || !msgArr[0].startsWith("!"))
            return;

        PlayerDatabase playerDatabase = new PlayerDatabase();
        Player player;
        Stamina curStamina;
        switch(msgArr[0]){
            case "!profile":
                if(msgArr.length == 1){
                    player = grabPlayer(playerDatabase, author.getId());
                    if(player.getIntelligence() < 100){
                        channel.sendMessage(author.getName() + "'s profile: \n" + player.toString()).queue();
                    }
                } else{
                    Player mentionedPlayer = grabMentionedPlayer(playerDatabase, message, channel, "profile");

                    if(mentionedPlayer != null){
                        channel.sendMessage(mentionedPlayer.toString()).queue();
                    }
                }

                break;
            case "!help":
                channel.sendMessage(ApplicationConstants.ALL_COMMANDS).queue();
                break;
            case "!train":
                if(msgArr.length < 3){
                    channel.sendMessage(ApplicationConstants.ALL_COMMANDS).queue();
                }else{
                    String arg2 = msgArr[1];
                    try{
                        int numTimesToTrain = Integer.parseInt(msgArr[2]);

                        if(numTimesToTrain < 1 || numTimesToTrain > 20){
                            channel.sendMessage(author.getName() + ", please type in a number between 1 and 20.").queue();
                        } else{
                            player = grabPlayer(playerDatabase, author.getId());
                            curStamina = playerDatabase.retreivePlayerStamina(author.getId());
                            TrainingHandler trainingHandler = new TrainingHandler(player, curStamina, channel, playerDatabase);

                            if(arg2.equals("speed")){
                                trainingHandler.trainSpeed(numTimesToTrain);
                            }else if(arg2.equals("power")){
                                trainingHandler.trainPower(numTimesToTrain);
                            }else if(arg2.equals("strength")){
                                trainingHandler.trainStrength(numTimesToTrain);
                            } else{
                                channel.sendMessage(author.getName() + ", invalid argument. Failed to train: " + arg2 + "\n" + ApplicationConstants.ALL_COMMANDS).queue();
                            }
                    }
                    } catch(Exception e){
                        channel.sendMessage(author.getName() + ", please type in a number between 1 and 20.").queue();
                    }
                }
                break;
            case "!stamina":
               curStamina = playerDatabase.retreivePlayerStamina(author.getId());
                channel.sendMessage(author.getName() + ", you currently have " + curStamina.getStamina() + " stamina.").queue();
                break;
            case "!fight":
                CombatHandler combatHandler = new CombatHandler();
                if(msgArr.length < 2){
                    channel.sendMessage(author.getName() + ", Please mention the name of the user you wish to fight with !fight @name.").queue();
                } else{
                    Player mentionedPlayer = grabMentionedPlayer(playerDatabase, message, channel, "fight");

                    if(mentionedPlayer != null){
                        player = grabPlayer(playerDatabase, author.getId());
                        CombatResult pvpResults = combatHandler.fightPlayer(player, mentionedPlayer);
                        channel.sendMessage(pvpResults.getCombatResultString() + "\n" + pvpResults.getEntityOneStats()).queue();
                    }
                }
                break;
            case "!hunt":
                CombatHandler handler = new CombatHandler();
                if(msgArr.length < 3){
                    channel.sendMessage(author.getName() + ", Please type the name of the monster you wish to hunt and the # of times with \"!hunt name 1\". !monsters for list of monsters.").queue();
                }

                String inputtedName = msgArr[1];
                Monster monster = identifyMonster(inputtedName);

                if(monster == null){
                    channel.sendMessage(author.getName() + ", " + inputtedName + " is not a valid monster name. Please type a valid name of the monster you wish to hunt with !!hunt monster-name. !monsters for list of monsters.").queue();
                } else{
                    curStamina = playerDatabase.retreivePlayerStamina(author.getId());
                    player = grabPlayer(playerDatabase, author.getId());

                    try{
                        int numTimesToHunt = Math.min(Integer.parseInt(msgArr[2]), curStamina.getStamina());

                        if(numTimesToHunt == 0){
                            channel.sendMessage(author.getName() + ", You are too tired to hunt monsters. You recover 1 stamina every 5 minutes.").queue();
                            break;
                        }

                        curStamina.setStamina(curStamina.getStamina() - numTimesToHunt);
                        CombatResult results = handler.fightMonster(player, monster, numTimesToHunt);

                        playerDatabase.insertPlayer(player);
                        playerDatabase.insertPlayerStamina(curStamina);

                        channel.sendMessage(results.getCombatResultString().toString() + "\n " + results.getEntityOneStats()).queue();

                    } catch(Exception e){
                        System.out.println(e.getMessage());
                        channel.sendMessage(author.getName() + ", Please type a valid number of times you wish to hunt that monster with \"!hunt name 1\". \"!monsters\" for list of monsters.").queue();
                    }
                }
                break;
            case "!monsters":
                channel.sendMessage("Available monsters to !hunt: slime (lvl 1), spider (lvl 5), goblin (lvl 10), kobold (lvl 20), orc (lvl 30), and ogre (lvl 50").queue();
                break;
            default:
                channel.sendMessage(author.getName() + ", Command not recognized: " + message.getContentDisplay() + "\n" + ApplicationConstants.ALL_COMMANDS).queue();
        }
    }

    public Player grabPlayer(PlayerDatabase playerDatabase, String id){
        Player player = playerDatabase.selectPlayer(id);
        if(player == null){
            Player newPlayer = new Player(id);
            playerDatabase.insertPlayer(newPlayer);
            player = newPlayer;

            Stamina stamina = new Stamina(id);
            playerDatabase.insertPlayerStamina(stamina);
        }

        return player;
    }

    public Player grabMentionedPlayer(PlayerDatabase playerDatabase, Message message, MessageChannel channel, String command){
        List<User> mentionedList = message.getMentionedUsers();
        if(mentionedList.size() != 1){
            channel.sendMessage(String.format("%s, please mention the name of the user you wish to interact with !%s @name", message.getAuthor().getName(), command )).queue();
            return null;
        }

        User userMentioned = mentionedList.get(0);
        Player player = playerDatabase.selectPlayer(userMentioned.getId());

        if(player == null){
            channel.sendMessage(message.getAuthor().getName() + ", the mentioned user does not play this awesome game. You should get that person to play.").queue();
            return null;
        }
        return player;
    }

    public Monster identifyMonster(String monsterString){
        switch(monsterString.toLowerCase()){
            case "slime":
                return MonsterConstants.SLIME;
            case "spider":
                return MonsterConstants.SPIDER;
            case "goblin":
                return MonsterConstants.GOBLIN;
            case "kobold":
                return MonsterConstants.KOBOLD;
            case "orc":
                return MonsterConstants.ORC;
            case "ogre":
                return MonsterConstants.OGRE;
        }

        return null;
    }
}
