package view.infoseg.morettic.com.br.infosegapp.view;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.TipoOcorrencia;
import view.infoseg.morettic.com.br.infosegapp.util.ToastHelper;

import static view.infoseg.morettic.com.br.infosegapp.R.style.MyDialog;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.KEYWORD;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;

/**
 * Created by LuisAugusto on 16/03/2016.
 */
public class MapFilter extends DialogFragment {
    /*private EditText email,senha0,senha1;
    private Button btRegisterSocial;
    private TextView txt;*/
    private CheckBox[] chkBox = new CheckBox[14];
    private View v;
    private SeekBar seekBarDistance;
    private EditText txtBuscar;
    private Button btBuscar;
    private TextView textViewSeekBar;
    private SharedPreferences.Editor editor;
    private int distance = 0;
    private static MapFilter mapFilter;

    static MapFilter getInstance() {
        return mapFilter==null? mapFilter = new MapFilter():mapFilter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, MyDialog);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.dialog_map_filter, container, false);

        chkBox[0] = (CheckBox) this.v.findViewById(R.id.rSegurança);//
        chkBox[1] = (CheckBox) this.v.findViewById(R.id.rSaude);//
        chkBox[2] = (CheckBox) this.v.findViewById(R.id.rCultura);//
        chkBox[3] = (CheckBox) this.v.findViewById(R.id.rTurismo);
        chkBox[4] = (CheckBox) this.v.findViewById(R.id.rEducacao);//
        chkBox[5] = (CheckBox) this.v.findViewById(R.id.rTransporte);//
        chkBox[6] = (CheckBox) this.v.findViewById(R.id.rMeioAmbiente);//
        chkBox[7] = (CheckBox) this.v.findViewById(R.id.rInfraestrutura);
        chkBox[8] = (CheckBox) this.v.findViewById(R.id.rPolitica);//
        chkBox[9] = (CheckBox) this.v.findViewById(R.id.rEsporte);//
        chkBox[10] = (CheckBox) this.v.findViewById(R.id.rImoveis);//
        chkBox[11] = (CheckBox) this.v.findViewById(R.id.rShop);//
        chkBox[12] = (CheckBox) this.v.findViewById(R.id.rAlimentacao);//
        chkBox[13] = (CheckBox) this.v.findViewById(R.id.rBeer);//

        txtBuscar = (EditText) this.v.findViewById(R.id.txtBusca);
        textViewSeekBar = (TextView) this.v.findViewById(R.id.txtDistance);
        editor = MY_PREFERENCES.edit();
        btBuscar = (Button) this.v.findViewById(R.id.btBuscarOcorrencias);
        btBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InfosegMain i = (InfosegMain) MAIN;
                i.loadFragment(new ActivityMap(),i.getString(R.string.mapa_ocorrencias));
                ToastHelper.makeToast(v.getContext(),i.getString(R.string.loading));
                editor.putInt("distance", distance).commit();
                editor.putBoolean(TipoOcorrencia.SEGURANCA.toString(), chkBox[0].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.SAUDE.toString(), chkBox[1].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.CULTURA.toString(), chkBox[2].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.TURISMO.toString(), chkBox[3].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.EDUCACAO.toString(), chkBox[4].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.TRANSPORTE.toString(), chkBox[5].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.MEIO_AMBIENTE.toString(), chkBox[6].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.INFRAESTRUTURA.toString(), chkBox[7].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.POLITICA.toString(), chkBox[8].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.ESPORTE.toString(), chkBox[9].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.IMOVEIS.toString(), chkBox[10].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.SHOP.toString(), chkBox[11].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.ALIMENTACAO.toString(), chkBox[12].isChecked()).commit();
                editor.putBoolean(TipoOcorrencia.BEER.toString(), chkBox[13].isChecked()).commit();
                editor.putBoolean("mine", false).commit();
                KEYWORD = txtBuscar.getText().toString();
                MY_PREFERENCES = v.getContext().getSharedPreferences("INFOSEGMAIN", 0);



                getInstance().dismiss();


            }
        });
        seekBarDistance = (SeekBar) v.findViewById(R.id.seekBarDistance);
        seekBarDistance.setMax(50);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                distance = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewSeekBar.setText(seekBar.getContext().getText(R.string.selecione_as_dist_ncias_para_filtrar) + "(" + distance + "KM )");
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

        return this.v;
    }
}
