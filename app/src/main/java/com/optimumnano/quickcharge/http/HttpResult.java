package com.optimumnano.quickcharge.http;

public interface HttpResult {
	/* 请求成功，获取到想要的结果 */
	static final int SUCCESS = 0;
	/* 请求失败 */
	static final int FAIL = -1;
	/* 请求取消 */
	static final int CANCELED = -2;
}
