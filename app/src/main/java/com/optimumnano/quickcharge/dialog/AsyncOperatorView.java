package cdn.cdn_app.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * 异步操作对话框实例类
 * 
 * @author YSJZ
 * 
 */
public class AsyncOperatorView extends ProgressDialog implements IAsyncOpteratorView {

	private Context context;
	public AsyncOperatorView(Context context) {
		super(context);
		this.context = context;
	}

	public  void start(String alertMessage) {
		setMessage(alertMessage);
		setIndeterminate(false);
		setCancelable(false);
		setOnCancelListener(null);
		show();
	}

	@Override
	public void finish() {
		dismiss();
	}

	@Override
	public  void finish(String alertMessage) {
		setMessage(alertMessage);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				dismiss();
			}
		}, 1500);
	}

	@Override
	public void start(int resStringId) {		
		start(context.getString(resStringId));
	}

	@Override
	public void finish(int resStringId) {

		finish(context.getString(resStringId));
		
	}

}
