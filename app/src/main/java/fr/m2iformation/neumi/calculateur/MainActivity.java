package fr.m2iformation.neumi.calculateur;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    TextView tvResult;
    double tauxEuros = 1;
    double tauxDollars;
    double tauxLivres;
    double tauxBitcoins;
    int selectionMonnaie = 0;
    int selectionConv = 0;
    Evaluator evaluator = new Evaluator();
    RadioButton rd;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);
        tvResult.setText("0");
        rd = findViewById(R.id.rd);
        rd.setOnLongClickListener(this);
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        editor = pref.edit();
        tauxDollars = pref.getFloat("tauxDollars", (float) 1.185);
        tauxLivres = pref.getFloat("tauxLivres", (float) 0.88);
        tauxBitcoins = pref.getFloat("tauxBitcoins", (float) 0.0001);

    }

    public void clear(View view) {
        tvResult.setText("0");
    }

    public void touche(View view) {
        Button bt = (Button) view;
        String str = tvResult.getText().toString();
        if (str.equals("0")) str = "";
        tvResult.setText(str + bt.getText());
    }

    public void convEuros(View view) {
        convDevise(0);
    }

    public void convDollars(View view) {
        convDevise(1);
    }

    public void convLivres(View view) {
        convDevise(2);
    }

    public void convBitcoins(View view) {
        convDevise(3);
    }

    private void convDevise(int nouvelMonnaie) {
        double tauxActuel = selectionnerTaux(selectionMonnaie);
        double tauxNouveau = selectionnerTaux(nouvelMonnaie);
        double nombreAfficher = Double.parseDouble(tvResult.getText().toString());
        double nombreNouveauAfficher = nombreAfficher * tauxNouveau / tauxActuel;
        String str = String.valueOf(nombreNouveauAfficher);
        str = str.replace(",", ".");
        tvResult.setText(str);
        selectionMonnaie = nouvelMonnaie;
    }

    private double selectionnerTaux(int monnaie) {
        double tauxSelection = 0;
        switch (monnaie) {
            case 0:
                tauxSelection = tauxEuros;
                break;
            case 1:
                tauxSelection = tauxDollars;
                break;
            case 2:
                tauxSelection = tauxLivres;
                break;
            case 3:
                tauxSelection = tauxBitcoins;
                break;
        }
        return tauxSelection;
    }

    public void convDeg(View view) {
        double mesureActuel = Double.parseDouble(tvResult.getText().toString());
        if (selectionConv == 1) {
            double result = mesureActuel / (3.14159 / 180);
            tvResult.setText(String.valueOf(result));
        } else if (selectionConv == 2) {
            double result = mesureActuel / (100.0 / 90.0);
            tvResult.setText(String.valueOf(result));
        }
        selectionConv = 0;
    }

    public void convRad(View view) {
        double mesureActuel = Double.parseDouble(tvResult.getText().toString());
        if (selectionConv == 0) {
            double result = mesureActuel * (3.14159 / 180);
            tvResult.setText(String.valueOf(result));
        } else if (selectionConv == 2) {
            double result = mesureActuel / (100.0 / 90.0) * (3.14159 / 180);
            tvResult.setText(String.valueOf(result));
        }
        selectionConv = 1;
    }

    public void convGrd(View view) {
        double mesureActuel = Double.parseDouble(tvResult.getText().toString());
        if (selectionConv == 0) {
            double result = mesureActuel * (100.0 / 90.0);
            tvResult.setText(String.valueOf(result));
        } else if (selectionConv == 1) {
            double result = mesureActuel / (3.14159 / 180) * (100.0 / 90.0);
            tvResult.setText(String.valueOf(result));
        }
        selectionConv = 2;
    }

    public void result(View view) {
        String calcul = tvResult.getText().toString();
        try {
            double resultat = evaluator.getNumberResult(calcul);
            tvResult.setText(String.valueOf(resultat));
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onLongClick(View v) {
        RadioButton rd = (RadioButton) v;
        switch (rd.getText().toString()) {
            case "$":
                tauxDollars = Double.parseDouble(tvResult.getText().toString());
                editor.putFloat("tauxDollars", (float) tauxDollars);
                break;
            case "£":
                tauxLivres = Double.parseDouble(tvResult.getText().toString());
                editor.putFloat("tauxLivres", (float) tauxLivres);
                break;
            case "B":
                tauxBitcoins = Double.parseDouble(tvResult.getText().toString());
                editor.putFloat("tauxBitcoins", (float) tauxBitcoins);
                break;
        }
        editor.apply();
        return true;
    }
}
