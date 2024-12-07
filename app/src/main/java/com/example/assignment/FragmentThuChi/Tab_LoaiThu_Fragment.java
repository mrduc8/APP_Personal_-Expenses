package com.example.assignment.FragmentThuChi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.AdapterThuChi.LoaiThuAdapter;
import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangloaithuchi;
import com.example.assignment.R;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;


public class Tab_LoaiThu_Fragment extends Fragment {
    View view;
    private RecyclerView rcv;
    private ArrayList<ThuChi> list = new ArrayList<>();
    private chucnangloaithuchi daoThuChi;
    FloatingActionButton girdBtn,danhsachBtn,addBtn, searchLoaiTC;
    LoaiThuAdapter adapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(list, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    public Tab_LoaiThu_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listview_loaithu, container, false);
        rcv = view.findViewById(R.id.rcv_LoaiThu);
        addBtn = view.findViewById(R.id.addBtn);
        searchLoaiTC = view.findViewById(R.id.searchLoaiTC);
        girdBtn = view.findViewById(R.id.girdBtn);
        danhsachBtn = view.findViewById(R.id.danhsachBtn);
        daoThuChi = new chucnangloaithuchi(getActivity());

        list = daoThuChi.getThuChi(0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new LoaiThuAdapter(getActivity(), R.layout.listview_item_loaitc, list);
        rcv.setAdapter(adapter);
        girdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new LoaiThuAdapter(getActivity(), R.layout.gridview_item_loaitc, list);
                rcv.setAdapter(adapter);
            }
        });
        danhsachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new LoaiThuAdapter(getActivity(), R.layout.listview_item_loaitc, list);
                rcv.setAdapter(adapter);
            }
        });

        // drop item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);

        searchLoaiTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timkiem_thuchi);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edt_TimLoaiChi = dialog.findViewById(R.id.tim_loai_thu);
                Button xoa = dialog.findViewById(R.id.xoaTextLT);
                final Button them = dialog.findViewById(R.id.btnThemLT);
                final TextView title = dialog.findViewById(R.id.titleTim);
                title.setText("TÌM LOẠI THU NHẬP");
                edt_TimLoaiChi.setHint("Nhập loại thu nhập cần tìm...");

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                rcv.addItemDecoration(dividerItemDecoration);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(rcv);

                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String themText = edt_TimLoaiChi.getText().toString();
                        if (themText.equals("")) {
                            edt_TimLoaiChi.setError("Chưa nhập gì cả!");
                            return;
                        }
                        list.clear();
                        list = daoThuChi.Timkiem(themText, 0);
                        if (list.size() != 0 ) {
                            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông Báo","Đang tìm kiếm...");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    adapter = new LoaiThuAdapter(getActivity(), R.layout.listview_item_loaitc, list);
                                    rcv.setAdapter(adapter);
                                    dialog.dismiss();
                                }
                            },1500);
                        } else {
                            edt_TimLoaiChi.setText("");
                            edt_TimLoaiChi.setError(themText +" không tồn tại!");
                        }
                    }
                });

                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = daoThuChi.getThuChi(0);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rcv.setLayoutManager(layoutManager);
                        adapter = new LoaiThuAdapter(getActivity(), R.layout.listview_item_loaitc, list);
                        rcv.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.them_loai_thuchi);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edt_ThemLoaiThu = dialog.findViewById(R.id.them_loai_thu);
                Button xoa = dialog.findViewById(R.id.xoaTextLT);
                final Button them = dialog.findViewById(R.id.btnThemLT);
                edt_ThemLoaiThu.setHint("Thêm loại thu...");


                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String themText = edt_ThemLoaiThu.getText().toString();
                        ThuChi tc = new ThuChi(0, themText, 0);
                        list.clear();
                        list = daoThuChi.Kiemtratontai(themText, 0);
                        if (list.size() != 0 ) {
                            edt_ThemLoaiThu.setText("");
                            edt_ThemLoaiThu.setError(themText+" đã tồn tại!");
                            return;
                        }
                        if (daoThuChi.themTC(tc) == true) {
                            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông Báo","Đang thêm...");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    list.clear();
                                    list.addAll(daoThuChi.getThuChi(0));
                                    adapter.notifyDataSetChanged();
                                    list = daoThuChi.getThuChi(0);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    rcv.setLayoutManager(layoutManager);
                                    adapter = new LoaiThuAdapter(getActivity(), R.layout.listview_item_loaitc, list);
                                    rcv.setAdapter(adapter);
                                    dialog.dismiss();
                                }
                            },1000);
                        } else {
                            Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = daoThuChi.getThuChi(0);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rcv.setLayoutManager(layoutManager);
                        adapter = new LoaiThuAdapter(getActivity(), R.layout.listview_item_loaitc, list);
                        rcv.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }


}
