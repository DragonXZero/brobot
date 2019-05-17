package brobot;

import brobot.eggthemall.EggUtils;
import net.dv8tion.jda.core.entities.User;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class BrobotUtils {

    public static void tickle(final User user, final StringBuilder messageToSend) {
        messageToSend.append("Did someone say tickle??? You tickle ")
            .append(EggUtils.bold(user.getName()))
            .append(" until they poo their pants a little bit. You regret it.");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null ? true : s.isEmpty();
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
}