package android.ali.com.briefme.data;

import android.ali.com.briefme.BriefMeApp;
import android.ali.com.briefme.R;
import android.ali.com.briefme.adapters.NewsAdapter.ROW_TYPE;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Paul on 3/3/17.
 */

public class OverviewObj extends RowObj {

    private static final String LOG_TAG = OverviewObj.class.getName();

    public String title;

    public String description;

    public String source;

    public String imageUrl;

    public String articleUrl;

    public Date publishedDate;

    public OverviewObj(String source, String title, String description, String imageUrl, String articleUrl, String publishedDate) {
        super(ROW_TYPE.NEWS_OVERVIEW.ordinal());
        this.source = source;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;

        if(publishedDate != null && !publishedDate.equals("null")) {
            String [] pieces = publishedDate.split("T");
            String [] timePieces = pieces[0].split("-");
            String [] hourPieces = pieces[1].split(":");

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.set(Calendar.YEAR, Integer.parseInt(timePieces[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(timePieces[1]) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timePieces[2]));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourPieces[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(hourPieces[1]));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            this.publishedDate = cal.getTime();
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            this.publishedDate = cal.getTime();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        OcrViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            LayoutInflater inflater = (LayoutInflater) BriefMeApp.getInstance().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.row_overview, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new OcrViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.title);
            holder.description = (TextView) vi.findViewById(R.id.description);
            holder.source = (TextView) vi.findViewById(R.id.source);
            holder.publishedDate = (TextView) vi.findViewById(R.id.publishedDate);
            holder.image = (ImageView) vi.findViewById(R.id.image);

            vi.setTag( holder );
        } else {
            holder = (OcrViewHolder) vi.getTag();
        }

        holder.title.setText(title);
        holder.description.setText(description);
        holder.source.setText(source);

        holder.publishedDate.setText(publishedDate.toString());

        Glide.with(BriefMeApp.getInstance())
                .load(imageUrl)
                .into(holder.image);

        return vi;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class OcrViewHolder {
        public TextView title;
        public TextView description;
        public TextView source;
        public TextView publishedDate;
        public ImageView image;
    }
}
