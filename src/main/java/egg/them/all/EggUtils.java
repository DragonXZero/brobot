package egg.them.all;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.IOException;
import java.util.List;

public class EggUtils {

    public static String bold(final String s) {
        return "**" + s + "**";
    }

    public static void tickle(final String user, final StringBuilder messageToSend) {
        messageToSend.append("Did someone say tickle??? You tickle")
            .append(bold(user))
            .append("until they poo their pants a little bit. You regret it.");
    }

    public static void markov(final MessageChannel channel, final StringBuilder msgBuilder) {
        try {
            MarkovChainGenerator markovChain = new MarkovChainGenerator();
            String messageToSend = markovChain.markov("/Users/john.vento/Desktop/JDA-master/src/examples/dictionaries/common_phrases", 2, 7);
            String cleanString = messageToSend.replaceAll("\\r\\n|\\r|\\n", " ");
            String output = cleanString.trim().substring(0, 1).toUpperCase() + cleanString.substring(2) + ".";
            msgBuilder.append(output);
        } catch (IOException ex) {
            // fail silently
        }
    }

    public static void reverseLookup(final MessageChannel channel, final String msg) {
        final ReverseLikeListLookup rev = new ReverseLikeListLookup("/Users/john.vento/Desktop/JDA-master/src/examples/likelists.txt");

        if (msg.contains("brobot who likes")) {
            String character = msg.substring(msg.indexOf("[") + 1, msg.indexOf("]"));
            String messageToSend = rev.reverseLookup(character);
            channel.sendMessage(messageToSend).queue();
        }
    }

    public static void pokeduel(MessageChannel channel, Message message, List<Member> members) {
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
