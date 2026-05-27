package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.messenger.FileLoadOperation;

public class DownloadSpeed {

    public static boolean isEnable = false;

    public static void init(){
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.FILE_LOAD_OPERATION) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.FILE_LOAD_OPERATION), AutomationResolver.resolve("FileLoadOperation", "updateParams", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                        @Override
                        protected void afterMethod(MethodHookParam param) {
                            if (ConfigManager.downloadSpeed.isEnable()) {
                                FileLoadOperation fileLoadOperation = new FileLoadOperation(param.thisObject);

                                int downloadChunkSizeBig = 1024 * 512;
                                int maxDownloadRequests = 8;

                                long defaultMaxFileSize = 1024L * 1024L * 2000L;

                                int maxCdnParts = (int) (defaultMaxFileSize / downloadChunkSizeBig);

                                fileLoadOperation.setDownloadChunkSizeBig(downloadChunkSizeBig);
                                fileLoadOperation.setMaxDownloadRequests(maxDownloadRequests);
                                fileLoadOperation.setMaxDownloadRequestsBig(maxDownloadRequests);
                                fileLoadOperation.setMaxCdnParts(maxCdnParts);
                                param.setResult(null);
                            }

                        }
                    });
                }
            }
        } catch (Throwable t){
            Logger.e(t);
        }
    }

}
