package br.com.view;

import br.com.dao.GenericDao;
import br.com.entity.DisciplinaEntity;
import br.com.entity.OpcoesEntity;
import br.com.entity.QuestoesEntity;
import br.com.entity.SerieEntity;
import com.sun.deploy.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class CadastrarQuestao extends JFXPanel {
    private HTMLEditor campoEnunciado, campoResposta;
    private TableView<QuestoesEntity> tabela;
    private QuestoesEntity questoes;
    private ComboBox comboSerie, comboDisciplina;
    private TabPane painelOpcoes = painelOpcoes();
    private char numeracaoAlfabetica = 'a';

    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Button button = new Button("Salvar");
        button.setLayoutX(410);
        button.setLayoutY(8);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(validarCampos()){
                    if(salvarQuestao()){
                        limparCampos();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("A questão foi cadastrada com sucesso!");
                        alert.showAndWait();
                    }
                }
            }
        });

        Button buttonAdicionar = new Button("Adicionar");
        buttonAdicionar.setLayoutX(630);
        buttonAdicionar.setLayoutY(350);
        buttonAdicionar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                adicionarOpcao();
            }
        });


        campoEnunciado = new HTMLEditor();
        campoEnunciado.setMaxSize(550, 200);
        campoEnunciado.setLayoutX(0);
        campoEnunciado.setLayoutY(120);
        Label labelNomeDisciplina = new Label("Enunciado");
        labelNomeDisciplina.setLabelFor(campoEnunciado);
        labelNomeDisciplina.setLayoutX(5);
        labelNomeDisciplina.setLayoutY(100);


        campoResposta = new HTMLEditor();
        campoResposta.setMaxSize(550, 200);
        campoResposta.setLayoutX(560);
        campoResposta.setLayoutY(120);
        Label labelResposta = new Label("Resposta");
        labelResposta.setLabelFor(campoResposta);
        labelResposta.setLayoutX(560);
        labelResposta.setLayoutY(100);


        Scene scene = new Scene(root, 1200, 700);
        comboDisciplina = comboBoxDisciplina();
        comboSerie = comboBoxSerie();
        root.getChildren().addAll(
                button,
                labelNomeDisciplina,
                campoEnunciado,
                labelResposta,
                campoResposta,
                comboDisciplina,
                comboSerie,
                painelOpcoes,
                buttonAdicionar
        );
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }


    private boolean salvarQuestao() {
        QuestoesEntity questao = new QuestoesEntity();
        questao.setEnunciado(campoEnunciado.getHtmlText());
        questao.setResposta(campoResposta.getHtmlText());
        questao.setDisciplinaEntity((DisciplinaEntity) comboDisciplina.getValue());
        questao.setSerieEntity((SerieEntity) comboSerie.getValue());
        List<OpcoesEntity> opcoes = new ArrayList();

        for (Tab tab : painelOpcoes.getTabs()) {
            OpcoesEntity opcao = new OpcoesEntity();
            opcao.setTextoResposta(((TextArea) ((Pane) tab.getContent()).getChildren().get(0)).getText());
            opcao.setFlResposta("" + ((CheckBox) ((Pane) tab.getContent()).getChildren().get(1)).isSelected());
            opcoes.add(opcao);
        }
        questao.setOpcoesList(opcoes);

       return GenericDao.getInstance().merge(questao);

    }

    private ComboBox<SerieEntity> comboBoxSerie() {
        ObservableList<SerieEntity> options =
                FXCollections.observableArrayList(
                        GenericDao.getInstance().findAll(SerieEntity.class));
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setLayoutX(80);
        comboBox.setLayoutY(10);
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
        DisciplinaEntity disciplinaEntity = new DisciplinaEntity();
        options.add(disciplinaEntity);
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
        comboBox.setLayoutY(40);
        return comboBox;
    }


    public TabPane painelOpcoes() {
        TabPane painelAbas = new TabPane();
        painelAbas.setLayoutX(20);
        painelAbas.setLayoutY(350);
        painelAbas.setPrefSize(600, 300);
        painelAbas.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
        return painelAbas;
    }

    private void adicionarOpcao() {
        Tab aba = new Tab("   " + numeracaoAlfabetica++ + "   ");
        Pane pane = new Pane();
        pane.setMaxSize(600, 300);
        TextArea textField = new TextArea();
        textField.setWrapText(true);

        textField.setLayoutY(10);
        textField.setLayoutX(5);
        textField.setMaxSize(500, 100);
        CheckBox checkBox = new CheckBox();
        checkBox.setLayoutX(510);
        checkBox.setLayoutY(10);
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (validarMaisDeUmCheckBoxSelecionado()) {
                    checkBox.setSelected(false);
                }
            }
        });
        pane.getChildren().addAll(textField, checkBox);
        aba.setContent(pane);
        painelOpcoes.getTabs().add(aba);
    }

    private boolean validarMaisDeUmCheckBoxSelecionado() {
        int cont = 0;
        for (Tab tab : painelOpcoes.getTabs()) {

            if (((CheckBox) ((Pane) tab.getContent()).getChildren().get(1)).isSelected()) {
                cont++;
            }

            if (cont > 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Somente uma questão pode ser marcada\ncomo correta.");
                alert.showAndWait();
                return true;
            }

        }
        return false;
    }

    private boolean validarCampos() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        String mensagemFinal = "";
        List<String> mensagemList = new ArrayList<String>();
        if (!checkBoxEstaSelecionado()) {
            mensagemList.add("Uma opção deve estar selecionada como correta!");
        }
        if (comboSerie.getValue() == null) {
            mensagemList.add("A Série deve ser selecionada!");
        }
        if (comboDisciplina.getValue() == null) {
            mensagemList.add("A Disciplina deve ser selecionada!");
        }
        if (Jsoup.parse(campoEnunciado.getHtmlText()).text().isEmpty()) {
            mensagemList.add("O enunciado da questão deve ser preenchido!");
        }
        mensagemFinal = StringUtils.join(mensagemList, "\n");
        if (mensagemFinal.isEmpty()) {
            return true;
        }
        alert.setContentText(mensagemFinal);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
        return false;
    }

    public boolean checkBoxEstaSelecionado() {
        for (Tab tab : painelOpcoes.getTabs()) {
            if (((CheckBox) ((Pane) tab.getContent()).getChildren().get(1)).isSelected()) {
                return true;
            }
        }
        return false;
    }

    public void limparCampos(){
    campoEnunciado.setHtmlText("");
    campoResposta.setHtmlText("");
    painelOpcoes.getTabs().removeAll(painelOpcoes.getTabs());
    }
}
