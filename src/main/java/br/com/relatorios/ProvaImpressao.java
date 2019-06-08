package br.com.relatorios;

import br.com.entity.QuestoesEntity;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProvaImpressao {
    public void gerarImpressao(List<QuestoesEntity> list) throws JRException {
        InputStream provaJasper = this.getClass().getClassLoader().getResourceAsStream("config/prova.jasper");

        JasperReport provatReporter = (JasperReport) JRLoader.loadObject(provaJasper);

        List<String> characters = new ArrayList<>(26);
        for (char c = 'a'; c <= 'z' ; c++) {
            characters.add(c+")");
        }

        Map parametros = new HashMap();
        parametros.put("SUBREPORT_DIR", "config/");
        parametros.put("charactersList", characters);

        JasperPrint impressao = JasperFillManager.fillReport(provatReporter, parametros,
                new JRBeanCollectionDataSource(list, false));

        JasperViewer viewer = new JasperViewer(impressao, true);
        viewer.setVisible(true);

    }

}
