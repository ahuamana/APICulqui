package com.android.culqi.culqi_android;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.culqi.culqi_android.CardModel.CardModel;
import com.android.culqi.culqi_android.CardModel.CardSender;
import com.android.culqi.culqi_android.Culqi.Card;
import com.android.culqi.culqi_android.Culqi.Token;
import com.android.culqi.culqi_android.Culqi.TokenCallback;
import com.android.culqi.culqi_android.Validation.Validation;
import com.android.culqi.culqi_android.interfaces.APIService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import org.json.JSONObject;
import org.w3c.dom.Element;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Validation validation;

    ProgressDialog progress;

    TextView txtcardnumber, txtcvv, txtmonth, txtyear, txtemail, kind_card, result;
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validation = new Validation();

        progress = new ProgressDialog(this);
        progress.setMessage("Validando informacion de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        txtcardnumber = (TextView) findViewById(R.id.txt_cardnumber);

        txtcvv = (TextView) findViewById(R.id.txt_cvv);

        txtmonth = (TextView) findViewById(R.id.txt_month);

        txtyear = (TextView) findViewById(R.id.txt_year);

        txtemail = (TextView) findViewById(R.id.txt_email);

        kind_card = (TextView) findViewById(R.id.kind_card);

        result = (TextView) findViewById(R.id.token_id);

        btnPay = (Button) findViewById(R.id.btn_pay);

        //txtcvv.setEnabled(false);

        txtcardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    txtcvv.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtcardnumber.getText().toString();
                if(s.length() == 0) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                if(validation.luhn(text)) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_sucess);
                } else {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                int cvv = validation.bin(text, kind_card);
                if(cvv > 0) {
                    txtcvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cvv)});
                    txtcvv.setEnabled(true);
                } else {
                    txtcvv.setEnabled(false);
                    txtcvv.setText("");
                }
            }
        });

        txtyear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtyear.getText().toString();
                if(validation.year(text)){
                    txtyear.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtyear.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        txtmonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtmonth.getText().toString();
                if(validation.month(text)){
                    txtmonth.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtmonth.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                retrofitToken();

            }
        });

    }

    public void btnPagar()
    {
        progress.show();


        Card card = new Card("411111111111111", "123", 9, 2022, "asd@gmail.com");

        Token token = new Token("pk_test_c06043ec9b052aa6");

        token.createToken(getApplicationContext(), card, new TokenCallback() {
            @Override
            public void onSuccess(JSONObject token) {
                try {
                    result.setText(token.get("id").toString());

                    Log.e("RESULT",""+token.get("id").toString());
                } catch (Exception ex){
                    progress.hide();
                    Log.e("ERROR",""+ex.getMessage());
                }
                progress.hide();
            }

            @Override
            public void onError(Exception error) {

                Log.e("ERROR",""+error.getMessage());

                progress.hide();
            }
        });
    }


    public void retrofitToken()
    {
        CardSender card = new CardSender();

        Map token = new HashMap();
        token.put("card_number", "4111111111111111");
        token.put("cvv", "123");
        token.put("email", "richard@piedpiper.com");
        token.put("expiration_month", 9);
        token.put("expiration_year", 2020);

        card.setCard_number("4111111111111111");
        card.setCvv("123");
        card.setEmail("richard@piedpiper.com");
        card.setExpiration_month(9);
        card.setExpiration_year(2022);

        JSONObject json = new JSONObject(token);
        String data =json.toString();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://secure.culqi.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);

        Call<JsonObject> call = service.getToken(card);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("DATA", "DATA: "+ response.body());
                Log.e("DATA", "DATA: "+ response.code());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {

                Log.e("ERROR", "ERROR: "+ throwable.getMessage());
            }
        });

                //Log.e("DATA", "DATA: "+ response.body());
                //Log.e("ERROR", "ERROR: "+ throwable.getMessage());
    }


}
