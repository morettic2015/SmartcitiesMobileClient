package view.infoseg.morettic.com.br.infosegapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LIST_OCORRENCIAS;

/**
 * Created by LuisAugusto on 31/03/2016.
 */
public class ListOcorrencia extends Fragment {
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_ocorrencia_list, container,
                false);

        final String[] itemname = LIST_OCORRENCIAS;

        OcorrenciaListAdapter adapter=new OcorrenciaListAdapter(getActivity(),itemname);
        ListView list=(ListView)v.findViewById(R.id.mainListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];

            }
        });

        return v;
    }
}
