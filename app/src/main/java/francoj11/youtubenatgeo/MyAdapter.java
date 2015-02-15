package francoj11.youtubenatgeo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter class for RecyclerView
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<VideoInfo> playlist;
    private Context context;

    /**
     * Constructor
     * @param l     The list to fill the Adapter
     * @param c     The context
     */
    public MyAdapter(List l,Context c) {
        playlist = l;
        context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoInfo vi = playlist.get(position);
        Picasso.with(context).load(vi.getImageLink()).into(holder.thumbnail);
        holder.videoTitle.setText(vi.getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != playlist ? playlist.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView videoTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NATGEO","CLICK" + getPosition());

                }
            });
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            videoTitle = (TextView) itemView.findViewById(R.id.videotitle);
        }
    }
}
