package miscellanea;

import entity.SerializableBufferedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AvatarGenerator {

    public static SerializableBufferedImage getRandomAvatar(boolean man) {
        BufferedImage avatar = null;
        String gender = man ? Constants.GENDER_MALE : Constants.GENDER_FEMALE;
        String folder = Constants.AVATAR_FOLDER.replace(Constants.GENDER_PLACEHOLDER, gender);

        File f = new File(folder);
        if (f.exists()) {
            String[] possibleAvatars = f.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(Constants.AVATAR_EXTENSION);
                }
            });
            try {
                String randomFilename = possibleAvatars[Utility.random.nextInt(possibleAvatars.length)];
                avatar = ImageIO.read(new File(folder + Constants.SEPARATOR + randomFilename));
            } catch (IOException ex) {
                avatar = null;
            }
        } else {
            System.err.println("Unable to pick an avatar from " + folder + "\nNo avatar will be used.\n");
        }

        if (avatar == null) {
            return null;
        }

        return new SerializableBufferedImage(avatar);
    }
}
