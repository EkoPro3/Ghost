package akro.ghost.tele;

import akro.ghost.tele.utils.Utils;

import java.util.Arrays;

public class ClientChecker {
    public static boolean check(ClientType client, String pkgName)
    {
        return Arrays.asList(client.getPackageNames()).contains(pkgName);
    }

    public static boolean check(ClientType client)
    {
        return check(client, Utils.pkgName);
    }

    public enum ClientType {
        Telegram("org.telegram.messenger", akro.ghost.tele.Clients.Telegram.class),
        TelegramWeb("org.telegram.messenger.web", akro.ghost.tele.Clients.TelegramWeb.class),
        TelegramPlus("org.telegram.plus", akro.ghost.tele.Clients.TelegramPlus.class),
        TGConnect("com.tgconnect.android", akro.ghost.tele.Clients.TGConnect.class),
        Nagram("xyz.nextalone.nagram", akro.ghost.tele.Clients.Nagram.class),
        Nicegram("app.nicegram", akro.ghost.tele.Clients.Nicegram.class),
        TelegramBeta("org.telegram.messenger.beta", akro.ghost.tele.Clients.TelegramBeta.class),
        NagramX("nu.gpu.nagram", akro.ghost.tele.Clients.NagramX.class),
        XPlus("com.xplus.messenger", akro.ghost.tele.Clients.XPlus.class),
        iMe("com.iMe.android", akro.ghost.tele.Clients.iMe.class),
        iMeWeb("com.iMe.android.web", akro.ghost.tele.Clients.iMeWeb.class),
        forkgram("org.forkgram.messenger", akro.ghost.tele.Clients.forkgram.class),
        forkgramBeta("org.forkclient.messenger.beta", akro.ghost.tele.Clients.forkgramBeta.class),
        Telegraph("ir.ilmili.telegraph", akro.ghost.tele.Clients.Telegraph.class),
        Telega("ru.dahl.messenger", akro.ghost.tele.Clients.Telega.class),
        Momogram(new String[]{"nekox.messenger.broken", "momo.gram"}, akro.ghost.tele.Clients.Momogram.class);

        private final String[] packageNames;
        private final Class<?> resolverClass;

        ClientType(String packageName, Class<?> resolverClass) {
            this.packageNames = new String[]{packageName};
            this.resolverClass = resolverClass;
        }

        ClientType(String[] packageNames, Class<?> resolverClass) {
            this.packageNames = packageNames;
            this.resolverClass = resolverClass;
        }

        public String[] getPackageNames() { return packageNames; }
        public Class<?> getResolverClass() { return resolverClass; }

        public static ClientType fromPackage(String pkg){
            for (ClientType type: ClientType.values()){
                for (String name: type.getPackageNames()){
                    if (name.equals(pkg)) return type;
                }
            }
            return null;
        }

        public static boolean containsPackage(String pkg){
            return fromPackage(pkg) != null;
        }
    }
}
