package brobot;

import brobot.eggthemall.EggUtils;
import brobot.pokemon.PokemonInfo;
import net.dv8tion.jda.core.entities.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    public static void tickle(final User user, final StringBuilder messageToSend) {
        messageToSend.append("Did someone say tickle??? You tickle ")
            .append(EggUtils.bold(user.getName()))
            .append(" until they poo their pants a little bit. You regret it.");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null ? true : s.isEmpty();
    }

    public static void concatAndCamelCaseStrings(StringBuilder resultBldr, String[] parts) {
        for (String part : parts) {
            resultBldr.append(part.substring(0, 1).toUpperCase() + part.substring(1)).append(" ");
        }
        resultBldr.setLength(resultBldr.length()-1);
    }

    public void downloadPokemonImages() {
        String baseUrl = "https://www.serebii.net/pokemon/art/";
        try {
            for (int i = 1; i <= 809; ++i) {
                String pokemonNumber = String.format("%03d", i);
                URL url = new URL(baseUrl + pokemonNumber + ".png");
                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                FileOutputStream fos = new FileOutputStream(
                        "C:\\Users\\admin\\Desktop\\Discord\\brobot\\src\\main\\java\\brobot\\eggthemall\\encounter\\monster\\images\\pokemon\\full\\" + pokemonNumber + ".png");
                fos.write(response);
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static Map<String, PokemonInfo> buildPokedex() throws IOException {
        Map<String, PokemonInfo> pokedex = new LinkedHashMap<>();

        Document doc = Jsoup.connect("https://www.serebii.net/pokemon/all.shtml").get();
        Elements pokemonTableElement = doc.getElementsByClass("dextable");
        Elements fooinfos = pokemonTableElement.select(".fooinfo");

        for (int i = 0; i < fooinfos.size(); ++i) {
            Element fooinfo = fooinfos.get(i);
            String text = fooinfo.text();
            if (!Utils.isNullOrEmpty(text) && (text.charAt(0)+"").equals("#")) {
                String pokedexNumber = text.substring(1);
                String name = fooinfos.get(i+2).text();
                int hp = Integer.parseInt(fooinfos.get(i+5).text());
                int attack = Integer.parseInt(fooinfos.get(i+6).text());
                int defense = Integer.parseInt(fooinfos.get(i+7).text());
                int specialAttack = Integer.parseInt(fooinfos.get(i+8).text());
                int specialDefense = Integer.parseInt(fooinfos.get(i+9).text());
                int speed = Integer.parseInt(fooinfos.get(i+10).text());
                pokedex.put(pokedexNumber, new PokemonInfo(pokedexNumber, name, hp, attack, defense, specialAttack, specialDefense, speed));
                i+=8;
            }
        }

        return pokedex;
    }
}