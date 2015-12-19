package com.github.captain_miao.android.ble.utils;

import android.util.Log;

/**
 * @author YanLu
 * @since  2015-09-14
 */

public class BleLog {
	private static boolean isPrintLog = true;

	private BleLog() {}

	public static boolean isPrintLog() {
		return isPrintLog;
	}

	public static void setPrintLog(boolean isPrintLog) {
		BleLog.isPrintLog = isPrintLog;
	}

	public static void v(String tag, String msg) {
		if (isPrintLog) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isPrintLog) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isPrintLog) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (isPrintLog) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isPrintLog) {
			Log.e(tag, msg);
		}
	}
}
