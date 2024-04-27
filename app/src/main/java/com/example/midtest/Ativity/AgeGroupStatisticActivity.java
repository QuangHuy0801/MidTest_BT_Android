package com.example.midtest.Ativity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgeGroupStatisticActivity extends AppCompatActivity {
    TextView tvtitle;
    Statistic item;
    ImageView ivback;
    AnyChartView anyChartView;
    Button btnEPF;
    List<Object[]> objects = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_group_statistic);
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBar3dChart();
    }

    private void setupBar3dChart(){
        API.api.AgeGroupRevenue().enqueue(new Callback<List<Object[]>>() {
            @Override
            public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    objects = response.body();
                    if (objects.isEmpty()) {
                        Toast.makeText(AgeGroupStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        List<DataEntry> data = new ArrayList<>();
                        int totalProducts = 0;
                        for (int i = 0; i < objects.size(); i++) {
                            Object[] object = objects.get(i);
                            String category_name = (String) object[0]; // Lấy tên danh mục ở chỉ mục 0

                            // Lấy doanh thu từ các đối tượng trong mảng
                            Double revenue_under_18 = (Double) object[1];
                            Double revenue_18_to_30 = (Double) object[2];
                            Double revenue_over_30 = (Double) object[3];

                            // Thêm các giá trị đã trích xuất vào danh sách data
                            data.add(new CustomDataEntry(category_name , revenue_under_18,revenue_18_to_30,revenue_over_30));
//

                            // Tính tổng số sản phẩm
                            totalProducts += revenue_under_18 + revenue_18_to_30 + revenue_over_30;
                        }
                        Cartesian3d bar3d = AnyChart.bar3d();

                        bar3d.animation(true);

                        bar3d.padding(10d, 40d, 5d, 20d);

                        bar3d.title(item.getTitle());

                        bar3d.yScale().minimum(0d);

                        bar3d.xAxis(0).labels()
                                .rotation(-90d)
                                .padding(0d, 0d, 20d, 0d);

                        bar3d.yAxis(0).labels().format("{%Value}{groupsSeparator: } VND");

                        bar3d.yAxis(0).title("Revenue in VND");

                        Set set = Set.instantiate();
                        set.data(data);
                        Mapping bar1Data = set.mapAs("{ x: 'x', value: 'value' }");
                        Mapping bar2Data = set.mapAs("{ x: 'x', value: 'value2' }");
                        Mapping bar3Data = set.mapAs("{ x: 'x', value: 'value3' }");

                        bar3d.bar(bar1Data)
                                .name("Under 18");

                        bar3d.bar(bar2Data)
                                .name("18 to 30");

                        bar3d.bar(bar3Data)
                                .name("Over 30");


                        bar3d.legend().enabled(true);
                        bar3d.legend().fontSize(13d);
                        bar3d.legend().padding(0d, 0d, 20d, 0d);

                        bar3d.interactivity().hoverMode(HoverMode.SINGLE);

                        bar3d.tooltip()
                                .positionMode(TooltipPositionMode.POINT)
                                .position("right")
                                .anchor(Anchor.LEFT_CENTER)
                                .offsetX(5d)
                                .offsetY(0d)
                                .format("{%Value} VND");

                        bar3d.zAspect("10%")
                                .zPadding(20d)
                                .zAngle(45d)
                                .zDistribution(true);
                        anyChartView.setChart(bar3d);

                    }
                } else {
                    Toast.makeText(AgeGroupStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object[]>> call, Throwable t) {
                Toast.makeText(AgeGroupStatisticActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setEvent() {
        ivbackCLick();
        exportFileOnclick();
    }

    private void exportFileOnclick() {
        btnEPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExcel(v);
            }
        });
    }
    public void createExcel(View view) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        ArrayList<String> headers = new ArrayList<>();
        headers.add("Mặt hàng");
        headers.add("Dưới 18 tuổi");
        headers.add("Từ 18 - 30 tuổi");
        headers.add("Trên 30 tuổi");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(headers.get(i));
        }

        for (int i = 0; i < objects.size(); i++) {
            Object[] object = objects.get(i);
            HSSFRow hssfRow1 = hssfSheet.createRow(i + 1);

            // Lấy dữ liệu từ objects và ghi vào các ô của dòng mới
            for (int j = 0; j < object.length; j++) {
                HSSFCell hssfCell = hssfRow1.createCell(j);
                hssfCell.setCellValue(String.valueOf(object[j]));
            }
        }

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ThongKeKhachHang.xls");
            FileOutputStream output = new FileOutputStream(file);
            hssfWorkbook.write(output);

            output.close();
            hssfWorkbook.close();

            Toast.makeText(this, "Tạo thành công: DOWNLOADS/ThongKeKhachHang.xls", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể tạo tập tin Excel", Toast.LENGTH_SHORT).show();
        }
    }

    private void ivbackCLick() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Double value, Double value2, Double value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }
    private void setControl() {
        anyChartView =findViewById(R.id.ANCProduce);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnEPF=findViewById(R.id.btnEPF);
        ivback=findViewById(R.id.ivback);
        item = (Statistic) getIntent().getSerializableExtra("detail");
        tvtitle=findViewById(R.id.tvtitle);
        tvtitle.setText(item.getTitle());
    }
}