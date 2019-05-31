package br.com.dao;

import br.com.entity.QuestoesEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class QuestaoDao {

    private static QuestaoDao instance;
    protected EntityManager entityManager;

    public static QuestaoDao getInstance(){
        if (instance == null){
            instance = new QuestaoDao();
        }

        return instance;
    }

    private QuestaoDao() {
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }

        return entityManager;
    }

    public QuestoesEntity getById(final int id) {
        return entityManager.find(QuestoesEntity.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<QuestoesEntity> findAll() {
        return entityManager.createQuery("SELECT entidade from " + QuestoesEntity.class.getName() + " entidade").getResultList();
    }

    public void persist(QuestoesEntity questao) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(questao);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public void merge(QuestoesEntity questao) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(questao);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public void remove(QuestoesEntity questao) {
        try {
            entityManager.getTransaction().begin();
            questao = entityManager.find(QuestoesEntity.class, questao.getId());
            entityManager.remove(questao);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public void removeById(final int id) {
        try {
            QuestoesEntity questao = getById(id);
            remove(questao);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}