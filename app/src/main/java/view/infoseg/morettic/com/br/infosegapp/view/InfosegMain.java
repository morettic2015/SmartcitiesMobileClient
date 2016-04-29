package view.infoseg.morettic.com.br.infosegapp.view;

import android.Manifest;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import java.io.FileNotFoundException;
import java.io.InputStream;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncLoadListOcorrencias;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncLoginRegister;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink;
import view.infoseg.morettic.com.br.infosegapp.util.ActivityUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static android.Manifest.permission.*;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.BITMAP_DEFAULT;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
//import static view.infoseg.morettic.com.br.infosegapp.view.LoginFragment.myInstance;

//import android.app.Fragment;
//import android.app.FragmentTransaction;

public class InfosegMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    //private static final int TAKE_PICTURE = 1;
    private static final int SELECT_PHOTO = 100;
    private Toolbar toolbar;
    //private Uri imageUri;
    private static int MY_REQUEST_CODE, MY_REQUEST_CODE1, MY_REQUEST_CODE2, MY_REQUEST_CODE3, MY_REQUEST_CODE4;

    static void setTitleToolbar(String title, View v) {
        Toolbar tb = (Toolbar) v.findViewById(R.id.toolbar);
        tb.setTitle(title);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoseg_main);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle("Smartcities framework");
        //Inicializa as preferencias do usuario
        ValueObject.MY_PREFERENCES = getApplicationContext().getSharedPreferences("INFOSEGMAIN", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener((View.OnClickListener) this);

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
         * Abre a janela para ativar o GPS do celular
         * */
        turnGPSOn();
        /**
         * SE nao estiver autenticado pop up maldito dos infernos
         * */
        if (!ValueObject.AUTENTICADO && MY_PREFERENCES.getString("id", "").equals("")) {
            if (ValueObject.LOGIN == null) {
                ValueObject.LOGIN = LoginFragment.newInstance();
                ValueObject.LOGIN.show(getFragmentManager(), "dialog");
            }
        } else {
            //Load Data
            BITMAP_DEFAULT = BitmapFactory.decodeResource(getResources(), R.drawable.ic_smartcities_icon_logo);
            AssyncLoginRegister assyncLoginRegister = new AssyncLoginRegister(this.getApplicationContext(), MY_PREFERENCES.getString("email", ""), MY_PREFERENCES.getString("passwd", ""));
            assyncLoginRegister.execute();
            //Load Ocorrencias
        }
        AssyncLoadListOcorrencias assyncLoadListOcorrencias = new AssyncLoadListOcorrencias();
        assyncLoadListOcorrencias.setCtx(getApplicationContext());
        assyncLoadListOcorrencias.execute();

        ValueObject.MAIN = this;
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
        //Checka permissão do GPS
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] p = {ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, p, MY_REQUEST_CODE2);
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
            if (ValueObject.MY_PREFERENCES.contains("ehMeu")) {
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
            fragment = new ListOcorrencia();

            title = getString(R.string.list_ocorrencias);
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
                    .setPositiveButton("SIM",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Limpa as preferencias
                                    getSharedPreferences("INFOSEGMAIN", 0).edit().clear().commit();
                                    //Move para background e destroi o app
                                    moveTaskToBack(true);
                                    Process.killProcess(Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
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
            transaction.replace(R.id.drawer_layout, fragment);
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

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        ImageButton bt = (ImageButton) findViewById(R.id.btAvatar);
                        bt.setImageBitmap(yourSelectedImage);

                        //Upload da imagem inicializado
                        new AssyncUploadURLlink(this, yourSelectedImage, 0).execute();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
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
                if (ActivityUtil.isLocationValid(ValueObject.MAIN) == null) {
                    loadFragment(new ActivityNoGps(), getString(R.string.invalid_locate));
                } else {
                    loadFragment(new ActivityOcorrencia(), getString(R.string.register_event));
                }
                break;

            case R.id.btMapaSplash:
                if (ActivityUtil.isLocationValid(ValueObject.MAIN) == null) {
                    loadFragment(new ActivityNoGps(), getString(R.string.invalid_locate));
                } else {
                    if (ValueObject.MY_PREFERENCES.contains("ehMeu")) {
                        loadFragment(new ActivityMap(), getString(R.string.view_events));
                    } else {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle(getString(R.string.preferences));
                        alertDialogBuilder
                                .setMessage(getString(R.string.view_config_map))
                                .setCancelable(false)
                                .setPositiveButton("SIM",
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
                loadFragment(new ListOcorrencia(), getString(R.string.list_ocorrencias));
                break;
            case R.id.btConfigSplash:
                //whatever
                loadFragment(new ActivityConfig(), getString(R.string.configura_es));
                break;
            case R.id.fab:
                //@todo
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
