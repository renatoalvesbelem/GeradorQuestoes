package br.com.dao;

import br.com.entity.DisciplinaEntity;
import br.com.entity.QuestoesEntity;
import br.com.entity.SerieEntity;
import com.sun.deploy.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class GenericDao<T> {

    private static GenericDao instance;
    protected EntityManager entityManager;

    public static GenericDao getInstance() {
        if (instance == null) {
            instance = new GenericDao();
        }

        return instance;
    }

    private GenericDao() {
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }

        return entityManager;
    }

    public T getById(final Class<T> type, final int id) {
        return entityManager.find(type, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(final Class<T> type) {
        return entityManager.createQuery("SELECT entidade from " + type.getName() + " entidade").getResultList();
    }

    public List<T> findWithSql(final Class<T> type, String enunciado, SerieEntity serieEntity, DisciplinaEntity disciplinaEntity) {
        StringBuilder sqlBase = new StringBuilder("SELECT entidade from " + type.getName() + " entidade ");
        List<String> whereCause = new ArrayList<String>();
        if (disciplinaEntity != null){
            whereCause.add("id_disciplina = "+disciplinaEntity.getId());
        }

        if (serieEntity != null){
            whereCause.add("id_serie = "+serieEntity.getId());
        }

        if (!enunciado.isEmpty()){
            whereCause.add("lower(entidade.enunciado) like '%"+enunciado.toLowerCase()+"%'");
        }

        if(disciplinaEntity != null || serieEntity != null ||!enunciado.isEmpty()){
            sqlBase.append(" where " + StringUtils.join(whereCause, " and "));

        }
        return entityManager.createQuery(sqlBase.toString()).getResultList();

    }

    public void persist(T questao) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(questao);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public boolean merge(T questao) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(questao);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public boolean remove(T questao) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(questao);
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public void removeById(final Class<T> type, final int id) {
        try {
            T questao = getById(type, id);
            remove(questao);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}