package view.infoseg.morettic.com.br.infosegapp.view;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncRate;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LIST_BITMAPS_OCORRENCIAS;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;

public class OcorrenciaListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private String titulo,desc,idOcorrencia;


    public OcorrenciaListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.content_activity_ocorrencia_list_model, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.content_activity_ocorrencia_list_model, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitListView);
        TextView txtDescListView = (TextView) rowView.findViewById(R.id.txtDescListView);
        TextView txtAuthorListView = (TextView) rowView.findViewById(R.id.txtAuthorListView);
        TextView txtDateListView = (TextView) rowView.findViewById(R.id.txtDateListView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.mcIconType);
        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.mcIcon);
        Button share = (Button)rowView.findViewById(R.id.btShareList);
        RatingBar rating = (RatingBar)rowView.findViewById(R.id.ratingBar);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        try {
            JSONObject ocorrencia = new JSONObject(itemname[position]);
            titulo = ocorrencia.getString("tit");
            desc = ocorrencia.getString("desc");
            idOcorrencia = ocorrencia.getString("id");
            rating.setRating((float)ocorrencia.getDouble("rating"));
            txtTitle.setText(ocorrencia.getString("tit"));
            txtDescListView.setText(ocorrencia.getString("desc"));
            txtAuthorListView.setText(ocorrencia.getString("author"));
            txtDateListView.setText(ocorrencia.getString("date"));
            if (ocorrencia.getString("tipo").equals("POLITICA")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_politics01));
            } else if (ocorrencia.getString("tipo").equals("SAUDE")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_health01));
            } else if (ocorrencia.getString("tipo").equals("EDUCACAO")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_education01));
            } else if (ocorrencia.getString("tipo").equals("MEIO_AMBIENTE")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_nature01));
            } else if (ocorrencia.getString("tipo").equals("TRANSPORTE")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_transport01));
            } else if (ocorrencia.getString("tipo").equals("SEGURANCA")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_security01));
            } else if (ocorrencia.getString("tipo").equals("UPA")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_upa01));
            }else if (ocorrencia.getString("tipo").equals("ESPORTE")) {
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_sport01));
            }
            if (LIST_BITMAPS_OCORRENCIAS[position] != null) {
                imageView1.setImageBitmap(LIST_BITMAPS_OCORRENCIAS[position]);
            }

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, MAIN.getString(R.string.t_tulo_da_ocorr_ncia)+titulo+"\n\n"+MAIN.getString(R.string.descri_o)+desc+"\n\n"+MAIN.getString(R.string.share_msg));
                    sendIntent.setType("text/plain");
                    MAIN.startActivity(sendIntent);
                }
            });
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    AssyncRate assyncRate = new AssyncRate(ratingBar.getRating()+"",idOcorrencia);
                    assyncRate.execute();
                    ratingBar.setEnabled(false);
                }
            });

            /**
             *
             *
             *
             * */

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //imageView.setImageResource(imgid[position]);
        //extratxt.setText("Description "+itemname[position]);
        return rowView;

    }

    ;
}