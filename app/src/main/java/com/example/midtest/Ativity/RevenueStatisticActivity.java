package com.example.midtest.Ativity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.midtest.Model.ReportTotal;
import com.example.midtest.Model.Statistic;
import com.example.midtest.R;
import com.example.midtest.Retrofit.API;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevenueStatisticActivity extends AppCompatActivity {
    TextView tvtitle;
    Statistic item;
    String datefrom, dateto;
    TextView dateformto,tvtotal;
    AnyChartView anyChartView;

    Button btnStartRevenue, btnChooseDateFrom, btnChooseDateTo;
    LinearLayout chartLayout;
    Button btnexportfile;
    List<ReportTotal> reportTotals = new ArrayList<>();

    Column column;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_statistic);
        setControl();
        setEvent();
    }


public void createExcel(View view)
{
    HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    HSSFSheet hssfSheet = hssfWorkbook.createSheet();


    ArrayList<String> headers = new ArrayList<>();
    headers.add("Mặt hàng");
    headers.add("Doanh Thu");

    HSSFRow hssfRow = hssfSheet.createRow(0);
    for (int i = 0; i < headers.size(); i++){
        HSSFCell hssfCell = hssfRow.createCell(i);
        hssfCell.setCellValue(headers.get(i));
    }

    for (int i = 0; i < reportTotals.size(); i++){
        HSSFRow hssfRow1 = hssfSheet.createRow(i+1);

        HSSFCell hssfCell = hssfRow1.createCell(0);
        hssfCell.setCellValue(reportTotals.get(i).getName());

        HSSFCell hssfCell1 = hssfRow1.createCell(1);
        hssfCell1.setCellValue(reportTotals.get(i).getValue());
    }

    try {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ThongKeDoanhThuMatHang.xls");;
        FileOutputStream output = new FileOutputStream(file);
        hssfWorkbook.write(output);

        output.close();
        hssfWorkbook.close();

        Toast.makeText(this, "Tạo thành công: DOWNLOADS/ThongKeDoanhThuMatHang.xls", Toast.LENGTH_LONG).show();

    } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, "Không thể tạo tập tin Excel ", Toast.LENGTH_SHORT).show();
    }


}

    private void setupChart() {
        if (dateto == null) {
            Calendar calendar = Calendar.getInstance();
            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH);
            int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
            dateto = yearNow + "-" + (monthNow + 1) + "-" + dayNow;
            btnChooseDateTo.setText(dateto);
        }
        if (datefrom == null) {
            datefrom = "2000-01-01";
            btnChooseDateFrom.setText(datefrom);
        }
        API.api.RevenueStatistic(datefrom, dateto).enqueue(new Callback<List<ReportTotal>>() {
            @Override
            public void onResponse(Call<List<ReportTotal>> call, Response<List<ReportTotal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportTotals = response.body();
                    if (reportTotals.isEmpty()) {
                        // Hiển thị thông báo nếu không có dữ liệu
                        Toast.makeText(RevenueStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        chartLayout.setVisibility(View.VISIBLE);
                        dateformto.setText("Từ: " + datefrom + "\nĐến: " + dateto);
                        // Tạo danh sách dữ liệu để vẽ biểu đồ
                        List<DataEntry> data = new ArrayList<>();
                        double totalRevenue = 0;
                        for (ReportTotal reportTotal : reportTotals) {
                            data.add(new ValueDataEntry(reportTotal.getName(), reportTotal.getValue()));
                            totalRevenue += reportTotal.getValue();
                        }
                        tvtotal.setText("Tổng: "+totalRevenue+" VND");
                        Cartesian cartesian = AnyChart.column();
                        // Nếu column chưa được khởi tạo, thực hiện khởi tạo
                        if (column == null) {
                            // Tạo biểu đồ cột
                            column = cartesian.column(data);

                            // Cấu hình tooltip
                            column.tooltip()
                                    .titleFormat("{%X}")
                                    .position(Position.CENTER_BOTTOM)
                                    .anchor(Anchor.CENTER_BOTTOM)
                                    .offsetX(0d)
                                    .offsetY(5d)
                                    .format("Tổng tiền: {%Value} vnd");
                            // Cấu hình biểu đồ
                            cartesian.animation(true);
                            cartesian.yScale().minimum(0d);
                            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                            cartesian.interactivity().hoverMode(HoverMode.BY_X);
                            cartesian.xAxis(0).title("Mặt Hàng");
                            cartesian.yAxis(0).title("Doanh thu");
                            cartesian.title(item.getTitle());
                            anyChartView.setChart(cartesian);
                        } else {
                            column.data(data);
                        }

                    }
                } else {
                    Toast.makeText(RevenueStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    chartLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ReportTotal>> call, Throwable t) {
                Toast.makeText(RevenueStatisticActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showDatePickerDialogFrom(View v) {

        final Calendar calendar = Calendar.getInstance();
        int yearfrom = calendar.get(Calendar.YEAR);
        int monthfrom = calendar.get(Calendar.MONTH);
        int dayfrom = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày tháng
                        String selectedDate = selectedYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        btnChooseDateFrom.setText(selectedDate);
                        datefrom = selectedDate;
                    }
                }, yearfrom, monthfrom, dayfrom);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    public void showDatePickerDialogTo(View v) {
        // Lấy ngày tháng năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int yearto = calendar.get(Calendar.YEAR);
        int monthto = calendar.get(Calendar.MONTH);
        int dayto = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày tháng
                        String selectedDate = selectedYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        btnChooseDateTo.setText(selectedDate);
                        dateto = selectedDate;
                    }

                }, yearto, monthto, dayto);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    private void exportFileOnclick() {
        btnexportfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExcel(v);
            }
        });
    }
    private void startRevenueOnclick() {
        btnStartRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChart();
            }
        });
    }

    public void onBackClicked(View view) {
        finish();
    }
    private void setEvent() {
        startRevenueOnclick();
        exportFileOnclick();

    }

    private void setControl() {
        chartLayout = findViewById(R.id.layoutchart);
        btnChooseDateFrom = findViewById(R.id.btnChooseDateFrom);
        btnChooseDateTo = findViewById(R.id.btnChooseDateTo);
        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        chartLayout.setVisibility(View.INVISIBLE);
        btnStartRevenue = findViewById(R.id.btnStartRevenue);
        dateformto = findViewById(R.id.datefromto);
        btnexportfile = findViewById(R.id.btnexportfile);
        tvtotal=findViewById(R.id.tvtotal);
        item = (Statistic) getIntent().getSerializableExtra("detail");
        tvtitle=findViewById(R.id.tvtitle);
        tvtitle.setText(item.getTitle());
    }
}