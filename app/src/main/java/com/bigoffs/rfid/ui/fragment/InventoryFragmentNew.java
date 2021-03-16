package com.bigoffs.rfid.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.biz.IInventoryBiz;
import com.bigoffs.rfid.mvp.biz.InventoryBiz;
import com.bigoffs.rfid.ui.adapter.InventoryDetailAdapterNew;


/**
 * Created by okbuy on 17-3-2.
 */

public class InventoryFragmentNew extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String result;
    private int mStatus;
    private ListView mListView;
    private IInventoryBiz mBiz;
    private TextView tvTrueShelfNum;
    private TextView tvShelfNum;

    public InventoryFragmentNew() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static InventoryFragmentNew newInstance(int sectionNumber, String result) {
        InventoryFragmentNew fragment = new InventoryFragmentNew();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("result", result);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        tvTrueShelfNum = (TextView) rootView.findViewById(R.id.tv_true_shelf_number);
        tvShelfNum = (TextView) rootView.findViewById(R.id.tv_shelf_number);
        result = getArguments().getString ("result");
        mStatus = getArguments().getInt(ARG_SECTION_NUMBER, 5);
        if(mStatus == 4){
            tvShelfNum.setText("盘点货架号");
            tvTrueShelfNum.setVisibility(View.VISIBLE);
        }
        mListView = (ListView) rootView.findViewById(R.id.lv_inventory);
        View emptyView = rootView.findViewById(R.id.tv_empty);
        mListView.setEmptyView(emptyView);
        mBiz = new InventoryBiz();
        getData();
        return rootView;
    }

    private void getData() {
        mListView.setAdapter(new InventoryDetailAdapterNew(getActivity(),result,mStatus));
    }
}
