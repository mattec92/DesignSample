package se.mattec.design.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import se.mattec.design.R;

public class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int ITEM_COUNT = 20;

    private boolean mShortItems;

    public RecyclerAdapter(boolean shortItems)
    {
        mShortItems = shortItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.element_recycler_item, parent, false))
        {

        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (mShortItems)
        {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = (int) (holder.itemView.getContext().getResources().getDimension(R.dimen.card_top_margin) / 1.5f);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount()
    {
        return ITEM_COUNT;
    }

}
