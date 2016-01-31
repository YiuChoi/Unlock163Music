package bin.xposed.Unblock163MusicClient;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Main implements IXposedHookLoadPackage {
    public static final String packageName = "com.netease.cloudmusic";
    public static String HOOK_UTILS;
    public static String HOOK_CONSTRUCTOR;
    public static final String VERSION_3_2_1 = "3.2.1";
    public static String VERSION;

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals(packageName)) {
            final Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context systemContext = (Context) callMethod(activityThread, "getSystemContext");
            VERSION = systemContext.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionName;
            switch (VERSION) {
                case VERSION_3_2_1:
                    HOOK_UTILS = "com.netease.cloudmusic.utils.n";
                    HOOK_CONSTRUCTOR="com.netease.cloudmusic.i.b";
                    break;
                default:
                    HOOK_UTILS = "com.netease.cloudmusic.utils.u";
                    HOOK_CONSTRUCTOR="com.netease.cloudmusic.i.f";
                    break;
            }

            Utility.Init(lpparam.classLoader);
            findAndHookMethod(HOOK_UTILS, lpparam.classLoader,
                    "i", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Exception {
                            String full_url = (String) Utility.FIELD_utils_c.get(param.thisObject);
                            String url = full_url.replace("http://music.163.com", "");
                            if (url.startsWith("/eapi/batch")
                                    || url.startsWith("/eapi/cloudsearch/pc")
                                    || url.startsWith("/eapi/v1/artist")
                                    || url.startsWith("/eapi/v1/album")
                                    || url.startsWith("/eapi/v1/play/record")
                                    || url.startsWith("/eapi/v1/search/get")
                                    || url.startsWith("/eapi/v3/playlist/detail")
                                    || url.startsWith("/eapi/v3/song/detail")
                                    || url.startsWith("/eapi/v3/song/enhance/privilege")) {
                                String modified = Utility.ModifyDetailApi((String) param.getResult());
                                param.setResult(modified);
                            } else if (url.startsWith("/eapi/song/enhance/player/url")) {
                                String modified = Utility.ModifyPlayerApi(url, (String) param.getResult());
                                param.setResult(modified);
                            }
                        }
                    });
        }
    }
}
