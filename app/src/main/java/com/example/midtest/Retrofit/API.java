package com.example.midtest.Retrofit;

import java.util.List;
import com.example.midtest.Model.ReportTotal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    RetrofitService retrofitService = new RetrofitService();
    API api = retrofitService.getRetrofit().create(API.class);
    @GET("/revenue-statistic")
    Call<List<ReportTotal>> RevenueStatistic(@Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo);
    @GET("/quantity-statistic")
    Call<List<ReportTotal>> QuantityStatistic(@Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo);
    @GET("/product-statistic")
    Call<List<ReportTotal>> ProductStatistic();
    @GET("/unit-of-product-statistic")
    Call<List<ReportTotal>> UnitOfProductStatistic();

    @GET("/age-group")
    Call<List<ReportTotal>> AgeGroup();
    @GET("/age-group-revenue")
    Call<List<Object[]>> AgeGroupRevenue();
    @GET("/month-statistic")
    Call<List<ReportTotal>> MonthStatistic(@Query("year") String year);
}
