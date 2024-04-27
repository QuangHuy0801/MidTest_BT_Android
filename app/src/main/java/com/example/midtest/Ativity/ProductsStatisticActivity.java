package com.example.midtest.Ativity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsStatisticActivity extends AppCompatActivity {
    TextView tvtitle;
    Statistic item;
    ImageView ivback;
    AnyChartView anyChartView;
    Button btnEPFProduct;
    List<ReportTotal> reportTotals = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_statistic);
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPieChart();
    }


    private void ivbackCLick() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupPieChart(){
        API.api.ProductStatistic().enqueue(new Callback<List<ReportTotal>>() {
            @Override
            public void onResponse(Call<List<ReportTotal>> call, Response<List<ReportTotal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportTotals = response.body();
                    if (reportTotals.isEmpty()) {
                        Toast.makeText(ProductsStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        Pie pie = AnyChart.pie();

                        List<DataEntry> data = new ArrayList<>();
                        int totalProducts = 0;
                        for (int i = 0; i < reportTotals.size(); i++){
                            data.add(new ValueDataEntry(reportTotals.get(i).getName(), reportTotals.get(i).getValue()));
                            totalProducts += reportTotals.get(i).getValue();
                        }

                        pie.data(data);
                        pie.palette( new String[]{"#FFCCFF", "#FF3333", "#FFFF33", "#0066FF", "#66FF99","#CCCCCC"});
                        pie.title(item.getTitle()+": còn "+totalProducts+" sản phẩm");
                        pie.labels().position("outside");

                        pie.legend()
                                .position("center-bottom")
                                .itemsLayout(LegendLayout.HORIZONTAL)
                                .align(Align.CENTER);

                        anyChartView.setChart(pie);
                    }
                } else {
                    Toast.makeText(ProductsStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReportTotal>> call, Throwable t) {
                Toast.makeText(ProductsStatisticActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void exportFileOnclick() {
        btnEPFProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExcel(v);
            }
        });
    }

    public void createExcel(View view)
    {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();


        ArrayList<String> headers = new ArrayList<>();
        headers.add("Mặt hàng");
        headers.add("Số lượng Sản phẩm");

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
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ThongKeSanPhamKho.xls");
            FileOutputStream output = new FileOutputStream(file);
            hssfWorkbook.write(output);

            output.close();
            hssfWorkbook.close();

            Toast.makeText(this, "Tạo thành công: DOWNLOADS/ThongKeSanPhamKho.xls", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể tạo tập tin Excel ", Toast.LENGTH_SHORT).show();
        }
    }
    private void setEvent() {
        ivbackCLick();
        exportFileOnclick();
    }
    private void setControl() {
        anyChartView =findViewById(R.id.ANCProduce);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnEPFProduct=findViewById(R.id.btnEPFProduct);
        ivback=findViewById(R.id.ivback);
        item = (Statistic) getIntent().getSerializableExtra("detail");
        tvtitle=findViewById(R.id.tvtitle);
        tvtitle.setText(item.getTitle());
    }
}