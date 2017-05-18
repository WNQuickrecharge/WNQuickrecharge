package com.optimumnano.quickcharge.http;

public interface HttpCallback {
	/**
	 * 
	 * @param id
	 *            the reuqest id
	 * @param result
	 */
	public void onRequestSuccess(int id, BaseResult result);

	public void onRequestFail(int id, BaseResult result);

	public void onRequestCancel(int id);

	public static class SimpleCallback implements HttpCallback {

		@Override
		public void onRequestSuccess(int id, BaseResult result) {
		}

		@Override
		public void onRequestFail(int id, BaseResult result) {
		}

		@Override
		public void onRequestCancel(int id) {
		}

	}
}
