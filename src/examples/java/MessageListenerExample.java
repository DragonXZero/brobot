/*
 *     Copyright 2015-2018 Austin Keener & Michael Ritter & Florian Spieß
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MessageListenerExample extends ListenerAdapter
{
    private static ReverseLikeListLookup rev;
    private static Map<String, Integer> eggCount;
    private static Map<String, Integer> kidCount;

    /**
     * This is the method where the program starts.
     */
    public static void main(String[] args)
    {
        //We construct a builder for a BOT account. If we wanted to use a CLIENT account
        // we would use AccountType.CLIENT
        try
        {
            JDA jda = new JDABuilder("NTcyODkzMTIxOTY5NjUxNzI0.XMjA3A.BehNp-Tdc7CLh9V058zQHfHwA6I")         // The token of the account that is logging in.
                    .addEventListener(new MessageListenerExample())  // An instance of a class that will handle events.
                    .build();
            jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building JDA!");

            rev = new ReverseLikeListLookup("/Users/john.vento/Desktop/JDA-master/src/examples/likelists.txt");
            System.out.println("Finished Building Like Lists!");

            eggCount = new HashMap<>();
            kidCount = new HashMap<>();
        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            //Due to the fact that awaitReady is a blocking method, one which waits until JDA is fully loaded,
            // the waiting can be interrupted. This is the exception that would fire in that situation.
            //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
            // you use awaitReady in a thread that has the possibility of being interrupted (async thread usage and interrupts)
            e.printStackTrace();
        }
    }

    /**
     * NOTE THE @Override!
     * This method is actually overriding a method in the ListenerAdapter class! We place an @Override annotation
     *  right before any method that is overriding another to guarantee to ourselves that it is actually overriding
     *  a method from a super class properly. You should do this every time you override a method!
     *
     * As stated above, this method is overriding a hook method in the
     * {@link net.dv8tion.jda.core.hooks.ListenerAdapter ListenerAdapter} class. It has convience methods for all JDA events!
     * Consider looking through the events it offers if you plan to use the ListenerAdapter.
     *
     * In this example, when a message is received it is printed to the console.
     *
     * @param event
     *          An event containing information about a {@link net.dv8tion.jda.core.entities.Message Message} that was
     *          sent in a channel.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
                                                        //  This could be a TextChannel, PrivateChannel, or Group!

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
                                                        // what you would see in the client.

        boolean bot = author.isBot();                    //This boolean is useful to determine if the User that
                                                        // sent the Message is a BOT or not!

        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {
            //Because we now know that this message was sent in a Guild, we can do guild specific things
            // Note, if you don't check the ChannelType before using these methods, they might return null due
            // the message possibly not being from a Guild!

            Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

            String name;
            if (message.isWebhookMessage())
            {
                name = author.getName();                //If this is a Webhook message, then there is no Member associated
            }                                           // with the User, thus we default to the author for name.
            else
            {
                name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
            }                                           // otherwise it will default to their username. (User#getName())

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            //The message was sent in a PrivateChannel.
            //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!
            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }
        else if (event.isFromType(ChannelType.GROUP))   //If this message was sent to a Group. This is CLIENT only!
        {
            //The message was sent in a Group. It should be noted that Groups are CLIENT only.
            Group group = event.getGroup();
            String groupName = group.getName() != null ? group.getName() : "";  //A group name can be null due to it being unnamed.

            System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
        }

        msg = msg.toLowerCase();

        List<Member> members = message.getMentionedMembers();
        if (members != null && members.size() == 1) {
            Member member = members.get(0);
            String name = member.getUser().getName();
            if (name.equals("brobot")) {
                try {
                    MarkovChain markovChain = new MarkovChain();
                    String messageToSend = markovChain.markov("/Users/john.vento/Desktop/JDA-master/src/examples/dictionaries/common_phrases", 2, 7);
                    String cleanString = messageToSend.replaceAll("\\r\\n|\\r|\\n", " ");
                    String output = cleanString.trim().substring(0, 1).toUpperCase() + cleanString.substring(2) + ".";
                    channel.sendMessage(output).queue();
                } catch (IOException ex) {
                    channel.sendMessage(ex.getMessage()).queue();
                    //fail silently
                }
            }
            else if (msg.contains("tickle")) {
                channel.sendMessage("Did someone say tickle??? You tickle **" + name + "** until they poo their pants a little bit. You regret it.").queue();
            } else if (msg.contains("give eggs")) {
                if(eggCount.containsKey(name)) {
                    channel.sendMessage("I already gave you eggs, go away!!!").queue();
                } else {
                    eggCount.put(name, 100);
                    channel.sendMessage("**" + name + "**, you now have 100 eggs.").queue();
                }
            } else if (msg.contains("steal from")) {
                String attacker = author.getName();
                String defender = name;
                String attackerFmt = "**" + attacker + "**";
                String defenderFmt = "**" + defender + "**";

                if (attacker.equals(defender)) {
                    channel.sendMessage("You can't steal from yourself dummy. HEY EVERYONE, " + attackerFmt + " IS A DUM DUM!!").queue();
                }
                else if (!eggCount.containsKey(attacker)) {
                    channel.sendMessage("You aren't part of the eggame. Ask me for some eggs.").queue();
                } else {
                    if (!eggCount.containsKey(name)) {
                        channel.sendMessage("This person isn't part of the eggame.").queue();
                    } else {
                        int attackerEggCount = eggCount.get(attacker);
                        int defenderEggCount = eggCount.get(defender);

                        if (defenderEggCount == 0) {
                            channel.sendMessage("This person has no eggs. :( Nooooo, dōshite?????").queue();
                        } else {
                            int min = 1;
                            int max = 20;
                            int eggsToSteal = ThreadLocalRandom.current().nextInt(min, max + 1);

                            while (eggsToSteal > defenderEggCount) {
                                eggsToSteal = ThreadLocalRandom.current().nextInt(min, defenderEggCount <= 20 ? defenderEggCount + 1 : max + 1);
                            }

                            attackerEggCount += eggsToSteal;
                            defenderEggCount -= eggsToSteal;

                            channel.sendMessage(attackerFmt + ", you stole " + eggsToSteal + " eggs from " + defenderFmt + ". You now have " + attackerEggCount
                                + " eggs and they have " + defenderEggCount + " eggs.").queue();
                            eggCount.put(attacker, attackerEggCount);
                            eggCount.put(defender, defenderEggCount);
                        }
                    }
                }
            } else if (msg.contains("give kids")) {
                String attacker = author.getName();
                String defender = name;
                String attackerFmt = "**" + attacker + "**";
                String defenderFmt = "**" + defender + "**";

                if (attacker.equals(defender)) {
                    channel.sendMessage("They're already your kids!").queue();
                } else {
                    int numKidsToGive = Integer.parseInt(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
                    int attackerNumKids = kidCount.getOrDefault(attacker, 0);
                    int defenderNumKids = kidCount.getOrDefault(defender, 0);

                    if (numKidsToGive > attackerNumKids) {
                        channel.sendMessage("You don't have enough kids to give away. :( Go make more! o:").queue();
                    } else {
                        attackerNumKids -= numKidsToGive;
                        defenderNumKids += numKidsToGive;

                        channel.sendMessage("Congratulations " + attackerFmt + ", you got rid of " + numKidsToGive +
                            " and gave them to " + defenderFmt +  ". You now have " + attackerNumKids
                            + " kids and they have " + defenderNumKids + " kids.").queue();
                    }
                    kidCount.put(attacker, attackerNumKids);
                    kidCount.put(defender, defenderNumKids);
                }
            }
        }

        if (msg.contains("brobot who likes")) {
            String character = msg.substring(msg.indexOf("[")+1, msg.indexOf("]"));
            String messageToSend = rev.reverseLookup(character);
            channel.sendMessage(messageToSend).queue();
        } else if (msg.contains("fertilize")) {
            String fertilizer = message.getAuthor().getName();
            String fertilizerFmt = "**" + message.getAuthor().getName() + "**";

            int kidsToMake = Integer.parseInt(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
            int numEggs = eggCount.getOrDefault(fertilizer, 0);

            if (kidsToMake > numEggs) {
                channel.sendMessage("You don't have enough eggs :( Go steal some more!").queue();
            } else {
                numEggs -= kidsToMake;
                int numKids = kidCount.getOrDefault(fertilizer, 0) + kidsToMake;
                channel.sendMessage("Congratulations " + fertilizerFmt + ", you made " + kidsToMake
                    + " kids! You now have " + numKids + " kids and " + numEggs + " eggs.").queue();
                kidCount.put(fertilizer, numKids);
                eggCount.put(fertilizer, numEggs);
            }
        } else if (msg.contains("let them eat cake")) {
            for (Map.Entry<String, Integer> entry : kidCount.entrySet()) {
                String parent = entry.getKey();
                String parentFmt = "**" + parent + "**";
                int numKids = entry.getValue();
                int numEggs = eggCount.getOrDefault(parent, 0);

                if (numKids > 0 && numKids <= numEggs) {
                    numEggs -= numKids;
                    channel.sendMessage(parentFmt + ", your kids ate " + numKids + " of your eggs!! Those bastards!"
                        + " You now have " + numEggs + " eggs.").queue();
                    eggCount.put(parent, numEggs);
                } else if (numKids > numEggs) {
                    int kidsGone = numKids - numEggs;
                    channel.sendMessage(parentFmt + ", you didn't have enough eggs to feed your kids... "
                        + kidsGone + " of your kids ran away. You now have " + numEggs + " kids and 0 eggs. :(").queue();
                    kidCount.put(parent, numEggs);
                    eggCount.put(parent, 0);
                }
            }
        } else if (msg.contains("copulate")) {
            if (!kidCount.containsKey(author.getName())) {
                kidCount.put(author.getName(), 0);
            }
            for (Map.Entry<String, Integer> entry : kidCount.entrySet()) {
                String parent = entry.getKey();
                String parentFmt = "**" + parent + "**";
                int numKids = entry.getValue();
                int numEggs = eggCount.getOrDefault(parent, 0);
                if (numKids > 0) {
                    int newEggCount = numEggs + numKids / 2;

                    channel.sendMessage("Congratulations " + parentFmt + "! Some of your kids lay eggs!"
                        + " You now have " + newEggCount + " eggs!! :D").queue();
                    eggCount.put(parent, newEggCount);
                } else {
                    channel.sendMessage("You don't have any kids. o.o So sad, you'll probably die alone too.. lul").queue();
                }
            }
        }

        if (msg.contains("count my eggs")) {
            String authorFmt = "**" + author.getName() + "**";
            int numEggs = eggCount.getOrDefault(author.getName(), 0);
            int numKids = kidCount.getOrDefault(author.getName(), 0);
            channel.sendMessage(authorFmt + ", you have " + numEggs + " eggs and " + numKids + " kids!").queue();
        }
    }

//    private void displayLeaderBoard() {
//        StringBuilder bldr = new StringBuilder();
//
//
//        Map<String, Integer> leaders = new LinkedHashMap<>();
//        eggCount.entrySet().stream()
//            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//            .forEachOrdered(x -> leaders.put(x.getKey(), x.getValue()));
//    }

    private void pokeduel(MessageChannel channel, Message message, List<Member> members) {
        String msg = message.getContentDisplay();
        if (msg.equals("brobot attack me")) {
            channel.sendMessage("attack").queue();
        }
        if(!message.getAuthor().getName().equals("brobot")) {
            if (msg.contains("(y/n)")) {
                channel.sendMessage("y").queue();
            } else if (message.getContentRaw().trim().isEmpty()) {
                try {
                    int n = 0;
                    while (n++ < 5) {
                        Thread.sleep(2000);
                        message.addReaction("💥").queue();
                    }
                    Thread.sleep(3000);
                    message.addReaction("✅").queue();
                } catch (Exception e) {
                    //fail silently
                    e.printStackTrace();
                }
            } else if (members != null && members.size() == 1) {
                Member member = members.get(0);
                String name = member.getUser().getName();
                if (name.equals("brobot") && message.getAuthor().getName().equals("Mudamaid 26")) {
                    channel.sendMessage("attack").queue();
                }
            }
        }
    }
}
