package view.infoseg.morettic.com.br.infosegapp.view;

import android.app.Activity;
import android.content.Intent;
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
import view.infoseg.morettic.com.br.infosegapp.util.ImageCache;
import view.infoseg.morettic.com.br.infosegapp.util.TipoOcorrencia;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

public class OcorrenciaListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private String idOcorrencia, title,desc;


    public OcorrenciaListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.content_activity_ocorrencia_list_model, itemname);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemname = itemname;
    }//

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.content_activity_ocorrencia_list_model, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitListView);
        TextView txtDescListView = (TextView) rowView.findViewById(R.id.txtDescListView);
        TextView txtAuthorListView = (TextView) rowView.findViewById(R.id.txtAuthorListView);
        TextView txtDateListView = (TextView) rowView.findViewById(R.id.txtDateListView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.mcIconType);
        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.mcIcon);
        Button share = (Button) rowView.findViewById(R.id.btShareList);
        RatingBar rating = (RatingBar) rowView.findViewById(R.id.ratingBar);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        try {
            JSONObject ocorrencia = new JSONObject(itemname[position]);

            if(ocorrencia.getString("mType").equals("experience")) {
                title = ocorrencia.getString("tit");
                desc = ocorrencia.getString("desc");
                idOcorrencia = ocorrencia.getString("id");
                rating.setRating((float) ocorrencia.getDouble("rating"));
                txtTitle.setText(ocorrencia.getString("tit"));
                txtDescListView.setText(ocorrencia.getString("desc"));
                txtAuthorListView.setText(ocorrencia.getString("author"));
                txtDateListView.setText(ocorrencia.getString("date"));
                TipoOcorrencia tp = TipoOcorrencia.valueOf(ocorrencia.getString("tipo"));
                int icon = tp.getIcon();

                imageView.setImageDrawable(getContext().getResources().getDrawable(icon));

                imageView1.setImageBitmap(ImageCache.getBitmapFromMemCache(ocorrencia.getString("token")));
            }else  if(ocorrencia.getString("mType").equals("twitter")) {
                title = ocorrencia.getString("text");
                desc = ocorrencia.getString("user_desc");
                idOcorrencia = ocorrencia.getString("id");
                //rating.setRating((float) ocorrencia.getDouble("rating"));
                txtTitle.setText(ocorrencia.getString("text"));
                txtDescListView.setText(ocorrencia.getString("user_desc"));
                txtAuthorListView.setText(ocorrencia.getString("twitter_user"));
                txtDateListView.setText(ocorrencia.getString("created_at"));
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.logo));
                imageView1.setImageBitmap(ImageCache.getBitmapFromMemCache(ocorrencia.getString("avatar_url")));
                rating.setEnabled(false);
                rating.setVisibility(View.GONE);

            }else  if(ocorrencia.getString("mType").equals("webhose")) {
                title = ocorrencia.getString("title");
                desc = ocorrencia.getString("text");
                idOcorrencia = ocorrencia.getString("id");
                //rating.setRating((float) ocorrencia.getDouble("rating"));
                txtTitle.setText(ocorrencia.getString("title"));
                txtDescListView.setText(ocorrencia.getString("text"));
                txtAuthorListView.setText(ocorrencia.getString("author"));
                txtDateListView.setText(ocorrencia.getString("date"));
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.logo));
                imageView1.setImageBitmap(ImageCache.getBitmapFromMemCache(ocorrencia.getString("token")));
                rating.setEnabled(false);
                rating.setVisibility(View.GONE);

            }
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + desc + "\n\n" + MAIN.getString(R.string.share_msg));
                    sendIntent.setType("text/plain");
                    MAIN.startActivity(sendIntent);
                }
            });
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    AssyncRate assyncRate = new AssyncRate(ratingBar.getRating() + "", idOcorrencia);
                    assyncRate.execute();
                    ratingBar.setEnabled(false);
                }
            });

            /**
             *
             *
             *
             * */

        } catch (JSONException ex) {
            logException(ex);
        } catch (NullPointerException ex) {
            logException(ex);
        } catch (Exception ex) {
            logException(ex);
        }

        //imageView.setImageResource(imgid[position]);
        //extratxt.setText("Description "+itemname[position]);
        return rowView;

    }

    ;
}