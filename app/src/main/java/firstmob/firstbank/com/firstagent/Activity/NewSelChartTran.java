package firstmob.firstbank.com.firstagent.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import firstmob.firstbank.com.firstagent.adapter.MyPerfServAdapt;
import firstmob.firstbank.com.firstagent.adapter.TxnAdapter;
import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants;
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment;
import firstmob.firstbank.com.firstagent.model.GetCommPerfData;
import firstmob.firstbank.com.firstagent.model.GetMyPerfServData;
import firstmob.firstbank.com.firstagent.model.GetSummaryData;
import firstmob.firstbank.com.firstagent.model.TxnList;
import firstmob.firstbank.com.firstagent.network.ApiInterface;
import firstmob.firstbank.com.firstagent.network.RetrofitInstance;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.SessionManagement;
import firstmob.firstbank.com.firstagent.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewSelChartTran extends Fragment implements View.OnClickListener, OnChartValueSelectedListener, DateRangePickerFragment.OnDateRangeSelectedListener, OnChartGestureListener {

    Spinner sp1;

    private BarChart mChart, mChart2;
    private LineChart mLineChart;
    private Typeface mTf;
    List<TxnList> planetsList = new ArrayList<TxnList>();
    TxnAdapter aAdpt;
    MyPerfServAdapt mypfadapt;
    ListView lv, lvserv;
 //   ListView lvpie
    Button ok, calendar;
    public String finalfx, finpfrom, finpto;
    ProgressDialog prgDialog2;
    RadioButton r1, r2, r3;
    LinearLayout layl;
    RelativeLayout ryl;
    ArrayList<PieEntry> finentry = new ArrayList<PieEntry>();
    ArrayList<BarEntry> barent = new ArrayList<BarEntry>();
    ArrayList<LegendEntry> legendent = new ArrayList<LegendEntry>();
    List<GetSummaryData> temp = new ArrayList<GetSummaryData>();
    List<GetCommPerfData> cperfdata = new ArrayList<GetCommPerfData>();
    ArrayList<Entry> listentry = new ArrayList<Entry>();
    SessionManagement session;
    private TextView emptyView, fromdate, endate;
    TextView succtrans;
    String fromd, endd, firtxt;
    PieDataSet dataSet;
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<String> labelslnchart = new ArrayList<>();
    List<GetMyPerfServData> myperfdata = new ArrayList<GetMyPerfServData>();
    Legend l, l2;
    PieChart pieChart;
    ProgressDialog pro;

    public NewSelChartTran() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.newselcharttran, container, false);
        pro = new ProgressDialog(getActivity());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "musleo.ttf");
        layl = (LinearLayout) rootView.findViewById(R.id.layl);
        ryl = (RelativeLayout) rootView.findViewById(R.id.rl2);

        lv = (ListView) rootView.findViewById(R.id.lv);
        lvserv = (ListView) rootView.findViewById(R.id.lvserv);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        fromdate = (TextView) rootView.findViewById(R.id.bnamebn);
        succtrans = (TextView) rootView.findViewById(R.id.succtrans);
        ok = (Button) rootView.findViewById(R.id.button2);
        calendar = (Button) rootView.findViewById(R.id.button4);
        ok.setOnClickListener(this);
        calendar.setOnClickListener(this);
        session = new SessionManagement(getActivity());

       // lvpie = (ListView) rootView.findViewById(R.id.lvpie);

        prgDialog2 = new ProgressDialog(getActivity());
        prgDialog2.setMessage("Loading ....");
        // Set Cancelable as False
        session = new SessionManagement(getActivity());

        prgDialog2.setCancelable(false);


        String dated = getToday();
        String datew = getWeek();
        finpfrom = datew;
        finpto = dated;
        mChart2 = (BarChart) rootView.findViewById(R.id.chart2);
        mChart2.setOnChartValueSelectedListener(this);

        mChart2.setDrawBarShadow(false);
        Description ds2 = new Description();
        ds2.setText("Transaction amount in NGN");

        mChart2.setDescription(ds2);
        mChart2.getDescription().setPosition(3f, 3f);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn


        // scaling can now only be done on x- and y-axis separately
        mChart2.setPinchZoom(true);

        mChart2.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);


        XAxis xAxis2 = mChart2.getXAxis();

        xAxis2.setTypeface(mTf);
        xAxis2.setDrawGridLines(false);
        xAxis2.setGranularity(1f); // only intervals of 1 day

        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextSize(7f);

        l2 = mChart2.getLegend();


        l2.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l2.setForm(Legend.LegendForm.DEFAULT);
        l2.setFormSize(6f);
        l2.setTextSize(6f);
        l2.setXOffset(2);

        l2.setXEntrySpace(4f); // set the space between the legend entries on the x-axis
        l2.setYEntrySpace(4f); // set the space between the legend entries on the y-axis


        mChart2.getAxisLeft().setDrawLabels(false);
        mChart2.getAxisRight().setDrawLabels(false);
        mChart2.getXAxis().setDrawLabels(false);

        pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setCenterText("");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(40f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setOnChartValueSelectedListener(this);

        //     setPieData(4, 100);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        l.setYOffset(0f);


        l.setFormSize(6f);
        l.setTextSize(6f);
        l.setXOffset(2);

        l.setXEntrySpace(4f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(4f); // set the space between the legend entries on the y-axis

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.setEntryLabelTextSize(8f);

        Calendar cal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH); // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        now.set(year, month, 01);

        SimpleDateFormat format1 = new SimpleDateFormat("" +
                "MMMM dd yyyy");
        System.out.println(cal.getTime());
// Output "Wed Sep 26 14:23:28 EST 2012"

        String formattednow = format1.format(cal.getTime());
        String formattedstartdate = format1.format(now.getTime());
// Output "2012-09-26"
        String date = "You picked the following date: From- " + formattedstartdate + " To " + formattednow;

        //  fromdate.setText(date);
        //  checkInternetConnection2();


        month = month + 1;

        String frmdymonth = Integer.toString(day);
        if (day < 10) {
            frmdymonth = "0" + frmdymonth;
        }
        String frmyear = Integer.toString(year);
        frmyear = frmyear.substring(2, 4);
        String tdate = frmdymonth + "-" + (month) + "-" + frmyear;
        String firdate = "01" + "-" + (month) + "-" + frmyear;

        Calendar calfrom = Calendar.getInstance();
        calfrom.set(year, month, 1);


        //   loadDataset(firdate, tdate);


        String commdata = session.getString(SessionManagement.COMMDATA);
        String summdata = session.getString(SessionManagement.SUMMDATA);
        firtxt = session.getString(SessionManagement.MYPERFTEXT);
        if (Utility.isNotNull(commdata) && Utility.isNotNull(summdata) && Utility.isNotNull(firtxt)) {
            try {
                JSONArray jscomm = new JSONArray(commdata);

                JSONArray jssumm = new JSONArray(summdata);
                SetActivityLogic(jssumm, jscomm);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
        Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                if (Utility.isNotNull(fromd) || Utility.isNotNull(endd)) {
                    loadDataset(fromd, endd);
                } else {
                    Toast.makeText(getActivity(), "Please select appropriate from date and end date", Toast.LENGTH_LONG).show();
                }
            }

        }
        if (view.getId() == R.id.button4) {

            DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false);
            dateRangePickerFragment.show(getFragmentManager(), "datePicker");


        }
    }


    public void loadDataset(String fromdt, String enddt) {
        prgDialog2.show();
        SecurityLayer.Log("From Date", fromdt);
        SecurityLayer.Log("End Date", enddt);
        final PieData[] data = {null};
        ApiInterface apiService =
                RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA");
        String params = "1/" + usid + "/" + agentid + "/" + mobnoo + "/CMSNRPT/" + fromdt + "/" + enddt;
        Loadd(params);
    }


    public String getToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SecurityLayer.Log("Date Today is", sdf.format(date));
        String datetod = sdf.format(date);
        return datetod;
    }

    public String getWeek() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        Date date = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SecurityLayer.Log("Date Week is", sdf.format(date));
        String datetod = sdf.format(date);
        return datetod;
    }

    public String getMonth() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        Date date = new Date(System.currentTimeMillis() - (30 * DAY_IN_MS));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SecurityLayer.Log("Date Month is", sdf.format(date));
        String datetod = sdf.format(date);
        return datetod;
    }


    private void Loadd(String params) {

        String endpoint = "report/genrpt.action";
        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint);
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror", e.toString());
        }


        ApiInterface apiService =
                RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int counter = 0;
                barent.clear();
                myperfdata.clear();
                try {
                    // JSON Object


                    SecurityLayer.Log("Cable TV Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());

                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj);
                    SecurityLayer.Log("decrypted_response", obj.toString());


                    JSONObject comdatas = obj.optJSONObject("data");
                    JSONArray comperf = comdatas.optJSONArray("transaction");
                    JSONArray summdata = comdatas.optJSONArray("summary");
                    boolean chktxncnt = false;
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");

                        SecurityLayer.Log("Response Message", responsemessage);
                        ArrayList<String> labelsstr = new ArrayList<>();

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);
                                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                                ArrayList<IBarDataSet> comdataSets = new ArrayList<IBarDataSet>();
                                ArrayList<Integer> colors = new ArrayList<Integer>();
                                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


                                for (int c : ColorTemplate.VORDIPLOM_COLORS)
                                    colors.add(c);

                                for (int c : ColorTemplate.JOYFUL_COLORS)
                                    colors.add(c);

                                for (int c : ColorTemplate.COLORFUL_COLORS)
                                    colors.add(c);

                                for (int c : ColorTemplate.LIBERTY_COLORS)
                                    colors.add(c);

                                for (int c : ColorTemplate.PASTEL_COLORS)
                                    colors.add(c);
                                colors.add(ColorTemplate.getHoloBlue());
                                if (respcode.equals("00")) {
                                    SecurityLayer.Log("JSON Aray", comperf.toString());
                                    if (summdata.length() > 0) {


                                        JSONObject json_data = null;
                                        for (int i = 0; i < summdata.length(); i++) {
                                            json_data = summdata.getJSONObject(i);
                                            //String accid = json_data.getString("benacid");


                                            String txncode = json_data.optString("txnCode");

                                            String amon = json_data.optString("amount");
                                            String status = json_data.optString("status");
                                            String agcmsn = json_data.optString("agentCmsn");
                                            String txnname = Utility.convertTxnCodetoServ(txncode);
                                            float dbagcmsn = Float.parseFloat(amon);
                                            float agamcmn = Float.parseFloat(agcmsn);
                                            if (Utility.isNotNull(status)) {
                                                if (((status.equals("SUCCESS"))) && (dbagcmsn > 0)) {
                                                    //   finentry.add(new PieEntry(dbagcmsn, txnname));
                                                    ArrayList<BarEntry> barnt = new ArrayList<BarEntry>();
                                                    SecurityLayer.Log("Log Amount", Double.toString(Double.parseDouble(agcmsn)));
                                                    myperfdata.add(new GetMyPerfServData(txnname, "", Utility.roundto2dp(Double.toString(agamcmn)) + Constants.KEY_NAIRA));
                                                    barnt.add(new BarEntry(i, dbagcmsn, txnname));

                                                    BarDataSet set1;

                                                    set1 = new BarDataSet(barnt, txnname);

                                                    set1.setColor(colors.get(i));
                                                    //  }
                                                    dataSets.add(set1);
                                                    ArrayList<BarEntry> bcmnt = new ArrayList<BarEntry>();

                                                    bcmnt.add(new BarEntry(i, agamcmn, txnname));
                                                    labelsstr.add(txnname);

                                                    chktxncnt = true;
                                                    SecurityLayer.Log("Entries", dbagcmsn + " Code" + txnname);
                                                    BarDataSet set2;

                                                    set2 = new BarDataSet(bcmnt, txnname);

                                                    set2.setColor(colors.get(i));
                                                    //  }
                                                    comdataSets.add(set2);
                                                    entries.add(new PieEntry((float) agamcmn,
                                                            txnname));

                                                }
                                            }

                                        }

                                        mypfadapt = new MyPerfServAdapt(myperfdata, getActivity());
                                        lvserv.setAdapter(mypfadapt);
                                        BarData databar = new BarData(dataSets);

                                        PieDataSet dataSet = new PieDataSet(entries, "");

                                        // dataSet.setDrawIcons(false);

                                        dataSet.setSliceSpace(3f);

                                        dataSet.setSelectionShift(5f);

                                        // add a lot of colors


                                        colors.add(ColorTemplate.getHoloBlue());

                                        dataSet.setColors(colors);
                                        //dataSet.setSelectionShift(0f);

                                        PieData data = new PieData(dataSet);
                                        data.setValueFormatter(new PercentFormatter());
                                        data.setValueTextSize(7f);
                                        data.setValueTextColor(Color.BLACK);

                                        pieChart.setData(data);

                                        // undo all highlights
                                        pieChart.highlightValues(null);

                                        pieChart.invalidate();


                                        BarData comdata = new BarData(comdataSets);

                                        mChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsstr));
                                        mChart2.setData(databar);
                                        mChart2.invalidate();
                                        JSONObject json_cperfdata = null;
                                        int cntr = 0;
                                        for (int i = 0; i < comperf.length(); i++) {
                                            json_cperfdata = comperf.getJSONObject(i);
                                            //String accid = json_data.getString("benacid");


                                            String txnCode = json_cperfdata.optString("txnCode");
                                            double agentCmsn = json_cperfdata.optDouble("agentCmsn");
                                            String txndateTime = json_cperfdata.optString("txndateTime");
                                            String amount = json_cperfdata.optString("amount");
                                            String status = json_cperfdata.optString("status");
                                            String toAcNum = json_cperfdata.optString("toAcNum");
                                            String refNumber = json_cperfdata.optString("refNumber");
                                            txndateTime = Utility.convertPerfDate(txndateTime);
                                            if (((status.equals("SUCCESS"))) && (agentCmsn > 0)) {
                                                listentry.add(new Entry(cntr, Float.parseFloat(Double.toString(agentCmsn))));

                                                labelslnchart.add(cntr, txndateTime);
                                                cntr++;
                                                SecurityLayer.Log("Counter", Integer.toString(cntr));
                                            }


                                        }


                                        if (cntr > 0) {
                                            //  setData(listentry);
                                        }
                                        SecurityLayer.Log("Transaction counter", Integer.toString(cntr));
                                        succtrans.setText(Integer.toString(cntr));
                                        if (chktxncnt == false) {
                                            Toast.makeText(
                                                    getActivity(),
                                                    "There are no transaction records to display",
                                                    Toast.LENGTH_LONG).show();
                                        }


                                    } else {
                                        Toast.makeText(
                                                getActivity(),
                                                "There are no records to display",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    if (comperf.length() > 0) {


                                        JSONObject json_data = null;
                                        for (int i = 0; i < comperf.length(); i++) {
                                            json_data = comperf.getJSONObject(i);
                                            //String accid = json_data.getString("benacid");


                                            String txncode = json_data.optString("txnCode");

                                            String amon = json_data.optString("amount");
                                            String status = json_data.optString("status");
                                            String txnname = Utility.convertTxnCodetoServ(txncode);
                                            float dbagcmsn = Float.parseFloat(amon);
                                            if (((status.equals("SUCCESS"))) && (dbagcmsn > 0)) {
                                                counter++;

                                            }

                                        }


                                    }

                                } else {
                                    Toast.makeText(
                                            getActivity(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                Toast.makeText(
                                        getActivity(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(
                                    getActivity(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();


                        }
                    } else {

                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }
                    // prgDialog2.dismiss();


                } catch (NumberFormatException e) {
                    SecurityLayer.Log("numberformatexception", e.toString());
                    //  ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getActivity());
                    // SecurityLayer.Log(e.toString());
                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    if (!(getActivity() == null)) {
                        Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();

                       SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());

                    }// SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("generalexception", e.toString());
                    if (!(getActivity() == null)) {
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                    }
                    // SecurityLayer.Log(e.toString());
                }

                prgDialog2.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error", t.toString());

                if (!(getActivity() == null)) {
                    Toast.makeText(
                            getActivity(),
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show();


                    prgDialog2.dismiss();
                    SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                }
            }
        });

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        Log.d("range : ", "from: " + startDay + "-" + startMonth + "-" + startYear + " to : " + endDay + "-" + endMonth + "-" + endYear);
        String date = "You picked the following date: From- " + startDay + "/" + (++startMonth) + "/" + startYear + " To " + endDay + "/" + (++endMonth) + "/" + endYear;
        Calendar calfrom = Calendar.getInstance();
        Calendar calto = Calendar.getInstance();
        calto.set(endYear, endMonth, endDay);
        calfrom.set(startYear, startMonth, startDay);

        if (calfrom.before(calto)) {
            fromdate.setText(date);
            String frmdymonth = Integer.toString(startDay);
            if (startDay < 10) {
                frmdymonth = "0" + frmdymonth;
            }
            String frmyear = Integer.toString(startYear);
            frmyear = frmyear.substring(2, 4);
            fromd = frmdymonth + "-" + (startMonth) + "-" + frmyear;
            String frmenddymonth = Integer.toString(endDay);
            if (endDay < 10) {
                frmenddymonth = "0" + frmenddymonth;
            }

            String frmendyr = Integer.toString(endYear);
            frmendyr = frmendyr.substring(2, 4);
            endd = frmenddymonth + "-" + (endMonth) + "-" + frmendyr;

            if (Utility.checkInternetConnection()) {
                if (Utility.isNotNull(fromd) || Utility.isNotNull(endd)) {
                    loadDataset(fromd, endd);
                } else {
                    Toast.makeText(getActivity(), "Please select appropriate from date and end date", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(
                    getActivity(),
                    "Please ensure the from date is before the after date",
                    Toast.LENGTH_LONG).show();
        }


    }


    public void SetActivityLogic(JSONArray summdata, JSONArray comperf) {

        if (summdata.length() > 0) {
            try {
                fromdate.setText(firtxt);
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                ArrayList<IBarDataSet> comdataSets = new ArrayList<IBarDataSet>();
                ArrayList<Integer> colors = new ArrayList<Integer>();
                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                ArrayList<String> labelsstr = new ArrayList<>();

                boolean chktxncnt = false;
                for (int c : ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.LIBERTY_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());
                JSONObject json_data = null;
                for (int i = 0; i < summdata.length(); i++) {

                    json_data = summdata.getJSONObject(i);
                    String txncode = json_data.optString("txnCode");

                    String amon = json_data.optString("amount");
                    String status = json_data.optString("status");
                    String agcmsn = json_data.optString("agentCmsn");
                    String txnname = Utility.convertTxnCodetoServ(txncode);
                    if ((!(amon.equals(""))) && (!(agcmsn.equals("")))) {
                        float dbagcmsn = Float.parseFloat(amon);
                        float agamcmn = Float.parseFloat(agcmsn);
                        if (Utility.isNotNull(status)) {
                            if (((status.equals("SUCCESS"))) && (dbagcmsn > 0)) {
                                //   finentry.add(new PieEntry(dbagcmsn, txnname));
                                ArrayList<BarEntry> barnt = new ArrayList<BarEntry>();
                                SecurityLayer.Log("Log Amount", Double.toString(Double.parseDouble(agcmsn)));
                                myperfdata.add(new GetMyPerfServData(txnname, "", Utility.roundto2dp(Double.toString(agamcmn)) + Constants.KEY_NAIRA));
                                barnt.add(new BarEntry(i, dbagcmsn, txnname));

                                BarDataSet set1;

                                set1 = new BarDataSet(barnt, txnname);

                                set1.setColor(colors.get(i));
                                //  }
                                dataSets.add(set1);
                                ArrayList<BarEntry> bcmnt = new ArrayList<BarEntry>();

                                bcmnt.add(new BarEntry(i, agamcmn, txnname));
                                labelsstr.add(txnname);

                                chktxncnt = true;
                                SecurityLayer.Log("Entries", dbagcmsn + " Code" + txnname);
                                BarDataSet set2;

                                set2 = new BarDataSet(bcmnt, txnname);

                                set2.setColor(colors.get(i));
                                //  }
                                comdataSets.add(set2);
                                entries.add(new PieEntry((float) agamcmn,
                                        txnname));

                            }
                        }
                    }
                }

                mypfadapt = new MyPerfServAdapt(myperfdata, getActivity());
                lvserv.setAdapter(mypfadapt);
                BarData databar = new BarData(dataSets);

                PieDataSet dataSet = new PieDataSet(entries, "");

                // dataSet.setDrawIcons(false);

                dataSet.setSliceSpace(3f);

                dataSet.setSelectionShift(5f);

                // add a lot of colors


                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);
                //dataSet.setSelectionShift(0f);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(7f);
                data.setValueTextColor(Color.BLACK);

                pieChart.setData(data);

                // undo all highlights
                pieChart.highlightValues(null);

                pieChart.invalidate();


                BarData comdata = new BarData(comdataSets);

                mChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsstr));
                mChart2.setData(databar);
                mChart2.invalidate();
                JSONObject json_cperfdata = null;
                int cntr = 0;
                for (int i = 0; i < comperf.length(); i++) {
                    json_cperfdata = comperf.getJSONObject(i);
                    //String accid = json_data.getString("benacid");


                    String txnCode = json_cperfdata.optString("txnCode");
                    double agentCmsn = json_cperfdata.optDouble("agentCmsn");
                    String txndateTime = json_cperfdata.optString("txndateTime");
                    String amount = json_cperfdata.optString("amount");
                    String status = json_cperfdata.optString("status");
                    String toAcNum = json_cperfdata.optString("toAcNum");
                    String refNumber = json_cperfdata.optString("refNumber");
                    txndateTime = Utility.convertPerfDate(txndateTime);
                    if (((status.equals("SUCCESS"))) && (agentCmsn > 0)) {
                        listentry.add(new Entry(cntr, Float.parseFloat(Double.toString(agentCmsn))));

                        labelslnchart.add(cntr, txndateTime);
                        cntr++;
                        SecurityLayer.Log("Counter", Integer.toString(cntr));
                    }
                }
                if (cntr > 0) {
                    //  setData(listentry);
                }
                SecurityLayer.Log("Transaction counter", Integer.toString(cntr));
                succtrans.setText(Integer.toString(cntr));
                if (chktxncnt == false) {
                    Toast.makeText(
                            getActivity(),
                            "There are no transaction records to display",
                            Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(
                    getActivity(),
                    "There are no records to display",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(getActivity())
                    .title(title)
                    .content(msg)

                    .negativeText("CONTINUE")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                            dialog.dismiss();
                            getActivity().finish();
                            session.logoutUser();

                            // After logout redirect user to Loing Activity
                            Intent i = new Intent(c, SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // Staring Login Activity
                            startActivity(i);

                        }
                    })
                    .show();
        }}
}
