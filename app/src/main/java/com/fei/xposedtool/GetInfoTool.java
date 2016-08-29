package com.fei.xposedtool;

import android.util.Log;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * 获得类变量信息
 */
public class GetInfoTool implements IXposedHookLoadPackage {

    private static final String mPackageName = "";
    private static final String mClassName = "";
    private static final String mMethodName = "init";

    private static final String LOG_TAG = "XXX:";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(mPackageName))
            return;

        printLog(lpparam.packageName);

        findAndHookMethod(mClassName, lpparam.classLoader, mMethodName, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                // this will be called before the clock was updated by the original method

                printLog("hook start");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                // this will be called after the clock was updated by the original method

                Class aClass = param.thisObject.getClass();
                printLog(aClass.getName());

                printLog("getDeclaredFields start");
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    String name = declaredField.getName();
                    Field field = aClass.getDeclaredField(name);
                    Object obj = field.get(param.thisObject);
                    printLog(name + "=" + obj);
                }
                printLog("getDeclaredFields end");

                printLog("getFields start");
                Field[] fields = aClass.getFields();
                for (Field field : fields) {
                    String name = field.getName();
                    Field field1 = aClass.getField(name);
                    Object obj = field1.get(param.thisObject);
                    printLog(name + "=" + obj);
                }
                printLog("getFields end");

                printLog("hook end");
            }
        });
    }

    private void printLog(String msg) {
        Log.e(LOG_TAG, msg);
        XposedBridge.log(LOG_TAG + msg);
    }
}
