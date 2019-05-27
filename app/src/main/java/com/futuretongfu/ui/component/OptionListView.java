package com.futuretongfu.ui.component;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ChenXiaoPeng on 2017/6/16.
 */

public class OptionListView extends ListView {

    private Object obj;
    private ConfirmListener listener;

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setListener(ConfirmListener listener) {
        this.listener = listener;
    }

    public OptionListView(Context context, ListAdapter adapter) {
        this(context, adapter, null);

    }

    public OptionListView(Context context, ListAdapter adapter, ConfirmListener listener) {
        super(context);
//        setDivider(new ColorDrawable(0xF5F5F5));
        setDividerHeight(0);
        if (adapter != null) {
            setOptionAdapter(adapter);
        }
        this.listener = listener;
    }

    public void setOptionAdapter(final ListAdapter adapter) {
        setAdapter(adapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                obj = adapter.getItem(position);
                if (listener != null) {
                    listener.confirm();
                }
            }
        });
        obj = adapter.getItem(0);
    }

    public interface ConfirmListener {
        void confirm();

    }


    public Object getObj() {
        return obj;
    }

}
