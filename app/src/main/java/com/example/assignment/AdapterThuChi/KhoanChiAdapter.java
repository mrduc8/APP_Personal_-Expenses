package com.example.assignment.AdapterThuChi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.ArrayListThuChi.GiaoDich;
import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangloaithuchi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangthuchi;
import com.example.assignment.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class KhoanChiAdapter extends RecyclerView.Adapter<KhoanChiAdapter.ViewHolder> {
    private Context context;
    public static ArrayList<GiaoDich> list;
    private chucnangthuchi chucnangthuchi;
    private ArrayList<ThuChi> listTC = new ArrayList<>();
    private chucnangloaithuchi daoThuChi;
    private DatePickerDialog datePickerDialog;
    int layout;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private NumberFormat fm = new DecimalFormat("#,###");

    public KhoanChiAdapter() {
    }

    public KhoanChiAdapter(Context context, ArrayList<GiaoDich> list) {
        this.context = context;
        this.list = list;
    }

    public KhoanChiAdapter(Context context,int layout, ArrayList<GiaoDich> list) {
        this.context = context;
        this.list = list;
        this.layout=layout;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text, giatext, ngaytext;
        private ImageView  img_avataitem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textList);
            giatext = itemView.findViewById(R.id.gialist);
            ngaytext = itemView.findViewById(R.id.ngaylist);
            img_avataitem = itemView.findViewById(R.id.img_avataitem);
            relativeLayout = itemView.findViewById(R.id.relative_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.text.setText(list.get(position).getMoTaGd());
        holder.giatext.setText("-"+fm.format(list.get(position).getSoTien())+"đ ");
        holder.ngaytext.setText(" "+dfm.format(list.get(position).getNgayGd())+" ");
        chucnangthuchi = new chucnangthuchi(context);
        final GiaoDich gd = list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        context, R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.layout_sua_xoa_xemchitiet_item,
                        (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
                TextView txtXemchiTiet=bottomSheetView.findViewById(R.id.txt_XemChiTiet);
                TextView txtSuaKhoanChi=bottomSheetView.findViewById(R.id.txt_SuaThuChi);
                TextView txtXoa=bottomSheetView.findViewById(R.id.txt_XoaThuChi);
                txtSuaKhoanChi.setText("Sửa khoản chi");
                txtXemchiTiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        GiaoDich gd = list.get(position);
                        //Hiện thông tin giao dịch khi click vào item
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.thong_tin_gd);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        TextView mota, ngay, tien, loai, title, ghichu;
                        title = dialog.findViewById(R.id.thongtinGD);
                        mota = dialog.findViewById(R.id.mota_gd);
                        ngay = dialog.findViewById(R.id.ngay_gd);
                        tien = dialog.findViewById(R.id.tien_gd);
                        loai = dialog.findViewById(R.id.loai_gd);
                        ghichu = dialog.findViewById(R.id.ghichu_gd);
                        title.setText("THÔNG TIN CHI TIÊU");
                        mota.setText("Mô tả: "+ gd.getMoTaGd());
                        ngay.setText("Ngày "+ dfm.format(gd.getNgayGd()));
                        tien.setText("Giảm -"+fm.format(Math.abs(gd.getSoTien())) + " VND");
                        daoThuChi = new chucnangloaithuchi(context);
                        loai.setText("Loại chi: "+ daoThuChi.getTen(gd.getMaKhoan()));
                        ghichu.setText("Ghi chú: "+ gd.getGhiChuGd());
                        dialog.show();
                    }
                });


                txtSuaKhoanChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.them_khoan_thuchi);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final TextView moTaGd = dialog.findViewById(R.id.them_mota_gd);
                        final TextView ngayGd = dialog.findViewById(R.id.them_ngay_gd);
                        final TextView tienGd = dialog.findViewById(R.id.them_tien_gd);
                        final Spinner spLoaiGd = dialog.findViewById(R.id.spLoaiGd);
                        final TextView ghichuGd = dialog.findViewById(R.id.them_ghichu_gd);
                        final TextView title = dialog.findViewById(R.id.titleThemKhoan);
                        final Button xoa = dialog.findViewById(R.id.xoaTextGD);
                        final Button them = dialog.findViewById(R.id.btnThemGD);
                        daoThuChi = new chucnangloaithuchi(context);
                        listTC = daoThuChi.getThuChi(1);

                        //Set tiêu đề, text
                        title.setText("SỬA KHOẢN CHI");
                        them.setText("SỬA");
                        moTaGd.setText(gd.getMoTaGd());
                        ngayGd.setText(dfm.format(gd.getNgayGd()));
                        tienGd.setText(String.valueOf(gd.getSoTien()));
                        final ArrayAdapter sp = new ArrayAdapter(context, R.layout.spiner_loaithuchi, listTC);
                        spLoaiGd.setAdapter(sp);
                        ghichuGd.setText(gd.getGhiChuGd());
                        int vitri = -1;
                        for (int i = 0; i < listTC.size(); i++) {
                            if (listTC.get(i).getTenKhoan().equalsIgnoreCase(daoThuChi.getTen(gd.getMaKhoan()))) {
                                vitri = i;
                                break;
                            }
                        }
                        spLoaiGd.setSelection(vitri);


                        //Khi nhấn ngày hiện lên lựa chọ ngày
                        ngayGd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar calendar = Calendar.getInstance();
                                int d = calendar.get(Calendar.DAY_OF_MONTH);
                                int m = calendar.get(Calendar.MONTH);
                                int y = calendar.get(Calendar.YEAR);
                                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        final String NgayGD = dayOfMonth + "/" + month + "/" + year;
                                        ngayGd.setText(NgayGD);
                                    }
                                }, y, m, d);
                                datePickerDialog.show();
                            }
                        });

                        //Khi nhấn nút xóa
                        xoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                spLoaiGd.setAdapter(sp);
                            }
                        });

                        //Khi nhấn nút sửa
                        them.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mota = moTaGd.getText().toString();
                                String ngay = ngayGd.getText().toString();
                                String tien = tienGd.getText().toString();
                                String ghichu = ghichuGd.getText().toString();
                                ThuChi tc = (ThuChi) spLoaiGd.getSelectedItem();
                                int ma = tc.getMaKhoan();
                                //Check lỗi
                                if (mota.isEmpty() && ngay.isEmpty() && tien.isEmpty()) {
                                    Toast.makeText(context, "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        GiaoDich giaoDich = new GiaoDich(gd.getMaGd(), mota, dfm.parse(ngay), Integer.parseInt(tien), ma, ghichu);
                                        if (chucnangthuchi.suaGD(giaoDich) == true) {
                                            list.clear();
                                            list.addAll(chucnangthuchi.getGDtheoTC(1));
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
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

                txtXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.popup_xoa_item);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }                final TextView txt_Massage = dialog.findViewById(R.id.txt_Titleconfirm);
                        final Button btn_Yes = dialog.findViewById(R.id.btn_yes);
                        final Button btn_No = dialog.findViewById(R.id.btn_no);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        txt_Massage.setText("Bạn có muốn xóa " + list.get(position).getMoTaGd() + " hay không ? ");
                        btn_Yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (chucnangthuchi.xoaGD(gd) == true) {
                                    txt_Massage.setText("");
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            list.clear();
                                            list.addAll(chucnangthuchi.getGDtheoTC(1));
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    },2000);
                                }
                            }
                        });
                        btn_No.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                bottomSheetView.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        holder.img_avataitem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}