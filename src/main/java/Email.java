import java.util.Random;

public class Email {
    private static final String domainName = "@mail.com";
    public static String generate(){
        int leftLimit = 97;
        int rightLimit = 122;
        int stringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomLimitedInt =
                    leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char)randomLimitedInt);
        }
        return buffer + domainName;
    }
}
