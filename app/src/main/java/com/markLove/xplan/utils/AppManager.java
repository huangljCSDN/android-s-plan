package com.markLove.xplan.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/**
 * 类名称：AppManager.java <br>
 * 内容摘要： Activity管理类。<br>
 * 属性描述：<br>
 * 方法描述：<br>
 * 修改备注： <br>
 * 创建时间： 2016-9-22下午12:43:09<br>
 * 公司：深圳市华移科技股份有限公司<br>
 * 
 * @author 邱越<br>
 */
public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		// //应用即将全部关闭，清理缓存
		// if(activityStack.size()==1){
		// ((AppContext)activity.getApplication()).clearWebViewCache();
		//
		// }
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	// 获取指定类名的Activity
	public Activity getActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context, Activity activity) {
		try {
			int currentVersion = android.os.Build.VERSION.SDK_INT;
			if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
				finishAllActivity();
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(startMain);
				System.exit(0);
			} else {
				finishAllActivity();
				ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				activityManager.killBackgroundProcesses(context.getPackageName());
				System.exit(0);
			}

		} catch (Exception e) {
		}
	}

}