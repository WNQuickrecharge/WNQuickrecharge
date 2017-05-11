package com.optimumnano.quickcharge.activity.qrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.Result;
import com.optimumnano.quickcharge.zxing.view.MipcaActivityCaptureActivity;


/**
 * 二维码
 * 
 * @ClassName: ZxingActivty
 */
public class ZxingActivty extends MipcaActivityCaptureActivity {

	public static String SHOP_ID_KEY = "shopId";

	public static String TABLE_ID_KEY = "tableId";

	@Override
	public void handleDecode(Result result, Bitmap barcode) {
		super.handleDecode(result, barcode);
		String resultString = result.getText();

		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}else {
			Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
		}

//		if (ShopTableScanInfo.IsInstanceJson(resultString)) {
//			ShopTableScanInfo scanInfo = ShopTableScanInfo.getInstanceBy(resultString);
//			ScanResultForShopTableInfo(scanInfo.getShopId(), scanInfo.getTableCode());
//		}
	}


	public static void start(Context mContext) {
		Intent intent = new Intent(mContext, ZxingActivty.class);
		mContext.startActivity(intent);
	}
}
