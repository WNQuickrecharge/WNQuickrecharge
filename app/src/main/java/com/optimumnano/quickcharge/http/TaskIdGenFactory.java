package com.optimumnano.quickcharge.http;

public class TaskIdGenFactory {
	private static int id = 0;
	private static final int ID_MAX = 10000;

	public static int gen() {
		int newId;
		synchronized (TaskIdGenFactory.class) {
			newId = (++id) % ID_MAX;
		}
		return newId;
	}
}
