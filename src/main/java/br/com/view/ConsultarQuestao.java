package br.com.view;

import br.com.dao.GenericDao;
import br.com.dao.QuestaoDao;
import br.com.entity.DisciplinaEntity;
import br.com.entity.QuestoesEntity;
import br.com.entity.SerieEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class ConsultarQuestao extends JFXPanel {
    private TextField campoEnunciado;
    private TableView<QuestoesEntity> tabela;
    private QuestoesEntity questoes;
    private ComboBox comboSerie, comboDisciplina;

    public void start(Stage primaryStage) {
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        Pane root = new Pane();
        Button button = new Button("Pesquisar");
        button.setLayoutX(410);
        button.setLayoutY(8);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                pesquisarQuestao();
            }
        });

        Button buttonNovo = new Button("Novo");
        buttonNovo.setLayoutX(500);
        buttonNovo.setLayoutY(8);
        buttonNovo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                new CadastrarQuestao().start(new Stage());
            }
        });

        Button buttonEditar = new Button("Editar");
        buttonEditar.setLayoutX(410);
        buttonEditar.setLayoutY(140);
        buttonEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                editarQuestao();
            }
        });

        Button buttonDeletar = new Button("Deletar");
        buttonDeletar.setLayoutX(475);
        buttonDeletar.setLayoutY(140);
        buttonDeletar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                deletarQuestao();
            }
        });

        campoEnunciado = new TextField();
        campoEnunciado.setMinSize(320, 20);
        campoEnunciado.setLayoutX(80);
        campoEnunciado.setLayoutY(8);
        Label labelNomeDisciplina = new Label("Enunciado");
        labelNomeDisciplina.setLabelFor(campoEnunciado);
        labelNomeDisciplina.setLayoutX(5);
        labelNomeDisciplina.setLayoutY(10);


        Scene scene = new Scene(root, 640, 480);

        comboDisciplina = comboBoxDisciplina();
        comboSerie = comboBoxSerie();
        root.getChildren().addAll(
                button,
                labelNomeDisciplina,
                campoEnunciado,
                tabela(),
                buttonEditar,
                buttonDeletar,
                buttonNovo,
                comboDisciplina,
                comboSerie
                );
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void deletarQuestao() {
        questoes = tabela.getSelectionModel().getSelectedItem();
        Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoInfo.setHeaderText("Deseja deletar " + questoes.getEnunciadoFormatado() + " ?");
        Optional<ButtonType> result = dialogoInfo.showAndWait();
        if (result.get() == ButtonType.OK) {
            GenericDao.getInstance().remove(questoes,"Questão");
            atualizaTabela();
        }
    }

    private void editarQuestao() {
        new CadastrarQuestao(tabela.getSelectionModel().getSelectedItem()).start(new Stage());
    }

    private void pesquisarQuestao() {
        atualizaTabela();

    }

    public ScrollPane tabela() {
        tabela = new TableView<>();
        tabela.setLayoutY(50);
        tabela.setLayoutX(140);
        tabela.setMinWidth(300);
        tabela.setMaxHeight(300);
        tabela.setTableMenuButtonVisible(true);

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //  List<QuestoesEntity> disciplinaList = GenericDao.getInstance().findAll(QuestoesEntity.class);
        TableColumn<QuestoesEntity, String> enunciado = new TableColumn<>("Enunciado");

        enunciado.setCellValueFactory(new PropertyValueFactory<>("enunciadoFormatado"));
        //   tabela.setItems(FXCollections.observableArrayList(disciplinaList));

        ScrollPane scrollpane = new ScrollPane();
        scrollpane.setFitToWidth(true);
        scrollpane.setLayoutY(140);
        scrollpane.setLayoutX(80);
        scrollpane.setMinWidth(320);
        scrollpane.setMaxHeight(302);
        scrollpane.setContent(tabela);
        tabela.getColumns().addAll(enunciado);
        tabela.getSortOrder().add(enunciado);
        return scrollpane;
    }

    private void atualizaTabela() {
        tabela.getItems().removeAll(tabela.getItems());
          List<QuestoesEntity> disciplinaList =  GenericDao.getInstance().findWithSql(QuestoesEntity.class,campoEnunciado.getText(),(SerieEntity) comboSerie.getValue(),(DisciplinaEntity) comboDisciplina.getValue());
                tabela.setItems(FXCollections.observableArrayList(disciplinaList));
        tabela.getSortOrder().add(tabela.getColumns().get(0));
    }

    private ComboBox<SerieEntity> comboBoxSerie() {
        ObservableList<SerieEntity> options =
                FXCollections.observableArrayList(
                        GenericDao.getInstance().findAll(SerieEntity.class));
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setLayoutX(80);
        comboBox.setLayoutY(39);
        Callback<ListView<SerieEntity>, ListCell<SerieEntity>> factory = lv -> new ListCell<SerieEntity>() {

            @Override
            protected void updateItem(SerieEntity item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNomeSerie());
            }

        };

        comboBox.setCellFactory(factory);
        comboBox.setButtonCell(factory.call(null));
        return comboBox;
    }


    private ComboBox<SerieEntity> comboBoxDisciplina() {
        ObservableList<DisciplinaEntity> options =
                FXCollections.observableArrayList(
                        GenericDao.getInstance().findAll(DisciplinaEntity.class));
        final ComboBox comboBox = new ComboBox(options);

        Callback<ListView<DisciplinaEntity>, ListCell<DisciplinaEntity>> factory = lv -> new ListCell<DisciplinaEntity>() {

            @Override
            protected void updateItem(DisciplinaEntity item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNomeDisciplina());
            }

        };

        comboBox.setCellFactory(factory);
        comboBox.setButtonCell(factory.call(null));
        comboBox.setLayoutX(80);
        comboBox.setLayoutY(70);
        return comboBox;
    }

}
