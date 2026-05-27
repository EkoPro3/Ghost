package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;

import java.io.File;

import de.robv.android.xposed.XposedHelpers;

public class FileLoader {

    Object fileLoader;

    public FileLoader(Object fileLoader){
        this.fileLoader = fileLoader;
    }

    public File getLocalFile(ImageLocation location) {
        return (File) XposedHelpers.callMethod(fileLoader, AutomationResolver.resolve("FileLoader", "getLocalFile", AutomationResolver.ResolverType.Method), location.imageLocation);
    }

    public static FileLoader getInstance(int num) {
        return new FileLoader(XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.FILE_LOADER), AutomationResolver.resolve("FileLoader", "getInstance", AutomationResolver.ResolverType.Method), num));
    }

}
