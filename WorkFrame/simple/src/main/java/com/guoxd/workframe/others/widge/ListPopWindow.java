package com.guoxd.workframe.others.widge;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.modles.BasePopItem;

import java.util.ArrayList;
import java.util.HashMap;

import razerdp.basepopup.BasePopupWindow;

/**使用BasePopupWindow
 * Created by guoxd on 2019/1/2.
 */

public class ListPopWindow extends BasePopupWindow {
    Context mContext;
    int flag;
    RecyclerView recyclerView;
    public ListPopWindow(Context context) {
        super(context);
        mContext = context;
        initRecycler();
    }

    private void initRecycler() {
        mAdapter = new ItemPopAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(
                new RecyclerView.ItemDecoration(){} );
        recyclerView.setAdapter(mAdapter);
    }

    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    //注意此处先于ListPopWindow
    @Override
    public View onCreateContentView() {
        View root = createPopupById(R.layout.main_pop_listpop);
        recyclerView = root.findViewById(R.id.recycler_view);
        return root;
    }

    ItemPopAdapter mAdapter;
    public void setData(int flag,HashMap<String,String> entities){
        this.flag = flag;
        ArrayList<BasePopItem> datas = new ArrayList<>();;
        if (entities != null) {//no data
            for(String key:entities.keySet()){
                datas.add(new BasePopItem(key,entities.get(key),""));
            }
        }
        datas.add(0,new BasePopItem("-1","全部",""));
        mAdapter.setData(datas);
        mAdapter.notifyDataSetChanged();
    }
    private OnPopItemClickListener mListener;
    public void setListener(OnPopItemClickListener listener) {
        this.mListener = listener;
        mAdapter.setListener(new ItemPopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String key, String value) {
                if(mListener == null)
                    return;
                if(key.equals("-1")){
                    mListener.onAll(flag);
                }else {
                    mListener.onItemClick(flag, key, value);
                }
            }
        });
    }

}
