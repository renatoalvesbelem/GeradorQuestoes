import br.com.dao.GenericDao;
import br.com.dao.QuestaoDao;
import br.com.entity.DisciplinaEntity;
import org.hibernate.cfg.QuerySecondPass;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class Teste {
    public static void main(String[] args) {

        List<DisciplinaEntity> lista = GenericDao.getInstance().findAll(DisciplinaEntity.class);
        DisciplinaEntity disciplinaEntity =  lista.get(0);
        disciplinaEntity.setNomeDisciplina("Novo");

        GenericDao.getInstance().merge(disciplinaEntity);
    }
}
