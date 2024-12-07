package com.example.assignment.FragmentThuChi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.AdapterThuChi.KhoanChiAdapter;
import com.example.assignment.AdapterThuChi.KhoanThuAdapter;
import com.example.assignment.ArrayListThuChi.GiaoDich;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangthuchi;
import com.example.assignment.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ThongKe_Fragment extends Fragment {
    private TextView tungay, denngay, thu, chi, conlai, thongbaochar;
    private Button btnShow;
    BarChart barChart;
    private RecyclerView rcv,rct;
    private ArrayList<GiaoDich> list = new ArrayList<>();
    KhoanThuAdapter adapter;
    KhoanChiAdapter adapterchi;
    String[] loaikhoan = {"Khoản thu", "Khoản chi", "Còn lại"};
    int[] tienkhoan ={};
    private DatePickerDialog datePickerDialog;
    private chucnangthuchi chucnangthuchi;
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");

    public ThongKe_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_ke_, container, false);
        tungay = view.findViewById(R.id.tungay);
        denngay = view.findViewById(R.id.denngay);
        thu = view.findViewById(R.id.tienThu);
        thongbaochar = view.findViewById(R.id.thongbaochar);
        chi = view.findViewById(R.id.tienChi);
        conlai = view.findViewById(R.id.tienConLai);
        btnShow = view.findViewById(R.id.btnShow);
        barChart = view.findViewById(R.id.barchart);
        rcv = view.findViewById(R.id.listchi);
        rct = view.findViewById(R.id.listthu);
        chucnangthuchi = new chucnangthuchi(getActivity());

        //set hinh ảnh
        list = chucnangthuchi.getGDtheoTC(0);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rct.setLayoutManager(gridLayoutManager);
        adapter = new KhoanThuAdapter(getActivity(), R.layout.gridview_item, list);
        rct.setAdapter(adapter);


        list = chucnangthuchi.getGDtheoTC(1);
        GridLayoutManager gridLayoutManagerchi = new GridLayoutManager(getActivity(), 1);
        rcv.setLayoutManager(gridLayoutManagerchi);
        adapterchi = new KhoanChiAdapter(getActivity(), R.layout.gridview_item, list);
        rcv.setAdapter(adapterchi);



        //Format dạng tiền
        final NumberFormat fm = new DecimalFormat("#,###");
        final ArrayList<GiaoDich> listThu = chucnangthuchi.getGDtheoTC(0);
        final ArrayList<GiaoDich> listChi = chucnangthuchi.getGDtheoTC(1);
        int tongThu = 0, tongChi = 0, tong = 0;
        for (int i = 0; i < listThu.size(); i++) {
            tongThu += listThu.get(i).getSoTien();
        }
        for (int i = 0; i < listChi.size(); i++) {
            tongChi += Math.abs(listChi.get(i).getSoTien());
        }
        tong = tongThu - tongChi;
        thu.setText(fm.format(tongThu) + " VND");
        chi.setText(fm.format(tongChi) + " VND");
        conlai.setText(fm.format(tong) + " VND");
        tienkhoan = new int[]{tongThu, tongChi, tong};
        //Bieu đồ cột
        Bieudocot(tongThu, tongChi, tong);
        //Khu chọn vào ngày
        tungay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarkt = Calendar.getInstance();
                int ngay = calendarkt.get(Calendar.DAY_OF_MONTH);
                int thang = calendarkt.get(Calendar.MONTH);
                int nam = calendarkt.get(Calendar.YEAR);
                DatePickerDialog datePickerDialogkt = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarkt.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        tungay.setText(simpleDateFormat.format(calendarkt.getTime()));

                    }
                }, nam,thang,ngay);
                datePickerDialogkt.show();
            }
        });

        denngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarkt = Calendar.getInstance();
                int ngay = calendarkt.get(Calendar.DAY_OF_MONTH);
                int thang = calendarkt.get(Calendar.MONTH);
                int nam = calendarkt.get(Calendar.YEAR);
                DatePickerDialog datePickerDialogkt = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarkt.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        denngay.setText(simpleDateFormat.format(calendarkt.getTime()));

                    }
                }, nam,thang,ngay);
                datePickerDialogkt.show();
                //Khi nhấn nút show lọc thu chi theo ngày
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tongThu = 0, tongChi = 0, tong = 0;
                        String bd = tungay.getText().toString();
                        String kt = denngay.getText().toString();
                        //Show danh sach thu chi
                        list = chucnangthuchi.LocLoaiKhoantheongay(0, bd, kt);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                        rct.setLayoutManager(gridLayoutManager);
                        adapter = new KhoanThuAdapter(getActivity(), R.layout.gridview_item, list);
                        rct.setAdapter(adapter);

                        list = chucnangthuchi.LocLoaiKhoantheongay(1, bd, kt);
                        GridLayoutManager gridLayoutManagerchi = new GridLayoutManager(getActivity(), 1);
                        rcv.setLayoutManager(gridLayoutManagerchi);
                        adapterchi = new KhoanChiAdapter(getActivity(), R.layout.gridview_item, list);
                        rcv.setAdapter(adapterchi);



                        //Tính tiền theo ngày
                        for (int i = 0; i < listThu.size(); i++) {
                            try {
                                if (listThu.get(i).getNgayGd().compareTo(dfm.parse(bd)) >= 0 && listThu.get(i).getNgayGd().compareTo(dfm.parse(kt)) <= 0) {
                                    tongThu += listThu.get(i).getSoTien();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        for (int i = 0; i < listChi.size(); i++) {
                            try {
                                if (listChi.get(i).getNgayGd().compareTo(dfm.parse(bd)) >= 0 && listChi.get(i).getNgayGd().compareTo(dfm.parse(kt)) <= 0) {
                                    tongChi += listChi.get(i).getSoTien();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        tong = tongThu - tongChi;
                        thu.setText(fm.format(tongThu) + " VND");
                        chi.setText(fm.format(tongChi) + " VND");
                        conlai.setText(fm.format(tong) + " VND");
                        tienkhoan = new int[]{tongThu, tongChi, tong};
                        Bieudocot(tongThu, tongChi, tong);
                    }
                });
            }
        });
        return view;
    }

    public void  Bieudocot(int tongChi, int tongThu, int tong) {
        if (tong == 0 && tongChi == 0 && tongThu == 0) {
            thongbaochar.setText("Không có dữ liệu để thiển thị...");
            barChart.clear();
            return;
        }
        thongbaochar.setText("");
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int[] tien ={tongThu , tongChi, tong};
        int[] loai = {1, 2, 3};
        for ( int i = 0; i < loai.length; i++) {
            barEntries.add(new BarEntry(loai[i], tien[i]));

        }
        BarDataSet dataSet = new BarDataSet(barEntries, "| Green: Khoản chi | Yellow: Khoản thu | Red: Còn lại |");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.rgb(255,154,141));
        dataSet.setValueTextSize(11f);

        BarData barData =  new BarData(dataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("");
        barChart.animateY(2500);
    }
}
