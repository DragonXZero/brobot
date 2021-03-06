package brobot;/*
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

import brobot.nagger.SheetsQuickstart;
import brobot.pokemon.PokemonInfo;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BrobotListener extends ListenerAdapter
{
    private static MessageParser messageParser;
    public static Map<String, PokemonInfo> pokedex;
    public static SheetsQuickstart sheetsQuickstart;

    /**
     * This is the method where the program starts.
     */
    public static void main(String[] args)
    {
        //We construct a builder for a BOT account. If we wanted to use a CLIENT account
        // we would use AccountType.CLIENT
        try
        {
            JDA jda = new JDABuilder(BrobotConstants.DEVELOPER_TOKEN)         // The token of the account that is logging in.
                    .addEventListener(new BrobotListener())  // An instance of a class that will handle events.
                    .build();
            jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building JDA!");

            messageParser = new MessageParser();
            sheetsQuickstart = new SheetsQuickstart();

            try {
                pokedex = Utils.buildPokedex();
            } catch (IOException e) {
                System.out.println("There was an error building the Pokedex!");
            }
            System.out.println("Finished building the Pokedex!");
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

        final ResponseObject responseObject = new ResponseObject();


        if (msg.equals("!nag") && !author.getId().equals("579027591164330024")) {
            sheetsQuickstart.nag(responseObject);
        } else if (msg.equals("!nag tasks") && !author.getId().equals("579027591164330024")) {
            sheetsQuickstart.displayTasksForToday(responseObject);
        } else if (msg.equals("!nag all") && !author.getId().equals("579027591164330024")) {
            try {
                sheetsQuickstart.nag(responseObject);
                sendMessageToAllChannels(responseObject, event);
                responseObject.getResponseBldr().setLength(0);
            } catch (Exception e) {
                // fail silently
            }
        }
        // TODO - Do this in a cleaner way.
        //  This is a special case since it's not a command, just parsing Mudae's message. For now we will send a fake request object.
        else if (author.isBot() && author.getName().equals("Mudamaid 26")) {
            RequestObject requestObject = messageParser.createMudaeMessage(responseObject, message);
            requestObject.executeCommand(responseObject);
        } else if (!Utils.isNullOrEmpty(msg) && msg.charAt(0) == BrobotConstants.BROBOT_PREFIX) {
            RequestObject requestObject = messageParser.parseMessage(responseObject, message);
            requestObject.executeCommand(responseObject);
        }

        final List<StringBuilder> responseBldrs = responseObject.finalizeAndGetBldrs();
        if (responseBldrs.size() > 0) {
            for (StringBuilder responseBldr : responseBldrs) {
                if (responseBldr.length() > 0) {
                    channel.sendMessage(responseBldr.toString()).queue();
                }
            }
            for (String filePath : responseObject.getImages()) {
                channel.sendFile(new File(filePath)).queue();
            }
        }
    }

    private void sendMessageToAllChannels(final ResponseObject responseObject, final MessageReceivedEvent event) throws InterruptedException {
        final List<String> channelIds = Arrays.asList(new String[] {
            "569428458430660619", // mudae main
            "575921455863431168", // mudae trade
            "575921683366412302", // mudae pokemon
            "578430007437950987", // developerment
            "579027591164330024", // test-bot
            "562115015776403462", // general
            "562132041379086364", // animoo
            "566395406401929217" // board game
        });

        String messageToSend = responseObject.getResponseBldr().toString();
        if (!messageToSend.isEmpty()) {
            for (String channelId : channelIds) {
                TextChannel textChannel = event.getGuild().getTextChannelById(Long.parseLong(channelId));
                Thread.sleep(1000);
                textChannel.sendMessage(messageToSend).queue();
            }
        }
    }
}
