/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package firstmob.firstbank.com.firstagent.adapter;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.model.GetBillersData;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;

/**
 * Created by tutlane on 24-08-2017.
 */
public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	int cardViewheight;
	public ImageAdapter(Context c,int cardViewheight) {
		mContext = c;
	}

	public int getCount() {
		return thumbImages.length;
	}
	public Object getItem(int position) {
		return null;
	}
	public long getItemId(int position) {
		return 0;
	}
	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		ImageGrid holder = new ImageGrid();

		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.gridview_list, null);
			// Now we can fill the layout with the right values


			TextView accid = (TextView) v.findViewById(R.id.txt);
			ImageView imgv = (ImageView) v.findViewById(R.id.imgt);
			CardView cardv = (CardView) v.findViewById(R.id.card_view2);




			holder.txtname = accid;
			holder.img = imgv;
			holder.cv = cardv;


			v.setTag(holder);
		}
		else {
			holder = (ImageGrid) v.getTag();
		}


		holder.txtname.setText(services[position]);
		holder.img.setImageResource(thumbImages[position]);

			holder.cv.setCardBackgroundColor(mContext.getResources().getColor(colors[position]));
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		SecurityLayer.Log("cardv",Integer.toString(cardViewheight));
		v.setMinimumHeight(cardViewheight/3);


		return v;
	}

	private static class ImageGrid {
		public TextView txtname;
		public ImageView img;
		public CardView cv;
}
	// Add all our images to arraylist
	public Integer[] thumbImages = {
			R.drawable.transfericon, R.drawable.withdrawicon,
			R.drawable.depositicon, R.drawable.airtimeicon,
			R.drawable.paybillicon, R.drawable.accountopenicon
	};

	public Integer[] colors = {
			R.color.transfercolor, R.color.withdcolor,
			R.color.depocolor, R.color.airtimecolor,
			R.color.paybillcolor, R.color.openacccolor
	};

	public String[] services = {"Transfer","Withdraw","Deposit","Buy Airtime","Pay Bill","Open Account/\n Wallet"};
}