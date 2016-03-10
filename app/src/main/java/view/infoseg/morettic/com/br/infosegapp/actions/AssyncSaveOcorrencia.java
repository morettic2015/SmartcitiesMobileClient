package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.ActivityOcorrencia;
import static view.infoseg.morettic.com.br.infosegapp.view.ActivityOcorrencia.*;

/**
 * Created by LuisAugusto on 24/02/2016.
 * <p/>
 * <p/>
 * {"results":[
 * {"address_components":[
 * {"long_name":"386","short_name":"386","types":["street_number"]},
 * {"long_name":"Rua General Bittencourt","short_name":"R. Gen. Bittencourt","types":["route"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]},{"long_name":"88020-100","short_name":"88020-100","types":["postal_code"]}],"formatted_address":"R. Gen. Bittencourt, 386 - Centro, Florianópolis - SC, 88020-100, Brazil","geometry":{"location":{"lat":-27.5969049,"lng":-48.5453109},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":-27.5955559197085,"lng":-48.5439619197085},"southwest":{"lat":-27.5982538802915,"lng":-48.5466598802915}}},"place_id":"ChIJva_BRTo4J5URwNwTlmQ2gZ8","types":["street_address"]},{"address_components":[{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5445594,"lng":-48.5079104},"southwest":{"lat":-27.6590226,"lng":-48.6134675}},"location":{"lat":-27.5922687,"lng":-48.5490266},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5445594,"lng":-48.5079104},"southwest":{"lat":-27.6590226,"lng":-48.6134675}}},"place_id":"ChIJW1r8jiQ4J5UR2iiCtZmcvSU","types":["sublocality_level_1","sublocality","political"]},{"address_components":[{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"State of Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Florianópolis, Florianópolis - State of Santa Catarina, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.3816342,"lng":-48.359155},"southwest":{"lat":-27.8390502,"lng":-48.6100064}},"location":{"lat":-27.5953778,"lng":-48.5480499},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.3816342,"lng":-48.359155},"southwest":{"lat":-27.8390502,"lng":-48.6100064}}},"place_id":"ChIJ1zLGsk45J5URRscEagtVvIE","types":["locality","political"]},{"address_components":[{"long_name":"88020-110","short_name":"88020-110","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88020-110, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5944433,"lng":-48.5437357},"southwest":{"lat":-27.5977637,"lng":-48.5462376}},"location":{"lat":-27.5964059,"lng":-48.5452155},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5944433,"lng":-48.5436376697085},"southwest":{"lat":-27.5977637,"lng":-48.5463356302915}}},"place_id":"ChIJsVZkIzo4J5URKpZPRJnu6W0","types":["postal_code"]},{"address_components":[{"long_name":"88020-150","short_name":"88020-150","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88020-150, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5956499,"lng":-48.5437357},"southwest":{"lat":-27.5989705,"lng":-48.5462376}},"location":{"lat":-27.5980392,"lng":-48.54580139999999},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5956499,"lng":-48.5436376697085},"southwest":{"lat":-27.5989705,"lng":-48.5463356302915}}},"place_id":"ChIJYQ7dFDo4J5URa7OmWM6e_G4","types":["postal_code"]},{"address_components":[{"long_name":"88020-100","short_name":"88020-100","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88020-100, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5939896,"lng":-48.542485},"southwest":{"lat":-27.5998778,"lng":-48.5487397}},"location":{"lat":-27.5974292,"lng":-48.5464651},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5939896,"lng":-48.542485},"southwest":{"lat":-27.5998778,"lng":-48.5487397}}},"place_id":"ChIJF9CMfjk4J5URPxuvQC_l7KQ","types":["postal_code"]},{"address_components":[{"long_name":"88020-120","short_name":"88020-120","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88020-120, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5949055,"lng":-48.5437357},"southwest":{"lat":-27.6001771,"lng":-48.5562466}},"location":{"lat":-27.5974292,"lng":-48.5464651},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.595804,"lng":-48.5437357},"southwest":{"lat":-27.6001771,"lng":-48.5487397}}},"place_id":"ChIJy8HgnzA4J5URTUga65PSf3g","types":["postal_code"]},{"address_components":[{"long_name":"88070-150","short_name":"88070-150","types":["postal_code"]},{"long_name":"Estreito","short_name":"Estreito","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Estreito, Florianópolis - SC, 88070-150, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5947726,"lng":-48.5449867},"southwest":{"lat":-27.5991476,"lng":-48.5825281}},"location":{"lat":-27.5978978,"lng":-48.5802011},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5947726,"lng":-48.5775211},"southwest":{"lat":-27.5991476,"lng":-48.5825281}}},"place_id":"ChIJs6cAzMM3J5URKlakZRB0c8Y","types":["postal_code"]},{"address_components":[{"long_name":"88010-100","short_name":"88010-100","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88010-100, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5926457,"lng":-48.5449867},"southwest":{"lat":-27.5988253,"lng":-48.5600002}},"location":{"lat":-27.5980392,"lng":-48.5509055},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5941524,"lng":-48.5499909},"southwest":{"lat":-27.5988253,"lng":-48.5574976}}},"place_id":"ChIJy42sVfU3J5URpJliH6U1mRs","types":["postal_code"]},{"address_components":[{"long_name":"88015-204","short_name":"88015-204","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88015-204, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5875113,"lng":-48.4999708},"southwest":{"lat":-27.6875122,"lng":-48.5574976}},"location":{"lat":-27.5917839,"lng":-48.5519131},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5902243,"lng":-48.5449867},"southwest":{"lat":-27.5936989,"lng":-48.5574976}}},"place_id":"ChIJn3goqxg4J5URKJ7DnjlLSjc","types":["postal_code"]},{"address_components":[{"long_name":"88020-001","short_name":"88020-001","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88020-001, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5915765,"lng":-48.542485},"southwest":{"lat":-27.6031984,"lng":-48.5700114}},"location":{"lat":-27.5984794,"lng":-48.5477406},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5915765,"lng":-48.542485},"southwest":{"lat":-27.6031984,"lng":-48.55124199999999}}},"place_id":"ChIJd2TYuDs4J5URKgzcy7-RA7M","types":["postal_code"]},{"address_components":[{"long_name":"88020","short_name":"88020","types":["postal_code_prefix","postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.588322,"lng":-48.5355679},"southwest":{"lat":-27.6100825,"lng":-48.5496176}},"location":{"lat":-27.5947426,"lng":-48.541234},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.588322,"lng":-48.5355679},"southwest":{"lat":-27.6100825,"lng":-48.5496176}}},"place_id":"ChIJWwLFPTc4J5URZkhDuRsXrhk","types":["postal_code_prefix","postal_code"]},{"address_components":[{"long_name":"88010-400","short_name":"88010-400","types":["postal_code"]},{"long_name":"Centro","short_name":"Centro","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Centro, Florianópolis - SC, 88010-400, Brazil","geometry":{"bounds":{"northeast":{"lat":-22.8941346,"lng":-47.05708569999999},"southwest":{"lat":-27.6191835,"lng":-50.6249188}},"location":{"lat":-27.5987076,"lng":-48.5499051},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5636623,"lng":-48.51872280000001},"southwest":{"lat":-27.6191835,"lng":-48.5762695}}},"place_id":"ChIJvV1MGCU4J5URa12ZGvPt2b0","types":["postal_code"]},{"address_components":[{"long_name":"88036-020","short_name":"88036-020","types":["postal_code"]},{"long_name":"Trindade","short_name":"Trindade","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["locality","political"]},{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Trindade, Florianópolis - SC, 88036-020, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.5262246,"lng":-48.4787258},"southwest":{"lat":-27.6252041,"lng":-48.5687599}},"location":{"lat":-27.5806807,"lng":-48.52550489999999},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.5262246,"lng":-48.4787258},"southwest":{"lat":-27.6252041,"lng":-48.5687599}}},"place_id":"ChIJ89B2emc4J5URQFb66o5dbDY","types":["postal_code"]},{"address_components":[{"long_name":"Florianópolis","short_name":"Florianópolis","types":["administrative_area_level_2","political"]},{"long_name":"State of Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Florianópolis - State of Santa Catarina, Brazil","geometry":{"bounds":{"northeast":{"lat":-27.382992,"lng":-48.3590847},"southwest":{"lat":-27.8454795,"lng":-48.6065648}},"location":{"lat":-27.5948698,"lng":-48.54821949999999},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-27.382992,"lng":-48.3590847},"southwest":{"lat":-27.8454795,"lng":-48.6065648}}},"place_id":"ChIJn7h-4b9JJ5URGCq6n0zj1tM","types":["administrative_area_level_2","political"]},{"address_components":[{"long_name":"State of Santa Catarina","short_name":"SC","types":["administrative_area_level_1","political"]},{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"State of Santa Catarina, Brazil","geometry":{"bounds":{"northeast":{"lat":-25.9559588,"lng":-48.35680809999999},"southwest":{"lat":-29.351441,"lng":-53.83635870000001}},"location":{"lat":-27.2423392,"lng":-50.2188556},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":-25.9559588,"lng":-48.35680809999999},"southwest":{"lat":-29.351441,"lng":-53.83635870000001}}},"place_id":"ChIJ-f9SwCVN2ZQRK6t_7YB1Jys","types":["administrative_area_level_1","political"]},{"address_components":[{"long_name":"Brazil","short_name":"BR","types":["country","political"]}],"formatted_address":"Brazil","geometry":{"bounds":{"northeast":{"lat":5.2717863,"lng":-29.3448224},"southwest":{"lat":-33.7509909,"lng":-73.982817}},"location":{"lat":-14.235004,"lng":-51.92528},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":5.2717863,"lng":-32.3781864},"southwest":{"lat":-33.7509909,"lng":-73.982817}}},"place_id":"ChIJzyjM68dZnAARYz4p8gYVWik","types":["country","political"]}],"status":"OK"}
 */
public class AssyncSaveOcorrencia extends AsyncTask<JSONObject, Void, String> {
    // public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private JSONObject ocorrencia;
    private Geocoder geocoder;
    private EditText edTit, editDesc;
    private RadioButton rdSelect;



    @Override
    protected void onPreExecute() {
        dialog.setMessage("Salvando ocorrencia...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


        edTit.setText("");
        editDesc.setText("");
        rdSelect.setChecked(false);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    public AssyncSaveOcorrencia(View activity, JSONObject ocorrencia, Geocoder geocoder1,EditText tit, EditText desc,RadioButton sel) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.ocorrencia = ocorrencia;
        this.geocoder = geocoder1;
        this.editDesc = desc;
        this.edTit = tit;
        this.rdSelect = sel;
        //builder = new AlertDialog.Builder(this.a1.getContext());
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {

            //Pega os campos
            String tit = this.ocorrencia.getString("tit");
            String ocorrenciaPic = ValueObject.UPLOAD_PIC_OCORRENCIA;
            String desc = this.ocorrencia.getString("desc");
            double lat = this.ocorrencia.getDouble("lat");
            double lon = this.ocorrencia.getDouble("lon");
            String tp = this.ocorrencia.getString("tipoOcorrencia");
            String iProfile = "" + ValueObject.ID_PROFILE;
            String idOcorrencia = null;

            //tenta localizar o endereço da ocorrencia pelo lat lon
            final List<Address> fromLocation = geocoder.getFromLocation(lat, lon, 1);
            StringBuilder sb = new StringBuilder();
            for (Address a1 : fromLocation) {
                sb.append(a1.getThoroughfare());
                sb.append(",");
                sb.append(a1.getSubLocality());
                sb.append(",");
                sb.append(a1.getFeatureName());
                sb.append(",");
                sb.append(a1.getLocality());
                sb.append(",");
                sb.append(a1.getPostalCode());
                sb.append(",");
                sb.append(a1.getAdminArea());
                sb.append(",");
                sb.append(a1.getCountryName());
            }

            //URL PARA SALVAR O ocorrencia.
            String url = HttpUtil.getSaveOcorrenciaPath(tit, lat, lon, desc, ocorrenciaPic, tp, iProfile,sb.toString());
            //url =
            js = HttpUtil.getJSONFromUrl(url);

            ValueObject.ID_OCORRENCIA = js.getString("key");


        } catch (Exception e) {
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {
            return js.toString();
        }
    }


}