package brobot.eggthemall;

import brobot.markov.MarkovChainGenerator;
import brobot.mudae.ReverseLikeListLookup;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.IOException;
import java.util.*;

public class EggUtils {

    public static String bold(final String s) {
        return "**" + s + "**";
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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortValuesAsc(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static LinkedHashMap<String, Long> sortValuesDesc(Map<String, Long> unSortedMap) {
        LinkedHashMap<String, Long> reverseSortedMap = new LinkedHashMap<>();
        //Use Comparator.reverseOrder() for reverse ordering
        unSortedMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        return reverseSortedMap;
    }
}
