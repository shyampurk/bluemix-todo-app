package adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import modals.CommentTemplate;
import modals.ContactTemplate;
import vitymobi.com.todobluemix.R;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class AddressBookAdapter extends BaseAdapter {

    private Context parentContext;
    private LayoutInflater mInflater;
    private ContactTemplate[] contactsList;


    public AddressBookAdapter(Context parentReference, ContactTemplate[] contacts){
        this.parentContext=parentReference;
        mInflater= LayoutInflater.from(parentReference);
        this.contactsList=contacts;
    }

    @Override
    public int getCount() {
        return contactsList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view=mInflater.inflate(R.layout.row_address_book,null);
            holder.mTxtVwContactName=(TextView)view.findViewById(R.id.txtvwContactName);
            holder.mTxtVwContactLastActivity=(TextView)view.findViewById(R.id.txtvwContactLastActivity);
            holder.mTxtVwContactEmail=(TextView)view.findViewById(R.id.txtvwContactEmail);
            holder.mTxtVwContactUserID=(TextView)view.findViewById(R.id.txtvwContactUserId);

            holder.mImgVwStatusBeacon=(ImageView)view.findViewById(R.id.imgvwStatusBeacon);
            view.setTag(holder);
        }else{
            holder =(ViewHolder)view.getTag();
        }

        holder.mTxtVwContactName.setText("Name: "+contactsList[i].getDISPLAY_NAME());
        holder.mTxtVwContactLastActivity.setText("Last Activity: "+contactsList[i].getLAST_ACTIVITY());
        holder.mTxtVwContactEmail.setText("Email: "+contactsList[i].getEMAIL());
        holder.mTxtVwContactUserID.setText("User ID: "+contactsList[i].getUSER_ID());
        if(contactsList[i].getUSER_STATE().equalsIgnoreCase("1"))
            holder.mImgVwStatusBeacon.setImageDrawable(parentContext.getResources().getDrawable(R.drawable.red_dot_md));
        else{
            holder.mImgVwStatusBeacon.setImageDrawable(parentContext.getResources().getDrawable(R.drawable.green_dot));
        }



        return view;
    }


    private class ViewHolder{
        TextView mTxtVwContactName;
        TextView mTxtVwContactLastActivity;
        TextView mTxtVwContactEmail;
        TextView mTxtVwContactUserID;
        ImageView mImgVwStatusBeacon;



    }
}
