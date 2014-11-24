package pl.soad.alzheimer_gps;

import java.util.ArrayList;

import pl.soad.alzheimer_gps.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DisplayAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> id;
	private ArrayList<String> fname;
	private ArrayList<String> lname;

	public DisplayAdapter(Context c, ArrayList<String> id,ArrayList<String> fname, ArrayList<String> lname) {
		this.context = c;
		this.id = id;
		this.fname = fname;
		this.lname = lname;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return id.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	public View getView(int pos, View child, ViewGroup parent) {
		Holder holder;
		LayoutInflater layoutInflater;
		if (child == null) {
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.list_row, null);
			holder = new Holder();
			holder.txt_id = (TextView) child.findViewById(R.id.txt_id);
			holder.txt_fName = (TextView) child.findViewById(R.id.txt_fName);
			holder.txt_lName = (TextView) child.findViewById(R.id.txt_lName);
			child.setTag(holder);
		} 
		
		else {
			holder = (Holder) child.getTag();
		}
		
		holder.txt_id.setText(id.get(pos));
		holder.txt_fName.setText(fname.get(pos));
		holder.txt_lName.setText(lname.get(pos));

		return child;
	}

	public class Holder {
		TextView txt_id;
		TextView txt_fName;
		TextView txt_lName;
	}

}
