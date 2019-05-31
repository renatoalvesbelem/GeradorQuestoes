package br.com.view;

import br.com.dao.GenericDao;
import br.com.entity.DisciplinaEntity;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class CadastroMateria extends JFXPanel {
    private TextField campoNomeDisciplina;
    private TableView<DisciplinaEntity> tabela;
    private DisciplinaEntity disciplina;

    public void start(Stage primaryStage) {
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        Pane root = new Pane();
        Button button = new Button("Salvar");
        button.setLayoutX(400);
        button.setLayoutY(8);
        Button buttonEditar = new Button("Editar");
        buttonEditar.setLayoutX(400);
        buttonEditar.setLayoutY(50);
        buttonEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                editarMateria();
            }
        });

        Button buttonDeletar = new Button("Deletar");
        buttonDeletar.setLayoutX(465);
        buttonDeletar.setLayoutY(50);
        buttonDeletar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                deletarMateria();
            }
        });


        campoNomeDisciplina = new TextField();
        campoNomeDisciplina.setMinSize(320, 20);
        campoNomeDisciplina.setLayoutX(70);
        campoNomeDisciplina.setLayoutY(8);
        Label labelNomeDisciplina = new Label("Disciplina");
        labelNomeDisciplina.setLabelFor(campoNomeDisciplina);
        labelNomeDisciplina.setLayoutX(5);
        labelNomeDisciplina.setLayoutY(10);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                salvarMateria();
            }
        });
        Scene scene = new Scene(root, 600, 400);


        root.getChildren().addAll(button, labelNomeDisciplina, campoNomeDisciplina, tabela(), buttonEditar, buttonDeletar);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void deletarMateria() {
        disciplina = tabela.getSelectionModel().getSelectedItem();
        Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoInfo.setHeaderText("Deseja deletar " + disciplina.getNomeDisciplina() + " ?");
        Optional<ButtonType> result = dialogoInfo.showAndWait();
        if (result.get() == ButtonType.OK) {
            GenericDao.getInstance().remove(disciplina);
            atualizaTabela();
        }
    }

    private void editarMateria() {
        disciplina = tabela.getSelectionModel().getSelectedItem();
        campoNomeDisciplina.setText(disciplina.getNomeDisciplina());
    }

    private void salvarMateria() {
        DisciplinaEntity disciplinaEntity;
        if (disciplina == null) {
            disciplinaEntity = new DisciplinaEntity();
        } else {
            disciplinaEntity = disciplina;
        }

        disciplinaEntity.setNomeDisciplina(campoNomeDisciplina.getText());
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);

        if (GenericDao.getInstance().merge(disciplinaEntity)) {
            dialogoInfo.setHeaderText("Salvo com sucesso!");
            campoNomeDisciplina.setText("");
            atualizaTabela();
        } else {
            dialogoInfo.setAlertType(Alert.AlertType.ERROR);
            dialogoInfo.setHeaderText("Houve um erro ao salvar!");
        }
        dialogoInfo.showAndWait();
    }

    public ScrollPane tabela() {
        tabela = new TableView<>();
        tabela.setLayoutY(50);
        tabela.setLayoutX(70);
        tabela.setMinWidth(300);
        tabela.setMaxHeight(300);
        tabela.setTableMenuButtonVisible(true);

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        List<DisciplinaEntity> disciplinaList = GenericDao.getInstance().findAll(DisciplinaEntity.class);
        TableColumn<DisciplinaEntity, String> colunaDisciplina = new TableColumn<>("Disciplina");

        colunaDisciplina.setCellValueFactory(new PropertyValueFactory<>("nomeDisciplina"));
        tabela.setItems(FXCollections.observableArrayList(disciplinaList));

        ScrollPane scrollpane = new ScrollPane();
        scrollpane.setFitToWidth(true);
        scrollpane.setLayoutY(50);
        scrollpane.setLayoutX(70);
        scrollpane.setMinWidth(320);
        scrollpane.setMaxHeight(302);
        scrollpane.setContent(tabela);
        tabela.getColumns().addAll(colunaDisciplina);
        tabela.getSortOrder().add(colunaDisciplina);
        return scrollpane;
    }

    private void atualizaTabela() {
        tabela.getItems().removeAll(tabela.getItems());
        List<DisciplinaEntity> disciplinaList = GenericDao.getInstance().findAll(DisciplinaEntity.class);
        tabela.setItems(FXCollections.observableArrayList(disciplinaList));
        tabela.getSortOrder().add(tabela.getColumns().get(0));
    }

}
