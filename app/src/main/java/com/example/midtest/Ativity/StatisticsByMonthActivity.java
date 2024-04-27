package com.example.midtest.Ativity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipDisplayMode;
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

public class StatisticsByMonthActivity extends AppCompatActivity {
    Spinner spinnerYear;
    ArrayAdapter<String> adapter;
    TextView tvtitle;
    Statistic item;

    TextView tvtotal;
    AnyChartView anyChartView;

    Button btnStartRevenue;
    LinearLayout chartLayout;
    Button btnexportfile;
    List<ReportTotal> reportTotals = new ArrayList<>();

    Bar bar;
    String[] years = {"2020", "2021", "2022", "2023","2024", "2025"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_by_month);
        setControl();
        setEvent();
    }

    private void setControl() {
        spinnerYear = findViewById(R.id.spinnerYear);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
        chartLayout = findViewById(R.id.layoutchart);
        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        chartLayout.setVisibility(View.INVISIBLE);
        btnStartRevenue = findViewById(R.id.btnStartRevenue);
        btnexportfile = findViewById(R.id.btnexportfile);
        tvtotal=findViewById(R.id.tvtotal);
        item = (Statistic) getIntent().getSerializableExtra("detail");
        tvtitle=findViewById(R.id.tvtitle);
        tvtitle.setText(item.getTitle());
    }

    private void setEvent() {
        startRevenueOnclick();
        exportFileOnclick();
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
    public void createExcel(View view)
    {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();


        ArrayList<String> headers = new ArrayList<>();
        headers.add("Tháng");
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
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ThongKeDoanhThuThang.xls");;
            FileOutputStream output = new FileOutputStream(file);
            hssfWorkbook.write(output);

            output.close();
            hssfWorkbook.close();

            Toast.makeText(this, "Tạo thành công: DOWNLOADS/ThongKeDoanhThuThang.xls", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể tạo tập tin Excel ", Toast.LENGTH_SHORT).show();
        }


    }

    private void setupChart() {
        String selectedYear = spinnerYear.getSelectedItem().toString();
        API.api.MonthStatistic(selectedYear).enqueue(new Callback<List<ReportTotal>>() {
            @Override
            public void onResponse(Call<List<ReportTotal>> call, Response<List<ReportTotal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportTotals = response.body();
                    if (reportTotals.isEmpty()) {
                        // Hiển thị thông báo nếu không có dữ liệu
                        Toast.makeText(StatisticsByMonthActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        chartLayout.setVisibility(View.VISIBLE);
                        // Tạo danh sách dữ liệu để vẽ biểu đồ
                        List<DataEntry> data = new ArrayList<>();
                        double totalRevenue = 0;
                        for (ReportTotal reportTotal : reportTotals) {
                            data.add(new ValueDataEntry(reportTotal.getName(), reportTotal.getValue()));
                            totalRevenue += reportTotal.getValue();
                        }
                        tvtotal.setText(totalRevenue+" VND");

                        // Tạo biểu đồ AnyChart Vertical Combination of Bar and Jump Line Chart
                        Cartesian vertical = AnyChart.vertical();
                        if (bar == null) {
                            vertical.animation(true)
                                    .title("Biểu đồ thông kê doanh thu theo tháng");

                            // Tạo dữ liệu cho biểu đồ
                            Set set = Set.instantiate();
                            set.data(data);
                            Mapping barData = set.mapAs("{ x: 'x', value: 'value' }");
                            bar = vertical.bar(barData);
                            bar.labels().format("{%Value} VND");

                            // Cấu hình các thành phần của biểu đồ
                            vertical.yScale().minimum(0d);
                            vertical.labels(true);
                            vertical.tooltip()
                                    .displayMode(TooltipDisplayMode.UNION)
                                    .positionMode(TooltipPositionMode.POINT)
                                    .unionFormat(
                                            "function() {\n" +
                                                    "      return 'Plain: ' + this.points[1].value + ' VND' +\n" +
                                                    "        '\\n' + 'Fact: ' + this.points[0].value + ' VND';\n" +
                                                    "    }");
                            vertical.interactivity().hoverMode(HoverMode.BY_X);
                            vertical.xAxis(true);
                            vertical.yAxis(true);
                            vertical.yAxis(0).labels().format("{%Value} VND");

                            // Hiển thị biểu đồ trên AnyChartView
                            anyChartView.setChart(vertical);
                        } else {
                            // Đặt dữ liệu mới cho column
                            bar.data(data);
                        }
                    }
                } else {
                    Toast.makeText(StatisticsByMonthActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    chartLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ReportTotal>> call, Throwable t) {
                Toast.makeText(StatisticsByMonthActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}