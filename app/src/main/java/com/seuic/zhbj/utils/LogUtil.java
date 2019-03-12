package com.seuic.zhbj.utils;

import android.util.Log;


/**
 * Log管理类
 * @author Administrator
 *
 */
public class LogUtil {
	private static boolean isDebug = true;//需要开发完毕后，上传市场前，置为false

	/**
	 * 打印d级别的log
	 * @param msg
	 */
	public static void d(String msg){
		String lineNumber = "" + Thread.currentThread().getStackTrace()[4].getLineNumber();
		if(isDebug){
			Log.d("tag", lineNumber+":"+msg);
		}
	}
	
	/**
	 * 方便打log
	 * @param object
	 * @param msg
	 */
	public static void d(Object object,String msg){
		String[] autoJumpLogInfos = getAutoJumpLogInfos();
		if(isDebug){
			Log.d(object.getClass().getSimpleName(), autoJumpLogInfos[2]+":"+msg);
		}
	}

	/**
	 * 打印e级别的log
	 * @param msg
	 */
	public static void e(String msg){
		String lineNumber = "" + Thread.currentThread().getStackTrace()[4].getLineNumber();
		if(isDebug){
			Log.e("tag", lineNumber+":"+msg);
		}
	}
	
	/**
	 * 方便打log
	 * @param object
	 * @param msg
	 */
	public static void e(Object object,String msg){
		String[] autoJumpLogInfos = getAutoJumpLogInfos();
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), autoJumpLogInfos[2]+":"+msg);
		}
	}
	
	/**
	 * 打印v级别的log
	 * @param msg
	 */
	public static void v(String msg){
		String lineNumber = "" + Thread.currentThread().getStackTrace()[4].getLineNumber();
		if(isDebug){
			Log.v("tag", lineNumber+":"+msg);
		}
	}
	
	/**
	 * 方便打log
	 * @param object
	 * @param msg
	 */
	public static void v(Object object,String msg){
		String[] autoJumpLogInfos = getAutoJumpLogInfos();
		if(isDebug){
			Log.v(object.getClass().getSimpleName(), autoJumpLogInfos[2]+":"+msg);
		}
	}
	
	/**
	 * 打印i级别的log
	 * @param msg
	 */
	public static void i(String msg){
		String lineNumber = "" + Thread.currentThread().getStackTrace()[4].getLineNumber();
		if(isDebug){
			Log.i("tag", lineNumber+":"+msg);
		}
	}
	
	/**
	 * 方便打log
	 * @param object
	 * @param msg
	 */
	public static void i(Object object,String msg){
		String[] autoJumpLogInfos = getAutoJumpLogInfos();
		if(isDebug){
			Log.i(object.getClass().getSimpleName(), autoJumpLogInfos[2]+":"+msg);
		}
	}

	/**
	 * 获取打印信息所在方法名，行号等信息
	 * @return
	 */
	private static String[] getAutoJumpLogInfos() {
		// 下标0对应的是类，下标1对应的是所在方法，下标2对应的是所在的类名全路径的行数
		String[] infos = new String[] { "", "", "" };
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		/**
		 * StackTraceElement[] 有很多堆栈追踪，其中前面5个为一下内容；
		 *
		 *  dalvik.system.VMStack.getThreadStackTrace(Native Method)
		 *  java.lang.Thread.getStackTrace(Thread.java:579)
		 *  com.seuic.zhbj.utils.LogUtil.getAutoJumpLogInfos(LogUtil.java:111)
		 *  com.seuic.zhbj.utils.LogUtil.d(LogUtil.java:31)
		 *  com.seuic.zhbj.fragment.ContentFragment$ContentAdapter.instantiateItem(ContentFragment.java:142)
		 */

		if (elements.length < 5) {
			return infos;
		} else {
			// ClassName
			infos[0] = elements[4].getClassName().substring(elements[4].getClassName().lastIndexOf(".") + 1);
			// MethodName
			infos[1] = "Method："+elements[4].getMethodName() + "()";
			// LineNumber
			infos[2] =  "java:"+ elements[4].getLineNumber();
			return infos;
		}
	}
}
