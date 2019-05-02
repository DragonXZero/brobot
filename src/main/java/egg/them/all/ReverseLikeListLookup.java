package egg.them.all;

import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ReverseLikeListLookup {

    private static final Map<String, Set<String>> userToLikeListMap = new HashMap<>();
    private static final Map<String, Set<String>> characterToUserMap = new HashMap<>();

    public ReverseLikeListLookup(final String filePath) {
        final Path path = Paths.get(filePath);
        final BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine().substring(1);
            String user = line;
            while (!line.equals("EOF")) {
                Set<String> likeList = userToLikeListMap.getOrDefault(user, new HashSet<>());
                line = reader.readLine();
                while (!line.isEmpty()) {
                    String characterToShow = line.substring(line.indexOf(".")+1).trim();
                    String character = characterToShow.substring(0, characterToShow.indexOf("-")).trim();
                    likeList.add(character);

                    Set<String> users = characterToUserMap.getOrDefault(character, new HashSet<>());
                    users.add(user);
                    characterToUserMap.put(character, users);

                    line = reader.readLine();
                }
                userToLikeListMap.putIfAbsent(user, likeList);
                line = reader.readLine();
                user = line.substring(1);
            }

//            for (Map.Entry<String, Set<String>> entry : characterToUserMap.entrySet()) {
//                System.out.println(entry.getKey());
//                for (String userStr : entry.getValue()) {
//                    System.out.print("\t" + userStr + ", ");
//                }
//            }

            reader.close();
        } catch (IOException e ) {

        }
    }

    public String reverseLookup(String character) {
        StringBuilder bldr = new StringBuilder();
        if (characterToUserMap.containsKey(character)) {
            bldr.append("The following users like **" + character + "**: ");
            Set<String> users = characterToUserMap.get(character);
            for (String user : users) {
                bldr.append("**").append(user).append("**, ");
            }
            bldr.setLength(bldr.length()-1);
        } else {
            bldr.append("Nobody likes **" + character + "**.");
        }
        return bldr.toString();
    }
}
