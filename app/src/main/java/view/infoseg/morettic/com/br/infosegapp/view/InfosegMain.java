package view.infoseg.morettic.com.br.infosegapp.view;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.crash.FirebaseCrash;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncLoadListOcorrencias;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncLoginRegister;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink;
import view.infoseg.morettic.com.br.infosegapp.util.ImageCache;
import view.infoseg.morettic.com.br.infosegapp.util.LocationManagerUtil;
import view.infoseg.morettic.com.br.infosegapp.util.TwitterUtil;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.AUTENTICADO;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.BITMAP_DEFAULT;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.COUNTER_CLICK;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LIST_OCORRENCIAS;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LOGGER;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LOGIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.WORD;

//import io.fabric.sdk.android.Fabric;


public class InfosegMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /**
     * @Tappax object to show ADS as possible way to
     */

    private final String TAPPX_KEY = "/120940746/Pub-9972-Android-9558";
    private com.google.android.gms.ads.doubleclick.PublisherInterstitialAd adInterstitial = null;

    //private static final int TAKE_PICTURE = 1;
    private static final int SELECT_PHOTO = 100;
    private static final int TWITTER_CODE = 140;
    private static final int REQUEST_CODE_MIC = 1234;
    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    /* The login button for Facebook */
    ///  private LoginButton mFacebookLoginButton;
    /* The callback manager for Facebook */
    //  private CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */
    //  private AccessTokenTracker mFacebookAccessTokenTracker;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    //private Uri imageUri;
    private static int MY_REQUEST_CODE, MY_REQUEST_CODE1, MY_REQUEST_CODE2, MY_REQUEST_CODE3, MY_REQUEST_CODE4;
    //private Firebase mFirebaseRef;

    static void setTitleToolbar(String title, View v) {
        Toolbar tb = (Toolbar) v.findViewById(R.id.toolbar);
        tb.setTitle(title);
    }


    @Override
    protected void onStart() {
        super.onStart();
        /**
         * Abre a janela para ativar o GPS do celular
         * */
        turnGPSOn();
        /**
         * SE nao estiver autenticado pop up maldito dos infernos
         * */
        if (!AUTENTICADO && MY_PREFERENCES.getString("id", "").equals("")) {
            if (LOGIN == null) {
                LOGIN = LoginFragment.newInstance();
                LOGIN.show(getFragmentManager(), "dialog");
            }
        } else {
            //Load Data
            BITMAP_DEFAULT = BitmapFactory.decodeResource(getResources(), R.drawable.ic_smartcities_icon_logo);
            AssyncLoginRegister assyncLoginRegister = new AssyncLoginRegister(this.getApplicationContext(), MY_PREFERENCES.getString("email", ""), MY_PREFERENCES.getString("passwd", ""));
            assyncLoginRegister.execute();
            //Load Ocorrencias
            //Load / register device
            AssyncLoadListOcorrencias assyncLoadListOcorrencias = new AssyncLoadListOcorrencias();
            assyncLoadListOcorrencias.setCtx(getApplicationContext());
            assyncLoadListOcorrencias.execute();
             /*
                *   @Here we show ADS!
                *   @If Counter click <5 show ads. 5 ads each navigation;
                *   @Only if the user has looged in.....
                * **/
            if (COUNTER_CLICK < 100) {
                //Promote TAPPAX NETWORK!
                Date d = new Date();
                boolean isTappaxNetwork = false;
                if ((d.getMinutes() % 4)== 0) {//If minutes mod 3 == 0 show ads
                    isTappaxNetwork = true;
                    COUNTER_CLICK++;
                    adInterstitial = com.tappx.TAPPXAdInterstitial.Configure(this, TAPPX_KEY,
                            new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    com.tappx.TAPPXAdInterstitial.Show(adInterstitial);
                                }
                            });
                }
                //If show Tappax dont show google play....
                if ((d.getMinutes() % 7) == 0 && !isTappaxNetwork) {//If minutes mod 2 == 0 show ads
                    loadFragment(new ActivityAds(), getString(R.string.help_us));
                    COUNTER_CLICK++;
                }

                d = null;
            }
        }


        MAIN = this;
        //Inicializa o cliente twitter
        TwitterUtil.initTwitterConfig(this);


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        COUNTER_CLICK = 0;
        setContentView(R.layout.activity_infoseg_main);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle(getString(R.string.app_title_main));
        //Inicializa as preferencias do usuario
        MY_PREFERENCES = getApplicationContext().getSharedPreferences("INFOSEGMAIN", 0);

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener((View.OnClickListener) this);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Inicializa os botooes da tela Splash
        ImageView b1 = (ImageView) findViewById(R.id.btNovoSplah);
        b1.setOnClickListener((View.OnClickListener) this);

        ImageView b2 = (ImageView) findViewById(R.id.btPerfilSlash);
        b2.setOnClickListener((View.OnClickListener) this);

        ImageView b3 = (ImageView) findViewById(R.id.btConfigSplash);
        b3.setOnClickListener((View.OnClickListener) this);

        ImageView b4 = (ImageView) findViewById(R.id.btMapaSplash);
        b4.setOnClickListener((View.OnClickListener) this);

        ImageView b5 = (ImageView) findViewById(R.id.btListOcorrencias);
        b5.setOnClickListener((View.OnClickListener) this);

        /**
         *
         *  @PQP O GOOGLE TEM QUE CAGAR NE AGORA PRECISO VERIFICAR CADA PERMISSAO NA MAO.... A  VAI SE FUDE SENAO A PORRA DA NULL POINTE E O CARALHO
         *  @ BRIAN e LARRY VCS CHUPAM MUITA ROLA PQP entendo o contexto da mudanca mas nao seria algo que a ENgine Android fosse responsavel em habilitar em runtime/;
         *
         *  @ Porra pra que dificultar caralho bugado esse android M ou 6 bando de chupa rola do caralho
         *
         *  E SE VOCE ESTIVER LENDO ISSO PORRA E PORQUE E UM VIADO FILHO DA PUTA QUE NAO MERECER SER UM DEUS VIVO
         *
         *   HUAUHAUAUHAU NO ARCO IRIS DA MANHA UM SILENCIO INVADE O PANTANO E FAZ OS CARNEIROS FLUTUAREM NAS MARGES DO NILO
         * */
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] p = {ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, p, MY_REQUEST_CODE);
        }

        //Checa permissão da camera
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, CAMERA)) {
            String[] p = {CAMERA};
            ActivityCompat.requestPermissions(this, p, MY_REQUEST_CODE);
        }
        //android.permission.WRITE_EXTERNAL_STORAGE
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)) {
            String[] p = {WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, p, MY_REQUEST_CODE3);
        }
        //Checa a permissao da puta que te pariu
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, GET_ACCOUNTS)) {
            String[] p = {GET_ACCOUNTS};
            ActivityCompat.requestPermissions(this, p, MY_REQUEST_CODE4);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.infoseg_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String title = null;
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            fragment = new ActivityOcorrencia();
            title = getString(R.string.register_event);
        } else if (id == R.id.nav_gallery) {
            if (MY_PREFERENCES.contains("ehMeu")) {
                fragment = new ActivityMap();
                title = getString(R.string.configura_es);
            } else {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.preferences));
                alertDialogBuilder
                        .setMessage(getString(R.string.view_config_map))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                fragment = new ActivityConfig();
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


            title = getString(R.string.mapa_ocorrencias);
        } else if (id == R.id.nav_list) {
            if (isDataListLoaded()) {
                fragment = new ListOcorrencia();
                title = getString(R.string.list_ocorrencias);
            } else {
                fragment = new ActivityAds();
                title = getString(R.string.help_us);
            }
        } else if (id == R.id.nav_slideshow) {
            fragment = new ActivityProfile();
            title = getString(R.string.perfil);
        } else if (id == R.id.nav_manage) {
            fragment = new ActivityConfig();
            title = getString(R.string.configura_es);
        } else if (id == R.id.nav_adds) {
            fragment = new ActivityAds();
            title = getString(R.string.help_us);
        } else if (id == R.id.nav_exit) {
            //Fecha o APP
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.sair));
            alertDialogBuilder
                    .setMessage(getString(R.string.sair_remover_sessao))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.SIM),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Limpa as preferencias
                                    //getSharedPreferences("INFOSEGMAIN", 0).edit().clear().commit();
                                    //Move para background e destroi o app
                                    moveTaskToBack(true);
                                    Process.killProcess(Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton(getString(R.string.NAO), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_msg));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        loadFragment(fragment, title);
        title = null;
        fragment = null;
        //setTitle(title);

        return true;
    }

    private void loadFragment(Fragment fragment, String title) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.drawer_layout, fragment, title);
            transaction.addToBackStack(title);

            transaction.commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            drawer = null;
            transaction = null;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), getString(R.string.selecioneData));

    }

    public void takePhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        ImageButton bt = (ImageButton) findViewById(R.id.btAvatar);
                        bt.setImageBitmap(yourSelectedImage);
                        if (yourSelectedImage != null)//Add the image to the cache
                            ImageCache.addBitmapToMemoryCache("avatar",yourSelectedImage);
                        //Upload da imagem inicializado
                        new AssyncUploadURLlink(this, yourSelectedImage, 0).execute();
                    } catch (FileNotFoundException ex) {
                        logException(ex);
                    }

                    break;
                case TWITTER_CODE:
                    TwitterUtil.onActResult(requestCode, resultCode, imageReturnedIntent);
                    break;
                case REQUEST_CODE_MIC:
                    ArrayList<String> textMatchList = imageReturnedIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (!textMatchList.isEmpty()) {
                        String Query = textMatchList.get(0);
                        WORD.setText(Query);
                    }
                    break;
            }
        }

    }


    @Override
    public void onClick(View v) {
        String title = null;
        switch (v.getId()) {
            case R.id.btPerfilSlash:
                loadFragment(new ActivityProfile(), getString(R.string.perfil));
                break;

            case R.id.btNovoSplah:
                //whatever
                if (LocationManagerUtil.isLocationValid(MAIN)==null) {
                    loadFragment(new ActivityNoGps(), getString(R.string.invalid_locate));
                } else {
                    loadFragment(new ActivityOcorrencia(), getString(R.string.register_event));
                }
                break;

            case R.id.btMapaSplash:
                if (LocationManagerUtil.isLocationValid(MAIN)==null) {
                    loadFragment(new ActivityNoGps(), getString(R.string.invalid_locate));
                } else {
                    if (MY_PREFERENCES.contains("ehMeu")) {
                        loadFragment(new ActivityMap(), getString(R.string.view_events));
                    } else {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle(getString(R.string.preferences));
                        alertDialogBuilder
                                .setMessage(getString(R.string.view_config_map))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.SIM),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        loadFragment(new ActivityConfig(), getString(R.string.configura_es));
                    }

                }
                break;
            case R.id.btListOcorrencias:
                AssyncLoadListOcorrencias assyncLoadListOcorrencias = new AssyncLoadListOcorrencias();
                assyncLoadListOcorrencias.setCtx(getApplicationContext());
                assyncLoadListOcorrencias.execute();//Carrega as ultimas ocorrencias atualizadas.
                if (isDataListLoaded()) {
                    loadFragment(new ListOcorrencia(), getString(R.string.list_ocorrencias));
                } else {
                    loadFragment(new ActivityAds(), getString(R.string.help_us));
                }
                break;
            case R.id.btConfigSplash:
                //whatever
                loadFragment(new ActivityConfig(), getString(R.string.configura_es));
                break;
            case R.id.fab:
                //@todo
                // TODO: Use a more specific parent
                // TODO: Base this Tweet ID on some data from elsewhere in your app
                //.TwitterUtil.getLatestTweet();
                // fa
                break;
        }
    }

    private void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
            provider = null;
        } catch (Exception ex) {
            logException(ex);
        }
    }

    private boolean isDataListLoaded() {
        if (LIST_OCORRENCIAS == null) {
            return false;
        } else {
            return true;
        }
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.soletre_ocorrencia));
        startActivityForResult(intent, REQUEST_CODE_MIC);
    }


    public static final void logException(Exception ex) {
        try {
            LOGGER.log(Level.SEVERE, ex.toString());
            // ex.printStackTrace();
            FirebaseCrash.report(ex);
            FirebaseCrash.log(ex.toString());
        } catch (Exception e) {
            FirebaseCrash.log(ex.toString());
        }

    }

}
