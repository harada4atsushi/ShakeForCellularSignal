package com.aharada.shakeforcellularsignal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * メインアクティビティ
 * @author a.harada
 */
public class ShakeForCellularSignalActivity extends Activity {
	
	// ShakeEventリスナークラス
	private ShakeListener shakeListener;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        shakeListener = new ShakeListener(this);
        
        // コールバック関数
        shakeListener.setOnShakeListener(new IOnShakeListener() {
        	public void onShake(final int shakeCnt) {
        		TextView txtView = (TextView)findViewById(R.id.touch_check);
        		txtView.append("1秒間に振った回数： " + shakeCnt + "\n");
        		
        		// 振ったい回数が6回以上で拡大アニメーション
        		if (shakeCnt > 5) {
        			ImageView img = (ImageView)findViewById(R.id.antenna);
        			
        			// アニメーション開始
//        			ScaleAnimation scale = new ScaleAnimation(1, 10, 1, 10, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
//        	    	scale.setDuration(2000);
//        	    	scale.setFillAfter(true);
//        	    	scale.setFillEnabled(true);
//        	    	img.startAnimation(scale);
        			LinearLayout.LayoutParams lp =
        					new LinearLayout.LayoutParams((int)(img.getWidth() + 3), (int)(img.getHeight() + 2));
        			lp.leftMargin = 245 - img.getWidth() + 3;
    				img.setLayoutParams(lp);
    				
        		    
        		} else {
        			ImageView img = (ImageView)findViewById(R.id.antenna);
        			if (img.getAnimation() != null) {
//        				img.clearAnimation();
//        				img.getLayoutParams().height = img.getHeight();
//        				img.getLayoutParams().width = img.getWidth();
        			//	img.setLayoutParams(new LinearLayout.LayoutParams(10, 20));
        			}
        		}
        	}
        });
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
}