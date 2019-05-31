package br.com.view;

import br.com.dao.GenericDao;
import br.com.entity.DisciplinaEntity;
import br.com.entity.SerieEntity;
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

public class CadastroSerie extends JFXPanel {
    private TextField campoNomeSerie;
    private TableView<SerieEntity> tabela;
    private SerieEntity serie;

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
                editarSerie();
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


        campoNomeSerie = new TextField();
        campoNomeSerie.setMinSize(320, 20);
        campoNomeSerie.setLayoutX(70);
        campoNomeSerie.setLayoutY(8);
        Label labelNomeDisciplina = new Label("Série");
        labelNomeDisciplina.setLabelFor(campoNomeSerie);
        labelNomeDisciplina.setLayoutX(5);
        labelNomeDisciplina.setLayoutY(10);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                salvarSerie();
            }
        });
        Scene scene = new Scene(root, 600, 400);


        root.getChildren().addAll(button, labelNomeDisciplina, campoNomeSerie, tabela(), buttonEditar, buttonDeletar);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void deletarMateria() {
        serie = tabela.getSelectionModel().getSelectedItem();
        Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoInfo.setHeaderText("Deseja deletar " + serie.getNomeSerie() + " ?");
        Optional<ButtonType> result = dialogoInfo.showAndWait();
        if (result.get() == ButtonType.OK) {
            GenericDao.getInstance().remove(serie);
            atualizaTabela();
        }
    }

    private void editarSerie() {
        serie = tabela.getSelectionModel().getSelectedItem();
        campoNomeSerie.setText(serie.getNomeSerie());
    }

    private void salvarSerie() {
        SerieEntity serieEntity;
        if (serie == null) {
            serieEntity = new SerieEntity();
        } else {
            serieEntity = serie;
        }

        serieEntity.setNomeSerie(campoNomeSerie.getText());
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);

        if (GenericDao.getInstance().merge(serieEntity)) {
            dialogoInfo.setHeaderText("Salvo com sucesso!");
            campoNomeSerie.setText("");
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
        List<SerieEntity> serieList = GenericDao.getInstance().findAll(SerieEntity.class);
        TableColumn<SerieEntity, String> colunaSerie = new TableColumn<>("Série");

        colunaSerie.setCellValueFactory(new PropertyValueFactory<>("nomeSerie"));
        tabela.setItems(FXCollections.observableArrayList(serieList));

        ScrollPane scrollpane = new ScrollPane();
        scrollpane.setFitToWidth(true);
        scrollpane.setLayoutY(50);
        scrollpane.setLayoutX(70);
        scrollpane.setMinWidth(320);
        scrollpane.setMaxHeight(302);
        scrollpane.setContent(tabela);
        tabela.getColumns().addAll(colunaSerie);
        tabela.getSortOrder().add(colunaSerie);
        return scrollpane;
    }

    private void atualizaTabela() {
        tabela.getItems().removeAll(tabela.getItems());
        List<SerieEntity> serieList = GenericDao.getInstance().findAll(SerieEntity.class);
        tabela.setItems(FXCollections.observableArrayList(serieList));
        tabela.getSortOrder().add(tabela.getColumns().get(0));
    }

}
