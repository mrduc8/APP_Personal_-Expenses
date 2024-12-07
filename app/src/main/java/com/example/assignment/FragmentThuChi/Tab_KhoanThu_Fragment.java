package com.example.assignment.FragmentThuChi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.AdapterThuChi.KhoanThuAdapter;
import com.example.assignment.ArrayListThuChi.GiaoDich;
import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangloaithuchi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangthuchi;
import com.example.assignment.R;
import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class Tab_KhoanThu_Fragment extends Fragment {
    private static final int RESULT_OK = 12;
    View view;
    private RecyclerView rcv;
    private ArrayList<GiaoDich> list = new ArrayList<>();
    private ArrayList<ThuChi> listTC = new ArrayList<>();
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private chucnangthuchi chucnangthuchi;
    private chucnangloaithuchi daoThuChi;
    private DatePickerDialog datePickerDialog;
    private SpeechRecognizer speechRecognizer;
    Intent mSpeechRecordIntent;
    KhoanThuAdapter adapter;
    TextView btnlocloai, locngaybatdau;


    FloatingActionButton girdBtn,danhsachBtn,addBtn, searchLoaiTC, locTC;
    protected static final int RESULT_SPEECH = 10;
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
    private int RecordAudioRequestCode = 123;

    public Tab_KhoanThu_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listview_khoanthu, container, false);
        rcv = view.findViewById(R.id.rcv_KhoanThu);
        addBtn = view.findViewById(R.id.addBtn);
        girdBtn = view.findViewById(R.id.girdBtn);
        danhsachBtn = view.findViewById(R.id.danhsachBtn);
        searchLoaiTC = view.findViewById(R.id.searchLoaiTC);
        btnlocloai = view.findViewById(R.id.them_danshach);
        locngaybatdau = view.findViewById(R.id.loc_ngay_bat_dau);
        chucnangthuchi = new chucnangthuchi(getActivity());
        list = chucnangthuchi.getGDtheoTC(0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item,list);
        rcv.setAdapter(adapter);




        girdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new KhoanThuAdapter(getActivity(), R.layout.gridview_item, list);
                rcv.setAdapter(adapter);
            }
        });
        danhsachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item, list);
                rcv.setAdapter(adapter);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);

        //Lọc theo ngày
        locngaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Chọn ngày bắt đầu!", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                int ngay = calendar.get(Calendar.DAY_OF_MONTH);
                int thang = calendar.get(Calendar.MONTH);
                int nam = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Toast.makeText(getActivity(), "Chọn ngày kết thúc!", Toast.LENGTH_SHORT).show();
                        Calendar calendarkt = Calendar.getInstance();
                        int ngay = calendarkt.get(Calendar.DAY_OF_MONTH);
                        int thang = calendarkt.get(Calendar.MONTH);
                        int nam = calendarkt.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialogkt = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendarkt.set(year,month,dayOfMonth);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
                                String ngaydau =  locngaybatdau.getText().toString();
                                String ngaysau =  simpleDateFormat.format(calendarkt.getTime());
                                locngaybatdau.setText(dateFormat.format(calendar.getFirstDayOfWeek())+" - "+simpleDateFormat.format(calendarkt.getTime()));
                                list.clear();
                                list = chucnangthuchi.LocLoaiKhoantheongay(0, ngaydau, ngaysau);
                                if (list.size() != 0 ) {
                                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông Báo","Đang lọc kết quả...");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item, list);
                                            rcv.setAdapter(adapter);
                                            Toast.makeText(getActivity(), "Lọc thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                    },1000);
                                } else {
                                    Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, nam,thang,ngay);
                        datePickerDialogkt.show();
                    }
                }, nam,thang,ngay);
                datePickerDialog.show();

            }
        });


        //Lọc theo loại
        btnlocloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.loctc);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final Spinner locloai = dialog.findViewById(R.id.locLoai);
                //Đổ dữ liệu vào spinner
                daoThuChi = new chucnangloaithuchi(getActivity());
                listTC = daoThuChi.getThuChi(0);
                final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.spiner_loaithuchi, listTC);
                locloai.setAdapter(sp);
                //Lọc
                final Button btnloc = dialog.findViewById(R.id.loc);
                btnloc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThuChi tc = (ThuChi) locloai.getSelectedItem();
                        btnlocloai.setText(tc.toString());
                        String loailoc = tc.toString();
                        list.clear();
                        list = chucnangthuchi.LocLoaiKhoanTheoLoai(loailoc);
                                          if (list.size() != 0 ) {
                                              final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông Báo","Đang lọc kết quả...");
                                              Handler handler = new Handler();
                                              handler.postDelayed(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      progressDialog.dismiss();
                        adapter = new KhoanThuAdapter(getContext(), R.layout.listview_item, list);
                        rcv.setAdapter(adapter);
                        Toast.makeText(getActivity(), "Lọc thành công!", Toast.LENGTH_SHORT).show();
                                                  }
                                              },1000);
                                          } else {
                                              Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_LONG).show();
                                          }

                        dialog.dismiss();
                    }
                });

                //Hủy lọc
                final Button btnhuy = dialog.findViewById(R.id.huyloc);
                btnhuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

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
                title.setText("TÌM KHOẢN THU NHẬP");
                edt_TimLoaiChi.setHint("Nhập khoản thu nhập cần tìm...");

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
                        list = chucnangthuchi.Timkiem(0, themText);
                        if (list.size() != 0 ) {
                            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông Báo","Đang tìm kiếm...");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item, list);
                                    rcv.setAdapter(adapter);
                                    Toast.makeText(getActivity(), "Tìm thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            },1500);

                        } else {
                            Toast.makeText(getActivity(), "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                            edt_TimLoaiChi.setText("");
                            edt_TimLoaiChi.setError(themText+" không tồn tại!");
                        }
                    }
                });

                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = chucnangthuchi.getGDtheoTC(0);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rcv.setLayoutManager(layoutManager);
                        adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item,list);
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
                dialog.setContentView(R.layout.them_khoan_thuchi);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText moTaGd = dialog.findViewById(R.id.them_mota_gd);
                final TextView ngayGd = dialog.findViewById(R.id.them_ngay_gd);
                final EditText tienGd = dialog.findViewById(R.id.them_tien_gd);
                final Spinner spLoaiGd = dialog.findViewById(R.id.spLoaiGd);
                final TextView title = dialog.findViewById(R.id.titleThemKhoan);
                final TextView ghichuGd = dialog.findViewById(R.id.them_ghichu_gd);
                final Button xoa = dialog.findViewById(R.id.xoaTextGD);
                final Button them = dialog.findViewById(R.id.btnThemGD);
                final Button voice = dialog.findViewById(R.id.voice_to_text);
                daoThuChi = new chucnangloaithuchi(getActivity());
                listTC = daoThuChi.getThuChi(0);
                //Set tiêu đề
                title.setText("THÊM KHOẢN THU");
                String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                ngayGd.setText(currentDate);

                //Khi nhấn mic hiện mục thu âm giọng nói
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
                mSpeechRecordIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mSpeechRecordIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mSpeechRecordIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {

                    }

                    @Override
                    public void onBeginningOfSpeech() {

                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {

                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {

                    }

                    @Override
                    public void onEndOfSpeech() {

                    }

                    @Override
                    public void onError(int error) {

                    }

                    @Override
                    public void onResults(Bundle results) {
                        ArrayList<String> text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (text!=null){
                            ghichuGd.setText(text.get(0));
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {

                    }
                });


                voice.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_UP:
                                speechRecognizer.stopListening();
                                ghichuGd.setHint("Nhấn giữ biểu tượng mic để nói...");
                                break;

                            case MotionEvent.ACTION_DOWN:
                                ghichuGd.setText("");
                                ghichuGd.setHint("Khi nói xong hãy thả tay! Đang nghe...");
                                speechRecognizer.startListening(mSpeechRecordIntent);
                                break;
                        }
                        return false;
                    }
                });

                //Khi nhấn ngày hiện lên lựa chọ ngày
                ngayGd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        int m = calendar.get(Calendar.MONTH);
                        int y = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String NgayGD = dayOfMonth + "/" + (month + 1) + "/" + year;
                                ngayGd.setText(NgayGD);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });

                //Đổ dữ liệu vào spinner
                final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.spiner_loaithuchi, listTC);
                spLoaiGd.setAdapter(sp);
                //Khi nhấn nút xóa
                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(), "Xóa",Toast.LENGTH_SHORT).show();
                        list = chucnangthuchi.getGDtheoTC(0);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rcv.setLayoutManager(layoutManager);
                        adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item,list);
                        rcv.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
                //Khi nhấn nút Thêm
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(), "Thêm", Toast.LENGTH_SHORT).show();
                        if (listTC.size() == 0) {
                            dialog.dismiss();
                            TextView tieude;
                            Button themltc;
                            Dialog dialoga = new Dialog(getActivity());
                            dialoga.setContentView(R.layout.popupchuacodullieu);
                            dialoga.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            tieude = dialoga.findViewById(R.id.txttieude);
                            themltc = dialoga.findViewById(R.id.btn_yes);
                            tieude.setText("Cần ít nhất một loại thu nhập! Hãy thêm ngay!");
                            themltc.setOnClickListener(v1 -> {
                                dialoga.dismiss();
                            });
                            dialoga.show();
                            return;
                        }
                        String mota = moTaGd.getText().toString();
                        String ngay = ngayGd.getText().toString();
                        String tien = tienGd.getText().toString();
                        String ghichu = ghichuGd.getText().toString();
                        ThuChi tc = (ThuChi) spLoaiGd.getSelectedItem();
                        int ma = tc.getMaKhoan();
                        //Check lỗi
                        if (mota.isEmpty() && tien.isEmpty()) {
                            moTaGd.setError("Không được để trống!");
                            tienGd.setError("Không được để trống!");
                        } else {
                            try {
                                GiaoDich gd = new GiaoDich(0, mota, dfm.parse(ngay), Integer.parseInt(tien), ma, ghichu);
                                list.clear();
                                if (chucnangthuchi.themGD(gd) == true) {
                                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông Báo","Đang thêm...");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            list.clear();
                                            list.addAll(chucnangthuchi.getGDtheoTC(0));
                                            adapter.notifyDataSetChanged();
                                            list = chucnangthuchi.getGDtheoTC(0);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                            rcv.setLayoutManager(layoutManager);
                                            adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item,list);
                                            rcv.setAdapter(adapter);
                                            dialog.dismiss();
                                        }
                                    },1000);

                                } else {
                                    Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                    }
                });

                dialog.show();
            }
        });
        return view;


    }


}
