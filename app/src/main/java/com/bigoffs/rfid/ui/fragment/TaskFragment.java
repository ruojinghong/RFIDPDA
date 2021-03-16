package com.bigoffs.rfid.ui.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.TaskInfo;
import com.bigoffs.rfid.mvp.biz.TaskBiz;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PagedResult;
import com.bigoffs.rfid.ui.activity.GroundingAllocationDetailActivity;
import com.bigoffs.rfid.ui.activity.GroundingDetailActivity;
import com.bigoffs.rfid.ui.activity.InventoryActivity;
import com.bigoffs.rfid.ui.activity.PickingDetailActivityNew;
import com.bigoffs.rfid.ui.activity.TaskActivity;
import com.bigoffs.rfid.ui.adapter.TaskListAdapter;
import com.bigoffs.rfid.ui.view.PagedListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by okbuy on 17-2-21.
 */

public class TaskFragment extends Fragment implements PagedListView.LoadMoreDelegate {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PagedListView mListView;
    private TaskBiz mBiz;
    private TaskListAdapter mAdapter;
    private List<TaskInfo> mData;
    private static CallBack callBack;
    private String title;
    private int position = 0;
    public interface CallBack{
        void updateTitle(String title,int position);
    }
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public TaskFragment(){

    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TaskFragment newInstance(int sectionNumber) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        mListView = (PagedListView) rootView.findViewById(R.id.lv_task);
        View emptyView = rootView.findViewById(R.id.tv_empty);
        mListView.setEmptyView(emptyView);
        mListView.setLoadMoreDelegate(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                jump(i);
            }
        });
        TextView tvTimeTitle = (TextView) rootView.findViewById(R.id.tv_time_title);
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            tvTimeTitle.setText("完成时间");
        }
        mBiz = new TaskBiz();
        mData = new ArrayList<>();
//        loadData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void jump(int position) {

        final TaskInfo info = mData.get(position);
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);// 0为待办，1为已办
        if (sectionNumber == 0) {
            // 待办任务都可跳转
            switch (info.type) {
                case 4:// 到货单
                    ((TaskActivity) getActivity()).showLoading("");
                    mBiz.queryArrivalTask(getActivity(), info.id + "", "TaskActivity", new ICommonResult() {
                        @Override
                        public void onSuccess(String result) {
                            ((TaskActivity) getActivity()).hideLoading();
                            try {
                                JSONObject object = new JSONObject(result);
                                GroundingDetailActivity.show(getActivity(),object.getInt("task_id")+"",object.getInt("arrival_id")+"",object.getString("arrival_code"));
                                getActivity().finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(Call call, Exception e) {
                            ((TaskActivity) getActivity()).hideLoading();
                        }

                        @Override
                        public void onInterrupt(int code, String message) {
                            ((TaskActivity) getActivity()).hideLoading();
                        }
                    });
                    break;
                case 2:// 拣货单
                    ((TaskActivity) getActivity()).showLoading("");
                    mBiz.queryPickingTask(getActivity(), info.id + "", "TaskActivity", new ICommonResult() {
                        @Override
                        public void onSuccess(String result) {
                            ((TaskActivity) getActivity()).hideLoading();
                            PickingDetailActivityNew.show(getActivity(), info.id,result);
                            getActivity().finish();
                        }

                        @Override
                        public void onFail(Call call, Exception e) {
                            ((TaskActivity) getActivity()).hideLoading();
                        }

                        @Override
                        public void onInterrupt(int code, String message) {
                            ((TaskActivity) getActivity()).hideLoading();
                        }
                    });


                    break;
                case 3:// 盘点单
                    InventoryActivity.show(getActivity(), info.id+"");
                    break;
                case 5://调拨单
                    mBiz.queryAllocationTask(getActivity(), info.id + "", "TaskActivity", new ICommonResult() {
                        @Override
                        public void onSuccess(String result) {
                            ((TaskActivity) getActivity()).hideLoading();
                            try {
                                JSONObject object = new JSONObject(result);
                                GroundingAllocationDetailActivity.show(getActivity(),object.getInt("task_id")+"",object.getInt("allocation_id")+"",object.getString("allocation_code"));
                                getActivity().finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(Call call, Exception e) {
                            ((TaskActivity) getActivity()).hideLoading();
                        }

                        @Override
                        public void onInterrupt(int code, String message) {
                            ((TaskActivity) getActivity()).hideLoading();
                        }
                    });
                    break;
            }
        } else {
            // 已办任务，盘点单、拣货单可以跳转
//            if (info.type == 3) {
//                InventoryDetailActivity.show(getActivity(), info.id);
//            } else if (info.type == 2) {
//                // 拣货单
//                PickingPreviewActivity.show(getActivity(), info.id);
//            }
        }
    }

    private void loadData() {
        ((TaskActivity) getActivity()).showLoading("");
        mListView.resetPageId();
        mBiz.query(getActivity(), getArguments().getInt(ARG_SECTION_NUMBER), 1, "TaskActivity", new PagedListView.LoadMoreCallback(mListView) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ((TaskActivity) getActivity()).hideLoading();
                PagedResult pagedResult = new PagedResult(result);
                Gson gson = new Gson();
                List<TaskInfo> infos = gson.fromJson(pagedResult.getResults(), new TypeToken<List<TaskInfo>>() {
                }.getType());
                mData.addAll(infos);
                mAdapter = new TaskListAdapter(getActivity(), infos);
                mListView.setAdapter(mAdapter);
                try {
                    JSONObject object = new JSONObject(result);
                    title = object.getString("counts");
                    ((TaskActivity)getActivity()).title[0] = title;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(Call call, Exception e) {
                ((TaskActivity) getActivity()).hideLoading();
            }

            @Override
            public void onInterrupt(int code, String message) {
                ((TaskActivity) getActivity()).hideLoading();
            }
        });
    }

    @Override
    public void loadMore() {
        mListView.pagePlus();
        mBiz.query(getActivity(), getArguments().getInt(ARG_SECTION_NUMBER), mListView.getCurPageId(), "TaskActivity", new PagedListView.LoadMoreCallback(mListView) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                PagedResult pagedResult = new PagedResult(result);
                Gson gson = new Gson();
                List<TaskInfo> infos = gson.fromJson(pagedResult.getResults(), new TypeToken<List<TaskInfo>>() {
                }.getType());
                mData.addAll(infos);
                mAdapter.addData(infos);
            }
        });
    }

    @Override
    public void showLoadMoreProgress() {
        ((TaskActivity) getActivity()).showLoading("");
    }

    @Override
    public void hideLoadMoreProgress() {
        ((TaskActivity) getActivity()).hideLoading();
    }
}
