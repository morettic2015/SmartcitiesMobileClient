package view.infoseg.morettic.com.br.infosegapp.util;

import view.infoseg.morettic.com.br.infosegapp.R;

/**
 * Created by LuisAugusto on 24/10/2016.
 */
public enum TipoOcorrencia {
    MEIO_AMBIENTE(R.mipmap.icon_nature01),
    SAUDE(R.mipmap.icon_health01),
    EDUCACAO(R.mipmap.icon_education01),
    SEGURANCA(R.mipmap.icon_security01),
    POLITICA(R.mipmap.icon_politics01),
    TRANSPORTE(R.mipmap.icon_transport01),
    ESPORTE(R.mipmap.icon_sport01),
    CULTURA(R.mipmap.ic_cultura),
    INFRAESTRUTURA(R.mipmap.ic_infraestrutura),
    IMOVEIS(R.mipmap.icon_imoveis),
    IMOVEIS_GIMO(R.mipmap.icon_imoveis),
    UPA(R.mipmap.icon_health01),
    ALIMENTACAO(R.mipmap.ic_alimentacao),
    TURISMO(R.mipmap.ic_turismo),
    SHOP( R.mipmap.ic_shop),
    OPENSTREEMAP(R.mipmap.icon),
    SERVICOS(R.mipmap.icon),
    BEER(R.mipmap.ic_ipa),
    SEARCH(R.mipmap.icon);
    private int icon;

    public int getIcon(){
        return icon;
    }

    TipoOcorrencia(int ic) {
        icon = ic;
    }
}
